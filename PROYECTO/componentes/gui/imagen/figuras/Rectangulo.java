package componentes.gui.imagen.figuras;

import java.awt.Graphics;

public class Rectangulo extends Figura
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8639747747514475007L;

	private int xf;

	private int yf;

	public Rectangulo( int x1, int y1, int xf1, int yf1 )
	{
		super(x1, y1);
		x = x1;
		y = y1;
		xf = xf1;
		yf = yf1;
	}

	@Override
	public void dibujar(Graphics g, float factor)
	{

		g.drawRect((int) ( x * factor ), (int) ( y * factor ),
				(int) ( ( xf - x ) * factor ), (int) ( ( yf - y ) * factor ));
	}

	@Override
	public boolean pertenece(int a, int b)
	{
		if (( a >= x ) && ( a <= xf ) && ( b >= y ) && ( b <= yf ))
			return true;
		else return false;
	}

}
