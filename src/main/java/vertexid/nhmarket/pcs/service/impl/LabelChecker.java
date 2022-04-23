package vertexid.nhmarket.pcs.service.impl;

import java.util.HashMap;
import java.util.List;

import vertexid.nhmarket.pcs.service.PickHeaderVO;

public class LabelChecker {
	private HashMap<String, Label> map = new HashMap<>();

	/*
	 *  헤더 데이터 Map에 셋팅
	 * 
	 */
	public void setHeaderData(List<PickHeaderVO> pickHeaderVOs) {
		
		
		if (pickHeaderVOs == null) {
			return;
		}
		for (PickHeaderVO pickHeaderVO : pickHeaderVOs) {
			if (map.containsKey(pickHeaderVO.getOrg_order_no())) {
				Label label = map.get(pickHeaderVO.getOrg_order_no());
				label.count();
			} else {
				Label label = new Label();
				label.count();
				map.put(pickHeaderVO.getOrg_order_no(), label);
			}
		}
	}

	public String getLabelStr(String orgOrderNo) {
		if (map.containsKey(orgOrderNo)) {
			Label label = map.get(orgOrderNo);
			if (label.isOnly()) {
				return "O";
			} else {
				String result = "";

				if (label.isFisrt()) {
					label.setFisrt(false);
					// 처음
					result = "F";
				} else {
					if (label.isLast()) {
						// 마지막
						result = "L";
					} else {
						// 중간
						result = "B";
					}
				}

				label.minus();
				return result;
			}
		}

		return "";
	}
	
	/*
	 *  헤더 데이터 Map에 셋팅
	 * 
	 */
	public void setZoneCodeList(List<PickHeaderVO> pickHeaderVOs) {
		
		if (pickHeaderVOs == null) {
			return;
		}
		for (PickHeaderVO pickHeaderVO : pickHeaderVOs) {
			if (map.containsKey(pickHeaderVO.getZone_code())) {
				Label label = map.get(pickHeaderVO.getZone_code());
				label.count();
			} else {
				Label label = new Label();
				label.count();
				map.put(pickHeaderVO.getZone_code(), label);
			}
		}
	}
	
	class Label {
		// 라벨 하나 여부
		private boolean only = true;
		// 라벨 갯수
		private int count = 0;
		// 처음인지 여부
		private boolean fisrt = true;

		public void count() {
			count++;
			if (count > 1) {
				only = false;
			}
		}

		public boolean isLast() {
			if (count == 1) {
				return true;
			}
			return false;
		}

		public void minus() {
			if (count > 1) {
				count--;
			}
		}

		public boolean isOnly() {
			return only;
		}

		public void setOnly(boolean only) {
			this.only = only;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public boolean isFisrt() {
			return fisrt;
		}

		public void setFisrt(boolean fisrt) {
			this.fisrt = fisrt;
		}

	}
}
