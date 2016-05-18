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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controls the login and logout page.
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    /**
     * Handles a login request.
     *
     * @return The template.
     */
    @RequestMapping
    public String login() {
        return "login";
    }

    /**
     * Handles a failed login attempt
     *
     * @param model
     *            The model.
     * @return The template.
     */
    @RequestMapping("/failed")
    public String loginFailed(final Model model) {
        model.addAttribute("failed", true);
        return "login";
    }

    /**
     * Handles a successful logout.
     *
     * @param model
     *            The model.
     * @return The template.
     */
    @RequestMapping("/out")
    public String loggedOut(final Model model) {
        model.addAttribute("out", true);
        return "redirect:/o/logout";
    }
}
