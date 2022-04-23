package vertexid.nhmarket.pcs.service;

import java.util.ArrayList;

public class PickingStateVO {
	private String workerId;
	private ArrayList<String> pickIdList;
	private String state_code;

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public ArrayList<String> getPickIdList() {
		return pickIdList;
	}

	public void setPickIdList(ArrayList<String> pickIdList) {
		this.pickIdList = pickIdList;
	}

	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	@Override
	public String toString() {
		return "PickingStartVO [workerId=" + workerId + ", pickIdList=" + pickIdList + ", state_code=" + state_code
				+ "]";
	}

}
