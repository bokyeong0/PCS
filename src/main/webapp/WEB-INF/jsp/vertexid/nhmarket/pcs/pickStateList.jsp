<%@ page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@ page import="com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor"%>
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
		  <h1>피킹현황조회 </h1>
		</div>
		
		<!-- 검색 영역 -->
		<div class="panel panel-default">
			<div class="panel-body">
 				<form:form commandName="searchVO" id="searchForm" name="searchForm" method="post">			
				<div class="form-inline">
				
					<table>
						<tr>
							<td style="padding-bottom: 10px;">				
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
								
								<form:select path="nCoupon_yn" cssClass="form-control">
									<form:option value="" label="N쿠폰(전체)" />
									<!-- N쿠폰 여부 -->						
									<form:option value="Y" label="여" />
									<form:option value="N" label="부" />
								</form:select>
		       				</td>	
		       				<td rowspan="2"  style="padding-left: 10px;">
		       					<button type="button" class="btn btn-primary btnSearch" style="height: 80px;"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>검색</button>
		       					<button type="button" class="btn btn-primary btnReset" style="height: 80px;"><p style="margin: 7px;">초기화</p></button>
		       				</td>
		       				<td></td>
		       			</tr>
		       			
		       			<tr>
		       				<td>
		       					<form:input path="search_short_order_no" cssClass="form-control" placeholder="단축주문번호" size="10" onkeyup="enterkey();"/>
		       					<form:input path="search_delivery_area_name" cssClass="form-control" placeholder="배송구명" size="12" onkeyup="enterkey();"/>
		       					<form:input path="search_goods_name" cssClass="form-control" placeholder="상품명" size="20" onkeyup="enterkey();"/>
		       					<form:input path="search_worker_id" cssClass="form-control" placeholder="피커명" size="15" onkeyup="enterkey();"/>
		       				</td>
		       				<td style="padding-left: 50px;">
		       				<!-- 처리버튼 영역 -->
								<div class="button-group pull-right" style="margin-bottom: 5px;">
									<button type="button" class="btn btn-primary btnChoiceLabelPrint"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>선택 라벨출력</button>
								</div>
							</td>
		       			</tr>
		       		</table>
		       							
					<%-- <form:select path="search_condition_type" cssClass="form-control">
						<!-- 검색 콤보박스(셀렉트박스) List -->						
						<form:option value="short_order_no" label="단축주문번호" />
						<form:option value="worker_id"      label="피커명" />
						<form:option value="goods_name"     label="상품명" />
					</form:select> --%>
					
					<%-- <form:input path="search_org_order_no" cssClass="form-control" placeholder="원주문번호"/> --%>
										
					<%-- <form:input path="search_short_order_no" cssClass="form-control" placeholder="검색조건 입력"/> --%>
					
				</div>
				</form:form>
				
				
			</div>
		</div>
		<!-- /검색 영역 -->
		
		<!-- 데이터 영역 -->
		<!--  피킹 헤더 데이터 -->
		<table id = "pickHeaderList"
		       data-pagination="true"
		       data-side-pagination="client"
		       data-page-size="10">
		    <thead>
		    <tr>
		        <th data-class="state" data-field="state" data-checkbox="true"></th>
		        <th data-class="pickId" data-field="pickId" data-halign="center" data-align="center" data-sortable="true">피킹ID</th>
		        <!-- <th data-class="deliveryDate" data-field="deliveryDate" data-halign="center" data-align="center">배송일자</th>
		        <th data-class="deliveryCount" data-field="deliveryCount" data-halign="center" data-align="center">배송차수</th> -->
		        <th data-class="shortOrderNo" data-field="shortOrderNo" data-halign="center" data-align="center">단축주문번호</th>
		        <!-- <th data-class="orgOrderNo" data-field="orgOrderNo" data-halign="center" data-align="center">원주문번호</th> -->
		        <th data-class="orderDate" data-field="orderDate" data-halign="center" data-align="center">주문일자</th>
		        <th data-class="orderNo" data-field="orderNo" data-halign="center" data-align="center">주문번호</th>
		        <!-- <th data-class="orderRowNo" data-field="orderRowNo" data-halign="center" data-align="left">주문순번</th> -->
		        <th data-class="deliveryAreaName" data-field="deliveryAreaName" data-halign="center" data-align="center" data-width="60px">권역구명</th>
		        <th data-class="orderCustomerName" data-field="orderCustomerName" data-halign="center" data-align="center">수취인명</th>
		        <!-- <th data-class="customerName" data-field="customerName" data-halign="center" data-align="center">회원명</th> -->
		        <th data-class="trayNo" data-field="trayNo" data-halign="center" data-align="right">트레이번호</th>
		        <th data-class="zoneCode" data-field="zoneCode" data-halign="center" data-align="center">존정보</th>		        
		        <th data-class="labelPrintCount" data-field="labelPrintCount" data-halign="center" data-align="right">출력횟수</th>
		        <th data-class="trolleyId" data-field="trolleyId" data-halign="center" data-align="right">트롤리ID</th>
		        <th data-class="workerId" data-field="workerId" data-halign="center" data-align="left">작업자</th>
		        <th data-class="stateCode" data-field="stateCode" data-halign="center" data-align="center" data-formatter="stateCode">진행상태</th>
		        <!--  <th data-class="deliveryAmount" data-field="deliveryAmount" data-halign="center" data-align="right">배송비</th> --> <!-- 09/15 반영대상 -->
		        <th data-class="deliveryMessage" data-field="deliveryMessage" data-halign="center" data-align="left">배송요청내용</th>
		        <th data-class="goodsMessage" data-field="goodsMessage" data-halign="center" data-align="left">공급처요청내용</th>
		        <!--  <th data-class="freeGiftName" data-field="freeGiftName" data-halign="center" data-align="center">사은품대상</th> --> <!-- 09/15 반영대상 -->
		    </tr>
		    </thead>
		</table>
		<!--  피킹 상세 데이터 -->
		<table id = "pickDetailList">
		    <thead>
		    <tr>
		    	<!-- <th data-class="orderRowNo" data-field="orderRowNo" data-halign="center" data-align="center">주문순번</th> -->
		        <th data-class="goodsCode" data-field="goodsCode" data-halign="center" data-align="center">경제통합상품코드</th>
		        <th data-class="goodsName" data-field="goodsName" data-halign="center" data-align="left">상품명</th>
		        <th data-class="goodsSpec" data-field="goodsSpec" data-halign="center" data-align="left">상품규격</th>
		        <th data-class="orderCost" data-field="orderCost" data-halign="center" data-align="right">주문단가</th>
		        <th data-class="orderQty" data-field="orderQty" data-halign="center" data-align="right">주문수량</th>
		        <th data-class="makerName" data-field="makerName" data-halign="center" data-align="left">생산자제조사</th>
		        <th data-class="goodsOptionName" data-field="goodsOptionName" data-halign="center" data-align="left">단품명</th>
		        <th data-class="pickQty" data-field="pickQty" data-halign="center" data-align="right">피킹수량</th>
		        <th data-class="stateCode" data-field="stateCode" data-halign="center" data-align="center" data-formatter="stateCode">진행상태</th>
		        <th data-class="salesGoodsCode" data-field="salesGoodsCode" data-halign="center" data-align="center">판매상품코드</th>
		        <th data-class="taxType" data-field="taxType" data-halign="center" data-align="center">과세구분코드</th>
		        <th data-class="categoryLarge" data-field="categoryLarge" data-halign="center" data-align="left">대카테고리명</th>
		        <th data-class="categoryMiddle" data-field="categoryMiddle" data-halign="center" data-align="left">중카테고리명</th>
		        <th data-class="changeAllowYn" data-field="changeAllowYn" data-halign="center" data-align="center">상품대체요청여부</th>
		        <th data-class="changeGoodsCode" data-field="changeGoodsCode" data-halign="center" data-align="left">대체경통코드</th>
		        <th data-class="changePickQty" data-field="changePickQty" data-halign="center" data-align="right">대체수량</th>
		        <th data-class="changeGoodsName" data-field="changeGoodsName" data-halign="center" data-align="left">대체상품명</th>
		        <th data-class="changeGoodsCost" data-field="changeGoodsCost" data-halign="center" data-align="right">대체상품단가</th>
		        <th data-class="reasonQty" data-field="reasonQty" data-halign="center" data-align="right">결품수량</th>
		        <th data-class="scanGoodsCode" data-field="scanGoodsCode" data-halign="center" data-align="right">스캔상품코드</th>
		        <th data-class="promotionName" data-field="promotionName" data-halign="center" data-align="left">프로모션명</th>
		        <th data-class="changeAllowYn" data-field="changeAllowYn" data-halign="center" data-align="left">상품대체요청여부</th>
		        <!--  <th data-class="payMethod" data-field="payMethod" data-halign="center" data-align="right">결제수단</th> 
		        <th data-class="ePayCost" data-field="ePayCost" data-halign="center" data-align="right">e-주문금액</th> 
		        <th data-class="dlvryPayCost" data-field="dlvryPayCost" data-halign="center" data-align="right">택배주문금액</th> 
		        <th data-class="eSentFee" data-field="eSentFee" data-halign="center" data-align="right">e-배송비</th> 
		        <th data-class="dlvrySentFee" data-field="dlvrySentFee" data-halign="center" data-align="right">택배배송비</th> 
		        <th data-class="eCardDc" data-field="eCardDc" data-halign="center" data-align="right">e-카드즉시할인</th> 
		        <th data-class="dlvryCardDc" data-field="dlvryCardDc" data-halign="center" data-align="right">택배카드즉시할인</th>
		        <th data-class="couponDc" data-field="couponDc" data-halign="center" data-align="right">쿠폰할인</th> 
		        <th data-class="payCost" data-field="payCost" data-halign="center" data-align="right">결제금액</th> -->
		    </tr>
		    </thead>
		</table>
		<!-- /피킹 상세 데이터 -->
		<!--  /피킹 헤더 데이터 -->
	<!-- //데이터 영역 -->		
		 
	</div>
	<!-- container -->
	<!-- 처리 스크립트 -->
    <script>
    	//==============================================================================================================================
    	// 배송일자에 따른 차수 셋팅
    	function setDeliveryCount(target, data){
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
    	
    	function enterkey(){
    		if(window.event.keyCode == 13){
	   			$(".btnSearch").click();
    		}
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
	    	var $detailTable = $('#pickDetailList');
	    	
	    	//==============================================================================================================================
			/*   $.ajax({
				 	type : "POST",
			  	 	url : "<c:url value='/pickStateList.do'/>,
			  	 	cache : false ,
			  		dataType : "json",
			  		data: formData,
			    	async: false,
			        success : function(data) {
			     
			   	 	var listHtml = "<option value='' selected='selected'>선택</option>";
			     
			     	if(data.result.length > 0){
			       		for(var i=0; i<data.result.length; i++){
			        	var res = data.result[i];     
			        
			        	listHtml += "<option value ='"+res.om_code+"' data-om_innb ='"+res.om_code+"' >"+ res.om_nm +"</option>";
			       	}
			   	}
	    		
	    			//$("#aaa").html(listHtml);  //jquery  */
	    		
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
	    		$("#searchForm").attr("action","/pickStateList.do");
	    		$("#searchForm").submit();
	    	});
	    	
	    	// 초기화 버튼 이벤트
	    	$(".btnReset").on("click",function(e){
	    		$("#search_zone_code").val("");
	    		$("#search_state_code").val("");
	    		$("#nCoupon_yn").val("");
	    		$("#search_short_order_no").val("");
	    		$("#search_delivery_area_name").val("");
	    		$("#search_goods_name").val("");
	    		$("#search_worker_id").val("");
	    	});
	    	
	    	// 선택 라벨출력 이벤트
	    	$(".btnChoiceLabelPrint").on("click",function(e){
	    		
				e.preventDefault();
	   	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
    	        var formData = JSON.stringify($headerTable.bootstrapTable('getSelections'));
	    		$.ajax({
	    			  type : "POST",
	    			  url : "<c:url value='/choiceLabelPrint.do'/>",
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
 	    		    	  waitingDialog.hide();
 	    		    	  $("#searchForm").attr("action","/pickStateList.do");
 	    		    	  $("#searchForm").submit();
    			   	  }

	    		});
	    	});
	    	
   	    	//==============================================================================================================================
   	    	// bootstrap table script
   	    	// formatShowingRows    /  pageFrom, pageTo, totalRows	/ 'Showing %s to %s of %s rows'
   	    	
			// 피킹헤더 데이터 로드
			$headerTable.bootstrapTable({data: <%= new ObjectMapper().writeValueAsString(request.getAttribute("pickHeaderList"))%>});
            
            // 피킹해더데이터 클릭시 상세정보 조회처리
            $headerTable.on('click-row.bs.table', function (row, element, field) {
            	// 선택시 해당 Row 색변경
        		$('.success').removeClass('success');
        		$(field).addClass('success');
        		// 상세데이터 가져와 데이터 집어넣기
	    		var formData = "pickId="+element.pickId;
	    		$.ajax({
	    			  type : "POST",
	    			  url : "<c:url value='/pickDetailList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    				  $detailTable.bootstrapTable('removeAll');
	    				  $detailTable.bootstrapTable('prepend', data.pickDetailList);
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    				}
	    		});
            });
            
         	// 피킹상세 데이터 로드 
			$detailTable.bootstrapTable({data : <%= new ObjectMapper().writeValueAsString(request.getAttribute("pickDetailList"))%>});
   	    	//==============================================================================================================================
   	    	// 데이터가 있는경우 첫행 선택 이벤트 처리
   	    	if($('#pickHeaderList tbody tr').length > 1){
		    	($('#pickHeaderList tbody tr td').eq(0)).trigger("click");
   	    	}
	    	
	    });
    	    		
    </script>

</body>
</html>
