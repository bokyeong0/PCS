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
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><spring:message code="title.nhmarket.pcs.main" /></title>

	<!-- 즐겨찾기 추가 및 숏컷 아이콘 -->
	<link rel="shortcut icon" href="/images/egovframework/hanaro.ico">
	<link rel="apple-touch-icon-precomposed" href="/images/egovframework/hanaro.ico">

	<!-- plugin css -->
    <link type="text/css" rel="stylesheet" href="<c:url value='/plugin/bootstrap/css/bootstrap.css'/>"/>
	<link type="text/css" rel="stylesheet" href="<c:url value='/plugin/bootstrap/css/bootstrap-cyborg.min.css'/>"/>

    <!-- default css -->
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/pcs/default.css'/>"/>

    <!-- plugin js -->
    <script src="<c:url value='/plugin/jquery-1.12.4.min.js'/>"></script>
    <script src="<c:url value='/plugin/bootstrap/js/bootstrap.js'/>"></script>
    <script src="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.js'/>"></script>

	<!-- 처리 스크립트 -->
    <script src="<c:url value='/plugin/RollingTableWidget/RollingTableWidget-1.0.js'/>"></script>
    <script src="<c:url value='/plugin/RollingTableWidget/jquery.timers-1.2.js'/>"></script>

    <style>
		@media (min-width: 1200px) {.container {width: 100%}}
		.totTable tr, .totTable td{font-size: 80px;}
		.stateTable {height: 950px;}
		.stateTable tr, .stateTable td{font-size: 60px;}
		.rollingtableheader tr, .rollingtableheader th{font-size: 40px; text-align: center; border-bottom: 3px solid #282828; padding-top: 15px; padding-bottom: 15px;}
		.col3 , .col4 , .col5 {text-align: center;}
    </style>
    <script type="text/javascript">
		// Object 비교 용
		jQuery.extend({
		    compare : function (a,b) {
		        var obj_str = '[object Object]',
		            arr_str = '[object Array]',
		            a_type  = Object.prototype.toString.apply(a),
		            b_type  = Object.prototype.toString.apply(b);

		            if ( a_type !== b_type) { return false; }
		            else if (a_type === obj_str) {
		                return $.compareObject(a,b);
		            }
		            else if (a_type === arr_str) {
		                return $.compareArray(a,b);
		            }
		            return (a === b);
		        }
		});

		jQuery.extend({
		    compareArray: function (arrayA, arrayB) {
		        var a,b,i,a_type,b_type;
		        // References to each other?
		        if (arrayA === arrayB) { return true;}

		        if (arrayA.length != arrayB.length) { return false; }
		        // sort modifies original array
		        // (which are passed by reference to our method!)
		        // so clone the arrays before sorting
		        a = jQuery.extend(true, [], arrayA);
		        b = jQuery.extend(true, [], arrayB);
		        a.sort();
		        b.sort();
		        for (i = 0, l = a.length; i < l; i+=1) {
		            a_type = Object.prototype.toString.apply(a[i]);
		            b_type = Object.prototype.toString.apply(b[i]);

		            if (a_type !== b_type) {
		                return false;
		            }

		            if ($.compare(a[i],b[i]) === false) {
		                return false;
		            }
		        }
		        return true;
		    }
		});

		jQuery.extend({
		    compareObject : function(objA, objB) {

		        var i,a_type,b_type;

		        // Compare if they are references to each other
		        if (objA === objB) { return true;}

		        if (Object.keys(objA).length !== Object.keys(objB).length) { return false;}
		        for (i in objA) {
		            if (objA.hasOwnProperty(i)) {
		                if (typeof objB[i] === 'undefined') {
		                    return false;
		                }
		                else {
		                    a_type = Object.prototype.toString.apply(objA[i]);
		                    b_type = Object.prototype.toString.apply(objB[i]);

		                    if (a_type !== b_type) {
		                        return false;
		                    }
		                }
		            }
		            if ($.compare(objA[i],objB[i]) === false){
		                return false;
		            }
		        }
		        return true;
		    }
		});

    </script>
</head>

<body>

	<!-- container -->
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<ul class="nav navbar-nav pull-left">
					<div><span class="chkBrowser text-danger ">${chkBrowser}</span></div>
				</ul>
				<ul class="nav navbar-nav pull-right">
					<li><h2 id="date_time"></h2>&nbsp;</li>
				</ul>
			</div>
		</div>
		<div class="row">

			<!-- 통계정보 -->
			<div class="col-lg-4">				
				<div class="panel panel-default">
					<div class="panel-heading"><h1>진행 현황표</h1></div>					
					<div class="panel-body">
						<table class="table table-bordered totTable">
							<tbody>
								<tr><th>금일 누계</th><td><p class="totalInfo text-danger text-right">${totalInfo[0].count}</p></td></tr>
								<tr><th><span class="deliveryCount text-danger ">${deliveryCount}</span>차 주문</th><td><p class="totalInfo text-danger text-right">${totalInfo[1].count}</p></td></tr>
								<tr><th>진척률(%)</th><td><p class="totalInfo text-warning text-right">${totalInfo[2].count}</p></td></tr>
								<tr><th>피킹 대기</th><td><p class="totalInfo text-danger text-right">${totalInfo[3].count}</p></td></tr>
								<tr><th>피킹 진행</th><td><p class="totalInfo text-danger text-right">${totalInfo[4].count}</p></td></tr>
								<tr><th>피킹 완료</th><td><p class="totalInfo text-danger text-right">${totalInfo[5].count}</p></td></tr>
							</tbody>
						</table>
				  </div>
				</div>
			</div>
			<div class="col-lg-8">
				<div class="panel panel-default">
				<!--
					<div class="panel-heading"><h3>진행 현황</h3></div>
				 -->
					<div class="panel-body">
						<table class="table table-bordered stateTable" id="stateList">
						</table>
				  </div>
				</div>
			</div>
		</div>

	</div>
	<!-- container -->


    <script>
	    var quoteData = null
		    ,quoteIndex = 0
		    ,oldData = new Object();

    	var isRolling = false;
    	var oldDataLoop = false;
    	
		//
		var jobStateCode = <%= new ObjectMapper().writeValueAsString(request.getAttribute("jobStateCode"))%>
			, infoTimeVal = jobStateCode[0].infoTime
			, maxRowCount = jobStateCode[0].rowCnt
			, skipTimeMin = jobStateCode[0].skipTime
			, stateTimeVal = jobStateCode[0].stateTime
			, rollingTimeVal = jobStateCode[0].rollingTime;

		// 설정값 확인용
// 		console.log(jobStateCode[0]);


	    // 데이터 롤링 처리
	    function rollingState()
	    {
	    	var dataLength = oldData.length;
	    	console.info(oldData);
	    	if(maxRowCount < dataLength){
		    	$('#stateList').rollingtable( 'addItem', setColumnData(oldData[quoteIndex]) );	    	
				quoteIndex++;
				quoteIndex %= oldData.length;
	    	}else{
	    		if(!oldDataLoop){
	    			
	    			for(var i = 0 ; i  < maxRowCount ; i++){
			    		$('#stateList').rollingtable( 'addItem', "");
	    			}
		    		for(var i = 0 ; i  < dataLength ; i++){
				    	$('#stateList').rollingtable( 'addItem', setColumnData(oldData[quoteIndex]) );	    	
						quoteIndex++;
						quoteIndex %= oldData.length;
		    		}
	    		}
	    		oldDataLoop = true;
	    	}
	    }
	    
		// 시간 타이머 자시자신 재귀호출
    	function startTime() {
    		var week = new Array('(일)', '(월)', '(화)', '(수)', '(목)', '(금)', '(토)');
        	var today = new Date();

    	    var month = today.getMonth()+1;
    	    var date = today.getDate();
    	    var day = today.getDay();

    	    var h = today.getHours();
    	    var m = today.getMinutes();
    	    var s = today.getSeconds();

    	    m = checkTime(m);
    	    s = checkTime(s);
    	    $('#date_time').text(month + "월 " + date + "일 " + week[day] + " " + h + ":" + m + ":" + s);
    	    
    	    // 시간표시 타이머
    	    setTimeout(function () {
    	        startTime()
    	    }, 500);
    	}

    	// 10보다 작을때 앞에 0 생성
    	function checkTime(i) {
    	    if (i < 10) {
    	        i = "0" + i;
    	    }
    	    return i;
    	}


    	// 표시될 array 데이터 생성(Row단위)
    	function setColumnData(dataList){

    		var areaNameList = "";
    		var areaName = "";

    		var timeList = "";
    		var time = "";

    		var rowData = new Array();

    		// 데이터가 있는경우 실행
    		if(!$.isEmptyObject(dataList)){
				// 배송지역 뒤 글자만 나오게 처리
    			areaNameList = (dataList.deliveryAreaName).split(" ");
    			areaName = areaNameList[areaNameList.length - 1];
    			timeList = (dataList.endTime).split(" ")
    			time = timeList[timeList.length - 1];

				// 특정시간 조정
	    		rowData.push(dataList.shortOrderNo + "(" + dataList.orderCustomerName + ")");
	    		rowData.push(areaName);
	    		rowData.push(time);
	    		rowData.push(dataList.orgItemQty + "(" + dataList.orgQty + ")");
	    		rowData.push(dataList.pickItemQty + "(" + dataList.pickQty + ")");
    		}
    		return rowData;
    	}

    	// 통계정보 데이터
    	function getTotalInfoData(){

    		$.ajax({
  			  type : "POST",
  			  url : "<c:url value='/jobStateTotalInfo.do'/>",
  			  cache : false,
  			  dataType : "json",
  			  success : function(data) {
  				setTotalInfoData(data.totalInfo, data.searchVO);
  			    //Sucess시, 처리
  			  },
  			  error : function(jqXHR){
  				location.reload(true);
  			    //Error시, 처리
    		  },
  			  complete : function(){
  				 // 무조건 처리
  		   	  }
  			});
    	}

    	// 20171201 개선 요망
    	// 진행 현황 데이터 조회
    	function getStateListData(){

    		$.ajax({
  			  type : "POST",
  			  url : "<c:url value='/jobStateStateList.do'/>",
  			  cache : false,
  			  dataType : "json",
  			  success : function(data) {
				  setStateListData(getSkipData(data.stateList));
				  //callPrint();
  			    //Sucess시, 처리
  			  },
  			  error : function(jqXHR){  			
  				location.reload(true);
  			    //Error시, 처리
    		  },
  			  complete : function(){
  				// 무조건 처리
  				chkBrowser();
  				//callPrint();
  				
  		   	  }
  			});
    	}

    	// 통계정보 데이터 셋팅
		function setTotalInfoData(dataList, searchVO){

			// 차수 설정
			$('.deliveryCount').text(searchVO.search_delivery_count);

			// 각통계현황 셋팅(순서대로)
			var listCnt = dataList.length;
			for(var i = 0 ; i < listCnt ; i++ ){
				$('.totalInfo').eq(i).text(dataList[i].count);
			}
		}

    	// 현황 정보 데이터 셋팅 (데이터 비교후 addItem 처리 및 롤링 처리)
		function setStateListData(dataList){
			////////////////////////////////////////////////////////////////////////////////////////////
			// 데이터 비교후 다른 경우 데이터 추가 처리(자동 타이밍에 따라 롤링됨)
			if(!$.compare(oldData,dataList)){
				oldData = dataList;
				oldDataLoop = false;
				getTotalInfoData();				
			}
			////////////////////////////////////////////////////////////////////////////////////////////
			// A20170327 osj 갑자기 롤링 테이블이 반응 없는 현상이 발생하여 추가
			else{
				getTotalInfoData();
			}
		}
    	
    	// 자동 프린터 호출
    	/* function callPrint(){
    		
    		$.ajax({
    			  type : "POST",
    			  url : "<c:url value='/pcsPrint.do'/>",
    			  cache : false,
    			  dataType : "json",
    			  success : function(data) {
    			    //Sucess시, 처리
    			  },
    			  error : function(jqXHR){
    			    //Error시, 처리
      		  },
    			  complete : function(){
    				 // 무조건 처리
    		   	  }
    		});
    	} */
    	
    	// 스킵타임 년/월/일 시:분
    	function isSkipTime(skipDateTime , skipTimes){
    		var curDate = new Date();
	    	var rowDate = new Date(skipDateTime);

	    	if(Math.floor(((curDate.getTime() - rowDate.getTime())/1000)/60) > skipTimes){
	    		return true;
	    	}else{
	    		return false;
	    	};
	    	return false;

    	}
		// 시간지난 데이터 스킵 후 정렬처리
		function getSkipData(dataList){
			var dataCnt = dataList.length;
 			var skipDataList = new Array();

			// 안지난 데이터 Object에 추가 처리
			for(var i = 0 ; i < dataCnt ; i++){
				if(!isSkipTime(dataList[i].endTime , skipTimeMin)){
					skipDataList.push(dataList[i]);
				}
			}
			// 데이터 정렬 처리
			skipDataList.sort(function(data1,data2){
				// 1 완료시간, 2 이름 , 3 지역, 4 단축번호
				var idx = 4;
				var sort;

				switch(idx){
					case 1:
						data1.endTime = data1.endTime.toString().toLowerCase();
						data2.endTime = data2.endTime.toString().toLowerCase();
						sort = (data1.endTime < data2.endTime) ? -1 : ( (data1.endTime > data2.endTime) ? 1 : 0);
						break;
					case 2:
						data1.orderCustomerName = data1.orderCustomerName.toString().toLowerCase();
						data2.orderCustomerName = data2.shortOrderNo.toString().toLowerCase();
						sort = (data1.orderCustomerName < data2.orderCustomerName) ? -1 : ( (data1.orderCustomerName > data2.orderCustomerName) ? 1 : 0);
						break;
					case 3:
						data1.areaName = data1.areaName.toString().toLowerCase();
						data2.areaName = data2.areaName.toString().toLowerCase();
						sort = (data1.areaName < data2.areaName) ? -1 : ( (data1.areaName > data2.areaName) ? 1 : 0);
						break;
					case 4:
						data1.shortOrderNo = data1.shortOrderNo.toString().toLowerCase();
						data2.shortOrderNo = data2.shortOrderNo.toString().toLowerCase();
						sort = (data1.shortOrderNo < data2.shortOrderNo) ? -1 : ( (data1.shortOrderNo > data2.shortOrderNo) ? 1 : 0);
						break;
				}
				return sort;
			});

			// 데이터 정렬 처리
			skipDataList.sort(function(data1,data2){
				// 1 완료시간, 2 이름 , 3 지역, 4 단축번호
				var idx = 1;
				var sort;

				switch(idx){
					case 1:
						data1.endTime = data1.endTime.toString().toLowerCase();
						data2.endTime = data2.endTime.toString().toLowerCase();
						sort = (data1.endTime < data2.endTime) ? -1 : ( (data1.endTime > data2.endTime) ? 1 : 0);
						break;
					case 2:
						data1.orderCustomerName = data1.orderCustomerName.toString().toLowerCase();
						data2.orderCustomerName = data2.shortOrderNo.toString().toLowerCase();
						sort = (data1.orderCustomerName < data2.orderCustomerName) ? -1 : ( (data1.orderCustomerName > data2.orderCustomerName) ? 1 : 0);
						break;
					case 3:
						data1.areaName = data1.areaName.toString().toLowerCase();
						data2.areaName = data2.areaName.toString().toLowerCase();
						sort = (data1.areaName < data2.areaName) ? -1 : ( (data1.areaName > data2.areaName) ? 1 : 0);
						break;
					case 4:
						data1.shortOrderNo = data1.shortOrderNo.toString().toLowerCase();
						data2.shortOrderNo = data2.shortOrderNo.toString().toLowerCase();
						sort = (data1.shortOrderNo < data2.shortOrderNo) ? -1 : ( (data1.shortOrderNo > data2.shortOrderNo) ? 1 : 0);
						break;
				}
				return sort;
			});
			
			return skipDataList;
		}

		// 20171201 개선 요망
    	// 페이지 로딩시 처리
	    $(function(){

	    	// 오른쪽 상단 시간 표시
    		startTime();

    		// 롤링테이블 셋팅
	    	$('#stateList').rollingtable({
				columns:["단축주문(고객명)","배송지역","피킹완료","주문(수량)","완료(수량)"]
	    		,maxRowCount: maxRowCount
	    		,textColourTop: "#FFFFFF"
	    		,textColourBottom: "#FFFFFF"
	    	});

	    	// 최초 데이터 가져오기
	    	getTotalInfoData();
			getStateListData();

	    	// 시간에 따른 데이터 가져오기
	    	var infoTimer= setInterval(function (){ getTotalInfoData()}, infoTimeVal * 1000);
	    	// 상태 목록 타이머
	    	var stateListTimer = setInterval(function (){ getStateListData()}, stateTimeVal * 1000);
	    	// 롤링 타이머
	    	var rollingTimer = setInterval(function (){ rollingState()}, rollingTimeVal * 1000);	    	

	    });
    	
    	function chkBrowser(){
		    /**
		     * Gets the browser name or returns an empty string if unknown. 
		     * This function also caches the result to provide for any 
		     * future calls this function has.
		     *
		     * @returns {string}
		     */
		    var browser = function() {
		        // Return cached result if avalible, else get result then cache it.
		        if (browser.prototype._cachedResult)
		            return browser.prototype._cachedResult;
	
		        // Opera 8.0+
		        var isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
	
		        // Firefox 1.0+
		        var isFirefox = typeof InstallTrigger !== 'undefined';
	
		        // Safari 3.0+ "[object HTMLElementConstructor]" 
		        var isSafari = /constructor/i.test(window.HTMLElement) || (function (p) { return p.toString() === "[object SafariRemoteNotification]"; })(!window['safari'] || safari.pushNotification);
	
		        // Internet Explorer 6-11
		        var isIE = /*@cc_on!@*/false || !!document.documentMode;
	
		        // Edge 20+
		        var isEdge = !isIE && !!window.StyleMedia;
	
		        // Chrome 1+
		        var isChrome = !!window.chrome && !!window.chrome.webstore;
	
		        // Blink engine detection
		        var isBlink = (isChrome || isOpera) && !!window.CSS;
	
		        return browser.prototype._cachedResult =
		            isOpera ? 'Opera' :
		            isFirefox ? 'Firefox' :
		            isSafari ? 'Safari' :
		            isChrome ? 'Chrome' :
		            isIE ? 'IE' :
		            isEdge ? 'Edge' :
		            "Custom-made";
		    };
	
		    var br = browser();
		    
		    $('.chkBrowser').text(br);
		    
		    $.ajax({
  			  type : "GET",
  			  url : "<c:url value='/chkBrowser.do'/>",
  			  cache : false,
  			  data : "chkBrowser="+br,
  			  dataType : "json",
  			  success : function(data) {
  			    //Sucess시, 처리
  			  },
  			  error : function(jqXHR){
  			    //Error시, 처리
    		  },
  			  complete : function(){
  				 // 무조건 처리
  		   	  }
  			});
    	}
    </script>
</body>
</html>
