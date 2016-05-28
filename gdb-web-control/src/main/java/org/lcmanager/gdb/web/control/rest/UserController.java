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
package org.lcmanager.gdb.web.control.rest;

import java.util.HashMap;
import java.util.Map;

import org.lcmanager.gdb.base.CommonConstants;
import org.lcmanager.gdb.service.data.model.User;
import org.lcmanager.gdb.service.user.UserService;
import org.lcmanager.gdb.web.control.status.GenerateStatus;
import org.lcmanager.gdb.web.control.util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the REST API to interact with users on <code>/api/user</code>.
 *
 */
@RestController
@RequestMapping("/api/user")
@GenerateStatus
@CacheConfig(cacheNames = "user-controller")
public class UserController {
    /**
     * The {@link UserService}.
     * 
     */
    @Autowired
    private UserService userService;

    /**
     * Lists all users.
     *
     * @return All users.
     */
    @RequestMapping
    @Cacheable
    public ResponseEntity<ResourceSupport> handleFind() {
        final Resources<User> resource = ControllerUtil.createResources(this.userService.retrieveUsers());

        resource.add(
                ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).handleFind()).withSelfRel());

        return ControllerUtil.createResponse(resource);
    }

    /**
     * Creates a user.
     *
     * @param body
     *            A map that contains the obligatory properties
     *            <code>username</code> and <code>password</code>.
     * @return A status message.
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Map<String, String>> createUser(@RequestBody final Map<String, String> body) {
        final String username = body.get("username");
        final String displayName = body.get("displayName");
        final String password = body.get("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(ControllerUtil.<String, String>createMap(map -> {
                map.put("msg", "Both username and password must be given!");
            }));
        }
        if (this.userService.userExists(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ControllerUtil.<String, String>createMap(map -> {
                map.put("msg", "The user already exists!");
            }));
        }

        final User user = new User();
        user.setUsername(username);
        if (displayName != null && !displayName.isEmpty()) {
            user.setDisplayName(displayName);
        }
        this.userService.createUser(user, password);
        this.userService.assignAuthority(CommonConstants.Role.USER_AUTHORITY, user);

        return ResponseEntity.ok().body(ControllerUtil.<String, String>createMap(map -> {
            map.put("msg", "User created!");
        }));
    }

    /**
     * Finds the user with the given ID.
     *
     * @param id
     *            The ID to search for.
     * @return The found user.
     */
    @RequestMapping("/{id}")
    @Cacheable
    public ResponseEntity<ResourceSupport> handleFindById(@PathVariable final int id) {
        final Resource<User> resource = ControllerUtil.createResource(this.userService.retrieveUser(id));

        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).handleFindById(id))
                .withSelfRel());

        return ControllerUtil.createResponse(resource);
    }

    /**
     * Checks whether the given user name exists.
     *
     * @param username
     *            The user name to check.
     * @return Whether the user name exists or not.
     */
    @RequestMapping("/exists/{username}")
    @Cacheable
    public ResponseEntity<ResourceSupport> handleExists(@PathVariable final String username) {
        final Map<String, Object> data = new HashMap<>();
        data.put("exists", this.userService.userExists(username));
        final Resource<Map<String, Object>> resource = ControllerUtil.createResource(data);

        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class).handleExists(username))
                .withSelfRel());

        return ControllerUtil.createResponse(resource);
    }
}
