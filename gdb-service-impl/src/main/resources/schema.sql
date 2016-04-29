---
-- #%L
-- Game Database
-- %%
-- Copyright (C) 2016 - 2016 LCManager Group
-- %%
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- 
--      http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
-- #L%
---


-- This SQL file is executed by Spring when it initializes the JDBC
-- connections. It should be used to create necesasary tables and so on.



-- Create table 'User'.
CREATE TABLE IF NOT EXISTS User (
	username VARCHAR(50) NOT NULL,
	password VARCHAR(100) NOT NULL,
	enabled BOOLEAN DEFAULT TRUE NOT NULL,
	PRIMARY KEY (username)
) CHARACTER SET utf8 COLLATE utf8_bin ENGINE InnoDB;

-- Create table 'User_Authority'.
CREATE TABLE IF NOT EXISTS User_Authority (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL,
	PRIMARY KEY (username, authority),
	CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES User(username)
) CHARACTER SET utf8 COLLATE utf8_bin ENGINE InnoDB;
