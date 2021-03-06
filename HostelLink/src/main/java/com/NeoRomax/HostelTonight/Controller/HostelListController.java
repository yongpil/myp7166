package com.NeoRomax.HostelTonight.Controller;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NeoRomax.HostelTonight.Command.HCommand;
import com.NeoRomax.HostelTonight.Command.HostelListAddCommand;
import com.NeoRomax.HostelTonight.Command.HostelListDetailCommand;
import com.NeoRomax.HostelTonight.Command.HostelListViewCommand;
import com.NeoRomax.HostelTonight.Command.RsvCommand;
import com.NeoRomax.HostelTonight.Command.RsvConfirmViewCommand;
import com.NeoRomax.HostelTonight.Command.RsvViewCommand;
import com.NeoRomax.HostelTonight.Dto.RsvAddDto;
import com.NeoRomax.HostelTonight.Dto.RsvSessionDto;
import com.NeoRomax.HostelTonight.Service.HostelService;

import util.Constant;

/**
 * <PRE>
 * 1. FileName  : HostelListController.java
 * 2. Package  : com.NeoRomax.HostelTonight.HostelList.Controller
 * 3. Comment  : 호스텔에 관련된 기능을 제어하는 컨트롤러, 들어온 요청에 따라 각 command클래스를 수행 시킨다.
 * 4. 작성자   : "Yong Pil Moon"
 * 5. 작성일   : 2015. 11. 20. 오후 3:44:40
 * </PRE>
 */ 


@Controller
@SessionAttributes("sessionDto")//예약을 위한 sessionDto 
public class HostelListController {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(HostelListController.class);

	@Resource(name="hostelService")
	private HostelService hostelService;
    
	
	HCommand command = null;
	public SqlSession sqlSession;


	@Autowired
	public void setSqlSession(SqlSession sqlSession){
		this.sqlSession = sqlSession;
		Constant.sqlSession = this.sqlSession;
	}

	
	@RequestMapping(value="/review")//이용후기
	public ModelAndView review(@RequestParam("hstNum") int hstNum) throws Exception{
		ModelAndView mv = new ModelAndView("/Hostels/hostel_review");
		List<Map<String,Object>> reviewList = hostelService.selectReviewList(hstNum);
		mv.addObject("reviewList",reviewList);
		return mv;
	}
	
	@RequestMapping(value="/index", method={RequestMethod.POST,RequestMethod.GET})//index 페이지
	public String list(@RequestParam(value="lctSearch", required=false, defaultValue="seoul") String lctSearch,
			@RequestParam(value="dayFrom", required=false, defaultValue="") String dayFrom,@RequestParam(value="dayTo", required=false, defaultValue="") String dayTo,Model model) {
		
		logger.info("index()"); //로그 생성
		logger.info(lctSearch);
		if(dayFrom.equals("") && dayTo.equals(""))//만약 사용자에 의해 날짜가 입력되지 않으면 현재 날짜를 default로 넣어준다.
		{
			Date defaultDayFrom = new Date();
			SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
			dayFrom = dayFormat.format(defaultDayFrom);
			Calendar cal = Calendar.getInstance(); //Calendar를 이용하여 3일 후 날짜 구하기
			cal.setTime ( defaultDayFrom );
			cal.add(Calendar.DATE, 3);
			dayTo = dayFormat.format(cal.getTime());
		}
		logger.info(dayFrom);
		logger.info(dayTo);
		model.addAttribute("lctSearch",lctSearch);
		model.addAttribute("dayFrom",dayFrom);
		model.addAttribute("dayTo",dayTo);
		command = new HostelListViewCommand();
		command.execute(model);
		return "/Hostels/hostel_index";	
	}
	
	@RequestMapping("/addHostel_view") //호스텔 추가 페이지
	public String addHostel_view(Model model) {
		return "/Hostels/hostel_add_view";
	}
	
	@RequestMapping("/addHostel")//호스텔 추가 기능 수행
	public String addHostel(MultipartHttpServletRequest mRequest, Model model) {
		System.out.println("addHostel()");
		model.addAttribute("mRequest", mRequest);
		command = new HostelListAddCommand();
		command.execute(model);
		return "redirect:index";
	}
	
	@RequestMapping("/hostel_detail") //호스텔 상세 정보 페이지
	public String hostel_detail(HttpServletRequest request, Model model) {
		System.out.println("hostel_detail()");
		model.addAttribute("request",request);
		
		command = new HostelListDetailCommand();
		command.execute(model);
		return "/Hostels/hostel_detail";
	}
	
	@RequestMapping("/rsvView") //예약 정보 확인 페이지
	public String rsvView(HttpServletRequest request, Model model) {
		System.out.println("rsvView()");
		model.addAttribute("request",request);
		
		command = new RsvViewCommand();
		command.execute(model);
		System.out.println("rsvView() finished");
		return "/Hostels/hostel_rsv_view";
	}
	
	@RequestMapping("/rsvConfirm") //예약 확정 페이지
	public String rsvConfirm(RedirectAttributes redirectAttributes, @ModelAttribute RsvSessionDto sessionDto, SessionStatus sessionStatus, HttpServletRequest request, Model model) {
		System.out.println("rsvConfirm()");
		model.addAttribute("request",request);
			command = new RsvCommand();
			command.execute(model);
			Map<String, Object> map = model.asMap();
			RsvAddDto rsvAddDto = (RsvAddDto) map.get("rsvAddDto");
			redirectAttributes.addAttribute("rsvNum",rsvAddDto.getRsvNum());
			return "redirect:rsvConfirmView.html";
	}
	
	@RequestMapping("/rsvConfirmView") //새로고침시 중복 예약을 방지하기 위해 redirect 시킨 후 db에서 예약된 내용을 다시 가져 온다.
	public String rsvConfirmView(@RequestParam("rsvNum") int rsvNum, @ModelAttribute RsvSessionDto sessionDto, SessionStatus sessionStatus, Model model) {
		System.out.println("rsvConfirmView()");
		model.addAttribute("rsvNum",rsvNum);
		
		command = new RsvConfirmViewCommand();
		command.execute(model);
		
		sessionStatus.setComplete();//세션 파기
			
			return "/Hostels/hostel_rsv_confirm";
	}
}
