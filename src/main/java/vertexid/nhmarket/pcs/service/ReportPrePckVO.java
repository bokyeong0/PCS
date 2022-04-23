package vertexid.nhmarket.pcs.service;

/**
 * 리포트의 사전 피킹 리스트 아이템
 * 사전피킹리스트에 아이템을 추가하고하 할 때 사용
 */
public class ReportPrePckVO {
	public static final String LCK_KND_NORMAL = "1"; // 기본

	private String row; // 행번호
	
	private String zone_code; // 존코드
	
	private String zone_code_all; // 존코드 전체

	private String delivery_date; // 배송일
	
	private String delivery_count; // 차수
	
	private String short_order_no; // 단축주문번호
	
	private String customer_name; // 주문고객명
	
	private String goods_code; // 상품코드

	private String goods_name; // 상품명 ( 과세구분 / 상품명 / 옵션 / 규격 / 제조원 )

	private String goods_option_name; // 상품옵션

	private String goods_spec; // 규격 
	
	private int order_qty; // 주문수량(수량)

	private int order_cost; // 판매단가(주문단가)

	private String delivery_message; // 배송 메시지 

	private String change_allow_yn; // 대체여부
	
	private String delivery_area_name; // 권역구명
	
	private String order_customer_name; // 수취고객명
	
	private String order_date; // 주문일자
	
	private String order_no; // 주문번호
	
	private String goods_message; // 상품 메세지
	
	private String tel; // 수취인 전화번호
	
	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getDelivery_area_name() {
		return delivery_area_name;
	}

	public void setDelivery_area_name(String delivery_area_name) {
		this.delivery_area_name = delivery_area_name;
	}

	public String getZone_code() {
		return zone_code;
	}

	public void setZone_code(String zone_code) {
		this.zone_code = zone_code;
	}
	
	public String getZone_code_all() {
		return zone_code_all;
	}

	public void setZone_code_all(String zone_code_all) {
		this.zone_code_all = zone_code_all;
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

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getGoods_code() {
		return goods_code;
	}

	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_option_name() {
		return goods_option_name;
	}

	public void setGoods_option_name(String goods_option_name) {
		this.goods_option_name = goods_option_name;
	}

	public String getGoods_spec() {
		return goods_spec;
	}

	public void setGoods_spec(String goods_spec) {
		this.goods_spec = goods_spec;
	}

	public int getOrder_qty() {
		return order_qty;
	}

	public void setOrder_qty(int order_qty) {
		this.order_qty = order_qty;
	}

	public int getOrder_cost() {
		return order_cost;
	}

	public void setOrder_cost(int order_cost) {
		this.order_cost = order_cost;
	}

	public String getDelivery_message() {
		return delivery_message;
	}

	public void setDelivery_message(String delivery_message) {
		this.delivery_message = delivery_message;
	}

	public String getChange_allow_yn() {
		return change_allow_yn;
	}

	public void setChange_allow_yn(String change_allow_yn) {
		this.change_allow_yn = change_allow_yn;
	}

	public String getOrder_customer_name() {
		return order_customer_name;
	}

	public void setOrder_customer_name(String order_customer_name) {
		this.order_customer_name = order_customer_name;
	}

	public static String getLckKndNormal() {
		return LCK_KND_NORMAL;
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

	public String getGoods_message() {
		return goods_message;
	}

	public void setGoods_message(String goods_message) {
		this.goods_message = goods_message;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "ReportPrePckVO [zone_code=" + zone_code + ", delivery_date="
				+ delivery_date + ", delivery_count=" + delivery_count
				+ ", short_order_no=" + short_order_no + ", customer_name="
				+ customer_name + ", goods_code=" + goods_code
				+ ", goods_name=" + goods_name + ", goods_option_name="
				+ goods_option_name + ", goods_spec=" + goods_spec
				+ ", order_qty=" + order_qty + ", order_cost=" + order_cost
				+ ", delivery_message=" + delivery_message
				+ ", change_allow_yn=" + change_allow_yn
				+ ", delivery_area_name=" + delivery_area_name
				+ ", order_customer_name=" + order_customer_name
				+ ", order_date=" + order_date + ", order_no=" + order_no
				+ ", goods_message=" + goods_message + "]";
	}
		
	
	

}
