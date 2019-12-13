<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div class ="easyui-layout" data-options="fit:true">
		<div data-options ="region:'west',split:true" title ="公司信息" style ="width: 45%;"> 
			<c:if test="${user.company_id!=null&&user.company_id==0}">
				<div id="toolbar_companyInfo" style="height:35px">
						<input id="company_name" class="easyui-textbox"   data-options="label:'公司名称:',labelWidth:'60px'">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'" onclick="findCompanyInfo()">查询</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-remove'" onclick="deleteCompanyInfo()">刪除</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-edit'" onclick="editCompanyInfo()">修改</a> 
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-add'" onclick="addCompanyInfo()">新增</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-edit'" onclick="editCompanyDeletePwd()">修改操作密码</a>	
				</div>
			</c:if>	
			<c:if test="${user.company_id!=null&&user.company_id!=0}">
				<div id="toolbar_companyInfo" style="height:1px">
					<input id="company_name" class="easyui-textbox" type="hidden"  data-options="label:'公司名称:',labelWidth:'60px'">
				</div>
			</c:if>
			<table id="companyInfo_vv" class="easyui-datagrid"
				data-options="
					fit:true,
					striped:true,
					fitColumns: true,
					singleSelect:true,
					rownumbers:true,
					toolbar: '#toolbar_companyInfo',
					pagination:true, //包含分页
					pageSize:50">
				<thead>
					<tr>
						<th data-options="field:'company_name',width:70,align:'center',formatter:addressType">公司名称</th>
						<th data-options="field:'duty_paragraph',width:50,align:'center',formatter:addressType">税号</th>
						<th data-options="field:'address',width:70,align:'center',formatter:addressType">地址</th>
						<th data-options="field:'landline_number',width:40,align:'center',formatter:addressType">号码</th>
						<th data-options="field:'fax',width:30,align:'center',formatter:addressType">传真</th>
						<th data-options="field:'mail',width:30,align:'center',formatter:addressType">邮箱</th>
						<th data-options="field:'bank_account',width:60,align:'center',formatter:addressType">卡号</th>
						<th data-options="field:'enable_application',width:60,align:'center',formatter:enableApplication">启用申请</th>
					</tr>
				</thead>
			</table>
			<div id="company_add" class="easyui-dialog" title="公司新增"    style="width:500px;height: 650px;padding:10px;" data-options="modal:true,closed:true">
				<input id="company_id" type="hidden">
				<input id="company_name_old" type="hidden">
				<div style="margin: 10px;">
					<input data-options="label:'公司名称'" class="easyui-textbox" id="company_name_add" style="width: 370px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'公司税号'" class="easyui-textbox" id="company_duty_paragraph_add" style="width: 370px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'联系地址'" class="easyui-textbox" id="company_address_add" style="width: 370px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'座机号码'" class="easyui-textbox" id="company_landline_number_add" style="width: 370px;">
					<span style="color: red;display: inline;">*必填</span>
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'公司官网'" class="easyui-textbox" id="company_official_website_add" style="width: 370px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'传真'" class="easyui-textbox" id="company_fax_add" style="width: 370px;">
				</div>
				<div style="margin: 10px;">
					<input data-options="label:'邮箱'" class="easyui-textbox" id="company_mail_add" style="width: 370px;">
				</div>
				<div style="margin: 10px;">
					<span style="display: inline;margin-right: 50px">启用申请: </span>
					<span style="display: inline;"><input id="radio_enable_1" type="radio" name="enable_application" value="1">启用
                	<input id="radio_enable_2" type="radio" name="enable_application" value="0">不启用</span>
                </div>
				<div style="margin: 10px;">
					<input data-options="label:'银行账号'" class="easyui-textbox" id="company_bank_account_add" style="width: 370px;">
					<span style="color: red;display: inline;" id="bankAccountRes">*必填</span>
				</div>
				<div style="margin: 10px;display: none" id="bank_div">
					<input data-options="label:'银行',readonly:true" class="easyui-textbox" id="company_bank_add" style="width: 370px;">
				</div>
				<div style="margin: 10px;display: none" id="bankType_div">
					<input data-options="label:'类型',readonly:true" class="easyui-textbox" id="company_bankType_add" style="width: 370px;">
				</div>
				<div style="margin: 10px;text-align: center;">
					<a  onclick="submitCompanyAdd()" class="easyui-linkbutton" style="width: 100px;">保存</a>
					<a  onclick="$('#company_add').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
				</div>
			</div>
			
			<div id="company_deletePwd_edit" class="easyui-dialog" title="删除密码修改"  style="width:300px;height: 150px;padding:10px;" data-options="modal:true,closed:true">
				<div style="margin: 10px;">
					<input data-options="label:'密码:'" class="easyui-textbox" id="company_deletePwd" style="width: 200px;">
				</div>
				<div style="margin: 10px;text-align: center;">
					<a  onclick="submitCompanydeletePwd_edit()" class="easyui-linkbutton" style="width: 100px;">保存</a>
					<a  onclick="$('#company_deletePwd_edit').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
				</div>
			</div>
		</div>
		<div data-options ="region:'center',split:true" title ="部门" > 
			<div class ="easyui-layout" data-options="fit:true">
				<div data-options ="region:'north',split:false"  style ="height: 8%;text-align: center"> 
					<span id="selected_company" style="text-align: center;color: green;font-size: 16px;font-weight: bold;"></span>
				</div>
				<div data-options ="region:'center',split:true" > 
					<div id="toolbar_departMent" style="height:35px">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'" onclick="findDepartMent()">查询</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-remove'" onclick="deleteDepartMent()">刪除</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-edit'" onclick="editDepartMent()">修改</a> 
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-add'" onclick="addDepartMent()">新增</a>
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-add'" onclick="confirmPriority()">确认排序</a>
					</div>
					<table id="departMent_vv" class="easyui-datagrid"
						data-options="
							fit:true,
							striped:true,
							fitColumns: true,
							singleSelect:true,
							rownumbers:true,
							toolbar: '#toolbar_departMent'">
						<thead>
							<tr>
								<th data-options="field:'department',width:50,align:'center'">部门名称</th>
								<th data-options="field:'priority',width:50,align:'center'">排序</th>
							</tr>
						</thead>
					</table>
					<div id="department_add" class="easyui-dialog" title="部门新增"    style="width:300px;height: 200px;padding:10px;" data-options="modal:true,closed:true">
						<input id="department_id" type="hidden">
						<input id="department_name_old" type="hidden">
						<input id="department_priority_old" type="hidden">
						<div style="margin: 10px;">
							<input data-options="label:'部门名称'" class="easyui-textbox" id="department_name_add" style="width: 200px;">
						</div>
						<div style="margin: 10px;">
							<input data-options="label:'排序',min:1,max:99,editable:false" class="easyui-numberspinner" id="department_priority_add" style="width: 200px;">
						</div>
						<div style="margin: 10px;text-align: center;">
							<a  onclick="submitDepartmentAdd()" class="easyui-linkbutton" style="width: 100px;">保存</a>
							<a  onclick="$('#department_add').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div data-options ="region:'east',split:true" title ="职位" style ="width: 33%;"> 
			<div class ="easyui-layout" data-options="fit:true">
				<div data-options ="region:'west',split:false"  style ="width: 68%;text-align: center"> 
					<div class ="easyui-layout" data-options="fit:true">
						<div data-options ="region:'north',split:false"  style ="height: 8%;text-align: center"> 
							<span id="selected_department" style="text-align: center;color: green;font-size: 16px;font-weight: bold;"></span>
						</div>
						<div data-options ="region:'center',split:true" > 
							<div id="toolbar_position" style="height:35px">
								<a href="javascript:void(0)" class="easyui-linkbutton"
									data-options="iconCls:'icon-search'" onclick="findPosition()">查询</a>
								<a href="javascript:void(0)" class="easyui-linkbutton"
									data-options="iconCls:'icon-remove'" onclick="deletePosition()">刪除</a>
								<a href="javascript:void(0)" class="easyui-linkbutton"
									data-options="iconCls:'icon-edit'" onclick="editPosition()">修改</a> 
								<a href="javascript:void(0)" class="easyui-linkbutton"
									data-options="iconCls:'icon-add'" onclick="addPosition()">新增</a>
								<a href="javascript:void(0)" class="easyui-linkbutton"
									data-options="iconCls:'icon-add'" onclick="positionPriority()">确认排序</a>
							</div>
							<table id="position_vv" class="easyui-datagrid"
								data-options="
									fit:true,
									striped:true,
									fitColumns: true,
									singleSelect:true,
									rownumbers:true,
									toolbar: '#toolbar_position'">
								<thead>
									<tr>
										<th data-options="field:'position',width:50,align:'center'">职位名称</th>
										<th data-options="field:'priority',width:50,align:'center'">排序</th>
									</tr>
								</thead>
							</table>
							<div id="position_add" class="easyui-dialog" title="职位新增"    style="width:300px;height: 200px;padding:10px;" data-options="modal:true,closed:true">
								<input id="position_id" type="hidden">
								<input id="position_name_old" type="hidden">
								<div style="margin: 10px;">
									<input data-options="label:'职位名称'" class="easyui-textbox" id="position_name_add" style="width: 200px;">
								</div>
								<div style="margin: 10px;text-align: center;">
									<a  onclick="submitPositionAdd()" class="easyui-linkbutton" style="width: 100px;">保存</a>
									<a  onclick="$('#position_add').dialog('close')" class="easyui-linkbutton" style="width: 100px;">取消</a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div data-options ="region:'center',split:true" > 
					<div id="toolbar_positionUser" style="height:35px">
						<a href="javascript:void(0)" class="easyui-linkbutton"
							data-options="iconCls:'icon-add'" onclick="positionUserPriority()">确认排序</a>
					</div>
					<table id="positionUser_vv" class="easyui-datagrid"
						data-options="
							fit:true,
							striped:true,
							fitColumns: true,
							singleSelect:true,
							rownumbers:true,
							toolbar: '#toolbar_positionUser'">
						<thead>
							<tr>
								<th data-options="field:'user_name',width:50,align:'center'">人员名称</th>
								<th data-options="field:'priority',width:50,align:'center'">排序</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/company/companyView.js"></script>
    <script type="text/javascript">
		var company_init = true;
		$.parser.onComplete = function(res) {
			if (company_init) {
			debugger;
				company_init = false;
				findCompanyInfo();
				$('#company_bank_account_add').textbox('textbox').bind('keyup', function(e){
						debugger;
						if(isNotNull(this.value)){
							console.log(bankCardAttribution(this.value));
							var res = bankCardAttribution(this.value);
							if(typeof(res)==="object"){
								$('#bankAccountRes')[0].innerText="√";
								$('#bank_div').show();
								$('#bankType_div').show();
								$('#company_bank_add').textbox("setValue",res.bankName);
								$('#company_bankType_add').textbox("setValue",res.cardTypeName);
							}else{
								$('#bank_div').hide();
								$('#bankType_div').hide();
								$('#bankAccountRes')[0].innerText="格式不正确";
							}
						}else{
								$('#bank_div').hide();
								$('#bankType_div').hide();
							$('#bankAccountRes')[0].innerText="*必填";
						}
                  });
                 
				$('#companyInfo_vv').datagrid({
					 onLoadSuccess: function(data) {
				    	debugger;
				    	if(data.rows.length>0){
				    		$(this).datagrid("selectRow",0);
				    	}
				    },
					onSelect : function(index,res){
						debugger;
						$("#selected_company")[0].innerText = "当前选中公司:"+res.company_name;
						$('#position_vv').datagrid("loadData",[]);
							$("#selected_department")[0].innerText = "";
						findDepartMent();
					}
				})
				
				$('#departMent_vv').datagrid({
					 onLoadSuccess: function(data) {
				    	debugger;
				    	if(data.rows.length>0){
				    		$(this).datagrid("selectRow",0);
				    	}
				    	$(this).datagrid('enableDnd');
				    },
					onSelect : function(index,res){
						debugger;
					$("#selected_department")[0].innerText = "当前选中部门:"+res.department;
						findPosition();
					}
				})
				
				$('#position_vv').datagrid({
					 onLoadSuccess: function(data) {
				    	debugger;
				    	if(data.rows.length>0){
				    		$(this).datagrid("selectRow",0);
				    	}
				    	$(this).datagrid('enableDnd');
				    },
					onSelect : function(index,res){
						debugger;
						findPositionUser(res);
					}
				})
				
				$('#positionUser_vv').datagrid({
					 onLoadSuccess: function(data) {
				    	debugger;
				    	if(data.rows.length>0){
				    		$(this).datagrid("selectRow",0);
				    	}
				    	$(this).datagrid('enableDnd');
				    },
					onSelect : function(index,res){
						debugger;
					}
				})
				
				var pager_page_company = $('#companyInfo_vv').datagrid("getPager");
				//公司信息分页
				if (pager_page_company) {
					$(pager_page_company).pagination({
						pageSize:50,
						pageList : [ 50,100,200 ],
						onSelectPage : function(pageNumber, pageSize) {
							findCompanyInfo();
						}
					});
				}
			}
		};
    </script>
    
</body>

</html>