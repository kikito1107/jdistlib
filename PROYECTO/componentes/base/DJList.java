package componentes.base;

import java.awt.Color;
import java.util.Vector;

import util.ElementoLista;
import util.ListaElementos;
import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJListEvent;
import Deventos.DMIEvent;
import Deventos.enlaceJS.DConector;

import componentes.listeners.DJListListener;
import componentes.listeners.LJListListener;

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

public class DJList extends ListaElementos
{

	private static final long serialVersionUID = 1L;

	private static final String uiClassID = "DListUI";

	private Vector<Object> djlistlisteners = new Vector<Object>(5);

	private Vector<Object> ljlistlisteners = new Vector<Object>(5);

	private Vector<Object> lujlistlisteners = new Vector<Object>(5);

	private Integer DID = new Integer(-1);

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private Integer ultimoProcesado = new Integer(-1);

	private int nivelPermisos = 10;

	public DJList()
	{
		this.nombre = null;
	}

	public DJList( String[] p0 )
	{
		super();
		for (String element : p0)
			aniadirElemento(element);
		this.nombre = null;
	}

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	/*
	 * public int obtenerPosicionElemento(String elemento){ return
	 * obtenerPosicionElemento(elemento); }
	 */

	@Override
	public void aniadirElemento(String elemento)
	{
		super.aniadirElemento(elemento);
	}

	public void addDJListListener(DJListListener listener)
	{
		djlistlisteners.add(listener);
	}

	public void addLJListListener(LJListListener listener)
	{
		ljlistlisteners.add(listener);
	}

	public void addLUJListListener(LJListListener listener)
	{
		lujlistlisteners.add(listener);
	}

	public void removeDJListListeners()
	{
		djlistlisteners.removeAllElements();
	}

	public void removeLJListListeners()
	{
		ljlistlisteners.removeAllElements();
	}

	public void removeLUJListListeners()
	{
		lujlistlisteners.removeAllElements();
	}

	public Vector<Object> getDJListListeners()
	{
		return djlistlisteners;
	}

	public Vector<Object> getLJListListeners()
	{
		return ljlistlisteners;
	}

	public Vector<Object> getLUJListListeners()
	{
		return lujlistlisteners;
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

	@SuppressWarnings("unchecked")
	public DJListEvent obtenerInfoEstado()
	{
		DJListEvent evento = new DJListEvent();
		evento.posicion = new Integer(getSelectedIndex());
		Vector<?> v = obtenerElementos();
		evento.elementos = new Vector<Object>();

		ElementoLista el = null;
		ElementoLista aux = null;

		for (int i = 0; i < v.size(); i++)
		{
			el = (ElementoLista) v.elementAt(i);
			aux = new ElementoLista(el.imagen, el.texto);
			evento.elementos.add(aux);
		}

		return evento;
	}

	public void procesarEvento(DEvent evento)
	{
		DJListEvent ev = (DJListEvent) evento;
		ultimoProcesado = new Integer(evento.contador.intValue());
		if (evento.tipo.intValue() == DJListEvent.CAMBIO_POSICION.intValue())
		{
			setSelectedIndex(ev.posicion.intValue());
			ensureIndexIsVisible(ev.posicion.intValue());

			Vector<Object> v = getLJListListeners();
			for (int i = 0; i < v.size(); i++)
				( (LJListListener) v.elementAt(i) ).cambioPosicion(ev.posicion
						.intValue());
			if (evento.usuario.equals(DConector.Dusuario))
			{
				v = getLUJListListeners();
				for (int i = 0; i < v.size(); i++)
					( (LJListListener) v.elementAt(i) )
							.cambioPosicion(ev.posicion.intValue());
			}

		}
	}

	public void procesarMetaInformacion(DMIEvent evento)
	{

	}

	public void sincronizar()
	{
		DJListEvent evento = new DJListEvent();

		evento.origen = new Integer(1); // Aplicacion
		evento.destino = new Integer(0); // Coordinador
		evento.componente = new Integer(DID.intValue());
		evento.tipo = new Integer(DJListEvent.SINCRONIZACION.intValue());
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

	@SuppressWarnings("unused")
	private class Listener implements DJListListener
	{
		private int ultimaEnviada = -1;

		public void cambioPosicion(DJListEvent evento)
		{
			if (nivelPermisos >= 20) try
			{
				if (evento.posicion.intValue() != ultimaEnviada)
				{ // Para evitar mensajs innecesarios
					ultimaEnviada = evento.posicion.intValue();
					evento.origen = new Integer(1); // Aplicacion
					evento.destino = new Integer(0); // Coordinador
					evento.componente = new Integer(DID.intValue());
					evento.tipo = new Integer(DJListEvent.CAMBIO_POSICION
							.intValue());
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

		DJList lista = null;

		HebraProcesadora( ColaEventos cola, DJList lista )
		{
			this.cola = cola;
			this.lista = lista;
		}

		public void run()
		{
			DJListEvent evento = null;
			DJListEvent saux = null;
			DJListEvent respSincr = null;
			ColaEventos colaAux = new ColaEventos();

			int numEventos = colaRecepcion.tamanio(); // Para evitar quedarnos
			// bloqueados
			int i = 0;
			// int posicion = -1;
			// boolean encontradaRespuestaSincronizacion = false;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJListEvent) colaRecepcion.extraerEvento();
				if (( respSincr == null )
						&& ( saux.tipo.intValue() == DJListEvent.RESPUESTA_SINCRONIZACION
								.intValue() ))
					respSincr = saux;
				else colaAux.nuevoEvento(saux);
			}

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				lista.setSelectedIndex(respSincr.posicion.intValue());
				lista.ensureIndexIsVisible(respSincr.posicion.intValue());
			}
			else
			{
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = colaAux.tamanio();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJListEvent) colaAux.extraerEvento();
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) colaRecepcion.nuevoEvento(saux);
			}

			setEnabled(true);

			while (true)
			{
				evento = (DJListEvent) cola.extraerEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (nivelPermisos >= 10)
				{
					/*
					 * System.out.println("HebraProcesadora(" + DID + "):
					 * Recibido evento del tipo " + evento.tipo);
					 */
					if (( evento.tipo.intValue() == DJListEvent.SINCRONIZACION
							.intValue() )
							&& !evento.usuario.equals(DConector.Dusuario))
					{
						DJListEvent aux = new DJListEvent();
						aux.origen = new Integer(1); // Aplicacion
						aux.destino = new Integer(0); // Coordinador
						aux.componente = new Integer(DID.intValue());
						aux.tipo = new Integer(
								DJListEvent.RESPUESTA_SINCRONIZACION.intValue());
						aux.ultimoProcesado = new Integer(ultimoProcesado
								.intValue());
						aux.posicion = new Integer(lista.getSelectedIndex());
						colaEnvio.nuevoEvento(aux);
					}
					if (evento.tipo.intValue() == DJListEvent.CAMBIO_POSICION
							.intValue())
					{
						lista.setSelectedIndex(evento.posicion.intValue());
						lista.ensureIndexIsVisible(evento.posicion.intValue());

						Vector<Object> v = getLJListListeners();
						for (i = 0; i < v.size(); i++)
							( (LJListListener) v.elementAt(i) )
									.cambioPosicion(evento.posicion.intValue());

						if (evento.usuario.equals(DConector.Dusuario))
						{
							v = getLUJListListeners();
							for (i = 0; i < v.size(); i++)
								( (LJListListener) v.elementAt(i) )
										.cambioPosicion(evento.posicion
												.intValue());
						}

					}
				}
			}
		}

	}

}
