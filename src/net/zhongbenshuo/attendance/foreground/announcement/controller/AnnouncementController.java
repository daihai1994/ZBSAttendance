package net.zhongbenshuo.attendance.foreground.announcement.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.bean.User;
import net.zhongbenshuo.attendance.foreground.announcement.bean.AnnouncementType;
import net.zhongbenshuo.attendance.foreground.announcement.bean.Priority;
import net.zhongbenshuo.attendance.foreground.announcement.service.AnnouncementService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.FileUnZip;
import net.zhongbenshuo.attendance.utils.Futil;

@Controller
@RequestMapping(value = "/announcementController", produces = "text/html;charset=UTF-8")
public class AnnouncementController {
	private Logger logger = Logger.getLogger(AnnouncementController.class);
	@Autowired
	LoggerMapper loggerMapper;
	@Autowired
	private AnnouncementService announcementService;
	
	//private static final String uploadPicture = "ammouncement";
	
	private static final String uploadFile = "ammouncementFile";
	
	private static final String pictureType = "^.*(png|jpg|gif|jpeg|webp)$";
	
	private static final String documentType = "^.*(doc|docx|xls|xlsx|ppt|pptx|pdf)$";
	
	private static final String videoType = "^.*(mp4|avi|rmvb|wmv|kux)$";
	
	private static final String audioType = "^.*(mp3|wav)$";
	
	private static final String zipType = "^.*(zip|rar)$";
	/**
	 * 查询公告制度的类型
	 * @return
	 */
	@RequestMapping(value="/findAnnouncementType.do")
	@ResponseBody
	private String findAnnouncementType(){
		List<AnnouncementType> announcementTypeList = new ArrayList<AnnouncementType>();
		announcementTypeList = announcementService.findAnnouncementType();
		return JSON.toJSONString(announcementTypeList);
	}
	/**
	 * 查询优先级
	 * @return
	 */
	@RequestMapping(value="/findPriority.do")
	@ResponseBody
	private String findPriority(){
		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList = announcementService.findPriority();
		return JSON.toJSONString(priorityList);
	}
	
