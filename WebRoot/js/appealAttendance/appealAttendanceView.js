/**
 * 补卡审批查询需要审批
 */
function findappealAttendance(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#appealAttendance_userId').textbox("getValue");
	var bt = $('#bt_appealAttendance').datebox("getValue");
	var et = $('#et_appealAttendance').datebox("getValue");
	var options = $('#appealAttendance_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#appealAttendance_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findappealAttendance.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#appealAttendance_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#appealAttendance_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 审批界面
 */
function auditappealAttendance(){
	var data = $("#appealAttendance_vv").datagrid("getSelected");
	if(data!=null){
		$('#auditappealAttendance_dialog').dialog("open");
		$('#auditappealAttendance_user_name').textbox("setValue",data.user_name);
		$('#auditappealAttendance_appeal_time').textbox("setValue",data.appeal_time);
		
		$('#auditappealAttendance_result').textbox("setValue",data.result);
		$('#auditappealAttendance_remarks').textbox("setValue",data.remarks);
		var radios = document.getElementsByName("auditappealAttendance_radio");
	    for(var i=0;i<radios.length;i++){
	    	radios[i].checked = false;
	    }
	    $('#auditappealAttendance_audit_remarks').textbox("setValue","");
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}
/**
 * 审批提交
 */
function submitAuditappealAttendance(){
	var data = $("#appealAttendance_vv").datagrid("getSelected");
	if(data!=null){
		var appealAttendanceRecordAudit_id = data.appealAttendanceRecordAudit_id;//审批人信息ID
		var appeal_attendance_id = data.appeal_attendance_id;//外勤打卡详情表ID
		var result_id = data.result_id;//申请状态
		var audit_status = 0;//审批结果，1通过，2不通过
		var radios = document.getElementsByName("auditappealAttendance_radio");
	    for(var i=0;i<radios.length;i++){
	    	if(radios[i].checked){
	    		audit_status = radios[i].value;
	    		break;
	    	}
	    }
	    var audit_resmarks = $('#auditappealAttendance_audit_remarks').textbox("getValue");//审批备注
	    $.post(BASE_PATH + "/OutAttendanceController/submitAuditappealAttendance.do", {
	    	appealAttendanceRecordAudit_id : appealAttendanceRecordAudit_id,
	    	appeal_attendance_id : appeal_attendance_id,
	    	id:data.id,
	    	result_id : result_id,
	    	audit_status : audit_status,
	    	audit_resmarks : audit_resmarks
		}, function(data) {
			debugger;
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					$('#auditappealAttendance_dialog').dialog("close");
					findappealAttendance();
					//searchNews();
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
 * 查询补卡审批过的记录
 */
function findauditappealAttendance_historical_records(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#appealAttendance_userId_historical_records').textbox("getValue");
	var bt = $('#bt_appealAttendance_historical_records').datebox("getValue");
	var et = $('#et_appealAttendance_historical_records').datebox("getValue");
	var options = $('#appealAttendance_historical_records_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#appealAttendance_historical_records_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findauditappealAttendance_historical_records.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#appealAttendance_historical_records_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#appealAttendance_historical_records_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}