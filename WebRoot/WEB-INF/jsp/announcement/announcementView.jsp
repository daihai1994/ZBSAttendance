<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div id="toolbar_announcement" style="height:35px">
			开始时间:<input id="bt_announcement" class="easyui-datebox" editable="false" style="width:150px;height:26px">
			结束时间:<input id="et_announcement" class="easyui-datebox" editable="false" style="width:150px;height:26px">
			<input id="title_announcement" class="easyui-textbox" data-options="label:'关键字:',labelWidth:'60px'">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="findannouncement()">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-add'" onclick="Addannouncement()">新增</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit'" onclick="updateannouncement()">修改</a>
	</div>
	<table id="announcement_vv" class="easyui-datagrid"
		data-options="
			fit:true,
			striped:true,
			fitColumns: true,
			singleSelect:true,
			rownumbers:true,
			toolbar: '#toolbar_announcement',
			nowrap:false,
			pagination:true, //包含分页
			pageSize:50">
		<thead>
			<tr>
				<th data-options="field:'title',width:50,align:'center'">标题</th>
				<th data-options="field:'url',width:30,align:'center'">链接地址</th>
				<th data-options="field:'priority',width:20,align:'center'">显示级别</th>
				<th data-options="field:'time',width:30,align:'center'">创建时间</th>
				<th data-options="field:'effectiveTime',width:20,align:'center'">失效时间</th>
				<th data-options="field:'author',width:20,align:'center'">发布人</th>
			</tr>
		</thead>
	</table>
	<div id="announcement_add_dialog" class="easyui-dialog" title="上传" style="width:600px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
		<form id="importImg_announcement" action="${pageContext.request.contextPath}/announcementController/uploadImg.do" method="post" enctype="multipart/form-data" 
			style="float: left;margin-left: 10px">
			<div style="margin: 10px;">
				<input id="announcement_company_id" name = "announcement_company_id" type="hidden">
				<input data-options="label:'标题'" class="easyui-textbox" id="title_announcement_add" name = "title_announcement_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<select id="type_announcement_add" name = "type_announcement_add" class="easyui-combobox" editable="false"  label="公告类型" style="width: 500px;">
				</select>
			</div>
			<div style="margin: 10px;">
				<select id="priority_announcement_add" name = "priority_announcement_add" class="easyui-combobox" editable="false"  label="优先级" style="width: 500px;">
				</select>
			</div>
			<div style="margin: 10px;">
				<input id="effectiveTime_announcement_add" name = "effectiveTime_announcement_add" class="easyui-datebox" editable="false" label = "失效时间" style="width:500px;height:26px">
			</div>
			<div style="margin: 10px;">
				<select id="state_announcement_add" name = "state_announcement_add" class="easyui-combobox" editable="false"  label="跳转类型" style="width: 500px;">
						<option value="picture" >图片</option>
						<option value="document">文件</option>
						<option value="video">视频</option>
						<option value="audio">音频</option>
						<option value="external_links">外部链接</option>
						<option value="local_links">本地链接</option>
				</select>
			</div>
			<div style="margin: 10px;" id="adUrl_announcement_add_div">
				<input data-options="label:'链接地址'" class="easyui-textbox" name = "adUrl_announcement_add" id="adUrl_announcement_add" style="width: 500px;">
			</div>
			<div style="margin: 10px;" id="uploadFile_announcement_div">
				<input style="width: 140px;" id="uploadFile_announcement" name="uploadFile_announcement" style="width: 500px;" 
				class="easyui-filebox" data-options="prompt:'添加文件',buttonText: '添加文件'" >
				<img id="turn_img_announcement" width="200" height="200" src=""/>
			</div>
		</form>
		<div style="margin: 40px;text-align: center;">
			<a  onclick="submitannouncementDialog()" class="easyui-linkbutton" style="width: 100px;">保存</a>
			<a  onclick="$('#announcement_add_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
		</div>
	</div>
	
	
	<div id="announcement_edit_dialog" class="easyui-dialog" title="上传" style="width:600px;height: 600px;padding:10px;" data-options="modal:true,closed:true">
		<form id="importImg_announcement_edit" action="${pageContext.request.contextPath}/announcementController/uploadImgEdit.do" method="post" enctype="multipart/form-data" 
			style="float: left;margin-left: 10px">
			<div style="margin: 10px;">
				<input id="announcement_id" name = "announcement_id" type="hidden">
				<input id="announcement_picurl" name = "announcement_picurl" type="hidden">
				<input data-options="label:'简介'" class="easyui-textbox" id="title_announcement_edit" name = "title_announcement_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;">
				<select id="type_announcement_edit" name = "type_announcement_edit" class="easyui-combobox" editable="false"  label="公告类型" style="width: 500px;">
				</select>
			</div>
			<div style="margin: 10px;">
				<select id="priority_announcement_edit" name = "priority_announcement_edit" class="easyui-combobox" editable="false"  label="优先级" style="width: 500px;">
				</select>
			</div>
			<div style="margin: 10px;">
				<input id="effectiveTime_announcement_edit" name = "effectiveTime_announcement_edit" class="easyui-datebox" editable="false" label = "失效时间" style="width:500px;height:26px">
			</div>
			<div style="margin: 10px;">
				<select id="state_announcement_edit" name = "state_announcement_edit" class="easyui-combobox" editable="false"  label="跳转类型" style="width: 500px;">
						<option value="picture" >图片</option>
						<option value="document">文件</option>
						<option value="video">视频</option>
						<option value="audio">音频</option>
						<option value="external_links">外部链接</option>
						<option value="local_links">本地链接</option>
				</select>
			</div>
			<div style="margin: 10px;" id="adUrl_announcement_edit_div">
				<input data-options="label:'链接地址'" class="easyui-textbox" name = "adUrl_announcement_edit" id="adUrl_announcement_edit" style="width: 500px;">
			</div>
			<div style="margin: 10px;" id="turn_img_show_announcement_div">
				<img id="turn_img_show_announcement" width="200" height="200" src=""/>
			</div>
			<div style="margin: 10px;" id="uploadFile_announcement_edit_div">
				<input style="width: 140px;" id="uploadFile_announcement_edit" name="uploadFile_announcement_edit" style="width: 500px;" 
				class="easyui-filebox" data-options="prompt:'添加文件',buttonText: '添加文件'" >
				<img id="turn_img_edit_announcement" width="200" height="200" src=""/>
			</div>
		</form>
		<div style="margin: 40px;text-align: center;">
			<a  onclick="submitannouncementDialogEdit()" class="easyui-linkbutton" style="width: 100px;">保存</a>
			<a  onclick="$('#announcement_edit_dialog').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
		</div>
	</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/announcement/announcementView.js"></script>
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
		var announcementInfo_init = true;
		$.parser.onComplete = function(res) {
			if (announcementInfo_init) {
				announcementInfo_init = false;
					var newDate = new Date();
					$('#bt_announcement').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()-7));
					$('#et_announcement').datebox('setValue',newDate.getFullYear()+"-"+(newDate.getMonth()+1)+"-"+(newDate.getDate()));
					
					$('#state_announcement_add').combobox({
						onSelect: function(value){
							debugger;
							if(value.value=="external_links"){
								$('#uploadFile_announcement_div').hide();
								$('#uploadFile_announcement').filebox("clear");
								$('#adUrl_announcement_add_div').show();
							}else{
								$('#uploadFile_announcement_div').show();
								$('#uploadFile_announcement').filebox("clear");
								$('#adUrl_announcement_add_div').hide();
							}
						}
					})
					
					$('#state_announcement_edit').combobox({
						onSelect: function(value){
							debugger;
							if(value.value=="external_links"){
								$('#uploadFile_announcement_edit_div').hide();
								$('#uploadFile_announcement_edit').filebox("clear");
								$('#adUrl_announcement_edit_div').show();
							}else{
								$('#uploadFile_announcement_edit_div').show();
								$('#uploadFile_announcement_edit').filebox("clear");
								$('#adUrl_announcement_edit_div').hide();
							}
						}
					})
					findannouncement();
					$('#turn_img_edit_announcement').hover(function(){
					$(this).addClass('hover')
					},function(){
					$(this).removeClass('hover')
					});
					$('#turn_img_announcement').hover(function(){
					$(this).addClass('hover')
					},function(){
					$(this).removeClass('hover')
					});
					
					$('#uploadFile_announcement').filebox({
						onChange : function(e) {
							debugger;
							if(isNotNull(e)){
								var tempFile = $("#uploadFile_announcement");
	       					 	var value=tempFile.filebox('getValue');
					    		 // 取后缀名
					        	 var ext=value.substring(value.lastIndexOf(".")+1).toLowerCase();
						         if(ext=='png'||ext=='jpeg'||ext=='gif'||ext=='jpg'||ext=='bmp'||ext=='webp'){
						              //取file
								 	var file = $(this).context.ownerDocument.activeElement.files[0];
									var oFReader = new FileReader();
									oFReader.readAsDataURL(file);
									oFReader.onloadend = function(oFRevent){
									debugger;
										$('#turn_img_announcement').show();
										var src = oFRevent.target.result;
										$('#turn_img_announcement').attr('src',src);
									}   
		        				 }else{
										$('#turn_img_announcement').hide();
										$('#turn_img_announcement').attr('src',"");
								}
							}
						}
					})
					
					$('#uploadImg_announcement_edit').filebox({
						onChange : function(e) {
							debugger;
							if(isNotNull(e)){
								var tempFile = $("#uploadImg_announcement_edit");
	       					 	var value=tempFile.filebox('getValue');
					    		 // 取后缀名
					        	 var ext=value.substring(value.lastIndexOf(".")+1).toLowerCase();
						        if(ext=='png'||ext=='jpeg'||ext=='gif'||ext=='jpg'||ext=='bmp'||ext=='webp'){
						                     //取file
								 	var file = $(this).context.ownerDocument.activeElement.files[0];
									var oFReader = new FileReader();
									oFReader.readAsDataURL(file);
									oFReader.onloadend = function(oFRevent){
									debugger;
									$('#turn_img_edit_announcement').show();
										var src = oFRevent.target.result;
										$('#turn_img_edit_announcement').attr('src',src);
										console.log(src);
									}
		        				 }else{
										$('#uploadImg_announcement_edit').filebox("clear");
										$('#turn_img_edit_announcement').attr('src',"");
								}
								
							}
						}
					})
					$("#announcement_vv").datagrid({
						onDblClickRow : function(index,res){
							debugger;
							
						}
					});
					var pager_page_announcement = $('#announcement_vv').datagrid("getPager");
					if (pager_page_announcement) {
						$(pager_page_announcement).pagination({
							pageSize:50,
							pageList : [ 50,100,200 ],
							onSelectPage : function(pageNumber, pageSize) {
								findannouncement();
							}
						});
					}
			}
		};
    </script>
    
</body>

</html>