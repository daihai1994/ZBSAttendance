/***
 * 查询定时任务时间
 */
function findTimmerCron(){
	var company_id = company_id_select;
	$('#dg_timmer_xitongpeizhi').datagrid('loading');
	$.post(BASE_PATH+"/ConfigInforController/findTimmerCron.do", 
			{company_id:company_id},function (res) {
					debugger;
					$('#dg_timmer_xitongpeizhi').datagrid('loaded');
					var temp = JSON.parse(res);
					$('#dg_timmer_xitongpeizhi').datagrid('loadData',temp);
				}, 'text');
}
/***
 * 新增定时任务时间
 */
function AddTimmerCron(){
	$('#dlg_xitongpeizhi_add').dialog('open');
	$('#timmerMode_xitongpeizhi').combobox("setValue","每天");
}
/***
 * 监测jobName的唯一性
 */
function findJobName(){
	var jobName = $('#jobName_xitongpeizhi').textbox('getValue');
	if(jobName!=null && jobName!=""){
		$.post(BASE_PATH+"/serviceAdmin/findJobName.do", 
				{jobName:jobName},function (res) {
						debugger;
						if(res==="1"){
							$.messager.alert('提示','任务名已存在!');
							$('#jobName_xitongpeizhi').textbox('setValue',"");
							return
						} else {
							$.messager.alert('提示','任务名可以使用!');
							return
						}
					}, 'text');
	} else {
		$.messager.alert('提示','任务名不能为空!');
		return
	}
}
/***
 * 定时任务的保存
 */
function save_timmer(){
	debugger;
	var type= $('#timmerMode_xitongpeizhi').combobox('getValue');
	var week = $('#week_xitongpeizhi').combobox('getValue');
	var hour = $('#hour_xitongpeizhi').combobox('getValue');
	var minute = $('#minute_xitongpeizhi').combobox('getValue');
	var time = $('#bt_dtbox_month_xitongpeizhi').datetimebox('getValue');
	var ttimmer = "1900-01-01";
	var name= "";
	var cron = "";
	if(type==="每隔几秒钟"){
		ttimmer = ttimmer +" 00:00:"+minute;
		cron = "*/"+minute+" * * * * ?";
		name = ttimmer.substring(17, 19);
		name = "每隔"+name+"秒钟";
	}
	if(type==="每隔几分钟"){
		ttimmer = ttimmer +" 00:"+minute+":00";
		cron = "0 */"+minute+" * * * ?";
		name = ttimmer.substring(14, 16);
		name = "每隔"+name+"分钟";
	}
	if(type==="每隔几小时"){
		ttimmer = ttimmer +" "+hour+":00:00";
		cron = "0 0 */"+hour+" * * ?";
		name = ttimmer.substring(11, 13);
		name = "每隔"+name+"小时";
	}
	if(type==="每天"){
		ttimmer = ttimmer +" "+hour+":"+minute+":00";
		cron = "0 "+minute+" "+hour+" * * ?";
		name = ttimmer.substring(11, 19);
		name = "每天"+name;
	}
	if(type==="每周"){
		ttimmer = ttimmer +" "+hour+":"+minute+":00";
		cron = "0 "+minute+" "+hour+" ? * "+week;
		switch (parseInt(week)) {
		case 1:
			name = "周日";
			break;
		case 2:
			name = "周一";			
			break;
		case 3:
			name = "周二";
			break;
		case 4:
			name = "周三";
			break;
		case 5:
			name = "周四";
			break;
		case 6:
			name = "周五";
			break;
		case 7:
			name = "周六";
			break;
		default:
			break;
		}
		name = name+" "+ttimmer.substring(11, 19);
	}
	if(type==="每月"){
		debugger;
		ttimmer = time.toString();
		if(ttimmer!=undefined){
			var day = parseInt(ttimmer.substring(8,10)).toString();
			var hour1 = parseInt(ttimmer.substring(11,13)).toString();
			var minute1 = parseInt(ttimmer.substring(14,16)).toString();
			cron = "0 "+minute1+" "+hour1+" "+day+" * ?";
			name = "每月"+day+"号"+hour1+"时"+hour1+"分";
		}
	}
	if(type==="每年"){
		debugger;
		ttimmer = time.toString();
		if(ttimmer!=undefined){
			var month = parseInt(ttimmer.substring(5,7)).toString();
			var day = parseInt(ttimmer.substring(8,10)).toString();
			var hour1 = parseInt(ttimmer.substring(11,13)).toString();
			var minute1 = parseInt(ttimmer.substring(14,16)).toString();
			cron = "0 "+minute1+" "+hour1+" "+day+" "+month+"  ? *";
			name = "每年"+month+"月"+day+"号"+hour1+"时"+hour1+"分";
		}
	}
	if(name==null || name===""){
		$.messager.alert('提示','任务名不能为空!');
		return
	}
	$.post(BASE_PATH+"/ConfigInforController/save_timmer.do", 
			{mode:type,ttime:ttimmer,cron:cron,jobName:name},function (res) {
					debugger;
					if(res==="-1"){
						$.messager.alert('提示','存在相同的定时时间!');
						return
					}
					if(res==="1"){
						$.messager.alert('提示','新增成功!');
						findTimmerCron();
						$('#dlg_xitongpeizhi_add').dialog('close');
						return
					} else {
						$.messager.alert('提示','新增失败!');
						return
					}
				}, 'text');
};
/***
 * 修改定时任务
 */
