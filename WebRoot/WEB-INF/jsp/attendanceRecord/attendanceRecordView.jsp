<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE>
<html>
<head>
</head>
<body>
<div class ="easyui-layout" data-options="fit:true">
	<div data-options ="region:'west',split:false"  style ="width: 50%;"> 
		 <div class="easyui-calendar" data-options="formatter:formatDay" id="attendanceRecordCalendar" style="width:100%;height:100%;"></div>
	</div>
	<div data-options ="region:'center',split:false" > 
		<div id="toolbar_attendanceRecord" >
			<a  onclick="repoetAttendanceRecordForm()" class="easyui-linkbutton" style="width: 100px;margin: 0.2rem 0.2rem 0.2rem">导出考勤记录</a>
		</div>
		<table id="dg_attendanceRecord" class="easyui-datagrid" style="width: 100%;height: 100%" 
			data-options="
						singleSelect: true,
						rownumbers:true,
						striped: true,
						fitColumns:true,
						toolbar: '#toolbar_attendanceRecord',
						rowStyler: rowStylerAttendance">
			<thead>
				<tr>
					<th data-options="field:'user_name'" style="width: 20%">用户名称</th>
					<th data-options="field:'date'" style="width: 20%">日期</th>
					<th data-options="field:'working'" style="width: 20%">工作时间</th>
					<th data-options="field:'overtimeTime'" style="width: 20%">加班时间</th>
					<th data-options="field:'vacationTime'" style="width: 20%">请假时间</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="repoetAttendanceRecord_dialog" class="easyui-dialog" title="考勤记录" style="width:600px;height: 300px;padding:10px;" data-options="modal:true,closed:true">
		<form id="exportReport_attendanceRecord" action="${pageContext.request.contextPath}/AttendanceRecordController/exportexportReportAttendanceRecord.do">
		<input id="attendanceRecort_reportForm_company_id" name = "attendanceRecort_reportForm_company_id" type="hidden">
			<div style="margin: 10px;">
				<select id="user_reportForm" name = "user_reportForm" class="easyui-combobox"  
					label="员工:" style="width: 450px;">
				</select>
			</div>
			<div style="margin: 10px;">
				<div style="margin: 10px;">
					<input class="easyui-numberspinner" label="时间:(年)" data-options="editable:false" id="repoetAttendanceRecord_year"
					 name = "repoetAttendanceRecord_year"   style="width:185px;">
					~(月)<input class="easyui-numberspinner" data-options="editable:false" min="1" max = "12" 
					id="repoetAttendanceRecord_month" name = "repoetAttendanceRecord_month"   style="width:185px;">
				</div>
			</div>
			<div style="margin: 40px;text-align: center;">
				<a  onclick="submitAttendanceExport()" class="easyui-linkbutton" style="width: 100px;">导出</a>
				<a  onclick="$('#repoetAttendanceRecord_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
			</div>
		</form>
	</div>
</div>
       
