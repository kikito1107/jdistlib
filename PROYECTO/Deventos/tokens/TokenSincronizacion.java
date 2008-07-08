package Deventos.tokens;

import net.jini.core.entry.Entry;

/**
 * Token usado para hacer que no pueda haber 2 usuarios simultaneos
 * sincronizando sus componentes
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class TokenSincronizacion implements Entry
{
	private static final long serialVersionUID = -2869940658610568333L;

	/**
	 * Nombre de la aplicacion
	 */
	public String aplicacion = null;

	/**
	 * Constructor por defecto
	 */
	public TokenSincronizacion()
	{

	}

	/**
	 * Constructor con parametros
	 * @param aplicacion Nombre de la aplicacion
	 */
	public TokenSincronizacion( String aplicacion )
	{
		this.aplicacion = aplicacion;
	}

}
