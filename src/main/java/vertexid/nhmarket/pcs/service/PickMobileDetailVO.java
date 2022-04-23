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

// PDA 전송 값 관련 VO
public class PickMobileDetailVO {
	private String pick_id; // 피킹ID
	private int pick_row_no; // 피킹행번호
	private String goods_code; // 경통코드
	private String sales_goods_code; // 판매상품코드
	private String goods_name; // 상품명
	private int order_qty; // 주문수량
	private int pick_qty; // 피킹수량
	private String change_allow_yn; // 대체허용여부
	private String change_goods_name; // 대체상품명
	private String change_goods_code; // 대체경통코드
	private String change_pick_qty; // 대체피킹수량
	private String state_code; // 진행상태(0:예정,1:지시,2:피킹,3:완료)
	private String create_user_id; // 생성사용자ID
	private String create_date; // 생성일자
	private String update_user_id; // 수정사용자ID
	private String update_date; // 수정일자
	private int order_cost; // 주문단가
	private String sold_out_reason; // 결품사유
	private String maker_name; // 제조사명
	private String goods_spec; // 상품규격
	private int change_goods_cost; // 대체상품단가

	private String zone_code; // 존정보
	private String worker_id; // 작업자 아이디

	private String goods_option_name; // 상품옵션명
	private String category_large; // 전시카테고리 대
	private String category_middle; // 전시카테고리 중
	
	private String promotion_name; // 프로모션명
	
	private String order_date; // 주문일자
	
	private String order_no; // 주문번호

	private int count; // 분배 or 대체 수량
	
	private String short_order_no; // 단축주문번호

	private String scan_goods_code; // 스캔상품코드
	
	private String delivery_message; // 배송메세지
	
	private String customer_name; // 주문고객명
	
	private String goods_message; // 공급처 요청내용
	
	private String order_customer_name; // 수취고객명
	
	//상품재고유형
	private String goods_stock_code; 
	private String goods_weight; // 규격 (실피킹)
	private String change_goods_weight; // 규격 (대체상품)
	
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

	public String getChange_goods_name() {
		return change_goods_name;
	}

	public void setChange_goods_name(String change_goods_name) {
		this.change_goods_name = change_goods_name;
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

	public String getSold_out_reason() {
		return sold_out_reason;
	}

	public void setSold_out_reason(String sold_out_reason) {
		this.sold_out_reason = sold_out_reason;
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

	public String getZone_code() {
		return zone_code;
	}

	public void setZone_code(String zone_code) {
		this.zone_code = zone_code;
	}

	public String getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(String worker_id) {
		this.worker_id = worker_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getChange_goods_cost() {
		return change_goods_cost;
	}

	public void setChange_goods_cost(int change_goods_cost) {
		this.change_goods_cost = change_goods_cost;
	}

	public String getGoods_option_name() {
		return goods_option_name;
	}

	public void setGoods_option_name(String goods_option_name) {
		this.goods_option_name = goods_option_name;
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

	public String getShort_order_no() {
		return short_order_no;
	}

	public void setShort_order_no(String short_order_no) {
		this.short_order_no = short_order_no;
	}

	public String getScan_goods_code() {
		return scan_goods_code;
	}

	public void setScan_goods_code(String scan_goods_code) {
		this.scan_goods_code = scan_goods_code;
	}

	

	public String getDelivery_message() {
		return delivery_message;
	}

	public void setDelivery_message(String delivery_message) {
		this.delivery_message = delivery_message;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
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

	public String getPromotion_name() {
		return promotion_name;
	}

	public void setPromotion_name(String promotion_name) {
		this.promotion_name = promotion_name;
	}

	public String getGoods_message() {
		return goods_message;
	}

	public void setGoods_message(String goods_message) {
		this.goods_message = goods_message;
	}

	public String getOrder_customer_name() {
		return order_customer_name;
	}

	public void setOrder_customer_name(String order_customer_name) {
		this.order_customer_name = order_customer_name;
	}

	public String getGoods_stock_code() {
		return goods_stock_code;
	}

	public void setGoods_stock_code(String goods_stock_code) {
		this.goods_stock_code = goods_stock_code;
	}
	
	public String getGoods_weight() {
		return goods_weight;
	}

	public void setGoods_weight(String goods_weight) {
		this.goods_weight = goods_weight;
	}

	public String getChange_goods_weight() {
		return change_goods_weight;
	}

	public void setChange_goods_weight(String change_goods_weight) {
		this.change_goods_weight = change_goods_weight;
	}

	@Override
	public String toString() {
		return "PickMobileDetailVO [pick_id=" + pick_id + ", pick_row_no="
				+ pick_row_no + ", goods_code=" + goods_code
				+ ", sales_goods_code=" + sales_goods_code + ", goods_name="
				+ goods_name + ", order_qty=" + order_qty + ", pick_qty="
				+ pick_qty + ", change_allow_yn=" + change_allow_yn
				+ ", change_goods_name=" + change_goods_name
				+ ", change_goods_code=" + change_goods_code
				+ ", change_pick_qty=" + change_pick_qty + ", state_code="
				+ state_code + ", create_user_id=" + create_user_id
				+ ", create_date=" + create_date + ", update_user_id="
				+ update_user_id + ", update_date=" + update_date
				+ ", order_cost=" + order_cost + ", sold_out_reason="
				+ sold_out_reason + ", maker_name=" + maker_name
				+ ", goods_spec=" + goods_spec + ", change_goods_cost="
				+ change_goods_cost + ", zone_code=" + zone_code
				+ ", worker_id=" + worker_id + ", goods_option_name="
				+ goods_option_name + ", category_large=" + category_large
				+ ", category_middle=" + category_middle + ", promotion_name="
				+ promotion_name + ", order_date=" + order_date + ", order_no="
				+ order_no + ", count=" + count + ", short_order_no="
				+ short_order_no + ", scan_goods_code=" + scan_goods_code
				+ ", delivery_message=" + delivery_message + ", customer_name="
				+ customer_name + ", goods_message=" + goods_message
				+ ", order_customer_name=" + order_customer_name 
				+ ", goods_stock_code=" + goods_stock_code 
				+ ", goods_weight=" + goods_weight 
				+ ", change_goods_weight=" + change_goods_weight 
				+ "]";
	}


}
