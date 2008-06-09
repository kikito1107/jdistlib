package metainformacion;

import java.io.Serializable;

/**
 * Permite almacenar informacion sobre un componente de la
 * aplicacion. Guardamos informacion sobre: Nombre del componente y permiso del
 * componente. El permiso solo tiene sentido en relacion con un usuario
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

	public int getIdentificador()
	{
		return identificador;
	}

	public void setIdentificador(int identificador)
	{
		this.identificador = identificador;
	}

	public MIComponente( String nombre, int permiso )
	{
		this.nombre = nombre;
		this.permiso = permiso;
	}

	/**
	 * Obtenemos el nombre del componente
	 * 
	 * @return String Nombre del componente
	 */
	public String getNombreComponente()
	{
		return nombre;
	}

	/**
	 * Obtenemos el permiso del componente
	 * 
	 * @return int Permiso del componente
	 */
	public int permisoComponente()
	{
		return permiso;
	}

	/**
	 * Establecemos el permiso del componente
	 * 
	 * @param permiso
	 *            int Nuevo permiso del componente
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
