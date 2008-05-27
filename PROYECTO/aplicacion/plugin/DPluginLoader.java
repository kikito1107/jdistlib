package aplicacion.plugin;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import aplicacion.plugin.jar.JarClassLoader;

public class DPluginLoader
{
	@SuppressWarnings( "unchecked" )
	public static synchronized DAbstractPlugin getPlugin(String file) throws Exception
	{
		ClassPathModifier.addFile(new File(file));
		JarClassLoader jcl = new JarClassLoader(file);

		String className = null;
		JarEntry je = null;
		Class cls = null;
		//Class superclass = null;

		JarFile jarFile = new JarFile(file);
		Enumeration<JarEntry> enu = jarFile.entries();

		while (enu.hasMoreElements())
		{
			je = enu.nextElement();
			if (je.getName().toLowerCase().endsWith(".class"))
			{
				className = je.getName().replaceAll(".class", "").replaceAll(
						"/", ".");
				cls = jcl.loadClass(className, true);

				/*superclass = cls.getSuperclass();
				
				if (superclass != null) //si la clase es Object o una interfaz, la superclase es null
					if (superclass.getName().compareTo(DAbstractPlugin.class.getName()) == 0)
						return (DAbstractPlugin) cls.newInstance();*/
				
				try{
					return (DAbstractPlugin)cls.newInstance();
				}
				catch(Exception ex)
				{
					//se dara una excepcion esperada si la clase del fichero no es subclase
					//de DAbstractPlugin o si no se puede crear la instancia porque el
					//constructor no es publico.
				}
			}
		}

		// si no se encontro ninguno...
		return null;
	}

	public static synchronized Vector<DAbstractPlugin> getAllPlugins(String directory)
			throws Exception
	{
		Vector<DAbstractPlugin> res = new Vector<DAbstractPlugin>();
		DAbstractPlugin dap = null;

		File dir = new File(directory);
		String children[] = dir.list();
		if (children == null)
			throw new Exception(
					"El directorio no existe o el path no corresponde a un directorio");
		else
		{
			File childs[] = dir.listFiles();
			for (int i = 0; i < childs.length; i++)
				if (!childs[i].isDirectory()
						&& childs[i].getPath().toLowerCase().endsWith(".jar"))
				{
					dap = getPlugin(childs[i].getPath());
					if (dap != null) res.add(dap);
				}
		}

		return res;
	}
}
