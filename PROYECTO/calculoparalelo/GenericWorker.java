package calculoparalelo;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.swing.JTextArea;

import calculoparalelo.eventos.ResultEntry;
import calculoparalelo.eventos.TaskEntry;

import javaspaces.SpaceLocator;
import javaspaces.TransactionManagerAccessor;

import net.jini.core.entry.*;
import net.jini.core.lease.*;
import net.jini.core.transaction.*;
import net.jini.core.transaction.server.*;
import net.jini.space.JavaSpace;

/**
 * Implementa un esclavo genérico para aplicaciones paralelas con arquitectura
 * Maestro/Esclavo. No es necesario reescribirlo para cada programa concreto
 * (salvo necesidades específicas). Basta con crear una clase que herede de
 * TaskeEntry y reimplementar el método execute
 * 
 * @author anab
 * 
 */
public final class GenericWorker implements Runnable
{
	private JavaSpace space;

	private JTextArea output = null;

	public GenericWorker( JTextArea t )
	{
		try
		{
			space = SpaceLocator.getSpace();
			output = t;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Can't find the space");
		}

		Thread thread = new Thread(this);
		thread.start();
	}

	public void run()
	{
		Entry taskTmpl = null;
		try
		{
			taskTmpl = space.snapshot(new TaskEntry());
		}
		catch (RemoteException e)
		{
			throw new RuntimeException("Can't obtain a snapshot");
		}

		while (true)
		{
			output.append("getting task\n");

			Transaction txn = getTransaction();
			if (txn == null)
			{
				throw new RuntimeException("Can't obtain a transaction");
			}

			try
			{
				TaskEntry task = (TaskEntry) space.take(taskTmpl, txn,
						Long.MAX_VALUE);
				ResultEntry result = task.execute(space);
				if (result != null)
				{
					space.write(result, txn, task.resultLeaseTime());
					output.append(result.mensaje + "\n");
				}
				txn.commit();
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
			catch (TransactionException e)
			{
				e.printStackTrace();
			}
			catch (UnusableEntryException e)
			{
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				try
				{
					txn.abort();
				}
				catch (Exception ex)
				{
					// RemoteException or BadTransactionException
					// lease expiration will take care of the
					// transaction
				}
				output.append("Task cancelled\n");
			}
		}
	}

	public Transaction getTransaction()
	{
		TransactionManager mgr = null;
		try
		{
			mgr = TransactionManagerAccessor.getManager();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			return null;
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
			return null;
		}

		if (mgr != null)
		{

			try
			{
				long leaseTime = 1000 * 10 * 60; // ten minutes
				Transaction.Created created = TransactionFactory.create(mgr,
						leaseTime);
				return created.transaction;
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
				return null;
			}
			catch (LeaseDeniedException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		else return null;
	}

	public static void main(String[] args) throws Throwable
	{
		new GenericWorker(new JTextArea());
	}
}
