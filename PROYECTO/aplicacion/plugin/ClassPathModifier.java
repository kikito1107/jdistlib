package aplicacion.plugin;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathModifier
{
	@SuppressWarnings("unchecked")
	private static final Class[] parameters = new Class[]
	{ URL.class };

	public static void addFile(String s)
	{
		File f = new File(s);
		addFile(f);
	}

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
