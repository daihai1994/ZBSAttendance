/***
 * 查询考勤记录和应勤天数
 */
function findAttendanceRecord(){
	debugger;
	attendanceRecord = null;
	var year = $('#attendanceRecordCalendar').calendar('options').year;
	var month = $('#attendanceRecordCalendar').calendar('options').month;
	var company_id = company_id_select;
	var user_id = loginUser.loginName;
	$.post(BASE_PATH + "/AttendanceRecordController/findAttendanceRecord.do", {
		year : year,
		month : month,
		company_id : company_id,
		user_id : user_id
	}, function(data) {
		try {
			debugger;
			var temp = JSON.parse(data);
			attendanceRecord = temp;
			$.parser.parse();
			$('#dg_attendanceRecord').datagrid("loadData",attendanceRecord.workingTime);
		} catch (e) {
			console.log(e);
		}
		
	});
}
function rowStylerAttendance(index,row){
	 if (row.status ==0){
		 if(row.working>=8&&row.overtimeTime==0&&row.vacationTime==0){
			 return 'background-color:green;font-weight:bold;'; 
		 }else if(row.working<8&&row.overtimeTime==0&&row.vacationTime==0){
			 return 'background-color:red;font-weight:bold;'; 
		 }else if(row.vacationTime>0){
			 return 'background-color:blue;font-weight:bold;'; 
		 }else if(row.overtimeTime>0){
			 return 'background-color:#BCEE68;font-weight:bold;'; 
		 }
     }else {
    	 if(row.overtimeTime>0){
    		 return 'background-color:#BCEE68;font-weight:bold;'; 
    	 }else {
    		 return 'background-color:#EEAEEE;font-weight:bold;'; 
    	 }
     }
}
/**
 * 打开导出考勤记录页面
 */
function repoetAttendanceRecordForm(){
	$('#repoetAttendanceRecord_dialog').dialog("open");
	var newDate = new Date();
	$("#repoetAttendanceRecord_year").numberspinner("setValue",newDate.getFullYear());
	$("#repoetAttendanceRecord_month").numberspinner("setValue",(newDate.getMonth()+1));
	$('#user_reportForm').combobox({
		url:BASE_PATH+"/AttendanceRecordController/findUser.do?company_id="+ company_id_select ,
	    onLoadSuccess: function(data) {
	    	debugger;
	    	if(data.length>0){
	    		$(this).combobox("select",data[0].id);
	    	}
	    },
	})
}
/**
 * 提交导出考勤记录
 */
function submitAttendanceExport(){
	$('#attendanceRecort_reportForm_company_id').val(company_id_select);
	$('#exportReport_attendanceRecord').form('submit', {
		success:function(data){
			debugger;
			
		}
	})
}

