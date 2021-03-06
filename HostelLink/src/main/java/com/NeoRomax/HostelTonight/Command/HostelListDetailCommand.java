package com.NeoRomax.HostelTonight.Command;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.ui.Model;

import com.NeoRomax.HostelTonight.Dao.HostelDao;
import com.NeoRomax.HostelTonight.Dao.RoomsDao;
import com.NeoRomax.HostelTonight.Dao.RsvDao;
import com.NeoRomax.HostelTonight.Dto.RoomsDto;
import com.NeoRomax.HostelTonight.Dto.RsvAvailableDto;
import com.NeoRomax.HostelTonight.Dto.RsvListDto;

import util.Constant;


/**
 * <PRE>
 * 1. FileName  : HostelListDetailCommand.java
 * 2. Package  : com.NeoRomax.HostelTonight.HostelList.Command
 * 3. Comment  : 호스텔을 클릭했을 때 상세 정보를 보기 위한 클래스, 호스텔 정보 및  예약 가능한 객실 현황을 보여준다.
 * 4. 작성자   : "Yong Pil Moon"
 * 5. 작성일   : 2015. 11. 20. 오후 3:44:57
 * </PRE>
 */ 


public class HostelListDetailCommand implements HCommand {
	SqlSession sqlSession = null;
	DateFormat outputFormattoer = new SimpleDateFormat("MM/dd/yyyy");
	public HostelListDetailCommand() {
		sqlSession = Constant.sqlSession;
	}

	@Override
	public void execute(Model model) {
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		HostelDao hDao = sqlSession.getMapper(HostelDao.class);
		RoomsDao roomsDao = sqlSession.getMapper(RoomsDao.class);
		RsvDao rsvDao = sqlSession.getMapper(RsvDao.class);
		
		ArrayList<RoomsDto> hostelListRoomDtos = (ArrayList<RoomsDto>)roomsDao.RoomsList(Integer.parseInt(request.getParameter("hstNum")));//해당 hostel의 room정보가 들어 있는 List를 가져온다.
		ArrayList<RsvAvailableDto> rsvAvailableDtos = (ArrayList<RsvAvailableDto>)rsvDao.rsvAvailList((Integer.parseInt(request.getParameter("hstNum"))),request.getParameter("dayFrom"),request.getParameter("dayTo"));//예약가능한 날짜 리스트를 가져온다.
		ArrayList<RsvListDto> RsvListDtos = new ArrayList<RsvListDto>();//hostel_detail.jsp에서 jstl로 화면에 편리하게 뿌리기위해 만든 DTO
		

		for(int i=0;i<hostelListRoomDtos.size();i++)
		{
			RsvListDto rsvListDto = new RsvListDto();
			RoomsDto hostelListRoomsDto = hostelListRoomDtos.get(i);
		
			rsvListDto.setRoomsNum(hostelListRoomsDto.getRoomsNum());
			rsvListDto.setRoomsName(hostelListRoomsDto.getRoomsName());
			rsvListDto.setRoomsInfo(hostelListRoomsDto.getRoomsInfo());
			ArrayList<RsvAvailableDto> rsvRoomCheckDtos = new ArrayList<RsvAvailableDto>();
			
			
			for(int j=0;j<rsvAvailableDtos.size();j++) //방번호에 따라서 rsvAvailableDto를 분류한다.
			{   
				if(hostelListRoomDtos.get(i).getRoomsNum()==rsvAvailableDtos.get(j).getRsvRoom())
				{
					rsvRoomCheckDtos.add(rsvAvailableDtos.get(j));		
				}
			}
			rsvListDto.settRsvAvailableDtos(rsvRoomCheckDtos);
			RsvListDtos.add(rsvListDto);
		}
		
		//admin페이지에 필요한 정보를 model을 통해 전송
		model.addAttribute("hDto",hDao.getHDto(Integer.parseInt(request.getParameter("hstNum"))));
		model.addAttribute("hImgDtos",hDao.getHImgList(Integer.parseInt(request.getParameter("hstNum"))));
		model.addAttribute("roomsDtos",hostelListRoomDtos);
		model.addAttribute("RsvAbleDto",rsvAvailableDtos);
		model.addAttribute("dayFrom",request.getParameter("dayFrom"));
		model.addAttribute("dayTo",request.getParameter("dayTo"));
		
		model.addAttribute("rsvListDtos",RsvListDtos);
	}
		
	}

