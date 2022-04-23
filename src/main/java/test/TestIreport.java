package test;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import test.vo.DataBean;
import test.vo.DataBeanList;

public class TestIreport {

	public static void main(String[] args) throws Exception {

		String sourceFileName = "c:/ireport/report.jasper";
		String destFileName = "c:/ireport/sample_report.pdf";

		String printFileName = null;
		DataBeanList DataBeanList = new DataBeanList();
		ArrayList<DataBean> dataList = DataBeanList.getDataBeanList();
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
		try {
			printFileName = JasperFillManager.fillReportToFile(sourceFileName, null, beanColDataSource);

			if (printFileName != null) {

				System.out.println("printFileName : " + printFileName);

				JasperExportManager.exportReportToPdfFile(printFileName, destFileName);

			}
		} catch (JRException e) {
			e.printStackTrace();
		}

		System.out.println("end");
	}

}
