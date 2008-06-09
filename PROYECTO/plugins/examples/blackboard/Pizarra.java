package plugins.examples.blackboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.border.EtchedBorder;



import Deventos.enlaceJS.DConector;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.HebraProcesadoraBase;
import componentes.gui.visualizador.DILienzo;
import componentes.gui.visualizador.DIViewer;
import componentes.util.DialogoIntroTexto;
import Deventos.DEvent;
import figuras.Elipse;
import figuras.Figura;
import figuras.Linea;
import figuras.Rectangulo;
import figuras.Texto;
import figuras.TrazoManoAlzada;

/**
 * Implementacion de una pizarra distribuida
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class Pizarra extends DIViewer implements MouseListener,
		MouseMotionListener
{
	private static final long serialVersionUID = -2746544910515149389L;

	/**
	 * Ventana en la que se dibuja el lienzo
	 */
	private JFrame padre = null;

	/*
	 * Modos de dibujo para las anotaciones
	 */
	/**
	 * Modo de dibujo con lineas
	 */
	public static final int LINEAS = 0;

	/**
	 * Modo de dibujo a mano alzada
	 */
	public static final int MANO_ALZADA = 1;

	/**
	 * Modo para escribir texto
	 */
	public static final int TEXTO = 2;

	/**
	 * Modo para dibujar rectangulos
	 */
	public static final int RECTANGULO = 3;

	/**
	 * Modo para dibujar elipses
	 */
	public static final int ELIPSE = 4;

	/** Indica si se ha sincronizado ya el lienzo 
	 * 
	 */
	public boolean sincronizada = false;

	/**
	 * Color con el que se dibujan las anotaciones
	 */
	private Color colorActual = Color.RED;

	/**
	 * Trazo que permite almancenar un trazo a mano alzada mientras está siendo
	 * realizado
	 */
	private TrazoManoAlzada trazo = null;

	/**
	 * Modo de dibujo, puede ser lineas o puntos. Inicialmente es lineas
	 */
	private int modoDibujo = LINEAS;

	/**
	 * Anotación actualmente seleccionada
	 */
	private int anotacionSeleccionada = -1;

	/**
	 * Vector con las anotaciones realizadas
	 */
	private Vector<Figura> anotaciones = new Vector<Figura>();

	/**
	 * Vector que contiene los objetos que han sido borrados
	 */
	private Vector<Figura> anotacionesBorradas = new Vector<Figura>();

	/*
	 * Coordenadas del trazo actual
	 */
	private int x1;
	private int y1;
	private int x2;
	private int y2;

	/**
	 * Hebra encargada del procesamiento de los eventos
	 */
	private HebraProcesadoraBase hebraProcesadora = null;
	
	/*
	 * Permite seleccionar si se esta seleccionando formas o pintandolas
	 */
	private boolean estaSeleccionando = false;

	/**
	 * Constructor de la clase
	 * 
	 * @param nombre
	 *            Nombre del componente
	 * @param conexionDC Indica si queremos tener una conexion directa
	 * 					 con el DConector
	 * @param padre Componente padre de este componente
	 */
	public Pizarra( String nombre, boolean conexionDC, DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		
		anotacionSeleccionada = -1;

		try
		{
			init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Establece el color actual para dibujar
	 * 
	 * @param unColor
	 *            Nuevo color
	 */
	public void setColor(Color unColor)
	{
		colorActual = unColor;
	}

	/**
	 * Asigna si se esta seleccionando o no
	 * @param b Indica si deseamos seleccionar o pintar
	 */
	public void setEstaSeleccionando(boolean b)
	{
		estaSeleccionando = b;
	}

	/**
	 * Establece el trazo del dibujo
	 * 
	 * @param trazo
	 *            tipo de trazo
	 */
	public void setTrazo(int trazo)
	{
		if (trazo == Pizarra.LINEAS)
			modoDibujo = Pizarra.LINEAS;
		else if (trazo == Pizarra.MANO_ALZADA)
			modoDibujo = Pizarra.MANO_ALZADA;
		else if (trazo == Pizarra.TEXTO)
			modoDibujo = Pizarra.TEXTO;
		else if (trazo == Pizarra.RECTANGULO)
			modoDibujo = RECTANGULO;
		else if (trazo == Pizarra.ELIPSE) modoDibujo = ELIPSE;
	}

	/**
	 * Inicializa el lienzo
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception
	{
		this.createToolTip();

		this.setToolTipText("");

		this.setBorder(new EtchedBorder(2));
		
		setBackground(Color.white);
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	/**
	 * Obtiene el objeto seleccionado
	 * @return Objeto seleccionado
	 */
	public int getObjetoSeleccionado()
	{
		return anotacionSeleccionada;
	}

	/**
	 * Selecciona el siguiente objeto
	 */
	public void siguienteObjeto(){
		if (anotacionSeleccionada == -1)
			anotacionSeleccionada = 0;
		else {
			anotacionSeleccionada++;
			
			if (anotacionSeleccionada == anotaciones.size()){
				anotacionSeleccionada = -1;
			}
		}
		
		this.repaint();
	}
	
	/**
	 * Selecciona el objeto anterior
	 */
	public void anteriorObjeto(){
		if (anotacionSeleccionada == -1)
			anotacionSeleccionada = anotaciones.size()-1;
		else {
			anotacionSeleccionada--;
		}
		
		this.repaint();
	}
	
	
	/**
	 * Rehace el último trazo
	 */
	public void rehacer()
	{
		int numBorrados = anotacionesBorradas.size();

		if (numBorrados > 0)
		{

			Figura objeto = anotacionesBorradas.remove(numBorrados - 1);
			anotaciones.add(objeto);
			repaint();
		}

	}

	/**
	 * Devuelve el color actual
	 * 
	 * @return el color actual
	 */
	public Color getColor()
	{

		return colorActual;
	}

	// ***********************************************************************
	// *********************** EVENTOS DE RATON ******************************
	// ***********************************************************************

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mouseMoved(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		e.consume();
		if (estaSeleccionando)
		{
			int x = e.getX();
			int y = e.getY();

			// recorremos todas las anotaciones de la página actual

			boolean encontrado = false;

			// System.out.println("x: "+ x + " y: " + y);

			if (anotaciones != null)
			{

				for (int i = 0; ( i < anotaciones.size() ) && !encontrado; ++i)
					if (anotaciones.get(i).pertenece(x, y))
					{
						anotacionSeleccionada = i;
						encontrado = true;
					}
			}

			this.repaint();
		}

		else
		{
			switch (modoDibujo)
			{
				case LINEAS:
				case RECTANGULO:
				case ELIPSE:
					x1 = e.getX();
					y1 = e.getY();
					x2 = -1;
					break;
				case MANO_ALZADA:

					trazo = new TrazoManoAlzada();
					trazo.setColor(colorActual);

					Linea l = new Linea(e.getX(), e.getY(), e.getX() + 1, e
							.getY() + 1);
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

					if (rsp != null)
					{
						Texto t = new Texto(x1, y1, rsp);

						// System.out.println("x: " + x1 + " y: " + y1);
						t.setColor(colorActual);

						DJPizarraEvent evt2 = new DJPizarraEvent();

						evt2.dibujo = t;
						evt2.tipo = new Integer(DJPizarraEvent.NUEVA_ANOTACION
								.intValue());
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
	}

	public void mouseReleased(MouseEvent e)
	{
		e.consume();

		if (!estaSeleccionando)
		{
			switch (modoDibujo)
			{
				case RECTANGULO:
				case ELIPSE:

					if (x1 >= x2)
					{
						int xaux = x1;
						x1 = x2;
						x2 = xaux;
					}
					if (y1 >= y2)
					{
						int yaux = y1;
						y1 = y2;
						y2 = yaux;
					}

					Figura f;
					if (modoDibujo == RECTANGULO)
						f = new Rectangulo(x1, y1, x2, y2);
					else f = new Elipse(x1, y1, x2, y2);

					f.setColor(colorActual);

					DJPizarraEvent evt = new DJPizarraEvent();

					evt.dibujo = f;
					evt.tipo = new Integer(DJPizarraEvent.NUEVA_ANOTACION
							.intValue());
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
					evt2.tipo = new Integer(DJPizarraEvent.NUEVA_ANOTACION
							.intValue());
					evt2.rol = new String(DConector.Drol);
					enviarEvento(evt2);

					anotaciones.add(l);

					x2 = -1;

					repaint();
					break;
				case MANO_ALZADA:
					DJPizarraEvent evt3 = new DJPizarraEvent();

					evt3.dibujo = trazo;
					evt3.tipo = new Integer(DJPizarraEvent.NUEVA_ANOTACION
							.intValue());
					evt3.rol = new String(DConector.Drol);

					enviarEvento(evt3);

					anotaciones.add(trazo);

					trazo = null;
					break;
				default:
					break;
			}
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		e.consume();

		if (!estaSeleccionando)
		{
			switch (modoDibujo)
			{
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
					break;

				default:
					break;
			}
		}
	}

	// ***********************************************************************
	// ******************** REPINTADO DEL LIENZO *****************************
	// ***********************************************************************
	@Override
	public void paint(Graphics g)
	{

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		int np = anotaciones.size();

		/* Dibujamos las figuras actuales */
		for (int i = 0; i < np; i++)
		{

			Figura f = anotaciones.get(i);

			if (i != anotacionSeleccionada) {
				g.setColor(f.getColor());
			}
			else {
				
				g.setColor(DILienzo.invertirColor(f.getColor()));
			}

			f.dibujar(g);
		}

		// si estamos dibujando una linea...
		if (modoDibujo == LINEAS)
		{
			g.setColor(colorActual);
			if (x2 != -1) g.drawLine(x1, y1, x2, y2);
		}
		else if (( modoDibujo == RECTANGULO ) || ( modoDibujo == ELIPSE ))
		{
			g.setColor(colorActual);
			if (x2 != -1)
			{
				int X1 = x1, X2 = x2, Y1 = y1, Y2 = y2;

				if (x1 >= x2)
				{
					X1 = x2;
					X2 = x1;
				}
				if (y1 >= y2)
				{
					Y1 = y2;
					Y2 = y1;
				}
				if (modoDibujo == RECTANGULO)
					g.drawRect(X1, Y1, X2 - X1, Y2 - Y1);
				else g.drawOval(X1, Y1, X2 - X1, Y2 - Y1);
			}
		}
		else if (modoDibujo == Pizarra.MANO_ALZADA) if (trazo != null)
		{
			g.setColor(this.colorActual);
			trazo.dibujar(g);
		}
	}

	/**
	 * Deshace el ultimo trazo realizado en el lienzo
	 */
	public void deshacer()
	{
		// si se ha dibujado algo...

		if (anotaciones.size() > 0)
		{
			this.anotacionesBorradas.add(anotaciones
					.remove(anotaciones.size() - 1));
			// repintamos el lienzo
			repaint();
		}
	}

	/**
	 * Elimina todos los trazos realizados en el lienzo hasta el momento
	 */
	public void limpiarLienzo()
	{
		anotacionesBorradas.addAll(anotaciones);
		anotaciones.clear();

		repaint();
	}

	/**
	 * Borra un objeto de la lista de objetos dibujados
	 * 
	 * @param posicion
	 *            posicion del objeto a borrar
	 * @param i
	 */
	public void borrarObjeto(int posicion)
	{

		if (( posicion > -1 ) && ( posicion < anotaciones.size() ))
		{
			this.anotacionesBorradas.add(anotaciones
					.remove(posicion));
			repaint();
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

			DJPizarraEvent e = new DJPizarraEvent();
			e.tipo = new Integer(DJPizarraEvent.SINCRONIZACION.intValue());
			this.enviarEvento(e);
		}
	}

	// ***********************************************************************
	// ******************* PROCESAMIENTO DE EVENTOS **************************
	// ***********************************************************************

	/**
	 * Inicia la hebra procesadora de eventos
	 */
	@Override
	public void iniciarHebraProcesadora()
	{
		hebraProcesadora = new HebraProcesadora(this);
		hebraProcesadora.iniciarHebra();
	}

	/**
	 * Procesa un evento recibido
	 * 
	 * @param evento Evento recibido
	 */
	@Override
	@SuppressWarnings( "static-access" )
	public void procesarEvento(DEvent evento)
	{

		if (( evento.tipo.intValue() == DJPizarraEvent.SINCRONIZACION
				.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
		{

			DJPizarraEvent evt = (DJPizarraEvent) evento;

			evt.tipo = DJPizarraEvent.RESPUESTA_SINCRONIZACION;
			evt.dibujos = anotaciones;

			enviarEvento(evt);

		}
		else if (( evento.tipo.intValue() == DJPizarraEvent.RESPUESTA_SINCRONIZACION
				.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario) && !sincronizada)
		{
			DJPizarraEvent evt = (DJPizarraEvent) evento;

			anotaciones = evt.dibujos;

			repaint();
		}

		else if (( evento.tipo.intValue() == DJPizarraEvent.NUEVA_ANOTACION
				.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
		{
			DJPizarraEvent evt = (DJPizarraEvent) evento;

			// JOptionPane.showMessageDialog(null, "Recibida nueva anotacion
			// para el fichero " + evt.path);

			anotaciones.add(evt.dibujo);
			repaint();
		}
		else if (evento.tipo.intValue() == DJPizarraEvent.LIMPIEZA_LIENZO
				.intValue())
			this.limpiarLienzo();
		else if (( evento.tipo.intValue() == DJPizarraEvent.DESHACER.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
			deshacer();
		else if (evento.tipo.intValue() == DJPizarraEvent.BORRADO.intValue())
		{

			DJPizarraEvent evt = (DJPizarraEvent) evento;

			int posicion = evt.aBorrar.intValue();
			this.borrarObjeto(posicion);
		}
		else if (evento.tipo.intValue() == DJPizarraEvent.REHACER.intValue())
			this.rehacer();
	}

	/**
	 * Hebra que se encarga de procesar los eventos asociados al lienzo
	 */
	private class HebraProcesadora extends HebraProcesadoraBase
	{

		/**
		 * Contructor de la clase
		 * 
		 * @param dc
		 *            componente cuyos eventos van a ser procesados
		 */
		HebraProcesadora( DComponente dc )
		{
			super(dc);
		}

		/**
		 * Inicia la ejecucion de la hebra
		 */
		@Override
		@SuppressWarnings({ "unused", "unchecked" })
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
				if (( saux.tipo.intValue() == DJPizarraEvent.RESPUESTA_SINCRONIZACION
						.intValue() ))
					respSincr = saux;
				else vaux.add(saux);
			}

			activar();

			if (respSincr != null)
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJPizarraEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) procesarEvento(saux);
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