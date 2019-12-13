/**
 * 查询安卓轮播图
 */
function findannouncement(){
	debugger;
	var bt = $('#bt_announcement').datebox("getValue");
	var et = $('#et_announcement').datebox("getValue");
	var title = $('#title_announcement').textbox("getValue");
	var options = $('#announcement_vv').datagrid('getPager').data("pagination").options;
	var company_id = company_id_select;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#announcement_vv").datagrid("loading");
	$.post(BASE_PATH + "/announcementController/findannouncement.do", {
		bNum : bNum,
		rows : rows,bt : bt,et:et,title:title,company_id:company_id
	}, function(data) {
		try {
			$("#announcement_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#announcement_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 新增公告
 */
function Addannouncement(){
	debugger;
	$('#announcement_add_dialog').dialog("open");
	$('#type_announcement_add').combobox({
		url:BASE_PATH+"/announcementController/findAnnouncementType.do",
		valueField:'type_id',
	    textField:'type',
	    onLoadSuccess: function(data) {
	    	debugger;
	    	if(data.length>0){
	    		$(this).combobox("select",data[0].type_id);
	    	}
	    }
	})
	$('#priority_announcement_add').combobox({
		url:BASE_PATH+"/announcementController/findPriority.do",
		valueField:'priority_id',
	    textField:'priority',
	    onLoadSuccess: function(data) {
	    	debugger;
	    	if(data.length>0){
	    		$(this).combobox("select",data[0].priority_id);
	    	}
	    }
	})
	
	$('#title_announcement_add').textbox("setValue","");
	$("#effectiveTime_announcement_add").datebox("setValue","");
	$("#state_announcement_add").combobox("setValue","picture");
	$('#adUrl_announcement_add').textbox("setValue","");
	$('#uploadImg_announcement').filebox("clear");
	$('#uploadFile_announcement').filebox("clear");
	$('#turn_img_announcement').hide();
	$('#turn_img_announcement').attr('src',"");

}
/**
 * 提交安卓轮播图
 */
function submitannouncementDialog(){
	debugger;
	var title = $('#title_announcement_add').textbox("getValue");
	var effectiveTime = $('#effectiveTime_announcement_add').datebox("getValue");
	var state = $('#state_announcement_add').combobox("getValue");
	var type = $('#type_announcement_add').combobox("getValue");
	var priority = $('#priority_announcement_add').combobox("getValue");
	var addressUrl = $('#adUrl_announcement_add').textbox("getValue");
	$('#announcement_company_id').val(company_id_select);
	if(!isNotNull(title)){
		$.messager.alert("消息提示","标题不能为空！","error");
		return ;
	}
	if(!isNotNull(state)){
		$.messager.alert("消息提示","跳转类型不能为空！","error");
		return ;
	}
	if(isNotNull(addressUrl)){
		if(!isURL(addressUrl)){
			$.messager.alert("消息提示","链接格式错误！","error");
			return ;
		}
	}
	if(!isNotNull(effectiveTime)){
		$.messager.alert("消息提示","失效时间不能为空！","error");
		return ;
	}
	$("#announcement_vv").datagrid("loading");
	$('#importImg_announcement').form('submit', {
		success:function(data){
			$("#announcement_vv").datagrid("loaded");
			debugger;
			var temp=JSON.parse(data);
			$.messager.alert('提示',temp.msg);
			if(temp.result==="success"){
				$('#announcement_add_dialog').dialog("close");
				findannouncement();
			}
			
		}
	});  
}
/**
 * 修改公告制度
 */
function updateannouncement(){
	debugger;
	var data = $('#announcement_vv').datagrid("getSelected");
	if(data==null){
		$.messager.alert("消息提示","请选择要修改的数据","error");
		return ;
	}
	var url = data.pictureUrl;
	$('#announcement_edit_dialog').dialog("open");
	$('#type_announcement_edit').combobox({
		url:BASE_PATH+"/announcementController/findAnnouncementType.do",
		valueField:'type_id',
	    textField:'type',
	    onLoadSuccess: function(datas) {
	    	debugger;
	    	if(datas.length>0){
	    		$(this).combobox("select",data.type_id);
	    	}
	    }
	})
	$('#priority_announcement_edit').combobox({
		url:BASE_PATH+"/announcementController/findPriority.do",
		valueField:'priority_id',
	    textField:'priority',
	    onLoadSuccess: function(datas) {
	    	debugger;
	    	if(datas.length>0){
	    		$(this).combobox("select",data.priority_id);
	    	}
	    }
	})
	$('#title_announcement_edit').textbox("setValue",data.title);
	$('#adUrl_announcement_edit').textbox("setValue",data.url);
	$("#effectiveTime_announcement_edit").datebox("setValue",data.effectiveTime);
	$("#state_announcement_edit").combobox("setValue",data.external_link);
	$('#uploadFile_announcement_edit').filebox("clear");
	$('#turn_img_edit_announcement').hide();
	$('#turn_img_edit_announcement').attr('src',"");
	$('#announcement_id').val(data.id);
	$('#announcement_picurl').val(data.url);
	if(data.external_link==="picture"){
		var picurl = "https://www.zhongbenshuo.com"+data.url.split("webapps")[1];
		picurl = picurl.replace(/\\/g,"/");
		console.log(picurl);
		$('#turn_img_show_announcement_div').show();
		$('#turn_img_show_announcement').attr('src',picurl);
	}else{
		$('#turn_img_show_announcement_div').hide();
	}
}
/**
 * 提交修改轮播图
 */
function submitannouncementDialogEdit(){
	debugger;
	var data = $('#announcement_vv').datagrid("getSelected");
	var id = data.id;
	$('#announcement_id').val(id);
	$('#announcement_picurl').val(data.url);
	var title = $('#title_announcement_edit').textbox("getValue");
	var effectiveTime = $('#effectiveTime_announcement_edit').datebox("getValue");
	var state = $('#state_announcement_edit').combobox("getValue");
	var type = $('#type_announcement_edit').combobox("getValue");
	var priority = $('#priority_announcement_edit').combobox("getValue");
	var addressUrl = $('#adUrl_announcement_edit').textbox("getValue");
	if(!isNotNull(title)){
		$.messager.alert("消息提示","标题不能为空！","error");
		return ;
	}
	if(!isNotNull(state)){
		$.messager.alert("消息提示","跳转类型不能为空！","error");
		return ;
	}
	
	if(state==="external_links"){
		if(!isURL(addressUrl)){
			$.messager.alert("消息提示","链接格式错误！","error");
			return ;
		}
	}
	if(!isNotNull(effectiveTime)){
		$.messager.alert("消息提示","失效时间不能为空！","error");
		return ;
	}
	$("#announcement_vv").datagrid("loading");
	$('#importImg_announcement_edit').form('submit', {
		success:function(data){
			$("#announcement_vv").datagrid("loaded");
			debugger;
			var temp=JSON.parse(data);
			$.messager.alert('提示',temp.msg);
			if(temp.result==="success"){
				$('#announcement_edit_dialog').dialog("close");
				findannouncement();
			}
			
		}
	}); 
}