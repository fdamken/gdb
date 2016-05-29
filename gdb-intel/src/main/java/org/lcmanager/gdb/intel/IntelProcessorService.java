/*
 * #%L
 * Game Database
 * %%
 * Copyright (C) 2016 - 2016 LCManager Group
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.lcmanager.gdb.intel;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lcmanager.gdb.base.CollectionUtil;
import org.lcmanager.gdb.base.NumberUtil;
import org.lcmanager.gdb.base.health.NoHealthTrace;
import org.lcmanager.gdb.intel.exception.IntelProcessorServiceException;
import org.lcmanager.gdb.service.annotation.Branded;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Processor;
import org.lcmanager.gdb.service.processor.ProcessorService;
import org.lcmanager.gdb.service.processor.exception.ProcessorServiceException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.mikael.urlbuilder.UrlBuilder;

/**
 * The Intel implementation of {@link ProcessorService}.
 *
 */
@Service
@Branded
@CacheConfig(cacheNames = "intel-processor-service")
public class IntelProcessorService implements ProcessorService {
    /**
     * The pattern no key must match (filters out the graphics-related
     * specifications).
     * 
     */
    private static final Pattern KEY_PATTERN_NEGATIVE = Pattern.compile("^.*graphic.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The pattern of the core count key.
     * 
     */
    private static final Pattern CORES_KEY_PATTERN = Pattern.compile("^.*core.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The pattern of the base frequency key.
     * 
     */
    private static final Pattern FREQUENCY_KEY_PATTERN = Pattern.compile("^.*?base\\s*frequency.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The pattern of the instruction set key.
     * 
     */
    private static final Pattern INSTRUCTION_SET_KEY_PATTERN = Pattern.compile("^.*instruction set.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The pattern the instruction set key must not match.
     * 
     */
    private static final Pattern INSTRUCTION_SET_KEY_PATTERN_NEGATIVE = Pattern.compile("^.*extension.*$",
            Pattern.CASE_INSENSITIVE);
    /**
     * The pattern of the thread count key.
     * 
     */
    private static final Pattern THREADS_KEY_PATTERN = Pattern.compile("^.*thread.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The pattern the thread count key must not match.
     * 
     */
    private static final Pattern THREADS_KEY_PATTERN_NEGATIVE = Pattern.compile("^.*hyper.*$", Pattern.CASE_INSENSITIVE);

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.processor.ProcessorService#retrieveProcessor(org.lcmanager.gdb.service.data.model.Brand,
     *      java.lang.String)
     */
    @Override
    @Cacheable
    public Processor retrieveProcessor(final Brand brand, final String rawModel) throws ProcessorServiceException {
        if (!this.isResponsible(brand)) {
            throw new IllegalArgumentException("The Intel service is not responsible for " + brand.getName() + " processors!");
        }

        final String model = rawModel.toLowerCase().trim();

        final Document detailsDocument = this.retrieveDetailsDocument(model);
        final Map<String, String> specifications = this.extractSpecifications(detailsDocument);

        if (specifications == null || specifications.isEmpty()) {
            return null;
        }

        final Processor processor = new Processor();
        processor.setBrand(brand);
        processor.setModel(model);

        processor.setCores(CollectionUtil.findValue(specifications, key -> {
            return IntelProcessorService.CORES_KEY_PATTERN.matcher(key).matches()
                    && !IntelProcessorService.KEY_PATTERN_NEGATIVE.matcher(key).matches();
        }, NumberUtil::extractNumber));

        processor.setFrequency(CollectionUtil.findValue(specifications, key -> {
            return IntelProcessorService.FREQUENCY_KEY_PATTERN.matcher(key).matches()
                    && !IntelProcessorService.KEY_PATTERN_NEGATIVE.matcher(key).matches();
        }, value -> {
            final String lowerValue = value.toLowerCase().trim();
            final double number = NumberUtil.extractNumberFloat(lowerValue);
            final double result;
            if (lowerValue.endsWith("ghz")) {
                result = number * 1000;
            } else if (lowerValue.endsWith("khz")) {
                result = number / 1000;
            } else {
                result = number;
            }
            return (int) Math.round(result);
        }));

        processor.setInstructionSet(CollectionUtil.findValue(specifications, key -> {
            return IntelProcessorService.INSTRUCTION_SET_KEY_PATTERN.matcher(key).matches()
                    && !IntelProcessorService.INSTRUCTION_SET_KEY_PATTERN_NEGATIVE.matcher(key).matches()
                    && !IntelProcessorService.KEY_PATTERN_NEGATIVE.matcher(key).matches();
        }, NumberUtil::extractNumber));

        processor.setThreads(CollectionUtil.findValue(specifications, key -> {
            return IntelProcessorService.THREADS_KEY_PATTERN.matcher(key).matches()
                    && !IntelProcessorService.THREADS_KEY_PATTERN_NEGATIVE.matcher(key).matches()
                    && !IntelProcessorService.KEY_PATTERN_NEGATIVE.matcher(key).matches();
        }, NumberUtil::extractNumber));

        return processor;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.processor.ProcessorService#isResponsible(org.lcmanager.gdb.service.data.model.Brand)
     */
    @Override
    @NoHealthTrace
    public boolean isResponsible(final Brand brand) {
        return Brand.WellKnownBrand.INTEL.getBrand().equals(brand);
    }

    /**
     * Retrieves the {@link Document} that contains the specifications for the
     * given processor model.
     *
     * @param model
     *            The model of the processor to retrieve the details from.
     * @return The details-document.
     * @throws ProcessorServiceException
     *             If any error occurs whilst fetching the data.
     */
    private Document retrieveDetailsDocument(final String model) throws ProcessorServiceException {
        final URL searchUrl = this.buildSearchUrl(model);
        final Document document = this.retrieveDocument(searchUrl);

        final Elements searchResultsElements = document.getElementsByClass("result-list-table");

        if (searchResultsElements.size() <= 0) {
            // This happens only if the model was unique and the Intel page
            // redirects to the processor details directly.

            return document;
        }

        final Element searchResultsElement = searchResultsElements.get(0);
        final Elements searchResults = searchResultsElement.getElementsByClass("search-result-link");
        for (final Element searchResult : searchResults) {
            final String resultModel = searchResult.text() //
                    .replaceAll("[^\\w\\s-]", "") //
                    .replaceAll("^(.*?)(Processor).*$", "$1") //
                    .toLowerCase().trim();
            if (resultModel.endsWith(model)) {
                return this.retrieveDocument(UrlBuilder.empty() //
                        .withScheme(searchUrl.getProtocol()) //
                        .withHost(searchUrl.getHost()) //
                        .withPath(searchResult.attr("href")) //
                        .toUrl());
            }
        }
        return null;
    }

    /**
     * Extracts the specifications from the given details-document.
     *
     * @param detailsDocument
     *            The document to extract the specifications from.
     * @return The extracted specifications.
     */
    private Map<String, String> extractSpecifications(final Document detailsDocument) {
        if (detailsDocument == null) {
            return null;
        }

        final Map<String, String> specs = new ConcurrentHashMap<>();
        detailsDocument.getElementsByClass("specs").stream() //
                .map(specTable -> specTable.getElementsByTag("tr")) //
                .map(specRowList -> specRowList.subList(1, specRowList.size())) //
                .flatMap(specRowList -> specRowList.stream()) //
                .forEach(specRow -> {
                    final Element keyElement = specRow.getElementsByClass("lc").get(0).getElementsByTag("span").get(0);
                    final Element valElement = specRow.getElementsByClass("rc").get(0);

                    specs.put(keyElement.ownText(), valElement.ownText());
                });
        return specs;
    }

    /**
     * Builds the URL that is used for searching the correct URL of the
     * details-document.
     *
     * @param model
     *            The Model to build to search URL for.
     * @return The search URL.
     */
    private URL buildSearchUrl(final String model) {
        final UrlBuilder urlBuilder = UrlBuilder.empty() //
                .withScheme("http") //
                .withHost("ark.intel.com") //
                .withPath("/search") //
                .addParameter("q", model);
        return urlBuilder.toUrl();
    }

    /**
     * Retrieves the document that is located behind the given {@link URL}.
     *
     * @param url
     *            The {@link URL} to retrieve the document of.
     * @return The retrieved document.
     * @throws ProcessorServiceException
     *             If any error occurs whilst retrieving the document (e.g. an
     *             Error 404: Not Found).
     */
    private Document retrieveDocument(final URL url) throws ProcessorServiceException {
        try {
            return Jsoup.connect(url.toString()).get();
        } catch (final IOException cause) {
            throw new IntelProcessorServiceException("Failed to fetch the Intel data!", cause);
        }
    }
}
