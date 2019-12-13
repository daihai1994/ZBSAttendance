<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div class="easyui-tabs"  data-options="plain:true" fit="true"> 
		<div title="外勤打卡申请记录" style="padding:0px;" >
			<div class ="easyui-layout" data-options="fit:true">
				<div data-options ="region:'west',split:true"  style ="width: 70%;"> 
					<div id="toolbar_apply_Record" style="height:35px">
						开始时间:<input id="bt_apply_Record" class="easyui-datebox" editable="false" style="width:150px;height:26px">
						结束时间:<input id="et_apply_Record" class="easyui-datebox" editable="false" style="width:150px;height:26px">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'" onclick="findApply_Record()">查询</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'" onclick="updateApplyEffective()">标记无效</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-edit'" onclick="markRead()">标记为已读</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-edit'" onclick="allTagsRead()">全部标为已读</a>
					</div>
					<table id="apply_Record_vv" class="easyui-datagrid"
						data-options="
							fit:true,
							striped:true,
							fitColumns: true,
							singleSelect:false,
							rownumbers:true,
							toolbar: '#toolbar_apply_Record',
							pagination:true, //包含分页
							pageSize:50">
						<thead>
							<tr>
							 	<th data-options="field:'ck',checkbox:true"></th>
								<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
								<th data-options="field:'user_id',width:10,align:'center',formatter:addressType">申请ID</th>
								<th data-options="field:'attendance_time',width:20,align:'center',formatter:addressType">申请时间</th>
								<th data-options="field:'attendance_address',width:40,align:'center',formatter:addressType">申请地址</th>
								<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
								<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
								<th data-options="field:'effective',width:20,align:'center',formatter:effectiveType">是否有效</th>
								<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture">图片</th>
								<th data-options="field:'_',width:20,align:'center',formatter:audit_record">审批记录</th>
								<th data-options="field:'readNotRead',width:20,align:'center',formatter:readNotRead">操作</th>
							</tr>
						</thead>
					</table>
				</div>
				<div data-options ="region:'center',split:true"  > 
					<div id="applyRecordMap" style="height:100%;width:100%"></div>
				</div>
			</div>
		</div>
		<div title="补卡申请记录" style="padding:0px;" >
			<div id="toolbar_appeal_Record" style="height:35px">
				开始时间:<input id="bt_appeal_Record" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_appeal_Record" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findAppeal_Record()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'" onclick="updateAppealEffective()">标记无效</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="markRead()">标记为已读</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="allTagsRead()">全部标为已读</a>
			</div>
			<table id="appeal_Record_vv" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:false,
					rownumbers:true,
					toolbar: '#toolbar_appeal_Record',
					pagination:true, //包含分页
					pageSize:50">
				<thead>
					<tr>
					 	<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
						<th data-options="field:'user_id',width:10,align:'center',formatter:addressType">申请ID</th>
						<th data-options="field:'attendance_time',width:20,align:'center',formatter:addressType">申请时间</th>
						<th data-options="field:'type_name',width:20,align:'center',formatter:addressType">补卡类型</th>
						<th data-options="field:'appeal_time',width:20,align:'center',formatter:addressType">补卡时间</th>
						<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
						<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
						<th data-options="field:'effective',width:20,align:'center',formatter:effectiveType">是否有效</th>
						<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_Appeal">图片</th>
						<th data-options="field:'_',width:20,align:'center',formatter:audit_record_Appeal">审批记录</th>
						<th data-options="field:'readNotRead',width:20,align:'center',formatter:readNotRead">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<div title="加班申请记录" style="padding:0px;" >
			<div id="toolbar_overTime_Record_apply" style="height:35px">
				开始时间:<input id="bt_overTime_Record_apply" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_overTime_Record_apply" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findOverTime_Record()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'" onclick="updateOverTimeEffective()">标记无效</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="markReadoverTime()">标记为已读</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="allTagsReadoverTime()">全部标为已读</a>
			</div>
			<table id="overTime_Record_vv_apply" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:false,
					rownumbers:true,
					toolbar: '#toolbar_overTime_Record',
					pagination:true, //包含分页
					pageSize:50">
				<thead>
					<tr>
					 	<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
						<th data-options="field:'user_id',width:10,align:'center',formatter:addressType">申请ID</th>
						<th data-options="field:'createTime',width:20,align:'center',formatter:addressType">申请时间</th>
						<th data-options="field:'overTimeType',width:20,align:'center',formatter:addressType">加班类型</th>
						<th data-options="field:'start_time',width:20,align:'center',formatter:addressType">加班开始时间</th>
						<th data-options="field:'stop_time',width:20,align:'center',formatter:addressType">加班结束时间</th>
						<th data-options="field:'day',width:20,align:'center',formatter:addressType">天数</th>
						<th data-options="field:'hour',width:20,align:'center',formatter:addressType">小时</th>
						<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
						<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
						<th data-options="field:'effective',width:20,align:'center',formatter:effectiveType">是否有效</th>
						<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_OverTime">图片</th>
						<th data-options="field:'_',width:20,align:'center',formatter:audit_record_OverTime">审批记录</th>
						<th data-options="field:'readNotRead',width:20,align:'center',formatter:readNotRead">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<div title="假期申请记录" style="padding:0px;" >
			<div id="toolbar_vacation_Record_apply" style="height:35px">
				开始时间:<input id="bt_vacation_Record_apply" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_vacation_Record_apply" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findvacation_Record()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'" onclick="updatevacationEffective()">标记无效</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="markReadvacation()">标记为已读</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="allTagsReadvacation()">全部标为已读</a>
			</div>
			<table id="vacation_Record_vv_apply" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:false,
					rownumbers:true,
					toolbar: '#toolbar_vacation_Record',
					pagination:true, //包含分页
					pageSize:50">
				<thead>
					<tr>
					 	<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
						<th data-options="field:'user_id',width:10,align:'center',formatter:addressType">申请ID</th>
						<th data-options="field:'createTime',width:20,align:'center',formatter:addressType">申请时间</th>
						<th data-options="field:'vacation_type_name',width:20,align:'center',formatter:addressType">假期类型</th>
						<th data-options="field:'start_time',width:20,align:'center',formatter:addressType">假期开始时间</th>
						<th data-options="field:'stop_time',width:20,align:'center',formatter:addressType">假期结束时间</th>
						<th data-options="field:'day',width:20,align:'center',formatter:addressType">天数</th>
						<th data-options="field:'hour',width:20,align:'center',formatter:addressType">小时</th>
						<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
						<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
						<th data-options="field:'effective',width:20,align:'center',formatter:effectiveType">是否有效</th>
						<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_vacation">图片</th>
						<th data-options="field:'_',width:20,align:'center',formatter:audit_record_vacation">审批记录</th>
						<th data-options="field:'readNotRead',width:20,align:'center',formatter:readNotRead">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<div title="外出申请记录" style="padding:0px;" >
			<div id="toolbar_outGoing_Record_apply" style="height:35px">
				开始时间:<input id="bt_outGoing_Record_apply" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_outGoing_Record_apply" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findoutGoing_Record()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'" onclick="updateoutGoingEffective()">标记无效</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="markReadoutGoing()">标记为已读</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="allTagsReadoutGoing()">全部标为已读</a>
			</div>
			<table id="outGoing_Record_vv_apply" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:false,
					rownumbers:true,
					toolbar: '#toolbar_outGoing_Record',
					pagination:true, //包含分页
					pageSize:50">
				<thead>
					<tr>
					 	<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
						<th data-options="field:'user_id',width:10,align:'center',formatter:addressType">申请ID</th>
						<th data-options="field:'createTime',width:20,align:'center',formatter:addressType">申请时间</th>
						<th data-options="field:'address',width:10,align:'center',formatter:addressType">外出地址</th>
						<th data-options="field:'start_time',width:20,align:'center',formatter:addressType">外出开始时间</th>
						<th data-options="field:'stop_time',width:20,align:'center',formatter:addressType">外出结束时间</th>
						<th data-options="field:'hour',width:20,align:'center',formatter:addressType">小时</th>
						<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
						<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
						<th data-options="field:'effective',width:20,align:'center',formatter:effectiveType">是否有效</th>
						<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_outGoing">图片</th>
						<th data-options="field:'_',width:20,align:'center',formatter:audit_record_outGoing">审批记录</th>
						<th data-options="field:'readNotRead',width:20,align:'center',formatter:readNotRead">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		
		<div title="出差申请记录" style="padding:0px;" >
			<div id="toolbar_businessTraveI_Record_apply" style="height:35px">
				开始时间:<input id="bt_businessTraveI_Record_apply" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				结束时间:<input id="et_businessTraveI_Record_apply" class="easyui-datebox" editable="false" style="width:150px;height:26px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findbusinessTraveI_Record()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'" onclick="updatebusinessTraveIEffective()">标记无效</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="markReadbusinessTraveI()">标记为已读</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="allTagsReadbusinessTraveI()">全部标为已读</a>
			</div>
			<table id="businessTraveI_Record_vv_apply" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:false,
					rownumbers:true,
					toolbar: '#toolbar_businessTraveI_Record',
					pagination:true, //包含分页
					pageSize:50">
				<thead>
					<tr>
					 	<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'user_name',width:10,align:'center',formatter:addressType">申请人</th>
						<th data-options="field:'user_id',width:10,align:'center',formatter:addressType">申请ID</th>
						<th data-options="field:'createTime',width:20,align:'center',formatter:addressType">申请时间</th>
						<th data-options="field:'address',width:10,align:'center',formatter:addressType">出差地址</th>
						<th data-options="field:'start_time',width:20,align:'center',formatter:addressType">出差开始时间</th>
						<th data-options="field:'stop_time',width:20,align:'center',formatter:addressType">出差结束时间</th>
						<th data-options="field:'day',width:20,align:'center',formatter:addressType">小时</th>
						<th data-options="field:'hour',width:20,align:'center',formatter:addressType">小时</th>
						<th data-options="field:'remarks',width:50,align:'center',formatter:addressType">申请备注</th>
						<th data-options="field:'result',width:20,align:'center',formatter:addressType">申请状态</th>
						<th data-options="field:'effective',width:20,align:'center',formatter:effectiveType">是否有效</th>
						<th data-options="field:'pic',width:20,align:'center',formatter:loadPicture_businessTraveI">图片</th>
						<th data-options="field:'_',width:20,align:'center',formatter:audit_record_businessTraveI">审批记录</th>
						<th data-options="field:'readNotRead',width:20,align:'center',formatter:readNotRead">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	
