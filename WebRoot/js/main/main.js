var  printtype =-1;
var  printflag = 0;//判断打印的是否是发票

		function hieClass(hieId,hieType,isFix){ 
                 this.hieId=hieId;
                 this.hieType=hieType;
                 this.isFix = isFix;
         }
         function showMessage(msg){
        	 $.messager.show({
					title:'提示!',
					msg:msg,
					showType:'show'
//					style:{right:'',bottom:''}
				});
         }
         function hieIdInit(){
        	curOptParam.optHieType = curSelHie.Nothing;//当前选中层级类型
     		curOptParam.optHieId = curSelHie.Nothing;//当前层级ID
     		curOptParam.exchangstationId = curSelHie.Nothing;
     		curOptParam.villageId = curSelHie.Nothing;
     		curOptParam.buildingId = curSelHie.Nothing;
     		curOptParam.entranceId = curSelHie.Nothing;
            curOptParam.exchangstation = "";
            curOptParam.village = "";
            curOptParam.building = "";
            curOptParam.entrance = "";
         }
      	var isfinish = false;
      	function findChildNode(datas,strtext){
      		for(var i=0;i<datas.length;i++){
      			if((datas[i].hasOwnProperty('children'))&&(datas[i].children.length>0)){
      				findChildNode(datas[i].children,strtext);
      			}else{
      				if(datas[i].view_Name===strtext){
	      				$('#areaList').treegrid('select',datas[i].view_Id);
	      				isfinish = true;
	      				break;
	      			}
      			}
      			if(isfinish)
      				break;
      		}
      	}
      
      	function showTab(tbtext){
      		var tgdata = $('#areaList').treegrid('getData');
      		isfinish = false;
      		findChildNode(tgdata,tbtext);
      	}
		//选择创建新tab页面
		function openTab(row,node){
            //var pnode  = node;
            var prow = row;
     		var paramArray = new Array();
            var rootnode = $('#areaList').treegrid('getRoot');
            var jsonsParam = JSON.stringify(paramArray); 
			var url = BASE_PATH+prow.value;
			if ($('#mainTab').tabs('exists',prow.text)){
				$('#mainTab').tabs('select',prow.text);
                $('#mainTab').tabs('getSelected').panel({ href:url});
				$('#mainTab').tabs('getSelected').panel('refresh');
			} else {
				$('#mainTab').tabs('add',{
					title:prow.text,
					href:url,
					closable:true
				});
			}
		}
		
		 //加载树形结构数据
		 function refreshAreaList(){
		 	$.getJSON(BASE_PATH+"/areaTree/getAsyncTree.do?loginid="+loginUser.tableLogin.login,
		 		function(areaListData){
		 			$("#areaList").treegrid('loadData',areaListData);
		 		});
		 }


		
		//删除Tabs
        function closeTab(menu, type){
            var allTabs = $("#mainTab").tabs('tabs');
            var allTabValue = [];
            var curTabTitle = $(menu).data("tabTitle");
            var curIndex = -1;
            $.each(allTabs,function(i,n){
                var opt=$(n).panel('options');
                if(opt.title===curTabTitle)
                	curIndex=i;
                if(opt.closable)
                    allTabValue.push({index:i,text:opt.title});
            });
            
            switch (type){
                case 1 :
                   // $("#mainTab").tabs("close", curTabTitle);
                   // return false;
                break;
                case 2 :
                    for(var i=0;i<allTabValue.length;i++){
                        $('#mainTab').tabs('close', allTabValue[i].text);
                    }
                break;
                case 3 :
                      for(var i=0;i<allTabValue.length;i++){
                    	  if(allTabValue[i].text!==curTabTitle)
                    		  $('#mainTab').tabs('close', allTabValue[i].text);
                      }
                break;
                case 4 :
                	$.each(allTabValue,function(i,val){
                        if(val.index<curIndex){
                        	 $('#mainTab').tabs('close', val.text);
                        }     
                    });
                break;
                case 5 :
                	$.each(allTabValue,function(i,val){
                        if(val.index>curIndex){
                        	 $('#mainTab').tabs('close', val.text);
                        }
                    });
                break;
            }
        }
        /** 定义收费方式 */
        var chargingMethod=[{
					value: 1,
					label: '现金'
        		}];
        /** 定义用户类型 */
        var customerType=[{
		        	value: 10,
					label: '集体用户'
        		},{
        			value: 11,
					label: '个人用户'
        		}];
    	/** 查询抄表模式 */
		var inquiryMeterReadingMode=[{
					value: 23,
					label: '单月'
				},{
					value: 24,
					label: '双月'
				},{
					value: 25,
					label: '每月'
				}];
    	/** 查询时间范围 */
		var queryTimeRange=[{
					value: 0,
					label: '当月'
				},{
					value: 1,
					label: '当日'
				},{
					value: 2,
					label: '自定义'
				}];
		
		/**票据状态*/
		var combo_pjzt=[{
			value: 0,
			label: '全部'
		},{
			value: 1,
			label: '已开'
		},{
			value: 2,
			label: '未开'
		}];
		
		/**呆死账**/
		var combo_dszlx=[{
			value: 0,
			label: '全部'
		},{
			value: 1,
			label: '呆账'
		},{
			value: 2,
			label: '死账'
		}];
		
		/**通讯类型**/
		var communication_yhjcxx=[{
			value: 0,
			label: '无'
		},{
			value: 1,
			label: 'GPRS集中器'
		},{
			value: 2,
			label: 'GPRS采集终端'
		}];
		/**阀控表模式**/
		var valveControlMeterMode=[{
			value: 0,
			label: '普通'
		},{
			value: 1,
			label: '充值缴费'
		},{
			value: 2,
			label: '强制开关阀'
		}];
		/** 获取当前时间 */
		function getNowFormatDate() {
		    var date = new Date();
		    var seperator1 = "-";
		    var month = ("0" + (date.getMonth() + 1)).slice(-2);
		    var strDate = ("0" + date.getDate()).slice(-2);
		    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
		    return currentdate;
		}
		
		/** 获取每月第一天 */
		function getNowFirstDate() {
		    var date = new Date();
		    var seperator1 = "-";
		    var month = ("0" + (date.getMonth() + 1)).slice(-2);
		    var currentdate = date.getFullYear()+seperator1+month+seperator1+"01";
		    return currentdate;
		}
		
        /*发票打印时间*/
		function getNowDate() {
		    var date = new Date();
		    var seperator1 = "-";
		    var seperator2 = ":";
		    var month = ("0" + (date.getMonth() + 1)).slice(-2);
		    var strDate = ("0" + date.getDate()).slice(-2);
		    var minutes = ("0" + date.getMinutes()).slice(-2);
		    var second = ("0" + date.getSeconds()).slice(-2);
		    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
		            + " " + date.getHours() + seperator2 + minutes
		            + seperator2 + second;
		    return currentdate;
		}

