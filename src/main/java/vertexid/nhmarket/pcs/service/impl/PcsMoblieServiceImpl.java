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
package vertexid.nhmarket.pcs.service.impl;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import vertexid.nhmarket.pcs.cmmn.DateUtil;
import vertexid.nhmarket.pcs.cmmn.FileUtil;
import vertexid.nhmarket.pcs.service.ComCodeVO;
import vertexid.nhmarket.pcs.service.DefaultJsonVO;
import vertexid.nhmarket.pcs.service.LoginVO;
import vertexid.nhmarket.pcs.service.PcsMobileService;
import vertexid.nhmarket.pcs.service.PickCheckVO;
import vertexid.nhmarket.pcs.service.PickHeaderVO;
import vertexid.nhmarket.pcs.service.PickMobileDetailVO;
import vertexid.nhmarket.pcs.service.PickTotalVO;
import vertexid.nhmarket.pcs.service.PickingStateVO;
import vertexid.nhmarket.pcs.service.ReportItemVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("pcsMobileService")
public class PcsMoblieServiceImpl extends EgovAbstractServiceImpl implements PcsMobileService {
	
	// Logger 생성용
	private static final Logger LOGGER = LoggerFactory.getLogger(PcsMoblieServiceImpl.class);

	@Resource(name = "pcsMobileMapper")
	private PcsMoblieMapper pcsMoblieMapper;

	@Resource(name = "pcsMapper")
	private PcsMapper pcsMapper;

	// Transaction 처리용 Manager
	@Resource(name = "txManager")
	PlatformTransactionManager txManager;

	/**
	 * 피킹 시작
	 */
	@Override
	public PickCheckVO pickingList(PickingStateVO pickingStateVO) {
		// Transaction 처리용
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);

		PickCheckVO pickCheckVO = new PickCheckVO();
		try {
			ArrayList<String> pickIdList = pickingStateVO.getPickIdList();
			for (String pickId : pickIdList) {
				PickHeaderVO headerVO = pcsMoblieMapper.selectHeader(pickId);
				if (headerVO == null) {
					txManager.commit(status);
					pickCheckVO.setMessage("[" + pickId + "] 는 존재 하지 않은 라벨입니다.");
					return pickCheckVO;
				}
			}

			// 진행상태 (0:피킹예정,1:피킹지시,2:피킹완료)
			List<PickHeaderVO> headerVOs = pcsMoblieMapper.selectStateHeaderList(pickingStateVO);
			// if (headerVOs.size() != pickingStateVO.getPickIdList().size()) {
			// txManager.commit(status);
			// pickCheckVO.setMessage("[" + workerName + "] 작업을 완료하였습니다.");
			// return pickCheckVO;
			// }

			for (PickHeaderVO headerVO : headerVOs) {
				System.out.println(headerVO);
				pickCheckVO.setSuccess(false);
				pickCheckVO.setState_code(headerVO.getState_code());
				pickCheckVO.setPick_id(headerVO.getPick_id());

				if (headerVO.getState_code().equals("2")) {
					// 피킹완료 체크
					String workerName = ""; // 작업자 이름
					List<ComCodeVO> selectComCodeList = pcsMapper.selectComCodeList(new ComCodeVO("WORKER_ID", headerVO.getWorker_id()));
					if (selectComCodeList.size() > 0) {
						workerName = selectComCodeList.get(0).getCom_name();
					}
					pickCheckVO.setMessage(workerName + "님이 [" + pickCheckVO.getPick_id() + "] 작업을 완료하였습니다.");

					txManager.commit(status);
					return pickCheckVO;

				} else if (headerVO.getStart_pick_date() != null) {
					// 작업 시작 체크

					// // 작업 시작되었어도 작업자가 같으면 진행
					if (!pickingStateVO.getWorkerId().equals(headerVO.getWorker_id())) {
						// 작업자가 다르면 체크
						String workerName = ""; // 작업자 이름
						List<ComCodeVO> selectComCodeList = pcsMapper.selectComCodeList(new ComCodeVO("WORKER_ID", headerVO.getWorker_id()));
						if (selectComCodeList.size() > 0) {
							workerName = selectComCodeList.get(0).getCom_name();
						}
						pickCheckVO.setMessage(workerName + "님이 [" + pickCheckVO.getPick_id() + "] 작업을 시작하였습니다.");

						txManager.commit(status);
						return pickCheckVO;
					}

				} else {
					// 배송일자 체크
					String delivery_date = headerVO.getDelivery_date();
					if (!DateUtil.isToday(delivery_date)) {
						txManager.commit(status);
						pickCheckVO.setMessage(pickCheckVO.getPick_id() + "] 오늘 날짜의 작업이 아닙니다.");
						return pickCheckVO;
					}

				}
			}

			// 헤더 테이블 업데이트 (작업자, 작업일시)
			int check = pcsMoblieMapper.updateStartPickHeader(pickingStateVO);
			
			// M20170214 카운트 하는 로직으로 다시 막음 
//			pcsMoblieMapper.updateStartPickHeader(pickingStateVO);
			
			// A20170214 workerId와 start_pick_date가 업데이트 되면 상세내역을 조회하여 PDA에 전달 
			if(check > 0){
				// pickId 와 매핑된 제품 가져옴
				
				List<PickMobileDetailVO> pickMobileDetailVOs = pcsMoblieMapper.selectMappingDetailList(pickingStateVO.getPickIdList());
				System.out.println("===============pickMobileDetailVOs===============" + pickMobileDetailVOs);
				
				pickCheckVO.setSuccess(true);
				pickCheckVO.setMobileDetailVOs(pickMobileDetailVOs);
				
				// 버전 정보 가져옴
				ArrayList<String> comGroupCodeList = new ArrayList<>();
				comGroupCodeList.add("MOBILE_VERSION");
				List<ComCodeVO> selectComGroupCodeList = pcsMoblieMapper.selectComGroupCodeList(comGroupCodeList);
				if (selectComGroupCodeList.size() > 0) {
					ComCodeVO comCodeVO = selectComGroupCodeList.get(0);
					pickCheckVO.setVersion(comCodeVO.getCom_sub_code());
				}				
			}else if(check == 0){
				LOGGER.info("PickCheckVO pickingList 피킹시작 업데이트 없을 때 : " + pickingStateVO.getPickIdList().toString());
			}

		} catch (Exception e) {
			txManager.rollback(status);
			LOGGER.error(e.toString());
			PickCheckVO result = new PickCheckVO();
			result.setSuccess(false);
			result.setMessage(e.toString());
			return result;
		}
		txManager.commit(status);

