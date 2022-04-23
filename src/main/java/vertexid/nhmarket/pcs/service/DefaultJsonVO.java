package vertexid.nhmarket.pcs.service;

public class DefaultJsonVO {
	// 성공 여부
	private boolean success;
	// 메시지
	private String message;

	public DefaultJsonVO() {

	}

	public DefaultJsonVO(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "DefaultJsonVO [success=" + success + ", message=" + message + "]";
	}

}
