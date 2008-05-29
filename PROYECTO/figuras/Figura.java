package figuras;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public abstract class Figura implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3309755047252198062L;

	/**
	 * coordenada x de la esquina superior izquierda de la figura
	 */
	int x;

	/**
	 * coordenada y de la esquina superior derecha
	 */
	int y;

	/**
	 * Color con el que ser� representada la figura
	 */
	Color color;

	/**
	 * Crea una nueva figura
	 * @param x1 coordenada x de la esquina superior izquierda de la figura
	 * @param y1 coordenada y de la esquina superior izquierda de la figura
	 */
	public Figura( int x1, int y1 )
	{
		x = x1;
		y = y1;
	}

	/**
	 * @return coordenada x de la esquina superior izquierda de la figura
	 */
	public int getX()
	{
		return x;
	}

	public abstract boolean pertenece(int x, int y);

	/**
	 * Actualiza el valor de la coordenada x de la esquina superior izquierda de la figura
	 * @param x
	 *            the x to set
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * @return coordenada y de la esquina superior izquierda de la figura
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Acutaliza el valor de la coordenada y de la esquina superior izquierda de la figura
	 * @param y
	 *            the y to set
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * Consulta el color de la figura
	 * @return the color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Establece el color de la figura
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Dibuja la figura el un objeto graphics
	 * @param g el grafico sobre el que dibujar la figura
	 */
	public abstract void dibujar(Graphics g);
}
