package figuras;

import java.awt.Graphics;
import java.util.Vector;

public class TrazoManoAlzada extends Figura
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -172401421751373490L;

	private Vector<Linea> lineas = new Vector<Linea>();

	public TrazoManoAlzada( int x1, int y1 )
	{
		super(x1, y1);
		int x = x1;
		int y = y1;
		// TODO Auto-generated constructor stub
	}

	public void agregarLinea(Linea l)
	{
		lineas.add(l);
	}

	@Override
	public void dibujar(Graphics g, float factor)
	{
		for (int i = 0; i < lineas.size(); ++i)
			lineas.get(i).dibujar(g, factor);
	}

	@Override
	public boolean pertenece(int a, int b)
	{

		for (int i = 0; i < lineas.size(); ++i)
			if (lineas.get(i).pertenece(a, b)) return true;

		return false;
	}

}