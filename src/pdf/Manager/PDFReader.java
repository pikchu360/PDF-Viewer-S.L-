package pdf.Manager;
import javax.swing.SwingUtilities;
import pdf.GUI.UI;

public class PDFReader {								//Main Class the program.

	public static void main(String[] args) {			//Main Method the program.
		SwingUtilities.invokeLater(new Runnable() {			
			public void run() {
				new UI();
			}
		});

	}

}
