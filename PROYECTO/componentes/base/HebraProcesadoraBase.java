package componentes.base;

import Deventos.ColaEventos;
import Deventos.DEvent;

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

public class HebraProcesadoraBase implements Runnable
{
	private ColaEventos colaEnvio = null;

	private ColaEventos colaRecepcion = null;

	private DComponente dc = null;

	public HebraProcesadoraBase( DComponente dc )
	{
		this.dc = dc;
		this.colaEnvio = dc.obtenerColaEnvio();
		this.colaRecepcion = dc.obtenerColaRecepcion();
	}

	public void run()
	{

	}

	public DEvent[] obtenerEventosColaRecepcion()
	{
		int numEventos = colaRecepcion.tamanio();
		DEvent[] eventos = new DEvent[numEventos];

		for (int i = 0; i < numEventos; i++)
			eventos[i] = colaRecepcion.extraerEvento();

		return eventos;
	}

	public DEvent leerSiguienteEvento()
	{
		return colaRecepcion.extraerEvento();
	}

	public void iniciarHebra()
	{
		Thread t = new Thread(this);
		t.start();
	}

}
