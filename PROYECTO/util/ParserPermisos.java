package util;

import aplicacion.fisica.documentos.MIFichero;

public class ParserPermisos
{
	public ParserPermisos()
	{

	}

	/**
	 * Compruba si un usuario tiene permiso de lectura o no de un fichero
	 * 
	 * @param idUsuario
	 *            id del usuario
	 * @param idFichero
	 *            id del fichero
	 * @return true si el usuario puede acceder al fichero y false en caso
	 *         contrario
	 */
	public static boolean comprobarPermisoLectura(MIFichero f, String u,
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

			// 1) El usuario es due–o del fichero
			if (u.equals(nombreUsuarioFichero))
			{
				/*if (permisos.charAt(0) == 'r')
					res = true;
				else res = false;*/ res = true;
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
	 * Compruba si un usuario tiene permiso de escritura o no de un fichero
	 * 
	 * @param idUsuario
	 *            id del usuario
	 * @param idFichero
	 *            id del fichero
	 * @return true si el usuario puede acceder al fichero y false en caso
	 *         contrario
	 */
	public static boolean comprobarPermisoEscritura(MIFichero f, String u,
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

			// 1) El usuario es due–o del fichero
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
