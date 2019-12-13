<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>苏中中本硕机电办公软件</title>
    <link rel="SHORTCUT ICON"
	href="${pageContext.request.contextPath }/img/favicon.ico" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/easyui/themes/bootstrap/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/easyui/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/common/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/common/jquery.jqprint-0.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/detailview.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/datagrid-dnd.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/main/index.js"></script>
</head>
<body style="margin-top: 20px;margin-bottom: 20px;margin-left: 0px;margin-right: 0px;background: #C5C5C5">
</body>
<div id="index_all_v1" style="width: 100%;height: 100%">
</div>   
<script type="text/javascript">
var BASE_PATH = '<%=path%>';
var pg_columns = [[
                 		{field:"name",width:20,align:'center',formatter:columnsCenter},
                 		{field:"remarks",width:60,align:'center',formatter:marqueeDiv},//
                 		{field:"status",width:20,align:'center'}
                 	]];
var jmz = {};
jmz.GetLength = function(str) {
	//先把中文替换成两个字节的英文，在计算长度
  return str.replace(/[\u0391-\uFFE5]/g,"aa").length;   
};  
function marqueeDiv(value,row,index){
	if(value!=null){
		var width = this.width;
		var valueWidth = jmz.GetLength(value)*(fontSize/2);
		if(valueWidth>width){
			return '<marquee scrollamount="5" loop = "-1" width = "100%" height = "97%" style="text-align: center;">'+value+'</marquee>';
		}else{
			return value;
		}
	}else{
		return value;
	}
}   
function columnsCenter(value,row,index){
	if(row.color==null){
		return "<div style='text-align:left;margin-top: 15px;margin-bottom: 5px;font-size:"+fontSize*1.3+"px'>"+value+"</div>"
	}else{
		return value;
	}
}
function rowStylerIndex_v1(index,row){
	if(row.color==null){
		return 'color:#000000;font-weight:bold;background:#C5C5C5;';
	}else{
		return 'color:'+row.color+';font-weight:normal';
	}
	
}    

