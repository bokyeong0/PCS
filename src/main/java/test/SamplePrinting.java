package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

public class SamplePrinting {

	public static void main(String[] args) throws IOException, PrintException {
//	    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
	    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	    PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
//	    patts.add(Sides.DUPLEX);PD
	    PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
	    if (ps.length == 0) {
	        throw new IllegalStateException("No Printer found");
	    }
	    System.out.println("Available printers: " + Arrays.asList(ps));

	    for (PrintService printService : ps) {
	    	System.out.println(printService.getName());
	    }
    
	    PrintService myService = null;
	    for (PrintService printService : ps) {
	    	System.out.println(printService.getName());
	        if (printService.getName().equals("Canon MF210 Series")) {
	            myService = printService;
	            break;
	        }
	    }

	    if (myService == null) {
	        throw new IllegalStateException("Printer not found");
	    }

	    FileInputStream fis = new FileInputStream("d:/test.pdf");
	    Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
	    DocPrintJob printJob = myService.createPrintJob();
	    printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
	    fis.close();
	}

}
