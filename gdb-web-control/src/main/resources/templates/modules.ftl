<#import "modules/login_form.ftl" as module_loginForm>

<#macro login_form>
	<@module_loginForm.renderScripts />
	<@module_loginForm.render />
</#macro>
