<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>

<html>
<head>

<style>
.radioSpan {
	position: relative;
	border: 1px solid #95B8E7;
	background-color: #fff;
	vertical-align: middle;
	display: inline-block;
	overflow: hidden;
	white-space: nowrap;
	margin: 0;
	padding: 0;
	-moz-border-radius: 5px 5px 5px 5px;
	-webkit-border-radius: 5px 5px 5px 5px;
	border-radius: 5px 5px 5px 5px;
	display: block;
}

</style>
</head>
<body>
<div class="easyui-tabs" id="childTabTimmer_yewuguanli" data-options="plain:true" fit="true"> 
	<div title="定时器配置" style="padding:0px;" >
		<div id = "timmer_toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="findTimmerCron()">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'" onclick="AddTimmerCron()">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'" onclick="updateTimmerCron()">修改</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-remove'" onclick="deleteTimmerCron()">删除</a>
		</div>
		<table id="dg_timmer_xitongpeizhi" class="easyui-datagrid" style="width: 100%;height: 100%" 
			data-options="
						singleSelect: true,
						rownumbers:true,
						toolbar: '#timmer_toolbar',
						striped: true,
						fitColumns:true">
			<thead>
				<tr>
					<th data-options="field:'jobName',align:'left'" style="width: 30%">定时器名称</th>
					<th data-options="field:'mode',align:'left'" style="width: 30%">类型</th>
					<th data-options="field:'cronName',align:'center'" style="width: 40%">时间</th>
				</tr>
			</thead>
		</table>
		<div id="dlg_xitongpeizhi_add" class="easyui-dialog" title="添加任务" closed="true" style="width:600px;height:300px;padding:10px"
	        			data-options="modal:true,iconCls: 'icon-add'">
	        <div style="margin:20px ">
				<select id="timmerMode_xitongpeizhi" class="easyui-combobox"
					data-options="editable:false" name="timmerMode_xitongpeizhi"
					label="类型:" prompt="请选择类型"  style="width:80%;">
					<option value="每隔几秒钟">每隔几秒钟</option>
					<option value="每隔几分钟">每隔几分钟</option>
					<option value="每隔几小时">每隔几小时</option>
					<option value="每天" selected="selected">每天</option>
					<option value="每周">每周</option>
					<option value="每月">每月</option>
				</select>
			</div>
			<div style="margin:20px ">
				时间： &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<select id="week_xitongpeizhi" class="easyui-combobox"
					data-options="editable:false" name="week_xitongpeizhi"
					style="width:25%;">
					<option value="2" selected="selected">周一</option>
					<option value="3">周二</option>
					<option value="4">周三</option>
					<option value="5">周四</option>
					<option value="6">周五</option>
					<option value="7">周六</option>
					<option value="1">周日</option>
				</select>
				<select id="hour_xitongpeizhi" class="easyui-combobox"
					data-options="editable:false" name="hour_xitongpeizhi"
					style="width:25%;">
					<option value="0" selected="selected">0</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
				</select>
				<select id="minute_xitongpeizhi" class="easyui-combobox"
					data-options="editable:false" name="minute_xitongpeizhi"
					style="width:25%;">
					<option value="0" selected="selected">0</option><option value="1">1</option><option value="2">2</option>
					<option value="3">3</option><option value="4">4</option><option value="5">5</option>
					<option value="6">6</option><option value="7">7</option><option value="8">8</option>
					<option value="9">9</option><option value="10">10</option><option value="11">11</option>
					<option value="12">12</option><option value="13">13</option><option value="14">14</option>
					<option value="15">15</option><option value="16">16</option><option value="17">17</option>
					<option value="18">18</option><option value="19">19</option><option value="20">20</option>
					<option value="21">21</option><option value="22">22</option><option value="23">23</option>
					<option value="24">24</option><option value="25">25</option><option value="26">26</option>
					<option value="27">27</option><option value="28">28</option><option value="29">29</option>
					<option value="30">30</option><option value="31">31</option><option value="32">32</option>
					<option value="33">33</option><option value="34">34</option><option value="35">35</option>
					<option value="36">36</option><option value="37">37</option><option value="38">38</option>
					<option value="39">39</option><option value="40">40</option><option value="41">41</option>
					<option value="42">42</option><option value="43">43</option><option value="44">44</option>
					<option value="45">45</option><option value="46">46</option><option value="47">47</option>
					<option value="48">48</option><option value="49">49</option><option value="50">50</option>
					<option value="51">51</option><option value="52">52</option><option value="53">53</option>
					<option value="54">54</option><option value="55">55</option><option value="56">56</option>
					<option value="57">57</option><option value="58">58</option><option value="59">59</option>
				</select>
				<input id="bt_dtbox_month_xitongpeizhi" class="easyui-datetimebox"
					 editable="false"
					style="width:30%;">
			</div>
			<div  align="center">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					 onclick="save_timmer()" style="margin:0 auto">保存</a>
			</div>
       	</div>
       	<div id="dlg_xitongpeizhi_update" class="easyui-dialog" title="修改任务" closed="true" style="width:600px;height:300px;padding:10px"
      			data-options="modal:true,iconCls: 'icon-edit'">
      		<input id="id_timmer_xitongpeizhi" name="id_timmer_xitongpeizhi" TYPE="hidden">
       		<div style="margin:20px ">
				<select id="timmerMode_xitongpeizhi_update" class="easyui-combobox"
					data-options="editable:false" name="timmerMode_xitongpeizhi_update"
					label="类型:" prompt="请选择类型"  style="width:80%;">
					<option value="每隔几秒钟">每隔几秒钟</option>
					<option value="每隔几分钟">每隔几分钟</option>
					<option value="每隔几小时">每隔几小时</option>
					<option value="每天" selected="selected">每天</option>
					<option value="每周">每周</option>
					<option value="每月">每月</option>
				</select>
			</div>
			<div style="margin:20px ">
				时间： &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<select id="week_xitongpeizhi_update" class="easyui-combobox"
					data-options="editable:false" name="week_xitongpeizhi_update"
					style="width:25%;">
					<option value="2" selected="selected">周一</option>
					<option value="3">周二</option>
					<option value="4">周三</option>
					<option value="5">周四</option>
					<option value="6">周五</option>
					<option value="7">周六</option>
					<option value="1">周日</option>
				</select>
				<select id="hour_xitongpeizhi_update" class="easyui-combobox"
					data-options="editable:false" name="hour_xitongpeizhi_update"
					style="width:25%;">
					<option value="0" selected="selected">0</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
				</select>
				<select id="minute_xitongpeizhi_update" class="easyui-combobox"
					data-options="editable:false" name="minute_xitongpeizhi_update"
					style="width:25%;">
					<option value="0" selected="selected">0</option><option value="1">1</option><option value="2">2</option>
					<option value="3">3</option><option value="4">4</option><option value="5">5</option>
					<option value="6">6</option><option value="7">7</option><option value="8">8</option>
					<option value="9">9</option><option value="10">10</option><option value="11">11</option>
					<option value="12">12</option><option value="13">13</option><option value="14">14</option>
					<option value="15">15</option><option value="16">16</option><option value="17">17</option>
					<option value="18">18</option><option value="19">19</option><option value="20">20</option>
					<option value="21">21</option><option value="22">22</option><option value="23">23</option>
					<option value="24">24</option><option value="25">25</option><option value="26">26</option>
					<option value="27">27</option><option value="28">28</option><option value="29">29</option>
					<option value="30">30</option><option value="31">31</option><option value="32">32</option>
					<option value="33">33</option><option value="34">34</option><option value="35">35</option>
					<option value="36">36</option><option value="37">37</option><option value="38">38</option>
					<option value="39">39</option><option value="40">40</option><option value="41">41</option>
					<option value="42">42</option><option value="43">43</option><option value="44">44</option>
					<option value="45">45</option><option value="46">46</option><option value="47">47</option>
					<option value="48">48</option><option value="49">49</option><option value="50">50</option>
					<option value="51">51</option><option value="52">52</option><option value="53">53</option>
					<option value="54">54</option><option value="55">55</option><option value="56">56</option>
					<option value="57">57</option><option value="58">58</option><option value="59">59</option>
				</select>
				<input id="bt_dtbox_month_xitongpeizhi_update" class="easyui-datetimebox"
					 editable="false"
					style="width:30%;">
			</div>
			<div  align="center">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					 onclick="update_timmer()" style="margin:0 auto">保存</a>
			</div>
      	</div>
	</div>
	<div title="计算工时" style="padding:0px;" >
		<div id = "timmer_configure_toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="findTimmerCron_configure()">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'" onclick="AddTimmerCron_configure()">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'" onclick="updateTimmerCron_configure()">修改</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-remove'" onclick="deleteTimmerCron_configure()">删除</a>
		</div>
		<table id="dg_timmer_configure_xitongpeizhi" class="easyui-datagrid" style="width: 100%;height: 100%" 
			data-options="
						singleSelect: true,
						rownumbers:true,
						toolbar: '#timmer_configure_toolbar',
						striped: true,
						fitColumns:true">
			<thead>
				<tr>
					<th data-options="field:'timerName',align:'left'" style="width: 25%">定时计算工时任务名</th>
					<th data-options="field:'jobName',align:'left'" style="width: 25%">定时器名称</th>
					<th data-options="field:'mode',align:'left'" style="width: 25%">类型</th>
					<th data-options="field:'cronName',align:'center'" style="width: 25%">时间</th>
				</tr>
			</thead>
		</table>
		<div id="dlg_xitongpeizhi_configureAdd" class="easyui-dialog" title="定时计算工时任务配置新增" closed="true" style="width:600px;height:300px;padding:10px"
	        			data-options="modal:true,iconCls: 'icon-add'">
	        <div style="margin:20px ">
				<input  id="timerName_xitongpeizhi" class="easyui-textbox" name="timerName_xitongpeizhi" label="任务名:"   style="width:80%;">
			</div>
			<div style="margin:20px ">
				<select editable="false" id="combotimerCron_xitongpeizhi" class="easyui-combobox" name="combotimerCron_xitongpeizhi" label="选择定时器:" prompt="请选择定时器"  style="width:80%;">
				</select>
			</div>
			<div  align="center">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					 onclick="configureAdd_timmer()" style="margin:0 auto">保存</a>
			</div>
	    </div>
	     <div id="dlg_xitongpeizhi_configureupdate" class="easyui-dialog" title="定时计算工时任务配置修改" closed="true" style="width:600px;height:500px;padding:10px"
	        data-options="modal:true,iconCls: 'icon-add'">
	        <input id="id_configureAdd_xitongpeizhi" name="id_timmer_xitongpeizhi" TYPE="hidden">
	        <div style="margin:20px ">
				<input  id="timerName_update_xitongpeizhi" class="easyui-textbox" name="timerName_update_xitongpeizhi" label="计算工时任务名:"   style="width:80%;">
			</div>
			<div style="margin:20px ">
				<select editable="false" id="combotimerCron_update_xitongpeizhi" class="easyui-combobox" name="combotimerCron_update_xitongpeizhi" label="选择定时器:" prompt="请选择定时器"  style="width:80%;">
				</select>
			</div>
			<div  align="center">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					 onclick="configureupdate_timmer()" style="margin:0 auto">保存</a>
			</div>
      	</div>
	</div>
