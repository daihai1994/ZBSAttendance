package net.zhongbenshuo.attendance.foreground.company.bean;

public class Company {
	private int id;//公司主键
	private String company_name = "";//公司名称
	private String createTime= "";//创建时间
	private String deletePwd= "";//删除密码
	private int effective;//是否有效，1是有效，0是无效
	private int size;//个数
	private String duty_paragraph= "";//税号
	private String address= "";//地址
	private String bank_name= "";//银行
	private String bank_account= "";//卡号
	private String official_website= "";//官网
	private String fax= "";//传真
	private String mail= "";//邮箱
	private String bankCode= "";//银行编号
	private String card_type_name= "";//卡类型
	private String card_type= "";//卡类型编码
	private String landline_number= "";//座机号码
	private String enable_application;//是否启用申请，0不启用，1启用
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDeletePwd() {
		return deletePwd;
	}
	public void setDeletePwd(String deletePwd) {
		this.deletePwd = deletePwd;
	}
	public int getEffective() {
		return effective;
	}
	public void setEffective(int effective) {
		this.effective = effective;
	}
	
	public String getDuty_paragraph() {
		return duty_paragraph;
	}
	public void setDuty_paragraph(String duty_paragraph) {
		this.duty_paragraph = duty_paragraph;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank_account() {
		return bank_account;
	}
	public void setBank_account(String bank_account) {
		this.bank_account = bank_account;
	}
	public String getOfficial_website() {
		return official_website;
	}
	public void setOfficial_website(String official_website) {
		this.official_website = official_website;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getCard_type_name() {
		return card_type_name;
	}
	public void setCard_type_name(String card_type_name) {
		this.card_type_name = card_type_name;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getLandline_number() {
		return landline_number;
	}
	public void setLandline_number(String landline_number) {
		this.landline_number = landline_number;
	}
	public String getEnable_application() {
		return enable_application;
	}
	public void setEnable_application(String enable_application) {
		this.enable_application = enable_application;
	}
	@Override
	public String toString() {
		return "Company [id=" + id + ", company_name=" + company_name + ", createTime=" + createTime + ", deletePwd="
				+ deletePwd + ", effective=" + effective + ", size=" + size + ", duty_paragraph=" + duty_paragraph
				+ ", address=" + address + ", bank_name=" + bank_name + ", bank_account=" + bank_account
				+ ", official_website=" + official_website + ", fax=" + fax + ", mail=" + mail + ", bankCode="
				+ bankCode + ", card_type_name=" + card_type_name + ", card_type=" + card_type + ", landline_number="
				+ landline_number + ", enable_application=" + enable_application + "]";
	}
	
}
