package util;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Separador extends JButton
{
	
	public Separador (){
		super();
		
		this.setIcon(new ImageIcon("./Resources/separador.gif"));
		this.setBorder(null);
		this.setBorderPainted(false);
		this.setPreferredSize(new Dimension(20,35));
		this.setEnabled(false);
	}
	
	public void setIcon(String path){
		this.setIcon(new ImageIcon(path));
	}
}
