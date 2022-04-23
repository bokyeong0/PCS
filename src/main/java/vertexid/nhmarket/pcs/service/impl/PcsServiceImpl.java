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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import vertexid.nhmarket.pcs.cmmn.FileUtil;
import vertexid.nhmarket.pcs.cmmn.SocketUtil;
import vertexid.nhmarket.pcs.cmmn.StringUtil;
import vertexid.nhmarket.pcs.service.ComCodeVO;
import vertexid.nhmarket.pcs.service.DefaultVO;
import vertexid.nhmarket.pcs.service.OrderVO;
import vertexid.nhmarket.pcs.service.PcsService;
import vertexid.nhmarket.pcs.service.PickDetailVO;
import vertexid.nhmarket.pcs.service.PickHeaderVO;
import vertexid.nhmarket.pcs.service.ReportPrePckVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : EgovSampleServiceImpl.java
 * @Description : Sample Business Implement Class
 * @Modification Information
 * @ @ 수정일 수정자 수정내용 @ --------- --------- ------------------------------- @
 *   2009.03.16 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 * 		Copyright (C) by MOPAS All right reserved.
 */

@Service("pcsService")
@Transactional
public class PcsServiceImpl extends EgovAbstractServiceImpl implements PcsService {

	// Logger 생성용
	private static final Logger LOGGER = LoggerFactory.getLogger(PcsServiceImpl.class);

	@Resource(name = "pcsMapper")
	private PcsMapper pcsDAO;
	
	@Resource(name = "pcsMobileMapper")
	private PcsMoblieMapper pcsMoblieDAO;

	/** ID Generation */
	@Resource(name = "egovIdGnrService")
	private EgovIdGnrService egovIdGnrService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	// Transaction 처리용 Manager
	@Resource(name = "txManager")
	PlatformTransactionManager txManager;

	/**
	 * 오더 목록을 조회 한다.
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 searchVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public List<?> selectOrderList(DefaultVO searchVO) throws Exception {
		return pcsDAO.selectOrderList(searchVO);
	}
	@Override
	public EgovMap orderCnt(DefaultVO searchVO) throws Exception {
		return pcsDAO.orderCnt(searchVO);
	}
	@Override
	public String orderItemCnt(DefaultVO searchVO) throws Exception {
		return pcsDAO.orderItemCnt(searchVO);
	}

	/**
	 * 주문 업로드(엑셀파일)
	 * 
	 * @param multipartRequest
	 *            - 업로드할 Excel파일
	 * @param searchVo
	 *            - 등록할 정보가 담긴 searchVo
	 * @return 등록 결과
	 * @exception Exception
	 */
	@Override

	public int insertImportData(MultipartHttpServletRequest multipartRequest, DefaultVO searchVo) throws Exception {
		// Transaction 처리용
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		// def.setName(new
		// Object(){}.getClass().getEnclosingMethod().toString());
		TransactionStatus status = txManager.getTransaction(def);

		int check             = 0;
		String uploadPath     = "/excelUpload/";
		String uploadFileName = "";
		String fileName       = "";

		//////////////////////////////////////////////////////////////
		// 테스트서버 linux , 운영서버 window OS 여부에 따라 경로 설정 처리
		// <entry key="uploadPathWin" value="/excelUpload/"/>
		// <entry key="uploadPathLinux" value="/var/lib/tomcat/upload/"/>

		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("window") >= 0) {
			uploadPath = propertiesService.getString("uploadPathWin");
		} else {
			uploadPath = propertiesService.getString("uploadPathLinux");
		}
		//////////////////////////////////////////////////////////////

		// 파일 처리
		File dir = new File(uploadPath);
		// 디렉토리 체크 생성
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		// 여러개 파일일경우 처리 ??
		Iterator<String> iter = multipartRequest.getFileNames();
		
