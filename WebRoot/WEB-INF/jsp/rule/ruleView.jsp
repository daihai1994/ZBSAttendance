<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div class ="easyui-layout" data-options="fit:true">
		<div data-options ="region:'west',split:true"  style ="width: 50%;"> 
			<div id="toolbar_ruleInfo" style="height:35px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findRuleInfo()">查询</a>
<!-- 				<a href="javascript:void(0)" class="easyui-linkbutton" -->
<!-- 					data-options="iconCls:'icon-remove'" onclick="deleteRuleInfo()">刪除</a> -->
<!-- 				<a href="javascript:void(0)" class="easyui-linkbutton" -->
<!-- 					data-options="iconCls:'icon-edit'" onclick="editRuleInfo()">修改</a>  -->
<!-- 				<a href="javascript:void(0)" class="easyui-linkbutton" -->
<!-- 					data-options="iconCls:'icon-add'" onclick="addRuleInfo()">新增</a> -->
			</div>
			<table id="RuleInfo_vv" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:true,
					rownumbers:true,
					toolbar: '#toolbar_ruleInfo'">
				<thead>
					<tr>
						<th data-options="field:'rule_name',width:50,align:'center',formatter:addressType">名称</th>
						<th data-options="field:'rule_address',width:50,align:'center',formatter:addressType">地址</th>
						<th data-options="field:'rule_radius',width:30,align:'center'">半径</th>
						<th data-options="field:'rule_time_work',width:30,align:'center'">上班时间</th>
						<th data-options="field:'rule_time_off_work',width:30,align:'center'">下班时间</th>
						<th data-options="field:'rule_rest_start',width:30,align:'center'">休息开始时间</th>
						<th data-options="field:'rule_rest_end',width:30,align:'center'">休息结束时间</th>
						<th data-options="field:'rule_manager',width:30,align:'center'">制定人</th>
						<th data-options="field:'effective',width:30,align:'center',formatter:effectiveType">是否有效</th>
					</tr>
				</thead>
			</table>
			<div id="rule_details_dialog" class="easyui-dialog" title="考勤规则" style="width:600px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
				<input id="rule_id" type="hidden">
				<div style="margin: 10px;">
					<input data-options="label:'名称',labelWidth:'100px'" class="easyui-textbox" id="rule_name" style="width: 500px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'地址',labelWidth:'100px'" class="easyui-textbox" id="rule_address" style="width: 500px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'半径',labelWidth:'100px'" class="easyui-numberbox" id="rule_radius" style="width: 500px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'上班时间',labelWidth:'100px'"  class="easyui-timespinner" id="rule_time_work" style="width: 500px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'下班时间',labelWidth:'100px'"  class="easyui-timespinner" id="rule_time_off_work" style="width: 500px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'休息开始时间',labelWidth:'100px'"  class="easyui-timespinner" id="rule_rest_start" style="width: 500px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'休息结束时间',labelWidth:'100px'"  class="easyui-timespinner" id="rule_rest_end" style="width: 500px;">
				</div>
				<div style="margin: 10px;">
					<span style="display: inline;margin-right: 50px">是否有效: </span>
					<span style="display: inline;">
						<input id="radio_rule_1" type="radio" name="rule_effective" value="1" checked>有效
                		<input id="radio_rule_2" type="radio" name="rule_effective" value="0">无效
                	</span>
				</div>
				<div style="margin: 10px;">
					<span style="display: inline;margin-right: 50px">考勤类型: </span>
					<span style="display: inline;">
						<input id="radio_rule_type_1" type="radio" name="rule_type" value="1" checked>正常
                		<input id="radio_rule_type_2" type="radio" name="rule_type" value="2">考勤机
                		<input id="radio_rule_type_3" type="radio" name="rule_type" value="3">WIFI考勤
                	</span>
				</div>
				<div style="margin: 10px;" id="rule_unique_address_div">
					<input data-options="label:'唯一地址',labelWidth:'100px'"  class="easyui-textbox" id="rule_unique_address" style="width: 500px;">
				</div>
				<div style="margin: 10px;text-align: center;">
					<a  onclick="submitRuleDetailsDialog()" class="easyui-linkbutton" style="width: 100px;">保存</a>
					<a  onclick="$('#rule_details_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
				</div>
			</div>
		</div>
		<div data-options ="region:'center',split:true"  > 
			<div   style="position: absolute;z-index: 999;left:100px;top:20px;padding:3px 5px;border-radius:3px;text-align:left">
				 <div style="margin-top: 5px">
					<span >
						<input id="switch_add_rule" class="easyui-switchbutton"
						data-options="handleWidth:'80px',handleText:'新增规则',onText:'开',offText:'关'"  
								style="width: 130px;height: 26px;margin-top: 5px;">
							<a href="javascript:void(0)"
									onclick="add_rule_submit();"
									class="easyui-linkbutton"
									data-options="plain:false">确定</a>
					</span>
					<p style="display: inline; color:red" id="add_rule_tooltip" type="hidden">请输入关键字查询或在地图中选择中心点</p>
				</div>
				<div style="margin-top: 5px">
					<span style="display: inline;">
						<input id="switch_edit_rule" class="easyui-switchbutton"
						data-options="handleWidth:'80px',handleText:'修改规则',onText:'开',offText:'关'"  
								style="width: 130px;height: 26px;margin-top: 5px;">
							<a href="javascript:void(0)"
									onclick="edit_rule_submit();"
									class="easyui-linkbutton"
									data-options="plain:false">确定</a>
					</span>
					<p style="display:inline;color:red"  id="edit_rule_tooltip" >请选择要修改的规则</p>
				</div>
