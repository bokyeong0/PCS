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
package vertexid.nhmarket.pcs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : PcsService.java
 * @Description : PcsService Class
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
public interface PcsService {

	/**
	 * 파일 데이터를 DB에 등록 처리 한다. (피킹지시 - 주문정보 업로드)
	 * 
	 * @param vo - 등록할 정보가 담긴 OrderVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	int insertImportData(MultipartHttpServletRequest multipartRequest, DefaultVO searchVo) throws Exception;
		
	/**
	 * 오더 정보를 가져온다.
	 * 
	 * @param vo
	 *            - 등록할 정보가 담긴 OrderVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	List<?> selectOrderList(DefaultVO searchVO) throws Exception;
	EgovMap orderCnt(DefaultVO searchVO) throws Exception;
	String orderItemCnt(DefaultVO searchVO) throws Exception;

	int updateLabelPrint(DefaultVO searchVO) throws Exception;

	int updateChoiceLabelPrint(PickHeaderVO pickHeaderVO) throws Exception;

	int insertPicking(DefaultVO searchVO) throws Exception;

	List<?> selectDeliveryCountList(DefaultVO searchVO) throws Exception;

	List<?> selectZoneCodeList(DefaultVO searchVO) throws Exception;

	int selectLabelPrintCount(DefaultVO searchVO) throws Exception;

	int selectLastDeliveryCount(DefaultVO searchVO) throws Exception;

	List<ComCodeVO> selectComCodeList(ComCodeVO comCodeVO) throws Exception;

	List<?> selectPickHeaderList(DefaultVO searchVO);

	List<?> selectPickDetailList(String searchPickId);

	List<?> selectChangePickList(DefaultVO searchVO);

	List<?> selectJobTotalInfo(DefaultVO searchVO);

	List<?> selectJobStateList(DefaultVO searchVO);

	int selectPickLastDeliveryCount(DefaultVO searchVO) throws Exception;

	List<?> selectPickDeliveryCountList(DefaultVO searchVO) throws Exception;

	List<?> selectPickZoneCodeList(DefaultVO searchVO) throws Exception;

	List<?> selectStateCodeList(ComCodeVO setCom_sub_code) throws Exception;

	List<?> selectReasonStateList(DefaultVO searchVO);
	
	List<?> selectPickingResultList(DefaultVO searchVO);
	List<Map<String, String>> selectChangePickOrderList(DefaultVO searchVO);

	List<?> selectALLComCodeList(DefaultVO searchVO);

	int updateComCodeGroup(ArrayList<EgovMap> comCodeList);

	int updateComCode(ArrayList<EgovMap> comCodeList);

	List<?> selectJobStateCode();

	void pdfPrint(String destPDFFileName) throws Exception;
	
	String getZplHostStatus();

	List<?> selectPrePckListPr(DefaultVO searchVO, String rootPath);
	
//	List<?> selectPrePckListInq(DefaultVO searchVO, String rootPath);
	
	List<?> selectPrintHeaderList(DefaultVO searchVO);
	
	List<?> nextDeliveryCountList(DefaultVO searchVO) throws Exception;
	
	void excelChangeStateList(List<?> changePickList, String fileNm) throws Exception;

	void excelReasonStateList(List<?> changePickList, String fileNm) throws Exception;
	
	void excelPickingResultList(List<?> pickHeaderList, String fileNm) throws Exception;

}
