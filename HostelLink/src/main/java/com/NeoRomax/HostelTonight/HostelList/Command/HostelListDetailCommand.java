package com.NeoRomax.HostelTonight.HostelList.Command;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.ibatis.session.SqlSession;
import org.springframework.ui.Model;

import com.NeoRomax.HostelTonight.HostelList.Dao.HostelDao;
import com.NeoRomax.HostelTonight.HostelList.Dao.ImgDao;
import com.NeoRomax.HostelTonight.HostelList.Dao.RoomsDao;
import com.NeoRomax.HostelTonight.HostelList.Dto.RoomsDto;
import com.NeoRomax.HostelTonight.Rsv.Dao.RsvDao;
import com.NeoRomax.HostelTonight.Rsv.Dto.RsvAvailableDto;
import com.NeoRomax.HostelTonight.Rsv.Dto.RsvRoomListDto;
import com.NeoRomax.HostelTonight.util.Constant;

/**
 * <PRE>
 * 1. FileName  : HostelListDetailCommand.java
 * 2. Package  : com.NeoRomax.HostelTonight.HostelList.Command
 * 3. Comment  : room개수만큼의 RsvRoomListDto의 객체를 만들고 rsvRoomListDtos에 List로 담아 hestel_detail.jsp에 전송 한다.
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
		ImgDao hImgDao = sqlSession.getMapper(ImgDao.class);
		RoomsDao roomsDao = sqlSession.getMapper(RoomsDao.class);
		RsvDao rsvDao = sqlSession.getMapper(RsvDao.class);
		
		ArrayList<RoomsDto> hostelListRoomDtos = (ArrayList<RoomsDto>)roomsDao.RoomsList(Integer.parseInt(request.getParameter("hstNum")));//해당 hostel의 room정보가 들어 있는 List
		System.out.println(request.getParameter("dayFrom"));
		System.out.println(request.getParameter("dayTo"));
		ArrayList<RsvAvailableDto> rsvAvailableDtos = (ArrayList<RsvAvailableDto>)rsvDao.rsvAvailList((Integer.parseInt(request.getParameter("hstNum"))),request.getParameter("dayFrom"),request.getParameter("dayTo"));
		ArrayList<RsvRoomListDto> rsvRoomListDtos = new ArrayList<RsvRoomListDto>();//hostel_detail.jsp에서 jstl로 화면에 편리하게 뿌리기위해 만든 DTO

		model.addAttribute("hDto",hDao.getHDto(Integer.parseInt(request.getParameter("hstNum"))));
		model.addAttribute("hImgDtos",hImgDao.getHImgList(Integer.parseInt(request.getParameter("hstNum"))));
		model.addAttribute("roomsDtos",hostelListRoomDtos);
		model.addAttribute("RsvAbleDto",rsvAvailableDtos);
		
		
		
		for(int i=0;i<rsvAvailableDtos.size();i++)
		{
			System.out.println(rsvAvailableDtos.get(i).getRsvDate());
		}

		for(int i=0;i<hostelListRoomDtos.size();i++)
		{
			RsvRoomListDto rsvRoomListDto = new RsvRoomListDto();
			RoomsDto hostelListRoomsDto = hostelListRoomDtos.get(i);
		
			rsvRoomListDto.setRoomsNum(hostelListRoomsDto.getRoomsNum());
			rsvRoomListDto.setRoomsName(hostelListRoomsDto.getRoomsName());
			rsvRoomListDto.setRoomsInfo(hostelListRoomsDto.getRoomsInfo());
			ArrayList<RsvAvailableDto> rsvRoomCheckDtos = new ArrayList<RsvAvailableDto>();
			
			
			for(int j=0;j<rsvAvailableDtos.size();j++) //방번호에 따라서 rsvAvailableDto를 분류한다.
			{   
				if(hostelListRoomDtos.get(i).getRoomsNum()==rsvAvailableDtos.get(j).getRsvRoom())
				{
					rsvRoomCheckDtos.add(rsvAvailableDtos.get(j));		
				}
			}
			rsvRoomListDto.settRsvAvailableDtos(rsvRoomCheckDtos);
			rsvRoomListDtos.add(rsvRoomListDto);
		}
		
		model.addAttribute("rsvRoomListDtos",rsvRoomListDtos);
	}
		
	}

