/**
 * 
 */
package vertexid.nhmarket.pcs.cmmn.web;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author doohwan
 *
 */
public class ZPLLablePrint {

	/*
	 * 출력 설정 
	 */
	public StringBuffer assetLabelDiversion() {
		StringBuffer assetSb = new StringBuffer();

		
		String sData1 = "L"; // 라벨상태 (F,L,O)
		String sData2 = "001"; // 단축주문번호 (3자리)
		String sData3 = "3"; // 트레이번호 (유동)
		String sData4 = "20160714-000154"; // 원주문번호 ( [9자리]-[6자리])
		String sData5 = "1"; // 회차
		String sData6 = "AB"; // Zone (2자리 | AA(후방/상온), AB(후방/냉장), AB(후방/냉동), AD(후방/대용량), BA(매장/상온), BB(매장/냉장), BC(매장/냉동),  BD(매장/대용량), CA(대면/축산) )
		String sData7 = "20160714"; // 배송일
		String sData8 = "관악구"; // 배송구명
		String sData9 = "양*준"; // 고객명

		
		assetSb.append("^XA");
		
		// Font 등 설정
		assetSb.append("^SEE:UHANGUL.DATT^FS  UHANGUL " );		
		assetSb.append("^CWH,E:KFONT3.FNT^FS  (폰트명) " );		
		assetSb.append("^CI28 " );
		assetSb.append("^FO520, 80^AHR,50,45^FD"+sData1+"^FS ");		
		assetSb.append("^FO340, 120^AHR,120,110^FD"+sData2+"-"+sData3+"^FS ");
        assetSb.append("^FO340, 600^BY4^B2R,150,Y,N,N^FD123456789012^FS  바코드용 ");
 		assetSb.append("^FO300, 120^AHR,50,45^FD["+sData4+"]^FS ");
		assetSb.append("^FO130, 120^AHR,120,110^FD"+sData5+"-"+sData6+"^FS ");
		assetSb.append("^FO10,  120^AHR,60,50^FD"+sData7+" "+sData8+" "+sData9+"^FS ");

		/*
		assetSb.append("^FO80,60^AHB,40,35^FD"+sData1+"^FS ");
		assetSb.append("^FO700, 61^AHB,40,35^FD요청자^FS ");
		assetSb.append("^FO95, 180^AHB,40,35^FD배송지^FS ");
		assetSb.append("^FO115, 299^AHB,45,40^FD품목^FS ");
		assetSb.append("^FO75, 418^AHB,45,40^FD로케이션^FS ");
		assetSb.append("^FO720, 418^AHB,45,40^FD수량^FS ");
		assetSb.append("^FO75, 580^AHB,40,35^FD출고번호^FS ");
		assetSb.append("^FO280, 61^AHB,40,35^FD" + sData1 + "^FS         (요청일자)");
		assetSb.append("^FO860, 61^AHB,40,35^FD" + sData2 + "^FS         (요청자)");
		assetSb.append("^FO280, 180^AHB,40,35^FD" + sData3 + "^FS         (배송지)");
		assetSb.append("^FO280, 299^AHB,40,35^FD" + sData4 + "-" + sData5 + "^FS         (품목)");
		assetSb.append("^FO280, 418^AHB,40,35^FD" + sData6 + "^FS         (로케이션)");
		assetSb.append("^FO860, 418^AHB,40,35^FD" + sData7 + "^FS         (수량)");
		assetSb.append("^FO350, 515^BY3^BCN,140,N,N,N^FD" + sData8 + "^FS   (출고번호)");
		assetSb.append("^FO500, 665^AHN,35,35^FD" + sData8 + "^FS   (출고번호)");
		assetSb.append("^FO40,10,0");
		assetSb.append("^GB1050,700,2,B,0^FS      (전체 상자)");
		assetSb.append("^FO260,10");
		assetSb.append("^GB00,700,2,B,0^FS           (세로선1-1)");
		assetSb.append("^FO670, 10");
		assetSb.append("^FO840, 10");
		assetSb.append("^FO670, 367");
		assetSb.append("^FO840, 367");
		assetSb.append("^FO40,129");
		assetSb.append("^FO40,248");
		assetSb.append("^GB1050, 0, 2, B, 0^FS        (가로선1-2)");
		assetSb.append("^FO40,367");
		assetSb.append("^FO40,485");
		assetSb.append("^GB1050, 0, 2, B, 0^FS        (가로선1-4)");
		*/
		
		assetSb.append("^XZ");

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
	public void main(String[] args) {

		int result = 0;
		
		// 셋팅 설정 
		String ip="10.10.21.146";
		int port=9100;
		
		// 출력처리
		result = assetPrinting(ip , port);
		
		System.out.println("Printing Result : " + result );
	}
	 */

	/*
	 * 출력 처리
	 */
	public int assetPrinting(String ip, int port) {
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
	public int connection(String ip, int port, StringBuffer sb) {
		Socket sc = null;
		OutputStream out = null;
		int result = 1;
		
		// finally에서 close 처리하는 방향으로 고려
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

	public void printing(OutputStream out, StringBuffer sb) {
		DataOutputStream dos = new DataOutputStream(out);
		try {
			dos.writeUTF(sb.toString());
			dos.close();
			Thread.sleep(1000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
}
