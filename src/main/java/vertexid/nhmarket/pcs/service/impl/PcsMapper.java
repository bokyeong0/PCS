/*
 * Copyright 2011 MOPAS(Ministry of Public Administration and Security).
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vertexid.nhmarket.pcs.service.ComCodeVO;
import vertexid.nhmarket.pcs.service.DefaultVO;
import vertexid.nhmarket.pcs.service.OrderVO;
import vertexid.nhmarket.pcs.service.PickDetailVO;
import vertexid.nhmarket.pcs.service.PickHeaderVO;
import vertexid.nhmarket.pcs.service.ReportPrePckVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * sample에 관한 데이터처리 매퍼 클래스
 *
 * @author  표준프레임워크센터
 * @since 2014.01.24
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *
 *          수정일          수정자           수정내용
 *  ----------------    ------------    ---------------------------
 *   2014.01.24        표준프레임워크센터          최초 생성
 *
 * </pre>
 */
@Mapper("pcsMapper")
public interface PcsMapper {
	
	/**
	 * 피킹지시 Order 목록 조회
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	List<EgovMap> selectOrderList(DefaultVO searchVO);
	EgovMap orderCnt(DefaultVO searchVO);
	String orderItemCnt(DefaultVO searchVO);
	
	/**
	 * 피킹지시2차 Order 목록 조회 (Zone 기준)
	 * @param searchVO
	 * @return
	 * @throws Exception
	 */
	List<EgovMap> selectOrder2ndList(DefaultVO searchVO);
	
	/**
	 * 엑셀파일 오더 테이블 등록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int insertImportData(OrderVO vo) throws Exception;

	int updateLabelPrint(EgovMap egovMap);

	int insertChoicePicking(DefaultVO searchVO);

	int updateChoiceLabelPrint(DefaultVO searchVO);

	/**
	 * 최근 pickId 조회 
	 * @return
	 */
	String selectLastPickId();

	int updateStateCode(DefaultVO searchVO);

	int insertPickHeder(PickHeaderVO pickHeaderVO);
	
	int insertPickDetail(PickDetailVO pickDetailVO);

	List<EgovMap> selectLablePrintingList(DefaultVO searchVO);
	List<EgovMap> selectLablePrintingListOrder(DefaultVO searchVO); // 주문별 라벨출력
	int updateLabelStateDelivery(PickHeaderVO pickHeaderVO) throws Exception; // 권역별 존별 라벨상태 업데이트
	
	List<EgovMap> selectChoiceLablePrintingList(PickHeaderVO pickHeaderVO);

	List<EgovMap> selectPickHeader(DefaultVO searchVO);
	
	int selectPickHeaderCount(DefaultVO searchVO);	
	
	List<EgovMap> selectPickDetail(String searchPickId);

	List<EgovMap> selectPickOrderList(DefaultVO searchVO);

	List<EgovMap> selectDeliveryCountList(DefaultVO searchVO) throws Exception;

	List<EgovMap> selectZoneCodeList(DefaultVO searchVO) throws Exception;
	
	List<EgovMap> selectPickDeliveryCountList(DefaultVO searchVO) throws Exception;
	
	List<EgovMap> selectPickZoneCodeList(DefaultVO searchVO) throws Exception;

	List<EgovMap> selectChoiceOrderList(DefaultVO orderKeyList) throws Exception;

	int deleteImportData(DefaultVO searchVO) throws Exception;

	int updateLabelPrintCount(EgovMap egovMap) throws Exception;

	int selectLabelPrintCount(DefaultVO searchVO) throws Exception;

	int selectLastDeliveryCount(DefaultVO searchVO) throws Exception;

	List<ComCodeVO> selectComCodeList(ComCodeVO comCodeVO);

	int selectTrolleyId() throws Exception;

	List<?> selectChangePickList(DefaultVO searchVO);

	List<?> selectJobTotalInfo(DefaultVO searchVO);

	List<?> selectJobStateList(DefaultVO searchVO);

	int selectPickLastDeliveryCount(DefaultVO searchVO);

	List<?> selectStateCodeList(ComCodeVO comCodeVO);

	List<?> selectReasonStateList(DefaultVO searchVO);
	
	List<?> selectPickingResultList(DefaultVO searchVO);
	List<Map<String, String>> selectChangePickOrderList(DefaultVO searchVO);

	List<ComCodeVO> selectALLComCodeList(DefaultVO searchVO);

	int updateComCodeGroup(EgovMap comCode);

	int insertComCodeGroup(EgovMap comCode);

	int updateComCode(EgovMap comCode);

	int insertComCode(EgovMap comCode);

	List<?> selectJobStateCode();
	
	ArrayList<ReportPrePckVO> selectPrePckList(DefaultVO searchVO);
	
	List<EgovMap> selectPrintHeader(DefaultVO searchVO);

//	ArrayList<PickHeaderVO> selectOrgOrderNoPrint();
	
//	int selectHeaderCompletePrint(Map<String, Object> callNoPrintMap);
	
	List<EgovMap> nextDeliveryCountList(DefaultVO searchVO) throws Exception;
	
	int checkOrderInfo(OrderVO orderVO);
	
}
