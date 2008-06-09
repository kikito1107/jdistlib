package plugins;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import plugins.jar.JarClassLoader;


/**
 * Permite cargar los plugins en la plataforma
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class DPluginLoader
{
	/**
	 * Obtiene el plugin contenido en un fichero
	 * @param file Fichero jar con el plugin a cargar
	 * @return Plugin del fichero o null si el fichero
	 *         no contiene ningun plugin o si ocurrio algun error
	 * @throws Exception
	 */
	@SuppressWarnings( "unchecked" )
	public static synchronized DAbstractPlugin getPlugin(String file) throws Exception
	{
		//modificar el classpath para agregar el fichero
		ClassPathModifier.addFile(new File(file));
		
		//cargar el fichero jar para obtener los elementos
		//que contiene
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
				
				try{
					//si se puede hacer un casting como
					//subclase de DAbstracPlugin, entonces
					//es un plugin valido
					cls.asSubclass(DAbstractPlugin.class);
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

	/**
	 * Obtiene todos los plugins de un directorio
	 * @param directory Directorio del cual obtener todos los plugins
	 * @return Vector con los plugins contenidos en el directorio especificado
	 * @throws Exception
	 */
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
			for (File element : childs)
				if (!element.isDirectory()
						&& element.getPath().toLowerCase().endsWith(".jar"))
				{
					dap = getPlugin(element.getPath());
					if (dap != null) res.add(dap);
				}
		}

		return res;
	}
}
