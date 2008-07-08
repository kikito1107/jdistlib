package componentes.gui.visualizador.figuras;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 * Clase abstracta para la creacion de una figura que pueda usarse para pintar
 * en un lienzo
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public abstract class Figura implements Serializable
{
	private static final long serialVersionUID = 3309755047252198062L;

	/**
	 * coordenada x de la esquina superior izquierda de la figura
	 */
	protected int x;

	/**
	 * coordenada y de la esquina superior derecha
	 */
	protected int y;

	/**
	 * Color con el que sera representada la figura
	 */
	protected Color color;

	/**
	 * Crea una nueva figura
	 * 
	 * @param x1
	 *            Coordenada x de la esquina superior izquierda de la figura
	 * @param y1
	 *            Coordenada y de la esquina superior izquierda de la figura
	 */
	public Figura( int x1, int y1 )
	{
		x = x1;
		y = y1;
	}

	/**
	 * Consulta la coordena x de la figura
	 * 
	 * @return Coordenada x de la esquina superior izquierda de la figura
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Comprueba si un determinado punto pertenece a la figura
	 * 
	 * @param x
	 *            Coordenada x del punto
	 * @param y
	 *            Coordenada y del punto
	 * @return True si el punto pertenece a la figura. False en caso contrario
	 */
	public abstract boolean pertenece(int x, int y);

	/**
	 * Actualiza el valor de la coordenada x de la esquina superior izquierda de
	 * la figura
	 * 
	 * @param x
	 *            Valor x a poner
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * Consulta la coordenada y de la figura
	 * 
	 * @return Coordenada y de la esquina superior izquierda de la figura
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Actualiza el valor de la coordenada y de la esquina superior izquierda de
	 * la figura
	 * 
	 * @param y
	 *            Valor y a poner
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * Consulta el color de la figura
	 * 
	 * @return Color de la figura
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Establece el color de la figura
	 * 
	 * @param color
	 *            Color a establecer
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Dibuja la figura el un objeto graphics
	 * 
	 * @param g
	 *            Grafico sobre el que dibujar la figura
	 */
	public abstract void dibujar(Graphics g);
}
