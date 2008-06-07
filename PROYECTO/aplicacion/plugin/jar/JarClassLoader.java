package aplicacion.plugin.jar;

/**
 * Carga una clase contenida en un fichero jar
 * 
 * @author Carlos Rodriguez Dominguez
 */
public class JarClassLoader extends MultiClassLoader
{
	private JarResources jarResources;

	/**
	 * Constructor
	 * 
	 * @param jarName
	 *            Fichero jar del cual cargar las clases
	 */
	public JarClassLoader( String jarName )
	{
		jarResources = new JarResources(jarName);
	}

	@Override
	protected byte[] loadClassBytes(String className)
	{
		className = formatClassName(className);
		return ( jarResources.getResource(className) );
	}
}
