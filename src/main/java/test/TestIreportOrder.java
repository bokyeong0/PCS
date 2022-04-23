package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vertexid.nhmarket.pcs.service.ReportItemVO;

public class TestIreportOrder {

	public static void main(String[] args) throws Exception {

		// jaspersoft iReport 툴로 만든 jrxml 파일의 경로 및 파일명
		String jrxmlFileName = "c:/ireport/confirmationOrder.jrxml";
		// jrxmlFileName의 파일이 컴파일 되어 생성될 jasper 파일의 경로 및 파일명
		String jasperFileName = "c:/ireport/confirmationOrder.jasper";
		// jasperFileName이 생성할 pdf 파일의 경로 및 파일명
		String destFileName = "c:/ireport/confirmationOrder.pdf";

		// .jrxml 파일을 .jasper 파일로 컴파일
		JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);

		int order_qty_sum = 1; // 주문수량
		int order_cost_sum = 2; // 주문합계
		int sold_out_qty_sum = 3; // 결품수량
		int change_pick_qty_sum = 4; // 대체수량
		int pick_qty_sum = 5; // 배송수량

		// 주문합계
		int sold_out_cost_sum = 70000; // 결품 합계
		int pos_cost_sum = 83000; // POS(외상매출) 합계
		int min_sum = 2000; // 대체 차액 합계
		
//		String e_pay_cost = "0"; // e-주문금액
//		String dlvry_pay_cost = "0"; // 택배주문금액
//		String e_sent_fee = "0"; // e-배송비
//		String dlvry_sent_fee = "0"; // 택배배송비
//		String e_card_dc = "0"; // e-카드즉시할인
//		String dlvry_card_dc = "0"; // 택배카드즉시할인
//		String coupon_dc = "0"; // 쿠폰할인
//		String pay_cost = "0"; // 결제금액

		String e_pay_cost = "57000"; // e-주문금액
		String dlvry_pay_cost = "30000"; // 택배주문금액
		String e_sent_fee = "300"; // e-배송비
		String dlvry_sent_fee = "3000"; // 택배배송비
		String e_card_dc = "2000"; // e-카드즉시할인
		String dlvry_card_dc = "1000"; // 택배카드즉시할인
		String coupon_dc = "3000"; // 쿠폰할인
		String pay_cost = "86000"; // 결제금액
		
		int order_pay_sum = 0; // 주문금액합계
		int delivery_pay_sum = 0; // 배송비합계
		int card_dc_sum = 0; // 카드즉시할인합계

		ArrayList<ReportItemVO> dataList = new ArrayList<>();
		
		for (int i = 0; i < 2; i++) {
			//i += i+1;
			ReportItemVO itemVO = new ReportItemVO();
			itemVO.setRow(i+1+""); // 순번
			itemVO.setGoods_name("[한우][냉장]다짐육/1등급/300g±"); //상품명
			itemVO.setOrder_cost(5000); // 판매단가			
			itemVO.setOrder_qty(2); // 주문수량
			itemVO.setOrder_cost_total(20000); //금액
			itemVO.setSold_out_qty(1);// 결품
			itemVO.setChange_pick_qty(1);// 대체
			itemVO.setPick_qty(1);// 배송
			itemVO.setGoods_code("2100009776970");// 상품코드

			// 주문 합계 구하기
			order_cost_sum += itemVO.getOrder_cost();
			order_qty_sum += itemVO.getOrder_qty();
			sold_out_qty_sum += itemVO.getSold_out_qty();
			change_pick_qty_sum += itemVO.getChange_pick_qty();
			pick_qty_sum += itemVO.getPick_qty();

			dataList.add(itemVO);

			// TODO 대체 처리 해야함
			if (1 > 0) { // 수정
				ReportItemVO _itemVO = new ReportItemVO();

				_itemVO.setRow(i + 1 + "\n부분결품및대체"); // 순번
//				_itemVO.setLck_knd(_itemVO.LCK_KND_LACK); // 결품
//				_itemVO.setLck_knd(_itemVO.LCK_KND_PARK_LACK); // 부분결품
				_itemVO.setLck_knd(_itemVO.LCK_KND_REPLACE); // 대체
				_itemVO.setGoods_name("[한우][냉장]다짐육/1등급/300g±"); //상품명
				_itemVO.setOrder_cost(5000); // 판매단가			
				_itemVO.setOrder_qty(2); // 주문수량
				_itemVO.setOrder_cost_total(20000); //금액
				_itemVO.setSold_out_qty(1);// 결품
				_itemVO.setChange_pick_qty(1);// 대체
				_itemVO.setPick_qty(1);// 배송
				_itemVO.setGoods_code("01234567890");// 상품코드

				dataList.add(_itemVO);
			}
		}

		// 주문금액합계 = e-주문금액+택배주문금액
		order_pay_sum = Integer.parseInt(e_pay_cost) + Integer.parseInt(dlvry_pay_cost);
		// 배송비합계 = e-배송비+택배_배송비
		delivery_pay_sum = Integer.parseInt(e_sent_fee) + Integer.parseInt(dlvry_sent_fee);
		// 카드즉시할인합계 = e-카드즉시할인+택배_카드즉시할인
		card_dc_sum = Integer.parseInt(e_card_dc) + Integer.parseInt(dlvry_card_dc);
				
		// 메모리에 저장한 javaBean 객체를 Array나 Collections로 사용
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);

		// Map 객체를 사용하여 key-value 쌍으로 데이터 맵핑
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("spc_knd", "2"); // 거래명세서 종류 (1:기본, 2:식류품)
		
		/** 탑 */
		params.put("short_order_no", "002"); // 단축주문번호
		params.put("delivery_area_name", "고양시 일산동구"); // 배송구명
		
		/** 주문/배송정보 */
		params.put("order_customer_name", "주문고객"); // 주문고객
		params.put("org_order_no", "주문번호"); // 주문번호
		params.put("order_date", "2016.10.26 13:30 ~ 15:30"); // 주문일자
		params.put("customer_name", "받는고객"); // 받는고객
		params.put("tel_no_1", "연락처1"); // 연락처1
		params.put("tel_no_2", "연락처2"); // 연락처2
		params.put("delivery_amount", "무료"); // 배송비
		
		params.put("change_allow_yn", "동의"); // 대체여부

		params.put("address", "서울특별시 은평구 통일로 796 불광동북한산 힐스테이 북한산일스테이트7차아파트 109동 1702호"); // 배송주소
		params.put("free_gift_name", "사은품"); // 사은품
		params.put("delivery_message", "1.고등어 1마리는 두토막으로 조림용. 1마리는 자르지말고 구이용으로 소금 아주 살짝. 2.치즈는 아무거나 원플러스원으로 주세요."); // 배송메시지
		params.put("pay_method", "신용카드, NH카드사은품포인트"); // 결제방법
		
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
		
		params.put("order_pay_sum", order_pay_sum); // 주문금액합계
		params.put("delivery_pay_sum", delivery_pay_sum); // 배송비합계
		params.put("card_dc_sum", card_dc_sum); // 카드즉시할인합계

		params.put("etc", "이것도 없음~"); // 기타
		params.put("rootPath", "C:/ireport"); // 파일경로
		/** A20170724 osj 반품주소와 연락처 추가 */
		params.put("cancel_addr","서울특별시 서초구 청계산로 10 온라인센터(양재)");
		params.put("custom_center","02-529-8221)");

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
