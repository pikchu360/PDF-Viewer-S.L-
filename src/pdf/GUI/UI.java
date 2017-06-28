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
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.FlowLayout;

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
	private JPanel panel_1;
	private JToolBar tbDown;
	private JLabel lblTextDown;
	private JComboBox cbDown;
	private JLabel lblDirectory;
	private JLabel lblTextDirectory;
	private JButton btnZoomOut;
	private JButton btnZoomIn;


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
		
		//Buttons
		open = new JButton();
		open.setIcon( getSizeImage( new ImageIcon("D:\\Documents\\Workspace JAVA\\PDF-Viewer-S.L-\\Images-Icons\\add.png"), 30, 30 ));
		tbHerramientas.add(open);
		
		btnZoomOut = new JButton("");
		btnZoomOut.setIcon( getSizeImage( new ImageIcon("D:\\Documents\\Workspace JAVA\\PDF-Viewer-S.L-\\Images-Icons\\001-zoom-out.png"), 30, 30 ) );
		tbHerramientas.add(btnZoomOut);
		
		btnZoomIn = new JButton("");
		btnZoomIn.setIcon(getSizeImage( new ImageIcon("D:\\Documents\\Workspace JAVA\\PDF-Viewer-S.L-\\Images-Icons\\002-zoom-in.png"), 30, 30 ));
		tbHerramientas.add(btnZoomIn);
		
		backPage = new JButton("");
		backPage.setIcon( getSizeImage( new ImageIcon("D:\\Documents\\Workspace JAVA\\PDF-Viewer-S.L-\\Images-Icons\\previous.png"), 30, 30 ) );
		backPage.setVisible(false);
		backPage.setFocusable(false);
		getContentPane().add(backPage, BorderLayout.WEST);
		
		nextPage = new JButton("");
		nextPage.setIcon( getSizeImage( new ImageIcon("D:\\Documents\\Workspace JAVA\\PDF-Viewer-S.L-\\Images-Icons\\next.png"), 30, 30 ));
		nextPage.setVisible(false);
		nextPage.setFocusable(false);
		getContentPane().add(nextPage, BorderLayout.EAST);
		
		panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		tbDown = new JToolBar();
		panel_1.add(tbDown);
		
		cbDown = new JComboBox();
		cbDown.setSize(100,20);
		cbDown.setMaximumSize(cbDown.getSize());
		tbDown.add(cbDown);
		
		lblTextDown = new JLabel(":");
		lblTextDown.setSize(500, 20);
		lblTextDown.setMaximumSize(lblTextDown.getSize());
		tbDown.add(lblTextDown);
		
		lblDirectory = new JLabel("Directory:  ");
		tbDown.add(lblDirectory);
		
		lblTextDirectory = new JLabel("                                                                                                                                                                          ");
		tbDown.add(lblTextDirectory);
		
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
						// UbicaciÃ³n del archivo pdf
						RandomAccessFile raf = new RandomAccessFile(file, "r");
						FileChannel channel = raf.getChannel();

						lblTextDirectory.setText(chooser.getSelectedFile().getAbsolutePath());
						
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
	
	//edit size Image to button
	public ImageIcon getSizeImage(ImageIcon icon, int w, int h){
		//ImageIcon icon = new ImageIcon("miimagen.jpeg");
		java.awt.Image img = icon.getImage(); //convertimos icon en una imagen
		java.awt.Image otraimg = img.getScaledInstance(w,h,java.awt.Image.SCALE_SMOOTH); //creamos una imagen nueva dándole las dimensiones que queramos a la antigua
		ImageIcon otroicon = new ImageIcon(otraimg);
		//JButton botón = new JButton(otroicon);
		return otroicon;
	}
}
