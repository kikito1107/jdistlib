/**
 * 
 */
package aplicacion.gui.editor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import Deventos.enlaceJS.DConector;

import componentes.base.DComponenteBase;
import Deventos.DEvent;
import Deventos.DJViewerEvent;

/**
 * Componente que implementa un visor de imagenes distribuido
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DIViewer extends DComponenteBase
{
	private static final long serialVersionUID = 1L;

	private Image imagen;

	/**
	 * Constructor
	 * @param nombre Nombre del componente
	 * @param conexionDC Indica si deseamos realizar una conexion
	 * 					directa con el @see DConector
	 * @param padre Ventana padre de este componente
	 */
	public DIViewer( String nombre, boolean conexionDC, DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);

		try
		{
			jbInit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		imagen = null;
	}

	/**
	 * Obtiene la imagen mostrada
	 * @return Imagen mostrada
	 */
	public Image getImage()
	{
		return imagen;
	}

	/**
	 * Asigna la imagen a mostrar
	 * @param filename Nombre del fichero que tiene la imagen a mostrar
	 */
	public void setImage(String filename)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		imagen = toolkit.getImage(filename);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(imagen, 0);
		try
		{
			mediaTracker.waitForID(0);
			imagen = imagen.getScaledInstance(getSize().width,
					getSize().height, Image.SCALE_DEFAULT);
			repaint();
		}
		catch (InterruptedException ie)
		{
			System.err.println(ie);
		}
	}

	/**
	 * Asigna la imagen a mostrar
	 * @param img Imagen a mostrar
	 */
	public void setImage(Image img)
	{
		MediaTracker mediatracker = new MediaTracker(this);
		imagen = img.getScaledInstance(img.getWidth(null), img.getHeight(null),
				Image.SCALE_DEFAULT);
		mediatracker.addImage(imagen, 0);

		try
		{
			mediatracker.waitForID(0);
			repaint();
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Obtiene un evento con la informacion de estado del componente
	 * @return Evento con la informacion de estado del componente
	 */
	public DJViewerEvent obtenerInfoEstado()
	{
		DJViewerEvent de = new DJViewerEvent();
		de.contenido = new ImageIcon(imagen);

		return de;
	}

	/**
	 * Procesa la informacion de estado del componente
	 * @param evento Evento recibido
	 */
	public void procesarInfoEstado(DJViewerEvent evento)
	{
		setImage(evento.contenido.getImage());
	}

	@Override
	public void activar()
	{
		this.setEnabled(true);
	}

	@Override
	public void desactivar()
	{
		this.setEnabled(false);
	}

	@Override
	public void procesarEvento(DEvent evento)
	{
		if (( evento.tipo.intValue() == DJViewerEvent.SINCRONIZACION )
				&& !evento.usuario.equals(DConector.Dusuario))
		{
			DJViewerEvent infoEstado = obtenerInfoEstado();
			infoEstado.tipo = new Integer(
					DJViewerEvent.RESPUESTA_SINCRONIZACION.intValue());
			infoEstado.contenido = new ImageIcon(imagen);
			enviarEvento(infoEstado);
		}

		else if (evento.tipo.intValue() == DJViewerEvent.CARGADO.intValue())
		{
			DJViewerEvent evt = (DJViewerEvent) evento;
			setImage(evt.contenido.getImage());

		}

		else if (evento.tipo.intValue() == DJViewerEvent.RESPUESTA_SINCRONIZACION
				.intValue())
		{
			DJViewerEvent evt = (DJViewerEvent) evento;

			if (evt.contenido != null) setImage(evt.contenido.getImage());
		}
	}

	@Override
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	@Override
	public void sincronizar()
	{
		if (conectadoDC())
		{
			DJViewerEvent peticion = new DJViewerEvent();
			peticion.tipo = new Integer(DJViewerEvent.SINCRONIZACION.intValue());
			if (imagen != null)
				peticion.contenido = new ImageIcon(imagen);
			else peticion.contenido = null;

			enviarEvento(peticion);
		}
	}

	@Override
	public void paint(Graphics g)
	{
		try
		{
			int img_x = getSize().width / 2 - imagen.getWidth(this) / 2;
			int img_y = getSize().height / 2 - imagen.getHeight(this) / 2;

			g.drawImage(imagen, img_x, img_y, this);
		}
		catch (NullPointerException ex)
		{
		}
	}

}
