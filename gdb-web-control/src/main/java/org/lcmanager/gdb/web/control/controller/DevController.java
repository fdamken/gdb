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

import java.util.Map;

import org.lcmanager.gdb.base.CollectionUtil;
import org.lcmanager.gdb.service.data.model.User;
import org.lcmanager.gdb.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Provides a utility page for developers.
 * 
 * <p>
 * <b> NOTE: This controller is only activated when running in development
 * mode. </b>
 * </p>
 *
 */
@Controller
@RequestMapping("/dev")
@Profile("dev")
public class DevController {
    /**
     * The {@link PasswordEncoder}.
     * 
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * The {@link UserService}.
     * 
     */
    @Autowired
    private UserService userService;

    /**
     * Handles reqests on <code>/</code>.
     *
     * @return The template to render.
     */
    @RequestMapping
    public String handle() {
        return "dev";
    }

    /**
     * Encodes the given password and returns it.
     *
     * @param password
     *            The password to encode.
     * @return The encoded password in plain text.
     */
    @RequestMapping(path = "/encoder",
                    method = RequestMethod.POST,
                    produces = "text/plain")
    @ResponseBody
    public String encodePassword(@RequestBody final String password) {
        return this.passwordEncoder.encode(password);
    }

    /**
     * Creates a user.
     *
     * @param body
     *            A map that contains the obligatory properties
     *            <code>username</code> and <code>password</code>.
     * @return A status message.
     */
    @RequestMapping(path = "/user",
                    method = RequestMethod.PUT)
    public ResponseEntity<Map<String, String>> createUser(@RequestBody final Map<String, String> body) {
        final String username = body.get("username");
        final String displayName = body.get("displayName");
        final String password = body.get("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(CollectionUtil.<String, String>createMap(map -> {
                map.put("msg", "Both username and password must be given!");
            }));
        }
        if (this.userService.userExists(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CollectionUtil.<String, String>createMap(map -> {
                map.put("msg", "The user already exists!");
            }));
        }

        final User user = new User();
        user.setUsername(username);
        if (displayName != null && !displayName.isEmpty()) {
            user.setDisplayName(displayName);
        }
        this.userService.createUser(user, password);

        return ResponseEntity.ok().body(CollectionUtil.<String, String>createMap(map -> {
            map.put("msg", "User created!");
        }));
    }
}
