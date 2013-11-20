import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Interfaz extends JPanel implements ActionListener{
	private static Image icon = new ImageIcon("icon2.png").getImage();
	private String address, cadena;
	private JButton exp, run, about;
	private JTextField tfTexto;
	private JTextArea salida;
	private ERaAFN a;
	
	public Interfaz(){
		super();
		this.exp = new JButton("Seleccionar un archivo");
		this.exp.addActionListener(this);
		this.add(exp);
		
		this.tfTexto = new JTextField(16);
		this.tfTexto.addActionListener(this);
		this.add(tfTexto);
		
		this.run = new JButton("Run");
		this.run.addActionListener(this);
		this.run.setEnabled(false);
		this.add(run);
		
		this.about = new JButton("Acerca de Nosotros");
		this.about.addActionListener(this);
		this.add(about);
		
		this.salida = new JTextArea(cadena, 7, 50);
		this.add(new JScrollPane(salida));
		
		this.setPreferredSize(new Dimension(665, 200));
		this.setBackground(Color.WHITE);
		this.setVisible(true);
	}
	
	public void cargarArchivo(){
		try {
			FileFilter filter = new FileNameExtensionFilter("Text Files only", "txt"); //filtro para que el usuario seleccione unicamente archivos de texto
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				address = chooser.getSelectedFile()+"";
				String temp = "";
				for (int i = 0; i < address.length()-4; i++) {
					temp = temp+address.charAt(i)+"";
				}
				address = temp+".txt";
				//String fileName = address+".afd";
				this.run.setEnabled(true);
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.exp){
			cargarArchivo();
		}
		else if(e.getSource()==this.tfTexto){
			cadena = this.tfTexto.getText();
		}
		else if(e.getSource()==this.run){
			String expresion = this.tfTexto.getText();
			
			expresion = expresion.replaceAll("\r","");
			String cad = "";
			for(int i = 0; i < expresion.length(); i++){
				if(expresion.charAt(i) == '\\' && expresion.charAt(i+1) == 'n'){
					i += 2;
					cad += '\n';
					continue;
				}
				cad += expresion.charAt(i);	
			}
			
			PostFix convertir = new PostFix(cad);
			//PostFix convertir = new PostFix(expresion);
			
			//System.out.println(this.tfTexto.getText() + "dkjvnfkj");
			//System.out.println(convertir.getResult());
			String exp = convertir.getResult();
			a = new ERaAFN(exp);
			//this.salida = new JTextArea(cadena, 7, 50);
			Vector<String> accepted;
			this.salida.setText(null);
			try {
				accepted = a.lector(this.address);
				System.out.println(accepted.size());
				for(int i = 0; i < accepted.size(); i++){
					this.salida.append(accepted.elementAt(i)+"\n");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
		}
		else{
			JOptionPane.showMessageDialog(null, "Integrantes:\nEdna Ramirez\nRonald Antonio Bernal\nAntonio Hernandez Campos");
		}
	}
	
	public static void main(String[] args) {
		
		JFrame ventana = new JFrame("AFN");
		ventana.setIconImage(icon);
		ventana.setResizable(false);
		ventana.setLocation(350,200);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Interfaz miPanel = new Interfaz();
		//ImagePanel panelImg = new ImagePanel();
		ventana.add(miPanel, BorderLayout.NORTH);
		//ventana.add(panelImg, BorderLayout.SOUTH);
		ventana.pack();
		ventana.setVisible(true);
		while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e){	
				System.out.println("ocurrio un aborto");
			}
			//ventana.repaint();
		}
		
	}
}

