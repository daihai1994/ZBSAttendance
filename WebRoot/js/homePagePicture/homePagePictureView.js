/**
 * 查询安卓轮播图
 */
function findhomePagePicture(){
	debugger;
	var bt = $('#bt_homePagePicture').datebox("getValue");
	var et = $('#et_homePagePicture').datebox("getValue");
	var remarks = $('#remarks_homePagePicture').textbox("getValue");
	var options = $('#homePagePicture_vv').datagrid('getPager').data("pagination").options;
	var company_id = company_id_select;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#homePagePicture_vv").datagrid("loading");
	$.post(BASE_PATH + "/homePagePictureController/findhomePagePicture.do", {
		bNum : bNum,
		rows : rows,bt : bt,et:et,remarks:remarks,company_id:company_id
	}, function(data) {
		try {
			$("#homePagePicture_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#homePagePicture_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 新增安卓轮播图
 */
function AddHomePagePicture(){
	debugger;
	$('#homePagePicture_add_dialog').dialog("open");
	$('#remarks_homePagePicture_add').textbox("setValue","");
	$("#effective_advertisement_add").combobox("setValue",1);
	$("#state_homePagePicture_add").combobox("setValue",0);
	$('#adUrl_homePagePicture_add').textbox("setValue","");
	$('#uploadImg_homePagePicture').filebox("clear");
	$('#uploadFile_homePagePicture').filebox("clear");
	$('#turn_img_homePagePicture').hide();
	$('#turn_img_homePagePicture').attr('src',"");

}
/**
 * 提交安卓轮播图
 */
function submithomePagePictureDialog(){
	debugger;
	var remarks = $('#remarks_homePagePicture_add').textbox("getValue");
	var effective = $('#effective_homePagePicture_add').combobox("getValue");
	var state = $('#state_homePagePicture_add').combobox("getValue");
	var addressUrl = $('#adUrl_homePagePicture_add').textbox("getValue");
	$('#homePagePicture_company_id').val(company_id_select);
	if(!isNotNull(remarks)){
		$.messager.alert("消息提示","简介不能为空！","error");
		return ;
	}
	
	if(isNotNull(addressUrl)){
		if(!isURL(addressUrl)){
			$.messager.alert("消息提示","链接格式错误！","error");
			return ;
		}
	}
	$("#homePagePicture_vv").datagrid("loading");
	$('#importImg_homePagePicture').form('submit', {
		success:function(data){
			$("#homePagePicture_vv").datagrid("loaded");
			debugger;
			var temp=JSON.parse(data);
			$.messager.alert('提示',temp.msg);
			if(temp.result==="success"){
				$('#homePagePicture_add_dialog').dialog("close");
				findhomePagePicture();
			}
			
		}
	});  
}


/***
 * 修改轮播图
 */
function updateHomePagePicture(){
	debugger;
	var data = $('#homePagePicture_vv').datagrid("getSelected");
	if(data==null){
		$.messager.alert("消息提示","请选择要修改的数据","error");
		return ;
	}
	var url = data.pictureUrl;
	$('#homePagePicture_edit_dialog').dialog("open");
	$('#remarks_homePagePicture_edit').textbox("setValue",data.remarks);
	$('#adUrl_homePagePicture_edit').textbox("setValue",data.addressUrl);
	$("#effective_homePagePicture_edit").combobox("setValue",data.effective);
	$("#state_homePagePicture_edit").combobox("setValue",data.state);
	$('#uploadImg_homePagePicture_edit').filebox("clear");
	$('#uploadFile_homePagePicture_edit').filebox("clear");
	$('#turn_img_edit_homePagePicture').hide();
	$('#turn_img_edit_homePagePicture').attr('src',"");
	var picurl = "https://www.zhongbenshuo.com"+url.split("webapps")[1];
	picurl = picurl.replace(/\\/g,"/");
	console.log(picurl);
	$('#turn_img_show_homePagePicture').attr('src',picurl);
}
/**
 * 提交修改轮播图
 */
function submithomePagePictureDialogEdit(){
	debugger;
	var data = $('#homePagePicture_vv').datagrid("getSelected");
	var id = data.id;
	$('#homePagePicture_id').val(id);
	$('#homePagePicture_picurl').val(data.pictureUrl);
	var remarks = $('#remarks_homePagePicture_edit').textbox("getValue");
	var effective = $('#effective_homePagePicture_edit').combobox("getValue");
	var state = $('#state_homePagePicture_edit').combobox("getValue");
	var addressUrl = $('#adUrl_homePagePicture_edit').textbox("getValue");
	$('#homePagePicture_company_id').val(company_id_select);
	if(!isNotNull(remarks)){
		$.messager.alert("消息提示","简介不能为空！","error");
		return ;
	}
	if(state==1){
		if(isNotNull(addressUrl)){
			if(!isURL(addressUrl)){
				$.messager.alert("消息提示","链接格式错误！","error");
				return ;
			}
		}
	}
	$("#homePagePicture_vv").datagrid("loading");
	$('#importImg_homePagePicture_edit').form('submit', {
		success:function(data){
			$("#homePagePicture_vv").datagrid("loaded");
			debugger;
			var temp=JSON.parse(data);
			$.messager.alert('提示',temp.msg);
			if(temp.result==="success"){
				$('#homePagePicture_edit_dialog').dialog("close");
				findhomePagePicture();
			}
			
		}
	}); 
}