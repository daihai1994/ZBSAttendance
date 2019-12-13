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
	<!-- <style>
		#index_all .datagrid-btable .datagrid-row  .datagrid-cell {
	 	font-size: 28px;
	 	line-height: 50px;
		   height: 50px;
		   padding: 0 0px;
		   font-weight: normal;
		}
		
		#index_all .datagrid-htable,
		#index_all .datagrid-btable,
		#index_all .datagrid-ftable {
		  color: #000000;
		  border-collapse: separate;
		   background: #cfe4aa;
		}
		#index_all .datagrid-row-expander{
			display: none;
		}
		#index_all .datagrid-header td,
		#index_all .datagrid-body td,
		#index_all .datagrid-footer td {
		  border-color: #000000;
		  border-style: solid;
		}
		#index_all .datagrid-view1 .datagrid-body-inner {
		  padding-bottom: 20px;
		  display: none;
		}
		#index_all .datagrid-header-row,
		#index_all .datagrid-row {
		  height: 50px;
		}
		#index_all
		.propertygrid .datagrid-group {
		  overflow: hidden;
		  border-width: 0 0 1px 0;
		  border-style: solid;
		  height: 50px;
		}
		#index_all
		.propertygrid .datagrid-group span {
		   font-size:35px;
		   line-height: 50px;
		}
		#index_all .propertygrid .datagrid-group,
		#index_all .propertygrid .datagrid-view1 .datagrid-body,
		#index_all .propertygrid .datagrid-view1 .datagrid-row-over,
		#index_all .propertygrid .datagrid-view1 .datagrid-row-selected {
		  background: #B5B5B5;
		}
		#index_all .datalist .datagrid-group-title {
		  padding: 0 0px;
		}
	</style> -->
</head>
<body style="margin-top: 20px;margin-bottom: 20px;margin-left: 0px;margin-right: 0px;background: #B5B5B5">
</body>
<div id="index_all" style="width: 100%;height: 100%">
	
</div>   
<script type="text/javascript">
var BASE_PATH = '<%=path%>';
var pg_columns = [[
                 		{field:"name",width:20,align:'center'},
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

function rowStylerIndex(index,row){
	return 'background-color:'+row.color+';font-weight:bold;';
}              	
 var tempAll ;                	
        function loadUserInfoAll(){
        timeStatus = false;
         windowWidth = window.innerWidth;
        debugger;
	        $.post(BASE_PATH + "/user/serachUserInfoAll.do", {}, function(data) {
			try {
			timeStatus = true;
			debugger;
			
				var temp = [];
					for(var i = 0;i<data.rows.length;i++){
						var group = "【"+data.rows[i].department+"】";
						var user = data.rows[i].users;
						for(var xx = 0;xx<user.length;xx++){
							var obj = new Object();
							obj.name = user[xx].user_name;
							obj.remarks = user[xx].remarks;
							obj.status = user[xx].status;
							obj.color = user[xx].color;
							obj.group = group;
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
					 	if(temp[i].group!==group){
					 		group = temp[i].group;
					 		forHerght+=fontWeight;
					 		forHerght+=(fontWeight+2);
					 		if(forHerght>=windowHeight){
						 		size+=1;
						 		list.push(temp.splice(0, i));
						 		i = -1;
						 		forHerght = 0;
						 		group = "";
						 		continue;
						 	}
					 	}else{
						 	forHerght+=(fontWeight+2);
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
					$("#index_all").empty();
					windowWidth = windowWidth-(size+1)*20;
					var width = windowWidth/size;
					for(var i = 0;i<size;i++){
						src+='<div style="width:'+width+'px;heigth:100%;float: left;margin-left: 20px">'+
								'<table id="pg'+i+'" class="easyui-propertygrid" style="width:100%;heigth:100%;" data-options="showGroup:true,'+
									'showHeader:false,'+
									'scrollbarSize:0,'+
									'columns:pg_columns,'+
									'rowStyler:rowStylerIndex'+
								'">'+
								'</table>'+
							'</div>'
					}
					$("#index_all").append(src);
					$.parser.parse('#index_all');
					
						if(temp.length>0){
							list.push(temp);
						}
 					for(var i = 0;i<list.length;i++){
					 	var x = list[i];
					 		$("#pg"+i).propertygrid("loadData",x);
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
        var lineSize = 22;//显示多少行
   $(function() {
  			  windowHeight = window.innerHeight-40-(lineSize*2);
			 windowWidth = window.innerWidth;
			 fontWeight = windowHeight/lineSize;
			 fontSize = fontWeight/1.4;
			 var datagridRow = fontWeight-2;
			 console.log("windowHeight:"+windowHeight+";windowWidth:"+windowWidth+";fontWeight:"+fontWeight+";fontSize:"+fontSize);
     var nod = document.createElement("style"),   
	 str =  "#index_all .datagrid-htable,#index_all .datagrid-btable,#index_all .datagrid-ftable {color: #000000;border-collapse: separate;background: #cfe4aa;}"+
	 		"#index_all .datagrid-btable .datagrid-row  .datagrid-cell {font-size: "+fontSize+"px;line-height: "+fontWeight+"px;height: "+datagridRow+"px; padding: 0 0px;font-weight: normal;}"+
// 		   "#index_all .datagrid-row-expander{display: none;}"+
		   "#index_all .datagrid-header td,#index_all .datagrid-body td,#index_all .datagrid-footer td {border-color: #B5B5B5; border-style: solid}"+
		   "#index_all .datagrid-view1 .datagrid-body-inner {padding-bottom: 20px;display: none;}"+
		   "#index_all .datagrid-header-row,#index_all .datagrid-row {height: "+datagridRow+"px;}"+
		   "#index_all .propertygrid .datagrid-group {overflow: hidden;border-width: 0 0 1px 0;border-style: solid;height: "+fontWeight+"px;}"+
		   "#index_all .propertygrid .datagrid-group span {font-size:"+fontSize+"px;line-height: "+fontWeight+"px;}"+
		   "#index_all .propertygrid .datagrid-group,#index_all .propertygrid .datagrid-view1 .datagrid-body,#index_all .propertygrid .datagrid-view1 .datagrid-row-over,#index_all .propertygrid .datagrid-view1 .datagrid-row-selected {background: #B5B5B5;}"+
		   "#index_all .datagrid-view2 .datagrid-btable{height: "+datagridRow+"px;}"+
		   "#index_all .datagrid-row {height: "+fontWeight+"px;}"+
		   "#index_all .panel-body {border-color: #B5B5B5;}"+
		   "#index_all .datagrid-header {border-color: #B5B5B5;}";
// 		   "#index_all .datalist .datagrid-group-title {padding: 0 0px;}"+
// 		    "#index_all .datalist .datagrid-group-title {padding: 0 0px;}";  
		 nod.type="text/css";  
		 if(nod.styleSheet){         //ie下  
	　　	 	nod.styleSheet.cssText = str;  
		 } else {  
		　　 nod.innerHTML = str;       //或者写成 nod.appendChild(document.createTextNode(str))  
		 }  
	document.getElementsByTagName("head")[0].appendChild(nod);
	windowHeight = windowHeight+(lineSize*2);
   		loadUserInfoAll();
   		if(intervalId==null){
	   		intervalId = setInterval(function () {
	   		debugger;
	   			var myDate = new Date();
	   			console.log(myDate.toLocaleTimeString());
	   			console.log("进入定时任务");
	   			if(timeStatus = true){
	   			 loadUserInfoAll();
	   			}
			}, 10000);
   		}
   		
   		
   })
</script>
</html>
