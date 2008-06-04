package aplicacion.fisica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import metainformacion.MIRol;
import metainformacion.MIUsuario;
import aplicacion.fisica.documentos.MIDocumento;

/**
 * Permite acceder a la base de datos de ficheros y consultar, ampliar, modificar y/o eliminar los datos almacenados en ella
 * @author Carlos Rodriguez Dominguez, Ana Belen Pelegrina Ortiz
 * 
 */
public class GestorFicherosBD
{
	ConectorBDFicheros conexion = null;

	/**
	 * Contructor de la clase. Abre la conexion con la base de datos
	 */
	public GestorFicherosBD()
	{
		conexion = new ConectorBDFicheros();
		conexion.abrir();
	}

	/**
	 * Permite acceder al primer nivel del sistema de ficheros
	 * 
	 * @return un vector de FicheroBD con los ficheros y directorios del primer
	 *         nivel
	 */
	public Vector<MIDocumento> recuperar()
	{
		try
		{
			// coger la raiz
			ResultSet rs = conexion
					.select("select * from fichero where padre is NULL");

			// avanzamos
			rs.next();

			// recuperamos el contenido del directorio / (junto con el propio /)
			Vector<MIDocumento> dir = recuperarDirectorio(rs.getInt("id_fichero"));

			// cerramos la conexion
			rs.close();

			// devolvemos el vector con los ficheros
			return dir;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Permite recuperar el contenido de un directorio (incluido el propio
	 * directorio)
	 * 
	 * @param id
	 *            identificador del directorio
	 * @return un vector con los ficheros contenidos en el directorio
	 */
	public Vector<MIDocumento> recuperarDirectorio(int id)
	{
		try
		{
			// creamos el vector de ficheros
			Vector<MIDocumento> res = new Vector<MIDocumento>();

			// seleccionamos todos los ficheros cuyo padre sea el directorio en
			// cuestion
			ResultSet rs = conexion
					.select("SELECT * FROM fichero WHERE padre='" + id + "'");

			// a�asdimos el propio directorio
			res.add(construirFicheroBD(id));

			// a�adimos cada uno de los ficheros de directorio
			while (rs.next())
				res.add(construirFicheroBD(rs));

			// cerramos la conexion
			rs.close();

			// devolvemos el vector con los ficheros
			return res;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Permite acceder al directorio padre de un fichero
	 * 
	 * @param id
	 *            id del directorio al que queremos acceder
	 * @return el directoiro padre del fichero. Si el directorio no tiene padre
	 *         ('/') devuelve null
	 */
	public MIDocumento obtenerPadre(int id)
	{
		try
		{
			// buscamos en la BD los datos del fichero en cuestion
			ResultSet rs = conexion
					.select("SELECT * FROM fichero WHERE id_fichero='" + id
							+ "'");
			rs.next();

			// obtenemos el directorio padre del fichero
			int id_padre = rs.getInt("padre");

			// si no es nulo
			if (!rs.wasNull())
			{
				// contruimos el fichero, cerramos la conexion y lo devolvemos
				MIDocumento f = construirFicheroBD(id_padre);
				rs.close();
				return f;
			}
			// si no tiene padre devolvemos null
			else
			{
				rs.close();
				return null;
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * DEvuelve un fichero a partir de su id
	 * 
	 * @param id
	 *            identificador del fichero
	 * @return el fichero, si no existe ning�n fichero con ese id devuelve null
	 */
	private MIDocumento construirFicheroBD(int id)
	{
		try
		{
			ResultSet rs = conexion
					.select("select * from fichero where id_fichero='" + id
							+ "'");
			rs.next();

			MIDocumento f = construirFicheroBD(rs);
			rs.close();
			return f;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Construye un objeto de la clase FicheroBD a partir de los datos de un
	 * ResultSet
	 * 
	 * @param rs
	 *            result set conteniendo los datos del fichero
	 * @return el objeto FicheroBD
	 */
	private MIDocumento construirFicheroBD(ResultSet rs)
	{
		try
		{
			int id = rs.getInt("id_fichero");
			String nombre = rs.getString("nombre");
			boolean es_directorio = rs.getBoolean("es_directorio");
			String permisos = rs.getString("permisos");
			String ruta_local = rs.getString("ruta_local");
			String tipo = rs.getString("tipo");
			int id_padre = rs.getInt("padre");

			// obtener el usuario y el rol
			int id_usuario = rs.getInt("usuario");

			MIUsuario mu = null;
			if (!rs.wasNull()) mu = obtenerUsuario(id_usuario);

			int id_rol = rs.getInt("rol");

			MIRol rol = null;
			if (!rs.wasNull()) rol = obtenerRol(id_rol);

			return new MIDocumento(id, nombre, es_directorio, permisos, mu, rol,
					id_padre, ruta_local, tipo);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Busca en la base de datos de Metainformacion a un usuario a traves de su
	 * ID
	 * 
	 * @param id
	 *            id del usario
	 * @return un objeto de la clase MIUsuario representando al usuario. Si no
	 *         se encuentra al usuario se devuelve null
	 */
	private MIUsuario obtenerUsuario(int id)
	{
		try
		{
			ConectorBD con = new ConectorBD();
			con.abrir();

			ResultSet rs = con
					.select("select * from usuario where id_usuario='" + id
							+ "'");
			rs.next();

			MIUsuario mu = new MIUsuario(rs.getString("nombre_usuario"), rs
					.getString("clave"));
			mu.setEsAdministrador(rs.getBoolean("administrador"));

			rs.close();
			con.cerrar();

			return mu;

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param f
	 */
	public MIDocumento insertarNuevoFichero(MIDocumento f)
	{
		try
		{

			if (f != null)
			{

				int isdir = f.esDirectorio() ? 1 : 0;
				int id_user = f.getUsuario().getIdentificador();
				int id_rol = f.getRol().getIdentificador();
				int id = conexion.generaId("Fichero", "id_fichero");
				f.setId(id);
				String parametros = id
						+ ", '" + f.getNombre() + "', " + isdir + ", '"
						+ f.getPermisos() + "'," + id_user + ", " + id_rol
						+ "," + f.getPadre() + ",'" + f.getRutaLocal()
						+ "', '" + f.getTipo() + "'";

				// insertamos la nueva tupla en fichero
				if (!conexion.insert("INSERT INTO fichero values(" + parametros
						+ ")")) 
					{
					 	System.err.println("ERROR en el insert");
					 	return null;
					}
				else
					return f;
			}
			else return null;

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Obtiene el rol a trav�s de su ID
	 * 
	 * @param id
	 *            id del rol
	 * @return un objeto de la clase MIRol correpondiente al id
	 */
	private MIRol obtenerRol(int id)
	{
		try
		{
			// conectamos con la base de datos de metainformacion
			ConectorBD con = new ConectorBD();
			con.abrir();

			// realizamos la consulta
			ResultSet rs = con.select("select * from rol where id_rol='" + id
					+ "'");
			rs.next();

			// creamos un nuevo objeto rol
			MIRol mu = new MIRol(rs.getString("nombre_rol"));

			// cerramos la conexion
			rs.close();
			con.cerrar();

			// devolvemos el rol
			return mu;

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public MIDocumento buscarFichero(int id)
	{
		MIDocumento f = new MIDocumento();
		
		f.setId(-100);

		ResultSet rs = conexion
				.select("SELECT * FROM fichero WHERE id_fichero=" + id);

		try
		{
			if (rs != null) {
				while (rs.next())
				{
					f.setId(id);
					f.setNombre(rs.getString("nombre"));
					f.setPadre(rs.getInt("padre"));
					f.setPermisos(rs.getString("permisos"));
					f.setRutaLocal(rs.getString("ruta_local"));
					f.setTipo(rs.getString("tipo"));
				}
	
				rs.close();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return f;
		}

		return f;
	}

	/**
	 * Busca un fichero en la BD por su path
	 * @param path path del fichero
	 * @return la metainformacion del fichero
	 */
	public MIDocumento buscarFicheroPath(String path)
	{
		MIDocumento f = new MIDocumento();
		
		f.setId(-100);

		ResultSet rs = conexion
				.select("SELECT * FROM fichero WHERE ruta_local='" + path + "'");

		try
		{
			if (rs != null) {
				
			
				while (rs.next())
				{
					f.setId(rs.getInt("id_fichero"));
					f.setNombre(rs.getString("nombre"));
					f.setPadre(rs.getInt("padre"));
					f.setPermisos(rs.getString("permisos"));
					f.setRutaLocal(rs.getString("ruta_local"));
					f.setTipo(rs.getString("tipo"));
					f.setUsuario(this.obtenerUsuario(rs.getInt("usuario")));
					f.setRol(this.obtenerRol(rs.getInt("rol")));
				}
				
				rs.close();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return f;
		}

		return f;
	}
	
	/**
	 * Cierra la conexi�n con la base de datos
	 */
	public void cerrarConexion()
	{
		conexion.cerrar();
	}

	/**
	 * Modifica la metainformacion de un fichero en la BD
	 * @param f el fichero con la nueva MI
	 */
	public void modificarFichero(MIDocumento f)
	{
		try
		{

			if (f != null)
			{

				String consulta = "UPDATE fichero SET nombre='" + f.getNombre()
						+ "', permisos ='" + f.getPermisos()
						+ "', padre =" + f.getPadre()
						+ ", tipo ='" + f.getTipo()
						+ "',  ruta_local ='" + f.getRutaLocal()
						+ "' WHERE id_fichero=" + f.getId();

				FrameServFich.println("Consulta a ejecutar: " + consulta);

				// insertamos la nueva tupla en fichero
				if (!conexion.update(consulta))
					System.err.println("ERROR en el update");
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	


	/**
	 * Elimina de la BD los datos de un documento
	 * @param f metainformacion del documento a borrar
	 */
	public void eliminarFichero(MIDocumento f)
	{
		try
		{

			if (f != null)
			{

				if (f.esDirectorio())
					eliminarDirectorio(f.getId());
				else
				{

					String consulta = "delete from fichero where id_fichero ="
							+ f.getId();

					FrameServFich.println("Consulta:\n" + consulta);

					if (!conexion.delete(consulta))
						System.err.println("ERROR en el delete");
				}
			}
			else FrameServFich.println("ERROR: fichero nulo");

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Elimina un directorio de la base de datos de forma recursiva
	 * @param id_fichero el identificador del directorio
	 */
	public void eliminarDirectorio(int id_fichero)
	{

		// 1. Buscamos sus hijos directorio y los borramos
		String consulta = "SELECT * FROM fichero WHERE es_directorio=1 AND padre="
				+ id_fichero;
		FrameServFich.println("Consulta:\n" + consulta);
		ResultSet rs = conexion.select(consulta);

		try
		{
			while (rs.next())
				eliminarDirectorio(rs.getInt("id_fichero"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		// 2. Eliminamos los hijos no directorio
		consulta = "delete from fichero where padre=" + id_fichero
				+ " AND es_directorio=0";
		FrameServFich.println("Consulta:\n" + consulta);
		conexion.delete(consulta);

		// 3. Eliminamos el directorio
		consulta = "delete from fichero where id_fichero =" + id_fichero;

		FrameServFich.println("Consulta:\n" + consulta);

		if (!conexion.delete(consulta))
			System.err.println("ERROR en el delete");
	}
}
