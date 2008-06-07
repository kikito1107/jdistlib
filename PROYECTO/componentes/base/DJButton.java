package componentes.base;

import java.awt.Color;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJButtonEvent;
import Deventos.enlaceJS.DConector;

import componentes.listeners.DJButtonListener;
import componentes.listeners.LJButtonListener;

/**
 * Implementacion de la clase captadora de eventos para el componente Boton
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJButton extends JButton
{
	private static final long serialVersionUID = 1L;

	private static final String uiClassID = "DButtonUI";

	@SuppressWarnings( "unchecked" )
	private Vector djbuttonlisteners = new Vector(5);

	@SuppressWarnings( "unchecked" )
	private Vector ljbuttonlisteners = new Vector(5);

	@SuppressWarnings( "unchecked" )
	private Vector ujbuttonlisteners = new Vector(5);

	private Integer DID = null;

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private Integer ultimoProcesado = new Integer(-1);

	private int nivelPermisos = 10;

	private HebraProcesadoraBase hebraProcesadora = null;

	/**
	 * Constructor por defecto
	 */
	public DJButton()
	{
		this.nombre = null;
		extrasConstructor();
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param p0
	 *            Texto que mostrara el boton
	 */
	public DJButton( String p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param p0
	 *            Accion que realizara el boton
	 */
	public DJButton( Action p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param p0
	 *            Icono que mostrara el boton
	 */
	public DJButton( Icon p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param p0
	 *            Texto que mostrara el boton
	 * @param p1
	 *            Icono que mostrara el boton
	 */
	public DJButton( String p0, Icon p1 )
	{
		super(p0, p1);
		this.nombre = null;
		extrasConstructor();
	}

	private void extrasConstructor()
	{

	}

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

	/**
	 * Permite agregar un listener para los eventos producidos
	 * 
	 * @param listener
	 *            Listener a agregar
	 */
	@SuppressWarnings( "unchecked" )
	public void addDJButtonListener(DJButtonListener listener)
	{
		djbuttonlisteners.add(listener);
	}

	/**
	 * Permite agregar un listener para los eventos que sean propagados para
	 * todos los usuarios conectados
	 * 
	 * @param listener
	 *            Listener a agregar
	 */
	@SuppressWarnings( "unchecked" )
	public void addLJButtonListener(LJButtonListener listener)
	{
		ljbuttonlisteners.add(listener);
	}

	/**
	 * Permite agregar un listener para los eventos que sean para un usuario
	 * concreto de los conectados
	 * 
	 * @param listener
	 *            Listener a agregar
	 */
	@SuppressWarnings( "unchecked" )
	public void addLUJButtonListener(LJButtonListener listener)
	{
		ujbuttonlisteners.add(listener);
	}

	/**
	 * Permite obtener el vector de Listeners para los eventos distribuidos que
	 * se produzcan
	 * 
	 * @return Vector de listeners
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getDJButtonListeners()
	{
		return djbuttonlisteners;
	}

	/**
	 * Permite obtener el vector de Listeners para los eventos producidos para
	 * los usuarios conectados
	 * 
	 * @return Vector de listeners
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getLJButtonListeners()
	{
		return ljbuttonlisteners;
	}

	/**
	 * Permite obtener el vector de Listeners para los eventos producidos para
	 * un usuario concreto de los conectados
	 * 
	 * @return Vector de listeners
	 */
	@SuppressWarnings( "unchecked" )
	public Vector getLUJButtonListeners()
	{
		return ujbuttonlisteners;
	}

	/**
	 * Elimina los listener que reciben los eventos distribuidos
	 */
	public void removeDJButtonListeners()
	{
		djbuttonlisteners.removeAllElements();
	}

	/**
	 * Elimina los listener que reciben los eventos para los usuarios conectados
	 * en el sistema
	 */
	public void removeLJButtonListeners()
	{
		ljbuttonlisteners.removeAllElements();
	}

	/**
	 * Elimina los listener que reciben los eventos para un usuario conectado al
	 * sistema
	 */
	public void removeLUJButtonListeners()
	{
		ujbuttonlisteners.removeAllElements();
	}

	/**
	 * Habilita el componente
	 */
	public void activar()
	{
		setEnabled(true);
	}

	/**
	 * Deshabilita el componente
	 */
	public void desactivar()
	{
		setEnabled(false);
	}

	/**
	 * Inicia la hebra de procesamiento de eventos
	 */
	public void iniciarHebraProcesadora()
	{
		hebraProcesadora = crearHebraProcesadora();
		hebraProcesadora.iniciarHebra();
	}

	/**
	 * Obtiene un evento con la informacion de estado del componente
	 * 
	 * @return Evento con la informacion de estado del componente
	 */
	public DJButtonEvent obtenerInfoEstado()
	{
		DJButtonEvent evento = new DJButtonEvent();
		evento.pulsado = new Boolean(getModel().isPressed());
		return evento;
	}

	/**
	 * Procesa un evento
	 * 
	 * @param evento
	 *            Evento a procesar
	 */
	@SuppressWarnings( "unchecked" )
	public void procesarEvento(DEvent evento)
	{
		int i;
		if (evento.tipo.intValue() == DJButtonEvent.PRESIONADO.intValue())
			if (!getModel().isPressed())
			{// No esta presionado
				getModel().setPressed(true);
				getModel().setArmed(true);

				Vector v = getLJButtonListeners();
				for (i = 0; i < v.size(); i++)
					( (LJButtonListener) v.elementAt(i) ).pulsado();

				if (evento.usuario.equals(DConector.Dusuario))
				{
					v = getLUJButtonListeners();
					for (i = 0; i < v.size(); i++)
						( (LJButtonListener) v.elementAt(i) ).pulsado();
				}

			}
		if (evento.tipo.intValue() == DJButtonEvent.SOLTADO.intValue())
			if (getModel().isPressed())
			{// Esta presionado
				getModel().setArmed(false);
				getModel().setPressed(false);

				Vector v = getLJButtonListeners();
				for (i = 0; i < v.size(); i++)
					( (LJButtonListener) v.elementAt(i) ).soltado();

				if (evento.usuario.equals(DConector.Dusuario))
				{
					v = getLUJButtonListeners();
					for (i = 0; i < v.size(); i++)
						( (LJButtonListener) v.elementAt(i) ).soltado();
				}
			}
	}

	/**
	 * Sincroniza las instancias del componente
	 */
	public void sincronizar()
	{
		DJButtonEvent evento = new DJButtonEvent();

		evento.origen = new Integer(1); // Aplicacion
		evento.destino = new Integer(0); // Coordinador
		evento.componente = new Integer(DID.intValue());
		evento.tipo = new Integer(DJButtonEvent.SINCRONIZACION.intValue());
		evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());
		colaEnvio.nuevoEvento(evento);
	}

	/**
	 * Obtiene el nivel de permisos del componente
	 * 
	 * @return Nivel de permisos del componente
	 */
	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	/**
	 * Asigna un nivel de permisos a un componente y realiza los cambios
	 * oportunos en el
	 * 
	 * @param nivel
	 *            Nivel de permisos a asignar
	 */
	public void setNivelPermisos(int nivel)
	{
		/*
		 * if(!isEnabled()){ setEnabled(true); }
		 */
		nivelPermisos = nivel;
		if (nivel >= 20)
			setForeground(Color.BLACK);
		else setForeground(Color.GRAY);
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
	 * @return
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
	 * Crea la hebra de procesamiento de eventos
	 * 
	 * @return Hebra procesadora de eventos. En este caso devuelve null siempre.
	 */
	public HebraProcesadoraBase crearHebraProcesadora()
	{
		// return new HebraProcesadora(this);
		return null;
	}

	/**
	 * Permite escuchar los eventos producidos
	 */
	@SuppressWarnings( "unused" )
	private class Listener implements DJButtonListener
	{

		public void presionado(DJButtonEvent evento)
		{
			enviarEvento(evento, DJButtonEvent.PRESIONADO.intValue());
		}

		public void soltado(DJButtonEvent evento)
		{
			enviarEvento(evento, DJButtonEvent.SOLTADO.intValue());
		}

		private void enviarEvento(DJButtonEvent evento, int tipo)
		{
			if (nivelPermisos >= 20) try
			{
				// System.out.println("Evento enviado");
				evento.componente = new Integer(DID.intValue());
				evento.origen = new Integer(1); // Aplicacion
				evento.destino = new Integer(0); // Coordinador
				evento.tipo = new Integer(tipo);
				evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

				colaEnvio.nuevoEvento(evento);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Esta hebra se encarga de realizar las operaciones pertinentes con los
	 * eventos que se reciben
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
			DJButtonEvent evento = null;
			DJButtonEvent saux = null;
			DJButtonEvent respSincr = null;
			Vector vaux = new Vector();

			DEvent[] eventos = obtenerEventosColaRecepcion();
			int numEventos = eventos.length;
			int i = 0;

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

			setEnabled(true);

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				getModel().setPressed(respSincr.pulsado.booleanValue());
				getModel().setArmed(respSincr.pulsado.booleanValue());
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = vaux.size();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJButtonEvent) vaux.elementAt(j);
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) procesarEvento(saux);
			}

			while (true)
			{
				evento = (DJButtonEvent) leerSiguienteEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (nivelPermisos >= 10)
				{
					/*
					 * System.out.println("HebraProcesadora(" + DID + "):
					 * Recibido evento del tipo " + evento.tipo);
					 */
					if (( evento.tipo.intValue() == DJButtonEvent.SINCRONIZACION
							.intValue() )
							&& !evento.usuario.equals(DConector.Dusuario))
					{
						DJButtonEvent aux = new DJButtonEvent();
						aux.origen = new Integer(1); // Aplicacion
						aux.destino = new Integer(0); // Coordinador
						aux.componente = new Integer(DID.intValue());
						aux.tipo = new Integer(
								DJButtonEvent.RESPUESTA_SINCRONIZACION
										.intValue());
						aux.ultimoProcesado = new Integer(ultimoProcesado
								.intValue());
						aux.pulsado = new Boolean(getModel().isPressed());
						colaEnvio.nuevoEvento(aux);
					}
					if (evento.tipo.intValue() == DJButtonEvent.PRESIONADO
							.intValue()) if (!getModel().isPressed())
					{// No esta presionado
							getModel().setPressed(true);
							getModel().setArmed(true);

							Vector v = getLJButtonListeners();
							for (i = 0; i < v.size(); i++)
								( (LJButtonListener) v.elementAt(i) ).pulsado();

						}
					if (evento.tipo.intValue() == DJButtonEvent.SOLTADO
							.intValue()) if (getModel().isPressed())
					{// Esta presionado
							getModel().setArmed(false);
							getModel().setPressed(false);

							Vector v = getLJButtonListeners();
							for (i = 0; i < v.size(); i++)
								( (LJButtonListener) v.elementAt(i) ).soltado();
						}
				}
			}
		}
	}

}
