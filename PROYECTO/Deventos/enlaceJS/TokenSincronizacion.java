package Deventos.enlaceJS;

import net.jini.core.entry.Entry;

/**
 * Token usado para hacer que no pueda haber 2 usuarios simultaneos
 * sincronizando sus componentes
 */

public class TokenSincronizacion implements Entry
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2869940658610568333L;

	String aplicacion = null;

	public TokenSincronizacion()
	{

	}

	public TokenSincronizacion( String aplicacion )
	{
		this.aplicacion = aplicacion;
	}

}
