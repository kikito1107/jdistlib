package util;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class Contador
{
	private int contador = -1;

	private int tiempo = -1;

	public Contador( int tiempo )
	{
		this.contador = tiempo;
		this.tiempo = tiempo;
	}

	public synchronized boolean acabado()
	{
		return ( contador == 0 );
	}

	public synchronized void reiniciar()
	{
		contador = tiempo;
	}

	public void iniciar()
	{
		new Hebra(this);
	}

	private synchronized void decrementar()
	{
		contador--;
	}

	private class Hebra implements Runnable
	{
		Contador contador = null;

		Hebra( Contador contador )
		{
			this.contador = contador;
			Thread t = new Thread(this);
			t.start();
		}

		public void run()
		{
			try
			{
				while (!contador.acabado())
				{
					Thread.sleep(1000);
					contador.decrementar();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
