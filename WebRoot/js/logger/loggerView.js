/**
 * 日志查询
 */
function findLoggerInfo(){
	var company_id = company_id_select;
	var bt = $('#bt_loggerInfo').datebox("getValue");
	var et = $('#et_loggerInfo').datebox("getValue");
	var user_id = $('#userId_loggerInfo').textbox("getValue");
	var remarks = $("#remarks_loggerInfo").textbox("getValue");
	var options = $('#loggerInfo_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#loggerInfo_vv").datagrid("loading");
	$.post(BASE_PATH + "/LoggerController/findLoggerInfo.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		remarks : remarks,
		company_id : company_id
	}, function(data) {
		try {
			$("#loggerInfo_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#loggerInfo_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}