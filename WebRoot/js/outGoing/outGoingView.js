/**
 * 外出审批查询需要审批
 */
function findoutGoing(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#outGoing_userId').textbox("getValue");
	var bt = $('#bt_outGoing').datebox("getValue");
	var et = $('#et_outGoing').datebox("getValue");
	var options = $('#outGoing_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#outGoing_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findoutGoing.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#outGoing_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#outGoing_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}

/**
 * 外出审批界面
 */
function auditoutGoing(){
	var data = $("#outGoing_vv").datagrid("getSelected");
	if(data!=null){
		$('#auditoutGoing_dialog').dialog("open");
		$('#auditoutGoing_user_name').textbox("setValue",data.user_name);
		$('#auditoutGoing_address').textbox("setValue",data.address);
		$('#auditoutGoing_start_time').textbox("setValue",data.start_time);
		$('#auditoutGoing_stop_time').textbox("setValue",data.stop_time);
		$('#auditoutGoing_hour').textbox("setValue",data.hour);
		$('#auditoutGoing_result').textbox("setValue",data.result);
		$('#auditoutGoing_remarks').textbox("setValue",data.remarks);
		var radios = document.getElementsByName("auditoutGoing_radio");
	    for(var i=0;i<radios.length;i++){
	    	radios[i].checked = false;
	    }
	    $('#auditoutGoing_audit_remarks').textbox("setValue","");
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}

/**
 * 外出审批提交
 */
function submitAuditoutGoing(){
	var data = $("#outGoing_vv").datagrid("getSelected");
	if(data!=null){
		var audit_id = data.audit_id;//审批人详情信息ID
		var id = data.id;//外出详情信息ID
		var result_id = data.result_id;//申请状态
		var audit_status = 0;//审批结果，1通过，2不通过
		var radios = document.getElementsByName("auditoutGoing_radio");
	    for(var i=0;i<radios.length;i++){
	    	if(radios[i].checked){
	    		audit_status = radios[i].value;
	    		break;
	    	}
	    }
	    var audit_resmarks = $('#auditoutGoing_audit_remarks').textbox("getValue");//审批备注
	    $.post(BASE_PATH + "/OutAttendanceController/submitAuditoutGoing.do", {
	    	audit_id:audit_id,
	    	id : id,
	    	result_id : result_id,
	    	audit_status : audit_status,
	    	audit_resmarks : audit_resmarks
		}, function(data) {
			debugger;
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					$('#auditoutGoing_dialog').dialog("close");
					findoutGoing();
					searchNews();
				}
			} catch (e) {
				console.log(e);
			}
			
		});
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}
/**
 * 外出审批记录的查询
 */
function findauditoutGoing_historical_records(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#outGoing_userId_historical_records').textbox("getValue");
	var bt = $('#bt_outGoing_historical_records').datebox("getValue");
	var et = $('#et_outGoing_historical_records').datebox("getValue");
	var options = $('#outGoing_historical_records_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#outGoing_historical_records_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findauditoutGoing_historical_records.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#outGoing_historical_records_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#outGoing_historical_records_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}