function updateTimmerCron(){
	debugger;
	var node  = $('#dg_timmer_xitongpeizhi').datagrid('getSelected');
	if(node!=null){
		$('#id_timmer_xitongpeizhi').val(node.id);
		$('#timmerMode_xitongpeizhi_update').combobox('setValue',node.mode);
		if(node.mode==="每隔几秒钟"){
			$('#hour_xitongpeizhi_update').next().hide();
			$('#minute_xitongpeizhi_update').next().show();
			$('#week_xitongpeizhi_update').next().hide();
			$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
			var arr=node.cron.split(" ")[0];
			var arr1 = arr.split("/")[1];
			$('#minute_xitongpeizhi_update').combobox('setValue',arr1);
		}
		if(node.mode==="每隔几分钟"){
			$('#hour_xitongpeizhi_update').next().hide();
			$('#minute_xitongpeizhi_update').next().show();
			$('#week_xitongpeizhi_update').next().hide();
			$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
			var arr=node.cron.split(" ")[1];
			var arr1 = arr.split("/")[1];
			$('#minute_xitongpeizhi_update').combobox('setValue',arr1);
		}
		if(node.mode==="每隔几小时"){
			$('#hour_xitongpeizhi_update').next().show();
			$('#minute_xitongpeizhi_update').next().hide();
			$('#week_xitongpeizhi_update').next().hide();
			$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
			var arr=node.cron.split(" ")[2];
			var arr1 = arr.split("/")[1];
			$('#hour_xitongpeizhi_update').combobox('setValue',arr1);
		}
		
		
		if(node.mode==="每天"){
			$('#hour_xitongpeizhi_update').next().show();
			$('#minute_xitongpeizhi_update').next().show();
			$('#week_xitongpeizhi_update').next().hide();
			$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
			var arr=node.cron.split(" ");
			$('#hour_xitongpeizhi_update').combobox('setValue',arr[2]);
			$('#minute_xitongpeizhi_update').combobox('setValue',arr[1]);
		}
		if(node.mode==="每周"){
			$('#hour_xitongpeizhi_update').next().show();
			$('#minute_xitongpeizhi_update').next().show();
			$('#week_xitongpeizhi_update').next().show();
			$('#bt_dtbox_month_xitongpeizhi_update').next().hide();
			var arr=node.cron.split(" ");
			$('#hour_xitongpeizhi_update').combobox('setValue',arr[2]);
			$('#minute_xitongpeizhi_update').combobox('setValue',arr[1]);
			$('#week_xitongpeizhi_update').combobox('setValue',arr[5]);
		}
		if(node.mode==="每月"){
			$('#hour_xitongpeizhi_update').next().hide();
			$('#minute_xitongpeizhi_update').next().hide();
			$('#week_xitongpeizhi_update').next().hide();
			$('#bt_dtbox_month_xitongpeizhi_update').next().show();
			$('#bt_dtbox_month_xitongpeizhi_update').datetimebox('setValue',node.ttime);
		}
		if(node.mode==="每年"){
			$('#hour_xitongpeizhi_update').next().hide();
			$('#minute_xitongpeizhi_update').next().hide();
			$('#week_xitongpeizhi_update').next().hide();
			$('#bt_dtbox_month_xitongpeizhi_update').next().show();
			$('#bt_dtbox_month_xitongpeizhi_update').datetimebox('setValue',node.ttime);
		}
		$('#jobName_xitongpeizhi_update').textbox('setValue',node.jobName);
		$('#dlg_xitongpeizhi_update').dialog('open');
	}else {
		$.messager.alert('提示','请选择要修改的数据!');
		return
	}
}
/***
 * 修改定时任务保存
 */
