/**
 * 
 */
package test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SampleEPLLablePrint {
	
	/*
	 * 출력 설정 
	 */
	public static StringBuffer assetLabelDiversion() {
		StringBuffer assetSb = new StringBuffer();
		String font="8";
		
		assetSb.append("N");
		assetSb.append("\r\n").append("Q320,24");
		assetSb.append("\r\n").append("R200,0");
		assetSb.append("\r\n").append("S2");
		assetSb.append("\r\n").append("D9");
		assetSb.append("\r\n").append("ZT");
		assetSb.append("\r\n").append("A20,10,0,"+font+",1,1,N,\"10-19 11:44:33\"");
		assetSb.append("\r\n").append("A470,10,0,"+font+",1,1,N,\"20161019999983010002\"");
		assetSb.append("\r\n").append("A15,35,0,"+font+",1,1,N,\"   02-****-3070   신용카드(비승인)///\"");
		assetSb.append("\r\n").append("A20,60,0,"+font+",1,1,N,\"도미노피자 개발 포장주문\"");
		assetSb.append("\r\n").append("A450,85,0,"+font+",1,1,N,\"\"");
		assetSb.append("\r\n").append("A20,115,0,"+font+",1,2,N,\"(L)[씬]와규 앤 비스테카(32,900)\"");
		assetSb.append("\r\n").append("A765,300,3,"+font+",2,1,N,\"[씬]와규 앤 비스테카(L)\"");
		assetSb.append("\r\n").append("A380,115,0,"+font+",1,1,N,\"판매가:32,900\"");
		assetSb.append("\r\n").append("A380,140,0,"+font+",1,1,N,\"금액할인:0\"");
		assetSb.append("\r\n").append("A380,165,0,"+font+",1,1,N,\"할인가:32,900\"");
		assetSb.append("\r\n").append("A380,215,0,"+font+",1,1,N,\"최종가:32,900\"");
		assetSb.append("\r\n").append("A380,240,0,"+font+",1,1,N,\"공급가액:29,909\"");
		assetSb.append("\r\n").append("A380,265,0,"+font+",1,1,N,\"부가세:2,991\"");
		assetSb.append("\r\n").append("A380,290,0,"+font+",1,1,N,\"실제판매가:32,900\"");
		assetSb.append("\r\n").append("A280,240,0,9,3,3,N,\"1\"");
		assetSb.append("\r\n").append("A670,45,0,"+font+",1,2,N,\"1/1\"");
		assetSb.append("\r\n").append("A20,165,0,"+font+",1,1,N,\"<영양 성분 표기>\"");
		assetSb.append("\r\n").append("A20,190,0,"+font+",1,1,N,\"2조각기준 (181g) / 8조각 (724g)\"");
		assetSb.append("\r\n").append("A20,215,0,"+font+",1,1,N,\"열량 427kcal  당류 13g\"");
		assetSb.append("\r\n").append("A20,240,0,"+font+",1,1,N,\"단백질 20g  포화지방 10g\"");
		assetSb.append("\r\n").append("A20,265,0,"+font+",1,1,N,\\3344\\");
		assetSb.append("\r\n").append("P1,1");

		
		
//		assetSb.append("N");
//		assetSb.append("\r\n").append("Q320,24");
//		assetSb.append("\r\n").append("R0,0");
//		assetSb.append("\r\n").append("S2");
//		assetSb.append("\r\n").append("D9");
//		assetSb.append("\r\n").append("ZT");
//		assetSb.append("\r\n").append("A200,165,0,"+font+",1,1,N,\\0041\\");
//		assetSb.append("\r\n").append("P1,1");
		
//		한글출력		
//		assetSb.append("N");
//		assetSb.append("\r\n").append("R200,0");
//		assetSb.append("\r\n").append("A50,0,4,1,1,1,N,\"예제 1\"");
//		assetSb.append("\r\n").append("A50,50,4,2,1,1,N,\"예제 2\"");
//		assetSb.append("\r\n").append("A50,100,4,3,1,1,N,\"예제 3\"");
//		assetSb.append("\r\n").append("A50,150,4,4,1,1,N,\"예제 4\"");
//		assetSb.append("\r\n").append("A50,200,4,5,1,1,N,\"예제 5\"");
//		assetSb.append("\r\n").append("A50,300,4,3,2,2,R,\"예제 6\"");
//		assetSb.append("\r\n").append("A50,400,4,8,1,1,N,\"예제 6.1\"");
//		assetSb.append("\r\n").append("P1,1");		

		
//		영문 출력		
//		assetSb.append("N");
//		assetSb.append("\r\n").append("R200,0");
//		assetSb.append("\r\n").append("A50,0,4,1,1,1,N,\"Example 1\"");
//		assetSb.append("\r\n").append("A50,50,4,2,1,1,N,\"Example 2\"");
//		assetSb.append("\r\n").append("A50,100,4,3,1,1,N,\"Example 3\"");
//		assetSb.append("\r\n").append("A50,150,4,4,1,1,N,\"Example 4\"");
//		assetSb.append("\r\n").append("A50,200,4,5,1,1,N,\"Example 5\"");
//		assetSb.append("\r\n").append("A50,300,4,3,2,2,R,\"Example 6\"");
//		assetSb.append("\r\n").append("A50,400,4,1,1,1,N,\"Example 6.1\"");
//		assetSb.append("\r\n").append("P1,1");		

		
		
//		프린터 폰트 정보 출력
//		assetSb.append("N");
//		assetSb.append("\r\n").append("EI");		
//		assetSb.append("\r\n").append("P1,1");
//		
		
		
		
		System.out.println(assetSb);
		return assetSb;
		
		
		/*
		sPrint.Append("^XA ")
        .Append("^SEE:UHANGUL.DAT^FS                                      (UHANGUL) ")
        .Append("^CWH,E:KFONT3.FNT^FS                                     (폰트명) ")
        .Append("^CI28                                     ")
        .Append("^FPH,0.5^FO12,18,0^AHN,22,15^FD공정^FS                          (공정명) ")
        .Append("^FPH,0.5^FO63,18,0^AHN,22,15^FD" + dataGridView1[1, i].Value.ToString() + "_" + dataGridView1[2, i].Value.ToString() + " " + dataGridView1[3, i].Value.ToString() + "^FS              (공정명) ")
        .Append("^FO15,70,0^BY1^^BXN,3,50^" + dataGridView1[9, i].Value.ToString() + "^FS                 (Data Matrix 내용)  ")
        .Append("^FPH,0.5^FO85,52,0^AHN,22,15^FD장비명: " + dataGridView1[8, i].Value.ToString() + "^FS               (장비명) ")
        .Append("^FPH,0.7^FO85,77,0^AHN,22,15^FD자산번호: " + dataGridView1[5, i].Value.ToString() + "^FS        (자산번호) ")
        .Append("^FPH,0.7^FO85,100,0^AHN,22,15^SNSN: " + dataGridView1[9, i].Value.ToString() + "^FS        (SN) ")
        .Append("^FPH,0.7^FO85,125,0^AHN,22,15^FD발행일: " + getDate() + "^FS    (발행일자/체크일자) ")
        .Append("^FPH,0.5^FO12,158,0^AHN,22,15^FD공급:^FS              (납품회사) ")
        .Append("^FPH,0.5^FO50,158,0^AHN,22,15^FD" + dataGridView1[10, i].Value.ToString() + "^FS              (납품회사) ")
        .Append("^FPH,0.3^FO193,158,0^AHN,22,15^FD입고:^FS           (입고) ")
        .Append("^FPH,0.5^FO233,158,0^AHN,22,15^FD" + getDate() + "^FS           (입고일자) ")
        .Append("^FO0,45,0 ")
        .Append("^GB345,0,2,20^FS    (첫번째 라인) ")
        .Append("^FO0,148,0 ")
        .Append("^GB345,0,2,20^FS    (두번째 라인) ")
        .Append("^FO55,0,0 ")
        .Append("^GB0,45,2,20^FS    (첫번째 가운데 세로선)  ")
        .Append("^FO185,148,0 ")
        .Append("^GB0,45,2,20^FS    (두번째 가운데 세로선)  ")
        .Append("^PQ1,0,0,N       (발행 수량) ")
        .Append("^XZ ");		
        */
	}
	

	/*
	 * 실행
	 */
	public static void main(String[] args) {

		int result = 0;
		
		// 셋팅 설정 
//		String ip="192.168.1.200";
		String ip="27.125.1.249";
		int port=9100;
		
		// 출력처리
		result = assetPrinting(ip , port);
		
		System.out.println("Printing Result : " + result );
	}

	/*
	 * 출력 처리
	 */
	public static int assetPrinting(String ip, int port) {
		int result = 0;
		StringBuffer sb = assetLabelDiversion();
		
		try{
			result = connection(ip, port, sb);
		}catch (Exception e) {
			e.printStackTrace();			
		}
		return result;
	}



	/*
	 *  Socket 플린팅
	 */
	public static int connection(String ip, int port, StringBuffer sb) {
		Socket sc = null;
		OutputStream out = null;
		int result = 1;

		try {
			sc = new Socket(ip, port);
			boolean boolResult = sc.isConnected();
			if (boolResult == true) {
				out = sc.getOutputStream();
				printing(out, sb);
				sc.close();
				out.close();
			} else {
				sc.close();
			}
		} catch (IOException e) {
			result = 0;
			e.printStackTrace();
		} catch (Exception ex) {
			result = 0;
			ex.printStackTrace();
		} finally {
			System.out.println("connection Finally");
		}
		System.out.println("result====>" + result);
		return result;
	}

	public static void printing(OutputStream out, StringBuffer sb) {
		DataOutputStream dos = new DataOutputStream(out);
		try {
			dos.writeUTF(sb.toString());
//			dos.writeChars(sb.toString());
//			System.out.println(sb.toString().getBytes("UTF-8"));
//			dos.writeBytes(sb.toString());

			dos.close();
			Thread.sleep(1000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