//总月收费统计
function showJiaoFeiChart(){
	var data = [{
        name: '未缴费',
        data: [1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 1210, 1310]
    },{
        name: '已缴费',
        data: [9000, 9100, 9200, 9300, 9400, 9500, 9600, 9700, 9800, 9900, 9210, 9310]
    }];
	
	 $('#chart_jfbl_month').highcharts({
		    chart: {
		        type: 'column'
		    },
		    title: {
		        text: "缴费统计"
		    },
		    xAxis: {
		        categories: ['一月', '二月', '三月', '四月', '五月','六月', '七月', '八月', '九月', '十月','十一月','十二月']
		    },
		    yAxis: {
		        min: 0,
		        title: {
		            text: '缴费比例'
		        }
		    },
		    tooltip: {
		        pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.1f}%)<br/>',
		        shared: true
		    },
		    plotOptions: {
		        column: {
		            stacking: 'percent',
		            events: {
				         click: function(e) {
							alert(e.point.category+"->"+this.name+":"+e.point.percentage+"%");
					    }
					}
		        }
		    },
		    series: data
		});
	
}
//总年度收费统计
function showChanXiaoChaChart(nopaycost_totle,yespaycost_totle){
	
	
	$('#chart_cxctj_month').highcharts({
		chart: {
            type: 'pie',
            options3d: {
                enabled: true,
                alpha: 45,
                beta: 0
            }
        },
        title: {
            text: "产销差统计"
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                depth: 35,
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                }
            }
        },
        series: [{
            type: 'pie',
            name: '产销比',
            data: [
                ['欠费',   20],
                ['已收费',  80]
            ]
        }]    
	});
};
//鼠标悬停提示
function remarkFormater(value, row, index){    
	var content = '';   
	var abValue = value +'';   
	if(value != undefined){      
		if(value.length>=5) {         
			abValue = value.substring(0,5) + "...";        
			content = '<a href="javascript:void(0);"  title="' + value + '" class="easyui-tooltip">' + abValue + '</a>';      
		}
		else{   
		    content = '<a href="javascript:void(0);"  title="' + abValue + '" class="easyui-tooltip">' + abValue + '</a>';     
		}  
	} 
	return content;
}
//判断户号输入是否正确
function judgeUserCode(userCode){
	var re = /^[0-9]+.?[0-9]*$/;//判断是否是数字
	if (!re.test(userCode)) {
		return 0;
	}else{
		return 1;
	}
}
//用户查询搜索框
function doSearch_mian(){
	var usercode_main=$('#usercode_main').textbox('getValue');//得到输入户号
	var bodyNumber_main=$('#bodyNumber_main').textbox('getValue');//表身号
	var name_main=$('#name_main').textbox('getValue');//用户/单位名称
	var drs_main=$('#drs_main').textbox('getValue');//用户/单位地址
	var userCode='';//查询所用户号
	if(usercode_main.trim()!=''){
		if(judgeUserCode(usercode_main)==1){
			userCode= ("00000" + (usercode_main*1)).slice(-6);
			$("#usercode_main").textbox("setValue",userCode);
		}else{
			showMessage('输入非法,查询全部!');
			$('#usercode_main').textbox('textbox').select();
		}
	}
	$.getJSON(BASE_PATH+"/main/doSearch_mian.do?userCode="+userCode.trim()+"&bodyNumber="+bodyNumber_main.trim()+"&name="+name_main.trim()+"&drs="+drs_main.trim(),
			function (result){
				if(result.length>0){
					$('#userInformation_main').datagrid('loadData',result);
					$('#usercode_main').textbox('textbox').select();
				}else{
					showMessage('未找到用户信息，请检查输入信息是否正确!');
					$('#usercode_main').textbox('textbox').select();
				}
		});
}
//打开用户查询弹窗
function userSeachOpen_main(val){
	resetUserInfo_main();
	$('#userInformation_main').datagrid('loadData',[]);
	$('#usercode_main').textbox('reset');//户号重置
	$('#bodyNumber_main').textbox('reset');//表身号重置
	$('#name_main').textbox('reset');//用户/单位名称重置
	$('#drs_main').textbox('reset');//用户/单位地址重置
	$('#userSeach_main').window('open');
	if(val==1){
		$('#sure_main').linkbutton('enable');
	}else{
		$('#sure_main').linkbutton('disable');
	}
	//加载页面时给输入表号框赋值光标
	$('#usercode_main').textbox('textbox').focus();
}
//赋值用户详细信息
function assignUserInfo_main(val){
	$('#c_name_main').textbox('setValue',val.c_name);
	$('#c_ident_main').textbox('setValue',val.c_ident);
	$('#c_sn_main').textbox('setValue',val.c_sn);
	$('#c_tel1_main').textbox('setValue',val.c_tel1);
	$('#c_account_main').textbox('setValue',val.c_account);
	$('#c_bankid_main').textbox('setValue',val.c_bankid);
	$('#c_kind_main').textbox('setValue',val.c_kind);
	$('#c_paymode_main').textbox('setValue',val.c_paymode);
	$('#c_remark_main').textbox('setValue',val.c_remark);
	$('#c_save_main').textbox('setValue',val.c_save);
	
	$('#c_ndate_main').datebox('setValue',val.c_ndate);
	$('#c_addr_main').textbox('setValue',val.c_addr);
	$('#c_tel2_main').textbox('setValue',val.c_tel2);
	$('#c_tel3_main').textbox('setValue',val.c_tel3);
	$('#cp_name_main').textbox('setValue',val.cp_name);
	$('#c_createdate_main').datebox('setValue',val.c_createdate);
	$('#dn_name_main').textbox('setValue',val.dn_name);
	$('#c_destroydate_main').datebox('setValue',val.c_destroydate);
	$('#c_chdate_main').datebox('setValue',val.c_chdate);
	$('#c_copymode_main').textbox('setValue',val.c_copymode);
	
	$('#c_oddment_main').textbox('setValue',val.c_oddment);
	$('#c_invoicetype_main').textbox('setValue',val.c_invoicetype);
	$('#c_area_main').textbox('setValue',val.c_area);
	$('#wm_code_main').textbox('setValue',val.wm_code);
	$('#pr_name1_main').textbox('setValue',val.pr_name1);
	$('#wm_scale1_main').textbox('setValue',val.wm_scale1);
	$('#pr_name3_main').textbox('setValue',val.pr_name3);
	$('#wm_scale3_main').textbox('setValue',val.wm_scale3);
	$('#sg_desc1_main').textbox('setValue',val.sg_desc1);
	$('#c_pause_main').textbox('setValue',val.c_pause);
	$('#wv_caliber_main').textbox('setValue',val.wv_caliber);
	$('#wm_count_main').textbox('setValue',val.wm_count);
	$('#pr_name2_main').textbox('setValue',val.pr_name2);
	$('#wm_scale2_main').textbox('setValue',val.wm_scale2);
	
	$('#waterworks_main').textbox('setValue',val.c_waterworks);
	$('#dutyParagraph_main').textbox('setValue',val.c_duty);
	
	$('#c_closing_main').textbox('setValue',val.c_closing);
	$('#c_opening_main').textbox('setValue',val.c_opening);
}
//重置用户详细信息
function resetUserInfo_main(){
	$('#c_name_main').textbox('reset');
	$('#c_ident_main').textbox('reset');
	$('#c_sn_main').textbox('reset');
	$('#c_tel1_main').textbox('reset');
	$('#c_account_main').textbox('reset');
	$('#c_bankid_main').textbox('reset');
	$('#c_kind_main').textbox('reset');
	$('#c_paymode_main').textbox('reset');
	$('#c_remark_main').textbox('reset');
	$('#c_save_main').textbox('reset');
	
	$('#c_ndate_main').datebox('reset');
	$('#c_addr_main').textbox('reset');
	$('#c_tel2_main').textbox('reset');
	$('#c_tel3_main').textbox('reset');
	$('#cp_name_main').textbox('reset');
	$('#c_createdate_main').datebox('reset');
	$('#dn_name_main').textbox('reset');
	$('#c_destroydate_main').datebox('reset');
	$('#c_chdate_main').datebox('reset');
	$('#c_copymode_main').textbox('reset');
	
	$('#c_oddment_main').textbox('reset');
	$('#c_invoicetype_main').textbox('reset');
	$('#c_area_main').textbox('reset');
	$('#wm_code_main').textbox('reset');
	$('#pr_name1_main').textbox('reset');
	$('#wm_scale1_main').textbox('reset');
	$('#pr_name3_main').textbox('reset');
	$('#wm_scale3_main').textbox('reset');
	$('#sg_desc1_main').textbox('reset');
	$('#c_pause_main').textbox('reset');
	$('#wv_caliber_main').textbox('reset');
	$('#wm_count_main').textbox('reset');
	
	$('#pr_name2_main').textbox('reset');
	$('#wm_scale2_main').textbox('reset');
	
	$('#waterworks_main').textbox('reset');
	$('#dutyParagraph_main').textbox('reset');
	$('#c_closing_main').textbox('reset');
	$('#c_opening_main').textbox('reset');
}
//打开更换税号窗口
function invoiceUpdateOpen_main(){
	$('#userCodeInvoice_main').textbox('reset');//户号重置
	$('#oldInvoice_main').textbox('reset');//原税号重置
	$('#userNameInvoice_main').textbox('reset');//户名重置
	$('#newInvoice_main').textbox('reset');//新税号重置
	$('#invoiceUpdate_main').window('open');
	//加载页面时给输入户号框赋值光标
	$('#userCodeInvoice_main').textbox('textbox').focus();
}
//搜索户号税号信息
function doSearchInvoice_mian(){
	var code=$('#userCodeInvoice_main').textbox('getValue');
	if(code.trim()!=''){
		if(judgeUserCode(code.trim())==1){
			userCode= ("00000" + (code.trim()*1)).slice(-6);
			$("#userCodeInvoice_main").textbox("setValue",userCode);
			$('#userCodeInvoice_main').textbox('textbox').focus();
			
			$.getJSON(BASE_PATH+"/sjdx/sjhdj/queryUserInforPhone_sjdxhdj.do?userCode="+userCode,
					function (result){
						if(result.msgRecord!=0){
							$("#userNameInvoice_main").textbox("setValue",result.datainfo.c_name);//户名
							$("#oldInvoice_main").textbox("setValue",result.datainfo.c_duty);//原税号
							$('#newInvoice_main').textbox('textbox').focus();
						}else{
							showMessage("此用户不存在，请核实户号!");
							$('#userCodeInvoice_main').textbox('textbox').focus();
						}
				});
		}else{
			showMessage('输入非法,查询全部!');
			$('#userCodeInvoice_main').textbox('textbox').focus();
		}
	}else{
		showMessage('请输入户号进行查询!');
		$('#userCodeInvoice_main').textbox('textbox').focus();
	}
}
//更新税号
function updateInvoiceDo_main(){
	var code=$('#userCodeInvoice_main').textbox('getValue');//户号
	var newInvoice=$('#newInvoice_main').textbox('getValue'); //新手机号
	var oldInvoice=$('#oldInvoice_main').textbox('getValue'); //原手机号
	if(newInvoice.trim()!=''){
		$.messager.confirm('确认','您确认想要修改此用户税号吗？',function(r){    
		    if (r){
				$.getJSON(BASE_PATH+"/sjdx/sjhdj/updateInvoiceDo_sjdxhdj.do?userCode="+code+"&newInvoice="+newInvoice+"&oldInvoice="+oldInvoice,
						function (result){
							showMessage(result.msg);
							doSearchInvoice_mian();
							$('#newInvoice_main').textbox('reset'); //新税号
					});
		    }
		}); 
	}else{
		showMessage('新手机号不允许为空!');
	}
};
//打开更换手机号码窗口
function phoneUpdateOpen_main(){
	$('#userCodePhone_main').textbox('reset');//户号重置
	$('#oldPhone_main').textbox('reset');//原号码重置
	$('#userNamePhone_main').textbox('reset');//户名重置
	$('#newPhone_main').textbox('reset');//新号码重置
	$('#phoneUpdate_main').window('open');
	//加载页面时给输入户号框赋值光标
	$('#userCodePhone_main').textbox('textbox').focus();
}
//搜索户号手机信息
function doSearchPhone_mian(){
	var code=$('#userCodePhone_main').textbox('getValue');
	if(code.trim()!=''){
		if(judgeUserCode(code.trim())==1){
			userCode= ("00000" + (code.trim()*1)).slice(-6);
			$("#userCodePhone_main").textbox("setValue",userCode);
			$('#userCodePhone_main').textbox('textbox').focus();
			
			$.getJSON(BASE_PATH+"/sjdx/sjhdj/queryUserInforPhone_sjdxhdj.do?userCode="+userCode,
					function (result){
						if(result.msgRecord!=0){
							$("#userNamePhone_main").textbox("setValue",result.datainfo.c_name);//户名
							$("#oldPhone_main").textbox("setValue",result.datainfo.c_tel1);//原手机号
							$('#newPhone_main').textbox('textbox').focus();
						}else{
							showMessage("此用户不存在，请核实户号!");
							$('#userCodePhone_main').textbox('textbox').focus();
						}
				});
		}else{
			showMessage('输入非法,查询全部!');
			$('#userCodePhone_main').textbox('textbox').focus();
		}
	}else{
		showMessage('请输入户号进行查询!');
		$('#userCodePhone_main').textbox('textbox').focus();
	}
}
//更新手机号
function updatePhoneDo_main(){
	var code=$('#userCodePhone_main').textbox('getValue');//户号
	var newPhone=$('#newPhone_main').textbox('getValue'); //新手机号
	var oldPhone=$('#oldPhone_main').textbox('getValue'); //原手机号
	if(newPhone.trim()!=''){
		$.messager.confirm('确认','您确认想要修改此用户手机号吗？',function(r){    
		    if (r){
				$.getJSON(BASE_PATH+"/sjdx/sjhdj/updatePhoneDo_sjdxhdj.do?userCode="+code+"&newPhone="+newPhone+"&oldPhone="+oldPhone,
						function (result){
							showMessage(result.msg);
							doSearchPhone_mian();
							$('#newPhone_main').textbox('reset'); //新手机号
					});
		    }
		}); 
	}else{
		showMessage('新手机号不允许为空!');
	}
};


