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
package org.lcmanager.gdb.web.control.controller;

import org.lcmanager.gdb.base.CommonConstants;
import org.lcmanager.gdb.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles all requests on <code>/o</code> and renders the correct templates.
 * 
 */
@Controller
@RequestMapping("/o")
public class OtherController {
    /**
     * The {@link UserService}.
     * 
     */
    @Autowired
    private UserService userService;

    /**
     * Handles all requests on <code>/o</code>.
     * 
     * @param model
     *            The data model that is passed to the template.
     * @return The template to render.
     */
    @RequestMapping
    public String handleRequest(final Model model) {
        return this.handle(model);
    }

    /**
     * Handles all requests on <code>/o/logout</code> which are the requests
     * that where redirected by Spring Security.
     *
     * @param model
     *            The data model that is passed to the template.
     * @return The template to render.
     */
    @RequestMapping("/logout")
    public String handleLogoutSuccessfullRequest(final Model model) {
        model.addAttribute("out", true);

        return this.handle(model);
    }

    /**
     * Handles any requests to the main-model by adding the required attributes
     * to the given model.
     *
     * @param model
     *            The model to add the required attributes to.
     * @return The template to render.
     */
    private String handle(final Model model) {
        model.addAttribute("principal", this.userService.retrieveUser());
        model.addAttribute("is_user", this.userService.hasAuthority(CommonConstants.Role.USER_ROLE));
        model.addAttribute("is_admin", this.userService.hasAuthority(CommonConstants.Role.ADMIN_ROLE));

        return "main";
    }
}
