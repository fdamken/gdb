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

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

/**
 * Handles the MyBatis mapping between {@link URL uniform resource locators} and
 * strings by parsing the strings.
 *
 */
@MappedJdbcTypes(value = JdbcType.VARCHAR,
                 includeNullJdbcType = true)
public class UrlHandler extends BaseTypeHandler<URL> {
    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement,
     *      int, java.lang.Object, org.apache.ibatis.type.JdbcType)
     */
    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final URL parameter, final JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.toExternalForm());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet,
     *      java.lang.String)
     */
    @Override
    public URL getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return this.determineUrl(rs.getString(columnName));
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet,
     *      int)
     */
    @Override
    public URL getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return this.determineUrl(rs.getString(columnIndex));
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.CallableStatement,
     *      int)
     */
    @Override
    public URL getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return this.determineUrl(cs.getString(columnIndex));
    }

    /**
     * Parses the given URL.
     *
     * @param url
     *            The URL to parse.
     * @return The parsed URL if possible. If the passed URL is
     *         <code>null</code>, <code>null</code>.
     * @throws SQLException
     *             If the parsing of the URL fails doe to a malformed URL.
     */
    private URL determineUrl(final String url) throws SQLException {
        try {
            return url == null ? null : new URL(url);
        } catch (final MalformedURLException cause) {
            throw new SQLException("Failed to map URL " + url + "!", cause);
        }
    }
}
