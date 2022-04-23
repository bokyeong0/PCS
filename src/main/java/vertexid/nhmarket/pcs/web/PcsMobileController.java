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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import vertexid.nhmarket.pcs.service.ComCodeVO;
import vertexid.nhmarket.pcs.service.DefaultJsonVO;
import vertexid.nhmarket.pcs.service.LoginVO;
import vertexid.nhmarket.pcs.service.PcsMobileService;
import vertexid.nhmarket.pcs.service.PcsService;
import vertexid.nhmarket.pcs.service.PickCheckVO;
import vertexid.nhmarket.pcs.service.PickMobileDetailVO;
import vertexid.nhmarket.pcs.service.PickingStateVO;

@RestController
public class PcsMobileController {
	
	// Logger 생성용
	private static final Logger LOGGER = LoggerFactory.getLogger(PcsMobileController.class);

	@Resource(name = "pcsService")
	private PcsService pcsService;

	@Resource(name = "pcsMobileService")
	private PcsMobileService pcsMobileService;

	/**
	 * 공통코드 리스트
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/comGroupCodeList.do", method = RequestMethod.POST)
	public List<ComCodeVO> comGroupCodeList(@RequestBody ArrayList<String> comGroupCodeList) throws Exception {
		LOGGER.info("comCodeList Start!!!");
		for (String string : comGroupCodeList) {
			LOGGER.info("comCodeList : " + string);
		}

		return pcsMobileService.selectComGroupCodeList(comGroupCodeList);
	}

	/**
	 * 피킹 시작
	 * 
	 * @param pickingStateVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pickingList.do", method = RequestMethod.POST)
	public PickCheckVO pickingList(@RequestBody PickingStateVO pickingStateVO) throws Exception {

		LOGGER.info("pickingList.do : " + pickingStateVO);

		return pcsMobileService.pickingList(pickingStateVO);
	}

	/**
	 * 작업 취소
	 * 
	 * @param pickMobileDetailVOs
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pickingCancel.do", method = RequestMethod.POST)
	public DefaultJsonVO pickingCancel(@RequestBody ArrayList<String> pickIdList) throws Exception {
		LOGGER.info("pickingCancel");
		for (String string : pickIdList) {
			LOGGER.info("pickIdList : " + string);
		}

		return pcsMobileService.pickingCancel(pickIdList);
	}

	/**
	 * 작업 완료
	 * 
	 * @param pickMobileDetailVOs
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pickingComplete.do", method = RequestMethod.POST)
	public DefaultJsonVO pickingComplete(@RequestBody ArrayList<PickMobileDetailVO> pickMobileDetailVOs, HttpServletRequest request) throws Exception {
		
		LOGGER.info("****** pickingComplete START ******");
		return pcsMobileService.pickingComplete(pickMobileDetailVOs);
	}

	/**
	 * 로그인
	 * 
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public DefaultJsonVO login(@RequestBody LoginVO loginVO) throws Exception {
		LOGGER.info("login " + loginVO);

		return pcsMobileService.login(loginVO);
	}

	/**
	 * 로그아웃
	 * 
	 * @param pickMobileDetailVOs
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout.do", method = RequestMethod.POST)
	public DefaultJsonVO logout(@RequestBody LoginVO loginVO) throws Exception {
		LOGGER.info("logout " + loginVO);

		return pcsMobileService.logout(loginVO);
	}

	/**
	 * 업로드 페이지
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload.do", method = RequestMethod.GET)
	public ModelAndView upload(ModelMap model, HttpServletRequest request) throws Exception {
		// LOGGER.info("upload");

		// 버전 조회 처리
		ComCodeVO searchComCodeVO = new ComCodeVO();
		searchComCodeVO.setCom_group_code("MOBILE_VERSION");
		searchComCodeVO.setCom_code("0");
		List<ComCodeVO> codeList = pcsService.selectComCodeList(searchComCodeVO);
		String version = "";
		if (codeList.size() > 0) {
			version = codeList.get(0).getCom_sub_code();
		}
		model.addAttribute("version", version);

		return new ModelAndView("upload", model);
	}

	/**
	 * apk 파일 업로드
	 * 
	 * @param version
	 *            버전 정보
	 * @param passwd
	 *            비밀번호 체크
	 * @param multipartRequest
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST)
	public ModelAndView fileUpload(String version, String passwd, MultipartHttpServletRequest multipartRequest, ModelMap model) throws Exception {
		// LOGGER.info("fileUpload");
		// LOGGER.info("fileUpload version : " + version);
		// LOGGER.info("fileUpload passwd : " + passwd);

		if (passwd != null && !passwd.equals("#1234")) {
			model.addAttribute("resultErrorMsg", "비밀번호가 맞지 않습니다.");
			return new ModelAndView("cmmn/egovError");
		}

		// apk 경로
		String uploadPath = "C:\\vertexid\\apk";

		LOGGER.info("path : " + uploadPath);

		// String uploadPath = "";
		String uploadFileName = "";
		String fileName = "";

		// 파일 처리
		File dir = new File(uploadPath);
		// 디렉토리 체크 생성
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}

		// 파일 업로드
		Iterator<String> iter = multipartRequest.getFileNames();

		String size = "";

		while (iter.hasNext()) {
			uploadFileName = iter.next();
			MultipartFile mFile = multipartRequest.getFile(uploadFileName);
			fileName = mFile.getOriginalFilename();
			File file = new File(uploadPath + File.separator + "pcs.apk");
			if (fileName != null && !fileName.equals("")) {
				mFile.transferTo(file);
				size = String.valueOf(mFile.getSize());
			}
		}

		// 버전 및 파일용량 업데이트
		ComCodeVO comCodeVO = new ComCodeVO();
		comCodeVO.setCom_group_code("MOBILE_VERSION");
		comCodeVO.setCom_code("0");
		comCodeVO.setCom_sub_code(version);
		comCodeVO.setCom_desc(size);
		int check = pcsMobileService.updateComCode(comCodeVO);

		if (check > 0) {
			return new ModelAndView("redirect:/upload.do");
		} else {
			model.addAttribute("resultErrorMsg", "비밀번호가 맞지 않습니다.");
			return new ModelAndView("cmmn/egovError");
		}

	}

/*	*//**
	 * 테스트 프린트용
	 * 
	 * @param pick_id
	 *            완료 피킹 아이디
	 * @param request
	 *//*
	@RequestMapping(value = "/testPrint.do", method = RequestMethod.GET)
	private void testPrint(String orgOrderNo, HttpServletRequest request) {
		LOGGER.info("testPrint orgOrderNo " + orgOrderNo);

		String rootPath = request.getSession().getServletContext().getRealPath("/");
		LOGGER.info("rootPath : " + rootPath);
		pcsMobileService.testPrint(orgOrderNo, rootPath);

	}*/ // 임시 주석 처리

}
