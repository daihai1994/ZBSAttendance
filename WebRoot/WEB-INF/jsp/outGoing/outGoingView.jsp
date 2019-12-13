<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
<div class="easyui-tabs"  data-options="plain:true" fit="true"> 
	<div title="需要审批" style="padding:0px;" >
		<div id="toolbar_outGoing" style="height:35px">
				开始时间:<input id="bt_outGoing" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_outGoing" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<input id="outGoing_userId" class="easyui-textbox"   data-options="label:'申请人:'">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findoutGoing()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="auditoutGoing()">审批</a>
		</div>
		<table id="outGoing_vv" class="easyui-datagrid"
			data-options="
				fit:true,
				striped:true,
				fitColumns: true,
				singleSelect:true,
				rownumbers:true,
				toolbar: '#toolbar_outGoing',
				pagination:true, //包含分页
				pageSize:50">
			<thead>
				<tr>
					<th data-options="field:'user_name',width:20,align:'center',formatter:addressType">申请人</th>
					<th data-options="field:'createTime',width:20,align:'center',formatter:addressType">申请时间</th>
					<th data-options="field:'address',width:20,align:'center',formatter:addressType">外出地址</th>
					<th data-options="field:'start_time',width:20,align:'center',formatter:addressType">开始时间</th>
					<th data-options="field:'stop_time',width:20,align:'center',formatter:addressType">结束时间</th>
					<th data-options="field:'hour',width:20,align:'center',formatter:addressType">小时</th>
					<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
					<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
					<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_outGoing">图片</th>
					<th data-options="field:'_',width:20,align:'center',formatter:audit_record_outGoing">审批记录</th>
				</tr>
			</thead>
		</table>
		<div id="auditoutGoing_dialog" class="easyui-dialog" title="审批"  style="width:470px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
			<div style="margin: 10px;">
				<input data-options="label:'申请人:',disabled:true"  class="easyui-textbox" id="auditoutGoing_user_name" style="width: 350px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'申请状态:',disabled:true"  class="easyui-textbox" id="auditoutGoing_result" style="width: 350px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'外出地址:',disabled:true"  class="easyui-textbox" id="auditoutGoing_address" style="width: 350px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'开始时间:',disabled:true"  class="easyui-textbox" id="auditoutGoing_start_time" style="width: 350px">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'结束时间:',disabled:true"  class="easyui-textbox" id="auditoutGoing_stop_time" style="width: 350px">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'小时:',disabled:true"  class="easyui-textbox" id="auditoutGoing_hour" style="width: 350px">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'申请备注:',disabled:true,multiline:true"  class="easyui-textbox" id="auditoutGoing_remarks" style="width: 350px;height:60px">
			</div>
			<div style="margin: 10px;">
				<span style="display: inline;margin-right: 50px">审批结果: </span>
				<span style="display: inline;"><input id="auditoutGoing_radio_1" type="radio" name="auditoutGoing_radio" value="2">通过
               		<input id="auditoutGoing_radio_2" type="radio" name="auditoutGoing_radio" value="3">不通过</span>
               	<span style="color: red;display: inline;">*必填</span>
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'审批备注:',multiline:true"  class="easyui-textbox" id="auditoutGoing_audit_remarks" style="width: 350px;height:60px">
				<span style="color: red;display: inline;">*必填</span>
			</div>
			<div style="margin: 10px;text-align: center;">
				<a  onclick="submitAuditoutGoing()" class="easyui-linkbutton" style="width: 100px;">保存</a>
				<a  onclick="$('#auditoutGoing_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
			</div>
		</div>
	</div>
	<div title="审批记录" style="padding:0px;" >
		<div id="toolbar_outGoing_historical_records" style="height:35px">
				开始时间:<input id="bt_outGoing_historical_records" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_outGoing_historical_records" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<input id="outGoing_userId_historical_records" class="easyui-textbox"   data-options="label:'申请人:'">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="findauditoutGoing_historical_records()">查看处理过程</a>
		</div>
		<table id="outGoing_historical_records_vv" class="easyui-datagrid"
			data-options="
				fit:true,
				striped:true,
				fitColumns: true,
				singleSelect:true,
				rownumbers:true,
				toolbar: '#toolbar_outGoing_historical_records',
				pagination:true, //包含分页
				pageSize:50">
			<thead>
				<tr>
					<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
					<th data-options="field:'createTime',width:20,align:'center',formatter:addressType">申请时间</th>
					<th data-options="field:'address',width:20,align:'center',formatter:addressType">外出地址</th>
					<th data-options="field:'start_time',width:20,align:'center',formatter:addressType">开始时间</th>
					<th data-options="field:'stop_time',width:20,align:'center',formatter:addressType">结束时间</th>
					<th data-options="field:'hour',width:20,align:'center',formatter:addressType">小时</th>
					<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
					<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
					<th data-options="field:'audit_status',width:20,align:'center',formatter:auditStatus">审批结果</th>
					<th data-options="field:'audit_remarks',width:50,align:'center',formatter:addressType">审批备注</th>
					<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_outGoing">图片</th>
					<th data-options="field:'_',width:20,align:'center',formatter:audit_record_outGoing">审批记录</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/outGoing/outGoingView.js"></script>
    <script type="text/javascript">
		var outGoingAudit = true;
		$.parser.onComplete = function(res) {
			if (outGoingAudit) {
			debugger;
				outGoingAudit = false;
				var newDate = new Date();
				$('#bt_outGoing').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
				$('#et_outGoing').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				$('#bt_outGoing_historical_records').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
				$('#et_outGoing_historical_records').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				
				findoutGoing();
				findauditoutGoing_historical_records();
			
					$('#outGoing_vv').datagrid({
						onSelect:function(index,value){
							debugger;
						}
					})
					$('#outGoing_historical_records_vv').datagrid({
						onSelect:function(index,value){
							debugger;
						}
					})
				
					var pager_page_outGoing_vv = $('#outGoing_vv').datagrid("getPager");
					if (pager_page_outGoing_vv) {
						$(pager_page_outGoing_vv).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findoutGoing();
							}
						});
					}
					
					var pager_page_outGoing_historical_records_vv = $('#outGoing_historical_records_vv').datagrid("getPager");
					if (pager_page_outGoing_historical_records_vv) {
						$(pager_page_outGoing_historical_records_vv).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findauditoutGoing_historical_records();
							}
						});
					}
			}
		};
    </script>
    
</body>

</html>