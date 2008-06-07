package componentes.base;

import Deventos.ColaEventos;
import Deventos.DEvent;

/**
 * Implementacion de la hebra procesadora de eventos
 * 
 * @author Juan Antonio Iba–ez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class HebraProcesadoraBase implements Runnable
{
	@SuppressWarnings( "unused" )
	private ColaEventos colaEnvio = null;

	private ColaEventos colaRecepcion = null;

	@SuppressWarnings( "unused" )
	private DComponente dc = null;

	/**
	 * Constructor
	 * 
	 * @param dc
	 *            Componente distribuido al que asociaremos la hebra
	 */
	public HebraProcesadoraBase( DComponente dc )
	{
		this.dc = dc;
		this.colaEnvio = dc.obtenerColaEnvio();
		this.colaRecepcion = dc.obtenerColaRecepcion();
	}

	/**
	 * Ejecucion de la hebra. No hace nada, con lo que se debera implementar por
	 * parte del programador
	 */
	public void run()
	{

	}

	/**
	 * Obtiene los eventos de la cola de recepcion
	 * 
	 * @return Array de eventos de la cola de recepcion
	 */
	public DEvent[] obtenerEventosColaRecepcion()
	{
		int numEventos = colaRecepcion.tamanio();
		DEvent[] eventos = new DEvent[numEventos];

		for (int i = 0; i < numEventos; i++)
			eventos[i] = colaRecepcion.extraerEvento();

		return eventos;
	}

	/**
	 * Extrae de la cola de recepcion de eventos el siguiente evento disponible
	 * 
	 * @return Evento leido de la cola de recepcion
	 */
	public DEvent leerSiguienteEvento()
	{
		return colaRecepcion.extraerEvento();
	}

	/**
	 * Inicia la ejecucion de la hebra
	 */
	public void iniciarHebra()
	{
		Thread t = new Thread(this);
		t.start();
	}

}
