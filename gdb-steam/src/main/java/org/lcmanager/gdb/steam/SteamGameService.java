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
package org.lcmanager.gdb.steam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lcmanager.gdb.base.FunctionUtil;
import org.lcmanager.gdb.base.Paged;
import org.lcmanager.gdb.base.PaginationMetadata;
import org.lcmanager.gdb.base.StreamUtil;
import org.lcmanager.gdb.base.exception.GdbExceptionWrapper;
import org.lcmanager.gdb.service.annotation.Branded;
import org.lcmanager.gdb.service.data.model.Category;
import org.lcmanager.gdb.service.data.model.Developer;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.data.model.Genre;
import org.lcmanager.gdb.service.data.model.Publisher;
import org.lcmanager.gdb.service.data.model.Screenshot;
import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.game.GameQuery;
import org.lcmanager.gdb.service.game.GameService;
import org.lcmanager.gdb.service.game.exception.GameServiceException;
import org.lcmanager.gdb.steam.exception.SteamGameServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.mikael.urlbuilder.UrlBuilder;

/**
 * The Steam implementation of {@link GameService}.
 *
 */
@Service
@Branded
@CacheConfig(cacheNames = "steam-game-service")
public class SteamGameService implements GameService {
    /**
     * The pattern of Steams <code>'pagination_left'</code> element.
     * 
     */
    private static final Pattern PAGINATION_LEFT_PATTERN = Pattern.compile("^showing\\s*(\\d+)\\s*-\\s*(\\d+)\\s*of\\s*(\\d+)$");
    /**
     * Contains all date formats that a release date may have.
     * 
     */
    private static final DateFormat[] RELEASE_DATE_FORMATS = new DateFormat[] { //
            new SimpleDateFormat("d MMM, yyyy"), //
            new SimpleDateFormat("MMM d, yyyy"), //
            new SimpleDateFormat("MMM yyyy") //
    };

