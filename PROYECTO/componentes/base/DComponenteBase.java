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
 * Componente base para construir componentes distribuidos. Para una descripcion
 * mas en profundidad consultar la documentacion del proyecto. Muchos de sus
 * metodos carecen de funcionalidad que deberá implementar el programador segun
 * sus necesidades
 */

public class DComponenteBase extends JPanel implements DComponente,
		java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	private CardLayout cardLayout1 = new CardLayout();

	private JPanel pcomponentes = new JPanel();

	private JPanel pfrontal = new JPanel();

	@SuppressWarnings("unused")
	private boolean mostrado = true;

	private boolean conexionDC = false;

	@SuppressWarnings("unused")
	private Vector<Object> djlisteners = new Vector<Object>(5);

	@SuppressWarnings("unused")
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

	public DComponenteBase()
	{
		super();
	}

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
	 * Método llamado cuando se oculta el padre del componente.
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
	 * Método llamado cuando el componente padre pasa de estado no visible a
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
	 * Devuelve el panel en el que se irán añadiendo los componentes que
	 * conforman el componente (distribuidos o no)
	 * 
	 * @return JPanel Panel de contenido
	 */
	public JPanel obtenerPanelContenido()
	{
		return pcomponentes;
	}

	/**
	 * Obtiene el numero de componentes hijo que tiene el componente.
	 * 
	 * @return int Número de componentes hijos
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
	 * Obtiene el componente con el número indicado como parametro
	 * 
	 * @param num
	 *            int Rango [0..n]
	 * @return DComponente
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
	 * @return boolean TRUE si esta conectado al DConector. FALSE en otro caso
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

	/**
	 * Consultamos si el componente esta oculto
	 * 
	 * @return boolean TRUE si esta oculto. FALSE en otro caso
	 */
	public boolean oculto()
	{
		return oculto;
	}

	/**
	 * Realiza el envío de un evento
	 * 
	 * @param evento
	 *            DEvent Evento que se desea enviar
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
	 *            LayoutManager
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
	 *            Component
	 * @param i
	 *            int
	 * @return Component
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
	 *            Component
	 * @param o
	 *            Object
	 * @param i
	 *            int
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
	 *            Component
	 * @param o
	 *            Object
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
	 *            Component
	 * @return Component
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
	 *            PopupMenu
	 */
	@Override
	public synchronized void add(PopupMenu pm)
	{
		pcomponentes.add(pm);
	}

	// ********* METODOS ASOCIADOS A LA INTERFAZ DCONECTOR ********************
	/**
	 * Se encola un evento para que sea procesado
	 * 
	 * @param evento
	 *            DEvent Evento Evento que se desea encolar
	 */
	public void procesarEvento(DEvent evento)
	{

		System.out.println("Recibido evento " + evento.tipo.toString());
		System.out.flush();
		colaRecepcion.nuevoEvento(evento);
	}

	/**
	 * Este metodo sera llamado por los componentes hijos cuando deseen enviar
	 * un evento
	 * 
	 * @param evento
	 *            DEvent Evento que desean enviar
	 */
	synchronized public void procesarEventoHijo(DEvent evento)
	{
		System.out.println(nombre + ": procesarEventoHijo(DEvent)");
		System.out.flush();
	}

	/**
	 * Realiza el procesamiento de un evento de metainformacion
	 * 
	 * @param evento
	 *            DMIEvent
	 */
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

	/**
	 * Activa el componente
	 */
	public void activar()
	{
		System.out.println(nombre + ": activar()");
		System.out.flush();
	}

	/**
	 * Desactiva el componente
	 */
	public void desactivar()
	{
		System.out.println(nombre + ": desactivar()");
		System.out.flush();
	}

	/**
	 * Inicia la hebra procesadora del componente
	 */
	public void iniciarHebraProcesadora()
	{
		if (( hp = crearHebraProcesadora() ) != null)
		{
			Thread t = new Thread(hp);
			t.start();
		}
		DComponente hijo;
		for (int i = 0; i < obtenerNumComponentesHijos(); i++)
		{
			hijo = obtenerComponente(i);
			hijo.iniciarHebraProcesadora();
		}
	}

	/**
	 * Mediante este metodo se le solicita el componente que envie su peticion
	 * de sincronizacion
	 */
	public void sincronizar()
	{

	}

	/**
	 * Devuelve el nivel de permisos actual del componente
	 * 
	 * @return int Permiso actual
	 */
	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	/**
	 * Establece el permiso actual del componente
	 * 
	 * @param nivel
	 *            int Permiso que se quiere establecer
	 */
	public void setNivelPermisos(int nivel)
	{
		nivelPermisos = nivel;
		if (nivel >= 10)
			mostrar();
		else ocultar();
	}

	/**
	 * Obtiene el ID del componente
	 * 
	 * @return Integer ID del componente
	 */
	public Integer getID()
	{
		return DID;
	}

	/**
	 * Obtiene el nombre del componente
	 * 
	 * @return String Nombre
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Devuelve la instancia de la hebra procesadora que se encargara de
	 * procesar los eventos que se reciban
	 * 
	 * @return HebraProcesadoraBase Hebra que procesará los eventos
	 */
	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return null;
	}

	/**
	 * Devuelve la instancia de la clase que se esta usando como cola de
	 * recepción
	 * 
	 * @return ColaEventos Cola de eventos
	 */
	public ColaEventos obtenerColaRecepcion()
	{
		return colaRecepcion;
	}

	/**
	 * Devuelve la instancia de la clase que se esta usando como cola de envio
	 * 
	 * @return ColaEventos Cola de eventos
	 */
	public ColaEventos obtenerColaEnvio()
	{
		return colaEnvio;
	}

}
