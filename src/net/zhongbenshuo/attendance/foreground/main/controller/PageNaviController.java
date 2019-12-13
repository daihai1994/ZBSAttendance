package net.zhongbenshuo.attendance.foreground.main.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageNaviController {

	/**
	 * 版本信息
	 * 
	 * @param session
	 * @param init
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/versionView")
	public ModelAndView versionView(HttpSession session, String init) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("version/versionView");
		return modelAndView;
	}
	/***
	 * 账号信息
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/accountInformationView")
	public ModelAndView accountInformationView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accountInformation/accountInformationView");
		return modelAndView;
	}
	/***
	 * 公司信息
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/companyView")
	public ModelAndView companyView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("company/companyView");
		return modelAndView;
	}
	
	/***
	 * 考勤规则
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ruleView")
	public ModelAndView ruleView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("rule/ruleView");
		return modelAndView;
	}
	
	/***
	 * 日志信息
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/loggerView")
	public ModelAndView loggerView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("logger/loggerView");
		return modelAndView;
	}
	
	/***
	 * 配置信息
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/configInforView")
	public ModelAndView configInforView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("configInfor/configInforView");
		return modelAndView;
	}
	/***
	 * 广告配置
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/advertisementView")
	public ModelAndView advertisementView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("advertisement/advertisementView");
		return modelAndView;
	}
	
	/***
	 * 外勤打卡审批
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/outAttendanceView")
	public ModelAndView outAttendanceView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("outAttendance/outAttendanceView");
		return modelAndView;
	}
	/**
	 * 申请记录
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/applyRecordView")
	public ModelAndView applyRecordView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("applyRecord/applyRecordView");
		return modelAndView;
	}
	/**
	 * 考勤记录查询
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/attendanceRecordView")
	public ModelAndView attendanceRecordView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("attendanceRecord/attendanceRecordView");
		return modelAndView;
	}
	/**
	 * 假期设置
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/holidaySettingsView")
	public ModelAndView holidaySettingsView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("holidaySettings/holidaySettingsView");
		return modelAndView;
	}
	/**
	 * 补卡审批
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/appealAttendanceView")
	public ModelAndView appealAttendanceView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("appealAttendance/appealAttendanceView");
		return modelAndView;
	}
	/**
	 * 加班审批
	 */
	@RequestMapping("/overTimeView")
	public ModelAndView overTimeView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("overTime/overTimeView");
		return modelAndView;
	}
	/**
	 * 假期审批
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/vacationView")
	public ModelAndView vacationView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("vacation/vacationView");
		return modelAndView;
	}
	/**
	 * 外出审批
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/outGoingView")
	public ModelAndView outGoingView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("outGoing/outGoingView");
		return modelAndView;
	}
	/**
	 * 出差审批
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/businessTravelView")
	public ModelAndView businessTravelView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("businessTravel/businessTravelView");
		return modelAndView;
	}
	
	/**
	 * 安卓轮播图设置
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/homePagePictureView")
	public ModelAndView homePagePictureView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("homePagePicture/homePagePictureView");
		return modelAndView;
	}
	/**
	 * 公告制度设置
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/announcementView")
	public ModelAndView announcementView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("announcement/announcementView");
		return modelAndView;
	}
	
	
	/**
	 * 工资信息
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/wageView")
	public ModelAndView wageView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("wage/wageView");
		return modelAndView;
	}
	
	
	/**
	 * 预约审批
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/visitorSubscribeView")
	public ModelAndView visitorSubscribeView(HttpSession session) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("visitorSubscribe/visitorSubscribeView");
		return modelAndView;
	}
}
