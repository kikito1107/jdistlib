package javaspaces;

import java.io.IOException;
import java.rmi.RMISecurityManager;

import net.jini.core.transaction.server.TransactionManager;

/**
 * Acceso al manejador de transacciones
 */
public class TransactionManagerAccessor
{
	/**
	 * Obtiene un manejador de transacciones con nombre concreto
	 * @param name Nombre del manejador de transacciones
	 * @return Manejador de transacciones
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static TransactionManager getManager(String name) throws IOException, InterruptedException
	{
		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new RMISecurityManager());
		}

		return (TransactionManager) ServiceLocator.getService(TransactionManager.class);
	}

	/**
	 * Obtiene el manejador de transacciones por defecto
	 * @return Manejador de transacciones
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static TransactionManager getManager() throws IOException, InterruptedException
	{
		return (TransactionManager) ServiceLocator.getService(TransactionManager.class);
	}
}
