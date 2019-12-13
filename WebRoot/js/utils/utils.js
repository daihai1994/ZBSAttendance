/***
 * 验证手机号码
 */
function phoneNumber(res){
	  if(!(/^1[34578]\d{9}$/.test(res))){ 
		return false;
	  }else {
		  return true;
	  }
}
/***
 * 验证邮箱
 */
function mailAddress(res){
	 var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
	 if(!reg.test(res)){ 
		 return false;
	 }else{
		 return true;
	 }
}
/***
 * 验证传真
 */
function isFax(res){
	var checkFax = /^(\d{3,4}-)?\d{7,8}$/; 
	if(checkFax.test(res)){
		return true;
	}else{
		return false;
	}
}
/***
 * 验证座机号码
 */
function landlineNumber(res){
	if(!/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(res)){
		return false;
	}else{
		return true;
	}
}
/***
 * 判断不为空，不为null
 */
function isNotNull(res){
	if(res !== null && res !== undefined && res !== ''){
		return true;
	}else{
		return false;
	}
}
/***
 * 判断url
 */
function isURL(str_url){
	 var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
	 + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@ 
	  + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184 
	  + "|" // 允许IP和DOMAIN（域名）
	  + "([0-9a-z_!~*'()-]+\.)*" // 域名- www. 
	  + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名 
	  + "[a-z]{2,6})" // first level domain- .com or .museum 
	  + "(:[0-9]{1,4})?" // 端口- :80 
	  + "((/?)|" // a slash isn't required if there is no file name 
	  + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$"; 
	  var re=new RegExp(strRegex); 
	  if (re.test(str_url)){
		  return true; 
	  }else{ 
		  return false; 
	  }
}
/***
 * 判断两个时间的大小
 */
function timeCompare(date1,date2){
    var oDate1 = new Date(date1);
    var oDate2 = new Date(date2);
    if(oDate1.getTime() >= oDate2.getTime()){
        return true;
    } else {
    	return false;
    }
}
