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

import java.io.Serializable;
import java.util.Arrays;

/**
 * @Class Name : SampleDefaultVO.java
 * @Description : SampleDefaultVO Class
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
public class DefaultVO implements Serializable {

	/**
	 * serialVersion UID
	 */
	private static final long serialVersionUID = -858838578081269359L;

	/*
	private int pageIndex = 1;
	private int pageUnit = 10;
	private int pageSize = 10;
	private int firstIndex = 1;
	private int lastIndex = 1;
	private int recordCountPerPage = 10;
	 */
	
	/** search 컬럼 */
	private String search_delivery_date;      // 배송일
	private String search_delivery_count;     // 배송차수
	private String search_zone_code;          // 존코드
	private String search_state_code;         // 진행상태(0:피킹예정,1:피킹지시,2:피킹완료,3:피킹확정)
	private String search_order_date;         // 주문일자
	private String search_order_no;           // 주문번호
	private String search_goods_name;         // 상품명
	private String search_short_order_no;     // 단축주문번호
	private String search_worker_id;          // 피커명
	private String search_delivery_area_name; // 권역구명
	private String search_form;               // 다중 검색을 위한 폼
	private String search_org_order_no;       // 원주문번호
	
	private String search_condition_type;     // A20180111 LJM 검색 조건 (단축주문번호, 피커명, 상품명)
	
	private String printIp;                   // 프린터 IP
	private int printPort;                    // 프린터 Port
	private String reason_yn;                 // 결품여부
	private String nCoupon_yn;                 // N쿠폰 여부 18-09-11

	private String[] order_key;
	
	private int order_qty;                    // 주문수량(수량) 
	
	private String search_com_group_code;     // 검색그룹코드
	private String[] nCouponOrder; //대체수량 있는 주문번호 ()
	private String LabelPrintType; // 라벨프린트 유형 (주문별 라벨출력) 
	
	
	public String getLabelPrintType() {
		return LabelPrintType;
	}
	public void setLabelPrintType(String labelPrintType) {
		LabelPrintType = labelPrintType;
	}
	public String[] getnCouponOrder() {
		return nCouponOrder;
	}
	public void setnCouponOrder(String[] nCouponOrder) {
		this.nCouponOrder = nCouponOrder;
	}
	public String getSearch_delivery_date() {
		return search_delivery_date;
	}
	public void setSearch_delivery_date(String search_delivery_date) {
		this.search_delivery_date = search_delivery_date;
	}
	public String getSearch_delivery_count() {
		return search_delivery_count;
	}
	public void setSearch_delivery_count(String search_delivery_count) {
		this.search_delivery_count = search_delivery_count;
	}
	public String getSearch_zone_code() {
		return search_zone_code;
	}
	public void setSearch_zone_code(String search_zone_code) {
		this.search_zone_code = search_zone_code;
	}
	public String getSearch_state_code() {
		return search_state_code;
	}
	public void setSearch_state_code(String search_state_code) {
		this.search_state_code = search_state_code;
	}
	public String getPrintIp() {
		return printIp;
	}
	public void setPrintIp(String printIp) {
		this.printIp = printIp;
	}
	public int getPrintPort() {
		return printPort;
	}
	public void setPrintPort(int printPort) {
		this.printPort = printPort;
	}
	public String[] getOrder_key() {
		return order_key;
	}
	public void setOrder_key(String[] order_key) {
		this.order_key = order_key;
	}
	public String getSearch_com_group_code() {
		return search_com_group_code;
	}
	public void setSearch_com_group_code(String search_com_group_code) {
		this.search_com_group_code = search_com_group_code;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSearch_goods_name() {
		return search_goods_name;
	}
	public void setSearch_goods_name(String search_goods_name) {
		this.search_goods_name = search_goods_name;
	}
	public String getSearch_short_order_no() {
		return search_short_order_no;
	}
	public void setSearch_short_order_no(String search_short_order_no) {
		this.search_short_order_no = search_short_order_no;
	}
	
	public int getOrder_qty() {
		return order_qty;
	}
	public void setOrder_qty(int order_qty) {
		this.order_qty = order_qty;
	}
	public String getSearch_worker_id() {
		return search_worker_id;
	}
	public void setSearch_worker_id(String search_worker_id) {
		this.search_worker_id = search_worker_id;
	}
	public String getSearch_form() {
		return search_form;
	}
	public void setSearch_form(String search_form) {
		this.search_form = search_form;
	}
	
	public String getSearch_order_date() {
		return search_order_date;
	}
	public void setSearch_order_date(String search_order_date) {
		this.search_order_date = search_order_date;
	}
	public String getSearch_order_no() {
		return search_order_no;
	}
	public void setSearch_order_no(String search_order_no) {
		this.search_order_no = search_order_no;
	}
	public String getReason_yn() {
		return reason_yn;
	}
	public void setReason_yn(String reason_yn) {
		this.reason_yn = reason_yn;
	}
	public String getnCoupon_yn() {
		return nCoupon_yn;
	}
	public void setnCoupon_yn(String nCoupon_yn) {
		this.nCoupon_yn = nCoupon_yn;
	}
	public String getSearch_delivery_area_name() {
		return search_delivery_area_name;
	}
	public void setSearch_delivery_area_name(String search_delivery_area_name) {
		this.search_delivery_area_name = search_delivery_area_name;
	}
	public String getSearch_condition_type() {
		return search_condition_type;
	}
	public void setSearch_condition_type(String search_condition_type) {
		this.search_condition_type = search_condition_type;
	}
	public String getSearch_org_order_no() {
		return search_org_order_no;
	}
	public void setSearch_org_order_no(String search_org_order_no) {
		this.search_org_order_no = search_org_order_no;
	}
	@Override
	public String toString() {
		return "DefaultVO [search_delivery_date=" + search_delivery_date
				+ ", search_delivery_count=" + search_delivery_count
				+ ", search_zone_code=" + search_zone_code
				+ ", search_state_code=" + search_state_code
				+ ", search_order_date=" + search_order_date
				+ ", search_order_no=" + search_order_no
				+ ", search_goods_name=" + search_goods_name
				+ ", search_short_order_no=" + search_short_order_no
				+ ", search_worker_id=" + search_worker_id
				+ ", search_delivery_area_name=" + search_delivery_area_name
				+ ", search_form=" + search_form + ", search_org_order_no="
				+ search_org_order_no + ", search_condition_type="
				+ search_condition_type + ", printIp=" + printIp
				+ ", printPort=" + printPort + ", reason_yn=" + reason_yn
				+ ", nCoupon_yn=" + nCoupon_yn + ", order_key="
				+ Arrays.toString(order_key) + ", order_qty=" + order_qty
				+ ", search_com_group_code=" + search_com_group_code 
				+ ", nCouponOrder=" + Arrays.toString(nCouponOrder )
				+ ", LabelPrintType=" + LabelPrintType 
				+ "]";
	}

}
