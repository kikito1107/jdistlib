package Deventos.enlaceJS;

import java.rmi.RemoteException;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import componentes.DComponente;
import componentes.listeners.*;

import aplicacion.fisica.ClienteFicheros;



import Deventos.*;

import interfaces.*;


import metainformacion.*;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.transaction.*;

import net.jini.core.transaction.server.*;

import net.jini.space.*;

import util.*;

import java.io.*;

/**
 * 
 * <p>
 * Description: Un objeto de esta clase hara de conector entre el JavaSpace
 * 
 * y la aplicacion. Dispondrá de métodos para comunicarle el deseo de enviar
 * 
 * o recibir mensajes del JavaSpace
 * </p>
 * 
 */

public class DConector
{

	private static final Vector v = new Vector();

	protected JavaSpace space = null;

	protected TransactionManager txnMgr = null;

	protected Monitor monitor = new Monitor();

	private static DConector dconector = null;

	private static ColaEventos cola = new ColaEventos();

	private DialogoSincronizacion dialogo = new DialogoSincronizacion();

	private String nombreJavaSpace = null;

	private DEvent plantilla = null;

	private DEvent leido = null;

	private long contador = -1;

	public static String Dusuario = null;

	public static String Drol = null;

	public static String DrolDefecto = null;

	public static String Dclave = null;

	public static String Daplicacion = null;

	private ClienteMetaInformacion cmi = null;

	private MICompleta infoCompleta = null;

	private boolean identificado = false;

	private DialogoLogin dialogoLogin = new DialogoLogin(new JFrame(),
			"Introduzca su usuario y contraseña", false);
	private boolean solicitadaDesconexion = false;
	private int tiempoSincronizacion = 4000;
	public static DefaultMutableTreeNode raiz = null;
	
	private ClienteFicheros cf = null;
	
	private TokenFichero tk = null;

	/**
	 * 
	 * @param aplicacion
	 *            String
	 * 
	 * @param nombreJavaSpace
	 *            String
	 * 
	 */

	public DConector( String aplicacion, String nombreJavaSpace )
	{

		this.nombreJavaSpace = new String(nombreJavaSpace);

		this.Daplicacion = new String(aplicacion);

		this.dconector = this;

	}

	/**
	 * 
	 * Creamos el DConector
	 * 
	 * @param aplicacion
	 *            String Nombre que hemos asignado a la aplicacion. Debe
	 * 
	 * estar definida en la metainformacion este mismo nombre
	 * 
	 */

	public DConector( String aplicacion )
	{

		this.nombreJavaSpace = new String("JavaSpace");

		this.Daplicacion = new String(aplicacion);

		this.dconector = this;

	}

	/**
	 * 
	 * Creamos el DConector
	 * 
	 * @param aplicacion
	 *            String Nombre que hemos asignado a la aplicacion. Debe
	 * 
	 * @param tiempoSincronizacion
	 *            int Tiempo en segundos que se espera desde
	 * 
	 * que se indica a los componentes que envíen sus peticiones de
	 * sincronizacion
	 * 
	 * hasta que se inician las hebras procesadoras de los componentes. Debe ser
	 * 
	 * suficiente para que de tiempo a que las instancias en ejecucion respondan
	 * 
	 * a las peticiones de sincronizacin. Por defecto el tiempo es de 4 segundos
	 * 
	 */

	public DConector( String aplicacion, int tiempoSincronizacion )
	{

		this.nombreJavaSpace = new String("JavaSpace");

		this.Daplicacion = new String(aplicacion);

		this.dconector = this;

		this.tiempoSincronizacion = tiempoSincronizacion;

	}

	/**
	 * 
	 * Mediante este metodo localizamos el JavaSpace y sincronizamos con
	 * 
	 * el coordinador
	 * 
	 */

