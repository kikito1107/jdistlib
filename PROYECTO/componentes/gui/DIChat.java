package componentes.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Vector;

import Deventos.DJChatEvent;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.DJChat;
import componentes.base.HebraProcesadoraBase;
import componentes.listeners.DJChatListener;
import componentes.listeners.LJChatListener;

/**
 * Chat con el que pueden hablar todos los usuarios de la aplicacion
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DIChat extends DComponenteBase implements java.io.Serializable
{
	private static final long serialVersionUID = 370978067165523554L;

	private DJChat chat = new DJChat();

	/**
	 * @param nombre
	 *            Nombre del componente.
	 * @param conexionDC
	 *            TRUE si esta en contacto directo con el DConector (no es hijo
	 *            de ningun otro componente) y FALSE en otro caso
	 * @param padre
	 *            Componente padre de este componente. Si no tiene padre
	 *            establecer a null
	 */
	public DIChat( String nombre, boolean conexionDC, DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);

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
	 * Constructor por defecto
	 */
	public DIChat()
	{
		super();

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
		this.setLayout(new BorderLayout());
		this.add(chat, null);
		chat.addDJChatListener(crearDJListener());
		// desactivar();//*******************************************************************************
	}

	/**
	 * Elimina el texto que haya en el chat
	 */
	public void limpiarTexto()
	{
		chat.limpiarTexto();
	}

	/**
	 * Obtiene el texto que haya en el chat
	 * 
	 * @return Texto del chat
	 */
	public String getTexto()
	{
		return chat.getTexto();
	}

	/**
	 * Asigna una fuente al chat
	 * 
	 * @param f
	 *            Fuente a asignar
	 */
	public void setFuente(Font f)
	{
		chat.setFuente(f);
	}

	/**
	 * Establece el nivel de permisos de este componente. No se recomienda
	 * llamar a este metodo desde el programa. Sera llamado de forma automatica
	 * cuando sea necesario
	 * 
	 * @param nivel
	 *            Nivel que queremos establecer
	 */
	@Override
	public void setNivelPermisos(int nivel)
	{
		super.setNivelPermisos(nivel);
		if (nivel < 20)
			chat.permisoLectura();
		else chat.permisoLecturaEscritura();
	}

	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE
	 * devuelve 0
	 * 
	 * @return Numero de componentes hijos. SIEMPRE devuelve 0.
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
	 *            Listener a añadir
	 */
	public void addDJChatListener(DJChatListener listener)
	{
		chat.addDJChatListener(listener);
	}

	/**
	 * Añade un LListener al componente para ser notificado cuando cambie el
	 * estado del componente
	 * 
	 * @param listener
	 *            Listener a añadir
	 */
	public void addLJChatListener(LJChatListener listener)
	{
		chat.addLJChatListener(listener);
	}

	/**
	 * Obtiene los DJListener que tiene asociado el componente
	 * 
	 * @return Vector de listeners DJChatListener
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getDJChatListeners()
	{
		return chat.getDJChatListeners();
	}

	/**
	 * Obtiene los LJListener que tiene asociado el componente
	 * 
	 * @return Vector de listeners LJChatListener
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getLJChatListeners()
	{
		return chat.getLJChatListeners();
	}

	/**
	 * Elimina todos los DJListeners que tiene asociado el componente
	 */
	public void removeDJChatListeners()
	{
		chat.removeDJChatListeners();
	}

	/**
	 * Elimina todos los LJListeners que tiene asociado el componente
	 */
	public void removeLJChatListeners()
	{
		chat.removeLJChatListeners();
	}

	/**
	 * Activa el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void activar()
	{
		chat.activar();
	}

	/**
	 * Desctiva el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void desactivar()
	{
		chat.desactivar();
	}

	/**
	 * Mediante una llamada a este metodo se envia un mensaje de peticion de
	 * sincronizacion. En este componente no se realiza sincronizacion por lo
	 * que no tiene utilidad ninguna.
	 */
	@Override
	public void sincronizar()
	{
		// Este componente no realiza sincronizacion
	}

	public DJChatListener crearDJListener()
	{
		return new Listener();
	}

	/**
	 * Devuelve una nueva instancia de la hebra que se encargara de procesar los
	 * eventos que se reciban. Este metodo no debe llamarse de forma directa.
	 * Sera llamado de forma automatica cuando sea necesario.
	 * 
	 * @return Nueva hebra procesadora
	 */
	@Override
	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return new HebraProcesadora(this);
	}

	private class Listener implements DJChatListener
	{
		public void nuevoMensaje(DJChatEvent evento)
		{
			enviarEvento(evento);
		}
	}

	/**
	 * Hebra procesadora de eventos. Se encarga de realizar las acciones que
	 * correspondan cuando recibe un evento. En este caso no se realiza
	 * sincronizacion dado que no es necesario.
	 */
	private class HebraProcesadora extends HebraProcesadoraBase
	{

		HebraProcesadora( DComponente dc )
		{
			super(dc);
		}

		@Override
		public void run()
		{
			DJChatEvent evento = null;
			// System.out.println("iniciada hebra procesadora");

			activar();

			while (true)
			{
				evento = (DJChatEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				chat.procesarEvento(evento);
			}

		}
	}

}
