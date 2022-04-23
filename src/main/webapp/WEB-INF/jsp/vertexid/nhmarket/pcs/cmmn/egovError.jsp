<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
  /**
  * @Class Name : egovError.jsp
  * @Description : 에러 메시지
  * @Modification Information
  *
  *   수정일         수정자                   수정내용
  *  -------    --------    ---------------------------
  *  2016.07.18            최초 생성
  *
  * author 실행환경 개발팀
  * since 2016.07.18
  *
  * Copyright (C) 2009 by MOPAS  All right reserved.
  */
%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>에러 페이지</title>
	
	<!-- plugin css -->    
    <link type="text/css" rel="stylesheet" href="<c:url value='/plugin/bootstrap/css/bootstrap.min.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.min.css'/>"/>
    
    <!-- plugin js -->
    <script src="<c:url value='/plugin/jquery-1.12.4.min.js'/>"></script>
    <script src="<c:url value='/plugin/bootstrap/js/bootstrap.js'/>"></script>
    <script src="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.js'/>"></script>
	
</head>

<body>
	<!-- container -->
	<div class="container">
		<br/><br/><br/>
		<div class="panel panel-danger">
			<div class="panel-heading">
				<span class="glyphicon glyphicon-remove"></span> &nbsp; 에러메시지
			</div>
			<div class="panel-body">
    			<c:out value="${resultErrorMsg}"/>
			</div>
		</div>	
    </div>
</body>
</html>