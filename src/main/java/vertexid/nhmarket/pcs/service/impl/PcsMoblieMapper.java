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

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import vertexid.nhmarket.pcs.service.ComCodeVO;
import vertexid.nhmarket.pcs.service.PickHeaderVO;
import vertexid.nhmarket.pcs.service.PickMobileDetailVO;
import vertexid.nhmarket.pcs.service.PickTotalVO;
import vertexid.nhmarket.pcs.service.PickingStateVO;

@Mapper("pcsMobileMapper")
public interface PcsMoblieMapper {

	int updateStartPickHeader(PickingStateVO pickingStateVO);

	List<PickMobileDetailVO> selectMappingDetailList(ArrayList<String> pickIdList);

	List<PickHeaderVO> selectStateHeaderList(PickingStateVO pickingStateVO);

	List<ComCodeVO> selectComGroupCodeList(ArrayList<String> comGroupCodeList);

	int updateEndPickHeader(String pick_id);

	int updateEndPickDetail(PickMobileDetailVO pickMobileDetailVO);

	int updatePickCancel(ArrayList<String> pickIdList);

	int updateComCode(ComCodeVO comCodeVO);

	PickHeaderVO selectHeader(String pickId);

	int selectHeaderComplete(Map<String, Object> pickMobileDetailMap);

	List<PickTotalVO> selectTotalList(Map<String, Object> pickMobileTotalMap);

	List<PickMobileDetailVO> selectOrderInfo(ArrayList<String> pick_ids);

	ArrayList<String> printPickIds();
	
	int updatePrintYes(Map<String, Object> pickMobileDetailMap);
	
	int insertPrintFileData(Map<String, Object> map);
	
	int updatePrintFileData(Map<String, Object> map);
	
	Map<String, Object> selectPrintFileData();
	
	int selectPrintFileInProgress();
	
	int deletePrintFileData(String today);
	
	int updatePrintFileFailData();
	
	int alterPrintFileIdReset();
	
	int selectPrintFileDataOrderNo(Map<String, Object> map);
	
	int updatePrintFileError();
}