</div>

	<script type="text/javascript"
		src="${pageContext.request.contextPath }/js/configInfor/configInforView.js"></script>
	<script>
	var configInfo_init= true;
	$.parser.onComplete = function (res) {
		if(configInfo_init){
			configInfo_init=false;
			
			$('#combotimerCron_xitongpeizhi').combobox({
					valueField:'id',
				    textField:'text',
				})
			$('#combotimerCron_update_xitongpeizhi').combobox({
					valueField:'id',
				    textField:'text',
				})
			
			
			findTimmerCron();
			findTimmerCron_configure();
			$('#hour_xitongpeizhi').next().show();
			$('#minute_xitongpeizhi').next().show();
			$('#week_xitongpeizhi').next().hide();
			$('#bt_dtbox_month_xitongpeizhi').next().hide();
			$('#timmerMode_xitongpeizhi').combobox({
				onChange:function (res){
					var data = $('#timmerMode_xitongpeizhi').combobox('getValue');
					if(data==="每隔几秒钟"){
						$('#hour_xitongpeizhi').next().hide();
						$('#minute_xitongpeizhi').next().show();
						$('#week_xitongpeizhi').next().hide();
						$('#bt_dtbox_month_xitongpeizhi').next().hide();
					}
					if(data==="每隔几分钟"){
						$('#hour_xitongpeizhi').next().hide();
						$('#minute_xitongpeizhi').next().show();
						$('#week_xitongpeizhi').next().hide();
						$('#bt_dtbox_month_xitongpeizhi').next().hide();
					}
					if(data==="每隔几小时"){
						$('#hour_xitongpeizhi').next().show();
						$('#minute_xitongpeizhi').next().hide();
						$('#week_xitongpeizhi').next().hide();
						$('#bt_dtbox_month_xitongpeizhi').next().hide();
					}
					
					if(data==="每天"){
						$('#hour_xitongpeizhi').next().show();
						$('#minute_xitongpeizhi').next().show();
						$('#week_xitongpeizhi').next().hide();
						$('#bt_dtbox_month_xitongpeizhi').next().hide();
					}
					if(data==="每周"){
						$('#hour_xitongpeizhi').next().show();
						$('#minute_xitongpeizhi').next().show();
						$('#week_xitongpeizhi').next().show();
						$('#bt_dtbox_month_xitongpeizhi').next().hide();
					}
					if(data==="每月"){
						$('#hour_xitongpeizhi').next().hide();
						$('#minute_xitongpeizhi').next().hide();
						$('#week_xitongpeizhi').next().hide();
						$('#bt_dtbox_month_xitongpeizhi').next().show();
					}
				}
			});
			$('#timmerMode_xitongpeizhi_update').combobox({
				onChange:function (res){
					var data = $('#timmerMode_xitongpeizhi_update').combobox('getValue');
					if(data==="每隔几秒钟"){
						$('#hour_xitongpeizhi_update').next().hide();
						$('#minute_xitongpeizhi_update').next().show();
						$('#week_xitongpeizhi_update').next().hide();
						$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
					}
					if(data==="每隔几分钟"){
						$('#hour_xitongpeizhi_update').next().hide();
						$('#minute_xitongpeizhi_update').next().show();
						$('#week_xitongpeizhi_update').next().hide();
						$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
					}
					if(data==="每隔几小时"){
						$('#hour_xitongpeizhi_update').next().show();
						$('#minute_xitongpeizhi_update').next().hide();
						$('#week_xitongpeizhi_update').next().hide();
						$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
					}
					
					if(data==="每天"){
						$('#hour_xitongpeizhi_update').next().show();
						$('#minute_xitongpeizhi_update').next().show();
						$('#week_xitongpeizhi_update').next().hide();
						$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
					}
					if(data==="每周"){
						$('#hour_xitongpeizhi_update').next().show();
						$('#minute_xitongpeizhi_update').next().show();
						$('#week_xitongpeizhi_update').next().show();
						$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
					}
					if(data==="每月"){
						$('#hour_xitongpeizhi_update').next().hide();
						$('#minute_xitongpeizhi_update').next().hide();
						$('#week_xitongpeizhi_update').next().hide();
						$('#bt_dtbox_month_xitongpeizhi_update').next().show();
					}
				}
			});
		}
	};
	</script>
</body>
</html>
