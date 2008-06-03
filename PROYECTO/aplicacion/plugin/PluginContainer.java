package aplicacion.plugin;

import java.util.Vector;

/**
 * Clase encargada de la gesti—n de los plugins, controlar el acceso a los plugins y notificar a las hebras 
 * que asi lo deseen los cambios producidos en los plugins
 * @author anab
 */
public class PluginContainer
{
	/**
	 * Vector de plugins
	 */
	private static Vector<DAbstractPlugin> plugins = null;

	/**
	 * Monitor encargado de sincronizar el acceso a los plugins
	 */
	private static MonitorPlugins monitor = null;
	
	/**
	 * Flag booleano que indica si el contenedor ha sido ya creado
	 */
	private static boolean created  = false;
	
	/**
	 * Inicializa el contenedor de plugins si no ha sido ya inicializado
	 */
	public PluginContainer()
	{
		
		if (!created) {
			
			//	cargar los plugins
			try
			{
				plugins =  DPluginLoader.getAllPlugins("plugin");
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			monitor = new MonitorPlugins();
			created = true;
		}
	}
	
	/**
	 * La hebra que ejecuta este mŽtodo espera hasta que se notifique algun cambio en la lista de plugins
	 */
	public static void actualizar(){
		monitor.actualizar();
	}
	
	/**
	 * Permite acceder al plugin i-Žsimo
	 * @param index posicion del plugin en la lista
	 * @return el plugin si el valor del indice es correcto, -1 en caso contrario
	 */
	public static DAbstractPlugin getPlugin(int index){
		if (index >= 0 && index < plugins.size())
			return plugins.get(index);
		else 
			return null;
	}
	
	
	/**
	 * Elimina un plugin de la lista de plugins
	 * 
	 * @param namen
	 *            nombre del plugin a eliminar
	 */
	public static void eliminarPlugin(String namen)
	{

		boolean encontrada = false;

		for (int i = 0; i < plugins.size() && !encontrada; ++i)
			if (plugins.get(i).getName().equals(namen))
			{
				encontrada = true;
				plugins.remove(i);
			}

		// notificamos la eliminacion del plugin
		monitor.notificarPlugins();
	}
	
	/**
	 * Comprueba si el i-esimo plugin de la lista es visible
	 * @param i posicion del plugin
	 * @return true si el plugin es visible y false en caso contrario
	 */
	public static boolean isVisible(int i) {
		return plugins.get(i).shouldShowIt();
	}

	
	/**
	 * Establece si un plugin ha de ser visible o no
	 * @param b booleano que indica si el plugin ha de ser visible o no
	 * @param name nombre del plugin
	 */
	public static void setVisible(boolean b, String name) {
		
		boolean encontrada = false;

		for (int i = 0; i < plugins.size() && !encontrada; ++i)
			if (plugins.get(i).getName().equals(name))
			{
				encontrada = true;
				plugins.get(i).setShouldShowit(b);
			}

		// notificamos la eliminacion del plugin
		monitor.notificarPlugins();
	}

	
	
	/**
	 * Agreaga un plugin a la lista
	 * 
	 * @param a
	 *            plugin a agregar
	 */
	public static void agregarPlugin(DAbstractPlugin a)
	{
		plugins.add(a);

		// notificamos la insercion de un nuevo plugin
		monitor.notificarPlugins();
	}

	/**
	 * Consulta el numero de plugins cargados actualmente
	 * 
	 * @return el numero de plugins. Devuelve -1 si se ha producido algœn error
	 */
	public static int numPlugins()
	{

		if (plugins == null)
			return -1;
		else return plugins.size();
	}

	/**
	 * Consulta el nombre del fichero jar asociado a un plugin
	 * 
	 * @param index
	 *            posicion del plugin en la lista
	 * @return el nombre del jar
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static String getPluginJarName(int index)
			throws ArrayIndexOutOfBoundsException
	{
		String jarName = plugins.get(index).getJarFile();

		return jarName;
	}

	/**
	 * Consulta la version del plugin
	 * 
	 * @param index
	 *            posicion del plugin en la lista
	 * @return la version
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static long getVersionPlugin(int index)
			throws ArrayIndexOutOfBoundsException
	{
		long v = plugins.get(index).getVersion();

		return v;
	}

	/**
	 * Consulta el nombre de un plugin
	 * 
	 * @param index
	 *            posicion del plugin en la lista
	 * @return el nombre
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static String getPluginName(int index)
			throws ArrayIndexOutOfBoundsException
	{
		String jarName = plugins.get(index).getName();

		return jarName;
	}
	
	/**
	 * Monitor que controla la actualizacion de la lista de aplicaciones
	 */
	private class MonitorPlugins
	{
		/**
		 * Metodo que controla el acceso a la lista de plugins. Toda hebra que 
		 * llame a este metodo se queda "dormida" hasta que se ejecute el metodo
		 * notificarPlugins()
		 */
		public synchronized void actualizar()
		{
			try
			{
				wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

		/**
		 * Notifica a todas las hebras que estŽn esperando por una actulizacion
		 * de la lista de plugin que esta se ha producido. EL efecto de esta llamada
		 * es que se despiertan todas la hebras que estaban esperando; el planificador
		 * decide que hebra de la que se acaban de despertar se ejecutar‡ primero.
		 */
		public synchronized void notificarPlugins()
		{
			notifyAll();
		}
	}
}
