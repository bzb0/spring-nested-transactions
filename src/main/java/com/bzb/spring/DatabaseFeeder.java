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

package com.bzb.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFeeder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseFeeder.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseFeeder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertData() {
        LOGGER.info("Started inserting test data.");
        jdbcTemplate.execute("INSERT INTO PROJECT(name,description) VALUES('House Build', 'Building a house')");
        jdbcTemplate.execute("INSERT INTO PROJECT(name,description) VALUES('Buying Groceires', 'Buying Groceries')");
        jdbcTemplate.execute("INSERT INTO PROJECT(name,description) VALUES('Project A', 'Project A')");
        jdbcTemplate.execute("INSERT INTO PROJECT(name,description) VALUES('Project B', 'Project B')");
        LOGGER.info("Successfully inserted test data.");
    }
}
