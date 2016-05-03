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
import org.lcmanager.gdb.service.data.util.OsFamily;

/**
 * Handles the MyBatis mapping of the type '{@link OsFamily}' to make a mapping
 * between the OS family ID and the database possible instead of relying on the
 * name.
 *
 */
@MappedJdbcTypes(value = JdbcType.INTEGER,
includeNullJdbcType = true)
public class OsFamilyHandler extends BaseTypeHandler<OsFamily> {
    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement,
     *      int, java.lang.Object, org.apache.ibatis.type.JdbcType)
     */
    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final OsFamily parameter, final JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet,
     *      java.lang.String)
     */
    @Override
    public OsFamily getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return this.determineOsFamily(rs.getInt(columnName));
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet,
     *      int)
     */
    @Override
    public OsFamily getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return this.determineOsFamily(rs.getInt(columnIndex));
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.CallableStatement,
     *      int)
     */
    @Override
    public OsFamily getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return this.determineOsFamily(cs.getInt(columnIndex));
    }

    /**
     * Determines the {@link OsFamily} that has the given index. If the given
     * index is greater than zero it is passed to {@link OsFamily#getById(int)}.
     * Otherwise, <code>null</code> is returned.
     *
     * @param osFamilyIndex
     *            The index of the OS family to determine.
     * @return The OS family, if any. Otherwise, if the index is smaller than
     *         zero or equal to zero, <code>null</code>.
     */
    private OsFamily determineOsFamily(final int osFamilyIndex) {
        return osFamilyIndex > 0 ? OsFamily.getById(osFamilyIndex) : null;
    }
}
