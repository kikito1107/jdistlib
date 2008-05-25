package calculoparalelo;

import net.jini.core.entry.Entry;
import net.jini.space.JavaSpace;

/**
 * Entrada en el JS, que implementa la tarea a ejecutar por los esclavos en una
 * aplicacion paralela
 * @author anab
 *
 */
public class TaskEntry implements Command
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7525622146532357726L;

	public Entry execute(JavaSpace space)
	{
		throw new RuntimeException("TaskEntry.execute() not implemented");
	}

	public long resultLeaseTime()
	{
		return 1000 * 10 * 60; // 10 minutes
	}
}
