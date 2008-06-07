package calculoparalelo.eventos;

import net.jini.space.JavaSpace;
import calculoparalelo.Command;
import Deventos.DEvent;

/**
 * Entrada en el JS, que implementa la tarea a ejecutar por los esclavos en una
 * aplicacion paralela
 * 
 * @author Ana Belen Pelegrina Ortiz. Carlos Rodriguez Dominguez
 */
public class TaskEntry extends DEvent implements Command
{
	private static final long serialVersionUID = 7525622146532357726L;

	/**
	 * Ejecucion de la tarea. Debera ser sobrecargada
	 */
	public ResultEntry execute(JavaSpace space)
	{
		throw new RuntimeException("TaskEntry.execute() not implemented");
	}

	/**
	 * Tiempo maximo que se deja en el JavaSpace
	 * @return Tiempo de espera
	 */
	public long resultLeaseTime()
	{
		return 1000 * 10 * 60; // 10 minutos
	}
}
