/***
 * 查询工资
 */
function findwage(){
	debugger;
	var year = $("#wage_year").numberspinner("getValue");
	var month = $("#wage_month").numberspinner("getValue");
	var userName = $("#userName_wage").textbox("getValue");
	var options = $('#wage_vv').datagrid('getPager').data("pagination").options;
	var company_id = company_id_select;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#wage_vv").datagrid("loading");
	$.post(BASE_PATH + "/wageController/findwage.do", {
		bNum : bNum,
		rows : rows,year : year,month:month,userName:userName,company_id:company_id
	}, function(data) {
		try {
			$("#wage_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#wage_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 下载导入模板
 */
function uploadTemplate(){
	debugger;
	$("#uploadTemplate").form('submit', {
		success:function(data){
			debugger;
			
		}
	})
}
/**
 * 上传工资
 */
function uploadWage(){
	debugger;
	$("#uploadWage").form('submit', {
		success:function(data){
			debugger;
			console.log(data);
			var temp = JSON.parse(data);
			$.messager.alert("提示",temp.message);
			findwage();
		}
	})
}
/**
 * 报表
 */
function repoetForm(){
	$("#wage_reportForm_dialog").dialog("open");
	$('#custom_div').show();
	$('#month_div').hide();
	$('#quarter_div').hide();
	$('#year_div').hide();
	$('#state_reportForm').combobox("setValue","custom");
	var newDate = new Date();
	$("#wage_reportForm_bt_year_custom").numberspinner("setValue",newDate.getFullYear());
	$("#wage_reportForm_et_year_custom").numberspinner("setValue",newDate.getFullYear());
	$("#wage_reportForm_year_month").numberspinner("setValue",newDate.getFullYear());
	$("#wage_reportForm_year_quarter").numberspinner("setValue",newDate.getFullYear());
	$("#wage_reportForm_year_year").numberspinner("setValue",newDate.getFullYear());
	
	$("#wage_reportForm_bt_month_custom").numberspinner("setValue",(newDate.getMonth()+1));
	$("#wage_reportForm_et_month_custom").numberspinner("setValue",(newDate.getMonth()+1));
}
/**
 * 发送邮件界面的打开
 */
function sendMail(){
	debugger;
	$('#wage_sendMail_dialog').dialog("open");
	var newDate = new Date();
	$("#wage_year_send").numberspinner("setValue",newDate.getFullYear());
	$("#wage_month_send").numberspinner("setValue",(newDate.getMonth()));
}
/**
 * 发送邮件
 */
function sendMail_OK(){
	$.messager.prompt('请示','请输入操作密码？',function(r){
		debugger;
		if(r==deletePwd){
				var company_id = company_id_select;
				var year = $("#wage_year_send").numberspinner("getValue");
				var month = $("#wage_month_send").numberspinner("getValue");
				$.post(BASE_PATH + "/wageController/sendEmail.do", {
					company_id : company_id,
					year : year,
					month:month
				}, function(data) {
					try {
						$('#wage_sendMail_dialog').dialog("close");
						$.messager.alert("提示",data);
					} catch (e) {
						console.log(e);
					}
					
				});
			}else{
				$.messager.alert("提示","密码错误!");
			}
	})
}
/**
 * 导出工资报表
 */
function submitExport(){
	$("#wage_reportForm_company_id").val(company_id_select);
	$('#exportReport_wage').form('submit', {
		success:function(data){
			debugger;
			
		}
	})
}