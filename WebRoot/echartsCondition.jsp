<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  "/";
%>

<!DOCTYPE html>
<html>
<head>
<title>中本硕机电环境检测仪数据</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="SHORTCUT ICON" 
	href="${pageContext.request.contextPath }/img/favicon.ico"/>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/topnav.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/easyui/themes/bootstrap/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/easyui/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/main/main.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/common/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/common/jquery.jqprint-0.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/echarts.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/main/main.js"></script>

<script type="text/javascript">
var echartsTemperature_Init;//温度
var echartsHumidity_Init;//湿度
var echartsPm25_Init;//pm25
var echartsIlluminance_Init;//光照度
var echartsCarbonDioxide_Init;//co2
var echartsPm10_Init;//pm10
var echarts1Formaldehyde_Init;//一站甲醛
var echarts2Formaldehyde_Init;//二站甲醛
var echarts3Formaldehyde_Init;//三站甲醛
var echarts4Formaldehyde_Init;//四站甲醛
var echarts5Formaldehyde_Init;//五站甲醛
var echarts6Formaldehyde_Init;//六站甲醛

var stationAll = [];
	 $(function() {
	 	 // echartsInit();
		setInterval(function(){   
			var date=new Date();   
			var year=date.getFullYear(); //获取当前年份   
			var mon=date.getMonth()+1; //获取当前月份   
			if(mon.toString().length<2){
				mon = "0"+mon;
			}
			var da=date.getDate(); //获取当前日   
			if(da.toString().length<2){
				da = "0"+da;
			}
			var day=date.getDay(); //获取当前星期几   
			switch(day){
				case 1: 
					day = "一";
					break
				case 2: 
					day = "二";
					break
				case 3: 
					day = "三";
					break
				case 4: 
					day = "四";
					break
				case 5: 
					day = "五";
					break
				case 6: 
					day = "六";
					break
				case 0: 
					day = "日";
					break
				default:
					break;
			}
			var h=date.getHours(); //获取小时   
			if(h.toString().length<2){
				h = "0"+h;
			}
			var m=date.getMinutes(); //获取分钟   
			if(m.toString().length<2){
				m = "0"+m;
			}
			var s=date.getSeconds(); //获取秒   
			if(s.toString().length<2){
				s = "0"+s;
			}
			var d=document.getElementById('Date');    
			d.innerHTML=year+'-'+mon+'-'+da+' '+'星期'+day+' '+h+':'+m+":"+s;  },1000) 
		if(websocket!=null){
			if(websocket.readyState!=1){
           		//alert("未连接。");
           		ConnectWebSocket();
           		return;
           	}else {
           		$.messager.show({title:'提示',msg:'通讯已经连接！'}); 
           	}
		}else {
			var websocket;
			ConnectWebSocket();
		}
	 })
	 function sortNumber(a,b){
		 debugger;
		return a.message.station - b.message.station
	 }
	 //start
	 function echartsUpload(res){
		 setTimeout(function(){debugger;stationAll = [];},12000)
 		if(stationAll.length<6){
 			stationAll.push(res);
 			if(stationAll.length==6){
	 			stationAll.sort(sortNumber);
	 			echartsInfo(stationAll);
	 			stationAll = [];
 			}
 		}
	 }//end 处理是否是六个设备都一起来的
	 //start
	 function echartsInfo(restmep){
	 	//start 温度曲线图
	 	echartsTemperature_Init = echarts.init(document.getElementById("echartsTemperature_Init"));
	 	var echartsTemperature_Init_xAxisData = [];
	 	var echartsTemperature_Init_seriesData = [];
	 	//end 温度曲线图
	 	
	 	//start 湿度曲线图
	 	echartsHumidity_Init = echarts.init(document.getElementById("echartsHumidity_Init"));
	 	var echartsHumidity_Init_xAxisData = [];
	 	var echartsHumidity_Init_seriesData = [];
	 	//end 温度曲线图
	 	
	 	//start pm25曲线图
	 	echartsPm25_Init = echarts.init(document.getElementById("echartsPm25_Init"));
	 	var echartsPm25_Init_xAxisData = [];
	 	var echartsPm25_Init_seriesData = [];
	 	//end pm25曲线图
	 	
	 	//start 光照度曲线图
	 	echartsIlluminance_Init = echarts.init(document.getElementById("echartsIlluminance_Init"));
	 	var echartsIlluminance_Init_xAxisData = [];
	 	var echartsIlluminance_Init_seriesData = [];
	 	//end 光照度曲线图
	 	
	 	//start co2曲线图
	 	echartsCarbonDioxide_Init = echarts.init(document.getElementById("echartsCarbonDioxide_Init"));
	 	var echartsCarbonDioxide_Init_xAxisData = [];
	 	var echartsCarbonDioxide_Init_seriesData = [];
	 	//end co2曲线图
	 	
	 	//start pm10曲线图
	 	echartsPm10_Init = echarts.init(document.getElementById("echartsPm10_Init"));
	 	var echartsPm10_Init_xAxisData = [];
	 	var echartsPm10_Init_seriesData = [];
	 	//end pm10曲线图
	 	
	 	//start 一站甲醛
	 	echarts1Formaldehyde_Init = echarts.init(document.getElementById("echarts1Formaldehyde_Init"));
	 	var echarts1Formaldehyde_Init_seriesData = [];
	 	//end 一站甲醛
	 	
	 	//start 二站甲醛
	 	echarts2Formaldehyde_Init = echarts.init(document.getElementById("echarts2Formaldehyde_Init"));
	 	var echarts2Formaldehyde_Init_seriesData = [];
	 	//end 二站甲醛
	 	
	 	//start 三站甲醛
	 	echarts3Formaldehyde_Init = echarts.init(document.getElementById("echarts3Formaldehyde_Init"));
	 	var echarts3Formaldehyde_Init_seriesData = [];
	 	//end 三站甲醛
	 	
	 	//start 四站甲醛
	 	echarts4Formaldehyde_Init = echarts.init(document.getElementById("echarts4Formaldehyde_Init"));
	 	var echarts4Formaldehyde_Init_seriesData = [];
	 	//end 四站甲醛
	 	
	 	//start 五站甲醛
	 	echarts5Formaldehyde_Init = echarts.init(document.getElementById("echarts5Formaldehyde_Init"));
	 	var echarts5Formaldehyde_Init_seriesData = [];
	 	//end 五站甲醛
	 	
	 	//start 六站甲醛
	 	echarts6Formaldehyde_Init = echarts.init(document.getElementById("echarts6Formaldehyde_Init"));
	 	var echarts6Formaldehyde_Init_seriesData = [];
	 	//end 六站甲醛
	 	for(var i = 0;i<restmep.length;i++){
	 		echartsTemperature_Init_xAxisData.push(restmep[i].message.station_name);
	 		echartsTemperature_Init_seriesData.push(restmep[i].message.temperature);
	 		
	 		var radarIndicator = new Object();
	 		radarIndicator.name = restmep[i].message.station_name;
	 		radarIndicator.max=80;
	 		echartsHumidity_Init_xAxisData.push(radarIndicator);
	 		echartsHumidity_Init_seriesData.push(restmep[i].message.humidity);
	 		
	 		echartsPm25_Init_xAxisData.push(restmep[i].message.station_name);
	 		echartsPm25_Init_seriesData.push(restmep[i].message.pm25);
	 		
	 		echartsIlluminance_Init_xAxisData.push(restmep[i].message.station_name);
	 		echartsIlluminance_Init_seriesData.push(restmep[i].message.illuminance);
	 		
	 		
	 		var radarIndicator = new Object();
	 		radarIndicator.name = restmep[i].message.station_name;
	 		radarIndicator.max=800;
	 		echartsCarbonDioxide_Init_xAxisData.push(radarIndicator);
	 		echartsCarbonDioxide_Init_seriesData.push(restmep[i].message.carbonDioxide);
	 		
	 		echartsPm10_Init_xAxisData.push(restmep[i].message.station_name);
	 		echartsPm10_Init_seriesData.push(restmep[i].message.pm10);
	 		
	 		if(restmep[i].message.station==1){
	 			var echarts1Formaldehyde_Init_Object = new Object();
	 			echarts1Formaldehyde_Init_Object.name = restmep[i].message.station_name;
	 			echarts1Formaldehyde_Init_Object.center=['50%', '60%'];// 默认底部没有位置
	 			echarts1Formaldehyde_Init_Object.type = "gauge";
	 			echarts1Formaldehyde_Init_Object.detail = {formatter:'{value}'};
	 			echarts1Formaldehyde_Init_Object.min=0;
	 			echarts1Formaldehyde_Init_Object.max=0.24;
	 			echarts1Formaldehyde_Init_Object.data=[{value:restmep[i].message.formaldehyde,name:restmep[i].message.station_name}];
	 			echarts1Formaldehyde_Init_Object.radius = "95%";
	 			echarts1Formaldehyde_Init_Object.axisLine=
	 			{lineStyle: {width: 1,color:[[0.33,'green'],[0.66,'orange'],[1,'red']]}}; // 坐标轴线// 属性lineStyle控制线条样式
	 			echarts1Formaldehyde_Init_Object.axisTick = {length: 5,lineStyle: {color: 'auto'}};
	 			echarts1Formaldehyde_Init_Object.splitLine = {length: 10,lineStyle: {color: 'auto'}};
	 			echarts1Formaldehyde_Init_Object.axisLabel = 
	 			{backgroundColor: 'auto',
	 			borderRadius: 2,
	 			color: '#eee', 
	 			padding: 1, 
	 			textShadowBlur: 2,
	 			textShadowOffsetX: 1,
	 			textShadowOffsetY: 1,
	 			textShadowColor: '#222'};
	 			echarts1Formaldehyde_Init_Object.title = {fontWeight: 'bolder',fontSize: 15,fontStyle: 'italic'};
	 			echarts1Formaldehyde_Init_Object.pointer={width:1};
	 			echarts1Formaldehyde_Init_seriesData.push(echarts1Formaldehyde_Init_Object);
	 			console.log(echarts1Formaldehyde_Init_seriesData);
	 		}
	 		if(restmep[i].message.station==2){
	 			var echarts2Formaldehyde_Init_Object = new Object();
	 			echarts2Formaldehyde_Init_Object.name = restmep[i].message.station_name;
	 			echarts2Formaldehyde_Init_Object.center=['50%', '60%'];// 默认底部没有位置
	 			echarts2Formaldehyde_Init_Object.type = "gauge";
	 			echarts2Formaldehyde_Init_Object.min=0;
	 			echarts2Formaldehyde_Init_Object.max=0.24;
	 			echarts2Formaldehyde_Init_Object.detail = {formatter:'{value}'};
	 			echarts2Formaldehyde_Init_Object.data=[{value:restmep[i].message.formaldehyde,name:restmep[i].message.station_name}];
	 			echarts2Formaldehyde_Init_Object.radius = "95%";
	 			echarts2Formaldehyde_Init_Object.axisLine={lineStyle: {width: 1,color:[[0.33,'green'],[0.66,'orange'],[1,'red']]}}; // 坐标轴线// 属性lineStyle控制线条样式
	 			echarts2Formaldehyde_Init_Object.axisTick = {length: 5,lineStyle: {color: 'auto'}};
	 			echarts2Formaldehyde_Init_Object.splitLine = {length: 10,lineStyle: {color: 'auto'}};
	 			echarts2Formaldehyde_Init_Object.axisLabel = {backgroundColor: 'auto',borderRadius: 2,color: '#eee', padding: 1, textShadowBlur: 2,textShadowOffsetX: 1,textShadowOffsetY: 1,textShadowColor: '#222'};
	 			echarts2Formaldehyde_Init_Object.title = {fontWeight: 'bolder',fontSize: 15,fontStyle: 'italic'};
	 			echarts2Formaldehyde_Init_Object.pointer={width:1};
	 			echarts2Formaldehyde_Init_seriesData.push(echarts2Formaldehyde_Init_Object);
	 			console.log(echarts2Formaldehyde_Init_seriesData);
	 		}
	 		if(restmep[i].message.station==3){
	 			var echarts3Formaldehyde_Init_Object = new Object();
	 			echarts3Formaldehyde_Init_Object.name = restmep[i].message.station_name;
	 			echarts3Formaldehyde_Init_Object.center=['50%', '60%'];// 默认底部没有位置
	 			echarts3Formaldehyde_Init_Object.type = "gauge";
	 			echarts3Formaldehyde_Init_Object.min=0;
	 			echarts3Formaldehyde_Init_Object.max=0.24;
	 			echarts3Formaldehyde_Init_Object.detail = {formatter:'{value}'};
	 			echarts3Formaldehyde_Init_Object.data=[{value:restmep[i].message.formaldehyde,name:restmep[i].message.station_name}];
	 			echarts3Formaldehyde_Init_Object.radius = "95%";
	 			echarts3Formaldehyde_Init_Object.axisLine={lineStyle: {width: 1,color:[[0.33,'green'],[0.66,'orange'],[1,'red']]}}; // 坐标轴线// 属性lineStyle控制线条样式
	 			echarts3Formaldehyde_Init_Object.axisTick = {length: 5,lineStyle: {color: 'auto'}};
	 			echarts3Formaldehyde_Init_Object.splitLine = {length: 10,lineStyle: {color: 'auto'}};
	 			echarts3Formaldehyde_Init_Object.axisLabel = {backgroundColor: 'auto',borderRadius: 2,color: '#eee', padding: 1, textShadowBlur: 2,textShadowOffsetX: 1,textShadowOffsetY: 1,textShadowColor: '#222'};
	 			echarts3Formaldehyde_Init_Object.title = {fontWeight: 'bolder',fontSize: 15,fontStyle: 'italic'};
	 			echarts3Formaldehyde_Init_Object.pointer={width:1};
	 			echarts3Formaldehyde_Init_seriesData.push(echarts3Formaldehyde_Init_Object);
	 			console.log(echarts3Formaldehyde_Init_seriesData);
	 		}
	 		if(restmep[i].message.station==4){
	 			var echarts4Formaldehyde_Init_Object = new Object();
	 			echarts4Formaldehyde_Init_Object.name = restmep[i].message.station_name;
	 			echarts4Formaldehyde_Init_Object.center=['50%', '60%'];// 默认底部没有位置
	 			echarts4Formaldehyde_Init_Object.type = "gauge";
	 			echarts4Formaldehyde_Init_Object.min=0;
	 			echarts4Formaldehyde_Init_Object.max=0.24;
	 			echarts4Formaldehyde_Init_Object.detail = {formatter:'{value}'};
	 			echarts4Formaldehyde_Init_Object.data=[{value:restmep[i].message.formaldehyde,name:restmep[i].message.station_name}];
	 			echarts4Formaldehyde_Init_Object.radius = "95%";
	 			echarts4Formaldehyde_Init_Object.axisLine={lineStyle: {width: 1,color:[[0.33,'green'],[0.66,'orange'],[1,'red']]}}; // 坐标轴线// 属性lineStyle控制线条样式
	 			echarts4Formaldehyde_Init_Object.axisTick = {length: 5,lineStyle: {color: 'auto'}};
	 			echarts4Formaldehyde_Init_Object.splitLine = {length: 10,lineStyle: {color: 'auto'}};
	 			echarts4Formaldehyde_Init_Object.axisLabel = {backgroundColor: 'auto',borderRadius: 2,color: '#eee', padding: 1, textShadowBlur: 2,textShadowOffsetX: 1,textShadowOffsetY: 1,textShadowColor: '#222'};
	 			echarts4Formaldehyde_Init_Object.title = {fontWeight: 'bolder',fontSize: 15,fontStyle: 'italic'};
	 			echarts4Formaldehyde_Init_Object.pointer={width:1};
	 			echarts4Formaldehyde_Init_seriesData.push(echarts4Formaldehyde_Init_Object);
	 			console.log(echarts4Formaldehyde_Init_seriesData);
	 		}
	 		if(restmep[i].message.station==5){
	 			var echarts5Formaldehyde_Init_Object = new Object();
	 			echarts5Formaldehyde_Init_Object.name = restmep[i].message.station_name;
	 			echarts5Formaldehyde_Init_Object.center=['50%', '60%'];// 默认底部没有位置
	 			echarts5Formaldehyde_Init_Object.type = "gauge";
	 			echarts5Formaldehyde_Init_Object.min=0;
	 			echarts5Formaldehyde_Init_Object.max=0.24;
	 			echarts5Formaldehyde_Init_Object.detail = {formatter:'{value}'};
	 			echarts5Formaldehyde_Init_Object.data=[{value:restmep[i].message.formaldehyde,name:restmep[i].message.station_name}];
	 			echarts5Formaldehyde_Init_Object.radius = "95%";
	 			echarts5Formaldehyde_Init_Object.axisLine={lineStyle: {width: 1,color:[[0.33,'green'],[0.66,'orange'],[1,'red']]}}; // 坐标轴线// 属性lineStyle控制线条样式
	 			echarts5Formaldehyde_Init_Object.axisTick = {length: 5,lineStyle: {color: 'auto'}};
	 			echarts5Formaldehyde_Init_Object.splitLine = {length: 10,lineStyle: {color: 'auto'}};
	 			echarts5Formaldehyde_Init_Object.axisLabel = {backgroundColor: 'auto',borderRadius: 2,color: '#eee', padding: 1, textShadowBlur: 2,textShadowOffsetX: 1,textShadowOffsetY: 1,textShadowColor: '#222'};
	 			echarts5Formaldehyde_Init_Object.title = {fontWeight: 'bolder',fontSize: 15,fontStyle: 'italic'};
	 			echarts5Formaldehyde_Init_Object.pointer={width:1};
	 			echarts5Formaldehyde_Init_seriesData.push(echarts5Formaldehyde_Init_Object);
	 			console.log(echarts5Formaldehyde_Init_seriesData);
	 		}
	 		if(restmep[i].message.station==6){
	 			var echarts6Formaldehyde_Init_Object = new Object();
	 			echarts6Formaldehyde_Init_Object.name = restmep[i].message.station_name;
	 			echarts6Formaldehyde_Init_Object.center=['50%', '60%'];// 默认底部没有位置
	 			echarts6Formaldehyde_Init_Object.type = "gauge";
	 			echarts6Formaldehyde_Init_Object.min=0;
	 			echarts6Formaldehyde_Init_Object.max=0.24;
	 			echarts6Formaldehyde_Init_Object.detail = {formatter:'{value}'};
	 			echarts6Formaldehyde_Init_Object.data=[{value:restmep[i].message.formaldehyde,name:restmep[i].message.station_name}];
	 			echarts6Formaldehyde_Init_Object.radius = "95%";
	 			echarts6Formaldehyde_Init_Object.axisLine={lineStyle: {width: 1,color:[[0.33,'green'],[0.66,'orange'],[1,'red']]}}; // 坐标轴线// 属性lineStyle控制线条样式
	 			echarts6Formaldehyde_Init_Object.axisTick = {length: 5,lineStyle: {color: 'auto'}};
	 			echarts6Formaldehyde_Init_Object.splitLine = {length: 10,lineStyle: {color: 'auto'}};
	 			echarts6Formaldehyde_Init_Object.axisLabel = {backgroundColor: 'auto',borderRadius: 2,color: '#eee', padding: 1, textShadowBlur: 2,textShadowOffsetX: 1,textShadowOffsetY: 1,textShadowColor: '#222'};
	 			echarts6Formaldehyde_Init_Object.title = {fontWeight: 'bolder',fontSize: 15,fontStyle: 'italic'};
	 			echarts6Formaldehyde_Init_Object.pointer={width:1};
	 			echarts6Formaldehyde_Init_seriesData.push(echarts6Formaldehyde_Init_Object);
	 			console.log(echarts6Formaldehyde_Init_seriesData);
	 		}
	 		
	 	}
	 	//start 温度曲线图
	 	echartsTemperature_Init.setOption({
			title : {
				text : '温度（℃）',
				left : 'left'
			},
			tooltip : {
				enabled : true
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			xAxis : {
				type : 'category',
				data:echartsTemperature_Init_xAxisData
			},
			yAxis : {
				type: 'value'
			},
			series : [ {
				type : 'bar',
				label:{
					show:true
				},
				data:echartsTemperature_Init_seriesData
			} ]
		});//end 温度曲线图
		
		//start 湿度曲线图
	 	echartsHumidity_Init.setOption({
			title : {
				text : '湿度（%）',
				left : 'left'
			},
			tooltip : {
				enabled : true
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
		     radar: {
		        // shape: 'circle',
		        name: {
		            textStyle: {
		                color: '#fff',
		                backgroundColor: '#999',
		                borderRadius: 3,
		                padding: [3, 5]
		           }
		        },
		        indicator: echartsHumidity_Init_xAxisData
		    },
			
			series : [ {
				type : 'radar',
				label:{
					show:true
				},
				data:[{
					value:echartsHumidity_Init_seriesData
				}]
			} ]
		});//end 湿度曲线图
		
		//start pm25曲线图
	 	echartsPm25_Init.setOption({
			title : {
				text : 'PM2.5（μg/m³）',
				left : 'left'
			},
			tooltip : {
				enabled : true
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			xAxis : {
				type : 'category',
				data:echartsPm25_Init_xAxisData
			},
			yAxis : {
				type: 'value'
			},
			series : [ {
				type : 'line',
				label:{
					show:true
				},
				data:echartsPm25_Init_seriesData
			} ]
		});//end pm25曲线图
		
		//start 光照度曲线图
	 	echartsIlluminance_Init.setOption({
			title : {
				text : '光照度（lux）',
				left : 'left'
			},
			tooltip : {
				enabled : true
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			xAxis : {
				type : 'category',
				data:echartsIlluminance_Init_xAxisData
			},
			yAxis : {
				type: 'value'
			},
			series : [ {
				type : 'bar',
				label:{
					show:true
				},
				data:echartsIlluminance_Init_seriesData
			} ]
		});//end 温度曲线图
		
		//start co2曲线图
	 	echartsCarbonDioxide_Init.setOption({
			title : {
				text : '二氧化碳（ppm）',
				left : 'left'
			},
			tooltip : {
				enabled : true
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
		     radar: {
		        // shape: 'circle',
		        name: {
		            textStyle: {
		                color: '#fff',
		                backgroundColor: '#999',
		                borderRadius: 3,
		                padding: [3, 5]
		           }
		        },
		        indicator: echartsCarbonDioxide_Init_xAxisData
		    },
			
			series : [ {
				type : 'radar',
				label:{
					show:true
				},
				data:[{
					value:echartsCarbonDioxide_Init_seriesData
				}]
			} ]
		});//end co2曲线图
		
		//start pm10曲线图
	 	echartsPm10_Init.setOption({
			title : {
				text : 'PM10（μg/m³）',
				left : 'left'
			},
			tooltip : {
				enabled : true
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			xAxis : {
				type : 'category',
				data:echartsPm10_Init_xAxisData
			},
			yAxis : {
				type: 'value'
			},
			series : [ {
				type : 'line',
				label:{
					show:true
				},
				data:echartsPm10_Init_seriesData
			} ]
		});//end pm10曲线图
		
		//start 一站甲醛
	 	echarts1Formaldehyde_Init.setOption({
			title : {
				text : '甲醛（mg/m³）',
				left : 'left'
			},
			tooltip : {
				formatter: "{a} <br/>{b} : {c}"
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '0%',
		        
		        containLabel: true
		    },
			series : echarts1Formaldehyde_Init_seriesData
		});//end 一站甲醛
		
		//start 二站甲醛
	 	echarts2Formaldehyde_Init.setOption({
			tooltip : {
				formatter: "{a} <br/>{b} : {c}"
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			series : echarts2Formaldehyde_Init_seriesData
		});//end 二站甲醛
		
		//start 三站甲醛
	 	echarts3Formaldehyde_Init.setOption({
			tooltip : {
				formatter: "{a} <br/>{b} : {c}"
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			series : echarts3Formaldehyde_Init_seriesData
		});//end 三站甲醛
		
		//start 四站甲醛
	 	echarts4Formaldehyde_Init.setOption({
			tooltip : {
				formatter: "{a} <br/>{b} : {c}"
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			series : echarts4Formaldehyde_Init_seriesData
		});//end 四站甲醛
		
		//start 五站甲醛
	 	echarts5Formaldehyde_Init.setOption({
			tooltip : {
				formatter: "{a} <br/>{b} : {c}"
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			series : echarts5Formaldehyde_Init_seriesData
		});//end 五站甲醛
		
		//start 六站甲醛
	 	echarts6Formaldehyde_Init.setOption({
			tooltip : {
				formatter: "{a} <br/>{b} : {c}"
			},
			  grid: {
		        left: '1%',
		        right: '1%',
		        bottom: '3%',
		        containLabel: true
		    },
			series : echarts6Formaldehyde_Init_seriesData
		});//end 六站甲醛
	 }//end 当全部设备都老的时候，处理数据，显示到html
</script>
</head>
<body  class="easyui-layout">
	<div data-options ="region:'north',split:false" style ="height: 70px"> 
		<div class="easyui-layout" data-options="fit:true">
			<div data-options ="region:'west',split:false,border:false" style ="width: 33%;"> 
			</div>
			<div data-options ="region:'center',split:false,border:false" style="text-align: center;"> 
				<span style="font-weight: bold;font-size: 45px;">中本硕机电环境监测系统</span>
			</div>
			<div data-options ="region:'east',split:false,border:false"  style ="width: 33%;"> 
				<span id="Date" style="float: right;margin-right: 30px;font-size: 25px;margin-top: 15px"><span>
			</div>
		</div>
	</div>
	<div data-options ="region:'center',split:false"  > 
		<div class="easyui-layout" data-options="fit:true">
			<div data-options ="region:'north',split:false" style ="height: 33%;"> 
				<div class="easyui-layout" data-options="fit:true">
					<div data-options ="region:'west',split:false" style ="width: 35%;"> 
						<div id="echartsTemperature_Init" style="width: 100%;height: 100%;"> </div>
					</div>
					<div data-options ="region:'center',split:false"  > 
						<div id="echartsHumidity_Init" style="width: 100%;height: 100%;"> </div>
					</div>
					<div data-options ="region:'east',split:false"  style ="width: 35%;"> 
						<div id="echartsPm25_Init" style="width: 100%;height: 100%;"> </div>
					</div>
				</div>
			</div>
			<div data-options ="region:'center',split:false"  > 
				<div class="easyui-layout" data-options="fit:true">
					<div data-options ="region:'west',split:false,border:false" style ="width: 50%;"> 
						<div class="easyui-layout" data-options="fit:true,border:false">
							<div data-options ="region:'west',split:false,border:false" style ="width: 33%;"> 
								<div id="echarts1Formaldehyde_Init" style="width: 100%;height: 100%;"> </div>
							</div>
							<div data-options ="region:'center',split:false,border:false"  > 
								<div id="echarts2Formaldehyde_Init" style="width: 100%;height: 100%;"> </div>
							</div>
							<div data-options ="region:'east',split:false,border:false"  style ="width: 33%;"> 
								<div id="echarts3Formaldehyde_Init" style="width: 100%;height: 100%;"> </div>
							</div>
						</div>
					</div>
					<div data-options ="region:'center',split:false,border:false"  > 
						<div class="easyui-layout" data-options="fit:true">
							<div data-options ="region:'west',split:false,border:false" style ="width: 33%;"> 
								<div id="echarts4Formaldehyde_Init" style="width: 100%;height: 100%;"> </div>
							</div>
							<div data-options ="region:'center',split:false,border:false"  > 
								<div id="echarts5Formaldehyde_Init" style="width: 100%;height: 100%;"> </div>
							</div>
							<div data-options ="region:'east',split:false,border:false"  style ="width: 33%;"> 
								<div id="echarts6Formaldehyde_Init" style="width: 100%;height: 100%;"> </div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div data-options ="region:'south',split:false" style ="height: 33%;"> 
				<div class="easyui-layout" data-options="fit:true">
					<div data-options ="region:'west',split:false" style ="width: 35%;"> 
						<div id="echartsIlluminance_Init" style="width: 100%;height: 100%;"> </div>
					</div>
					<div data-options ="region:'center',split:false"  > 
						<div id="echartsCarbonDioxide_Init" style="width: 100%;height: 100%;"> </div>
					</div>
					<div data-options ="region:'east',split:false"  style ="width: 35%;"> 
						<div id="echartsPm10_Init" style="width: 100%;height: 100%;"> </div>
					</div>
				</div>
			</div>
			<div id="calldoorbell_dialog" class="easyui-dialog" title="访客"  style="width:520px;height: 560px;" data-options="closed:true,border:false,noheader:true">
				<img style="width: 520px;height: 560px " id="img_calldoorbell" src ="" alt="人脸图">
				<audio id="audio_calldoorbell" src="http://106.12.55.177/ZBSAttendance/mp3/10111.wav"  loop></audio>
			</div>
		</div>
	</div>
</body>
</html>
