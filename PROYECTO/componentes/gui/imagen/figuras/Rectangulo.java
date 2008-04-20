package componentes.gui.imagen.figuras;

import java.awt.Graphics;

public class Rectangulo extends Figura
{
	
	private int xf;
	private int yf;

	public Rectangulo( int x1, int y1 , int xf1, int yf1)
	{
		super(x1, y1);
		x  = x1;
		y  = y1;
		xf = xf1;
		yf = yf1;
	}

	@Override
	public void dibujar(Graphics g, float factor)
	{
		
		g.drawRect((int)((float)x*factor), (int)((float)y*factor), (int)((float)(xf-x)*factor), (int)((float)(yf-y)*factor));
	}
	
	@Override
	public boolean pertenece(int a, int b) {
		if (a>=x && a<=xf && b>=y && b<=yf)
			return true;
		else
			return false;
	}

}
