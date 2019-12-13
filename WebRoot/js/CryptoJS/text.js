 			/**
			 * 加密
			 * @param word
			 * @returns {*}
			 */
			function encrypt(word){
			    var key = CryptoJS.enc.Utf8.parse("IZnqNJqgLwPLO9LxMP23xZNmHHq55AmB");
			    var srcs = CryptoJS.enc.Utf8.parse(word);
			    var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
			    return encrypted.toString();
			}
			 
			/**
			 * 解密
			 * @param word
			 * @returns {*}
			 */
			function decrypt(word){
			    var key = CryptoJS.enc.Utf8.parse("IZnqNJqgLwPLO9LxMP23xZNmHHq55AmB");
			    var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
			    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
			}
			
			console.log(encrypt("123456"));//加密
			console.log(decrypt(encrypt("123456")));//解密