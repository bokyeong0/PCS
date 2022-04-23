/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vertexid.nhmarket.pcs.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.commons.DefaultBeanValidator;

import vertexid.nhmarket.pcs.cmmn.FileUtil;
import vertexid.nhmarket.pcs.service.ComCodeVO;
import vertexid.nhmarket.pcs.service.DefaultVO;
import vertexid.nhmarket.pcs.service.OrderVO;
import vertexid.nhmarket.pcs.service.PcsMobileService;
import vertexid.nhmarket.pcs.service.PcsService;
import vertexid.nhmarket.pcs.service.PickHeaderVO;
import vertexid.nhmarket.pcs.service.impl.PcsMapper;
import vertexid.nhmarket.pcs.service.impl.PcsMoblieMapper;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
 * @Modification Information
 * @ @ 수정일 수정자 수정내용 @ --------- --------- ------------------------------- @ 2009.03.16 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 * 		Copyright (C) by MOPAS All right reserved.
 */

@Controller
public class PcsController {
	
	// Logger 생성용
	private static final Logger LOGGER = LoggerFactory.getLogger(PcsController.class);

	/** EgovSampleService */
	@Resource(name = "pcsService")
	private PcsService pcsService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	@Resource(name = "pcsMapper")
	private PcsMapper pcsMapper;	
	
	@Resource(name = "pcsMobileService")
	private PcsMobileService pcsMobileService;
	
	@Resource(name = "pcsMobileMapper")
	private PcsMoblieMapper pcsMoblieMapper;

