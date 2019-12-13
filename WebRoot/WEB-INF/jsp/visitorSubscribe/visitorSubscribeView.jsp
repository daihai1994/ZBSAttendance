<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
<div class="easyui-tabs"  data-options="plain:true" fit="true"> 
	<div title="需要审批" style="padding:0px;" >
		<div id="toolbar_visitorSubscribe" style="height:35px">
				开始时间:<input id="bt_visitorSubscribe" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_visitorSubscribe" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findvisitorSubscribe()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="auditvisitorSubscribe()">审批</a>
		</div>
		<table id="visitorSubscribe_vv" class="easyui-datagrid"
			data-options="
				fit:true,
				striped:true,
				fitColumns: true,
				singleSelect:true,
				rownumbers:true,
				toolbar: '#toolbar_visitorSubscribe',
				pagination:true, //包含分页
				pageSize:50">
			<thead>
				<tr>
					<th data-options="field:'user_name',width:20,align:'center',formatter:addressType">申请人</th>
					<th data-options="field:'createTime',width:40,align:'center',formatter:addressType">申请时间</th>
					<th data-options="field:'theme',width:20,align:'center',formatter:addressType">预约主题</th>
					<th data-options="field:'breaks',width:20,align:'center',formatter:addressType">预约信息</th>
					<th data-options="field:'startDate',width:20,align:'center',formatter:addressType">有效开始日期</th>
					<th data-options="field:'endDate',width:20,align:'center',formatter:addressType">有效结束日期</th>
					<th data-options="field:'startTime',width:20,align:'center',formatter:addressType">有效开始时间段</th>
					<th data-options="field:'endTime',width:20,align:'center',formatter:addressType">有效结束时间段</th>
					<th data-options="field:'faceUrl',width:20,align:'center',formatter:addressType">人脸图片</th>
					<th data-options="field:'status',width:20,align:'center',formatter:auditVisitorStatus">申请状态</th>
					<th data-options="field:'auditBreaks',width:20,align:'center',formatter:auditVisitorStatus">审批信息</th>
					<th data-options="field:'carryingNumber',width:20,align:'center',formatter:addressType">携带人数</th>
					<th data-options="field:'effectiveDay',width:20,align:'center',formatter:addressType">有效日</th>
				</tr>
			</thead>
		</table>
		<div id="auditvisitorSubscribe_dialog" class="easyui-dialog" title="审批"  style="width:470px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
			<div style="margin: 10px;">
				<input data-options="label:'申请人:',disabled:true"  class="easyui-textbox" id="auditvisitorSubscribe_user_name" style="width: 350px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'申请状态:',disabled:true"  class="easyui-textbox" id="auditvisitorSubscribe_result" style="width: 350px;">
			</div>
			<div style="margin: 10px;">
				<span style="display: inline;margin-right: 50px">审批结果: </span>
				<span style="display: inline;"><input id="auditvisitorSubscribe_radio_1" type="radio" name="auditvisitorSubscribe_radio" value="1">通过
               		<input id="auditvisitorSubscribe_radio_2" type="radio" name="auditvisitorSubscribe_radio" value="2">不通过</span>
               	<span style="color: red;display: inline;">*必填</span>
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'审批备注:',multiline:true"  class="easyui-textbox" id="auditvisitorSubscribe_audit_remarks" style="width: 350px;height:60px">
				<span style="color: red;display: inline;">*必填</span>
			</div>
			<div style="margin: 10px;text-align: center;">
				<a  onclick="submitAuditvisitorSubscribe()" class="easyui-linkbutton" style="width: 100px;">保存</a>
				<a  onclick="$('#auditvisitorSubscribe_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
			</div>
		</div>
	</div>
</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/visitorSubscribe/visitorSubscribeView.js"></script>
    <script type="text/javascript">
		var visitorSubscribeAudit = true;
		$.parser.onComplete = function(res) {
			if (visitorSubscribeAudit) {
			debugger;
				visitorSubscribeAudit = false;
				var newDate = new Date();
				$('#bt_visitorSubscribe').datebox('setValue',newDate.getFullYear()-1+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				$('#et_visitorSubscribe').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				$('#bt_visitorSubscribe_historical_records').datebox('setValue',newDate.getFullYear()-1+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				$('#et_visitorSubscribe_historical_records').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				
				findvisitorSubscribe();
				//findauditvisitorSubscribe_historical_records();
			
					$('#visitorSubscribe_vv').datagrid({
						onSelect:function(index,value){
							debugger;
						}
					})
					$('#visitorSubscribe_historical_records_vv').datagrid({
						onSelect:function(index,value){
							debugger;
						}
					})
				
					var pager_page_visitorSubscribe_vv = $('#visitorSubscribe_vv').datagrid("getPager");
					if (pager_page_visitorSubscribe_vv) {
						$(pager_page_visitorSubscribe_vv).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findvisitorSubscribe();
							}
						});
					}
					
					
			}
		};
    </script>
    
</body>

</html>