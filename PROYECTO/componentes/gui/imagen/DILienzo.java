package componentes.gui.imagen;

import interfaces.DComponente;
import interfaces.listeners.DJLienzoListener;
import interfaces.listeners.LJLienzoListener;
import interfaces.listeners.LJViewerListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;
import java.awt.*;
import java.io.Serializable;
import java.math.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.Transfer;
import aplicacion.fisica.TransferP2P;
import aplicacion.fisica.documentos.Anotacion;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.eventos.DDocumentEvent;
import Deventos.DEvent;
import Deventos.DJLienzoEvent;
import Deventos.DJViewerEvent;
import Deventos.enlaceJS.DConector;
import Deventos.enlaceJS.TokenFichero;
import componentes.DComponenteBase;
import componentes.HebraProcesadoraBase;
import componentes.gui.imagen.figuras.*;


/**
 * Implementacion de un editor basico de imagenes distribuido
 * @author Ana Belen Pelegrina Ortiz
 * @date 9-2-2008
 */
public class DILienzo extends DIViewer implements MouseListener,
		MouseMotionListener
{

	
	/**
	 * Ventana en la que se dibuja el lienzo
	 */
	private JFrame padre = null;
	
	JToolTip tt = new JToolTip();
	
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
     * documento de trabajo
     */
    private Documento doc = new Documento();
    
    /**
     * Anotación actualmente seleccionada
     */
    public int anotacionSeleccionada = -1;
    
    /**
     * Vector que contiene los objetos que han sido borrados
     */
    Vector<Anotacion> anotacionesBorradas = new Vector<Anotacion>();
    
    /**
     * Pagina actual del documento visualizada
     */
    private int paginaActual = 0;

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
	public DILienzo( String nombre, boolean conexionDC, DComponenteBase padre)
	{
		super(nombre, conexionDC, padre);
		
		setBackground(Color.white);
		addMouseMotionListener(this);
		addMouseListener(this); 
		doc.setUsuario(DConector.Dusuario);
		doc.setRol(DConector.Drol);
		doc.setPath("");

		try{
			init();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Establece la imagen de fondo con la que se trabajará
	 * @param img objeto imagen que se quiere establecer como fondo
	 */
	public void setImagen(Image img) {
		MediaTracker mediatracker = new MediaTracker(this);
		mediatracker.addImage(img, 0);
		
		try{
			mediatracker.waitForID(0);
			//this.limpiarLienzo();
			repaint();
		}
		catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Establece la imagen de fondo con la que se trabajará
	 * @param filename path de la imagen que se quiere establecer como fondo
	 */
	public void setImagen(String filename) {
		MediaTracker mediaTracker = new MediaTracker(this);
		Image imagen = Toolkit.getDefaultToolkit().getImage(filename);
		paginaActual++;
		
		System.out.println("Agregada imagen numero " + paginaActual);
		
		mediaTracker.addImage(imagen, 0);
		try
		{
			mediaTracker.waitForID(0);
			doc.addPagina(imagen);
			repaint();
		}
		catch (InterruptedException ie)
		{
			System.err.println(ie);
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
	 * Devuelve el documento con el que se está trabajando actualmente
	 * @return
	 */
	public Documento getDocumento(){
		return doc;
	}
	
	/**
	 * Establece el documento de trabajo
	 * @param nuevo documento de trabajo
	 */
	public void setDocumento(Documento nuevo) {
		doc = nuevo;
		paginaActual = 1;
		
		repaint();
	}
	
	/**
	 * Establece el trazo del dibujo
	 * @param trazo tipo de trazo
	 */
	public void setTrazo(int trazo) {
		if (trazo == DILienzo.LINEAS) {
			modoDibujo = DILienzo.LINEAS;
		}
		else if (trazo == DILienzo.MANO_ALZADA)
			modoDibujo = DILienzo.MANO_ALZADA;
		else if (trazo == DILienzo.TEXTO)
			modoDibujo = DILienzo.TEXTO;
		else if (trazo == DILienzo.RECTANGULO)
			modoDibujo = RECTANGULO;
		else if (trazo == DILienzo.ELIPSE)
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
	 * Devuelve la ventana en la que se encuentra el lienzo
	 */
	public JFrame getPadre(){
		return this.padre;
	}
	
	/**
	 * Establece la ventana en la que es dibujado el lienzo
	 * @param j ventana en la que se dibuja el lienzo
	 */
	public void setPadre (JFrame j) {
		padre = j;
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
		if (objeto > -1 && objeto < doc.getPagina(paginaActual-1).getAnotaciones().size() ) {
			this.anotacionSeleccionada = objeto;
			repaint();
		}
		else if (objeto > -1 && objeto >= doc.getPagina(paginaActual-1).getAnotaciones().size()) {
			this.anotacionSeleccionada = 0;
			repaint();
		}
		else
			this.anotacionSeleccionada = 0;
		//System.out.println("valor objeto seleccionado " + this.objetoSeleccionado);
	}
	
	/**
	 * Método que permite invertir un color
	 * @param c color a invertir
	 * @return color invertido
	 */
	public static Color invertirColor(Color c ) {
		int red, green, blue;
    	
    	red = c.getRed();
    	green = c.getGreen();
    	blue = c.getBlue();
    	
    	red = 255 - red;
    	blue = 255 - blue;
    	green = 255 - green; 
    	
    	return new Color(red, green, blue);
	}

	/**
	 * Rehace el último trazo
	 */
	public void rehacer()
	{
		int numBorrados = anotacionesBorradas.size();
		
		if (numBorrados > 0) {

			Anotacion objeto = anotacionesBorradas.remove(numBorrados - 1);
			
			
			doc.getPagina(paginaActual-1).getAnotaciones().add(objeto);
			
			repaint();
		}
		
	}

	/**
	 * Borra una página del documento
	 * @param i número de la página a borrar (entre 0 y numPaginas-1)
	 */
	public void borrarPagina(int i)	{
		doc.delPagina(i);
		
		// afecta al nuevo orden de páginas
		if (paginaActual-1 > i )
			paginaActual--;
		else if(i==0 && paginaActual-1==i)
			paginaActual = 1;
		
		repaint();
		System.gc();
	}

	/**
	 * Devuelve el número de la página actual
	 * @return
	 */
	public int getNumPaginas(){
		return doc.getNumeroPaginas();
	}
	
	/**
	 * Recupera la página actual del documento visualizado
	 * @return el número de la página actual
	 */
	public int getPaginaActual(){
		return this.paginaActual;
	}
	
	/**
	 * Establece la página actual con la que se está trabajando
	 * @param numPagina número de la nueva página actual
	 */
	public void setPaginaActual(int numPagina){
		if (numPagina > 0 && numPagina < getNumPaginas()+1) {
			paginaActual = numPagina;
			
			anotacionesBorradas = new Vector<Anotacion>();
			
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
		
		//
		//ToolTipManager.
		
		int x = e.getX();
		int y = e.getY();
		
		//recorremos todas las anotaciones de la página actual
		
		boolean encontrado = false;
		Vector<Anotacion> v = doc.getPagina(this.paginaActual-1).getAnotaciones();
		
		System.out.println("x: "+ x + " y: " + y);
		
		for (int i=0; i<v.size() && !encontrado; ++i) {
			if (v.get(i).getContenido().pertenece(x, y)) {
				
				String texto = "Usuario: " + v.get(i).getUsuario();
				
				texto += "\nRol" + v.get(i).getRol();
				
				this.setToolTipText(texto);
				encontrado = true;
			}
		}
		
		if(!encontrado)
			this.setToolTipText("");
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
	            	
	            	
	            	DJLienzoEvent evt2 = new DJLienzoEvent();
	                
	                evt2.color = colorActual;
	                evt2.dibujo = t;
	                evt2.numPagina = new Integer(paginaActual-1);
	                evt2.tipo = new Integer(DJLienzoEvent.NUEVA_ANOTACION.intValue());
	                evt2.path = new String(doc.getPath());
	                evt2.rol = DConector.Drol;
	                
	                enviarEvento(evt2);
	                
	                if (doc.getNumeroPaginas() > 0) {
	                
		                Anotacion a = new Anotacion();
		                a.setContenido(t);
		                a.setRol(DConector.Drol);
		                a.setUsuario(DConector.Dusuario);
		                
		                doc.getPagina(this.paginaActual-1).addAnotacion(a);
	                }
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
	                
	            DJLienzoEvent evt = new DJLienzoEvent();
	                
	            evt.color = colorActual;
	            evt.dibujo = f;
	            evt.numPagina = new Integer(paginaActual-1);
	            evt.tipo = new Integer(DJLienzoEvent.NUEVA_ANOTACION.intValue());
	            evt.path = new String(doc.getPath());
	            evt.rol = new String(DConector.Drol);
	            enviarEvento(evt);
	     
	            if (doc.getNumeroPaginas() > 0) {
	                
	                Anotacion a = new Anotacion();
	                a.setContenido(f);
	                a.setRol(DConector.Drol);
	                a.setUsuario(DConector.Dusuario);
	                
	                doc.getPagina(this.paginaActual-1).addAnotacion(a);
                }
	            
	            x2 = -1;
	            repaint();
	            
        		break;
        	case LINEAS:
                
                Linea l = new Linea(x1, y1, e.getX(), e.getY());
                l.setColor(colorActual);
                

                
                DJLienzoEvent evt2 = new DJLienzoEvent();
                
                evt2.color = colorActual;
                evt2.dibujo = l;
                evt2.numPagina = new Integer(paginaActual-1);
                evt2.tipo = new Integer(DJLienzoEvent.NUEVA_ANOTACION.intValue());
                evt2.path = new String(doc.getPath());
                evt2.rol = new String(DConector.Drol);
                enviarEvento(evt2);
                
                if (doc.getNumeroPaginas() > 0) {
	                
	                Anotacion a = new Anotacion();
	                a.setContenido(l);
	                a.setRol(DConector.Drol);
	                a.setUsuario(DConector.Dusuario);
	                
	                doc.getPagina(this.paginaActual-1).addAnotacion(a);
                }
                
                x2 = -1;
                
                repaint();
                break;
            case MANO_ALZADA:
            	DJLienzoEvent evt3 = new DJLienzoEvent();
                
                evt3.color = colorActual;
                evt3.dibujo = trazo;
                evt3.numPagina = new Integer(paginaActual-1);
                evt3.tipo = new Integer(DJLienzoEvent.NUEVA_ANOTACION.intValue());
                evt3.path = new String(doc.getPath());
                evt3.rol = new String(DConector.Drol);
                
                enviarEvento(evt3);
                
                if (doc.getNumeroPaginas() > 0) {
	                
	                Anotacion a = new Anotacion();
	                a.setContenido(trazo);
	                a.setRol(DConector.Drol);
	                a.setUsuario(DConector.Dusuario);
	                
	                doc.getPagina(this.paginaActual-1).addAnotacion(a);
	                
	                trazo = null;
                }
                
            default:
                break;
        }
	}
	
	@Override
	public void setSize(int width, int height){
		super.setSize(width, height);
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
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
		
		
		if (doc != null && doc.getNumeroPaginas() > 0) {
		
			try{
				Image imagen = doc.getPagina(paginaActual-1).getImagen();
				int img_w = (int)((float)imagen.getWidth(null) );
				int img_h = (int)((float)imagen.getHeight(null) );
				
				int lienzo_w = this.getWidth();
				int lienzo_h = this.getHeight();

				int img_x=0, img_y=0;
				
				if (img_w < lienzo_w && img_h < lienzo_h){
					
					img_x = getSize().width/2 - img_w/2;
					img_y = getSize().height/2 - img_h/2;
					
					lienzo_w = img_w;
					lienzo_h = img_h;
					
				}
				else {
					if (img_w > lienzo_w)
						this.setSize(img_w, lienzo_h);
					if (img_h > lienzo_h)
						this.setSize(lienzo_w, img_h);
				}
				
				
				g.clearRect(0, 0, this.getWidth(), this.getHeight());
				g.drawImage(imagen, img_x, img_y, img_w, img_h, this);
			}
			catch(NullPointerException ex)
			{}
		}
		else {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		
		if (doc!=null && doc.getNumeroPaginas() > 0) {	
			Vector<Anotacion> v = doc.getPagina(paginaActual-1).getAnotaciones();

			int np = v.size();

			/* Dibujamos las figuras actuales */
			for (int i=0; i < np; i++) {

				Figura f = v.get(i).getContenido();

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
			else if (modoDibujo == DILienzo.MANO_ALZADA) {
				if (trazo != null) {
					g.setColor(this.colorActual);
					trazo.dibujar(g, 1);
				}
				
			}
		}
	}
	


	/**
	 * Deshace el ultimo trazo realizado en el lienzo
	 * @param i 
	 */
	public void deshacer(int i) {
		// si se ha dibujado algo...
		if (doc.getPagina(i).getAnotaciones().size() > 0) {
			
			Vector<Anotacion> anotaciones = doc.getPagina(i).getAnotaciones();
			
			// eliminamos el último elemento dibujado y su color
			Anotacion a = anotaciones.remove(anotaciones.size()-1);
			
			// la agregamos a la lista de anotaciones borradas
			this.anotacionesBorradas.add(a);
			
			// repintamos el lienzo
			repaint();
		}
	}
	
	
	/**
	 * Elimina todos los trazos realizados en el lienzo hasta el momento
	 */
	public void limpiarLienzo() {
		
		int np = doc.getNumeroPaginas();
		
		for (int i=0; i<np; ++i){
			doc.getPagina(i).getAnotaciones().removeAllElements();
		}
		repaint();
	}
	
	/**
	 * Borra un objeto de la lista de objetos dibujados
	 * @param posicion posicion del objeto a borrar
	 * @param i 
	 */
	public void borrarObjeto( int posicion, int i) {
		
		if (posicion > -1 && posicion < doc.getPagina(i).getAnotaciones().size()) {
			Anotacion a = doc.getPagina(i).getAnotaciones().remove(posicion);
			anotacionSeleccionada--;
			
			anotacionesBorradas.add(a);
			
			if (anotacionSeleccionada == -1)
				anotacionSeleccionada = 0;
			
			repaint();
		}
	}
	
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}
	
	public void sincronizar()
	{
		if (conectadoDC()  && doc.getPath()!= null && !doc.getPath().equals("")) {
			
			DJLienzoEvent e = new DJLienzoEvent();
			e.tipo = new Integer(DJLienzoEvent.SINCRONIZACION.intValue());
			e.path = new String(doc.getPath());
			
			String ip = null;//
			
			
			//TokenFichero tk = DConector.obtenerDC().buscarTokenFichero(doc.getPath());
			
			// si no hay que sincronizar el fichero
			if ( true){//!tk.sincronizar.booleanValue() ) {
				
				Transfer t = new Transfer(ClienteFicheros.ipConexion, doc.getPath() );

				Documento p = t.receive();
				
				if (p!= null)
					doc = p;
				else JOptionPane.showMessageDialog(this.padre, "Eror al cargar el fichero "+doc.getPath()+" desde el servidor");
				
				//DConector.obtenerDC().devolverTokenFichero();
				
				e.sincronizarFichero = new Boolean(false);
				this.sincronizada = true;
			}
			else {
				e.sincronizarFichero = new Boolean(true);
			}
			
			this.enviarEvento(e);
			
			//tk.nuevoUsuario();
			
			//DConector.obtenerDC().devolverTokenFichero( tk );
				
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

		public void cargado(DJLienzoEvent evento) {
			enviarEvento(evento);
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
		
		//JOptionPane.showMessageDialog(this.padre, "Reciido evento id " + evento.tipo);
		
		
		if (evento.tipo.intValue() == DJLienzoEvent.SINCRONIZACION.intValue() &&
										!evento.usuario.equals(DConector.Dusuario)) 
		{
			
			DJLienzoEvent evt = (DJLienzoEvent) evento;
			
			if ( evt.sincronizarFichero.booleanValue() &&
					evt.path != null && evt.path.equals(doc.getPath())){
				
				TransferP2P.pararHebra();
			
				TransferP2P.establecerServidor(id, doc); 
				try
				{
					evt.direccionRMI = new String(InetAddress.getLocalHost().getHostName());
				}
				catch (UnknownHostException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				evt.idDestino = new Integer(id);
				evt.puerto = new Integer(TransferP2P.port);
				evt.tipo = DJLienzoEvent.RESPUESTA_SINCRONIZACION;
				evt.path = new String(doc.getPath());
				
				enviarEvento(evt);
				System.out.println("\n================================================================================");
				System.out.println("Enviada Respuesta sincronizacion desde la direccción: "
						+ evt.direccionRMI + "\nen el puerto " + evt.puerto + " para el fichero "+ evt.path);
				System.out.println("================================================================================\n");
				id++;
			}
		}
		
		else if (evento.tipo.intValue() == DJLienzoEvent.NUEVA_PAGINA.intValue() &&
				!evento.usuario.equals(DConector.Dusuario))
		{
			DJLienzoEvent evt = (DJLienzoEvent)evento;
	
			if (evt.path != null && evt.path.equals(doc.getPath())) {
			
				if ( evt.numPagina > paginaActual ){
					doc.addPagina(evt.imagen.getImage());
					
					if (paginaActual == 0)
						paginaActual++;
				}
				else {
					doc.insertarPagina(evt.numPagina-1, evt.imagen.getImage());
				}
				
				repaint();
				
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
		}
		
		else if (evento.tipo.intValue() == DJLienzoEvent.RESPUESTA_SINCRONIZACION.intValue() &&
				!evento.usuario.equals(DConector.Dusuario) && !sincronizada)
		{
			DJLienzoEvent evt = (DJLienzoEvent)evento;
			
			System.out.println("Recibida respuesta sincronizacion");
			
			if (evt.path != null && evt.path.equals(doc.getPath())){
				System.out.println("Recibida Respuesta sincronizacion desde la direccción: "+ evt.direccionRMI 
						+ "\nen el puerto " + evt.puerto + " para el fichero "+ evt.path);
				TransferP2P tf = new TransferP2P(evt.direccionRMI, evt.idDestino.intValue(), evt.puerto.intValue());
				Documento puente = tf.receive();
				
				if(puente != null) {
					this.setDocumento(puente);
					sincronizada = true;
				}
				
				//DConector.obtenerDC().devolverTokenFichero();
			}
		}	
		
		else if (evento.tipo.intValue() == DJLienzoEvent.NUEVA_ANOTACION.intValue())
		 {
			DJLienzoEvent evt = (DJLienzoEvent ) evento;
			
			//JOptionPane.showMessageDialog(null, "Recibida nueva anotacion para el fichero " + evt.path);
			
			if(evt.path != null &&evt.path.equals(doc.getPath()) &&
					!evento.usuario.equals(DConector.Dusuario)) {
				Anotacion a = new Anotacion();
				a.setContenido(evt.dibujo);
				a.setUsuario(evt.usuario);
				a.setRol(evt.rol);
				
				doc.getPagina(evt.numPagina.intValue()).addAnotacion(a);
				this.repaint();
			}
		}
		else if (evento.tipo.intValue() == DJLienzoEvent.LIMPIEZA_LIENZO.intValue()
				&& !evento.usuario.equals(DConector.Dusuario)) {
			
			DJLienzoEvent evt = (DJLienzoEvent ) evento;
			if(evt.path != null && evt.path.equals(doc.getPath()))
				this.limpiarLienzo();
		}
		else if (evento.tipo.intValue() == DJLienzoEvent.DESHACER.intValue()
				&& !evento.usuario.equals(DConector.Dusuario)) {
				DJLienzoEvent evt = (DJLienzoEvent ) evento;
				
				if(evt.path != null &&evt.path.equals(doc.getPath()))
					deshacer(evt.numPagina.intValue());
		}
		else if (evento.tipo.intValue() == DJLienzoEvent.BORRADO.intValue()
				&& !evento.usuario.equals(DConector.Dusuario)) {
			
			DJLienzoEvent evt = (DJLienzoEvent ) evento;
			
			if(evt.path != null && evt.path.equals(doc.getPath())) {
				int posicion = evt.aBorrar.intValue();
				this.borrarObjeto(posicion, evt.numPagina.intValue());
			}
		}
		else if (evento.tipo.intValue() == DJLienzoEvent.REHACER.intValue()
				&& !evento.usuario.equals(DConector.Dusuario)) {

			DJLienzoEvent evt = (DJLienzoEvent ) evento;
			
			if(evt.path != null && evt.path.equals(doc.getPath()))
				this.rehacer();
		}
		else if (evento.tipo.intValue() == DJLienzoEvent.BORRAR_IMAGEN.intValue()
				&& !evento.usuario.equals(DConector.Dusuario)) {
			DJLienzoEvent evt = (DJLienzoEvent ) evento;
			
			if(evt.path != null && evt.path.equals(doc.getPath()))
			this.borrarPagina(evt.numPagina.intValue()-1);
		}
		else if (evento.tipo.intValue() ==DDocumentEvent.RESPUESTA_FICHERO.intValue()
				// nos aseguramos que el evento esté dirigido a nosotros
				&& evento.usuario.equals(DConector.Dusuario)) {
			
			
			DDocumentEvent evt = (DDocumentEvent) evento;
			
			if(evt.path != null && evt.path.equals(doc.getPath())) {
				Transfer ts = new Transfer(evt.direccionRespuesta, evt.path);
				
				doc = ts.receive();
				
				repaint();
			}
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
}
