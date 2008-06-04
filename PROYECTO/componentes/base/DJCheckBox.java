package componentes.base;

import java.awt.Color;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJCheckBoxEvent;
import Deventos.enlaceJS.DConector;

import componentes.listeners.DJCheckBoxListener;
import componentes.listeners.LJCheckBoxListener;

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

public class DJCheckBox extends JCheckBox
{
	private static final long serialVersionUID = 1L;

	private static final String uiClassID = "DCheckBoxUI";

	private Vector<Object> djcheckboxlisteners = new Vector<Object>(5);

	private Vector<Object> ljcheckboxlisteners = new Vector<Object>(5);

	private Vector<Object> lujcheckboxlisteners = new Vector<Object>(5);

	private Integer DID = null;

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private int nivelPermisos = 10;

	private Integer ultimoProcesado = new Integer(-1);

	public DJCheckBox()
	{
		super();
		this.nombre = null;
		extrasConstructor();
	}

	public DJCheckBox( String p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	public DJCheckBox( String p0, boolean p1 )
	{
		super(p0, p1);
		this.nombre = null;
		extrasConstructor();
	}

	public DJCheckBox( Action p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	public DJCheckBox( Icon p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	public DJCheckBox( Icon p0, boolean p1 )
	{
		super(p0, p1);
		this.nombre = null;
		extrasConstructor();
	}

	public DJCheckBox( String p0, Icon p1 )
	{
		super(p0, p1);
		this.nombre = null;
		extrasConstructor();
	}

	public DJCheckBox( String p0, Icon p1, boolean p2 )
	{
		super(p0, p1, p2);
		this.nombre = null;
		extrasConstructor();
	}

	private void extrasConstructor()
	{
		/*
		 * addDJCheckBoxListener(new Listener()); DID = new
		 * Integer(DConector.alta(this)); colaEnvio =
		 * DConector.getColaEventos(); setForeground(Color.GRAY);
		 */
	}

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

	public void addDJCheckBoxListener(DJCheckBoxListener listener)
	{
		djcheckboxlisteners.add(listener);
	}

	public void addLJCheckBoxListener(LJCheckBoxListener listener)
	{
		ljcheckboxlisteners.add(listener);
	}

	public void addLUJCheckBoxListener(LJCheckBoxListener listener)
	{
		lujcheckboxlisteners.add(listener);
	}

	public void removeDJCheckBoxListeners()
	{
		djcheckboxlisteners.removeAllElements();
	}

	public void removeLJCheckBoxListeners()
	{
		ljcheckboxlisteners.removeAllElements();
	}

	public void removeLUJCheckBoxListeners()
	{
		lujcheckboxlisteners.removeAllElements();
	}

	public Vector<Object> getDJCheckBoxListeners()
	{
		return djcheckboxlisteners;
	}

	public Vector<Object> getLJCheckBoxListeners()
	{
		return ljcheckboxlisteners;
	}

	public Vector<Object> getLUJCheckBoxListeners()
	{
		return lujcheckboxlisteners;
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
		Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
		t.start();
	}

	public DJCheckBoxEvent obtenerInfoEstado()
	{
		DJCheckBoxEvent evento = new DJCheckBoxEvent();
		evento.presionada = new Boolean(getModel().isPressed());
		evento.marcada = new Boolean(isSelected());
		return evento;
	}

	public void procesarEvento(DEvent evento)
	{
		if (evento.tipo.intValue() == DJCheckBoxEvent.PRESIONADO.intValue())
			if (!getModel().isPressed())
			{
				getModel().setArmed(true);
				getModel().setPressed(true);
			}
		if (evento.tipo.intValue() == DJCheckBoxEvent.SOLTADO.intValue())
			if (getModel().isPressed())
			{
				getModel().setArmed(false);
				getModel().setPressed(false);
				setSelected(!isSelected());

				Vector<?> v = getLJCheckBoxListeners();
				for (int i = 0; i < v.size(); i++)
					( (LJCheckBoxListener) v.elementAt(i) )
							.cambioEstado(isSelected());
				if (evento.usuario.equals(DConector.Dusuario))
				{
					v = getLUJCheckBoxListeners();
					for (int i = 0; i < v.size(); i++)
						( (LJCheckBoxListener) v.elementAt(i) )
								.cambioEstado(isSelected());
				}

			}
	}

	public void sincronizar()
	{
		DJCheckBoxEvent evento = new DJCheckBoxEvent();

		evento.origen = new Integer(1); // Aplicacion
		evento.destino = new Integer(0); // Coordinador
		evento.componente = new Integer(DID.intValue());
		evento.tipo = new Integer(DJCheckBoxEvent.SINCRONIZACION.intValue());
		evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());
		colaEnvio.nuevoEvento(evento);
	}

	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	public void setNivelPermisos(int nivel)
	{
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
		return null;
	}

	private class Listener implements DJCheckBoxListener
	{

		public void presionado(DJCheckBoxEvent evento)
		{
			enviarEvento(evento, DJCheckBoxEvent.PRESIONADO.intValue());
		}

		public void soltado(DJCheckBoxEvent evento)
		{
			enviarEvento(evento, DJCheckBoxEvent.SOLTADO.intValue());
		}

		private void enviarEvento(DJCheckBoxEvent evento, int tipo)
		{
			try
			{
				if (nivelPermisos >= 20)
				{
					evento.componente = new Integer(DID.intValue());
					evento.origen = new Integer(1); // Aplicacion
					evento.destino = new Integer(0); // Coordinador
					evento.tipo = new Integer(tipo);
					evento.ultimoProcesado = new Integer(ultimoProcesado
							.intValue());

					colaEnvio.nuevoEvento(evento);
				}
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
	class HebraProcesadora implements Runnable
	{

		ColaEventos cola = null;

		DJCheckBox checkbox = null;

		HebraProcesadora( ColaEventos cola, DJCheckBox checkbox )
		{
			this.cola = cola;
			this.checkbox = checkbox;
		}

		public void run()
		{
			DJCheckBoxEvent evento = null;
			DJCheckBoxEvent saux = null;
			DJCheckBoxEvent respSincr = null;
			ColaEventos colaAux = new ColaEventos();

			int numEventos = colaRecepcion.tamanio(); // Para evitar quedarnos
			// bloqueados
			int i = 0;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJCheckBoxEvent) colaRecepcion.extraerEvento();
				if (( respSincr == null )
						&& ( saux.tipo.intValue() == DJCheckBoxEvent.RESPUESTA_SINCRONIZACION
								.intValue() ))
					respSincr = saux;
				else colaAux.nuevoEvento(saux);
			}

			setEnabled(true);

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				checkbox.setSelected(respSincr.marcada.booleanValue());
				checkbox.getModel().setPressed(
						respSincr.presionada.booleanValue());
				checkbox.getModel().setArmed(
						respSincr.presionada.booleanValue());
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = colaAux.tamanio();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJCheckBoxEvent) colaAux.extraerEvento();
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) colaRecepcion.nuevoEvento(saux);
			}

			while (true)
			{
				evento = (DJCheckBoxEvent) cola.extraerEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (nivelPermisos >= 10)
				{
					/*
					 * System.out.println("HebraProcesadora(" + DID + "):
					 * Procesado evento " + evento);
					 */
					if (( evento.tipo.intValue() == DJCheckBoxEvent.SINCRONIZACION
							.intValue() )
							&& !evento.usuario.equals(DConector.Dusuario))
					{
						DJCheckBoxEvent aux = new DJCheckBoxEvent();
						aux.origen = new Integer(1); // Aplicacion
						aux.destino = new Integer(0); // Coordinador
						aux.componente = new Integer(DID.intValue());
						aux.tipo = new Integer(
								DJCheckBoxEvent.RESPUESTA_SINCRONIZACION
										.intValue());
						aux.ultimoProcesado = new Integer(ultimoProcesado
								.intValue());
						aux.presionada = new Boolean(checkbox.getModel()
								.isPressed());
						aux.marcada = new Boolean(checkbox.isSelected());
						colaEnvio.nuevoEvento(aux);
					}
					if (evento.tipo.intValue() == DJCheckBoxEvent.PRESIONADO
							.intValue()) if (!checkbox.getModel().isPressed())
					{
						checkbox.getModel().setArmed(true);
						checkbox.getModel().setPressed(true);
					}
					if (evento.tipo.intValue() == DJCheckBoxEvent.SOLTADO
							.intValue())
						if (checkbox.getModel().isPressed())
						{
							checkbox.getModel().setArmed(false);
							checkbox.getModel().setPressed(false);
							checkbox.setSelected(!checkbox.isSelected());

							Vector<?> v = getLJCheckBoxListeners();
							for (i = 0; i < v.size(); i++)
								( (LJCheckBoxListener) v.elementAt(i) )
										.cambioEstado(checkbox.isSelected());
							if (evento.usuario.equals(DConector.Dusuario))
							{
								v = getLUJCheckBoxListeners();
								for (i = 0; i < v.size(); i++)
									( (LJCheckBoxListener) v.elementAt(i) )
											.cambioEstado(checkbox.isSelected());
							}

						}
				}
			}
		}
	}

}
