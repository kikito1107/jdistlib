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
 * Esta clase contiene la funcionalidad necesaria para manejar una base de datos
 * 
 * @created 19-enero-2008 1:13
 * @version 1.0
 * @author Ana Belen Pelegrina Ortiz, Carlos Rodriguez Dominguez
 */
public class ConectorBDFicheros
{

	private Connection con = null;

	private static String cadena_conexion = null;

	private static String user = null;

	private static String pass = null;

	private static String ip = null;

	/**
	 * Inicializaci�n de atributos est�ticos: Este bloque se ejecutará al
	 * cargarse las clases de la aplicación antes de ninguna otra cosa. Si no
	 * fuese correcto no arrancaría la aplicación.
	 */
	static
	{
		File f = new File("./IPGestorBD.txt");
		FileReader fr = null;
		try
		{
			fr = new FileReader(f);
		}
		catch (FileNotFoundException e)
		{
			System.err
					.println("Error en apertura de fichero de Conexion de BD. No se ha encontrado el fichero IPGestorBDFicheros.txt");
			System.exit(1);
		}
		BufferedReader br = new BufferedReader(fr);
		try
		{
			ip = br.readLine();
			user = br.readLine();
			pass = br.readLine();
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

	public boolean abrir()
	{
		if (con != null)
		{
			System.out.println("Recuperando datos de la conexion...");
			return true;
		}
		// System.out.print("Abriendo conexion con MySQL server...");
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
	 * @param (String)
	 *            Comando de la consulta sql
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
	 * Cierra la conexion con la base de datos
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
	 * @param (String)
	 *            Comando de la consulta sql
	 * @return (boolean) Cierto si se ha ejecutado la consulta con exito (caso
	 *         en el que la sentencia devuelve un entero correspondiente al num
	 *         de filas modificadas)
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
	 * @param (String)
	 *            Comando de la consulta sql
	 * @return (boolean) Cierto si se ha ejecutado la consulta con exito
	 */
	public boolean delete(String comando)
	{
		return this.update(comando);
	}

	/**
	 * Ejecuta consultas SQL del tipo INSERT que no devuelven un resultado de
	 * consulta
	 * 
	 * @param (String)
	 *            Comando de la consulta sql
	 * @return (boolean) Cierto si se ha ejecutado la consulta con exito
	 */
	public boolean insert(String comando)
	{
		return this.update(comando);
	}

	/**
	 * Genera un nuevo id para el campo <b>columna</b> de la tabla de nombre
	 * <b>tabla</b>
	 * 
	 * @param tabla
	 *            nombre de la tabla para la que queremos generar un nuevo id
	 * @param columna
	 *            nombre de la columna para la cual queremos generar el nuevo id
	 * @return el nuevo id generado
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
