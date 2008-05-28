package calculoparalelo.eventos;

import net.jini.space.JavaSpace;
import calculoparalelo.Command;
import Deventos.DEvent;

/**
 * Entrada en el JS, que implementa la tarea a ejecutar por los esclavos en una
 * aplicacion paralela
 * @author anab
 *
 */
public class TaskEntry extends DEvent implements Command
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7525622146532357726L;

	public ResultEntry execute(JavaSpace space)
	{
		throw new RuntimeException("TaskEntry.execute() not implemented");
	}

	public long resultLeaseTime()
	{
		return 1000 * 10 * 60; // 10 minutes
	}
}