function onLoadSuccess_v1(data){
	debugger;
	var merges = [];
	for(var i = 0;i<data.rows.length;i++){
		if(data.rows[i].status==null){
			var obj = new Object();
			obj.index = i;
			obj.colspan = 3;
			merges.push(obj);
		}
	}
	 for(var i=0; i<merges.length; i++){
          $(this).propertygrid('mergeCells',{
              index: merges[i].index,
              field: 'name',
              colspan: merges[i].colspan
          });
      }
}          	
 var tempAll ;                	
        function loadUserInfoAll_v1(){
        timeStatus = false;
        windowWidth = window.innerWidth;
        debugger;
	        $.post(BASE_PATH + "/user/serachUserInfoAll.do", {}, function(data) {
			try {
			timeStatus = true;
			debugger;
			
				var temp = [];
					for(var i = 0;i<data.rows.length;i++){
						var group = data.rows[i].department;
						var user = data.rows[i].users;
						var obj = new Object();
							obj.name = "【"+data.rows[i].department+"】";
							temp.push(obj);
						for(var xx = 0;xx<user.length;xx++){
							var obj = new Object();
							obj.name = user[xx].user_name;
							obj.remarks = user[xx].remarks;
							obj.status = user[xx].status;
							obj.color = user[xx].color;
							//obj.groups = group;
							temp.push(obj);
						}
					}
					console.log("总的长度："+temp.length);
					tempAll = 	temp;
					var size = 0;
					 var list = [];
					 var group = "";
					 var forHerght = 0;
					  if(temp.length>0){
					 	size++;
					 }
					
					 for(var i = 0;i<temp.length;i++){
						 if(temp[i].color==null){
						 	forHerght+=(fontWeight+28);
							 	if(forHerght>=windowHeight){
							 		size+=1;
							 		list.push(temp.splice(0, i));
								 	i = -1;
							 		forHerght = 0;
							 		group = "";
							 		continue;
							 	}
						 }else{
							 forHerght+=(fontWeight+8);
							 	if(forHerght>=windowHeight){
							 		size+=1;
							 		list.push(temp.splice(0, i));
								 	i = -1;
							 		forHerght = 0;
							 		group = "";
							 		continue;
							 	}
						 }
					 }
					var src = "";
					$("#index_all_v1").empty();
					windowWidth = windowWidth-(size+1)*20;
					var width = windowWidth/size;
					debugger;
					for(var i = 0;i<size;i++){
						src+='<div style="width:'+width+'px;heigth:100%;float: left;margin-left: 20px">'+
								'<table id="pgs'+i+'" class="easyui-propertygrid" style="width:100%;heigth:100%;" data-options="showGroup:true,'+
									'showHeader:false,'+
									'showGroup:false,'+
									'scrollbarSize:0,'+
									'columns:pg_columns,'+
									'rowStyler:rowStylerIndex_v1,'+
									'onLoadSuccess:onLoadSuccess_v1'+
								'">'+
								'</table>'+
							'</div>'
					}
					$("#index_all_v1").append(src);
					$.parser.parse('#index_all_v1');
					
						if(temp.length>0){
							list.push(temp);
						}
 					for(var i = 0;i<list.length;i++){
					 	var x = list[i];
					 		$("#pgs"+i).propertygrid("loadData",x);
					 }       
				} catch (e) {
					console.log(e);
				}
			
			});
        }
        var intervalId;//定时任务
        var timeStatus = true;//本次定时任务是否结束
        var windowHeight;//浏览器高
        var windowWidth;//浏览器宽
        var fontWeight;//显示多宽
        var fontSize;//字体显示的大小
        var lineSize = 23;//显示多少行
   $(function() {
  			  windowHeight = window.innerHeight-40-(lineSize*8);
			 windowWidth = window.innerWidth;
			 fontWeight = windowHeight/lineSize;
			 fontSize = fontWeight/1.4;
			 var datagridRow = fontWeight;
			 console.log("windowHeight:"+windowHeight+";windowWidth:"+windowWidth+";fontWeight:"+fontWeight+";fontSize:"+fontSize);
     var nod = document.createElement("style"),   
	 str =  "#index_all_v1 .datagrid-htable,#index_all_v1 .datagrid-btable,#index_all_v1 .datagrid-ftable {color: #000000;border-collapse: separate;background: #BFEFFF;}"+
	 		"#index_all_v1 .datagrid-btable .datagrid-row  .datagrid-cell {font-size: "+fontSize+"px;line-height: "+fontWeight+"px;height: "+datagridRow+"px; padding: 0 0px;}"+
// 		   "#index_all .datagrid-row-expander{display: none;}"+
		   "#index_all_v1 .datagrid-header td,#index_all_v1 .datagrid-body td,#index_all_v1 .datagrid-footer td {border-color: #C5C5C5; border-style: double ;border-width:3px;}"+
		   "#index_all_v1 .datagrid-view1 .datagrid-body-inner {padding-bottom: 20px;display: none;}"+
		   "#index_all_v1 .datagrid-header-row,#index_all_v1 .datagrid-row {height: "+datagridRow+"px;}"+
		   "#index_all_v1 .propertygrid .datagrid-group {overflow: hidden;border-width: 0 0 1px 0;border-style: solid;height: "+fontWeight+"px;}"+
		   "#index_all_v1 .propertygrid .datagrid-group span {font-size:"+fontSize+"px;line-height: "+fontWeight+"px;}"+
		   "#index_all_v1 .propertygrid .datagrid-group,#index_all_v1 .propertygrid .datagrid-view1 .datagrid-body,#index_all_v1 .propertygrid .datagrid-view1 .datagrid-row-over,#index_all_v1 .propertygrid .datagrid-view1 .datagrid-row-selected {background: #C5C5C5;}"+
		   "#index_all_v1 .datagrid-view2 .datagrid-btable{height: "+datagridRow+"px;}"+
		   "#index_all_v1 .datagrid-row {height: "+fontWeight+"px;}"+
		   "#index_all_v1 .panel-body {border-color: #C5C5C5;}"+
		   "#index_all_v1 .datagrid-header {border-color: #C5C5C5;}"+
		   "#index_all_v1 .propertygrid .datagrid-view1 .datagrid-body{background: #C5C5C5}";
// 		   "#index_all_v1 .datalist .datagrid-group-title {padding: 0 0px;}"+
// 		    "#index_all_v1 .datalist .datagrid-group-title {padding: 0 0px;}";  
		 nod.type="text/css";  
		 if(nod.styleSheet){         //ie下  
	　　	 	nod.styleSheet.cssText = str;  
		 } else {  
		　　 nod.innerHTML = str;       //或者写成 nod.appendChild(document.createTextNode(str))  
		 }  
	document.getElementsByTagName("head")[0].appendChild(nod);
	windowHeight = windowHeight+(lineSize*8);
   		loadUserInfoAll_v1();
   		if(intervalId==null){
	   		intervalId = setInterval(function () {
	   		debugger;
	   			var myDate = new Date();
	   			console.log(myDate.toLocaleTimeString());
	   			console.log("进入定时任务");
	   			if(timeStatus = true){
	   			 loadUserInfoAll_v1();
	   			}
			}, 10000);
   		}
   		
   		
   })
</script>
</html>
