<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><spring:message code="title.nhmarket.pcs.main" /></title>

<!-- plugin css -->
<link type="text/css" rel="stylesheet"
	href="<c:url value='/plugin/bootstrap/css/bootstrap.css'/>" />
<link type="text/css" rel="stylesheet"
	href="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.css'/>" />
<link type="text/css" rel="stylesheet"
	href="<c:url value='/css/pcs/default.css'/>" />

<!-- plugin js -->
<script src="<c:url value='/plugin/jquery-1.12.4.min.js'/>"></script>
<script src="<c:url value='/plugin/bootstrap/js/bootstrap.js'/>"></script>
<script
	src="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.js'/>"></script>
</head>

<body>
	<!-- container -->
	<div class="container">

		<!-- 페이지명 -->
		<div class="page-header">
			<h1>모바일 apk 파일 업로드</h1>
		</div>
		<!-- /페이지명 -->

		<!-- 검색 영역 -->
		<div class="panel panel-default">
			<div class="panel-body">
				<form class="form-horizontal" id="uploadForm" name="uploadForm" method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="version" class="col-sm-2 control-label">버전</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="version" name="version" value="${version}">
					</div>
				</div>
				<div class="form-group">
					<label for="passwd" class="col-sm-2 control-label">비밀번호</label>
					<div class="col-sm-3">
						<input type="password" class="form-control" id="passwd"	name="passwd" placeholder="Password" >
					</div>
				</div>
				<div class="form-group">
					<label for="fileToUpload" class="col-sm-2 control-label">File upload</label>
					<div class="col-sm-3">
						<input type="file" name="fileToUpload" id="fileToUpload" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="button" class="btn btn-default btnUpload">파일 업로드</button>
					</div>
				</div>
				<input type="hidden" id="checkVersion" name="checkVersion" value="${version}">
			</form>
			</div>
		</div>
		<!-- /검색 영역 -->

	</div>
	<!-- container -->
	<!-- 처리 스크립트 -->
	<script>
		// 페이지 로딩시 처리
		$(function() {

			// 검색버튼 이벤트
			$(".btnUpload").on("click", function(e) {
				if(!$("#passwd").val()){
					alert("비밀번호를 입력해 주세요.");
					return;
				}
				
				if(!$("#fileToUpload").val()){
					alert("파일을 올려주세요.");
					return;
				}
				
				if(parseFloat($("#version").val()) <= parseFloat($("#checkVersion").val())){
					alert("기존 버전보다 높아야 합니다.");
					return;
				}
				
				$("#uploadForm").attr("action", "/fileUpload.do");
				$("#uploadForm").submit();
				alert("apk 파일 업로드가 완료되습니다.")
			});

		});
	</script>

</body>
</html>
