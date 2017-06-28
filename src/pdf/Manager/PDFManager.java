package pdf.Manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

public class PDFManager {
	
	/* Atributos */
	private File file;
	private PDFParser pdfParser;
	private COSDocument cosDocument;
	private PDDocument pdDocument;
	private PDFTextStripper pdfTextStripper;
	private String text;
	private PDFRenderer pdfRenderer;
	private PDDocumentInformation pdDocumentInformation;
	
	/* Constructor */
	public PDFManager(String ruta) throws IOException{
		this.file = new File(ruta);
		this.pdfParser = null;
		this.cosDocument = null;
		this.pdDocument = null;
		this.pdfTextStripper = new PDFTextStripper();
		this.pdDocumentInformation = null;
	}
	
	/* Getters */
	public File getFile(){
		return this.file;
	}
	
	public PDFParser getPDFParser(){
		return this.pdfParser;
	}
	
	public COSDocument getCOSDocument(){
		return this.cosDocument;
	}
	
	public PDDocument getPDDocument(){
		return this.pdDocument;
	}
	
	public PDFTextStripper getPDFTextStripper(){
		return this.pdfTextStripper;
	}
	
	public String getText(){
		return this.text;
	}
	
	public PDFRenderer getPDFRenderer(){
		return this.pdfRenderer;
	}
	
	public PDDocumentInformation getPDDocumentInformation(){
		return this.pdDocumentInformation;
	}
	
	/* Metodo para extraer el texto del pdf */
	public String openPDF() throws FileNotFoundException, IOException{
		this.pdfParser = new PDFParser(new RandomAccessFile(getFile(),"r"));
		getPDFParser().parse();
		this.cosDocument = getPDFParser().getDocument();
		this.pdDocument = new PDDocument(getCOSDocument());
		this.text = this.pdfTextStripper.getText(getPDDocument());
		this.pdDocumentInformation = getPDDocument().getDocumentInformation();
		return getText();
	}
	
	/* Metodo para obtener la cantidad de palabras del pdf */
	public int getNumberOfWords(){
		StringTokenizer stringTokenizer = new StringTokenizer(getText());
		return stringTokenizer.countTokens();
	}
	
	/* Metodo para obtener la cantidad de paginas del pdf */
	public int getNumberOfPages(){
		return getPDDocument().getNumberOfPages();
	}
	
	/* Metodo para obtener el titulo del pdf */
	public String getTitle(){
		String title = getPDDocumentInformation().getTitle();
		if(title == null)
			title = "No title";
		return title;
	}
	
	/* Metodo para cambiar el titulo del pdf */
	public void setTitle(String title){
		if(title==null)
			title = "";
		getPDDocumentInformation().setTitle(title);
	}
	
	/* Metodo para obtener el titulo del pdf */
	public String getAuthor(){
		String author = getPDDocumentInformation().getAuthor();
		if(author == null)
			return "No author";
		return author;
	}
	
	/* Metodo para cambiar el autor del pdf */
	public void setAuthor(String author){
		if(author==null)
			author = "";
		getPDDocumentInformation().setAuthor(author);
	}
	
	/* Metodo para obtener el asunto del pdf */
	public String getSubject(){
		String author = getPDDocumentInformation().getSubject();
		if(author == null)
			return "No subject";
		return author;
	}
	
	/* Metodo para cambiar el asunto del pdf */
	public void setSubject(String subject){
		if(subject==null)
			subject = "";
		getPDDocumentInformation().setAuthor(subject);
	}
	
	/* Metodo para obtener la palabra clave del pdf */
	public String getKeywords(){
		String keywords = getPDDocumentInformation().getKeywords();
		if(keywords == null)
			return "No keywords";
		return keywords;
	}
	
	/* Metodo para cambiar la palabra clave del pdf */
	public void setKeywords(String keywords){
		if(keywords==null)
			keywords = "";
		getPDDocumentInformation().setKeywords(keywords);
	}
	
	/* Metodo para obtener el promedio de palabras del pdf */
	public double getAverageWords(){
		double averageWords = (double)getNumberOfWords()/getNumberOfPages();
		averageWords = Math.round(averageWords * 100d)/100d;
		return averageWords;
	}
	
	/* Metodo para cerrar el pdf */
	public void closePDF(){
		try {
			getPDDocument().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		PDFManager pdfManager = new PDFManager("D://vs-copyright.pdf");
		
		System.out.println(pdfManager.openPDF());
		System.out.println("Number of words: "+pdfManager.getNumberOfWords());
		System.out.println("Number of pages: "+pdfManager.getNumberOfPages());
		System.out.println("Title: "+pdfManager.getTitle());
		System.out.println("Author: "+pdfManager.getAuthor());
		System.out.println("Subject: "+pdfManager.getSubject());
		System.out.println("Keywords: "+pdfManager.getKeywords());
		System.out.println("Average words: "+pdfManager.getAverageWords());
		
		pdfManager.closePDF();
		
	}

}