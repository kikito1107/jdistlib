package plugins;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Permite modificar el classpath de la aplicacion
 * de forma dinamica
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class ClassPathModifier
{
	@SuppressWarnings("unchecked")
	private static final Class[] parameters = new Class[]
	{ URL.class };

	/**
	 * Agrega un fichero al classpath
	 * @param s Path del fichero
	 */
	public static void addFile(String s)
	{
		File f = new File(s);
		addFile(f);
	}

	/**
	 * Agrega un fichero al classpath
	 * @param f Fichero a agregar al classpath
	 */
	public static void addFile(File f)
	{
		try
		{
			addURL(f.toURI().toURL());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Agrega una URL al classpath
	 * @param u URL a agregar al classpath
	 */
	public static void addURL(URL u)
	{
		URLClassLoader sysloader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		try
		{
			Method method = URLClassLoader.class.getDeclaredMethod("addURL",
					parameters);
			method.setAccessible(true);
			method.invoke(sysloader, new Object[]{ u });
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