	public void inicializar()
	{

		try
		{

			// **************** LOCALIZACION DEL JAVASPACE *******************//

			// Mostramos dialogo indicando el comienzo de la localizacion

			dialogo.mostrar(
					"Localizando JavaSpace y Manejador de Transacciones...",
					true);

			// Encargada de localizar el JavaSpace

			HebraLocalizadora hl = new HebraLocalizadora(this, "JavaSpace");

			// Espera un tiempo la localizacion del JavaSpace. En caso de no

			// localizarse en este tiempo lo indica. Asi evitamos que el

			// programa se quede atrancado

			HebraDetectoraError hde = new HebraDetectoraError(this, 10);

			// Espera un evento sobre la inicializacion

			if (monitor.inicializacionCorrecta())
			{

				dialogo.mostrar("Localizacion correcta", false);

			}

			else
			{

				dialogo.mostrar("Error inicializando el JavaSpace", false);

				JOptionPane.showMessageDialog(null,

				"Error localizando el JavaSpace y el Transacion Manager",

				"Error", JOptionPane.ERROR_MESSAGE);

				System.exit(1);

			}

			// ***************************************************************//

			dialogo.ocultar();

			// ********** PROCESO DE IDENTIFICACION **************************//

			while (!identificado)
			{

				// **************** PETICION USUARIO/CONTRASEÑA
				// ******************//

				String[] datosLogin = dialogoLogin.obtenerDatosLogin();

				Dusuario = datosLogin[0];

				Dclave = datosLogin[1];

				// ***************************************************************//

				if (cmi == null)
				{

					cmi = new ClienteMetaInformacion(Daplicacion, Dusuario,
							Dclave);

				}

				cmi.desconectarUsuario(Dusuario); // ****************************************
													// QUITAR
													// **********************************************

				dialogo.mostrar("Identificando usuario...", true);

				// ********** IDENTIFICACION DEL USUARIO
				// *************************//

				infoCompleta = cmi.conectarUsuario(Dusuario, Dclave);

				identificado = infoCompleta.identificacionValida.booleanValue();

				if (identificado)
				{

					dialogo.mostrar("Identificacion correcta", false);

					// System.out.println("Identificacion correcta");

					Drol = new String(infoCompleta.rol);

					DrolDefecto = new String(infoCompleta.rol);

				}

				else
				{

					dialogo.mostrar("Error en la identificacion del usuario",
							false);

					JOptionPane.showMessageDialog(null,

					"Error en la identificacion del usuario:\n" +

					infoCompleta.mensajeError,

					"Error", JOptionPane.ERROR_MESSAGE);

				}

				dialogo.ocultar();

				// ***************************************************************//

			}

			// ***************************************************************//

			// ********** ACCIONES INICIALES CON EL TOKEN ********************//

			dialogo.mostrar("Buscando token existente...", true);

			int tiempo = 2000;

			Fecha f = new Fecha();

			Random rd = new Random(f.getTime());

			tiempo = tiempo + rd.nextInt(1000);

			Transaction.Created txnC = TransactionFactory.create(txnMgr,
					100000L);

			Transaction txn = txnC.transaction;

			TokenSincronizacion tkscrnuevo = new TokenSincronizacion(
					Daplicacion);

			Token tkplantilla = new Token();

			Token nuevo = new Token(0);

			nuevo.aplicacion = new String(Daplicacion);

			Token tk = (Token) space.read(tkplantilla, null, tiempo);

			if (tk == null)
			{ // No hay token. Somos la primera aplicacion en lanzarse

				space.write(nuevo, txn, Long.MAX_VALUE);

				space.write(tkscrnuevo, txn, Long.MAX_VALUE);

				contador = 0;

			}

			else
			{

				contador = tk.sec.intValue();

			}

			tk = (Token) space.readIfExists(tkplantilla, null, tiempo);

			if (tk == null)
			{ // Sigue sin haber token (Comprobacion de seguridad)

				txn.commit(); // Confirmamos escritura del token el el
								// JavaSpace

			}

			else
			{

				contador = tk.sec.intValue();

			}

			// System.out.println("Contador = " + contador);

			// ***************************************************************//

			// ********** PREPARANDO HEBRA CONSUMIDORA DE EVENTOS ************//

			dialogo.mostrar("Iniciando hebra consumidora...", true);

			Thread consumidora = new Thread(new HebraConsumidora(cola, space));

			consumidora.start();

			dialogo.mostrar("Hebra consumidora iniciada", false);

			// ***************************************************************//

			// ********** PREPARANDO HEBRA RECOLECTORA DE EVENTOS ************//

			dialogo.mostrar("Iniciando hebra recolectora...", true);

			Thread recolectora = new Thread(new HebraRecolectora(cola, space));

			recolectora.start();

			recolectora.setPriority(Thread.MAX_PRIORITY);

			dialogo.mostrar("Hebra recolectora iniciada", false);

			// ***************************************************************//

			dialogo.ocultar();
			
			if (cf == null)
				cf = new ClienteFicheros(Daplicacion, Dusuario, Dclave, Drol);
			
			raiz = cf.getRaiz();

		}

		catch (Exception e)
		{

			e.printStackTrace();

			JOptionPane.showMessageDialog(null,

			"Hubo un error en la comunicacion\nDebera identificarse de nuevo.1"
					+ e.getLocalizedMessage(),

			"Error", JOptionPane.ERROR_MESSAGE);

			System.exit(1);

		}

	}