function update_timmer(){
	debugger;
	var id = $('#id_timmer_xitongpeizhi').val();
	var type= $('#timmerMode_xitongpeizhi_update').combobox('getValue');
	var week = $('#week_xitongpeizhi_update').combobox('getValue');
	var hour = $('#hour_xitongpeizhi_update').combobox('getValue');
	var minute = $('#minute_xitongpeizhi_update').combobox('getValue');
	var time = $('#bt_dtbox_month_xitongpeizhi_update').datetimebox('getValue');
	var ttimmer = "1900-01-01";
	var name= "";
	var cron = "";
	if(type==="每隔几秒钟"){
		ttimmer = ttimmer +" 00:00:"+minute;
		cron = "*/"+minute+" * * * * ?";
		name = ttimmer.substring(17, 19);
		name = "每隔"+name+"秒钟";
	}
	if(type==="每隔几分钟"){
		ttimmer = ttimmer +" 00:"+minute+":00";
		cron = "* */"+minute+" * * * ?";
		name = ttimmer.substring(14, 16);
		name = "每隔"+name+"分钟";
	}
	if(type==="每隔几小时"){
		ttimmer = ttimmer +" "+hour+":00:00";
		cron = "* * */"+hour+" * * ?";
		name = ttimmer.substring(11, 13);
		name = "每隔"+name+"小时";
	}
	if(type==="每天"){
		ttimmer = ttimmer +" "+hour+":"+minute+":00";
		cron = "0 "+minute+" "+hour+" * * ?";
		name = ttimmer.substring(11, 19);
		name = "每天"+name;
	}
	if(type==="每周"){
		ttimmer = ttimmer +" "+hour+":"+minute+":00";
		cron = "0 "+minute+" "+hour+" ? * "+week;
		switch (parseInt(week)) {
		case 1:
			name = "周日";
			break;
		case 2:
			name = "周一";			
			break;
		case 3:
			name = "周二";
			break;
		case 4:
			name = "周三";
			break;
		case 5:
			name = "周四";
			break;
		case 6:
			name = "周五";
			break;
		case 7:
			name = "周六";
			break;
		default:
			break;
		}
		name = name+" "+ttimmer.substring(11, 19);
	}
	if(type==="每月"){
		debugger;
		ttimmer = time.toString();
		if(ttimmer!=undefined){
			var day = parseInt(ttimmer.substring(8,10)).toString();
			var hour1 = parseInt(ttimmer.substring(11,13)).toString();
			var minute1 = parseInt(ttimmer.substring(14,16)).toString();
			cron = "0 "+minute1+" "+hour1+" "+day+" * ?";
			name = "每月"+day+"号"+hour1+"时"+hour1+"分";
		}
	}
	if(type==="每年"){
		debugger;
		ttimmer = time.toString();
		if(ttimmer!=undefined){
			var month = parseInt(ttimmer.substring(5,7)).toString();
			var day = parseInt(ttimmer.substring(8,10)).toString();
			var hour1 = parseInt(ttimmer.substring(11,13)).toString();
			var minute1 = parseInt(ttimmer.substring(14,16)).toString();
			cron = "0 "+minute1+" "+hour1+" "+day+" "+month+"  ? *";
			name = "每年"+month+"月"+day+"号"+hour1+"时"+hour1+"分";
		}
	}
	$.post(BASE_PATH+"/ConfigInforController/update_timmer.do", 
			{mode:type,ttime:ttimmer,cron:cron,id:id,jobName:name},function (res) {
					debugger;
					if(res==="-1"){
						$.messager.alert('提示','存在相同的定时时间!');
						return
					}
					if(res==="1"){
						$.messager.alert('提示','修改成功!');
						findTimmerCron();
						$('#dlg_xitongpeizhi_update').dialog('close');
						return
					} else {
						$.messager.alert('提示','新增失败!');
						return
					}
				}, 'text');
}
/***
 * 删除定时任务
 */
function deleteTimmerCron(){
	$.messager.prompt('请示','请输入删除密码？',function(r){
		debugger;
		if(r==deletePwd){
			var node  = $('#dg_timmer_xitongpeizhi').datagrid('getSelected');
			if(node!=null){
				$.post(BASE_PATH+"/ConfigInforController/deleteTimmerCron.do", 
						{id:node.id},function (res) {
							debugger;
							if(res==="-1"){
								$.messager.alert('提示',' 有定时任务使用此时间，无法删除!');
								return
							}
							if(res==="1"){
								$.messager.alert('提示','删除成功!');
								findTimmerCron();
								return
							} else {
								$.messager.alert('提示','删除失败!');
								return
							}
						}, 'text');
			}else {
				$.messager.alert('提示','请选择要删除的数据!');
				return
			}
		}else{
			$.messager.alert("提示",'密码错误');
		}
	})
}

