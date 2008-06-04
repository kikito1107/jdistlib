package componentes.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Vector;

import Deventos.DEvent;
import Deventos.DJButtonEvent;
import Deventos.enlaceJS.DConector;

import componentes.base.DComponente;
import componentes.base.DComponenteBase;
import componentes.base.DJButton;
import componentes.base.HebraProcesadoraBase;
import componentes.listeners.DJButtonListener;
import componentes.listeners.LJButtonListener;

/**
 * Boton compartido. Consultar documentacion del proyecto para ver su
 * funcionamiento
 */

public class DIButton extends DComponenteBase implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7807193484457835590L;

	private DJButton boton = null;

	private String text = "";

	public DIButton()
	{
		super("boton", false, null);

		this.boton = new DJButton();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	};

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
	public DIButton( String nombre, boolean conexionDC, DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		this.boton = new DJButton();
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
	 * @param textoBoton
	 *            String Cadena de texto que presentara el boton
	 * @param nombre
	 *            String Nombre del componente.
	 * @param conexionDC
	 *            boolean TRUE si esta en contacto directo con el DConector (no
	 *            es hijo de ningun otro componente) y FALSE en otro caso
	 * @param padre
	 *            DComponenteBase Componente padre de este componente. Si no
	 *            tiene padre establecer a null
	 */
	public DIButton( String textoBoton, String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);
		this.boton = new DJButton(textoBoton);

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
		this.setLayout(new FlowLayout());
		this.add(boton, null);
		boton.setText(text);

		boton.addDJButtonListener(crearDJListener());
		// desactivar();//*******************************************************************************
	}

	/**
	 * Devuelve la instancia de la clase captadora que está usando.
	 * 
	 * @return DJButton Clase captadora
	 */
	public DJButton obtenerJComponente()
	{
		return boton;
	}

	public String getText()
	{
		return boton.getText();
	}

	public void setText(String text)
	{
		boton.setText(text);
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
		super.setNivelPermisos(nivel);
		if (nivel < 20)
			boton.setForeground(Color.GRAY);
		else boton.setForeground(Color.BLACK);
	}

	/**
	 * Obtiene la información de estado del componente que normalmente sera
	 * adjuntada aun evento de respuesta de sincronizacion
	 * 
	 * @return DJButtonEvent Evento que describe el estado del componente
	 */
	public DJButtonEvent getInfoEstado()
	{
		return boton.obtenerInfoEstado();
	}

	/**
	 * Establece el estado del componente a partir de un evento que contiene una
	 * informacion de estado. Este evento sera normalmente una respuesta
	 * recibida tras una peticion de sincronizacion
	 * 
	 * @param evento
	 *            DJButtonEvent Evento con la informacion del estado
	 */
	public void procesarInfoEstado(DJButtonEvent evento)
	{
		boton.getModel().setPressed(evento.pulsado.booleanValue());
		boton.getModel().setArmed(evento.pulsado.booleanValue());
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
	public void addDJButtonListener(DJButtonListener listener)
	{
		boton.addDJButtonListener(listener);
	}

	/**
	 * Añade un LListener al componente para ser notificado cuando cambie el
	 * estado del componente
	 * 
	 * @param listener
	 *            LJButtonListener Listener a añadir
	 */
	public void addLJButtonListener(LJButtonListener listener)
	{
		boton.addLJButtonListener(listener);
	}

	/**
	 * Añade un LUListener al componente para ser notificado cuando cambie el
	 * estado del componente. Solo sera notificado cuando el cambio de estado se
	 * deba a una accion realizada por el usuario de la aplicacion.
	 * 
	 * @param listener
	 *            LJButtonListener Listener a añadir
	 */
	public void addLUJButtonListener(LJButtonListener listener)
	{
		boton.addLUJButtonListener(listener);
	}

	/**
	 * Obtiene los DJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners DJButtonListener
	 */
	public Vector getDJButtonListeners()
	{
		return boton.getDJButtonListeners();
	}

	/**
	 * Obtiene los LJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners LJButtonListener
	 */
	public Vector getLJButtonListeners()
	{
		return boton.getLJButtonListeners();
	}

	/**
	 * Obtiene los LUJListener que tiene asociado el componente
	 * 
	 * @return Vector Vector de listeners LJButtonListener
	 */
	public Vector getLUJButtonListeners()
	{
		return boton.getLUJButtonListeners();
	}

	/**
	 * Elimina todos los DJListeners que tiene asociado el componente
	 */
	public void removeDJButtonListeners()
	{
		boton.removeDJButtonListeners();
	}

	/**
	 * Elimina todos los LJListeners que tiene asociado el componente
	 */
	public void removeLJButtonListeners()
	{
		boton.removeLJButtonListeners();
	}

	/**
	 * Elimina todos los LUJListeners que tiene asociado el componente
	 */
	public void removeLUJButtonListeners()
	{
		boton.removeLUJButtonListeners();
	}

	/**
	 * Activa el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void activar()
	{
		boton.activar();
	}

	/**
	 * Desctiva el componente. No se recomienda llamar a este metodo desde el
	 * programa. Sera llamado de forma automatica cuando sea necesario
	 */
	@Override
	public void desactivar()
	{
		boton.desactivar();
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
			DJButtonEvent peticion = new DJButtonEvent();
			peticion.tipo = new Integer(DJButtonEvent.SINCRONIZACION.intValue());
			enviarEvento(peticion);
		}
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
	 * Devuelve una instancia de un listener que se encargara de tratar los
	 * eventos que se reciben desde la clase captadora. Normalmente este
	 * tratamiento se reduce a enviar el evento.
	 * 
	 * @return DJButtonListener Listener creado
	 */
	public DJButtonListener crearDJListener()
	{
		return new Listener();
	}

	/**
	 * Listener encargado de gestionar los eventos procedentes de la clase
	 * captadora
	 */
	private class Listener implements DJButtonListener
	{

		public void presionado(DJButtonEvent evento)
		{
			enviarEvento(evento);
		}

		public void soltado(DJButtonEvent evento)
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
			// System.out.println("iniciada hebra procesadora");
			DJButtonEvent evento = null;
			DJButtonEvent saux = null;
			DJButtonEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJButtonEvent) eventos[j];
				if (( respSincr == null )
						&& ( saux.tipo.intValue() == DJButtonEvent.RESPUESTA_SINCRONIZACION
								.intValue() ))
					respSincr = saux;
				else vaux.add(saux);
			}

			activar();

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				boton.getModel().setPressed(respSincr.pulsado.booleanValue());
				boton.getModel().setArmed(respSincr.pulsado.booleanValue());
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJButtonEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) boton.procesarEvento(saux);
			}

			while (true)
			{
				evento = (DJButtonEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (( evento.tipo.intValue() == DJButtonEvent.SINCRONIZACION
						.intValue() )
						&& !evento.usuario.equals(DConector.Dusuario))
				{
					DJButtonEvent infoEstado = getInfoEstado();
					infoEstado.tipo = new Integer(
							DJButtonEvent.RESPUESTA_SINCRONIZACION.intValue());
					enviarEvento(infoEstado);
				}
				else boton.procesarEvento(evento);
			}

		}
	}

}
