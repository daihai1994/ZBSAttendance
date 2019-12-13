<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>

<body>
    <div class="easyui-tabs" data-options="fit:true">
        <div data-options="region:'east' ,collapsible:false,split:true"
			title="版本信息" style="width:50%;">
			<div id="toolbar_vv" style="height:35px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="deleteVersion()">刪除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="downVersion()">下载</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="uploadVersion()">上传</a>	
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findVersion()">查询</a>
			</div>
			<table id="versionInfo_vv" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:true,
					rownumbers:true,
					toolbar: '#toolbar_vv',
					pagination:true, //包含分页
					pageSize:50,
					showFooter: true">
				<thead>
					<tr>
						<th data-options="field:'versionId',width:20,align:'center',hidden:true">版本Id</th>
						<th data-options="field:'apkTypeId',width:20,align:'center'">软件编号</th>
						<th data-options="field:'versionCode',width:20,align:'center'">版本编号</th>
						<th data-options="field:'versionName',width:20,align:'center'">版本名称</th>
						<th data-options="field:'versionLog',width:80,align:'center'">版本日志</th>
						<th data-options="field:'versionFileName',width:50,align:'center'">文件名称</th>
						<th data-options="field:'versionSize',width:20,align:'center'">文件大小</th>
						<th data-options="field:'createTime',width:40,align:'center'">上传时间</th>
						<th data-options="field:'action',width:30,align:'center',formatter:verOperate">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		
		<div title="应用信息" style="width:50%;">
			<div id="toolbar_apkvv" style="height:35px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="deleteApkInfo()">刪除</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="editApkInfo()">修改</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-edit'" onclick="addApkInfo()">新增</a>	
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="findApkInfo()">查询</a>
			</div>
			<table id="apkInfo_vv" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:true,
					rownumbers:true,
					toolbar: '#toolbar_apkvv',
					pagination:true, //包含分页
					pageSize:50,
					showFooter: true">
				<thead>
					<tr>
						<th data-options="field:'apkTypeId',width:50,align:'center'">版本种类Id</th>
						<th data-options="field:'apkTypeName',width:50,align:'center'">版本种类名称</th>
					</tr>
				</thead>
			</table>
		</div>
		
    </div>
    
    <div id="upload_vvv" class="easyui-window" title="版本上传"
			style="width:600px;height:400px,margin:10px"
			data-options="iconCls:'icon-add',modal:true,closed:true ,maximizable: false,minimizable: false,">
			<div style="margin:20px 0;"></div>
			<form id="upload_ver" action="${pageContext.request.contextPath }/VersionController/upload.do" 
				style="width:500px;height:420px; padding-left:100px"
				enctype="multipart/form-data" method="post">
				<div style="margin-bottom:20px">
                	<select data-editable="false" id="apkTypeId" class="easyui-combobox" name="apkTypeId" data-options="label:'软件类型:'"  style="width:100%;"></select>
            	</div>
				<div style="margin-bottom:20px">
                	<input id="versionCode" class="easyui-textbox" name="versionCode" style="width:100%" data-options="label:'版本编号:'">
            	</div>
            	<div style="margin-bottom:20px">
                	<input id="versionName" class="easyui-textbox" name="versionName" style="width:100%" data-options="label:'版本名称:'">
            	</div>
            	<div style="margin-bottom:20px">
                	<input id="versionLog" class="easyui-textbox" name="versionLog" style="width:100%;height:120px" data-options="label:'版本日志:',multiline:true">
            	</div>
            	<div style="margin-bottom:20px">
                	<input id="file" class="easyui-filebox" name="file" style="width:100%" data-options="label:'最新版本:',buttonText:'浏览',prompt:'请选择文件...',accept:'.apk'">
            	</div>
            	<div style="text-align:center;margin-bottom:40px">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="updload_ver()" style="width:80px">上传</a>
				</div>
            </form>
		</div>
		
		<div id="addApkType_vv" class="easyui-window" title="新增版本种类信息"
			style="width:600px;height:250px,margin:10px"
			data-options="iconCls:'icon-add',modal:true,closed:true ,maximizable: false,minimizable: false,">
			<div style="margin:20px 0;"></div>
			<form id="addApkType"
				style="width:500px;height:180px; padding-left:80px"
				class="easyui-form" method="post">
				 <div style="margin-bottom:20px">
                	<input id="apkTypeName_addvv" class="easyui-textbox" name="apkTypeName_addvv" style="width:100%" data-options="label:'版本种类名称:'">
            	</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="addApkTypeForm()" style="width:80px">保存</a> <a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="clearApkForm('add')" style="width:80px">重置</a>
				</div>
			</form>
		</div>
		
		<div id="editApkType_vv" class="easyui-window" title="修改版本种类信息"
			style="width:600px;height:250px,margin:10px"
			data-options="iconCls:'icon-add',modal:true,closed:true ,maximizable: false,minimizable: false,">
			<div style="margin:20px 0;"></div>
			<form id="addApkType"
				style="width:500px;height:180px; padding-left:80px"
				class="easyui-form" method="post">
				<div style="margin-bottom:20px">
                	<input id="apkTypeId_editvv" class="easyui-textbox" name="apkTypeId_editvv" style="width:100%" data-options="label:'版本种类Id:',editable:false">
            	</div>
				 <div style="margin-bottom:20px">
                	<input id="apkTypeName_editvv" class="easyui-textbox" name="apkTypeName_editvv" style="width:100%" data-options="label:'版本种类名称:'">
            	</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="editApkTypeForm()" style="width:80px">保存</a> <a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="clearApkForm('edit')" style="width:80px">重置</a>
				</div>
			</form>
		</div>
    
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/version/versionView.js"></script>
    <script type="text/javascript">
    $(function() {
		
	});
	var curstatusview_init = true;
	$.parser.onComplete = function(res) {
		if (curstatusview_init) {
			curstatusview_init = false;
			var pager_page_lscx1 = $('#versionInfo_vv').datagrid("getPager");
			//版本信息管理
			if (pager_page_lscx1) {
				$(pager_page_lscx1).pagination({
					pageSize:50,
					pageList : [ 50,100,200 ],
					onSelectPage : function(pageNumber, pageSize) {
						findVersion();
					}
				});
			}
			var pager_page_lscxapk = $('#apkInfo_vv').datagrid("getPager");
			//版本种类信息管理
			if (pager_page_lscxapk) {
				$(pager_page_lscxapk).pagination({
					pageSize:50,
					pageList : [ 50,100,200 ],
					onSelectPage : function(pageNumber, pageSize) {
						findApkInfo();
					}
				});
			}
			findVersion();
			findApkInfo();
		}
	};
    </script>
    
</body>

</html>