package net.zhongbenshuo.attendance.foreground.advertisement.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import net.zhongbenshuo.attendance.foreground.advertisement.bean.Advertisement;
import net.zhongbenshuo.attendance.foreground.advertisement.service.AdvertisementService;
import net.zhongbenshuo.attendance.mapper.LoggerMapper;
import net.zhongbenshuo.attendance.utils.Futil;

@Controller
@RequestMapping(value = "/AdvertisementController", produces = "text/html;charset=UTF-8")
public class AdvertisementController {
	
	public static final String filePath = "Advertisement";// 广告图片地址
	@Autowired
	AdvertisementService advertisementService;
	
	@Autowired
	LoggerMapper loggerMapper;
	
	/***
	 * 查询广告
	 * @param bt
	 * @param et
	 * @param name
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findAdvertisement.do")
	@ResponseBody
	public String findAdvertisement(String bt,String et,String name,int bNum,int rows,HttpSession session,HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = advertisementService.findAdvertisement(bNum,rows,bt,et,name);
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
	public String uploadImg( @RequestParam(value = "uploadImg") MultipartFile uploadImg,HttpSession session,HttpServletRequest request) throws   Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","上传失败");
		jsonObject.put("result","fail");
		if(uploadImg==null){
			jsonObject.put("msg","图片不能为空!");
			jsonObject.put("result","fail");
			return jsonObject.toString();
		}
		String fileName=uploadImg.getOriginalFilename();
		//不是图片
		if(!fileName.matches("^.*(png|jpg|gif|jpeg)$")){
			//不是图片类型
			jsonObject.put("msg","图片格式错误!");
			jsonObject.put("result","fail");
			return jsonObject.toString();
		}
		String name = request.getParameter("name_advertisement_add");
		String showTime = request.getParameter("showTime_advertisement_add");
		String adUrl = request.getParameter("adUrl_advertisement_add");
		String start_time = request.getParameter("start_time_advertisement_add")+" 00:00:00";
		String end_time = request.getParameter("end_time_advertisement_add")+" 23:59:59";
		String effective = request.getParameter("effective_advertisement_add");
		String force = request.getParameter("force_advertisement_add");
		//2.判断是否为恶意程序
				try {
					BufferedImage bufferedImage=
							ImageIO.read(uploadImg.getInputStream());
					//2.1获取宽高
					int height=bufferedImage.getHeight();
					int width=bufferedImage.getWidth();
					double scale = height/width;
					if(width<720||scale<1||scale>2){
						//表示不是图片
						jsonObject.put("msg","图片尺寸错误!高宽比例在1和2之间，宽最少720!");
						jsonObject.put("result","fail");
						return jsonObject.toString();
					}
					 long fileSize = uploadImg.getSize();
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
				i = advertisementService.uploadImg(name,showTime,adUrl,start_time,end_time,fileDirPath,effective,force);
				if(i!=1){
					jsonObject.put("msg","上传失败,存储错误!");
					jsonObject.put("result","fail");
					return jsonObject.toString();
				}
				//文件上传
				uploadImg.transferTo(new File(fileDirPath));
				
				jsonObject.put("msg","上传成功!");
				jsonObject.put("result","success");
				int user_ids = Futil.getUserId(session, request);
				String ip = Futil.getIpAddr(request);
				String remarks = "上传广告||简介："+name+"&图片地址："+fileDirPath+"&跳转链接:"+adUrl+"&显示时间："+showTime+"秒&开始时间："+start_time+"&结束时间："+end_time
						+"&是否有效："+effective+"&是否强制："+force;
				loggerMapper.addLogger(user_ids,ip,remarks);
				
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return jsonObject.toString();
	}
	
	
	@RequestMapping(value="/uploadImgEdit") 
	@ResponseBody
	public String uploadImgEdit( @RequestParam(value = "uploadImgEdit") MultipartFile uploadImgEdit,HttpSession session,HttpServletRequest request) throws   Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","上传失败");
		jsonObject.put("result","fail");
		String name = request.getParameter("name_advertisement_edit");
		String showTime = request.getParameter("showTime_advertisement_edit");
		String adUrl = request.getParameter("adUrl_advertisement_edit");
		String start_time = request.getParameter("start_time_advertisement_edit")+" 00:00:00";
		String end_time = request.getParameter("end_time_advertisement_edit")+" 23:59:59";
		String effective = request.getParameter("effective_advertisement_edit");
		String force = request.getParameter("force_advertisement_edit");
		String id = request.getParameter("advertisement_id");
		String oldPicurl = request.getParameter("advertisement_picurl");
		Advertisement advertisement = new Advertisement();
		advertisement = advertisementService.findAdvertisementByID(id);
		if(uploadImgEdit==null||StringUtils.isBlank(uploadImgEdit.getOriginalFilename())){
			int i = 0;
			i = advertisementService.updateuploadNotImg(name,showTime,adUrl,start_time,end_time,effective,force,id);
			if(i!=1){
				jsonObject.put("msg","上传失败,存储错误!");
				jsonObject.put("result","fail");
				return jsonObject.toString();
			}
			jsonObject.put("msg","上传成功!");
			jsonObject.put("result","success");
			int user_ids = Futil.getUserId(session, request);
			String ip = Futil.getIpAddr(request);
			String remarks = "修改广告||简介："+name+"&跳转链接:"+adUrl+"&显示时间："+showTime+"秒&开始时间："+start_time+"&结束时间："+end_time
					+"&是否有效："+effective+"&是否强制："+force+"\\原有数据:"+net.sf.json.JSONObject.fromObject(advertisement).toString();
			loggerMapper.addLogger(user_ids,ip,remarks);
		}else{
			String fileName=uploadImgEdit.getOriginalFilename();
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
								ImageIO.read(uploadImgEdit.getInputStream());
						//2.1获取宽高
						int height=bufferedImage.getHeight();
						int width=bufferedImage.getWidth();
						double scale = height/width;
						if(width<720||scale<1||scale>2){
							//表示不是图片
							jsonObject.put("msg","图片尺寸错误!高宽比例在1和2之间，宽最少720!");
							jsonObject.put("result","fail");
							return jsonObject.toString();
						}
						 long fileSize = uploadImgEdit.getSize();
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
					i = advertisementService.updateuploadImg(name,showTime,adUrl,start_time,end_time,fileDirPath,effective,force,id);
					if(i!=1){
						jsonObject.put("msg","上传失败,存储错误!");
						jsonObject.put("result","fail");
						return jsonObject.toString();
					}
					//文件上传
					uploadImgEdit.transferTo(new File(fileDirPath));
					
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
					String remarks = "修改广告||简介："+name+"&图片地址："+fileDirPath+"&跳转链接:"+adUrl+"&显示时间："+showTime+"秒&开始时间："+start_time+"&结束时间："+end_time
							+"&是否有效："+effective+"&是否强制："+force+"\\原有数据:"+net.sf.json.JSONObject.fromObject(advertisement).toString();
					loggerMapper.addLogger(user_ids,ip,remarks);
					
					
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return jsonObject.toString();
	}
	
}
