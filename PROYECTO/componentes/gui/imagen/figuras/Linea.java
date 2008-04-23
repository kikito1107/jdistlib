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
	
	@Override
	public boolean pertenece(int a, int b) {

		int x1=x,xf1=xf, y1=y, yf1=yf;
		int margen = 10;
		
		if (x > xf) {
			x1 = xf;
			xf1 = x;
		}
		
		if (y > yf) {
			y1 = yf;
			yf1 = y;
		}
		
		if (a>=x1 && a<=xf1 && b>=y1 && b<=yf1) {

			
			/// calculamos el valor estimado de y para la posicion dada (a) en el eje x
			int valor = (int)(((float)(yf-y)/(float)(xf-x))* (float)(a-x)+(float)y);
			
			System.out.println("Valor: " + valor +" B: " + b);
			
			if (valor < b+margen && valor > b-margen)
				return true;
			else
				return false;
			
		}
		else
			return false;
	}

}
