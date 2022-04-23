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
		  <h1>주문진행현황조회 </h1>
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
<!-- 		       					<button type="button" class="btn btn-primary btnChoicePDFPrint" style="height: 80px;"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>선택 <br>리포트출력</button> -->
<!-- 		       					<button type="button" class="btn btn-primary" id="btnExcelExport" style="height: 80px;"><span class="glyphicon glyphicon-download" aria-hidden="true">&nbsp;</span>피킹완료 <br>엑셀다운로드</button> -->
<!-- 		       					<button type="button" class="btn btn-primary" id="btnChoiceExcelExport" style="height: 80px;"><span class="glyphicon glyphicon-download" aria-hidden="true">&nbsp;</span>선택엑셀<br><span style="margin-right: 30px"></span>다운로드</button> -->
		       				</td>
	       				</tr>
					
						<tr>	
							<td>
							
								<form:input path="search_short_order_no" cssClass="form-control" placeholder="단축주문번호" size="10" onkeyup="enterkey();"/>
								<form:input path="search_delivery_area_name" cssClass="form-control" placeholder="배송구명" size="12" onkeyup="enterkey();"/>
								<form:input path="search_goods_name" cssClass="form-control" placeholder="상품명" size="20" onkeyup="enterkey();"/>
								<form:input path="search_worker_id" cssClass="form-control" placeholder="피커명" size="15" onkeyup="enterkey();"/>
								
								<%-- <form:input path="search_org_order_no" cssClass="form-control" placeholder="원주문번호"/> --%>					
							</td>
						</tr>
					</table>
					<!-- 처리버튼 영역 -->
					<div class="button-group pull-right" style="margin-top: 10px;">
						<button type="button" class="btn btn-primary btnChoicePDFPrint"><span class="glyphicon glyphicon-print" aria-hidden="true">&nbsp;</span>선택 리포트출력</button>
<!-- 						Excel 다운로드 버튼 -->
						<button type="button" class="btn btn-primary" id="btnExcelExport"><span class="glyphicon glyphicon-download" aria-hidden="true">&nbsp;</span>피킹완료 엑셀다운로드</button>
