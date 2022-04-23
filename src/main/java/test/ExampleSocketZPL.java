package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class ExampleSocketZPL {
	
	public static void main(String[] args) {

		// 소켓 통신용
		SocketUtil socket = new SocketUtil();
		
		// 소켓 설정정보
		String ip="27.125.1.249";
		int port=9100;
		HashMap<Integer,String> dataMap = new HashMap<Integer, String>();
		boolean isOnCheck;
		try {
			int i = 1;
			while(true){
				try{
					// 기본 프린터 상태 조회 체크 처리 
					socket.connect(ip, port);
					socket.sendMessage(getZplMessagePringStatus());
					dataMap = socket.receiveMessage();
					socket.disconnect();
					
					isOnCheck = checkPrintStatus(dataMap);
					
					System.out.print("프린트여부:" + isOnCheck);
					
					// 기본 프린터 출력 가능시
					if(isOnCheck){
						socket.connect(ip, port);
						socket.sendMessage(getZplMessage(String.valueOf(i)));
						socket.disconnect();
						System.out.println(" 1번프린터 ZPL:"+getZplMessage(String.valueOf(i)));
					}else{
						System.out.println(" 2번프린터 ZPL:"+getZplMessage(String.valueOf(i)));
					}
					i++;
					// 오류처리를 위한 타임
					Thread.sleep(10*1000);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
//			for(int i = 0 ; i < 20 ; i++){
//				
//				 //기본 프린터 상태 조회 체크 처리 
//				socket.connect(ip, port);
//				socket.sendMessage(getZplMessagePringStatus());
//				dataMap = socket.receiveMessage();
//				socket.disconnect();
//				
//				isOnCheck = checkPrintStatus(dataMap);
//				
//				System.out.print("프린트여부:" + isOnCheck);
//				
//				// 기본 프린터 출력 가능시
//				if(isOnCheck){
//					socket.connect(ip, port);
//					socket.sendMessage(getZplMessage(String.valueOf(i)));
//					socket.disconnect();
//					System.out.println("ZPL:"+getZplMessage(String.valueOf(i)));
//				}
//				
//				// 오류처리를 위한 타임
//				Thread.sleep(10*1000);
//			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	// 프린터 장애 체크 로직
	private static boolean checkPrintStatus(HashMap<Integer, String> dataMap) {
		/*
		 *
		 *	[1 Line]
		 	aaa = communication (interface) settings*
>			b = paper out flag (1 = paper out)
>			c = pause flag (1 = pause active)
			dddd = label length (value in number of dots)
			eee = number of formats in receive buffer
>			f = buffer full flag (1 = receive buffer full)
			g = communications diagnostic mode flag (1 = diagnostic mode active)
			h = partial format flag (1 = partial format in progress)
			iii = unused (always 000)
			j = corrupt RAM flag (1 = configuration data lost)
			k = temperature range (1 = under temperature)
			l = temperature range (1 = over temperature)
			
			[2 Line]
			mmm = function settings*
			n = unused
>			o = head up flag (1 = head in up position)
?			p = ribbon out flag (1 = ribbon out)
?			q = thermal transfer mode flag (1 = Thermal Transfer Mode selected)
			r = Print Mode
			0 = Rewind
			1 = Peel-Off
			2 = Tear-Off
			3 = Cutter
			4 = Applicator
			s = print width mode
			t = label waiting flag (1 = label waiting in Peel-off Mode)
			uuuuuu
			uu
			= labels remaining in batch
			v = format while printing flag (always 1)
			www = number of graphic images stored in memory
		 */		
		
		boolean isOnCheck = true;
		String[] rowData1  = dataMap.get(0).split(",");
		String[] rowData2  = dataMap.get(1).split(",");
//		String[] rowData3  = dataMap.get(2).split(",");
		
//		System.out.println("===========================");
//		System.out.println(rowData1[1] + "/" + rowData1[2] + "/" + rowData1[5]);
//		System.out.println("===========================");
//		System.out.println(rowData2[2] + "/" + rowData2[3] + "/" + rowData2[4]);
//		System.out.println("===========================");
//		System.out.println("용지:"+rowData1[1] + "/메모리:" + rowData1[5] + "/오픈헤드:" + rowData2[2]);
//		System.out.println("===========================");
		
		// 용지가 없는 경우
		if("1".equals(rowData1[1])){
			isOnCheck = false;
		}
		
		// 일시정지인 경우
		if("1".equals(rowData1[2])){
			isOnCheck = false;
		}
		
		// 메모리가 full 인 경우
		if("1".equals(rowData1[5])){
			isOnCheck = false;
		}
		
		// 프린터가 열려있는경우
		if("1".equals(rowData2[2])){
			isOnCheck = false;
		}
		return isOnCheck;
	}

	/**
	 * ZPL 테스트용 메시지
	 * @return
	 */
	public static String getZplMessagePringStatus() {
		
		/*
		 *
		 *	[1 Line]
		 	aaa = communication (interface) settings*
>			b = paper out flag (1 = paper out)
>			c = pause flag (1 = pause active)
			dddd = label length (value in number of dots)
			eee = number of formats in receive buffer
>			f = buffer full flag (1 = receive buffer full)
			g = communications diagnostic mode flag (1 = diagnostic mode active)
			h = partial format flag (1 = partial format in progress)
			iii = unused (always 000)
			j = corrupt RAM flag (1 = configuration data lost)
			k = temperature range (1 = under temperature)
			l = temperature range (1 = over temperature)
			
			[2 Line]
			mmm = function settings*
			n = unused
>			o = head up flag (1 = head in up position)
?			p = ribbon out flag (1 = ribbon out)
?			q = thermal transfer mode flag (1 = Thermal Transfer Mode selected)
			r = Print Mode
			0 = Rewind
			1 = Peel-Off
			2 = Tear-Off
			3 = Cutter
			4 = Applicator
			s = print width mode
			t = label waiting flag (1 = label waiting in Peel-off Mode)
			uuuuuu
			uu
			= labels remaining in batch
			v = format while printing flag (always 1)
			www = number of graphic images stored in memory
		 */
		StringBuffer zplMessage = new StringBuffer();
		
		zplMessage.append("~HS");
		return zplMessage.toString();
	}
	

	

	/**
	 * ZPL 테스트용 메시지
	 * @return
	 */
	public static String getZplMessage(String num) {
		StringBuffer zplMessage = new StringBuffer();

		String sData1 = "Only"; // 라벨상태 (F,L,O)
		String sData2 = "001"; // 단축주문번호 (3자리)
		String sData3 = "1"; // 트레이번호 (유동)
		String sData4 = "20160714-000154"; // 원주문번호 ( [9자리]-[6자리])
		String sData5 = "1"; // 회차
		String sData6 = "AB"; // Zone (2자리 | AA(후방/상온), AB(후방/냉장), AB(후방/냉동), AD(후방/대용량), BA(매장/상온), BB(매장/냉장), BC(매장/냉동),  BD(매장/대용량), CA(대면/축산) )
		String sData7 = "20160803"; // 배송일
		String sData8 = "영등포구"; // 배송구명
		String sData9 = "홍*길"; // 고객명
		String sData10 = "123456789012"; // 고객명
		String sData11 = "XY"; // 회차
		
		sData3 = num;

		zplMessage.append("^XA");
		// Font 등 설정 (LH 시작위치, PW 용지너비)
		zplMessage.append("^CWH,E:KFONT3.FNT^FS  (폰트명) ");
		zplMessage.append("^CI28 ");
		zplMessage.append("^LH0,0 ");		
		zplMessage.append("^PW900 ");
		zplMessage.append("^FO700, 80^AHR,50,45^FD" + sData1 + "^FS ");
		zplMessage.append("^FO500, 80^AHR,170,150^FD" + sData2 + "-" + sData3 + "^FS ");

		// 바코드 2차
		zplMessage.append("^FO550, 650^BY4^B2R,150,Y,N,N^FD" + sData10 + "^FS  바코드용 2차");
		zplMessage.append("^FO450, 100^AHR,50,45^FD[" + sData4 + "]^FS ");
		zplMessage.append("^FO210, 80^AHR,170,130^FD" + sData5 + sData11 + "-"	+ sData6 + "^FS ");
		zplMessage.append("^FO310, 580^AHR,150,140^FD" + sData8 + "^FS ");		
		zplMessage.append("^FO140, 580^AHR,150,140^FD" + sData9 + "^FS ");		
		zplMessage.append("^FO135, 100^AHR,60,50^FD"
										+  (sData7).substring(0,4) 
										+ "/" + (sData7).substring(4,6) 
										+ "/" + (sData7).substring(6,8) 
										+ " " + ""+ "^FS ");
		zplMessage.append("^XZ");
//		System.out.println(assetSb.toString());
		return zplMessage.toString();
	}
	
}	
	class SocketUtil {
		
		private Socket socket;
		private BufferedInputStream bis;
		private BufferedOutputStream bos;

		/**
		 * 소켓 접속
		 * @param host
		 * @param port
		 * @throws Exception 
		 */
		public void connect(String host, int port) throws Exception {
			socket = new Socket(InetAddress.getByName(host), port);
			
			bis = new BufferedInputStream(socket.getInputStream());
			bos = new BufferedOutputStream(socket.getOutputStream());
		}
	
		/**
		 * 소켓 접속
		 * @param host
		 * @param port
		 * @param localPort
		 * @throws Exception 
		 */
		public void connect(String host, int port, int localPort) throws Exception {
			socket = new Socket(InetAddress.getByName(host), port, InetAddress.getLocalHost(), localPort);
		}
	
		/**
		 *  요청 메시지
		 * @param message
		 * @throws IOException
		 */
		
		public void sendMessage(String message) throws IOException {
			bos.write(message.getBytes());
			bos.flush();
		}
	
		/**
		 * 응답 메시지
		 * @return
		 * @throws IOException
		 */
		public HashMap<Integer, String> receiveMessage() throws IOException {
			
			HashMap<Integer, String> dataMap = new HashMap<Integer, String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(bis));
			// Host Status String 1~3
			for(int i = 0 ; i < 3 ; i++){
				dataMap.put(i,br.readLine());
			}
			br.close();
			System.out.println("receive:"+dataMap);
			return dataMap;
		}
	
		/**
		 * 접속종료
		 * @throws IOException
		 */
		
		public void disconnect() throws IOException {
			bis.close();
			bos.close();
			socket.close();
		}

	}
