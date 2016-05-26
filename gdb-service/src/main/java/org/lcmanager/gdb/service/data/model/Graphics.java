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
import lombok.experimental.Accessors;

/**
 * Represents a graphics card and all its relevant technical data.
 *
 */
@Data
@Accessors(chain = true)
public class Graphics implements BaseModel<Integer> {
    /**
     * The pattern that a DirectX version must match.
     * 
     */
    private static final Pattern DIRECTX_VERSION_REGEX = Pattern.compile("^([0-9]+)(\\.([0-9]+)([a-z])?)?$");
    /**
     * The pattern that a OpenGL version must match.
     * 
     */
    private static final Pattern OPENGL_VERSION_REGEX = Pattern.compile("^([0-9]+)(\\.([0-9]+))?$");

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
    /**
     * The brand of this graphics card.
     * 
     */
    private Brand brand;
    /**
     * The model of this graphics card.
     * 
     */
    private String model;
    /**
     * The video memory of this graphics card in mebibyte.
     * 
     */
    private Integer memory;
    /**
     * The frequency of this graphics card in megahertz.
     * 
     */
    private Integer frequency;
    /**
     * The DirectX version this graphics card supports.
     * 
     */
    private String directXVersion;
    /**
     * The OpenGL version this graphics card supports.
     * 
     */
    private String openGlVersion;

    /**
     * Encodes the DirectX version in the format <code>XXYYYZZZ</code> where
     * <code>XX</code> represents the major version, <code>YYY</code> represents
     * the minor version and <code>ZZZ</code> the letter of the version starting
     * from <code>a = 1</code>.
     * <p>
     * For example, the DirectX version <code>9.0c</code> would be
     * <code>9000003</code>.
     * </p>
     *
     * @return The encoded DirectX version.
     */
    public long encodeDirectXVersion() {
        final Matcher matcher = Graphics.DIRECTX_VERSION_REGEX.matcher(this.directXVersion);
        matcher.matches();
        String majorStr = null;
        try {
            majorStr = matcher.group(1);
        } catch (final IllegalStateException dummy) {
            // No match --> majorStr = null
        }
        String minorStr = null;
        try {
            minorStr = matcher.group(3);
        } catch (final IllegalStateException dummy) {
            // No match --> minorStr = null
        }
        String letterStr = null;
        try {
            letterStr = matcher.group(4);
        } catch (final IllegalStateException dummy) {
            // No match --> letterStr = null
        }

        long encodedDirectXVersion = 0;
        if (majorStr != null) {
            encodedDirectXVersion += Integer.parseInt(majorStr) * 1_000_000;
        }
        if (minorStr != null) {
            encodedDirectXVersion += Integer.parseInt(minorStr) * 1_000;
        }
        if (letterStr != null) {
            encodedDirectXVersion += letterStr.charAt(0) - ('a' - 1);
        }

        return encodedDirectXVersion;
    }

    /**
     * Encodes the OpenGL version in the format <code>XXYYY</code> where
     * <code>XX</code> represents the major version and <code>YYY</code>
     * represents the minor version.
     * <p>
     * For example, the OpenGL version <code>3.2</code> would be
     * <code>3001</code>.
     * </p>
     *
     * @return The encoded OpenGL version
     */
    public long encodeOpenGlVersion() {
        final Matcher matcher = Graphics.OPENGL_VERSION_REGEX.matcher(this.openGlVersion);
        matcher.matches();
        String majorStr = null;
        try {
            majorStr = matcher.group(1);
        } catch (final IllegalStateException dummy) {
            // No match --> majorStr = null
        }
        String minorStr = null;
        try {
            minorStr = matcher.group(3);
        } catch (final IllegalStateException dummy) {
            // No match --> minorStr = null
        }

        long encodedOpenGlVersion = 0;
        if (majorStr != null) {
            encodedOpenGlVersion += Integer.parseInt(majorStr) * 1_000;
        }
        if (minorStr != null) {
            encodedOpenGlVersion += Integer.parseInt(minorStr);
        }

        return encodedOpenGlVersion;
    }
}
