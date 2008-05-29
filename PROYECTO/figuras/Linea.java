package figuras;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Linea extends Figura
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3369752970214467809L;

	/**
	 * coordenada x de la esquina superior izquierda de la figura
	 */
	int xf;

	/**
	 * coordenada y de la esquina superior izquierda de la figura
	 */
	int yf;

	/**
	 * Crea una nueva e linea
	 * @param x1 coordenada x de la esquina superior izquierda de la figura
	 * @param y1 coordenada y de la esquina superior izquierda de la figura
	 * @param xf1 coordenada x de la esquina inferior derecha de la figura
	 * @param yf1 coordenada x de la esquina inferior derecha de la figura
	 */
	public Linea( int x1, int y1, int xf1, int yf1 )
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
		g2.drawLine(x, y, xf ,yf);
	}

	@Override
	public boolean pertenece(int a, int b)
	{

		int x1 = x, xf1 = xf, y1 = y, yf1 = yf;
		int margen = 10;

		if (x > xf)
		{
			x1 = xf;
			xf1 = x;
		}

		if (y > yf)
		{
			y1 = yf;
			yf1 = y;
		}

		if (( a >= x1 ) && ( a <= xf1 ) && ( b >= y1 ) && ( b <= yf1 ))
		{

			// / calculamos el valor estimado de y para la posicion dada (a) en
			// el eje x
			int valor = (int) ( ( (float) ( yf - y ) / (float) ( xf - x ) )
					* ( a - x ) + y );

			System.out.println("Valor: " + valor + " B: " + b);

			if (( valor < b + margen ) && ( valor > b - margen ))
				return true;
			else return false;

		}
		else return false;
	}

}
