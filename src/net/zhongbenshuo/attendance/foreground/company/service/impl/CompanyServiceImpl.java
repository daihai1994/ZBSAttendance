package net.zhongbenshuo.attendance.foreground.company.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.zhongbenshuo.attendance.bean.Department;
import net.zhongbenshuo.attendance.bean.Position;
import net.zhongbenshuo.attendance.foreground.accountInfo.bean.PositionUser;
import net.zhongbenshuo.attendance.foreground.company.bean.Company;
import net.zhongbenshuo.attendance.foreground.company.bean.DepartmentJSON;
import net.zhongbenshuo.attendance.foreground.company.bean.PositionJSON;
import net.zhongbenshuo.attendance.foreground.company.bean.PositionUserJSON;
import net.zhongbenshuo.attendance.foreground.company.mapper.CompanyMapper;
import net.zhongbenshuo.attendance.foreground.company.service.CompanyService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.Futil;

@Service
public class CompanyServiceImpl implements CompanyService {
	public static Logger logger = LogManager.getLogger(CompanyServiceImpl.class);
	
	@Autowired
	CompanyMapper companyMapper;
	
	@Autowired
	LoggerMapper loggerMapper;
	/***
	 * 查询公司信息
	 * @param company_name
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject findCompanyInfo(String company_name, int bNum, int rows, int company_id) {
		JSONObject jsonObject = new JSONObject();
		List<Company> companyList = new ArrayList<Company>();
		try {
			companyList = companyMapper.findCompanyInfo(company_name,bNum,rows,company_id);
			int size = 0;
			if(companyList!=null&&companyList.size()>0){
				size = companyList.get(0).getSize();
			}
			jsonObject.put("rows", companyList);
			jsonObject.put("total", size);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("查询公司信息报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 新增修改公司名称
	 * @param company_name
	 * @param compan_id
	 * @param company_name_old
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitCompanyAdd(String company_name, String company_id,String company_name_old,
			String duty_paragraph, String address,  String landline_number, String official_website, String fax, String mail, String bank_account, 
			 String bankCode, String bankName, String cardType, String cardTypeName,String dataCompanyStrng,String enable_application,
			HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if(StringUtils.isNotBlank(company_id)){
			jsonObject.put("msg","修改失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = companyMapper.updateCompanyName(company_name,company_id,duty_paragraph,address,landline_number,
							official_website,fax,mail,bank_account,bankCode,bankName,cardType,cardTypeName,enable_application);
				 if(i>0){
						jsonObject.put("msg","修改成功");
						jsonObject.put("result","success");
						int user_id = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "原有数据："+dataCompanyStrng+
								"-----修改后数据 公司："+company_name+"税号："+duty_paragraph
								+"地址："+address
								+"座机号："+landline_number
								+"官网："+official_website
								+"传真："+fax
								+"邮箱："+mail
								+"卡号："+bank_account
								+"银行："+bankName;
						loggerMapper.addLogger(user_id,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("修改公司信息报错"+e);
			}
			
		}else{
			jsonObject.put("msg","新增失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = companyMapper.addCompanyName(company_name,duty_paragraph,address,landline_number,
							official_website,fax,mail,bank_account,bankCode,bankName,cardType,cardTypeName,enable_application);
				 if(i>0){
						jsonObject.put("msg","新增成功");
						jsonObject.put("result","success");
						int user_id = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "新增公司："+company_name+"税号："+duty_paragraph
								+"地址："+address
								+"座机号："+landline_number
								+"官网："+official_website
								+"传真："+fax
								+"邮箱："+mail
								+"卡号："+bank_account
								+"银行："+bankName;
						loggerMapper.addLogger(user_id,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("新增公司信息报错"+e);
			}
		}
		return jsonObject;
	}
	/***
	 * 删除公司信息
	 * @param company_name
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject deleteCompanyInfo(String company_name, String company_id, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","删除失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = companyMapper.deleteCompanyInfo(company_id);
			 if(i>0){
					jsonObject.put("msg","删除成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "把"+company_name+"删除";
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("删除公司信息报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 修改删除密码
	 * @param company_id
	 * @param old_deletePwd
	 * @param deletePwd
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitCompanydeletePwd_edit(String company_id, String old_deletePwd, String deletePwd,String company_name,
			HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","修改失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = companyMapper.submitCompanydeletePwd_edit(company_id,deletePwd);
			 if(i>0){
					jsonObject.put("msg","修改成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "把"+company_name+"的密码"+old_deletePwd+"改为"+deletePwd;
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("修改公司删除密码信息报错"+e);
		}
		return jsonObject;
	}
	/***
	 * 查询部门的信息
	 * @param company_id
	 * @return
	 */
	@Override
	public JSONObject findDepartMent(String company_id) {
		JSONObject jsonObject = new JSONObject();
		List<Department> departmentList = new ArrayList<Department>();
		try {
			departmentList = companyMapper.findDepartMent(company_id);
			jsonObject.put("rows", departmentList);
			jsonObject.put("total", departmentList.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("查询部门信息报错"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 新增，修改部门的信息
	 * @param department_id
	 * @param department_name_old
	 * @param department_priority_old
	 * @param department_name
	 * @param department_priority
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitDepartmentAdd(String department_id, String department_name_old,
			String department_priority_old, String department_name, String department_priority, String company_id,
			HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if(StringUtils.isNotBlank(department_id)){
			jsonObject.put("msg","修改失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = companyMapper.updateDepartment(department_name,department_priority,department_id);
				 if(i>0){
						jsonObject.put("msg","修改成功");
						jsonObject.put("result","success");
						int user_id = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "把"+department_name_old+"改成"+department_name+";把排序"+department_priority_old+"改成"+department_priority;
						loggerMapper.addLogger(user_id,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("修改部门信息报错"+e);
			}
			
		}else{
			jsonObject.put("msg","新增失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = companyMapper.addDepartment(department_name,department_priority,company_id);
				 if(i>0){
						jsonObject.put("msg","新增成功");
						jsonObject.put("result","success");
						int user_id = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "新增"+department_name+"排序是"+department_priority;
						loggerMapper.addLogger(user_id,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("新增公司信息报错"+e);
			}
		}
		return jsonObject;
	
	}
	/***
	 * 删除部门信息
	 * @param department_id
	 * @param department_name
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject deleteDepartMent(String department_id, String department_name, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","删除失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = companyMapper.deleteDepartMent(department_id);
			 if(i>0){
					jsonObject.put("msg","删除成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "把"+department_name+"删除";
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("删除部门信息报错"+e);
		}
		return jsonObject;
	
	}
	
	/***
	 * 查询职位信息
	 * @param department_id
	 * @return
	 */
	@Override
	public JSONObject findPosition(String department_id) {
		JSONObject jsonObject = new JSONObject();
		List<Position> positionList = new ArrayList<Position>();
		try {
			positionList = companyMapper.findPosition(department_id);
			jsonObject.put("rows", positionList);
			jsonObject.put("total", positionList.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("查询职位信息报错"+e);
		}
		return jsonObject;
	
	
	}
	/***
	 * 新增修改职位信息
	 * @param position_id
	 * @param position_name_old
	 * @param position_name
	 * @param departMent_id
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject submitPositionAdd(String position_id, String position_name_old, String position_name,
			String departMent_id, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		if(StringUtils.isNotBlank(position_id)){
			jsonObject.put("msg","修改失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = companyMapper.updatePosition(position_name,position_id);
				 if(i>0){
						jsonObject.put("msg","修改成功");
						jsonObject.put("result","success");
						int user_id = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "把"+position_name_old+"改成"+position_name;
						loggerMapper.addLogger(user_id,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("修改部门信息报错"+e);
			}
			
		}else{
			jsonObject.put("msg","新增失败");
			jsonObject.put("result","fail");
			int i = 0;
			try {
				 i = companyMapper.addPosition(position_name,departMent_id);
				 if(i>0){
						jsonObject.put("msg","新增成功");
						jsonObject.put("result","success");
						int user_id = Futil.getUserId(session, request);
						String ip = Futil.getIpAddr(request);
						String remarks = "新增"+position_name;
						loggerMapper.addLogger(user_id,ip,remarks);
					}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				logger.info("新增公司信息报错"+e);
			}
		}
		return jsonObject;
	
	
	}
	/***
	 * 删除职位信息
	 * @param position_id
	 * @param position_name
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject deletePosition(String position_id, String position_name, HttpSession session,
			HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","删除失败");
		jsonObject.put("result","fail");
		int i = 0;
		try {
			 i = companyMapper.deletePosition(position_id);
			 if(i>0){
					jsonObject.put("msg","删除成功");
					jsonObject.put("result","success");
					int user_id = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remarks = "把"+position_name+"删除";
					loggerMapper.addLogger(user_id,ip,remarks);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("删除职位信息报错"+e);
		}
		return jsonObject;
	
	}
	/***
	 * 修改职位的排序
	 */
	@Override
	public JSONObject positionPriority(String liststring, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","修改失败");
		jsonObject.put("result","fail");
		List<Position> positionList = new ArrayList<Position>();
		String remark = "";
		try {
			///Json的解析类对象
		    JsonParser parser = new JsonParser();
		    //将JSON的String 转成一个JsonArray对象
		    JsonArray jsonArray = parser.parse(liststring).getAsJsonArray();

		    Gson gson = new Gson();
		    List<PositionJSON> positionJSONs = new ArrayList<PositionJSON>();

		    //循环遍历
		    for (JsonElement user : jsonArray) {
		    	PositionJSON positionJSON = gson.fromJson(user, PositionJSON.class);
		    	positionJSONs.add(positionJSON);
		    }
			for(PositionJSON job : positionJSONs){
				String position = job.getPosition();
			    String id = job.getId();
			    String oldpriority = job.getOldpriority();
			    String priority = job.getPriority();
			    remark+="把"+position+"从排序"+oldpriority+"变成排序"+priority+";";
			    Position positions = new Position();
			    positions.setPosition_id(Integer.valueOf(id));
			    positions.setPriority(Integer.valueOf(priority));
			    positionList.add(positions);
			}
			companyMapper.positionPriority(positionList);
			if(remark.length()>0){
				remark = remark.substring(0, remark.length()-1);
			}
			jsonObject.put("msg","修改成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "职位排序改变；"+remark;
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("修改职位排序信息报错"+e);
		}
		return jsonObject;
	
	}
	/**
	 * 员工的排序
	 */
	@Override
	public JSONObject positionUserPriority(String liststring, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","修改失败");
		jsonObject.put("result","fail");
		List<PositionUser> positionUserList = new ArrayList<PositionUser>();
		String remark = "";
		try {
			///Json的解析类对象
		    JsonParser parser = new JsonParser();
		    //将JSON的String 转成一个JsonArray对象
		    JsonArray jsonArray = parser.parse(liststring).getAsJsonArray();

		    Gson gson = new Gson();
		    List<PositionUserJSON> positionUserJSONs = new ArrayList<PositionUserJSON>();

		    //循环遍历
		    for (JsonElement user : jsonArray) {
		    	PositionUserJSON positionUserJSON = gson.fromJson(user, PositionUserJSON.class);
		    	positionUserJSONs.add(positionUserJSON);
		    }
			for(PositionUserJSON job : positionUserJSONs){
				String user_name = job.getUser_name();
			    String id = job.getId();
			    String oldpriority = job.getOldpriority();
			    String priority = job.getPriority();
			    remark+="把"+user_name+"从排序"+oldpriority+"变成排序"+priority+";";
			    PositionUser positions = new PositionUser();
			    positions.setId(Integer.valueOf(id));
			    positions.setPriority(Integer.valueOf(priority));
			    positionUserList.add(positions);
			}
			companyMapper.positionUserPriority(positionUserList);
			if(remark.length()>0){
				remark = remark.substring(0, remark.length()-1);
			}
			jsonObject.put("msg","修改成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "职位排序改变；"+remark;
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("修改职位排序信息报错"+e);
		}
		return jsonObject;
	
	}
	/***
	 * 修改部门的排序
	 * @param liststring
	 * @param session
	 * @param request
	 * @return
	 */
	@Override
	public JSONObject confirmPriority(String liststring, HttpSession session, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","修改失败");
		jsonObject.put("result","fail");
		List<Department> departmentList = new ArrayList<Department>();
		String remark = "";
		try {
			///Json的解析类对象
		    JsonParser parser = new JsonParser();
		    //将JSON的String 转成一个JsonArray对象
		    JsonArray jsonArray = parser.parse(liststring).getAsJsonArray();

		    Gson gson = new Gson();
		    List<DepartmentJSON> departmentJSONs = new ArrayList<DepartmentJSON>();

		    //循环遍历
		    for (JsonElement user : jsonArray) {
		    	DepartmentJSON departmentJSON = gson.fromJson(user, DepartmentJSON.class);
		    	departmentJSONs.add(departmentJSON);
		    }
			for(DepartmentJSON job : departmentJSONs){
				String department = job.getDepartment();
			    String id = job.getId();
			    String oldpriority = job.getOldpriority();
			    String priority = job.getPriority();
			    remark+="把"+department+"从排序"+oldpriority+"变成排序"+priority+";";
			    Department departments = new Department();
			    departments.setDepartment_id(Integer.valueOf(id));
			    departments.setPriority(Integer.valueOf(priority));
			    departmentList.add(departments);
			}
			companyMapper.confirmPriority(departmentList);
			if(remark.length()>0){
				remark = remark.substring(0, remark.length()-1);
			}
			jsonObject.put("msg","修改成功");
			jsonObject.put("result","success");
			int user_id = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "部门排序改变；"+remark;
			loggerMapper.addLogger(user_id,ip,remarks);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("修改部门排序信息报错"+e);
		}
		return jsonObject;
	}
	/**
	 * 查询职位和人员之间的信息
	 */
	@Override
	public JSONObject findPositionUser(String position_id) {
		JSONObject jsonObject = new JSONObject();
		List<PositionUser> positionUser = new ArrayList<PositionUser>();
		try {
			positionUser = companyMapper.findPositionUser(position_id);
			jsonObject.put("rows", positionUser);
			jsonObject.put("total", positionUser.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			logger.info("查询职位和人员信息报错"+e);
		}
		return jsonObject;
	}
}
