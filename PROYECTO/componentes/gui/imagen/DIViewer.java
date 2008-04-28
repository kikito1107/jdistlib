/**
 * 
 */
package componentes.gui.imagen;

import java.awt.*;
import javax.swing.ImageIcon;
import Deventos.*;
import Deventos.enlaceJS.DConector;

import java.util.Vector;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.HebraProcesadoraBase;
import componentes.listeners.*;

/**
 * Implementacion de un visor de imagenes distribuido
 * 
 * @author Carlos Rodriguez Dominguez
 * @date 1-1-2008
 */
public class DIViewer extends DComponenteBase
{
	private static final long serialVersionUID = 1L;
	
	private Image imagen;
	private Vector djviewerlisteners = new Vector(5);
	private Vector ljviewerlisteners = new Vector(5);
	private Vector lujviewerlisteners = new Vector(5);
	private HebraProcesadoraBase hebraProcesadora = null;
	
	
	public DIViewer(String nombre, boolean conexionDC, DComponenteBase padre)
	{
		super(nombre, conexionDC, padre);
		
		try{
		 jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	private void jbInit() throws Exception
	{
		imagen = null;
		addDJViewerListener(crearDJListener());
	}
	
	public Image getImage()
	{
		return imagen;
	}
	
	public void setImage(String filename)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		imagen = toolkit.getImage(filename);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(imagen, 0);
		try
		{
			mediaTracker.waitForID(0);
			imagen = imagen.getScaledInstance(getSize().width, getSize().height, Image.SCALE_DEFAULT);
			repaint();
		}
		catch (InterruptedException ie)
		{
			System.err.println(ie);
		}
	}
	
	public void setImage(Image img)
	{
		MediaTracker mediatracker = new MediaTracker(this);
		imagen = img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_DEFAULT);
		mediatracker.addImage(imagen, 0);
		
		try{
			mediatracker.waitForID(0);
			repaint();
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public DJViewerEvent obtenerInfoEstado()
	{
		DJViewerEvent de = new DJViewerEvent();
		de.contenido = new ImageIcon(imagen);
		
		return de;
	}
	
	public void procesarInfoEstado(DJViewerEvent evento)
	{
		setImage(evento.contenido.getImage());
	}
	
	
	
	public void addDJViewerListener(DJViewerListener dvl)
	{
		djviewerlisteners.add(dvl);
	}
	
	public void addLJViewerListener(LJViewerListener lvl)
	{
		ljviewerlisteners.add(lvl);
	}
	
	public void addLUJViewerListener(LJViewerListener lvl)
	{
		lujviewerlisteners.add(lvl);
	}
	
	public Vector getDJViewerListeners()
	{
		return djviewerlisteners;
	}
	
	public Vector getLJViewerListeners()
	{
		return ljviewerlisteners;
	}
	
	public Vector getLUJViewerListeners()
	{
		return lujviewerlisteners;
	}
	
	public void removeDJViewerListeners()
	{
		djviewerlisteners.removeAllElements();
	}
	
	public void removeLJViewerListeners()
	{
		ljviewerlisteners.removeAllElements();
	}
	
	public void removeLUJViewerListeners()
	{
		lujviewerlisteners.removeAllElements();
	}
	
	
	
	public void activar()
	{
		this.setEnabled(true);
	}
	
	public void desactivar()
	{
		this.setEnabled(false);
	}
	
	public void iniciarHebraProcesadora()
	{
		hebraProcesadora = crearHebraProcesadora();
		hebraProcesadora.iniciarHebra();
	}
	
	public void procesarEvento(DEvent evento)
	{
		if (evento.tipo.intValue() == DJViewerEvent.SINCRONIZACION &&
										!evento.usuario.equals(DConector.Dusuario)) 
		{
			DJViewerEvent infoEstado = obtenerInfoEstado();
			infoEstado.tipo = new Integer(DJViewerEvent.RESPUESTA_SINCRONIZACION.intValue());
			infoEstado.contenido = new ImageIcon(imagen);
			enviarEvento(infoEstado);
		}
		
		else if (evento.tipo.intValue() == DJViewerEvent.CARGADO.intValue())
		{
			DJViewerEvent evt = (DJViewerEvent)evento;
			setImage(evt.contenido.getImage());
			
			Vector v = getLJViewerListeners();
			for (int i = 0; i < v.size(); i++) 
			{
			 ( (LJViewerListener) v.elementAt(i)).cargado();
			}

			  if(evento.usuario.equals(DConector.Dusuario)){
				 v = getLUJViewerListeners();
				 for (int i = 0; i < v.size(); i++) {
					( (LJViewerListener) v.elementAt(i)).cargado();
				 }
			  }
		}
		
		else if (evento.tipo.intValue() == DJViewerEvent.RESPUESTA_SINCRONIZACION.intValue())
		{
			DJViewerEvent evt = (DJViewerEvent)evento;
			
			if (evt.contenido != null)
				setImage(evt.contenido.getImage());
		}	
	}
	
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}
	
	public void sincronizar()
	{
		if (conectadoDC()) {
			DJViewerEvent peticion = new DJViewerEvent();
			peticion.tipo = new Integer(DJViewerEvent.SINCRONIZACION.intValue());
			if (imagen != null) peticion.contenido = new ImageIcon(imagen);
			else peticion.contenido = null;

			
			enviarEvento(peticion);
		 }
	}
	
	public void paint(Graphics g)
	{	
		try{
		  int img_x = getSize().width/2 - imagen.getWidth(this)/2;
	      int img_y = getSize().height/2 - imagen.getHeight(this)/2;
		
	      g.drawImage(imagen, img_x, img_y, this);
		}
		catch(NullPointerException ex)
		{}
	}
	
	public DJViewerListener crearDJListener()
	{
		return new Listener();
	}
	
	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return new HebraProcesadora(this);
	}
	
	private class Listener implements DJViewerListener 
	{

		public void cargado(DJViewerEvent evento) {
			evento.contenido = new ImageIcon(imagen);
			enviarEvento(evento);
		}
	}
	
	private class HebraProcesadora extends HebraProcesadoraBase 
	{

		HebraProcesadora(DComponente dc) 
		{
			super(dc);
		}

		public void run() 
		{
			DJViewerEvent evento = null;
			DJViewerEvent saux = null;
			DJViewerEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			int i = 0;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++) 
			{
				saux = (DJViewerEvent) eventos[j];
				if ( 
						(saux.tipo.intValue() == DJViewerEvent.RESPUESTA_SINCRONIZACION.intValue())) 
					respSincr = saux;
				else 
					vaux.add(saux);
			}

			activar();

			if (respSincr != null) 
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
				setImage(respSincr.contenido.getImage());
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++) 
			{
				saux = (DJViewerEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
					procesarEvento(saux);
				}
			}

			while (true) 
			{
				evento = (DJViewerEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				
				
				
				if (evento.tipo.intValue() == 77 /*&&
						!evento.usuario.equals(DConector.Dusuario)*/) 
				{
					
					DJViewerEvent infoEstado = obtenerInfoEstado();
					infoEstado.tipo = new Integer(DJViewerEvent.RESPUESTA_SINCRONIZACION.intValue());
					infoEstado.contenido = new ImageIcon(imagen);
					enviarEvento(infoEstado);
				}
				else 
				{
					
					procesarEvento(evento);
				}
			}
		}
	}
}
