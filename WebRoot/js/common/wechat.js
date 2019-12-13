	
	var appid = "wx2f6a976d263013fb";
	var scope = "snsapi_userinfo";//snsapi_base 为静默授权；snsapi_userinfo为用户同意授权
	var redirect_uri = "http://www.mydma.cn/bind/redirect.do";
	/**
	 * 请求用户同意授权，获取code
	 */
	function getCodeThenRedirect(){
		/*在redirect_uri后跟上要带的绑定参数, suplierId,meterId,cellphone
		注：这里的redirect_uri必须在配置域名下运行且必须是80端口,否则报错redirect_uri参数错误*/
		var supplierId = $("#supplierId").val();
		var meterId = $("#meterId").val();
		var cellphone = $("#cellphone").val();
		redirect_uri += "?supplierId=" + supplierId + "&meterId=" + meterId + "&cellphone=" + cellphone;
		redirect_uri = UrlEncode(redirect_uri);
		var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid +
		"&redirect_uri=" + redirect_uri + "&response_type=code" + "&scope=" + scope +
		"&state=mt#wechat_redirect";
//		document.write(url);
		window.location.href = url;
	}
	
	
	/**
	 * 字符串转ASSII
	 * @param strstr
	 * @returns
	 */
	function str2asc(strstr) {
		return ("0" + strstr.charCodeAt(0).toString(16)).slice(-2);
	}
	
	/**
	 * ASCII转字符串
	 * @param ascasc
	 * @returns
	 */
	function asc2str(ascasc) {
		return String.fromCharCode(ascasc);
	} 

	/**
	 * 正则转换 url 序列
	 * 
	 * @param str
	 * @returns {String}
	 */
	function UrlEncode(str){
		var ret="";
		var strSpecial="!\"#$%&'()*+,/:;<=>?[]^`{|}~%";
		var tt= "";
		for(var i=0;i<str.length;i++){
			var chr = str.charAt(i);
			var c = str2asc(chr);
			tt += chr+":"+c+"n";
			if(parseInt("0x"+c) > 0x7f){
				ret+="%"+c.slice(0,2)+"%"+c.slice(-2);
			}else{
				if(chr==" ")
					ret+="+";
				else if(strSpecial.indexOf(chr)!=-1)
					ret+="%"+c.toString(16);
				else
					ret+=chr;
			}
		}
		return ret;
	}


	/**
	 * @param str
	 * @returns {String}
	 */
	function UrlDecode(str){
		var ret="";
		for(var i=0;i<str.length;i++){
			var chr = str.charAt(i);
			if(chr == "+"){
				ret+=" ";
			}else if(chr=="%"){
				var asc = str.substring(i+1,i+3);
				if(parseInt("0x"+asc)>0x7f){
					ret+=asc2str(parseInt("0x"+asc+str.substring(i+4,i+6)));
					i+=5;
				}else{
					ret+=asc2str(parseInt("0x"+asc));
					i+=2;
				}
			}else{
				ret+= chr;
			}
		}
		return ret;
	}


	/** 
	 * 获取当前路径，此方法不需要<%=bathpath%>代码来获取
	 * @returns
	 */
	function getContextPath () {
	    var currentPath = window.document.location.href;
	    var pathName = window.document.location.pathname;
	    var pos = currentPath.indexOf(pathName);
	    var localhostPath = currentPath.substring(0, pos);
	    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	    return (localhostPath + projectName);
	}