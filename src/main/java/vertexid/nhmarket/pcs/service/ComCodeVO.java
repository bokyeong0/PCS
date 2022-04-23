package vertexid.nhmarket.pcs.service;

public class ComCodeVO {
	private String com_group_code; // 공통그룹코드(@@-공통그룹인 경우)
	private String com_code; // 공통코드
	private String com_name; // 공통명
	private String com_order; // 공통순위
	private String com_sub_code; // 공통서브코드
	private String com_desc; // 공통설명
	private String use_yn; // 사용여부
	private String create_user_id; // 생성사용자ID
	private String create_date; // 생성일시
	private String update_user_id; // 수정사용자ID
	private String update_date; // 수정일시
	
	
	private String state; // data 수정여부

	public ComCodeVO() {
	}

	@Override
	public String toString() {
		return "ComCodeVO [com_group_code=" + com_group_code + ", com_code=" + com_code + ", com_name=" + com_name
				+ ", com_order=" + com_order + ", com_sub_code=" + com_sub_code + ", com_desc=" + com_desc
				+ ", use_yn=" + use_yn + ", create_user_id=" + create_user_id + ", create_date=" + create_date
				+ ", update_user_id=" + update_user_id + ", update_date=" + update_date + "]";
	}

	
	
	public ComCodeVO(String com_group_code, String com_code) {
		super();
		this.com_group_code = com_group_code;
		this.com_code = com_code;
	}

	public String getCom_group_code() {
		return com_group_code;
	}

	public void setCom_group_code(String com_group_code) {
		this.com_group_code = com_group_code;
	}

	public String getCom_code() {
		return com_code;
	}

	public void setCom_code(String com_code) {
		this.com_code = com_code;
	}

	public String getCom_name() {
		return com_name;
	}

	public void setCom_name(String com_name) {
		this.com_name = com_name;
	}

	public String getCom_order() {
		return com_order;
	}

	public void setCom_order(String com_order) {
		this.com_order = com_order;
	}

	public String getCom_sub_code() {
		return com_sub_code;
	}

	public void setCom_sub_code(String com_sub_code) {
		this.com_sub_code = com_sub_code;
	}

	public String getCom_desc() {
		return com_desc;
	}

	public void setCom_desc(String com_desc) {
		this.com_desc = com_desc;
	}

	public String getUse_yn() {
		return use_yn;
	}

	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
