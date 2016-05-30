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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lcmanager.gdb.base.CollectionUtil;
import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.data.util.RequirementType;
import org.lcmanager.gdb.service.data.util.TypedRequirement;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Represents the details of any game.
 *
 */
@Data
@Accessors(chain = true)
public class Game implements BaseModel<Integer> {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 3815416359591148682L;

    // ~ Direct ~
    /**
     * The ID of the game. Represents a steam game ID.
     * 
     */
    private Integer id;
    /**
     * The name of the game.
     * 
     */
    private String name;
    /**
     * The minimum age that is required to play the game (in years).
     * 
     */
    private Integer requiredAge;
    /**
     * A detailed description of the game.
     * 
     */
    private String description;
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
     * The store on Metacritic.
     * 
     */
    private Integer metacriticScore;
    /**
     * The URL to the Metacritic test.
     * 
     */
    private URL metacriticUrl;
    /**
     * The release data.
     * 
     */
    private Date releaseDate;
    /**
     * The image to be used as the background image.
     * 
     */
    private URL backgroundImage;

    // ~ Mapped ~
    /**
     * Screenshots of the game.
     * 
     */
    private Set<Screenshot> screenshots;
    /**
     * The genres this game is part of.
     * 
     */
    private Set<Genre> genres;
    /**
     * The categories this game is assigned to.
     * 
     */
    private Set<Category> categories;
    /**
     * The publishers of the game.
     * 
     */
    private Set<Publisher> publishers;
    /**
     * The developers that have developed the game.
     * 
     */
    private Set<Developer> developers;
    /**
     * The OS families the game can run on.
     * 
     */
    private Set<OsFamily> platforms;

    // ~ Double Mapped ~
    /**
     * The minimal system requirements sorted by the OS family.
     * 
     */
    private Map<OsFamily, Requirement> minimumRequirements = new HashMap<>();
    /**
     * The recommended system requirements sorted by the OS family.
     * 
     */
    private Map<OsFamily, Requirement> recommendedRequirements = new HashMap<>();

    // ~ Transient ~

    /**
     * A list that is used by MyBatis only to add requirements without mapping
     * them explicitly to a map.
     * 
     */
    @Getter(AccessLevel.PRIVATE)
    private final transient List<TypedRequirement> requirementAdder = CollectionUtil.createAddOnlyList(this::addRequirement);

    /**
     * Adds the given requirement by determining the requirement type.
     * 
     * <p>
     * This method is also invoked when invoking the {@link List#add(Object)} on
     * {@link #getRequirementAdder()}.
     * </p>
     *
     * @param requirement
     *            The requirement to add.
     */
    private void addRequirement(final TypedRequirement requirement) {
        if (requirement == null || requirement.getType() == null) {
            throw new IllegalArgumentException("Neither requirement nor requirement.type shall be null!");
        }

        if (requirement.getType().equals(RequirementType.MINIMUM)) {
            this.minimumRequirements.put(requirement.getOsFamily(), requirement);
        } else if (requirement.getType().equals(RequirementType.RECOMMENDED)) {
            this.recommendedRequirements.put(requirement.getOsFamily(), requirement);
        } else {
            throw new IllegalArgumentException("Unsupported requirement type: " + requirement.getType() + "!");
        }
    }
}
