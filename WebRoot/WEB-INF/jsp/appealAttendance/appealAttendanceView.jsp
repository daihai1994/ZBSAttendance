<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
<div class="easyui-tabs"  data-options="plain:true" fit="true"> 
	<div title="需要审批" style="padding:0px;" >
		<div id="toolbar_appealAttendance" style="height:35px">
				开始时间:<input id="bt_appealAttendance" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_appealAttendance" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<input id="appealAttendance_userId" class="easyui-textbox"   data-options="label:'申请人:'">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findappealAttendance()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="auditappealAttendance()">审批</a>
		</div>
		<table id="appealAttendance_vv" class="easyui-datagrid"
			data-options="
				fit:true,
				striped:true,
				fitColumns: true,
				singleSelect:true,
				rownumbers:true,
				toolbar: '#toolbar_appealAttendance',
				pagination:true, //包含分页
				pageSize:50">
			<thead>
				<tr>
					<th data-options="field:'user_name',width:20,align:'center',formatter:addressType">申请人</th>
					<th data-options="field:'attendance_time',width:30,align:'center',formatter:addressType">申请时间</th>
					<th data-options="field:'type_name',width:20,align:'center',formatter:addressType">补卡类型</th>
					<th data-options="field:'appeal_time',width:20,align:'center',formatter:addressType">补卡时间</th>
					<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
					<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
					<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_Appeal">图片</th>
					<th data-options="field:'_',width:20,align:'center',formatter:audit_record_Appeal">审批记录</th>
				</tr>
			</thead>
		</table>
		<div id="auditappealAttendance_dialog" class="easyui-dialog" title="审批"  style="width:470px;height: 450px;padding:10px;" data-options="modal:true,closed:true">
			<div style="margin: 10px;">
				<input data-options="label:'申请人:',disabled:true"  class="easyui-textbox" id="auditappealAttendance_user_name" style="width: 350px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'补卡时间:',disabled:true"  class="easyui-textbox" id="auditappealAttendance_appeal_time" style="width: 350px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'申请状态:',disabled:true"  class="easyui-textbox" id="auditappealAttendance_result" style="width: 350px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'申请备注:',disabled:true,multiline:true"  class="easyui-textbox" id="auditappealAttendance_remarks" style="width: 350px;height:60px">
			</div>
			<div style="margin: 10px;">
				<span style="display: inline;margin-right: 50px">审批结果: </span>
				<span style="display: inline;"><input id="auditappealAttendance_radio_1" type="radio" name="auditappealAttendance_radio" value="2">通过
               		<input id="auditappealAttendance_radio_2" type="radio" name="auditappealAttendance_radio" value="3">不通过</span>
               	<span style="color: red;display: inline;">*必填</span>
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'审批备注:',multiline:true"  class="easyui-textbox" id="auditappealAttendance_audit_remarks" style="width: 350px;height:60px">
				<span style="color: red;display: inline;">*必填</span>
			</div>
			<div style="margin: 10px;text-align: center;">
				<a  onclick="submitAuditappealAttendance()" class="easyui-linkbutton" style="width: 100px;">保存</a>
				<a  onclick="$('#auditappealAttendance_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
			</div>
		</div>
	</div>
	<div title="审批记录" style="padding:0px;" >
		<div id="toolbar_appealAttendance_historical_records" style="height:35px">
				开始时间:<input id="bt_appealAttendance_historical_records" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_appealAttendance_historical_records" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<input id="appealAttendance_userId_historical_records" class="easyui-textbox"   data-options="label:'申请人:'">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="findauditappealAttendance_historical_records()">查看处理过程</a>
		</div>
		<table id="appealAttendance_historical_records_vv" class="easyui-datagrid"
			data-options="
				fit:true,
				striped:true,
				fitColumns: true,
				singleSelect:true,
				rownumbers:true,
				toolbar: '#toolbar_appealAttendance_historical_records',
				pagination:true, //包含分页
				pageSize:50">
			<thead>
				<tr>
					<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
					<th data-options="field:'attendance_time',width:20,align:'center',formatter:addressType">申请时间</th>
					<th data-options="field:'type_name',width:20,align:'center',formatter:addressType">补卡类型</th>
					<th data-options="field:'appeal_time',width:20,align:'center',formatter:addressType">补卡时间</th>
					<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
					<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
					<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_Appeal">图片</th>
					<th data-options="field:'audit_status',width:20,align:'center',formatter:auditStatus">审批结果</th>
					<th data-options="field:'audit_remarks',width:50,align:'center',formatter:addressType">审批备注</th>
					<th data-options="field:'audit_time',width:20,align:'center',formatter:addressType">处理时间</th>
					<th data-options="field:'_',width:20,align:'center',formatter:audit_record_Appeal">审批记录</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/appealAttendance/appealAttendanceView.js"></script>
    <script type="text/javascript">
		var appealAttendanceAudit = true;
		$.parser.onComplete = function(res) {
			if (appealAttendanceAudit) {
			debugger;
				appealAttendanceAudit = false;
				var newDate = new Date();
				$('#bt_appealAttendance').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
				$('#et_appealAttendance').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				$('#bt_appealAttendance_historical_records').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
				$('#et_appealAttendance_historical_records').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				
				findappealAttendance();
				findauditappealAttendance_historical_records();
			
					$('#appealAttendance_vv').datagrid({
						onSelect:function(index,value){
							debugger;
						}
					})
					$('#appealAttendance_historical_records_vv').datagrid({
						onSelect:function(index,value){
							debugger;
						}
					})
				
					var pager_page_appealAttendance_vv = $('#appealAttendance_vv').datagrid("getPager");
					if (pager_page_appealAttendance_vv) {
						$(pager_page_appealAttendance_vv).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findappealAttendance();
							}
						});
					}
					
					var pager_page_appealAttendance_historical_records_vv = $('#appealAttendance_historical_records_vv').datagrid("getPager");
					if (pager_page_appealAttendance_historical_records_vv) {
						$(pager_page_appealAttendance_historical_records_vv).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findauditappealAttendance_historical_records();
							}
						});
					}
			}
		};
    </script>
    
</body>

</html>