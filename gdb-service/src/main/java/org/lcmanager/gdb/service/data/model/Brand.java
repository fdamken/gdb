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

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A brand is used to mark products to safely compare them with other products
 * of the same brand.
 *
 */
@Data
@Accessors(chain = true)
public class Brand implements BaseModel<String> {
    /**
     * The serial version UID.
     * 
     */
    private static final long serialVersionUID = 5239321573574886108L;

    /**
     * The ID of this brand.
     * 
     */
    private String id;
    /**
     * The name of this brand.
     * 
     */
    private String name;

    /**
     * Creates a new {@link Brand} with the given brand ID.
     *
     * @param brandId
     *            The brand ID. Case insensitive.
     * @return The newly created brand.
     */
    public static Brand makeBrand(final String brandId) {
        if (brandId == null || brandId.trim().isEmpty()) {
            throw new IllegalArgumentException("BrandId must not be null or empty!");
        }

        return new Brand().setId(brandId.toLowerCase().trim());
    }

    /**
     * Constains some well-known brands.
     *
     */
    @Getter
    @RequiredArgsConstructor
    public static enum WellKnownBrand {
        /**
         * Represents the brand <code>AMD</code>.
         * 
         */
        AMD("amd", "AMD"),
        /**
         * Represents the brand <code>Intel</code>.
         * 
         */
        INTEL("intel", "Intel"),
        /**
         * Represents the brand <code>Nvidia</code>.
         * 
         */
        NVIDIA("nvidia", "Nvidia");

        /**
         * The ID of this brand.
         * 
         */
        private final String id;
        /**
         * The name of this brand.
         * 
         */
        private final String name;

        /**
         *
         * @return The representing {@link Brand}.
         */
        public Brand getBrand() {
            return Brand.makeBrand(this.id).setName(this.name);
        }
    }
}
