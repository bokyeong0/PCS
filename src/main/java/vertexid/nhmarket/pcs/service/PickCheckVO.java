package vertexid.nhmarket.pcs.service;

import java.util.ArrayList;
import java.util.List;

public class PickCheckVO extends DefaultJsonVO {
	private String state_code;
	private String worker_id;
	private String pick_id;
	private String version;

	private List<PickMobileDetailVO> mobileDetailVOs = new ArrayList<PickMobileDetailVO>();

	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	public String getWorker_id() {
		return worker_id;
	}

	public void setWorker_id(String worker_id) {
		this.worker_id = worker_id;
	}

	public String getPick_id() {
		return pick_id;
	}

	public void setPick_id(String pick_id) {
		this.pick_id = pick_id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<PickMobileDetailVO> getMobileDetailVOs() {
		return mobileDetailVOs;
	}

	public void setMobileDetailVOs(List<PickMobileDetailVO> mobileDetailVOs) {
		this.mobileDetailVOs = mobileDetailVOs;
	}

	@Override
	public String toString() {
		return "PickCheckVO [state_code=" + state_code + ", worker_id=" + worker_id + ", pick_id=" + pick_id + ", version=" + version + ", mobileDetailVOs=" + mobileDetailVOs + "]";
	}

}
