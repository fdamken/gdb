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

import java.util.Arrays;
import java.util.Locale;

import org.lcmanager.gdb.base.Formatable;

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
public class Brand implements BaseModel<Integer>, Formatable {
    /**
     * The serial version UID.
     * 
     */
    private static final long serialVersionUID = 5239321573574886108L;

    /**
     * The ID of this brand.
     * 
     */
    private Integer id;
    /**
     * The name of this brand.
     * 
     */
    private String name;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.base.Formatable#format(java.util.Locale)
     */
    @Override
    public String format(final Locale locale) {
        return this.name;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Contains some well-known brands.
     *
     */
    @Getter
    @RequiredArgsConstructor
    public static enum WellKnownBrand {
        /**
         * Represents the brand <code>AMD</code>.
         * 
         */
        AMD(1, "AMD"),
        /**
         * Represents the brand <code>Intel</code>.
         * 
         */
        INTEL(2, "Intel"),
        /**
         * Represents the brand <code>Nvidia</code>.
         * 
         */
        NVIDIA(3, "Nvidia");

        /**
         * The ID of this brand.
         * 
         */
        private final int id;
        /**
         * The name of this brand.
         * 
         */
        private final String name;

        public static boolean isWellKnownBrand(final Brand brand) {
            return brand.getId() >= 1 && brand.getId() <= 3;
        }

        public static WellKnownBrand getWellKnownBrand(final Brand brand) {
            if (!WellKnownBrand.isWellKnownBrand(brand)) {
                throw new IllegalArgumentException("Brand is not a well-known brand!");
            }

            final int id = brand.getId();
            final WellKnownBrand result = Arrays.stream(WellKnownBrand.values()) //
                    .filter(value -> value.getId() == id) //
                    .findFirst().get();
            if (result == null) {
                throw new IllegalArgumentException("Invalid id: " + id + ". Not found!");
            }
            return result;
        }

        /**
         *
         * @return The representing {@link Brand}.
         */
        public Brand getBrand() {
            return new Brand().setId(this.id).setName(this.name);
        }
    }
}