	/**
	 * [VIEW] 피킹지시
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴
	 * @param orderVO
	 *            - 주문데이터 조회
	 * @param model
	 * @return "pickOrderList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/pickOrderList.do")
	public String selectPickOrderList(@ModelAttribute("searchVO") DefaultVO searchVO, @ModelAttribute("orderVO") OrderVO orderVO, ModelMap model) throws Exception {

		try{
		
			// 오늘 날짜를 제외하고 pdf 폴더 삭제
			ComCodeVO filePathVo = new ComCodeVO();
			filePathVo.setCom_group_code("FILE_PATH");
			String pdfPath = pcsMapper.selectComCodeList(filePathVo).get(1).getCom_sub_code();
					
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String today = format.format(new Date());
			FileUtil.deleteFolder(pdfPath, today);
	
			// tb_print 테이블 오늘 이전 날짜 데이터 삭제
			int cnt = pcsMobileService.deletePrintFileData(today);
			LOGGER.info("tb_print 테이블 오늘 이전 날짜 데이터 삭제 : "+cnt);
			if (cnt>0){
				LOGGER.info("tb_print 테이블 print_id 초기화");
				pcsMobileService.alterPrintFileIdReset();
			}
	
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectLastDeliveryCount(searchVO) + "");
			}
	
			// 주문정보 불러오기
			List<?> orderList = pcsService.selectOrderList(searchVO);
			
			// 주문건수 구하기 18-09-12
			EgovMap orderCnt = pcsService.orderCnt(searchVO);
			// 아이템수
			String orderItemCnt  = pcsService.orderItemCnt(searchVO);
	
			// 상태코드목록 조회 list
			ComCodeVO comCodeVO = new ComCodeVO();
			comCodeVO.setCom_sub_code("1_");
			
			List<?> stateCodeList = pcsService.selectStateCodeList(comCodeVO);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("stateCodeList", stateCodeList);		
			model.addAttribute("recentDeliveryCount", searchVO.getSearch_delivery_count()); //배송회차
			model.addAttribute("orderCnt", orderCnt.get("orderCnt")); //주문건수
			model.addAttribute("nCouponCnt", orderCnt.get("nCouponCnt")); //N쿠폰 주문건수
			model.addAttribute("orderItemCnt", orderItemCnt); //아이템수
			model.addAttribute("orderList", orderList);
			model.addAttribute("pageId", "pickOrderList");
		
		} catch (Exception e){
			e.printStackTrace();
		}
		return "pickOrderList";
	}
	
	/**
	 * [VIEW] 피킹현황조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param orderVO
	 *            -
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/pickStateList.do")
	public String selectPickStateList(@ModelAttribute("searchVO") DefaultVO searchVO, @ModelAttribute("orderVO") OrderVO orderVO, ModelMap model) throws Exception {

		try{
			
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null || "".equals(searchVO.getSearch_delivery_date())) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null || "".equals(searchVO.getSearch_delivery_count())) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
	
			// 2016.09.02 상태코드 조회sub 뒷글자가 1인것 추가
			ComCodeVO comCodeVO = new ComCodeVO();
			comCodeVO.setCom_sub_code("_1");
			List<?> stateCodeList = pcsService.selectStateCodeList(comCodeVO);
			
			List<?> pickHeaderList = pcsService.selectPickHeaderList(searchVO);
			List<?> deliveryCountList = pcsService.selectPickDeliveryCountList(searchVO);
			List<?> zoneCodeList = pcsService.selectPickZoneCodeList(searchVO);
			
			// 헤더 첫번째 Detail Data 가져오기
			List<?> pickDetailList = null;
			if (!pickHeaderList.isEmpty()) {
				pickDetailList = pcsService.selectPickDetailList((String) ((EgovMap) pickHeaderList.get(0)).get("pickId"));
				// 불러온 결과값을 화면에 표시
				model.addAttribute("pickDetailList", pickDetailList);
			} else {
				model.addAttribute("pickDetailList", "");
			}
	
			// 불러온 결과값을 화면에 표시
			model.addAttribute("stateCodeList", stateCodeList);
			model.addAttribute("pickHeaderList", pickHeaderList);
			model.addAttribute("deliveryCountList", deliveryCountList);
			model.addAttribute("zoneCodeList", zoneCodeList);
			model.addAttribute("pageId", "pickStateList");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "pickStateList";
	}

	/**
	 * [JSON] 피킹현황 상세(Detail) 조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param orderVO
	 *            -
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/pickDetailList.do")
	public ModelAndView selectPickDetailList(ModelMap model, String pickId) throws Exception {
		
		try{
		
			List<?> pickDetailList = pcsService.selectPickDetailList(pickId);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("pickDetailList", pickDetailList);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ModelAndView("jsonView", model);
		
	}

	/**
	 * [VIEW] 상품대체현황조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param orderVO
	 *            -
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/changeStateList.do")
	public String selectChangeStateList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		try{
			
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
	
			List<?> changePickList = pcsService.selectChangePickList(searchVO);
			List<?> deliveryCountList = pcsService.selectPickDeliveryCountList(searchVO);
			List<?> zoneCodeList = pcsService.selectPickZoneCodeList(searchVO);
	
			// 불러온 결과값을 화면에 표시
			model.addAttribute("changePickList", changePickList);
			model.addAttribute("deliveryCountList", deliveryCountList);
			model.addAttribute("zoneCodeList", zoneCodeList);
			model.addAttribute("pageId", "changeStateList");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "changeStateList";
	}

	/**
	 * 상품대체현황조회 엑셀 다운로드
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excelChangeStateList.do")
	public String excelChangeStateList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {
		
		try{
		
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
			
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
			
			// 파일 경로 읽어오기
			ComCodeVO filePathVo = new ComCodeVO();
			filePathVo.setCom_group_code("FILE_PATH");
			String excelPath = pcsMapper.selectComCodeList(filePathVo).get(2).getCom_sub_code();
			
			// 폴더 생성
			File destFld = new File(excelPath);
			if(!destFld.exists()){
				destFld.mkdirs();
			}
	
			// 파일 명 읽어오기
			filePathVo.setCom_group_code("FILE_NAME");
			String excelNm = pcsMapper.selectComCodeList(filePathVo).get(0).getCom_sub_code();
			
			List<?> changePickList = pcsService.selectChangePickList(searchVO);
			List<?> deliveryCountList = pcsService.selectPickDeliveryCountList(searchVO);
			List<?> zoneCodeList = pcsService.selectPickZoneCodeList(searchVO);
			
			// 날짜와 차수 조합
			String optNm = searchVO.getSearch_delivery_date()+"_"+searchVO.getSearch_delivery_count()+"_";
			
			// 상품대체현황 엑셀 다운로드
			pcsService.excelChangeStateList(changePickList, excelPath+optNm+excelNm);
	
			// 불러온 결과값을 화면에 표시
			model.addAttribute("changePickList", changePickList);
			model.addAttribute("deliveryCountList", deliveryCountList);
			model.addAttribute("zoneCodeList", zoneCodeList);
			model.addAttribute("pageId", "changeStateList");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "changeStateList";

	}

	/**
	 * [VIEW] TV 현황판
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param orderVO
	 *            -
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/tv.do")
	public String selectJobState(@ModelAttribute("searchVO") DefaultVO searchVO, @ModelAttribute("orderVO") OrderVO orderVO, ModelMap model) throws Exception {

		try{
			
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
	
			List<?> totalInfo = pcsService.selectJobTotalInfo(searchVO);
			List<?> stateList = pcsService.selectJobStateList(searchVO);
			List<?> jobStateCode = pcsService.selectJobStateCode();
	
			// 불러온 결과값을 화면에 표시
			model.addAttribute("deliveryCount", searchVO.getSearch_delivery_count()); 
			model.addAttribute("totalInfo", totalInfo);
			model.addAttribute("jobStateCode", jobStateCode);
	
			model.addAttribute("stateList", stateList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "jobState";
	}

	/**
	 * [PS] 주문 정보 엑셀 파일 업로드
	 * 
	 * @param mReq
	 * @return pickOrderList
	 * @exception Exception
	 */
	@RequestMapping(value = "/orderImportData.do", method = RequestMethod.POST)
	public String insertOrderImportData(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model, MultipartHttpServletRequest multipartRequest) throws Exception {

		// 파일 import 처리
		try {

			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}	

			// 파일 업로드(DB Insert)
			pcsService.insertImportData(multipartRequest, searchVO);

			// 업로드후 데이터 조회 처리
			List<?> orderList = pcsService.selectOrderList(searchVO);

			// 불러온 결과값을 화면에 표시
			model.addAttribute("orderList", orderList);
			model.addAttribute("pageId", "pickOrderList");
		} catch (Exception e) {
			model.addAttribute("resultErrorMsg", e.getMessage());
			e.printStackTrace();
			return "cmmn/egovError";
		}

