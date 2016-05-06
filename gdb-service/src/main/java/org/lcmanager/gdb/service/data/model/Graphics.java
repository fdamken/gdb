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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;

/**
 * Represents a graphics card and all its relevant technical data.
 *
 */
@Data
@Accessors(chain = true)
public class Graphics implements BaseModel<Integer>, Comparable<Graphics> {
    private static final Pattern DIRECTX_VERSION_REGEX = Pattern.compile("^([0-9]+)(\\.([0-9]+)([a-z])?)?$");

    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 5131569989763237510L;

    /**
     * The ID of this graphics (card).
     * 
     */
    private Integer id;
    private String brand;
    private String model;
    private Integer memory;
    private Integer frequency;
    private Integer directXVersion;
    private String openGlVersion;

    public Graphics setDirectXVersion(final String version) {
        final Matcher matcher = Graphics.DIRECTX_VERSION_REGEX.matcher(version);

        if (matcher.matches()) {
            throw new IllegalArgumentException(
                    version + " does not match the regex " + Graphics.DIRECTX_VERSION_REGEX.pattern() + "!");
        }

        final String majorStr = matcher.group(1);
        final String minorStr = matcher.group(3);
        final String letterStr = matcher.group(4);

        int directXVersion = 0;
        if (majorStr != null) {
            directXVersion += Integer.parseInt(majorStr) * 1_000_000;
        }
        if (minorStr != null) {
            directXVersion += Integer.parseInt(minorStr) * 1_000;
        }
        if (letterStr != null) {
            directXVersion += letterStr.charAt(0) - ('a' - 1);
        }

        this.directXVersion = directXVersion;

        return this;
    }

    public static void main(final String[] args) {
        val g = new Graphics();
        g.setDirectXVersion("9.0c");
        System.out.println(g.getDirectXVersion());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Graphics graphics) {
        // TODO Auto-generated method body.
        return 0;
    }
}
