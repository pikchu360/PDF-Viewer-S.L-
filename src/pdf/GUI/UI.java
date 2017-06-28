package pdf.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import javax.swing.JToolBar;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;

public class UI extends JFrame implements ActionListener {		//GUI the program PDFViewer
	private static final long serialVersionUID = 1L;
	private static final String PDF = "pdf";
	private JButton open;										//Button to run JFileChoser
	private JButton nextPage;									//Button Nest page to PDF 
	private JButton backPage;									//Button Back page to PDF
	private int pagina;											//PDF number page 
	private int paginas;										//PDF total number pages
	private int number;											//
	private PagePanel panel;									//
	private UI lectorPdf;										//
	private PDFFile pdffile;									//


	public UI() {
		lectorPdf = this;
		panel = new PagePanel();

		// Dimesion del frame y panel
		Dimension pantalla;
		Dimension cuadro;
		setSize(750, 690);
		pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		cuadro = this.getSize();
		this.setLocation(((pantalla.width - cuadro.width) / 2),
				(pantalla.height - cuadro.height) / 3);
		getContentPane().setLayout(new BorderLayout(0, 0));
		panel.setBackground(Color.WHITE);
		getContentPane().add(panel);
		repaint();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("PDFViewer");
		
		JToolBar tbHerramientas = new JToolBar();
		getContentPane().add(tbHerramientas, BorderLayout.NORTH);
		open = new JButton("Abrir");
		tbHerramientas.add(open);
		backPage = new JButton("<");
		backPage.setIcon(null);
		backPage.setVisible(false);
		getContentPane().add(backPage, BorderLayout.WEST);
		nextPage = new JButton(">");
		nextPage.setVisible(false);
		getContentPane().add(nextPage, BorderLayout.EAST);
		nextPage.addActionListener(this);
		backPage.addActionListener(this);
		
		// Botones y area texto
		open.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return PDF;
					}
					
					@Override
					public boolean accept(File f) {
						return f.isDirectory() || f.getName().endsWith(PDF);
					}
				});
				int select = chooser.showOpenDialog(lectorPdf);
				
				if(select == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						File file = new File(
							chooser.getSelectedFile().getAbsolutePath()); 
						// Ubicación del archivo pdf
						RandomAccessFile raf = new RandomAccessFile(file, "r");
						FileChannel channel = raf.getChannel();

						ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,channel.size());
						pdffile = new PDFFile(buf);
						paginas = pdffile.getNumPages();
						pagina = 1;
						viewPage();
						raf.close();
						backPage.setVisible(true);
						nextPage.setVisible(true);
					}catch (Exception e) {
						e.printStackTrace();
					}								
				}
				
			}
		});
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == nextPage) {
			pagina += 1;
			if (pagina > paginas) {
				pagina -= 1;
			}
			viewPage();
		}

		if (e.getSource() == backPage) {
			pagina -= 1;
			if (pagina > paginas || pagina < 1) {
				pagina = 1;
			}
			viewPage();
		}
	}
	
	private void viewPage(){
		PDFPage page = pdffile.getPage(pagina);
		panel.useZoomTool(false);
		panel.showPage(page);
		repaint();
		panel.repaint();
	}

	public void keyPressed(KeyEvent el) {
		if (el.getKeyCode() == KeyEvent.VK_A)
		{
			panel.removeAll();
		}
		panel.repaint();
		repaint();
	}
}
