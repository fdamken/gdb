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
package org.lcmanager.gdb.service.impl.data.mapper;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.lcmanager.gdb.service.data.model.BaseModel;

/**
 * This is the base interface for any MyBatis mapper and is also used to detect
 * the base package for the mapper scan.
 * 
 * <p>
 * <b> NOTE: Mapped properties are not saved into the database when inserting
 * them. </b>
 * </p>
 *
 * @param <T>
 *            The type of the model this mapper is for.
 * @param <I>
 *            The type of the ID that is used.
 */
public interface BaseMapper<T extends BaseModel<I>, I extends Serializable> {
    /**
     * Inserts the given model.
     *
     * <p>
     * <b> If the model already exists, an error occurs. </b>
     * </p>
     *
     * @param obj
     *            The model to insert.
     * @return The count of the inserted rows.
     */
    int insert(@Param("obj") T obj);

    /**
     * Updates the given model.
     * 
     * <p>
     * <b> If the model does not exist, nothing happens. </b>
     * </p>
     * <p>
     * <b> If the model does not provide any mutable properties, nothing
     * happens. </b>
     * </p>
     *
     * @param obj
     *            The model to update.
     * @return The count of the updated rows.
     */
    int update(@Param("obj") T obj);

    /**
     * Saves the given model. That is inserting it if it does not exist and
     * updating it if it does exist.
     *
     * <p>
     * <b> If the model already exists but it does not provide any mutable
     * properties, nothing happens. </b>
     * </p>
     * 
     * @param obj
     *            The model to save.
     * @return The count of the inserted or updates rows.
     */
    int save(@Param("obj") T obj);

    /**
     * Deletes the model with the given ID.
     * 
     * <p>
     * <b> If no model with the given ID exists, nothing happens. </b>
     * </p>
     * private Integer id;
     *
     * @param id
     *            The ID of the model to delete.
     * @return The count of the deleted rows.
     */
    int delete(@Param("id") I id);

    /**
     * Checks whether a model with the given ID exists.
     *
     * @param id
     *            The ID to check.
     * @return Whether a model with such an ID exists or not.
     */
    boolean exists(@Param("id") I id);

    /**
     * Finds the model with the given ID.
     *
     * @param id
     *            The ID of the model to find.
     * @return The model, if any.
     */
    T findById(@Param("id") I id);

    /**
     * Finds all models.
     *
     * @return All models.
     */
    List<T> find();
}
