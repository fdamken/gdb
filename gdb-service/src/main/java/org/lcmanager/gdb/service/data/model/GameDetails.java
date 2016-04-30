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
package org.lcmanager.gdb.service.data.model;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

import org.lcmanager.gdb.service.data.model.os.OsFamily;

/**
 * Represents the details of any game.
 *
 */
@Data
@Accessors(chain = true)
public class GameDetails implements BaseModel<Integer> {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 3815416359591148682L;

    /**
     * The name of the game.
     * 
     */
    private String name;
    /**
     * The ID of the game. Represents a steam app ID.
     * 
     */
    private Integer id;
    /**
     * The minimum age that is required to play the game (in years).
     * 
     */
    private int requiredAge;
    /**
     * A detailed description of the game.
     * 
     */
    private String detailedDescription;
    /**
     * The URL of the header image.
     * 
     */
    private URL headerImage;
    /**
     * The website of this game.
     * 
     */
    private URL website;
    /**
     * The minimal system requirements sorted by the OS family.
     * 
     */
    private Map<OsFamily, Requirement> minimumRequirements;
    /**
     * The recommended system requirements sorted by the OS family.
     * 
     */
    private Map<OsFamily, Requirement> recommendedRequirements;
    /**
     * The developers that have developed the game.
     * 
     */
    private List<String> developers;
    /**
     * The publishers of the game.
     * 
     */
    private List<String> publishers;
    /**
     * The OS families the game can run on.
     * 
     */
    private List<OsFamily> platforms;
    /**
     * The store on Metacritic.
     * 
     */
    private int metacriticScore;
    /**
     * The URL to the Metacritic test.
     * 
     */
    private URL metacriticUrl;
    /**
     * The categories this game is assigned to.
     * 
     */
    private List<Category> categories;
    /**
     * The genres this game is part of.
     * 
     */
    private List<Genre> genres;
    /**
     * Screenshots of the game.
     * 
     */
    private List<Screenshot> screenshots;
    /**
     * The release data.
     * 
     */
    private Date releaseDate;
    /**
     * The image to be used as the background image.
     * 
     */
    private URL background;
}
