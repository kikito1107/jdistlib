package figuras;

import java.awt.Graphics;

/**
 * Clase que representa un rectangulo
 * @author anab
 */
public class Rectangulo extends Figura
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8639747747514475007L;

	private static final int error = 10;
	
	/**
	 * coordenada x de la esquina superior izquierda de la figura
	 */
	private int xf;

	/**
	 * coordenada y de la esquina superior izquierda de la figura
	 */
	private int yf;

	/**
	 * Crea un nuevo rectangulo
	 * @param x1 coordenada x de la esquina superior izquierda de la figura
	 * @param y1 coordenada y de la esquina superior izquierda de la figura
	 * @param xf1 coordenada x de la esquina inferior derecha de la figura
	 * @param yf1 coordenada x de la esquina inferior derecha de la figura
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

		g.drawRect(x , y , xf - x, yf - y);
	}

	@Override
	public boolean pertenece(int a, int b)
	{
		// si esta en el rectangulo externo ...
		if (( a >= x - error) && ( a <= xf + error) && ( b >= y-error) && ( b <= yf+error)) {
		
			// ... y en el interno: el punto no pertenece a la figura
			if (( a >= x + error ) && ( a <= xf - error) && ( b >= y + error) && ( b <= yf - error))
				return false;
			
			// ... pero no sobre el externo: el punto pertence a la figura
			else
				return true;
		}
		
		// no esta en el rectangulo externo: no  pertenece a la figura
		else {
			
			return false;
		}
	}

}
