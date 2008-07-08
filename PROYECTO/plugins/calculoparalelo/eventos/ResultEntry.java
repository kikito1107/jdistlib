package plugins.calculoparalelo.eventos;

import Deventos.DEvent;

/**
 * Entrada para el envio de resultados a JavaSpace
 * @author carlosrodriguezdominguez
 */
public class ResultEntry extends DEvent
{
	private static final long serialVersionUID = 8515912533437337084L;
	
	/**
	 * Mensaje de retorno
	 */
	public String mensaje = null;
}
