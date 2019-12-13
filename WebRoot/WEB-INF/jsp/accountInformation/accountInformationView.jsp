<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div class ="easyui-layout" data-options="fit:true">
		<div data-options ="region:'west',split:false" title ="用户信息" style ="width: 80%;"> 
			<div id="toolbar_accountInfo" style="height:35px">
				<c:if test="${user.role!=null&&user.role==1}">
					<input id="userId_accountInfo" class="easyui-textbox" data-options="label:'用户ID:',labelWidth:'60px'">
					<input id="jobNumber_accountInfo" class="easyui-textbox" data-options="label:'工号:',labelWidth:'60px'">
					<input id="phone_accountInfo" class="easyui-textbox" data-options="label:'手机号:',labelWidth:'60px'">
					<input id="userName_accountInfo" class="easyui-textbox" data-options="label:'用户姓名:',labelWidth:'60px'">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'" onclick="findAccountInfo()">查询</a>
				</c:if>
				<c:if test="${user.role!=null&&user.role==0}">
					<div   style="display: none">
						<input id="userId_accountInfo" class="easyui-textbox" data-options="label:'用户ID:',labelWidth:'60px'">
						<input id="jobNumber_accountInfo" class="easyui-textbox" data-options="label:'工号:',labelWidth:'60px'">
						<input id="phone_accountInfo" class="easyui-textbox" data-options="label:'手机号:',labelWidth:'60px'">
						<input id="userName_accountInfo" class="easyui-textbox" data-options="label:'用户姓名:',labelWidth:'60px'">
					</div>
				</c:if>
				<c:if test="${user.role!=null&&user.role==1}">
					<a href="javascript:void(0)" class="easyui-linkbutton" id="deleteAccountInfo"
						data-options="iconCls:'icon-remove'" onclick="deleteAccountInfo()">刪除</a>
				</c:if>
				<a href="javascript:void(0)" class="easyui-linkbutton" id="editAccountInfo"
					data-options="iconCls:'icon-edit'" onclick="editAccountInfo()">修改</a>
				<c:if test="${user.role!=null&&user.role==1}">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-add'" onclick="addAccountInfo()">新增</a>	
				</c:if>
				<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-edit'" onclick="updateAccountInfoPasswork()">修改密码</a>
			</div>
			<table id="accountInfo_vv" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:true,
					rownumbers:true,
					toolbar: '#toolbar_accountInfo',
					pagination:true, //包含分页
					pageSize:50,
					clickToEdit:false,
					dblclickToEdit:true">
				<thead>
					<tr>
						<th data-options="field:'user_id',width:40,align:'center'">用户ID</th>
						<th data-options="field:'job_number',width:40,align:'center'">工号</th>
						<th data-options="field:'gender_id',width:20,align:'center',formatter:genderType,editor:
						{ type: 'combobox', options: { data: [{ 'value': 1, 'text': '男' }, { 'value': 2, 'text': '女' }, { 'value': 3, 'text': '其它' }], valueField: 'value', textField: 'text' } }">性别</th>
						<th data-options="field:'employeeTypeId',width:40,align:'center',formatter:employeeType,editor:
						{ type: 'combobox', options: { data: [{ 'value': 1, 'text': '事务所' }, { 'value': 0, 'text': '非事务所' }], valueField: 'value', textField: 'text' } }">员工类型</th>
						<th data-options="field:'user_name',width:40,align:'center',editor:'text'">用户姓名</th>
