package metainformacion;

import java.io.Serializable;

/**
 * Permite almacenar informacion sobre un componente de la aplicacion. Guardamos
 * informacion sobre: Nombre del componente y permiso del componente. El permiso
 * solo tiene sentido en relacion con un usuario
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class MIComponente implements Serializable
{
	private static final long serialVersionUID = -8212385220511015348L;

	private String nombre = null;

	private int permiso = -1;

	private int identificador;

	/**
	 * Constructor
	 * 
	 * @param nombre
	 *            Nombre del componente
	 * @param permiso
	 *            Nivel de permisos
	 */
	public MIComponente( String nombre, int permiso )
	{
		this.nombre = nombre;
		this.permiso = permiso;
	}

	/**
	 * Obtiene el identificador del componente
	 * 
	 * @return Identificador del componente
	 */
	public int getIdentificador()
	{
		return identificador;
	}

	/**
	 * Asigna un identificador al componente
	 * 
	 * @param identificador
	 *            Identificador del componente
	 */
	public void setIdentificador(int identificador)
	{
		this.identificador = identificador;
	}

	/**
	 * Obtiene el nombre del componente
	 * 
	 * @return Nombre del componente
	 */
	public String getNombreComponente()
	{
		return nombre;
	}

	/**
	 * Obtiene el nivel de permisos del componente
	 * 
	 * @return Nivel de permisos del componente
	 */
	public int permisoComponente()
	{
		return permiso;
	}

	/**
	 * Asigna el nivel de permisos del componente
	 * 
	 * @param permiso
	 *            Nivel de permisos del componente
	 */
	public void setPermisoComponente(int permiso)
	{
		this.permiso = permiso;
	}

	@Override
	public String toString()
	{
		return nombre;
	}

}
