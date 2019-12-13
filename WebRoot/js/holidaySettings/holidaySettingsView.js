/**
 * 查询年月下的工作日
 */
function findWorkingTime(){
	debugger;
	var company_id = company_id_select;
	var year = $('#year_holidy').numberspinner("getValue");
	var month = $('#month_holidy').combobox("getValue");
	if(isNotNull(year)&&isNotNull(month)){
		$('#holidayInfo_vv').datagrid("loading");
		$.post(BASE_PATH + "/HolidaySettingController/findWorkingTime.do", {
			year : year,
			month : month,
			company_id:company_id
		}, function(data) {
			try {
				$("#holidayInfo_vv").datagrid("loaded");
				var temp = JSON.parse(data);
				$("#holidayInfo_vv").datagrid("loadData",temp);
			} catch (e) {
				console.log(e);
			}
			
		});
	}
}