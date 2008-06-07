package calculoparalelo;

import java.rmi.RemoteException;

import javaspaces.SpaceLocator;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import aplicacion.plugin.DAbstractPlugin;
import calculoparalelo.eventos.TaskEntry;

import componentes.base.DComponenteBase;

/**
 * Maestro genérico para aplicaciones paralelas con estructura Master/Slave. Es
 * necesario modificarlo para cada problema concreto
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public abstract class GenericMaster extends DAbstractPlugin
{
	protected JavaSpace space;

	protected GenericWorker esclavo = null;

	/**
	 * Constructor
	 * 
	 * @param nombre
	 *            Nombre del componente
	 * @param conexionDC
	 *            Indica si queremos conectarnos directamente al
	 * @see DConector
	 * @param padre
	 *            Padre del componente
	 * @throws Exception
	 */
	protected GenericMaster( String nombre, boolean conexionDC,
			DComponenteBase padre ) throws Exception
	{
		super(nombre, conexionDC, padre);
	}

	@Override
	public void init()
	{

		try
		{
			space = SpaceLocator.getSpace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		GenerateThread gThread = new GenerateThread();
		gThread.start();
		CollectThread cThread = new CollectThread();
		cThread.start();
	}

	/**
	 * Genera las tareas para cada uno de los esclavos
	 */
	protected abstract void generateTasks();

	/**
	 * Recolecta los resultados obtenidos desde los esclavos
	 */
	protected abstract void collectResults();

	/**
	 * Escribe una tarea en el JavaSpace
	 * 
	 * @param task
	 *            Tarea a escribir
	 */
	protected final void writeTask(TaskEntry task)
	{
		try
		{
			space.write(task, null, Lease.FOREVER);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (TransactionException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Coge una tarea del JavaSpace
	 * 
	 * @param template
	 *            Plantilla de la entrada a recuperar
	 * @return Entrada recuperada
	 */
	protected final Entry takeTask(Entry template)
	{
		try
		{
			Entry result = space.take(template, null, Long.MAX_VALUE);
			return result;
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
			System.out.println("Task cancelled");
		}
		return null;
	}

	/**
	 * Hebra encargada de generar las tareas
	 */
	private class GenerateThread extends Thread
	{
		@Override
		public void run()
		{
			generateTasks();
		}
	}

	/**
	 * Hebra encargada de recoger los resultados
	 */
	private class CollectThread extends Thread
	{
		@Override
		public void run()
		{
			collectResults();
		}
	}
}
