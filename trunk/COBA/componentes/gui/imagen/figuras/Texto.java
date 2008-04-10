package componentes.gui.imagen.figuras;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class Texto extends Figura
{

	String texto = "";
	
	public Texto( int x1, int y1, String texto1 )
	{
		super(x1,y1);
		x =x1;
		y=y1;
		texto = texto1;
	}

	@Override
	public void dibujar(Graphics g, float factor)
	{
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		String lineas[] = texto.split("\n");
		
		int aux = y;
		
		System.out.println("x: " + x + ", y: " + y);
		
		for (int i=0; i<lineas.length; ++i) {
			g2.drawString(lineas[i], x, aux);
			aux += 14;
		}
		

	}

}
