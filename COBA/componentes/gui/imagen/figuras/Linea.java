package componentes.gui.imagen.figuras;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Linea extends Figura
{

	int xf;
	int yf;
	
	public Linea( int x1, int y1, int xf1, int yf1 )
	{
		super(x1, y1);
		x =x1;
		y=y1;
		xf=xf1;
		yf=yf1;
	}

	@Override
	public	void dibujar(Graphics g, float factor)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawLine((int)((float)x*factor), (int)((float)y*factor), (int)((float)xf*factor), (int)((float)yf*factor));
	}

}