<!-- 						<th data-options="field:'company_name',width:50,align:'center',formatter:genderType">公司</th> -->
						<th data-options="field:'department',width:50,align:'center',formatter:infoType">部门</th>
						<th data-options="field:'position',width:50,align:'center',formatter:infoType">职位</th>
						<th data-options="field:'role',width:30,align:'center',formatter:roleType">权限</th>
						<th data-options="field:'phone_number',width:50,align:'center',editor:'text'">手机号码</th>
						<th data-options="field:'mail_address',width:50,align:'center',formatter:infoType,editor:'text'">邮箱</th>
						<th data-options="field:'contact_address',width:50,align:'center',formatter:infoType,editor:'text'">联系地址</th>
						<th data-options="field:'emergency_contact_name',width:30,align:'center',editor:'text'">紧急联系人</th>
						<th data-options="field:'emergency_contact_phone',width:50,align:'center',editor:'text'">紧急联系电话</th>
						<th data-options="field:'entry_time',width:50,align:'center'">入职时间</th>
					</tr>
				</thead>
			</table>
			<div id="accountInfo_updatePasswork" class="easyui-dialog" title="密码修改"  style="width:470px;height: 680px;padding:10px;" data-options="modal:true,closed:true">
				<div style="margin: 10px;">
					<input data-options="label:'旧密码'"  class="easyui-textbox" id="accountInfo_oldPasswork" style="width: 300px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'新密码'"  class="easyui-textbox" id="accountInfo_newPasswork" style="width: 300px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'确认密码'"  class="easyui-textbox" id="accountInfo_newPassworkTwo" style="width: 300px;">
				</div>
				<div style="margin: 10px;text-align: center;">
					<a  onclick="submitUpdatePasswork()" class="easyui-linkbutton" style="width: 100px;">保存</a>
					<a  onclick="$('#accountInfo_updatePasswork').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
				</div>
			</div>
			<div id="accountInfo_add" class="easyui-dialog" title="账号新增"  style="width:470px;height: 680px;padding:10px;" data-options="modal:true,closed:true">
				<input id="accountInfo_id" type="hidden">
				<div style="margin: 10px;">
					<input data-options="label:'公司名称',disabled:true"  class="easyui-textbox" id="company_accountInfo" style="width: 300px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'用户ID',disabled:true"  class="easyui-textbox" id="user_id_accountInfo" style="width: 300px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<select id="role_accountInfo" class="easyui-combobox" editable="false"  label="权限" style="width: 300px;">
						<option value="0">普通用户</option>
						<c:if test="${user.company_id!=null&&user.company_id==0}">
							<option value="1">管理员</option>
						</c:if>
					</select>
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<select id="job_number_accountInfo" class="easyui-numberbox"   label="工号" style="width: 300px;"></select>
					<span style="color: red;display: inline;">*必填(100001)</span>
				</div>
				<div style="margin: 10px;">
					<select id="department_accountInfo" class="easyui-combobox" editable="false"  label="部门1" style="width: 300px;"></select>
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<select id="position_accountInfo" class="easyui-combobox" editable="false" label="职位1" style="width: 300px;"></select>
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<select id="department_accountInfo1" class="easyui-combobox" editable="true"  label="部门2" style="width: 300px;"></select>
				</div>
				<div style="margin: 10px;">
					<select id="position_accountInfo1" class="easyui-combobox" editable="true" label="职位2" style="width: 300px;"></select>
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'用户姓名'"  class="easyui-textbox" id="user_name_accountInfo" style="width: 300px;">
				</div>
				<div style="margin: 10px;" id="pwd_div">
					<input data-options="label:'用户密码'"  class="easyui-textbox" id="user_pwd_accountInfo" style="width: 300px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;" id="pwd_div">
					<input data-options="label:'入职时间'"  class="easyui-datebox" id="entry_time_accountInfo" style="width: 300px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<span style="display: inline;margin-right: 50px">性别: </span>
					<span style="display: inline;"><input id="radio_1" type="radio" name="gender" value="1">男
                		<input id="radio_2" type="radio" name="gender" value="2">女
                		<input id="radio_3" type="radio" name="gender" value="3">其它</span>
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'电话号码'"  class="easyui-textbox" id="phone_number_accountInfo" style="width: 300px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'邮箱'"  class="easyui-textbox" id="mail_address_accountInfo" style="width: 300px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'联系地址'"  class="easyui-textbox" id="contact_address_accountInfo" style="width: 300px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'紧急联系人'"  class="easyui-textbox" id="emergency_contact_name_accountInfo" style="width: 300px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'紧急联系人电话',labelWidth:'90px'"  class="easyui-textbox" id="emergency_contact_phone_accountInfo" style="width: 300px;">
				</div>
				<div style="margin: 10px;text-align: center;">
					<a  onclick="submitAccountInfoAdd()" class="easyui-linkbutton" style="width: 100px;">保存</a>
					<a  onclick="$('#accountInfo_add').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
				</div>
			</div>
		</div>
		<div data-options ="region:'center',split:false" title ="权限信息" > 
			<div id="toolbar_authority" style="height:35px">
				<c:if test="${user.role!=null&&user.role==1}">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-remove'" onclick="deleteAuthority()">刪除</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-edit'" onclick="editAuthority()">修改</a> 
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-add'" onclick="addAuthority()">新增</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" id ="addUserAndAuthority"
						data-options="iconCls:'icon-add'" onclick="addUserAndAuthority()">确认权限</a>
				</c:if>
			</div>
			<table id="authority_vv" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:false,
					rownumbers:true,
					toolbar: '#toolbar_authority'">
				<thead>
					<tr>
						<c:if test="${user.role!=null&&user.role==1}">
							<th data-options="field:'ck',checkbox:true"></th>
						</c:if>
						<th data-options="field:'authority_describe',width:50,align:'center'">权限</th>
					</tr>
				</thead>
			</table>
			<div id="authority_add" class="easyui-dialog" title="权限新增"    style="width:300px;height: 200px;padding:10px;" data-options="modal:true,closed:true">
				<input id="authority_id" type="hidden">
				<input id="authority_describe_old" type="hidden">
				<div style="margin: 10px;">
					<input data-options="label:'权限名称'" class="easyui-textbox" id="authority_describe_add" style="width: 200px;">
				</div>
				<div style="margin: 10px;text-align: center;">
					<a  onclick="submitAuthorityAdd()" class="easyui-linkbutton" style="width: 100px;">保存</a>
					<a  onclick="$('#authority_add').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
				</div>
			</div>
		</div>
	</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/accountInformation/accountInformationView.js"></script>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/easyui/datagrid-cellediting.js"></script>
    <script type="text/javascript">
    var clickRowAccountInfo = null;
		var accountInformation_init = true;
		$.parser.onComplete = function(res) {
			if (accountInformation_init) {
				accountInformation_init = false;
				findAuthority();
				findAccountInfo();
				/**部门信息的加载**/
				$('#department_accountInfo').combobox({
					valueField:'id',
				    textField:'text',
			        onSelect:function(record){
			        	debugger;
			        	$('#position_accountInfo').combobox({
			        		url:BASE_PATH+"/AccountInfoController/findPosition.do?department_id="+ record.id ,
			        	})
			        }
				})
				/**职位信息的加载**/
				$('#position_accountInfo').combobox({
					valueField:'id',
				    textField:'text',
				     onSelect:function(record){
			        	debugger;
			        	
			        }
				})
				
				/**部门信息你的加载1**/
				$('#department_accountInfo1').combobox({
					valueField:'id',
				    textField:'text',
			        onSelect:function(record){
			        	debugger;
			        	$('#position_accountInfo1').combobox({
			        		url:BASE_PATH+"/AccountInfoController/findPosition.do?department_id="+ record.id ,
			        	})
			        }
				})
				/**职位信息的加载1**/
				$('#position_accountInfo1').combobox({
					valueField:'id',
				    textField:'text',
				     onSelect:function(record){
			        	debugger;
			        	
			        }
				})
				/**账号信息点击事件的处理**/
				$("#accountInfo_vv").datagrid({
					onClickCell:function(rowIndex, rowData){
						//debugger;
					},
					onClickRow: function(index,res){
						clickRowAccountInfo = res;
			        	var authority = res.authority_id;
			        	$('#authority_vv').datagrid("clearChecked")
			        	if(authority!=null){
			        		var authoritylist = authority.split(",");
			        		var authorityData = $('#authority_vv').datagrid("getData").rows;
			        		if(authorityData!=null){
				        		for(var i = 0;i<authoritylist.length;i++){
				        			if(authoritylist[i]!=null&&authoritylist[i]!==""){
					        			for(var x = 0;x<authorityData.length;x++){
					        				if(parseInt(authoritylist[i])==authorityData[x].id){
					        					$('#authority_vv').datagrid("checkRow",x);
					        					break;
					        				}
											
										}
				        			}
				        		}
								
							}
			        	}
			        	if(loginUser.company_id==0){//如果公司等于0，是admin账号
						
						}else {
							if(loginUser.role==1){
								if(res.role==1&&res.user_id!=loginUser.loginName){//选择的是管理员账号，同时不是自己的账号
									$('#deleteAccountInfo').linkbutton("disable");
									$('#editAccountInfo').linkbutton("disable");
								}else if(res.role==1&&res.user_id==loginUser.loginName){
									$('#deleteAccountInfo').linkbutton("disable");
									$('#editAccountInfo').linkbutton("enable");
								}else if(res.role==0){
									$('#deleteAccountInfo').linkbutton("enable");
									$('#editAccountInfo').linkbutton("enable");
								}
							}else{
								$('#deleteAccountInfo').linkbutton("disable");
								$('#editAccountInfo').linkbutton("enable");
							}
						}
					},
					onSelect:function(index,res){
			        	
			        },
			        onBeforeCellEdit:function(index,field){//在编辑单元格之前触发，返回false以拒绝编辑操作。
			        	debugger;
			        },
			        onCellEdit:function(index,field,value){//在单元格编辑时触发。参数包含:index:编辑行索引。field:列字段名。value:键盘上按下的char代码的初始值。当按DEL或BACKSPACE键时，该值为空字符串。
			        	debugger;
		        	  var ed = $('#accountInfo_vv').datagrid('getEditors', index);  
		        	  var oldInfo = ed[0].oldHtml;
		        	  var dataSelect = $('#accountInfo_vv').datagrid('getRows')[index];
		        	  var accountIndfoId = dataSelect.id;
		        	  var userId = dataSelect.user_id;
		        	  var key = field;
	        	      for (var i = 0; i < ed.length; i++){  
		                var e = ed[i];    
		                   if(e.field == field) {  
			                   if(field==="employeeTypeId"){
			                  		 $(e.target).combobox({
				                     	onChange:function(n,o){
				                     		debugger;
				                     		if(o!=""){
					                     		$.post(BASE_PATH + "/AccountInfoController/updateInfo.do", {
													accountIndfoId : accountIndfoId,
													oldInfo : o,
													newInfo:n,
													key : key,
													userId:userId
												}, function(data) {
													var temp = JSON.parse(data);
													$.messager.show({
											       			title:'提示',
											       			msg:temp.msg,
											       			width:100,                           //设置弹框的宽度和高度
													        height:200,
													        timeout: 1000  
											       		});
												});
				                     		}
				                     	}
				                     })
				                     if(e.oldHtml==="事务所"){
				                     	$(e.target).combobox("setValue",1);
				                     }else if(e.oldHtml==="非事务所"){
				                     	$(e.target).combobox("setValue",0);
				                     }
			                   }else  if(field==="gender_id"){
				                   $(e.target).combobox({
				                     	onChange:function(n,o){
				                     		debugger;
				                     		if(o!=""){
					                     		$.post(BASE_PATH + "/AccountInfoController/updateInfo.do", {
													accountIndfoId : accountIndfoId,
													oldInfo : o,
													newInfo:n,
													key : key,
													userId:userId
												}, function(data) {
													var temp = JSON.parse(data);
													$.messager.show({
											       			title:'提示',
											       			msg:temp.msg,
											       			width:100,                           //设置弹框的宽度和高度
													        height:200,
													        timeout: 1000  
											       		});
												});
				                     		}
				                     	}
				                     })
				                     if(e.oldHtml==="男"){
				                     	$(e.target).combobox("setValue",1);
				                     }else if(e.oldHtml==="女"){
				                     	$(e.target).combobox("setValue",2);
				                     }else if(e.oldHtml==="其它"){
				                     	$(e.target).combobox("setValue",3);
				                     }
			                   }else{
				                   $(e.target).bind("blur",function(){  
				                       debugger; 
				                       var newInfo = this.value;
				                       if(oldInfo!==newInfo){
				                       var boolean = true;
				                       	 switch(field) {
										     case "phone_number":
										       	if(!phoneNumber(newInfo)){
										       	boolean = false;
										       		$.messager.show({
										       			title:'提示',
										       			msg:'号码格式不正确！',
										       			width:100,                           //设置弹框的宽度和高度
												        height:200,
												        timeout: 1000  
										       		});
										       		this.value = oldInfo;
										       	}
										        break;
										     case "mail_address":
										       	if(!mailAddress(newInfo)){
										       		boolean = false;
										       		$.messager.show({
										       			title:'提示',
										       			msg:'邮箱格式不正确！',
										       			width:100,                           //设置弹框的宽度和高度
												        height:200,
												        timeout: 1000  
										       		});
										       		this.value = oldInfo;
										       	}
										        break;
										     case "emergency_contact_phone":
										       if(!phoneNumber(newInfo)){
										       	boolean = false;
										       		$.messager.show({
										       			title:'提示',
										       			msg:'号码格式不正确！',
										       			width:100,                           //设置弹框的宽度和高度
												        height:200,
												        timeout: 1000  
										       		});
										       		this.value = oldInfo;
										       	}
										        break;
										     default:
										} 
										if(boolean){
											$.post(BASE_PATH + "/AccountInfoController/updateInfo.do", {
												accountIndfoId : accountIndfoId,
												oldInfo : oldInfo,
												newInfo:newInfo,
												key : key,
												userId:userId
											}, function(data) {
												var temp = JSON.parse(data);
												$.messager.show({
										       			title:'提示',
										       			msg:temp.msg,
										       			width:100,                           //设置弹框的宽度和高度
												        height:200,
												        timeout: 1000  
										       		});
											});
										}
				                       }
				                    }); 
			                   }
		                   }  
		            	}  
			        },
			        onSelectCell:function(index,field,res){
			        	//debugger;
			        },
			        onUnselectCell : function(index,field){
			        	debugger;
			        }
			        
				})
				var pager_page_accountInfo = $('#accountInfo_vv').datagrid("getPager");
				if (pager_page_accountInfo) {
					$(pager_page_accountInfo).pagination({
						pageSize:50,
						pageList : [ 50,100,200 ],
						onSelectPage : function(pageNumber, pageSize) {
							findAccountInfo();
						}
					});
				}
				$('#accountInfo_vv').datagrid('enableCellEditing');
				$('#accountInfo_vv').datagrid('enableCellSelecting');
			}
		};
    </script>
    
</body>

</html>