/***
 * 查询计算工时定时任务
 */
function findTimmerCron_configure(){
	var company_id = company_id_select;
	$('#dg_timmer_configure_xitongpeizhi').datagrid('loading');
	$.post(BASE_PATH+"/ConfigInforController/findTimmerCron_configure.do", 
			{company_id:company_id},function (res) {
					debugger;
					$('#dg_timmer_configure_xitongpeizhi').datagrid('loaded');
					var temp = JSON.parse(res);
					$('#dg_timmer_configure_xitongpeizhi').datagrid('loadData',temp);
				}, 'text');
}
/***
 * 新增计算工时定时任务
 */
function AddTimmerCron_configure(){
	$('#dlg_xitongpeizhi_configureAdd').dialog("open");
	$('#timerName_xitongpeizhi').textbox("setValue","");
	$('#combotimerCron_xitongpeizhi').combobox({
		url:BASE_PATH+"/ConfigInforController/findTimmer.do",
	})
}
/***
 * 提交计算工时定时任务
 */
function configureAdd_timmer(){
	var company_id = company_id_select;
	var timmer_id = $('#combotimerCron_xitongpeizhi').combobox("getValue");
	var timer_name = $('#timerName_xitongpeizhi').textbox("getValue");
	
	$.post(BASE_PATH+"/ConfigInforController/configureAdd_timmer.do", 
			{company_id:company_id,timmer_id:timmer_id,timer_name:timer_name,group_id:1},function (res) {
					debugger;
					try {
						var temp = JSON.parse(res);
						$.messager.alert("提示",temp.msg);
						if(temp.result==="success"){
							$('#dlg_xitongpeizhi_configureAdd').dialog("close");
							findTimmerCron_configure();
						}
					} catch (e) {
						console.log(e);
					}
				}, 'text');
}
/***
 * 修改计算工时定时任务
 */
function updateTimmerCron_configure(){
	var data = $('#dg_timmer_configure_xitongpeizhi').datagrid("getSelected");
	if(data!=null){
		$('#dlg_xitongpeizhi_configureupdate').dialog("open");
		$('#id_configureAdd_xitongpeizhi').val(data.id);
		$('#timerName_update_xitongpeizhi').textbox("setValue",data.timerName);
		$('#combotimerCron_update_xitongpeizhi').combobox({
			url:BASE_PATH+"/ConfigInforController/findTimmer.do",
			 onLoadSuccess: function(res) {
				 $('#combotimerCron_update_xitongpeizhi').combobox("setValue",data.timmer_id); 
			 }
		})
	}else{
		$.messager.alert("提示","请选择要修改的数据");
		return;
	}
}
/***
 * 提交修改计算工时定时任务
 */
function configureupdate_timmer(){
	var timmer_id = $('#combotimerCron_update_xitongpeizhi').combobox("getValue");
	var timer_name = $('#timerName_update_xitongpeizhi').textbox("getValue");
	var id = $('#id_configureAdd_xitongpeizhi').val();
	$.post(BASE_PATH+"/ConfigInforController/configureupdate_timmer.do", 
			{id:id,timmer_id:timmer_id,timer_name:timer_name},function (res) {
					debugger;
					try {
						var temp = JSON.parse(res);
						$.messager.alert("提示",temp.msg);
						if(temp.result==="success"){
							$('#dlg_xitongpeizhi_configureupdate').dialog("close");
							findTimmerCron_configure();
						}
					} catch (e) {
						console.log(e);
					}
				}, 'text');
}
/***
 * 删除定时任务
 */
function deleteTimmerCron_configure(){
	$.messager.prompt('请示','请输入删除密码？',function(r){
		debugger;
		if(r==deletePwd){
			var node  = $('#dg_timmer_configure_xitongpeizhi').datagrid('getSelected');
			if(node!=null){
				$.post(BASE_PATH+"/ConfigInforController/deleteTimmerCron_configure.do", 
						{id:node.id},function (res) {
							debugger;
							var temp = JSON.parse(res);
							$.messager.alert("提示",temp.msg);
							if(temp.result==="success"){
								findTimmerCron_configure();
							}
						}, 'text');
			}else {
				$.messager.alert('提示','请选择要删除的数据!');
				return
			}
		}else{
			$.messager.alert("提示",'密码错误');
		}
	})
}