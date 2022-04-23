package test;

import java.util.List;

import vertexid.nhmarket.pcs.service.ComCodeVO;
import vertexid.nhmarket.pcs.service.impl.PcsServiceImpl;

public class testString {

	public static void main(String[] args) throws Exception {

		
		
//		ArrayList<EgovMap>  bigZoneList = new ArrayList<EgovMap>() ;
//		bigZoneList.add((EgovMap) new EgovMap().put("comName","AD"));
//		bigZoneList.add((EgovMap) new EgovMap().put("comName","BD"));

		ComCodeVO comCodeVO = new ComCodeVO();
		comCodeVO.setCom_group_code("LARGE_ZONE_CODE");
		
		
		PcsServiceImpl ps = new PcsServiceImpl();
		List<ComCodeVO> bigZoneList = ps.selectComCodeList(comCodeVO);
		
//		bigZoneList. .indexOf("AD");
		for(ComCodeVO data : bigZoneList){
			if(data.getCom_code() == "AD"){
				System.out.println("대용량");
			}else{
				System.out.println("그냥임");
			}

		}
		
//		System.out.println(bigZoneList.indexOf("AD"));
		
//		EgovMap em = new EgovMap();
//		PickHeaderVO phv = new PickHeaderVO();
//		phv.setZone_code("AD");
		
		
//		 Sleep Test
//		int loopCount = 1000;
//		for(int i = 1 ; i < loopCount ; i++){
//			if(i % 200 ==0){
//				Thread.sleep(1000*3);
//				System.out.println("i:"+i + "/"+(i % 200 ==0));		
//			}
//		}
		
	}
	

}