	public static ColaEventos getColaEventos()
	{

		return cola;

	}

	/**
	 * 
	 * En primer lugar indica a cada componente que envie su peticion de
	 * sincronizacion
	 * 
	 * si le es necesario y posteriormente inicia sus hebras procesadoras.
	 * 
	 */

	public void sincronizarComponentes()
	{

		sincronizarComponentes(null);

	}

	/**
	 * 
	 * En primer lugar indica a cada componente que envie su peticion de
	 * sincronizacion
	 * 
	 * si le es necesario y posteriormente inicia sus hebras procesadoras.
	 * 
	 */

	public void sincronizarComponentes(JFrame frame)
	{

		dialogo.mostrar("Esperando turno de sincronizacion...", true);

		// ***** COGER TOKEN DE SINCRONIZACION **************

		// Garantizamos que solo hay un usuario sincronizando en un cierto
		// momento

		TokenSincronizacion tk = null;

		Transaction txn = null;

		try
		{

			Transaction.Created txnC = TransactionFactory.create(txnMgr,
					( tiempoSincronizacion + 5 ) * 1000);

			txn = txnC.transaction;

			TokenSincronizacion tkscrplantilla = new TokenSincronizacion(
					Daplicacion);

			tk = (TokenSincronizacion) space.take(tkscrplantilla, txn,
					( tiempoSincronizacion + 5 ) * 1000);

			if (tk == null)
			{

				JOptionPane.showMessageDialog(null,

				"No se pudo sincronizar la sincronizacion\n",

				"Error", JOptionPane.ERROR_MESSAGE);

				System.exit(0);

			}

		}

		catch (Exception e)
		{

			e.printStackTrace();

		}

		// **********************************

		dialogo.mostrar("Sincronizando...", true);

		DComponente componente = null;

		for (int i = 0; i < v.size(); i++)
		{

			componente = (DComponente) v.elementAt(i);

			componente.sincronizar();

		}

		try
		{

			Thread.currentThread().sleep(tiempoSincronizacion); // *****************************************************

		}

		catch (Exception e)
		{

			e.printStackTrace();

		}

		// Iniciamos las hebras procesadoras de eventos de cada componente

		for (int i = 0; i < v.size(); i++)
		{

			componente = (DComponente) v.elementAt(i);

			componente.iniciarHebraProcesadora();

		}

		// ******* DEVOLVEMOS EL TOKEN PARA QUE OTRO USUARIO PUEDA SINCRONIZAR
		// ****

		try
		{

			space.write(tk, txn, Long.MAX_VALUE);

			txn.commit();

		}

		catch (Exception e2)
		{
		}

		// ************************************************************************

		cmi.addDMIListener(new ListenerMI());

		MIComponente c = null;

		DMIEvent broadCast = new DMIEvent();

		broadCast.tipo = new Integer(DMIEvent.INFO_COMPLETA.intValue());

		broadCast.infoCompleta = infoCompleta;

		broadCastMI(broadCast);

		dialogo.ocultar();

	}

	public void salir()
	{

		ClienteMetaInformacion.obtenerCMI().desconectarUsuario(Dusuario);

		System.exit(0);

	}

	public static DConector obtenerDC()
	{

		return dconector;

	}
	