		return "redirect:/pickOrderList.do";
	}
	
	/**
	 * [PS] 피킹현황조회 - 체크한 부분만 라벨 프린팅
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "orderList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/choiceDeleteOrder.do")
	public String updateChoiceDeleteOrder(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {
		/*
		 * searchVO.setPrintIp(propertiesService.getString("printIp1")); searchVO.setPrintPort(propertiesService.getInt("printPort")); // 출력처리
		 * pcsService.updateLabelPrint(searchVO);
		 */
		
		try{
		
			// 데이터 조회 처리
			List<?> orderList = pcsService.selectOrderList(searchVO);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("orderList", orderList);
	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "forward:/orderList.do";
	}

	/**
	 * [PS] 피킹지시 - 피킹지시
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "pickOrderList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/picking.do")
	public String insertPicking(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		try{
			// tb_print 테이블 인쇄 안된 건 update
			pcsMobileService.updatePrintFileFailData();
			
			// 피킹지시 처리
			pcsService.insertPicking(searchVO);
			
			// 데이터 조회 처리
			List<?> orderList = pcsService.selectOrderList(searchVO);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("orderList", orderList);
			model.addAttribute("pageId", "pickOrderList");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "forward:/pickOrderList.do";
	}

	/**
	 * [PS] 피킹지시 - 라벨출력
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "pickOrderList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/labelPrint.do")
	public String updateLabelPrint(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model, SessionStatus sessionStatus) throws Exception {

		ComCodeVO searchComCodeVO = new ComCodeVO();
		searchComCodeVO.setCom_group_code("BARCODE_PRINTER");
		List<ComCodeVO> codeList = pcsService.selectComCodeList(searchComCodeVO);

		// 프린터 변경등
		ComCodeVO dataComCodeVO = codeList.get(0);

		// IP/PORT 가져오기(DB)
		searchVO.setPrintIp(dataComCodeVO.getCom_name());
		searchVO.setPrintPort(Integer.parseInt(dataComCodeVO.getCom_sub_code()));

		// 출력처리
		try {
			pcsService.updateLabelPrint(searchVO);
		} catch (Exception e) {
			model.addAttribute("resultErrorMsg", e.getMessage());
			e.printStackTrace();
			return "cmmn/egovError";
		}

		// 데이터 조회 처리
		List<?> orderList = pcsService.selectOrderList(searchVO); // 존코드, 단축주문번호, 권역명
		
		// 불러온 결과값을 화면에 표시
		model.addAttribute("orderList", orderList);
		
		sessionStatus.setComplete();
		
		return "forward:/pickOrderList.do";
	}

	/**
	 * [PS] 피킹현황조회 - 선택 라벨출력
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "pickStateList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/choiceLabelPrint.do")
	public ModelAndView updateChoiceLabelPrint(@RequestBody ArrayList<EgovMap> selectPickHeaderList, ModelMap model) throws Exception {

		
		ComCodeVO searchComCodeVO = new ComCodeVO();
		searchComCodeVO.setCom_group_code("BARCODE_PRINTER");
		List<ComCodeVO> codeList = pcsService.selectComCodeList(searchComCodeVO);

		// 프린터 변경등
		ComCodeVO dataComCodeVO = codeList.get(0);
		String printIp = dataComCodeVO.getCom_name();
		int printPort = Integer.parseInt(dataComCodeVO.getCom_sub_code());

		for (EgovMap selectPickHeader : selectPickHeaderList) {

			PickHeaderVO pickHeaderVO = new PickHeaderVO();
			pickHeaderVO.setPrintIp(printIp);
			pickHeaderVO.setPrintPort(printPort);
			pickHeaderVO.setPick_id((String) selectPickHeader.get("pickId"));
			// 출력처리
			try {
				pcsService.updateChoiceLabelPrint(pickHeaderVO);
			} catch (Exception e) {
				model.addAttribute("resultErrorMsg", e.getMessage());
				e.printStackTrace();
				return new ModelAndView("jsonView", model);
			}

		}
		return new ModelAndView("jsonView", model);
	}

	/**
	 * [COMMON] 배송일별 차수 조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "jsonView"
	 * @exception Exception
	 */
	@RequestMapping(value = "/deliveryCountList.do", method = RequestMethod.GET)
	public ModelAndView selectDeliveryCountList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		try{
		
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 차수 리스트 가져오기
			List<?> deliveryCountList = pcsService.selectDeliveryCountList(searchVO);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("deliveryCountList", deliveryCountList);
	
			// 존코드 리스트 가져오기
			searchVO.setSearch_delivery_count("");
			List<?> zoneCodeList = pcsService.selectZoneCodeList(searchVO);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("zoneCodeList", zoneCodeList);

		}catch(Exception e){
			e.toString();
			e.printStackTrace();
		}
		
		return new ModelAndView("jsonView", model);
	}

	/**
	 * Order 기본목록 조회 (pageing)
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/zoneCodeList.do", method = RequestMethod.GET)
	public ModelAndView selectZoneCodeList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		try{
		
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 존코드 리스트 가져오기
			List<?> zoneCodeList = pcsService.selectZoneCodeList(searchVO);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("zoneCodeList", zoneCodeList);

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ModelAndView("jsonView", model);
	}

	/**
	 * Order 기본목록 조회 (pageing)
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/labelPrintCount.do", method = RequestMethod.GET)
	public ModelAndView labelPrintCount(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		// 날짜가 없는경우 오늘날짜 셋팅
		if (searchVO.getSearch_delivery_date() == null) {
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
			searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
		}
		// 라벨 출력 횟수 조회 (배송일 + 배송차수)
		int labelPrintCount = pcsService.selectLabelPrintCount(searchVO);
		
		// 불러온 결과값을 화면에 표시
		model.addAttribute("labelPrintCount", labelPrintCount);

		return new ModelAndView("jsonView", model);
	}

	/**
	 * JobState 통계정보 조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "totaInfo (Json)"
	 * @exception Exception
	 */
	@RequestMapping(value = "/jobStateTotalInfo.do", method = RequestMethod.POST)
	public ModelAndView selectJobStateTotalInfo(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		try{
		
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
	
			// 진행 통계 정보 조회
			List<?> totalInfo = pcsService.selectJobTotalInfo(searchVO);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("totalInfo", totalInfo);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ModelAndView("jsonView", model);
	}

	/**
	 * JobState 진행현황 조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param model
	 * @return "stateList (Json)"
	 * @exception Exception
	 */
	@RequestMapping(value = "/jobStateStateList.do", method = RequestMethod.POST)
	public ModelAndView selectJobStateStateList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		
		// 날짜가 없는경우 오늘날짜 셋팅
		if (searchVO.getSearch_delivery_date() == null) {
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
			searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
		}

		// 오늘날짜 최근 차수 조회
		if (searchVO.getSearch_delivery_count() == null) {
			searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
		}

		// 진행현황 조회
		List<?> stateList = pcsService.selectJobStateList(searchVO);
		
		// 불러온 결과값을 화면에 표시
		model.addAttribute("stateList", stateList);
		
		return new ModelAndView("jsonView", model);
	}

	/**
	 * [VIEW] 상품 결품 현황조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param orderVO
	 *            -
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/reasonStateList.do")
	public String selectReasonStateList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		try{
		
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
	
			// 배송차수 목록 ,배송차수별 코드 목록
			List<?> deliveryCountList = pcsService.selectPickDeliveryCountList(searchVO);
			List<?> zoneCodeList = pcsService.selectPickZoneCodeList(searchVO);
	
			// 결품현황 목록
			List<?> reasonStateList = pcsService.selectReasonStateList(searchVO);
	
			// 불러온 결과값을 화면에 표시
			model.addAttribute("reasonStateList", reasonStateList);
			model.addAttribute("deliveryCountList", deliveryCountList);
			model.addAttribute("zoneCodeList", zoneCodeList);
	
			model.addAttribute("pageId", "reasonStateList");

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "reasonStateList";
	}

	/**
	 * 상품결품현황조회 엑셀 다운로드
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excelReasonStateList.do")
	public String excelReasonStateList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {
		
		try{
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
			
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
			
			// 파일 경로 읽어오기
			ComCodeVO filePathVo = new ComCodeVO();
			filePathVo.setCom_group_code("FILE_PATH");
			String excelPath = pcsMapper.selectComCodeList(filePathVo).get(2).getCom_sub_code();
			
			// 폴더 생성
			File destFld = new File(excelPath);
			if(!destFld.exists()){
				destFld.mkdirs();
			}
	
			// 파일 명 읽어오기
			filePathVo.setCom_group_code("FILE_NAME");
			String excelNm = pcsMapper.selectComCodeList(filePathVo).get(1).getCom_sub_code();
			
			// 배송차수 목록 ,배송차수별 코드 목록
			List<?> deliveryCountList = pcsService.selectPickDeliveryCountList(searchVO);
			List<?> zoneCodeList = pcsService.selectPickZoneCodeList(searchVO);
			
			// 상품결품현황 목록
			List<?> reasonStateList = pcsService.selectReasonStateList(searchVO);
			
			// 날짜와 차수 조합
			String optNm = searchVO.getSearch_delivery_date()+"_"+searchVO.getSearch_delivery_count()+"_";
			
			// 상품결품현황 엑셀 다운로드
			pcsService.excelReasonStateList(reasonStateList, excelPath+optNm+excelNm);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("reasonStateList", reasonStateList);
			model.addAttribute("deliveryCountList", deliveryCountList);
			model.addAttribute("zoneCodeList", zoneCodeList);
			
			model.addAttribute("pageId", "reasonStateList");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "reasonStateList";
	}
	
	/**
	 * 주문진행현황조회 (프린트 현황 조회)
	 * @param searchVO
	 * @param orderVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/printStateList.do")
	public String selectPrintStateList(@ModelAttribute("searchVO") DefaultVO searchVO, @ModelAttribute("orderVO") OrderVO orderVO, ModelMap model) throws Exception {

		try{
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
	
			// 2016.09.02 상태코드 조회sub 뒷글자가 1인것 추가
			ComCodeVO comCodeVO = new ComCodeVO();
			comCodeVO.setCom_sub_code("_1");
			
			List<?> stateCodeList = pcsService.selectStateCodeList(comCodeVO);
			List<?> pickHeaderList = pcsService.selectPrintHeaderList(searchVO);
			List<?> deliveryCountList = pcsService.selectPickDeliveryCountList(searchVO);
			List<?> zoneCodeList = pcsService.selectPickZoneCodeList(searchVO);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("stateCodeList", stateCodeList);
			model.addAttribute("pickHeaderList", pickHeaderList);
			model.addAttribute("deliveryCountList", deliveryCountList);
			model.addAttribute("pageId", "printStateList");
			model.addAttribute("zoneCodeList", zoneCodeList);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "printStateList";
		
	}
	
	/**
	 * [PS] 피킹 결과 조회 및 엑셀 다운로드
	 * 
	 * @param mReq
	 * @return pickOrderList
	 * @exception Exception
	 */
	@RequestMapping(value = "/excelPickingResultList.do", method = RequestMethod.POST)
	public ModelAndView excelPickingResultList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {
		try{
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
			
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
			
			// 파일 경로 읽어오기
			ComCodeVO filePathVo = new ComCodeVO();
			filePathVo.setCom_group_code("FILE_PATH");
			String excelPath = pcsMapper.selectComCodeList(filePathVo).get(2).getCom_sub_code();
			
			// 폴더 생성
			File destFld = new File(excelPath);
			if(!destFld.exists()){
				destFld.mkdirs();
			}

			// 파일 명 읽어오기
			filePathVo.setCom_group_code("FILE_NAME");
			String excelNm = pcsMapper.selectComCodeList(filePathVo).get(2).getCom_sub_code();
			
			// 배송차수 목록
			List<?> deliveryCountList = pcsService.selectPickDeliveryCountList(searchVO);
			
			// 차수별 피킹 결과 목록
			List<?> pickHeaderList = pcsService.selectPickingResultList(searchVO);
			
			// N쿠폰 제외 엑셀 파일
			searchVO.setnCoupon_yn("Y");
			List<Map<String, String>> nCouponOrder =pcsService.selectChangePickOrderList(searchVO);
			String[] arr = new String[nCouponOrder.size()];
			for(int i=0; i<nCouponOrder.size(); i++){
				Map<String, String> map = new HashMap<String, String>();
				map = nCouponOrder.get(i);
				arr[i] = map.get("orderNo");
			}
			searchVO.setnCouponOrder(arr);
			if(arr.length==0){
				searchVO.setnCouponOrder(null);
			}
			List<?> pickHeaderList_exceptNcoupon = pcsService.selectPickingResultList(searchVO);
			
			// 날짜와 차수 조합
			String optNm = searchVO.getSearch_delivery_date()+ "_" + searchVO.getSearch_delivery_count() + "_";
			
			// 차수별 피킹 결과 엑셀 다운로드
			pcsService.excelPickingResultList(pickHeaderList_exceptNcoupon, excelPath+optNm+excelNm);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("pickHeaderList", pickHeaderList);
			model.addAttribute("deliveryCountList", deliveryCountList);
			
			model.addAttribute("excelPath", excelPath);
			
			model.addAttribute("pageId", "printStateList");
			
			
			}catch(Exception e){
				e.printStackTrace();
			}
			return new ModelAndView("jsonView", model);
		}
	/**
	 * 전체 주문건수, 대체제외건수 Count
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excelPickingResultListCnt.do", method = RequestMethod.POST)
	public ModelAndView excelPickingResultListCnt(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {
		try{
			
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
			
			// 오늘날짜 최근 차수 조회
			if (searchVO.getSearch_delivery_count() == null) {
				searchVO.setSearch_delivery_count(pcsService.selectPickLastDeliveryCount(searchVO) + "");
			}
			
			List<Map<String, String>> pickOrderListCnt = pcsService.selectChangePickOrderList(searchVO); // 전체 주문건수
			searchVO.setnCoupon_yn("Y");
			List<Map<String, String>> nCouponOrder =pcsService.selectChangePickOrderList(searchVO); // N쿠폰 주문건수
			int totalCnt = pickOrderListCnt.size();
			int nCouponOrderCnt = nCouponOrder.size();
			
			model.addAttribute("totalCnt", totalCnt);
			model.addAttribute("nCouponOrderCnt", nCouponOrderCnt);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", model);
	}
	
	/**
	 * 180725
	 * 선택 엑셀 다운로드
	 * 전체 주문건수, 대체제외건수 Count
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/choiceExcelPickingResultListCnt.do")
	public ModelAndView choiceExcelPickingResultListCnt(@RequestBody ArrayList<EgovMap> selectPickHeaderList, ModelMap model) throws Exception {
		try{
			LOGGER.info("============== choiceExcelPickingResultListCnt START!!! ==============");
			DefaultVO searchVO = new DefaultVO();
			searchVO.setSearch_delivery_date((String)selectPickHeaderList.get(0).get("deliveryDate"));
			
			String[] orderNo = new String[selectPickHeaderList.size()]; 
			for (int i=0; i<selectPickHeaderList.size(); i++) {
				orderNo[i] = (String) selectPickHeaderList.get(i).get("orderDate") + (String) selectPickHeaderList.get(i).get("orderNo");
			}
			if(orderNo.length==0){
				orderNo = null;
			}
			searchVO.setOrder_key(orderNo);
			
			List<Map<String, String>> pickOrderListCnt = pcsService.selectChangePickOrderList(searchVO); // 전체 주문건수
			searchVO.setnCoupon_yn("Y");
			List<Map<String, String>> nCouponOrder =pcsService.selectChangePickOrderList(searchVO); // N쿠폰 주문건수
			int totalCnt = pickOrderListCnt.size();
			int nCouponOrderCnt = nCouponOrder.size();
			
			model.addAttribute("totalCnt", totalCnt);
			model.addAttribute("nCouponOrderCnt", nCouponOrderCnt);
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("jsonView", model);
	}
	
	/**
	 * 180725
	 * 선택 엑셀 다운로드
	 * [PS] 피킹 결과 조회 및 엑셀 다운로드
	 * 
	 * @param mReq
	 * @return pickOrderList
	 * @exception Exception
	 */
	@RequestMapping(value = "/choiceExcelPickingResultList.do", method = RequestMethod.POST)
	public ModelAndView choiceExcelPickingResultList(@RequestBody ArrayList<EgovMap> selectPickHeaderList, ModelMap model) throws Exception {
		try{
			
			// 파일 경로 읽어오기
			ComCodeVO filePathVo = new ComCodeVO();
			filePathVo.setCom_group_code("FILE_PATH");
			String excelPath = pcsMapper.selectComCodeList(filePathVo).get(2).getCom_sub_code();
			
			// 폴더 생성
			File destFld = new File(excelPath);
			if(!destFld.exists()){
				destFld.mkdirs();
			}

			// 파일 명 읽어오기
			filePathVo.setCom_group_code("FILE_NAME");
			String excelNm = pcsMapper.selectComCodeList(filePathVo).get(2).getCom_sub_code();
			
			DefaultVO searchVO = new DefaultVO();
			searchVO.setSearch_delivery_date((String)selectPickHeaderList.get(0).get("deliveryDate"));
			
			// 선택한 주문번호
			String[] ChoiceOrderNo = new String[selectPickHeaderList.size()]; 
			for (int i=0; i<selectPickHeaderList.size(); i++) {
				ChoiceOrderNo[i] = (String) selectPickHeaderList.get(i).get("orderDate") + (String) selectPickHeaderList.get(i).get("orderNo");
			}
			if(ChoiceOrderNo.length==0){
				ChoiceOrderNo = null;
			}
			searchVO.setOrder_key(ChoiceOrderNo);
						
			// N쿠폰 제외 엑셀 파일
			searchVO.setnCoupon_yn("Y");
			List<Map<String, String>> nCouponOrder =pcsService.selectChangePickOrderList(searchVO);
			String[] arr = new String[nCouponOrder.size()];
			for(int i=0; i<nCouponOrder.size(); i++){
				Map<String, String> map = new HashMap<String, String>();
				map = nCouponOrder.get(i);
				arr[i] = map.get("orderNo");
			}
			searchVO.setnCouponOrder(arr);
			if(arr.length==0){
				searchVO.setnCouponOrder(null);
			}
			
			List<?> pickHeaderList_exceptNcoupon = pcsService.selectPickingResultList(searchVO);
			
			// 날짜와 차수 조합
			String optNm = searchVO.getSearch_delivery_date()+ "_" + selectPickHeaderList.get(0).get("deliveryCount") + "_선택_"; 
			
			// 차수별 피킹 결과 엑셀 다운로드
			pcsService.excelPickingResultList(pickHeaderList_exceptNcoupon, excelPath+optNm+excelNm);
			
			// 불러온 결과값을 화면에 표시
			model.addAttribute("excelPath", excelPath);
			model.addAttribute("pageId", "printStateList");
			
			}catch(Exception e){
				e.printStackTrace();
			}
			return new ModelAndView("jsonView", model);
		}
		

	/**
	 * [VIEW] 공통코드 조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param orderVO
	 *            -
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */

	@RequestMapping(value = "/comCodeList.do")
	public String selectALLComCodeList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		searchVO.setSearch_com_group_code("@@");
		List<?> comCodeGorupList = pcsService.selectALLComCodeList(searchVO);

		searchVO.setSearch_com_group_code((String) ((EgovMap) comCodeGorupList.get(0)).get("comCode"));
		List<?> comCodeList = pcsService.selectALLComCodeList(searchVO);

		// 불러온 결과값을 화면에 표시
		model.addAttribute("comCodeGorupList", comCodeGorupList);
		model.addAttribute("comCodeList", comCodeList);
		model.addAttribute("pageId", "comCodeList");
		return "comCodeList";
	}

	/**
	 * [JSON] 공통코드 상세 조회
	 * 
	 * @param searchVO
	 *            - 조회할 정보가 담긴 DefaultVO
	 * @param orderVO
	 *            -
	 * @param model
	 * @return "egovSampleList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/allComCodeListJson.do")
	public ModelAndView selectAllComCodeListToJson(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		List<?> allComCodeList = pcsService.selectALLComCodeList(searchVO);
		
		// 불러온 결과값을 화면에 표시
		model.addAttribute("allComCodeList", allComCodeList);
		return new ModelAndView("jsonView", model);
	}

	/**
	 * [PS] 그룹코드 - 업데이트 처리
	 * 
	 * @param comCodeVO
	 *            - 업데이트할 데이터가 담긴 부분
	 * @param model
	 * @return "json"
	 * @exception Exception
	 */
	@RequestMapping(value = "/comCodeGroupSave.do")
	public ModelAndView updateComCodeGroup(@RequestBody ArrayList<EgovMap> comCodeList, ModelMap model) throws Exception {

		int result = pcsService.updateComCodeGroup(comCodeList);
		model.put("result", result);

		return new ModelAndView("jsonView", model);
	}

	/**
	 * [PS] 그룹코드 - 업데이트 처리
	 * 
	 * @param comCodeVO
	 *            - 업데이트할 데이터가 담긴 부분
	 * @param model
	 * @return "json"
	 * @exception Exception
	 */
	@RequestMapping(value = "/comCodeSave.do")
	public ModelAndView updateComCode(@RequestBody ArrayList<EgovMap> comCodeList, ModelMap model) throws Exception {

		int result = pcsService.updateComCode(comCodeList);
		model.put("result", result);

		return new ModelAndView("jsonView", model);
	}
	
	/**
	 * 사전피킹리스트의 차수와 존코드 조회
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/preDeliveryCountList.do", method = RequestMethod.GET)
	public ModelAndView preDeliveryCountList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		LOGGER.info("preDeliveryCountList getSearch_delivery_date() : " + searchVO.getSearch_delivery_date());
		
		// 날짜가 없는경우 오늘날짜 셋팅
		if (searchVO.getSearch_delivery_date() == null) {
			SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
			searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
		}
		
		// 오늘날짜 최근 차수 조회
		if (searchVO.getSearch_delivery_count() == null) {
			searchVO.setSearch_delivery_count(pcsService.selectLastDeliveryCount(searchVO) + "");
		}

		// 차수 리스트 가져오기
		List<?> deliveryCountList = pcsService.selectDeliveryCountList(searchVO);
		
		model.addAttribute("deliveryCountList", deliveryCountList);

		// 존코드 리스트 가져오기
		List<?> zoneCodeList = pcsService.selectZoneCodeList(searchVO);
		
		// 불러온 결과값을 화면에 표시
		model.addAttribute("zoneCodeList", zoneCodeList);

		return new ModelAndView("jsonView", model);
	}
	
	/**
	 * 사전피킹리스트 출력
	 * @param searchVO
	 * @param model
	 * @param sessionStatus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/prePicking.do")
	public String prePckList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model, SessionStatus sessionStatus, HttpServletRequest request) throws Exception {
		
		LOGGER.info("******************* prePckList Start ***************************");
		LOGGER.info("Search_delivery_count : " + searchVO.getSearch_delivery_count()+", Search_zone_code : "+searchVO.getSearch_zone_code() +", Search_state_code : "+searchVO.getSearch_state_code());
		
		ComCodeVO filePathVo = new ComCodeVO();
		filePathVo.setCom_group_code("FILE_PATH");
		String rootPath = pcsMapper.selectComCodeList(filePathVo).get(0).getCom_sub_code();
//		String rootPath = request.getSession().getServletContext().getRealPath("/");
		LOGGER.info(" prePckList rootPath : "+ rootPath);
		pcsService.selectPrePckListPr(searchVO, rootPath);
		
		LOGGER.info("******************* prePckList End   ***************************");		
		
		return "redirect:/pickOrderList.do";
	}
	
	/**
	 * 사전피킹리스트 조회
	 * @param searchVO
	 * @param model
	 * @param sessionStatus
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "/prePickingInquiry.do")
//	public String prePckListInqry(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model, SessionStatus sessionStatus, HttpServletRequest request) throws Exception {
//		
//		LOGGER.info("******************* prePckListInqry Start ***************************");
//		LOGGER.info("Search_delivery_count : " + searchVO.getSearch_delivery_count()+", Search_zone_code : "+searchVO.getSearch_zone_code());
//		
//		String rootPath = request.getSession().getServletContext().getRealPath("/");
//		LOGGER.info(" prePckListInqry rootPath : "+rootPath);
//		pcsService.selectPrePckListInq(searchVO, rootPath);
//		
//		LOGGER.info("******************* prePckListInqry End   ***************************");		
//		
//		return "redirect:/pickList.do";
//	}
	
	
	/**
	 * 주문진행현황조회 선택한 리포트 프린트
	 * @param selectPickHeaderList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/choicePDFPrint.do")
	public ModelAndView choicePDFPrint(@RequestBody ArrayList<EgovMap> selectPickHeaderList, ModelMap model) throws Exception {
		
		LOGGER.info("============== choicePDFPrint START!!! ==============");
		PickHeaderVO pickHeaderVO = new PickHeaderVO();
		ArrayList<String> pick_ids = new ArrayList<String>();
		
		for (EgovMap selectPickHeader : selectPickHeaderList) {
			pickHeaderVO.setPick_id((String) selectPickHeader.get("pickId"));				
			pickHeaderVO.setDelivery_date((String) selectPickHeader.get("deliveryDate"));				
			pick_ids.add(pickHeaderVO.getPick_id());
		}

		try {
			// pick_ids 가 null이 아닐 경우 프린트 함수 호출
			if (pick_ids != null) {
				pcsMobileService.checkPrint(pick_ids, 2);
			}
		} catch (Exception e) {
			model.addAttribute("resultErrorMsg", e.getMessage());
			e.printStackTrace();
			return new ModelAndView("jsonView", model);
		}

		return new ModelAndView("jsonView", model);
	}
	
	//자동 리포트 출력 스케쥴러
//	@Scheduled(cron = "*/30 * * * * *") // 30초 간격으로 실행, 리포트 출력 처리
	// initialDelay=1000 : 최초 Task 수행시 1초 딜레이 타임이 발생함
	// fixedDelay=60000  : 60초 간격으로 Task 실행, 해당 Task가 종료된 후 60초 후 다음 Task 실행
	// cron 매분 0초
