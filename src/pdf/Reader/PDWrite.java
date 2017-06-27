package pdf.Reader;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;


public class PDWrite {

	//Attributes
	private File fArchivo;
	private PDDocument pdDocumento;
	
	//Builds
	public PDWrite(String path) throws InvalidPasswordException, IOException{
		leerPDF(path);
		System.out.println();
	}
	
	//Getters and setters
	public File getArchivo(){
		return this.fArchivo;
	}
	public PDDocument getDocumento(){
		return this.pdDocumento;
	}
	
	//General Methods
	public void leerPDF(String ruta) throws InvalidPasswordException, IOException{
		this.fArchivo = new File(ruta);
		this.pdDocumento = PDDocument.load(fArchivo);
	}
	
	
	//Test Class PDFWrite
	public static void main(String [] args) throws InvalidPasswordException, IOException{
		PDWrite pdf = new PDWrite("/home/pikchu360/Documents/TP1.pdf");
		System.out.println("Hola");
	}
}
