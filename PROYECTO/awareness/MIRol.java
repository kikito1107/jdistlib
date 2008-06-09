package awareness;

import java.io.Serializable;

/**
 * Metainformacion de un rol
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
@SuppressWarnings( "serial" )
public class MIRol extends MIUsuario implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param nombreRol
	 *            Nombre del rol
	 */
	public MIRol( String nombreRol )
	{
		super(nombreRol, new String(""));
	}

	/**
	 * Obtiene el nombre del rol
	 * 
	 * @return Nombre del rol
	 */
	public String getNombreRol()
	{
		return super.getNombreUsuario();
	}

	@Override
	public String toString()
	{
		String cadena = new String("[ ++++++++++ ROL +++++++++]\n");
		cadena = new String(cadena + super.toString());
		cadena = new String(cadena + "[ ++++++++++++++++++++++++]\n");
		return cadena;
	}

}
