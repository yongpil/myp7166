<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="s" %>
<%@ page import="java.util.*"%>
<%@ page session="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
<script src="dist/js/bootstrap.js"></script>
<link rel="stylesheet" href="dist/css/bootstrap.css">
<title>Insert title here</title>
</head>
	<meta charset="utf-8" name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0"/>
<body>
	<div id="main">
		<!--Header-->
		<jsp:include page="../header.jsp"></jsp:include>
			<!-- 컨텐츠 -->
			<div id="content">
				<div class="container">
				<h2>review 게시판</h2>
				  <table class="table">
				        <tr>
				        <th>번호</th>
				        <th>호스텔 번호</th>
				        <th>유저 번호</th>
				        <th>내용</th>
				        <th>날짜</th>
				     	</tr>
				  <c:forEach items="${reviewList}" var="rev">				  
                    <tr>
                        <td>${rev.reNum }</td>
                        <td>${rev.hstNum }</td>
                        <td>${rev.userNum }</td>
                        <td>${rev.reContent }</td>
                        <td>${rev.reDate }</td>
                    </tr>
                </c:forEach>
                </table>
				</div>
			</div>
		</div>	
		

		

</body>
</html>