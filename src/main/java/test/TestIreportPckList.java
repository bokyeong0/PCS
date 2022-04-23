package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vertexid.nhmarket.pcs.service.ReportPrePckVO;

public class TestIreportPckList {

	public static void main(String[] args) throws Exception {

		// jaspersoft iReport 툴로 만든 jrxml 파일의 경로 및 파일명
		String jrxmlFileName = "c:/ireport/prePickingList.jrxml";
		// jrxmlFileName의 파일이 컴파일 되어 생성될 jasper 파일의 경로 및 파일명
		String jasperFileName = "c:/ireport/prePickingList.jasper";
		// jasperFileName이 생성할 pdf 파일의 경로 및 파일명
		String destFileName = "c:/ireport/prePickingList.pdf";

		// .jrxml 파일을 .jasper 파일로 컴파일
		JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

		ArrayList<ReportPrePckVO> dataList = new ArrayList<>();
		
		int or_cost = 5700;
		for (int i = 1; i < 30; i++) {
			ReportPrePckVO itemVO = new ReportPrePckVO();
			itemVO.setZone_code("AA");
			itemVO.setDelivery_date("16.12.09");
			itemVO.setDelivery_count("1");
			itemVO.setShort_order_no("00" + i);
			itemVO.setCustomer_name("홍*동");
			itemVO.setGoods_code("8801073102545");
			itemVO.setGoods_name("브랜드 목살 /구이용/8,700→5,700/1회 2팩");
			itemVO.setGoods_option_name("구이용");
			itemVO.setGoods_spec("600g/팩");
			itemVO.setOrder_qty(i);
			itemVO.setOrder_cost(or_cost*i);
			itemVO.setDelivery_message("고기 육질이 좋은 것");
			itemVO.setChange_allow_yn("대체");
//			itemVO.setDelivery_message_arrow("->");

			dataList.add(itemVO);

		}

		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);

		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("rootPath", "C:/ireport"); // 파일경로

		try {
			/*
			 * .jasper, params, beanColDataSource 내용을 읽어서 .jrprint 파일로 생성함 printFileName에 찍히는 파일명은 .jrxml 파일의 Report name 이다
			 */
			String printFileName = JasperFillManager.fillReportToFile(jasperFileName, params, beanColDataSource);

			if (printFileName != null) {

				System.out.println("printFileName : " + printFileName);

				// .jrprint 파일의 내용을 pdf 파일로 변환
				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);

			}
		} catch (JRException e) {
			e.printStackTrace();
		}

		System.out.println("end");
	}

}
