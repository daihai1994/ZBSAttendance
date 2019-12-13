<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div id="toolbar_advertisement" style="height:35px">
			开始时间:<input id="bt_advertisement" class="easyui-datebox" editable="false" style="width:150px;height:26px">
			结束时间:<input id="et_advertisement" class="easyui-datebox" editable="false" style="width:150px;height:26px">
			<input id="remarks_advertisement" class="easyui-textbox" data-options="label:'关键字:',labelWidth:'60px'">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="findAdvertisement()">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'" onclick="AddAdvertisement()">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'" onclick="updateAdvertisement()">修改</a>
	</div>
	<table id="advertisement_vv" class="easyui-datagrid"
		data-options="
			fit:true,
			striped:true,
			fitColumns: true,
			singleSelect:true,
			rownumbers:true,
			toolbar: '#toolbar_advertisement',
			pagination:true, //包含分页
			pageSize:50">
		<thead>
			<tr>
				<th data-options="field:'name',width:50,align:'center'">简介</th>
				<th data-options="field:'picUrl',width:50,align:'center'">图片地址</th>
				<th data-options="field:'adUrl',width:30,align:'center'">链接地址</th>
				<th data-options="field:'showTime',width:20,align:'center'">显示时间(秒)</th>
				<th data-options="field:'start_time',width:30,align:'center'">开始时间</th>
				<th data-options="field:'end_time',width:50,align:'center'">结束时间</th>
				<th data-options="field:'effective',width:20,align:'center',formatter:effectiveType">是否有效</th>
				<th data-options="field:'force',width:20,align:'center',formatter:forceType">是否强制</th>
				<th data-options="field:'createTime',width:30,align:'center'">创建时间</th>
			</tr>
		</thead>
	</table>
	<div id="advertisement_add_dialog" class="easyui-dialog" title="上传" style="width:600px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
		<form id="importImg_advertisement" action="${pageContext.request.contextPath}/AdvertisementController/uploadImg.do" method="post" enctype="multipart/form-data" 
			style="float: left;margin-left: 10px">
			<div style="margin: 10px;">
				<input data-options="label:'简介'" class="easyui-textbox" id="name_advertisement_add" name = "name_advertisement_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'显示秒',min:3,max:10" class="easyui-numberspinner" name = "showTime_advertisement_add" id="showTime_advertisement_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'链接地址'" class="easyui-textbox" name = "adUrl_advertisement_add" id="adUrl_advertisement_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'开始时间'" class="easyui-datebox" name = "start_time_advertisement_add" id="start_time_advertisement_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'结束时间'" class="easyui-datebox" name = "end_time_advertisement_add" id="end_time_advertisement_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<select id="effective_advertisement_add" name = "effective_advertisement_add" class="easyui-combobox" editable="false"  label="是否有效" style="width: 500px;">
						<option value="1">有效</option>
						<option value="0">无效</option>
				</select>
			</div>
			<div style="margin: 10px;">
				<select id="force_advertisement_add" name = "force_advertisement_add" class="easyui-combobox" editable="false"  label="是否强制" style="width: 500px;">
						<option value="1">强制</option>
						<option value="0">不强制</option>
				</select>
			</div>
			<div style="margin: 10px;">
				<input style="width: 140px;" id="uploadImg_advertisement" name="uploadImg" style="width: 500px;" 
				class="easyui-filebox" data-options="prompt:'添加图片',buttonText: '添加图片'" >
				<img id="turn_img" width="200" height="200" src=""/>
			</div>
		</form>
		<div style="margin: 40px;text-align: center;">
			<a  onclick="submitAdvertisementDialog()" class="easyui-linkbutton" style="width: 100px;">保存</a>
			<a  onclick="$('#advertisement_add_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
		</div>
	</div>
	
	
	<div id="advertisement_edit_dialog" class="easyui-dialog" title="上传" style="width:600px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
		<form id="importImg_advertisement_edit" action="${pageContext.request.contextPath}/AdvertisementController/uploadImgEdit.do" method="post" enctype="multipart/form-data" 
			style="float: left;margin-left: 10px">
			<div style="margin: 10px;">
				<input id="advertisement_id" name = "advertisement_id" type="hidden">
				<input id="advertisement_picurl" name = "advertisement_picurl" type="hidden">
				<input data-options="label:'简介'" class="easyui-textbox" id="name_advertisement_edit" name = "name_advertisement_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'显示秒',min:3,max:10" class="easyui-numberspinner" name = "showTime_advertisement_edit" id="showTime_advertisement_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'链接地址'" class="easyui-textbox" name = "adUrl_advertisement_edit" id="adUrl_advertisement_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'开始时间'" class="easyui-datebox" name = "start_time_advertisement_edit" id="start_time_advertisement_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<input data-options="label:'结束时间'" class="easyui-datebox" name = "end_time_advertisement_edit" id="end_time_advertisement_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<select id="effective_advertisement_edit" name = "effective_advertisement_edit" class="easyui-combobox" editable="false"  label="是否有效" style="width: 500px;">
						<option value="1">有效</option>
						<option value="0">无效</option>
				</select>
			</div>
			<div style="margin: 10px;">
				<select id="force_advertisement_edit" name = "force_advertisement_edit" class="easyui-combobox" editable="false"  label="是否强制" style="width: 500px;">
						<option value="1">强制</option>
						<option value="0">不强制</option>
				</select>
			</div>
			<div style="margin: 10px;text-align: center;">
				<img id="turn_img_show" width="200" height="200" src=""/>
			</div>
			<div style="margin: 10px;">
				<input style="width: 140px;" id="uploadImg_advertisement_edit" name="uploadImgEdit" style="width: 500px;" 
				class="easyui-filebox" data-options="prompt:'添加图片',buttonText: '添加图片'" >
				<img id="turn_img_edit" width="200" height="200" src=""/>
			</div>
		</form>
		<div style="margin: 40px;text-align: center;">
			<a  onclick="submitAdvertisementDialogEdit()" class="easyui-linkbutton" style="width: 100px;">保存</a>
			<a  onclick="$('#advertisement_edit_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
		</div>
	</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/advertisement/advertisementView.js"></script>
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
					$('#bt_advertisement').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
					$('#et_advertisement').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
					findAdvertisement();
					$('#turn_img_edit').hover(function(){
					$(this).addClass('hover')
					},function(){
					$(this).removeClass('hover')
					});
					$('#turn_img').hover(function(){
					$(this).addClass('hover')
					},function(){
					$(this).removeClass('hover')
					});
					
					$('#uploadImg_advertisement').filebox({
						onChange : function(e) {
							debugger;
							if(isNotNull(e)){
								var tempFile = $("#uploadImg_advertisement");
	       					 	var value=tempFile.filebox('getValue');
					    		 // 取后缀名
					        	 var ext=value.substring(value.lastIndexOf(".")+1).toLowerCase();
						         if(ext!='png'&&ext!='jpeg'&&ext!='gif'&&ext!='jpg'&&ext!='bmp'){
						                     $.messager.alert("消息提示", "文件类型不合法,只能是jpg、gif、bmp、png、jpeg、png类型！", "error");
											$('#uploadImg_advertisement').filebox("clear");
											$('#turn_img').attr('src',"");
						                     return;
		        				 }
								//取file
							 	var file = $(this).context.ownerDocument.activeElement.files[0];
								var oFReader = new FileReader();
								oFReader.readAsDataURL(file);
								oFReader.onloadend = function(oFRevent){
								debugger;
								$('#turn_img').show();
									var src = oFRevent.target.result;
									$('#turn_img').attr('src',src);
									console.log(src);
								}
							}
						}
					})
					$('#uploadImg_advertisement_edit').filebox({
						onChange : function(e) {
							debugger;
							if(isNotNull(e)){
								var tempFile = $("#uploadImg_advertisement_edit");
	       					 	var value=tempFile.filebox('getValue');
					    		 // 取后缀名
					        	 var ext=value.substring(value.lastIndexOf(".")+1).toLowerCase();
						         if(ext!='png'&&ext!='jpeg'&&ext!='gif'&&ext!='jpg'&&ext!='bmp'){
						                     $.messager.alert("消息提示", "文件类型不合法,只能是jpg、gif、bmp、png、jpeg、png类型！", "error");
											$('#uploadImg_advertisement_edit').filebox("clear");
											$('#turn_img_edit').attr('src',"");
						                     return;
		        				 }
								//取file
							 	var file = $(this).context.ownerDocument.activeElement.files[0];
								var oFReader = new FileReader();
								oFReader.readAsDataURL(file);
								oFReader.onloadend = function(oFRevent){
								debugger;
								$('#turn_img_edit').show();
									var src = oFRevent.target.result;
									$('#turn_img_edit').attr('src',src);
									console.log(src);
								}
							}
						}
					})
					$("#advertisement_vv").datagrid({
						onDblClickRow : function(index,res){
							debugger;
							
						}
					});
					var pager_page_advertisement = $('#advertisement_vv').datagrid("getPager");
					if (pager_page_advertisement) {
						$(pager_page_advertisement).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findAdvertisement();
							}
						});
					}
			}
		};
    </script>
    
</body>

</html>