		return pickCheckVO;
	}

	/**
	 * 피킹 취소
	 */
	@Override
	public DefaultJsonVO pickingCancel(ArrayList<String> pickIdList) {
		int check = pcsMoblieMapper.updatePickCancel(pickIdList);
		if (check > 0) {
			return new DefaultJsonVO(true, null);
		} else {
			return new DefaultJsonVO(false, "피킹 취소에 실패되었습니다.");
		}
	}

	/**
	 * 작업 완료
	 */
	@Override
	public DefaultJsonVO pickingComplete(ArrayList<PickMobileDetailVO> pickMobileDetailVOs) {

		// Transaction 처리용
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);

		int check = 0;

		try {
			// 필요 데이터 - 피킹 아이디
			// 헤더 테이블
			// state_code = 2 (피킹완료) 로 수정, end_pick_date 날짜 수정

			Set<String> pickIds = new HashSet<>(); // pickId 뽑아냄(중복제거)
			for (PickMobileDetailVO pickMobileDetailVO : pickMobileDetailVOs) {
				pickIds.add(pickMobileDetailVO.getPick_id());
			}
			LOGGER.info("완료 대상 피킹 아이디 : " + pickIds.toString());

			LOGGER.info("완료 Detail 상태코드 update");
			for (PickMobileDetailVO pickMobileDetailVO : pickMobileDetailVOs) {
				check = pcsMoblieMapper.updateEndPickDetail(pickMobileDetailVO);
			}

			if (check > 0) {
				LOGGER.info("완료 Header 상태코드 update");
				for (String pick_id : pickIds) {
					// 특정 pickId로 디테일 테이블 조회해서 전체 완료 된것만 헤더 업데이트 (오늘날짜만)
					pcsMoblieMapper.updateEndPickHeader(pick_id);
				}
				// 완료 여부 체크해서 프린터
				// 프린트 시간이 오래 걸려 프로세스에서 제외시키고 Spring Scheduler로 대체함
				checkPrint(new ArrayList<String>(pickIds), 1);
			}

		} catch (Exception e) {
			txManager.rollback(status);
			LOGGER.error(e.toString());
			return new DefaultJsonVO(false, "");
		}
		txManager.commit(status);

		LOGGER.info("****** pickingComplete END ******");

		if (check > 0) {
			return new DefaultJsonVO(true, "");
		} else {
			LOGGER.error("완료가 실패되었습니다. check = " + check);
			return new DefaultJsonVO(false, "완료가 실패되었습니다.");
		}
		
	}

	/**
	 * 주문진행현황조회에서 호출
	 */
	@Override
	public void checkPrint(ArrayList<String> pick_ids, int printType) { // printType 1: 자동 , 2: 선택출력

		try {

			LOGGER.info("============ checkPrint : ============");

			// Root Path
			ComCodeVO filePathVo = new ComCodeVO();
			filePathVo.setCom_group_code("FILE_PATH");
			String rootPath = pcsMapper.selectComCodeList(filePathVo).get(0).getCom_sub_code();
			LOGGER.info("파일path 조회 ============" + rootPath);

			LOGGER.info("주문일시, 주문번호 조회 ============");
			List<PickMobileDetailVO> orderInfos = pcsMoblieMapper.selectOrderInfo(pick_ids);

			Map<String, Object> pickMobileDetailMap = new HashMap<String, Object>();

			// 이후 pdf 파일 생성 및 프린트
			for (PickMobileDetailVO pickMobileDetailVO : orderInfos) {

				pickMobileDetailMap.put("order_date", pickMobileDetailVO.getOrder_date());
				pickMobileDetailMap.put("order_no", pickMobileDetailVO.getOrder_no());
				//pickMobileDetailMap.put("pick_id", pickMobileDetailVO.getPick_id());
				
				LOGGER.info("완료여부 조회 ============");
				int count = pcsMoblieMapper.selectHeaderComplete(pickMobileDetailMap);

				Map<String, Object> pickMobileTotalMap = new HashMap<String, Object>();

				pickMobileTotalMap.put("order_date", pickMobileDetailVO.getOrder_date());
				pickMobileTotalMap.put("order_no", pickMobileDetailVO.getOrder_no());
				//pickMobileDetailMap.put("pick_id", pickMobileDetailVO.getPick_id());

//				LOGGER.info("프린트 상태값 업데이트 진행 count : " + count + ", orderInfo : " + orderDate + orderNo);

				if (count > 0) {
					// 완료된 주문 정보 가져오기
					LOGGER.info("완료된 주문 정보 가져오기 ============주문일자: "+pickMobileDetailVO.getOrder_date()+"주문번호: "+pickMobileDetailVO.getOrder_no());
					List<PickTotalVO> pickTotalVOs = pcsMoblieMapper.selectTotalList(pickMobileTotalMap);

					// 식자재 존 정보 가져오기
					LOGGER.info("식자재 존 정보 가져오기 ============");
					ComCodeVO comCodeVO = new ComCodeVO();
					comCodeVO.setCom_group_code("ZONE_CODE");
					comCodeVO.setCom_sub_code("1_");
					List<ComCodeVO> comCodeVOs = pcsMapper.selectComCodeList(comCodeVO);

					// 식자재 여부 판단
					List<PickTotalVO> foods = new ArrayList<PickTotalVO>();
					List<PickTotalVO> basics = new ArrayList<PickTotalVO>();

					for (PickTotalVO pickTotalVO : pickTotalVOs) {
						if (isFood(pickTotalVO, comCodeVOs)) {
							// 식자재일 경우 pickTotalVO에 추가
							foods.add(pickTotalVO);
						} else {
							// 기본일 경우 pickTotalVO에 추가
							basics.add(pickTotalVO);
						}
					}

					// 거래명세서 종류 (1:기본, 2:식자재)
					if (basics.size() > 0) {
						// 거래명세서 출력
						printSpecifications(basics, rootPath, "1", printType);
					}

					if (foods.size() > 0) {
						// 거래명세서 식(식자재) 출력
						printSpecifications(foods, rootPath, "2", printType);
					}

					// 주문확인서 출력
					printConfirmationOrder(pickTotalVOs, rootPath, printType);

					LOGGER.info("============== checkPrint End!!! ==============");
				}else{
					LOGGER.info("완료되지 않은 주문 주문일자: "+pickMobileDetailVO.getOrder_date()+"주문번호: "+pickMobileDetailVO.getOrder_no());
				}

			}// endfor

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	/**
	 * 현황판 스케줄러에서 호출 -> 톰캣서버에서 호출로 변경
	 */
	public void checkPrintSch(PickHeaderVO orgOrderNoNew) {

		try {
			ComCodeVO filePathVo = new ComCodeVO();
			filePathVo.setCom_group_code("FILE_PATH");
			String rootPath = pcsMapper.selectComCodeList(filePathVo).get(0).getCom_sub_code();

			// ArrayList를 Map으로 형변환해서 맵핑
			Map<String, Object> pickMobileTotalMap = new HashMap<String, Object>();

			pickMobileTotalMap.put("order_date", orgOrderNoNew.getOrder_date());
			pickMobileTotalMap.put("order_no", orgOrderNoNew.getOrder_no());
			//pickMobileTotalMap.put("pick_id", orgOrderNoNew.getPick_id());

			
			List<PickTotalVO> pickTotalVOs = pcsMoblieMapper.selectTotalList(pickMobileTotalMap);
			LOGGER.info("===================pickTotalVOs===========출력할 주문번호 : " + pickTotalVOs.get(0).getOrder_no()); // pickTotalVOs 데이터 확인용 로그
			
			// 식자재 존 정보 가져오기
			ComCodeVO comCodeVO = new ComCodeVO();
			comCodeVO.setCom_group_code("ZONE_CODE");
			comCodeVO.setCom_sub_code("1_"); // Da = 식자재/상온, Db = 식자재/냉장, Dc = 식자재/냉동, Dd = 식자재/대용량, 
			List<ComCodeVO> comCodeVOs = pcsMapper.selectComCodeList(comCodeVO);

			// 식자재 여부 판단
			List<PickTotalVO> foods = new ArrayList<PickTotalVO>();
			List<PickTotalVO> basics = new ArrayList<PickTotalVO>();
			for (PickTotalVO pickTotalVO : pickTotalVOs) {
				if (isFood(pickTotalVO, comCodeVOs)) {
					// 식자재
					foods.add(pickTotalVO);
				} else {
					// 기본
					basics.add(pickTotalVO);

				}
			}

			// 거래명세서 종류 (1:기본, 2:식자재)
			if (basics.size() > 0) {
				LOGGER.info("========= 기본 printSpecifications() ==========");
				// 거래명세서 출력
				printSpecifications(basics, rootPath, "1", 1);
			}

			if (foods.size() > 0) {
				LOGGER.info("========= 식자재 printSpecifications() ==========");
				// 거래명세서 식자재 출력
				printSpecifications(foods, rootPath, "2", 1);
			}

			// 주문확인서 출력
			LOGGER.info("========= 주문확인서 printSpecifications() ==========");
			printConfirmationOrder(pickTotalVOs, rootPath, 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

	/**
	 * 
	 * 식자재 여부
	 * 
	 * @param pickTotalVO
	 * @param comCodeVOs
	 * @return
	 */
	private boolean isFood(PickTotalVO pickTotalVO, List<ComCodeVO> comCodeVOs) {
		for (ComCodeVO codeVO : comCodeVOs) {
			if (pickTotalVO.getZone_code().equalsIgnoreCase(codeVO.getCom_code())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 거래 명세서 출력
	 * 
	 * @param pickTotalVOs
	 * @param rootPath
	 * @param spc_knd
	 *            거래명세서 종류 (1:기본, 2:식류품)
	 */
	public void printSpecifications(List<PickTotalVO> pickTotalVOs, String rootPath, String spc_knd, int printType) {
		
		LOGGER.info("========= 기본 printSpecifications() start ==========");
		
		if (pickTotalVOs == null) {
			LOGGER.info("printSpecifications is null");
			return;
		} else if (pickTotalVOs.size() == 0) {
			LOGGER.info("printSpecifications 사이즈 0");
			return;
		}

		LOGGER.info("printSpecifications pickTotalVOs.size() " + pickTotalVOs.size());

		int order_qty_sum = 0; // 주문수량
		int order_cost_sum = 0; // 주문합계
		int sold_out_qty_sum = 0; // 결품수량
		int change_pick_qty_sum = 0; // 대체수량
		int pick_qty_sum = 0; // 배송수량

		// 주문합계
		int sold_out_cost_sum = 0; // 결품 합계
		int pos_cost_sum = 0; // POS(외상매출) 합계
		int min_sum = 0; // 대체 차액 합계
		// A20170316 osj 대체 혜택 합계 추가
		int pls_sum = 0; // 대체 혜택 합계
		
		String pay_method = ""; // 결제방법
		
		String e_pay_cost = ""; // e-주문금액
		String dlvry_pay_cost = ""; // 택배주문금액
		String e_sent_fee = ""; // e-배송비
		String dlvry_sent_fee = ""; // 택배배송비
		String e_card_dc = ""; // e-카드즉시할인
		String dlvry_card_dc = ""; // 택배카드즉시할인
		String coupon_dc = ""; // 쿠폰할인
		String pay_cost = ""; // 결제금액
		
		int order_pay_sum = 0; // 주문금액합계
		int delivery_pay_sum = 0; // 배송비합계
		int card_dc_sum = 0; // 카드즉시할인합계
		
		ArrayList<ReportItemVO> dataList = new ArrayList<>();
		
		for (int i = 0; i < pickTotalVOs.size(); i++) {
			PickTotalVO pickTotalVO = pickTotalVOs.get(i);

			String row = "0";
			row = String.valueOf(i + 1);
			
			ArrayList<ReportItemVO> createReportRow = createReportRow(row, pickTotalVO, true);
			if (createReportRow != null) {
				dataList.addAll(createReportRow);
			}

			if (pickTotalVO.getSold_out_reason() != null) { // 결품 존재
				int sold_out_qty = pickTotalVO.getOrder_qty() - pickTotalVO.getPick_qty(); // 결품 수량
				sold_out_cost_sum += (pickTotalVO.getOrder_cost() * sold_out_qty); // 결품 합계
			}
			
			if(!"".equals(pickTotalVO.getPay_method())){ // 결제방법
				pay_method = pickTotalVO.getPay_method();
			}

			if(!"".equals(pickTotalVO.getE_pay_cost())){ // e-주문금액
				e_pay_cost = pickTotalVO.getE_pay_cost();
			}
			if(!"".equals(pickTotalVO.getDlvry_pay_cost())){ // 택배주문금액
				dlvry_pay_cost = pickTotalVO.getDlvry_pay_cost();
			}
			if(!"".equals(pickTotalVO.getE_sent_fee())){ // e-배송비
				e_sent_fee = pickTotalVO.getE_sent_fee();
			}
			if(!"".equals(pickTotalVO.getDlvry_sent_fee())){ // 택배배송비
				dlvry_sent_fee = pickTotalVO.getDlvry_sent_fee();
			}
			if(!"".equals(pickTotalVO.getE_card_dc())){ // e-카드즉시할인
				e_card_dc = pickTotalVO.getE_card_dc();
			}
			if(!"".equals(pickTotalVO.getDlvry_card_dc())){ // 택배카드즉시할인
				dlvry_card_dc = pickTotalVO.getDlvry_card_dc();
			}
			if(!"".equals(pickTotalVO.getCoupon_dc())){ // 쿠폰할인
				coupon_dc = pickTotalVO.getCoupon_dc();
			}
			if(!"".equals(pickTotalVO.getPay_cost())){ // 결제금액
				pay_cost = pickTotalVO.getPay_cost();
			}
			
		}

		// 합계 구하기
		for (int i = 0; i < dataList.size(); i++) {
			ReportItemVO reportItemVO = dataList.get(i);

			order_qty_sum += reportItemVO.getOrder_qty(); // 주문수량
			// M20170316 osj 원주문의 물품 합계 금액
//			if (!reportItemVO.getLck_knd().equals(ReportItemVO.LCK_KND_REPLACE)) { // 대체 아닐때
//				order_cost_sum += reportItemVO.getOrder_cost_total(); // 주문합계
//			}
			
			order_cost_sum += reportItemVO.getOrder_cost() * reportItemVO.getOrder_qty(); // 주문합계
			
			sold_out_qty_sum += reportItemVO.getSold_out_qty(); // 결품수량
			change_pick_qty_sum += reportItemVO.getChange_pick_qty(); // 대체수량
			pick_qty_sum += reportItemVO.getPick_qty(); // 배송수량
			min_sum += reportItemVO.getMin_sum(); // 대체 차액 합계 (가격 내려간 상품만)
			pls_sum += reportItemVO.getPls_sum(); // 대체 혜택 합계
		}

		// M20170316 osj pos 합계 계산식 정정
		pos_cost_sum = order_cost_sum - (sold_out_cost_sum + min_sum); // POS(외상매출) 합계 = 주문합계-(결품합계+대체하위합계)
		
		// 주문금액합계 = e-주문금액+택배주문금액
		order_pay_sum = Integer.parseInt(e_pay_cost) + Integer.parseInt(dlvry_pay_cost);
		// 배송비합계 = e-배송비+택배_배송비
		delivery_pay_sum = Integer.parseInt(e_sent_fee) + Integer.parseInt(dlvry_sent_fee);
		// 카드즉시할인합계 = e-카드즉시할인+택배_카드즉시할인
		card_dc_sum = Integer.parseInt(e_card_dc) + Integer.parseInt(dlvry_card_dc);

		PickTotalVO pickTotalVO = pickTotalVOs.get(0);

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);

		Map<String, Object> params = new HashMap<String, Object>();

		
		
		// delivery_time 배송시간
		// min_sum 차액 합계
		// sold_out_qty_sum 결품 합계 -> 가격 합계로 수정 해야함

		// 거래명세서 종류 (1:기본, 2:식류품)
		params.put("spc_knd", spc_knd);

		/** 탑 */
		params.put("short_order_no", pickTotalVO.getShort_order_no()); // 단축주문번호
		params.put("delivery_area_name", pickTotalVO.getDelivery_area_name()); // 배송구명

		/** 주문/배송정보 */
		params.put("order_customer_name", pickTotalVO.getOrder_customer_name()); // 수취고객명
		params.put("org_order_no", pickTotalVO.getOrder_date()+ "-" + pickTotalVO.getOrder_no()); // 주문일자+주문번호
		// M20170605 OSJ 원거래번호에서 배송일자로 날짜 변경
		params.put("order_date", DateUtil.parseDate(pickTotalVO.getDelivery_date())); // 배송일자
		params.put("delivery_count", pickTotalVO.getDelivery_count()); // 배송차수
		params.put("customer_name", pickTotalVO.getCustomer_name()); // 주문고객명
		//params.put("customer_name", "서초*********집집집"); // 받는고객
		params.put("zone_code", pickTotalVO.getZone_code()+ "\n" + pickTotalVO.getPromotion_name()); // 존코드 \n 프로모션명
		params.put("promotion_name", pickTotalVO.getPromotion_name()); // 프로모션명
		params.put("tel_no_1", pickTotalVO.getTel_no_1()); // 연락처1 (집전화번호)
		params.put("tel_no_2", pickTotalVO.getTel_no_2()); // 연락처2 (휴대폰번호)
		if(pickTotalVO.getDelivery_amount() == 0){
			params.put("delivery_amount", "무료"); // 배송비
		}else{
			params.put("delivery_amount", pickTotalVO.getDelivery_amount()); // 배송비			
		}
		// M20170727 LJM 대체여부 수정
		if("Y".equalsIgnoreCase(pickTotalVO.getChange_allow_yn())) {
			params.put("change_allow_yn", "동의"); // 대체여부			
		} else {
			params.put("change_allow_yn", "거절"); // 대체여부			
			
		}
		

		params.put("address", pickTotalVO.getAddress() + " " + pickTotalVO.getAddress_detail()); // 배송주소+배송주소상세
		params.put("address_detail", pickTotalVO.getAddress_detail()); // 배송주소 상세
		params.put("free_gift_name", pickTotalVO.getFree_gift_name()); // 사은품
		params.put("delivery_message", pickTotalVO.getDelivery_message()); // 배송메시지
		params.put("goods_message", pickTotalVO.getGoods_message()); // 상품메시지
		params.put("pay_method", pay_method); // 결제방법

		/** 합계 */
		params.put("order_qty_sum", order_qty_sum); // 주문수량
		params.put("order_cost_sum", order_cost_sum); // 주문합계
		params.put("sold_out_qty_sum", sold_out_qty_sum); // 결품수량
		params.put("change_pick_qty_sum", change_pick_qty_sum); // 대체수량
		params.put("pick_qty_sum", pick_qty_sum); // 배송수량

		params.put("e_pay_cost", e_pay_cost); // e-주문금액
		params.put("dlvry_pay_cost", dlvry_pay_cost); // 택배주문금액
		params.put("e_sent_fee", e_sent_fee); // e-배송비
		params.put("dlvry_sent_fee", dlvry_sent_fee); // 택배배송비
		params.put("e_card_dc", e_card_dc); // e-카드즉시할인
		params.put("dlvry_card_dc", dlvry_card_dc); // 택배카드즉시할인
		params.put("coupon_dc", coupon_dc); // 쿠폰할인
		params.put("pay_cost", pay_cost); // 결제금액

		/**  */
		// order_cost_sum 주문합계
		params.put("sold_out_cost_sum", sold_out_cost_sum); // 결품 합계
		params.put("pos_cost_sum", pos_cost_sum); // POS(외상매출) 합계
		params.put("min_sum", min_sum); // 대체 차액 합계
		params.put("pls_sum", pls_sum); // 대체 혜택 합계
		
		params.put("order_pay_sum", order_pay_sum); // 주문금액합계
		params.put("delivery_pay_sum", delivery_pay_sum); // 배송비합계
		params.put("card_dc_sum", card_dc_sum); // 카드즉시할인합계

		params.put("rootPath", rootPath); // 루트 경로
		

		// 거래 명세서 파일 이름
		String jasperFileName = rootPath + "\\jasper\\specifications.jasper";
		// 오늘 날짜 폴더로 생성 후 그 폴더에 pdf 생성
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String today = format.format(new Date());
		FileUtil.createFolder("D:\\vertexid\\pdf\\" + today);
		String destFileName = "D:\\vertexid\\pdf\\" + today + "\\specifications_" + spc_knd + "_" + pickTotalVO.getOrder_date() + pickTotalVO.getOrder_no() + ".pdf";
		
		Map<String, Object> map =  new HashMap<String, Object>();
		map.put("file_type", spc_knd); // 1: 거래명세서 2: 거래명세서(식)
		map.put("order_no", pickTotalVO.getOrder_date() + pickTotalVO.getOrder_no()); // 주문일자 + 주문번호 저장
		map.put("file_name", destFileName); 
		map.put("state", "1"); // 1: 파일생성 준비
		
		LOGGER.info("파일 있는지 확인 [주문번호] "+pickTotalVO.getOrder_date() + pickTotalVO.getOrder_no());
		int orderCnt = pcsMoblieMapper.selectPrintFileDataOrderNo(map);
		if(orderCnt>0){ // 주문번호에 해당하는 파일존재
			if(printType==2){ // 선택출력
				map.put("state", "2"); 
				pcsMoblieMapper.updatePrintFileData(map);
			}else{ 
				return;
			}
		}else{
			LOGGER.info("파일 상태 업데이트 (파일생성 준비)");
			pcsMoblieMapper.insertPrintFileData(map);
		}
		
		try {
			// pdf 생성
			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);
			
			if (printFileName != null) {
				LOGGER.info("printFileName : " + printFileName);
				LOGGER.info("destFileName : "  + destFileName);
				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);
		
				map.put("state", "2"); // 2: 파일생성
				LOGGER.info("파일 상태 업데이트 (파일생성 완료)");
				pcsMoblieMapper.updatePrintFileData(map);
//				pdfPrint(destFileName); 프린트 분리 2018-09-19
				LOGGER.info("========= 기본 printSpecifications() end ==========");
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			map.put("state", "F"); // Fail
			LOGGER.info("파일 상태 업데이트 (파일생성 실패)");
			pcsMoblieMapper.updatePrintFileData(map);
		}

	}

	/**
	 * 주문 확인서 출력
	 * 
	 * @param pickTotalVOs
	 * @param rootPath
	 */
public void printConfirmationOrder(List<PickTotalVO> pickTotalVOs, String rootPath, int printType) {

		
		LOGGER.info("========= printConfirmationOrder() start ==========");
		///////////////////////////////////////////////////////////////////////
		// 기본 정보 및 validation 셋팅부
		///////////////////////////////////////////////////////////////////////
		if (pickTotalVOs == null) {
			LOGGER.info("printConfirmationOrder is null");
			return;
		} else if (pickTotalVOs.size() == 0) {
			LOGGER.info("printConfirmationOrder 사이즈 0");
			return;
		}

//		LOGGER.info("printConfirmationOrder pickTotalVOs " + pickTotalVOs);
//		LOGGER.info("printConfirmationOrder rootPath " + rootPath);

		int order_qty_sum = 0; // 주문수량
		int order_cost_sum = 0; // 주문합계
		int sold_out_qty_sum = 0; // 결품수량
		int change_pick_qty_sum = 0; // 대체수량
		int pick_qty_sum = 0; // 배송수량

		// 주문합계
		int sold_out_cost_sum = 0; // 결품 합계
		int min_sum = 0; // 대체 차액 합계
		
		String pay_method = ""; // 결제방법
		
		String e_pay_cost = ""; // e-주문금액
		String dlvry_pay_cost = ""; // 택배주문금액
		String e_sent_fee = ""; // e-배송비
		String dlvry_sent_fee = ""; // 택배배송비
		String e_card_dc = ""; // e-카드즉시할인
		String dlvry_card_dc = ""; // 택배카드즉시할인
		String coupon_dc = ""; // 쿠폰할인
		String pay_cost = ""; // 결제금액
		
		int order_pay_sum = 0; // 주문금액합계
		int delivery_pay_sum = 0; // 배송비합계
		int card_dc_sum = 0; // 카드즉시할인합계
		
//		String cancel_addr = ""; // 반품주소
//		String custom_center = ""; // 고객센터

		///////////////////////////////////////////////////////////////////////
		// 비즈니스 로직 구현부
		///////////////////////////////////////////////////////////////////////
		ArrayList<ReportItemVO> dataList = new ArrayList<>();
		for (int i = 0; i < pickTotalVOs.size(); i++) {
			PickTotalVO pickTotalVO = pickTotalVOs.get(i);

			String row = "0";
			//String orderRowNo = pickTotalVO.getOrder_row_no();
			
			//리포트 순번 부여
			/*if (orderRowNo != null) {
			row = orderRowNo;
			} else {*/
			row = String.valueOf(i + 1);
			/*}*/

			ArrayList<ReportItemVO> createReportRow = createReportRow(row, pickTotalVO, false);
			if (createReportRow != null) {
				dataList.addAll(createReportRow);
			}

			if (pickTotalVO.getSold_out_reason() != null) { // 결품 존재
				int sold_out_qty = pickTotalVO.getOrder_qty() - pickTotalVO.getPick_qty(); // 결품 수량
				sold_out_cost_sum += pickTotalVO.getOrder_cost() * sold_out_qty; // 결품 합계
			}
			
			if(!"".equals(pickTotalVO.getPay_method())){ // 결제방법
				pay_method = pickTotalVO.getPay_method();
			}

			if(!"".equals(pickTotalVO.getE_pay_cost())){ // e-주문금액
				e_pay_cost = pickTotalVO.getE_pay_cost();
			}
			if(!"".equals(pickTotalVO.getDlvry_pay_cost())){ // 택배주문금액
				dlvry_pay_cost = pickTotalVO.getDlvry_pay_cost();
			}
			if(!"".equals(pickTotalVO.getE_sent_fee())){ // e-배송비
				e_sent_fee = pickTotalVO.getE_sent_fee();
			}
			if(!"".equals(pickTotalVO.getDlvry_sent_fee())){ // 택배배송비
				dlvry_sent_fee = pickTotalVO.getDlvry_sent_fee();
			}
			if(!"".equals(pickTotalVO.getE_card_dc())){ // e-카드즉시할인
				e_card_dc = pickTotalVO.getE_card_dc();
			}
			if(!"".equals(pickTotalVO.getDlvry_card_dc())){ // 택배카드즉시할인
				dlvry_card_dc = pickTotalVO.getDlvry_card_dc();
			}
			if(!"".equals(pickTotalVO.getCoupon_dc())){ // 쿠폰할인
				coupon_dc = pickTotalVO.getCoupon_dc();
			}
			if(!"".equals(pickTotalVO.getPay_cost())){ // 결제금액
				pay_cost = pickTotalVO.getPay_cost();
			}
			
		}

		// 합계 구하기
		for (int i = 0; i < dataList.size(); i++) {
			ReportItemVO reportItemVO = dataList.get(i);

			order_qty_sum += reportItemVO.getOrder_qty(); // 주문수량
			// M20170314 osj 원주문의 물품 합계 금액
//			if (!reportItemVO.getLck_knd().equals(ReportItemVO.LCK_KND_REPLACE)) { // 대체 아닐때
//				order_cost_sum += reportItemVO.getOrder_cost_total(); // 주문합계
//			}
			order_cost_sum += reportItemVO.getOrder_cost() * reportItemVO.getOrder_qty(); // 주문합계
			
			sold_out_qty_sum += reportItemVO.getSold_out_qty(); // 결품수량
			change_pick_qty_sum += reportItemVO.getChange_pick_qty(); // 대체수량
			pick_qty_sum += reportItemVO.getPick_qty(); // 배송수량
			min_sum += reportItemVO.getMin_sum(); // 대체 차액 합계 (가격 내려간 상품만)
		}
		
		// 주문금액합계 = e-주문금액+택배주문금액
		order_pay_sum = Integer.parseInt(e_pay_cost) + Integer.parseInt(dlvry_pay_cost);
		// 배송비합계 = e-배송비+택배_배송비
		delivery_pay_sum = Integer.parseInt(e_sent_fee) + Integer.parseInt(dlvry_sent_fee);
		// 카드즉시할인합계 = e-카드즉시할인+택배_카드즉시할인
		card_dc_sum = Integer.parseInt(e_card_dc) + Integer.parseInt(dlvry_card_dc);
		
		// A20170724 osj 점포정보 가져오기
		ComCodeVO comCodeVO = new ComCodeVO();
		comCodeVO.setCom_group_code("STORE_INFO");
		List<ComCodeVO> st_info = pcsMapper.selectComCodeList(comCodeVO);

		PickTotalVO pickTotalVO = pickTotalVOs.get(0);

		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);

		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
		Map<String, Object> params = new HashMap<String, Object>();

		/** 탑 */
		params.put("short_order_no", pickTotalVO.getShort_order_no()); // 단축주문번호
		params.put("delivery_area_name", pickTotalVO.getDelivery_area_name()); // 배송구명

		/** 주문/배송정보 */
		params.put("order_customer_name", pickTotalVO.getOrder_customer_name()); // 주문고객
		params.put("org_order_no", pickTotalVO.getOrder_date()+ "-"+pickTotalVO.getOrder_no()); // 주문일자+주문번호
		// M20170605 OSJ 원거래번호에서 배송일자로 날짜 변경
		params.put("order_date", DateUtil.parseDate(pickTotalVO.getDelivery_date())); // 배송날짜+배송차수
		params.put("delivery_count", pickTotalVO.getDelivery_count()); // 배송차수
		params.put("customer_name", pickTotalVO.getCustomer_name()); // 받는고객
		params.put("promotion_name", pickTotalVO.getPromotion_name()); // 프로모션명
		params.put("tel_no_1", pickTotalVO.getTel_no_1()); // 연락처1
		params.put("tel_no_2", pickTotalVO.getTel_no_2()); // 연락처2
		if(pickTotalVO.getDelivery_amount() == 0){
			params.put("delivery_amount", "무료"); // 배송비						
		}else{
			params.put("delivery_amount", pickTotalVO.getDelivery_amount()); // 배송비			
		}
		// M20170727 LJM 대체여부 수정
		if("Y".equalsIgnoreCase(pickTotalVO.getChange_allow_yn())) {
			params.put("change_allow_yn", "동의"); // 대체여부			
		} else {
			params.put("change_allow_yn", "거절"); // 대체여부			
			
		}
		
		params.put("address", pickTotalVO.getAddress() + " " + pickTotalVO.getAddress_detail()); // 배송주소+배송주소상세
		//params.put("address_detail", pickTotalVO.getAddress_detail()); // 배송주소 상세
		params.put("free_gift_name", pickTotalVO.getFree_gift_name()); // 사은품
		params.put("delivery_message", pickTotalVO.getDelivery_message()); // 배송메시지
		params.put("goods_message", pickTotalVO.getGoods_message()); // 상품메시지
		params.put("pay_method", pay_method); // 결제방법

		/** 합계 */
		params.put("order_qty_sum", order_qty_sum); // 주문수량
		params.put("order_cost_sum", order_cost_sum); // 주문합계
		params.put("sold_out_qty_sum", sold_out_qty_sum); // 결품수량
		params.put("change_pick_qty_sum", change_pick_qty_sum); // 대체수량
		params.put("pick_qty_sum", pick_qty_sum); // 배송수량

		params.put("e_pay_cost", e_pay_cost); // e-주문금액
		params.put("dlvry_pay_cost", dlvry_pay_cost); // 택배주문금액
		params.put("e_sent_fee", e_sent_fee); // e-배송비
		params.put("dlvry_sent_fee", dlvry_sent_fee); // 택배배송비
		params.put("e_card_dc", e_card_dc); // e-카드즉시할인
		params.put("dlvry_card_dc", dlvry_card_dc); // 택배카드즉시할인
		params.put("coupon_dc", coupon_dc); // 쿠폰할인
		params.put("pay_cost", pay_cost); // 결제금액

		/**  */
		// order_cost_sum 주문합계
		params.put("sold_out_cost_sum", sold_out_cost_sum); // 결품 합계
		params.put("min_sum", min_sum); // 대체 차액 합계
		
		params.put("order_pay_sum", order_pay_sum); // 주문금액합계
		params.put("delivery_pay_sum", delivery_pay_sum); // 배송비합계
		params.put("card_dc_sum", card_dc_sum); // 카드즉시할인합계
		
		params.put("rootPath", rootPath); // 루트 경로
		
		/** A20170724 osj 반품주소와 연락처 추가 */
		params.put("cancel_addr",st_info.get(0).getCom_sub_code());
		params.put("custom_center",st_info.get(0).getCom_desc()+")");
		
		// 주문 확인서 파일 이름
		String jasperFileName = rootPath + "\\jasper\\confirmationOrder.jasper";
		// 오늘 날짜 폴더로 생성 후 그 폴더에 pdf 생성
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String today = format.format(new Date());
		FileUtil.createFolder("D:\\vertexid\\pdf\\" + today);
		String destFileName = "D:\\vertexid\\pdf\\" + today + "\\confirmationOrder_" +  pickTotalVO.getOrder_date()+pickTotalVO.getOrder_no() + ".pdf";
		
		Map<String, Object> map =  new HashMap<String, Object>();
		map.put("file_type", "3"); // 2: 주문확인서
		map.put("order_no", pickTotalVO.getOrder_date() + pickTotalVO.getOrder_no()); // 주문일자 + 주문번호 저장
		map.put("file_name", destFileName); 
		map.put("state", "1"); // 1: 파일생성 준비
		
		LOGGER.info("파일 있는지 확인 [주문번호] "+pickTotalVO.getOrder_date() + pickTotalVO.getOrder_no());
		int orderCnt = pcsMoblieMapper.selectPrintFileDataOrderNo(map);
		if(orderCnt>0){ // 주문번호에 해당하는 파일존재
			if(printType==2){ // 선택출력
				map.put("state", "2"); 
				pcsMoblieMapper.updatePrintFileData(map);
			}else{ 
				return;
			}
		}else{
			LOGGER.info("파일 상태 업데이트 (파일생성 준비)");
			pcsMoblieMapper.insertPrintFileData(map);
		}
		
		try {
			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);
			
			if (printFileName != null) {
				LOGGER.info("printFileName : " + printFileName);
				LOGGER.info("destFileName : " + destFileName);

				// .jrprint 파일의 내용을 pdf 파일로 변환
				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);
				
				map.put("state", "2"); // 2: 파일생성
				LOGGER.info("파일 상태 업데이트 (파일생성 완료)");
				pcsMoblieMapper.updatePrintFileData(map);
				
				// pdf 파일을 프린터 인쇄 메소드 호출
//				pdfPrint(destFileName); 프린트 분리 2018-09-19
				LOGGER.info("========= printConfirmationOrder() end ==========");
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			map.put("state", "F"); // Fail
			LOGGER.info("파일 상태 업데이트 (파일생성 실패)");
			pcsMoblieMapper.updatePrintFileData(map);
		}

	}

	// 금액 산정 룰
	// 거래명세서 대체 시 - 대체 가격이 높으면 판매단가 기준 / 대체 가격이 낮으면 판매단가 기준
	// 주문확인서 대체 시 - 대체 가격이 높으면 판매단가 기준 / 대체 가격이 낮으면 대체가격 기준
	/**
	 * 리포터 로우 생성하기 (기본, 결품, 부분결품, 상품+대체, 부분결품+대체)
	 * 
	 * @param row
	 *            순번
	 * @param pickTotalVO
	 *            데이터
	 * @param isSpecification
	 *            거래명세서 여부
	 * @return
	 */
	private ArrayList<ReportItemVO> createReportRow(String row, PickTotalVO totalVO, boolean isSpecification) {
		ArrayList<ReportItemVO> rowVO = new ArrayList<ReportItemVO>();
		ComCodeVO comCodeVO = new ComCodeVO();
		comCodeVO.setCom_group_code("ZONE_CODE");
		comCodeVO.setCom_sub_code("1_"); // Da = 식자재/상온, Db = 식자재/냉장, Dc = 식자재/냉동, Dd = 식자재/대용량, 
		List<ComCodeVO> comCodeVOs = pcsMapper.selectComCodeList(comCodeVO);

		// 정상
		if (totalVO.getOrder_qty() == totalVO.getPick_qty() && totalVO.getChange_pick_qty() == 0 && totalVO.getSold_out_reason()==null) {
			ReportItemVO itemVO = new ReportItemVO();			
			itemVO.setLck_knd(ReportItemVO.LCK_KND_NORMAL);
			if(isSpecification){
				itemVO.setRow(row); // 순번
			}else{
				for(int i=0; i<comCodeVOs.size(); i++){
					if (totalVO.getZone_code().equals(comCodeVOs.get(i).getCom_code())) {
						itemVO.setRow(row + "/식"); // 순번/식
						break;
					} else {
						itemVO.setRow(row); // 순번
					}
				}
			}

			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명
			
			itemVO.setPromotion_name(totalVO.getPromotion_name()); // 프로모션명
			
			itemVO.setGoods_name(createReportGoodsName(totalVO));// 상품명

			itemVO.setOrder_cost(totalVO.getOrder_cost()); // 판매단가
			itemVO.setN_coupon_cost(totalVO.getN_coupon_cost()); // N쿠폰 단가

			itemVO.setOrder_qty(totalVO.getOrder_qty()); // 주문수량

			itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getOrder_qty()); // 금액
			itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getOrder_qty()); // N쿠폰 제외 금액

			// 결품 X
			// 대체 X

			itemVO.setPick_qty(totalVO.getPick_qty()); // 배송

			itemVO.setGoods_code(totalVO.getGoods_code()); // 상품코드

			rowVO.add(itemVO);

			return rowVO;
		}

		// 결품 (전체결품)
		if (totalVO.getSold_out_reason() != null && totalVO.getSold_out_reason() != "07" && totalVO.getPick_qty() == 0 && totalVO.getChange_pick_qty() == 0) {
			ReportItemVO itemVO = new ReportItemVO();
			itemVO.setLck_knd(ReportItemVO.LCK_KND_LACK);
			if(isSpecification){
				itemVO.setRow(row + "\n" + "결품"); // 순번
			}else{
				for(int i=0; i<comCodeVOs.size(); i++){
					if (totalVO.getZone_code().equals(comCodeVOs.get(i).getCom_code())) {
						itemVO.setRow(row + "/식" + "\n" + "결품"); // 순번/식
						break;
					} else {
						itemVO.setRow(row + "\n" + "결품"); // 순번
					}
				}
			}
			
			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명
			
			itemVO.setPromotion_name(totalVO.getPromotion_name()); // 프로모션명
			
			itemVO.setGoods_name(createReportGoodsName(totalVO));// 상품명

			itemVO.setOrder_cost(totalVO.getOrder_cost()); // 판매단가
			itemVO.setN_coupon_cost(totalVO.getN_coupon_cost()); // N쿠폰 단가

			itemVO.setOrder_qty(totalVO.getOrder_qty()); // 주문수량

			itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getOrder_qty()); // 금액
			itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getOrder_qty()); // N쿠폰 제외 금액

			itemVO.setSold_out_qty(totalVO.getOrder_qty()); // 결품수량

			// 대체 X

			itemVO.setPick_qty(0); // 배송

			// M20170227 osj 전체결품에서도 상품코드 노출
			itemVO.setGoods_code(totalVO.getGoods_code()); // 상품코드

			rowVO.add(itemVO);

			return rowVO;
		}

		// 부분결품 (대체 X)
		if (totalVO.getSold_out_reason() != null && totalVO.getSold_out_reason() != "07" && totalVO.getPick_qty() > 0 && totalVO.getChange_pick_qty() == 0) {
			ReportItemVO itemVO = new ReportItemVO();
			itemVO.setLck_knd(ReportItemVO.LCK_KND_PARK_LACK);
			if(isSpecification){
				itemVO.setRow(row + "\n" + "부분결품"); // 순번
			}else{
				for(int i=0; i<comCodeVOs.size(); i++){
					if (totalVO.getZone_code().equals(comCodeVOs.get(i).getCom_code())) {
						itemVO.setRow(row + "/식" + "\n" + "부분결품"); // 순번/식
						break;
					} else {
						itemVO.setRow(row + "\n" + "부분결품"); // 순번
					}
				}
			}
			
			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명
			
			itemVO.setPromotion_name(totalVO.getPromotion_name()); // 프로모션명

			itemVO.setGoods_name(createReportGoodsName(totalVO));// 상품명

			itemVO.setOrder_cost(totalVO.getOrder_cost()); // 판매단가
			itemVO.setN_coupon_cost(totalVO.getN_coupon_cost()); // N쿠폰 단가

			itemVO.setOrder_qty(totalVO.getOrder_qty()); // 주문수량

			itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getOrder_qty()); // 금액
			itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getOrder_qty()); // N쿠폰 제외 금액

			itemVO.setSold_out_qty(totalVO.getOrder_qty() - totalVO.getPick_qty()); // 결품수량

			// 대체 X

			itemVO.setPick_qty(totalVO.getPick_qty()); // 배송

			itemVO.setGoods_code(totalVO.getGoods_code()); // 상품코드

			rowVO.add(itemVO);

			return rowVO;
		}

		/** 두줄짜리 */
		// 전체 대체
		if (totalVO.getSold_out_reason() != null && totalVO.getSold_out_reason() != "07" && totalVO.getChange_pick_qty() > 0 && totalVO.getOrder_qty()==totalVO.getPick_qty()) { 
			/** 상품 */
			ReportItemVO itemVO = new ReportItemVO();
			// M20170315 osj 대체원거래번호를 색깔표시
			itemVO.setLck_knd(ReportItemVO.LCK_KND_REPLACE); // 전체색칠				
			
			if(isSpecification){
				itemVO.setRow(row); // 순번
			}else{
				for(int i=0; i<comCodeVOs.size(); i++){
					if (totalVO.getZone_code().equals(comCodeVOs.get(i).getCom_code())) {
						itemVO.setRow(row + "/식"); // 순번/식
						break;
					} else {
						itemVO.setRow(row); // 순번
					}
				}
			}
			
			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명
			
			itemVO.setPromotion_name(totalVO.getPromotion_name()); // 프로모션명

			itemVO.setGoods_name(createReportGoodsName(totalVO));// 상품명

			itemVO.setOrder_cost(totalVO.getOrder_cost()); // 판매단가
			itemVO.setN_coupon_cost(totalVO.getN_coupon_cost()); // N쿠폰 단가

			itemVO.setOrder_qty(totalVO.getOrder_qty()); // 주문수량

			itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getOrder_qty()); // 금액
			itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getOrder_qty()); // N쿠폰 제외 금액

			itemVO.setSold_out_qty(totalVO.getChange_pick_qty()); // 결품

			// 대체 X

			itemVO.setPick_qty(totalVO.getOrder_qty() - totalVO.getChange_pick_qty()); // 배송
			
			itemVO.setGoods_code(totalVO.getGoods_code()); // 상품코드
			
			rowVO.add(itemVO);

			/** 대체 */
			itemVO = new ReportItemVO();
			itemVO.setLck_knd(ReportItemVO.LCK_KND_NORMAL);
			String rowStr = "대체 ";
			
			if (totalVO.getChange_goods_cost() > totalVO.getOrder_cost()) {
				rowStr = "대체 ↑";
				//* M20170316 osj 대체↓ 차액 합계와 대체↑ 혜택 합계 수정
				itemVO.setUnder_line(ReportItemVO.UNDERLINE); // 밑줄긋기
				// 대체↑ 혜택 합계
				int pls_sum = (totalVO.getChange_goods_cost() - totalVO.getOrder_cost()) * totalVO.getChange_pick_qty();
				itemVO.setPls_sum(pls_sum);
			} else if (totalVO.getChange_goods_cost() < totalVO.getOrder_cost()){
				rowStr = "대체 ↓";
				// 대체↓ 차액 합계
				int min_sum = (totalVO.getOrder_cost() - totalVO.getChange_goods_cost()) * totalVO.getChange_pick_qty();
				itemVO.setMin_sum(min_sum);				
			}

			itemVO.setRow(rowStr); // 순번
			
			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명
			
			itemVO.setPromotion_name(totalVO.getPromotion_name()); // 프로모션명

			itemVO.setGoods_name(totalVO.getChange_goods_name());// 상품명

			itemVO.setOrder_cost(totalVO.getChange_goods_cost()); // 판매단가

			// 주문수량 X

			// 금액
			if (totalVO.getChange_goods_cost() > totalVO.getOrder_cost()) {
				itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getChange_pick_qty());
				itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getChange_pick_qty()); // N쿠폰 제외 금액
			} else {
				if (isSpecification) {
					// 거래명세서
					itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getChange_pick_qty());
					itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getChange_pick_qty()); // N쿠폰 제외 금액
				} else {
					// 주문확인서
					itemVO.setOrder_cost_total(totalVO.getChange_goods_cost() * totalVO.getChange_pick_qty());
				}
			}

			// 결품 X

			itemVO.setChange_pick_qty(totalVO.getChange_pick_qty()); // 대체

			itemVO.setPick_qty(totalVO.getChange_pick_qty()); // 배송

			itemVO.setGoods_code(totalVO.getChange_goods_code()); // 상품코드

			rowVO.add(itemVO);

			return rowVO;
		}

		// 부분대체
		if (totalVO.getSold_out_reason() != null && totalVO.getSold_out_reason() != "07" && totalVO.getChange_pick_qty() > 0 && totalVO.getOrder_qty() > totalVO.getPick_qty()) {
			
			ReportItemVO itemVO = new ReportItemVO();
			itemVO.setLck_knd(ReportItemVO.LCK_KND_REPLACE_ONLY); // 순번만 색칠
			if(isSpecification){
				itemVO.setRow(row); // 순번
			}else{
				for(int i=0; i<comCodeVOs.size(); i++){
					if (totalVO.getZone_code().equals(comCodeVOs.get(i).getCom_code())) {
						itemVO.setRow(row + "/식"); // 순번/식
						break;
					} else {
						itemVO.setRow(row); // 순번
					}
				}
			}
			
			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명
			
			itemVO.setPromotion_name(totalVO.getPromotion_name()); // 프로모션명

			itemVO.setGoods_name(createReportGoodsName(totalVO));// 상품명

			itemVO.setOrder_cost(totalVO.getOrder_cost()); // 판매단가
			itemVO.setN_coupon_cost(totalVO.getN_coupon_cost()); // N쿠폰 단가

			itemVO.setOrder_qty(totalVO.getOrder_qty()); // 주문수량

			itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getOrder_qty()); // 금액
			itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getOrder_qty()); // N쿠폰 제외 금액

			itemVO.setSold_out_qty(totalVO.getOrder_qty() - totalVO.getPick_qty() + totalVO.getChange_pick_qty()); // 결품

			// 대체 X

			itemVO.setPick_qty(totalVO.getPick_qty() - totalVO.getChange_pick_qty()); // 배송
			
			itemVO.setGoods_code(totalVO.getGoods_code()); // 상품코드				

			rowVO.add(itemVO);

			/** 대체 */
			itemVO = new ReportItemVO();
			// M20170224 osj 대체인경우에는 번호에 색깔 안 칠함
