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
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class DJButton extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String uiClassID = "DButtonUI";

	private Vector djbuttonlisteners = new Vector(5);

	private Vector ljbuttonlisteners = new Vector(5);

	private Vector ujbuttonlisteners = new Vector(5);

	private Integer DID = null;

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private Integer ultimoProcesado = new Integer(-1);

	private int nivelPermisos = 10;

	private HebraProcesadoraBase hebraProcesadora = null;

	public DJButton()
	{
		this.nombre = null;
		extrasConstructor();
	}

	public DJButton( String p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	public DJButton( Action p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	public DJButton( Icon p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	public DJButton( String p0, Icon p1 )
	{
		super(p0, p1);
		this.nombre = null;
		extrasConstructor();
	}

	private void extrasConstructor()
	{
		/*
		 * addDJButtonListener(new Listener()); DID = new
		 * Integer(DConector.alta(this)); colaEnvio =
		 * DConector.getColaEventos(); setForeground(Color.GRAY);
		 */
	}

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

	public void addDJButtonListener(DJButtonListener listener)
	{
		djbuttonlisteners.add(listener);
	}

	public void addLJButtonListener(LJButtonListener listener)
	{
		ljbuttonlisteners.add(listener);
	}

	public void addLUJButtonListener(LJButtonListener listener)
	{
		ujbuttonlisteners.add(listener);
	}

	public Vector getDJButtonListeners()
	{
		return djbuttonlisteners;
	}

	public Vector getLJButtonListeners()
	{
		return ljbuttonlisteners;
	}

	public Vector getLUJButtonListeners()
	{
		return ujbuttonlisteners;
	}

	public void removeDJButtonListeners()
	{
		djbuttonlisteners.removeAllElements();
	}

	public void removeLJButtonListeners()
	{
		ljbuttonlisteners.removeAllElements();
	}

	public void removeLUJButtonListeners()
	{
		ujbuttonlisteners.removeAllElements();
	}

	public void activar()
	{
		setEnabled(true);
	}

	public void desactivar()
	{
		setEnabled(false);
	}

	public void iniciarHebraProcesadora()
	{
		hebraProcesadora = crearHebraProcesadora();
		hebraProcesadora.iniciarHebra();
	}

	public DJButtonEvent obtenerInfoEstado()
	{
		DJButtonEvent evento = new DJButtonEvent();
		evento.pulsado = new Boolean(getModel().isPressed());
		return evento;
	}

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

	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

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

	public Integer getID()
	{
		return DID;
	}

	public String getNombre()
	{
		return nombre;
	}

	public ColaEventos obtenerColaRecepcion()
	{
		return colaRecepcion;
	}

	public ColaEventos obtenerColaEnvio()
	{
		return colaEnvio;
	}

	public HebraProcesadoraBase crearHebraProcesadora()
	{
		// return new HebraProcesadora(this);
		return null;
	}

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