//删除密码
function deletePsw(){
	var date = new Date();
	var month = ("0" + (date.getMonth() + 1)).slice(-2);
    var strDate = ("0" + date.getDate()).slice(-2);
    var scmm = date.getFullYear()+month+strDate;
    return scmm;
};
//判断电话号码输入格式
function judgePhone(phone){
	if(phone && /^1[3|4|5|8]\d{9}$/.test(phone)){
		return 0;
	} else {
		return 1;
	}
};
// 外勤申请（审批人收到）
var OUT_ATTENDANCE_APPLY = 10001;
// 外勤审批结果（申请人收到）
var OUT_ATTENDANCE_APPROVAL = 10002;
// 休假申请（审批人收到）
var VACATION_APPLY = 10003;
// 休假审批结果（申请人收到）
var VACATION_APPROVAL = 10004;
// 加班申请（审批人收到）
var OVERTIME_APPLY = 10005;
// 加班审批结果（申请人收到）
var OVERTIME_APPROVAL = 10006;
// 外出申请（审批人收到）
var GO_OUT_APPLY = 10007;
// 外出审批结果（申请人收到）
var GO_OUT_APPROVAL = 10008;
// 出差申请（审批人收到）
var BUSINESS_TRIP_APPLY = 10009;
// 出差审批结果（申请人收到）
var BUSINESS_TRIP_APPROVAL = 10010;
// 考勤补卡申请（审批人收到）
var ATTENDANCE_APPEAL_APPLY = 10011;
// 考勤补卡审批结果（申请人收到）
var ATTENDANCE_APPEAL_APPROVAL = 10012;
// 公告制度
var ANNOUNCEMENT = 10013;
// 工资信息
var SALARY = 10014;
// 问卷调查
var QUESTIONNAIRE = 10015;
// 新的会议通知
var NEW_MEETING = 10016;
// 工作汇报（管理员收到）
var WORK_REPORT = 10017;
// 其他通用的消息通知
var MORMAL_MESSAGE = 10018;
//环境检测仪信息
var CONDITION_MESSAGE = 20000;

