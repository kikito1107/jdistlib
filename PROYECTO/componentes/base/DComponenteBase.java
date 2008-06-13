package componentes.base;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.PopupMenu;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DMIEvent;
import Deventos.enlaceJS.DConector;

/**
 * Componente base para construir componentes distribuidos. Muchos de sus
 * metodos carecen de funcionalidad, por que debera de ser implementada por el
 * programador segun sus necesidades
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DComponenteBase extends JPanel implements DComponente,
		java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	private CardLayout cardLayout1 = new CardLayout();

	private JPanel pcomponentes = new JPanel();

	private JPanel pfrontal = new JPanel();

	@SuppressWarnings( "unused" )
	private boolean mostrado = true;

	private boolean conexionDC = false;

	@SuppressWarnings( "unused" )
	private Vector<Object> djlisteners = new Vector<Object>(5);

	@SuppressWarnings( "unused" )
	private Vector<Object> ljlisteners = new Vector<Object>(5);

	protected Integer DID = new Integer(-1);

	protected String nombre = null;

	protected ColaEventos colaRecepcion = new ColaEventos();

	protected ColaEventos colaEnvio = null;

	protected Integer ultimoProcesado = new Integer(-1);

	protected int nivelPermisos = 20;

	protected DComponenteBase DCompPadre = null;

	private HebraProcesadoraBase hp = null;

	private BorderLayout borderLayout1 = new BorderLayout();// *******************************

	private BorderLayout borderLayout2 = new BorderLayout();// *******************************

	private boolean oculto = true;

	/**
	 * Constructor por defecto
	 */
	public DComponenteBase()
	{
		super();
		this.nombre = "";
		this.DCompPadre = null;
		this.conexionDC = false;
		
		try
		{
			jbInit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param nombre
	 *            Nombre del componente
	 * @param conexionDC
	 *            Indica si deseamos realizar una conexion directa con el
	 * @see DConector
	 * @param padre
	 *            Componente padre
	 */
	public DComponenteBase( String nombre, boolean conexionDC,
			DComponenteBase padre )
	{ // **********
		this.conexionDC = conexionDC;
		this.nombre = new String(nombre);
		this.DCompPadre = padre;

		try
		{
			jbInit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		extrasConstructor();
	}

	private void extrasConstructor()
	{
		if (conexionDC)
		{
			DID = new Integer(DConector.alta(this));
			colaEnvio = DConector.getColaEventos();
		}
	}

	/**
	 * Inicializacion de los componentes graficos
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception
	{
		super.setLayout(cardLayout1);
		pcomponentes.setBorder(null);
		pcomponentes.setLayout(borderLayout1);
		pfrontal.setBackground(new Color(102, 102, 102));
		pfrontal.setBorder(BorderFactory.createEtchedBorder());
		pfrontal.setLayout(borderLayout2);
		super.add(pcomponentes, "pcomponentes");
		super.add(pfrontal, "pfrontal");
		ocultar();
	}

	/**
	 * Obtiene el componente padre del componente
	 * 
	 * @return DComponente Componente padre. Si no tiene padre devuelve null.
	 */
	public DComponente obtenerPadre()
	{
		return DCompPadre;
	}

	/**
	 * Metodo llamado cuando se oculta el padre del componente.
	 */
	public void padreOcultado()
	{
		DComponente hijo = null;
		int numHijos = obtenerNumComponentesHijos();
		for (int i = 0; i < numHijos; i++)
		{
			hijo = obtenerComponente(i);
			if (hijo != null) hijo.padreOcultado();
		}
	}

	/**
	 * Metodo llamado cuando el componente padre pasa de estado no visible a
	 * esta visible
	 */
	public void padreMostrado()
	{
		DComponente hijo = null;
		int numHijos = obtenerNumComponentesHijos();
		for (int i = 0; i < numHijos; i++)
		{
			hijo = obtenerComponente(i);
			if (hijo != null) hijo.padreMostrado();
		}
	}

	/**
	 * Devuelve el panel en el que se iran añadiendo los componentes que
	 * conforman el componente (distribuidos o no)
	 * 
	 * @return Panel de contenido
	 */
	public JPanel obtenerPanelContenido()
	{
		return pcomponentes;
	}

	/**
	 * Obtiene el numero de componentes hijo que tiene el componente.
	 * 
	 * @return Numero de componentes hijos
	 */
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	/**
	 * Obtiene el componente con el nombre indicado como parametro.
	 * 
	 * @param nombre
	 *            String Nombre del componente que se desea obtener
	 * @return DComponente Componente deseado.
	 */
	public DComponente obtenerComponente(String nombre)
	{
		System.out.println(nombre + ": obtenerComponente(String)");
		System.out.flush();
		return null;
	}

	/**
	 * Obtiene el componente con el numero indicado como parametro
	 * 
	 * @param num
	 *            Numero de componente
	 * @return Componente obtenido o null si el componente no es valido
	 */
	public DComponente obtenerComponente(int num)
	{
		System.out.println(nombre + ": obtenerComponente(int)");
		System.out.flush();
		return null;
	}

	/**
	 * Consultamos si esta en conexion directa con el DConector.
	 * 
	 * @return True si esta conectado al DConector. False en otro caso
	 */
	public boolean conectadoDC()
	{
		return conexionDC;
	}

	/**
	 * Muestra el componente
	 */
	public void mostrar()
	{
		cardLayout1.show(this, "pcomponentes");
		oculto = false;
		padreMostrado();
	}

	/**
	 * Oculta el componente
	 */
	public void ocultar()
	{
		cardLayout1.show(this, "pfrontal");
		oculto = true;
		padreOcultado();
	}

	public boolean oculto()
	{
		return oculto;
	}

	/**
	 * Realiza el envio de un evento
	 * 
	 * @param evento
	 *            Evento que se desea enviar
	 */
	public void enviarEvento(DEvent evento)
	{
		if (nivelPermisos >= 20)
		{
			evento.componente = new Integer(DID.intValue());
			evento.nombreComponente = new String(nombre);
			evento.origen = new Integer(1); // Aplicacion
			evento.destino = new Integer(1); // Aplicacion
			evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

			// Procedemos al envio
			if (conexionDC && ( colaEnvio != null ))
				colaEnvio.nuevoEvento(evento);
			else if (!conexionDC && ( DCompPadre != null ))
				DCompPadre.procesarEventoHijo(evento);
		}
	}

	/**
	 * Establece el layoutmanager del panel de contenido
	 * 
	 * @param lm
	 *            LayoutManager a establecer
	 */
	@Override
	public void setLayout(LayoutManager lm)
	{
		if (pcomponentes != null) pcomponentes.setLayout(lm);
	}

	// ********* METODOS ADD DEL PANEL ********************
	/**
	 * Añade un componente al panel de contenido
	 * 
	 * @param c
	 *            Componente a agregar
	 * @param i
	 *            Identificador del componente
	 * @return Componente agregado o null si ocurrio algun error
	 */
	@Override
	public Component add(Component c, int i)
	{
		return pcomponentes.add(c, i);
	}

	/**
	 * Añade un componente al panel de contenido
	 * 
	 * @param c
	 *            Componente a agregar
	 * @param o
	 *            Objeto con las restricciones del componente
	 * @param i
	 *            Identificador del componente
	 */
	@Override
	public void add(Component c, Object o, int i)
	{
		pcomponentes.add(c, o, i);
	}

	/**
	 * Añade un componente al panel de contenido
	 * 
	 * @param c
	 *            Componente a agregar
	 * @param o
	 *            Objeto con las restricciones del componente
	 */
	@Override
	public void add(Component c, Object o)
	{
		pcomponentes.add(c, o);
	}

	/**
	 * Añade un componente al panel de contenido
	 * 
	 * @param c
	 *            Componente a agregar
	 * @return Componente agregado o null si ocurrio algun error
	 */
	@Override
	public Component add(Component c)
	{
		return pcomponentes.add(c);
	}

	/**
	 * Añade un componente al panel de contenido
	 * 
	 * @param pm
	 *            Popup Menu que se desea agregar
	 */
	@Override
	public synchronized void add(PopupMenu pm)
	{
		pcomponentes.add(pm);
	}

	// ********* METODOS ASOCIADOS A LA INTERFAZ DCONECTOR ********************
	public void procesarEvento(DEvent evento)
	{
		colaRecepcion.nuevoEvento(evento);
	}

	synchronized public void procesarEventoHijo(DEvent evento)
	{

	}

	public void procesarMetaInformacion(DMIEvent evento)
	{
		if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue())
		{
			int permiso = evento.obtenerPermisoComponente(nombre);
			setNivelPermisos(permiso);
		}
		if (( evento.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO
				.intValue() )
				&& evento.usuario.equals(DConector.Dusuario))
		{
			int permiso = evento.obtenerPermisoComponente(nombre);
			setNivelPermisos(permiso);
		}
		if (( evento.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_USUARIO
				.intValue() )
				&& evento.usuario.equals(DConector.Dusuario)
				&& evento.componente.equals(nombre))
		{
			int nuevoPermiso = evento.permiso.intValue();
			setNivelPermisos(nuevoPermiso);
		}
		if (( evento.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_ROL
				.intValue() )
				&& evento.rol.equals(DConector.Drol)
				&& evento.componente.equals(nombre))
		{
			int nuevoPermiso = evento.permiso.intValue();
			setNivelPermisos(nuevoPermiso);
		}

		int numHijos = obtenerNumComponentesHijos();
		DComponente hijo = null;
		for (int i = 0; i < numHijos; i++)
		{
			hijo = obtenerComponente(i);
			if (hijo != null) hijo.procesarMetaInformacion(evento);
		}
	}

	public void activar()
	{
		System.out.println(nombre + ": activar()");
		System.out.flush();
	}

	public void desactivar()
	{
		System.out.println(nombre + ": desactivar()");
		System.out.flush();
	}

	public void iniciarHebraProcesadora()
	{
		if (( hp = crearHebraProcesadora() ) != null)
		{
			Thread t = new Thread(hp);
			t.start();
		}
		DComponente hijo;
		
		System.out.flush();
		for (int i = 0; i < obtenerNumComponentesHijos(); i++)
			
		{
			hijo = obtenerComponente(i);
			hijo.iniciarHebraProcesadora();
		}
	}

	public void sincronizar()
	{

	}

	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	public void setNivelPermisos(int nivel)
	{
		nivelPermisos = nivel;
		if (nivel >= 10)
			mostrar();
		else ocultar();
	}

	public Integer getID()
	{
		return DID;
	}

	public String getNombre()
	{
		return nombre;
	}

	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return null;
	}

	public ColaEventos obtenerColaRecepcion()
	{
		return colaRecepcion;
	}

	public ColaEventos obtenerColaEnvio()
	{
		return colaEnvio;
	}

}
