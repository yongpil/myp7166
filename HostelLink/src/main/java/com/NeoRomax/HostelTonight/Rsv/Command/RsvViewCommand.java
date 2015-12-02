package com.NeoRomax.HostelTonight.Rsv.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.ui.Model;

import com.NeoRomax.HostelTonight.HostelList.Command.HostelListCommand;
import com.NeoRomax.HostelTonight.HostelList.Dao.HostelDao;
import com.NeoRomax.HostelTonight.HostelList.Dao.RoomsDao;
import com.NeoRomax.HostelTonight.HostelList.Dto.HostelDto;
import com.NeoRomax.HostelTonight.HostelList.Dto.RoomsDto;
import com.NeoRomax.HostelTonight.HostelList.Dto.SessionDto;
import com.NeoRomax.HostelTonight.Rsv.Dao.RsvDao;
import com.NeoRomax.HostelTonight.Rsv.Dto.RsvAvailableDto;
import com.NeoRomax.HostelTonight.Rsv.Dto.RsvConfirmDto;
import com.NeoRomax.HostelTonight.Rsv.Dto.RsvRoomListDto;
import com.NeoRomax.HostelTonight.util.Constant;

/**
 * <PRE>
 * 1. FileName  : RsvViewCommand.java
 * 2. Package  : com.NeoRomax.HostelTonight.Rsv.Command
 * 3. Comment  : 
 * 4. 작성자   : "Yong Pil Moon"
 * 5. 작성일   : 2015. 11. 20. 오후 3:45:34
 * </PRE>
 */ 


public class RsvViewCommand implements HostelListCommand {
	SqlSession sqlSession = null;
public RsvViewCommand() {
	sqlSession = Constant.sqlSession;
}
	@Override
	public void execute(Model model) {
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		HostelDao hDao = sqlSession.getMapper(HostelDao.class);
		RoomsDao roomsDao = sqlSession.getMapper(RoomsDao.class);
		RsvDao rsvDao = sqlSession.getMapper(RsvDao.class);
		
		System.out.println(request.getParameter("hostelNum"));
		
		List<RoomsDto> roomList = new ArrayList<RoomsDto>();
		roomList = roomsDao.RoomsList(Integer.parseInt(request.getParameter("hostelNum")));
		System.out.println(request.getParameter("hostelNum"));
		List<RsvAvailableDto> rsvAbleDto = rsvDao.rsvList((Integer.parseInt(request.getParameter("hostelNum"))),"20150915","20150920");
		
	 	ArrayList<ArrayList<String>> rsvDatesList = new ArrayList<ArrayList<String>>();
	 	ArrayList<ArrayList<String>> rsvRatesList = new ArrayList<ArrayList<String>>();
	 	
	 	
	 	
	 		
		for(int i=0;i<roomList.size();i++)
		{
			ArrayList<String> rsvDates =  new ArrayList<String>();
			ArrayList<String> rsvRates = new ArrayList<String>();
			
			for(int j=0;j<rsvAbleDto.size();j++)
			{   
				if(request.getParameter("check"+i+"-"+j) != null)
				{
					String[] tempRsv = request.getParameter("check"+i+"-"+j).split(",");//,로 구분 되어 있는 rsvRate와 rsvDate를 분리 한다.
					rsvDates.add(tempRsv[0]);
					rsvRates.add(tempRsv[1]);
				}
			}
			
			if(rsvDates!=null)
				rsvDatesList.add(rsvDates);
			if(rsvRates!=null)
				rsvRatesList.add(rsvRates);
		}
		
		SessionDto sessionDto = new SessionDto(rsvDatesList,rsvRatesList,
				hDao.getHDto(Integer.parseInt(request.getParameter("hostelNum"))),roomList);

	 	model.addAttribute("sessionDto", sessionDto);
	 	
	 	
	}

}
