package pdf.GUI;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.pdfbox.debugger.ui.ZoomMenu;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFViewer;
import com.sun.pdfview.PagePanel;
import com.sun.scenario.effect.ZoomRadialBlur;

import javafx.beans.value.ChangeListener;
import pdf.Manager.PDFManager;

import javax.swing.JToolBar;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

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
	private JButton btnResetZoom;

	private JScrollPane scLateral;
	private JScrollPane scInferior;
	
	private Dimension dSizePanel;
	
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
		open.setIcon( getSizeImage( new ImageIcon("Images-Icons\\add.png"), 30, 30 ));
		open.setFocusable(false);
		tbHerramientas.add(open);
		
		btnZoomOut = new JButton("");
		btnZoomOut.setIcon( getSizeImage( new ImageIcon("Images-Icons\\001-zoom-out.png"), 30, 30 ) );
		btnZoomOut.setFocusable(false);
		tbHerramientas.add(btnZoomOut);
		
		btnZoomIn = new JButton("");
		btnZoomIn.setIcon(getSizeImage( new ImageIcon("Images-Icons\\002-zoom-in.png"), 30, 30 ));
		btnZoomIn.setFocusable(false);
		tbHerramientas.add(btnZoomIn);
		
		btnResetZoom = new JButton("");
		btnResetZoom.setIcon(getSizeImage( new ImageIcon("Images-Icons\\reset-zoom.png"), 30, 30) ); 
		tbHerramientas.add(btnResetZoom);
		
		backPage = new JButton("");
		backPage.setVisible(false);
		backPage.setFocusable(false);
		getContentPane().add(backPage, BorderLayout.WEST);
		
		nextPage = new JButton("");
		nextPage.setVisible(false);
		nextPage.setFocusable(false);
		getContentPane().add(nextPage, BorderLayout.EAST);
		
		panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		tbDown = new JToolBar();
		panel_1.add(tbDown);
		
		cbDown = new JComboBox();
		cbDown.setSize(150,20);
		cbDown.setMaximumSize(cbDown.getSize());
		cbDown.setVisible(false);
		cbDown.addItem("Title");
		cbDown.addItem("Author");
		cbDown.addItem("Subject");
		cbDown.addItem("Keywords");
		cbDown.addItem("Number of words");
		cbDown.addItem("Number of pages");
		cbDown.addItem("Average words");
		tbDown.add(cbDown);
		
		lblTextDown = new JLabel();
		lblTextDown.setSize(500, 20);
		lblTextDown.setMaximumSize(lblTextDown.getSize());
		tbDown.add(lblTextDown);
		
		lblDirectory = new JLabel("Directory:  ");
		lblDirectory.setVisible(false);
		tbDown.add(lblDirectory);
		
		lblTextDirectory = new JLabel("                                                                                                                                                                          ");
		tbDown.add(lblTextDirectory);
		
		nextPage.addActionListener(this);
		backPage.addActionListener(this);
		
		btnZoomOut.addActionListener(this);
		btnZoomIn.addActionListener(this);
		btnResetZoom.addActionListener(this);
		
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
						File file = new File( chooser.getSelectedFile().getAbsolutePath() );		//get file PDF 
						
						RandomAccessFile raf = new RandomAccessFile(file, "r");
						FileChannel channel = raf.getChannel();

						lblTextDirectory.setText(chooser.getSelectedFile().getAbsolutePath());		//add directory in label text
						
						ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,channel.size());
						buf.clear();
						
						pdffile = new PDFFile(buf);													//save file pdf in variable
						paginas = pdffile.getNumPages();											//get number of pages the PDF file
						pagina = 1;																	//
						viewPage();																	//show file in panel
						
						raf.close();
						
						dSizePanel = new Dimension(panel.getSize());								//save size original panel
						
						lblDirectory.setVisible(true);
						cbDown.setVisible(true);
						backPage.setVisible(true);
						nextPage.setVisible(true);
						
					}catch (Exception e) {
						e.printStackTrace();
					}								
				}
				
			}
		});
		
		//this.scLateral = new JScrollPane();
		//this.scLateral.setVisible(true);
		
		backPage.addMouseListener(new MouseAdapter() { 				//Add event to mouse.			
            public void mouseEntered(MouseEvent evt) {				//mouse entered in button			            	
            	backPage.setIcon( getSizeImage( new ImageIcon("Images-Icons\\previous.png"), 30, 30 ) );
            	backPage.setVisible(true);							//button is visible
            	backPage.setEnabled(true);							//enable button
            }

            public void mouseExited(MouseEvent evt) {				//mouse exited in button
            	backPage.setIcon(null);								//disable image of button
            	backPage.setEnabled(false);							//disable button 
            }
		});
		
		nextPage.addMouseListener(new MouseAdapter() { 				//Add event to mouse.			
            public void mouseEntered(MouseEvent evt) {				//mouse entered in button			
            	nextPage.setIcon( getSizeImage( new ImageIcon("Images-Icons\\next.png"), 30, 30 ));
            	nextPage.setVisible(true);							//button is visible
            	nextPage.setEnabled(true);							//enable button
            }

            public void mouseExited(MouseEvent evt) {				//mouse exited in button
            	nextPage.setIcon(null);								//disable image of button
            	nextPage.setEnabled(false);							//disable button 
            }
		});
		
		cbDown.addActionListener(new ActionListener() {				//Add options list a JComboBox
			@Override
			public void actionPerformed(ActionEvent e) {
				String ruta = lblTextDirectory.getText();
				try {
					PDFManager pdfManager = new PDFManager(ruta);
					String abrir = pdfManager.openPDF();
					String seleccion = cbDown.getSelectedItem().toString();
					if(seleccion == "Title"){
						lblTextDown.setText("     "+pdfManager.getTitle());
					}
					else{
						if(seleccion == "Author"){
							lblTextDown.setText("     "+pdfManager.getAuthor());
						}
						else{
							if(seleccion == "Subject"){
								lblTextDown.setText("     "+pdfManager.getSubject());
							}
							else{
								if(seleccion == "Keywords"){
									lblTextDown.setText("     "+pdfManager.getKeywords());
								}
								else{
									if(seleccion == "Number of words"){
										lblTextDown.setText("     "+pdfManager.getNumberOfWords());
									}
									else{
										if(seleccion == "Number of pages"){
											lblTextDown.setText("     "+pdfManager.getNumberOfPages());
										}
										else{
											lblTextDown.setText("     "+pdfManager.getAverageWords());
										}
									}
								}
							}
						}
					}
					
					pdfManager.closePDF();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				cbDown.getSelectedItem();
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
		
		if(e.getSource()==btnZoomIn){
			panel.useZoomTool(true);
			Dimension  dimen = new Dimension(getSize());
			dimen.setSize(panel.getSize().getWidth()*1.05, panel.getSize().getHeight()*1.05);
			panel.setSize(dimen);
		}
		if(e.getSource()==btnZoomOut){
			panel.useZoomTool(true);
			Dimension  dimen = new Dimension(getSize());
			dimen.setSize(panel.getSize().getWidth()*0.85, panel.getSize().getHeight()*0.85);
			panel.setSize(dimen);
		}
		
		if(e.getSource() == btnResetZoom ){
			panel.setSize(this.dSizePanel);
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
	
	public ImageIcon getSizeImage(ImageIcon icon, int w, int h){							//edit size Image to button
		java.awt.Image img = icon.getImage(); 												//parse icon in image
		java.awt.Image otraimg = img.getScaledInstance(w,h,java.awt.Image.SCALE_SMOOTH); 	//create new image to size a like 
		ImageIcon otroicon = new ImageIcon(otraimg);
		return otroicon;																	//return new image 
	}
	
	
}
