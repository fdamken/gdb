/*
 * #%L
 * Game Database Control
 * %%
 * Copyright (C) 2016 LCManager Group
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

window.Dialog = {
	error : function(msg) {
		BootstrapDialog.show({
			type : BootstrapDialog.TYPE_WARNING,
			title : 'Error',
			message : msg
		});
	},
	ajaxError : function() {
		Dialog.error('Failed to retrieve data from remote service!');
	},
	info : function(msg) {
		BootstrapDialog.show({
			type : BootstrapDialog.TYPE_INFO,
			message : msg
		});
	},
	success : function(msg) {
		BootstrapDialog.show({
			type : BootstrapDialog.TYPE_SUCCESS,
			message : msg
		});
	},
	confirm : function(msg, danger, onOk, onCancel) {
		BootstrapDialog.confirm({
			type : danger ? BootstrapDialog.TYPE_DANGER : BootstrapDialog.TYPE_PRIMARY,
			message : msg,
			btnOKClass : danger ? 'btn-danger' : 'btn-primary',
			callback : function(result) {
				if (result) {
					(onOk || function() { })();
				} else {
					(onCancel || function() { })();
				}
			}
		});
	}
};
