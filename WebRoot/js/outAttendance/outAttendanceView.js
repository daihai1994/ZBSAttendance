/***
 * 查询外勤打卡需要审批
 */
function findOutAttendance(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#outAttendance_userId').textbox("getValue");
	var bt = $('#bt_outAttendance').datebox("getValue");
	var et = $('#et_outAttendance').datebox("getValue");
	var options = $('#outAttendance_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#outAttendance_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findOutAttendance.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#outAttendance_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#outAttendance_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 打开外勤打卡审批页面
 */
function auditOutAttendance(){
	var data = $("#outAttendance_vv").datagrid("getSelected");
	if(data!=null){
		$('#auditOutAttendance_dialog').dialog("open");
		$('#auditOutAttendance_user_name').textbox("setValue",data.user_name);
		$('#auditOutAttendance_result').textbox("setValue",data.result);
		$('#auditOutAttendance_remarks').textbox("setValue",data.remarks);
		$('#auditOutAttendance_attendance_address').textbox("setValue",data.attendance_address);
		var radios = document.getElementsByName("auditOutAttendance_radio");
	    for(var i=0;i<radios.length;i++){
	    	radios[i].checked = false;
	    }
	    $('#auditOutAttendance_audit_remarks').textbox("setValue","");
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}

/***
 * 外勤打卡审批提交
 */
function submitAuditOutAttendance(){
	var data = $("#outAttendance_vv").datagrid("getSelected");
	if(data!=null){
		var outAttendanceRecordAudit_id = data.outAttendanceRecordAudit_id;//审批人信息ID
		var out_attendance_id = data.out_attendance_id;//外勤打卡详情表ID
		var result_id = data.result_id;//申请状态
		var audit_status = 0;//审批结果，1通过，2不通过
		var radios = document.getElementsByName("auditOutAttendance_radio");
	    for(var i=0;i<radios.length;i++){
	    	if(radios[i].checked){
	    		audit_status = radios[i].value;
	    		break;
	    	}
	    }
	    var audit_resmarks = $('#auditOutAttendance_audit_remarks').textbox("getValue");//审批备注
	    $.post(BASE_PATH + "/OutAttendanceController/submitAuditOutAttendance.do", {
	    	outAttendanceRecordAudit_id : outAttendanceRecordAudit_id,
	    	out_attendance_id : out_attendance_id,
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
					$('#auditOutAttendance_dialog').dialog("close");
					findOutAttendance();
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
/***
 * 查询审批记录
 */
function findauditOutAttendance_historical_records(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#outAttendance_userId_historical_records').textbox("getValue");
	var bt = $('#bt_outAttendance_historical_records').datebox("getValue");
	var et = $('#et_outAttendance_historical_records').datebox("getValue");
	var options = $('#outAttendance_historical_records_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#outAttendance_historical_records_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findauditOutAttendance_historical_records.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#outAttendance_historical_records_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#outAttendance_historical_records_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}