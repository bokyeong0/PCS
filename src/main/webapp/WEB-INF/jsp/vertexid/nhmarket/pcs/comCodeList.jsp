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
		  <h1>공통코드관리</h1>
		</div>
		<!-- /페이지명 -->
		
		
		
		
		<!-- 검색 영역 -->
<!-- 		<div class="panel panel-default"> -->
<!-- 			<div class="panel-body"> -->
<%--  				<form:form commandName="searchVO" id="searchForm" name="searchForm" method="post">			 --%>
<!-- 				<div class="form-inline pull-right"> -->
<!-- 					<button type="button" class="btn btn-primary btnSearch"><span class="glyphicon glyphicon-search" aria-hidden="true">&nbsp;</span>검색</button> -->
<!-- 				</div> -->
<%-- 				</form:form> --%>
				
<!-- 			</div> -->
<!-- 		</div> -->
		<!-- /검색 영역 -->		
		
		
		
		<!-- 데이터 영역 -->
		<div class="panel panel-default">
			<div class="panel-body">
				<div class="row">
				
					<div class="col-lg-4">
					
						<!-- 추가 저장 버튼-->
						<div id="saveCode" class="button-group pull-right mb10">
							<button type="button" class="btn btn-primary" id="btnGroupAdd"><span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>추가</button>
							<button type="button" class="btn btn-primary" id="btnGroupSave"><span class="glyphicon glyphicon-ok" aria-hidden="true">&nbsp;</span>저장</button>
						</div>

					
						<!-- 그룹코드 목록 -->
						<table id = "comCodeGorupList" data-unique-id="comCode" class="table table-striped">
						    <thead>
						    <tr>
						        <th data-class="state" data-field="state" data-visible="false" ></th>
						        <th data-class="comCode" data-field="comCode" data-halign="center" data-align="left" >그룹코드</th>
						        <th data-class="comName" data-field="comName" data-halign="center" data-align="left">그룹코드명</th>
						        <th data-class="useYn" data-field="useYn" data-halign="center" data-align="center">사용유무</th>
						    </tr>
						    </thead>
						</table>
						<!-- /그룹코드 목록 -->
					</div>
					
					<div class="col-lg-8">
					
						<!-- 추가 저장 버튼-->
						<div id="saveCode" class="button-group pull-right mb10">
							<button type="button" class="btn btn-primary" id="btnAdd"><span class="glyphicon glyphicon-plus" aria-hidden="true">&nbsp;</span>추가</button>
							<button type="button" class="btn btn-primary" id="btnSave"><span class="glyphicon glyphicon-ok" aria-hidden="true">&nbsp;</span>저장</button>
						</div>

						<!-- 코드 목록 -->
						<table id = "comCodeList" data-unique-id="comCode" class="table table-striped">
						    <thead>
						    <tr>
						        <th data-class="state" data-field="state" data-visible="false"></th>
						        <th data-class="comGroupCode" data-field="comGroupCode" data-visible="false"></th>
						        <th data-class="comCode" data-field="comCode" data-halign="center" data-align="left">코드</th>
						        <th data-class="comName" data-field="comName" data-halign="center" data-align="left">코드명</th>
						        <th data-class="comOrder" data-field="comOrder" data-halign="center" data-align="right">정렬</th>
						        <th data-class="comSubCode" data-field="comSubCode" data-halign="center" data-align="left">서브코드</th>
						        <th data-class="comDesc" data-field="comDesc" data-halign="center" data-align="left">설명</th>
						        <th data-class="useYn" data-field="useYn" data-halign="center" data-align="center">사용여부</th>
						    </tr>
						    </thead>
						</table>						
						<!-- /코드 목록 -->
					
					</div>
				</div> <!-- /end row -->
			
			</div> <!-- /end panel-body -->
		</div>	<!-- /end panel -->
			
	</div>
	<!-- container -->
	
	<!-- 처리 스크립트 -->
    <script>
    
    
    	// 페이지 로딩시 처리
	    $(function(){
	    	
	    	var $groupTable = $('#comCodeGorupList')	    	
	    		, $codeTable = $('#comCodeList')
	    		, groupData = <%= new ObjectMapper().writeValueAsString(request.getAttribute("comCodeGorupList"))%>
	    		, codeData = <%= new ObjectMapper().writeValueAsString(request.getAttribute("comCodeList"))%>
    			, selComGroupCode
    			, selComGroupRow;
	    		
	    	// TODO 사용여부에 따라 삭제 필요
		    var oldGroupData = <%= new ObjectMapper().writeValueAsString(request.getAttribute("comCodeGorupList"))%>
		    	, oldCodeData = <%= new ObjectMapper().writeValueAsString(request.getAttribute("comCodeList"))%>;

			// 오더 데이터 로드
			$groupTable.bootstrapTable({data: groupData});
			$codeTable.bootstrapTable({data: codeData});
			
			// 그룹코드 클릭 이벤트
            $groupTable.on('click-row.bs.table', function (row, element, field) {
            	
            	// 선택시 해당 Row 색변경
        		$('.success').removeClass('success');
        		$(field).addClass('success');
        		
        		// 선택한 groupCode 변수에 셋팅
            	selComGroupCode = element.comCode
        		, selComGroupRow = field.index();
        		
	    		var formData = "search_com_group_code="+selComGroupCode;
	    		
	    		// comcode가 없는경우 코드목록 안가져오기
	    		if(element.comCode !=""){
		    		$.ajax({
		    			  type : "POST",
		    			  url : "<c:url value='/allComCodeListJson.do'/>",
		    			  cache : false,
		    			  dataType : "json",
		    			  data : formData,
		    			  success : function(data) {
		    				  $codeTable.bootstrapTable('removeAll');
		    				  $codeTable.bootstrapTable('prepend', data.allComCodeList);
		    			  }, 
		    			  error : function(jqXHR){
		    			    //Error시, 처리
		    				}
		    		});
	    		}
            });
			
			// 클릭 이벤트
 			$groupTable.on('click-cell.bs.table', function(field, value, row, element) {
 				$groupTable.editableTableWidget({editor: $('<input>'), cloneProperties: ['background', 'border', 'outline']});
            });
			
			// 클릭 이벤트
 			$codeTable.on('click-cell.bs.table', function(field, value, row, $element) {
		        $codeTable.editableTableWidget({editor: $('<input>'), cloneProperties: ['background', 'border', 'outline']}); 				
            });

			// 상세코드 클릭 이벤트
            $codeTable.on('click-row.bs.table', function (row, element, field) {
            	// 선택시 해당 Row 색변경
        		$codeTable.find('.success').removeClass('success');
        		$(field).addClass('success');
            });
			
			// 데이터 변경시 이벤트
 			$groupTable.on('change','td',function(evt, newValue) {
 				
				// table-data update 함수정의
				var cell = $(this)
					, cellIndex = cell.index() + 1
					, rowIndex = cell.parent().index()
					, rowData = new Object()
					, state = $groupTable.bootstrapTable('getData')[rowIndex].state;
				
				// 값이없는경우 null 추가
				if(state == null){
					state = 'U';
				}
				
				// 코드값은 수정안되게 처리
				if(cell.index()==0 && state=='U'){
					alert('코드값은 변경할수 없습니다.');
					return false;
				}
				
				// y or n 이 아닌경우
				if(cell.index()==2 && (newValue.toUpperCase()!='Y'&&newValue.toUpperCase()!='N')){
					alert('사용유무의 입력이 잘못 되었습니다. \r\n (Y 또는 N)');
					return false;
				}
				
				// 해당하는 컬럼 데이터 셋팅
 				switch(cellIndex) {
	 			    case 1:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.comCode = newValue;	 			    	
	 			        break;
	 			    case 2:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.comName = newValue;	 			    	
	 			        break;
	 			    case 3:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.useYn = newValue.toUpperCase();	 			    	
	 			        break;
 				}
 				
				// 해당 테이블 데이터 업데이트
 				$groupTable.bootstrapTable('updateRow', {
 	                index: rowIndex,
 	                row: rowData
 	            });
 			});
			
			// 데이터 변경시 이벤트
 			$codeTable.on('change','td',function(evt, newValue) {
 				
				// table-data update 함수정의
				var cell = $(this)
					, cellIndex = cell.index() + 2
					, rowIndex = cell.parent().index()
					, rowData = new Object()
					, state = $codeTable.bootstrapTable('getData')[rowIndex].state;
				
				// 값이없는경우 null 추가
				if(state == null){
					state = 'U';
				}
				
				// 코드값은 수정안되게 처리
				if(cell.index()==0 && state=='U'){
					alert('코드값은 변경할수 없습니다.');
					return false;
				}
				
				// y or n 이 아닌경우
				if(cell.index()==5 && (newValue.toUpperCase()!='Y'&&newValue.toUpperCase()!='N')){
					alert('사용유무의 입력이 잘못 되었습니다. \r\n (Y 또는 N)');
					return false;
				}
				
				// 코드키값 중복 체크
				if(cell.index()==0){
					if($codeTable.bootstrapTable('getRowByUniqueId',newValue) != null ){
						alert('코드값이 중복 되었습니다.');
						return false;
					}
				}
				

				// 해당하는 컬럼 데이터 셋팅
 				switch(cellIndex) {
	 			    case 2:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.comCode = newValue;	 			    	
	 			        break;
	 			    case 3:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.comName = newValue;	 			    	
	 			        break;
	 			    case 4:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.comOrder = newValue;	 			    	
	 			        break;
	 			    case 5:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.comSubCode = newValue;	 			    	
	 			        break;
	 			    case 6:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.comDesc = newValue;	 			    	
	 			        break;
	 			    case 7:
	 	 				rowData.state = state;	 			    	
	 	 				rowData.useYn = newValue.toUpperCase();	 			    	
	 			        break;
 				}
 				
				// 해당 테이블 데이터 업데이트
 				$codeTable.bootstrapTable('updateRow', {
 	                index: rowIndex,
 	                row: rowData
 	            });
 			});
            
			// 그룹코드 추가버튼
			$('#btnGroupAdd').on('click',function(e){
				var addRowIndex = $groupTable.find("tbody tr").length + 1
				 	, dataCount = $groupTable.find("tbody tr td.comCode").length
				 	, rowData = new Object();
				
				// add 시 기본값 셋팅
				rowData.state = 'A'
				, rowData.comCode = null
				, rowData.useYn = 'Y';
				
				// 여러개의 ADD 되는것을 막기위해 추가
				var isFindCode = false
					, data = $groupTable.bootstrapTable('getData')
					, dataCount = data.length;
				for(var i = 0; i < dataCount ; i++){
					 if(data[i].comCode==null){
						 isFindCode = true;
					 }
				}
				
				// 공백이 없는경우 행추가
				if(!isFindCode){
					$groupTable.bootstrapTable('insertRow', {
		                index: addRowIndex,
		                row: rowData
		            });				
				}
			});
			
			// 코드 추가버튼
			$('#btnAdd').on('click',function(e){
				var addRowIndex = $codeTable.find("tbody tr").length + 1
			 		, dataCount = $codeTable.find("tbody tr td.comCode").length
				 	, rowData = new Object();
			 	
				// 여러개의 ADD 되는것을 막기위해 추가
				var isFindCode = false
					, data = $codeTable.bootstrapTable('getData')
					, dataCount = data.length;
				
				for(var i = 0; i < dataCount ; i++){
					if(data[i].comCode==null){
						 isFindCode = true;
					 }
				}
				
				// add 시 기본값 셋팅
				rowData.state = 'A'
				, rowData.comCode = null				
				, rowData.useYn = 'Y'
				, rowData.comGroupCode = selComGroupCode;
				
				// 공백이 없는경우 행추가
				if(!isFindCode){
					$codeTable.bootstrapTable('insertRow', {
		                index: addRowIndex,
		                row: rowData
		            });
				}
			});

			// 그룹코드 저장버튼
			$('#btnGroupSave').on('click',function(e){

				
		    	var result = confirm("코드 정보를 저장 하시겠습니까?");
		    	if(result){
	    	        $(this).attr("disabled",true);
		    		waitingDialog.show();    	        
		    		
	 				var jsonData = JSON.stringify($groupTable.bootstrapTable('getData'));
		    		$.ajax({
		    			  type : "POST",
		    			  url : "<c:url value='/comCodeGroupSave.do'/>",
		    			  cache : false,
		    			  dataType : "json",
		    			  contentType : "application/json; charset=UTF-8",	    			  
		    			  data : jsonData,
		    			  success : function(data) {
		    				   $('.btnSearch').trigger("click");
		    			  }, 
		    			  error : function(jqXHR, state , message){
		    			    //Error시, 처리
			    				alert(error.message);	    				  
		    			  }
		    		});
		    	}else{
		    		$(".btn").attr("disabled",false);	  
		    		waitingDialog.hide();		    		
		    	}

		    	
				
				
			});
			
			// 코드 저장버튼
			$('#btnSave').on('click',function(e){
				
		    	var result = confirm("코드 정보를 저장 하시겠습니까?");
		    	if(result){
					
					// 처리중 팝업
	    	        $('#btnSave').attr("disabled",true);
		    		waitingDialog.show();    	        
					
	 				var jsonData = JSON.stringify($codeTable.bootstrapTable('getData'))
		    			,formData = "search_com_group_code="+selComGroupCode
		    			,checkData = $codeTable.bootstrapTable('getData');
	
		    		$.ajax({
		    			  type : "POST",
		    			  url : "<c:url value='/comCodeSave.do'/>",
		    			  cache : false,
		    			  dataType : "json",
		    			  contentType : "application/json; charset=UTF-8",	    			  
		    			  data : jsonData,
		    			  success : function(data) {
		    				//Success, 처리	    				  
		    			  }, 
		    			  error : function(jqXHR, state, error){
		    				alert(error.message);
		    			    //Error시, 처리
		    			  },
		    			  complete : function(){
		    	    	      $('#btnSave').attr("disabled",false);
			    		      waitingDialog.hide();
			    		      // 데이터 조회처리
		  			    		$.ajax({
					    			  type : "POST",
					    			  url : "<c:url value='/allComCodeListJson.do'/>",
					    			  cache : false,
					    			  dataType : "json",
					    			  data : formData,
					    			  success : function(data) {
					    				  $codeTable.bootstrapTable('removeAll');
					    				  $codeTable.bootstrapTable('prepend', data.allComCodeList);
					    			  }, 
					    			  error : function(jqXHR){
					    			    //Error시, 처리
					    			  }	    			    			  
					    		});		    		      
	    			   	  }	    			    			  
		    			  
		    		});
			  		$groupTable.find('tbody tr').eq(selComGroupRow).trigger('click');
		    	}else{
		    		$(".btn").attr("disabled",false);	  
		    		waitingDialog.hide();		    		
		    	}
			});

			
	    	// 검색버튼 이벤트
	    	$(".btnSearch").on("click",function(e){
	    		$("#searchForm").attr("action","/comCodeList.do");
	    		$("#searchForm").submit();
	    	});
	    	
	    	
   	    	// 데이터가 있는경우 첫행 선택 이벤트 처리
   	    	if($('#comCodeGorupList tbody tr').length > 1){
		    	($('#comCodeGorupList tbody tr td').eq(0)).trigger("click");
   	    	}
	    	
	    });
	    		    
    </script>


</body>
</html>
