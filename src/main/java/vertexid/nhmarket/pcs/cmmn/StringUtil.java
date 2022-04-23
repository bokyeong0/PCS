package vertexid.nhmarket.pcs.cmmn;

public class StringUtil {

	/**
	 * 문자열의 길이에 따라 줄임말 표시
	 * @param text
	 * @param length
	 * @return
	 */
	public static String ellipsis(String text, int length){
		String ellStr = "...";
		String outStr = text;
		
		if(text.length()>0 && length>0){
			if(text.length()>length){
				outStr = text.substring(0, length);
				outStr += ellStr;
			}
		}
		return outStr;		
	}
	
	/**
	 * 전체 길이에서 뒷자리부터 자름
	 * @param text
	 * @param len
	 * @return
	 */
	public static String cutBakSubStr(String text, int len){		
		String result = text.substring(text.length()-len, text.length());
		return result;
	}
	
}
