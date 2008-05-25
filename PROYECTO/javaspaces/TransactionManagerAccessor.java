package javaspaces;

import java.io.IOException;
import java.rmi.RMISecurityManager;

import net.jini.core.transaction.server.TransactionManager;

public class TransactionManagerAccessor
{

	public static TransactionManager getManager(String name) throws IOException, InterruptedException
	{
		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new RMISecurityManager());
		}

		return (TransactionManager) ServiceLocator.getService(TransactionManager.class);

	}

	public static TransactionManager getManager() throws IOException, InterruptedException
	{
		return (TransactionManager) ServiceLocator.getService(TransactionManager.class);
	}
}