	/**
	 * Comprueba si el token correspondiente al fichero se encuentra en el JS
	 * @param fichero nombre del fichero
	 * @return true si el token esta en el JS y false en caso contrario
	 */
	public boolean leerToken(String fichero){
		
		//1. Buscamos el token asociado al fichero
		TokenFichero plantilla = new TokenFichero();
		plantilla.Fichero = new String(fichero);
		plantilla.aplicacion = new String(Daplicacion+"_");
		
		try
		{
			tk =  (TokenFichero) space.take(plantilla, null, 2000L);
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnusableEntryException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransactionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2. Si no existe:
		//	2.1. Creamos el token
		//  2.2. Devolvemos false
		if (tk == null) {
			tk = new TokenFichero();
			tk.Fichero = new String(fichero);
			tk.aplicacion = new String(Daplicacion+"_");
			tk.sec = new Long(1L);
			tk.nuevoUsuario();
			return false;
		}
		//3. Si existe el token
		//	3.1. Devolvemos true
		else {
			tk.nuevoUsuario();
			tk.sec = new Long(tk.sec.longValue()+1L);
			return true;
		}
	}
	
	public boolean escribirToken(){
		try
		{
			space.write(tk, null, 10000L);
			return true;
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (TransactionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean cerrarFichero(String fichero){
		//1. Buscamos el token asociado al fichero
		TokenFichero plantilla = new TokenFichero();
		plantilla.Fichero = new String(fichero);
		plantilla.aplicacion = new String(Daplicacion+"_");
		
		try
		{
			// buscamos el token en el JS
			tk =  (TokenFichero) space.take(plantilla, null, 1000L);
			
			//Si existe el token:
			if (tk != null) {
				
				// si todav’a quedan usuarios editando ese documento
				if (tk.NumUsuarios.intValue() > 1) {
					tk.bajaUsuario();
					space.write(tk, null, 10000L);	
				}
				return true;
			}
			else
				return false;
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnusableEntryException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransactionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		

	}
	

	private void broadCastMI(DMIEvent evento)
	{

		// System.out.println("boadCastMI() tipo=" + evento.tipo);

		DComponente dc = null;

		for (int j = 0; j < v.size(); j++)
		{

			dc = (DComponente) v.elementAt(j);

			dc.procesarMetaInformacion(evento);

		}

	}

	/**
	 * 
	 * Genera un número aleatorio entre 1000 y 50000
	 * 
	 * @return int El numero aleatorio
	 * 
	 */

	private int aleatorio()
	{

		Date fecha = new Date();

		Random r = new Random(fecha.getTime());

		int aleatorio = r.nextInt(50000);

		aleatorio += 1000;

		return aleatorio;

	}

	public static int alta(DComponente componente)
	{

		// System.out.println("Añadido. Tamaño=" + v.size());

		DComponente c = null;

		// Comprobamos si ya existe un componente con este nombre

		for (int i = 0; i < v.size(); i++)
		{

			c = (DComponente) v.elementAt(i);

			if (c.getNombre().equals(componente.getNombre()))
			{

				JOptionPane
						.showMessageDialog(
								null,

								"Ya existe un componente con Nombre = " +

								c.getNombre() +

								"\nCambie alguno de los nombres para un correcto funcionamiento",

								"Error", JOptionPane.ERROR_MESSAGE);

				System.exit(1);

			}

		}

		v.add(componente);

		// componente.desactivar();

		return v.size();

	}
	
	
	
	public TokenFichero buscarTokenFichero(String path) {
		TokenFichero t = null, plantilla = new TokenFichero(Daplicacion, path);
		
		Transaction.Created txnC;
		try
		{
			t = (TokenFichero) space.take(plantilla, null, 10000L);
			
			if (t == null) {
				JOptionPane.showMessageDialog(null, "no se ha encontrado el token");
				t = new TokenFichero(Daplicacion, path);
				t.sincronizar = new Boolean(false);
			}
			else {
				t.sincronizar= new Boolean(true);
			}
			
			return t;
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnusableEntryException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransactionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return t;
	}
	
	public boolean devolverTokenFichero(TokenFichero tf) {
		try
		{
			space.write(tf, null, Long.MAX_VALUE);
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransactionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (tf == null) {
			JOptionPane.showMessageDialog(null, "Error al escribir el token");
			return false;
		}
		else 
			return true;
	}
	

	/**
	 * 
	 * Encargada de localizar el JavaSpace
	 * 
	 */

	private class HebraLocalizadora

	implements Runnable
	{

		DConector conector = null;

		String nombre = null;

		Thread t = null;

		int i = 0;

		/**
		 * 
		 * 
		 * 
		 * @param conector
		 *            DConector Clase desde la que se llama a la hebra
		 * 
		 * @param nombreJavaSpace
		 *            String Nombre del JavaSpace que deseamos localizar
		 * 
		 */

		public HebraLocalizadora( DConector conector, String nombreJavaSpace )
		{

			this.conector = conector;

			this.nombre = nombreJavaSpace;

			t = new Thread(this);

			t.start();

		}

		/**
		 * 
		 * Método que ejecuta la hebra
		 * 
		 */

		public void run()
		{

			try
			{

				// Intentamos localizar el JavaSpace

				conector.space = SpaceLocator.getSpace(nombre);

				// Intentamos localizar un manejador de transacciones

				txnMgr = (TransactionManager) ServiceLocator.getService(

				TransactionManager.class);

				// Localizacion del JavaSpace exitosa

				conector.monitor.setInicializado(true);

			}

			catch (Exception e)
			{

				e.printStackTrace();

			}

		}

	}

	/**
	 * 
	 * 
	 * 
	 * Mediante esta clase podemos indicar que no ha sido posible
	 * 
	 * localizar el JavaSpace pasado el tiempo indicado. De esta forma evitamos
	 * 
	 * que el programa se quede bloqueado en la fase de localizacion del
	 * 
	 * JavasPace
	 * 
	 */

	private class HebraDetectoraError

	implements Runnable
	{

		DConector conector = null;

		int tiempoEspera = -1;

		Thread t = null;

		/**
		 * 
		 * 
		 * 
		 * @param conector
		 *            DConector Clase que crea esta hebra
		 * 
		 * @param tiempoEspera
		 *            int Tiempo que deseamos esperar
		 * 
		 */

		public HebraDetectoraError( DConector conector, int tiempoEspera )
		{

			this.conector = conector;

			this.tiempoEspera = tiempoEspera;

			t = new Thread(this);

			t.start();

		}

		/**
		 * 
		 * Método que ejecuta la hebra
		 * 
		 */

		public void run()
		{

			try
			{

				Thread.currentThread().sleep(tiempoEspera * 1000);

				if (!conector.monitor.getInicializado())
				{

					conector.monitor.setError(true);

				}

			}

			catch (Exception e)
			{

				e.printStackTrace();

			}

		}

	}

	/**
	 * 
	 * Dado el comportamiento concurrente de las 2 Hebras mediante este Monitor
	 * 
	 * gestionamos la informacion sobre la inicializacion. La clase DConector
	 * 
	 * realizara una llamada a inicializacionCorrecta() quedándose bloqueada
	 * 
	 * hasta que se producza la correcta localizacion del JavaSpace o se
	 * sobrepase
	 * 
	 * el tiempo de espera sin haber sido localizado.
	 * 
	 */

	private class Monitor
	{

		private boolean inicializado = false;

		private boolean error = false;

		private boolean sincronizado = false;

		/**
		 * 
		 * Bloquea al llamante hasta que se produzca una actualizacion de las
		 * 
		 * variables inicializado o error
		 * 
		 * @return boolean Devuelve si se ha producido o no la inicializacion
		 * 
		 */

		public synchronized boolean inicializacionCorrecta()
		{

			try
			{

				while (!inicializado && !error)
				{

					wait();

				}

			}

			catch (Exception e)
			{

				e.printStackTrace();

			}

			return inicializado;

		}

		/**
		 * 
		 * Devuelve si se ha sobrepasado el tiempo de espera sin haber sido
		 * localizado
		 * 
		 * el JavaSpace
		 * 
		 */

		public synchronized boolean getError()
		{

			return error;

		}

		/**
		 * 
		 * Cambamos el valor d ela variable error
		 * 
		 * @param b
		 *            boolean Valor deseado
		 * 
		 */

		public synchronized void setError(boolean b)
		{

			error = b;

			notifyAll();

		}

		/**
		 * 
		 * Devuelve si se ha localizado el JavaSpace
		 * 
		 */

		public synchronized boolean getInicializado()
		{

			return inicializado;

		}

		/**
		 * 
		 * Cambiamos el valor de la variable inicializado
		 * 
		 * @param b
		 *            boolean Valor deseado
		 * 
		 */

		public synchronized void setInicializado(boolean b)
		{

			inicializado = b;

			notifyAll();

		}

		/**
		 * 
		 * Devuelve si se ha sincronizado
		 * 
		 */

		public synchronized boolean getSincronizado()
		{

			return sincronizado;

		}

		/**
		 * 
		 * Cambiamos el valor de la variable sincronizado
		 * 
		 * @param b
		 *            boolean Valor deseado
		 * 
		 */

		public synchronized void setSincronizado(boolean b)
		{

			sincronizado = b;

			notifyAll();

		}

	}

	private class HebraConsumidora

	implements Runnable
	{

		ColaEventos cola = null;

		JavaSpace space = null;

		HebraConsumidora( ColaEventos cola, JavaSpace space )
		{

			this.cola = cola;

			this.space = space;

		}

		public void run()
		{

			int i = 0;

			DEvent evento = null;

			Token tkplantilla = new Token();

			Token tk = null;

			tkplantilla.aplicacion = new String(Daplicacion);

			long numSalida = -1;

			while (true)
			{

				try
				{

					// Preparamos el evento a enviar

					evento = cola.extraerEvento();

					evento.usuario = new String(Dusuario);

					evento.aplicacion = new String(Daplicacion);

					evento.origen = new Integer(1);

					evento.destino = new Integer(1);

					// Cogemos el token

					Transaction.Created txnC = TransactionFactory.create(
							txnMgr, 2000L);

					Transaction txn = txnC.transaction;

					tk = (Token) space.take(tkplantilla, txn, Long.MAX_VALUE);

					numSalida = tk.sec.longValue();

					tk.sec = new Long(tk.sec.longValue() + 1);

					// Escribimos el evento

					evento.contador = new Long(numSalida);

					space.write(evento, txn, 10000L);

					// System.out.println("DConector: Escrito evento " +
					// evento);

					// Escribimos el token

					space.write(tk, txn, Long.MAX_VALUE);

					// Hacemos el COMMIT de la transaccion

					txn.commit();

				}

				catch (Exception e)
				{

					e.printStackTrace();

					JOptionPane.showMessageDialog(null,

					"Hubo un error en la comunicacion\nDebera identificarse de nuevo.1"
							+ e.getLocalizedMessage(),

					"Error", JOptionPane.ERROR_MESSAGE);

					System.exit(1);

				}

			}

		}

	}

	private class HebraRecolectora

	implements Runnable
	{

		ColaEventos cola = null;

		JavaSpace space = null;

		HebraRecolectora( ColaEventos cola, JavaSpace space )
		{

			this.cola = cola;

			this.space = space;

		}

		public void run()
		{

			DEvent evento = null;

			DEvent plantilla = new DEvent();

			DComponente componente = null;

			// plantilla.origen = new Integer(0); // El Coordinador

			plantilla.destino = new Integer(1); // La Aplicacon

			plantilla.aplicacion = new String(Daplicacion);

			// plantilla.usuario = new String(usuario);

			while (true)
			{

				try
				{

					plantilla.contador = new Long(contador);

					// System.out.println("HebraRecolectora: Esperando evento
					// "+contador);

					evento = (DEvent) space.read(plantilla, null,
							Long.MAX_VALUE);

					// System.out.println("DConector: Leido evento "+evento);

					contador++;

					for (int i = 0; i < v.size(); i++)
					{

						componente = (DComponente) v.elementAt(i);

						if (componente.getID().intValue() == evento.componente
								.intValue())
						{

							componente.procesarEvento(evento);

						}

					}

					// System.out.println("");

				}

				catch (Exception e)
				{

					// e.printStackTrace();

					JOptionPane
							.showMessageDialog(
									null,

									"Hubo un error en la comunicacion\nDebera identificarse de nuevo.3",

									"Error", JOptionPane.ERROR_MESSAGE);

					e.printStackTrace();

					System.exit(1);

				}

			}

		}

	}

	private class ListenerMI

	implements DMIListener
	{

		public void conexionUsuario(String usuario, String rol)
		{

			DMIEvent evento = new DMIEvent();

			evento.tipo = new Integer(DMIEvent.NOTIFICACION_CONEXION_USUARIO
					.intValue());

			evento.usuario = new String(usuario);

			evento.rol = new String(rol);

			broadCastMI(evento);

			// System.out.println("Conexion usuario");

		}

		public void desconexionUsuario(String usuario)
		{

			DMIEvent evento = new DMIEvent();

			evento.tipo = new Integer(DMIEvent.NOTIFICACION_DESCONEXION_USUARIO
					.

					intValue());

			evento.usuario = new String(usuario);

			broadCastMI(evento);

			// System.out.println("Desconexion usuario");

		}

		public void cambioRolUsuario(String usuario, String rol,
				String rolAntiguo,

				MICompleta info)
		{

			if (usuario.equals(Dusuario))
			{

				Drol = info.rol;

			}

			DMIEvent evento = new DMIEvent();

			evento.tipo = new Integer(DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO.

			intValue());

			evento.usuario = new String(usuario);

			evento.rol = new String(rol);

			evento.rolAntiguo = new String(rolAntiguo);

			evento.infoCompleta = info;

			broadCastMI(evento);

		}

		public void cambioPermisoComponenteUsuario(String usuario,

		String componente, int permiso)
		{

			DMIEvent evento = new DMIEvent();

			evento.tipo = new Integer(DMIEvent.

			NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_USUARIO.

			intValue());

			evento.usuario = new String(usuario);

			evento.componente = new String(componente);

			evento.permiso = new Integer(permiso);

			broadCastMI(evento);

		}

		public void cambioPermisoComponenteRol(String rol, String componente,

		int permiso)
		{

			DMIEvent evento = new DMIEvent();

			evento.tipo = new Integer(DMIEvent.

			NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_ROL.

			intValue());

			evento.rol = new String(rol);

			evento.componente = new String(componente);

			evento.permiso = new Integer(permiso);

			broadCastMI(evento);

		}

		public void nuevoRolPermitido(String usuario, String rol)
		{

			DMIEvent evento = new DMIEvent();

			evento.tipo = new Integer(DMIEvent.

			NOTIFICACION_NUEVO_ROL_PERMITIDO.

			intValue());

			evento.usuario = new String(usuario);

			evento.rol = new String(rol);

			broadCastMI(evento);

		}

		public void eliminarRolPermitido(String usuario, String rol)
		{

			if (usuario.equals(Dusuario) && rol.equals(Drol))
			{

				ClienteMetaInformacion.obtenerCMI().cambiarRolUsuario(Dusuario,

				DrolDefecto);

			}

			DMIEvent evento = new DMIEvent();

			evento.tipo = new Integer(
					DMIEvent.NOTIFICACION_ELIMINAR_ROL_PERMITIVO.

					intValue());

			evento.usuario = new String(usuario);

			evento.rol = new String(rol);

			broadCastMI(evento);

		}

		public void usuarioEliminado(String usuario)
		{

			if (usuario.equals(Dusuario))
			{

				JOptionPane
						.showMessageDialog(
								null,

								"Acaba de ser eliminado como usuario. \nEl programa finalizara",

								"Error", JOptionPane.ERROR_MESSAGE);

				System.exit(0);

			}

			else
			{

				DMIEvent evento = new DMIEvent();

				evento.tipo = new Integer(DMIEvent.

				NOTIFICACION_USUARIO_ELIMINADO.

				intValue());

				evento.usuario = new String(usuario);

				broadCastMI(evento);

			}

		}

		public void rolEliminado(String rol)
		{

			if (rol.equals(Drol))
			{

				ClienteMetaInformacion.obtenerCMI().cambiarRolUsuario(Dusuario,

				DrolDefecto);

			}

			DMIEvent evento = new DMIEvent();

			evento.tipo = new Integer(DMIEvent.

			NOTIFICACION_ROL_ELIMINADO.

			intValue());

			evento.rol = new String(rol);

			broadCastMI(evento);

		}

	}

}
