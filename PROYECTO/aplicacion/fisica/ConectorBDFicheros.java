package aplicacion.fisica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Esta clase contiene la funcionalidad necesaria para manejar la base de datos
 * con la metainformacion de los documentos del sistema.
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class ConectorBDFicheros
{

	private Connection con = null;

	private static String cadena_conexion = null;

	private static String user = null;

	private static String pass = null;

	private static String ip = null;

	private static final String fichero = "config";

	/**
	 * Inicializaci—n de atributos est‡ticos: Este bloque se ejecutara al
	 * cargarse las clases de la aplicacion (se ejecutara antes de todo lo demas). 
	 * Si ocurriera algun error, entonces la aplicacion se cerraria.
	 */
	static
	{
		File f = new File(fichero);
		FileReader fr = null;
		try
		{
			fr = new FileReader(f);
		}
		catch (FileNotFoundException e)
		{
			System.err
					.println("Error en apertura de fichero de Conexion de BD. No se ha encontrado el fichero config");
			System.exit(1);
		}
		BufferedReader br = new BufferedReader(fr);
		try
		{
			String datos;

			br.readLine();

			datos = br.readLine();

			String data[] = datos.split(" ");

			if (data.length != 4)
			{
				System.err
						.println("Error en lectura de fichero de Conexion de BD");
				System.exit(1);
			}

			ip = data[1];
			user = data[2];
			pass = data[3];
		}
		catch (IOException e)
		{
			System.err
					.println("Error en lectura de fichero de Conexion de BD: "
							+ e.getMessage());
			System.exit(2);
		}
		if (( ip == null ) || ip.equals(""))
		{
			System.err
					.println("Error en lectura de la iIP desde el fichero IPGestorBDFicheros.txt");
			System.exit(3);
		}
		if (( user == null ) || user.equals(""))
		{
			System.err
					.println("Error en lectura del USUARIO desde el fichero IPGestorBDFicheros.txt");
			System.exit(3);
		}
		if (( pass == null ) || pass.equals(""))
		{
			System.err
					.println("Error en lectura del PASSWORD desde el fichero IPGestorBDFicheros.txt");
			System.exit(3);
		}
		cadena_conexion = new String("jdbc:mysql://" + ip + "/Ficheros");
	}

	/**
	 * Constructor de la clase.
	 * 
	 */
	public ConectorBDFicheros()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Abre una conexion con la Base de Datos
	 * @return True si la conexion se abrio con exito. False en caso contrario
	 */
	public boolean abrir()
	{
		if (con != null)
		{
			System.out.println("Recuperando datos de la conexion...");
			return true;
		}

		try
		{
			con = DriverManager.getConnection(cadena_conexion, user, pass);
		}
		catch (SQLException e)
		{
			System.err.println("Error al crear conexion con BD: "
					+ e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Ejecuta una consulta del tipo SELECT, que devuelve un objeto ResultSet
	 * 
	 * @param comando Expresion para la consulta en SQL
	 * @return ResultSet con los resultados obtenidos por la consulta.
	 */
	public ResultSet select(String comando)
	{
		Statement s;
		ResultSet rs = null;
		try
		{
			s = con.createStatement();
			rs = s.executeQuery(comando);
		}
		catch (/* SQL */Exception e)
		{
			System.err.println("Error ejecutando select '" + comando + "': "
					+ e);
			e.printStackTrace();
			return null;
		}
		return rs;
	}

	/**
	 * Cierra la conexion con la Base de Datos
	 */
	public void cerrar()
	{// LO TENIAN COMENTADO, XQ????
		// System.out.print("Cerrando conexion con MySQL server...");
		try
		{
			if (con != null) con.close();
		}
		catch (SQLException e)
		{
			System.err.println("Error al cerrar la DB: " + e);
		}
		// System.out.println("conexion cerrada con exito");
	}

	/**
	 * Ejecuta consultas SQL del tipo INSER, UPDATE o DELETE que no devuelven un
	 * resultado de consulta
	 * 
	 * @param comando Expresion para la consulta en SQL
	 * @return True si se ha ejecutado la consulta con exito, False en otro caso.
	 */
	public boolean update(String comando)
	{
		Statement s;
		int res = 0;
		try
		{
			s = con.createStatement();
			res = s.executeUpdate(comando);
		}
		catch (SQLException e)
		{
			System.err.println("Error ejecutando sentencia update '" + comando
					+ "': " + e);
			return false;
		}

		if (res == 0)
			return false;
		else return true;
	}

	/**
	 * Ejecuta consultas SQL del tipo DELETE que no devuelven un resultado de
	 * consulta
	 * 
	 * @param comando Expresion de la consulta SQL
	 * @return True si se ha ejecutado la consulta con exito. False en otro caso.
	 */
	public boolean delete(String comando)
	{
		return this.update(comando);
	}

	/**
	 * Ejecuta consultas SQL del tipo INSERT que no devuelven un resultado de
	 * consulta
	 * 
	 * @param comando Expresion de la consulta SQL
	 * @return True si se ha ejecutado la consulta con exito. False en otro caso.
	 */
	public boolean insert(String comando)
	{
		return this.update(comando);
	}

	/**
	 * Genera un nuevo id para el campo <b>columna</b> de la tabla de nombre
	 * <b>tabla</b>
	 * 
	 * @param tabla Nombre de la tabla para la que queremos generar un nuevo id
	 * @param columna Nombre de la columna para la cual queremos generar el nuevo id
	 * @return El nuevo id generado
	 */
	public int generaId(String tabla, String columna)
	{
		int id = 100;

		try
		{
			ResultSet rs = this.select("SELECT MAX(" + columna + ")+1 FROM "
					+ tabla);

			if (rs.next()) id = rs.getInt(1);

			rs.close();

		}
		catch (Exception e)
		{
			System.out.println("Exc:" + e);
		}

		return id;
	}

}