<!-- 						선택 Excel 다운로드 버튼 180725 -->
						<button type="button" class="btn btn-primary" id="btnChoiceExcelExport"><span class="glyphicon glyphicon-download" aria-hidden="true">&nbsp;</span>선택 엑셀다운로드</button>
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
		        <!-- <th data-class="Rn" data-field="rn" data-halign="center" data-align="center">NO.</th> -->
		        <th data-class="deliveryDate" data-field="deliveryDate" data-halign="center" data-align="center">배송일자</th>
		        <th data-class="deliveryCount" data-field="deliveryCount" data-halign="center" data-align="center">배송차수</th>
		        <th data-class="shortOrderNo" data-field="shortOrderNo" data-halign="center" data-align="center">단축주문번호</th>
		        <!-- <th data-class="orgOrderNo" data-field="orgOrderNo" data-halign="center" data-align="center">원주문번호</th> -->
		        <th data-class="orderDate" data-field="orderDate" data-halign="center" data-align="center">주문일자</th>
		        <th data-class="orderNo" data-field="orderNo" data-halign="center" data-align="center">주문번호</th>
		        <!-- <th data-class="orderRowNo" data-field="orderRowNo" data-halign="center" data-align="left">주문순번</th> -->
		        <th data-class="deliveryAreaName" data-field="deliveryAreaName" data-halign="center" data-align="center" data-width="80px">권역명</th>
		        <th data-class="orderCustomerName" data-field="orderCustomerName" data-halign="center" data-align="center">수취인명</th>
		        <!-- <th data-class="customerName" data-field="customerName" data-halign="center" data-align="center">회원명</th> -->
		        <!-- <th data-class="trayNo" data-field="trayNo" data-halign="center" data-align="right">트레이번호</th> -->
		        <!-- <th data-class="zoneCode" data-field="zoneCode" data-halign="center" data-align="center">존코드</th> -->		        
		        <!-- <th data-class="labelPrintCount" data-field="labelPrintCount" data-halign="center" data-align="right">출력횟수</th> -->
		        <!-- <th data-class="trolleyId" data-field="trolleyId" data-halign="center" data-align="right">트롤리ID</th> -->
		        <!-- <th data-class="workerId" data-field="workerId" data-halign="center" data-align="left">작업자</th> -->
		        <th data-class="stateCode" data-field="stateCode" data-halign="center" data-align="center" data-formatter="stateCode">진행상태</th>
		        <!-- <th data-class="deliveryAmount" data-field="deliveryAmount" data-halign="center" data-align="right">배송비</th> -->
		        <!-- <th data-class="deliveryMessage" data-field="deliveryMessage" data-halign="center" data-align="left">배송메시지</th> -->
		        <!-- <th data-class="freeGiftName" data-field="freeGiftName" data-halign="center" data-align="center">사은품대상</th> -->
		        <th data-class="printYn" data-field="printYn" data-halign="center" data-align="center">리포트출력여부</th>
		        <th data-class="changeAllowYn" data-field="changeAllowYn" data-halign="center" data-align="center">상품대체요청여부</th>
		        <th data-class="pickType" data-field="pickType" data-halign="center" data-align="center" class="pickType">피킹유형</th>
		    </tr>
		    </thead>
		</table>
		<!--  /피킹 헤더 데이터 -->
	<!-- //데이터 영역 -->		
		 
	</div>
	<!-- container -->
	<!-- 처리 스크립트 -->
    <script>
    	var $pickData;
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
    	/* function setZoneCode(target, data){
    		var dataArr = [];
    		var idx = 0;
    		target.empty();
   			dataArr[idx++] = "<option value=''>존코드(전체)</option> ";
   			
    		$(data).each( function() {
    			dataArr[idx++] = "<option value=" + this.zoneCode + ">" + this.zoneCode+ "</option> ";
    		});
    		target.append(dataArr);    		
    	} */
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
	    	
	    	//==============================================================================================================================

	    		
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
	    			  }, 
	    			  error : function(jqXHR){
	    			    //Error시, 처리
	    			}
	    			});
	    		
	    	});
	    	
	    	// 배송차수 변경시 검색버튼 Event 처리
	    	$('#search_delivery_count').on("change",function(){
	    		$(".btnSearch").click();
	    	});
	    	
	    	// 검색버튼 이벤트
	    	$(".btnSearch").on("click",function(e){
	    		$("#searchForm").attr("action","/printStateList.do");
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
	    	
	    	// excel파일 다운로드 처리
	    	// 미완료된 주문건이 있을시 엑셀 다운로드 불가 처리 ljm_20180409
   	    	$('#btnExcelExport').on('click',function(e){
   	    		if($pickData.data.length < 1) {
   	    			alert("엑셀 다운로드 건이 존재하지 않습니다 \n대상건을 확인 바랍니다.");
   	    			return;
   	    		}
//    	    		180703 state_code 체크 로직 제거
//    	    		var isNotComplete = false;
//    	    		for(i = 0; i<$pickData.data.length; i++){
//    	    			if($pickData.data[i].stateCode != '2'){
//    	    				isNotComplete = true;
//    	    				break;   	    				
//    	    			}   	    			
//    	    		}
//    	    		if(isNotComplete){
//    	    			alert("피킹 완료되지 않은 주문 건이 있습니다. \n해당 회차의 피킹 완료 후에 다운로드 가능합니다.");
//    	    			return;
//    	    		}
   	    	 	
				var formData = $("#searchForm").serialize();
   	    		$.ajax({
	    			  type : "POST",
	    			  url : "<c:url value='/excelPickingResultListCnt.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
	    			    var search_delivery_date = $("#search_delivery_date").val();
	    			    var search_delivery_count = $("#search_delivery_count option:selected").text();
	    			    var totalCnt = data.totalCnt;
	    			    var nCouponOrderCnt = data.nCouponOrderCnt;
	    			    var minusCnt = totalCnt - nCouponOrderCnt;
	    			    
	    			    var y = search_delivery_date.substring(0, 4);
	    			    var m = search_delivery_date.substring(4, 6);
	    			    var d = search_delivery_date.substring(6, 8);
	    			    
	    			    if(confirm(y+"년 "+m+"월 "+d+"일 "+search_delivery_count+" 피킹완료정보를 다운로드합니다. \n(총 "+totalCnt+"건 / 다운로드 "+minusCnt+"건 / N쿠폰제외 "+nCouponOrderCnt+"건)")){
	    			    	$.ajax({
	    		    			  type : "POST",
	    		    			  url : "<c:url value='/excelPickingResultList.do'/>",
	    		    			  cache : false,
	    		    			  dataType : "json",
	    		    			  data : formData,
	    		    			  success : function(data) {
	    		    				  alert("\""+data.excelPath+"\"에 다운로드 완료되었습니다.");
	    		    			  }
	    			    	});
	    			    }
	    			  }, 
	    			  error : function(jqXHR){
	    			  }
	    			});

   	    	});	  
	    	
	    	// 180725 선택 주문 엑셀 다운로드
   	    	$('#btnChoiceExcelExport').on('click',function(e){
				var formData = JSON.stringify($headerTable.bootstrapTable('getSelections'));
				// 진행상태 체크
    	        var chkVal = jQuery.parseJSON(formData);
    	        if(chkVal == ''){
    	        	alert("최소 하나는 선택하셔야 합니다.");   
    	        	return;
    	        }
    	        
   	    		$.ajax({
	    			  type : "POST",
	    			  url : "<c:url value='/choiceExcelPickingResultListCnt.do'/>",
	    			  cache : false,
	    			  dataType : "json",
	    			  contentType: "application/json; charset=UTF-8",
	    			  data : formData,
	    			  success : function(data) {
	    			    //Sucess시, 처리
	    			    var search_delivery_date = $("#search_delivery_date").val();
	    			    var search_delivery_count = $("#search_delivery_count option:selected").text();
	    			    var totalCnt = data.totalCnt;
	    			    var nCouponOrderCnt = data.nCouponOrderCnt;
	    			    var minusCnt = totalCnt - nCouponOrderCnt;
	    			    
	    			    var y = search_delivery_date.substring(0, 4);
	    			    var m = search_delivery_date.substring(4, 6);
	    			    var d = search_delivery_date.substring(6, 8);
	    			    
	    			    if(confirm(y+"년 "+m+"월 "+d+"일 "+search_delivery_count+" 피킹완료정보를 다운로드합니다. \n(총 "+totalCnt+"건 / 다운로드 "+minusCnt+"건 / N쿠폰제외 "+nCouponOrderCnt+"건)")){
	    			    	$.ajax({
	    		    			  type : "POST",
	    		    			  url : "<c:url value='/choiceExcelPickingResultList.do'/>",
	    		    			  cache : false,
	    		    			  dataType : "json",
	    		    			  contentType: "application/json; charset=UTF-8",
	    		    			  data : formData,
	    		    			  success : function(data) {
	    		    				  alert("\""+data.excelPath+"\"에 다운로드 완료되었습니다.");
	    		    			  }
	    			    	});
	    			    }
	    			  }, 
	    			  error : function(jqXHR){
	    			  }
	    			});

   	    	});	  
	    	
	    	
	    	
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
    	    		$("#searchForm").attr("action","/printStateList.do");
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
    	    	        	alert("해당 번호( "+value.orderDate+value.orderNo+" )는 작업이 완료되지 않았습니다."+"\n피킹완료된 주문만 리포트 출력이 가능합니다.");			    	        
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
	 	    		    	  $("#searchForm").attr("action","/printStateList.do");
	 	    		    	  $("#searchForm").submit();
	    			   	  }
	
		    		});
    	        }
	    	});
	    	
   	    	//==============================================================================================================================
   	    	// bootstrap table script
   	    	// formatShowingRows    /  pageFrom, pageTo, totalRows	/ 'Showing %s to %s of %s rows'
			// 피킹헤더 데이터 로드
			$pickData = {data : <%= new ObjectMapper().writeValueAsString(request.getAttribute("pickHeaderList"))%>};
			$headerTable.bootstrapTable($pickData);
			$headerTable.bootstrapTable('hideColumn', 'pickId');
			
			// 요청(18.10.03) : 대체+결품은 파란폰트 / 결품만 있는 주문은 붉은 폰트 
			$("#pickHeaderList tbody tr").each(function(){
				if($(this).find(".pickType").text()=='대체'){
					$(this).css('color', 'blue');
	    		}else if($(this).find(".pickType").text()=='결품'){
	    			$(this).css('color', 'red');
	    		}
			})
	    });
    	
    </script>

</body>
</html>
