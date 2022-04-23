/*
] * Copyright 2008-2009 the original author or authors.
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


/**
 * @Class Name : OrderVO.java
 * @Description : OrderVO Class
 * @Modification Information
 * @ @ 수정일 수정자 수정내용 @ --------- --------- ------------------------------- @
 *   2016.07.16 최초생성
 *   2016.10.24 엑셀데이터 임포트 컬럼 추가 (전시카테고리(대):category_large / 전시카테고리(중):category_middle)
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2016. 07.16
 * @version 0.1
 * @see
 *
 * 		Copyright (C) by MOPAS All right reserved.
 * 
 */
public class OrderVO extends DefaultVO {

	private static final long serialVersionUID = 1L;

	
	private String short_order_no; // 단축주문번호
	private String delivery_date; // 배송일->배송일자
	private String delivery_count; // 배송차수
	//private String org_order_no; // 원주문번호->주문일자,주문번호
	private String order_date; // 주문일자
	private String order_no; // 주문번호
	private String order_row_no; // 주문순번
	private String delivery_area_name; // 배송구명->권역명
	private String customer_name; // 고객명->회원명
	private String goods_code; // 경통코드->경제통합상품코드
	private String sales_goods_code; // 판매상품코드->상품코드
	private String goods_name; // 상품명
	private int order_qty; // 주문수량
	private String change_allow_yn; // 대체허용여부->상품대체요청여부
	private String zone_code; // 존코드->존정보
	private String state_code; // 진행상태(0:예정,1:지시,2:피킹,3:완료)
	private String create_user_id = "SYSTEM"; // 생성사용자ID
	private String create_date; // 생성일시
	private String update_user_id; // 수정사용자ID
	private String update_date; // 수정일시
	private String org_delivery_count; // 원본 배송차수
	
	// 20160829 Excel 업로드용 데이터 추가
	private String order_cost; // 주문단가 
	private String maker_name; // 제조사명
	private String goods_spec; // 상품규격 
	
	// 20160929 Excel 업로드용 데이터 추가
	private String card_dc_cost;
	private String delivery_amount;
	private String goods_option_name;
	private String tax_type;
	private String order_customer_name;
	private String tel_no_1;
	private String tel_no_2;
	private String address; // 수취인주소
	private String address_detail; // 수취인상세주소
	private String free_gift_name;
	private String delivery_message; 
	
	
	private String category_large;
	private String category_middle;
	
	private String pay_method; // 결제방법
	private String e_pay_cost;
	private String dlvry_pay_cost;
	private String e_sent_fee;
	private String dlvry_sent_fee;
	private String e_card_dc;
	private String dlvry_card_dc;
	private String coupon_dc;
	private String pay_cost; // 결제금액
	private String promotion_name; // 프로모션명
	private String goods_message; // 공급처 요청사항
	
