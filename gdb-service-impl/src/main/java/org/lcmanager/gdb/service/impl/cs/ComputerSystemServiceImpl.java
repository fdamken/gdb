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
package org.lcmanager.gdb.service.impl.cs;

import java.util.SortedSet;
import java.util.TreeSet;

import org.lcmanager.gdb.service.cs.ComputerSystemService;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.User;
import org.lcmanager.gdb.service.impl.data.mapper.ComputerSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ComputerSystemService}.
 *
 */
@Service
@CacheConfig(cacheNames = "computer-system-service")
public class ComputerSystemServiceImpl implements ComputerSystemService {
    /**
     * The {@link ComputerSystemMapper}.
     * 
     */
    @Autowired
    private ComputerSystemMapper computerSystemMapper;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.cs.ComputerSystemService#retrievePrimaryComputerSystem(org.lcmanager.gdb.service.data.model.User)
     */
    @Override
    @Cacheable
    public ComputerSystem retrievePrimaryComputerSystem(final User user) {
        return this.computerSystemMapper.findPrimaryByUser(user.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.cs.ComputerSystemService#retrieveComputerSystems(org.lcmanager.gdb.service.data.model.User)
     */
    @Override
    @Cacheable
    public SortedSet<ComputerSystem> retrieveComputerSystems(final User user) {
        return new TreeSet<>(this.computerSystemMapper.findByUser(user.getId()));
    }
}
