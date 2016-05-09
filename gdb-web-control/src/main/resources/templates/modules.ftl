<#--
 #%L
 Game Database Control
 %%
 Copyright (C) 2016 LCManager Group
 %%
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 #L%
-->

<#import "modules/login.ftl" as module_login>
<#import "modules/navigation.ftl" as module_navigation>
<#import "modules/search.ftl" as module_search>

<#macro login_form>
	<@module_login.renderScripts />
	<@module_login.render />
</#macro>

<#macro navigation>
	<@module_navigation.renderScripts />
	<@module_navigation.render />
</#macro> 

<#macro search>
	<@module_search.renderScripts />
	<@module_search.render />
</#macro>
