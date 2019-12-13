<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div id="toolbar_wage" style="height:80px;">
		年:<input class="easyui-numberspinner" id="wage_year"   style="width:90px;height:26px;">
		月:<input class="easyui-numberspinner" id="wage_month"   style="width:90px;height:26px;">
		<input id="userName_wage" class="easyui-textbox" data-options="label:'员工:',labelWidth:'60px'">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onclick="findwage()">查询</a>
		<c:if test="${user.role!=null&&user.role==1}">
			<a  onclick="uploadTemplate()" class="easyui-linkbutton" style="width: 100px;">导出模板</a>
			<a  onclick="repoetForm()" class="easyui-linkbutton" style="width: 100px;">报表</a>
			<form id="uploadTemplate" action="${pageContext.request.contextPath}/wageController/uploadTemplate.do"  >
			</form>
			<form id="uploadWage" action="${pageContext.request.contextPath}/wageController/uploadWage.do" method="post" enctype="multipart/form-data"  >
				<input style="width: 140px;" id="excelFileWage" name="excelFileWage" style="margin-right:10px" 
					class="easyui-filebox" data-options="prompt:'导入工资',buttonText: '添加文件'" >
					<a  onclick="uploadWage()" class="easyui-linkbutton" style="width: 100px;">导入工资</a>
					<a  onclick="sendMail()" class="easyui-linkbutton" style="width: 100px;">发送邮件</a>
			</form>
		</c:if>
	</div>
	<table id="wage_vv" class="easyui-datagrid"
		data-options="
			fit:true,
			striped:true,
			fitColumns: true,
			singleSelect:true,
			rownumbers:true,
			toolbar: '#toolbar_wage',
			nowrap:false,
			pagination:true, //包含分页
			pageSize:50">
		<thead>
			<tr>
				<th data-options="field:'user_name',width:20,align:'center'">姓名</th>
				<th data-options="field:'year',width:20,align:'center'">年</th>
				<th data-options="field:'month',width:20,align:'center'">月</th>
				<th data-options="field:'wage_basic',width:30,align:'center'">基本工资</th>
				<th data-options="field:'attendance_days',width:20,align:'center'">出勤天数</th>
				<th data-options="field:'subsidy',width:20,align:'center'">补助(餐补)</th>
				<th data-options="field:'overtime_hours',width:20,align:'center'">加班时间</th>
				<th data-options="field:'social_security_fund_persional',width:20,align:'center'">个人承担社保</th>
				<th data-options="field:'social_security_fund_company',width:20,align:'center'">公司承担社保</th>
				<th data-options="field:'individual_income_tax',width:20,align:'center'">个人所得税</th>
				<th data-options="field:'wage_real',width:20,align:'center'">实发工资</th>
				<th data-options="field:'reimbursement',width:20,align:'center'">报销</th>
				<th data-options="field:'achievement',width:20,align:'center'">业绩</th>
			</tr>
		</thead>
	</table>
	<div id="wage_reportForm_dialog" class="easyui-dialog" title="报表" style="width:600px;height: 300px;padding:10px;" data-options="modal:true,closed:true">
		<form id="exportReport_wage" action="${pageContext.request.contextPath}/wageController/exportReport.do"  >
			<div style="margin: 10px;">
				<input id="wage_reportForm_company_id" name = "wage_reportForm_company_id" type="hidden">
				<select id="state_reportForm" name = "state_reportForm" class="easyui-combobox" editable="false"  
					label="报表类型" style="width: 500px;">
					<option value="custom">自定义</option>
					<option value="month" >月</option>
					<option value="quarter">季度</option>
					<option value="year">年</option>
				</select>
			</div>
			<div style="margin: 10px;" id="custom_div">
				<div style="margin: 10px;">
					开始时间:(年)<input class="easyui-numberspinner" data-options="editable:false" id="wage_reportForm_bt_year_custom"
					 name = "wage_reportForm_bt_year_custom"   style="width:185px;">
					~(月)<input class="easyui-numberspinner" data-options="editable:false" min="1" max = "12" 
					id="wage_reportForm_bt_month_custom" name = "wage_reportForm_bt_month_custom"   style="width:185px;">
				</div>
				<div style="margin: 10px;">
					结束时间:(年)<input class="easyui-numberspinner" id="wage_reportForm_et_year_custom"
					 name = "wage_reportForm_et_year_custom"   style="width:185px;">
					~(月)<input class="easyui-numberspinner" data-options="editable:false" min="1" max = "12" 
					name = "wage_reportForm_et_month_custom" id="wage_reportForm_et_month_custom"   style="width:185px;">
				</div>
			</div>
			<div style="margin: 10px;" id="month_div">
				<div style="margin: 10px;">
					<input class="easyui-numberspinner" data-options="editable:false"
					name = "wage_reportForm_year_month" id="wage_reportForm_year_month" label="年:"   style="width:490px;">
				</div>
				<div style="margin: 10px;">
					<select id="wage_reportForm_month_month"
					name = "wage_reportForm_month_month" class="easyui-combobox" 
					data-options="editable:false" name="wage_reportForm_month_month"
					label="月份:"   style="width:490px;">
						<option value="1" >一月</option>
						<option value="2">二月</option>
						<option value="3">三月</option>
						<option value="4">四月</option>
						<option value="5">五月</option>
						<option value="6">六月</option>
						<option value="7">七月</option>
						<option value="8">八月</option>
						<option value="9">九月</option>
						<option value="10">十月</option>
						<option value="11">十一月</option>
						<option value="12">十二月</option>
						<option value="all">全部</option>
					</select>
				</div>
			</div>
			<div style="margin: 10px;" id="quarter_div">
				<div style="margin: 10px;">
					<input class="easyui-numberspinner" data-options="editable:false" id="wage_reportForm_year_quarter" 
					name = "wage_reportForm_year_quarter" label="年:"  style="width:490px;">
				</div>
				<div style="margin: 10px;">
					<select id="wage_reportForm_month_quarter" class="easyui-combobox" name = "wage_reportForm_month_quarter"
					data-options="editable:false" name="wage_reportForm_month_quarter"
					label="月份:"   style="width:490px;">
						<option value="1" >第一季度</option>
						<option value="2">第二季度</option>
						<option value="3">第三季度</option>
						<option value="4">第四季度</option>
						<option value="all">全部</option>
					</select>
				</div>
			</div>
			<div style="margin: 10px;" id="year_div">
				<div style="margin: 10px;">
					<input class="easyui-numberspinner" data-options="editable:false" id="wage_reportForm_year_year" 
					name = "wage_reportForm_year_year" label="年:"  style="width:490px;">
				</div>
			</div>
			<div style="margin: 40px;text-align: center;">
				<a  onclick="submitExport()" class="easyui-linkbutton" style="width: 100px;">导出</a>
				<a  onclick="$('#wage_reportForm_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
			</div>
		</form>
	</div>
	
	<div id="wage_sendMail_dialog" class="easyui-dialog" title=发送邮件 style="width:400px;height: 300px;padding:10px;" data-options="modal:true,closed:true">
		<div style="margin: 10px;">
			<input class="easyui-numberspinner" id="wage_year_send" label="年:"   style="width:350px;height:26px;">
		</div>
		<div style="margin: 10px;">
			<input class="easyui-numberspinner" id="wage_month_send" label="月:"   style="width:350px;height:26px;">
		</div>
		<div style="margin: 40px;text-align: center;">
			<a  onclick="sendMail_OK()" class="easyui-linkbutton" style="width: 100px;">发送</a>
			<a  onclick="$('#wage_sendMail_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
		</div>
	</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/wage/wageView.js"></script>
    <script type="text/javascript">
		var wageInfo_init = true;
		$.parser.onComplete = function(res) {
			if (wageInfo_init) {
				wageInfo_init = false;
				$('#state_reportForm').combobox({
					onChange:function(n,o){
						debugger;
						switch (n) {
							case "custom":
								$('#custom_div').show();
								$('#month_div').hide();
								$('#quarter_div').hide();
								$('#year_div').hide();
								break;
							case "month":
								$('#custom_div').hide();
								$('#month_div').show();
								$('#quarter_div').hide();
								$('#year_div').hide();
								break;
							case "quarter":
								$('#custom_div').hide();
								$('#month_div').hide();
								$('#quarter_div').show();
								$('#year_div').hide();
								break;
							case "year":
								$('#custom_div').hide();
								$('#month_div').hide();
								$('#quarter_div').hide();
								$('#year_div').show();
								break;
							default:
								break;
							}
					}
				})
					var newDate = new Date();
					$("#wage_year").numberspinner("setValue",newDate.getFullYear());
					$("#wage_month").numberspinner("setValue",(newDate.getMonth()));
					findwage();
					if(loginUser.role!=null&&loginUser.role==1){
						$('#userName_wage').textbox("enable");
					}else{
						$('#userName_wage').textbox("disable");
						$('#userName_wage').textbox("setValue",loginUser.loginName);
					}
					findwage();
					var pager_page_wage = $('#wage_vv').datagrid("getPager");
					if (pager_page_wage) {
						$(pager_page_wage).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findwage();
							}
						});
					}
			}
		};
    </script>
    
</body>

</html>