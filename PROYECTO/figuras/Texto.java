package figuras;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Texto extends Figura
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6651266174731584301L;

	String texto = "";

	int num_lineas = 0;

	int num_columnas = 0;

	public Texto( int x1, int y1, String texto1 )
	{
		super(x1, y1);
		x = x1;
		y = y1;
		texto = texto1;
	}

	@Override
	public void dibujar(Graphics g, float factor)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		String lineas[] = texto.split("\n");

		int y_aux = y;

		for (int i = 0; i < lineas.length; ++i)
		{
			g2.drawString(lineas[i], x, y_aux);
			y_aux += 14;
			num_lineas++;

			if (lineas[i].length() > num_columnas)
				num_columnas = lineas[i].length();
		}

		num_lineas = y_aux;

	}

	@Override
	public boolean pertenece(int a, int b)
	{
		if (( a < x ) || ( b < y ))
			return false;
		else
		{
			int yf = num_lineas;

			int xf = x + num_columnas * 5 + 7;

			if (( a < xf ) && ( b < yf ))
				return true;
			else return false;

		}

	}

}
