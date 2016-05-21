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

<#macro nothing msg title="null">
	<div ng-show="state === 'state_nothing'">
		<div class="alert alert-info">
			<#if title != "null">
				<strong>${title}</strong>
			</#if>
			${msg}
		</div>
	</div>
</#macro>

<#macro loading msg="null">
	<div ng-show="state === 'state_loading'">
		<div class="alert alert-info">
			<div class="spinner">
				<div class="double-bounce1">&nbsp;</div>
				<div class="double-bounce2">&nbsp;</div>
			</div>
			<span>
				<#if msg == "null">
					Loading …
				<#else>
					${msg}
				</#if>
			</span>
		</div>
	</div>
</#macro>

<#macro loaded_start>
	<div ng-show="state === 'state_loaded'">
</#macro>
<#macro loaded_end>
	</div>
</#macro>
