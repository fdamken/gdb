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
package org.lcmanager.gdb.web.control.config;

import lombok.val;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Configures Spring Security.
 *
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    /**
     * {@inheritDoc}
     *
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf()
                .csrfTokenRepository(this.csrfTokenRepository())
            .and()
                .authorizeRequests()
                    .antMatchers("/a/**", "/m/**")
                        .hasRole("ADMIN")
                    .antMatchers("/u/**")
                        .hasRole("USER")
                    .antMatchers("/o/**")
                        .permitAll()
            .and()
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login/failed")
                    .defaultSuccessUrl("/u")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
            .and()
                .logout()
                    .logoutSuccessUrl("/login/out")
                    .permitAll();
    }

    /**
     * Creates a {@link CsrfTokenRepository} that sets the token name to
     * <code>_csrf</code>.
     *
     * @return The {@link CsrfTokenRepository}.
     */
    private CsrfTokenRepository csrfTokenRepository() {
        val csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfTokenRepository.setSessionAttributeName("_csrf");
        return csrfTokenRepository;
    }
}
