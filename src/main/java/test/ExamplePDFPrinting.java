package test;

import java.awt.print.PrinterJob;
import java.io.File;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

public class ExamplePDFPrinting {

	public static void main(String args[]) {

		// PDDocument document = PDDocument.load(new File("d:/test.pdf"));

		PrintService myPrintService = findPrintService("Canon MF210 Series");

		PrinterJob job = PrinterJob.getPrinterJob();
		try {
			for (int i = 0; i < 1; i++) {
				PDDocument document = PDDocument.load(new File("C:/ireport/specifications.pdf"));
				job.setPageable(new PDFPageable(document));
				job.setPrintService(myPrintService);
				job.print();

			}
		} catch (Exception e) {
			System.out.println("에러 = " + e);
		}

		// savePdf();

		// PDDocument document = PDDocument.load(new File("d:/test.pdf"));
		//
		// // 윈도우 기본 프린터 가져옴
		// PrinterJob job = PrinterJob.getPrinterJob();
		// job.setPageable(new PDFPageable(document));

		// try {
		// job.print();
		// } catch (PrinterException pe) {
		// }

		System.out.println("end");

	}

	private static PrintService findPrintService(String printerName) {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService printService : printServices) {
			if (printService.getName().trim().equals(printerName)) {
				return printService;
			}
		}
		return null;
	}

	// public static void savePdf() throws IOException, DocumentException{
	// FileInputStream input_document = new FileInputStream(new
	// File("d:\\test.xls"));
	// // Read workbook into HSSFWorkbook
	// HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
	// // Read worksheet into HSSFSheet
	// HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
	// // To iterate over the rows
	// Iterator<Row> rowIterator = my_worksheet.iterator();
	// //We will create output PDF document objects at this point
	// Document iText_xls_2_pdf = new Document();
	//
	// PdfWriter.getInstance(iText_xls_2_pdf, new
	// FileOutputStream("d:\\Excel2PDF_Output.pdf"));
	//
	// iText_xls_2_pdf.open();
	//// //we have two columns in the Excel sheet, so we create a PDF table with
	// two columns
	//// //Note: There are ways to make this dynamic in nature, if you want to.
	// PdfPTable my_table = new PdfPTable(9);
	//// //We will use the object below to dynamically add new data to the table
	// PdfPCell table_cell;
	// //Loop through rows.
	// while(rowIterator.hasNext()) {
	// Row row = rowIterator.next();
	// Iterator<Cell> cellIterator = row.cellIterator();
	// while(cellIterator.hasNext()) {
	// Cell cell = cellIterator.next(); //Fetch CELL
	// switch(cell.getCellType()) { //Identify CELL type
	// //you need to add more code here based on
	// //your requirement / transformations
	// case Cell.CELL_TYPE_STRING:
	// //Push the data from Excel to PDF Cell
	// table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
	// //feel free to move the code below to suit to your needs
	// my_table.addCell(table_cell);
	// break;
	// }
	// //next line
	// }
	//
	// }
	// //Finally add the table to PDF document
	// iText_xls_2_pdf.add(my_table);
	// iText_xls_2_pdf.close();
	// //we created our pdf file..
	// input_document.close(); //close xls
	// }
}