//			itemVO.setLck_knd(ReportItemVO.LCK_KND_REPLACE);
			itemVO.setLck_knd(ReportItemVO.LCK_KND_NORMAL);
			String rowStr = "부분대체";
			
			if (totalVO.getChange_goods_cost() > totalVO.getOrder_cost()) {
				// M20170224 osj 대체를 부분대체로 변경
				rowStr = "부분대체 ↑";
				
				//* M20170316 osj 대체↓ 차액 합계와 대체↑ 혜택 합계 수정
				itemVO.setUnder_line(ReportItemVO.UNDERLINE); // 밑줄긋기

				// 대체↑ 혜택 합계
				int pls_sum = (totalVO.getChange_goods_cost() - totalVO.getOrder_cost()) * totalVO.getChange_pick_qty();
				itemVO.setPls_sum(pls_sum);
				
			} else if (totalVO.getChange_goods_cost() < totalVO.getOrder_cost()){
				// M20170224 osj 대체를 부분대체로 변경
				rowStr = "부분대체 ↓";

				// 대체↓ 차액 합계
				int min_sum = (totalVO.getOrder_cost() - totalVO.getChange_goods_cost()) * totalVO.getChange_pick_qty();
				itemVO.setMin_sum(min_sum);
			}

			itemVO.setRow(rowStr); // 순번
			
			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명

			itemVO.setGoods_name(totalVO.getChange_goods_name());// 상품명

			itemVO.setOrder_cost(totalVO.getChange_goods_cost()); // 판매단가

			// 주문수량 X

			// 금액
			if (totalVO.getChange_goods_cost() > totalVO.getOrder_cost()) {
				itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getChange_pick_qty());
				itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getChange_pick_qty()); // N쿠폰 제외 금액
			} else {
				if (isSpecification) {
					// 거래명세서
					itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getChange_pick_qty());
					itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getChange_pick_qty()); // N쿠폰 제외 금액
				} else {
					// 주문확인서
					itemVO.setOrder_cost_total(totalVO.getChange_goods_cost() * totalVO.getChange_pick_qty());
				}
			}

			// 결품 X

			itemVO.setChange_pick_qty(totalVO.getChange_pick_qty()); // 대체

			itemVO.setPick_qty(totalVO.getChange_pick_qty()); // 배송

			itemVO.setGoods_code(totalVO.getChange_goods_code()); // 상품코드

			rowVO.add(itemVO);

			return rowVO;
		}
		
		// 상품대체요청 (결품사유 '07')
		if (totalVO.getSold_out_reason() != null && totalVO.getSold_out_reason() == "07" && totalVO.getChange_pick_qty() > 0) {
			ReportItemVO itemVO = new ReportItemVO();
			// M20170315 osj 대체원거래번호를 색깔표시
			itemVO.setLck_knd(ReportItemVO.LCK_KND_REPLACE); // 전체색칠				
			
			if(isSpecification){
				itemVO.setRow(row); // 순번
			}else{
				for(int i=0; i<comCodeVOs.size(); i++){
					if (totalVO.getZone_code().equals(comCodeVOs.get(i).getCom_code())) {
						itemVO.setRow(row + "/식"); // 순번/식
						break;
					} else {
						itemVO.setRow(row); // 순번
					}
				}
			}
			
			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명
			
			itemVO.setPromotion_name(totalVO.getPromotion_name()); // 프로모션명

			itemVO.setGoods_name(createReportGoodsName(totalVO));// 상품명

			itemVO.setOrder_cost(totalVO.getOrder_cost()); // 판매단가
			itemVO.setN_coupon_cost(totalVO.getN_coupon_cost()); // N쿠폰 단가

			itemVO.setOrder_qty(totalVO.getOrder_qty()); // 주문수량

			itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getOrder_qty()); // 금액
			itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getOrder_qty()); // N쿠폰 제외 금액

			itemVO.setSold_out_qty(totalVO.getChange_pick_qty()); // 결품

			// 대체 X

			itemVO.setPick_qty(totalVO.getOrder_qty() - totalVO.getChange_pick_qty()); // 배송
			
			itemVO.setGoods_code(totalVO.getGoods_code()); // 상품코드
			
			rowVO.add(itemVO);

			/** 대체 */
			itemVO = new ReportItemVO();
			itemVO.setLck_knd(ReportItemVO.LCK_KND_NORMAL);
			String rowStr = "대체 ";
			
			if (totalVO.getChange_goods_cost() > totalVO.getOrder_cost()) {
				rowStr = "대체 ↑";
				//* M20170316 osj 대체↓ 차액 합계와 대체↑ 혜택 합계 수정
				itemVO.setUnder_line(ReportItemVO.UNDERLINE); // 밑줄긋기
				// 대체↑ 혜택 합계
				int pls_sum = (totalVO.getChange_goods_cost() - totalVO.getOrder_cost()) * totalVO.getChange_pick_qty();
				itemVO.setPls_sum(pls_sum);
			} else if (totalVO.getChange_goods_cost() < totalVO.getOrder_cost()){
				rowStr = "대체 ↓";
				// 대체↓ 차액 합계
				int min_sum = (totalVO.getOrder_cost() - totalVO.getChange_goods_cost()) * totalVO.getChange_pick_qty();
				itemVO.setMin_sum(min_sum);				
			}

			itemVO.setRow(rowStr); // 순번
			
			itemVO.setZone_code(totalVO.getZone_code() + "\n" + totalVO.getPromotion_name()); // 존코드 + \n 프로모션명
			
			itemVO.setPromotion_name(totalVO.getPromotion_name()); // 프로모션명

			itemVO.setGoods_name(totalVO.getChange_goods_name());// 상품명

			itemVO.setOrder_cost(totalVO.getChange_goods_cost()); // 판매단가

			// 주문수량 X

			// 금액
			if (totalVO.getChange_goods_cost() > totalVO.getOrder_cost()) {
				itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getChange_pick_qty());
				itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getChange_pick_qty()); // N쿠폰 제외 금액
			} else {
				if (isSpecification) {
					// 거래명세서
					itemVO.setOrder_cost_total(totalVO.getOrder_cost() * totalVO.getChange_pick_qty());
					itemVO.setN_coupon_cost_total((totalVO.getOrder_cost() - totalVO.getN_coupon_cost()) * totalVO.getChange_pick_qty()); // N쿠폰 제외 금액
				} else {
					// 주문확인서
					itemVO.setOrder_cost_total(totalVO.getChange_goods_cost() * totalVO.getChange_pick_qty());
				}
			}

			// 결품 X

			itemVO.setChange_pick_qty(totalVO.getChange_pick_qty()); // 대체

			itemVO.setPick_qty(totalVO.getChange_pick_qty()); // 배송

			itemVO.setGoods_code(totalVO.getChange_goods_code()); // 상품코드

			rowVO.add(itemVO);

			return rowVO;
		}

		return null;
	}

	/**
	 * 리포트용 상품명 생성 - 과세구분 상품명 + (옵션/규격/제조원)
	 * 
	 * @param vo
	 * @return
	 */
	private String createReportGoodsName(PickTotalVO vo) {
		String result = ""; //

		if (vo.getTax_type() != null) {
			result += vo.getTax_type() + " ";
		} // 과세구분 코드

		if (vo.getGoods_name() != null) {
			result += vo.getGoods_name() + "\n";
		} else {
			result += "\n";
		} // 상품명
		
		result += "(";

		if (vo.getGoods_option_name() != null) {
			result += vo.getGoods_option_name() + "/";
		} else {
			result += "-/";
		} // 옵션(단품명)

		if (vo.getGoods_spec() != null) {
			result += vo.getGoods_spec() + "/";
		} else {
			result += "-/";
		} // 상품규격

		if (vo.getMaker_name() != null) {
			result += vo.getMaker_name();
		} else {
			result += "-";
		} // 제조원

		result += ")";

		return result;
	}

	/**
	 * PDF 파일 프린트  
	 * 
	 * @param pdfFileName
	 * @throws Exception
	 */
	
	public synchronized void pdfPrint(Map<String, Object> map) throws Exception {
		
		LOGGER.info("***** PcsMoblieServiceImpl pdfPrint START *****");
		
		map.put("state", "3"); // 3: 프린트 준비

		LOGGER.info("파일 상태 업데이트 (프린트 준비)");
		pcsMoblieMapper.updatePrintFileData(map);
		
		// 프린터 이름 가져오기
		String printerName = ""; // "Canon MF210 Series"
		ComCodeVO comCodeVO = new ComCodeVO();
		comCodeVO.setCom_group_code("PRINTER_NAME");
		comCodeVO.setCom_code("0");
		List<ComCodeVO> selectComCodeList = null;
		
		// 공통코드 불러오기
		try {
			selectComCodeList = pcsMapper.selectComCodeList(comCodeVO);
		} catch (Exception e1) {
			LOGGER.error(e1.toString());
		}

		// 공통코드가 NULL일 경우
		if (selectComCodeList == null) {
			// throw new Exception("프린터명 사용 안함");
			LOGGER.info("공통코드 null");
			return;
		}

		// 공통코드가 NULL이 아니거나 사이즈가 0보다 클 경우
		if (selectComCodeList != null && selectComCodeList.size() > 0) {
			ComCodeVO codeVO = selectComCodeList.get(0);
			printerName = codeVO.getCom_name();

			if (!codeVO.getUse_yn().equalsIgnoreCase("Y")) {
				// throw new Exception("프린터 사용 안함");
				LOGGER.info("프린터 사용안함");
				return;
			}
		}

		// 프린터 서비스 찾기
		PrintService myPrintService = null;
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService printService : printServices) {
			if (printService.getName().trim().equals(printerName)) {
				try{
					myPrintService = printService;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		LOGGER.info("프린터 서비스 : "+myPrintService);

		// 프린터 서비스가 NULL일 경우
		if (myPrintService == null) {
			throw new Exception("프린터 서비스가 잡히지 않습니다.");
		}else{
			String pdfFileName = (String) map.get("file_name");
			// PDF 파일 출력
			LOGGER.info("pdf파일 출력 Start*****"+pdfFileName);
			File inputFile = new File(pdfFileName);
			PDDocument document = null;
//			PDDocument document = PDDocument.load(new File(pdfFileName));
			LOGGER.info("파일준비");
			try {
				document = PDDocument.load(inputFile);
				LOGGER.info("1/6 파일로드");
				PrinterJob job = PrinterJob.getPrinterJob();
				LOGGER.info("2/6 프린터 JOB");
				job.setPageable(new PDFPageable(document));
				LOGGER.info("3/6 페이지세팅");
				job.setPrintService(myPrintService);
				LOGGER.info("4/6 프린터 서비스");
				job.print();
				LOGGER.info("5/6 프린트");
				document.close();
				LOGGER.info("6/6 CLOSE");
				
				map.put("state", "4"); // 4: 프린트 완료
				LOGGER.info("파일 상태 업데이트 (프린트 완료)");
				pcsMoblieMapper.updatePrintFileData(map);
				
				LOGGER.info("pdf파일 출력 End*****"+pdfFileName);
			} catch (PrinterException pe) {
				map.put("state", "E"); // 에러
				LOGGER.info("파일 상태 업데이트 (프린트 에러)");
				pcsMoblieMapper.updatePrintFileData(map);
				pe.printStackTrace();
				LOGGER.error("print error : " + pe.toString());
			} catch(IOException io){
				map.put("state", "E"); // 에러
				LOGGER.info("파일 상태 업데이트 (PDF로딩 실패)");
				pcsMoblieMapper.updatePrintFileData(map);
				io.printStackTrace();
			}catch(Exception e){
				map.put("state", "E"); // 에러
				LOGGER.info("파일 상태 업데이트 (프린터 Exception)");
				pcsMoblieMapper.updatePrintFileData(map);
				e.printStackTrace();
			} finally{
				if(document != null){
					try{
						document.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		LOGGER.info("***** PcsMoblieServiceImpl pdfPrint END *****");		
		
		return;

	}
	

	/**
	 * 로그인
	 * 
	 * @param loginVO
	 * @return
	 */
	@Override
	public DefaultJsonVO login(LoginVO loginVO) {

		// 사용자가 로그인 되어 있는지 체크
		ComCodeVO comCodeVO = new ComCodeVO();
		comCodeVO.setCom_group_code("WORKER_ID");

		List<ComCodeVO> comCodeVOs = pcsMapper.selectComCodeList(comCodeVO);
		for (ComCodeVO vo : comCodeVOs) {
			if ("Y".equals(vo.getCom_sub_code()) && vo.getCom_code().equals(loginVO.getWorkerId())) {
				if (vo.getCom_desc() != null && !loginVO.getDeviceId().equals(vo.getCom_desc())) {
					return new DefaultJsonVO(false, " 다른 단말기에서 이미 사용중인 작업자 입니다.");
				}
			}
		}

		// Transaction 처리용
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);

		int check = 0;

		try {
			// 같은 단말기에 다른 사용자가 등록한 경우 -> 기존 등록되어 있던 디바이스 아이디 초기화후 로그인 처리
			for (ComCodeVO vo : comCodeVOs) {
				if (vo.getCom_desc() != null && vo.getCom_desc().equals(loginVO.getDeviceId())) {
					vo.setCom_sub_code("N");
					vo.setCom_desc("");
					pcsMoblieMapper.updateComCode(vo);
				}
			}

			// 로그인 처리
			ComCodeVO loginComCode = new ComCodeVO();
			loginComCode.setCom_group_code("WORKER_ID");
			loginComCode.setCom_code(loginVO.getWorkerId());
			loginComCode.setCom_sub_code("Y");
			loginComCode.setCom_desc(loginVO.getDeviceId());

			check = pcsMoblieMapper.updateComCode(loginComCode);

		} catch (Exception e) {
			txManager.rollback(status);
			LOGGER.error(e.toString());
			return new DefaultJsonVO(false, e.toString());
		}
		txManager.commit(status);

		if (check > 0) {
			return new DefaultJsonVO(true, null);
		} else {
			LOGGER.error("로그인에 실패하였습니다. check = " +check);
			return new DefaultJsonVO(false, "로그인에 실패하였습니다.");
		}
	}

	/**
	 * 로그아웃
	 * 
	 * @param loginVO
	 * @return
	 */
	@Override
	public DefaultJsonVO logout(LoginVO loginVO) {
		ComCodeVO loginComCode = new ComCodeVO();
		loginComCode.setCom_group_code("WORKER_ID");
		loginComCode.setCom_code(loginVO.getWorkerId());
		loginComCode.setCom_sub_code("N");
		loginComCode.setCom_desc("");

		int check = pcsMoblieMapper.updateComCode(loginComCode);
		if (check > 0) {
			return new DefaultJsonVO(true, null);
		} else {
			LOGGER.error("로그아웃에 실패하였습니다. check = " +check);
			return new DefaultJsonVO(false, "로그아웃에 실패하였습니다.");
		}
	}

	@Override
	public List<ComCodeVO> selectComGroupCodeList(ArrayList<String> comGroupCodeList) {
		return pcsMoblieMapper.selectComGroupCodeList(comGroupCodeList);
	}

	@Override
	public int updateComCode(ComCodeVO comCodeVO) {
		return pcsMoblieMapper.updateComCode(comCodeVO);
	}
	
	@Override
	public ArrayList<String> printPickIds() {
		return pcsMoblieMapper.printPickIds();
	}
	
	@Override
	public Map<String, Object> selectPrintFileData() {
		return pcsMoblieMapper.selectPrintFileData();
	}
	@Override
	public int selectPrintFileInProgress() {
		return pcsMoblieMapper.selectPrintFileInProgress();
	}
	@Override
	public int deletePrintFileData(String today) {
		return pcsMoblieMapper.deletePrintFileData(today);
	}
	@Override
	public int updatePrintFileFailData() {
		return pcsMoblieMapper.updatePrintFileFailData();
	}
	@Override
	public int alterPrintFileIdReset() {
		return pcsMoblieMapper.alterPrintFileIdReset();
	}

	@Override
	public int updatePrintFileError() {
		return pcsMoblieMapper.updatePrintFileError();
	}
	
}
