<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
</head>
<body>
	<div id="toolbar_holiday" style="height:35px">
			<input id="year_holidy" class="easyui-numberspinner" style="width:200px;"  data-options="min:2019" label="年份:">
			<select id="month_holidy" class="easyui-combobox"
				data-options="editable:false" name="timmerMode_xitongpeizhi"
				label="月份:"   style="width:200px;">
				<option value="1" >一月</option>
				<option value="2">二月</option>
				<option value="3">三月</option>
				<option value="4">四月</option>
				<option value="5">五月</option>
				<option value="6">六月</option>
				<option value="7">七月</option>
				<option value="8">八月</option>
				<option value="9">九月</option>
				<option value="10">十月</option>
				<option value="11">十一月</option>
				<option value="12">十二月</option>
			</select>
	</div>
	<table id="holidayInfo_vv" class="easyui-datagrid"
		data-options="
			fit:true,
			striped:true,
			fitColumns: true,
			singleSelect:true,
			rownumbers:true,
			toolbar: '#toolbar_holiday',
			onDblClickCell: onDblClickCell,
                onEndEdit: onEndEdit,
			rowStyler: function(index,row){
                    if (row.status !=0){
                        return 'background-color:#BCEE68;font-weight:bold;';
                    }
                }">
		<thead>
			<tr>
				<th data-options="field:'date',width:40,align:'center'">日期</th>
				<th data-options="field:'week',width:40,align:'center'">星期</th>
				<th data-options="field:'status',width:40,align:'center',formatter:holidayStatus,editor:{
                            type:'combobox',
                            options: { data: [{ 'productid': 0, 'productname': '工作日' }, { 'productid': 1, 'productname': '休息日' }, { 'productid': 2, 'productname': '节假日' }], valueField: 'productid', textField: 'productname',onChange:onChangeHoliday } 
                        }">状态</th>
			</tr>
		</thead>
	</table>
    <script type="text/javascript"  src="${pageContext.request.contextPath }/js/holidaySettings/holidaySettingsView.js"></script>
    <script type="text/javascript">
		var holidaySettings_init = true;
		var editIndex = undefined;
		$.parser.onComplete = function(res) {
			if (holidaySettings_init) {
				holidaySettings_init = false;
				debugger;
				var newDate = new Date();
				$('#year_holidy').numberspinner({
					onChange:function(n,o){
						debugger;
						if(!isNotNull(n)){
							$('#year_holidy').numberspinner("setValue",o);
						}else{
							findWorkingTime();
						}
					}
				})
				$('#year_holidy').numberspinner("setValue",newDate.getFullYear());
				var month = newDate.getMonth()+1+"";
				$('#month_holidy').combobox({
					onChange:function(n,o){
						debugger;
						findWorkingTime();
					}
				})
				$('#month_holidy').combobox("setValue",month);
       
			}
		};
		$('#holidayInfo_vv').datagrid({
			onSelect:function(index,row){
				$('#holidayInfo_vv').datagrid('endEdit', editIndex);
                editIndex = undefined;
			}
		})
		function endEditing(){
            if (editIndex == undefined){return true}
            if ($('#holidayInfo_vv').datagrid('validateRow', editIndex)){
                $('#holidayInfo_vv').datagrid('endEdit', editIndex);
                editIndex = undefined;
                return true;
            } else {
                return false;
            }
        }
        function onDblClickCell(index, field){
        debugger;
            if (editIndex != index){
                if (endEditing()){
                    $('#holidayInfo_vv').datagrid('selectRow', index)
                            .datagrid('beginEdit', index);
                    var ed = $('#holidayInfo_vv').datagrid('getEditor', {index:index,field:field});
                    if (ed){
                        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                    }
                    editIndex = index;
                } else {
                    setTimeout(function(){
                        $('#holidayInfo_vv').datagrid('selectRow', editIndex);
                    },0);
                }
            }
        }
        function onEndEdit(index, row){
//             var ed = $(this).datagrid('getEditor', {
//                 index: index,
//                 field: 'status'
//             });
//             row.status = $(ed.target).combobox('getText');
				debugger;
        }
        function onChangeHoliday(n,o){
        	debugger;
        	if(isNotNull(n)&&isNotNull(o)){
        		var data = $('#holidayInfo_vv').datagrid('getSelected');
	        	var id = data.id;
	        	var status = n;
	        	var oldStatusName = statusName(o);
	        	var newStatusName = statusName(n);
	        	var date = data.date;
	        	$.post(BASE_PATH + "/HolidaySettingController/updateWorkingInfo.do", {
					id : id,
					status : status,
					oldStatusName : oldStatusName,
					newStatusName : newStatusName,
					date : date
				}, function(data) {
					try {
						var temp = JSON.parse(data);
						$.messager.show({
				       			title:'提示',
				       			msg:temp.msg,
				       			width:100,                           //设置弹框的宽度和高度
						        height:200,
						        timeout: 1000  
				       		});
					} catch (e) {
						console.log(e);
					}
					
				});
        	}
        }
        function statusName(temp){
        	var res = "";
        	if(parseInt(temp)==0){
        		res = "工作日";
        	}else if(parseInt(temp)==1){
        		res = "休息日";
        	}if(parseInt(temp)==2){
        		res = "节假日";
        	}
        	return res;
        }
    </script>
    
</body>

</html>