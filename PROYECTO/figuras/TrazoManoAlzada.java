package figuras;

import java.awt.Graphics;
import java.util.Vector;

public class TrazoManoAlzada extends Figura
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -172401421751373490L;

	/**
	 * lineas que forman el trazo
	 */
	private Vector<Linea> lineas = new Vector<Linea>();

	/**
	 * Crea un nuevo trazo
	 *
	 */
	public TrazoManoAlzada( )
	{
		super(-1, -1);
	}

	/**
	 * Agrega una nueva linea al trazo
	 * @param l linea a agregar
	 */
	public void agregarLinea(Linea l)
	{
		lineas.add(l);
	}

	@Override
	public void dibujar(Graphics g)
	{
		for (int i = 0; i < lineas.size(); ++i)
			lineas.get(i).dibujar(g);
	}

	@Override
	public boolean pertenece(int a, int b)
	{

		for (int i = 0; i < lineas.size(); ++i)
			if (lineas.get(i).pertenece(a, b)) return true;

		return false;
	}

}
