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

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title><spring:message code="title.nhmarket.pcs.main" /></title>


	<!-- 즐겨찾기 추가 및 숏컷 아이콘 -->
	<link rel="shortcut icon" href="/images/egovframework/hanaro.ico">
	<link rel="apple-touch-icon-precomposed" href="/images/egovframework/hanaro.ico">
		
	<!-- plugin css -->    
    <link type="text/css" rel="stylesheet" href="<c:url value='/plugin/bootstrap/css/bootstrap.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.css'/>"/>
    <link type="text/css" rel="stylesheet" href="<c:url value='/plugin/bootstrap-table/bootstrap-table.css'/>"/>
    
    <!-- default css -->
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/pcs/default.css'/>"/>
    
    <!-- plugin js -->
    <script src="<c:url value='/plugin/jquery-1.12.4.min.js'/>"></script>
    <script src="<c:url value='/plugin/bootstrap/js/bootstrap.js'/>"></script>
    <script src="<c:url value='/plugin/jquery-ui-1.12.0.custom/jquery-ui.js'/>"></script>
    <script src="<c:url value='/plugin/bootstrap-table/bootstrap-table.js'/>"></script>
    <script src="<c:url value='/plugin/bootstrap-table/bootstrap-table-ko-KR.js'/>"></script>
    
    <!-- export 플러그인 -->
    <script src="<c:url value='/plugin/bootstrap-table/export/bootstrap-table-export.js'/>"></script>
    <script src="<c:url value='/plugin/file-saver/FileSaver.js'/>"></script>
    <script src="<c:url value='/plugin/tableExport.js'/>"></script>
    
    <!-- editable 플러그인 -->
    <script src="<c:url value='/plugin/bootstrap3-editable/js/bootstrap-editable.js'/>"></script>
    <script src="<c:url value='/plugin/bootstrap-table/editable/bootstrap-table-editable.js'/>"></script>
    <script src="<c:url value='/plugin/mindmup-editabletable.js'/>"></script>
    
	<script>
		// TODO 공통 Script - 더블클릭 방지 처리
	    jQuery.fn.preventDoubleSubmission = function() {
	    	  $(this).on('submit',function(e){
	    	    var $form = $(this);
	
	    	    if ($form.data('submitted') === true) {
	    	      // Previously submitted - don't submit again
	    	      e.preventDefault();
	
	    	    } else {
	    	      // Mark it so that the next submit can be ignored
	    	      $form.data('submitted', true);
	    	    }
	    	  });
	
	    	  // Keep chainability
	    	  return this;
	    	};	
	    	
	    	
	    	// 처리중 프로그레스바 (wationg Dialogs)
	    	var waitingDialog = waitingDialog || (function ($) {
	    		// Creating modal dialog's DOM
	    		var $dialog = $(
	    			'<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true" style="padding-top:15%; overflow-y:visible;">' +
	    			'<div class="modal-dialog modal-m">' +
	    			'<div class="modal-content">' +
	    				'<div class="modal-header"><h3 style="margin:0;"></h3></div>' +
	    				'<div class="modal-body">' +
	    					'<div class="progress progress-striped active" style="margin-bottom:0;"><div class="progress-bar" style="width: 100%"></div></div>' +
	    				'</div>' +
	    			'</div></div></div>');

	    		return {
	    			/**
	    			 * Opens our dialog
	    			 * @param message Custom message
	    			 * @param options Custom options:
	    			 * 				  options.dialogSize - bootstrap postfix for dialog size, e.g. "sm", "m";
	    			 * 				  options.progressType - bootstrap postfix for progress bar type, e.g. "success", "warning".
	    			 */
	    			show: function (message, options) {
	    				// Assigning defaults
	    				if (typeof options === 'undefined') {
	    					options = {dialogSize: 'sm', progressType: 'info'};
	    				}
	    				if (typeof message === 'undefined') {
	    					message = '처리중 입니다.';
	    				}
	    				var settings = $.extend({
	    					dialogSize: 'm',
	    					progressType: '',
	    					onHide: null // This callback runs after the dialog was hidden
	    				}, options);

	    				// Configuring dialog
	    				$dialog.find('.modal-dialog').attr('class', 'modal-dialog').addClass('modal-' + settings.dialogSize);
	    				$dialog.find('.progress-bar').attr('class', 'progress-bar');
	    				if (settings.progressType) {
	    					$dialog.find('.progress-bar').addClass('progress-bar-' + settings.progressType);
	    				}
	    				$dialog.find('h3').text(message);
	    				// Adding callbacks
	    				if (typeof settings.onHide === 'function') {
	    					$dialog.off('hidden.bs.modal').on('hidden.bs.modal', function (e) {
	    						settings.onHide.call($dialog);
	    					});
	    				}
	    				// Opening dialog
	    				$dialog.modal();
	    			},
	    			/**
	    			 * Closes dialog
	    			 */
	    			hide: function () {
	    				$dialog.modal('hide');
	    			}
	    		};

	    	})(jQuery);
	</script>
</head>
