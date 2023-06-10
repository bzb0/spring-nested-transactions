/*
 * Copyright 2017-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bzb.spring.service;

import com.bzb.spring.entity.Project;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProjectService {

    private final static Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private NestedProjectService nestedProjectService;

    public ProjectService(NestedProjectService nestedProjectService) {
        this.nestedProjectService = nestedProjectService;
    }

    @Transactional
    public Optional<Project> findById(int id) {
        return nestedProjectService.findById(id);
    }

    @Transactional
    public int createProject(String name, String description) {
        return nestedProjectService.createProject(name, description);
    }

    @Transactional
    public void createProjects(String... projectNames) {
        nestedProjectService.createProjects(projectNames);
    }

    @Transactional
    public List<Project> findAllProjects() {
        return nestedProjectService.findAllProjects();
    }

    @Transactional
    public Project updateProject(int id, String name, String description) {
        try {
            return nestedProjectService.updateProject(id, name, description);
        } catch (Exception e) {
            logger.error("Something went wrong", e);
            var txId = nestedProjectService.createProject("TX_NOT_ROLLBACKED",
                    "The current transaction wasn't rollbacked so we can perform error handling");
            return nestedProjectService.findById(txId).get();
        }
    }
}