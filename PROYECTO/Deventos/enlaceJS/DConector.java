package Deventos.enlaceJS;

import java.rmi.RemoteException;
import java.util.Random;
import java.util.Vector;

import javaspaces.ServiceLocator;
import javaspaces.SpaceLocator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;
import util.Fecha;
import awareness.ClienteMetaInformacion;
import awareness.MICompleta;

import componentes.base.DComponente;
import componentes.listeners.DMIListener;
import fisica.ClienteFicheros;
import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DMIEvent;
import Deventos.tokens.Token;
import Deventos.tokens.TokenFichero;
import Deventos.tokens.TokenSincronizacion;

/**
 * Conector entre el JavaSpace y la aplicacion. Dispondra de metodos para
 * comunicarle el deseo de enviar o recibir mensajes del JavaSpace, asi como
 * obtener distintas caracteristicas de la conexion de la aplicacion (usuario,
 * rol, nombre de la aplicacion, etc.)
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DConector
{
	private static final Vector<DComponente> v = new Vector<DComponente>();

	protected JavaSpace space = null;

	protected TransactionManager txnMgr = null;

	protected Monitor monitor = new Monitor();

	private static DConector dconector = null;

	private static ColaEventos cola = new ColaEventos();

	private DialogoSincronizacion dialogo = new DialogoSincronizacion();

	private long contador = -1;

	/**
	 * Nombre de usuario que tenemos en el sistema
	 */
	public static String Dusuario = null;

	/**
	 * Nombre del rol que tenemos en el sistema
	 */
	public static String Drol = null;

	/**
	 * Nombre del rol por defecto asociado a nuestro usuario
	 */
	public static String DrolDefecto = null;

	/**
	 * Clave asociada a nuestro usuario
	 */
	public static String Dclave = null;

	/**
	 * Nombre de la aplicacion de la que hacemos uso
	 */
	public static String Daplicacion = null;

	private ClienteMetaInformacion cmi = null;

	private MICompleta infoCompleta = null;

	private boolean identificado = false;

	private DialogoLogin dialogoLogin = new DialogoLogin(new JFrame(),
			"Introduzca su usuario y contraseña", false);

	private int tiempoSincronizacion = 4000;

	/**
	 * Raiz del arbol de documentos del sistema distribuido
	 */
	public static DefaultMutableTreeNode raiz = null;

	private ClienteFicheros cf = null;

	private static TokenFichero tk = null;

	/**
	 * Constructor con parametros
	 * 
	 * @param aplicacion
	 *            Nombre que hemos asignado a la aplicacion. Debe estar definida
	 *            en la metainformacion este mismo nombre
	 * 
	 * @param nombreJavaSpace
	 *            Nombre del JavaSpace a localizar
	 * 
	 */
	public DConector( String aplicacion, String nombreJavaSpace )
	{

		new String(nombreJavaSpace);

		DConector.Daplicacion = new String(aplicacion);

		DConector.dconector = this;
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param aplicacion
	 *            Nombre que hemos asignado a la aplicacion. Debe estar definida
	 *            en la metainformacion este mismo nombre
	 * 
	 */
	public DConector( String aplicacion )
	{

		new String("JavaSpace");

		DConector.Daplicacion = new String(aplicacion);

		DConector.dconector = this;

	}

	/**
	 * 
	 * Constructor con parametros
	 * 
	 * @param aplicacion
	 *            Nombre que hemos asignado a la aplicacion. Debe estar definida
	 *            en la metainformacion este mismo nombre
	 * 
	 * @param tiempoSincronizacion
	 *            Tiempo en segundos que se espera desde que se indica a los
	 *            componentes que envien sus peticiones de sincronizacion hasta
	 *            que se inician las hebras procesadoras de los componentes.
	 *            Debe ser suficiente para que de tiempo a que las instancias en
	 *            ejecucion respondan a las peticiones de sincronizacin. Por
	 *            defecto el tiempo es de 4 segundos
	 */
	public DConector( String aplicacion, int tiempoSincronizacion )
	{

		new String("JavaSpace");

		DConector.Daplicacion = new String(aplicacion);

		DConector.dconector = this;

		this.tiempoSincronizacion = tiempoSincronizacion;

	}

	/**
	 * Localiza el JavaSpace y sincroniza con el coordinador
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
			new HebraLocalizadora(this, "JavaSpace");

			/*
			 * Espera un tiempo la localizacion del JavaSpace. En caso de no
			 * localizarse en este tiempo lo indica. Asi evitamos que el
			 * programa se quede atrancado
			 */
			new HebraDetectoraError(this, 10);

			// Espera un evento sobre la inicializacion
			if (monitor.inicializacionCorrecta())
				dialogo.mostrar("Localizacion correcta", false);

			else
			{
				dialogo.mostrar("Error inicializando el JavaSpace", false);

				JOptionPane
						.showMessageDialog(
								null,
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
					cmi = new ClienteMetaInformacion(Daplicacion, Dusuario,
							Dclave);

				// cmi.desconectarUsuario(Dusuario); //
				// ****************************************
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
					Drol = new String(infoCompleta.rol);
					DrolDefecto = new String(infoCompleta.rol);
				}

				else
				{
					dialogo.mostrar("Error en la identificacion del usuario",
							false);

					JOptionPane.showMessageDialog(null,
							"Error en la identificacion del usuario:\n"
									+ infoCompleta.mensajeError, "Error",
							JOptionPane.ERROR_MESSAGE);
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

			else contador = tk.sec.intValue();

			tk = (Token) space.readIfExists(tkplantilla, null, tiempo);

			if (tk == null)
				txn.commit(); // Confirmamos escritura del token el el
			// JavaSpace
			else contador = tk.sec.intValue();

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

			raiz = cf.getArbolDoc();
		}

		catch (Exception e)
		{
			e.printStackTrace();

			JOptionPane.showMessageDialog(null,
					"Hubo un error en la comunicacion\nDebera identificarse de nuevo.1"
							+ e.getLocalizedMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		}
	}

	/**
	 * Obtiene la cola de eventos
	 * 
	 * @return Cola de eventos
	 */
	public static ColaEventos getColaEventos()
	{
		return cola;
	}

	/**
	 * Indica a cada componente que envie su peticion de sincronizacion si es
	 * necesario y posteriormente inicia sus hebras procesadoras.
	 */
	public void sincronizarComponentes()
	{
		sincronizarComponentes(null);
	}

	/**
	 * Indica a cada componente que envie su peticion de sincronizacion si es
	 * necesario y posteriormente inicia sus hebras procesadoras.
	 * 
	 * @param frame
	 *            Ventana a la que realizar el metodo
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
						"No se pudo sincronizar la sincronizacion\n", "Error",
						JOptionPane.ERROR_MESSAGE);

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
			componente = v.elementAt(i);
			componente.sincronizar();
		}

		try
		{
			Thread.sleep(tiempoSincronizacion);
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Iniciamos las hebras procesadoras de eventos de cada componente
		for (int i = 0; i < v.size(); i++)
		{
			componente = v.elementAt(i);
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
		DMIEvent broadCast = new DMIEvent();
		broadCast.tipo = new Integer(DMIEvent.INFO_COMPLETA.intValue());
		broadCast.infoCompleta = infoCompleta;
		broadCastMI(broadCast);

		dialogo.ocultar();
	}

	/**
	 * Sale de la aplicacion. Siempre es necesario llamarlo para que la
	 * desconexion del JavaSpace sea la adecuada
	 */
	public void salir()
	{
		ClienteMetaInformacion.obtenerCMI().desconectarUsuario(Dusuario);
		System.exit(0);
	}

	/**
	 * Obtiene la instacion del DConector en ejecucion
	 * 
	 * @return DConector en ejecucion
	 */
	public static DConector obtenerDC()
	{
		return dconector;
	}

	/**
	 * Obtiene el conjunto de usuarios que editan un documento del sistema
	 * 
	 * @param path
	 *            Path del documento en edicion
	 * @return Vector con los nombres de usuario que editan un documento
	 */
	public Vector<String> consultarEditores(String path)
	{
		TokenFichero plantilla = new TokenFichero();
		plantilla.Fichero = new String(path);
		plantilla.aplicacion = new String(Daplicacion + "_");

		try
		{
			tk = (TokenFichero) space.readIfExists(plantilla, null, 500L);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (UnusableEntryException e)
		{
			return null;
		}
		catch (TransactionException e)
		{
			return null;
		}
		catch (InterruptedException e)
		{
			return null;
		}

		if (tk == null)
			return null;

		else return tk.editores;
	}

	/**
	 * Comprueba si el token correspondiente al fichero se encuentra en el JS
	 * 
	 * @param fichero
	 *            Nombre del fichero
	 * @return True si el token esta en el JS. False en caso contrario
	 */
	public boolean leerToken(String fichero)
	{
		// 1. Buscamos el token asociado al fichero
		TokenFichero plantilla = new TokenFichero();
		plantilla.Fichero = new String(fichero);
		plantilla.aplicacion = new String(Daplicacion + "_");

		try
		{
			tk = (TokenFichero) space.take(plantilla, null, 2000L);
		}
		catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (UnusableEntryException e)
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
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		// 2. Si no existe:
		// 2.1. Creamos el token
		// 2.2. Devolvemos false
		if (tk == null)
		{

			// System.err.println("El token para el fichero " + fichero
			// + " no existe: creando nuevo token");

			tk = new TokenFichero();
			tk.Fichero = new String(fichero);
			tk.aplicacion = new String(Daplicacion + "_");
			tk.sec = new Long(1L);
			tk.editores = new Vector<String>();
			tk.nuevoUsuario(DConector.Dusuario);

			return false;
		}
		// 3. Si existe el token
		// 3.1. Incrementamos el numero de usuarios que actualmente estan
		// anotando en el documento
		// 3.2. Devolvemos true
		else
		{
			// System.err.println("El token para el " + fichero
			// + " existe: incrementando el contador de usuarios");
			tk.nuevoUsuario(DConector.Dusuario);
			tk.sec = new Long(tk.sec.longValue() + 1L);
			return true;
		}
	}

	/**
	 * Escribe un token para la edicion de documentos
	 * 
	 * @return True si se pudo escribir el token. False en caso contrario
	 */
	public boolean escribirToken()
	{
		try
		{
			if (tk != null)
			{
				space.write(tk, null, Lease.FOREVER);
				System.err.println("escribiendo el token");
				return true;
			}
			else
			{
				// System.err
				// .println("ERROR: NO SE HA PODIDO ESCRIBIR EL TOKEN!!!");
				return false;
			}
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (TransactionException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Termina la edicion de un fichero
	 * 
	 * @param fichero
	 *            Nombre del fichero
	 * @return True si se cerro correctamente. False en caso contrario
	 */
	public boolean cerrarFichero(String fichero)
	{
		// 1. Buscamos el token asociado al fichero
		TokenFichero plantilla = new TokenFichero();
		plantilla.Fichero = new String(fichero);
		plantilla.aplicacion = new String(Daplicacion + "_");

		// System.err.println("Decrementando el numero de usuarios del
		// documento"
		// + fichero);
		try
		{
			// buscamos el token en el JS
			tk = (TokenFichero) space.take(plantilla, null, 1000L);

			// Si existe el token:
			if (tk != null)
			{
				// System.err.println("Numero de usuarios activos "
				// + ( tk.editores.size() - 1 ));

				// si todavia quedan usuarios editando ese documento
				if (tk.editores.size() > 1)
				{
					tk.bajaUsuario(Dusuario);
					space.write(tk, null, Lease.FOREVER);
				}
				return true;
			}
			else return false;
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
		catch (UnusableEntryException e)
		{
			e.printStackTrace();
		}
		catch (TransactionException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Hace un broadcast de un evento de metainformacion
	 * 
	 * @param evento
	 *            Evento a enviar
	 */
	private void broadCastMI(DMIEvent evento)
	{

		// System.out.println("boadCastMI() tipo=" + evento.tipo);

		DComponente dc = null;

		for (int j = 0; j < v.size(); j++)
		{

			dc = v.elementAt(j);

			dc.procesarMetaInformacion(evento);

		}

	}

	/**
	 * Da de alta un componente en el sistema
	 * 
	 * @param componente
	 *            Componente a dar de alta
	 * @return Numero de componentes dados de alta en el sistema
	 */
	public static int alta(DComponente componente)
	{
		// System.out.println("Añadido. Tamaño=" + v.size());
		DComponente c = null;

		// Comprobamos si ya existe un componente con este nombre
		for (int i = 0; i < v.size(); i++)
		{
			c = v.elementAt(i);

			if (c.getNombre().equals(componente.getNombre()))
			{
				JOptionPane
						.showMessageDialog(
								null,
								"Ya existe un componente con Nombre = "
										+ c.getNombre()
										+ "\nCambie alguno de los nombres para un correcto funcionamiento",
								"Error", JOptionPane.ERROR_MESSAGE);

				DConector.obtenerDC().salir();
				System.exit(1);
			}
		}

		v.add(componente);

		// componente.desactivar();
		return v.size();
	}

	/**
	 * Hebra de localizacion del JavaSpace
	 */
	private class HebraLocalizadora implements Runnable
	{
		private DConector conector = null;

		private String nombre = null;

		private Thread t = null;

		/**
		 * Constructor
		 * 
		 * @param conector
		 *            Clase desde la que se llama a la hebra
		 * @param nombreJavaSpace
		 *            Nombre del JavaSpace que deseamos localizar
		 */
		public HebraLocalizadora( DConector conector, String nombreJavaSpace )
		{
			this.conector = conector;
			this.nombre = nombreJavaSpace;
			t = new Thread(this);
			t.start();
		}

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			try
			{
				// Intentamos localizar el JavaSpace
				conector.space = SpaceLocator.getSpace(nombre);
				// Intentamos localizar un manejador de transacciones
				txnMgr = (TransactionManager) ServiceLocator
						.getService(TransactionManager.class);
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
	 * Mediante esta clase podemos indicar que no ha sido posible localizar el
	 * JavaSpace pasado el tiempo indicado. De esta forma evitamos que el
	 * programa se quede bloqueado en la fase de localizacion del JavasPace
	 */
	private class HebraDetectoraError implements Runnable
	{
		private DConector conector = null;

		private int tiempoEspera = -1;

		private Thread t = null;

		/**
		 * Constructor
		 * 
		 * @param conector
		 *            Clase que crea esta hebra
		 * @param tiempoEspera
		 *            Tiempo que deseamos esperar
		 */
		public HebraDetectoraError( DConector conector, int tiempoEspera )
		{
			this.conector = conector;
			this.tiempoEspera = tiempoEspera;
			t = new Thread(this);
			t.start();
		}

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			try
			{
				Thread.sleep(tiempoEspera * 1000);

				if (!conector.monitor.getInicializado())
					conector.monitor.setError(true);
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Dado el comportamiento concurrente de las 2 Hebras mediante este Monitor
	 * gestionamos la informacion sobre la inicializacion. La clase DConector
	 * realizara una llamada a inicializacionCorrecta() quedandose bloqueada
	 * hasta que se producza la correcta localizacion del JavaSpace o se
	 * sobrepase el tiempo de espera sin haber sido localizado.
	 */
	private class Monitor
	{
		private boolean inicializado = false;

		private boolean error = false;

		private boolean sincronizado = false;

		/**
		 * Bloquea al llamante hasta que se produzca una actualizacion de las
		 * variables inicializado o error
		 * 
		 * @return True si se ha producido la inicializacion. False en otro caso
		 */
		public synchronized boolean inicializacionCorrecta()
		{
			try
			{
				while (!inicializado && !error)
					wait();
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}

			return inicializado;
		}

		/**
		 * Devuelve si se ha sobrepasado el tiempo de espera sin haber sido
		 * localizado el JavaSpace
		 * 
		 * @return True si ocurrio un error al localizar el JavaSpace. False en
		 *         otro caso
		 */
		public synchronized boolean getError()
		{
			return error;
		}

		/**
		 * Cambiamos el valor de la variable de error
		 * 
		 * @param b
		 *            Valor deseado
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
		 * Comprueba si se ha sincronizado con exito
		 * 
		 * @return True si se ha sincronizado con exito. False en caso contrario
		 */
		public synchronized boolean getSincronizado()
		{
			return sincronizado;
		}

		/**
		 * Permite cambiar el valor de la variable de sincronizado
		 * 
		 * @param b
		 *            Valor deseado
		 */
		public synchronized void setSincronizado(boolean b)
		{
			sincronizado = b;
			notifyAll();
		}
	}

	/**
	 * Consume los eventos de la cola de eventos
	 */
	private class HebraConsumidora implements Runnable
	{
		private ColaEventos cola = null;

		private JavaSpace space = null;

		/**
		 * Constructor
		 * 
		 * @param cola
		 *            Cola de eventos
		 * @param space
		 *            JavaSpace asociado a la cola de eventos
		 */
		public HebraConsumidora( ColaEventos cola, JavaSpace space )
		{
			this.cola = cola;
			this.space = space;
		}

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			DEvent evento = null;
			Token tkplantilla = new Token();
			Token tk = null;
			tkplantilla.aplicacion = new String(Daplicacion);
			long numSalida = -1;

			while (true)
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
									+ e.getLocalizedMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);

					DConector.obtenerDC().salir();
					System.exit(1);
				}
		}
	}

	/**
	 * Permite recopilar los eventos producidos
	 */
	private class HebraRecolectora implements Runnable
	{
		ColaEventos cola = null;

		private JavaSpace space = null;

		/**
		 * Constructor
		 * 
		 * @param cola
		 *            Cola de eventos
		 * @param space
		 *            JavaSpace asociado
		 */
		public HebraRecolectora( ColaEventos cola, JavaSpace space )
		{
			this.cola = cola;
			this.space = space;
		}

		/**
		 * Ejecucion de la hebra
		 */
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
				try
				{
					plantilla.contador = new Long(contador);

					// System.out.println("HebraRecolectora: Esperando evento
					// "+contador);

					evento = (DEvent) space.read(plantilla, null,
							Long.MAX_VALUE);

					System.out.println("DConector: Leido evento " + evento);
					contador++;

					for (int i = 0; i < v.size(); i++)
					{
						componente = v.elementAt(i);
						if (componente.getID().intValue() == evento.componente
								.intValue()) componente.procesarEvento(evento);
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

					DConector.obtenerDC().salir();
					System.exit(1);
				}
		}
	}

	/**
	 * Listener para los eventos de metainformacion
	 */
	private class ListenerMI implements DMIListener
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
					.intValue());
			evento.usuario = new String(usuario);
			broadCastMI(evento);
			// System.out.println("Desconexion usuario");
		}

		public void cambioRolUsuario(String usuario, String rol,
				String rolAntiguo, MICompleta info)
		{
			if (usuario.equals(Dusuario)) Drol = info.rol;

			DMIEvent evento = new DMIEvent();
			evento.tipo = new Integer(DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO
					.intValue());
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
			evento.tipo = new Integer(
					DMIEvent.NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_USUARIO
							.intValue());
			evento.usuario = new String(usuario);
			evento.componente = new String(componente);
			evento.permiso = new Integer(permiso);
			broadCastMI(evento);
		}

		public void cambioPermisoComponenteRol(String rol, String componente,
				int permiso)
		{
			DMIEvent evento = new DMIEvent();
			evento.tipo = new Integer(
					DMIEvent.NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_ROL
							.intValue());
			evento.rol = new String(rol);
			evento.componente = new String(componente);
			evento.permiso = new Integer(permiso);
			broadCastMI(evento);
		}

		public void nuevoRolPermitido(String usuario, String rol)
		{
			DMIEvent evento = new DMIEvent();
			evento.tipo = new Integer(DMIEvent.NOTIFICACION_NUEVO_ROL_PERMITIDO
					.intValue());
			evento.usuario = new String(usuario);
			evento.rol = new String(rol);
			broadCastMI(evento);
		}

		public void eliminarRolPermitido(String usuario, String rol)
		{
			if (usuario.equals(Dusuario) && rol.equals(Drol))
				ClienteMetaInformacion.obtenerCMI().cambiarRolUsuario(Dusuario,
						DrolDefecto);

			DMIEvent evento = new DMIEvent();
			evento.tipo = new Integer(
					DMIEvent.NOTIFICACION_ELIMINAR_ROL_PERMITIVO.intValue());
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

				DConector.obtenerDC().salir();
				System.exit(0);
			}

			else
			{
				DMIEvent evento = new DMIEvent();
				evento.tipo = new Integer(
						DMIEvent.NOTIFICACION_USUARIO_ELIMINADO.intValue());
				evento.usuario = new String(usuario);
				broadCastMI(evento);
			}
		}

		public void rolEliminado(String rol)
		{
			if (rol.equals(Drol))
				ClienteMetaInformacion.obtenerCMI().cambiarRolUsuario(Dusuario,
						DrolDefecto);

			DMIEvent evento = new DMIEvent();
			evento.tipo = new Integer(DMIEvent.NOTIFICACION_ROL_ELIMINADO
					.intValue());
			evento.rol = new String(rol);
			broadCastMI(evento);
		}
	}
}
