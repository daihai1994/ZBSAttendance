/**
 * 假期审批查询需要审批
 */
function findvacation(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#vacation_userId').textbox("getValue");
	var bt = $('#bt_vacation').datebox("getValue");
	var et = $('#et_vacation').datebox("getValue");
	var options = $('#vacation_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#vacation_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findvacation.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#vacation_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#vacation_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}

/**
 * 假期审批界面
 */
function auditvacation(){
	var data = $("#vacation_vv").datagrid("getSelected");
	if(data!=null){
		$('#auditvacation_dialog').dialog("open");
		$('#auditvacation_user_name').textbox("setValue",data.user_name);
		$('#auditvacation_type').textbox("setValue",data.vacation_type_name);
		$('#auditvacation_start_time').textbox("setValue",data.start_time);
		$('#auditvacation_stop_time').textbox("setValue",data.stop_time);
		$('#auditvacation_day').textbox("setValue",data.day);
		$('#auditvacation_hour').textbox("setValue",data.hour);
		$('#auditvacation_result').textbox("setValue",data.result);
		$('#auditvacation_remarks').textbox("setValue",data.remarks);
		var radios = document.getElementsByName("auditvacation_radio");
	    for(var i=0;i<radios.length;i++){
	    	radios[i].checked = false;
	    }
	    $('#auditvacation_audit_remarks').textbox("setValue","");
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}
/**
 * 假期审批提交
 */
function submitAuditvacation(){
	var data = $("#vacation_vv").datagrid("getSelected");
	if(data!=null){
		var audit_id = data.audit_id;//审批人详情信息ID
		var id = data.id;//假期详情信息ID
		var result_id = data.result_id;//申请状态
		var audit_status = 0;//审批结果，1通过，2不通过
		var radios = document.getElementsByName("auditvacation_radio");
	    for(var i=0;i<radios.length;i++){
	    	if(radios[i].checked){
	    		audit_status = radios[i].value;
	    		break;
	    	}
	    }
	    var audit_resmarks = $('#auditvacation_audit_remarks').textbox("getValue");//审批备注
	    $.post(BASE_PATH + "/OutAttendanceController/submitAuditvacation.do", {
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
					$('#auditvacation_dialog').dialog("close");
					findvacation();
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
 * 假期审批记录的查询
 */
function findauditvacation_historical_records(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#vacation_userId_historical_records').textbox("getValue");
	var bt = $('#bt_vacation_historical_records').datebox("getValue");
	var et = $('#et_vacation_historical_records').datebox("getValue");
	var options = $('#vacation_historical_records_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#vacation_historical_records_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findauditvacation_historical_records.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#vacation_historical_records_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#vacation_historical_records_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}