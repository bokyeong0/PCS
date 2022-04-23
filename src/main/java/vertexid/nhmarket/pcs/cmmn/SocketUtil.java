/**
 * 
 */
package vertexid.nhmarket.pcs.cmmn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

/**
 * @Class Name : SocketUtil.java
 * @Description : SocketUtil Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2016.10.11           최초생성
 *
 * @author 김두환
 * @since 2016. 10.11
 * @version 0.1
 * @see
 *
 *  Copyright (C) by dhkim All right reserved.
 */
public class SocketUtil{
	
		// 소켓 통신용 변수
		private Socket socket = null;
		private InputStreamReader inStream = null;
		private DataOutputStream outStream = null;
	
		// 타임아웃 셋팅 socket
		private int timeOut = 3000
			, streamTimeOut = 3000;
	
		/**
		 * Connect 접속 소켓
		 * @param connectSocket 소켓
		 * @param host 			- IP정보
		 * @param port			- PORT 정보
		 * @throws Exception
		 */
		public void connect(String host, int port) throws Exception {
			socket = new Socket();
			this.socket.setSoTimeout(this.streamTimeOut);
			this.socket.connect(new InetSocketAddress(host, port), this.timeOut);
			this.inStream = new InputStreamReader(this.socket.getInputStream());
			this.outStream = new DataOutputStream(this.socket.getOutputStream());
		}
		
		/**
		 *  요청 메시지
		 * @param message
		 * @throws IOException
		 */
		
		public void sendMessage(String message) throws IOException {
			this.outStream.writeUTF(message);
			this.outStream.flush();
		}
	
		/**
		 * 응답 메시지
		 * @param readLineSize
		 * @return
		 * @throws IOException
		 */
		public HashMap<Integer, String> receiveMessage(int readLineSize) throws IOException {
			
			HashMap<Integer, String> dataMap = new HashMap<Integer, String>();
			BufferedReader br = new BufferedReader(this.inStream);
			
			// Host Status String 1~3
			for(int readLineIndex = 0 ; readLineIndex < readLineSize ; readLineIndex++){
				dataMap.put(readLineIndex, br.readLine());
			}
			br.close();
			return dataMap;
		}
	
		/**
		 * 접속종료
		 * @throws IOException
		 */
		public void disconnect() throws IOException {
			this.inStream.close();
			this.outStream.close();
			this.socket.close();
		}
		
		/**
		 * 접속여부 확인
		 * @return
		 * @throws IOException
		 */
		public boolean isConnected() throws IOException {
			return this.socket.isConnected();
		}		
}
