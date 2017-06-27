package pdf.GUI;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdfViewer.PDFViewerBean;

public class UIPDF {

	//components the windows
	private PDFViewerBean visor;
	
	//Builders
	public UIPDF() throws PDFException {
		initialize();
		this.visor.loadPDF("/home/pikchu360/Documents/TP1.pdf");
	}
	
	//Methods and components
	public void initialize() {
		visor = new PDFViewerBean();
		visor.setVisible(true);
	}
	
	//Main methods
	public static void main(String [] args) throws PDFException {
		UIPDF v = new UIPDF();
	}
	
}
