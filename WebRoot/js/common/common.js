/**
 * common.js
 * 作用 ： 不同模块多页面共用的通用或共用的业务事件，可视作简单业务组件
 * */


// 全局变量
var _module = $("#module").val();


/************************************************************************************
***********************      	共用业务方法              *******************************
*************************************************************************************/

/**
 * 设定模块号，通过tab页的选中条件作为子模块
 * 大模块 A B C D E
 * 子模块 A-1;B-1,B-2,B-3;C-1;D-1;E-1
 * @param no 大模块号
 */
function setmoduleNo(no){
	$('#tt').tabs({
	    border:false,
	    onSelect:function(title,index){
			$("#module").attr("value", no + "-" + (index+1));
			_module = $("#module").val();
	    }
	});
}


/**
 * 每个模块的双击动作，有不同的定义
 * @param node
 */
function doubleClickEvent(node){
	//_module = $("#module").val();
	$(".currentlocation").html(currentlocation_str);
	if(_module === "B-2"){
		if(node.attributes.is_bottom === 1){
			$.ajax({
				type:"POST",
				url:BASE_PATH+"/deviceAdmin/getCenterPoint.do",
				data:{hieId : node.id},
				async:false,	
				success:function(data){
					darw_CenterPoint(data);
				},
				error:function(msg) {
					alert(msg);
		        }
			});
		}
	}
}

/**
 * 树节点右击事件 每个模块拥有不一样的定义
 * @param e
 * @param node
 */
function rightClickonTree(e, node) {
//	alert(_module);
	e.preventDefault();
	if(_module === "A-1"){
		$("#tree").tree('select', node.target);
		$('#queryMenu').menu('show', {
			left : e.pageX,
			top : e.pageY
		});
	}
	
	if(_module === "B-1"){
		$("#tree").tree('select', node.target);
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY
		});
		
	}
	
	if(_module === "D-1"){		//用户管理模块
		$("#tree").tree('select', node.target);
		$('#muser').menu('show', {
			left : e.pageX,
			top : e.pageY
		});
		
	}
	
}

/**
 * 定义匿名函数 serializeJson, from表单转为Json对象
 */
(function($){
    $.fn.serializeJson=function(){
        var serializeObj={};
        var array=this.serializeArray();
        var str=this.serialize();
        $(array).each(function(){
            if(serializeObj[this.name]){
                if($.isArray(serializeObj[this.name])){
                    serializeObj[this.name].push(this.value);
                }else{
                    serializeObj[this.name]=[serializeObj[this.name],this.value];
                }
            }else{
                serializeObj[this.name]=this.value;    
            }
        });
        return serializeObj;
    };
})(jQuery);
    





