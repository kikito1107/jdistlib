package aplicacion.fisica.documentos;

import java.awt.Image;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;

/**
 * Clase que representa una pagina de un Documento
 * @author anab
 */
public class Pagina implements Serializable
{
	private static final long serialVersionUID = 1L;

	private ImageIcon imagen = null;

	private Vector<Anotacion> anotaciones = new Vector<Anotacion>();

	/**
	 * Crea una nueva pagina
	 */
	public Pagina()
	{

	}

	/**
	 * Devuelve la imagen de la pagina
	 * @return la imagen
	 */
	public Image getImagen()
	{
		return imagen.getImage();
	}

	/**
	 * Establece la imagen de la pagina
	 * @param pag imagen a establecer
	 */
	public void setImagen(Image pag)
	{
		imagen = new ImageIcon(pag);
	}

	/**
	 * Establece la imagen de la pagina
	 * @param pag imagen a establecer
	 */
	public void setImagen(ImageIcon pag)
	{
		imagen = pag;
	}

	/**
	 * Agrega una anotacion a la pagina
	 * @param anot anotacion a agregar
	 */
	public void addAnotacion(Anotacion anot)
	{
		anotaciones.add(anot);
	}

	/**
	 * elimina una anotacion de la pagina
	 * @param index posicion de la anotacion
	 */
	public void delAnotacion(int index)
	{
		anotaciones.remove(index);
	}

	/**
	 * Agrega una anotacion a la pagina en una determinada posicion
	 * @param index posicion que ocupara la nueva anotacion
	 * @param anot anotacion a agregar
	 */
	public void setAnotacion(int index, Anotacion anot)
	{
		anotaciones.set(index, anot);
	}

	/**
	 * Le asigna a la pagina unas nuevas anotaciones
	 * @param anot vector conteniendo las nuevas anotaciones
	 */
	public void setAnotaciones(Vector<Anotacion> anot)
	{
		anotaciones.removeAllElements();
		anotaciones = anot;
	}

	/**
	 * Devuelve las anotaciones de la pagina
	 * @return vector conteniendo las anotaciones de la pagina
	 */
	public Vector<Anotacion> getAnotaciones()
	{
		return anotaciones;
	}

	/**
	 * Consulta las anotaciones realizadas por un usario en la pagina
	 * @param usu usuario 
	 * @return vector conteniendo las anotaciones
	 */
	public Vector<Anotacion> getAnotacionesUsuario(String usu)
	{

		Vector<Anotacion> res = new Vector<Anotacion>();
		for (int i = 0; i < anotaciones.size(); i++)
			if (anotaciones.get(i).getUsuario().compareTo(usu) == 0)
				res.add(anotaciones.get(i));

		return res;
	}

	/**
	 * Consulta las anotaciones realizadas en al pagina bajo un determinado rol
	 * @param rol el rol
	 * @return vector conteniendo las anotaciones
	 */
	public Vector<Anotacion> getAnotacionesRol(String rol)
	{

		Vector<Anotacion> res = new Vector<Anotacion>();
		for (int i = 0; i < anotaciones.size(); i++)
			if (anotaciones.get(i).getRol().compareTo(rol) == 0)
				res.add(anotaciones.get(i));

		return res;
	}
}
