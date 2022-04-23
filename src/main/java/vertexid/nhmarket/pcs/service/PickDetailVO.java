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

/**
 * @Class Name : PickDetailVO.java
 * @Description : PickDetailVO Class
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
 */
public class PickDetailVO extends DefaultVO {

	private static final long serialVersionUID = 1L;

	private String pick_id; // 피킹ID
	private int pick_row_no; // 피킹행번호
	private String goods_code; // 경통코드
	private String sales_goods_code; // 판매상품코드
	private String goods_name; // 상품명
	private int order_qty; // 주문수량
	private int pick_qty; // 피킹수량
	private String change_allow_yn; // 대체허용여부
	private String change_goods_code; // 대체경통코드
	private String change_pick_qty; // 대체피킹수량
	private String state_code; // 진행상태(0:예정,1:지시,2:피킹,3:완료)
	private String create_user_id = "SYSTEM"; // 생성사용자ID
	private String create_date; // 생성일자
	private String update_user_id; // 수정사용자ID
	private String update_date; // 수정일자
	
	// 20160829 Excel 업로드용 데이터 추가
	private int order_cost; // 주문단가 
	private String maker_name; // 제조사명
	private String goods_spec; // 상품규격 
	
	// 20160929 Excel 업로드용 데이터 추가
	private int card_dc_cost;
	private String goods_option_name;	
	private String tax_type;
	private String order_row_no;	
	
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
	
	private String goods_stock_code; // 상품재고유형 18-06-18
	private String n_coupon_cost; // N쿠폰 금액 18-09-11
	
	public String getPick_id() {
		return pick_id;
	}
	public void setPick_id(String pick_id) {
		this.pick_id = pick_id;
	}
	public int getPick_row_no() {
		return pick_row_no;
	}
	public void setPick_row_no(int pick_row_no) {
		this.pick_row_no = pick_row_no;
	}
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
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
	public int getPick_qty() {
		return pick_qty;
	}
	public void setPick_qty(int pick_qty) {
		this.pick_qty = pick_qty;
	}
	public String getChange_allow_yn() {
		return change_allow_yn;
	}
	public void setChange_allow_yn(String change_allow_yn) {
		this.change_allow_yn = change_allow_yn;
	}
	public String getChange_goods_code() {
		return change_goods_code;
	}
	public void setChange_goods_code(String change_goods_code) {
		this.change_goods_code = change_goods_code;
	}
	public String getChange_pick_qty() {
		return change_pick_qty;
	}
	public void setChange_pick_qty(String change_pick_qty) {
		this.change_pick_qty = change_pick_qty;
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
	public int getOrder_cost() {
		return order_cost;
	}
	public void setOrder_cost(int order_cost) {
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
	public int getCard_dc_cost() {
		return card_dc_cost;
	}
	public void setCard_dc_cost(int card_dc_cost) {
		this.card_dc_cost = card_dc_cost;
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
		return "PickDetailVO [pick_id=" + pick_id + ", pick_row_no="
				+ pick_row_no + ", goods_code=" + goods_code
				+ ", sales_goods_code=" + sales_goods_code + ", goods_name="
				+ goods_name + ", order_qty=" + order_qty + ", pick_qty="
				+ pick_qty + ", change_allow_yn=" + change_allow_yn
				+ ", change_goods_code=" + change_goods_code
				+ ", change_pick_qty=" + change_pick_qty + ", state_code="
				+ state_code + ", create_user_id=" + create_user_id
				+ ", create_date=" + create_date + ", update_user_id="
				+ update_user_id + ", update_date=" + update_date
				+ ", order_cost=" + order_cost + ", maker_name=" + maker_name
				+ ", goods_spec=" + goods_spec + ", card_dc_cost="
				+ card_dc_cost + ", goods_option_name=" + goods_option_name
				+ ", tax_type=" + tax_type + ", order_row_no=" + order_row_no
				+ ", category_large=" + category_large + ", category_middle="
				+ category_middle + ", pay_method=" + pay_method
				+ ", e_pay_cost=" + e_pay_cost + ", dlvry_pay_cost="
				+ dlvry_pay_cost + ", e_sent_fee=" + e_sent_fee
				+ ", dlvry_sent_fee=" + dlvry_sent_fee + ", e_card_dc="
				+ e_card_dc + ", dlvry_card_dc=" + dlvry_card_dc
				+ ", coupon_dc=" + coupon_dc + ", pay_cost=" + pay_cost
				+ ", promotion_name=" + promotion_name 
				+ ", goods_stock_code=" + goods_stock_code 
				+ ", n_coupon_cost=" + n_coupon_cost 
				+ "]";
	}
	
	

}
