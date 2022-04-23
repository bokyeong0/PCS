<%-- <%@ page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@ page import="com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
  /**
  * @Class Name : printStateList.jsp
  * @Description : print List 화면
  * @Modification Information
  *
  *   수정일         수정자                   수정내용
  *  -------    --------    ---------------------------
  *  2017.01.31            최초 생성
  *
  * author 실행환경 개발팀
  * since 2016.07.18
  *
  * Copyright (C) 2009 by MOPAS  All right reserved.
  
  */
%>
<!DOCTYPE html>
<html>

<!-- header -->
<%@include file="cmmn/header.jsp" %>

<body>
	<!-- 상단 메뉴  -->
	<%@include file="cmmn/menu.jsp" %>
	<!-- /상단 메뉴  -->

	<!-- container -->
	<div class="container">
	
		<!-- 페이지명 -->
		<div class="page-header">
		  <h1>피킹리스트 </h1>
		</div>
		
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
						<form:option value="" label="존코드(전체)" />
						<!-- 존코드 List -->						
						<c:forEach var="result" items="${zoneCodeList}" varStatus="status">
							<form:option value="${result.zoneCode}" label="${result.zoneCode}" />
						</c:forEach>
       				</form:select>
					
					<form:select path="search_state_code" cssClass="form-control">
						<form:option value="" label="상태(전체)" />
						<!-- 상태코드 List -->						
						<c:forEach var="result" items="${stateCodeList}" varStatus="status">
							<form:option value="${result.comCode}" label="${result.comName}" />
						</c:forEach>
					</form:select>
					
					<form:input path="search_org_order_no" cssClass="form-control" placeholder="원주문번호"/>					
					
					<button type="button" class="btn btn-primary btnSearch"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>검색</button>
					
					<!-- 처리버튼 영역 -->
					<div class="button-group pull-right" style="margin-bottom: 5px;">
						<button type="button" class="btn btn-primary btnChoicePDFPrintPicking"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>선택 리포트출력</button>
						<button type="button" class="btn btn-primary btnPrePcking" data-toggle="modal" data-target="#prePckData"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>사전피킹리스트</button>
						<button type="button" class="btn btn-primary btnDelMessage" data-toggle="modal" data-target="#delMessageData"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>배송메모리스트</button>
						<button type="button" class="btn btn-primary btnPickingRequest" data-toggle="modal" data-target="#pickingRequestData"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>피킹요청서</button>
					</div>
					
				</div>
				</form:form>
				
			</div>
		</div>
		<!-- /검색 영역 -->
		
		<!-- 데이터 영역 -->
		<!--  피킹 헤더 데이터 -->
		<table id = "pickHeaderList" class="table table-striped">
		    <thead>
		    <tr>
		        <th data-class="state" data-field="state" data-checkbox="true"></th>
		        <!-- <th data-class="pickId" data-field="pickId" data-halign="center" data-align="center" data-sortable="true">피킹ID</th> -->
		        <th data-class="deliveryDate" data-field="deliveryDate" data-halign="center" data-align="center">배송일자</th>
		        <th data-class="deliveryCount" data-field="deliveryCount" data-halign="center" data-align="center">배송차수</th>
		        <th data-class="shortOrderNo" data-field="shortOrderNo" data-halign="center" data-align="center">단축주문번호</th>
		        <!-- <th data-class="orgOrderNo" data-field="orgOrderNo" data-halign="center" data-align="center">원주문번호</th> -->
		        <th data-class="orderDate" data-field="orderDate" data-halign="center" data-align="center">주문일자</th>
		        <th data-class="orderNo" data-field="orderNo" data-halign="center" data-align="center">주문번호</th>
		        <!-- <th data-class="orderRowNo" data-field="orderRowNo" data-halign="center" data-align="left">주문순번</th> -->
		        <th data-class="deliveryAreaName" data-field="deliveryAreaName" data-halign="center" data-align="center" data-width="120px">권역명</th>
		        <th data-class="orderCustomerName" data-field="orderCustomerName" data-halign="center" data-align="center">수취인명</th>
		        <!-- <th data-class="categoryMiddle" data-field="categoryMiddle" data-halign="center" data-align="left">중카테고리명</th> -->
		        <!-- <th data-class="customerName" data-field="customerName" data-halign="center" data-align="center">회원명</th> -->
		        <!-- <th data-class="trayNo" data-field="trayNo" data-halign="center" data-align="right">트레이번호</th> -->
		        <th data-class="zoneCode" data-field="zoneCode" data-halign="center" data-align="center">존코드</th>		        
		        <!-- <th data-class="labelPrintCount" data-field="labelPrintCount" data-halign="center" data-align="right">출력횟수</th> -->
		        <!-- <th data-class="trolleyId" data-field="trolleyId" data-halign="center" data-align="right">트롤리ID</th> -->
		        <!-- <th data-class="workerId" data-field="workerId" data-halign="center" data-align="left">작업자</th> -->
		        <th data-class="stateCode" data-field="stateCode" data-halign="center" data-align="center" data-formatter="stateCode">진행상태</th>
		        <!-- <th data-class="deliveryAmount" data-field="deliveryAmount" data-halign="center" data-align="right">배송비</th> -->
		        <!-- <th data-class="freeGiftName" data-field="freeGiftName" data-halign="center" data-align="center">사은품대상</th> -->
		        <th data-class="printYn" data-field="printYn" data-halign="center" data-align="center">리포트출력여부</th>
		        <th data-class="deliveryMessage" data-field="deliveryMessage" data-halign="center" data-align="left">배송요청내용</th>
		        <th data-class="goodsMessage" data-field="goodsMessage" data-halign="center" data-align="left">공급처요청내용</th>
		    </tr>
		    </thead>
		</table>
		<!--  /피킹 헤더 데이터 -->
	<!-- //데이터 영역 -->		
		 
	</div>
	<!-- container -->
	
	<!-- 사전피킹리스트 Modal -->
	<div class="modal fade" id="prePckData" tabindex="-1" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
			
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">사전피킹리스트</h4>
				</div>
				
				<div class="modal-body">
 					<form:form commandName="searchVO" cssClass="form-inline" id="prePckForm" name="prePckForm" method="post">
 					<div class="form-group">
 					
					<form:hidden path="search_delivery_date" cssClass="form-control"/>
					
        			<form:select path="search_delivery_count" cssClass="form-control"  style="width: 150px">

						<!-- 배송차수 List -->        				
        				<c:forEach var="result" items="${deliveryCountList}" varStatus="status">
        					<form:option value="${result.deliveryCount}" label="${result.deliveryCount}차" />
        				</c:forEach>
        				
       				</form:select>

        			<form:select path="search_zone_code" cssClass="form-control">
						<form:option value="" label="존코드(전체)" />
						<!-- 존코드 List -->						
						<c:forEach var="result" items="${zoneCodeList}" varStatus="status">
							<form:option value="${result.zoneCode}" label="${result.zoneCode}" />
						</c:forEach>
       				</form:select>
					
					</div>    
					</form:form>
				</div>
			
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					<button type="button" class="btn btn-primary btnPrePckDataInqry"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>조회</button>
					<button type="button" class="btn btn-primary btnPrePckData"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>출력</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<!-- 배송 메모 리스트 Modal -->
	<div class="modal fade" id="delMessageData" tabindex="-1" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
			
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">배송메모리스트</h4>
				</div>
				
				<div class="modal-body">
 					<form:form commandName="searchVO" cssClass="form-inline" id="delMessageForm" name="delMessageForm" method="post">
 					<div class="form-group">
 					
					<form:input path="search_delivery_date" cssClass="form-control" placeholder="배송일자" size="10" maxlength="10" />
						
        			<form:select path="search_delivery_count" cssClass="form-control">
        				<form:option value="" label="배송차수(전체)" />

						<!-- 배송차수 List -->        				
        				<c:forEach var="result" items="${deliveryCountList}" varStatus="status">
        					<form:option value="${result.deliveryCount}" label="${result.deliveryCount}차" />
        				</c:forEach>
        				
       				</form:select>

        			<form:select path="search_zone_code" cssClass="form-control">
						<form:option value="" label="존코드(전체)" />
						<!-- 존코드 List -->						
						<c:forEach var="result" items="${zoneCodeList}" varStatus="status">
							<form:option value="${result.zoneCode}" label="${result.zoneCode}" />
						</c:forEach>
       				</form:select>
					
					</div>    
					</form:form>
				</div>
			
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					<button type="button" class="btn btn-primary btnDelMessageDataInqry"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>조회</button>
					<button type="button" class="btn btn-primary btnDelMessageDataPr"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>출력</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<!-- 피킹 요청서 Modal -->
	<div class="modal fade" id="pickingRequestData" tabindex="-1" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
			
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">피킹요청서</h4>
				</div>
				
				<div class="modal-body">
 					<form:form commandName="searchVO" cssClass="form-inline" id="pickingRequestForm" name="pickingRequestForm" method="post">
 					<div class="form-group">
 					
					<form:input path="search_delivery_date" cssClass="form-control" placeholder="배송일자" size="10" maxlength="10" />
						
        			<form:select path="search_delivery_count" cssClass="form-control">
        				<form:option value="" label="배송차수(전체)" />

						<!-- 배송차수 List -->        				
        				<c:forEach var="result" items="${deliveryCountList}" varStatus="status">
        					<form:option value="${result.deliveryCount}" label="${result.deliveryCount}차" />
        				</c:forEach>
        				
       				</form:select>

        			<form:select path="search_zone_code" cssClass="form-control">
						<form:option value="" label="존코드(전체)" />
						<!-- 존코드 List -->						
						<c:forEach var="result" items="${zoneCodeList}" varStatus="status">
							<form:option value="${result.zoneCode}" label="${result.zoneCode}" />
						</c:forEach>
       				</form:select>
					
					</div>    
					</form:form>
				</div>
			
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					<button type="button" class="btn btn-primary btnPickingReqDataInqry"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>조회</button>
					<button type="button" class="btn btn-primary btnPickingReqDataPr"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>출력</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<!-- 처리 스크립트 -->
    <script>
    	//==============================================================================================================================
    	// 피킹리스트 내 사전피킹리스트 모달 배송일자에 따른 차수 셋팅
		function setDeliveryCount(target, data){
			var dataArr = [];
			var idx = 0;
			target.empty();
			if(target == ""){
				dataArr[idx++] = "<option value=''>배송차수 없음</option> ";								
			}			
			
			$(data).each( function() {
				dataArr[idx++] = "<option value=" + this.deliveryCount + ">" + this.deliveryCount+ "차</option> ";
			});			
			target.append(dataArr);    
		}
    	
		// 피킹리스트 배송일자에 따른 차수 셋팅
    	function setDeliveryCountP(target, data){
    		var dataArr = [];
    		var idx = 0;
    		target.empty();
   			dataArr[idx++] = "<option value=''>배송차수(전체)</option> ";
   			
    		$(data).each( function() {
    			dataArr[idx++] = "<option value=" + this.deliveryCount + ">" + this.deliveryCount+ "차</option> ";
    		});
    		target.append(dataArr);    		
    	}
    	
    	
    	// 차수에 따른 ZoneCode 셋팅
    	function setZoneCode(target, data){
    		var dataArr = [];
    		var idx = 0;
    		target.empty();
   			dataArr[idx++] = "<option value=''>존코드(전체)</option> ";
   			
    		$(data).each( function() {
    			dataArr[idx++] = "<option value=" + this.zoneCode + ">" + this.zoneCode+ "</option> ";
    		});
    		target.append(dataArr);    		
    	}
    	//==============================================================================================================================
    		
    	//==============================================================================================================================
    	// bootstrap table script
        function stateCode(value) {
        	var codeName;
    		var codeList = <%= new ObjectMapper().writeValueAsString(request.getAttribute("stateCodeList"))%>;
    		var codeListCnt = codeList.length;

    		// 코드 반복 확인
    		for(var i = 0 ; i < codeListCnt ; i++){
    			if(codeList[i].comCode == value){
    				codeName = codeList[i].comName;
    			}
    				
    		}
    		return codeName;
        }
       
    	//==============================================================================================================================
    	// 페이지 로딩시 처리
	    $(function(){
	    	
	    	//==============================================================================================================================
	    	// data picker 설정
   		    $("#search_delivery_date").datepicker({
   		    	dateFormat: "yymmdd",
   		    	
   		    	});
	    	
	    	var $headerTable = $('#pickHeaderList');
	    	
	    	//==============================================================================================================================
			/* function cbo1_10600(){
			 $.ajax({
			  	 url : "/organization/auth",
			     data:{"om_code":"", "om_orgnzt_se": "1"},
			     type : "POST",
			     dataType : "json",
			     async: false,
			     cache : false ,
			     success : function(data) {
			     
			   	 var listHtml = "<option value='' selected='selected'>선택</option>";
			     
			     if(data.result.length > 0){
			       for(var i=0; i<data.result.length; i++){
			        var res = data.result[i];     
			        
			        listHtml += "<option value ='"+short_order_no+"' data-om_innb ='"+res.om_code+"' >"+ res.om_nm +"</option>";
			       }
			   }
	    		
	    		$("#aaa").html(listHtml);  //jquery */
	    		
	    		//document.getElementById("aaa").innerHTML = listHtml; //javascript
	    		
	    		
	    		
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
					    setDeliveryCountP($("#search_delivery_count"),data.deliveryCountList);
					    /* setZoneCode($("#search_zone_code"),data.zoneCodeList); */	    			    
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
	    		$("#searchForm").attr("action","/pickList.do");
	    		$("#searchForm").submit();
	    	});
	    	
	    	// 사전피킹리스트 출력 Event 처리
	    	$(".btnPrePcking").on("click",function(e){
		    	// date 설정
		    	var now  = new Date();
	   			var year = now.getFullYear();
	   			var mon	 = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	   			var day	 = (now.getDate())>9 ? ''+(now.getDate()) : '0'+(now.getDate());
	   			var toDay = year + mon + day;
	      			 
	   		    $("#prePckForm #search_delivery_date").val(toDay); 
		    			    	
	    		var formData = $("#prePckForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/preDeliveryCountList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setDeliveryCount($("#prePckForm #search_delivery_count"),data.deliveryCountList);
					    setZoneCode($("#prePckForm #search_zone_code"),data.zoneCodeList);	    			    
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});	    		    		
	    		
	    	});
	    	
	    	// 사전피킹리스트 조회 Event 처리
	    	$(".btnPrePckingInquiry").on("click",function(e){
		    	// date 설정
		    	var now  = new Date();
	   			var year = now.getFullYear();
	   			var mon	 = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	   			var day	 = (now.getDate())>9 ? ''+(now.getDate()) : '0'+(now.getDate());
	   			var toDay = year + mon + day;
	      			 
	   		    $("#prePckForm #search_delivery_date").val(toDay); 
		    			    	
	    		var formData = $("#prePckForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/preDeliveryCountList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setDeliveryCount($("#prePckForm #search_delivery_count"),data.deliveryCountList);
					    setZoneCode($("#prePckForm #search_zone_code"),data.zoneCodeList);	    			    
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});	    		    		
	    		
	    	});
	    	
	    	// 사전피킹리스트 모달 창을 클릭시 Event 처리
 	    	$('#prePckForm #search_delivery_count').on("click",function(e){	    	
	    		var formData = $("#prePckForm").serialize();	
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/zoneCodeList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setZoneCode($("#prePckForm #search_zone_code"),data.zoneCodeList);
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});
	    	});
	    	
	    	
 	    	// 사전피킹리스트 출력 이벤트
	    	$(".btnPrePckData").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
	    		e.preventDefault();
	    		$("#prePckForm").attr("action","/prePicking.do");
	    		$("#prePckForm").preventDoubleSubmission().submit();
	    	});
	    	
	    	// 사전피킹리스트 조회 이벤트
	    	$(".btnPrePckDataInqry").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
	    		e.preventDefault();
	    		$("#prePckForm").attr("action","/prePickingInquiry.do");
	    		$("#prePckForm").preventDoubleSubmission().submit();
	    	});
	    	
	 //=======================배송메모 리스트 이벤트 처리========================   	
	    	
		 // 배송메모리스트 출력 Event 처리
	    	$(".btnDelMessageDataPr").on("click",function(e){
		    	// date 설정
		    	var now  = new Date();
	   			var year = now.getFullYear();
	   			var mon	 = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	   			var day	 = (now.getDate())>9 ? ''+(now.getDate()) : '0'+(now.getDate());
	   			var toDay = year + mon + day;
	      			 
	   		    $("#delMessageForm #search_delivery_date").val(toDay); 
		    			    	
	    		var formData = $("#delMessageForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/preDeliveryCountList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setDeliveryCount($("#delMessageForm #search_delivery_count"),data.deliveryCountList);
					    //setZoneCode($("#delMessageForm #search_zone_code"),data.zoneCodeList);	    			    
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});	    		    		
	    		
	    	});
	    	
	    	// 배송메모리스트 조회 Event 처리
	    	$(".btnDelMessageDataInqry").on("click",function(e){
		    	// date 설정
		    	var now  = new Date();
	   			var year = now.getFullYear();
	   			var mon	 = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	   			var day	 = (now.getDate())>9 ? ''+(now.getDate()) : '0'+(now.getDate());
	   			var toDay = year + mon + day;
	      			 
	   		    $("#delMessageForm #search_delivery_date").val(toDay); 
		    			    	
	    		var formData = $("#delMessageForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/preDeliveryCountList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setDeliveryCount($("#delMessageForm #search_delivery_count"),data.deliveryCountList);
					    //setZoneCode($("#delMessageForm #search_zone_code"),data.zoneCodeList);	    			    
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});	    		    		
	    		
	    	});
	    	
	    	// 배송메모리스트 모달 창을 클릭시 Event 처리
 	    	$('#delMessageForm #search_delivery_count').on("click",function(e){	    	
	    		var formData = $("#delMessageForm").serialize();	
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/zoneCodeList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setZoneCode($("#delMessageForm #search_zone_code"),data.zoneCodeList);
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});
	    	});
	    	
	    	
 	    	// 배송메모리스트 출력 이벤트
	    	$(".btnDelMessageDataPr").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
	    		e.preventDefault();
	    		$("#delMessageForm").attr("action","/orderDelMessagePr.do");
	    		$("#delMessageForm").preventDoubleSubmission().submit();
	    	});
	    	
	    	// 배송메모리스트 조회 이벤트
	    	$(".btnDelMessageDataInqry").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
	    		e.preventDefault();
	    		$("#delMessageForm").attr("action","/orderDelMessageInqry.do");
	    		$("#delMessageForm").preventDoubleSubmission().submit();
	    	});
	//end//================================================================================
	//=======================피킹 요청서 이벤트 처리===============================================   	
	    	
		 // 피킹요청서 출력 Event 처리
	    	$(".btnPickingReqDataPr").on("click",function(e){
		    	// date 설정
		    	var now  = new Date();
	   			var year = now.getFullYear();
	   			var mon	 = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	   			var day	 = (now.getDate())>9 ? ''+(now.getDate()) : '0'+(now.getDate());
	   			var toDay = year + mon + day;
	      			 
	   		    $("#pickingRequestForm #search_delivery_date").val(toDay); 
		    			    	
	    		var formData = $("#pickingRequestForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/preDeliveryCountList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setDeliveryCount($("#pickingRequestForm #search_delivery_count"),data.deliveryCountList);
					    setZoneCode($("#delMessageForm #search_zone_code"),data.zoneCodeList);	    			    
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});	    		    		
	    		
	    	});
	    	
	    	// 피킹요청리스트 조회 Event 처리
	    	$(".btnPickingReqDataInqry").on("click",function(e){
		    	// date 설정
		    	var now  = new Date();
	   			var year = now.getFullYear();
	   			var mon	 = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	   			var day	 = (now.getDate())>9 ? ''+(now.getDate()) : '0'+(now.getDate());
	   			var toDay = year + mon + day;
	      			 
	   		    $("#pickingRequestForm #search_delivery_date").val(toDay); 
		    			    	
	    		var formData = $("#pickingRequestForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/preDeliveryCountList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setDeliveryCount($("#pickingRequestForm #search_delivery_count"),data.deliveryCountList);
					    setZoneCode($("#pickingRequestForm #search_zone_code"),data.zoneCodeList);	    			    
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});	    		    		
	    		
	    	});
	    	
	    	// 피킹요청리스트 모달 창을 클릭시 Event 처리
 	    	$('#pickingRequestForm #search_delivery_count').on("click",function(e){	    	
	    		var formData = $("#pickingRequestForm").serialize();	
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/zoneCodeList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setZoneCode($("#pickingRequestForm #search_zone_code"),data.zoneCodeList);
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});
	    	});
	    	
	    	
 	    	// 피킹요청리스트 출력 이벤트
	    	$(".btnPickingReqDataPr").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
	    		e.preventDefault();
	    		$("#pickingRequestForm").attr("action","/pickingRequestPr.do");
	    		$("#pickingRequestForm").preventDoubleSubmission().submit();
	    	});
	    	
	    	// 피킹요청리스트 조회 이벤트
	    	$(".btnPickingReqDataInqry").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
	    		e.preventDefault();
	    		$("#pickingRequestForm").attr("action","/pickingRequestInqry.do");
	    		$("#pickingRequestForm").preventDoubleSubmission().submit();
	    	});
	//end//================================================================================     	
	    	// 선택 리포트출력 이벤트
	    	$(".btnChoicePDFPrint").on("click",function(e){
	    		
				e.preventDefault();
	   	        $(".btn").attr("disabled",true);
	    		//waitingDialog.show();    	        
    	        var formData = JSON.stringify($headerTable.bootstrapTable('getSelections'));
    	        
    	        // 진행상태 체크
    	        var chkVal = jQuery.parseJSON(formData);
    	        
    	        if(chkVal == ''){
    	        	alert("최소 하나는 선택하셔야 합니다.");   
    	        	// 밑에 bootstrap table 실행해야 하므로 servlet 적용함 
    	    		$("#searchForm").attr("action","/pickList.do");
    	    		$("#searchForm").submit();
    	        }else{
    	        	$.each(chkVal, function (key, value) {  
        	        	// 현재날짜와 배송일자 체크해서 오늘이 아니면 리턴시킴
    	    	        var now = new Date();
    	    	        var year= now.getFullYear();
    	    	        var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
    	    	        var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();		    	                
    	    	        var curDt = year+mon+day;	
    	    	        
    	    	        if(curDt != value.deliveryDate){
    	    	        	alert("리포트 출력은 배송일자가 오늘날짜인 경우만 가능 합니다.");
    	    	        }
    	    	        
    	    	        if(value.stateCode != '2'){
    	    	        	alert("해당 번호( "+value.orderDate+-+value.orderNo+" )는 작업이 완료되지 않았습니다."+"\n피킹완료된 주문만 리포트 출력이 가능합니다.");			    	        
    	    	        } 			    	        		    	        
        	        }); 
    	        	
		    		$.ajax({
		    			  type : "POST",
		    			  url : "<c:url value='/choicePDFPrint.do'/>",
		    			  cache : false,
		    			  dataType : "json",
		    			  contentType: "application/json; charset=UTF-8",	    			  
		    			  data : formData,
		    			  success : function(data) {
		    				// 에러 메시지가 있는경우
		    				if(data.resultErrorMsg != null){
				    			alert(data.resultErrorMsg);	    				  
		    				}
		    			    //Sucess시, 처리
		    			  },
		    			  error : function(jqXHR){
		    			    //Error시, 처리
			    		  },
		    			  complete : function(){
		    	    	      $(".btn").attr("disabled",false);
	 	    		    	  //waitingDialog.hide();
	 	    		    	  $("#searchForm").attr("action","/pickList.do");
	 	    		    	  $("#searchForm").submit();
	    			   	  }
	
		    		});
    	        }
	    	});
	
	    	// 피킹리스트 선택 리포트출력 이벤트
	    	$(".btnChoicePDFPrintPicking").on("click",function(e){
	    		
				e.preventDefault();
	   	        $(".btn").attr("disabled",true);
	    		//waitingDialog.show();    	        
    	        var formData = JSON.stringify($headerTable.bootstrapTable('getSelections'));
    	        
    	        // 진행상태 체크
    	        var chkVal = jQuery.parseJSON(formData);
    	        
    	        if(chkVal == ''){
    	        	alert("최소 하나는 선택하셔야 합니다.");   
    	        	// 밑에 bootstrap table 실행해야 하므로 servlet 적용함 
    	    		$("#searchForm").attr("action","/pickList.do");
    	    		$("#searchForm").submit();
    	        }else{
    	        	$.each(chkVal, function (key, value) {  
        	        	// 현재날짜와 배송일자 체크해서 오늘이 아니면 리턴시킴
    	    	        var now = new Date();
    	    	        var year= now.getFullYear();
    	    	        var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
    	    	        var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();		    	                
    	    	        var curDt = year+mon+day;	
    	    	        
    	    	        if(curDt != value.deliveryDate){
    	    	        	alert("리포트 출력은 배송일자가 오늘날짜인 경우만 가능 합니다.");
    	    	        }
    	    	        
    	    	        if(value.stateCode != '2'){
    	    	        	alert("해당 번호( "+value.orderDate+-+value.orderNo+" )는 작업이 완료되지 않았습니다."+"\n피킹완료된 주문만 리포트 출력이 가능합니다.");			    	        
    	    	        } 			    	        		    	        
        	        }); 
    	        	
		    		$.ajax({
		    			  type : "POST",
		    			  url : "<c:url value='/choicePDFPrintPicking.do'/>",
		    			  cache : false,
		    			  dataType : "json",
		    			  contentType: "application/json; charset=UTF-8",	    			  
		    			  data : formData,
		    			  success : function(data) {
		    				// 에러 메시지가 있는경우
		    				if(data.resultErrorMsg != null){
				    			alert(data.resultErrorMsg);	    				  
		    				}
		    			    //Sucess시, 처리
		    			  },
		    			  error : function(jqXHR){
		    			    //Error시, 처리
			    		  },
		    			  complete : function(){
		    	    	      $(".btn").attr("disabled",false);
	 	    		    	  //waitingDialog.hide();
	 	    		    	  $("#searchForm").attr("action","/pickList.do");
	 	    		    	  $("#searchForm").submit();
	    			   	  }
	
		    		});
    	        }
	    	});
	    	
   	    	//==============================================================================================================================
   	    	// bootstrap table script
   	    	// formatShowingRows    /  pageFrom, pageTo, totalRows	/ 'Showing %s to %s of %s rows'
   	    	
			// 피킹헤더 데이터 로드
			$headerTable.bootstrapTable({data: <%= new ObjectMapper().writeValueAsString(request.getAttribute("pickHeaderList"))%>});
			$headerTable.bootstrapTable('hideColumn', 'pickId');
	    });
    	    		
    </script>

</body>
</html>
 --%>