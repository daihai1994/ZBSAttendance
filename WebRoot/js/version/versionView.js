/* 上传文件页面 */
function uploadVersion() {
	$('#upload_vvv').window('open');
	$('#apkTypeId').combobox({
		url:BASE_PATH+"/VersionController/findApkTypeId.do", 
	      	valueField:'id',
	      	textField:'text',
	});
}

//上传新版本文件
function updload_ver() {
    $.messager.progress({
        title: '请稍后',
        msg: '文件上传中...'
    });
    $('#upload_ver').form('submit', {
        success: function(data) {
            if (data == "success") {
                $.messager.alert("提示", "上传成功");
                findVersion();
            } else {
                $.messager.alert("提示", "上传失败");
            }
            $.messager.progress('close');
            $('#upload_vvv').window('close');
            $("#file").filebox("clear");
            $('#apkTypeId').textbox('setValue', "");
            $('#versionCode').textbox('setValue', "");
            $('#versionName').textbox('setValue', "");
            $('#versionType').combobox('setValue', "stable");
            $('#versionLog').textbox('setValue', "");
        }
    });
}

//查询版本信息
function findVersion() {
	var options = $('#versionInfo_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	var eNum = page * rows;
	/*$('#LoginData_umv').datagrid('loadData', []);*/
	var versionType = "all";
	$("#versionInfo_vv").datagrid("loading");
	$.post(BASE_PATH + "/VersionController/findVersionLog.do", {
		bNum : bNum,
		rows : rows,
		versionType : versionType
	}, function(data) {
		try {
			var temp = JSON.parse(data);
			$('#versionInfo_vv').datagrid('loadData', temp);
			$("#versionInfo_vv").datagrid("loaded");
		} catch (e) {
			console.log(e);
		}
		
	});
}

//下载版本信息
function downVersion() {
	var rowItemCustomer = $('#versionInfo_vv').datagrid('getSelected');
	if (rowItemCustomer == null) {
		$.messager.alert('Warning', '请在表中选择某个版本，再点击下载');
		return;
	}
//	alert(JSON.stringify(rowItemCustomer));
	var versionFileName = rowItemCustomer.versionFileName;
	var url = BASE_PATH + "/VersionController/exportNewVersion.do";
	window.location.href=url+"?versionFileName="+versionFileName;
}

//删除版本信息
function deleteVersion() {
	var rowItemCustomer = $('#versionInfo_vv').datagrid('getSelected');
	if (rowItemCustomer == null) {
		$.messager.alert('Warning', '请在表中选择某个版本，再点击删除');
		return;
	}
	var versionFileName = rowItemCustomer.versionFileName;
	var versionId = rowItemCustomer.versionId;
	delVer(versionFileName,versionId);
}

//格式化数据网格中的删除和下载的显示样式，格式化方法有三个参数
//第一个表示格式化的值，第二个表示行记录，第三个表示行索引
//返回字段值
function verOperate(value,row,index){
	var delVer = "<a href='#' onclick='deleteverrow("+JSON.stringify(row)+")'>删除</a> ";
	var downVer = "<a href='#' onclick='downloadverrow("+JSON.stringify(row)+")'>下载</a> ";
	return delVer + downVer;
}

//删除行
function deleteverrow(row){
//	alert(JSON.stringify(row));
	var versionFileName = row.versionFileName;
	var versionId = row.versionId;
	delVer(versionFileName,versionId);
}

//下载行
function downloadverrow(row){
	var versionFileName = row.versionFileName;
	var url = BASE_PATH + "/VersionController/exportNewVersion.do";
	window.location.href=url+"?versionFileName="+versionFileName;
}

//确认删除版本信息
function delVer(versionFileName,versionId){
	$.messager.confirm("操作提示", "您确定要删除此版本信息和文件吗？", function(data) {
		if (data) {
			$.messager.prompt("提示信息", "请输入密码：", function(r){
				if (r){
					var myDate = new Date();
					var month = (myDate.getMonth()+1);
					if(month<10){
						month = "0"+(myDate.getMonth()+1);
					}
					var day = myDate.getDate();
					if(day<10){
						day = "0"+myDate.getDate();
					}
					var date = myDate.getFullYear()+""+month+""+day;
					if(r==date){
						$.post(BASE_PATH + "/VersionController/deleteVersion.do", {
							versionFileName : versionFileName,
							versionId : versionId
						}, function(data) {
							if (data == "true") {
								$.messager.alert('Warning', '删除成功');
								findVersion();
							} else {
								$.messager.alert('Warning', '删除失败');
							}
						});
					} else {
						$.messager.alert('Warning', '密码不正确');
					};
				}
			});
		};
	});
}

//查询版本种类信息
function findApkInfo() {
	var options = $('#apkInfo_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	var eNum = page * rows;
	/*$('#LoginData_umv').datagrid('loadData', []);*/
	$("#apkInfo_vv").datagrid("loading");
	$.post(BASE_PATH + "/VersionController/findApkInfo.do", {
		bNum : bNum,
		rows : rows,
	}, function(data) {
		try {
			var temp = eval('(' + data + ')');
			$('#apkInfo_vv').datagrid('loadData', temp);
			$("#apkInfo_vv").datagrid("loaded");
		} catch (e) {
			console.log(e);
		}
		
	});
}

//新增版本种类信息
function addApkInfo() {
	$('#addApkType_vv').window('open');
}

//新增版本种类信息确认
function addApkTypeForm() {
	var rowItemCustomer = $('#apkInfo_vv').datagrid('getSelected');
	var apkTypeName = $('#apkTypeName_addvv').textbox('getText');
	//简单验证
	if (apkTypeName == "" || apkTypeName == undefined || apkTypeName == null) {
		$.messager.alert('Warning', '请填写种类名称');
		return;
	}
	$.post(BASE_PATH + "/VersionController/addApkInfo.do", {
		apkTypeName : apkTypeName,
		apkTypeIcon : "",
	}, function(data) {
		if (data == "true") {
			$.messager.alert('Warning', '新增成功');
			$('#addApkType_vv').window('close');
			findApkInfo();
		} else {
			$.messager.alert('Warning', '新增失败');
		}
	});
}

//重置版本种类信息
function clearApkForm(type) {
	if(type=='add'){
		$('#apkTypeName_addvv').textbox('setValue', "");
		$('#apkTypeIcon_addvv').textbox('setValue', "");
	}else if(type=='edit'){
		$('#apkTypeName_editvv').textbox('setValue', "");
		$('#apkTypeIcon_editvv').textbox('setValue', "");
	}
}

//修改版本种类信息
function editApkInfo() {
	var rowItemCustomer = $('#apkInfo_vv').datagrid('getSelected');
	if (rowItemCustomer == null) {
		$.messager.alert('Warning', '请在表中选择某个种类，再点击修改');
		return;
	}
	$('#editApkType_vv').window('open');
	$('#apkTypeId_editvv').textbox('setValue', rowItemCustomer.apkTypeId);
	$('#apkTypeName_editvv').textbox('setValue', rowItemCustomer.apkTypeName);
	$('#apkTypeIcon_editvv').textbox('setValue', rowItemCustomer.apkTypeIcon);
}

//修改版本种类信息确认
function editApkTypeForm() {
	debugger;
	var rowItemCustomer = $('#apkInfo_vv').datagrid('getSelected');
	var apkTypeId = $('#apkTypeId_editvv').textbox('getText');
	var apkTypeName = $('#apkTypeName_editvv').textbox('getText');
	//var apkTypeIcon = $('#apkTypeIcon_editvv').textbox('getText');
	//简单验证
	if (apkTypeName == "" || apkTypeName == undefined || apkTypeName == null) {
		$.messager.alert('Warning', '请填写种类名称');
		return;
	}
//	if (apkTypeIcon == "" || apkTypeIcon == undefined || apkTypeIcon == null) {
//		$.messager.alert('Warning', '请填写种类标志');
//		return;
//	}
	$.post(BASE_PATH + "/VersionController/editApkInfo.do", {
		apkTypeId : apkTypeId,
		apkTypeName : apkTypeName
	}, function(data) {
		if (data == "true") {
			$.messager.alert('Warning', '修改成功');
			$('#addApkType_vv').window('close');
			findApkInfo();
		} else {
			$.messager.alert('Warning', '修改失败');
		}
	});
}

//删除版本信息种类
function deleteApkInfo() {
	var rowItemCustomer = $('#apkInfo_vv').datagrid('getSelected');
	if (rowItemCustomer == null) {
		$.messager.alert('Warning', '请在表中选择某个种类，再点击删除');
		return;
	}
	var apkTypeId = rowItemCustomer.apkTypeId;
	$.messager.confirm("操作提示", "您确定要删除此版本种类信息吗？", function(data) {
		if (data) {
			$.messager.prompt("提示信息", "请输入密码：", function(r){
				if (r){
					var myDate = new Date();
					var month = (myDate.getMonth()+1);
					if(month<10){
						month = "0"+(myDate.getMonth()+1);
					}
					var day = myDate.getDate();
					if(day<10){
						day = "0"+myDate.getDate();
					}
					var date = myDate.getFullYear()+""+month+""+day;
					if(r==date){
						$.post(BASE_PATH + "/VersionController/deleteApkInfo.do", {
							apkTypeId : apkTypeId
						}, function(data) {
							if (data == "true") {
								$.messager.alert('Warning', '删除成功');
								findApkInfo();
							} else {
								$.messager.alert('Warning', '删除失败');
							}
						});
					} else {
						$.messager.alert('Warning', '密码不正确');
					};
				}
			});
		};
	});
}

//格式化数据网格中的删除和下载的显示样式，格式化方法有三个参数
//第一个表示格式化的值，第二个表示行记录，第三个表示行索引
//返回字段值
function chooseversiontype(value,row,index){
	if(value=="stable"){
		value="稳定版";
	}else if(value=="beta"){
		value="预览版";
	}
	return value;
}
