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

<#macro renderScripts>
	<link rel="stylesheet" href="${context}/css/modules/details.css">
	<script src="${context}/js/modules/details.js"></script>
</#macro>
<#macro render>
	<div gdb-layout="{ id : 'details', title : 'Game Details', side : 'right' }" class="panel panel-default overlay-parent">
		<div id="details-content-wrapper" class="panel-body">
			<div ng-controller="detailsController" id="details-content">
				<pre>
{{ gameDetails | json }}
				</pre>
			</div>
		</div>
	</div>
</#macro>