		try {
			// 기존 예정 데이터 삭제
			pcsDAO.deleteImportData(searchVo);

			while (iter.hasNext()) {
			 	uploadFileName       = iter.next();
				MultipartFile  mFile = multipartRequest.getFile(uploadFileName);
				fileName             = mFile.getOriginalFilename();
				File            file = new File(uploadPath + fileName);

				if (fileName != null && !fileName.equals("")) {

					mFile.transferTo(file);

					List<OrderVO> orderVOs;
					
					/** 엑셀파일 Read */
					orderVOs = readExcelXlsx(file, searchVo);
					
					if(orderVOs.size() == 0){
						throw new EgovBizException("엑셀 파일 내용이 형식에 맞지 않습니다. 엑셀 파일을 열어 내용을 확인 하시기 바랍니다.");
					}else{
						// A20170725 osj 엑셀 데이터 입력 전 동일한 원거래번호 존재 여부 체크
						int chk = 0;
						for(OrderVO chkVO : orderVOs){						
							LOGGER.info(chkVO.getDelivery_count());
							
							// 이전의 주문 정보 존재 여부 체크 후 카운트
							chk = pcsDAO.checkOrderInfo(chkVO);	

							// 카운트 후 카운트 값 0 이상일때 작업 지속
							if(chk > 0){
								continue;
							}
							
							check = check + pcsDAO.insertImportData(chkVO);
							
						}
						
					}
				}

				if (file != null && file.exists()) {
					file.delete();
				}

			}
		} catch (Exception e) {
			txManager.rollback(status);
			// M20170306 osj 적합하지 않은 오류 메시지로서 삭제
//			if(check == 0){
//				LOGGER.error(e.getMessage());
//				throw new Exception("피킹 예정 상태의 동일한 데이터가 존재 합니다.");	
//			}else{
				LOGGER.error(e.getMessage());
				e.printStackTrace();
				throw new Exception(e.getMessage());
//			}
		}
		txManager.commit(status);
		return check;
	}

	/**
	 * 피킹지시
	 * 
	 * @param vo
	 *            - 피킹할 데이터 조회 searchVO
	 * @return 등록결과(0)
	 * @throws Exception
	 */
	@Override
	public int insertPicking(DefaultVO searchVO) throws Exception {

		// Transaction 처리용
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		// def.setName(new
		// Object(){}.getClass().getEnclosingMethod().toString());
		TransactionStatus status = txManager.getTransaction(def);

		// 예정 인 오더정보 피킹 처리
		searchVO.setSearch_state_code("0");
		// 1차 피킹 조회
		// List<EgovMap> selectOrderList = (List<EgovMap>)
		// pcsDAO.selectOrderList(searchVO);
		// 2차 피킹 조회
		List<EgovMap> selectOrder2ndList = (List<EgovMap>) pcsDAO.selectOrder2ndList(searchVO);

		int result = 0;
		int listCount = 0;
		EgovMap dataMap = new EgovMap();
		try {

			// 피킹로직후 Map List로 반환
			// dataMap = picking(selectOrderList);

			// 2차 피킹 로직 처리
			dataMap = picking2nd(selectOrder2ndList);

			// 피킹현황 조회 분기 로직
			
				// 피킹 헤더 데이터 등록 처리
				List<?> pickHeaderList = (List<?>) dataMap.get("pickHeader");
				listCount = pickHeaderList.size();
				for (int i = 0; i < listCount; i++) {
					PickHeaderVO pickHeaderVO = (PickHeaderVO) pickHeaderList.get(i);
					pcsDAO.insertPickHeder(pickHeaderVO);
				}
	
				// 피킹 디테일 데이터 등록 처리
				List<?> pickDetailList = (List<?>) dataMap.get("pickDetail");
				listCount = pickDetailList.size();
				for (int i = 0; i < listCount; i++) {
					PickDetailVO pickDetailVO = (PickDetailVO) pickDetailList.get(i);
					pcsDAO.insertPickDetail(pickDetailVO);
				}

			// 진행 상태로 수정 업데이트
			result = pcsDAO.updateStateCode(searchVO);
		} catch (Exception e) {
			txManager.rollback(status);
			e.printStackTrace();
			LOGGER.error(e.toString());
		}
		txManager.commit(status);
		return result;
	}

	/**
	 * 라벨출력 처리 (배차일자 , 배송차수)
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 searchVO (배송차수, 배송일자)
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public int updateLabelPrint(DefaultVO searchVO) throws Exception {

		int result = 0;
		ArrayList<EgovMap> printingList = new ArrayList<EgovMap>();
		
		// 대용량 여부 확인
		ComCodeVO comCodeVo = new ComCodeVO();
		comCodeVo.setCom_group_code("ZONE_CODE");
		comCodeVo.setCom_sub_code("_1");
		List<ComCodeVO> bigZoneList = pcsDAO.selectComCodeList(comCodeVo);
		
		// 주문별 라벨출력 (배송구명)
		if(searchVO.getLabelPrintType().equals("order")){
			printingList = (ArrayList<EgovMap>) pcsDAO.selectLablePrintingListOrder(searchVO);
			ArrayList<PickHeaderVO> pickHeaderVOs = new ArrayList<PickHeaderVO>();
			
			for(int i=0; i<printingList.size(); i++){
				EgovMap map = new EgovMap();
				PickHeaderVO vo = new PickHeaderVO();
				map = printingList.get(i);
				vo.setZone_code((String)map.get("orderDate")+(String)map.get("orderNo"));
				vo.setPick_id((String)map.get("pickId"));
				
				pickHeaderVOs.add(vo);
			}
			// 라벨 셋팅 처리
			LabelChecker checker = new LabelChecker();
			checker.setZoneCodeList(pickHeaderVOs);
			for (PickHeaderVO pvo : pickHeaderVOs) {
				pvo.setLabel_state(checker.getLabelStr(pvo.getZone_code()));
//				System.out.println("주문별 라벨출력 > 라벨상태 변경--->"+pvo.getPick_id()+" // "+pvo.getZone_code());
				pcsDAO.updateLabelStateDelivery(pvo);
			}
			
			printingList = (ArrayList<EgovMap>) pcsDAO.selectLablePrintingListOrder(searchVO); // 라벨상태 업데이트 후 다시 조회
		}else{
			printingList = (ArrayList<EgovMap>) pcsDAO.selectLablePrintingList(searchVO);
		}

		ArrayList<EgovMap> printingList_Big = new ArrayList<EgovMap>();
		for (EgovMap egovMap : printingList) {
			boolean isBigZone = false;
			for(ComCodeVO data : bigZoneList){
				if(data.getCom_code().equals((String)egovMap.get("zoneCode"))){
					isBigZone = true;
				}
			}
			
			if(isBigZone){ //대용량이면 수량만큼 라벨출력
				for(int i=0; i<((BigDecimal) egovMap.get("orderQty")).intValue(); i++){
					if(i==0){ // 대용량 중에 첫번째 것만 pick Id 출력
						printingList_Big.add(egovMap);
					}else { // 나머지는 pick Id 바코드 제거
						EgovMap map = new EgovMap();
						map.put("labelState", "");
						map.put("shortOrderNo", egovMap.get("shortOrderNo"));
						map.put("trayNo", egovMap.get("trayNo"));
						map.put("pickId", null);
						map.put("orderNo", egovMap.get("orderNo"));
						map.put("deliveryCount", egovMap.get("deliveryCount"));
						map.put("zoneCode", egovMap.get("zoneCode"));
						map.put("deliveryAreaName", egovMap.get("deliveryAreaName"));
						map.put("orderCustomerName", egovMap.get("orderCustomerName"));
						map.put("deliveryDate", egovMap.get("deliveryDate"));
						map.put("trolleyId", egovMap.get("trolleyId"));
						printingList_Big.add(map);
					}
				}
			}else{
				printingList_Big.add(egovMap);
			}
		}
		
		result = printingListData(printingList_Big);
		return result;
	}

	/**
	 * 라벨출력 [PS] 현황조회 선택 라벨 출력 개발(선택라벨 출력)
	 * 
	 * @param vo
	 *            - 출력할 Pick라벨정보
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public int updateChoiceLabelPrint(PickHeaderVO pickHeaderVO) throws Exception {

		int result = 0;

		ArrayList<EgovMap> printingList = (ArrayList<EgovMap>) pcsDAO.selectChoiceLablePrintingList(pickHeaderVO);
		
		result = printingListData(printingList);
		return result;

	}

	/**
	 * 엑셀파일 List로 읽어들이기
	 * 
	 * @param file
	 * @param searchVo
	 * @return
	 * @throws Exception
	 */
	private List<OrderVO> readExcelXlsx(File file, DefaultVO searchVo) throws Exception {
		ArrayList<OrderVO> orderVOs = new ArrayList<>();
		XSSFWorkbook       workbook = null;
		
		
		
		try {
			FileInputStream fis = new FileInputStream(file);
			workbook            = new XSSFWorkbook(fis);

			XSSFSheet     sheet = workbook.getSheetAt(0);
			//workbook = (XSSFWorkbook) decryptExcelFile();
			/*String isWorkbookLock = "";
			InputStream is = new FileInputStream(file);
						a
			if (!is.markSupported()) {
				is = new PushbackInputStream(is, 10);
            }

            if (POIFSFileSystem.hasPOIFSHeader(is)) {
                POIFSFileSystem fs = new POIFSFileSystem(is);
                EncryptionInfo info = new EncryptionInfo(fs);
                Decryptor d = Decryptor.getInstance(info);
                
                if (!d.verifyPassword("1")) {
                    System.out.println("Unable to process: document is encrypted.");
                }
                
                try {
                    d.verifyPassword("1");
                    is = d.getDataStream(fs);
                    workbook = new XSSFWorkbook(OPCPackage.open(is));
                    isWorkbookLock = "true";
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }               
            }           
            if (isWorkbookLock != "true") {
                FileInputStream fileInputStream = new FileInputStream(file);
                workbook = new XSSFWorkbook(fileInputStream);
            }*/
			
			

			/*XSSFSheet     sheet = workbook.getSheetAt(0);*/
			
			
			int            rows = sheet.getPhysicalNumberOfRows();  // 엑셀 파일  sheet 행 정보
			System.out.println("==================rows의 갯수================="+ rows);
			for (int rowindex = 0; rowindex < rows; rowindex++) {
				try {

					if (rowindex == 0) {
						continue;   // header skip
					}
					
					// 행 Read
					XSSFRow row = sheet.getRow(rowindex);
					
					if (row != null) {
						// 셀의 수
						// int cells = row.getPhysicalNumberOfCells();
						// System.out.println("cells : " + cells);
						OrderVO orderVO = new OrderVO();
						int      celNum = 0;
						// 체크 처리
						// 2016.09.05 오늘날자체크 처리
						SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
						String       currnetDate = dateFmt.format(new Date());
						////////////////////////////
						// 체크 로직
						// 당일데이터 체크 처리
						// 로컬 테스트용 (20171127 LJM)
						if (getValueXls(row, 1).length()==0 && getValueXls(row, 8).length()==0) { //빈행 있으면 skip 배송일자, 경통코드 null
							continue; 
						}else if (!currnetDate.equals(getValueXls(row, 1))) {
							throw new EgovBizException("당일 데이터만 업로드 가능 합니다.");
						}
						////////////////////////////
						
						// 엑셀 셀 값 읽기
						orderVO.setShort_order_no    (getValueXls(row, celNum++));                                            // 단축주문번호 A
						orderVO.setDelivery_date     (getValueXls(row, celNum++).toString());                                 // 배송일자 B
						orderVO.setDelivery_count    (String.valueOf((int) Double.parseDouble(getValueXls(row, celNum++))));  // 배송회차 C
						orderVO.setOrder_date        (getValueXls(row, celNum++).toString());                                 // 주문일자
						orderVO.setOrder_no          (getValueXls(row, celNum++).toString());                                 // 주문번호
						//orderVO.setOrder_row_no      (String.valueOf((int) Double.parseDouble(getValueXls(row, celNum++))) ); // 주문순번 (20160929 컬럼 추가)
						orderVO.setOrder_row_no      (getValueXls(row, celNum++));                                            // 차세대 인터페이스 변경으로 인한 0001, 0002 형식의 주문순번 읽어들이기 20180322 ljm
						orderVO.setDelivery_area_name(getValueXls(row, celNum++));                                            // 권역명
						orderVO.setCustomer_name     (getValueXls(row, celNum++));                                            // 주문고객명
						orderVO.setGoods_code        (getValueXls(row, celNum++).trim());                                     // 경제통합상품코드
						orderVO.setSales_goods_code  (getValueXls(row, celNum++));                                            // 상품코드
						orderVO.setGoods_name        (getValueXls(row, celNum++));                                            // 상품명
						orderVO.setOrder_qty         ((int) Double.parseDouble(getValueXls(row, celNum++)));                  // 수량
						orderVO.setChange_allow_yn   (getValueXls(row, celNum++));                                            // 상품대체요청여부
						String str_zoneCode = getValueXls(row, celNum++).trim();
						orderVO.setZone_code         (str_zoneCode);                                            		      // 존정보 (차세대 존코드 공백 제거 20180219)
						//orderVO.setZone_code         (getValueXls(row, celNum++));                                            // 존정보
						
						// M20170306 osj 존정보가 없으면 에러 처리
						if("".equals(orderVO.getZone_code())){
							throw new EgovBizException("존정보가 누락되었습니다.\n엑셀파일 확인 후 다시 시도해 주십시오.");
						}

						//------------------------------------------------------------------------------------------------------------------
						// 20160829 컬럼 추가 (상품금액:order_cost, 제조사명:maker_name, 상품규격:goods_spec)
						
						
						orderVO.setGoods_spec(getValueXls(row, celNum++)); // 상품규격

						if ("".equals(getValueXls(row, celNum))) {
							orderVO.setOrder_cost("0");
						} else {
							orderVO.setOrder_cost(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 상품금액
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setGoods_stock_code(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 재고관리구분
						}
						celNum++;
						
 						orderVO.setMaker_name(getValueXls(row, celNum++)); // 생산자제조사

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setGoods_option_name(getValueXls(row, celNum)); // 단품명
						}
						celNum++;

						if ("00".equals(getValueXls(row, celNum))) {
							orderVO.setTax_type(""); // 과세
						} else if ("01".equals(getValueXls(row, celNum))) {
							orderVO.setTax_type("*"); // 면세
						} else if ("02".equals(getValueXls(row, celNum))) { // 과세구분코드
							orderVO.setTax_type("#"); // 영세
						} 
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setCategory_large(getValueXls(row, celNum)); // 대카테고리명
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setCategory_middle(getValueXls(row, celNum)); // 중카테고리명
						}
						celNum++;

						if ("".equals(getValueXls(row, celNum))) {
							orderVO.setDelivery_amount("0");
						} else {
							orderVO.setDelivery_amount(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 배송비
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setOrder_customer_name(getValueXls(row, celNum)); // 수취고객명
						}
						celNum++;
						
						//------------------------------------------------------------------------------------------------------------------
						
						//------------------------------------------------------------------------------------------------------------------
						// 20160929 컬럼 추가 (카드할인가:card_dc_cost, 배송비:delivery_amount,옵션명:goods_option_name, 과세구분:tax_type, 주문고객명:order_customer_name
						//					, 연락처1:tel_no_1, 연락처2:tel_no_2, 주소:address, 사은품명:free_gift_name, 배송메시지:delivery_message )
						
//						if ("".equals(getValueXls(row, celNum))) {
//							orderVO.setCard_dc_cost("0");
//						} else {
//							orderVO.setCard_dc_cost(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 카드할인가
//						}
//						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setTel_no_1(getValueXls(row, celNum)); // 수취인전화번호
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setTel_no_2(getValueXls(row, celNum)); // 수취인휴대폰번호
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setAddress(getValueXls(row, celNum)); // 수취인주소
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setAddress_detail(getValueXls(row, celNum)); // 수취인상세주소
						}
						celNum++;
						
					
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setDelivery_message(getValueXls(row, celNum)); // 배송요청내용
						}
						else if ("".equals(getValueXls(row, celNum))) {
							orderVO.setDelivery_message("");
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {
							String goods_message = getValueXls(row, celNum);
							orderVO.setGoods_message(goods_message.replaceAll("\n", " ")); // 공급처요청내용(=상품메모)
						}
						else if ("".equals(getValueXls(row, celNum))) {
							orderVO.setGoods_message("");
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setFree_gift_name(getValueXls(row, celNum)); // 사은품대상
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setPay_method(getValueXls(row, celNum)); // 결제 수단 명
						}
						celNum++;

						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setE_pay_cost(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 하나로 주문금액(점포배송)
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setDlvry_pay_cost(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 택배 주문금액(택배배송)
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setE_sent_fee(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 하나로 배송비(점포배송)
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setDlvry_sent_fee(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 택배 배송비(택배배송)
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setE_card_dc(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 하나로 카드즉시할인(점포배송)
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setDlvry_card_dc(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 택배 카드즉시할인(택배배송)
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setCoupon_dc(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 쿠폰금액(합계)
						}
						celNum++;

						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setPay_cost(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // 결제금액
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setPromotion_name(getValueXls(row, celNum)); // 프로모션명
						}else{
							orderVO.setPromotion_name("");
						}
						celNum++;
						
						if (!"".equals(getValueXls(row, celNum))) {						
							orderVO.setN_coupon_cost(String.valueOf((int) Double.parseDouble(getValueXls(row, celNum)))); // N쿠폰 금액
						}
						celNum++;
						
						//------------------------------------------------------------------------------------------------------------------
						orderVOs.add(orderVO);
					}
				} catch (Exception e) {
					LOGGER.error(e.toString());
					e.printStackTrace();
					throw new Exception(e.getMessage());
				}

			}

		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			workbook.close();
		}

		return orderVOs;
	}

	/**
	 * Excel 셀데이터 읽어들여 String 변환처리
	 * @param row
	 * @param columnindex
	 * @return
	 */
	private String getValueXls(XSSFRow row, int columnindex) {
		String value = "";

		XSSFCell cell = row.getCell(columnindex);

		// 셀이 빈값일경우를 위한 널체크
		if (cell == null) {
			return value;
		} else {
			
			// 타입별로 내용 읽기
			switch (cell.getCellType()) {
			// 값 수식 자체를 가져올 때
			case XSSFCell.CELL_TYPE_FORMULA: 
				value = cell.getStringCellValue();
				break;
			// 값이 숫자 혹은 날짜 타입일 때
			case XSSFCell.CELL_TYPE_NUMERIC:
				value = cell.getNumericCellValue() + "";
				break;
			// 값이 문자 타입일 때
			case XSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue() + "";
				break;
			// 값이 공백일 때
			case XSSFCell.CELL_TYPE_BLANK:
				value = ""; 
				break;
			// 값이 에러일 때
			case XSSFCell.CELL_TYPE_ERROR:
				value = cell.getErrorCellValue() + "";
				break;
			}
		}

		return value;
	}
/*
	*//**
	 * Excel 파일 암호 지정 후 해제하여 읽어들이기
	 * @param file 
	 * @param row
	 * @param columnindex
	 * @return
	 *//*
	 private Workbook decryptExcelFile() throws IOException, InvalidFormatException {
         
		Workbook workbook = null;
		File file = new File("C:/excelUpload/");
		file.isDirectory();
		//String filePath = ;

			// Checking of .xlsx file with password protected.
			//String isWorkbookLock = "";
			InputStream is = null;
			is = new FileInputStream(file);
			if (!is.markSupported()) {
				is = new PushbackInputStream(is, 8);
			}

			if (POIFSFileSystem.hasPOIFSHeader(is)) {
				POIFSFileSystem fs = new POIFSFileSystem(is);
				EncryptionInfo info = new EncryptionInfo(fs);
				Decryptor d = Decryptor.getInstance(info);
				try {
					d.verifyPassword("1");
					is = d.getDataStream(fs);
					workbook = new XSSFWorkbook(OPCPackage.open(is));
					//isWorkbookLock = "true";
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
			}
			if (isWorkbookLock != "true") {
				FileInputStream fileInputStream = new FileInputStream(new File(filePath));
				workbook = new XSSFWorkbook(fileInputStream);
			}
		return workbook;
	}*/

	/**
	 * Socket 프린팅 처리 (선택 및 전체 출력시 호출같음 하나로 수정필요)
	 * 
	 * @param ip
	 * @param port
	 * @param sb
	 * @return
	 * @throws Exception
	 */
	public int socketPrinting(StringBuffer sb) throws Exception {

		// 접속정보 및 타임아웃 셋팅
		String host;
		int port 
			, result = 0 ;
		
		boolean isOnPrinter1 = false
				, isOnPrinter2 = false;

		// 프린터 정보 가져오기
		ComCodeVO searchComCodeVO = new ComCodeVO();
		searchComCodeVO.setCom_group_code("BARCODE_PRINTER");
		List<ComCodeVO> codeList = pcsDAO.selectComCodeList(searchComCodeVO);
		
		HashMap<Integer, String> dataMap = new HashMap<Integer, String>();
		SocketUtil socket = new SocketUtil();
		////////////////////////////////////////////////////////////////
		// 기본 프린터 상태 확인
		// 기본 프린터 셋팅
		host = codeList.get(0).getCom_name();
		port = Integer.parseInt(codeList.get(0).getCom_sub_code());
		
		try{

			// 프린터 서버 접속
			socket.connect(host, port);
			
			// 접속여부 확인
			if(socket.isConnected()){
				
				// 프린터 상태 요청
				socket.sendMessage(getZplHostStatus());
				dataMap = socket.receiveMessage(2);
				
				// 프린터 가능여부 처리
				isOnPrinter1 = checkPrintStatus(dataMap);
				LOGGER.info(dataMap.toString());
				socket.disconnect();
			}
		}catch(Exception e){
			result = 0;
			LOGGER.error(e.toString());
		}finally{
		}
		////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////
		// Printer ON인경우 라벨 출력
		if(isOnPrinter1){
			
			try{
				// 프린터 서버 접속
				socket.connect(host, port);

				// 프린터 접속 인경우 출력 처리
				if(socket.isConnected()){
					socket.sendMessage(sb.toString());
					socket.disconnect();
				}
			}catch(Exception e){
				result = 0;
			}finally{
			}
		}
		// 다른 프린터 상태 확인
		else{
			// 다른 프린터 셋팅
			host = codeList.get(1).getCom_name();
			port = Integer.parseInt(codeList.get(1).getCom_sub_code());
			
			try{

				// 2번 프린터 서버 접속
				socket.connect(host, port);
				
				// 접속여부 확인
				if(socket.isConnected()){

					// 프린터 상태 요청
					socket.sendMessage(getZplHostStatus());
					dataMap = socket.receiveMessage(2);
					
					// 2번 프린터 가능여부 처리
					isOnPrinter2 = checkPrintStatus(dataMap);
					LOGGER.info(dataMap.toString());
					socket.disconnect();

				}
			}catch(Exception e){
				result = 0;
				LOGGER.error(e.toString());
			}finally{
			}
			// 데이터 출력
			if(isOnPrinter2){
				try{
					// 2번 프린터 서버 접속					
					socket.connect(host, port);

					// 프린터 출력
					if(socket.isConnected()){
						socket.sendMessage(sb.toString());
						socket.disconnect();
					}
				}catch(Exception e){
					result = 0;		
					LOGGER.error(e.toString());
				}finally{
				}
			}
		}
		
		// 출력 가능 프린터 없음
		if(!isOnPrinter1 && !isOnPrinter2){
			result = 0;
			LOGGER.error("isOnPrinter1 = " +isOnPrinter1+ ", isOnPrinter2 = " +isOnPrinter2);
			throw new EgovBizException("출력가능한 프린터가 없습니다.");
		}
		
		Thread.sleep(1000);
		
		return result;
	}

	/*
	 * 출력 양식
	 */
	public StringBuffer assetLabelDiversion(EgovMap printingInfo) {
		/*
		 * sample parameter 순서 및 data shortOrderNo=001 , trayNo=1 ,
		 * orgOrderNo=20160708-000010 , pickId=000000000001 , deliveryCount=2 ,
		 * zoneCode=AB , deliveryDate=20160709 , deliveryAreaName=도봉구 ,
		 * customerName=홍*동}
		 */

		// 라벨상태 (F,L,O)
		// 단축주문번호 (3자리)
		// 트레이번호 (유동)
		// 원주문번호 ( [9자리]-[6자리])
		// 회차
		// Zone (2자리 | AA(후방/상온), AB(후방/냉장), AB(후방/냉동), AD(후방/대용량), BA(매장/상온),
		// BB(매장/냉장), BC(매장/냉동), BD(매장/대용량), CA(대면/축산) )
		// 배송일
		// 배송구명
		// 고객명

		// van / 트롤리ID 정보등
		printingInfo.put("van", "");
//		printingInfo.put("trolleyId", "");
		printingInfo.put("updateUserId", "SYSTEM");

		LOGGER.info(printingInfo.toString());
		StringBuffer assetSb = new StringBuffer();
		String labelStatusFullName = "";
		switch ((String) printingInfo.get("labelState")) {
		case "F":
			labelStatusFullName = "First";
			break;
		case "L":
			labelStatusFullName = "Last";
			break;
		case "O":
			labelStatusFullName = "Only";
			break;
		case "B":
			labelStatusFullName = "";
			break;
		default:
			labelStatusFullName = "";
			break;
		}
		
		
		//---------------------------------------------------------------------------------------
		// 라벨 프린트 설정
		assetSb.append("^XA");
		// Font 등 설정  (LH 시작위치, PW 용지너비)
		assetSb.append("^SEE:UHANGUL.DATT^FS  UHANGUL ");
		assetSb.append("^CWH,E:KFONT3.FNT^FS  (폰트명) ");
		assetSb.append("^CI28 ");
		assetSb.append("^LH0,0 ");
		assetSb.append("^PW900 ");	
		//              가로축      ,세로축   ,가로세로글자사이즈
		assetSb.append("^FO700, 80^AHR,50,45^FD" + labelStatusFullName + "^FS ");
		// M20170303 osj 단축번호 정보의 폰트 크기를 줄임
		assetSb.append("^FO500, 80^AHR,180,160^FD" + printingInfo.get("shortOrderNo") + "^FS "); // 단축주문번호
		assetSb.append("^FO500, 380^AHR,110,100^FD"  + "-" + printingInfo.get("trayNo") + "^FS "); // 트레이번호

		// 바코드 2차
		assetSb.append("^FO590, 650^BY4^B2R,150,Y,N,N^FD" + printingInfo.get("pickId") + "^FS  바코드용 2차"); // PICK ID
		// M20170316 osj 주문번호는 뒤에서 6자리만 노출
		assetSb.append("^FO360, 80^AHR,150,100^FD[" + StringUtil.cutBakSubStr((String) printingInfo.get("orderNo"), 6) + "]^FS "); // 주문번호
		// M20170303 osj 차수 정보의 폰트 크기를 줄임
		assetSb.append("^FO235, 80^AHR,100,100^FD" + printingInfo.get("deliveryCount") + printingInfo.get("van") + "-" + "^FS "); 
		assetSb.append("^FO210, 200^AHR,170,130^FD" + printingInfo.get("zoneCode") + "^FS ");
//		assetSb.append("^FO210, 80^AHR,170,130^FD" + printingInfo.get("deliveryCount") + printingInfo.get("van") + "-" + printingInfo.get("zoneCode") + "^FS ");
		assetSb.append("^FO390, 580^AHR,150,140^FD" + printingInfo.get("deliveryAreaName") + "^FS ");
		assetSb.append("^FO220, 580^AHR,130,110^FD" + printingInfo.get("orderCustomerName") + "^FS ");
		assetSb.append("^FO135, 100^AHR,60,50^FD" + ((String) printingInfo.get("deliveryDate")).substring(0, 4)
													+ "/" + ((String) printingInfo.get("deliveryDate")).substring(4, 6) 
													+ "/" + ((String) printingInfo.get("deliveryDate")).substring(6, 8)
													+ " " + printingInfo.get("trolleyId")
													+ "^FS ");
		assetSb.append("^XZ");
		//---------------------------------------------------------------------------------------
		
		
		return assetSb;
	}

	/**
	 * 배송일자 차수 그룹핑 조회
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 OrderVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public List<?> selectDeliveryCountList(DefaultVO searchVO) throws Exception {
		return pcsDAO.selectDeliveryCountList(searchVO);
	}

	/**
	 * 배송일자 차수에 따른 존 그룹핑 조회
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 OrderVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public List<?> selectZoneCodeList(DefaultVO searchVO) throws Exception {
		return pcsDAO.selectZoneCodeList(searchVO);
	}

	/**
	 * 피킹지시 차수 목록 조회
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 OrderVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public List<?> selectPickDeliveryCountList(DefaultVO searchVO) throws Exception {
		return pcsDAO.selectPickDeliveryCountList(searchVO);
	}

	/**
	 * 피킹지시 차수 존 목록 조회
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 OrderVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public List<?> selectPickZoneCodeList(DefaultVO searchVO) throws Exception {
		return pcsDAO.selectPickZoneCodeList(searchVO);
	}

	/**
	 * 피킹 처리 프로세서(2차)
	 * 
	 * @param List<EgovMap>
	 *            - 피킹지시할 리스트 처리
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	private EgovMap picking2nd(List<EgovMap> orderList) throws Exception {
		
		LOGGER.info("피킹지시 start");

		// 마지막 픽아이디 조회
		String pickId = pcsDAO.selectLastPickId();

		// 헤더 픽아이디
		int h_pickId = 0;
		int d_pickId = 0;

		// 최초등록
		if (pickId == null) {
			h_pickId = 1;
		} else {
			h_pickId = Integer.parseInt(pickId) + 1;
		}

		// 헤더 리스트 생성
		ArrayList<PickHeaderVO> pickHeaderVOs = new ArrayList<>();
		// 디테일 리스트 생성
		ArrayList<PickDetailVO> pickDetailVOs = new ArrayList<>();

		// 트레이 번호 , 상세 Detail 번호,
		int trayNo = 1;
		int pickRowNo = 1;
		int pickHeaderItemCount = 1;
		int trolleyIdCount = 1;
		int trolleyId = 1;
		int trayItemsSize = 10;

		// 오늘날자 ID 조회 없을경우 1부터 시작
		trolleyId = pcsDAO.selectTrolleyId();

		// 체크용 VO
		ArrayList<PickHeaderVO> oldPickHeaderVOs = new ArrayList<>();

		// 대용량 체크용
//		ComCodeVO comCodeVo = new ComCodeVO();
//		comCodeVo.setCom_group_code("ZONE_CODE");
//		comCodeVo.setCom_sub_code("_1");
//		List<ComCodeVO> bigZoneList = pcsDAO.selectComCodeList(comCodeVo);
		
		// 트레이 아이템수
		ComCodeVO trayComCodeVo = new ComCodeVO();
		trayComCodeVo.setCom_group_code("TRAY_ITEMS");

		trayItemsSize = Integer.parseInt(pcsDAO.selectComCodeList(trayComCodeVo).get(0).getCom_sub_code());
		
		// 대용량 pick Id 하나로 출력 (일반상품과 동일) 18-09-11
//		boolean isBigZone;
		try{
			// 데이터 생성용
			for (EgovMap egovMap : orderList) {
	
				// 트롤리 ID 일자별 처리
				if (trolleyIdCount > 6) {
					trolleyId = trolleyId + 1;
					trolleyIdCount = 1;
				}
				// 헤더 데이터용
				PickHeaderVO pickHeaderVo = new PickHeaderVO();
				PickHeaderVO oldPickHeaderVo = new PickHeaderVO();
				PickDetailVO pickDetailVo = new PickDetailVO();
	
				// 헤더비교용 Old 데이터 가져오기
				if (oldPickHeaderVOs.size() > 0) {
					oldPickHeaderVo = oldPickHeaderVOs.get(0);
				}
	
				// 대용량여부 확인
//				isBigZone = false;
//				for(ComCodeVO data : bigZoneList){
//					if(data.getCom_code().equals((String)egovMap.get("zoneCode"))){
//						isBigZone = true;
//					}
//				}
				// ====================================================================================================
				// 헤더정보생성
				// Pick Header
				// 먼저번 비교후 헤더정보 생성, 존 정보에 따른 헤더정보 생성, 10개이상일경우 헤더정보 생성
				pickHeaderVo.setDelivery_date((String) egovMap.get("deliveryDate"));
				pickHeaderVo.setDelivery_count((String) egovMap.get("deliveryCount"));
				pickHeaderVo.setShort_order_no((String) egovMap.get("shortOrderNo"));
				pickHeaderVo.setOrder_date((String) egovMap.get("orderDate"));
				pickHeaderVo.setOrder_no((String) egovMap.get("orderNo"));
				pickHeaderVo.setZone_code((String) egovMap.get("zoneCode"));
				pickHeaderVo.setOrg_delivery_count((String) egovMap.get("orgDeliveryCount"));
	
				// 2016.08.06 추가 (헤더 PK 이외에 경통코드가 틀린경우도 헤더추가
				pickHeaderVo.setGoods_code((String) egovMap.get("goodsCode"));
	
				// 있는 헤더 정보가 아닌경우 헤더 생성 (배송일,차수,단축주문번호,원주문번호,존코드)
				if (!oldPickHeaderVo.toHeaderKeyString().equals(pickHeaderVo.toHeaderKeyString()) ) { // && ! isBigZone 
	
					// 새로운 헤더 정보(주문정보) 일경우 트레이번호 초기화
					if (!oldPickHeaderVo.toTrayCheckString2nd().equals(pickHeaderVo.toTrayCheckString2nd())) {
						trayNo = 1;
						
					}
					// Header에 추가
					pickHeaderVo.setPick_id(String.format("%012d", h_pickId++));
					pickHeaderVo.setState_code("1");
					pickHeaderVo.setTray_no(trayNo++);
					pickHeaderVo.setDelivery_area_name((String) egovMap.get("deliveryAreaName"));
					pickHeaderVo.setCustomer_name((String) egovMap.get("customerName"));
					pickHeaderVo.setTrolley_id(String.format("%08d", trolleyId));
					trolleyIdCount++;
					
					pickHeaderVo.setDelivery_amount(((BigDecimal) egovMap.get("deliveryAmount")).intValue());
					pickHeaderVo.setOrder_customer_name((String) egovMap.get("orderCustomerName"));
					pickHeaderVo.setTel_no_1((String) egovMap.get("telNo1"));
					pickHeaderVo.setTel_no_2((String) egovMap.get("telNo2"));
					pickHeaderVo.setAddress((String) egovMap.get("address"));
					pickHeaderVo.setAddress_detail((String) egovMap.get("addressDetail"));
					pickHeaderVo.setFree_gift_name((String) egovMap.get("freeGiftName"));
					pickHeaderVo.setDelivery_message((String) egovMap.get("deliveryMessage"));
					pickHeaderVo.setGoods_message((String) egovMap.get("goodsMessage"));
					
					pickHeaderVOs.add(pickHeaderVo);
					
					// 체크용Header에 추가
					oldPickHeaderVOs.clear();
					oldPickHeaderVOs.add(pickHeaderVo);
	
					pickHeaderItemCount = 1;
				}
				
				// 존이 대용량일경우 갯수별 해더 생성 // 제거 18-09-11
//				if (isBigZone) {
//					int orderQty = ((BigDecimal) egovMap.get("orderQty")).intValue();
//					// 갯수별 헤더 생성
//					for (int i = 1; i <= orderQty; i++) {
//	
//						// 트롤리 ID 대용량일경우도 6넘어가면 추가 처리
//						if (trolleyIdCount > 6) {
//							trolleyId = trolleyId + 1;
//							trolleyIdCount = 1;
//						}
//						// old 데이터 가져오기 (trayNo 넘버링을 위해)
//						PickHeaderVO oldPickHeaderVo2 = new PickHeaderVO();
//						
//						// M20170213 nullpointexception check
//						if(oldPickHeaderVOs == null || oldPickHeaderVOs.size() <= 0){
//						
//							
//							// Header에 추가(M20170213 대용량 최초인 경우 pickId++ 제거)
//							pickHeaderVo.setPick_id(String.format("%012d", h_pickId));
//							pickHeaderVo.setState_code("1");
//							pickHeaderVo.setTray_no(trayNo++);
//							pickHeaderVo.setDelivery_area_name((String) egovMap.get("deliveryAreaName"));
//							pickHeaderVo.setCustomer_name((String) egovMap.get("customerName"));
//							pickHeaderVo.setTrolley_id(String.format("%08d", trolleyId));
//							trolleyIdCount++;
//							
//							// 20160927 컬럼 추가 (delivery_amount, order_customer_name, tel_no_1, tel_no_2, address
//							//				, free_gift_name , delivery_message)
//							pickHeaderVo.setDelivery_amount(((BigDecimal) egovMap.get("deliveryAmount")).intValue());				
//							pickHeaderVo.setOrder_customer_name((String) egovMap.get("orderCustomerName"));
//							pickHeaderVo.setTel_no_1((String) egovMap.get("telNo1"));
//							pickHeaderVo.setTel_no_2((String) egovMap.get("telNo2"));
//							pickHeaderVo.setAddress((String) egovMap.get("address"));
//							pickHeaderVo.setAddress_detail((String) egovMap.get("addressDetail"));
//							pickHeaderVo.setFree_gift_name((String) egovMap.get("freeGiftName"));
//							pickHeaderVo.setDelivery_message((String) egovMap.get("deliveryMessage"));
//							pickHeaderVo.setGoods_message((String) egovMap.get("goodsMessage"));
//	
//							// 체크용Header에 추가
//							oldPickHeaderVOs.clear();
//							oldPickHeaderVOs.add(pickHeaderVo);
//	
//							pickHeaderItemCount = 1;
//							
//							oldPickHeaderVo2 = oldPickHeaderVOs.get(0);
//							
//						}else{
//							oldPickHeaderVo2 = oldPickHeaderVOs.get(0);
//						}
//						
//						// 새로운 헤더 정보(주문정보) 일경우 트레이번호 초기화
//						if (!oldPickHeaderVo2.toTrayCheckString2nd().equals(pickHeaderVo.toTrayCheckString2nd())) {
//							trayNo = 1;
//							
//						}
//	
//						PickHeaderVO pickHeaderVo2 = new PickHeaderVO();
//						pickHeaderVo2.setDelivery_date(pickHeaderVo.getDelivery_date());
//						pickHeaderVo2.setDelivery_count(pickHeaderVo.getDelivery_count());
//						pickHeaderVo2.setShort_order_no(pickHeaderVo.getShort_order_no());
//						pickHeaderVo2.setOrder_date((String) egovMap.get("orderDate"));
//						pickHeaderVo2.setOrder_no((String) egovMap.get("orderNo"));
//						//pickHeaderVo2.setOrg_order_no(pickHeaderVo.getOrg_order_no());
//						pickHeaderVo2.setZone_code((String) egovMap.get("zoneCode"));
//						pickHeaderVo2.setOrg_delivery_count((String) egovMap.get("orgDeliveryCount"));
//						pickHeaderVo2.setPick_id(String.format("%012d", h_pickId++));
//						pickHeaderVo2.setState_code("1");
//						pickHeaderVo2.setTray_no(trayNo++);
//						pickHeaderVo2.setDelivery_area_name((String) egovMap.get("deliveryAreaName"));
//						pickHeaderVo2.setCustomer_name((String) egovMap.get("customerName"));
//						pickHeaderVo2.setTrolley_id(String.format("%08d", trolleyId));
//						
//						// 20160927 컬럼 추가 (delivery_amount, order_customer_name, tel_no_1, tel_no_2, address
//						//				, free_gift_name , delivery_message)
//						pickHeaderVo2.setDelivery_amount(((BigDecimal) egovMap.get("deliveryAmount")).intValue());
//						pickHeaderVo2.setOrder_customer_name((String) egovMap.get("orderCustomerName"));
//						pickHeaderVo2.setTel_no_1((String) egovMap.get("telNo1"));
//						pickHeaderVo2.setTel_no_2((String) egovMap.get("telNo2"));
//						pickHeaderVo2.setAddress((String) egovMap.get("address"));
//						pickHeaderVo2.setAddress_detail((String) egovMap.get("addressDetail"));
//						pickHeaderVo2.setFree_gift_name((String) egovMap.get("freeGiftName"));
//						pickHeaderVo2.setDelivery_message((String) egovMap.get("deliveryMessage"));
//						pickHeaderVo2.setGoods_message((String) egovMap.get("goodsMessage"));
//	
//						
//						
//						
//						trolleyIdCount++;
//						// 헤더 List에 추가
//						pickHeaderVOs.add(pickHeaderVo2);
//	
//						// 체크용Header에 추가
//						oldPickHeaderVOs.clear();
//						oldPickHeaderVOs.add(pickHeaderVo2);
//	
//					}
//					pickHeaderItemCount = 1;
//				}
	
				// 갯수가 10개 이상 넘어가면 헤더정보 생성(공통코드의 트레이 상품수로 대체)
				if (pickHeaderItemCount > trayItemsSize) {
					pickHeaderVo.setPick_id(String.format("%012d", h_pickId++));
					pickHeaderVo.setState_code("1");
					pickHeaderVo.setTray_no(trayNo++);
					pickHeaderVo.setDelivery_area_name((String) egovMap.get("deliveryAreaName"));
					pickHeaderVo.setCustomer_name((String) egovMap.get("customerName"));
					pickHeaderVo.setTrolley_id(String.format("%08d", trolleyId));
					
					// 20160927 컬럼 추가 (delivery_amount, order_customer_name, tel_no_1, tel_no_2, address
					//				, free_gift_name , delivery_message)
//					pickHeaderVo.setDelivery_amount(((BigDecimal) egovMap.get("deliveryAmount")).intValue());	
					pickHeaderVo.setOrder_customer_name((String) egovMap.get("orderCustomerName"));
					pickHeaderVo.setTel_no_1((String) egovMap.get("telNo1"));
					pickHeaderVo.setTel_no_2((String) egovMap.get("telNo2"));
					pickHeaderVo.setAddress((String) egovMap.get("address"));
					pickHeaderVo.setAddress_detail((String) egovMap.get("addressDetail"));
					pickHeaderVo.setFree_gift_name((String) egovMap.get("freeGiftName"));
					pickHeaderVo.setDelivery_message((String) egovMap.get("deliveryMessage"));
					pickHeaderVo.setGoods_message((String) egovMap.get("goodsMessage"));
	
					trolleyIdCount++;
					pickHeaderVOs.add(pickHeaderVo);
	
					pickHeaderItemCount = 1;
				}
	
				// ====================================================================================================
	
				// ====================================================================================================
				// pick detail 정보 등록
				if (oldPickHeaderVo.toHeaderKeyString().equals(pickHeaderVo.toHeaderKeyString())) {
					pickRowNo++;
					if (pickRowNo > trayItemsSize) { // Mod20170118 osj 공통코드의 트레이 상품 수로 비교 정정
						pickRowNo = 1;
					}
				} else {
					pickRowNo = 1;
				}
	
				d_pickId = h_pickId - 1;
				// AD ,BD 대용량일경우 1수량단위로 분할 // 제거 18-09-11
//				if (isBigZone) {
//					int orderQty = ((BigDecimal) egovMap.get("orderQty")).intValue();
//					for (int i = 1; i <= orderQty; i++) {
//						PickDetailVO pickDetailVo2 = new PickDetailVO();
//						pickDetailVo2.setPick_id(String.format("%012d", d_pickId - (orderQty - i)));
//						pickDetailVo2.setPick_row_no(1);
//						pickDetailVo2.setGoods_code((String) egovMap.get("goodsCode"));
//						pickDetailVo2.setSales_goods_code((String) egovMap.get("salesGoodsCode"));
//						pickDetailVo2.setGoods_name((String) egovMap.get("goodsName"));
//						pickDetailVo2.setOrder_qty(1);
//						pickDetailVo2.setChange_allow_yn((String) egovMap.get("changeAllowYn"));
//						pickDetailVo2.setState_code("1");
//	
//						// 20160829 컬럼 추가 (주문단가:order_cost, 제조사명:maker_name,
//						// 상품규격:goods_spec)
//						pickDetailVo2.setOrder_cost((int) egovMap.get("orderCost"));
//						pickDetailVo2.setMaker_name((String) egovMap.get("makerName"));
//						pickDetailVo2.setGoods_spec((String) egovMap.get("goodsSpec"));
//	
//						// 20160927 컬럼 추가 (card_dc_cost, goods_option_name, tax_type , order_row_no)
//						pickDetailVo2.setGoods_option_name((String) egovMap.get("goodsOptionName"));
//						pickDetailVo2.setTax_type((String) egovMap.get("taxType"));
//						pickDetailVo2.setOrder_row_no((String) egovMap.get("orderRowNo"));
//						
//				 		// 20161024 엑셀데이터 임포트 컬럼 추가 (전시카테고리(대):category_large / 전시카테고리(중):category_middle)
//						// 20161024 엑셀데이터 임포트 컬럼 삭제 (카드할인가:card_dc_cost)
//						pickDetailVo2.setCategory_large((String) egovMap.get("categoryLarge"));
//						pickDetailVo2.setCategory_middle((String) egovMap.get("categoryMiddle"));
//						
//						pickDetailVo2.setPay_method((String) egovMap.get("payMethod"));
//						
//						pickDetailVo2.setE_pay_cost((String) egovMap.get("ePayCost"));
//						pickDetailVo2.setDlvry_pay_cost((String) egovMap.get("dlvryPayCost"));
//						pickDetailVo2.setE_sent_fee((String) egovMap.get("eSentFee"));
//						pickDetailVo2.setDlvry_sent_fee((String) egovMap.get("dlvrySentFee"));
//						pickDetailVo2.setE_card_dc((String) egovMap.get("eCardDc"));
//						pickDetailVo2.setDlvry_card_dc((String) egovMap.get("dlvryCardDc"));
//						pickDetailVo2.setCoupon_dc((String) egovMap.get("couponDc"));
//						pickDetailVo2.setPay_cost((String) egovMap.get("payCost"));
//						pickDetailVo2.setPromotion_name((String) egovMap.get("promotionName"));
//						pickDetailVo2.setGoods_stock_code((String) egovMap.get("goodsStockCode"));
//	
//						
//						pickDetailVOs.add(pickDetailVo2);
//						pickHeaderItemCount = 1;
//					}
//				}
				// 일반 상세
//				else {
					
					pickDetailVo.setPick_id(String.format("%012d", d_pickId));
					pickDetailVo.setPick_row_no(pickRowNo);
					pickDetailVo.setGoods_code((String) egovMap.get("goodsCode"));
					pickDetailVo.setSales_goods_code((String) egovMap.get("salesGoodsCode"));
					pickDetailVo.setGoods_name((String) egovMap.get("goodsName"));
					pickDetailVo.setOrder_qty(((BigDecimal) egovMap.get("orderQty")).intValue());
					pickDetailVo.setChange_allow_yn((String) egovMap.get("changeAllowYn"));
					pickDetailVo.setState_code("1");
	
					// 20160829 컬럼 추가 (주문단가:order_cost, 제조사명:maker_name,
					// 상품규격:goods_spec)
					pickDetailVo.setOrder_cost((int) egovMap.get("orderCost"));
					pickDetailVo.setMaker_name((String) egovMap.get("makerName"));
					pickDetailVo.setGoods_spec((String) egovMap.get("goodsSpec"));
	
					// 20160927 컬럼 추가 (card_dc_cost, goods_option_name, tax_type , order_row_no)
					pickDetailVo.setGoods_option_name((String) egovMap.get("goodsOptionName"));
					pickDetailVo.setTax_type((String) egovMap.get("taxType"));
					pickDetailVo.setOrder_row_no((String) egovMap.get("orderRowNo"));
	
			 		// 20161024 엑셀데이터 임포트 컬럼 추가 (전시카테고리(대):category_large / 전시카테고리(중):category_middle)
					// 20161024 엑셀데이터 임포트 컬럼 삭제 (카드할인가:card_dc_cost)
					pickDetailVo.setCategory_large((String) egovMap.get("categoryLarge"));
					pickDetailVo.setCategory_middle((String) egovMap.get("categoryMiddle"));
					
					pickDetailVo.setPay_method((String) egovMap.get("payMethod"));
					
					pickDetailVo.setE_pay_cost((String) egovMap.get("ePayCost"));
					pickDetailVo.setDlvry_pay_cost((String) egovMap.get("dlvryPayCost"));
					pickDetailVo.setE_sent_fee((String) egovMap.get("eSentFee"));
					pickDetailVo.setDlvry_sent_fee((String) egovMap.get("dlvrySentFee"));
					pickDetailVo.setE_card_dc((String) egovMap.get("eCardDc"));
					pickDetailVo.setDlvry_card_dc((String) egovMap.get("dlvryCardDc"));
					pickDetailVo.setCoupon_dc((String) egovMap.get("couponDc"));
					pickDetailVo.setPay_cost((String) egovMap.get("payCost"));
					pickDetailVo.setPromotion_name((String) egovMap.get("promotionName"));
					pickDetailVo.setGoods_stock_code((String) egovMap.get("goodsStockCode"));
					pickDetailVo.setN_coupon_cost((String) egovMap.get("nCouponCost"));
					
					pickDetailVOs.add(pickDetailVo);
					pickHeaderItemCount++;
//				}
				
			
				// ====================================================================================================
	
			} // End List (데이터 생성용)
	
			// 라벨 셋팅 처리
			LabelChecker checker = new LabelChecker();
			checker.setZoneCodeList(pickHeaderVOs);
			for (PickHeaderVO pvo : pickHeaderVOs) {
				pvo.setLabel_state(checker.getLabelStr(pvo.getZone_code()));
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}

		// return 데이터 생성
		EgovMap dataMap = new EgovMap();
		dataMap.put("pickHeader", pickHeaderVOs);
		dataMap.put("pickDetail", pickDetailVOs);

		return dataMap;
		
	}// end picking 2nd

	/**
	 * 라벨출력 횟수 조회 (배송일자 + 배송차수)
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 OrderVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public int selectLabelPrintCount(DefaultVO searchVO) throws Exception {
		return pcsDAO.selectLabelPrintCount(searchVO);
	}

	/**
	 * 라벨출력 횟수 조회 (배송일자 + 배송차수)
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 OrderVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public int selectLastDeliveryCount(DefaultVO searchVO) throws Exception {
		return pcsDAO.selectLastDeliveryCount(searchVO);
	}

	/**
	 * 라벨출력 횟수 조회 (배송일자 + 배송차수)
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 OrderVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public int selectPickLastDeliveryCount(DefaultVO searchVO) throws Exception {
		return pcsDAO.selectPickLastDeliveryCount(searchVO);
	}

	/**
	 * 공통코드 목록 조회
	 * 
	 * @param com_group_code
	 *            - 공통그룹코드
	 * @param com_code
	 *            - 공통코드
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public List<ComCodeVO> selectComCodeList(ComCodeVO comCodeVO) throws Exception {
		return pcsDAO.selectComCodeList(comCodeVO);
	}

	/**
	 * 진행상태 목록 조회용
	 * 
	 * @param com_sub_code
	 *            - 공통서브 코드
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public List<?> selectStateCodeList(ComCodeVO comCodeVO) throws Exception {
		return pcsDAO.selectStateCodeList(comCodeVO);
	}

	@Override
	public List<?> selectPickHeaderList(DefaultVO searchVO) {
		return pcsDAO.selectPickHeader(searchVO);
	}

	@Override
	public List<?> selectPickDetailList(String searchPickId) {
		return pcsDAO.selectPickDetail(searchPickId);
	}

	/**
	 * 대체현황조회
	 * 
	 * @param vo
	 *            - 조회할 정보가 담긴 searchVO
	 * @return 조회한 오더정보
	 * @throws Exception
	 */
	@Override
	public List<?> selectChangePickList(DefaultVO searchVO) {
		return pcsDAO.selectChangePickList(searchVO);
	}

	/**
	 * 통계 정보
	 */
	@Override
	public List<?> selectJobTotalInfo(DefaultVO searchVO) {
		return pcsDAO.selectJobTotalInfo(searchVO);
	}

	/**
	 * 작업완료 목록
	 */
	@Override
	public List<?> selectJobStateList(DefaultVO searchVO) {
		return pcsDAO.selectJobStateList(searchVO);
	}

	/**
	 * 결품현황 목록 조회 @param vo - 조회활 정보가 담긴 searchVO @return 결품현황 목록 @throws
	 */
	@Override
	public List<?> selectReasonStateList(DefaultVO searchVO) {
		return pcsDAO.selectReasonStateList(searchVO);
	}
	
	/**
	 * 차수별 피킹결과 목록 조회 @param vo - 조회활 정보가 담긴 searchVO @return 결품현황 목록 @throws
	 */
	@Override
	public List<?> selectPickingResultList(DefaultVO searchVO) {
		return pcsDAO.selectPickingResultList(searchVO);
	}
	/**
	 * 대체수량이 있는 주문건 조회
	 * @param searchVO
	 * @return
	 */
	@Override
	public List<Map<String, String>> selectChangePickOrderList(DefaultVO searchVO) {
		return pcsDAO.selectChangePickOrderList(searchVO);
	}

	/**
	 * 그룹코드에 해당하는 코드목록 조회(ALL) @param vo - 그룹코드 @return 그룹코드 목록 조회 @throws
	 */
	@Override
	public List<?> selectALLComCodeList(DefaultVO searchVO) {
		return pcsDAO.selectALLComCodeList(searchVO);
	}

	/**
	 * 그룹코드 업데이트 처리 (UPDATE 또는 INSERT) @param vo - 그룹코드 @return 그룹코드 목록
	 * 조회 @throws
	 */
	@Override
	public int updateComCodeGroup(ArrayList<EgovMap> comCodeList) {

		String state = "";
		int result = 0;

		// 데이터 확인 처리
		for (EgovMap comCode : comCodeList) {

			// 추가,수정에 따른 구분값 (U, A)

			state = (String) comCode.get("state");
			// 구분값이 있는 경우
			if (null != state) {
				// 업데이트 인경우
				if (state.equals("U")) {
					result += pcsDAO.updateComCodeGroup(comCode);
				}
				// 추가인 경우
				else if (state.equals("A")) {
					result += pcsDAO.insertComCodeGroup(comCode);
				}
			}
		}
		return result;
	}

	/**
	 * 코드 업데이트 처리 (UPDATE 또는 INSERT) @param vo - 그룹코드 @return 그룹코드 목록 조회 @throws
	 */
	@Override
	public int updateComCode(ArrayList<EgovMap> comCodeList) {

		String state = "";
		int result = 0;

		// 데이터 확인 처리
		for (EgovMap comCode : comCodeList) {

			// 추가,수정에 따른 구분값 (U, A)
			state = (String) comCode.get("state");

			// 구분값이 있는 경우
			if (null != state) {
				// 업데이트 인경우
				if (state.equals("U")) {
					result += pcsDAO.updateComCode(comCode);
				}
				// 추가인 경우
				else if (state.equals("A")) {
					result += pcsDAO.insertComCode(comCode);
				}
				System.out.println(comCode);
			}
		}
		return result;
	}

	/**
	 * jobStateCode 설정값 조회 @param vo - 그룹코드 @return 그룹코드 목록 조회 @throws
	 */
	@Override
	public List<?> selectJobStateCode() {
		return pcsDAO.selectJobStateCode();
	}

	// PDF 파일 출력
	@Override
	public void pdfPrint(String destPDFFileName) throws Exception {
		
		LOGGER.info("============================================== PcsServiceImpl pdfPrint START==============================================");

		String printerName = ""; // "Canon MF210 Series"
		// 프린터 이름 가져오기
		ComCodeVO comCodeVO = new ComCodeVO();
		comCodeVO.setCom_group_code("PRINTER_NAME");
		comCodeVO.setCom_code("0");
		List<ComCodeVO> selectComCodeList = selectComCodeList(comCodeVO);
		if (selectComCodeList.size() > 0) {
			ComCodeVO codeVO = selectComCodeList.get(0);
			printerName = codeVO.getCom_name();
		}

		// 프린터 서비스 찾기
		PrintService myPrintService = null;
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService printService : printServices) {
			if (printService.getName().trim().equals(printerName)) {
				myPrintService = printService;
			}
		}

		if (myPrintService == null) {
			throw new Exception("프린터 서비스가 잡히지 않습니다.");
		}

		// PDF 파일 출력
		PrinterJob job = PrinterJob.getPrinterJob();
		PDDocument document = PDDocument.load(new File(destPDFFileName));
		job.setPageable(new PDFPageable(document));
		job.setPrintService(myPrintService);
		

		try {
			job.print();
		} catch (PrinterException pe) {
			pe.printStackTrace();
			LOGGER.error("print error : " + pe.toString());
		}
		
		document.close();

		LOGGER.info("============================================== PcsServiceImpl pdfPrint END==============================================");
	}
	
	/**
	 * 프린터 상태 확인용 ZPL
	 * 
	 * @return
	 */
	@Override
	public String getZplHostStatus() {

		StringBuffer zplMessage = new StringBuffer();
		zplMessage.append("~HS");

		return zplMessage.toString();
	}
	

	/**
	 * 프린터 장애 체크 로직
	 * @param dataMap
	 * @return
	 */
	private boolean checkPrintStatus(HashMap<Integer, String> dataMap) {
		/*
		 *
		 *	[1 Line]
		 	aaa = communication (interface) settings*
>			b = paper out flag (1 = paper out)
>			c = pause flag (1 = pause active)
			dddd = label length (value in number of dots)
			eee = number of formats in receive buffer
>			f = buffer full flag (1 = receive buffer full)
			g = communications diagnostic mode flag (1 = diagnostic mode active)
			h = partial format flag (1 = partial format in progress)
			iii = unused (always 000)
			j = corrupt RAM flag (1 = configuration data lost)
			k = temperature range (1 = under temperature)
			l = temperature range (1 = over temperature)
			
			[2 Line]
			mmm = function settings*
			n = unused
>			o = head up flag (1 = head in up position)
?			p = ribbon out flag (1 = ribbon out)
?			q = thermal transfer mode flag (1 = Thermal Transfer Mode selected)
			r = Print Mode
			0 = Rewind
			1 = Peel-Off
			2 = Tear-Off
			3 = Cutter
			4 = Applicator
			s = print width mode
			t = label waiting flag (1 = label waiting in Peel-off Mode)
			uuuuuu
			uu
			= labels remaining in batch
			v = format while printing flag (always 1)
			www = number of graphic images stored in memory
		 */		
		
		boolean isOnCheck = true;
		
		String[] rowData1  = dataMap.get(0).split(",");
		String[] rowData2  = dataMap.get(1).split(",");
//		String[] rowData3  = dataMap.get(2).split(",");
		
		// 용지가 없는 경우
		if("1".equals(rowData1[1])){
			isOnCheck = false;
		}
		
		// 일시정지인 경우
		if("1".equals(rowData1[2])){
			isOnCheck = false;
		}
		
		// 메모리가 full 인 경우
		if("1".equals(rowData1[5])){
			isOnCheck = false;
		}
		
		// 프린터가 열려있는경우
		if("1".equals(rowData2[2])){
			isOnCheck = false;
		}
		return isOnCheck;
	}
	
	/**
	 * 리스트 데이터 출력 처리
	 * @param listData
	 * @return
	 * @throws Exception 
	 */
	private int printingListData(ArrayList<EgovMap> listData) throws Exception{
		
		//
		int printingCnt = 0
			, listCnt = listData.size();
		
		try {
			// pick_id로 출력후 업데이트
			for (EgovMap dataInfo : listData) {
				// 출력 갯수200개에 따른 Sleep 처리(10초)
				if (printingCnt % 200 == 0 && listCnt > 200) {
					Thread.sleep(10 * 1000);
					LOGGER.debug(">>>>>>>>> LabelCount:" + printingCnt + " Sleep");
				}
				printingCnt++;
				// 라벨 출력 처리
				socketPrinting(assetLabelDiversion(dataInfo));
			        
				// 출력 카운트 업데이트 처리
				pcsDAO.updateLabelPrintCount(dataInfo);
			
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}		
		
		return printingCnt; 
	}		
	
	/**
	 * 사전피킹리스트 출력 불러오기
	 */
	@Override
	public List<?> selectPrePckListPr(DefaultVO searchVO, String rootPath) {
		ArrayList<ReportPrePckVO> printingList = (ArrayList<ReportPrePckVO>) pcsDAO.selectPrePckList(searchVO);
		
		    try{
		    	printPrePckList(printingList, rootPath);
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		
		return printingList; 
	}
	
	/**
	 * 사전피킹리스트 조회 불러오기
	 */
//	@Override
//	public List<?> selectPrePckListInq(DefaultVO searchVO, String rootPath) {
//		ArrayList<ReportPrePckVO> printingListInq = (ArrayList<ReportPrePckVO>) pcsDAO.selectPrePckList(searchVO);
//		
//			try{
//				inquiryPrePckList(printingListInq, rootPath);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//
//		return printingListInq; 
//	}
	
	/**
	 * 사전피킹리스트 pdf 생성 후 출력
	 * @param reportPrePckVOs
	 * @param rootPath
	 */
	private void printPrePckList(ArrayList<ReportPrePckVO> reportPrePckVOs,
			String rootPath) {
		// /////////////////////////////////////////////////////////////////////
		// 기본 정보 및 validation 셋팅부
		// /////////////////////////////////////////////////////////////////////
		if (reportPrePckVOs == null) {
			LOGGER.info("printPrePckList is null");
			return;
		} else if (reportPrePckVOs.size() == 0) {
			LOGGER.info("printPrePckList 사이즈 0");
			return;
		}

		LOGGER.info("printPrePckList reportPrePckVO " + reportPrePckVOs);
		LOGGER.info("printPrePckList rootPath " + rootPath);

		// /////////////////////////////////////////////////////////////////////
		// 비즈니스 로직 구현부
		// /////////////////////////////////////////////////////////////////////

		// data를 가져옴
//		ReportPrePckVO pVO = null;
//		for (int i = 0; reportPrePckVOs.size() > i; i++) {
//			pVO = reportPrePckVOs.get(i);
//			LOGGER.info("pVO.getZone_code() : "          	   + pVO.getZone_code());
//			LOGGER.info("pVO.getDelivery_date() : "            + pVO.getDelivery_date());
//			LOGGER.info("pVO.getDelivery_count() : "           + pVO.getDelivery_count());
//			LOGGER.info("pVO.getShort_order_no() : "           + pVO.getShort_order_no());
//			LOGGER.info("pVO.getCustomer_name() : "            + pVO.getCustomer_name());
//			LOGGER.info("pVO.getOrder_customer_name() : "      + pVO.getOrder_customer_name());
//			LOGGER.info("pVO.getGoods_code() : "               + pVO.getGoods_code());
//			LOGGER.info("pVO.getGoods_name() : "               + pVO.getGoods_name());
//			LOGGER.info("pVO.getGoods_option_name() :  "       + pVO.getGoods_option_name());
//			LOGGER.info("pVO.getGoods_spec() : "               + pVO.getGoods_spec());
//			LOGGER.info("pVO.getOrder_qty() : "                + pVO.getOrder_qty());
//			LOGGER.info("pVO.getOrder_cost() : "               + pVO.getOrder_cost());
//			LOGGER.info("pVO.getDelivery_message() : "         + pVO.getDelivery_message());
//			LOGGER.info("pVO.getChange_allow_yn() : "          + pVO.getChange_allow_yn());
////			LOGGER.info("pVO.getGoods_message() : "            + pVO.getGoods_message());
//		}

		// 파일 생성용
		ReportPrePckVO reportPrePckVO = reportPrePckVOs.get(0);
		String zone_code ="";
		if(reportPrePckVO.getZone_code_all() != null){ // 전체검색
			zone_code = reportPrePckVO.getZone_code_all();
		}else {
			zone_code = reportPrePckVO.getZone_code();
		}

		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportPrePckVOs);

		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("rootPath", rootPath); // 루트 경로

		try {
			// 사전피킹리스트 파일 이름
			String jasperFileName = rootPath + "/jasper/prePickingList.jasper";
			LOGGER.info("jasperFileName : " + jasperFileName);
			// 오늘 날짜 폴더로 생성 후 그 폴더에 pdf 생성
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			String today = format.format(new Date());
			FileUtil.createFolder("D:/vertexid/pdf/" + today);
			String destFileName = "D:/vertexid/pdf/" + today + "/prePickingList_" + reportPrePckVO.getDelivery_date()
					+ "_" + reportPrePckVO.getDelivery_count() + "_" + zone_code + ".pdf";

			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);

			if (printFileName != null) {
				LOGGER.info("printFileName : " + printFileName);

				// .jrprint 파일의 내용을 pdf 파일로 변환
				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);

				// pdf 파일을 프린터 인쇄 메소드 호출
				pdfPrint(destFileName);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
		}

	}
	
	/**
	 * 사전피킹리스트 pdf 생성 후 조회
	 * 
	 * @param reportPrePckVOs
	 * @param rootPath
	 */

//	private void inquiryPrePckList(ArrayList<ReportPrePckVO> reportPrePckVOs, String rootPath) {
//		// /////////////////////////////////////////////////////////////////////
//		// 기본 정보 및 validation 셋팅부
//		// /////////////////////////////////////////////////////////////////////
//		if (reportPrePckVOs == null) {
//			LOGGER.info("printPrePckList is null");
//			return;
//		} else if (reportPrePckVOs.size() == 0) {
//			LOGGER.info("printPrePckList 사이즈 0");
//			return;
//		}
//
//		LOGGER.info("printPrePckList reportPrePckVO " + reportPrePckVOs);
//		LOGGER.info("printPrePckList rootPath " + rootPath);
//
//		// /////////////////////////////////////////////////////////////////////
//		// 비즈니스 로직 구현부
//		// /////////////////////////////////////////////////////////////////////
//
//		// data를 가져옴
//		ReportPrePckVO pVO = null;
//		for (int i = 0; reportPrePckVOs.size() > i; i++) {
//			pVO = reportPrePckVOs.get(i);
//			LOGGER.info("pVO.getZone_code() : "          + pVO.getZone_code());
//			LOGGER.info("pVO.getDelivery_date() : "      + pVO.getDelivery_date());
//			LOGGER.info("pVO.getDelivery_count() : "     + pVO.getDelivery_count());
//			LOGGER.info("pVO.getShort_order_no() : "     + pVO.getShort_order_no());
//			LOGGER.info("pVO.getCustomer_name() : "      + pVO.getCustomer_name());
//			LOGGER.info("pVO.getGoods_code() : "         + pVO.getGoods_code());
//			LOGGER.info("pVO.getGoods_name() : "         + pVO.getGoods_name());
//			LOGGER.info("pVO.getGoods_option_name() :  " + pVO.getGoods_option_name());
//			LOGGER.info("pVO.getGoods_spec() : "         + pVO.getGoods_spec());
//			LOGGER.info("pVO.getOrder_qty() : "          + pVO.getOrder_qty());
//			LOGGER.info("pVO.getOrder_cost() : "         + pVO.getOrder_cost());
//			LOGGER.info("pVO.getDelivery_message() : "   + pVO.getDelivery_message());
//			LOGGER.info("pVO.getChange_allow_yn() : "    + pVO.getChange_allow_yn());
//			LOGGER.info("pVO.getGoods_message() : "      + pVO.getGoods_message());
//		}
//
//		// 파일 생성용
//		ReportPrePckVO reportPrePckVO = reportPrePckVOs.get(0);
//
//		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
//		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportPrePckVOs);
//
//		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
//		Map<String, Object> params = new HashMap<String, Object>();
//
//		params.put("rootPath", rootPath); // 루트 경로
//
//		try {
//			// 사전피킹리스트 파일 이름
//			String jasperFileName = rootPath + "/jasper/prePickingList.jasper";
//			LOGGER.info("jasperFileName : " + jasperFileName);
//			// 오늘 날짜 폴더로 생성 후 그 폴더에 pdf 생성
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//			String today = format.format(new Date());
//			FileUtil.createFolder("c:/vertexid/pdf/" + today);
//			String destFileName = "c:/vertexid/pdf/" + today + "/prePickingList_" + reportPrePckVO.getDelivery_date() + "_" + reportPrePckVO.getDelivery_count() + "_" + reportPrePckVO.getZone_code() + ".pdf";
//
//			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);
//
//			if (printFileName != null) {
//				LOGGER.info("printFileName : " + printFileName);
//
//				// .jrprint 파일의 내용을 pdf 파일로 변환
//				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);
//
//				// pdf 파일을 조회(열기) 메소드 호출
////				pdfOpen(destFileName);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
	
	/**
	 * 주문 배송 메모 리스트 pdf 생성 후 출력
	 * @param reportPrePckVOs
	 * @param rootPath
	 */
//	private void printOrderDelMessageList(ArrayList<ReportPrePckVO> reportPrePckVOs, String rootPath) {
//		// /////////////////////////////////////////////////////////////////////
//		// 기본 정보 및 validation 셋팅부
//		// /////////////////////////////////////////////////////////////////////
//		if (reportPrePckVOs == null) {
//			LOGGER.info("orderDeliveryMessageList is null");
//			return;
//		} else if (reportPrePckVOs.size() == 0) {
//			LOGGER.info("orderDeliveryMessageList 사이즈 0");
//			return;
//		}
//
//		LOGGER.info("orderDeliveryMessageList reportPrePckVO " + reportPrePckVOs);
//		LOGGER.info("orderDeliveryMessageList rootPath " + rootPath);
//
//		// /////////////////////////////////////////////////////////////////////
//		// 비즈니스 로직 구현부
//		// /////////////////////////////////////////////////////////////////////
//
//		// data를 가져옴
//		ReportPrePckVO pVO = null;
//		for (int i = 0; reportPrePckVOs.size() > i; i++) {
//			pVO = reportPrePckVOs.get(i);
//			LOGGER.info("pVO.getDelivery_date() : "           + pVO.getDelivery_date());
//			LOGGER.info("pVO.getDelivery_count() : "          + pVO.getDelivery_count());
//			LOGGER.info("pVO.getShort_order_no() : "          + pVO.getShort_order_no());
//			LOGGER.info("pVO.getOrder_customer_name() : "     + pVO.getOrder_customer_name());
//			LOGGER.info("pVO.getDelivery_area_name() : "      + pVO.getDelivery_area_name());
//			LOGGER.info("pVO.getDelivery_message() : "        + pVO.getDelivery_message());
//			LOGGER.info("pVO.getGoods_message() : "           + pVO.getGoods_message());
//		}
//
//		// 파일 생성용
//		ReportPrePckVO reportPrePckVO = reportPrePckVOs.get(0);
//
//		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
//		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportPrePckVOs);
//
//		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
//		Map<String, Object> params = new HashMap<String, Object>();
//
//		params.put("rootPath", rootPath); // 루트 경로
//
//		try {
//			// 주문 배송 메모 리스트 파일 이름
//			String jasperFileName = rootPath + "/jasper/orderDeliveryMessageList.jasper";
//			LOGGER.info("jasperFileName : " + jasperFileName);
//			// 오늘 날짜 폴더로 생성 후 그 폴더에 pdf 생성
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//			String today = format.format(new Date());
//			FileUtil.createFolder("c:/vertexid/pdf/" + today);
//			String destFileName = "c:/vertexid/pdf/" + today + "/orderDeliveryMessageList" +  "_" + reportPrePckVO.getDelivery_date() + "_" + reportPrePckVO.getDelivery_count() + ".pdf";
//
//			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);
//
//			if (printFileName != null) {
//				LOGGER.info("printFileName : " + printFileName);
//
//				// .jrprint 파일의 내용을 pdf 파일로 변환
//				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);
//
//				// pdf 파일을 프린터 인쇄 메소드 호출
//				pdfPrint(destFileName);
//			}
//		} catch (Exception e) {
//			LOGGER.error(e.toString());
//			e.printStackTrace();
//		}
//
//	}
	
	/**
	 * 주문 배송 메모 리스트 pdf 생성 후 조회
	 * 
	 * @param reportPrePckVOs
	 * @param rootPath
	 */

//	private void inquiryOrderDelMessageList(ArrayList<ReportPrePckVO> reportPrePckVOs, String rootPath) {
//		// /////////////////////////////////////////////////////////////////////
//		// 기본 정보 및 validation 셋팅부
//		// /////////////////////////////////////////////////////////////////////
//		System.out.println("====================reportPrePckVOs : " + reportPrePckVOs);
//		if (reportPrePckVOs == null) {
//			LOGGER.info("orderDeliveryMessageList is null");
//			return;
//		} else if (reportPrePckVOs.size() == 0) {
//			LOGGER.info("orderDeliveryMessageList 사이즈 0");
//			return;
//		}
//
//		LOGGER.info("inquiryOrderDelMessageList reportPrePckVO " + reportPrePckVOs);
//		LOGGER.info("inquiryOrderDelMessageList rootPath " + rootPath);
//
//		// /////////////////////////////////////////////////////////////////////
//		// 비즈니스 로직 구현부
//		// /////////////////////////////////////////////////////////////////////
//
//		// data를 가져옴
//		ReportPrePckVO pVO = null;
//		for (int i = 0; reportPrePckVOs.size() > i; i++) {
//			pVO = reportPrePckVOs.get(i);
//			LOGGER.info("pVO.getDelivery_date() : "           + pVO.getDelivery_date());
//			LOGGER.info("pVO.getDelivery_count() : "          + pVO.getDelivery_count());
//			LOGGER.info("pVO.getShort_order_no() : "          + pVO.getShort_order_no());
//			LOGGER.info("pVO.getOrder_customer_name() : "     + pVO.getOrder_customer_name());
//			LOGGER.info("pVO.getDelivery_area_name() : "      + pVO.getDelivery_area_name());
//			LOGGER.info("pVO.getDelivery_message() : "        + pVO.getDelivery_message());
//			LOGGER.info("pVO.getGoods_message() : "           + pVO.getGoods_message());
//		}
//
//		// 파일 생성용
//		ReportPrePckVO reportPrePckVO = reportPrePckVOs.get(0);
//
//		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
//		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportPrePckVOs);
//
//		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
//		Map<String, Object> params = new HashMap<String, Object>();
//
//		params.put("rootPath", rootPath); // 루트 경로
//
//		try {
//			// 주문 배송 메모 리스트 파일 이름
//			String jasperFileName = rootPath + "/jasper/orderDeliveryMessageList.jasper";
//			LOGGER.info("jasperFileName : " + jasperFileName);
//			// 오늘 날짜 폴더로 생성 후 그 폴더에 pdf 생성
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//			String today = format.format(new Date());
//			FileUtil.createFolder("c:/vertexid/pdf/" + today);
//			String destFileName = "c:/vertexid/pdf/" + today + "/orderDeliveryMessageList" +  "_" + reportPrePckVO.getDelivery_date() + "_" + reportPrePckVO.getDelivery_count() + ".pdf";
//
//			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);
//
//			if (printFileName != null) {
//				LOGGER.info("printFileName : " + printFileName);
//
//				// .jrprint 파일의 내용을 pdf 파일로 변환
//				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);
//
//				// pdf 파일을 조회(열기) 메소드 호출
////				pdfOpen(destFileName);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
	
	
	/**
	 * 피킹 요청서 리스트 pdf 생성 후 출력
	 * @param reportPrePckVOs
	 * @param rootPath
	 */
//	private void printPickingRequestList(ArrayList<ReportPrePckVO> reportPrePckVOs, String rootPath) {
//		// /////////////////////////////////////////////////////////////////////
//		// 기본 정보 및 validation 셋팅부
//		// /////////////////////////////////////////////////////////////////////
//		if (reportPrePckVOs == null) {
//			LOGGER.info("pickingRequestList is null");
//			return;
//		} else if (reportPrePckVOs.size() == 0) {
//			LOGGER.info("pickingRequestList 사이즈 0");
//			return;
//		}
//
//		LOGGER.info("printPickingRequestList reportPrePckVO " + reportPrePckVOs);
//		LOGGER.info("printPickingRequestList rootPath " + rootPath);
//
//		// /////////////////////////////////////////////////////////////////////
//		// 비즈니스 로직 구현부
//		// /////////////////////////////////////////////////////////////////////
//
//		// data를 가져옴
//		ReportPrePckVO pVO = null;
//		for (int i = 0; reportPrePckVOs.size() > i; i++) {
//			pVO = reportPrePckVOs.get(i);
//			LOGGER.info("pVO.getDelivery_date() : "           + pVO.getDelivery_date());
//			LOGGER.info("pVO.getDelivery_count() : "          + pVO.getDelivery_count());
//			LOGGER.info("pVO.getShort_order_no() : "          + pVO.getShort_order_no());
//			LOGGER.info("pVO.getOrder_customer_name() : "     + pVO.getOrder_customer_name());
//			LOGGER.info("pVO.getOrder_date() : "              + pVO.getOrder_date());
//			LOGGER.info("pVO.getOrder_no() : "                + pVO.getOrder_no());
//			LOGGER.info("pVO.getGoods_code() : "              + pVO.getGoods_code());
//			LOGGER.info("pVO.getGoods_name() : "              + pVO.getGoods_name());
//			LOGGER.info("pVO.getGoods_spec() : "              + pVO.getGoods_spec());
//			LOGGER.info("pVO.getOrder_qty() : "               + pVO.getOrder_qty());
//			LOGGER.info("pVO.getOrder_cost() : "              + pVO.getOrder_cost());
//			LOGGER.info("pVO.getZone_code() : "               + pVO.getZone_code());
//		}
//
//		// 파일 생성용
//		ReportPrePckVO reportPrePckVO = reportPrePckVOs.get(0);
//
//		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
//		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportPrePckVOs);
//
//		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
//		Map<String, Object> params = new HashMap<String, Object>();
//
//		params.put("rootPath", rootPath); // 루트 경로
//
//		try {
//			// 주문 배송 메모 리스트 파일 이름
//			String jasperFileName = rootPath + "/jasper/pickingRequestList.jasper";
//			LOGGER.info("jasperFileName : " + jasperFileName);
//			// 오늘 날짜 폴더로 생성 후 그 폴더에 pdf 생성
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//			String today = format.format(new Date());
//			FileUtil.createFolder("c:/vertexid/pdf/" + today);
//			String destFileName = "c:/vertexid/pdf/" + today + "/pickingRequestList" +  "_" + reportPrePckVO.getDelivery_date() + "_" + reportPrePckVO.getDelivery_count() + ".pdf";
//
//			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);
//
//			if (printFileName != null) {
//				LOGGER.info("printFileName : " + printFileName);
//
//				// .jrprint 파일의 내용을 pdf 파일로 변환
//				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);
//
//				// pdf 파일을 프린터 인쇄 메소드 호출
//				pdfPrint(destFileName);
//			}
//		} catch (Exception e) {
//			LOGGER.error(e.toString());
//			e.printStackTrace();
//		}
//
//	}
	
	/**
	 * 피킹요청서 리스트 pdf 생성 후 조회
	 * 
	 * @param reportPrePckVOs
	 * @param rootPath
	 */

//	private void inquiryPickingRequestList(ArrayList<ReportPrePckVO> reportPrePckVOs, String rootPath) {
//		// /////////////////////////////////////////////////////////////////////
//		// 기본 정보 및 validation 셋팅부
//		// /////////////////////////////////////////////////////////////////////
//		if (reportPrePckVOs == null) {
//			LOGGER.info("pickingRequestList is null");
//			return;
//		} else if (reportPrePckVOs.size() == 0) {
//			LOGGER.info("pickingRequestList 사이즈 0");
//			return;
//		}
//
//		LOGGER.info("inquiryPickingRequestList reportPrePckVO " + reportPrePckVOs);
//		LOGGER.info("inquiryPickingRequestList rootPath " + rootPath);
//
//		// /////////////////////////////////////////////////////////////////////
//		// 비즈니스 로직 구현부
//		// /////////////////////////////////////////////////////////////////////
//
//		// data를 가져옴
//		ReportPrePckVO pVO = null;
//		for (int i = 0; reportPrePckVOs.size() > i; i++) {
//			pVO = reportPrePckVOs.get(i);
//			LOGGER.info("pVO.getDelivery_date() : "           + pVO.getDelivery_date());
//			LOGGER.info("pVO.getDelivery_count() : "          + pVO.getDelivery_count());
//			LOGGER.info("pVO.getShort_order_no() : "          + pVO.getShort_order_no());
//			LOGGER.info("pVO.getOrder_customer_name() : "     + pVO.getOrder_customer_name());
//			LOGGER.info("pVO.getOrder_date() : "              + pVO.getOrder_date());
//			LOGGER.info("pVO.getOrder_no() : "                + pVO.getOrder_no());
//			LOGGER.info("pVO.getGoods_code() : "              + pVO.getGoods_code());
//			LOGGER.info("pVO.getGoods_name() : "              + pVO.getGoods_name());
//			LOGGER.info("pVO.getGoods_spec() : "              + pVO.getGoods_spec());
//			LOGGER.info("pVO.getOrder_qty() : "               + pVO.getOrder_qty());
//			LOGGER.info("pVO.getOrder_cost() : "              + pVO.getOrder_cost());
//			LOGGER.info("pVO.getZone_code() : "               + pVO.getZone_code());
//		}
//
//		// 파일 생성용
//		ReportPrePckVO reportPrePckVO = reportPrePckVOs.get(0);
//
//		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
//		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(reportPrePckVOs);
//
//		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
//		Map<String, Object> params = new HashMap<String, Object>();
//
//		params.put("rootPath", rootPath); // 루트 경로
//
//		try {
//			// 피킹요청서 리스트 파일 이름
//			String jasperFileName = rootPath + "/jasper/pickingRequestList.jasper";
//			LOGGER.info("jasperFileName : " + jasperFileName);
//			// 오늘 날짜 폴더로 생성 후 그 폴더에 pdf 생성
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//			String today = format.format(new Date());
//			FileUtil.createFolder("c:/vertexid/pdf/" + today);
//			String destFileName = "c:/vertexid/pdf/" + today + "/pickingRequestList" +  "_" + reportPrePckVO.getDelivery_date() + "_" + reportPrePckVO.getDelivery_count() + ".pdf";
//
//			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);
//
//			if (printFileName != null) {
//				LOGGER.info("printFileName : " + printFileName);
//
//				// .jrprint 파일의 내용을 pdf 파일로 변환
//				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);
//
//				// pdf 파일을 조회(열기) 메소드 호출
////				pdfOpen(destFileName);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
	
	@Override
	public List<?> selectPrintHeaderList(DefaultVO searchVO) {
		return pcsDAO.selectPrintHeader(searchVO);
	}

	@Override
	public List<?> nextDeliveryCountList(DefaultVO searchVO) throws Exception {
		return pcsDAO.nextDeliveryCountList(searchVO);
	}
	
	// 상품대체현황조회 엑셀 양식 생성 및 다운로드
	@Override
	public void excelChangeStateList(List<?> changePickList, String fileNm) throws Exception{
		
		LOGGER.info("============================================== PcsServiceImpl excelChangeStateList START ==============================================");
		
		// 워크북 생성
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 워크시트 생성
		XSSFSheet sheet = workbook.createSheet();
		
		// 헤더 부
		XSSFCellStyle cellStyle = workbook.createCellStyle();			
		cellStyle.setAlignment(cellStyle.ALIGN_CENTER); // 가운데 정렬
		
		// 폰트
		Font font = workbook.createFont();
		font.setBold(true);
		cellStyle.setFont(font);
		
		// 파란색
		XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setFillForegroundColor(new XSSFColor(new java.awt.Color(197, 217, 241))); // 색깔 설정		
		cellStyle1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); // 색 패턴을 반드시 설정해야 색깔이 셀이 들어감

		cellStyle1.setAlignment(cellStyle1.ALIGN_CENTER); // 가운데 정렬
		
		// 테두리 설정
		cellStyle1.setBorderTop(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderLeft(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderRight(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderBottom(cellStyle1.BORDER_THIN);

		cellStyle1.setFont(font);

		// 황토색 
		XSSFCellStyle cellStyle2 = workbook.createCellStyle();
		cellStyle2.setFillForegroundColor(new XSSFColor(new java.awt.Color(253, 233, 217))); // 색깔 설정		
		cellStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); // 색 패턴을 반드시 설정해야 색깔이 셀이 들어감

		cellStyle2.setAlignment(cellStyle2.ALIGN_CENTER); // 가운데 정렬
		
		// 테두리 설정
		cellStyle2.setBorderTop(cellStyle2.BORDER_THIN);
		cellStyle2.setBorderLeft(cellStyle2.BORDER_THIN);
		cellStyle2.setBorderRight(cellStyle2.BORDER_THIN);
		cellStyle2.setBorderBottom(cellStyle2.BORDER_THIN);
		
		cellStyle2.setFont(font);

		
		// 데이타 부
		XSSFCellStyle cellStyle3 = workbook.createCellStyle();
		// 테두리 설정
		cellStyle3.setBorderTop(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderLeft(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderRight(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderBottom(cellStyle3.BORDER_THIN);

		cellStyle3.setAlignment(cellStyle3.ALIGN_CENTER); // 가운데 정렬
		
		// 행 생성
		XSSFRow row1 = sheet.createRow(1);
		XSSFRow row = sheet.createRow(3);
		
		// 셀 생성
		XSSFCell cell;
		
		// 헤더 제목 구성
		cell = row1.createCell(0);
		cell.setCellValue("대체 리스트");
		cell.setCellStyle(cellStyle);

		// 셀병합 영역 생성
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));
		
		// 헤더 정보 구성		
		cell = row.createCell(0);
		cell.setCellValue("NO."); // NO.
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(1);
		cell.setCellValue("단축번호"); // 단축번호
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(2);
		cell.setCellValue("상품코드"); // 상품코드
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(3);
		cell.setCellValue("상품 명"); // 상품 명
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(4);
		cell.setCellValue("판매가"); // 판매가
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(5);
		cell.setCellValue("수량"); // 수량
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(6);
		cell.setCellValue("대체코드"); // 대체코드
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(7);
		cell.setCellValue("대체상품명"); // 대체상품명
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(8);
		cell.setCellValue("대체가격"); // 대체가격
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(9);
		cell.setCellValue("수량"); // 수량
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(10);
		cell.setCellValue("피커명"); // 피커명
		cell.setCellStyle(cellStyle1);
		
		// 리스트의 사이즈 만큼 row 생성
		EgovMap egovMap;
		for(int i=0; i < changePickList.size(); i++){
			egovMap = (EgovMap) changePickList.get(i);
			System.out.println("========================egovMap========================" + egovMap);
			
			// 행 생성
			int j = 4+i;
			row = sheet.createRow(j);
			
			// 조회된 값 각 셀에 넣기
			cell = row.createCell(0);
			// 조회한 번호가 double type이라서 형 변환 시도 함
			Double douVal = Double.parseDouble(egovMap.get("rowNumber").toString());
			int intVal = Integer.parseInt(String.valueOf(Math.round(douVal)));
			String StrVal = Integer.toString(intVal);			
			cell.setCellValue(StrVal);
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(1);
			cell.setCellValue(egovMap.get("shortOrderNo").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(2);
			cell.setCellValue(egovMap.get("goodsCode").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(3);
			cell.setCellValue(egovMap.get("goodsName").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(4);
			cell.setCellValue(egovMap.get("orderCost").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(5);
			cell.setCellValue(egovMap.get("orderQty").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(6);
			cell.setCellValue(egovMap.get("changeGoodsCode").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(7);
			cell.setCellValue(egovMap.get("changeGoodsName").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(8);
			cell.setCellValue(egovMap.get("changeGoodsCost").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(9);
			cell.setCellValue(egovMap.get("changePickQty").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(10);
			cell.setCellValue(egovMap.get("comName").toString());
			cell.setCellStyle(cellStyle3);
		}
		//칼럼 너비 조절
		 	//sheet.setColumnWidth(2, (sheet.getColumnWidth(2))+2500);
		 	//sheet.setColumnWidth(3, (sheet.getColumnWidth(3))+4500);
		 	//sheet.setColumnWidth(3, (sheet.getColumnWidth(3))+4500);
		 	//sheet.setColumnWidth(3, (sheet.getColumnWidth(3))+4500);
		
		
		// 입력된 내용을 파일로 쓰기
		File file = new File(fileNm);
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file);
			workbook.write(fos);
		}catch(FileNotFoundException fe){
			fe.printStackTrace();
		} 
		catch (IOException ie) {
			ie.printStackTrace();
		}finally{
			try {
				if(workbook != null) workbook.close();
				if(fos != null) fos.close();
			} catch (IOException ie2) {
				ie2.printStackTrace();
			}
		}
		
		LOGGER.info("============================================== PcsServiceImpl excelChangeStateList END==============================================");
		
	}
	
	// 상품결품현황조회 엑셀 양식 생성 및 다운로드
	@Override
	public void excelReasonStateList(List<?> changePickList, String fileNm) throws Exception{
		
		LOGGER.info("============================================== PcsServiceImpl excelReasonStateList START==============================================");
		
		// 워크북 생성
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 워크시트 생성
		XSSFSheet sheet = workbook.createSheet();
		
		// 헤더 부
		XSSFCellStyle cellStyle = workbook.createCellStyle();			
		cellStyle.setAlignment(cellStyle.ALIGN_CENTER); // 가운데 정렬
		
		
		// 폰트
		Font font = workbook.createFont();
		font.setBold(true);
		cellStyle.setFont(font);
		
		// 파란색
		XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setFillForegroundColor(new XSSFColor(new java.awt.Color(197, 217, 241))); // 색깔 설정		
		cellStyle1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); // 색 패턴을 반드시 설정해야 색깔이 셀이 들어감
		
		// 테두리 설정
		cellStyle1.setBorderTop(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderLeft(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderRight(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderBottom(cellStyle1.BORDER_THIN);
		
		cellStyle1.setAlignment(cellStyle1.ALIGN_CENTER); // 가운데 정렬
		
		cellStyle1.setFont(font);
		
		
		// 데이타 부
		XSSFCellStyle cellStyle3 = workbook.createCellStyle();
		// 테두리 설정
		cellStyle3.setBorderTop(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderLeft(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderRight(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderBottom(cellStyle3.BORDER_THIN);
		
		cellStyle3.setAlignment(cellStyle3.ALIGN_CENTER); // 가운데 정렬
		
		
		// 행 생성
		XSSFRow row1 = sheet.createRow(1);
		XSSFRow row = sheet.createRow(3);
		// 셀 생성
		XSSFCell cell;
		
		// 헤더 제목 구성
		cell = row1.createCell(0);
		cell.setCellValue("결품 리스트");
		cell.setCellStyle(cellStyle);
		
		// 셀병합 영역 생성
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
			
		// 헤더 정보 구성		
		cell = row.createCell(0);
		cell.setCellValue("NO."); // NO.
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(1);
		cell.setCellValue("단축번호"); // 단축번호
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(2);
		cell.setCellValue("상품코드"); // 상품코드
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(3);
		cell.setCellValue("상품 명"); // 상품 명
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(4);
		cell.setCellValue("판매가"); // 판매가
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(5);
		cell.setCellValue("수량"); // 수량
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(6);
		cell.setCellValue("피커명"); // 피커명
		cell.setCellStyle(cellStyle1);
		
		// 리스트의 사이즈 만큼 row 생성
		EgovMap egovMap;
		for(int i=0; i < changePickList.size(); i++){
			egovMap = (EgovMap) changePickList.get(i);
			System.out.println("========================egovMap========================" + egovMap);
			
			// 행 생성
			int j = 4+i;
			row = sheet.createRow(j);
			
			// 조회된 값 각 셀에 넣기
			cell = row.createCell(0);
			// 조회한 번호가 double type이라서 형 변환 시도 함
			Double douVal = Double.parseDouble(egovMap.get("rowNumber").toString());
			int intVal = Integer.parseInt(String.valueOf(Math.round(douVal)));
			String StrVal = Integer.toString(intVal);			
			cell.setCellValue(StrVal);
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(1);
			cell.setCellValue(egovMap.get("shortOrderNo").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(2);
			cell.setCellValue(egovMap.get("goodsCode").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(3);
			cell.setCellValue(egovMap.get("goodsName").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(4);
			cell.setCellValue(egovMap.get("orderCost").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(5);
			cell.setCellValue(egovMap.get("orderQty").toString());
			cell.setCellStyle(cellStyle3);
			cell = row.createCell(6);
			cell.setCellValue(egovMap.get("comName").toString());
			cell.setCellStyle(cellStyle3);
		}
		//칼럼 너비 조절
			 //sheet.setColumnWidth(2, (sheet.getColumnWidth(2))+2500);
			 //sheet.setColumnWidth(3, (sheet.getColumnWidth(3))+4500);

		// 입력된 내용을 파일로 쓰기
		File file = new File(fileNm);
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file);
			workbook.write(fos);
		}catch(FileNotFoundException fe){
			fe.printStackTrace();
		} 
		catch (IOException ie) {
			ie.printStackTrace();
		}finally{
			try {
				if(workbook != null) workbook.close();
				if(fos != null) fos.close();
			} catch (IOException ie2) {
				ie2.printStackTrace();
			}
		}
		LOGGER.info("============================================== PcsServiceImpl excelReasonStateList END==============================================");
	}
	
	// 차수별 피킹 완료 결과 엑셀 양식 생성 및 다운로드
	@Override
	public void excelPickingResultList(List<?> pickHeaderList, String fileNm) throws Exception{
		
		LOGGER.info("============================================== PcsServiceImpl excelPickingResultList START==============================================");
		
		// 워크북 생성
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 워크시트 생성
		XSSFSheet sheet = workbook.createSheet("Sheet1");
		
		// 헤더 부
		XSSFCellStyle cellStyle = workbook.createCellStyle();			
		cellStyle.setAlignment(cellStyle.ALIGN_CENTER); // 가운데 정렬
		
		
		// 폰트
		Font font = workbook.createFont();
		font.setBold(true);
		cellStyle.setFont(font);
		
		// 파란색
		XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setFillForegroundColor(new XSSFColor(new java.awt.Color(197, 217, 241))); // 색깔 설정
		cellStyle1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); // 색 패턴을 반드시 설정해야 색깔이 셀이 들어감
		
		// 주황색
		XSSFCellStyle cellStyle2 = workbook.createCellStyle();
		cellStyle2.setFillForegroundColor(new XSSFColor(new java.awt.Color(242, 157, 9))); // 색깔 설정
		cellStyle2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); // 색 패턴을 반드시 설정해야 색깔이 셀이 들어감
		
		// 테두리 설정
		cellStyle1.setBorderTop(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderLeft(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderRight(cellStyle1.BORDER_THIN);
		cellStyle1.setBorderBottom(cellStyle1.BORDER_THIN);
		
		cellStyle1.setAlignment(cellStyle1.ALIGN_LEFT); // 왼쪽 정렬
		
		cellStyle1.setFont(font);
		
		
		// 테두리 설정
		cellStyle2.setBorderTop(cellStyle1.BORDER_THIN);
		cellStyle2.setBorderLeft(cellStyle1.BORDER_THIN);
		cellStyle2.setBorderRight(cellStyle1.BORDER_THIN);
		cellStyle2.setBorderBottom(cellStyle1.BORDER_THIN);

		cellStyle2.setAlignment(cellStyle1.ALIGN_LEFT); // 왼쪽 정렬

		cellStyle2.setFont(font);
		
		// 데이타 부
		XSSFCellStyle cellStyle3 = workbook.createCellStyle();
		// 테두리 설정
		cellStyle3.setBorderTop(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderLeft(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderRight(cellStyle3.BORDER_THIN);
		cellStyle3.setBorderBottom(cellStyle3.BORDER_THIN);
		
		cellStyle3.setAlignment(cellStyle3.ALIGN_LEFT); // 가운데 정렬
		
		
		// 행 생성
		XSSFRow row1 = sheet.createRow(0);
		XSSFRow row = sheet.createRow(3);
		// 셀 생성
		XSSFCell cell;
		
		// 헤더 제목 구성
		cell = row1.createCell(0);
		cell.setCellValue("orrDt");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(1);
		cell.setCellValue("orrNo");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(2);
		cell.setCellValue("orrDtlSqno");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(3);
		cell.setCellValue("naWrsC");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(4);
		cell.setCellValue("wrsNm");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(5);
		cell.setCellValue("orrQt");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(6);
		cell.setCellValue("wrsAltRqrYn");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(7);
		cell.setCellValue("dqpdYn");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(8);
		cell.setCellValue("dqpdRsnC");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(9);
		cell.setCellValue("dqpdTotQt");
		cell.setCellStyle(cellStyle2);
		cell = row1.createCell(10);
		cell.setCellValue("wrsBcd");
		cell.setCellStyle(cellStyle1);
		cell = row1.createCell(11);
		cell.setCellValue("wrsStd");
		cell.setCellStyle(cellStyle1);
		cell = row1.createCell(12);
		cell.setCellValue("altTotQt");
		cell.setCellStyle(cellStyle1);
		cell = row1.createCell(13);
		cell.setCellValue("altWrsNm");
		cell.setCellStyle(cellStyle1);
		cell = row1.createCell(14);
		cell.setCellValue("altAm");
		cell.setCellStyle(cellStyle1);
		cell = row1.createCell(15);
		cell.setCellValue("altQt");
		cell.setCellStyle(cellStyle1);
		cell = row1.createCell(16);
		cell.setCellValue("altWrsBcd");
		cell.setCellStyle(cellStyle1);
		cell = row1.createCell(17);
		cell.setCellValue("altWrsStd");
		cell.setCellStyle(cellStyle1);
		
		// 셀병합 영역 생성
		//sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
		
		// 헤더 정보 구성		
		cell = row.createCell(0);
		cell.setCellValue("주문일자"); // 주문일자
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(1);
		cell.setCellValue("주문번호"); // 주문번호
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(2);
		cell.setCellValue("주문순번"); // 주문순번
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(3);
		cell.setCellValue("경통상품코드"); // 경통상품코드
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(4);
		cell.setCellValue("상품명"); // 상품명
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(5);
		cell.setCellValue("주문수량"); // 주문수량
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(6);
		cell.setCellValue("상품대체요청여부"); // 상품대체요청여부
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(7);
		cell.setCellValue("결품여부"); // 결품여부
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(8);
		cell.setCellValue("결품사유코드"); // 결품사유코드
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(9);
		cell.setCellValue("결품전체수량"); // 결품전체수량
		cell.setCellStyle(cellStyle2);
		cell = row.createCell(10);
		cell.setCellValue("실피킹바코드"); // 실피킹바코드
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(11);
		cell.setCellValue("실피킹바코드규격"); // 실피킹바코드규격
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(12);
		cell.setCellValue("대체전체수량"); // 대체전체수량
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(13);
		cell.setCellValue("대체상품명"); // 대체상품명
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(14);
		cell.setCellValue("대체상품가격"); // 대체상품가격
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(15);
		cell.setCellValue("대체수량"); // 대체수량
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(16);
		cell.setCellValue("대체실피킹바코드"); // 대체실피킹바코드
		cell.setCellStyle(cellStyle1);
		cell = row.createCell(17);
		cell.setCellValue("대체실피킹바코드규격"); // 대체실피킹바코드규격
		cell.setCellStyle(cellStyle1);
		
		try{
		// 리스트의 사이즈 만큼 row 생성
		EgovMap egovMap;
		for(int i=0; i < pickHeaderList.size(); i++){
			egovMap = (EgovMap) pickHeaderList.get(i);
			System.out.println("========================egovMap========================" + egovMap);
			// 행 생성
			int j = 4+i;
			row = sheet.createRow(j);
			
			// 조회된 값 각 셀에 넣기
			/*cell = row.createCell(0);
			// 조회한 번호가 double type이라서 형 변환 시도 함
			Double douVal = Double.parseDouble(egovMap.get("rowNumber").toString());
			int intVal = Integer.parseInt(String.valueOf(Math.round(douVal)));
			String StrVal = Integer.toString(intVal);			
			cell.setCellValue(StrVal);
			cell.setCellStyle(cellStyle3);*/
			
			
			cell = row.createCell(0);
			cell.setCellValue(egovMap.get("orderDate").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(1);
			cell.setCellValue(egovMap.get("orderNo").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(2);
			cell.setCellValue(egovMap.get("orderRowNo").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(3);
			cell.setCellValue(egovMap.get("goodsCode").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(4);
			cell.setCellValue(egovMap.get("goodsName").toString());
			cell.setCellStyle(cellStyle3);
		
			cell = row.createCell(5);
			cell.setCellValue(egovMap.get("orderQty").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(6);
			cell.setCellValue(egovMap.get("changeAllowYn").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(7);
			cell.setCellValue(egovMap.get("reasonYn").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(8);
			cell.setCellValue(egovMap.get("soldOutReason").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(9);
			cell.setCellValue(egovMap.get("reasonQty").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(10);
			cell.setCellValue(egovMap.get("scanGoodsCode").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(11);
			cell.setCellValue(egovMap.get("goodsWeight").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(12);
			cell.setCellValue(egovMap.get("changePickQty").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(13);
			cell.setCellValue(egovMap.get("changeGoodsName").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(14);
			cell.setCellValue(egovMap.get("changeGoodsCost").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(15);
			cell.setCellValue(egovMap.get("changePickQty").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(16);
			cell.setCellValue(egovMap.get("changeGoodsCode").toString());
			cell.setCellStyle(cellStyle3);
			
			cell = row.createCell(17);
			cell.setCellValue(egovMap.get("changeGoodsWeight").toString());
			cell.setCellStyle(cellStyle3);
			
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// 180803 KBK 엑셀 2,3행 빈칸 0으로 채움
		XSSFRow blank = null;
		for(int i=1; i<3; i++){
			blank = sheet.createRow(i);
			for(int j=0; j < row.getLastCellNum(); j++){
				cell = blank.createCell(j);
				cell.setCellValue("0"); 
			}
		}
		
		// 칼럼 너비 조절
		sheet.setColumnWidth(3, (sheet.getColumnWidth(2))+2000);  // 경통상품코드
		sheet.setColumnWidth(4, (sheet.getColumnWidth(2))+6500);  // 상품명
		sheet.setColumnWidth(6, (sheet.getColumnWidth(2))+2000);  // 상품대체요청여부
		sheet.setColumnWidth(8, (sheet.getColumnWidth(2))+1000);  // 결품사유코드
		sheet.setColumnWidth(9, (sheet.getColumnWidth(2))+1000);  // 결품전체수량
		sheet.setColumnWidth(10, (sheet.getColumnWidth(2))+4500); // 실피킹바코드
		sheet.setColumnWidth(11, (sheet.getColumnWidth(2))+2000); // 실피킹바코드규격
		sheet.setColumnWidth(12, (sheet.getColumnWidth(2))+1000); // 대체전체수량
		sheet.setColumnWidth(13, (sheet.getColumnWidth(2))+6500); // 대체상품명
		sheet.setColumnWidth(14, (sheet.getColumnWidth(2))+1000); // 대체상품가격
		sheet.setColumnWidth(15, (sheet.getColumnWidth(2))+400);  // 대체수량
		sheet.setColumnWidth(16, (sheet.getColumnWidth(2))+4500); // 대체실피킹바코드
		sheet.setColumnWidth(17, (sheet.getColumnWidth(2))+3000); // 대체실피킹바코드규격
		
		// 입력된 내용을 파일로 쓰기
		File file = new File(fileNm);
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file);
			workbook.write(fos);
		}catch(FileNotFoundException fe){
			fe.printStackTrace();
			throw new EgovBizException("다른 프로세스가 파일을 사용 중이기 때문에 프로세스가 액세스 할 수 없습니다");
		} 
		catch (IOException ie) {
			ie.printStackTrace();
		}finally{
			try {
				if(workbook != null) workbook.close();
				if(fos != null) fos.close();
			} catch (IOException ie2) {
				ie2.printStackTrace();
			}
			
		}
		LOGGER.info("============================================== PcsServiceImpl excelPickingResultList END==============================================");
	}

	
	// 피킹지시 - 주문수량 구하기
//	@Override
//	public int selectPickingOrderQty(DefaultVO searchVO) throws Exception {
//		return pcsDAO.selectPickingOrderQty(searchVO);
//	}

	// 피킹현황조회 - 결품여부 표시
//	@Override
//	public String selectReasonYn(DefaultVO searchVO) throws Exception {
//		return pcsDAO.selectReasonYn(searchVO);
//	}

	// 피킹현황조회 - 대체여부 표시
//	@Override
//	public String selectChangeYn(DefaultVO searchVO) throws Exception {
//		return pcsDAO.selectChangeYn(searchVO);
//	}


	



	
}
