package componentes.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import lookandfeel.Dmetal.DMetalComboBoxUI;
import Deventos.DEvent;
import Deventos.DJComboBoxEvent;
import Deventos.enlaceJS.DConector;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.DJComboBox;
import componentes.base.HebraProcesadoraBase;
import componentes.listeners.DJComboBoxListener;
import componentes.listeners.LJComboBoxListener;

/**
 * ComboBox distribuido. Consultar documentacion del proyecto para ver su
 * funcionamiento.
 */

public class DIComboBox extends DComponenteBase implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	DJComboBox combobox = null;

	BorderLayout borderLayout1 = new BorderLayout();

	public DIComboBox()
	{
		super();
	}

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
	public DIComboBox( String nombre, boolean conexionDC, DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		this.combobox = new DJComboBox(this, new String[]
		{ "elemento1", "elemento2" });

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
	 * @param elementos
	 *            String[] Array de elementos que contendra el combobox
	 * @param nombre
	 *            String Nombre del componente.
	 * @param conexionDC
	 *            boolean TRUE si esta en contacto directo con el DConector (no
	 *            es hijo de ningun otro componente) y FALSE en otro caso
	 * @param padre
	 *            DComponenteBase Componente padre de este componente. Si no
	 *            tiene padre establecer a null
	 */
	public DIComboBox( String[] elementos, String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		this.combobox = new DJComboBox(this, elementos);

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
		this.setDebugGraphicsOptions(0);
		this.setLayout(borderLayout1);
		combobox.setPreferredSize(new Dimension(60, 19));
		this.add(combobox, BorderLayout.NORTH);
		combobox.addDJComboBoxListener(crearDJListener());
	}

	/**
	 * Devuelve la instancia de la clase captadora que está usando.
	 * 
	 * @return DJComboBox Clase captadora
	 */
	public DJComboBox obtenerJComponente()
	{
		return combobox;
	}

	public DJComboBox obtenerCmp()
	{
		return combobox;
	}

	public boolean ocultoEnJerarquia()
	{
		boolean oculto = false;
		DComponente padre = this.obtenerPadre();
		while (padre != null)
		{
			if (( oculto == true ) || padre.oculto()) oculto = true;
			padre = padre.obtenerPadre();
		}
		return oculto;
	}

	/**
	 * Establece el nivel de permisos de este componente. No se recomienda
	 * llamar a este método desde el programa. Será llamado de forma automática
	 * cuando sea necesario
	 * 
	 * @param nivel
	 *            int Nivel que queremos establecer
	 */
	@Override
	public void setNivelPermisos(int nivel)
	{
		// nivelPermisos = nivel;
		if (( nivel < 10 ) || ocultoEnJerarquia()) combobox.ocultarPopup();

		super.setNivelPermisos(nivel);
		if (nivel < 20)
			combobox.setForeground(Color.GRAY);
		else combobox.setForeground(Color.BLACK);

		if (( nivel > 10 ) && !ocultoEnJerarquia())
			combobox.actualizarEstadoPopup();
	}

	@Override
	public void padreOcultado()
	{
		// System.out.println("Padre ocultado");
		if (combobox != null) combobox.hidePopup();
	}

	@Override
	public void padreMostrado()
	{
		// System.out.println("Padre mostrado");
		if (combobox != null)
			if (( nivelPermisos > 10 ) && !ocultoEnJerarquia())
				combobox.actualizarEstadoPopup();
	}

	public DJComboBoxEvent obtenerInfoEstado()
	{
		return combobox.obtenerInfoEstado();
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
	 *            DJComboBoxListener Listener a añadir
	 */
	public void addDJComboBoxListener(DJComboBoxListener listener)
	{
		combobox.addDJComboBoxListener(listener);
	}

	/**
	 * Añade un LListener al componente para ser notificado cuando cambie el
	 * estado del componente
	 * 
	 * @param listener
	 *            LJComboBoxListener Listener a añadir
	 */
	public void addLJComboBoxListener(LJComboBoxListener listener)
	{
		combobox.addLJComboBoxListener(listener);
	}

	/**
	 * Añade un LUListener al componente para ser notificado cuando cambie el
	 * estado del componente. Solo sera notificado cuando el cambio de estado se
	 * deba a una accion realizada por el usuario de la aplicacion.
	 * 
	 * @param listener
	 *            LJComboBoxListener Listener a añadir
	 */
	public void addLUJComboBoxListener(LJComboBoxListener listener)
	{
		combobox.addLUJComboBoxListener(listener);
	}

	/**
	 * Obtiene los DJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners DJComboBoxListener
	 */
	@SuppressWarnings("unchecked")
	public Vector getDJComboBoxListeners()
	{
		return combobox.getDJComboBoxListeners();
	}

	/**
	 * Obtiene los LJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners LJComboBoxListener
	 */
	@SuppressWarnings("unchecked")
	public Vector getLJComboBoxListeners()
	{
		return combobox.getLJComboBoxListeners();
	}

	/**
	 * Obtiene los LUJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners LJComboBoxListener
	 */
	@SuppressWarnings("unchecked")
	public Vector getLUJComboBoxListeners()
	{
		return combobox.getLUJComboBoxListeners();
	}

	/**
	 * Elimina todos los DJListeners que tiene asociado el componente
	 */
	public void removeDJComboBoxListeners()
	{
		combobox.removeDJComboBoxListeners();
	}

	/**
	 * Elimina todos los LJListeners que tiene asociado el componente
	 */
	public void removeLJComboBoxListeners()
	{
		combobox.removeLJComboBoxListeners();
	}

	/**
	 * Elimina todos los LUJListeners que tiene asociado el componente
	 */
	public void removeLUJComboBoxListeners()
	{
		combobox.removeLUJComboBoxListeners();
	}

	/**
	 * Activa el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void activar()
	{
		combobox.activar();
	}

	/**
	 * Desctiva el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void desactivar()
	{
		combobox.desactivar();
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
			DJComboBoxEvent peticion = new DJComboBoxEvent();
			peticion.tipo = new Integer(DJComboBoxEvent.SINCRONIZACION
					.intValue());
			enviarEvento(peticion);
		}
	}

	/**
	 * Devuelve una instancia de un listener que se encargara de tratar los
	 * eventos que se reciben desde la clase captadora. Normalmente este
	 * tratamiento se reduce a enviar el evento.
	 * 
	 * @return DJComboBoxListener Listener creado
	 */
	public DJComboBoxListener crearDJListener()
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
	private class Listener implements DJComboBoxListener
	{
		int i = 0;

		public void abierto()
		{
			DJComboBoxEvent evento = new DJComboBoxEvent();
			evento.tipo = new Integer(DJComboBoxEvent.ABIERTO.intValue());
			enviarEvento(evento);
		}

		public void cerrado()
		{
			DJComboBoxEvent evento = new DJComboBoxEvent();
			evento.tipo = new Integer(DJComboBoxEvent.CERRADO.intValue());
			enviarEvento(evento);
		}

		public void cambioSeleccionLista(DJComboBoxEvent evento)
		{
			enviarEvento(evento);
			// System.out.println("Cambio seleccion de lista " + i++);
		}

		public void seleccion(DJComboBoxEvent evento)
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

		@SuppressWarnings( "unchecked" )
		@Override
		public void run()
		{
			DJComboBoxEvent evento = null;
			DJComboBoxEvent saux = null;
			DJComboBoxEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJComboBoxEvent) eventos[j];
				if (( respSincr == null )
						&& ( saux.tipo.intValue() == DJComboBoxEvent.RESPUESTA_SINCRONIZACION
								.intValue() ))
					respSincr = saux;
				else vaux.add(saux);
			}

			activar();

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				combobox.setInfoInicial(respSincr.abierto.booleanValue(),
						respSincr.itemSeleccionado.intValue());
				// combobox.actualizarEstadoPopup();
				if (!oculto() && !ocultoEnJerarquia())
				{
					combobox.showPopup();
					( (DMetalComboBoxUI) combobox.getUI() )
							.setItemLista(respSincr.itemSeleccionado.intValue());

				}
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJComboBoxEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) combobox.procesarEvento(saux);
			}

			// Comenzamos a escuchar eventos del usuario

			while (true)
			{
				evento = (DJComboBoxEvent) colaRecepcion.extraerEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				// Indicamos cual ha sido el último evento procesado
				ultimoProcesado = new Integer(evento.contador.intValue());

				if (( evento.tipo.intValue() == DJComboBoxEvent.SINCRONIZACION
						.intValue() )
						&& !evento.usuario.equals(DConector.Dusuario))
				{
					DJComboBoxEvent infoEstado = obtenerInfoEstado();
					infoEstado.tipo = new Integer(
							DJComboBoxEvent.RESPUESTA_SINCRONIZACION.intValue());
					enviarEvento(infoEstado);
				}
				else combobox.procesarEvento(evento);
			}

		}
	}

}