	/***
	 * 提交图片
	 * @param uploadImg
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/uploadImg") 
	@ResponseBody
	public String uploadImg(MultipartFile uploadFile_announcement,HttpSession session,HttpServletRequest request) throws   Exception{
		User user = new User();
		user  = (User) session.getAttribute("user");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","上传失败");
		jsonObject.put("result","fail");
		String title = request.getParameter("title_announcement_add");
		String type = request.getParameter("type_announcement_add");
		String priority = request.getParameter("priority_announcement_add");
		String effectiveTime = request.getParameter("effectiveTime_announcement_add")+" 23:59:59";
		String external_link = request.getParameter("state_announcement_add");
		String addressUrl = request.getParameter("adUrl_announcement_add");
		String company_id = request.getParameter("announcement_company_id");
		String fileDirPath = "";
		
			String curProjectPath = session.getServletContext().getRealPath("/");
	      	int ps = curProjectPath.indexOf("webapps");
			String realpaths = curProjectPath.substring(0, ps + 8) +uploadFile + "\\";
			realpaths = realpaths.replaceAll("%20", " ");
			switch (external_link) {
			case "picture":
				if(!uploadImg_announcement(jsonObject,uploadFile_announcement,session,request,uploadFile)) {
					return jsonObject.toString();
				}
				addressUrl = (String) jsonObject.get("fileDirPath");
				break;
			case "document":
				if(!uploadDocument_announcement(jsonObject,uploadFile_announcement,session,request,uploadFile,documentType)) {
					return jsonObject.toString();
				}
				addressUrl = (String) jsonObject.get("fileDirPath");
				break;
			case "video":
				if(!uploadDocument_announcement(jsonObject,uploadFile_announcement,session,request,uploadFile,videoType)) {
					return jsonObject.toString();
				}
				addressUrl = (String) jsonObject.get("fileDirPath");
				break;
			case "audio":
				if(!uploadDocument_announcement(jsonObject,uploadFile_announcement,session,request,uploadFile,audioType)) {
					return jsonObject.toString();
				}
				addressUrl = (String) jsonObject.get("fileDirPath");
				break;
			case "local_links":
				if(!uploadZip_announcement(uploadFile_announcement,session,request)){
					//不是图片类型
					jsonObject.put("msg","压缩文件上传失败，需要的格式是zip或rar!");
					jsonObject.put("result","fail");
					return jsonObject.toString();
				}
					String a[] = uploadFile_announcement.getOriginalFilename().split("\\.");
					String saveUnZipPath=a[0];
					addressUrl = realpaths+"/projects/"+saveUnZipPath+"/index.html";
				break;
			case "external_links":
				
				break;
			default:
				break;
			}
			int user_id = 0;
			String user_name = "";
			if(user!=null){
				user_id = user.getUser_id();
				user_name = user.getUser_name();
			}
			int i = 0;
			i = announcementService.uploadImg(title,addressUrl,external_link,fileDirPath,effectiveTime,company_id,priority,type,user_id,user_name);
			if(i!=1){
				jsonObject.put("msg","上传失败,存储错误!");
				jsonObject.put("result","fail");
				return jsonObject.toString();
			}else{
				jsonObject.put("msg","上传成功!");
				jsonObject.put("result","success");
			}
		return jsonObject.toString();
	}
	
	
	
	@RequestMapping(value="/uploadImgEdit") 
	@ResponseBody
	public String uploadImgEdit(MultipartFile uploadFile_announcement_edit,HttpSession session,HttpServletRequest request) throws   Exception{
		User user = new User();
		user  = (User) session.getAttribute("user");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","上传失败");
		jsonObject.put("result","fail");
		String title = request.getParameter("title_announcement_edit");
		String type = request.getParameter("type_announcement_edit");
		String priority = request.getParameter("priority_announcement_edit");
		String effectiveTime = request.getParameter("effectiveTime_announcement_edit")+" 23:59:59";
		String external_link = request.getParameter("state_announcement_edit");
		String addressUrl = request.getParameter("adUrl_announcement_edit");
		String id = request.getParameter("announcement_id");
		String oldUrl = request.getParameter("announcement_picurl");
		String fileDirPath = "";
			String curProjectPath = session.getServletContext().getRealPath("/");
	      	int ps = curProjectPath.indexOf("webapps");
			String realpaths = curProjectPath.substring(0, ps + 8) +uploadFile + "\\";
			realpaths = realpaths.replaceAll("%20", " ");
			int user_id = 0;
			String user_name = "";
			if(user!=null){
				user_id = user.getUser_id();
				user_name = user.getUser_name();
			}
			if(uploadFile_announcement_edit==null||StringUtils.isBlank(uploadFile_announcement_edit.getOriginalFilename())){
				int i = 0;
				i = announcementService.uploadImgEditNotPicture(title,addressUrl,
						effectiveTime,id,priority,type,user_id,user_name);
				if(i!=1){
					jsonObject.put("msg","上传失败,存储错误!");
					jsonObject.put("result","fail");
					return jsonObject.toString();
				}else{
					jsonObject.put("msg","上传成功!");
					jsonObject.put("result","success");
				}
			}else{
				if (oldUrl != "" && oldUrl != null) {
					File delfile = new File(oldUrl);
					// 路径为文件且不为空则进行删除
					if (delfile.isFile() && delfile.exists()) {
						delfile.delete();
						System.out.println("删除旧公告成功");
					}
				}
				switch (external_link) {
				case "picture":
					if(!uploadImg_announcement(jsonObject,uploadFile_announcement_edit,session,request,uploadFile)) {
						return jsonObject.toString();
					}
					addressUrl = (String) jsonObject.get("fileDirPath");
					break;
				case "document":
					if(!uploadDocument_announcement(jsonObject,uploadFile_announcement_edit,session,request,uploadFile,documentType)) {
						return jsonObject.toString();
					}
					addressUrl = (String) jsonObject.get("fileDirPath");
					break;
				case "video":
					if(!uploadDocument_announcement(jsonObject,uploadFile_announcement_edit,session,request,uploadFile,videoType)) {
						return jsonObject.toString();
					}
					addressUrl = (String) jsonObject.get("fileDirPath");
					break;
				case "audio":
					if(!uploadDocument_announcement(jsonObject,uploadFile_announcement_edit,session,request,uploadFile,audioType)) {
						return jsonObject.toString();
					}
					addressUrl = (String) jsonObject.get("fileDirPath");
					break;
				case "local_links":
					if(!uploadZip_announcement(uploadFile_announcement_edit,session,request)){
						//不是图片类型
						jsonObject.put("msg","压缩文件上传失败，需要的格式是zip或rar!");
						jsonObject.put("result","fail");
						return jsonObject.toString();
					}
						String a[] = uploadFile_announcement_edit.getOriginalFilename().split("\\.");
						String saveUnZipPath=a[0];
						addressUrl = realpaths+"/projects/"+saveUnZipPath+"/index.html";
					break;
				case "external_links":
					
					break;
				default:
					break;
				}
				
				int i = 0;
				i = announcementService.uploadImgEdit(title,addressUrl,external_link,fileDirPath,effectiveTime,id,priority,type,user_id,user_name);
				if(i!=1){
					jsonObject.put("msg","上传失败,存储错误!");
					jsonObject.put("result","fail");
					return jsonObject.toString();
				}else{
					jsonObject.put("msg","上传成功!");
					jsonObject.put("result","success");
				}
			}
		return jsonObject.toString();
	}
	private boolean uploadZip_announcement(MultipartFile uploadFile_announcement, HttpSession session,
			HttpServletRequest request) {
		// 清除上次上传进度信息
		try {
			String curProjectPath = session.getServletContext().getRealPath("/");
          	String saveDirectoryPath = curProjectPath + "/" + uploadFile;
          //	File saveDirectory = new File(saveZipPath);
          	int p = curProjectPath.indexOf("webapps");
			String realpath = curProjectPath.substring(0, p + 8) +uploadFile + "\\";
			realpath = realpath.replaceAll("%20", " ");
			//判断是否存在
			File files=new File(realpath);
			if(!files.exists()){
				//生成文件夹
				files.mkdirs();
			}
          	// 判断文件是否存在
          	if (!uploadFile_announcement.isEmpty()) {
				String fileName = uploadFile_announcement.getOriginalFilename();
				String filePath = realpath+ fileName;
				String fileExtension = FilenameUtils.getExtension(fileName);
				if(!fileExtension.matches(zipType)) {
					return false;
				}
					//下面的方法用于上传文件
				uploadFile_announcement.transferTo(new File(filePath));
				String a[] = fileName.split("\\.");
				String saveUnZipPath=a[0];
				try {
					//解压缩,上传的压缩包存放在zips目录下，解压后的文件存在projects目录下
					FileUnZip.zipToFile(filePath,realpath+"/projects/"+saveUnZipPath);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
      	
	return true;
}
	private boolean uploadDocument_announcement(JSONObject jsonObject, MultipartFile uploadFile_announcement,
			HttpSession session, HttpServletRequest request, String type,String fileTypes) {
		if(uploadFile_announcement==null){
			jsonObject.put("msg","文件不能为空!");
			jsonObject.put("result","fail");
			return false;
		}
		String fileName=uploadFile_announcement.getOriginalFilename();
		//不是文件
		if(!fileName.matches(fileTypes)){
			//不是文件类型
			jsonObject.put("msg","文件格式错误!");
			jsonObject.put("result","fail");
			return false;
		}
		
		//2.判断是否为恶意程序
		try {
			 long fileSize = uploadFile_announcement.getSize();
			 if (fileSize <= 0) {
				 	jsonObject.put("msg","文件不能为空!");
					jsonObject.put("result","fail");
					return false;
		        } else if (fileSize > (1024 * 1024*100)) {
		        	jsonObject.put("msg","文件不能大于100M");
					jsonObject.put("result","fail");
					return false;
		            
		        }
		//3.由于文件个数多,采用分文件存储
		String dateDir=
				new SimpleDateFormat("yyyy/MM/dd")
						.format(new Date());
	
		//生成对应的文件夹
		String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
		int p = path.indexOf("webapps");
		String realpath = path.substring(0, p + 8) +type+dateDir + "\\";
		realpath = realpath.replaceAll("%20", " ");
		//判断是否存在
		File file=new File(realpath);
		if(!file.exists()){
			//生成文件夹
			file.mkdirs();
		}
		//防止图片上传量过大引起的重名问题
		String  uuidName=
				UUID.randomUUID()
					.toString().replace("-", "");	
		String  randomNum=((int)(Math.random()*99999))+"";
		//获取文件后缀名
		String fileType=
				fileName.substring(
						fileName.lastIndexOf("."));
		String prefix=fileName.substring(0, fileName.lastIndexOf("."));
		//路径拼接(文件真实的存储路径)
		String  fileDirPath=
				realpath+"/"+prefix+uuidName+randomNum+fileType;
		
		//文件上传
		uploadFile_announcement.transferTo(new File(fileDirPath));
		jsonObject.put("fileDirPath",fileDirPath);
		jsonObject.put("msg","上传成功!");
		jsonObject.put("result","success");
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	private boolean uploadImg_announcement(JSONObject jsonObject, MultipartFile uploadImg_announcement,
			HttpSession session, HttpServletRequest request,String type) {
		if(uploadImg_announcement==null){
			jsonObject.put("msg","图片不能为空!");
			jsonObject.put("result","fail");
			return false;
		}
		String fileName=uploadImg_announcement.getOriginalFilename();
		//不是图片
		if(!fileName.matches(pictureType)){
			//不是图片类型
			jsonObject.put("msg","图片格式错误!");
			jsonObject.put("result","fail");
			return false;
		}
		//2.判断是否为恶意程序
				try {
					BufferedImage bufferedImage=
							ImageIO.read(uploadImg_announcement.getInputStream());
					//2.1获取宽高
					int height=bufferedImage.getHeight();
					int width=bufferedImage.getWidth();
					
					if(width<720){
						//表示不是图片
						jsonObject.put("msg","图片宽度不能小于720!");
						jsonObject.put("result","fail");
						return false;
					}
					 long fileSize = uploadImg_announcement.getSize();
					 if (fileSize <= 0) {
						 	jsonObject.put("msg","图片不能为空!");
							jsonObject.put("result","fail");
							return false;
				        } else if (fileSize > (1024 * 1024)) {
				        	jsonObject.put("msg","图片不能大于1M");
							jsonObject.put("result","fail");
							return false;
				            
				        }
				//3.由于文件个数多,采用分文件存储
				String dateDir=
						new SimpleDateFormat("yyyy/MM/dd")
								.format(new Date());
			
				//生成对应的文件夹
				String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
				int p = path.indexOf("webapps");
				String realpath = path.substring(0, p + 8) +type+dateDir + "\\";
				realpath = realpath.replaceAll("%20", " ");
				//判断是否存在
				File file=new File(realpath);
				if(!file.exists()){
					//生成文件夹
					file.mkdirs();
				}
				//防止图片上传量过大引起的重名问题
				String  uuidName=
						UUID.randomUUID()
							.toString().replace("-", "");	
				String  randomNum=((int)(Math.random()*99999))+"";
				//获取文件后缀名
				String fileType=
						fileName.substring(
								fileName.lastIndexOf("."));
				String prefix=fileName.substring(0, fileName.lastIndexOf("."));
				//路径拼接(文件真实的存储路径)
				String  fileDirPath=
						realpath+"/"+prefix+uuidName+randomNum+fileType;
				
				//文件上传
				uploadImg_announcement.transferTo(new File(fileDirPath));
				jsonObject.put("fileDirPath",fileDirPath);
				jsonObject.put("msg","上传成功!");
				jsonObject.put("result","success");
				
				} catch (IOException e) {
					e.printStackTrace();
				}
		return true;
	}
	/**
	 * 查询公告制度
	 * @param bt
	 * @param et
	 * @param remarks
	 * @param company_id
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/findannouncement.do")
	@ResponseBody
	private String findannouncement(String bt,String et,String title,int company_id,int bNum,int rows,HttpSession session,HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		jsonObject = announcementService.findannouncement(bNum,rows,bt,et,title,company_id);
		return jsonObject.toString();
	}
}
