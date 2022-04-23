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
 * @Class Name : OrderVO.java
 * @Description : OrderVO Class
 * @Modification Information
 * @ @ 수정일 수정자 수정내용 @ --------- --------- ------------------------------- @
 *   2016.07.16 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2016. 07.16
 * @version 0.1
 * @see
 *
 * 		Copyright (C) by MOPAS All right reserved.
 */
public class TempOrderVO extends DefaultVO {

	private static final long serialVersionUID = 1L;

	private String pick_id; // 피킹ID
	private String delivery_date; // 배송일
	private String delivery_count; // 배송차수
	private String short_order_no; // 단축주문번호
	private String org_order_no; // 원주문번호
	private String order_date; // 주문일자
	private String order_no; // 주문번호
	private String order_row_no; // 주문순번
	private String goods_code; // 경통코드

	private String delivery_area_name; // 배송구명
	private String customer_name; // 고객명
	private String sales_goods_code; // 판매상품코드
	private String goods_name; // 상품명
	private int order_qty; // 주문수량
	private String change_allow_yn; // 대체허용여부
	private String zone_code; // 존코드
	private String state_code; // 진행상태(0:예정,1:지시,2:피킹,3:완료)
	private String create_user_id; // 생성사용자ID
	private String create_date; // 생성일시
	private String update_user_id; // 수정사용자ID
	private String update_date; // 수정일시

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

	public String getOrder_row_no() {
		return order_row_no;
	}

	public void setOrder_row_no(String order_row_no) {
		this.order_row_no = order_row_no;
	}

	@Override
	public String toString() {
		return "TempOrderVO [pick_id=" + pick_id + ", delivery_date="
				+ delivery_date + ", delivery_count=" + delivery_count
				+ ", short_order_no=" + short_order_no + ", org_order_no="
				+ org_order_no + ", order_date=" + order_date + ", order_no="
				+ order_no + ", order_row_no=" + order_row_no + ", goods_code="
				+ goods_code + ", delivery_area_name=" + delivery_area_name
				+ ", customer_name=" + customer_name + ", sales_goods_code="
				+ sales_goods_code + ", goods_name=" + goods_name
				+ ", order_qty=" + order_qty + ", change_allow_yn="
				+ change_allow_yn + ", zone_code=" + zone_code
				+ ", state_code=" + state_code + ", create_user_id="
				+ create_user_id + ", create_date=" + create_date
				+ ", update_user_id=" + update_user_id + ", update_date="
				+ update_date + "]";
	}

}
