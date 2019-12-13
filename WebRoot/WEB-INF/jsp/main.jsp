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
<title>中本硕机电办公系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="SHORTCUT ICON" 
	href="${pageContext.request.contextPath }/img/favicon.ico"/>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/topnav.css" />
<!-- <link rel="stylesheet" type="text/css" -->
<%-- 	href="${pageContext.request.contextPath }/css/main.css" /> --%>
<!-- <link rel="stylesheet" type="text/css" -->
<%-- 	href="${pageContext.request.contextPath }/css/kube.css" /> --%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/easyui/themes/bootstrap/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/easyui/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/main/main.js" ></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/common/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/common/jquery.jqprint-0.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/detailview.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/datagrid-dnd.js"></script>
<!-- <SCRIPT TYPE="TEXT/JAVASCRIPT" -->
<%-- 	SRC="${PAGECONTEXT.REQUEST.CONTEXTPATH }/JS/HIGHCHARTS/HIGHCHARTS.JS"></SCRIPT> --%>
<!-- <SCRIPT TYPE="TEXT/JAVASCRIPT" -->
<%-- 	SRC="${PAGECONTEXT.REQUEST.CONTEXTPATH }/JS/HIGHCHARTS/HIGHCHARTS-MORE.JS"></SCRIPT> --%>
<!-- <SCRIPT TYPE="TEXT/JAVASCRIPT" -->
<%-- 	SRC="${PAGECONTEXT.REQUEST.CONTEXTPATH }/JS/HIGHCHARTS/HIGHCHARTS-3D.JS"></SCRIPT> --%>
<!-- <SCRIPT TYPE="TEXT/JAVASCRIPT" -->
<%-- 	SRC="${PAGECONTEXT.REQUEST.CONTEXTPATH }/JS/HIGHCHARTS/HIGHCHARTS-ZH_CN.JS"></SCRIPT> --%>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/common/common.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/bankCheck.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/utils/utils.js"></script>
<script type="text/javascript"
	src="https://api.map.baidu.com/api?v=3.0&ak=74232c2e19c45420a1e61bf88d5a2017"></script>
	<!--加载鼠标绘制工具-->
<script type="text/javascript"
	src="https://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
<link rel="stylesheet"
	href="https://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
