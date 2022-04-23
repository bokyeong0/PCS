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
    <title><spring:message code="title.nhmarket.pcs.main" /></title>

	<!-- plugin css -->    
    <link type="text/css" rel="stylesheet" href="<c:url value='/plugin/bootstrap/css/bootstrap.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/pcs/default.css'/>"/>
    
    <!-- plugin js -->
    <script src="<c:url value='/plugin/jquery-1.12.4.min.js'/>"></script>
    <script src="<c:url value='/plugin/bootstrap/js/bootstrap.js'/>"></script>
    <script src="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.js'/>"></script>
    
</head>

<body>

	<!-- 상단 메뉴  -->
	<nav class="navbar navbar-default navbar-static-top">
	  <div class="container">
		<ul class="nav navbar-nav">
			<li class="active"><a href="#">피킹지시</a></li>
			<li><a href="#">피킹현황조회</a></li>
			<li><a href="#">상품대체현황조회</a></li>
			<li><a href="#">작업현황판</a></li>
		</ul>
	  </div>
	</nav>
	<!-- /상단 메뉴  -->

	<!-- container -->
	<div class="container">
	
		<!-- 페이지명 -->
		<div class="page-header">
		  <h1>피킹지시 </h1>
		</div>
		<!-- /페이지명 -->
		
		<!-- 검색 영역 -->
		<div class="panel panel-default">
			<div class="panel-body">
 				<form:form commandName="searchVO" id="searchForm" name="searchForm" method="post">			
				<div class="form-inline">
				
					<form:input path="search_delivery_date" cssClass="form-control" placeholder="배송일자" size="10" maxlength="10" />
					
        			<form:select path="search_delivery_count" cssClass="form-control">
        				<form:option value="" label="배송차수(전체)" />

						<!-- 배송차수 List -->        				
        				<c:forEach var="result" items="${deliveryCountList}" varStatus="status">
        					<form:option value="${result.deliveryCount}" label="${result.deliveryCount}차" />
        				</c:forEach>
        				
       				</form:select>

        			<form:select path="search_zone_code" cssClass="form-control">
						<form:option data-delivery-count = "5" value="" label="존코드(전체)" />
						<!-- 존코드 List -->						
						<c:forEach var="result" items="${zoneCodeList}" varStatus="status">
							<form:option data-delivery-count = "1" value="${result.zoneCode}" label="${result.zoneCode}" />
						</c:forEach>
       				</form:select>
					
					<form:select path="search_state_code" cssClass="form-control">
						<form:option value="" label="상태(전체)" />
						<form:option value="0" label="0(예정)" />
						<form:option value="1" label="1(지시)" />
						<form:option value="2" label="2(피킹)" />
					</form:select>
					
					<form:input path="search_org_order_no" cssClass="form-control" placeholder="원주문번호"/>					
					
					<button type="button" class="btn btn-primary btnSearch">검색</button>
				</div>
				</form:form>
				
				
 				
				<!-- 처리버튼 영역 -->
				<div class="button-group pull-right">
					<button type="button" class="btn btn-default" data-toggle="modal" data-target="#importData">주문 업로드</button>
					<button type="button" class="btn btn-default btnPicking">피킹지시</button>
					<button type="button" class="btn btn-default btnLabelPrint">라벨출력</button>
				</div>
				
			</div>
		</div>
		<!-- /검색 영역 -->
		
		<!-- 데이타 List -->
		<div class="panel panel-default">
			<div class="panel-body">
			<form:form commandName="orderVO" id="listForm" name="listForm" method="post">
				<!-- Table -->
				<table class="table table-condensed" >
					<thead>
						<tr>
							<td><input type="checkbox" id="allCheckToggle" class="form-control"/></td>
							<td>배송일자</td>
							<td>배송차수</td>
							<td>단축주문번호</td>
							<td>원주문번호</td>
							<td>배송구명</td>
							<td>고객명</td>
							<td>경통코드</td>
							<td>상품명</td>
							<td>주문수량</td>
							<td>대체허용</td>
							<td>존코드</td>
							<td>진행상태</td>
							<!-- 
							<td>상품코드</td>
							-->
						</tr>
					</thead>
					<tbody>
						<c:forEach var="result" items="${orderList}" varStatus="status">
						
						<c:if test="${status.last}">
								Total Count : <b>${status.count}</b>
						</c:if>
						<tr>
							 
							<td><form:checkbox path="order_key" value="${result.deliveryDate}${result.deliveryCount}${result.shortOrderNo}${result.orgOrderNo}${result.goodsCode}" cssClass="form-control checkItem"/> </td>
							<td><c:out value="${fn:substring(result.deliveryDate, 0, 4)}"/>/<c:out value="${fn:substring(result.deliveryDate, 4, 6)}"/>/<c:out value="${fn:substring(result.deliveryDate, 6, 8)}"/></td>
							<td><c:out value="${result.deliveryCount}"/></td>
							<td><c:out value="${result.shortOrderNo}"/></td>
							<td><c:out value="${result.orgOrderNo}"/></td>
							<td><c:out value="${result.deliveryAreaName}"/></td>
							<td><c:out value="${result.customerName}"/></td>
							<td><c:out value="${result.goodsCode}"/></td>
							<td data-toggle="tooltip" data-placement="top" title="${result.goodsName}">
								<c:out value="${fn:substring(result.goodsName,0,20)}"/>
							</td>
							<td><c:out value="${result.orderQty}"/></td>
							<td><c:out value="${result.changeAllowYn}"/></td>
							<td><c:out value="${result.zoneCode}"/></td>
							<td>
								<c:choose>
									<c:when test="${result.stateCode=='0'}">예정</c:when>
									<c:when test="${result.stateCode=='1'}">지시</c:when>
									<c:when test="${result.stateCode=='2'}">피킹</c:when>
									<c:otherwise>오류</c:otherwise>
								</c:choose>
							</td>
							<!-- 
							<td><c:out value="${result.salesGoodsCode}"/></td>
							 -->
						</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<!-- 처리버튼 영역 -->
				<div class="button-group pull-right">
					<button type="button" class="btn btn-default btnChoiceDelete">선택 삭제</button>
					<button type="button" class="btn btn-default btnChoicePicking">선택 피킹지시</button>
					<button type="button" class="btn btn-default btnChoiceLabelPrint">선택 라벨출력</button>
				</div>
				
			</form:form>				
			</div>
		</div>
		<!-- 데이타 List -->
		
	</div>
	<!-- container -->
	<!-- 처리 스크립트 -->
    <script>
    
    	// 페이지 로딩시 처리
	    $(function(){
	    	
	    	// 배송일자 변경시 Event 처리
	    	$('#search_delivery_date').on("change",function(){
	    		var formData = $("#searchForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/deliveryCountList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setDeliveryCount($("#search_delivery_count"),data.deliveryCountList);
					    setZoneCode($("#search_zone_code"),data.zoneCodeList);	    			    
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});
	    		
	    	});
	    	
	    	// 차수 변경시 Event 처리
	    	$('#search_delivery_count').on("change",function(){
	    		var formData = $("#searchForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/zoneCodeList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setZoneCode($("#search_zone_code"),data.zoneCodeList);
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});
	    		
	    	});
	    	

	    	
	    	// 검색버튼 이벤트
	    	$(".btnSearch").on("click",function(e){
	    		$("#searchForm").attr("action","/orderList.do");
	    		$("#searchForm").submit();
	    	});
	    	
	    	// 주문 업로드 이벤트 (모달창 통해서 처리)
	    	$(".btnImportData").on("click",function(){
	    		$("#importForm").attr("action","/orderImportData.do");
	    		$("#importForm").submit();
	    		
	    	});
	    	
	    	// 피킹지시 이벤트
	    	$(".btnPicking").on("click",function(){
	    		$("#searchForm").attr("action","/picking.do");
	    		$("#searchForm").submit();
	    	});
	    	
	    	// 선택피킹지시 이벤트
	    	$(".btnChoicePicking").on("click",function(){
	    		var searchParam = $("#searchForm").serialize();
	    		$("#listForm").attr("action","/choicePicking.do"+"?"+searchParam);
	    		$("#listForm").submit();
	    	});
	    	
	    	// 라벨출력 이벤트
	    	$(".btnLabelPrint").on("click",function(){
	    		$("#searchForm").attr("action","/labelPrint.do");
	    		$("#searchForm").submit();
	    	});
	    	
	    	// 선택 라벨출력 이벤트
	    	$(".btnChoiceLabelPrint").on("click",function(){
	    		$("#listForm").attr("action","/choiceLabelPrint.do");
	    		$("#listForm").submit();
	    	});
	    	
	    	// 선택 삭제 이벤트
	    	$(".btnChoiceDelete").on("click",function(){
	    		$("#listForm").attr("action","/choiceDeleteOrder.do");
	    		$("#listForm").submit();
	    		
	    	});
	    	
	    	// data picker 설정
   		    $("#search_delivery_date").datepicker({
   		    		  dateFormat: "yymmdd"
   		    });
	    	
   	    	// checkBox Allt 체크 처리
   	    	$('#allCheckToggle').on('click',function(){
   	    		// 전체 체크여부
   	    		var allCheck = $('#allCheckToggle').prop('checked');
   	    		
   	    		// 체크여부에 따라 체크/언체크 처리
   	    		if(allCheck){
   	    			$('.checkItem').prop('checked',true);
   	    		}else{
   	    			$('.checkItem').prop('checked',false);
   	    		}
   	    	});
   	    	
	    });

    	
    	// 배송일자에 따른 차수 셋팅
    	function setDeliveryCount(target, data){
    		var dataArr = [];
    		var inx = 0;
    		target.empty();
   			dataArr[inx++] = "<option value=''>배송차수(전체)</option> ";
   			
    		$(data).each( function() {
    			dataArr[inx++] = "<option value=" + this.deliveryCount + ">" + this.deliveryCount+ "차</option> ";
    		});
    		target.append(dataArr);    		
    	}
    	
    	// 차수에 따른 ZoneCode 셋팅
    	function setZoneCode(target, data){
    		var dataArr = [];
    		var inx = 0;
    		target.empty();
    		
   			dataArr[inx++] = "<option value=''>존코드(전체)</option> ";
   			
    		$(data).each( function() {
    			console.log(this.zoneCode);
    			dataArr[inx++] = "<option value=" + this.zoneCode + ">" + this.zoneCode+ "</option> ";
    		});
    		target.append(dataArr);    		
    	}
    	
    </script>

	<!-- 업로드 Modal -->
	<div class="modal fade" id="importData" tabindex="-1" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
			
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">주문 업로드</h4>
				</div>
				
				<div class="modal-body">
					<!-- file upload -->
 					<form id="importForm" name="importForm" method="post" enctype="multipart/form-data">					
					    <input type="file" name="fileToUpload" id="fileToUpload"/>
					</form>
				</div>
			
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					<button type="button" class="btn btn-default btnImportData" data-dismiss="modal">업로드</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->

</body>
</html>
