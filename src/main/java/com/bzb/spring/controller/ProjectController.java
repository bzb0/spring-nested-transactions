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

package com.bzb.spring.controller;

import com.bzb.spring.dto.Project;
import com.bzb.spring.service.ProjectService;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/")
    public int createProject(@RequestBody Project project) {
        return projectService.createProject(project.name(), project.description());
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Integer id, @RequestBody Project project) {
        return Optional.of(projectService.updateProject(id, project.name(), project.description()))
                .map(p -> new Project(p.id(), p.name(), p.description()))
                .orElseThrow(() -> new RuntimeException("Error while updating project"));
    }

    @GetMapping("/{id}")
    public Project findProjectById(@PathVariable Integer id) {
        return projectService.findById(id)
                .map(p -> new Project(p.id(), p.name(), p.description()))
                .orElse(new Project(-1L, "NOT FOUND", "NOT FOUND"));
    }
}