<!--加载检索信息窗口-->
<script type="text/javascript"
	src="https://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet"
	href="https://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
	<style>
		.m-box {width: 505px;display: inline-block;margin: 17px;}
		.m-table {border-collapse: collapse;}
		.m-table span {display: block;color: #4d4d4d;font-size: 14px;float: left;}
		.m-table span:first-child {padding-left: 10px;}
		.m-table td {border: 1px solid #dadada;width: 250px;line-height: 30px;height: 30px;}
		.m-time td span {color: #3d6dcc;font-size: 16px;font-weight: bold;height: 40px;line-height: 40px;}
		.m-icon1,.m-icon2,.m-icon3,.m-icon4,.m-icon5,.m-icon6{
			display:block;
			width:100px;
			height:50%;
			margin-top:7%;
		    vertical-align: bottom;
		}
		#sm_main .panel-header{background-color: #B2DFEE}
	</style>

<script type="text/javascript">
	$.ajaxSetup({
		  type: 'POST',
		  complete: function(xhr,status) {
			  debugger;
			  console.log("进入ajaxSetup");
		    var sessionStatus = xhr.getResponseHeader('sessionstatus');
		    if(sessionStatus == 'timeout') {
		    	console.log("进入session过期");
		    	console.log(window.top.location);
		      window.top.location.href = "https://www.zhongbenshuo.com/ZBSAttendance/LoginController/logout.do?init=timeout";  
		    }
		  }
		});
	var BASE_PATH = '<%=path%>';
	var BASE_PATH_ALL = '<%=basePath%>';
	var _module="";
	var loginUser = new Object();
	var company_id_select ;
	var deletePwd;
	loginUser.loginName = "${user.user_id}";
	loginUser.company_id = "${user.company_id}";
	loginUser.role = "${user.role}";
	var WEBSOCKET_PATH = '${websocketspath}';
	var websocket;
	var outAttendanceApproval;//外勤打卡审批
	var outAttendanceApply;//外勤打卡申请
	
	var appealAttendanceApproval;//补卡审批
	var appealAttendanceApply;//补卡申请
	
	var overTimeApproval;//加班审批
	var overTimeApply;//加班申请
	
	var vacationApproval = 0;//休假审批
	var vacationApply = 0;//休假申请
	var outGoingApproval = 0;//外出审批
	var outGoingApply = 0;//外出申请
	var businessTraveIApproval = 0;//出差审批
	var businessTraveIApply = 0;//出差申请
	var dataSidemenu = [];
var jichuxinxi= new Object();
	jichuxinxi.text = "基础信息";
	jichuxinxi.state = "open";
	jichuxinxi.children = [];
var jichuxinxiChildren = new Object();
	jichuxinxiChildren.text = "公司信息";
	jichuxinxiChildren.value = "/companyView.do";
	//jichuxinxiChildren.iconCls = "icon-companyInfo";
	if(loginUser.role!=null&&loginUser.role==1){
		jichuxinxi.children.push(jichuxinxiChildren);
	}
	
	jichuxinxiChildren = new Object();
	jichuxinxiChildren.text = "账号信息";
	jichuxinxiChildren.value = "/accountInformationView.do";
	//jichuxinxiChildren.iconCls = "icon-userInfo";
	jichuxinxi.children.push(jichuxinxiChildren);
	
	jichuxinxiChildren = new Object();
	jichuxinxiChildren.text = "版本信息";
	jichuxinxiChildren.value = "/versionView.do";
	//jichuxinxiChildren.iconCls = "icon-large-chart";
	jichuxinxi.children.push(jichuxinxiChildren);
	
	jichuxinxiChildren = new Object();
	jichuxinxiChildren.text = "日志信息";
	jichuxinxiChildren.value = "/loggerView.do";
	//jichuxinxiChildren.iconCls = "icon-loggerInfo";
	if(loginUser.role!=null&&loginUser.role==1){
		jichuxinxi.children.push(jichuxinxiChildren);
	}
	dataSidemenu.push(jichuxinxi);
	
var shenpixinxi= new Object();
	shenpixinxi.text = "审批信息";
	shenpixinxi.state = "open";
	shenpixinxi.children = [];
var shenpixinxiChildren = new Object();
	shenpixinxiChildren.text = "外勤打卡审批";
	shenpixinxiChildren.value = "/outAttendanceView.do";
	//shenpixinxiChildren.iconCls = "icon-OutAttendance";
	if(loginUser.role!=null&&loginUser.role==1){
		shenpixinxi.children.push(shenpixinxiChildren);
	}
	shenpixinxiChildren = new Object();
	shenpixinxiChildren.text = "补卡审批";
	shenpixinxiChildren.value = "/appealAttendanceView.do";
	//shenpixinxiChildren.iconCls = "icon-appeal";
	if(loginUser.role!=null&&loginUser.role==1){
		shenpixinxi.children.push(shenpixinxiChildren);
	}
	shenpixinxiChildren = new Object();
	shenpixinxiChildren.text = "加班审批";
	shenpixinxiChildren.value = "/overTimeView.do";
	//shenpixinxiChildren.iconCls = "icon-overTime";
	if(loginUser.role!=null&&loginUser.role==1){
		shenpixinxi.children.push(shenpixinxiChildren);
	}
	
	shenpixinxiChildren = new Object();
	shenpixinxiChildren.text = "假期审批";
	shenpixinxiChildren.value = "/vacationView.do";
	//shenpixinxiChildren.iconCls = "icon-vacation";
	if(loginUser.role!=null&&loginUser.role==1){
		shenpixinxi.children.push(shenpixinxiChildren);
	}
	shenpixinxiChildren = new Object();
	shenpixinxiChildren.text = "外出审批";
	shenpixinxiChildren.value = "/outGoingView.do";
	//shenpixinxiChildren.iconCls = "icon-vacation";
	if(loginUser.role!=null&&loginUser.role==1){
		shenpixinxi.children.push(shenpixinxiChildren);
	}
	shenpixinxiChildren = new Object();
	shenpixinxiChildren.text = "出差审批";
	shenpixinxiChildren.value = "/businessTravelView.do";
	//shenpixinxiChildren.iconCls = "icon-vacation";
	if(loginUser.role!=null&&loginUser.role==1){
		shenpixinxi.children.push(shenpixinxiChildren);
	}
	
	shenpixinxiChildren = new Object();
	shenpixinxiChildren.text = "预约审批";
	shenpixinxiChildren.value = "/visitorSubscribeView.do";
	//shenpixinxiChildren.iconCls = "icon-vacation";
	shenpixinxi.children.push(shenpixinxiChildren);
	
	dataSidemenu.push(shenpixinxi);
	
var gerenxinxi= new Object();
	gerenxinxi.text = "个人信息";
	gerenxinxi.state = "open";
	gerenxinxi.children = [];
	var gerenxinxiChildren = new Object();
	gerenxinxiChildren.text = "申请记录";
	gerenxinxiChildren.value = "/applyRecordView.do";
	//gerenxinxiChildren.iconCls = "icon-applyRecord";
	gerenxinxi.children.push(gerenxinxiChildren);
	
	gerenxinxiChildren = new Object();
	gerenxinxiChildren.text = "考勤记录";
	gerenxinxiChildren.value = "/attendanceRecordView.do";
	//gerenxinxiChildren.iconCls = "icon-attendanceRecord";
	gerenxinxi.children.push(gerenxinxiChildren);
	
	dataSidemenu.push(gerenxinxi);
	
var peizhixinxi= new Object();
	peizhixinxi.text = "配置信息";
	peizhixinxi.state = "open";
	peizhixinxi.children = [];
	var peizhixinxiChildren = new Object();
	peizhixinxiChildren.text = "定时任务";
	peizhixinxiChildren.value = "/configInforView.do";
	//peizhixinxiChildren.iconCls = "icon-configInfor";
	if(loginUser.role!=null&&loginUser.role==1){
		peizhixinxi.children.push(peizhixinxiChildren);
	}
	
	peizhixinxiChildren = new Object();
	peizhixinxiChildren.text = "假期设置";
	peizhixinxiChildren.value = "/holidaySettingsView.do";
	//peizhixinxiChildren.iconCls = "icon-holidaySettings";
	if(loginUser.role!=null&&loginUser.role==1){
		peizhixinxi.children.push(peizhixinxiChildren);
	}
	peizhixinxiChildren = new Object();
	peizhixinxiChildren.text = "考勤规则";
	peizhixinxiChildren.value = "/ruleView.do";
	//peizhixinxiChildren.iconCls = "icon-rule";
	if(loginUser.role!=null&&loginUser.role==1){
		peizhixinxi.children.push(peizhixinxiChildren);
	}
	
	peizhixinxiChildren = new Object();
	peizhixinxiChildren.text = "公告制度";
	peizhixinxiChildren.value = "/announcementView.do";
	//peizhixinxiChildren.iconCls = "icon-announcementInfo";
	if(loginUser.role!=null&&loginUser.role==1){
		peizhixinxi.children.push(peizhixinxiChildren);
	}
	
	peizhixinxiChildren = new Object();
	peizhixinxiChildren.text = "广告配置";
	peizhixinxiChildren.value = "/advertisementView.do";
	//peizhixinxiChildren.iconCls = "icon-advertisement";
	if(loginUser.role!=null&&loginUser.role==1&&loginUser.company_id==0){
		peizhixinxi.children.push(peizhixinxiChildren);
	}
	peizhixinxiChildren = new Object();
	peizhixinxiChildren.text = "轮播图配置";
	peizhixinxiChildren.value = "/homePagePictureView.do";
	//peizhixinxiChildren.iconCls = "icon-advertisement";
	if(loginUser.role!=null&&loginUser.role==1&&loginUser.company_id==0){
		peizhixinxi.children.push(peizhixinxiChildren);
	}
	
	peizhixinxiChildren = new Object();
	peizhixinxiChildren.text = "工资信息";
	peizhixinxiChildren.value = "/wageView.do";
	//peizhixinxiChildren.iconCls = "icon-advertisement";
	peizhixinxi.children.push(peizhixinxiChildren);
	dataSidemenu.push(peizhixinxi);
	

	/***
	 * 初始化tooltip
	 */
	function infoType(value,row,index){
		return "<div  class='easyui-panel easyui-tooltip'  title='"+value+"' >"+value+"</div>";
	}
	/***
	 * 初始化是否有效
	 */
	function effectiveType(value,row,index){
		if(value=="1"){
			value="有效";
		}else if(value=="0"){
			value="无效";
		}
		return value;
	}
	/***
	 * 初始化是否强制
	 */
	function forceType(value,row,index){
		if(value=="1"){
			value="强制";
		}else if(value=="0"){
			value="不强制";
		}
		return value;
	}
	/***
	 * 初始化审批记录结果
	 */
	function auditStatus(value,row,index){
		if(value=="2"){
			value="通过";
		}else if(value=="3"){
			value="不通过";
		}
		return value;
	}
	
	/***
	 * 初始化访客预约审批记录结果
	 */
	function auditVisitorStatus(value,row,index){
		if(value=="0"){
			value="未审核";
		}else if(value=="1"){
			value="审核通过";
		}else if(value=="2"){
			value="审核不通过";
		}
		return value;
	}
	/***
	 * 公司是否启用申请
	 */
	function enableApplication(value,row,index){
		if(value=="0"){
			value="未启用";
		}else if(value=="1"){
			value="启用";
		}
		return value;
	}
	/***
	 * 图片的查看按钮初始化
	 */
	function loadPicture(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='loadPictureDialog("+JSON.stringify(row.outAttendanceRecordPic)+")'>查看</a> ";
		return delVer;
	}
	/***
	 * 补卡图片的查看按钮初始化
	 */
	function loadPicture_Appeal(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='loadPictureDialog("+JSON.stringify(row.appealAttendanceRecordPic)+")'>查看</a> ";
		return delVer;
	}
	/***
	 * 加班申请图片的查看按钮初始化
	 */
	function loadPicture_OverTime(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='loadPictureDialog("+JSON.stringify(row.overTimeRecordPic)+")'>查看</a> ";
		return delVer;
	}
	/***
	 * 外出申请图片的查看按钮初始化
	 */
	function loadPicture_businessTraveI(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='loadPictureDialog("+JSON.stringify(row.businessTraveIRecordPic)+")'>查看</a> ";
		return delVer;
	}
	/***
	 * 出差申请图片的查看按钮初始化
	 */
	function loadPicture_outGoing(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='loadPictureDialog("+JSON.stringify(row.outGoingRecordPic)+")'>查看</a> ";
		return delVer;
	}
	
	/***
	 * 假期申请图片的查看按钮初始化
	 */
	function loadPicture_vacation(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='loadPictureDialog("+JSON.stringify(row.vacationRecordPic)+")'>查看</a> ";
		return delVer;
	}
	/**
	*已经外勤审批的记录
	*/
	function audit_record(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='audit_recordDialog("+JSON.stringify(row.outAttendanceInfoAuditRecord)+")'>查看</a> ";
		return delVer;
	}
	/**
	*已经补卡审批的记录
	*/
	function audit_record_Appeal(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='audit_recordDialog("+JSON.stringify(row.appealAttendanceInfoAuditRecord)+")'>查看</a> ";
		return delVer;
	}
	/**
	*已经加班审批的记录
	*/
	function audit_record_OverTime(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='audit_recordDialog("+JSON.stringify(row.overTimeAuditRecord)+")'>查看</a> ";
		return delVer;
	}
	/**
	*已经假期审批的记录
	*/
	function audit_record_vacation(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='audit_recordDialog("+JSON.stringify(row.vacationAuditRecord)+")'>查看</a> ";
		return delVer;
	}
	/**
	*已经外出审批的记录
	*/
	function audit_record_outGoing(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='audit_recordDialog("+JSON.stringify(row.outGoingAuditRecord)+")'>查看</a> ";
		return delVer;
	}
	/**
	*已经出差审批的记录
	*/
	function audit_record_businessTraveI(value,row,index){
		debugger;
		var delVer = "<a href='#' onclick='audit_recordDialog("+JSON.stringify(row.businessTraveIAuditRecord)+")'>查看</a> ";
		return delVer;
	}
	/**
	*假期设置状态
	*/
	function holidayStatus(value,row,index){
		debugger;
		var res = "";
		if(value==0){
			res = "工作日";
		}else if(value==1){
			res = "休息日";
		}else if(value==2){
			res = "节假日";
		}
		return res;
	}
	/**
	*已读未读
	*/
	function readNotRead(value,row,index){
		debugger;
			var res = "";
			if(value==0){
				res = "未读";
			}else if(value==1){
				res = "已读";
			}
			return res;
	}
	/***
	 * 初始化性别
	 */
	function genderType(value,row,index){
		if(value=="1"){
			value="男";
		}else if(value=="2"){
			value="女";
		}else if(value=="3"){
			value="其它";
		}
		return value;
	}
		/***
	 * 初始化员工类型
	 */
	function employeeType(value,row,index){
		if(value=="1"){
			value="事务所";
		}else if(value=="0"){
			value="非事务所";
		}
		return value;
	}
	/***
	 * 初始化权限
	 */
	function roleType(value,row,index){
		if(value=="1"){
			value="管理员";
		}else if(value=="0"){
			value="普通用户";
		}
		return value;
	}
	/**
	*审批过的历史记录
	*/
	function audit_recordDialog(temp){
		debugger;
		$('#audit_record_dialog').dialog("open");
		$("#audit_record_dialog").empty();
		var res="";
			if(temp.length==0){
				res+='<span style="color:red">没有审批记录!</span>';
			}else{
				for(var i = 0;i<temp.length;i++){
					var data = temp[i];
					var status = "";
					if(data.audit_status==2){
						status = "通过";
					}else if(data.audit_status==3){
						status = "不通过";
					}else if(data.audit_status==1||data.audit_status==0){
						status = "待审批";
					}
					res+='<div style="margin: 10px;margin-top:40px">'+
							'审批人  &nbsp&nbsp:<input  class="easyui-textbox" value="'+data.user_name+'" style="width: 300px;">'+
						'</div>'+
						'<div style="margin: 10px;">'+
							'审批结果:<input  class="easyui-textbox" value="'+status+'" style="width: 300px;">'+
						'</div>'+
						'<div style="margin: 10px;">'+
							'审批时间:<input  class="easyui-textbox" value="'+data.audit_time+'" style="width: 300px;">'+
						'</div>'+
						'<div style="margin: 10px;">'+
							'审批备注:<input data-options="multiline:true" class="easyui-textbox" value="'+data.audit_remarks+'" style="width: 300px;height:60px">'+
						'</div>';
				}
			}
			$("#audit_record_dialog").append(res);
			$.parser.parse("#audit_record_dialog");
				
		
	}
	/***
	 * 图片的查看
	 */
	function loadPictureDialog(temp){
		debugger;
 		$('#loadpictrue_dialog').dialog("open");
		$("#picturedialog").empty();
		var res = "";
		var picData = temp;
			if(picData.length<=0){
				res = "<span style='color:red'>没有图片</span>";
				$("#picturedialog").append(res);
				$('#loadNext').linkbutton("disable");
				$('#loadup').linkbutton("disable");
			}else{
				var picurl = "https://www.zhongbenshuo.com"+picData[0].url.split("webapps")[1];
				picurl = picurl.replace(/\\/g,"/");
				console.log(picurl);
				res+='<img id="turn_img_shenpi" width="550" height="400" src="'+picurl+'"/>'
				$('#showNumber').val(0);
				$('#picObject').val(JSON.stringify(temp));
				$("#picturedialog").append(res);
				$.parser.parse("#picturedialog");
				if(picData.length==1){
					$('#loadNext').linkbutton("disable");
				}else{
					$('#loadNext').linkbutton("enable");
				}
			}
	}
	/**
	*下一张
	*/
	function loadNext(){
	debugger;
		var data = JSON.parse($('#picObject').val());
		var number = $('#showNumber').val();
		number = parseInt(number)+1;
		$('#showNumber').val(number);
		var picurl = "https://www.zhongbenshuo.com"+data[number].url.split("webapps")[1];
		picurl = picurl.replace(/\\/g,"/");
		console.log(picurl);
		$('#turn_img_shenpi').attr('src',picurl);
		if(data.length-1==number){
			$('#loadNext').linkbutton("disable");
		}
		$('#loadup').linkbutton("enable");
	}
	/**
	*上一张
	*/
	function loadup(){
	debugger;
		var data = JSON.parse($('#picObject').val());
		var number = $('#showNumber').val();
		number = parseInt(number)-1;
		$('#showNumber').val(number);
		var picurl = "https://www.zhongbenshuo.com"+data[number].url.split("webapps")[1];
		picurl = picurl.replace(/\\/g,"/");
		console.log(picurl);
		$('#turn_img_shenpi').attr('src',picurl);
		if(number==0){
			$('#loadup').linkbutton("disable");
		}
		$('#loadNext').linkbutton("enable");
	}
	/***
	 * 初始内容过长
	 */
	function addressType(value,row,index){
		 return '<div style="width=150px;word-break:break-all; word-wrap:break-word;white-space:pre-wrap;">'+value+'</div>';
	}
	function showNewFrame(viewname, viewurl) {
		debugger;
		var url = BASE_PATH + viewurl;
		if ($('#mainTab').tabs('exists', viewname)) {
			$('#mainTab').tabs('select', viewname);
// 			$('#mainTab').tabs('getSelected').panel({
// 				href : url
// 			});
			//$('#mainTab').tabs('getSelected').panel('refresh');
		} else {
			$('#mainTab').tabs('add', {
				title : viewname,
				href : url,
				closable : true
			});
		}
	}
	$(function() {
		//启动websocket
		if(websocket!=null){
			if(websocket.readyState!=1){
           		//alert("未连接。");
           		ConnectWebSocket();
           		return;
           	}else {
           		$.messager.show({title:'提示',msg:'通讯已经连接！'}); 
           	}
		}else {
			ConnectWebSocket();
		}
		//监听右键事件，创建右键菜单
		$('#mainTab').tabs({
			onSelect : function(title, index) {
				debugger;
			},
			onContextMenu : function(e, title, index) {
			debugger;
				e.preventDefault();
				if (index > 0) {
					$('#mainTabmm').menu('show', {
						left : e.pageX,
						top : e.pageY
					}).data("tabTitle", title);
				}
			}
		});
		//右键菜单click
		$("#mainTabmm").menu({
			onClick : function(item) {
				closeTab(this, item.name);
			}
		});
		$("#mmInfo").menu({
			onClick : function (item) {
				debugger;
			}
		})
		searchNews();
		$('#sm_main').sidemenu({
			onSelect:function(item){
				debugger;
				showNewFrame(item.text,item.value);
			}
		})
	});
	//刚开始载入中。。。。。。。。。。。
	var shadow_init;
	function lodingcloses(){
		$("#Loading").fadeOut("normal",function(){
			$(this).remove();
		});
	}
	$.parser.onComplete = function (res) {
		if(shadow_init) clearTimeout(shadow_init);
		shadow_init = setTimeout(lodingcloses, 1000);
		companyInit();
	};
	function companyInit(){
		$('#company_main').combobox({
				url:BASE_PATH+"/LoginController/compang_main.do?company_id="+ loginUser.company_id ,
				valueField:'id',
			    textField:'text',
			    onLoadSuccess: function(data) {
			    	debugger;
			    	if(data.length>0){
			    		$(this).combobox("select",data[0].id);
			    	}
			    },
		        onSelect:function(record){
		        	debugger;
		        	company_id_select = record.id;
		        	/**查询当前公司下的操作密码**/
		        	$.post(BASE_PATH + "/LoginController/findDeletePwd.do", {
						company_id : company_id_select
					}, function(data) {
						var temp = JSON.parse(data);
						if(temp.result==="success"){
							deletePwd = temp.msg;
						}
					});
					/**如果当前tab存在账号信息的把权限信息刷新下*/
					if($('#mainTab').tabs('exists',"账号信息")){
						findAuthority();
						findAccountInfo();
					}
					/**存在假期设置，就刷新数据*/
					if($('#mainTab').tabs('exists',"假期设置")){
						findWorkingTime();
					}
					
					
		        }
			})
	}
	</script>
</head>
<body id="mainlayout" class="easyui-layout">
	<div id='Loading'
		style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#DDDDDB;text-align:center;padding-top: 20%;">
		<h1>
			<font color="#15428B">加载中···</font>
		</h1>
	</div>
	<div data-options="region:'north',border:false" style="height:73px;">
		<div class="m-top">
<!-- 			<a class="u-logo" href="javascript:void(0);"></a>  -->
			<img
				src="${pageContext.request.contextPath }/img/title_android.png"> <a
				class="m-btn222" href="${pageContext.request.contextPath }/LoginController/logout.do">退出系统</a>
			<p class="u-loginuser" style="font-family:'微软雅黑'">
				<c:if test="${user.role!=null&&user.role==1}">
				管理员：
				</c:if>
				<c:if test="${user.role!=null&&user.role==0}">
				普通用户：
				</c:if>
				&nbsp;
				<c:if test="${user!=null}"> ${user.user_name} </c:if>
				&nbsp;
			</p>
		</div>
	</div>
	<div data-options="region:'center'">
		<div id="main_layout" class="easyui-layout" data-options="fit:true">
			<div id="main_west_title"
				data-options="region:'west',split:true,collapsible:true"
				style="width: 225px; text-align: center;" title="快捷菜单">
				<div id="sm_main" class="easyui-sidemenu" data-options="data:dataSidemenu"></div>
			</div>
			<div data-options="region:'center'">
				<div  id="tab-tools">
					<a href="javascript:void(0)" class="easyui-linkbutton" style="width: 150px;" id="newInfoMain" onclick="newInfoMainClick();"   data-options="
					iconCls:'icon-newInfoNo'">最新消息</a>
					<select id="company_main" class="easyui-combobox" editable="true" style="width: 180px;"></select>
				</div>
				<div id="mainTab" class="easyui-tabs" data-options="fit:true,tools:'#tab-tools'">
					<div title="主页" style="width: 100%; height: 100%; padding: 10px; ">
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="outAttendanceLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('外勤打卡审批','/outAttendanceView.do');"
								data-options="iconCls:'icon-OutAttendance',size:'large',iconAlign:'top'">外勤打卡审批</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="applyRecordLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('申请记录','/applyRecordView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">外勤打卡申请记录</a>
						</div>
						
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="appealAttendanceLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('补卡审批','/appealAttendanceView.do');"
								data-options="iconCls:'icon-OutAttendance',size:'large',iconAlign:'top'">补卡审批</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="appealRecordLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('申请记录','/applyRecordView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">补卡申请记录</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="overTimeLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('加班审批','/overTimeView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">加班审批</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="overTimeApplyLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('申请记录','/applyRecordView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">加班申请记录</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="vacationLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('假期审批','/vacationView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">假期审批</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="vacationApplyLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('申请记录','/applyRecordView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">假期申请记录</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="outGoingLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('外出审批','/outGoingView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">外出审批</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="outGoingApplyLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('申请记录','/applyRecordView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">外出申请记录</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="businessTravelLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('出差审批','/businessTravelView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">出差审批</a>
						</div>
						<div style="margin-top: 10px;margin-left: 10px;display: inline;">
							<a style="height: 60px;" href="#" id="businessTravelApplyLinkbutton" class="easyui-linkbutton"
								onclick="showNewFrame('申请记录','/applyRecordView.do');"
								data-options="iconCls:'icon-applyRecord',size:'large',iconAlign:'top'">出差申请记录</a>
						</div>
					</div>
				</div>
				<div id="mainTabmm" class="easyui-menu" style="width: 120px;">
					<div id="mm-tabcloseall" data-options="name:2">全部关闭</div>
					<div id="mm-tabcloseother" data-options="name:3">除此之外全部关闭</div>
					<div class="menu-sep"></div>
					<div id="mm-tabcloseright" data-options="name:4">当前页左侧全部关闭</div>
					<div id="mm-tabcloseleft" data-options="name:5">当前页右侧全部关闭</div>
				</div>
				<div id="loadpictrue_dialog" class="easyui-dialog" title="图片的显示" style="width: 700px;height: 500px"  data-options="closed:true">
					<input id="showNumber" type="hidden">
					<input id="picObject" type="hidden">
					<div id="picturedialog" align="center" style="width: 100%;height: 90%">
					</div>
					<div  align="center" style="width: 100%;height: 10%">
						<a href="javascript:void(0)" id="loadup" data-options="disabled:true" class="easyui-linkbutton"
							 onclick="loadup()" style="margin:0 auto">上一张</a>
						<a href="javascript:void(0)" id="loadNext" class="easyui-linkbutton"
							 onclick="loadNext()" style="margin:0 auto">下一张</a>
					</div>
				</div>
				
				<div id="audit_record_dialog" class="easyui-dialog" title="审批记录" style="width: 400px;height: 600px"  data-options="closed:true">
					
					
				</div>
			</div>
		</div>
	</div>
</body>
</html>
