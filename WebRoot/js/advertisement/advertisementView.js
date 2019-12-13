/***
 * 查询广告配置
 */
function findAdvertisement(){
	debugger;
	var bt = $('#bt_advertisement').datebox("getValue");
	var et = $('#et_advertisement').datebox("getValue");
	var name = $('#remarks_advertisement').textbox("getValue");
	var options = $('#advertisement_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#advertisement_vv").datagrid("loading");
	$.post(BASE_PATH + "/AdvertisementController/findAdvertisement.do", {
		bNum : bNum,
		rows : rows,bt : bt,et:et,name:name
	}, function(data) {
		try {
			$("#advertisement_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#advertisement_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 新增广告
 */
function AddAdvertisement(){
	$('#advertisement_add_dialog').dialog("open");
	$('#name_advertisement_add').textbox("setValue","");
	$('#showTime_advertisement_add').numberspinner("setValue",1);
	$('#adUrl_advertisement_add').textbox("setValue","");
	$('#start_time_advertisement_add').datebox("setValue","");
	$('#end_time_advertisement_add').datebox("setValue","");
	$("#effective_advertisement_add").combobox("setValue",1);
	$("#force_advertisement_add").combobox("setValue",0);
	$('#uploadImg_advertisement').filebox("clear");
	$('#turn_img').hide();
	$('#turn_img').attr('src',"");
}
/***
 * 提交新增广告
 */
function submitAdvertisementDialog(){
	var name = $('#name_advertisement_add').textbox("getValue");
	var showTime = $('#showTime_advertisement_add').numberspinner("getValue");
	var adUrl = $('#adUrl_advertisement_add').textbox("getValue");
	var start_time = $('#start_time_advertisement_add').datebox("getValue");
	var end_time = $('#end_time_advertisement_add').datebox("getValue");
	var newDate = new Date();
	var nowDate = newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+newDate.getDate();
	if(!isNotNull(name)){
		$.messager.alert("消息提示","简介不能为空！","error");
		return ;
	}
	if(!isNotNull(showTime)){
		$.messager.alert("消息提示","展示时间最少1秒！","error");
		return ;
	}
	if(!isNotNull(adUrl)){
		$.messager.alert("消息提示","链接不能为空！","error");
		return ;
	}else{
		if(!isURL(adUrl)){
			$.messager.alert("消息提示","链接格式错误！","error");
			return ;
		}
	}
	if(!timeCompare(start_time,nowDate)){
		$.messager.alert("消息提示","开始日期不能小于系统时间","error");
		return ;
	}
	if(!timeCompare(end_time,start_time)){
		$.messager.alert("消息提示","结束日期不能比开始日期小","error");
		return ;
	}
	
	$("#advertisement_vv").datagrid("loading");
	$('#importImg_advertisement').form('submit', {
		success:function(data){
			$("#advertisement_vv").datagrid("loaded");
			debugger;
			var temp=JSON.parse(data);
			$.messager.alert('提示',temp.msg);
			if(temp.result==="success"){
				$('#advertisement_add_dialog').dialog("close");
				findAdvertisement();
			}
			
		}
	});  
}
/***
 * 修改广告
 */
function updateAdvertisement(){
	debugger;
	var data = $('#advertisement_vv').datagrid("getSelected");
	if(data==null){
		$.messager.alert("消息提示","请选择要修改的数据","error");
		return ;
	}
	var url = data.picUrl;
	
	$('#advertisement_edit_dialog').dialog("open");
	$('#name_advertisement_edit').textbox("setValue",data.name);
	$('#showTime_advertisement_edit').numberspinner("setValue",data.showTime);
	$('#adUrl_advertisement_edit').textbox("setValue",data.adUrl);
	$('#start_time_advertisement_edit').datebox("setValue",data.start_time);
	$('#end_time_advertisement_edit').datebox("setValue",data.end_time);
	$("#effective_advertisement_edit").combobox("setValue",data.effective);
	$("#force_advertisement_edit").combobox("setValue",data.force);
	$('#uploadImg_advertisement_edit').filebox("clear");
	$('#turn_img_edit').hide();
	$('#turn_img_edit').attr('src',"");
	var picurl = "https://www.zhongbenshuo.com"+url.split("webapps")[1];
	picurl = picurl.replace(/\\/g,"/");
	console.log(picurl);
	$('#turn_img_show').attr('src',picurl);
}
/***
 * 提交修改广告
 */
function submitAdvertisementDialogEdit(){
	debugger;
	var data = $('#advertisement_vv').datagrid("getSelected");
	var id = data.id;
	$('#advertisement_id').val(id);
	$('#advertisement_picurl').val(data.picUrl);
	var name = $('#name_advertisement_edit').textbox("getValue");
	var showTime = $('#showTime_advertisement_edit').numberspinner("getValue");
	var adUrl = $('#adUrl_advertisement_edit').textbox("getValue");
	var start_time = $('#start_time_advertisement_edit').datebox("getValue");
	var end_time = $('#end_time_advertisement_edit').datebox("getValue");
	var newDate = new Date();
	var nowDate = newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+newDate.getDate();
	if(!isNotNull(name)){
		$.messager.alert("消息提示","简介不能为空！","error");
		return ;
	}
	if(!isNotNull(showTime)){
		$.messager.alert("消息提示","展示时间最少1秒！","error");
		return ;
	}
	if(!isNotNull(adUrl)){
		$.messager.alert("消息提示","链接不能为空！","error");
		return ;
	}else{
		if(!isURL(adUrl)){
			$.messager.alert("消息提示","链接格式错误！","error");
			return ;
		}
	}
	if(!timeCompare(start_time,nowDate)){
		$.messager.alert("消息提示","开始日期不能小于系统时间","error");
		return ;
	}
	if(!timeCompare(end_time,start_time)){
		$.messager.alert("消息提示","结束日期不能比开始日期小","error");
		return ;
	}
	
	$("#advertisement_vv").datagrid("loading");
	$('#importImg_advertisement_edit').form('submit', {
		success:function(data){
			$("#advertisement_vv").datagrid("loaded");
			debugger;
			var temp=JSON.parse(data);
			$.messager.alert('提示',temp.msg);
			if(temp.result==="success"){
				$('#advertisement_edit_dialog').dialog("close");
				findAdvertisement();
			}
			
		}
	});  

	
}