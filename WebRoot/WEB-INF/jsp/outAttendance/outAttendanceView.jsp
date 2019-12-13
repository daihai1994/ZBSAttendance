<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div class ="easyui-layout" data-options="fit:true">
		<div data-options ="region:'west',split:true"  style ="width: 70%;"> 
			<div class="easyui-tabs"  data-options="plain:true" fit="true"> 
				<div title="需要审批" style="padding:0px;" >
					<div id="toolbar_outAttendance" style="height:35px">
							开始时间:<input id="bt_outAttendance" class="easyui-datebox" editable="false" style="width:150px;height:26px">
							结束时间:<input id="et_outAttendance" class="easyui-datebox" editable="false" style="width:150px;height:26px">
							<input id="outAttendance_userId" class="easyui-textbox"   data-options="label:'申请人:'">
							<a href="javascript:void(0)" class="easyui-linkbutton"
								data-options="iconCls:'icon-search'" onclick="findOutAttendance()">查询</a>
							<a href="javascript:void(0)" class="easyui-linkbutton"
								data-options="iconCls:'icon-edit'" onclick="auditOutAttendance()">审批</a>
					</div>
					<table id="outAttendance_vv" class="easyui-datagrid"
						data-options="
							fit:true,
							striped:true,
							fitColumns: true,
							singleSelect:true,
							rownumbers:true,
							toolbar: '#toolbar_outAttendance',
							pagination:true, //包含分页
							pageSize:50">
						<thead>
							<tr>
								<th data-options="field:'user_name',width:20,align:'center',formatter:addressType">申请人</th>
								<th data-options="field:'attendance_time',width:30,align:'center',formatter:addressType">申请时间</th>
								<th data-options="field:'attendance_address',width:40,align:'center',formatter:addressType">申请地址</th>
								<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
								<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
								<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture">图片</th>
								<th data-options="field:'_',width:20,align:'center',formatter:audit_record">审批记录</th>
							</tr>
						</thead>
					</table>
					<div id="auditOutAttendance_dialog" class="easyui-dialog" title="审批"  style="width:470px;height: 450px;padding:10px;" data-options="modal:true,closed:true">
						<div style="margin: 10px;">
							<input data-options="label:'申请人:',disabled:true"  class="easyui-textbox" id="auditOutAttendance_user_name" style="width: 350px;">
						</div>
						<div style="margin: 10px;">
							<input data-options="label:'申请状态:',disabled:true"  class="easyui-textbox" id="auditOutAttendance_result" style="width: 350px;">
						</div>
						<div style="margin: 10px;">
							<input data-options="label:'申请备注:',disabled:true,multiline:true"  class="easyui-textbox" id="auditOutAttendance_remarks" style="width: 350px;height:60px">
						</div>
						<div style="margin: 10px;">
							<input data-options="label:'申请地址:',disabled:true,multiline:true"  class="easyui-textbox" id="auditOutAttendance_attendance_address" style="width: 350px;height:60px">
						</div>
						<div style="margin: 10px;">
							<span style="display: inline;margin-right: 50px">审批结果: </span>
							<span style="display: inline;"><input id="auditOutAttendance_radio_1" type="radio" name="auditOutAttendance_radio" value="2">通过
		                		<input id="auditOutAttendance_radio_2" type="radio" name="auditOutAttendance_radio" value="3">不通过</span>
		                	<span style="color: red;display: inline;">*必填</span>
						</div>
						<div style="margin: 10px;">
							<input data-options="label:'审批备注:',multiline:true"  class="easyui-textbox" id="auditOutAttendance_audit_remarks" style="width: 350px;height:60px">
							<span style="color: red;display: inline;">*必填</span>
						</div>
						<div style="margin: 10px;text-align: center;">
							<a  onclick="submitAuditOutAttendance()" class="easyui-linkbutton" style="width: 100px;">保存</a>
							<a  onclick="$('#auditOutAttendance_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
						</div>
					</div>
				</div>
				<div title="审批记录" style="padding:0px;" >
					<div id="toolbar_outAttendance_historical_records" style="height:35px">
							开始时间:<input id="bt_outAttendance_historical_records" class="easyui-datebox" editable="false" style="width:150px;height:26px">
							结束时间:<input id="et_outAttendance_historical_records" class="easyui-datebox" editable="false" style="width:150px;height:26px">
							<input id="outAttendance_userId_historical_records" class="easyui-textbox"   data-options="label:'申请人:'">
							<a href="javascript:void(0)" class="easyui-linkbutton"
								data-options="iconCls:'icon-edit'" onclick="findauditOutAttendance_historical_records()">查看处理过程</a>
					</div>
					<table id="outAttendance_historical_records_vv" class="easyui-datagrid"
						data-options="
							fit:true,
							striped:true,
							fitColumns: true,
							singleSelect:true,
							rownumbers:true,
							toolbar: '#toolbar_outAttendance_historical_records',
							pagination:true, //包含分页
							pageSize:50">
						<thead>
							<tr>
								<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
								<th data-options="field:'attendance_time',width:20,align:'center',formatter:addressType">申请时间</th>
								<th data-options="field:'attendance_address',width:40,align:'center',formatter:addressType">申请地址</th>
								<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
								<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
								<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture">图片</th>
								<th data-options="field:'audit_status',width:20,align:'center',formatter:auditStatus">审批结果</th>
								<th data-options="field:'audit_remarks',width:50,align:'center',formatter:addressType">审批备注</th>
								<th data-options="field:'audit_time',width:20,align:'center',formatter:addressType">处理时间</th>
								<th data-options="field:'_',width:20,align:'center',formatter:audit_record">审批记录</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<div data-options ="region:'center',split:true"  > 
			<div id="outAttendanceMap" style="height:100%;width:100%"></div>
		</div>
	</div>
	
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/outAttendance/outAttendanceView.js"></script>
    <script type="text/javascript">
		var outAttendanceAudit = true;
		var outAttendanceMap;
		$.parser.onComplete = function(res) {
			if (outAttendanceAudit) {
			debugger;
				outAttendanceAudit = false;
				var newDate = new Date();
				$('#bt_outAttendance').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
				$('#et_outAttendance').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				$('#bt_outAttendance_historical_records').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
				$('#et_outAttendance_historical_records').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
				outAttendanceMap = new BMap.Map("outAttendanceMap",{enableMapClick:false});
				outAttendanceMap.centerAndZoom("苏州",12);                   // 初始化地图,设置城市和地图级别。
				outAttendanceMap.enableScrollWheelZoom();
				
				findOutAttendance();
				findauditOutAttendance_historical_records();
				/**
				*加载地图
				*/
				function loadOutAttendanceMap(value){
					outAttendanceMap.clearOverlays();    //清除地图上所有覆盖物
					var point = new BMap.Point(value.attendance_longitude,value.attendance_latitude);
					outAttendanceMap.centerAndZoom(point, 18);
					var marker = new BMap.Marker(point);
					outAttendanceMap.addOverlay(marker);    //添加标注
				}
					$('#outAttendance_vv').datagrid({
						onSelect:function(index,value){
							debugger;
							loadOutAttendanceMap(value);
						}
					})
					$('#outAttendance_historical_records_vv').datagrid({
						onSelect:function(index,value){
							debugger;
							loadOutAttendanceMap(value);
						}
					})
				
					var pager_page_outAttendance_vv = $('#outAttendance_vv').datagrid("getPager");
					if (pager_page_outAttendance_vv) {
						$(pager_page_outAttendance_vv).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findOutAttendance();
							}
						});
					}
					
					var pager_page_outAttendance_historical_records_vv = $('#outAttendance_historical_records_vv').datagrid("getPager");
					if (pager_page_outAttendance_historical_records_vv) {
						$(pager_page_outAttendance_historical_records_vv).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findauditOutAttendance_historical_records();
							}
						});
					}
			}
		};
    </script>
    
</body>

</html>