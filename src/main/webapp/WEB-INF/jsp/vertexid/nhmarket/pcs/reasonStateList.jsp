<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor"%>
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
		  <h1>상품결품 현황 조회</h1>
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
       				
       				<%-- <form:select path="search_condition_type" cssClass="form-control">
						<!-- 검색 콤보박스(셀렉트박스) List -->						
						<form:option value="short_order_no" label="단축주문번호" />
						<form:option value="worker_id"      label="피커명" />
						<form:option value="goods_name"     label="상품명" />
					</form:select> --%>
					
					<%-- <form:input path="search_short_order_no" cssClass="form-control" placeholder="검색조건 입력"/> <!-- A20180111 LJM 검색조건 입력 (단축주문번호, 피커명, 상품명) --> --%>
					
					<%-- <form:input path="search_org_order_no" cssClass="form-control" placeholder="원주문번호"/> --%>					

					<!-- 검색버튼 -->					
					<button type="button" class="btn btn-primary btnSearch"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>검색</button>
					
					<!-- Excel 다운로드 버튼-->
					<div id="excelDownload" class="button-group pull-right">
						<button type="button" class="btn btn-primary" id="btnExcelExport"><span class="glyphicon glyphicon-download" aria-hidden="true">&nbsp;</span>엑셀 다운로드</button>
					</div>
					
				</div>
				</form:form>
				
			</div>
		</div>
		<!-- /검색 영역 -->
		
		<!--  피킹 헤더 데이터
				export 데이터 타입type
				        json: 'JSON',
				        xml: 'XML',
				        png: 'PNG',
				        csv: 'CSV',
				        txt: 'TXT',
				        sql: 'SQL',
				        doc: 'MS-Word',
				        excel: 'MS-Excel',
				        powerpoint: 'MS-Powerpoint',
				        pdf: 'PDF'
				option 샘플				        
				         "fileName": "대체현황목록", 
				         "worksheetName": "대체현황목록",         
				         "jspdf": {                  
				           "autotable": {
				             "styles": { "rowHeight": 20, "fontSize": 10 },
				             "headerStyles": { "fillColor": 255, "textColor": 0 },
				             "alternateRowStyles": { "fillColor": [60, 69, 79], "textColor": 255 }
				           }
				         }
				        
		 -->
		<table id = "dataTable" 
		       data-pagination="true"
		       data-side-pagination="client">
		    <thead>
		    <tr>
		        <!-- <th data-class="deliveryDate" data-field="deliveryDate" data-halign="center" data-align="center">배송일자</th>
		        <th data-class="deliveryCount" data-field="deliveryCount" data-halign="center" data-align="center">배송차수</th> -->
		        <th data-class="rowNumber" data-field="rowNumber" data-halign="center" data-align="center">NO.</th>
		        <th data-class="shortOrderNo" data-field="shortOrderNo" data-halign="center" data-align="center">단축주문번호</th>
		        <!-- <th data-class="orgOrderNo" data-field="orgOrderNo" data-halign="center" data-align="center">원주문번호</th>
		        <th data-class="deliveryAreaName" data-field="deliveryAreaName" data-halign="center" data-align="left">배송구명</th>
		        <th data-class="customerName" data-field="customerName" data-halign="center" data-align="left">고객명</th> -->
				<th data-class="goodsCode" data-field="goodsCode" data-halign="center" data-align="left">상품코드</th>		        
				<th data-class="goodsName" data-field="goodsName" data-halign="center" data-align="left" data-width="200px">상품 명</th>
		        <th data-class="orderCost" data-field="orderCost" data-halign="center" data-align="right">판매가</th>
		        <th data-class="orderQty" data-field="orderQty" data-halign="center" data-align="right">주문수량</th> 
		        <th data-class="comName" data-field="comName" data-halign="center" data-align="right">피커명</th>
		        <!-- <th data-class="pickQty" data-field="pickQty" data-halign="center" data-align="right">피킹수량</th> -->
		        <th data-class="reasonQty" data-field="reasonQty" data-halign="center" data-align="right">결품수량</th> 
		        <th data-class="soldOutReason" data-field="soldOutReason" data-halign="center" data-align="left">결품사유</th> 
		        <th data-class="changeAllowYn" data-field="changeAllowYn" data-halign="center" data-align="center">상품대체요청여부</th>
				<th data-class="goodsSpec" data-field="goodsSpec" data-halign="center" data-align="left">상품규격</th>
		        <th data-class="makerName" data-field="makerName" data-halign="center" data-align="left">생산자제조사</th>
		        <th data-class="salesGoodsCode" data-field="salesGoodsCode" data-halign="center" data-align="center">판매상품코드</th>		        
		        <th data-class="goodsOptionName" data-field="goodsOptionName" data-halign="center" data-align="left">단품명</th>
		        <th data-class="taxType" data-field="taxType" data-halign="center" data-align="center">과세구분코드</th>
		        <th data-class="categoryLarge" data-field="categoryLarge" data-halign="center" data-align="left">대카테고리명</th>
		        <th data-class="categoryMiddle" data-field="categoryMiddle" data-halign="center" data-align="left">중카테고리명</th>
		    </tr>
		    </thead>
		</table>
		 
	</div>
	<!-- container -->
	<!-- 처리 스크립트 -->
    <script>
    
    
    	// 페이지 로딩시 처리
    	var $dataTable = $('#dataTable');

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
    	
    	
    	// MSO format 처리
    	onMsoNumberFormat = function(cell, row, col) {
	   		// 단축주문번호 , 경통코드 (text 처리) 
    		if (row > 0 && (col == 2 
    					|| col == 6)) {
    		  return "\\@";
    		 }
    	};
    	
    	// 페이지로드시 처리
	    $(function(){
	    	
   	    	// excel파일 다운로드 처리
   	    	$('#btnExcelExport').on('click',function(){
   	    		
   	    		$("#searchForm").attr("action","/excelReasonStateList.do");
   	    		$("#searchForm").submit();
   	    		//alert("바탕화면-내 PC-D:\상품결품대체현황엑셀 폴더에 다운로드가 완료되었습니다")
   	    			
   	    	});	    	
	    	
	    	
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
	    		$("#searchForm").attr("action","/reasonStateList.do");
	    		$("#searchForm").submit();
	    	});
	    	
	    	// data picker 설정
   		    $("#search_delivery_date").datepicker({
   		    	dateFormat: "yymmdd",
   		    	
   		    	});
	    	
   	    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   	    	// bootstrap table script
   	    	// formatShowingRows    /  pageFrom, pageTo, totalRows	/ 'Showing %s to %s of %s rows'
   	    	
            $dataTable.bootstrapTable({data: <%= new ObjectMapper().writeValueAsString(request.getAttribute("reasonStateList"))%> });
   	    	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   	    	
	    });    	
    	
    	
    </script>
</body>
</html>
