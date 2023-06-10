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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class NestedProjectService {

    private final static Logger logger = LoggerFactory.getLogger(NestedProjectService.class);

    private final JdbcTemplate jdbcTemplate;

    public NestedProjectService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Optional<Project> findById(int id) {
        try {
            var project = jdbcTemplate.queryForObject(
                    "select id, name, description from project where id = ?",
                    (rs, rowNum) -> new Project(rs.getLong("id"), rs.getString("name"), rs.getString("description")), id);
            return Optional.ofNullable(project);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public int createProject(String name, String description) {
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    var preparedStmt = connection.prepareStatement(
                            "insert into project(name, description) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
                    preparedStmt.setString(1, name);
                    preparedStmt.setString(2, description);
                    return preparedStmt;
                }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Transactional
    public void createProjects(String... projectNames) {
        logger.info("Creating a list of projects");
        for (String projectName : projectNames) {
            jdbcTemplate.update("insert into project(name) values (?)", projectName);
        }
    }

    @Transactional
    public Project updateProject(int id, String name, String project) {
        if ("INVALID".equals(name)) {
            logger.warn("Detected invalid project. Throwing exception...");
            throw new RuntimeException("Invalid value for project name");
        }

        int updateCount = jdbcTemplate.update("update project SET name = ?, description = ? where id = ?", name, project, id);
        if (updateCount != 1) {
            throw new RuntimeException("Project update failed.");
        }
        return jdbcTemplate.queryForObject("select * from project where id = ?",
                (rs, rowNum) -> new Project(rs.getLong("id"), rs.getString("name"), rs.getString("description")), id);
    }

    @Transactional
    public List<Project> findAllProjects() {
        return jdbcTemplate.query("select * from project",
                (rs, rowNum) -> new Project(rs.getLong("id"), rs.getString("name"), rs.getString("description")));
    }
}
