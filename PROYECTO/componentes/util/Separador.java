package componentes.util;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Separador utilizado para las barras de herramientas
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class Separador extends JButton
{
	private static final long serialVersionUID = 3115477453021523269L;

	/**
	 * Constructor
	 * 
	 * @pre La imagen "Resources/separador.gif" debe existir
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
	 * 
	 * @param path
	 *            Path del fichero con el icono en cualquier formato de imagen
	 *            soportado por Java
	 */
	public void setIcon(String path)
	{
		this.setIcon(new ImageIcon(path));
	}
}
