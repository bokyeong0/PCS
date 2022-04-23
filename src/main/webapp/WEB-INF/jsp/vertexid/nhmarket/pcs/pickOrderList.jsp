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
<%@include file="cmmn/header.jsp" %>
<body>

	<!-- 상단 메뉴  -->
	<%@include file="cmmn/menu.jsp" %>
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
			
				<!-- Hidden 데이터 -->
 				<form:form commandName="searchVO" id="searchForm" name="searchForm" method="post">			
					<form:hidden path="search_delivery_date" cssClass="form-control" />
	       			<form:hidden path="search_delivery_count" cssClass="form-control" />
	       			<form:hidden path="LabelPrintType" cssClass="form-control" id="LabelPrintType"/>
				</form:form>
				<!-- 처리버튼 영역 -->
				<div class="button-group pull-right">
					<button type="button" class="btn btn-primary btnUpload" data-toggle="modal" data-target="#importData"><span class="glyphicon glyphicon-upload" aria-hidden="true">&nbsp;</span>주문 업로드</button>
					<button type="button" class="btn btn-primary btnPicking"><span class="glyphicon glyphicon-ok" aria-hidden="true">&nbsp;</span>피킹지시</button>
					<button type="button" class="btn btn-primary btnLabelPrint"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>존별 라벨 출력</button>
					<button type="button" class="btn btn-primary btnLabelPrintOrderNo"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>주문별 라벨 출력</button>
					<button type="button" class="btn btn-primary btnPrePcking" data-toggle="modal" data-target="#prePckData"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>사전피킹리스트</button>
				</div>
				
			</div>
		</div>
		<!-- /검색 영역 -->
		
		<h4><b>배송회차 : ${recentDeliveryCount}차 / 주문건수 : ${orderCnt}건 (N쿠폰주문 : ${nCouponCnt}건) / 아이템수 : ${orderItemCnt}개</b></h4>

		<!-- Order 데이터 -->
		<table id = "orderList" class="table table-striped">

		    <thead>
		    <tr>
		    	<th data-class="shortOrderNo" data-field="shortOrderNo" data-halign="center" data-align="center">단축주문번호</th>
		        <!-- <th data-class="deliveryDate" data-field="deliveryDate" data-halign="center" data-align="center" >배송일자</th>
		        <th data-class="deliveryCount" data-field="deliveryCount" data-halign="center" data-align="center">배송회차</th> -->
		        <!-- <th data-class="orgOrderNo" data-field="orgOrderNo" data-halign="center" data-align="left">원주문번호</th> -->
		        <th data-class="orderDate" data-field="orderDate" data-halign="center" data-align="left">주문일자</th>
		        <th data-class="orderNo" data-field="orderNo" data-halign="center" data-align="center">주문번호</th>
		        <th data-class="orderRowNo" data-field="orderRowNo" data-halign="center" data-align="center">주문순번</th>
		        <th data-class="deliveryAreaName" data-field="deliveryAreaName" data-halign="center" data-align="center">권역구명</th>
		        <th data-class="orderCustomerName" data-field="orderCustomerName" data-halign="center" data-align="center">수취인명</th>
		        <!-- <th data-class="customerName" data-field="customerName" data-halign="center" data-align="center">회원명</th> -->
		        <th data-class="stateCode" data-field="stateCode" data-halign="center" data-align="center" data-formatter="stateCode">진행상태</th>
		        <th data-class="goodsCode" data-field="goodsCode" data-halign="center" data-align="center">경제통합상품코드</th>
		        <th data-class="salesGoodsCode" data-field="salesGoodsCode" data-halign="center" data-align="center">상품코드</th>
		        <!-- <th data-class="goodsName" data-field="goodsName" data-halign="center" data-align="left" >상품명</th> -->
		        <th data-class="nCouponCost" data-field="nCouponCost" data-halign="center" data-align="left" data-width="120px">N쿠폰</th>
		        <th data-class="promotionName" data-field="promotionName" data-halign="center" data-align="left" data-width="120px">프로모션명</th>
		        <th data-class="orderQty" data-field="orderQty" data-halign="center" data-align="right">수량</th>
		        <th data-class="changeAllowYn" data-field="changeAllowYn" data-halign="center" data-align="center">상품대체요청여부</th>
		        <th data-class="zoneCode" data-field="zoneCode" data-halign="center" data-align="center">존정보</th>
		        <th data-class="goodsSpec" data-field="goodsSpec" data-halign="center" data-align="left">상품규격</th>
		        <th data-class="orderCost" data-field="orderCost" data-halign="center" data-align="right">상품금액</th>
		        <th data-class="makerName" data-field="makerName" data-halign="center" data-align="left">생산자제조사</th>
		        <th data-class="goodsOptionName" data-field="goodsOptionName" data-halign="center" data-align="left">단품명</th>
		        <th data-class="taxType" data-field="taxType" data-halign="center" data-align="center">과세구분코드</th>
		        <th data-class="deliveryAmount" data-field="deliveryAmount" data-halign="center" data-align="right">배송비</th>
		        <th data-class="categoryLarge" data-field="categoryLarge" data-halign="center" data-align="left">대카테고리명</th>
		        <th data-class="categoryMiddle" data-field="categoryMiddle" data-halign="center" data-align="left">중카테고리명</th>
		        <th data-class="freeGiftName" data-field="freeGiftName" data-halign="center" data-align="left">사은품명</th>
		        <th data-class="deliveryMessage" data-field="deliveryMessage" data-halign="center" data-align="left">배송요청내용</th>
		        <th data-class="goodsMessage" data-field="goodsMessage" data-halign="center" data-align="left">공급처요청내용</th>
		       <!--  <th data-class="payMethod" data-field="payMethod" data-halign="center" data-align="center">결제 수단 명</th>
		        <th data-class="ePayCost" data-field="ePayCost" data-halign="center" data-align="right">하나로 주문금액(점포배송)</th>
		        <th data-class="dlvryPayCost" data-field="dlvryPayCost" data-halign="center" data-align="right">택배 주문금액(택배배송)</th>
		        <th data-class="eSentFee" data-field="eSentFee" data-halign="center" data-align="right">하나로 배송비(점포배송)</th>
		        <th data-class="dlvrySentFee" data-field="dlvrySentFee" data-halign="center" data-align="right">택배 배송비(택배배송)</th>
		        <th data-class="eCardDc" data-field="eCardDc" data-halign="center" data-align="right">하나로 카드즉시할인(점포배송)</th>
		        <th data-class="dlvryCardDc" data-field="dlvryCardDc" data-halign="center" data-align="right">택배 카드즉시할인(택배배송)</th>
		        <th data-class="couponDc" data-field="couponDc" data-halign="center" data-align="right">쿠폰금액(합계)</th>
		        <th data-class="payCost" data-field="payCost" data-halign="center" data-align="right">결제금액</th> -->
		        
		    </tr>
		    </thead>
		</table>
		<!-- /Order 데이터 -->
		
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
       				
       				<form:checkbox path="search_state_code" value="2" style="margin-left:10px; margin-right:5px; vertical-align:middle"/><span style="vertical-align:middle;">피킹완료 제외</span>
					
					</div>    
					</form:form>
				</div>
			
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					<!-- <button type="button" class="btn btn-primary btnPrePckDataInqry"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>조회</button> -->
					<button type="button" class="btn btn-primary btnPrePckData"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>출력</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
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
 					<form:form commandName="searchVO" cssClass="form-inline" id="importForm" name="importForm" method="post" enctype="multipart/form-data">
 					<div class="form-group">
 					
 						<form:hidden path="search_delivery_date" cssClass="form-control"/>
 						
						<!-- 배송차수 1~5차 픽스 -->
						<form:select path="search_delivery_count" cssClass="form-control" style="width: 150px">
						
	        				<!-- 배송차수 List -->        				
	        				<c:forEach var="result" items="${deliveryCountList}" varStatus="status">
	        					<form:option value="${result.deliveryCount}" label="${result.deliveryCount}차" />
	        				</c:forEach>
	        				
	        			</form:select>
	        			
	        			<!-- 파일 업로드 -->
					    <input class="form-control" type="file" name="fileToUpload" id="fileToUpload"/>
					</div>    
					</form:form>
				</div>
			
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					
					<button type="button" class="btn btn-primary btnImportData"><span class="glyphicon glyphicon-upload" aria-hidden="true">&nbsp;</span>업로드</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->	
	
	<!-- 처리 스크립트 -->
    <script>
	  //==============================================================================================================================
		// 배송일자에 따른 차수 셋팅
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
    
    
    	// 페이지 로딩시 처리
	    $(function(){
	    	var $orderTable = $('#orderList');
			// 오더 데이터 로드
			$orderTable.bootstrapTable({data: <%= new ObjectMapper().writeValueAsString(request.getAttribute("orderList"))%>});
			
	    	// 주문 업로드 이벤트 (모달창 통해서 처리)
	    	$(".btnImportData").on("click",function(e){
    	        $(".btn").attr("disabled",true);	    		
	    		waitingDialog.show();
	    		e.preventDefault();
	    		
	    		// 차수 선택 체크 
	    		if($("#importForm #search_delivery_count").val()==""){
	    			alert("배송차수를 선택하세요");
		    		$(".btn").attr("disabled",false);	  
		    		waitingDialog.hide();		    		
	    			$("#importForm #search_delivery_count").focus();
	    			return;
	    		};
	    		
	    		// 업로드 선택 체크 
    			if($("#importForm #fileToUpload").val()==""){
    				alert("업로드할 파일을 선택하세요");
		    		$(".btn").attr("disabled",false);
		    		waitingDialog.hide();
	    			$("#importForm #fileToUpload").focus();
   					return;
   				};
	    		$("#importForm").attr("action","/orderImportData.do");
	    		$("#importForm").preventDoubleSubmission().submit();
	    	});
	    	
	    	// 피킹지시 이벤트
	    	$(".btnPicking").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
	    		e.preventDefault();
	    		$("#searchForm").attr("action","/picking.do");
	    		$("#searchForm").preventDoubleSubmission().submit();
	    	});
	    	
	    	
	    	// 라벨출력 이벤트
	    	$(".btnLabelPrint").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
				e.preventDefault();
	    		var formData = $("#searchForm").serialize();
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/labelPrintCount.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
	    			    if(data.labelPrintCount>1){
	    			    	var result = confirm("이미 출력된 라벨이 있습니다. \n이어서 출력 하시겠습니까?");
	    			    	if(result){
		    		    		$("#searchForm").attr("action","/labelPrint.do");
		    		    		$("#searchForm").preventDoubleSubmission().submit();
	    			    	}else{
	    			    		$(".btn").attr("disabled",false);	  
	    			    		waitingDialog.hide();		    		
	    			    	}
	    			    }else{
	    		    		$("#searchForm").attr("action","/labelPrint.do");
	    		    		$("#searchForm").preventDoubleSubmission().submit();
	    			    }
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    		});
	    	});
	    	
	    	// 주문번호별 라벨출력
	    	$(".btnLabelPrintOrderNo").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
				e.preventDefault();
				$("#LabelPrintType").val("order");
	    		var formData = $("#searchForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/labelPrintCount.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
	    			    if(data.labelPrintCount>1){
	    			    	var result = confirm("이미 출력된 라벨이 있습니다. \n이어서 출력 하시겠습니까?");
	    			    	if(result){
		    		    		$("#searchForm").attr("action","/labelPrint.do");
		    		    		$("#searchForm").preventDoubleSubmission().submit();
	    			    	}else{
	    			    		$(".btn").attr("disabled",false);	  
	    			    		waitingDialog.hide();		    		
	    			    	}
	    			    }else{
	    		    		$("#searchForm").attr("action","/labelPrint.do");
	    		    		$("#searchForm").preventDoubleSubmission().submit();
	    			    }
	    			    $("#LabelPrintType").val("");
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    		});
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
	    	
	    	// 모달 창을 클릭시 Event 처리
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
	    	
	    	
 	    	// 출력(사전피킹리스트) 이벤트
	    	$(".btnPrePckData").on("click",function(e){
    	        $(".btn").attr("disabled",true);
	    		waitingDialog.show();    	        
	    		e.preventDefault();
	    		$("#prePckForm").attr("action","/prePicking.do");
	    		$("#prePckForm").preventDoubleSubmission().submit();
	    	});
	    	
	    	// 조회(사전피킹리스트) 이벤트
// 	    	$(".btnPrePckDataInqry").on("click",function(e){
//     	        $(".btn").attr("disabled",true);
// 	    		waitingDialog.show();    	        
// 	    		e.preventDefault();
// 	    		$("#prePckForm").attr("action","/prePickingInquiry.do");
// 	    		$("#prePckForm").preventDoubleSubmission().submit();
// 	    	});
	    	
	    	// 주문업로드 Event 처리
	    	$(".btnUpload").on("click",function(e){
		    	// date 설정
		    	var now  = new Date();
	   			var year = now.getFullYear();
	   			var mon	 = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	   			var day	 = (now.getDate())>9 ? ''+(now.getDate()) : '0'+(now.getDate());
	   			var toDay = year + mon + day;
	      			 
	   		    $("#importForm #search_delivery_date").val(toDay); 
		    			    	
	    		var formData = $("#importForm").serialize();
	    		
	    		// ajax 처리
	    		$.ajax({
	    			  type : "GET",
	    			  url : "<c:url value='/toDayDeliveryCountList.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
					    setDeliveryCount($("#importForm #search_delivery_count"),data.deliveryCountList);  
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});	    		    		
	    		
	    	});
	    	
	    	
	    });
    	
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
    </script>
</body>
</html>
