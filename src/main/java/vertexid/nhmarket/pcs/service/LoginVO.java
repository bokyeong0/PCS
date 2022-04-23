package vertexid.nhmarket.pcs.service;

public class LoginVO {
	private String workerId;
	private String deviceId;

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "LoginVO{" + "workerId='" + workerId + '\'' + ", deviceId='" + deviceId + '\'' + '}';
	}
}
