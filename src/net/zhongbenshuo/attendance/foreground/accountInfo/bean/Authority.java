package net.zhongbenshuo.attendance.foreground.accountInfo.bean;

public class Authority {
	public int id;//权限ID
	public String authority_describe;//描述
	public String authority_code;//编码
	public int effective;//是否有效，1有效，0无效
	public int company_id;//公司ID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthority_describe() {
		return authority_describe;
	}
	public void setAuthority_describe(String authority_describe) {
		this.authority_describe = authority_describe;
	}
	public String getAuthority_code() {
		return authority_code;
	}
	public void setAuthority_code(String authority_code) {
		this.authority_code = authority_code;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	@Override
	public String toString() {
		return "Authority [id=" + id + ", authority_describe=" + authority_describe + ", authority_code="
				+ authority_code + ", effective=" + effective + ", company_id=" + company_id + "]";
	}
	
}