//	@Scheduled(cron="0 0/2 * * * ?") 
	@Scheduled(initialDelay=1000, fixedDelay=10000) 
	public void pcsPrintNew() throws Exception {	
		LOGGER.info("================== 프린트 스케줄러 START ==================");		
		int printIngCnt = pcsMobileService.selectPrintFileInProgress();
		LOGGER.info("프린트 진행중 건수 : "+printIngCnt);
		
		if(printIngCnt == 0){ // 프린트 진행중이 없으면
			Map<String, Object> map = new HashMap<String, Object>();
			map = pcsMobileService.selectPrintFileData();
			
			if (map!=null){ // 생성된 파일 존재함
				LOGGER.info("프린트 진행 파일명 : "+map.get("file_name"));
				// 프린트 진행
				pcsMobileService.pdfPrint(map);
			}
		}else{
			LOGGER.info("프린트 진행중");
			pcsMobileService.updatePrintFileError(); // STATE 3으로 5분 이상 남아있는 데이터 제거
		}
		LOGGER.info("================== 프린트 스케줄러 END ====================");
	}
//	
	/**
	 * 다음 차수를 가져와 select box에 표시, 오직 다음 차수만 표시함
	 * @param searchVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDayDeliveryCountList.do", method = RequestMethod.GET)
	public ModelAndView toDayDeliveryCountList(@ModelAttribute("searchVO") DefaultVO searchVO, ModelMap model) throws Exception {

		try{
		
			LOGGER.info("toDayDeliveryCountList getSearch_delivery_date() : " + searchVO.getSearch_delivery_date());
			
			// 날짜가 없는경우 오늘날짜 셋팅
			if (searchVO.getSearch_delivery_date() == null) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
				searchVO.setSearch_delivery_date(dateFmt.format(new Date()));
			}
	
			// 차수 리스트 가져오기
			List<?> deliveryCountList = pcsService.nextDeliveryCountList(searchVO);
			model.addAttribute("deliveryCountList", deliveryCountList);

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ModelAndView("jsonView", model);
	}
	
	/**
	 * 브라우저 종류 구해오기
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/chkBrowser.do", method = RequestMethod.GET)
	public void chkBrowser(HttpServletRequest request) throws Exception {			
		LOGGER.info("================== chkBrowser Start : " + request.getParameter("chkBrowser"));
	}

	
	
	
	
}
