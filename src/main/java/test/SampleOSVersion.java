package test;
public class SampleOSVersion {

	public static void main(String[] args) {
		String osName = "";
		osName = System.getProperty("os.name");

		if(osName.toLowerCase().indexOf("window") >= 0){
			System.out.println("window");
		}else{
			System.out.println(osName.toLowerCase());
		}
	}

}
