package net.zhongbenshuo.attendance.foreground.homePagePicture.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.homePagePicture.bean.HomePagePicture;
import net.zhongbenshuo.attendance.foreground.homePagePicture.service.HomePagePictureService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.FileUnZip;
import net.zhongbenshuo.attendance.utils.Futil;

@Controller
@RequestMapping(value = "/homePagePictureController", produces = "text/html;charset=UTF-8")
public class HomePagePictureController {
	
	public static final String filePath = "HomePagePicture";// 轮播图图片地址
	
	private static final String uploadFolderName = "uploadFiles";
	
	 private static final String [] extensionPermit = {"zip","rar"};
	@Autowired
	HomePagePictureService homePagePictureService;
	
	@Autowired
	LoggerMapper loggerMapper;
	/**
	 * 安卓轮播图的查询
	 * @param bt
	 * @param et
	 * @param name
	 * @param bNum
	 * @param rows
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findhomePagePicture.do")
	@ResponseBody
	public String findhomePagePicture(String bt,String et,String remarks,int company_id,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = homePagePictureService.findhomePagePicture(bNum,rows,bt,et,remarks,company_id);
		return jsonObject.toString();
	}
	
	/***
	 * 提交新增广告
	 * @param uploadImg
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/uploadImg") 
	@ResponseBody
	public String uploadImg( MultipartFile uploadImg_homePagePicture,MultipartFile uploadFile_homePagePicture,HttpSession session,HttpServletRequest request) throws   Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","上传失败");
		jsonObject.put("result","fail");
		if(uploadImg_homePagePicture==null){
			jsonObject.put("msg","图片不能为空!");
			jsonObject.put("result","fail");
			return jsonObject.toString();
		}
		 long fileHomeSize = uploadFile_homePagePicture.getSize();
		 if(fileHomeSize>0){
			 if(!uploadFile_home(uploadFile_homePagePicture,session,request)){
					//不是图片类型
					jsonObject.put("msg","压缩文件上传失败，需要的格式是zip或rar!");
					jsonObject.put("result","fail");
					return jsonObject.toString();
				}
		 }
		
		String fileName=uploadImg_homePagePicture.getOriginalFilename();
		//不是图片
		if(!fileName.matches("^.*(png|jpg|gif|jpeg)$")){
			//不是图片类型
			jsonObject.put("msg","图片格式错误!");
			jsonObject.put("result","fail");
			return jsonObject.toString();
		}
		String curProjectPath = session.getServletContext().getRealPath("/");
      	int ps = curProjectPath.indexOf("webapps");
		String realpaths = curProjectPath.substring(0, ps + 8) +uploadFolderName + "\\";
		realpaths = realpaths.replaceAll("%20", " ");
		String remarks = request.getParameter("remarks_homePagePicture_add");
		String effective = request.getParameter("effective_homePagePicture_add");
		String state = request.getParameter("state_homePagePicture_add");
		String addressUrl = request.getParameter("adUrl_homePagePicture_add");
		String company_id = request.getParameter("homePagePicture_company_id");
		if(fileHomeSize>0){
			if(state.equals("0")){
				String a[] = uploadFile_homePagePicture.getOriginalFilename().split("\\.");
				String saveUnZipPath=a[0];
				addressUrl = realpaths+"/projects/"+saveUnZipPath+"/index.html";
			}
		}
		
		//2.判断是否为恶意程序
				try {
					BufferedImage bufferedImage=
							ImageIO.read(uploadImg_homePagePicture.getInputStream());
					//2.1获取宽高
					int height=bufferedImage.getHeight();
					int width=bufferedImage.getWidth();
					double scale = height/width;
					int heightS = height*16;
					int widthS = width*9;
					if(width<720||heightS!=widthS){
						//表示不是图片
						jsonObject.put("msg","图片尺寸错误!高宽比例16:9，宽最少720!");
						jsonObject.put("result","fail");
						return jsonObject.toString();
					}
					 long fileSize = uploadImg_homePagePicture.getSize();
					 if (fileSize <= 0) {
						 	jsonObject.put("msg","图片不能为空!");
							jsonObject.put("result","fail");
							return jsonObject.toString();
				        } else if (fileSize > (1024 * 1024)) {
				        	jsonObject.put("msg","图片不能大于1M");
							jsonObject.put("result","fail");
							return jsonObject.toString();
				            
				        }
				//3.由于文件个数多,采用分文件存储
				String dateDir=
						new SimpleDateFormat("yyyy/MM/dd")
								.format(new Date());
			
				//生成对应的文件夹
				String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
				int p = path.indexOf("webapps");
				String realpath = path.substring(0, p + 8) +filePath+dateDir + "\\";
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
				//存储数据
				int i = 0;
				i = homePagePictureService.uploadImg(remarks,addressUrl,state,fileDirPath,effective,company_id);
				if(i!=1){
					jsonObject.put("msg","上传失败,存储错误!");
					jsonObject.put("result","fail");
					return jsonObject.toString();
				}
				//文件上传
				uploadImg_homePagePicture.transferTo(new File(fileDirPath));
				
				jsonObject.put("msg","上传成功!");
				jsonObject.put("result","success");
				int user_ids = Futil.getUserId(session, request);
				String ip = Futil.getIpAddr(request);
				String remark = "上传图片轮播||简介："+remarks+"&图片地址："+fileDirPath+"&跳转链接:"+addressUrl+"&是否有效："+effective;
				loggerMapper.addLogger(user_ids,ip,remark);
				
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return jsonObject.toString();
	}
	
	
	private boolean uploadFile_home(MultipartFile file, HttpSession session,
			HttpServletRequest request) throws Exception {
			// 清除上次上传进度信息
			try {
				String curProjectPath = session.getServletContext().getRealPath("/");
	          	String saveDirectoryPath = curProjectPath + "/" + uploadFolderName;
	          //	File saveDirectory = new File(saveZipPath);
	          	int p = curProjectPath.indexOf("webapps");
				String realpath = curProjectPath.substring(0, p + 8) +uploadFolderName + "\\";
				realpath = realpath.replaceAll("%20", " ");
				//判断是否存在
				File files=new File(realpath);
				if(!files.exists()){
					//生成文件夹
					files.mkdirs();
				}
	          	// 判断文件是否存在
	          	if (!file.isEmpty()) {
					String fileName = file.getOriginalFilename();
					String filePath = realpath+ fileName;
					String fileExtension = FilenameUtils.getExtension(fileName);
					if(!ArrayUtils.contains(extensionPermit, fileExtension)) {
						return false;
					}
						//下面的方法用于上传文件
					file.transferTo(new File(filePath));
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

	/**
	 * 修改轮播图
	 * @param uploadImg_homePagePicture
	 * @param uploadFile_homePagePicture
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/uploadImgEdit") 
	@ResponseBody
	public String uploadImgEdit( MultipartFile uploadImg_homePagePicture_edit,MultipartFile uploadFile_homePagePicture_edit,HttpSession session,HttpServletRequest request) throws   Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","上传失败");
		jsonObject.put("result","fail");
		long fileHomeSize = uploadFile_homePagePicture_edit.getSize();
		if(fileHomeSize>0){
			if(!uploadFile_home(uploadFile_homePagePicture_edit,session,request)){
				//不是图片类型
				jsonObject.put("msg","压缩文件上传失败，需要的格式是zip或rar!");
				jsonObject.put("result","fail");
				return jsonObject.toString();
			}
		}
		
		String curProjectPath = session.getServletContext().getRealPath("/");
      	int ps = curProjectPath.indexOf("webapps");
		String realpaths = curProjectPath.substring(0, ps + 8) +uploadFolderName + "\\";
		realpaths = realpaths.replaceAll("%20", " ");
		String remarks = request.getParameter("remarks_homePagePicture_edit");
		String effective = request.getParameter("effective_homePagePicture_edit");
		String state = request.getParameter("state_homePagePicture_edit");
		String addressUrl = request.getParameter("adUrl_homePagePicture_edit");
		String company_id = request.getParameter("homePagePicture_company_id");
		if(fileHomeSize>0){
			if(state.equals("0")){
				String a[] = uploadFile_homePagePicture_edit.getOriginalFilename().split("\\.");
				String saveUnZipPath=a[0];
				addressUrl = realpaths+"/projects/"+saveUnZipPath+"/index.html";
			}
		}
		
		String id = request.getParameter("homePagePicture_id");
		String oldPicurl = request.getParameter("homePagePicture_picurl");
		HomePagePicture homePagePicture = new HomePagePicture();
		homePagePicture = homePagePictureService.findHomePagePictureByID(id);
		if(uploadImg_homePagePicture_edit==null||StringUtils.isBlank(uploadImg_homePagePicture_edit.getOriginalFilename())){
			int i = 0;
			i = homePagePictureService.updateuploadNotImg(remarks,state,addressUrl,effective,id);
			if(i!=1){
				jsonObject.put("msg","上传失败,存储错误!");
				jsonObject.put("result","fail");
				return jsonObject.toString();
			}
			jsonObject.put("msg","上传成功!");
			jsonObject.put("result","success");
			int user_ids = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remark = "修改轮播图||简介："+remarks+"&跳转链接:"+addressUrl+"&是否有效："+effective+"\\原有数据:"+net.sf.json.JSONObject.fromObject(homePagePicture).toString();
			loggerMapper.addLogger(user_ids,ip,remark);
		}else{
			String fileName=uploadImg_homePagePicture_edit.getOriginalFilename();
			//不是图片
			if(!fileName.matches("^.*(png|jpg|gif|jpge)$")){
				//不是图片类型
				jsonObject.put("msg","图片格式错误!");
				jsonObject.put("result","fail");
				return jsonObject.toString();
			}
			//2.判断是否为恶意程序
					try {
						BufferedImage bufferedImage=
								ImageIO.read(uploadImg_homePagePicture_edit.getInputStream());
						//2.1获取宽高
						int height=bufferedImage.getHeight();
						int width=bufferedImage.getWidth();
						double scale = height/width;
						int heightS = height*16;
						int widthS = width*9;
						if(width<720||heightS!=widthS){
							//表示不是图片
							jsonObject.put("msg","图片尺寸错误!高宽比例16:9，宽最少720!");
							jsonObject.put("result","fail");
							return jsonObject.toString();
						}
						 long fileSize = uploadImg_homePagePicture_edit.getSize();
						 if (fileSize <= 0) {
							 	jsonObject.put("msg","图片不能为空!");
								jsonObject.put("result","fail");
								return jsonObject.toString();
					        } else if (fileSize > (1024 * 1024)) {
					        	jsonObject.put("msg","图片不能大于1M");
								jsonObject.put("result","fail");
								return jsonObject.toString();
					            
					        }
					//3.由于文件个数多,采用分文件存储
					String dateDir=
							new SimpleDateFormat("yyyy/MM/dd")
									.format(new Date());
				
					//生成对应的文件夹
					String path = request.getSession().getServletContext().getRealPath(""); // 获取项目地址
					int p = path.indexOf("webapps");
					String realpath = path.substring(0, p + 8) +filePath+dateDir + "\\";
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
					//存储数据
					int i = 0;
					i = homePagePictureService.updateuploadImg(remarks,state,addressUrl,effective,id,fileDirPath);
					if(i!=1){
						jsonObject.put("msg","上传失败,存储错误!");
						jsonObject.put("result","fail");
						return jsonObject.toString();
					}
					//文件上传
					uploadImg_homePagePicture_edit.transferTo(new File(fileDirPath));
					
					if (oldPicurl != "" && oldPicurl != null) {
						File delfile = new File(oldPicurl);
						// 路径为文件且不为空则进行删除
						if (delfile.isFile() && delfile.exists()) {
							delfile.delete();
							System.out.println("删除旧广告图片成功");
						}
					}
					jsonObject.put("msg","上传成功!");
					jsonObject.put("result","success");
					int user_ids = Futil.getUserId(session, request);
					String ip = Futil.getIpAddr(request);
					String remark = "修改广告||简介："+remarks+"&图片地址："+fileDirPath+"&跳转链接:"+addressUrl+"&是否有效："+effective+"\\原有数据:"+net.sf.json.JSONObject.fromObject(homePagePicture).toString();
					loggerMapper.addLogger(user_ids,ip,remark);
					
					
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return jsonObject.toString();
	}
}