<script type="text/javascript" src="${pageContext.request.contextPath }/js/attendanceRecord/attendanceRecordView.js">
</script>
<script>
var attendance_record_init= true;
var attendanceRecord = null;
var yearAll = null;
var monthAll = null;
function formatDay(date){
	debugger
	var y = date.getFullYear();
	var m = date.getMonth()+1+"";
	if(m.length<2){
		m = "0"+m;
	}
      var d = date.getDate()+"";
      if(d.length<2){
		d = "0"+d;
	}
      var time = y+"-"+m+"-"+d;
      var res = "";
      if(attendanceRecord==null||attendanceRecord.map==null){
      	return '<div><h1>' + d + '</h1></div>';
      }else{
      	if(isNotNull(attendanceRecord.map[time])){
      		
      		 res =  '<div style="display: inline;margin-left: 60px"><h1>' + d + '</h1></div>';
      		 		switch (attendanceRecord.map[time].am) {
						case 1://1是正常
						res+=	'<div  style="width:25px;height:25px;border:1px solid green;border-radius:25px;line-height:25px;text-align:center;color:green;display: inline;">正</div>';
							break;
						case 2://迟到早退
						res+=	'<div  style="width:25px;height:25px;border:1px  #FFA500;border-radius:25px;line-height:25px;text-align:center;color:#FFA500;display: inline;">迟/早</div>';	
							break;
						case 3://外勤正常
						res+=	'<div  style="width:25px;height:25px;border:1px solid #63B8FF;border-radius:25px;line-height:25px;text-align:center;color:#63B8FF;display: inline;">外</div>';	
							break;
						case 4://外勤待审批
						res+=	'<div  style="width:25px;height:25px;border:1px solid #7A67EE;border-radius:25px;line-height:25px;text-align:center;color:#7A67EE;display: inline;">待</div>';	
							break;
						case 5://外勤审批中
						res+=	'<div  style="width:25px;height:25px;border:1px solid #7A67EE;border-radius:25px;line-height:25px;text-align:center;color:#7A67EE;display: inline;">审</div>';		
							break;
						case 6://审批失败
						res+=	'<div  style="width:25px;height:25px;border:1px solid #EE1289;border-radius:25px;line-height:25px;text-align:center;color:#EE1289;display: inline;">拒</div>';
							break;
						case 7://缺卡
						res+=	'<div  style="width:25px;height:25px;border:1px solid red;border-radius:25px;line-height:25px;text-align:center;color:red;display: inline;">缺</div>';	
							break;
						case -1://默认
							break;
						default:
							break;
						}
						switch (attendanceRecord.map[time].pm) {
						case 1://1是正常
						res+=	'<div  style="width:25px;height:25px;border:1px solid green;border-radius:25px;line-height:25px;text-align:center;color:green;margin-left:20px;display: inline;">正</div>';
							break;
						case 2://迟到早退
						res+=	'<div  style="width:25px;height:25px;border:1px  #FFA500;border-radius:25px;line-height:25px;text-align:center;color:#FFA500;display: inline;margin-left:15px;">迟/早</div>';	
							break;
						case 3://外勤正常
						res+=	'<div  style="width:25px;height:25px;border:1px solid #63B8FF;border-radius:25px;line-height:25px;text-align:center;color:#63B8FF;display: inline;margin-left:20px;">外</div>';	
							break;
						case 4://外勤待审批
						res+=	'<div  style="width:25px;height:25px;border:1px solid #7A67EE;border-radius:25px;line-height:25px;text-align:center;color:#7A67EE;display: inline;margin-left:20px;">待</div>';	
							break;
						case 5://外勤审批中
						res+=	'<div  style="width:25px;height:25px;border:1px solid #7A67EE;border-radius:25px;line-height:25px;text-align:center;color:#7A67EE;display: inline;margin-left:20px;">审</div>';		
							break;
						case 6://审批失败
						res+=	'<div  style="width:25px;height:25px;border:1px solid #EE1289;border-radius:25px;line-height:25px;text-align:center;color:#EE1289;display: inline;margin-left:20px;">拒</div>';
							break;
						case 7://缺卡
						res+=	'<div  style="width:25px;height:25px;border:1px solid red;border-radius:25px;line-height:25px;text-align:center;color:red;display: inline;margin-left:20px;">缺</div>';	
							break;
						case -1://默认
							break;
						default:
							break;
						}
				return res;      
      	}else{
      		return '<div><h1>' + d + '</h1></div>';
      	}
      }
  }
$.parser.onComplete = function (res) {
	if(attendance_record_init){
		attendance_record_init=false;
		$('#user_reportForm').combobox({
			valueField:'id',
		    textField:'text'
		})
		$('#attendanceRecordCalendar').calendar({
			onChange:function(n,o){
				debugger;
				
			},
			onNavigate:function(n,o){//改变月份，年分的时候触发事件
				debugger;
				if(isNotNull(yearAll)){
					if(n===yearAll&&o===monthAll){
					
					}else{
						yearAll = n;
						monthAll = o;
						findAttendanceRecord();
					}
				}else{
						yearAll = n;
						monthAll = o;
						findAttendanceRecord();
				}
				
			}
		})
		findAttendanceRecord();
	}
};
</script>
</body>
</html>
