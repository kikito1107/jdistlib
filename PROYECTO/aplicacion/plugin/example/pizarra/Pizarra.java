package aplicacion.plugin.example.pizarra;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolTip;
import javax.swing.border.EtchedBorder;

import Deventos.DEvent;
import Deventos.DJLienzoEvent;
import Deventos.enlaceJS.DConector;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.HebraProcesadoraBase;
import componentes.gui.imagen.DILienzo;
import componentes.gui.imagen.DIViewer;
import componentes.gui.imagen.DialogoIntroTexto;
import componentes.gui.imagen.figuras.Elipse;
import componentes.gui.imagen.figuras.Figura;
import componentes.gui.imagen.figuras.Linea;
import componentes.gui.imagen.figuras.Rectangulo;
import componentes.gui.imagen.figuras.Texto;
import componentes.gui.imagen.figuras.TrazoManoAlzada;
import componentes.listeners.DJLienzoListener;
import componentes.listeners.LJLienzoListener;


/**
 * Implementacion de un editor basico de imagenes distribuido
 * @author Ana Belen Pelegrina Ortiz
 * @date 9-2-2008
 */
public class Pizarra extends DIViewer implements MouseListener,
		MouseMotionListener
{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2746544910515149389L;

	/**
	 * Ventana en la que se dibuja el lienzo
	 */
	private JFrame padre = null;
	
	/**
	 * Modos de dibujo para las anotaciones
	 */
	public static final int LINEAS = 0;
	public static final int MANO_ALZADA = 1;
	public static final int TEXTO = 2;
	public static final int RECTANGULO = 3;
	public static final int ELIPSE = 4; 
	
	/**Indica si se ha sincronizado ya el lienzo*/
	public boolean sincronizada = false;
	
	/**
	 * Color con el que se dibujan las anotaciones
	 */
    private Color colorActual = Color.RED; 
    
    /**
     * Trazo que permite almancenar un trazo a mano alzada mientras está siendo realizado
     */
    private TrazoManoAlzada trazo = null;
    
    /**
     * Modo de dibujo, puede ser lineas o puntos. Inicialmente es lineas
     */
    int	   modoDibujo = LINEAS;
    
    /**
     * Anotación actualmente seleccionada
     */
    public int anotacionSeleccionada = -1;
    
    /**
     * Vector con las anotaciones realizadas
     */
    Vector<Figura> anotaciones = new Vector<Figura>();
    
    /**
     * Vector que contiene los objetos que han sido borrados
     */
    Vector<Figura> anotacionesBorradas = new Vector<Figura>();
    
    /**
     * Coordenadas del trazo actual
     */
    int x1,y1;
    int x2,y2;
    
    // Listeners
	private Vector dj_lienzo_listeners = new Vector(5);
	private Vector lj_lienzo_listeners = new Vector(5);
	private Vector luj_lienzo_listeners = new Vector(5);
    
	/**
	 * Hebra encargada del procesamiento de los eventos
	 */
    private HebraProcesadoraBase hebraProcesadora = null;
    
	/**
	 * identificador del lienzo
	 */
	private int id = (new Random()).nextInt(1000);
	
	/**
	 * indica el zoom actual aplicado al documento
	 */
	private float zoom = 1;
	
    /**
     * Constructor de la clase
     * @param nombre nombre del componente
     * @param conexionDC 
     * @param padre
     */
	public Pizarra( String nombre, boolean conexionDC, DComponenteBase padre)
	{
		super(nombre, conexionDC, padre);
		
		setBackground(Color.white);
		addMouseMotionListener(this);
		addMouseListener(this); 

		try{
			init();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	

	
	/**
	 * Establece el color actual para dibujar
	 * @param unColor el nuevo color actual
	 */
	public void setColor(Color unColor) {
		colorActual = unColor;
	}
	
	/**
	 * Establece el trazo del dibujo
	 * @param trazo tipo de trazo
	 */
	public void setTrazo(int trazo) {
		if (trazo == Pizarra.LINEAS) {
			modoDibujo = Pizarra.LINEAS;
		}
		else if (trazo == Pizarra.MANO_ALZADA)
			modoDibujo = Pizarra.MANO_ALZADA;
		else if (trazo == Pizarra.TEXTO)
			modoDibujo = Pizarra.TEXTO;
		else if (trazo == Pizarra.RECTANGULO)
			modoDibujo = RECTANGULO;
		else if (trazo == Pizarra.ELIPSE)
			modoDibujo = ELIPSE;
	}
	
	/**
	 * Inicializa el lienzo
	 * @throws Exception
	 */
	private void init() throws Exception
	{
		addDJLienzoListener(new Listener());
		
		this.createToolTip();
		
		this.setToolTipText("");
		
		this.setBorder(new EtchedBorder(2));
	}
	
	
	/**
	 * @return the objetoSeleccionado
	 */
	public int getObjetoSeleccionado()
	{
		return anotacionSeleccionada;
	}

	/**
	 * @param anotacionSeleccionada the objetoSeleccionado to set
	 */
	public void setObjetoSeleccionado(int objeto)
	{
		if (objeto > -1  && objeto < anotaciones.size()) {
			this.anotacionSeleccionada = objeto;
			repaint();
		}
		else if (objeto > -1) {
			this.anotacionSeleccionada = 0;
			repaint();
		}
		else
			this.anotacionSeleccionada = 0;
	}
	

	/**
	 * Rehace el último trazo
	 */
	public void rehacer()
	{
		int numBorrados = anotacionesBorradas.size();
		
		if (numBorrados > 0) {

			Figura objeto = anotacionesBorradas.remove(numBorrados - 1);
			anotaciones.add(objeto);
			repaint();
		}
		
	}

	/**
	 * Devuelve el color actual
	 * @return el color actual
	 */
	public Color getColor()
	{
		
		return colorActual;
	}
	
	
	//***********************************************************************
	//*********************** EVENTOS DE RATON ******************************
	//***********************************************************************
	
	public void mouseClicked(MouseEvent e){}

	public void mouseEntered(MouseEvent e){}

	public void mouseExited(MouseEvent e){}
	
	public void mouseMoved(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e)
	{
        e.consume();
        switch (modoDibujo) {
            case LINEAS:
            case RECTANGULO:
            case ELIPSE:
                x1 = e.getX();
                y1 = e.getY();
                x2 = -1;
                break;
            case MANO_ALZADA:
            
            	trazo = new TrazoManoAlzada(x1,y1);
            	trazo.setColor(colorActual);
                
            	Linea l = new Linea(e.getX(), e.getY(), e.getX()+1, e.getY()+1);
            	l.setColor(colorActual);
            	trazo.agregarLinea(l);
                
                
                
                x1 = e.getX();
                y1 = e.getY();
                repaint();
                break;
                
                
            case TEXTO:
            	x1 = e.getX();
            	y1 = e.getY();
            	
            	DialogoIntroTexto ct = new DialogoIntroTexto(this.padre);
            	
            	String rsp = ct.obtenerTexto();
            	
            	if (rsp != null) {
 	            	Texto t = new Texto(x1, y1, rsp);

 	            	//System.out.println("x: " + x1 + "  y: " + y1);
 	            	t.setColor(colorActual);
	            	
	            	
	            	DJPizarraEvent evt2 = new DJPizarraEvent();
	                
	                evt2.dibujo = t;
	                evt2.tipo = new Integer(DJPizarraEvent.NUEVA_ANOTACION.intValue());
	                evt2.rol = DConector.Drol;
	                
	                anotaciones.add(t);
	                
	                enviarEvento(evt2);
	            	repaint();
            	}
            	break;
            default:
                break;
        }
	}
	
	public void mouseReleased(MouseEvent e)
	{
        e.consume();
        switch (modoDibujo) {
        	case RECTANGULO:
        	case ELIPSE:
        		
        		if (x1 >= x2){
        			int xaux = x1;
        			x1 = x2;
        			x2 = xaux;
        		}
        		if	( y1 >= y2) {
        			int yaux = y1;
        			y1 = y2;
        			y2 = yaux;
        		}
        		
                Figura f;
                if (modoDibujo == RECTANGULO)
                	f = new Rectangulo(x1,y1,x2,y2);
                else
                	f = new Elipse(x1,y1, x2,y2);
                
                f.setColor(colorActual);
	                
	            DJPizarraEvent evt = new DJPizarraEvent();
	                
	            evt.dibujo = f;
	            evt.tipo = new Integer(DJPizarraEvent.NUEVA_ANOTACION.intValue());
	            evt.rol = new String(DConector.Drol);
	            enviarEvento(evt);
	            
	            anotaciones.add(f);

	            x2 = -1;
	            repaint();
	            
        		break;
        	case LINEAS:
                
                Linea l = new Linea(x1, y1, e.getX(), e.getY());
                l.setColor(colorActual);
                

                
                DJPizarraEvent evt2 = new DJPizarraEvent();
                
                evt2.dibujo = l;
                evt2.tipo = new Integer(DJPizarraEvent.NUEVA_ANOTACION.intValue());
                evt2.rol = new String(DConector.Drol);
                enviarEvento(evt2);
                
                anotaciones.add(l);
                
                x2 = -1;
                
                repaint();
                break;
            case MANO_ALZADA:
            	DJPizarraEvent evt3 = new DJPizarraEvent();
                
                evt3.dibujo = trazo;
                evt3.tipo = new Integer(DJPizarraEvent.NUEVA_ANOTACION.intValue());
                evt3.rol = new String(DConector.Drol);
                
                enviarEvento(evt3);

                anotaciones.add(trazo);
	                
	            trazo = null;
                
            default:
                break;
        }
	}

	public void mouseDragged(MouseEvent e){
        e.consume();
        switch (modoDibujo) {
            case LINEAS:
            case RECTANGULO:
            case ELIPSE:
            	x2 = e.getX();
                y2 = e.getY();
                repaint();
                break;
                
            case MANO_ALZADA:
                Linea r = new Linea(x1, y1, e.getX(), e.getY());
                r.setColor(colorActual);
                
                trazo.agregarLinea(r);
                x1 = e.getX();
                y1 = e.getY();
                
                repaint();
        }
        
	}

	//***********************************************************************
	//******************** REPINTADO DEL LIENZO *****************************
	//***********************************************************************
	public void paint (Graphics g) {
		
		
		
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());


			int np = anotaciones.size();

			/* Dibujamos las figuras actuales */
			for (int i=0; i < np; i++) {

				Figura f = anotaciones.get(i);

				if (i != anotacionSeleccionada) {
					g.setColor(f.getColor());
				}
				else {
					g.setColor( DILienzo.invertirColor( f.getColor() ) );
				}

				f.dibujar(g, zoom);
			}

			// si estamos dibujando una linea...
			if (modoDibujo == LINEAS) {
				g.setColor(colorActual);
				if (x2 != -1) g.drawLine(x1, y1, x2, y2);
			}
			else if (modoDibujo == RECTANGULO || modoDibujo == ELIPSE) {
				g.setColor(colorActual);
				if (x2 != -1) {
					int X1 = x1, X2 = x2, Y1 = y1, Y2 = y2;

					if (x1 >= x2){
						X1 = x2;
						X2 = x1;
					}
					if	( y1 >= y2) {
						Y1 = y2;
						Y2 = y1;
					}
					if (modoDibujo == RECTANGULO)
						g.drawRect(X1, Y1, X2-X1, Y2-Y1);
					else
						g.drawOval(X1, Y1, X2-X1, Y2-Y1);
				}
			}
			else if (modoDibujo == Pizarra.MANO_ALZADA) {
				if (trazo != null) {
					g.setColor(this.colorActual);
					trazo.dibujar(g, 1);
				}
				
			}
	}
	


	/**
	 * Deshace el ultimo trazo realizado en el lienzo
	 */
	public void deshacer() {
		// si se ha dibujado algo...
		
		
		if (anotaciones.size() > 0) {
			this.anotacionesBorradas.add(anotaciones.remove(anotaciones.size()-1));
			// repintamos el lienzo
			repaint();
		}
	}
	
	
	/**
	 * Elimina todos los trazos realizados en el lienzo hasta el momento
	 */
	public void limpiarLienzo() {
		anotacionesBorradas.addAll(anotaciones);
		anotaciones.clear();
		
		repaint();
	}
	
	/**
	 * Borra un objeto de la lista de objetos dibujados
	 * @param posicion posicion del objeto a borrar
	 * @param i 
	 */
	public void borrarObjeto( int posicion) {
		
		if (posicion > -1 && posicion < anotaciones.size()) {
			this.anotacionesBorradas.add(anotaciones.remove(anotaciones.size()-1));
			repaint();
		}
	}
	
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}
	
	public void sincronizar()
	{
		if (conectadoDC()) {
			
			DJPizarraEvent e = new DJPizarraEvent();
			e.tipo = new Integer(DJPizarraEvent.SINCRONIZACION.intValue());
			this.enviarEvento(e);
		 }
	}
	
	
	//***********************************************************************
	//**************************** LISTENERS ********************************
	//***********************************************************************
	
	/**
	 * Clase encargada de estar a la "escucha" de los eventos de lienzo 
	 * @author Ana Belen Pelegrina Ortiz
	 */
	private class Listener implements DJLienzoListener 
	{

		public void cargado(DJPizarraEvent evento) {
			enviarEvento(evento);
		}

		public void cargado(DJLienzoEvent evento)
		{
			// TODO Auto-generated method stub
			
		}
	}
	

	/**
	 * Agrega un nuevo listener
	 * @param dvl nuevo listener a agregar DJ
	 */
	public void addDJLienzoListener(DJLienzoListener dvl)
	{
		dj_lienzo_listeners.add(dvl);
	}
	
	/**
	 * Agrega un nuevo listener
	 * @param lvl nuevo listener a agregar LJ
	 */
	public void addLJLienzoListener(LJLienzoListener lvl)
	{
		lj_lienzo_listeners.add(lvl);
	}
	
	/**
	 * Agrega un nuevo listener LUJ
	 * @param lvl nuevo listener a agregar
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
	
	//***********************************************************************
	//******************* PROCESAMIENTO DE EVENTOS **************************
	//***********************************************************************
	
	/**
	 * Inicia la hebra procesadora de eventos
	 */
	public void iniciarHebraProcesadora()
	{
		hebraProcesadora = new HebraProcesadora(this);
		hebraProcesadora.iniciarHebra();
	}
	
	
	/**
	 * Procesa un evento recibido
	 * @arg evento evento recibido
	 */
	@SuppressWarnings("static-access")
	public void procesarEvento(DEvent evento)
	{
	
		if (evento.tipo.intValue() == DJPizarraEvent.SINCRONIZACION.intValue() &&
										!evento.usuario.equals(DConector.Dusuario)) 
		{
			
			DJPizarraEvent evt = (DJPizarraEvent) evento;
			
				evt.tipo = DJPizarraEvent.RESPUESTA_SINCRONIZACION;
				evt.dibujos = anotaciones;
				
				enviarEvento(evt);
				
		}
		else if (evento.tipo.intValue() == DJPizarraEvent.RESPUESTA_SINCRONIZACION.intValue() &&
				!evento.usuario.equals(DConector.Dusuario) && !sincronizada)
		{
			DJPizarraEvent evt = (DJPizarraEvent)evento;
			
			anotaciones = evt.dibujos;
			
			repaint();
		}	
		
		else if (evento.tipo.intValue() == DJPizarraEvent.NUEVA_ANOTACION.intValue())
		 {
			DJPizarraEvent evt = (DJPizarraEvent ) evento;
			
			//JOptionPane.showMessageDialog(null, "Recibida nueva anotacion para el fichero " + evt.path);
			
			anotaciones.add(evt.dibujo);
			repaint();
		}
		else if (evento.tipo.intValue() == DJPizarraEvent.LIMPIEZA_LIENZO.intValue()) {
				this.limpiarLienzo();
		}
		else if (evento.tipo.intValue() == DJPizarraEvent.DESHACER.intValue()
				&& !evento.usuario.equals(DConector.Dusuario)) {
				
					deshacer();
		}
		else if (evento.tipo.intValue() == DJPizarraEvent.BORRADO.intValue()) {
			
			DJPizarraEvent evt = (DJPizarraEvent ) evento;
			
			
				int posicion = evt.aBorrar.intValue();
				this.borrarObjeto(posicion);
		}
		else if (evento.tipo.intValue() == DJPizarraEvent.REHACER.intValue()) {
				this.rehacer();
		}
	}
	
	
	/**
	 * Hebra que se encarga de procesar los eventos asociados al lienzo
	 * @author Ana Belen Pelegrina Ortiz
	 */
	private class HebraProcesadora extends HebraProcesadoraBase 
	{

		/**
		 * Contructor de la clase
		 * @param dc componente cuyos eventos van a ser procesados
		 */
		HebraProcesadora(DComponente dc) 
		{
			super(dc);
		}

		/**
		 * Inicia la ejecucion de la hebra
		 */
		public void run() 
		{
			DJPizarraEvent evento = null;
			DJPizarraEvent saux = null;
			DJPizarraEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			int i = 0;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++) 
			{
				saux = (DJPizarraEvent) eventos[j];
				if ( 
						(saux.tipo.intValue() == DJPizarraEvent.RESPUESTA_SINCRONIZACION.intValue())) 
					respSincr = saux;
				else 
					vaux.add(saux);
			}

			activar();

			if (respSincr != null) 
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado.intValue());
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++) 
			{
				saux = (DJPizarraEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado.intValue()) {
					procesarEvento(saux);
				}
			}

			while (true) 
			{
				evento = (DJPizarraEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				procesarEvento(evento);
			}
		}
	}
}
