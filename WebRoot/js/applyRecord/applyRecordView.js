/***
 * 查询外勤打卡申请记录
 */
function findApply_Record(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var bt = $('#bt_apply_Record').datebox("getValue");
	var et = $('#et_apply_Record').datebox("getValue");
	var options = $('#apply_Record_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#apply_Record_vv").datagrid("loading");
	$.post(BASE_PATH + "/ApplyRecordController/findApply_Record.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
	}, function(data) {
		try {
			$("#apply_Record_vv").datagrid("loaded");
			applyRecordMap.clearOverlays();    //清除地图上所有覆盖物
			var temp = JSON.parse(data);
			$("#apply_Record_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 查询补卡申请记录
 */
function findAppeal_Record(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var bt = $('#bt_appeal_Record').datebox("getValue");
	var et = $('#et_appeal_Record').datebox("getValue");
	var options = $('#appeal_Record_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#appeal_Record_vv").datagrid("loading");
	$.post(BASE_PATH + "/ApplyRecordController/findAppeal_Record.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
	}, function(data) {
		try {
			$("#appeal_Record_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#appeal_Record_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 查询加班申请记录
 */
function findOverTime_Record(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var bt = $('#bt_overTime_Record_apply').datebox("getValue");
	var et = $('#et_overTime_Record_apply').datebox("getValue");
	var options = $('#overTime_Record_vv_apply').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#overTime_Record_vv_apply").datagrid("loading");
	$.post(BASE_PATH + "/ApplyRecordController/findOverTime_Record.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
	}, function(data) {
		try {
			$("#overTime_Record_vv_apply").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#overTime_Record_vv_apply").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 查询假期申请记录
 */
function findvacation_Record(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var bt = $('#bt_vacation_Record_apply').datebox("getValue");
	var et = $('#et_vacation_Record_apply').datebox("getValue");
	var options = $('#vacation_Record_vv_apply').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#vacation_Record_vv_apply").datagrid("loading");
	$.post(BASE_PATH + "/ApplyRecordController/findvacation_Record.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
	}, function(data) {
		try {
			$("#vacation_Record_vv_apply").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#vacation_Record_vv_apply").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}


/**
 * 查询外出申请记录
 */
function findoutGoing_Record(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var bt = $('#bt_outGoing_Record_apply').datebox("getValue");
	var et = $('#et_outGoing_Record_apply').datebox("getValue");
	var options = $('#outGoing_Record_vv_apply').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#outGoing_Record_vv_apply").datagrid("loading");
	$.post(BASE_PATH + "/ApplyRecordController/findoutGoing_Record.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
	}, function(data) {
		try {
			$("#outGoing_Record_vv_apply").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#outGoing_Record_vv_apply").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 查询出差申请记录
 */
function findbusinessTraveI_Record(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var bt = $('#bt_businessTraveI_Record_apply').datebox("getValue");
	var et = $('#et_businessTraveI_Record_apply').datebox("getValue");
	var options = $('#businessTraveI_Record_vv_apply').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#businessTraveI_Record_vv_apply").datagrid("loading");
	$.post(BASE_PATH + "/ApplyRecordController/findbusinessTraveI_Record.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
	}, function(data) {
		try {
			$("#businessTraveI_Record_vv_apply").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#businessTraveI_Record_vv_apply").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}

/**
 * 标记为已读
 */
function markRead(){
	debugger;
	var data = $('#apply_Record_vv').datagrid("getChecked");
	if(data!=null&&data.length>0){
		var obj = [];
		for(var i = 0;i<data.length;i++){
			if(data[i].readNotRead==0){
				obj.push(data[i].id);
			}
			
		}
		if(obj.length>0){
			var jsonObj = JSON.stringify(obj);
			console.log(jsonObj);
			$.post(BASE_PATH + "/ApplyRecordController/markRead.do", {
				jsonObj : jsonObj,
				userId : loginUser.loginName
			}, function(data) {
				try {
					var temp = JSON.parse(data);
					if(temp.result==="success"){
						findApply_Record();
						searchNews();
					}
				} catch (e) {
					console.log(e);
				}
				
			});
		}
		
	}
	$('#apply_Record_vv').datagrid("clearChecked");
}
/**
 * 加班标记为已读
 */
function markReadoverTime(){
	debugger;
	var data = $('#overTime_Record_vv_apply').datagrid("getChecked");
	if(data!=null&&data.length>0){
		var obj = [];
		for(var i = 0;i<data.length;i++){
			if(data[i].readNotRead==0){
				obj.push(data[i].id);
			}
			
		}
		if(obj.length>0){
			var jsonObj = JSON.stringify(obj);
			console.log(jsonObj);
			$.post(BASE_PATH + "/ApplyRecordController/markReadoverTime.do", {
				jsonObj : jsonObj,
				userId : loginUser.loginName
			}, function(data) {
				try {
					var temp = JSON.parse(data);
					if(temp.result==="success"){
						findOverTime_Record();
						searchNews();
					}
				} catch (e) {
					console.log(e);
				}
				
			});
		}
		
	}
	$('#overTime_Record_vv_apply').datagrid("clearChecked");
}

/**
 * 外出标记为已读
 */
function markReadoutGoing(){
	debugger;
	var data = $('#outGoing_Record_vv_apply').datagrid("getChecked");
	if(data!=null&&data.length>0){
		var obj = [];
		for(var i = 0;i<data.length;i++){
			if(data[i].readNotRead==0){
				obj.push(data[i].id);
			}
			
		}
		if(obj.length>0){
			var jsonObj = JSON.stringify(obj);
			console.log(jsonObj);
			$.post(BASE_PATH + "/ApplyRecordController/markReadoutGoing.do", {
				jsonObj : jsonObj,
				userId : loginUser.loginName
			}, function(data) {
				try {
					var temp = JSON.parse(data);
					if(temp.result==="success"){
						findoutGoing_Record();
						searchNews();
					}
				} catch (e) {
					console.log(e);
				}
				
			});
		}
		
	}
	$('#outGoing_Record_vv_apply').datagrid("clearChecked");
}
/**
 * 出差标记为已读
 */
function markReadbusinessTraveI(){
	debugger;
	var data = $('#businessTraveI_Record_vv_apply').datagrid("getChecked");
	if(data!=null&&data.length>0){
		var obj = [];
		for(var i = 0;i<data.length;i++){
			if(data[i].readNotRead==0){
				obj.push(data[i].id);
			}
			
		}
		if(obj.length>0){
			var jsonObj = JSON.stringify(obj);
			console.log(jsonObj);
			$.post(BASE_PATH + "/ApplyRecordController/markReadbusinessTraveI.do", {
				jsonObj : jsonObj,
				userId : loginUser.loginName
			}, function(data) {
				try {
					var temp = JSON.parse(data);
					if(temp.result==="success"){
						findbusinessTraveI_Record();
						searchNews();
					}
				} catch (e) {
					console.log(e);
				}
				
			});
		}
		
	}
	$('#businessTraveI_Record_vv_apply').datagrid("clearChecked");
}

/**
 * 假期标记为已读
 */
function markReadvacation(){
	debugger;
	var data = $('#vacation_Record_vv_apply').datagrid("getChecked");
	if(data!=null&&data.length>0){
		var obj = [];
		for(var i = 0;i<data.length;i++){
			if(data[i].readNotRead==0){
				obj.push(data[i].id);
			}
		}
		if(obj.length>0){
			var jsonObj = JSON.stringify(obj);
			console.log(jsonObj);
			$.post(BASE_PATH + "/ApplyRecordController/markReadvacation.do", {
				jsonObj : jsonObj,
				userId : loginUser.loginName
			}, function(data) {
				try {
					var temp = JSON.parse(data);
					if(temp.result==="success"){
						findvacation_Record();
						searchNews();
					}
				} catch (e) {
					console.log(e);
				}
				
			});
		}
		
	}
	$('#vacation_Record_vv_apply').datagrid("clearChecked");
}
/**
 * 标记失效
 */
function updateApplyEffective(){
	debugger;
	var data = $('#apply_Record_vv').datagrid("getChecked");
	$.messager.prompt('请示','请输入操作密码？',function(r){
		debugger;
		if(r==deletePwd){
			if(data!=null&&data.length>0){
				var obj = [];
				for(var i = 0;i<data.length;i++){
						if(data[i].result_id!=7&&data[i].result_id!=8){
							obj.push(data[i].out_attendance_id);
						}
						
				}
				if(obj.length>0){
					var jsonObj = JSON.stringify(obj);
					console.log(jsonObj);
					$.post(BASE_PATH + "/ApplyRecordController/updateApplyEffective.do", {
						jsonObj : jsonObj,
						userId : loginUser.loginName
					}, function(data) {
						try {
							var temp = JSON.parse(data);
							if(temp.result==="success"){
								findApply_Record();
								searchNews();
							}
						} catch (e) {
							console.log(e);
						}
						
					});
				}
				
			}
			$('#apply_Record_vv').datagrid("clearChecked");
		}else{
			$.messager.alert("提示","密码错误!");
		}
	})
}
/**
 * 补卡审批标记无效
 */
function updateAppealEffective(){
	debugger;
	var data = $('#appeal_Record_vv').datagrid("getChecked");
	$.messager.prompt('请示','请输入操作密码？',function(r){
		debugger;
		if(r==deletePwd){
			if(data!=null&&data.length>0){
				var obj = [];
				for(var i = 0;i<data.length;i++){
					if(data[i].result_id!=7&&data[i].result_id!=8){
						obj.push(data[i].appeal_attendance_id);
					}
				}
				if(obj.length>0){
					var jsonObj = JSON.stringify(obj);
					console.log(jsonObj);
					$.post(BASE_PATH + "/ApplyRecordController/updateAppealEffective.do", {
						jsonObj : jsonObj,
						userId : loginUser.loginName
					}, function(data) {
						try {
							var temp = JSON.parse(data);
							if(temp.result==="success"){
								findApply_Record();
								searchNews();
							}
						} catch (e) {
							console.log(e);
						}
						
					});
				}
				
			}
			$('#appeal_Record_vv').datagrid("clearChecked");
		}else{
			$.messager.alert("提示","密码错误!");
		}
	})

}
/**
 * 加班申请标记无效
 */
function updateOverTimeEffective(){
	debugger;
	var data = $('#overTime_Record_vv_apply').datagrid("getChecked");
	$.messager.prompt('请示','请输入操作密码？',function(r){
		debugger;
		if(r==deletePwd){
			if(data!=null&&data.length>0){
				var obj = [];
				for(var i = 0;i<data.length;i++){
					if(data[i].result_id!=7&&data[i].result_id!=8){
						obj.push(data[i].id);
					}
				}
				if(obj.length>0){
					var jsonObj = JSON.stringify(obj);
					console.log(jsonObj);
					$.post(BASE_PATH + "/ApplyRecordController/updateOverTimeEffective.do", {
						jsonObj : jsonObj,
						userId : loginUser.loginName
					}, function(data) {
						try {
							var temp = JSON.parse(data);
							if(temp.result==="success"){
								findOverTime_Record();
								searchNews();
							}
						} catch (e) {
							console.log(e);
						}
						
					});
				}
				
			}
			$('#overTime_Record_vv_apply').datagrid("clearChecked");
		}else{
			$.messager.alert("提示","密码错误!");
		}
	})
}

/**
 * 外出申请标记无效
 */
function updateoutGoingEffective(){
	debugger;
	var data = $('#outGoing_Record_vv_apply').datagrid("getChecked");
	$.messager.prompt('请示','请输入操作密码？',function(r){
		debugger;
		if(r==deletePwd){
			if(data!=null&&data.length>0){
				var obj = [];
				for(var i = 0;i<data.length;i++){
					if(data[i].result_id!=7&&data[i].result_id!=8){
						obj.push(data[i].id);
					}
				}
				if(obj.length>0){
					var jsonObj = JSON.stringify(obj);
					console.log(jsonObj);
					$.post(BASE_PATH + "/ApplyRecordController/updateoutGoingEffective.do", {
						jsonObj : jsonObj,
						userId : loginUser.loginName
					}, function(data) {
						try {
							var temp = JSON.parse(data);
							if(temp.result==="success"){
								findoutGoing_Record();
								searchNews();
							}
						} catch (e) {
							console.log(e);
						}
						
					});
				}
				
			}
			$('#outGoing_Record_vv_apply').datagrid("clearChecked");
		}else{
			$.messager.alert("提示","密码错误!");
		}
	})
}



/**
 * 出差申请标记无效
 */
function updatebusinessTraveIEffective(){
	debugger;
	var data = $('#businessTraveI_Record_vv_apply').datagrid("getChecked");
	$.messager.prompt('请示','请输入操作密码？',function(r){
		debugger;
		if(r==deletePwd){
			if(data!=null&&data.length>0){
				var obj = [];
				for(var i = 0;i<data.length;i++){
					if(data[i].result_id!=7&&data[i].result_id!=8){
						obj.push(data[i].id);
					}
				}
				if(obj.length>0){
					var jsonObj = JSON.stringify(obj);
					console.log(jsonObj);
					$.post(BASE_PATH + "/ApplyRecordController/updatebusinessTraveIEffective.do", {
						jsonObj : jsonObj,
						userId : loginUser.loginName
					}, function(data) {
						try {
							var temp = JSON.parse(data);
							if(temp.result==="success"){
								findbusinessTraveI_Record();
								searchNews();
							}
						} catch (e) {
							console.log(e);
						}
						
					});
				}
				
			}
			$('#businessTraveI_Record_vv_apply').datagrid("clearChecked");
		}else{
			$.messager.alert("提示","密码错误!");
		}
	})
}
/**
 * 假期申请标记无效
 */
function updatevacationEffective(){
	debugger;
	var data = $('#vacation_Record_vv_apply').datagrid("getChecked");
	$.messager.prompt('请示','请输入操作密码？',function(r){
		debugger;
		if(r==deletePwd){
			if(data!=null&&data.length>0){
				var obj = [];
				for(var i = 0;i<data.length;i++){
					if(data[i].result_id!=7&&data[i].result_id!=8){
						obj.push(data[i].id);
					}
				}
				if(obj.length>0){
					var jsonObj = JSON.stringify(obj);
					console.log(jsonObj);
					$.post(BASE_PATH + "/ApplyRecordController/updatevacationEffective.do", {
						jsonObj : jsonObj,
						userId : loginUser.loginName
					}, function(data) {
						try {
							var temp = JSON.parse(data);
							if(temp.result==="success"){
								findvacation_Record();
								searchNews();
							}
						} catch (e) {
							console.log(e);
						}
						
					});
				}
				
			}
			$('#vacation_Record_vv_apply').datagrid("clearChecked");
		}else{
			$.messager.alert("提示","密码错误!");
		}
	})
}

/**
 * 全部标记已读
 */
function allTagsRead(){
	debugger;
	$.post(BASE_PATH + "/ApplyRecordController/allTagsRead.do", {
		userId : loginUser.loginName
	}, function(data) {
		try {
			var temp = JSON.parse(data);
			if(temp.result==="success"){
				findApply_Record();
				searchNews();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 加班申请标记全部已读
 */
function allTagsReadoverTime(){
	debugger;
	$.post(BASE_PATH + "/ApplyRecordController/allTagsReadoverTime.do", {
		userId : loginUser.loginName
	}, function(data) {
		try {
			var temp = JSON.parse(data);
			if(temp.result==="success"){
				findOverTime_Record();
				searchNews();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 外出申请标记全部已读
 */
function allTagsReadoutGoing(){
	debugger;
	$.post(BASE_PATH + "/ApplyRecordController/allTagsReadoutGoing.do", {
		userId : loginUser.loginName
	}, function(data) {
		try {
			var temp = JSON.parse(data);
			if(temp.result==="success"){
				findoutGoing_Record();
				searchNews();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 出差申请标记全部已读
 */
function allTagsReadbusinessTraveI(){
	debugger;
	$.post(BASE_PATH + "/ApplyRecordController/allTagsReadbusinessTraveI.do", {
		userId : loginUser.loginName
	}, function(data) {
		try {
			var temp = JSON.parse(data);
			if(temp.result==="success"){
				findbusinessTraveI_Record();
				searchNews();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}

/**
 * 假期申请标记全部已读
 */
function allTagsReadvacation(){
	debugger;
	$.post(BASE_PATH + "/ApplyRecordController/allTagsReadvacation.do", {
		userId : loginUser.loginName
	}, function(data) {
		try {
			var temp = JSON.parse(data);
			if(temp.result==="success"){
				findvacation_Record();
				searchNews();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}