    /**
     * The {@link GameService} used for fetching the game details. May
     * delegating to this one.
     * 
     */
    @Autowired
    private GameService gameService;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGames(org.lcmanager.gdb.service.game.GameQuery,
     *      int, boolean)
     */
    @Override
    @Cacheable
    public Paged<Game> retrieveGames(final GameQuery query, final int page, final boolean loadAll) throws GameServiceException {
        final URL url = this.buildUrl(query, page);
        final Document document = this.retrieveDocument(url);
        final PaginationMetadata completePaginationMetadata = this.extractCompletePaginationMetadata(document, page);
        final List<Integer> gameIds = this.extractGameIds(document);

        final List<Game> games;
        if (loadAll) {
            games = new ArrayList<>();
            for (final int gameId : gameIds) {
                games.add(this.gameService.retrieveGame(gameId));
            }
        } else {
            try {
                games = gameIds.stream().map(gameId -> {
                    final Element appElement = document.getElementsByAttributeValue("data-ds-appid", String.valueOf(gameId))
                            .get(0);
                    final Element searchName = appElement.getElementsByClass("search_name").get(0);
                    final Element searchReleased = appElement.getElementsByClass("search_released").get(0);

                    final Game game = new Game();

                    game.setId(gameId);

                    game.setName(searchName.getElementsByClass("search_name").get(0).text());

                    final Elements platformElements = searchName.getElementsByClass("platform_img");
                    game.setPlatforms(platformElements.stream() //
                            .map(Element::classNames) //
                            .map(classNames -> {
                                if (classNames.contains("win")) {
                                    return OsFamily.WINDOWS;
                                } else if (classNames.contains("mac")) {
                                    return OsFamily.MAC;
                                } else if (classNames.contains("linux")) {
                                    return OsFamily.UNIX;
                                } else {
                                    return null;
                                }
                            }) //
                            .filter(FunctionUtil.notNull()) //
                            .collect(Collectors.toSet()) //
                    );

                    try {
                        game.setReleaseDate(this.parseReleaseDate(searchReleased.text()));
                    } catch (final ParseException cause) {
                        throw new GdbExceptionWrapper(new SteamGameServiceException("Failed to parse the release data!", cause));
                    }

                    return game;
                }).collect(Collectors.toList());
            } catch (final GdbExceptionWrapper wrapper) {
                throw (GameServiceException) wrapper.getCause();
            }
        }

        return new Paged<>(completePaginationMetadata, games);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGame(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    @Cacheable
    public Game retrieveGame(final int gameId) throws GameServiceException {
        final Map<String, Object> gameData = this.retrieveGameData(gameId);

        if (gameData == null || gameData.isEmpty()) {
            return null;
        }

        final Game game = new Game();

        final Object backgroundImageObj = gameData.get("background");
        if (backgroundImageObj instanceof String && !((String) backgroundImageObj).isEmpty()) {
            try {
                game.setBackgroundImage(new URL((String) backgroundImageObj));
            } catch (final MalformedURLException cause) {
                throw new SteamGameServiceException("Failed to parse the background image URL!", cause);
            }
        }

        final Object categoriesObj = gameData.get("categories");
        if (categoriesObj instanceof List) {
            game.setCategories(((List<Object>) categoriesObj).stream() //
                    .filter(categoryObj -> categoryObj instanceof Map) //
                    .map(categoryObj -> (Map<String, Object>) categoryObj) //
                    .map(categoryMap -> { //
                        final Object idObj = categoryMap.get("id");
                        final Object descriptionObj = categoryMap.get("description");
                        if (idObj instanceof Integer && descriptionObj instanceof String) {
                            return new Category().setId((int) idObj).setDescription((String) descriptionObj);
                        }
                        return (Category) null;
                    }).collect(Collectors.toSet()));
        }

        final Object descriptionObj = gameData.get("detailed_description");
        if (descriptionObj instanceof String) {
            game.setDescription((String) descriptionObj);
        }

        final Object developersObj = gameData.get("developers");
        if (developersObj instanceof List) {
            game.setDevelopers(((List<String>) developersObj).stream() //
                    .map(developer -> new Developer().setName(developer)) //
                    .collect(Collectors.toSet()));
        }

        final Object genresObj = gameData.get("genres");
        if (genresObj instanceof List) {
            game.setGenres(((List<Object>) genresObj).stream() //
                    .filter(genreObj -> genreObj instanceof Map) //
                    .map(genreObj -> (Map<String, Object>) genreObj) //
                    .map(genreMap -> {
                        final Object idObj = genreMap.get("id");
                        final Object genreDescriptionObj = genreMap.get("description");
                        if ((idObj instanceof String || idObj instanceof Integer) && genreDescriptionObj instanceof String) {
                            return new Genre().setId(idObj instanceof String ? Integer.parseInt((String) idObj) : (int) idObj)
                                    .setDescription((String) genreDescriptionObj);
                        }
                        return null;
                    }).collect(Collectors.toSet()));
        }

        final Object headerImageObj = gameData.get("header_image");
        if (headerImageObj instanceof String && !((String) headerImageObj).isEmpty()) {
            try {
                game.setHeaderImage(new URL((String) headerImageObj));
            } catch (final MalformedURLException cause) {
                throw new SteamGameServiceException("Failed to parse the header image URL!", cause);
            }
        }

        final Object idObj = gameData.get("steam_appid");
        if (idObj instanceof Integer) {
            game.setId((int) idObj);
        }

        final Object metacriticObj = gameData.get("metacritic");
        if (metacriticObj instanceof Map) {
            final Map<String, Object> metacriticMap = (Map<String, Object>) metacriticObj;
            final Object urlObj = metacriticMap.get("url");
            final Object scoreObj = metacriticMap.get("score");
            if (scoreObj instanceof Integer && urlObj instanceof String && !((String) urlObj).isEmpty()) {
                try {
                    game.setMetacriticUrl(new URL((String) urlObj));
                    game.setMetacriticScore((int) scoreObj);
                } catch (final MalformedURLException cause) {
                    throw new SteamGameServiceException("Failed to parse the metacritic URL!", cause);
                }
            }
        }

        game.setMinimumRequirements(null); // TODO

        final Object nameObj = gameData.get("name");
        if (nameObj instanceof String) {
            game.setName((String) nameObj);
        }

        final Object platformsObj = gameData.get("platforms");
        if (platformsObj instanceof Map) {
            final Map<String, Object> platformsMap = (Map<String, Object>) platformsObj;
            final Set<OsFamily> platforms = new HashSet<>();
            if (platformsMap.containsKey("windows")) {
                platforms.add(OsFamily.WINDOWS);
            }
            if (platformsMap.containsKey("mac")) {
                platforms.add(OsFamily.MAC);
            }
            if (platformsMap.containsKey("linux")) {
                platforms.add(OsFamily.UNIX);
            }
            game.setPlatforms(platforms);
        }

        final Object publishersObj = gameData.get("publishers");
        if (publishersObj instanceof List) {
            game.setPublishers(((List<String>) publishersObj).stream() //
                    .map(publisher -> new Publisher().setName(publisher)) //
                    .collect(Collectors.toSet()));
        }

        game.setRecommendedRequirements(null); // TODO

        final Object releaseDateObj = gameData.get("release_date");
        if (releaseDateObj instanceof Map) {
            final Map<String, Object> releaseDateMap = (Map<String, Object>) releaseDateObj;
            final Object dateObj = releaseDateMap.get("date");
            if (dateObj instanceof String && !((String) dateObj).isEmpty()) {
                try {
                    game.setReleaseDate(this.parseReleaseDate((String) dateObj));
                } catch (final ParseException cause1) {
                    throw new SteamGameServiceException("Failed to parse the release date!", cause1);
                }
            }
        }

        final Object requiredAgeObj = gameData.get("required_age");
        if (requiredAgeObj instanceof Integer) {
            game.setRequiredAge((int) requiredAgeObj);
        } else if (requiredAgeObj instanceof String) {
            game.setRequiredAge(Integer.parseInt((String) requiredAgeObj));
        }

        final Object screenshotsObj = gameData.get("screenshots");
        if (screenshotsObj instanceof List) {
            try {
                game.setScreenshots(((List<Object>) screenshotsObj).stream() //
                        .filter(screenshotObj -> screenshotObj instanceof Map) //
                        .map(screenshotObj -> (Map<String, Object>) screenshotObj) //
                        .map(screenshotMap -> {
                            final Object thumbnailObj = screenshotMap.get("path_thumbnail");
                            final Object fullObj = screenshotMap.get("path_full");
                            if (fullObj instanceof String && !((String) fullObj).isEmpty()) {
                                final Screenshot screenshot = new Screenshot();
                                try {
                                    screenshot.setImage(new URL((String) fullObj));
                                } catch (final MalformedURLException cause) {
                                    throw new GdbExceptionWrapper(
                                            new SteamGameServiceException("Failed to parse the screenshot URL!", cause));
                                }
                                if (thumbnailObj instanceof String) {
                                    try {
                                        screenshot.setThumbnail(new URL((String) thumbnailObj));
                                    } catch (final MalformedURLException cause) {
                                        throw new GdbExceptionWrapper(
                                                new SteamGameServiceException("Failed to parse the thumbnail URL!", cause));
                                    }
                                }
                                return screenshot;
                            }
                            return null;
                        }).collect(Collectors.toSet()));
            } catch (final GdbExceptionWrapper wrapper) {
                throw (SteamGameServiceException) wrapper.getCause();
            }
        }

        final Object websiteObj = gameData.get("website");
        if (websiteObj instanceof String && !((String) websiteObj).isEmpty()) {
            try {
                game.setWebsite(new URL((String) websiteObj));
            } catch (final MalformedURLException cause) {
                throw new SteamGameServiceException("Failed to parse the website URL!", cause);
            }
        } else {
            final Object supportInfoObj = gameData.get("support_info");
            if (supportInfoObj instanceof Map) {
                final Map<String, Object> supportInfoMap = (Map<String, Object>) supportInfoObj;
                final Object urlObj = supportInfoMap.get("url");
                if (urlObj instanceof String && !((String) urlObj).isEmpty()) {
                    try {
                        game.setWebsite(new URL((String) urlObj));
                    } catch (final MalformedURLException cause) {
                        if (cause.getMessage().contains("no protocol")) {
                            try {
                                game.setWebsite(new URL("http://" + (String) urlObj));
                            } catch (final MalformedURLException cause2) {
                                throw new SteamGameServiceException("Failed to parse the support info URL!", cause2);
                            }
                        } else {
                            throw new SteamGameServiceException("Failed to parse the support info URL!", cause);
                        }
                    }
                }
            }
        }

        return game;
    }

    /**
     * Builds the URL that can be used to access the Steam page for the search
     * results that include the game IDs.
     *
     * @param query
     *            The query to build the URL for.
     * @param page
     *            The page to build the URL for.
     * @return The built URL.
     */
    private URL buildUrl(final GameQuery query, final int page) {
        UrlBuilder builder = UrlBuilder.empty() //
                .withScheme("http") //
                .withHost("store.steampowered.com") //
                .withPath("/search");

        builder = builder.addParameter("category1", String.valueOf(998));

        builder = builder.addParameter("category2",
                query.getCategories1().stream() //
                        .map(category -> category.getId()) //
                        .collect(StreamUtil.collectString(",")));

        builder = builder.addParameter("category3",
                query.getCategories2().stream() //
                        .map(category -> category.getId()) //
                        .collect(StreamUtil.collectString(",")));

        builder = builder.addParameter("os", query.getPlatforms().stream() //
                .filter(platform -> !platform.equals(OsFamily.OTHER)).map(platform -> {
                    if (platform.equals(OsFamily.WINDOWS)) {
                        return "win";
                    } else if (platform.equals(OsFamily.MAC)) {
                        return "mac";
                    } else if (platform.equals(OsFamily.UNIX)) {
                        return "linux";
                    } else {
                        return null;
                    }
                }).collect(StreamUtil.collectString(",")));

        if (query.getSorting() == null) {
            builder = builder.addParameter("sort_by", "_ASC");
        } else {
            builder = builder.addParameter("sort_by",
                    query.getSorting().getTerm() + "_" + query.getSorting().getDirection().getAbbreviation());
        }

        if (query.getTerm() != null) {
            builder = builder.addParameter("term", query.getTerm());
        }

        builder = builder.addParameter("page", String.valueOf(page));

        return builder.toUrl();
    }

    /**
     * Retrieves the {@link Document} behind the given {@link URL}.
     *
     * @param url
     *            The URL to retrieve.
     * @return The retrieved Steam search result page {@link Document}.
     * @throws GameServiceException
     *             If an error occurs whilst accessing the given {@link URL}.
     */
    private Document retrieveDocument(final URL url) throws GameServiceException {
        try {
            return Jsoup.connect(url.toString()).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 "
                    + "(KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36").get();
        } catch (final IOException cause) {
            throw new SteamGameServiceException("Failed to fetch the games from Steam!", cause);
        }
    }

    /**
     * Extracts a complete {@link PaginationMetadata} from the given Steam
     * search result page {@link Document}.
     *
     * @param document
     *            The Steam search result page {@link Document}.
     * @param page
     *            The current page.
     * @return The complete {@link PaginationMetadata}.
     */
    private PaginationMetadata extractCompletePaginationMetadata(final Document document, final int page) {
        final Element searchResults = document.getElementById("search_results");

        final Elements paginationLeftElements = searchResults.getElementsByClass("search_pagination_left");
        final int totalItems;
        final int totalPages;
        if (paginationLeftElements.size() > 0) {
            final String paginationLeft = paginationLeftElements.get(0).text();
            final Matcher paginationMatcher = SteamGameService.PAGINATION_LEFT_PATTERN.matcher(paginationLeft);
            if (!paginationMatcher.matches()) {
                throw new IllegalStateException("The pagination left " + paginationLeft + " is invalid!");
            }
            totalItems = Integer.parseInt(paginationMatcher.group(3));
            final Elements paginationRightAnchors = searchResults.getElementsByClass("search_pagination_right").get(0)
                    .getElementsByTag("a");
            totalPages = Integer.parseInt(paginationRightAnchors.get(paginationRightAnchors.size() - 2).text());
        } else {
            totalItems = 0;
            totalPages = 1;
        }

        return new PaginationMetadata(page, GameService.PAGE_SIZE, totalItems, totalPages);
    }

    /**
     * Extracts the game IDs of the given Steam search result page
     * {@link Document}.
     *
     * @param document
     *            The Steam search result page {@link Document}.
     * @return A list of all game IDs on the given Steam search result page.
     */
    private List<Integer> extractGameIds(final Document document) {
        return document.getElementById("search_results").getElementsByClass("search_result_row").stream() //
                .sequential() //
                .map(row -> row.attr("data-ds-appid")) //
                .map(Integer::parseInt) //
                .collect(Collectors.toList());
    }

    /**
     * Builds an {@link URL} that points to the REST endpoint of the Steam
     * services for the details for the given game ID.
     *
     * @param gameId
     *            The game ID to built the {@link URL} for.
     * @return The built {@link URL}.
     */
    private URL buildRestUrl(final int gameId) {
        return UrlBuilder.empty() //
                .withScheme("http") //
                .withHost("store.steampowered.com") //
                .withPath("/api/appdetails") //
                .addParameter("appids", String.valueOf(gameId)) //
                .toUrl();
    }

    /**
     * Retrieves the game data for the given game ID.
     *
     * @param gameId
     *            The game ID to retrieve the data for.
     * @return The retrieved game data, if any. If the given game ID does not
     *         exist, <code>null</code>.
     * @throws SteamGameServiceException
     *             If the access to the Steam services fails.
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> retrieveGameData(final int gameId) throws SteamGameServiceException {
        final Map<String, Object> data;
        try {
            final Map<String, Object> rawGameData = new JacksonJsonParser()
                    .parseMap(Jsoup.connect(this.buildRestUrl(gameId).toString()) //
                            .ignoreContentType(true) //
                            .execute() //
                            .body());
            data = (Map<String, Object>) ((Map<String, Object>) rawGameData.get(String.valueOf(gameId))).get("data");
        } catch (final IOException cause) {
            throw new SteamGameServiceException("Failed to parse the JSON from Steam!", cause);
        }
        return data;
    }

    /**
     * Parses the given date by trying all {@link #RELEASE_DATE_FORMATS} until a
     * matching one was found.
     *
     * @param date
     *            The date string to parse.
     * @return The parsed date.
     * @throws ParseException
     *             If the parsing was not successful with every date format.
     */
    private Date parseReleaseDate(final String date) throws ParseException {
        Date result = null;

        final List<Exception> exceptions = new ArrayList<>();
        for (final DateFormat dateFormat : SteamGameService.RELEASE_DATE_FORMATS) {
            try {
                result = dateFormat.parse(date);

                // The parsing was successful --> Stop trying.
                break;
            } catch (final ParseException | NumberFormatException ex) {
                exceptions.add(ex);
            }
        }

        if (result == null) {
            final ParseException exception = new ParseException(
                    "Failed to parse the release date! See suppressed error for more details", -1);
            exceptions.forEach(exception::addSuppressed);
            throw exception;
        }

        return result;
    }
}