//门铃
var CAllDOORBELL = 20001;

var websocketcode = 0;
/***
 * 连接WebSocket
 */
function ConnectWebSocket(){
	debugger;
	if(WEBSOCKET_PATH==null||WEBSOCKET_PATH==undefined){
		var WEBSOCKET_PATH = "www.zhongbenshuo.com:9010";
	}
	if ('WebSocket' in window) {
        websocket = new WebSocket(encodeURI("wss://"+WEBSOCKET_PATH+"/environment"));
    } else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket(encodeURI("wss://"+WEBSOCKET_PATH+"/environment"));
    } else {
        websocket = new SockJS(encodeURI("http://"+WEBSOCKET_PATH+"/sockjs/environment"));
    }
	
    if(websocket!=null){
    	websocket.onopen = function (evnt) {
    		if(websocketcode){
    			console.log("colseSetTimeout");
    			clearInterval(websocketcode);
    		}
    		$.messager.show({title:'提示',msg:'通讯建立成功！'});   
        };
        websocket.onmessage = function (evnt) {
        	debugger;
        	console.log(evnt.data);
        	var data = "";
        	try {
        		 data = JSON.parse(evnt.data);
				} catch (e) {
			}
        	
        	switch (data.key) {
			case OUT_ATTENDANCE_APPLY:	//外勤申请（审批人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您有新的外勤打卡审批需要处理，请尽快审批!"});  
					searchNews();
				}
				break;
			case OUT_ATTENDANCE_APPROVAL:	// 外勤审批结果（申请人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您的外勤打卡申请有新的审批结果，请查阅!"});  
					searchNews();
				}
				break;
			case VACATION_APPLY:	// 休假申请（审批人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您有新的假期审批需要处理，请尽快审批!"});  
					searchNews();
				}
				break;
			case VACATION_APPROVAL:	// 休假审批结果（申请人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您的假期申请有新的审批结果，请查阅!"});  
					searchNews();
				}
				break;
			case OVERTIME_APPLY:// 加班申请（审批人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您有新的加班审批需要处理，请尽快审批!"});  
					searchNews();
				}
				break;
			case OVERTIME_APPROVAL:// 加班审批结果（申请人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您的加班申请有新的审批结果，请查阅!"});  
					searchNews();
				}
				break;
			case GO_OUT_APPLY:// 外出申请（审批人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您有新的外出审批需要处理，请尽快审批!"});  
					searchNews();
				}
				break;
			case GO_OUT_APPROVAL:// 外出审批结果（申请人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您的外出申请有新的审批结果，请查阅!"});  
					searchNews();
				}
				break;
			case BUSINESS_TRIP_APPLY:// 出差申请（审批人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您有新的出差审批需要处理，请尽快审批!"});  
					searchNews();
				}
				break;
			case BUSINESS_TRIP_APPROVAL:// 出差审批结果（申请人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您的出差申请有新的审批结果，请查阅!"});  
					searchNews();
				}
				break;
			case ATTENDANCE_APPEAL_APPLY:// 考勤补卡申请（审批人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您有新的考勤补卡审批需要处理，请尽快审批!"});  
					searchNews();
				}
				break;
			case ATTENDANCE_APPEAL_APPROVAL:// 考勤补卡审批结果（申请人收到）
				var user_id = data.user_id;
				if(loginUser.loginName==user_id){
					$.messager.show({title:'提示',msg:loginUser.loginName+"用户，您的考勤补卡申请有新的审批结果，请查阅!"});  
					searchNews();
				}
				break;
			case ANNOUNCEMENT:// 公告制度
				break;
			case SALARY:// 工资信息
				break;
			case QUESTIONNAIRE:// 问卷调查
				break;
			case NEW_MEETING:// 新的会议通知
				break;
			case WORK_REPORT:// 工作汇报（管理员收到）
				break;
			case MORMAL_MESSAGE:// 其他通用的消息通知
				break;
				
			case CONDITION_MESSAGE://环境检测仪消息推送
				try {
					echartsUpload(data);
				} catch (e) {
					
				}
				break;
			case CAllDOORBELL://门铃
				debugger;
				console.log(data);
				try {
					if(document.location.pathname==="/ZBSAttendance/echartsCondition.jsp"){
						$('#calldoorbell_dialog').dialog("open");
						document.getElementById('img_calldoorbell').src = document.location.origin+"/"+data.message;
						var audio = document.getElementById("audio_calldoorbell");
						audio.play();//开始播放
						setTimeout("closeCalldoorbell_dialog()","5000");  //5000毫秒后执行test()函数，只执行一次。
					}
				} catch (e) {
						
				}
				
				
				break;
			default:
				break;
}
        };
        websocket.onerror = function (evnt) {
            console.log('websocketOnerror');
           // reconnect();
        };
        websocket.onclose = function (evnt) {
        	debugger;
        	console.log('websocketOnclose');
            reconnect();
        };
	}
}
function closeCalldoorbell_dialog(){
	var audio = document.getElementById("audio_calldoorbell");
	audio.pause();//开始播放
	document.getElementById('img_calldoorbell').src = "";
	$('#calldoorbell_dialog').dialog("close");
}
var lockReconnect = false;//避免重复链接
function reconnect() {
//	try {
//		if(websocket!=null){
//			websocket.close();
//			websocket = null;
//		}
//	} catch (e) {
//	
//	}
	console.log(lockReconnect);
    if (lockReconnect) return;
    lockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    setTimeout(function() {
    	console.log("ConnectWebSocket");
    	ConnectWebSocket();
        lockReconnect = false;
    }, 2000);
}

