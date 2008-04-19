package componentes.gui.imagen;

import interfaces.DComponente;
import interfaces.listeners.DJLienzoListener;
import interfaces.listeners.LJLienzoListener;
import interfaces.listeners.LJViewerListener;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import aplicacion.fisica.documentos.Documento;

import Deventos.DEvent;
import Deventos.DJLienzoEvent;
import Deventos.enlaceJS.DConector;
import componentes.DComponenteBase;
import componentes.HebraProcesadoraBase;

public class DIPanelDibujo extends DComponenteBase
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DILienzo lienzo = null;
	private ControlesDibujo controles = null;
	private BarraEstado barra = null;
	
    // Listeners
	private Vector dj_lienzo_listeners = new Vector(5);
	private Vector lj_lienzo_listeners = new Vector(5);
	private Vector luj_lienzo_listeners = new Vector(5);
	private HebraProcesadora hebraProcesadora;
	private JFrame padre = null;
	private JScrollPane jsp = null;
	private String pathDocumento;
	
	
	public String getPathDocumento()
	{
		return pathDocumento;
	}

	public void setPathDocumento(String pathDocumento)
	{
		this.pathDocumento = pathDocumento;
	}

	public void setPadre(JFrame unaVentana) {
		padre = unaVentana;
	}
	
	/**
	 * Crea un nuevo objeto de la clase DIPanelDibujo
	 * @param nombre nombre del objeto en la BD
	 * @param conexionDC
	 * @param padre
	 */
	public DIPanelDibujo( String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		
		lienzo = new DILienzo("lienzo", true, this);
		lienzo.setPadre(this.padre);
		
		barra = new BarraEstado(lienzo);
		controles = new ControlesDibujo(lienzo, barra);
		controles.setPadre(this.padre);
		
		init();
	}
	
	/**
	 * Inicializa el objeto
	 */
	public void init() {
		this.setLayout(new BorderLayout());
		
		/*JPanel aux = new JPanel( new BorderLayout() );
		aux.setBorder(new EtchedBorder(4));
		aux.add(lienzo, BorderLayout.CENTER);
		*/
		
		//JScrollPane jsp = new JScrollPane(lienzo,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//jsp.setLayout(new BorderLayout());
		if (jsp==null) jsp = new JScrollPane(lienzo, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.add(jsp , 
				BorderLayout.CENTER);
		
		this.add(controles, BorderLayout.NORTH);
		this.add(barra, BorderLayout.SOUTH);
	}
	
	
	public DILienzo getLienzo(){
		return lienzo;
	}
	
	/**
	 * Establece la imagen de fondo con la que se trabajar‡
	 * @param img objeto imagen que se quiere establecer como fondo
	 */
	public void setImagen(String img) {
		lienzo.setImagen(img);
		jsp.repaint();
		jsp.validate();
	}
	
	/**
	 * Establece la imagen de fondo con la que se trabajar‡
	 * @param img path de la imagen que se quiere establecer como fondo
	 */
	public void setImagen(Image img){
		lienzo.setImagen(img);
		jsp.repaint();
		jsp.validate();
	}
	
	
	
	public void sincronizar()
	{
		if (conectadoDC()) {
			DJLienzoEvent peticion = new DJLienzoEvent();
			peticion.tipo = new Integer(DJLienzoEvent.SINCRONIZACION.intValue());

			
			enviarEvento(peticion);
		 }
	}
	
	
	/**
	 * Clase encargada de estar a la "escucha" de los eventos de lienzo 
	 * @author Ana Belen Pelegrina Ortiz
	 */
	private class Listener implements DJLienzoListener 
	{

		public void cargado(DJLienzoEvent evento) {
			
			enviarEvento(evento);
		}
	}
	
	/**
	 * Agrega un nuevo listener DJ
	 * @param dvl nuevo listener a agregar
	 */	
	public void addDJLienzoListener(DJLienzoListener dvl)
	{
		dj_lienzo_listeners.add(dvl);
	}
	
	
	/**
	 * Agrega un nuevo listener LJ
	 * @param dvl nuevo listener a agregar
	 */
	public void addLJLienzoListener(LJLienzoListener lvl)
	{
		lj_lienzo_listeners.add(lvl);
	}
	
	/**
	 * Agrega un nuevo listener LUJ
	 * @param dvl nuevo listener a agregar
	 */
	public void addLUJLienzoListener(LJLienzoListener lvl)
	{
		luj_lienzo_listeners.add(lvl);
	}
	
	
	/**
	 * Permite recuperar los listeners DJ
	 * @returm los listener
	 */
	public Vector getDJLienzoListeners()
	{
		return dj_lienzo_listeners;
	}
	
	/**
	 * Permite recuperar los listeners LJ
	 * @returm los listener
	 */
	public Vector getLJLienzoListeners()
	{
		return lj_lienzo_listeners;
	}
	
	/**
	 * Permite recuperar los listeners LUJ
	 * @returm los listener
	 */
	public Vector getLUJLienzoListeners()
	{
		return luj_lienzo_listeners;
	}
	
	/**
	 * Elimina los listeners DJ
	 */
	public void removeDJLienzoListeners()
	{
		dj_lienzo_listeners.removeAllElements();
	}
	
	/**
	 * Elimina los listeners LJ
	 */
	public void removeLJLienzoListeners()
	{
		lj_lienzo_listeners.removeAllElements();
	}
	
	/**
	 * Elimina los listeners LUJ
	 */
	public void removeLUJLienzoListeners()
	{
		luj_lienzo_listeners.removeAllElements();
	}
	
	/**
	 * Inicia la hebra procesadora de eventos
	 */
	public void iniciarHebraProcesadora()
	{
		hebraProcesadora = new HebraProcesadora(this);
		hebraProcesadora.iniciarHebra();
	}
	
	
	/**
	 * Obtiene la informaci—n actual y la carga en un evento de lienzo
	 * @return el evento con los datos actuales
	 */
	public DJLienzoEvent obtenerInfoEstado()
	{
		DJLienzoEvent de = new DJLienzoEvent();
		
		
		return de;
	}
	
	
	/**
	 * Procesa un evento recibido
	 * @arg evento evento recibido
	 */
	public void procesarEvento(DEvent evento)
	{
		if (evento.tipo.intValue() == DJLienzoEvent.SINCRONIZACION &&
										!evento.usuario.equals(DConector.Dusuario)) 
		{
			DJLienzoEvent infoEstado = obtenerInfoEstado();
			infoEstado.tipo = new Integer(DJLienzoEvent.RESPUESTA_SINCRONIZACION.intValue());
			
			//lienzo.procesarEvento(evento);
			
			
		}
		
		else if (evento.tipo.intValue() == DJLienzoEvent.NUEVA_PAGINA.intValue())
		{
			DJLienzoEvent evt = (DJLienzoEvent)evento;
			setImagen(evt.imagen.getImage());
			
			Vector v = getLJLienzoListeners();
			for (int i = 0; i < v.size(); i++) 
			{
			 ( (LJViewerListener) v.elementAt(i)).cargado();
			}

			  if(evento.usuario.equals(DConector.Dusuario)){
				 v = getLUJLienzoListeners();
				 for (int i = 0; i < v.size(); i++) {
					( (LJViewerListener) v.elementAt(i)).cargado();
				 }
			  }
		}
		
		else if (evento.tipo.intValue() == DJLienzoEvent.RESPUESTA_SINCRONIZACION.intValue())
		{
			DJLienzoEvent evt = (DJLienzoEvent)evento;
			
			if (evt.imagen != null)
				setImagen(evt.imagen.getImage());
		}	
		else if (evento.tipo.intValue() == DJLienzoEvent.NUEVA_ANOTACION.intValue()) {
			System.out.println("Recibido nuevo trazo");
		}
	}
	
	
	/**
	 * 
	 * @author anab
	 *
	 */
	private class HebraProcesadora extends HebraProcesadoraBase 
	{

		HebraProcesadora(DComponente dc) 
		{
			super(dc);
		}

		public void run() 
		{
			DJLienzoEvent evento = null;
			DJLienzoEvent saux = null;
			DJLienzoEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			int i = 0;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++) 
			{
				saux = (DJLienzoEvent) eventos[j];
				if ( 
						(saux.tipo.intValue() == DJLienzoEvent.RESPUESTA_SINCRONIZACION.intValue())) 
					respSincr = saux;
				else 
					vaux.add(saux);
			}

			activar();

			if (respSincr != null) 
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
				setImagen(respSincr.imagen.getImage());
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++) 
			{
				saux = (DJLienzoEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
					procesarEvento(saux);
				}
			}

			while (true) 
			{
				evento = (DJLienzoEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				procesarEvento(evento);
			}
		}
	}

	public int obtenerNumComponentesHijos()
	{
		return 1;
	}
	
	public DComponente obtenerComponente(int i) {
		return lienzo;
		
	}
	
	synchronized public void procesarEventoHijo(DEvent evento) {
		 try {
			if (evento.tipo.intValue() == DJLienzoEvent.NUEVA_ANOTACION) {
			  enviarEvento(evento);
			}
		 }
		 catch (Exception e) {

		 }
	  }

	public void setDocumento(Documento d)
	{
		lienzo.setDocumento(d);
		
		lienzo.setSize(this.getSize().width-jsp.getHorizontalScrollBar().getWidth(), this.getSize().height-controles.getHeight()-barra.getHeight()-4);
	}
}
