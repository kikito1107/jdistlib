package componentes.gui.imagen.figuras;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;

public class Figura implements Serializable
{
	/**
	 * coordenada x de la esquina superior izquierda de la figura
	 */
	int x;
	
	/**
	 * coordenada y de la esquina superior derecha
	 */
	int y;
	
	/**
	 * Color con el que ser‡ representada la figura
	 */
	Color color;
	
	public Figura(int x1, int y1){
		int x = x1;
		int y = y1;
	}
	
	
	
	/**
	 * @return the x
	 */
	public int getX()
	{
		return x;
	}
	
	public boolean pertenece(int x, int y)
	{
		return false;
	}



	/**
	 * @param x the x to set
	 */
	public void setX(int x)
	{
		this.x = x;
	}



	/**
	 * @return the y
	 */
	public int getY()
	{
		return y;
	}



	/**
	 * @param y the y to set
	 */
	public void setY(int y)
	{
		this.y = y;
	}



	/**
	 * @return the color
	 */
	public Color getColor()
	{
		return color;
	}



	/**
	 * @param color the color to set
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}



	public void dibujar(Graphics g, float razon){}
}
