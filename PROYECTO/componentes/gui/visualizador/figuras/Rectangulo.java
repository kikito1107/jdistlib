package componentes.gui.visualizador.figuras;

import java.awt.Graphics;

/**
 * Figura correspondiente a un rectangulo
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class Rectangulo extends Figura
{
	private static final long serialVersionUID = -8639747747514475007L;

	/**
	 * Pixeles de error permitidos
	 */
	private static final int error = 10;

	/**
	 * Coordenada x de la esquina superior izquierda de la figura
	 */
	private int xf;

	/**
	 * Coordenada y de la esquina superior izquierda de la figura
	 */
	private int yf;

	/**
	 * Crea un nuevo rectangulo
	 * 
	 * @param x1
	 *            Coordenada x de la esquina superior izquierda de la figura
	 * @param y1
	 *            Coordenada y de la esquina superior izquierda de la figura
	 * @param xf1
	 *            Coordenada x de la esquina inferior derecha de la figura
	 * @param yf1
	 *            Coordenada x de la esquina inferior derecha de la figura
	 */
	public Rectangulo( int x1, int y1, int xf1, int yf1 )
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

		g.drawRect(x, y, xf - x, yf - y);
	}

	@Override
	public boolean pertenece(int a, int b)
	{
		// si esta en el rectangulo externo ...
		if (( a >= x - error ) && ( a <= xf + error ) && ( b >= y - error )
				&& ( b <= yf + error ))
		{

			// ... y en el interno: el punto no pertenece a la figura
			if (( a >= x + error ) && ( a <= xf - error ) && ( b >= y + error )
					&& ( b <= yf - error ))
				return false;

			// ... pero no sobre el externo: el punto pertence a la figura
			else return true;
		}

		// no esta en el rectangulo externo: no pertenece a la figura
		else
		{

			return false;
		}
	}

}
