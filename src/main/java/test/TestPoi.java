package test;

import java.io.File;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class TestPoi {
	public static void main(String[] args) throws Exception {

		Document doc = org.apache.poi.hssf.converter.ExcelToHtmlConverter.process(new File("C:/Temp/test1.xls"));

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File("C:/Temp/test.html"));
		Source input = new DOMSource(doc);

		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "html");
		
		transformer.setOutputProperty(OutputKeys.METHOD, "html");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		
		transformer.transform(input, output);
//		transformer.transform(input, output);

		System.out.println("end");

	}
}
