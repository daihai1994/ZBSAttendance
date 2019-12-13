package net.zhongbenshuo.attendance.foreground.homePagePicture.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zhongbenshuo.attendance.foreground.homePagePicture.bean.HomePagePicture;

public interface HomePagePictureMapper {
	/**
	 * 安卓轮播图的查询
	 * @param bNum
	 * @param rows
	 * @param bt
	 * @param et
	 * @param name
	 * @return
	 */
	List<HomePagePicture> findhomePagePicture(@Param("bNum")int bNum, @Param("rows")int rows, @Param("bt")String bt, @Param("et")String et, 
			@Param("remarks")String remarks,@Param("company_id")int company_id);
	/**
	 * 提交新增轮播图
	 * @param remarks
	 * @param addressUrl
	 * @param state
	 * @param fileDirPath
	 * @param effective
	 * @return
	 */
	int uploadImg(@Param("remarks")String remarks, @Param("addressUrl")String addressUrl, @Param("state")String state, @Param("fileDirPath")String fileDirPath, 
			@Param("effective")String effective,@Param("company_id")String company_id);
	/**
	 * 查询需要修改的轮播图详情信息
	 * @param id
	 * @return homePagePicture
	 */
	HomePagePicture findHomePagePictureByID(@Param("id")String id);
	/**
	 * 修改没有改变图片的轮播图信息
	 * @param remarks
	 * @param state
	 * @param addressUrl
	 * @param effective
	 * @param id
	 * @return
	 */
	int updateuploadNotImg(@Param("remarks")String remarks, @Param("state")String state, @Param("addressUrl")String addressUrl, @Param("effective")String effective, @Param("id")String id);
	/**
	 * 修改有改过图片的轮播图信息
	 * @param remarks
	 * @param state
	 * @param addressUrl
	 * @param effective
	 * @param id
	 * @param fileDirPath
	 * @return
	 */
	int updateuploadImg(@Param("remarks")String remarks, @Param("state")String state, @Param("addressUrl")String addressUrl, @Param("effective")String effective, @Param("id")String id,
			@Param("fileDirPath")String fileDirPath);
	
	
}
