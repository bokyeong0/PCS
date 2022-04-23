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

public class PickTotalVO {

	// header
	private String pick_id;
	private String delivery_date;
	private String delivery_count;
	private String short_order_no;
	private String org_order_no;
	private String order_date; // 주문일자
	private String order_no; // 주문번호
	private String order_row_no; // 주문순번
	private int tray_no;
	private String delivery_area_name;
	private String customer_name; // 주문고객명
	private String label_state;
	private int label_print_count;
	private String trolley_id;
	private String worker_id;
	private String state_code;
	private String create_user_id = "SYSTEM";
	private String create_date;
	private String update_user_id;
	private String update_date;
	private String org_delivery_count;
	private int delivery_amount;
	private String order_customer_name; // 수취고객명
	private String tel_no_1;
	private String tel_no_2;
	private String address;
	private String address_detail;
	private String free_gift_name;
	private String delivery_message; // 배송메시지
	private String goods_message; // 상품메시지

	// detail
	private String goods_code;
	private String sales_goods_name;
	private String sales_goods_code;
	private String goods_name;
	private int order_qty;
	private int pick_qty;
	private String change_allow_yn;
	private String change_goods_code;
	private int change_pick_qty;
	private int order_cost;
	private String sold_out_reason;
	private String maker_name;
	private String goods_spec;
	private String goods_option_name;
	private String category_large; // 전시카테고리 대
	private String category_middle; // 전시카테고리 중
	private String tax_type;
	
	private String change_goods_name;
	private int change_goods_cost;
	private int card_dc_cost;
	private String pay_method; // 결제방법	
	private String e_pay_cost;
	private String dlvry_pay_cost;
	private String e_sent_fee;
	private String dlvry_sent_fee;
	private String e_card_dc;
	private String dlvry_card_dc;
	private String coupon_dc;
	private String pay_cost; // 결제금액
	private String zone_code; // 존코드
	private String promotion_name; // 프로모션명
	private int n_coupon_cost; // N쿠폰 금액
	
	public String getPick_id() {
		return pick_id;
	}
	public void setPick_id(String pick_id) {
		this.pick_id = pick_id;
	}
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
	public String getOrg_order_no() {
		return org_order_no;
	}
	public void setOrg_order_no(String org_order_no) {
		this.org_order_no = org_order_no;
	}
	public String getZone_code() {
		return zone_code;
	}
	public void setZone_code(String zone_code) {
		this.zone_code = zone_code;
	}
	public int getTray_no() {
		return tray_no;
	}
	public void setTray_no(int tray_no) {
		this.tray_no = tray_no;
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
	public String getLabel_state() {
		return label_state;
	}
	public void setLabel_state(String label_state) {
		this.label_state = label_state;
	}
	public int getLabel_print_count() {
		return label_print_count;
	}
	public void setLabel_print_count(int label_print_count) {
		this.label_print_count = label_print_count;
	}
	public String getTrolley_id() {
		return trolley_id;
	}
	public void setTrolley_id(String trolley_id) {
		this.trolley_id = trolley_id;
	}
	public String getWorker_id() {
		return worker_id;
	}
	public void setWorker_id(String worker_id) {
		this.worker_id = worker_id;
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
	public int getDelivery_amount() {
		return delivery_amount;
	}
	public void setDelivery_amount(int delivery_amount) {
		this.delivery_amount = delivery_amount;
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
	public String getGoods_code() {
		return goods_code;
	}
	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}
	public String getSales_goods_name() {
		return sales_goods_name;
	}
	public void setSales_goods_name(String sales_goods_name) {
		this.sales_goods_name = sales_goods_name;
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
	public int getChange_pick_qty() {
		return change_pick_qty;
	}
	public void setChange_pick_qty(int change_pick_qty) {
		this.change_pick_qty = change_pick_qty;
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
	public String getChange_goods_name() {
		return change_goods_name;
	}
	public void setChange_goods_name(String change_goods_name) {
		this.change_goods_name = change_goods_name;
	}
	public int getChange_goods_cost() {
		return change_goods_cost;
	}
	public void setChange_goods_cost(int change_goods_cost) {
		this.change_goods_cost = change_goods_cost;
	}
	public int getCard_dc_cost() {
		return card_dc_cost;
	}
	public void setCard_dc_cost(int card_dc_cost) {
		this.card_dc_cost = card_dc_cost;
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
	public String getAddress_detail() {
		return address_detail;
	}
	public void setAddress_detail(String address_detail) {
		this.address_detail = address_detail;
	}
	public int getN_coupon_cost() {
		return n_coupon_cost;
	}
	public void setN_coupon_cost(int n_coupon_cost) {
		this.n_coupon_cost = n_coupon_cost;
	}
	@Override
	public String toString() {
		return "PickTotalVO [pick_id=" + pick_id + ", delivery_date="
				+ delivery_date + ", delivery_count=" + delivery_count
				+ ", short_order_no=" + short_order_no + ", org_order_no="
				+ org_order_no + ", order_date=" + order_date + ", order_no="
				+ order_no + ", order_row_no=" + order_row_no + ", tray_no="
				+ tray_no + ", delivery_area_name=" + delivery_area_name
				+ ", customer_name=" + customer_name + ", label_state="
				+ label_state + ", label_print_count=" + label_print_count
				+ ", trolley_id=" + trolley_id + ", worker_id=" + worker_id
				+ ", state_code=" + state_code + ", create_user_id="
				+ create_user_id + ", create_date=" + create_date
				+ ", update_user_id=" + update_user_id + ", update_date="
				+ update_date + ", org_delivery_count=" + org_delivery_count
				+ ", delivery_amount=" + delivery_amount
				+ ", order_customer_name=" + order_customer_name
				+ ", tel_no_1=" + tel_no_1 + ", tel_no_2=" + tel_no_2
				+ ", address=" + address + ", address_detail=" + address_detail
				+ ", free_gift_name=" + free_gift_name + ", delivery_message="
				+ delivery_message + ", goods_message=" + goods_message
				+ ", goods_code=" + goods_code + ", sales_goods_name="
				+ sales_goods_name + ", sales_goods_code=" + sales_goods_code
				+ ", goods_name=" + goods_name + ", order_qty=" + order_qty
				+ ", pick_qty=" + pick_qty + ", change_allow_yn="
				+ change_allow_yn + ", change_goods_code=" + change_goods_code
				+ ", change_pick_qty=" + change_pick_qty + ", order_cost="
				+ order_cost + ", sold_out_reason=" + sold_out_reason
				+ ", maker_name=" + maker_name + ", goods_spec=" + goods_spec
				+ ", goods_option_name=" + goods_option_name
				+ ", category_large=" + category_large + ", category_middle="
				+ category_middle + ", tax_type=" + tax_type
				+ ", change_goods_name=" + change_goods_name
				+ ", change_goods_cost=" + change_goods_cost
				+ ", card_dc_cost=" + card_dc_cost + ", pay_method="
				+ pay_method + ", e_pay_cost=" + e_pay_cost
				+ ", dlvry_pay_cost=" + dlvry_pay_cost + ", e_sent_fee="
				+ e_sent_fee + ", dlvry_sent_fee=" + dlvry_sent_fee
				+ ", e_card_dc=" + e_card_dc + ", dlvry_card_dc="
				+ dlvry_card_dc + ", coupon_dc=" + coupon_dc + ", pay_cost="
				+ pay_cost + ", zone_code=" + zone_code + ", promotion_name="
				+ promotion_name + ", n_coupon_cost=" + n_coupon_cost+ "]";
	}
	
}
