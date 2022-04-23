package vertexid.nhmarket.pcs.service;

import java.util.Date;

public class PickHeaderVO extends DefaultVO {
	private static final long serialVersionUID = 1L;

	private String pick_id; // 피킹 ID
	private String delivery_date; // 배송일자
	private String delivery_count; // 배송차수
	private String short_order_no; // 단축주문번호
	private String org_order_no;
	private String order_date; // 주문일자
	private String order_no; // 주문번호
	private String order_row_no; // 주문순번
	private String goods_code; // 경통상품코드
	private String zone_code; // 존코드
	private int tray_no; // 트레이번호
	private String delivery_area_name; // 권역명
	private String customer_name; // 주문고객명
	private String label_state; // 라벨상태
	private int label_print_count; // 라벨프린트횟수
	private String trolley_id; // 트롤리 ID
	private String worker_id; // 작업자 ID
	private String state_code; // 상태코드
	private String create_user_id = "SYSTEM";
	private String create_date;
	private String update_user_id;
	private String update_date;
	private String org_delivery_count;
	private Date start_pick_date;

	// 20160929 Excel 업로드용 데이터 추가
	private int delivery_amount; 
	private String order_customer_name; // 수취고객명
	private String tel_no_1; // 고객 전화번호
	private String tel_no_2; // 고객 휴대폰번호
	private String address; // 고객주소
	private String address_detail; // 고객상세주소
	private String free_gift_name; // 단품명
	private String delivery_message; // 배송메세지
	private String promotion_name; // 프로모션명
	private String goods_message; // 상품메세지
	private int complete_count;
	
	
	@Override
	public String toString() {
		return "PickHeaderVO [pick_id=" + pick_id + ", delivery_date="
				+ delivery_date + ", delivery_count=" + delivery_count
				+ ", short_order_no=" + short_order_no + ", order_date="
				+ order_date + ", order_no=" + order_no + ", order_row_no="
				+ order_row_no + ", goods_code=" + goods_code + ", zone_code="
				+ zone_code + ", tray_no=" + tray_no + ", delivery_area_name="
				+ delivery_area_name + ", customer_name=" + customer_name
				+ ", label_state=" + label_state + ", label_print_count="
				+ label_print_count + ", trolley_id=" + trolley_id
				+ ", worker_id=" + worker_id + ", state_code=" + state_code
				+ ", create_user_id=" + create_user_id + ", create_date="
				+ create_date + ", update_user_id=" + update_user_id
				+ ", update_date=" + update_date + ", org_delivery_count="
				+ org_delivery_count + ", start_pick_date=" + start_pick_date
				+ ", delivery_amount=" + delivery_amount
				+ ", order_customer_name=" + order_customer_name
				+ ", tel_no_1=" + tel_no_1 + ", tel_no_2=" + tel_no_2
				+ ", address=" + address + ", address_detail=" + address_detail
				+ ", free_gift_name=" + free_gift_name + ", delivery_message="
				+ delivery_message + ", promotion_name=" + promotion_name
				+ ", goods_message=" + goods_message + ", complete_count="
				+ complete_count + "]";
	}

	public String toHeaderKeyString() {
		return "PickHeaderVO [delivery_date=" + delivery_date + ", delivery_count=" + delivery_count
				+ ", short_order_no=" + short_order_no + ", org_order_no=" + org_order_no + ", zone_code=" + zone_code
				+ "]";
	}

	public String toTrayCheckString() {
		return "PickHeaderVO [delivery_date=" + delivery_date + ", delivery_count=" + delivery_count
				+ ", short_order_no=" + short_order_no + ", org_order_no=" + org_order_no + "]";
	}

	public Date getStart_pick_date() {
		return start_pick_date;
	}

	public void setStart_pick_date(Date start_pick_date) {
		this.start_pick_date = start_pick_date;
	}
	
	public String toTrayCheckString2nd() {
		return "PickHeaderVO [zone_code=" + zone_code + "]";
	}

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

	public int getComplete_count() {
		return complete_count;
	}

	public void setComplete_count(int complete_count) {
		this.complete_count = complete_count;
	}


}