	private String goods_stock_code; // 상품재고유형 18-06-18
	private String n_coupon_cost; // N쿠폰 금액 18-09-11
	
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}
	public String getDelivery_count() {
		return delivery_count;
	}
	public void setDelivery_count(String delivery_count) {
		this.delivery_count = delivery_count;
	}
	public String getShort_order_no() {
		return short_order_no;
	}
	public void setShort_order_no(String short_order_no) {
		this.short_order_no = short_order_no;
	}
	/*public String getOrg_order_no() {
		return org_order_no;
	}
	public void setOrg_order_no(String org_order_no) {
		this.org_order_no = org_order_no;
	}*/
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public String getDelivery_area_name() {
		return delivery_area_name;
	}
	public void setDelivery_area_name(String delivery_area_name) {
		this.delivery_area_name = delivery_area_name;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getSales_goods_code() {
		return sales_goods_code;
	}
	public void setSales_goods_code(String sales_goods_code) {
		this.sales_goods_code = sales_goods_code;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public int getOrder_qty() {
		return order_qty;
	}
	public void setOrder_qty(int order_qty) {
		this.order_qty = order_qty;
	}
	public String getChange_allow_yn() {
		return change_allow_yn;
	}
	public void setChange_allow_yn(String change_allow_yn) {
		this.change_allow_yn = change_allow_yn;
	}
	public String getZone_code() {
		return zone_code;
	}
	public void setZone_code(String zone_code) {
		this.zone_code = zone_code;
	}
	public String getState_code() {
		return state_code;
	}
	public void setState_code(String state_code) {
		this.state_code = state_code;
	}
	public String getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(String create_user_id) {
		this.create_user_id = create_user_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getUpdate_user_id() {
		return update_user_id;
	}
	public void setUpdate_user_id(String update_user_id) {
		this.update_user_id = update_user_id;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getOrg_delivery_count() {
		return org_delivery_count;
	}
	public void setOrg_delivery_count(String org_delivery_count) {
		this.org_delivery_count = org_delivery_count;
	}
	public String getOrder_cost() {
		return order_cost;
	}
	public void setOrder_cost(String order_cost) {
		this.order_cost = order_cost;
	}
	public String getMaker_name() {
		return maker_name;
	}
	public void setMaker_name(String maker_name) {
		this.maker_name = maker_name;
	}
	public String getGoods_spec() {
		return goods_spec;
	}
	public void setGoods_spec(String goods_spec) {
		this.goods_spec = goods_spec;
	}
	public String getCard_dc_cost() {
		return card_dc_cost;
	}
	public void setCard_dc_cost(String card_dc_cost) {
		this.card_dc_cost = card_dc_cost;
	}
	public String getDelivery_amount() {
		return delivery_amount;
	}
	public void setDelivery_amount(String delivery_amount) {
		this.delivery_amount = delivery_amount;
	}
	public String getGoods_option_name() {
		return goods_option_name;
	}
	public void setGoods_option_name(String goods_option_name) {
		this.goods_option_name = goods_option_name;
	}
	public String getTax_type() {
		return tax_type;
	}
	public void setTax_type(String tax_type) {
		this.tax_type = tax_type;
	}
	public String getOrder_customer_name() {
		return order_customer_name;
	}
	public void setOrder_customer_name(String order_customer_name) {
		this.order_customer_name = order_customer_name;
	}
	public String getTel_no_1() {
		return tel_no_1;
	}
	public void setTel_no_1(String tel_no_1) {
		this.tel_no_1 = tel_no_1;
	}
	public String getTel_no_2() {
		return tel_no_2;
	}
	public void setTel_no_2(String tel_no_2) {
		this.tel_no_2 = tel_no_2;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFree_gift_name() {
		return free_gift_name;
	}
	public void setFree_gift_name(String free_gift_name) {
		this.free_gift_name = free_gift_name;
	}
	public String getDelivery_message() {
		return delivery_message;
	}
	public void setDelivery_message(String delivery_message) {
		this.delivery_message = delivery_message;
	}
	public String getOrder_row_no() {
		return order_row_no;
	}
	public void setOrder_row_no(String order_row_no) {
		this.order_row_no = order_row_no;
	}
	public String getCategory_large() {
		return category_large;
	}
	public void setCategory_large(String category_large) {
		this.category_large = category_large;
	}
	public String getCategory_middle() {
		return category_middle;
	}
	public void setCategory_middle(String category_middle) {
		this.category_middle = category_middle;
	}
	public String getPay_method() {
		return pay_method;
	}
	public void setPay_method(String pay_method) {
		this.pay_method = pay_method;
	}
	public String getE_pay_cost() {
		return e_pay_cost;
	}
	public void setE_pay_cost(String e_pay_cost) {
		this.e_pay_cost = e_pay_cost;
	}
	public String getDlvry_pay_cost() {
		return dlvry_pay_cost;
	}
	public void setDlvry_pay_cost(String dlvry_pay_cost) {
		this.dlvry_pay_cost = dlvry_pay_cost;
	}
	public String getE_sent_fee() {
		return e_sent_fee;
	}
	public void setE_sent_fee(String e_sent_fee) {
		this.e_sent_fee = e_sent_fee;
	}
	public String getDlvry_sent_fee() {
		return dlvry_sent_fee;
	}
	public void setDlvry_sent_fee(String dlvry_sent_fee) {
		this.dlvry_sent_fee = dlvry_sent_fee;
	}
	public String getE_card_dc() {
		return e_card_dc;
	}
	public void setE_card_dc(String e_card_dc) {
		this.e_card_dc = e_card_dc;
	}
	public String getDlvry_card_dc() {
		return dlvry_card_dc;
	}
	public void setDlvry_card_dc(String dlvry_card_dc) {
		this.dlvry_card_dc = dlvry_card_dc;
	}
	public String getCoupon_dc() {
		return coupon_dc;
	}
	public void setCoupon_dc(String coupon_dc) {
		this.coupon_dc = coupon_dc;
	}
	public String getPay_cost() {
		return pay_cost;
	}
	public void setPay_cost(String pay_cost) {
		this.pay_cost = pay_cost;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPromotion_name() {
		return promotion_name;
	}
	public void setPromotion_name(String promotion_name) {
		this.promotion_name = promotion_name;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getAddress_detail() {
		return address_detail;
	}
	public void setAddress_detail(String address_detail) {
		this.address_detail = address_detail;
	}
	public String getGoods_message() {
		return goods_message;
	}
	public void setGoods_message(String goods_message) {
		this.goods_message = goods_message;
	}
	public String getGoods_stock_code() {
		return goods_stock_code;
	}
	public void setGoods_stock_code(String goods_stock_code) {
		this.goods_stock_code = goods_stock_code;
	}
	public String getN_coupon_cost() {
		return n_coupon_cost;
	}
	public void setN_coupon_cost(String n_coupon_cost) {
		this.n_coupon_cost = n_coupon_cost;
	}
	@Override
	public String toString() {
		return "OrderVO [short_order_no=" + short_order_no + ", delivery_date="
				+ delivery_date + ", delivery_count=" + delivery_count
				+ ", order_date=" + order_date + ", order_no=" + order_no
				+ ", order_row_no=" + order_row_no + ", delivery_area_name="
				+ delivery_area_name + ", customer_name=" + customer_name
				+ ", goods_code=" + goods_code + ", sales_goods_code="
				+ sales_goods_code + ", goods_name=" + goods_name
				+ ", order_qty=" + order_qty + ", change_allow_yn="
				+ change_allow_yn + ", zone_code=" + zone_code
				+ ", state_code=" + state_code + ", create_user_id="
				+ create_user_id + ", create_date=" + create_date
				+ ", update_user_id=" + update_user_id + ", update_date="
				+ update_date + ", org_delivery_count=" + org_delivery_count
				+ ", order_cost=" + order_cost + ", maker_name=" + maker_name
				+ ", goods_spec=" + goods_spec + ", card_dc_cost="
				+ card_dc_cost + ", delivery_amount=" + delivery_amount
				+ ", goods_option_name=" + goods_option_name + ", tax_type="
				+ tax_type + ", order_customer_name=" + order_customer_name
				+ ", tel_no_1=" + tel_no_1 + ", tel_no_2=" + tel_no_2
				+ ", address=" + address + ", address_detail=" + address_detail
				+ ", free_gift_name=" + free_gift_name + ", delivery_message="
				+ delivery_message + ", category_large=" + category_large
				+ ", category_middle=" + category_middle + ", pay_method="
				+ pay_method + ", e_pay_cost=" + e_pay_cost
				+ ", dlvry_pay_cost=" + dlvry_pay_cost + ", e_sent_fee="
				+ e_sent_fee + ", dlvry_sent_fee=" + dlvry_sent_fee
				+ ", e_card_dc=" + e_card_dc + ", dlvry_card_dc="
				+ dlvry_card_dc + ", coupon_dc=" + coupon_dc + ", pay_cost="
				+ pay_cost + ", promotion_name=" + promotion_name
				+ ", goods_message=" + goods_message 
				+ ", goods_stock_code=" + goods_stock_code 
				+ ", n_coupon_cost=" + n_coupon_cost 
				+ "]";
	}
	
	

}
