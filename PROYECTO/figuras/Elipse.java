package figuras;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Elipse extends Figura
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2862430808358522126L;

	private int xf;

	private int yf;

	/**
	 * Crea una nueva elipse
	 * @param x1 coordenada x de la esquina superior izquierda de la figura
	 * @param y1 coordenada y de la esquina superior izquierda de la figura
	 * @param xf1 coordenada x de la esquina inferior derecha de la figura
	 * @param yf1 coordenada x de la esquina inferior derecha de la figura
	 */
	public Elipse( int x1, int y1, int xf1, int yf1 )
	{
		super(x1, y1);
		x = x1;
		y = y1;
		xf = xf1;
		yf = yf1;
	}

	@Override
	public void dibujar(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawOval(x, y, xf - x, yf - y);
	}

	@Override
	public boolean pertenece(int a, int b)
	{
		if (( a >= x ) && ( a <= xf ) && ( b >= y ) && ( b <= yf ))
			return true;
		else return false;
	}
}
