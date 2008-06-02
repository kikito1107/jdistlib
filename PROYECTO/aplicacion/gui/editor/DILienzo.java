package aplicacion.gui.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolTip;
import javax.swing.border.EtchedBorder;

import util.DialogoIntroTexto;
import Deventos.DEvent;
import Deventos.DJLienzoEvent;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.Anotacion;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.Pagina;
import aplicacion.fisica.net.Transfer;
import aplicacion.fisica.net.TransferP2P;
import componentes.base.DComponenteBase;

import figuras.Elipse;
import figuras.Figura;
import figuras.Linea;
import figuras.Rectangulo;
import figuras.Texto;
import figuras.TrazoManoAlzada;

/**
 * Implementacion de un editor basico de imagenes distribuido
 * 
 * @author Ana Belen Pelegrina Ortiz
 * @date 9-2-2008
 */
public class DILienzo extends DIViewer implements MouseListener,
		MouseMotionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6690106456671059169L;

	/**
	 * Ventana en la que se dibuja el lienzo
	 */
	private FramePanelDibujo padre = null;

	JToolTip tt = new JToolTip();

	/**
	 * Modos de dibujo para las anotaciones
	 */
	public static final int LINEAS = 0;

	public static final int MANO_ALZADA = 1;

	public static final int TEXTO = 2;

	public static final int RECTANGULO = 3;

	public static final int ELIPSE = 4;

	/** Indica si se ha sincronizado ya el lienzo */
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
	int modoDibujo = LINEAS;

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
	
	/*
	 * Permite seleccionar si se esta seleccionando formas o pintandolas
	 */
	private boolean estaSeleccionando = false;

	/**
	 * Coordenada x inicial del trazo actual
	 */
	private int x1;

	private int y1;

	private int x2;

	private int y2;

	/**
	 * identificador del lienzo
	 */
	private int id = ( new Random() ).nextInt(1000);

	/**
	 * Constructor de la clase
	 * 
	 * @param nombre
	 *            nombre del componente
	 * @param conexionDC
	 * @param padre
	 */
	public DILienzo( String nombre, boolean conexionDC, DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);

		setBackground(Color.white);
		addMouseMotionListener(this);
		addMouseListener(this);
		doc.setUsuario(DConector.Dusuario);
		doc.setRol(DConector.Drol);
		doc.setPath("");

		try
		{
			init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public DILienzo()
	{
		super("", false, null);
		try
		{
			init();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Establece el color actual para dibujar
	 * 
	 * @param unColor
	 *            el nuevo color actual
	 */
	public void setColor(Color unColor)
	{
		colorActual = unColor;
	}
	
	public void setEstaSeleccionando(boolean b)
	{
		estaSeleccionando = b;
	}

	/**
	 * Devuelve el documento con el que se está trabajando actualmente
	 * 
	 * @return
	 */
	public Documento getDocumento()
	{
		return doc;
	}

	/**
	 * Establece el documento de trabajo
	 * 
	 * @param nuevo
	 *            documento de trabajo
	 */
	public void setDocumento(Documento nuevo)
	{
		doc = nuevo;
		paginaActual = 1;
		anotacionSeleccionada = -1;

		repaint();
	}

	/**
	 * Establece el trazo del dibujo
	 * 
	 * @param trazo
	 *            tipo de trazo
	 */
	public void setTrazo(int trazo)
	{
		if (trazo == DILienzo.LINEAS)
			modoDibujo = DILienzo.LINEAS;
		else if (trazo == DILienzo.MANO_ALZADA)
			modoDibujo = DILienzo.MANO_ALZADA;
		else if (trazo == DILienzo.TEXTO)
			modoDibujo = DILienzo.TEXTO;
		else if (trazo == DILienzo.RECTANGULO)
			modoDibujo = RECTANGULO;
		else if (trazo == DILienzo.ELIPSE) modoDibujo = ELIPSE;
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
	}

	/**
	 * Devuelve la ventana en la que se encuentra el lienzo
	 */
	public FramePanelDibujo getPadre()
	{
		return this.padre;
	}

	/**
	 * Establece la ventana en la que es dibujado el lienzo
	 * 
	 * @param j
	 *            ventana en la que se dibuja el lienzo
	 */
	public void setPadre(FramePanelDibujo j)
	{
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
	 * Selecciona la siguiente anotacion de la pagina actual del documento
	 */
	public void seleccionarSiguienteAnotacion(){
		if (anotacionSeleccionada == -1)
			anotacionSeleccionada = 0;
		else {
			anotacionSeleccionada++;
			
			if (anotacionSeleccionada == doc.getPagina(paginaActual-1).getAnotaciones().size()){
				anotacionSeleccionada = -1;
			}
		}
		
		this.repaint();
	}
	
	/**
	 * Selecciona la anterior anotacion de la pagina actual del documento
	 */
	public void seleccionarAnteriorAnotacion(){
		if (anotacionSeleccionada == -1)
			anotacionSeleccionada = doc.getPagina(paginaActual-1).getAnotaciones().size()-1;
		else {
			anotacionSeleccionada--;
		}
		
		this.repaint();
	}
	
	
	/**
	 * Método que permite invertir un color
	 * 
	 * @param c
	 *            color a invertir
	 * @return color invertido
	 */
	public static Color invertirColor(Color c)
	{
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

		if (numBorrados > 0)
		{

			Anotacion objeto = anotacionesBorradas.remove(numBorrados - 1);

			doc.getPagina(paginaActual - 1).getAnotaciones().add(objeto);

			anotacionSeleccionada = doc.getPagina(paginaActual - 1)
					.getAnotaciones().size();

			repaint();
		}

	}

	/**
	 * Devuelve el número de la página actual
	 * 
	 * @return
	 */
	public int getNumPaginas()
	{
		return doc.getNumeroPaginas();
	}

	/**
	 * Recupera la página actual del documento visualizado
	 * 
	 * @return el número de la página actual
	 */
	public int getPaginaActual()
	{
		return this.paginaActual;
	}

	/**
	 * Establece la página actual con la que se está trabajando
	 * 
	 * @param numPagina
	 *            número de la nueva página actual
	 */
	public void setPaginaActual(int numPagina)
	{
		if (( numPagina > 0 ) && ( numPagina < getNumPaginas() + 1 ))
		{
			paginaActual = numPagina;

			anotacionesBorradas = new Vector<Anotacion>();
			
			anotacionSeleccionada = -1;

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

		//
		// ToolTipManager.

		int x = e.getX();
		int y = e.getY();

		// recorremos todas las anotaciones de la página actual

		boolean encontrado = false;

		Pagina p = doc.getPagina(this.paginaActual - 1);

		Vector<Anotacion> v = null;

		if (p != null) v = p.getAnotaciones();

		// System.out.println("x: "+ x + " y: " + y);

		if (v != null)
		{

			for (int i = 0; ( i < v.size() ) && !encontrado; ++i)
				if (v.get(i).getContenido().pertenece(x, y))
				{

					String texto = "Usuario: " + v.get(i).getUsuario();

					texto += "    Rol: " + v.get(i).getRol();

					texto += "    Fecha: " + v.get(i).getFecha();

					this.setToolTipText(texto);
					encontrado = true;
				}

			if (!encontrado) this.setToolTipText("");

		}
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

			Pagina p = doc.getPagina(this.paginaActual - 1);

			Vector<Anotacion> v = null;

			if (p != null) v = p.getAnotaciones();

			// System.out.println("x: "+ x + " y: " + y);

			if (v != null)
			{

				for (int i = 0; ( i < v.size() ) && !encontrado; ++i)
					if (v.get(i).getContenido().pertenece(x, y))
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

						DJLienzoEvent evt2 = new DJLienzoEvent();

						evt2.color = colorActual;
						evt2.dibujo = t;
						evt2.numPagina = new Integer(paginaActual - 1);
						evt2.tipo = new Integer(DJLienzoEvent.NUEVA_ANOTACION
								.intValue());
						evt2.path = new String(doc.getPath());
						evt2.rol = DConector.Drol;

						enviarEvento(evt2);

						if (doc.getNumeroPaginas() > 0)
						{

							Anotacion a = new Anotacion();
							a.setContenido(t);
							a.setRol(DConector.Drol);
							a.setUsuario(DConector.Dusuario);

							doc.getPagina(this.paginaActual - 1)
									.addAnotacion(a);
						}
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

					DJLienzoEvent evt = new DJLienzoEvent();

					evt.color = colorActual;
					evt.dibujo = f;
					evt.numPagina = new Integer(paginaActual - 1);
					evt.tipo = new Integer(DJLienzoEvent.NUEVA_ANOTACION
							.intValue());
					evt.path = new String(doc.getPath());
					evt.rol = new String(DConector.Drol);
					enviarEvento(evt);

					if (doc.getNumeroPaginas() > 0)
					{

						Anotacion a = new Anotacion();
						a.setContenido(f);
						a.setRol(DConector.Drol);
						a.setUsuario(DConector.Dusuario);

						doc.getPagina(this.paginaActual - 1).addAnotacion(a);
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
					evt2.numPagina = new Integer(paginaActual - 1);
					evt2.tipo = new Integer(DJLienzoEvent.NUEVA_ANOTACION
							.intValue());
					evt2.path = new String(doc.getPath());
					evt2.rol = new String(DConector.Drol);
					enviarEvento(evt2);

					if (doc.getNumeroPaginas() > 0)
					{

						Anotacion a = new Anotacion();
						a.setContenido(l);
						a.setRol(DConector.Drol);
						a.setUsuario(DConector.Dusuario);

						doc.getPagina(this.paginaActual - 1).addAnotacion(a);
					}

					x2 = -1;

					repaint();
					break;
				case MANO_ALZADA:
					DJLienzoEvent evt3 = new DJLienzoEvent();

					evt3.color = colorActual;
					evt3.dibujo = trazo;
					evt3.numPagina = new Integer(paginaActual - 1);
					evt3.tipo = new Integer(DJLienzoEvent.NUEVA_ANOTACION
							.intValue());
					evt3.path = new String(doc.getPath());
					evt3.rol = new String(DConector.Drol);

					enviarEvento(evt3);

					if (doc.getNumeroPaginas() > 0)
					{

						Anotacion a = new Anotacion();
						a.setContenido(trazo);
						a.setRol(DConector.Drol);
						a.setUsuario(DConector.Dusuario);

						doc.getPagina(this.paginaActual - 1).addAnotacion(a);

						trazo = null;
					}

					break;
				default:
					break;
			}
		}
	}

	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
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
		// cargamos la imagen de la pagina actual
		if (( doc != null ) && ( doc.getNumeroPaginas() > 0 ))
		{
			try
			{
				Image imagen = doc.getPagina(paginaActual - 1).getImagen();
				int img_w = imagen.getWidth(null);
				int img_h = imagen.getHeight(null);

				int lienzo_w = this.getWidth();
				int lienzo_h = this.getHeight();

				int img_x = 0, img_y = 0;

				if (( img_w < lienzo_w ) && ( img_h < lienzo_h ))
				{

					img_x = getSize().width / 2 - img_w / 2;
					img_y = getSize().height / 2 - img_h / 2;

					lienzo_w = img_w;
					lienzo_h = img_h;

				}
				else
				{
					if (img_w > lienzo_w) this.setSize(img_w, lienzo_h);
					if (img_h > lienzo_h) this.setSize(lienzo_w, img_h);
				}

				g.clearRect(0, 0, this.getWidth(), this.getHeight());
				g.drawImage(imagen, img_x, img_y, img_w, img_h, this);
			}
			catch (NullPointerException ex)
			{
			}
		}
		else
		{
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}

		if (( doc != null ) && ( doc.getNumeroPaginas() > 0 ))
		{
			Vector<Anotacion> v = doc.getPagina(paginaActual - 1)
					.getAnotaciones();

			int np = v.size();

			/* Dibujamos las figuras de la pagina actual */
			for (int i = 0; i < np; i++)
			{

				Figura f = v.get(i).getContenido();

				if (i != anotacionSeleccionada)
					g.setColor(f.getColor());
				else g.setColor(DILienzo.invertirColor(f.getColor()));

				f.dibujar(g);
			}


			// dibujamos el dibujo que se esta realizando en este momento
			switch (modoDibujo) {
				case LINEAS:
					g.setColor(colorActual);
					if (x2 != -1) g.drawLine(x1, y1, x2, y2);
					break;
				case RECTANGULO:
				case ELIPSE:
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
					break;
				case MANO_ALZADA:
					g.setColor(this.colorActual);
					if (trazo!=null)
						trazo.dibujar(g);
					break;
				default:
						break;
					
			}
			
		}
	}

	/**
	 * Deshace el ultimo trazo realizado en el lienzo
	 * 
	 * @param i
	 *            página
	 */
	public void deshacer(int i)
	{
		// si se ha dibujado algo...
		if (doc.getPagina(i).getAnotaciones().size() > 0)
		{

			Vector<Anotacion> anotaciones = doc.getPagina(i).getAnotaciones();

			// eliminamos el último elemento dibujado y su color
			Anotacion a = anotaciones.remove(anotaciones.size() - 1);

			// la agregamos a la lista de anotaciones borradas
			this.anotacionesBorradas.add(a);

			// repintamos el lienzo
			repaint();
		}
	}

	/**
	 * Elimina todas las trazos realizados en el lienzo hasta el momento
	 */
	public void limpiarLienzo()
	{

		int np = doc.getNumeroPaginas();

		for (int i = 0; i < np; ++i)
			doc.getPagina(i).getAnotaciones().removeAllElements();
		repaint();
	}

	/**
	 * Borra un objeto de la lista de objetos dibujados
	 * 
	 * @param posicion
	 *            posicion del objeto a borrar
	 * @param i
	 */
	public void borrarObjeto(int posicion, int i)
	{

		if (( posicion > -1 )
				&& ( posicion < doc.getPagina(i).getAnotaciones().size() ))
		{
			Anotacion a = doc.getPagina(i).getAnotaciones().remove(posicion);

			anotacionesBorradas.add(a);

			this.seleccionarAnteriorAnotacion();

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
		if (conectadoDC() && ( doc.getPath() != null )
				&& !doc.getPath().equals(""))
		{
			DJLienzoEvent e = new DJLienzoEvent();
			e.tipo = new Integer(DJLienzoEvent.SINCRONIZACION.intValue());
			e.path = new String(doc.getPath());
			
			if (!DConector.obtenerDC().leerToken(doc.getPath()))
			{
				
				//JOptionPane.showMessageDialog(null, "PATH " + doc.getPath());

				if (!DConector.obtenerDC().escribirToken())
					JOptionPane.showMessageDialog(padre,
							"Error al guardar el token");

				Transfer t = new Transfer(ClienteFicheros.ipConexion, doc
						.getPath());

				// intentamos abrir el documento con su formato nativo
				Documento p = t.receiveDocumento(false);

				if (p != null)
				{
					p.setDatosBD(doc.getDatosBD());
					doc = p;
					
					
					
					repaint();
				}
				else
				{
					int eleccion = JOptionPane
							.showConfirmDialog(
									this,
									"El formato del fichero no está soportado. ¿Desea abrirlo en modo texto?",
									"Formato no soportado",
									JOptionPane.YES_NO_OPTION);

					if (eleccion == JOptionPane.YES_OPTION)
					{

						// intentamos abrirlo forzando a documento de texto
						p = t.receiveDocumento(true);

						
						if (p == null)
						{

							JOptionPane.showMessageDialog(this.padre,
									"Error al cargar el fichero "
											+ doc.getPath()
											+ " desde el servidor");

							doc.setPath("");
						}
						else
						{
							p.setDatosBD(doc.getDatosBD());
							doc = p;

							repaint();

						}
					}
					else
						doc.setPath("");
				}

				e.sincronizarFichero = new Boolean(false);
				this.sincronizada = true;

			}
			else
			{
				e.sincronizarFichero = new Boolean(true);
				this.sincronizada = false;
				if (!DConector.obtenerDC().escribirToken())
					JOptionPane.showMessageDialog(padre,
							"Error al guardar el token");
			}

			this.enviarEvento(e);

		}
	}

	// ***********************************************************************
	// ******************* PROCESAMIENTO DE EVENTOS **************************
	// ***********************************************************************

	/**
	 * Procesa un evento recibido
	 * 
	 * @arg evento evento recibido
	 */
	@Override
	@SuppressWarnings( "static-access" )
	public void procesarEvento(DEvent evento)
	{

		// JOptionPane.showMessageDialog(this.padre, "Reciido evento id " +
		// evento.tipo);

		if (( evento.tipo.intValue() == DJLienzoEvent.SINCRONIZACION.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
		{

			DJLienzoEvent evt = (DJLienzoEvent) evento;

			if (evt.sincronizarFichero.booleanValue() && ( evt.path != null )
					&& evt.path.equals(doc.getPath()))
			{

				TransferP2P.pararHebra();

				TransferP2P.establecerServidor(id, doc);

				try
				{
					evt.direccionRMI = new String(InetAddress.getLocalHost()
							.getHostName());
				}
				catch (UnknownHostException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				evt.idDestino = new Integer(id);
				evt.puerto = new Integer(TransferP2P.getPort());
				evt.tipo = DJLienzoEvent.RESPUESTA_SINCRONIZACION;
				evt.path = new String(doc.getPath());

				enviarEvento(evt);
				System.out
						.println("\n================================================================================");
				System.out
						.println("Enviada Respuesta sincronizacion desde la direccción: "
								+ evt.direccionRMI
								+ "\nen el puerto "
								+ evt.puerto + " para el fichero " + evt.path);
				System.out
						.println("================================================================================\n");
				id++;
			}
		}

		else if (( evento.tipo.intValue() == DJLienzoEvent.NUEVA_PAGINA
				.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
		{
			DJLienzoEvent evt = (DJLienzoEvent) evento;

			if (( evt.path != null ) && evt.path.equals(doc.getPath()))
			{

				if (evt.numPagina > paginaActual)
				{
					doc.addPagina(evt.imagen.getImage());

					if (paginaActual == 0) paginaActual++;
				}
				else doc.insertarPagina(evt.numPagina - 1, evt.imagen
						.getImage());

				repaint();
			}
		}

		else if (( evento.tipo.intValue() == DJLienzoEvent.RESPUESTA_SINCRONIZACION
				.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario) && !sincronizada)
		{
			DJLienzoEvent evt = (DJLienzoEvent) evento;

			System.out.println("Recibida respuesta sincronizacion");

			if (( evt.path != null ) && evt.path.equals(doc.getPath()))
			{
				System.out
						.println("Recibida Respuesta sincronizacion desde la direccción: "
								+ evt.direccionRMI
								+ "\nen el puerto "
								+ evt.puerto + " para el fichero " + evt.path);
				TransferP2P tf = new TransferP2P(evt.direccionRMI,
						evt.idDestino.intValue(), evt.puerto.intValue());
				Documento puente = tf.receive();

				if (puente != null)
				{
					puente.setDatosBD(doc.getDatosBD());
					this.setDocumento(puente);
					repaint();
					sincronizada = true;
				}

			}
		}

		else if (evento.tipo.intValue() == DJLienzoEvent.NUEVA_ANOTACION
				.intValue())
		{
			DJLienzoEvent evt = (DJLienzoEvent) evento;

			if (( evt.path != null ) && evt.path.equals(doc.getPath())
					&& !evento.usuario.equals(DConector.Dusuario))
			{
				Anotacion a = new Anotacion();
				a.setContenido(evt.dibujo);
				a.setUsuario(evt.usuario);
				a.setRol(evt.rol);

				try
				{
					doc.getPagina(evt.numPagina.intValue()).addAnotacion(a);
				}
				catch (NullPointerException ex)
				{
				}

				this.repaint();
			}
		}
		else if (( evento.tipo.intValue() == DJLienzoEvent.LIMPIEZA_LIENZO
				.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
		{

			DJLienzoEvent evt = (DJLienzoEvent) evento;
			if (( evt.path != null ) && evt.path.equals(doc.getPath()))
				this.limpiarLienzo();
		}
		else if (( evento.tipo.intValue() == DJLienzoEvent.DESHACER.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
		{
			DJLienzoEvent evt = (DJLienzoEvent) evento;

			if (( evt.path != null ) && evt.path.equals(doc.getPath()))
				deshacer(evt.numPagina.intValue());
		}
		else if (( evento.tipo.intValue() == DJLienzoEvent.BORRADO.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
		{

			DJLienzoEvent evt = (DJLienzoEvent) evento;

			if (( evt.path != null ) && evt.path.equals(doc.getPath()))
			{
				int posicion = evt.aBorrar.intValue();
				this.borrarObjeto(posicion, evt.numPagina.intValue());
			}
		}
		else if (( evento.tipo.intValue() == DJLienzoEvent.REHACER.intValue() )
				&& !evento.usuario.equals(DConector.Dusuario))
		{

			DJLienzoEvent evt = (DJLienzoEvent) evento;

			if (( evt.path != null ) && evt.path.equals(doc.getPath()))
				this.rehacer();
		}
	}
}
