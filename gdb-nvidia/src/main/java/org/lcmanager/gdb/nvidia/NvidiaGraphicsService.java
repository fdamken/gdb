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
package org.lcmanager.gdb.nvidia;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lcmanager.gdb.base.CollectionUtil;
import org.lcmanager.gdb.base.NumberUtil;
import org.lcmanager.gdb.nvidia.exception.NvidiaGraphicsServiceException;
import org.lcmanager.gdb.service.annotation.Branded;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Brand.WellKnownBrand;
import org.lcmanager.gdb.service.data.model.Graphics;
import org.lcmanager.gdb.service.graphics.GraphicsService;
import org.lcmanager.gdb.service.graphics.exception.GraphicsServiceException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.mikael.urlbuilder.UrlBuilder;

/**
 * The Nvidia implementation of {@link GraphicsService}.
 * 
 */
@Service
@Branded
@CacheConfig(cacheNames = "nvidia-graphics-service")
public class NvidiaGraphicsService implements GraphicsService {
    /**
     * The regular expression that is used to determine the DirectX version key.
     * 
     */
    private static final Pattern DIRECTX_KEY_PATTERN = Pattern.compile("^.*direct[-_ ]?x.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The regular expression that is used to determine the DirectX version
     * value. The DirectX version group is named <code>version</code>.
     * 
     */
    private static final Pattern DIRECTX_VALUE_PATTERN = Pattern.compile("^.*?(?<version>[\\d\\.]+).*$");
    /**
     * The regular expression that is used to determine the OpenGL version key.
     * 
     */
    private static final Pattern OPENGL_KEY_PATTERN = Pattern.compile("^.*open[-_ ]?gl.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The regular expression that is used to determine the OpenGL version
     * value. The OpenGL version group is named <code>version</code>.
     * 
     */
    private static final Pattern OPENGL_VALUE_PATTERN = Pattern.compile("^.*?(?<version>[\\d\\.]+).*$");
    /**
     * The regular expression that is used to determine the frequency key.
     * 
     */
    private static final Pattern FREQUENCY_KEY_PATTERN = Pattern.compile("^.*?base\\s*clock.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The regular expression that is used to determine the memory key.
     * 
     */
    private static final Pattern MEMORY_KEY_PATTERN = Pattern.compile("^.*?memory.*$", Pattern.CASE_INSENSITIVE);
    /**
     * The regular expression that is used to determine the memory key. This one
     * must be <code>false</code>.
     * 
     */
    private static final Pattern MEMORY_KEY_PATTERN_NEGATIVE = Pattern.compile("^.*?memory.*?(speed|interface|bandwith).*$",
            Pattern.CASE_INSENSITIVE);

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#retrieveGraphics(org.lcmanager.gdb.service.data.model.Brand,
     *      java.lang.String)
     */
    @Override
    @Cacheable
    public Graphics retrieveGraphics(final Brand brand, final String model) throws GraphicsServiceException {
        if (!this.isResponsible(brand)) {
            throw new IllegalArgumentException(
                    "The Nvidia service is not responsible for " + brand.getName() + " graphics cards!");
        }

        final GpuType gpuType = GpuType.getByModel(model);
        final URL url = gpuType.buildUrl(model);
        final Document document = NvidiaGraphicsService.retrieveDocument(url);
        final Map<String, String> data = this.extractSpecifications(gpuType, document);

        final Graphics graphics = new Graphics();
        graphics.setBrand(WellKnownBrand.NVIDIA.getBrand());
        graphics.setModel(model);

        graphics.setDirectXVersion(CollectionUtil.findValue(data, key -> {
            return NvidiaGraphicsService.DIRECTX_KEY_PATTERN.matcher(key).matches();
        }, value -> {
            final Matcher matcher = NvidiaGraphicsService.DIRECTX_VALUE_PATTERN.matcher(value);
            matcher.matches();
            return matcher.group("version");
        }));

        graphics.setOpenGlVersion(CollectionUtil.findValue(data, key -> {
            return NvidiaGraphicsService.OPENGL_KEY_PATTERN.matcher(key).matches();
        }, value -> {
            final Matcher matcher = NvidiaGraphicsService.OPENGL_VALUE_PATTERN.matcher(value);
            matcher.matches();
            return matcher.group("version");
        }));

        graphics.setFrequency(CollectionUtil.findValue(data, key -> {
            return NvidiaGraphicsService.FREQUENCY_KEY_PATTERN.matcher(key).matches();
        }, value -> {
            final String lowerValue = value.toLowerCase().trim();
            final int number = NumberUtil.extractNumber(lowerValue);
            if (lowerValue.endsWith("ghz")) {
                return number * 1000;
            } else if (lowerValue.endsWith("khz")) {
                return number / 1000;
            } else {
                return number;
            }
        }));

        graphics.setMemory(CollectionUtil.findValue(data, key -> {
            return NvidiaGraphicsService.MEMORY_KEY_PATTERN.matcher(key).matches()
                    && !NvidiaGraphicsService.MEMORY_KEY_PATTERN_NEGATIVE.matcher(key).matches();
        }, value -> {
            final String lowerValue = value.toLowerCase().trim();
            final int number = NumberUtil.extractNumber(lowerValue);
            if (lowerValue.endsWith("gb") || lowerValue.endsWith("gib")) {
                return number * 1024;
            } else if (lowerValue.endsWith("kb") || lowerValue.endsWith("kib")) {
                return number / 1024;
            } else {
                return number;
            }
        }));

        return graphics;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#isResponsible(org.lcmanager.gdb.service.data.model.Brand)
     */
    @Override
    public boolean isResponsible(final Brand brand) {
        return WellKnownBrand.NVIDIA.getBrand().equals(brand);
    }

    /**
     * Retrieves the document that is located behind the given {@link URL}.
     *
     * @param url
     *            The {@link URL} to retrieve the document of.
     * @return The retrieved document.
     * @throws GraphicsServiceException
     *             If any error occurs whilst retrieving the document (e.g. an
     *             Error 404: Not Found).
     */
    private static Document retrieveDocument(final URL url) throws GraphicsServiceException {
        try {
            return Jsoup.connect(url.toString()).get();
        } catch (final IOException cause) {
            throw new NvidiaGraphicsServiceException("Failed to fetch the Nvidia data!", cause);
        }
    }

    /**
     * Extracts all specifications from the given {@link Document} for the given
     * {@link GpuType}.
     *
     * @param gpuType
     *            The {@link GpuType} to extract the specifications for.
     * @param document
     *            The document to extract the the specifications from.
     * @return The extracted specifications in a key-value storage.
     */
    private Map<String, String> extractSpecifications(final GpuType gpuType, final Document document) {
        final Map<String, String> specifications = new HashMap<>();
        switch (gpuType) {
            case DESKTOP:
            case NOTEBOOK: {
                final Element main = document.getElementById("block-system-main");
                final Element table = main.getElementsByClass("coloredTable").get(0);
                final Elements rows = table.getElementsByClass("row");
                rows.forEach(row -> {
                    final String key = row.ownText();
                    final String value = row.getElementsByClass("right").get(0).text();
                    specifications.put(key, value);
                });
                break;
            }
            case OTHER: {
                final Element main = document.getElementById("mainContent");
                final Element table = main.getElementsByTag("table").get(1);
                final Elements rows = table.getElementsByTag("tr");
                rows.forEach(row -> {
                    final Elements additionalInfoHeaderElements = row.getElementsByClass("additionalInfoHeader");
                    final Elements additionalInfoElements = row.getElementsByClass("additionalInfo");
                    if (additionalInfoHeaderElements.size() > 0 && additionalInfoElements.size() > 0) {
                        final String key = additionalInfoHeaderElements.get(0).text();
                        final String value = additionalInfoElements.get(0).text();
                        specifications.put(key, value);
                    }
                });
                break;
            }
            default:
                throw new IllegalArgumentException("The GPU type " + gpuType + " is not supported!");
        }
        return specifications;
    }

    /**
     * A GPU differs in multiple type (e.g. desktop GPUs). This enumeration
     * represents all supported GPU types.
     *
     */
    private static enum GpuType {
        /**
         * A desktop GPU. A desktop GPU is built into a normal desktop computer.
         * 
         */
        DESKTOP("^geforce (gtx?) (\\d+)( .+)?$") {
            /**
             * {@inheritDoc}
             *
             * @see org.lcmanager.gdb.nvidia.NvidiaGraphicsService.GpuType#buildUrl(java.lang.String)
             */
            @Override
            public URL buildUrl(final String model) {
                return UrlBuilder.empty() //
                        .withScheme("http") //
                        .withHost("www.geforce.com") //
                        .withPath("/hardware/desktop-gpus/" + model.replace(' ', '-') + "/specifications") //
                        .toUrl();
            }
        },
        /**
         * A notebook GPU. A notebook GPU is built into notebooks.
         * 
         */
        NOTEBOOK("^geforce (gtx?) (\\d+)m( .+)?$") {
            /**
             * {@inheritDoc}
             *
             * @see org.lcmanager.gdb.nvidia.NvidiaGraphicsService.GpuType#buildUrl(java.lang.String)
             */
            @Override
            public URL buildUrl(final String model) {
                return UrlBuilder.empty() //
                        .withScheme("http") //
                        .withHost("www.geforce.com") //
                        .withPath("/hardware/notebook-gpus/" + model.replace(' ', '-') + "/specifications") //
                        .toUrl();
            }
        },
        /**
         * All other types of GPUs.
         * 
         */
        OTHER(".*") {
            /**
             * {@inheritDoc}
             *
             * @see org.lcmanager.gdb.nvidia.NvidiaGraphicsService.GpuType#buildUrl(java.lang.String)
             */
            @Override
            public URL buildUrl(final String model) {
                return UrlBuilder.empty() //
                        .withScheme("http") //
                        .withHost("www.nvidia.com") //
                        .withPath("/page/" + model.replace(' ', '_') + ".html") //
                        .toUrl();
            }
        };

        /**
         * The regular expression that describes the format of the model name
         * that a GPU type supports.
         * 
         */
        private final Pattern pattern;

        /**
         * Constructor of GpuType.
         *
         * @param pattern
         *            The {@link #pattern} to set.
         */
        private GpuType(final String pattern) {
            this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        }

        /**
         * Builds the {@link URL} to the specification page for the given model.
         *
         * @param model
         *            The model to build the {@link URL} for.
         * @return The built {@link URL}.
         */
        public abstract URL buildUrl(final String model);

        /**
         * Finds the GPU type that is responsible for the given model.
         *
         * @param model
         *            The model to find the GPU type for.
         * @return The found GPU type, if any.
         * @throws IllegalArgumentException
         *             If not GPU type was found.
         */
        public static GpuType getByModel(final String model) {
            final GpuType result = Arrays.stream(GpuType.values()).filter(value -> value.pattern.matcher(model).matches())
                    .findFirst().get();
            if (result == null) {
                throw new IllegalArgumentException(model + "does not match any GPU type!");
            }
            return result;
        }
    }
}
