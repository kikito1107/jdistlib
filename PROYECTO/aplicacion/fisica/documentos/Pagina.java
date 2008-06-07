package aplicacion.fisica.documentos;

import java.awt.Image;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;

/**
 * Clase que representa una pagina de un Documento
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez.
 */
public class Pagina implements Serializable
{
	private static final long serialVersionUID = 1L;

	private ImageIcon imagen = null;

	private Vector<Anotacion> anotaciones = new Vector<Anotacion>();

	/**
	 * Constructor por defecto
	 */
	public Pagina()
	{

	}

	/**
	 * Devuelve la imagen contenida en la pagina
	 * 
	 * @return Imagen que representa una pagina
	 */
	public Image getImagen()
	{
		return imagen.getImage();
	}

	/**
	 * Establece la imagen que representa una pagina
	 * 
	 * @param pag
	 *            Imagen a establecer como pagina
	 */
	public void setImagen(Image pag)
	{
		imagen = new ImageIcon(pag);
	}

	/**
	 * Establece la imagen que representa una pagina
	 * 
	 * @param pag
	 *            Imagen a establecer como pagina
	 */
	public void setImagen(ImageIcon pag)
	{
		imagen = pag;
	}

	/**
	 * Agrega una anotacion a la pagina
	 * 
	 * @param anot
	 *            Anotacion a agregar
	 */
	public void addAnotacion(Anotacion anot)
	{
		anotaciones.add(anot);
	}

	/**
	 * Elimina una anotacion de la pagina
	 * 
	 * @param index
	 *            Posicion de la anotacion a eliminar
	 */
	public void delAnotacion(int index)
	{
		anotaciones.remove(index);
	}

	/**
	 * Agrega una anotacion a la pagina en una determinada posicion
	 * 
	 * @param index
	 *            Posicion que ocupara la nueva anotacion
	 * @param anot
	 *            Anotacion a agregar
	 */
	public void setAnotacion(int index, Anotacion anot)
	{
		anotaciones.set(index, anot);
	}

	/**
	 * Permite asignar a la pagina un vector con nuevas anotaciones
	 * 
	 * @param anot
	 *            Vector conteniendo las nuevas anotaciones
	 */
	public void setAnotaciones(Vector<Anotacion> anot)
	{
		anotaciones.removeAllElements();
		anotaciones = anot;
	}

	/**
	 * Devuelve las anotaciones de la pagina
	 * 
	 * @return Vector que contiene las anotaciones de la pagina
	 */
	public Vector<Anotacion> getAnotaciones()
	{
		return anotaciones;
	}

	/**
	 * Consulta las anotaciones realizadas por un usario en la pagina
	 * 
	 * @param usu
	 *            Usuario del cual queremos consultar las anotaciones
	 * @return Vector conteniendo las anotaciones introducidas por el usuario
	 *         requerido
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
	 * Consulta las anotaciones realizadas en la pagina bajo un determinado rol
	 * 
	 * @param rol
	 *            Rol que desempeñaban los usuarios que realizaron una anotacion
	 *            en la pagina
	 * @return Vector conteniendo las anotaciones requeridas
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
