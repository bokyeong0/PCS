package vertexid.nhmarket.pcs.cmmn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	/**
	 * 오늘 날짜 비교
	 * 
	 * @param inputDate
	 *            Date 객체
	 * @return
	 */
	public static boolean isToday(Date inputDate) {
		if (inputDate == null) {
			return false;
		}

		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		String todayTime = mSimpleDateFormat.format(new Date()); // 현재시간
		String inputTime = mSimpleDateFormat.format(inputDate); // 비교 시간

		return todayTime.equals(inputTime);
	}

	/**
	 * 오늘 날짜 비교
	 * 
	 * @param inputDate
	 *            'yyyyMMdd' 형식의 스트링값
	 * @return
	 */
	public static boolean isToday(String inputDate) {
		if (inputDate == null) {
			return false;
		}

		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String todayTime = mSimpleDateFormat.format(new Date()); // 현재시간

		return todayTime.equals(inputDate);
	}

	/**
	 * 특정 형식의 날짜 포맷 변경
	 * 
	 * @param strDate
	 *            형식(20160929)
	 * @return
	 */
	public static String parseDate(String strDate) {
		if (strDate == null) {
			return "";
		}

		if (strDate.length() < 8) {
			return "";
		}

		return strDate.substring(0, 4) + "." + strDate.substring(4, 6) + "." + strDate.substring(6, 8);

	}

}