/***
 * 查询最新消息
 */
function searchNews(){
	debugger;
	var user_id = loginUser.loginName;
	$.post(BASE_PATH + "/ApplyRecordController/searchNews.do", {
		userId : loginUser.loginName
	}, function(data) {
		debugger;
		try {
			var temp = JSON.parse(data);
			console.log(temp);
			outAttendanceApproval = temp.outAttendanceApproval;//外勤打卡审批
			outAttendanceApply = temp.outAttendanceApply;//外勤申请记录
			appealAttendanceApproval = temp.appealAttendanceApproval;//补卡审批
			appealAttendanceApply = temp.appealAttendanceApply;//补卡申请
			
			overTimeApproval = temp.overTimeApproval;//加班审批
			overTimeApply = temp.overTimeApply;//加班申请
			
			vacationApproval = temp.vacationApproval;//休假审批
			vacationApply = temp.vacationApply;//休假申请
			outGoingApproval = temp.outGoingApproval;//外出审批
			outGoingApply = temp.outGoingApply;//外出申请
			businessTraveIApproval = temp.businessTraveIApproval;//出差审批
			businessTraveIApply = temp.businessTraveIApply;//出差申请
			var all = outAttendanceApproval+outAttendanceApply+appealAttendanceApproval+appealAttendanceApply+overTimeApproval+overTimeApply+
			vacationApproval+vacationApply+outGoingApproval+outGoingApply+businessTraveIApproval+businessTraveIApply;//总和
			
			//出差审批
			if(businessTraveIApproval==0){
				$('#businessTravelLinkbutton').linkbutton({
				    text: '出差审批',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#businessTravelLinkbutton').linkbutton({
				    text: '出差审批'+businessTraveIApproval+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			//出差申请记录
			if(businessTraveIApply==0){
				$('#businessTravelApplyLinkbutton').linkbutton({
				    text: '出差申请记录',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#businessTravelApplyLinkbutton').linkbutton({
				    text: '出差申请记录'+businessTraveIApply+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			
			//外出审批
			if(outGoingApproval==0){
				$('#outGoingLinkbutton').linkbutton({
				    text: '外出审批',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#outGoingLinkbutton').linkbutton({
				    text: '外出审批'+outGoingApproval+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			//外出申请记录
			if(outGoingApply==0){
				$('#outGoingApplyLinkbutton').linkbutton({
				    text: '外出申请记录',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#outGoingApplyLinkbutton').linkbutton({
				    text: '外出申请记录'+outGoingApply+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			
			//休假审批
			if(vacationApproval==0){
				$('#vacationLinkbutton').linkbutton({
				    text: '假期审批',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#vacationLinkbutton').linkbutton({
				    text: '假期审批'+vacationApproval+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			//休假申请记录
			if(vacationApply==0){
				$('#vacationApplyLinkbutton').linkbutton({
				    text: '假期申请记录',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#vacationApplyLinkbutton').linkbutton({
				    text: '假期申请记录'+vacationApply+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			
			
			//加班审批
			if(overTimeApproval==0){
				$('#overTimeLinkbutton').linkbutton({
				    text: '加班审批',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#overTimeLinkbutton').linkbutton({
				    text: '加班审批'+overTimeApproval+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			//加班申请记录
			if(overTimeApply==0){
				$('#overTimeApplyLinkbutton').linkbutton({
				    text: '加班申请记录',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#overTimeApplyLinkbutton').linkbutton({
				    text: '加班申请记录'+overTimeApply+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			
			//外勤打卡审批
			if(outAttendanceApproval==0){
				$('#outAttendanceLinkbutton').linkbutton({
				    text: '外勤打卡审批',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#outAttendanceLinkbutton').linkbutton({
				    text: '外勤打卡审批'+outAttendanceApproval+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			//外勤申请记录
			if(outAttendanceApply==0){
				$('#applyRecordLinkbutton').linkbutton({
				    text: '外勤打卡申请记录',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#applyRecordLinkbutton').linkbutton({
				    text: '外勤打卡申请记录'+outAttendanceApply+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			
			//补卡审批
			if(appealAttendanceApproval==0){
				$('#appealAttendanceLinkbutton').linkbutton({
				    text: '补卡审批',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#appealAttendanceLinkbutton').linkbutton({
				    text: '补卡审批'+appealAttendanceApproval+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			
			//补卡申请记录
			if(appealAttendanceApply==0){
				$('#appealRecordLinkbutton').linkbutton({
				    text: '补卡申请记录',
				    iconCls:'icon-newInfoNo'
				});
			}else{
				$('#appealRecordLinkbutton').linkbutton({
				    text: '补卡申请记录'+appealAttendanceApply+"条",
				    iconCls:'icon-newInfoYes'
				});
			}
			if(all==0){
				$('#newInfoMain').menubutton({
					iconCls: 'icon-newInfoNo',
					text : "最新消息"
				})
			}else{
				$('#newInfoMain').menubutton({
					iconCls: 'icon-newInfoYes',
					text : "最新消息"+all+"条"
				})
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/**
 * 点击最新消息
 */
function newInfoMainClick(){
	debugger;
	$('#mainTab').tabs('select', "主页");
}

//{
//	"BUTTON":[{
//		"TYPE":"MINIPROGRAM",
//        "NAME":"考勤打卡",
//        "URL":"HTTPS://WWW.ZHONGBENSHUO.COM/DIST",
//        "APPID":"WX76814D39C74BB897",
//        "PAGEPATH":"PAGES/INDEX/INDEX"
//	},{
//		"TYPE":"VIEW",
//        "NAME":"年会入口",
//        "URL":"HTTPS://WWW.LIYULIANG.NET/MOBILE/QIANDAO.PHP?VCODE=5DB9516B05514"
//	}
//	]
//	}