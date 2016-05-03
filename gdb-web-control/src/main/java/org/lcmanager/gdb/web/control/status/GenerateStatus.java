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
package org.lcmanager.gdb.web.control.status;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a controller method or a controller class to generate a status based on
 * the result of any method. When annotating a class with this annotation, it is
 * applied to all request mappings in that class. If it is applied to a method
 * only, only that method is targeted.
 * 
 * <p>
 * Notice that all methods that should be intercepted must be tagged with
 * {@link org.springframework.web.bind.annotation.RequestMapping
 * &#064;RequestMapping} and must return a
 * {@link org.springframework.http.ResponseEntity}. If the response entity
 * contains a {@link org.springframework.hateoas.Resource}, the content of the
 * resource is passed to the {@link StatusGenerator}.
 * </p>
 *
 * <br>
 * <p>
 * Therefore you may use this annotation on class level: <code>
 * <pre>
 * {@link org.springframework.stereotype.Controller &#064;Controller}
 * {@link org.springframework.web.bind.annotation.RequestMapping &#064;RequestMapping("/hello")}
 * {@link GenerateStatus &#064;GenerateStatus}
 * public class HelloWorldController {
 *     {@link org.springframework.web.bind.annotation.RequestMapping &#064;RequestMapping}
 *     public {@link org.springframework.http.ResponseEntity ResponseEntity&lt;String&gt;} handle() {
 *         return "Hello, World!";
 *     }
 * 
 *     {@link org.springframework.web.bind.annotation.RequestMapping &#064;RequestMapping("/&#123;name&#125;")}
 *     public {@link org.springframework.http.ResponseEntity ResponseEntity&lt;String&gt;} handleHello({@link org.springframework.web.bind.annotation.PathVariable &#064;PathVariable} final {@link String} name) {
 *         return "Hello, " + name + "!";
 *     }
 * }
 * </pre>
 * </code> The code above intercepts both the method <code>handle()</code> and
 * <code>handle({@link String java.lang.String})</code> and generates a status
 * for them.
 * </p>
 *
 * <br>
 * <p>
 * Otherwise you can use the annotation on method level: <code>
 * <pre>
 * {@link org.springframework.stereotype.Controller &#064;Controller}
 * {@link org.springframework.web.bind.annotation.RequestMapping &#064;RequestMapping("/hello")}
 * public class HelloWorldController {
 *     {@link org.springframework.web.bind.annotation.RequestMapping &#064;RequestMapping}
 *     {@link GenerateStatus &#064;GenerateStatus}
 *     public {@link org.springframework.http.ResponseEntity ResponseEntity&lt;String&gt;} handle() {
 *         return "Hello, World!";
 *     }
 * 
 *     {@link org.springframework.web.bind.annotation.RequestMapping &#064;RequestMapping("/&#123;name&#125;")}
 *     public {@link org.springframework.http.ResponseEntity ResponseEntity&lt;String&gt;} handleHello({@link org.springframework.web.bind.annotation.PathVariable &#064;PathVariable} final {@link String} name) {
 *         return "Hello, " + name + "!";
 *     }
 * }
 * </pre>
 * </code> The code above intercepts the method <code>handle()</code> only.
 * </p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface GenerateStatus {
    /**
     * Sets the {@link StatusGenerator} that should be used to generate the
     * status. When a method is intercepted and a status is about to be
     * generated, an instance of this class is created by Spring and asked for
     * the status to set.
     * 
     * @return The {@link StatusGenerator}. Defaults to
     *         {@link DefaultStatusGenerator}.
     */
    Class<? extends StatusGenerator<?>> value() default DefaultStatusGenerator.class;
}
