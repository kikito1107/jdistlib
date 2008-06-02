package util;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Separador utilizado en la generacion de 
 * @author anab
 */
public class Separador extends JButton
{
	private static final long serialVersionUID = 3115477453021523269L;

	/**
	 * Crea un nuevo separador
	 */
	public Separador()
	{
		super();

		this.setIcon(new ImageIcon("Resources/separador.gif"));
		this.setBorder(null);
		this.setBorderPainted(false);
		this.setMinimumSize(new Dimension(20, 35));
		this.setPreferredSize(new Dimension(20, 35));
		this.setEnabled(false);
	}

	/**
	 * Establece el icono con el que se pinta el separador
	 * @param path path del fichero del nuevo icono
	 */
	public void setIcon(String path)
	{
		this.setIcon(new ImageIcon(path));
	}
}
