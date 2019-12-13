<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div id="toolbar_loggerInfo" style="height:35px">
			开始时间:<input id="bt_loggerInfo" class="easyui-datebox" editable="false" style="width:150px;height:26px">
			结束时间:<input id="et_loggerInfo" class="easyui-datebox" editable="false" style="width:150px;height:26px">
			<input id="userId_loggerInfo" class="easyui-textbox" data-options="label:'操作ID:',labelWidth:'60px'">
			<input id="remarks_loggerInfo" class="easyui-textbox" data-options="label:'关键字:',labelWidth:'60px'">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="findLoggerInfo()">查询</a>
	</div>
	<table id="loggerInfo_vv" class="easyui-datagrid"
		data-options="
			fit:true,
			striped:true,
			fitColumns: true,
			singleSelect:true,
			rownumbers:true,
			toolbar: '#toolbar_loggerInfo',
			pagination:true, //包含分页
			pageSize:50">
		<thead>
			<tr>
				<th data-options="field:'user_id',width:10,align:'center'">操作人ID</th>
				<th data-options="field:'user_name',width:10,align:'center'">操作人</th>
				<th data-options="field:'company_name',width:20,align:'center',formatter:addressType">公司</th>
				<th data-options="field:'createTime',width:20,align:'center',formatter:addressType">操作时间</th>
				<th data-options="field:'remarks',width:100,align:'center',formatter:addressType">操作内容</th>
				<th data-options="field:'ip',width:20,align:'center'">IP</th>
			</tr>
		</thead>
	</table>
	<div id="logger_details" class="easyui-dialog" title="日志详情" style="width:600px;height: 400px;padding:10px;" data-options="modal:true,closed:true">
		<div style="margin: 10px;">
			<input data-options="label:'操作人ID'" class="easyui-textbox" id="user_id_details" style="width: 500px;">
		</div>
		<div style="margin: 10px;">
			<input data-options="label:'操作人'" class="easyui-textbox" id="user_name_details" style="width: 500px;">
		</div>
		<div style="margin: 10px;">
			<input data-options="label:'公司'" class="easyui-textbox" id="company_name_details" style="width: 500px;">
		</div>
		<div style="margin: 10px;">
			<input data-options="label:'操作时间'" class="easyui-textbox" id="createTime_details" style="width: 500px;">
		</div>
		<div style="margin: 10px;">
			<input data-options="label:'操作内容',multiline:true" class="easyui-textbox" id="remarks_details" style="width: 500px;height: 120px">
		</div>
		<div style="margin: 10px;">
			<input data-options="label:'IP'" class="easyui-textbox" id="ip_details" style="width: 500px;">
		</div>
	</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/logger/loggerView.js"></script>
    <script type="text/javascript">
		var loggerInfo_init = true;
		$.parser.onComplete = function(res) {
			if (loggerInfo_init) {
				loggerInfo_init = false;
					var newDate = new Date();
					$('#bt_loggerInfo').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
					$('#et_loggerInfo').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
					findLoggerInfo();
					$("#loggerInfo_vv").datagrid({
						onDblClickRow : function(index,res){
							debugger;
							$('#logger_details').dialog("open");
							$('#user_id_details').textbox("setValue",res.user_id);
							$('#user_name_details').textbox("setValue",res.user_name);
							$('#company_name_details').textbox("setValue",res.company_name);
							$('#createTime_details').textbox("setValue",res.createTime);
							$('#remarks_details').textbox("setValue",res.remarks);
							$('#ip_details').textbox("setValue",res.ip);
						}
					});
					var pager_page_loggerInfo = $('#loggerInfo_vv').datagrid("getPager");
					if (pager_page_loggerInfo) {
						$(pager_page_loggerInfo).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findLoggerInfo();
							}
						});
					}
			}
		};
    </script>
    
</body>

</html>