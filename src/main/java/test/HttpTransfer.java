/**
 * 
 */
package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author sjoh
 *
 */
public class HttpTransfer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		        // 한글의 경우 인코딩을 해야함.

		        // 서버쪽에서는 따로 decode할 필욘 없음. 대신 new String(str.getBytes("8859_1"), "UTF-8");로 인코딩을 변경해야함

		        String str;
		        
				str = URLEncoder.encode("한글", "UTF-8");
   
		        URL url = new URL("http://10.10.21.202:8080//comGroupCodeList.do");

		        // open connection

		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		        conn.setDoInput(true);            // 입력스트림 사용여부

		        conn.setDoOutput(true);            // 출력스트림 사용여부

		        conn.setUseCaches(false);        // 캐시사용 여부

		        conn.setReadTimeout(20000);        // 타임아웃 설정 ms단위

		        conn.setRequestMethod("POST");  // or GET

		        conn.setRequestProperty("content-type", "application/json; charset=utf-8");
		        
		        // Post로 Request하기
		        
		        OutputStream os = conn.getOutputStream();

		        OutputStreamWriter writer = new OutputStreamWriter(os);
		        
		        writer.write("ABCD");

		        writer.close();

		        os.close();

		        // Response받기

		       StringBuffer sb =  new StringBuffer();

		        BufferedReader br = new BufferedReader( new InputStreamReader(conn.getInputStream()));

		        for(;;){

		        String line =  br.readLine();

		        if(line == null) break;

		        sb.append(line+"\n");

		        }

		        br.close();

		        conn.disconnect();

		        String getXml = sb.toString();

		        System.out.println(sb.toString());        
	}

}
