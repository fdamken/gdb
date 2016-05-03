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
package org.lcmanager.gdb.service.impl.data.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.lcmanager.gdb.service.data.util.RequirementType;

/**
 * Handles the MyBatis mapping of the type '{@link RequirementType}' to make a
 * mapping between the requirement type ID and the database possible instead of
 * relying on the name.
 *
 */
@MappedJdbcTypes(value = JdbcType.INTEGER,
                 includeNullJdbcType = true)
public class RequirementTypeHandler extends BaseTypeHandler<RequirementType> {
    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement,
     *      int, java.lang.Object, org.apache.ibatis.type.JdbcType)
     */
    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final RequirementType parameter,
            final JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet,
     *      java.lang.String)
     */
    @Override
    public RequirementType getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return this.determineRequirementType(rs.getInt(columnName));
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet,
     *      int)
     */
    @Override
    public RequirementType getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return this.determineRequirementType(rs.getInt(columnIndex));
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.CallableStatement,
     *      int)
     */
    @Override
    public RequirementType getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return this.determineRequirementType(cs.getInt(columnIndex));
    }

    /**
     * Determines the {@link RequirementType} that has the given index. If the
     * given index is greater than zero it is passed to
     * {@link RequirementType#getById(int)}. Otherwise, <code>null</code> is
     * returned.
     *
     * @param requirementTypeIndex
     *            The index of the requirement type to determine.
     * @return The requirement type, if any. Otherwise, if the index is smaller
     *         than zero or equal to zero, <code>null</code>.
     */
    private RequirementType determineRequirementType(final int requirementTypeIndex) {
        return requirementTypeIndex > 0 ? RequirementType.getById(requirementTypeIndex) : null;
    }
}