<!-- 				<div style="margin-top: 5px"> -->
<!-- 					<span > -->
<!-- 						<input id="switch_delete_rule" class="easyui-switchbutton" -->
<!-- 						data-options="handleWidth:'100px',handleText:'删除规则',onText:'开',offText:'关'"   -->
<!-- 								style="width: 164px;height: 26px;margin-top: 5px;"> -->
<!-- 					</span> -->
<!-- 					<p style="display: inline;color:red" id="delete_rule_tooltip" type="hidden">请选择要删除的规则</p> -->
<!-- 				</div> -->
			</div>
			<div id="r-result" style="position: absolute;z-index: 999;left:100px;top:85px;padding:3px 5px;border-radius:3px;text-align:left">
			<input type="text" id="suggestId" size="20" placeholder="搜索"  value="" style="width:150px;height: 26px" /></div>
			<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
			<div id="l-map" style="height:100%;width:100%"></div>
		
		</div>
	</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/rule/ruleView.js"></script>
    <script type="text/javascript">
		var loggerInfo_init = true;
		var map;
		var ac;
		var geoc;
		var addRuleOverly ;
		$.parser.onComplete = function(res) {
			if (loggerInfo_init) {
				loggerInfo_init = false;
				$('#rule_unique_address_div').hide();
				$('input[type=radio][name=rule_type]').change(function() {
					debugger;
			           if (this.value == '1') {
			           		$('#rule_unique_address_div').hide();
			           }else if(this.value == '2'||this.value == '3'){
			           		$('#rule_unique_address_div').show();
			           }
		      });
				
				$('#rule_details_dialog').dialog({
					onClose:function(){
						debugger;
						console.log("关闭窗口");
						if($('#switch_edit_rule').switchbutton("options").checked){
							debugger;
							map.clearOverlays();    //清除地图上所有覆盖物
							var res = $('#RuleInfo_vv').datagrid("getSelected");
							uploadRuleMarker(res);
						}
					}
				})
				
				$('#RuleInfo_vv').datagrid({
					onSelect : function(index,res){
						debugger;
						map.clearOverlays();    //清除地图上所有覆盖物
						uploadRuleMarker(res);
						$('#switch_edit_rule').switchbutton("check"); 
					}
				})
				$('#add_rule_tooltip').hide();
				$('#edit_rule_tooltip').hide();
				$('#delete_rule_tooltip').hide();
				//新增规则
				$('#switch_add_rule').switchbutton({
					onChange:function(checked){
						if(checked){
							map.clearOverlays();    //清除地图上所有覆盖物
							$('#RuleInfo_vv').datagrid("clearSelections");
							$('#switch_edit_rule').switchbutton("uncheck");
							//$('#switch_delete_rule').switchbutton("uncheck");
							$('#add_rule_tooltip').show();
						}else{
							$('#add_rule_tooltip').hide();
						}
					}
				})
				//修改规则
				$('#switch_edit_rule').switchbutton({
					onChange:function(checked){
						if(checked){
							$('#switch_add_rule').switchbutton("uncheck");
							//$('#switch_delete_rule').switchbutton("uncheck");
							$('#edit_rule_tooltip').show();
						}else{
							$('#edit_rule_tooltip').hide();
						}
					}
				})
				//删除规则
// 				$('#switch_delete_rule').switchbutton({
// 					onChange:function(checked){
// 						if(checked){
// 							$('#switch_add_rule').switchbutton("uncheck");
// 							$('#switch_edit_rule').switchbutton("uncheck");
// 							$('#delete_rule_tooltip').show();
// 						}else{
// 							$('#delete_rule_tooltip').hide();
// 						}
// 					}
// 				})
				
			
				
					map = new BMap.Map("l-map",{enableMapClick:false});
					map.centerAndZoom("苏州",12);                   // 初始化地图,设置城市和地图级别。
					map.enableScrollWheelZoom();
					var mapType2 = new BMap.MapTypeControl({
						mapTypes: [BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP],
						anchor : BMAP_ANCHOR_TOP_LEFT
					});
					map.addControl(mapType2);
					map.addEventListener("dragend", function showInfo(){
					 	ac.setLocation(map.Tg);//修改检索的范围
					});
					 geoc = new BMap.Geocoder();    
					map.addControl(new BMap.CityListControl({
					    anchor: BMAP_ANCHOR_TOP_LEFT,
					    offset: new BMap.Size(10, 50),
					    // 切换城市之间事件
						  onChangeBefore: function(){
						  },
// 						  // 切换城市之后事件
						  onChangeAfter:function(){
						  },
// 						  // 切换城市成功响应事件
						  onChangeSuccess: function (e) {
						 	 ac.setLocation(e.city);//修改检索的范围
						  }
					}));
					drawingManager = new BMapLib.DrawingManager(map, {
						isOpen: false, //是否开启绘制模式
						enableDrawingTool: true, //是否显示工具栏
						enableCalculate: false,
						drawingToolOptions: {
							anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
							offset: new BMap.Size(5, 5), //偏离值
							drawingModes : [BMAP_DRAWING_MARKER],
							drawingTypes : [
								BMAP_DRAWING_POLYGON
							 ]
						}
					});
					drawingManager.addEventListener('overlaycomplete', overlaycomplete);
					 ac = new BMap.Autocomplete(    //建立一个自动完成的对象
						{"input" : "suggestId"
						,"location" : map
					});
					ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
					debugger;
					var str = "";
						var _value = e.fromitem.value;
						var value = "";
						if (e.fromitem.index > -1) {
							value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
						}    
						str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
						
						value = "";
						if (e.toitem.index > -1) {
							_value = e.toitem.value;
							value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
						}    
						str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
						G("searchResultPanel").innerHTML = str;
					});
				
					var myValue;
					ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
					debugger;
					var _value = e.item.value;
						myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
						G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
						
						setPlace();
					});
					function G(id) {
						return document.getElementById(id);
					}
					function setPlace(){
					debugger;
						map.clearOverlays();    //清除地图上所有覆盖物
						function myFun(){
							var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
							map.centerAndZoom(pp, 18);
							var marker = new BMap.Marker(pp);
								map.addOverlay(marker);    //添加标注
								marker.addEventListener("click",function(){
									debugger;
										if($('#switch_add_rule').switchbutton("options").checked){//判定当前是不是在新增规则按钮打开下
											var position = marker.getPosition();
											var lat = position.lat;
											var lng = position.lng;
											var location = lat+","+lng;
											var url = "https://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location="+location+"&output=json&pois=1&latest_admin=1&ak=74232c2e19c45420a1e61bf88d5a2017";
											  $.ajax({
									                async : true,
									                url : url,
									                type : "GET",
									                dataType : "jsonp", // 返回的数据类型，设置为JSONP方式
									                jsonp : 'callback', //指定一个查询参数名称来覆盖默认的 jsonp 回调参数名 callback
									                jsonpCallback: 'handleResponse', //设置回调函数名
									                success: function(response, status, xhr){
									                   if(status==="success"){
										                   	var data = response.result;
										                   	var address = data.formatted_address;
										                   	var sematic_description = data.sematic_description;
										                   	if(sematic_description !==""){
										                   		address +="("+sematic_description+")";
										                   	}
										                   $('#rule_details_dialog').dialog("open");
										                   $('#rule_id').val("");
										                   $('#rule_name').textbox("setValue","");
										                   $('#rule_address').textbox("setValue",address);
										                    $('#rule_radius').numberbox("setValue","");
										                   $('#rule_time_work').timespinner("clear");
										                   $('#rule_time_off_work').timespinner("clear");
										                   $('#rule_rest_start').timespinner("clear");
										                   $('#rule_rest_end').timespinner("clear");
										                   var radios = document.getElementsByName("rule_effective");
														   radios[0].checked = true;
														   radios[1].checked = false;
									                   }
									                   map.removeOverlay(marker);
									                }
								          		});
												addRuleOverly = marker;
											}else if($('#switch_edit_rule').switchbutton("options").checked){
												var param = $('#RuleInfo_vv').datagrid("getSelected");
												var position = marker.getPosition();
												var lat = position.lat;
												var lng = position.lng;
												var location = lat+","+lng;
												var url = "https://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location="+location+"&output=json&pois=1&latest_admin=1&ak=74232c2e19c45420a1e61bf88d5a2017";
												  $.ajax({
									                async : true,
									                url : url,
									                type : "GET",
									                dataType : "jsonp", // 返回的数据类型，设置为JSONP方式
									                jsonp : 'callback', //指定一个查询参数名称来覆盖默认的 jsonp 回调参数名 callback
									                jsonpCallback: 'handleResponse', //设置回调函数名
									                success: function(response, status, xhr){
									                   if(status==="success"){
										                   	var data = response.result;
										                   	var address = data.formatted_address;
										                   	var sematic_description = data.sematic_description;
										                   	if(sematic_description !==""){
										                   		address +="("+sematic_description+")";
										                   	}
										                   $('#rule_details_dialog').dialog("open");
										                   $('#rule_id').val(param.id);
										                   $('#rule_name').textbox("setValue",param.rule_name);
										                   $('#rule_address').textbox("setValue",address);
										                    $('#rule_radius').numberbox("setValue",parseInt(param.rule_radius));
										                   $('#rule_time_work').timespinner("setValue",param.rule_time_work);
										                   $('#rule_time_off_work').timespinner("setValue",param.rule_time_off_work);
										                   $('#rule_rest_start').timespinner("setValue",param.rule_rest_start);
										                   $('#rule_rest_end').timespinner("setValue",param.rule_rest_end);
										                   var effective = param.effective;
										                   var radios = document.getElementsByName("rule_effective");
										                   if(effective==1){
										                    radios[0].checked = true;
														   	radios[1].checked = false;
										                   }else{
										                    radios[1].checked = true;
														   	radios[0].checked = false;
										                   }
														  
									                   }
									                }
									            });
												addRuleOverly = marker;
											}else{
												map.removeOverlay(marker);
												$.messager.show({
									       			title:'提示',
									       			msg:"请打开新增或修改规则按钮！",
									       			width:200,                           //设置弹框的宽度和高度
											        height:100,
											        timeout: 1000  
									       		});
									       		return;
											}
									});
						}
						var local = new BMap.LocalSearch(map, { //智能搜索
						  onSearchComplete: myFun
						});
						local.search(myValue);
					}
					findRuleInfo();
			}
		};
    </script>
    
</body>

</html>