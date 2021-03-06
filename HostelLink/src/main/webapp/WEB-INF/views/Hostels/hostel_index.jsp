<%
/* =================================================================
 * 
 * 작성일 : 2015. 11. 20.
 *  
 * 작성자 : 문용필
 * 
 * 상세설명 : 사이트 시작 페이지
 *   
 */ 
%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="s" %>
<%@ page import="java.util.*"%>
<%@ page session="false" %>
<%
	String realFolder="";
	realFolder="./HostelsUpload/";
	String dayfrom = ((String)request.getAttribute("dayfrom"));
	String dayto = ((String)request.getAttribute("dayto"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/hostels/hstList.css">

<title>Insert title here</title>
</head>
	<meta charset="utf-8" name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0"/>
<body>
	<div id="main">
		<!--Header-->
		<jsp:include page="../header.jsp"></jsp:include>
		<!--Search-->
				<jsp:include page="../search.jsp" />
			<!-- 컨텐츠 -->
			<div id="content">
				<!--검색내용란  -->
				<div id=searchContent1>
<%-- 				<%=dayfrom %>~<%=dayto %> --%>
				</div>
				<div id=searchContent2>
				검색결과2
				</div>
				<!-- 버튼 -->
				<div id="buttonGroup">
				<a id="filterBtn" class="myButton">필터</a>
				<a id="DetailSearchBtn" class="myButton">상세검색</a>
				<a class="myButton">지도</a>
				</div>

				<div id="filter">안녕하세요</div> 
				<div id="DetailSearch">반갑습니다</div> 
				
				
				
				<!-- 호스텔 리스트 -->
				<div id="hostelsGallery">
				<c:forEach items="${hDtos}" var="hDto">
					<li>
					    <a href="./hostel_detail.html?hstNum=${hDto.hostelNum}&dayFrom=${dayFrom}&dayTo=${dayTo}"> 						
						<div  id="HostelsPic" style="background:url(<%=realFolder%>${hDto.imageName}) no-repeat; background-size:100%">
						<div id=price>13,000 Won</div>
						</div>
						<div id="HostelsName">${hDto.hostelName}</div>
						<span class="text">
						  <%-- <span class="title"><%=hos.getHostelsName()%></span> --%>
						</span>	
						</a>
					</li>
					<!--Footer-->
						<%-- <jsp:include page="../Main/footer.jsp" /> --%>
				</c:forEach> 
				</div>
			</div>
			<a href="addHostel_view.html">호스텔 추가</a>
			
					
		</div>	
		

		
<script>
 $('#filter').hide();
  $('#filterBtn').click(function(){
	   if($("#DetailSearch").css("display")=="block") 
	  $("#DetailSearch").slideUp("fast");
	  $("#filter").slideToggle("fast"); 
 }); 
  
 $('#DetailSearch').hide();
  $('#DetailSearchBtn').click(function(){
	  if($("#filter").css("display")=="block") 
	$("#filter").slideUp("fast");
	 $("#DetailSearch").slideToggle("fast");
 }); 
  

 var today = new Date();
 var year = today.getFullYear();
 var month = today.getMonth()+1;
 var today = today.getDate();
 var tomorrow = today + 1;
 
 if(<%=dayfrom%>==year-month-today && <%=dayto%>==year-month-today )
		{
			$('#searchContent1').hide();
			$('#searchContent2').show();
		}
		else if(<%=dayfrom%>==year-month-tomorrow || <%=dayto%>==year-month-tomorrow)
			{
				$('#searchContent1').hide();
				$('#searchContent2').show();
			}
		else
			{
				$('#searchContent1').show();
				$('#searchContent2').hide();
			} 
</script>
</body>
</html>