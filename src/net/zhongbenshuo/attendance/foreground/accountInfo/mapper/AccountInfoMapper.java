package net.zhongbenshuo.attendance.foreground.accountInfo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.foreground.accountInfo.bean.Authority;
import net.zhongbenshuo.attendance.foreground.accountInfo.bean.PositionUser;
import net.zhongbenshuo.attendance.foreground.version.bean.Combox;

public interface AccountInfoMapper {
	/***
	 * 查询公司下的权限信息
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	List<Authority> findAuthority(@Param("company_id")String company_id);
	/***
	 * 修改权限信息
	 * @param authority_id
	 * @param authority_describe
	 * @param authority_code
	 * @return
	 */
	int updateAuthority(@Param("authority_id")String authority_id, @Param("authority_describe")String authority_describe);
	/***
	 * 新增权限信息
	 * @param company_id
	 * @param authority_describe
	 * @param authority_code
	 * @return
	 */
	int addAuthority(@Param("company_id")String company_id, @Param("authority_describe")String authority_describe);
	/**
	 * 物理删除权限信息
	 * @param authority_id
	 * @param authority_describe
	 * @param session
	 * @param request
	 * @return
	 */
	int deleteAuthority(@Param("authority_id")String authority_id);
	/**
	 * 查询账号信息
	 * @param company_id
	 * @param user_name 
	 * @param phone_number 
	 * @param user_id 
	 * @param session
	 * @param request
	 * @return
	 */
	List<User> findAccountInfo(@Param("company_id")String company_id, @Param("user_id")String user_id, @Param("phone_number")String phone_number,
			@Param("user_name")String user_name,@Param("role")int role,@Param("id")int id,@Param("jobNumber")String jobNumber,@Param("bNum")int bNum,@Param("rows")int rows);
	/***
	 * 根据公司ID查询部门信息
	 * @param company_id
	 * @param session
	 * @param request
	 * @return
	 */
	List<Combox> findDepartment(@Param("company_id")String company_id);
	/***
	 * 根据部门ID查询职位信息
	 * @param department_id
	 * @param session
	 * @param request
	 * @return
	 */
	List<Combox> findPosition(@Param("department_id")String department_id);
	/***
	 * 查询最大的user_id
	 * @param session
	 * @param request
	 * @return
	 */
	String findUser_Id();
	/***
	 * 新增账号信息
	 * @param user_id
	 * @param company_id
	 * @param department
	 * @param position
	 * @param department1
	 * @param position1
	 * @param user_name
	 * @param user_pwd
	 * @param gender
	 * @param phone_number
	 * @param mail_address
	 * @param contact_address
	 * @param emergency_contact_name
	 * @param emergency_contact_phone
	 * @param positionList 
	 * @param departmentList 
	 * @return
	 */
	int addAccountInfo(@Param("job_number")String job_number ,@Param("user_id")String user_id, @Param("company_id")String company_id, @Param("user_name")String user_name, @Param("user_pwd")String user_pwd, 
			@Param("gender")String gender, @Param("phone_number")String phone_number,
			@Param("mail_address")String mail_address, @Param("contact_address")String contact_address, @Param("emergency_contact_name")String emergency_contact_name, 
			@Param("emergency_contact_phone")String emergency_contact_phone, @Param("departmentList")String departmentList, @Param("positionList")String positionList,
			@Param("ip")String ip,@Param("role")String role,@Param("entry_time")String entry_time);
	/***
	 * 修改账号信息
	 * @param user_name
	 * @param user_pwd
	 * @param gender
	 * @param phone_number
	 * @param mail_address
	 * @param contact_address
	 * @param emergency_contact_name
	 * @param emergency_contact_phone
	 * @param departmentList
	 * @param positionList
	 * @return
	 */
	int updateAccountInfo(@Param("job_number")String job_number,@Param("accountInfo_id")String accountInfo_id,@Param("user_name")String user_name, 
			@Param("gender")String gender, @Param("phone_number")String phone_number, @Param("mail_address")String mail_address,
			@Param("contact_address")String contact_address, @Param("emergency_contact_name")String emergency_contact_name, @Param("emergency_contact_phone")String emergency_contact_phone,
			@Param("departmentList")String departmentList, @Param("positionList")String positionList,@Param("role")String role,@Param("entry_time")String entry_time);
	/***
	 * 根据department_id查询department（是以逗号分隔的）
	 * @param department_id
	 * @return
	 */
	String findDepartmentName(@Param("department_id")String department_id);
	/***
	 * 根据position_id查询position（是以逗号分隔的）
	 * @param position_id
	 * @return
	 */
	String findPositionName(@Param("position_id")String position_id);
	/***
	 * 物理删除账号信息
	 * @param accountInfo_id
	 * @return
	 */
	int deleteAccountInfo(@Param("accountInfo_id")String accountInfo_id);
	/***
	 * 查询公司下的最大的工号
	 * @param company_id
	 * @return
	 */
	String findJobNumber(@Param("company_id")String company_id);
	/***
	 * 根据账号ID，公司查询工号，是否存在
	 * @param job_number
	 * @param accountInfo_id
	 * @param company_id
	 * @return
	 */
	int findJobNumberCount(@Param("job_number")String job_number, @Param("accountInfo_id")String accountInfo_id,@Param("company_id")String company_id);
	/***
	 * 配置账号权限信息
	 * @param authorityId
	 * @param user_id
	 * @return
	 */
	int addUserAndAuthority(@Param("authorityId")String authorityId, @Param("user_id")String user_id);
	/***
	 * 查询账号和用户关联的权限
	 * @param authority_id
	 * @return
	 */
	String findAuthorityName(@Param("authority_id")String authority_id);
	/***
	 * 根据key修改用户的某一个信息
	 * @param accountIndfoId
	 * @param newInfo
	 * @param key
	 * @return
	 */
	int updateInfo(@Param("accountIndfoId")String accountIndfoId, @Param("newInfo")String newInfo, @Param("key")String key);
	/**
	 * 新增用户和职位之间的关系
	 * @param positionUser
	 * @param user_id
	 */
	void addPositionUser(@Param("list")List<String> positionUser, @Param("user_id")String user_id);
	/**
	 * 查询用户的原先职位和优先级
	 * @param user_id
	 * @return
	 */
	List<PositionUser> findPositionUser(@Param("user_id")String user_id);
	/**
	 * 删除用户和职位之间的关系
	 * @param positionUserDelete
	 */
	void deletePositionUser(List<String> positionUserDelete);
	/**
	 * 删除职位和用户的关系
	 * @param user_id
	 */
	void deletePositionByUser(@Param("user_id")String user_id);
}
