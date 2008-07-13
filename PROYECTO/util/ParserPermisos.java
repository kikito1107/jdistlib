package util;

import fisica.documentos.MIDocumento;

/**
 * Permite comprobar los permisos de un documento
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class ParserPermisos
{
	/**
	 * Constructor
	 */
	public ParserPermisos()
	{
	}

	/**
	 * Comprueba si un usuario tiene permiso de lectura o no de un fichero
	 * 
	 * @param f
	 *            Metainformacion del fichero
	 * @param u
	 *            Nombre del usuario
	 * @param r
	 *            Rol que desempeña el usuario
	 * @return True si el usuario puede leer el fichero. False en caso contrario
	 */
	public static boolean comprobarPermisoLectura(MIDocumento f, String u,
			String r)
	{
		boolean res;

		if (( u == null ) || ( r == null ))
			res = false;

		else if (!f.getNombre().equals("/"))
		{

			String permisos = f.getPermisos();
			String nombreUsuarioFichero = f.getUsuario().getNombreUsuario();
			String nombreRolFichero = f.getRol().getNombreRol();

			// 1) El usuario es dueño del fichero
			if (u.equals(nombreUsuarioFichero))
			{
				
				 if (permisos.charAt(0) == 'r') res = true; 
				 else if (r.equals(nombreRolFichero)) res = true;
				 else res = false;
			}

			// 2) el usuario pertenece al rol que le da acceso
			else if (r.equals(nombreRolFichero))
			{

				if (permisos.charAt(2) == 'r')
					res = true;
				else res = false;
			}
			else if (permisos.charAt(4) == 'r')
				res = true;
			else res = false;
		}
		else res = true;

		return res;
	}

	/**
	 * Comprueba si un usuario tiene permiso de escritura o no de un fichero
	 * 
	 * @param f
	 *            Metainformacion del fichero
	 * @param u
	 *            Nombre del usuario
	 * @param r
	 *            Rol que desempeña el usuario
	 * @return True si el usuario puede escribir en el fichero. False en caso
	 *         contrario
	 */
	public static boolean comprobarPermisoEscritura(MIDocumento f, String u,
			String r)
	{
		boolean res;

		if (( u == null ) || ( r == null ))
			res = false;

		else if (!f.getNombre().equals("/"))
		{
			String permisos = f.getPermisos();
			String nombreUsuarioFichero = f.getUsuario().getNombreUsuario();
			String nombreRolFichero = f.getRol().getNombreRol();

			// 1) El usuario es dueño del fichero
			if (u.equals(nombreUsuarioFichero))
			{
				if (permisos.charAt(1) == 'w')
					res = true;
				else res = false;
			}

			// 2) el usuario pertenece al rol que le da acceso
			else if (r.equals(nombreRolFichero))
			{
				if (permisos.charAt(3) == 'w')
					res = true;
				else res = false;
			}
			else if (permisos.charAt(5) == 'w')
				res = true;
			else res = false;
		}
		else res = true;

		return res;
	}
}