<script type="text/javascript" src="${pageContext.request.contextPath }/js/applyRecord/applyRecordView.js">
</script>
<script>
var applyRecord_init= true;
var applyRecordMap;
$.parser.onComplete = function (res) {
	if(applyRecord_init){
		applyRecord_init=false;
		var newDate = new Date();
		$('#bt_apply_Record').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
		$('#et_apply_Record').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
		
		$('#bt_appeal_Record').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
		$('#et_appeal_Record').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
		
		
		$('#bt_overTime_Record_apply').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
		$('#et_overTime_Record_apply').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
		
		$('#bt_vacation_Record_apply').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
		$('#et_vacation_Record_apply').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
		
		$('#bt_outGoing_Record_apply').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
		$('#et_outGoing_Record_apply').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
		
		$('#bt_businessTraveI_Record_apply').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
		$('#et_businessTraveI_Record_apply').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
		
		findApply_Record();
		applyRecordMap = new BMap.Map("applyRecordMap",{enableMapClick:false});
		applyRecordMap.centerAndZoom("苏州",12);                   // 初始化地图,设置城市和地图级别。
		applyRecordMap.enableScrollWheelZoom();
		
		/**
		*加载地图
		*/
		function loadOutAttendanceMap(value){
			applyRecordMap.clearOverlays();    //清除地图上所有覆盖物
			var point = new BMap.Point(value.attendance_longitude,value.attendance_latitude);
			applyRecordMap.centerAndZoom(point, 18);
			var marker = new BMap.Marker(point);
			applyRecordMap.addOverlay(marker);    //添加标注
		}
		$('#apply_Record_vv').datagrid({
			onSelect:function(index,value){
				debugger;
				loadOutAttendanceMap(value);
			}
		})
		
		var pager_page_apply_Record_vv = $('#apply_Record_vv').datagrid("getPager");
		if (pager_page_apply_Record_vv) {
			$(pager_page_apply_Record_vv).pagination({
				pageSize:50,
				pageList : [ 50,100,200 ],
				onSelectPage : function(pageNumber, pageSize) {
					findApply_Record();
				}
			});
		}
		
		var pager_page_appeal_Record_vv = $('#appeal_Record_vv').datagrid("getPager");
		if (pager_page_appeal_Record_vv) {
			$(pager_page_appeal_Record_vv).pagination({
				pageSize:50,
				pageList : [ 50,100,200 ],
				onSelectPage : function(pageNumber, pageSize) {
					findAppeal_Record();
				}
			});
		}
		
		var pager_page_overTime_Record_vv_apply = $('#overTime_Record_vv_apply').datagrid("getPager");
		if (pager_page_overTime_Record_vv_apply) {
			$(pager_page_overTime_Record_vv_apply).pagination({
				pageSize:50,
				pageList : [ 50,100,200 ],
				onSelectPage : function(pageNumber, pageSize) {
					findOverTime_Record();
				}
			});
		}
		
		var pager_page_vacation_Record_vv_apply = $('#vacation_Record_vv_apply').datagrid("getPager");
		if (pager_page_vacation_Record_vv_apply) {
			$(pager_page_vacation_Record_vv_apply).pagination({
				pageSize:50,
				pageList : [ 50,100,200 ],
				onSelectPage : function(pageNumber, pageSize) {
					findvacation_Record();
				}
			});
		}
		
		var pager_page_outGoing_Record_vv_apply = $('#outGoing_Record_vv_apply').datagrid("getPager");
		if (pager_page_outGoing_Record_vv_apply) {
			$(pager_page_outGoing_Record_vv_apply).pagination({
				pageSize:50,
				pageList : [ 50,100,200 ],
				onSelectPage : function(pageNumber, pageSize) {
					findoutGoing_Record();
				}
			});
		}
		
		var pager_page_businessTraveI_Record_vv_apply = $('#businessTraveI_Record_vv_apply').datagrid("getPager");
		if (pager_page_businessTraveI_Record_vv_apply) {
			$(pager_page_businessTraveI_Record_vv_apply).pagination({
				pageSize:50,
				pageList : [ 50,100,200 ],
				onSelectPage : function(pageNumber, pageSize) {
					findbusinessTraveI_Record();
				}
			});
		}
	}
};
</script>
</body>
</html>
