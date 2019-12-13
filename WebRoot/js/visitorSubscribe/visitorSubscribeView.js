/**
 * 查询预约审批
 */
function findvisitorSubscribe(){
	var bt = $("#bt_visitorSubscribe").datebox("getValue");
	var et = $("#et_visitorSubscribe").datebox("getValue");
	var user_id = loginUser.loginName;//当前登入人的user_id
	var options = $('#visitorSubscribe_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#visitorSubscribe_vv").datagrid("loading");
	$.post(BASE_PATH + "/VisitorSubscribeController/findvisitorSubscribe.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id
	}, function(data) {
		try {
			$("#visitorSubscribe_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#visitorSubscribe_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 审批按钮
 */
function auditvisitorSubscribe(){
	var data = $("#visitorSubscribe_vv").datagrid("getSelected");
	if(data!=null){
		$('#auditvisitorSubscribe_dialog').dialog("open");
		$('#auditvisitorSubscribe_user_name').textbox("setValue",data.user_name);
		var status = "";
		if(data.status==0){
			status = "未审核";
		}else if(data.status==1){
			status = "审核通过";
		}else if(data.status==2){
			status = "审核不通过";
		}
		$('#auditvisitorSubscribe_result').textbox("setValue",status);
		var radios = document.getElementsByName("auditvisitorSubscribe_radio");
	    for(var i=0;i<radios.length;i++){
	    	radios[i].checked = false;
	    }
	    $('#auditvisitorSubscribe_audit_remarks').textbox("setValue","");
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}
/**
 * 提交审批结果
 */
function submitAuditvisitorSubscribe(){
	var data = $("#visitorSubscribe_vv").datagrid("getSelected");
	if(data!=null){
		var id = data.id;//预约Id
		var audit_status = 0;//审批结果，1通过，2不通过
		var radios = document.getElementsByName("auditvisitorSubscribe_radio");
	    for(var i=0;i<radios.length;i++){
	    	if(radios[i].checked){
	    		audit_status = radios[i].value;
	    		break;
	    	}
	    }
	    var audit_resmarks = $('#auditvisitorSubscribe_audit_remarks').textbox("getValue");//审批备注
	    $.post(BASE_PATH + "/VisitorSubscribeController/submitAuditvisitorSubscribe.do", {
	    	id : id,
	    	audit_status : audit_status,
	    	audit_resmarks:audit_resmarks
		}, function(data) {
			debugger;
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					$('#auditvisitorSubscribe_dialog').dialog("close");
					findvisitorSubscribe();
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
