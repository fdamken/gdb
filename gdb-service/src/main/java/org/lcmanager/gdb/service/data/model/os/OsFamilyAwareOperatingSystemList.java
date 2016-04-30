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
package org.lcmanager.gdb.service.data.model.os;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;

/**
 * Represents a list of operating systems that is aware of the OS family and
 * keeps track that all operating systems are belonging to the same family.
 *
 */
public class OsFamilyAwareOperatingSystemList extends ArrayList<OperatingSystem> implements OsFamilyAware {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = -2799439798924599613L;

    /**
     * The object to retrieve locks on for modifying {@link #osFamily}.
     * 
     */
    private final Object lockOsFamily = new Object();
    /**
     * The OS family this list contains elements of. It is automatically set
     * when the first elements gets added.
     * 
     */
    @Getter
    private OsFamily osFamily;

    /**
     * {@inheritDoc}
     *
     * @see java.util.ArrayList#add(int, java.lang.Object)
     */
    @Override
    public void add(final int index, final OperatingSystem element) {
        this.checkOsFamily(element);

        super.add(index, element);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.ArrayList#add(java.lang.Object)
     */
    @Override
    public boolean add(final OperatingSystem e) {
        this.checkOsFamily(e);

        return super.add(e);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.ArrayList#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(final Collection<? extends OperatingSystem> c) {
        c.forEach(this::checkOsFamily);

        return super.addAll(c);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.ArrayList#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends OperatingSystem> c) {
        c.forEach(this::checkOsFamily);

        return super.addAll(index, c);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.ArrayList#set(int, java.lang.Object)
     */
    @Override
    public OperatingSystem set(final int index, final OperatingSystem element) {
        this.checkOsFamily(element);

        return super.set(index, element);
    }

    /**
     * Checks whether the given OS belongs to the set OS family, if any. If the
     * OS family is not set yet, it is set to the family of the given OS.
     *
     * @param operatingSystem
     *            The operating system to check.
     */
    private void checkOsFamily(final OperatingSystem operatingSystem) {
        if (this.osFamily == null) {
            synchronized (this.lockOsFamily) {
                if (this.osFamily == null) {
                    this.osFamily = operatingSystem.getOsFamily();
                }
            }
        } else if (!operatingSystem.getOsFamily().equals(this.osFamily)) {
            throw new IllegalArgumentException("The given OS " + operatingSystem + " does not belong to the OS family "
                    + this.osFamily + "!");
        }
    }
}
