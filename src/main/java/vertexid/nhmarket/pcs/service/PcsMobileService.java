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

public interface PcsMobileService {

	List<ComCodeVO> selectComGroupCodeList(ArrayList<String> comGroupCodeList);

	DefaultJsonVO pickingComplete(ArrayList<PickMobileDetailVO> pickMobileDetailVOs);

	PickCheckVO pickingList(PickingStateVO pickingStateVO);

	DefaultJsonVO pickingCancel(ArrayList<String> pickIdList);

	int updateComCode(ComCodeVO comCodeVO);

	DefaultJsonVO login(LoginVO loginVO);

	DefaultJsonVO logout(LoginVO loginVO);

	ArrayList<String> printPickIds();
	
	void pdfPrint(Map<String, Object> map) throws Exception;
	
	public void checkPrint(ArrayList<String> pick_ids, int prinType);

	public void checkPrintSch(PickHeaderVO orgOrderNoNew);

	public Map<String, Object> selectPrintFileData();
	
	int selectPrintFileInProgress();
	
	int deletePrintFileData(String today);
	
	int updatePrintFileFailData();
	
	int alterPrintFileIdReset();
	
	int updatePrintFileError();
}
