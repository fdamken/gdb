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


	---------------------
	-- Data Population --
	---------------------

-- Pupulates the default well-known brand names.
INSERT IGNORE INTO Brand (
	id,
	name
) VALUES (
	1,
	"AMD"
), (
	2,
	"Intel"
), (
	3,
	"Nvidia"
);
