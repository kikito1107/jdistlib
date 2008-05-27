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
 * Maestro genérico para aplicaciones paralelas con estructura Master/Slave. Es necesario
 * modificarlo para cada problema concreto
 * @author anab
 */
public abstract class GenericMaster extends DAbstractPlugin {
    protected JavaSpace space;
    
    protected GenericWorker esclavo = null;

    protected GenericMaster( String nombre, boolean conexionDC,
			DComponenteBase padre ) throws Exception
	{
		super(nombre, conexionDC, padre);
	}
    
    public void init() {
    	
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

    protected abstract void generateTasks();

    protected abstract void collectResults();


        protected final void writeTask(TaskEntry task) {
            try {
                    space.write(task, null, Lease.FOREVER);
            } catch (RemoteException e) {
                    e.printStackTrace();
            } catch (TransactionException e) {
                    e.printStackTrace();
            }
        }
        
        protected  final Entry takeTask(Entry template) {
            try {
                    Entry result =
                        (Entry) space.take(template, null, Long.MAX_VALUE);
                    return result;
            } catch (RemoteException e) {
                    e.printStackTrace();
            } catch (TransactionException e) {
                    e.printStackTrace();
            } catch (UnusableEntryException e) {
                    e.printStackTrace();
            } catch (InterruptedException e) {
                    System.out.println("Task cancelled");
            }
            return null;
        }
    
    private class GenerateThread extends Thread {
        public void run() {
            generateTasks();
        }
    }
    
    private class CollectThread extends Thread {
        public void run() {
            collectResults();
        }
    }   
}
