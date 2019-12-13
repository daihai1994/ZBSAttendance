<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div id="toolbar_homePagePicture" style="height:35px">
			开始时间:<input id="bt_homePagePicture" class="easyui-datebox" editable="false" style="width:150px;height:26px">
			结束时间:<input id="et_homePagePicture" class="easyui-datebox" editable="false" style="width:150px;height:26px">
			<input id="remarks_homePagePicture" class="easyui-textbox" data-options="label:'关键字:',labelWidth:'60px'">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="findhomePagePicture()">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'" onclick="AddHomePagePicture()">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'" onclick="updateHomePagePicture()">修改</a>
	</div>
	<table id="homePagePicture_vv" class="easyui-datagrid"
		data-options="
			fit:true,
			striped:true,
			fitColumns: true,
			singleSelect:true,
			rownumbers:true,
			toolbar: '#toolbar_homePagePicture',
			pagination:true, //包含分页
			pageSize:50">
		<thead>
			<tr>
				<th data-options="field:'remarks',width:50,align:'center'">简介</th>
				<th data-options="field:'pictureUrl',width:50,align:'center'">图片地址</th>
				<th data-options="field:'addressUrl',width:30,align:'center'">链接地址</th>
<!-- 				<th data-options="field:'level',width:20,align:'center'">显示级别</th> -->
				<th data-options="field:'createTime',width:30,align:'center'">创建时间</th>
				<th data-options="field:'effective',width:20,align:'center',formatter:effectiveType">是否有效</th>
