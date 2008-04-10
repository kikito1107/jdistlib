package aplicacion.fisica.documentos;

import java.awt.Image;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;

public class Pagina implements Serializable
{
	private static final long serialVersionUID=1L;
	
	private ImageIcon imagen = null;
	private Vector<Anotacion> anotaciones = new Vector<Anotacion>();
	
	public Pagina()
	{
		
	}
	
	public Image getImagen()
	{
		return imagen.getImage();
	}
	
	public void setImagen(Image pag)
	{
		imagen= new ImageIcon(pag);
	}
	
	public void setImagen(ImageIcon pag)
	{
		imagen = pag;
	}
	
	public void addAnotacion(Anotacion anot)
	{
		anotaciones.add(anot);
	}
	
	public void delAnotacion(int index)
	{
		anotaciones.remove(index);
	}
	
	public void setAnotacion(int index, Anotacion anot)
	{
		anotaciones.set(index, anot);
	}
	
	public void setAnotaciones(Vector<Anotacion> anot)
	{
		anotaciones = anot;
	}
	
	public Vector<Anotacion> getAnotaciones()
	{
		return anotaciones;
	}

	public Vector<Anotacion> getAnotacionesUsuario(String usu)
	{

		Vector<Anotacion> res = new Vector<Anotacion>();
		for(int i=0; i<anotaciones.size(); i++)
		{
			if (anotaciones.get(i).getUsuario().compareTo(usu) == 0)
			{
				res.add(anotaciones.get(i));
			}
		}

		return res;
	}
	
	public Vector<Anotacion> getAnotacionesRol(String rol)
	{

		Vector<Anotacion> res = new Vector<Anotacion>();
		for(int i=0; i<anotaciones.size(); i++)
		{
			if (anotaciones.get(i).getRol().compareTo(rol) == 0)
			{
				res.add(anotaciones.get(i));
			}
		}

		return res;
	}
}
