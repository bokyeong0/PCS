<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
  /**
  * @Class Name : orderList.jsp
  * @Description : order List 화면
  * @Modification Information
  *
  *   수정일         수정자                   수정내용
  *  -------    --------    ---------------------------
  *  2016.07.18            최초 생성
  *10qpalzm
  * author 실행환경 개발팀
  * since 2016.07.18
  *
  * Copyright (C) 2009 by MOPAS  All right reserved.
  */
  
%>
	<nav class="navbar navbar-default navbar-static-top">
	  <div class="container">
		<ul class="nav navbar-nav">
			<li class="<c:out value='${pageId=="pickOrderList"?"active":""}'/>"><a href="/pickOrderList.do">피킹지시</a></li>
			<%-- <li class="<c:out value='${pageId=="pickList"?"active":""}'/>"><a href="/pickList.do">피킹리스트</a></li> --%>
			<li class="<c:out value='${pageId=="pickStateList"?"active":""}'/>"><a href="/pickStateList.do">피킹현황조회</a></li>
			<li class="<c:out value='${pageId=="changeStateList"?"active":""}'/>"><a href="/changeStateList.do">상품대체현황조회</a></li>
			<li class="<c:out value='${pageId=="reasonStateList"?"active":""}'/>"><a href="/reasonStateList.do">상품결품현황조회</a></li>
			<li class="<c:out value='${pageId=="printStateList"?"active":""}'/>"><a href="/printStateList.do">주문진행현황조회</a></li>
			<li class="<c:out value='${pageId=="comCodeList"?"active":""}'/>"><a href="/comCodeList.do">공통코드관리</a></li>
			<!-- <li><a href="" class="jobState">작업현황판</a></li> -->
		</ul>
	  </div>
	</nav>
	<script>
	$(function(){
		// 작업현황판 새창에서 열기
		$('.jobState').on('click',function(e){
			e.preventDefault();
			var url = "/tv.do";
			window.open(url);
		});
	});
	
	</script>