<!-- 				<th data-options="field:'force',width:20,align:'center',formatter:forceType">是否强制</th> -->
			</tr>
		</thead>
	</table>
	<div id="homePagePicture_add_dialog" class="easyui-dialog" title="上传" style="width:600px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
		<form id="importImg_homePagePicture" action="${pageContext.request.contextPath}/homePagePictureController/uploadImg.do" method="post" enctype="multipart/form-data" 
			style="float: left;margin-left: 10px">
			<div style="margin: 10px;">
				<input id="homePagePicture_company_id" name = "homePagePicture_company_id" type="hidden">
				<input data-options="label:'简介'" class="easyui-textbox" id="remarks_homePagePicture_add" name = "remarks_homePagePicture_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<select id="effective_homePagePicture_add" name = "effective_homePagePicture_add" class="easyui-combobox" editable="false"  label="是否有效" style="width: 500px;">
						<option value="1">有效</option>
						<option value="0">无效</option>
				</select>
			</div>
			<div style="margin: 10px;">
				<select id="state_homePagePicture_add" name = "state_homePagePicture_add" class="easyui-combobox" editable="false"  label="跳转类型" style="width: 500px;">
						<option value="1">第三方链接</option>
						<option value="0">本地链接</option>
				</select>
			</div>
			<div style="margin: 10px;" id="adUrl_homePagePicture_add_div">
				<input data-options="label:'链接地址'" class="easyui-textbox" name = "adUrl_homePagePicture_add" id="adUrl_homePagePicture_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input style="width: 140px;" id="uploadImg_homePagePicture" name="uploadImg_homePagePicture" style="width: 500px;" 
				class="easyui-filebox" data-options="prompt:'添加图片',buttonText: '添加图片'" >
				<img id="turn_img_homePagePicture" width="200" height="200" src=""/>
			</div>
			<div style="margin: 10px;" id="uploadFile_homePagePicture_div">
				<input style="width: 140px;" id="uploadFile_homePagePicture" name="uploadFile_homePagePicture" style="width: 500px;" 
				class="easyui-filebox" data-options="prompt:'添加文件',buttonText: '添加文件'" >
			</div>
		</form>
		<div style="margin: 40px;text-align: center;">
			<a  onclick="submithomePagePictureDialog()" class="easyui-linkbutton" style="width: 100px;">保存</a>
			<a  onclick="$('#homePagePicture_add_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
		</div>
	</div>
	
	
	<div id="homePagePicture_edit_dialog" class="easyui-dialog" title="上传" style="width:600px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
		<form id="importImg_homePagePicture_edit" action="${pageContext.request.contextPath}/homePagePictureController/uploadImgEdit.do" method="post" enctype="multipart/form-data" 
			style="float: left;margin-left: 10px">
			<div style="margin: 10px;">
				<input id="homePagePicture_id" name = "homePagePicture_id" type="hidden">
				<input id="homePagePicture_picurl" name = "homePagePicture_picurl" type="hidden">
				<input data-options="label:'简介'" class="easyui-textbox" id="remarks_homePagePicture_edit" name = "remarks_homePagePicture_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<select id="effective_homePagePicture_edit" name = "effective_homePagePicture_edit" class="easyui-combobox" editable="false"  label="是否有效" style="width: 500px;">
						<option value="1">有效</option>
						<option value="0">无效</option>
				</select>
			</div>
			<div style="margin: 10px;">
				<select id="state_homePagePicture_edit" name = "state_homePagePicture_edit" class="easyui-combobox" editable="false"  label="跳转类型" style="width: 500px;">
						<option value="1">第三方链接</option>
						<option value="0">本地链接</option>
				</select>
			</div>
			<div style="margin: 10px;" id="adUrl_homePagePicture_edit_div">
				<input data-options="label:'链接地址'" class="easyui-textbox" name = "adUrl_homePagePicture_edit" id="adUrl_homePagePicture_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;text-align: center;">
				<img id="turn_img_show_homePagePicture" width="200" height="200" src=""/>
			</div>
			<div style="margin: 10px;">
				<input style="width: 140px;" id="uploadImg_homePagePicture_edit" name="uploadImg_homePagePicture_edit" style="width: 500px;" 
				class="easyui-filebox" data-options="prompt:'添加图片',buttonText: '添加图片'" >
				<img id="turn_img_edit_homePagePicture" width="200" height="200" src=""/>
			</div>
			<div style="margin: 10px;" id="uploadFile_homePagePicture_edit_div">
				<input style="width: 140px;" id="uploadFile_homePagePicture_edit" name="uploadFile_homePagePicture_edit" style="width: 500px;" 
				class="easyui-filebox" data-options="prompt:'添加文件',buttonText: '添加文件'" >
			</div>
		</form>
		<div style="margin: 40px;text-align: center;">
			<a  onclick="submithomePagePictureDialogEdit()" class="easyui-linkbutton" style="width: 100px;">保存</a>
			<a  onclick="$('#homePagePicture_edit_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
		</div>
	</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/homePagePicture/homePagePictureView.js"></script>
    <style type="text/css">
		img{
		-webkit-transition: ease .2s;
		transition: ease .2s;
		-webkit-transform-origin:50% 50%; /* transform-origin默认值就是居中，可以不加 */
		transform-origin:50% 50%; /* transform-origin默认值就是居中，可以不加 */
		}
		.hover{
		-webkit-transform: scale(2); /*放大1.2倍*/
		transform: scale(2); /*放大1.2倍*/
		}
	</style>
    <script type="text/javascript">
		var loggerInfo_init = true;
		$.parser.onComplete = function(res) {
			if (loggerInfo_init) {
				loggerInfo_init = false;
					var newDate = new Date();
					$('#bt_homePagePicture').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
					$('#et_homePagePicture').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
					
					$('#state_homePagePicture_add').combobox({
						onChange: function(value){
							debugger;
							if(value=="0"){//本地连接
								$('#uploadFile_homePagePicture_div').show();
								$('#uploadFile_homePagePicture').filebox("clear");
								$('#adUrl_homePagePicture_add_div').hide();
							}else{//外部连接
								$('#uploadFile_homePagePicture_div').hide();
								$('#uploadFile_homePagePicture').filebox("clear");
								$('#adUrl_homePagePicture_add_div').show();
							}
						}
					})
					
					$('#state_homePagePicture_edit').combobox({
						onChange: function(value){
							debugger;
							if(value=="0"){//本地连接
								$('#uploadFile_homePagePicture_edit_div').show();
								$('#uploadFile_homePagePicture_edit').filebox("clear");
								$('#adUrl_homePagePicture_edit_div').hide();
							}else{//外部连接
								$('#uploadFile_homePagePicture_edit_div').hide();
								$('#uploadFile_homePagePicture_edit').filebox("clear");
								$('#adUrl_homePagePicture_edit_div').show();
							}
						}
					})
					findhomePagePicture();
					$('#turn_img_edit_homePagePicture').hover(function(){
					$(this).addClass('hover')
					},function(){
					$(this).removeClass('hover')
					});
					$('#turn_img_homePagePicture').hover(function(){
					$(this).addClass('hover')
					},function(){
					$(this).removeClass('hover')
					});
					
					$('#uploadImg_homePagePicture').filebox({
						onChange : function(e) {
							debugger;
							if(isNotNull(e)){
								var tempFile = $("#uploadImg_homePagePicture");
	       					 	var value=tempFile.filebox('getValue');
					    		 // 取后缀名
					        	 var ext=value.substring(value.lastIndexOf(".")+1).toLowerCase();
						         if(ext!='png'&&ext!='jpeg'&&ext!='gif'&&ext!='jpg'&&ext!='bmp'){
						                     $.messager.alert("消息提示", "文件类型不合法,只能是jpg、gif、bmp、png、jpeg、png类型！", "error");
											$('#uploadImg_homePagePicture').filebox("clear");
											$('#turn_img_homePagePicture').attr('src',"");
						                     return;
		        				 }
								//取file
							 	var file = $(this).context.ownerDocument.activeElement.files[0];
								var oFReader = new FileReader();
								oFReader.readAsDataURL(file);
								oFReader.onloadend = function(oFRevent){
								debugger;
								$('#turn_img_homePagePicture').show();
									var src = oFRevent.target.result;
									$('#turn_img_homePagePicture').attr('src',src);
									console.log(src);
								}
							}
						}
					})
					
					$('#uploadImg_homePagePicture_edit').filebox({
						onChange : function(e) {
							debugger;
							if(isNotNull(e)){
								var tempFile = $("#uploadImg_homePagePicture_edit");
	       					 	var value=tempFile.filebox('getValue');
					    		 // 取后缀名
					        	 var ext=value.substring(value.lastIndexOf(".")+1).toLowerCase();
						         if(ext!='png'&&ext!='jpeg'&&ext!='gif'&&ext!='jpg'&&ext!='bmp'){
						                     $.messager.alert("消息提示", "文件类型不合法,只能是jpg、gif、bmp、png、jpeg、png类型！", "error");
											$('#uploadImg_homePagePicture_edit').filebox("clear");
											$('#turn_img_edit_homePagePicture').attr('src',"");
						                     return;
		        				 }
								//取file
							 	var file = $(this).context.ownerDocument.activeElement.files[0];
								var oFReader = new FileReader();
								oFReader.readAsDataURL(file);
								oFReader.onloadend = function(oFRevent){
								debugger;
								$('#turn_img_edit_homePagePicture').show();
									var src = oFRevent.target.result;
									$('#turn_img_edit_homePagePicture').attr('src',src);
									console.log(src);
								}
							}
						}
					})
					$("#homePagePicture_vv").datagrid({
						onDblClickRow : function(index,res){
							debugger;
							
						}
					});
					var pager_page_homePagePicture = $('#homePagePicture_vv').datagrid("getPager");
					if (pager_page_homePagePicture) {
						$(pager_page_homePagePicture).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findhomePagePicture();
							}
						});
					}
			}
		};
    </script>
    
</body>

</html>