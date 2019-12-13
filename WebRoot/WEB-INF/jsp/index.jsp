<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<title>登录页面</title>
<link rel="SHORTCUT ICON"
	href="${pageContext.request.contextPath }/img/favicon.ico" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/login.css">

</head>

<body>
	<!--背景颜色-->
	<div class="g-bg-color"></div>
		<div class="g-wrap">
			<div class="g-wrap-container">
				<div class="m-top hidden">
					<div class="left"></div>
					<p class="left">中本硕机电</p>
				</div>
	
				<!--中间登录框start-->
				<div class="m-login">
					<div class="u-tit inline-block">
						<div style='color:#1B6C97;font-family:"微软雅黑";float: left'>中本硕机电办公系统</div>
						<div style="width:300px;height:100%;float:right;margin-left: 100px">
							<form action="${pageContext.request.contextPath }/LoginController/login.do"
								method="post">
								<div class="u-form-1 hidden">
									<em class="u-form-img left relative"> <strong></strong>
									</em> <i class="u-form-input left"> <input name="username"
										id="username" type="text" placeholder="用户名/账号ID">
									</i>
								</div>
								<div class="u-form-2">
									<em class="u-form-img u-form-img-2 left relative "> <strong></strong>
									</em> <i class="u-form-input left"> <input name="password"
										id="password" type="password" placeholder="密码">
										<input type='hidden' name = 'aes_pwd' id='aes_pwd' value=''/>
									</i>
								</div>
								<input type="text" id="ctl00_txtcode" style="height: 37px" autocomplete="off" placeholder="验证码:" />
								 <input type="button" id="login_code" onclick="createCode()" />
								 <p id="yanzhengma" style="color:red;font-size: 26px;display: none;">验证码错误</p>
								<input class="u-btn" type="submit" value="登入"><br> <span
									id="error" style='color:red'>${errMsg}</span>
							</form>
						</div>
				</div>
			</div>
			<p class="textalign g-bottom-txt">Copyright © 2019 ZBS Co., LTD.
JSS		All rights reserved.</p>
		</div>
	</div>
	<!--mainEnd-->
</body>
</html>
<script
	src="${pageContext.request.contextPath }/js/common/jquery.min.js"></script>
<script
	src="${pageContext.request.contextPath }/js/CryptoJS/aes_1.js"></script>
<script
	src="${pageContext.request.contextPath }/js/CryptoJS/pad-zeropadding-min.js"></script>
	
	
<script src="${pageContext.request.contextPath }/js/login/login.js"></script>
<script>
	 //产生验证码  
            window.onload = function() {
                createCode();
            }
            var code; //在全局定义验证码  
            function createCode() {
                code = "";
                var codeLength = 4; //验证码的长度  
                var checkCode = document.getElementById("login_code");
                var random = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'); //随机数  
                for(var i = 0; i < codeLength; i++) { //循环操作  
                    var index = Math.floor(Math.random() * 36); //取得随机数的索引（0~35）  
                    code += random[index]; //根据索引取得随机数加到code上  
                }
                checkCode.value = code; //把code值赋给验证码  
            }
            //校验验证码  
            function validate() {
                var inputCode = document.getElementById("ctl00_txtcode").value.toUpperCase(); //获取输入框内验证码并转化为大写        
                var reg = /^[0-9a-zA-Z]+$/;
                if(inputCode.length <= 0) { //若输入的验证码长度为0  
                	 $("#yanzhengma").show();
               		 $("#yanzhengma")[0].innerText="验证码不能为空";
                    return false;
                }else if(!reg.test(inputCode)){
                	 $("#yanzhengma").show();
               		 $("#yanzhengma")[0].innerText="验证码格式错误";
                    createCode(); //刷新验证码  
                    document.getElementById("ctl00_txtcode").value = ""; //清空文本框  
                    return false;
                } else if(inputCode != code) { //若输入的验证码与产生的验证码不一致时  
               		 $("#yanzhengma").show();
               		 $("#yanzhengma")[0].innerText="验证码错误";
                    createCode(); //刷新验证码  
                    document.getElementById("ctl00_txtcode").value = ""; //清空文本框  
                    return false;
                } else { //输入正确时  
                    return true;
                }
            }
             $("form").submit(function () {
		        debugger;
		        if(! validate()){
		        	return false;
		        }
				var encryptedData = encrypt($('#password').val());
				console.log(encryptedData);
		        $('#aes_pwd').val(encryptedData);
		        $('#password').val("");
		    })
		   // var key  = CryptoJS.enc.Latin1.parse('IZnqNJqgLwPLO9LxMP23xZNmHHq55AmB');
			//var iv   = CryptoJS.enc.Latin1.parse('1234567890987654');
			
// 			 var key = CryptoJS.enc.Utf8.parse("IZnqNJqgLwPLO9LxMP23xZNmHHq55AmB");  
//        		 var iv  = CryptoJS.enc.Utf8.parse('1234567890987654');  
// 		    /**
// 			 * 加密
// 			 * @param word
// 			 * @returns {*}
// 			 */
// 			function encrypt(word){	
// 				debugger;
// 				  srcs = CryptoJS.enc.Utf8.parse(word);
// 	            var encrypted = CryptoJS.AES.encrypt(srcs, key, { iv: iv,mode:CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7});
// 	            return encrypted.ciphertext.toString();
// 			}
			 
// 			/**
// 			 * 解密
// 			 * @param word
// 			 * @returns {*}
// 			 */
// 			function decrypt(word){
// 			    var encryptedHexStr = CryptoJS.enc.Hex.parse(word);
// 	            var srcs = CryptoJS.enc.Base64.stringify(encryptedHexStr);
// 	            var decrypt = CryptoJS.AES.decrypt(srcs, key, { iv: iv,mode:CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7});
// 	            var decryptedStr = decrypt.toString(CryptoJS.enc.Utf8); 
// 	            return decryptedStr.toString();
// 			}


		//模块初始化
	
		 var  key = CryptoJS.enc.Utf8.parse("IZnqNJqgLwPLO9LxMP23xZNmHHq55AmB");
		 var  iv = CryptoJS.enc.Utf8.parse("1234567890987654");
		
		function encrypt(data) {
		    var encrypted='';
		    if(typeof(data)=='string')
		    {
		      encrypted = CryptoJS.AES.encrypt(data,key,{
		        iv : iv,
		        mode : CryptoJS.mode.CBC
		      });
		    }else if(typeof(data)=='object'){
		      data = JSON.stringify(data);
		      encrypted = CryptoJS.AES.encrypt(data,key,{
		        iv : iv,
		        mode : CryptoJS.mode.CBC
		      })
		    }
		    return encrypted.toString();
		}
		/*AES解密
		* param : message 密文
		* return : decrypted string 明文
		*/
		function decrypt(message) {
		    decrypted='';
		    decrypted=CryptoJS.AES.decrypt(message,key,{
		      iv : iv,
		      mode : CryptoJS.mode.CBC,
		      padding : CryptoJS.pad.ZeroPadding
		    });
		    return decrypted.toString(CryptoJS.enc.Utf8);
		}
</script>

