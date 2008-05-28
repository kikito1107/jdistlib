package componentes.gui;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.tree.TreeNode;

import lookandfeel.Dmetal.DMetalTreeUI;
import Deventos.enlaceJS.DConector;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.DJTree;
import componentes.base.HebraProcesadoraBase;
import componentes.listeners.DJTreeListener;
import componentes.listeners.LJTreeListener;
import Deventos.DEvent;
import Deventos.DJTreeEvent;

/**
 * Arbol compartido. Consultar documentación del proyecto para ver su
 * funcionamiento
 */
public class DITree extends DComponenteBase implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4168312720558774854L;

	BorderLayout borderLayout1 = new BorderLayout();

	JScrollPane jScrollPane1 = new JScrollPane();

	DJTree arbol = null;

	/**
	 * @param nombre
	 *            String Nombre del componente.
	 * @param conexionDC
	 *            boolean TRUE si esta en contacto directo con el DConector (no
	 *            es hijo de ningun otro componente) y FALSE en otro caso
	 * @param padre
	 *            DComponenteBase Componente padre de este componente. Si no
	 *            tiene padre establecer a null
	 */
	public DITree( String nombre, boolean conexionDC, DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		this.arbol = new DJTree();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param nodoRaiz
	 *            String Nodo raiz del arbol
	 * @param nombre
	 *            String Nombre del componente.
	 * @param conexionDC
	 *            boolean TRUE si esta en contacto directo con el DConector (no
	 *            es hijo de ningun otro componente) y FALSE en otro caso
	 * @param padre
	 *            DComponenteBase Componente padre de este componente. Si no
	 *            tiene padre establecer a null
	 */
	public DITree( TreeNode nodoRaiz, String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		this.arbol = new DJTree(nodoRaiz);
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(arbol, null);
		arbol.addDJTreeListener(crearDJListener());
		// desactivar();//*******************************************************************************
	}

	/**
	 * Devuelve la instancia de la clase captadora que está usando.
	 * 
	 * @return DJTree Clase captadora
	 */
	public DJTree obtenerJComponente()
	{
		return arbol;
	}

	public DJTreeEvent obtenerInfoEstado()
	{
		return arbol.obtenerInfoEstado();
	}

	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE
	 * devuelve 0
	 * 
	 * @return int Número de componentes hijos. SIEMPRE devuelve 0.
	 */
	@Override
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	/**
	 * Añade un DJListener a la clase captadora para recibir los eventos
	 * generados por la interaccion del usuario con el componente
	 * 
	 * @param listener
	 *            DJButtonListener Listener a añadir
	 */
	public void addDJTreeListener(DJTreeListener listener)
	{
		arbol.addDJTreeListener(listener);
	}

	/**
	 * Añade un LListener al componente para ser notificado cuando cambie el
	 * estado del componente
	 * 
	 * @param listener
	 *            LJTreeListener Listener a añadir
	 */
	public void addLJTreeListener(LJTreeListener listener)
	{
		arbol.addLJTreeListener(listener);
	}

	/**
	 * Añade un LUListener al componente para ser notificado cuando cambie el
	 * estado del componente. Solo sera notificado cuando el cambio de estado se
	 * deba a una accion realizada por el usuario de la aplicacion.
	 * 
	 * @param listener
	 *            LJTreeListener Listener a añadir
	 */
	public void addLUJTreeListener(LJTreeListener listener)
	{
		arbol.addLUJTreeListener(listener);
	}

	/**
	 * Obtiene los DJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners DJTreeListener
	 */
	public Vector getDJTreeListeners()
	{
		return arbol.getDJTreeListeners();
	}

	/**
	 * Obtiene los LJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners LJTreeListener
	 */
	public Vector getLJTreeListeners()
	{
		return arbol.getLJTreeListeners();
	}

	/**
	 * Obtiene los LUJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners LJTreeListener
	 */
	public Vector getLUJTreeListeners()
	{
		return arbol.getLUJTreeListeners();
	}

	/**
	 * Elimina todos los DJListeners que tiene asociado el componente
	 */
	public void removeDJTreeListeners()
	{
		arbol.removeDJTreeListeners();
	}

	/**
	 * Elimina todos los LJListeners que tiene asociado el componente
	 */
	public void removeLJTreeListeners()
	{
		arbol.removeLJTreeListeners();
	}

	/**
	 * Elimina todos los LUJListeners que tiene asociado el componente
	 */
	public void removeLUJTreeListeners()
	{
		arbol.removeLUJTreeListeners();
	}

	/**
	 * Activa el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void activar()
	{
		arbol.activar();
	}

	/**
	 * Desctiva el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void desactivar()
	{
		arbol.desactivar();
	}

	/**
	 * Mediante una llamada a este método se envía un mensaje de peticion de
	 * sincronizacion. No se debe llamar a este método de forma directa. Será
	 * llamado de forma automatica cuando sea necesario realizar la
	 * sincronizacion
	 */
	@Override
	public void sincronizar()
	{
		if (conectadoDC())
		{
			DJTreeEvent peticion = new DJTreeEvent();
			peticion.tipo = new Integer(DJTreeEvent.SINCRONIZACION.intValue());
			enviarEvento(peticion);
		}
	}

	/**
	 * Devuelve una instancia de un listener que se encargara de tratar los
	 * eventos que se reciben desde la clase captadora. Normalmente este
	 * tratamiento se reduce a enviar el evento.
	 * 
	 * @return DJTreeListener Listener creado
	 */
	public DJTreeListener crearDJListener()
	{
		return new Listener();
	}

	/**
	 * Devuelve una nueva instancia de la hebra que se encargara de procesar los
	 * eventos que se reciban. Este metodo no debe llamarse de forma directa.
	 * Sera llamado de forma automatica cuando sea necesario.
	 * 
	 * @return HebraProcesadoraBase Nueva hebra procesadora
	 */
	@Override
	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return new HebraProcesadora(this);
	}

	/**
	 * Listener encargado de gestionar los eventos procedentes de la clase
	 * captadora
	 */
	private class Listener implements DJTreeListener
	{

		public void apertura_cierre(DJTreeEvent evento)
		{
			enviarEvento(evento);
		}

		public void seleccion(DJTreeEvent evento)
		{
			enviarEvento(evento);
		}
	}

	/**
	 * Hebra procesadora de eventos. Se encarga de realizar las acciones que
	 * correspondan cuando recibe un evento. Tambén se encarga en su inicio de
	 * sincronizar el componente.
	 */
	class HebraProcesadora extends HebraProcesadoraBase
	{

		HebraProcesadora( DComponente dc )
		{
			super(dc);
		}

		private void setEstado(Vector v)
		{
			( (DMetalTreeUI) arbol.getUI() ).setEstado(v);
		}

		@Override
		public void run()
		{
			// System.out.println(getNombre() + " Iniciada HebraProcesadora");
			DJTreeEvent evento = null;
			DJTreeEvent saux = null;
			DJTreeEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			int i = 0;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJTreeEvent) eventos[j];
				if (( respSincr == null )
						&& ( saux.tipo.intValue() == DJTreeEvent.RESPUESTA_SINCRONIZACION
								.intValue() ))
					respSincr = saux;
				else vaux.add(saux);
			}

			activar();

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				setEstado(respSincr.estado);
			}
			else
			{

			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJTreeEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) arbol.procesarEvento(saux);
			}

			while (true)
			{
				evento = (DJTreeEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (( evento.tipo.intValue() == DJTreeEvent.SINCRONIZACION
						.intValue() )
						&& !evento.usuario.equals(DConector.Dusuario))
				{
					DJTreeEvent infoEstado = obtenerInfoEstado();
					infoEstado.tipo = new Integer(
							DJTreeEvent.RESPUESTA_SINCRONIZACION.intValue());
					enviarEvento(infoEstado);
				}
				else arbol.procesarEvento(evento);
			}

		}
	}

}
