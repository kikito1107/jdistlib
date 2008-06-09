package componentes.util;

import java.awt.Container;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

import Deventos.enlaceJS.DConector;

import componentes.listeners.DMenuSelectionManagerListener;
import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJListEvent;
import Deventos.DMenuSelectionManagerEvent;

/**
 * Implementa un manejador de seleccion para los menus distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
@SuppressWarnings( "unchecked" )
public class DMenuSelectionManager extends MenuSelectionManager
{
	private static final DMenuSelectionManager dinstance = new DMenuSelectionManager();

	private boolean inicializado = false;

	private Vector dmenuselectionmanagerlisteners = new Vector(5);

	private Integer DID = new Integer(-1);

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private Integer ultimoProcesado = new Integer(-1);

	private int nivelPermisos = 20;

	private JMenuBar barraMenu = null;

	/**
	 * Inicializa la clase
	 * 
	 * @param barraMenu
	 *            Barra de menu asociada al menu
	 */
	public void inicializar(JMenuBar barraMenu)
	{
		if (!inicializado)
		{
			addDMenuSelectionManagerListener(new Listener());
			// DID = new Integer(DConector.alta(this));
			colaEnvio = DConector.getColaEventos();
			this.barraMenu = barraMenu;
			inicializado = true;
		}
	}

	/**
	 * Obtiene la barra de menu
	 * 
	 * @return Barra de menu asociada
	 */
	public JMenuBar getBarraMenu()
	{
		return barraMenu;
	}

	/**
	 * Indica si esta o no oculto
	 * 
	 * @return False siempre
	 */
	public boolean oculto()
	{
		return false;
	}

	/**
	 * Agrega un listener al manejador
	 * 
	 * @param listener
	 *            Listener para el manejador
	 */
	public void addDMenuSelectionManagerListener(
			DMenuSelectionManagerListener listener)
	{
		dmenuselectionmanagerlisteners.add(listener);
	}

	/**
	 * Obtiene los listeners para el manejador
	 * 
	 * @return Vector con los listeners para el manejador
	 */
	public Vector getDMenuSelectionManagerListeners()
	{
		return dmenuselectionmanagerlisteners;
	}

	/**
	 * Obtiene la instancia del manejador
	 * 
	 * @return Instancia del manejador
	 */
	public static MenuSelectionManager defaultManager()
	{
		return dinstance;
	}

	@Override
	public void setSelectedPath(MenuElement[] path)
	{
		super.setSelectedPath(path);
	}

	private void superSetSelectedPath(MenuElement[] path)
	{
		super.setSelectedPath(path);
	}

	@Override
	public MenuElement[] getSelectedPath()
	{
		return super.getSelectedPath();
	}

	/**
	 * Inicia la hebra procesadora de eventos
	 */
	public void iniciarHebraProcesadora()
	{
		Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
		t.start();
	}

	/**
	 * Procesa un evento
	 * 
	 * @param evento
	 *            Evento a procesar
	 */
	public void procesarEvento(DEvent evento)
	{
		colaRecepcion.nuevoEvento(evento);
	}

	/**
	 * Sincroniza los componentes
	 */
	public void sincronizar()
	{
		DMenuSelectionManagerEvent evento = new DMenuSelectionManagerEvent();

		evento.origen = new Integer(1); // Aplicacion
		evento.destino = new Integer(0); // Coordinador
		evento.componente = new Integer(DID.intValue());
		evento.tipo = new Integer(DMenuSelectionManagerEvent.SINCRONIZACION
				.intValue());
		evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());
		colaEnvio.nuevoEvento(evento);
	}

	/**
	 * Asigna un nivel de permisos
	 * 
	 * @param nivel
	 *            Nivel de permisos
	 */
	public void setNivelPermisos(int nivel)
	{
		nivelPermisos = nivel;
	}

	/**
	 * Obtiene el nivel de permisos
	 * 
	 * @return Nivel de permisos
	 */
	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	/**
	 * Obtiene el identificador del componente
	 * 
	 * @return Identificador del componente
	 */
	public Integer getID()
	{
		return DID;
	}

	/**
	 * Obtiene el nombre del componente
	 * 
	 * @return Nombre del componente
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Obtiene la cola de recepcion de eventos
	 * 
	 * @return Cola de eventos
	 */
	public ColaEventos obtenerColaRecepcion()
	{
		return colaRecepcion;
	}

	/**
	 * Obtiene la cola de envio de eventos
	 * 
	 * @return Cola de eventos
	 */
	public ColaEventos obtenerColaEnvio()
	{
		return colaEnvio;
	}

	/**
	 * Convierte un path de seleccion a un vector
	 * 
	 * @param path
	 *            Path de seleccion
	 * @return Vector creado a partir del path de seleccion
	 */
	private Vector pathToVector(MenuElement[] path)
	{
		Vector v = new Vector();
		int i, j, n;
		v.add(new Integer(0));
		for (i = 1; i < path.length; i++)
			if (path[i] instanceof JPopupMenu)
				v.add(new Integer(-5));
			else
			{
				MenuElement[] me = path[i - 1].getSubElements();
				n = -1;
				for (j = 0; j < me.length; j++)
					if (me[j].equals(path[i])) n = j;
				v.add(new Integer(n));
			}
		return v;
	}

	/**
	 * Convierte un vector a un path de seleccion
	 * 
	 * @param v
	 *            Vector a convertir
	 * @return Array de elementos de menu que forman el path de seleccion
	 */
	private MenuElement[] vectorToPath(Vector v)
	{
		MenuElement[] me = new MenuElement[v.size()];
		MenuElement elementoActual = null;
		int i;
		if (me.length > 0)
		{
			me[0] = barraMenu;
			elementoActual = barraMenu;
			for (i = 1; i < v.size(); i++)
				if (( (Integer) v.elementAt(i) ).intValue() == -5)
				{ // es un popup
					me[i] = ( (JMenu) elementoActual ).getPopupMenu();
					elementoActual = me[i];
				}
				else
				{
					int indice = ( (Integer) v.elementAt(i) ).intValue();
					me[i] = (MenuElement) ( (Container) elementoActual )
							.getComponent(indice);
					elementoActual = me[i];
				}
		}
		return me;
	}

	/**
	 * Permite probar la clase
	 */
	public void prueba()
	{
		MenuElement[] me = dinstance.getSelectedPath();
		dinstance.clearSelectedPath();
		Vector v = pathToVector(me);
		me = vectorToPath(v);
		dinstance.setSelectedPath(me);
	}

	/**
	 * Listener para el cambio de path en la seleccion
	 */
	private class Listener implements DMenuSelectionManagerListener
	{
		public void cambioPath(DMenuSelectionManagerEvent evento)
		{
			evento.componente = new Integer(DID.intValue());
			evento.origen = new Integer(1); // Aplicacion
			evento.destino = new Integer(0); // Coordinador
			evento.componente = new Integer(DID.intValue());
			evento.tipo = new Integer(DMenuSelectionManagerEvent.CAMBIO_PATH
					.intValue());
			evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

			colaEnvio.nuevoEvento(evento);

		}
	}

	/**
	 * Hebra encargada de procesar los eventos
	 */
	private class HebraProcesadora implements Runnable
	{
		private ColaEventos cola = null;

		/**
		 * Constructor
		 * 
		 * @param cola
		 *            Cola de eventos
		 * @param manager
		 *            Manejador de seleccion de menus
		 */
		public HebraProcesadora( ColaEventos cola, DMenuSelectionManager manager )
		{
			this.cola = cola;
		}

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			DMenuSelectionManagerEvent evento = null;
			DMenuSelectionManagerEvent saux = null;
			DMenuSelectionManagerEvent respSincr = null;
			ColaEventos colaAux = new ColaEventos();

			int numEventos = colaRecepcion.tamanio(); // Para evitar quedarnos
			// bloqueados
			// int i=0;
			// int posicion=-1;
			// boolean encontradaRespuestaSincronizacion = false;

			if (respSincr != null)
			{// Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				superSetSelectedPath(vectorToPath(respSincr.path));
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = colaAux.tamanio();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DMenuSelectionManagerEvent) colaAux.extraerEvento();
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) colaRecepcion.nuevoEvento(saux);
			}

			while (true)
			{
				evento = (DMenuSelectionManagerEvent) cola.extraerEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				// System.out.println("HebraProcesadora("+DID+"): Recibido
				// evento del tipo "+evento.tipo);
				if (( evento.tipo.intValue() == DMenuSelectionManagerEvent.SINCRONIZACION
						.intValue() )
						&& !evento.usuario.equals(DConector.Dusuario))
				{
					DMenuSelectionManagerEvent aux = new DMenuSelectionManagerEvent();
					aux.origen = new Integer(1); // Aplicacion
					aux.destino = new Integer(0); // Coordinador
					aux.componente = new Integer(DID.intValue());
					aux.tipo = new Integer(DJListEvent.RESPUESTA_SINCRONIZACION
							.intValue());
					aux.ultimoProcesado = new Integer(ultimoProcesado
							.intValue());
					aux.path = pathToVector(dinstance.getSelectedPath());
					colaEnvio.nuevoEvento(aux);
				}
				if (evento.tipo.intValue() == DMenuSelectionManagerEvent.CAMBIO_PATH
						.intValue())
				{
					MenuElement[] me = vectorToPath(evento.path);
					superSetSelectedPath(me);
				}
			}
		}
	}
}
