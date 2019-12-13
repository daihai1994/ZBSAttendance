/***
 * 查询考勤规则
 */
function findRuleInfo(){
	debugger;
	map.clearOverlays();
	$('#switch_edit_rule').switchbutton("uncheck");
	$('#switch_add_rule').switchbutton("uncheck");
	var company_id = company_id_select;
	$("#RuleInfo_vv").datagrid("loading");
	$.post(BASE_PATH + "/RuleController/findRuleInfo.do", {
		company_id : company_id
	}, function(data) {
		try {
			$("#RuleInfo_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#RuleInfo_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 考勤规则的新增
 */
function submitRuleDetailsDialog(){
	debugger;
    var radios = document.getElementsByName("rule_effective");
    var radios_type = document.getElementsByName("rule_type");
    	var effective  = 1;  
    	var rule_type  = 1;  
	   var rule_id = $("#rule_id").val();
	   var company_id = company_id_select;
	   var user_id = loginUser.loginName;
	   var rule_name = $('#rule_name').textbox("getValue");
	   var rule_address = $('#rule_address').textbox("getValue");
	   var rule_radius = $('#rule_radius').numberbox("getValue");
	   var rule_time_work = $('#rule_time_work').timespinner("getValue");
	   var rule_time_off_work = $('#rule_time_off_work').timespinner("getValue");
	   var rule_rest_start = $('#rule_rest_start').timespinner("getValue");
	   var rule_rest_end = $('#rule_rest_end').timespinner("getValue");
	   var rule_unique_address = $("#rule_unique_address").textbox("getValue");
	   if(radios[0].checked){
		   effective = 1;
	   }else{
		   effective = 0;
	   }
	   
	   if(radios_type[0].checked){
		   rule_type = 1;
		   rule_unique_address = "";
	   }else if(radios_type[1].checked){
		   rule_type = 2;
	   }else if(radios_type[2].checked){
		   rule_type = 3;
	   }
	   if(!isNotNull(rule_name)){
		   $.messager.alert("提示","名称不能为空！");
		   return;
	   }
	   if(!isNotNull(rule_address)){
		   $.messager.alert("提示","地址不能为空！");
		   return;
	   }
	   if(!isNotNull(rule_radius)){
		   $.messager.alert("提示","半径不能为空！");
		   return;
	   }
	   if(!isNotNull(rule_time_work)){
		   $.messager.alert("提示","上班时间不能为空！");
		   return;
	   }
	   if(!isNotNull(rule_time_off_work)){
		   $.messager.alert("提示","下班时间不能为空！");
		   return;
	   }
	   if(!isNotNull(rule_rest_start)){
		   $.messager.alert("提示","开始休息时间不能为空！");
		   return;
	   }
	   if(!isNotNull(rule_rest_end)){
		   $.messager.alert("提示","结束休息时间不能为空！");
		   return;
	   }
	   if(rule_type==2||rule_type==3){
		   if(!isNotNull(rule_unique_address)){
			   $.messager.alert("提示","唯一地址不能为空！");
			   return;
		   }
	   }
	   var position = addRuleOverly.getPosition();
		var lat = position.lat;
		var lng = position.lng;
	   $.post(BASE_PATH + "/RuleController/submitRuleDetailsDialog.do", {
		   rule_id : rule_id,company_id:company_id,user_id:user_id,rule_name:rule_name,
		   rule_address:rule_address,rule_radius:rule_radius,rule_time_work:rule_time_work,
		   rule_time_off_work:rule_time_off_work,rule_rest_start:rule_rest_start,rule_rest_end:rule_rest_end,
		   effective:effective,lat:lat,lng:lng,rule_type:rule_type,rule_unique_address:rule_unique_address
		}, function(data) {
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					$('#rule_details_dialog').dialog("close");
					map.clearOverlays();    //清除地图上所有覆盖物
					$('#switch_add_rule').switchbutton("uncheck");
					$('#switch_edit_rule').switchbutton("uncheck");
					findRuleInfo();
				}
			} catch (e) {
				console.log(e);
			}
			
		});
}


/***
 * 加载规则map
 */
		function uploadRuleMarker(res){
			var lat = res.rule_latitude;
			var lng = res.rule_longitude;
			var point = new BMap.Point(lng,lat);
				map.centerAndZoom(point, 15);
				var marker = new BMap.Marker(point);// 创建标注
				map.addOverlay(marker);             // 将标注添加到地图中
				marker.enableDragging();           // 不可
				marker.setOffset(new BMap.Size(0,0))
				marker.param = res;
				marker.addEventListener("dragend",function(type, target, pixel, point){
					debugger;
				if($('#switch_edit_rule').switchbutton("options").checked){//判定当前是不是在新增规则按钮打开下
						var position = this.getPosition();
						var circle = this.circle;//拖拽点，圆也更着动
						map.removeOverlay(circle);
						circle.setCenter(position);
						map.addOverlay(circle); 
						
						var param = this.param;
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
				                   var rule_type = param.rule_type;
				                   var unique_address = param.unique_address;
				                   $('#rule_unique_address').textbox("setValue",unique_address);
				                   var radios = document.getElementsByName("rule_effective");
				                   var radios_type = document.getElementsByName("rule_type");
				                   if(effective==1){
				                    radios[0].checked = true;
								   	radios[1].checked = false;
				                   }else{
				                    radios[1].checked = true;
								   	radios[0].checked = false;
				                   }
				                   if(rule_type==1){
				                	   $('#rule_unique_address_div').hide();
				                	   radios_type[0].checked = true;
				                	   radios_type[1].checked = false;
				                	   radios_type[2].checked = false;
				                   }else if(rule_type==2){
				                	   $('#rule_unique_address_div').show();
				                	   $('#rule_unique_address').textbox("setValue",param.unique_address);
				                	   radios_type[0].checked = false;
				                	   radios_type[1].checked = true;
				                	   radios_type[2].checked = false;
				                   }else if(rule_type==3){
				                	   $('#rule_unique_address_div').show();
				                	   $('#rule_unique_address').textbox("setValue",param.unique_address);
				                	   radios_type[0].checked = false;
				                	   radios_type[1].checked = false;
				                	   radios_type[2].checked = true;
				                   }
								  
			                   }
			                }
			            });
						addRuleOverly = marker;
					}else{
						$.messager.show({
			       			title:'提示',
			       			msg:"请打开修改规则按钮！",
			       			width:200,                           //设置弹框的宽度和高度
					        height:100,
					        timeout: 1000  
			       		});
			       		return;
					}
				
				})
				
				marker.addEventListener("click",function(){
					debugger;
					var param = marker.param;
				if($('#switch_edit_rule').switchbutton("options").checked){//判定当前是不是在新增规则按钮打开下
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
				                   var rule_type = param.rule_type;
				                   var unique_address = param.unique_address;
				                   $('#rule_unique_address').textbox("setValue",unique_address);
				                   var radios = document.getElementsByName("rule_effective");
				                   var radios_type = document.getElementsByName("rule_type");
				                   if(effective==1){
				                    radios[0].checked = true;
								   	radios[1].checked = false;
				                   }else{
				                    radios[1].checked = true;
								   	radios[0].checked = false;
				                   }
				                   if(rule_type==1){
				                	   $('#rule_unique_address_div').hide();
				                	   radios_type[0].checked = true;
				                	   radios_type[1].checked = false;
				                	   radios_type[2].checked = false;
				                   }else if(rule_type==2){
				                	   $('#rule_unique_address_div').show();
				                	   $('#rule_unique_address').textbox("setValue",param.unique_address);
				                	   radios_type[0].checked = false;
				                	   radios_type[1].checked = true;
				                	   radios_type[2].checked = false;
				                   }else if(rule_type==3){
				                	   $('#rule_unique_address_div').show();
				                	   $('#rule_unique_address').textbox("setValue",param.unique_address);
				                	   radios_type[0].checked = false;
				                	   radios_type[1].checked = false;
				                	   radios_type[2].checked = true;
				                   }
								  
			                   }
			                }
			            });
						addRuleOverly = marker;
					}else{
						$.messager.show({
			       			title:'提示',
			       			msg:"请打开修改规则按钮！",
			       			width:200,                           //设置弹框的宽度和高度
					        height:100,
					        timeout: 1000  
			       		});
			       		return;
					}
				});
				var circle = new BMap.Circle(point,parseFloat(res.rule_radius));
				map.addOverlay(circle); 
				marker.circle =  circle;
		}
		/***
		 * 地图绘制控件
		 */
		var overlaycomplete = function(e){
			var drowingMode = e.drawingMode;
			drawingManager.close();
			if("marker"===drowingMode){
				var marker = e.overlay;
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
									   var radios_type = document.getElementsByName("rule_type");
									   radios_type[0].checked = true;
									   radios_type[1].checked = false;
									   radios_type[2].checked = false;
									   $('#rule_unique_address_div').hide();
				                	   $('#rule_unique_address').textbox("setValue","");
				                   }
				                   map.removeOverlay(marker);
				                }
				            });
						addRuleOverly = marker;
				}else if($('#switch_edit_rule').switchbutton("options").checked){
					map.clearOverlays();
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
			                   var rule_type = param.rule_type;
			                   var unique_address = param.unique_address;
			                   $('#rule_unique_address').textbox("setValue",unique_address);
			                   var radios = document.getElementsByName("rule_effective");
			                   var radios_type = document.getElementsByName("rule_type");
			                   if(effective==1){
			                    radios[0].checked = true;
							   	radios[1].checked = false;
			                   }else{
			                    radios[1].checked = true;
							   	radios[0].checked = false;
			                   }
			                   if(rule_type==1){
			                	   $('#rule_unique_address_div').hide();
			                	   radios_type[0].checked = true;
			                	   radios_type[1].checked = false;
			                	   radios_type[2].checked = false;
			                   }else if(rule_type==2){
			                	   $('#rule_unique_address_div').show();
			                	   $('#rule_unique_address').textbox("setValue",param.unique_address);
			                	   radios_type[0].checked = false;
			                	   radios_type[1].checked = true;
			                	   radios_type[2].checked = false;
			                   }else if(rule_type==3){
			                	   $('#rule_unique_address_div').show();
			                	   $('#rule_unique_address').textbox("setValue",param.unique_address);
			                	   radios_type[0].checked = false;
			                	   radios_type[1].checked = false;
			                	   radios_type[2].checked = true;
			                   }
		                   }
		                }
		            });
					addRuleOverly = marker;
				}else{
					map.removeOverlay(marker);
					$.messager.show({
		       			title:'提示',
		       			msg:"请打开新增或者修改规则按钮！",
		       			width:200,                           //设置弹框的宽度和高度
				        height:100,
				        timeout: 1000  
		       		});
		       		return;
				}
			}else if("polygon"===drowingMode){
			
			}else if("polyline"===drowingMode){	
			
			}
		}
