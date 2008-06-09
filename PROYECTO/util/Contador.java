package util;

/**
 * Contador totalmente sincronizado para poder acceder a el desde distintas
 * hebras
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class Contador
{
	private int contador = -1;

	private int tiempo = -1;

	/**
	 * Constructor
	 * 
	 * @param tiempo
	 *            Tiempo inicial del contador
	 */
	public Contador( int tiempo )
	{
		this.contador = tiempo;
		this.tiempo = tiempo;
	}

	/**
	 * Comprueba si el contador ha terminado
	 * 
	 * @return True si el contador ha terminado. False en caso contrario
	 */
	public synchronized boolean acabado()
	{
		return ( contador == 0 );
	}

	/**
	 * Reinicia el contador
	 */
	public synchronized void reiniciar()
	{
		contador = tiempo;
	}

	/**
	 * Inicia la cuenta atras en el contador
	 */
	public void iniciar()
	{
		new Hebra(this);
	}

	/**
	 * Decrementa en uno el valor del contador
	 */
	private synchronized void decrementar()
	{
		contador--;
	}

	/**
	 * Hebra encargada de decrementar el contador
	 */
	private class Hebra implements Runnable
	{
		private Contador contador = null;

		/**
		 * Constructor
		 * 
		 * @param contador
		 *            Contador al cual hacer la cuenta atras
		 */
		public Hebra( Contador contador )
		{
			this.contador = contador;
			Thread t = new Thread(this);
			t.start();
		}

		/**
		 * Ejecucion de la hebra
		 */
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
