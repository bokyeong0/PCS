package vertexid.nhmarket.pcs.service;

/**
 * 리포트의 주문내역 리스트 아이템
 */
public class ReportItemVO {
	public static final String LCK_KND_NORMAL = "1"; // 기본
	public static final String LCK_KND_LACK = "2"; // 결품
	public static final String LCK_KND_PARK_LACK = "3"; // 부분 결품 및 대체
	public static final String LCK_KND_REPLACE = "4"; // 대체(전체색칠)
	public static final String LCK_KND_REPLACE_ONLY = "5"; // 대체(하나색칠)
	public static final String UNDERLINE = "6"; // 밑줄긋기

	private String lck_knd; // ROW 종류

	private String row; // 순번	

	private String zone_code; // 존코드 (20170906 이지만) 

	private String goods_name; // 상품명 ( 과세구분 / 상품명 / 옵션 / 규격 / 제조원 )

	private int order_cost; // 판매단가(주문단가)

	private int order_qty; // 주문수량(수량)

	private int order_cost_total; // 금액 (계산)

	private int sold_out_qty; // 결품 (계산)

	private int change_pick_qty; // 대체

	private int pick_qty; // 배송 (계산)

	private String goods_code; // 상품코드
	
	private String promotion_name; // 프로모션명

	/** 합계 구하기 위해 추가 */
	private int min_sum; // 대체 차액 합계

	private int pls_sum; // 대체 혜택 합계
	
	private String under_line; // 밑줄긋기

	private int n_coupon_cost; // N쿠폰 단가 18-09-11
	private int n_coupon_cost_total; // N쿠폰 금액 18-09-11

	public String getLck_knd() {
		return lck_knd;
	}

	public void setLck_knd(String lck_knd) {
		this.lck_knd = lck_knd;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public int getOrder_cost() {
		return order_cost;
	}

	public void setOrder_cost(int order_cost) {
		this.order_cost = order_cost;
	}

	public int getOrder_qty() {
		return order_qty;
	}

	public void setOrder_qty(int order_qty) {
		this.order_qty = order_qty;
	}

	public int getOrder_cost_total() {
		return order_cost_total;
	}

	public void setOrder_cost_total(int order_cost_total) {
		this.order_cost_total = order_cost_total;
	}

	public int getSold_out_qty() {
		return sold_out_qty;
	}

	public void setSold_out_qty(int sold_out_qty) {
		this.sold_out_qty = sold_out_qty;
	}

	public int getChange_pick_qty() {
		return change_pick_qty;
	}

	public void setChange_pick_qty(int change_pick_qty) {
		this.change_pick_qty = change_pick_qty;
	}

	public int getPick_qty() {
		return pick_qty;
	}

	public void setPick_qty(int pick_qty) {
		this.pick_qty = pick_qty;
	}

	public String getGoods_code() {
		return goods_code;
	}

	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}

	public int getMin_sum() {
		return min_sum;
	}

	public void setMin_sum(int min_sum) {
		this.min_sum = min_sum;
	}

	public int getPls_sum() {
		return pls_sum;
	}

	public void setPls_sum(int pls_sum) {
		this.pls_sum = pls_sum;
	}

	public String getUnder_line() {
		return under_line;
	}

	public String getPromotion_name() {
		return promotion_name;
	}

	public void setPromotion_name(String promotion_name) {
		this.promotion_name = promotion_name;
	}

	public void setUnder_line(String under_line) {
		this.under_line = under_line;
	}
	

	public String getZone_code() {
		return zone_code;
	}

	public void setZone_code(String zone_code) {
		this.zone_code = zone_code;
	}

	public int getN_coupon_cost() {
		return n_coupon_cost;
	}

	public void setN_coupon_cost(int n_coupon_cost) {
		this.n_coupon_cost = n_coupon_cost;
	}

	public int getN_coupon_cost_total() {
		return n_coupon_cost_total;
	}

	public void setN_coupon_cost_total(int n_coupon_cost_total) {
		this.n_coupon_cost_total = n_coupon_cost_total;
	}

	@Override
	public String toString() {
		return "ReportItemVO [lck_knd=" + lck_knd + ", row=" + row
				+ ", zone_code=" + zone_code + ", goods_name=" + goods_name
				+ ", order_cost=" + order_cost + ", order_qty=" + order_qty
				+ ", order_cost_total=" + order_cost_total + ", sold_out_qty="
				+ sold_out_qty + ", change_pick_qty=" + change_pick_qty
				+ ", pick_qty=" + pick_qty + ", goods_code=" + goods_code
				+ ", promotion_name=" + promotion_name + ", min_sum=" + min_sum
				+ ", pls_sum=" + pls_sum + ", under_line=" + under_line + "]";
	}

	
}
