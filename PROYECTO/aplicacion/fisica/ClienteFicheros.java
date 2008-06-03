package aplicacion.fisica;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javaspaces.SpaceLocator;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import Deventos.DEvent;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.documentos.MIDocumento;
import aplicacion.fisica.eventos.DFileEvent;
import aplicacion.fisica.eventos.DNodeEvent;

/**
 * Cliente del modulo de ficheros
 */
public class ClienteFicheros
{
	public static ClienteFicheros cf = null;

	private static String usuario = null;

	private static String aplicacion = null;

	private static String rol = null;

	private static final long leaseWriteTime = Lease.FOREVER;

	private static final long leaseReadTime = 10000L;

	private JavaSpace space = null;

	private Monitor monitor = new Monitor();

	private DefaultMutableTreeNode raiz = null;

	public static String ipConexion = null;

	@SuppressWarnings( "static-access" )
	public ClienteFicheros( String aplicacion2, String usuario2, String clave2,
			String rol2 )
	{

		this.aplicacion = new String(aplicacion2);
		this.usuario = new String(usuario2);
		this.rol = new String(rol2);
		localizarJavaSpace();
		cf = this;

		inicializar();
	}

	/**
	 * Obtiene el arbol de documentos asociado al usuario+rol
	 * @return la raiz del arbol
	 */
	public DefaultMutableTreeNode getArbolDoc()
	{
		return raiz;
	}
	
	private void localizarJavaSpace()
	{
		// Encargada de localizar el JavaSpace
		Thread t1 = new Thread(new HebraLocalizadora("JavaSpace"));
		t1.start();
		// Espera un tiempo la localizacion del JavaSpace. En caso de no
		// localizarse en este tiempo lo indica. Asi evitamos que el
		// programa se quede bloqueado
		Thread t2 = new Thread(new HebraDetectoraError(10));
		t2.start();
		// Espera un evento sobre la inicializacion
		if (monitor.inicializacionCorrecta())
			inicializar();
		// System.out.println("Localizacion correcta");
		else // System.out.println("Error localizando el JavaSpace");{
		{
			DConector.obtenerDC().salir();
			System.exit(1);
		}
	}

	public static ClienteFicheros obtenerClienteFicheros()
	{
		return cf;
	}

	/**
	 * inicializa el cliente enviando una solicitud de sincronizaci�n al
	 * servidor de Metainformaci�n
	 */
	public void inicializar()
	{
		try
		{
			// ********** SINCRONIZACION CON EL SERVIDOR DE ficheros *********//

			// evento en el que se almacenar� la respuesta del servidor a la
			// petici�n de sincronizaci�n del SF
			DNodeEvent leido = null;

			// evento que ser� enviado al servidor para solicitar
			// sincronizaci�n
			DEvent evento = new DEvent();

			// evento "template" utilizado para leer la respuesta a la
			// petici�n
			// de sincronizaci�n del servidor
			DNodeEvent plantilla = new DNodeEvent();

			// origen y destino del evento
			evento.origen = new Integer(31); // Cliente ficheros
			evento.destino = new Integer(30); // Servidor ficheros

			// tipo de evento
			evento.tipo = new Integer(DNodeEvent.SINCRONIZACION.intValue());
			evento.aplicacion = new String(aplicacion);
			evento.usuario = new String(usuario);
			evento.rol = new String(rol);

			// escribimos en el JavaSpace la solicitud
			space.write(evento, null, leaseWriteTime);

			// inicializamos la plantilla
			plantilla.origen = new Integer(30); // Servidor ficheros
			plantilla.destino = new Integer(31); // Cliente ficheros
			plantilla.tipo = new Integer(DNodeEvent.RESPUESTA_SINCRONIZACION
					.intValue());

			// leemos la respuesta a la solicitud
			leido = (DNodeEvent) space.take(plantilla, null, leaseReadTime);

			if (leido == null)
			{ // Sin respuesta del coordinador
				JOptionPane.showMessageDialog(null,
						"Error sincronizando con el Servidor de ficheros",
						"Error", JOptionPane.ERROR_MESSAGE);
				DConector.obtenerDC().salir();
				System.exit(1);
			}
			else
			{
				// contador = leido.contador.longValue();

				raiz = leido.raiz;
				ClienteFicheros.ipConexion = leido.direccionRMI;
				// hr = new Thread(new HebraRecolectora());
				// hr.start();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion en el cliente de fichero\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			DConector.obtenerDC().salir();
			System.exit(1);
		}
	}

	/**
	 * Envia un evento al servidor de Ficheros para que este elimine un fichero de la BD y del sistema de archivos
	 * @param f metainformacion del documento a borrar
	 * @param aplicacion aplicacion desde la que se solicita borrar el documento
	 */
	public void borrarFichero(MIDocumento f, String aplicacion)
	{
		try
		{
			DFileEvent evt = new DFileEvent();
			// origen y destino del evento
			evt.origen = new Integer(31); // Cliente ficheros
			evt.destino = new Integer(30); // Servidor ficheros

			// tipo de evento
			evt.tipo = new Integer(DFileEvent.NOTIFICAR_ELIMINAR_FICHERO
					.intValue());
			evt.aplicacion = new String(aplicacion);
			evt.usuario = new String(usuario);
			evt.rol = new String(rol);
			evt.fichero = f;

			space.write(evt, null, leaseWriteTime);
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
	}

	/**
	 * Envia un evento al servidor de Ficheros para que este agrege un fichero a la BD
	 * @param f metainformacion del documento
	 * @param aplicacion aplicacion desde la que se solicita borrar el documento
	 * @return metainformacion del documento borrado, actualizada con nuevos datos
	 */
	public MIDocumento insertarNuevoFichero(MIDocumento f, String aplicacion)
	{
		try
		{
			DFileEvent evt = new DFileEvent();
			DFileEvent plantilla = new DFileEvent(), leido;
			
			
			// origen y destino del evento
			evt.origen = new Integer(31); // Cliente ficheros
			evt.destino = new Integer(30); // Servidor ficheros

			// tipo de evento
			evt.tipo = new Integer(DFileEvent.NOTIFICAR_INSERTAR_FICHERO
					.intValue());
			evt.aplicacion = new String(aplicacion);
			evt.usuario = new String(usuario);
			evt.rol = new String(rol);
			evt.fichero = f;

			space.write(evt, null, leaseWriteTime);
			
//			 inicializamos la plantilla
			plantilla.origen = new Integer(30); // Servidor ficheros
			plantilla.destino = new Integer(31); // Cliente ficheros
			plantilla.tipo = new Integer(DFileEvent.RESPUESTA_INSERTAR_FICHERO
					.intValue());

			// leemos la respuesta a la solicitud
			leido = (DFileEvent) space.take(plantilla, null, leaseReadTime);

			if (leido == null)
			{ // Sin respuesta del coordinador
				return null;
			}
			else
			{
				return leido.fichero;
			}
		}
		catch (Exception e)
		{
			return null;
		}
	}

	
	/**
	 * Comprueba si existe un determinado fichero
	 * @param path path del fichero en el servidor
	 * @param aplicacion aplicacion desde la que se hace la solicitud
	 * @return la metainformacion del fichero si este existe y null en caso contrario
	 */
	public MIDocumento existeFichero(String  path, String aplicacion) {
		try
		{
			DFileEvent evt = new DFileEvent();
			DFileEvent plantilla = new DFileEvent(), leido;
			
			
			// origen y destino del evento
			evt.origen = new Integer(31); // Cliente ficheros
			evt.destino = new Integer(30); // Servidor ficheros

			// tipo de evento
			evt.tipo = new Integer(DFileEvent.EXISTE_FICHERO
					.intValue());
			evt.aplicacion = new String(aplicacion);
			evt.usuario = new String(usuario);
			evt.rol = new String(rol);
			evt.fichero = null;
			evt.path = new String(path);
			
			space.write(evt, null, leaseWriteTime);
			
			//inicializamos la plantilla
			plantilla.origen = new Integer(30); // Servidor ficheros
			plantilla.destino = new Integer(31); // Cliente ficheros
			plantilla.tipo = new Integer(DFileEvent.RESPUESTA_EXISTE_FICHERO
					.intValue());

			// leemos la respuesta a la solicitud
			leido = (DFileEvent) space.take(plantilla, null, leaseReadTime);

			if (leido == null)
			{ // Sin respuesta del coordinador
				System.err.println("Sin respuesta del coordinador");
				return null;
			}
			else
			{
				if (leido.fichero.getId() != -100) {
					System.err.println("encontrado fichero");
					return leido.fichero;
				}
				else {
					System.err.println("fichero no encontrado");
					return null;
				}
			}
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Modifica los datos de un fichero en la BD
	 * @param f metainformacion nueva
	 * @param aplicacion aplicacion desde la cual se hace la solicitud
	 */
	public void modificarFichero(MIDocumento f, String aplicacion)
	{
		try
		{
			DFileEvent evt = new DFileEvent();
			// origen y destino del evento
			evt.origen = new Integer(31); // Cliente ficheros
			evt.destino = new Integer(30); // Servidor ficheros

			// tipo de evento
			evt.tipo = new Integer(DFileEvent.NOTIFICAR_MODIFICACION_FICHERO
					.intValue());
			evt.aplicacion = new String(aplicacion);
			evt.usuario = new String(usuario);
			evt.rol = new String(rol);
			evt.fichero = f;

			space.write(evt, null, leaseWriteTime);
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
	}

	
	public void generarVersion(MIDocumento doc, String path) {
		Date fecha = new Date();

		SimpleDateFormat formato = new SimpleDateFormat(
				"dd-MM-yy_hh:mm:ss");

		String name = doc.getNombre().substring(0,
				doc.getNombre().lastIndexOf('.'));
		String extension = doc.getNombre().substring(
				doc.getNombre().lastIndexOf('.') + 1,
				doc.getNombre().length());

		String fe = formato.format(fecha);

		String nombreVersion = name + "_" + fe + "." + extension;

		doc.setNombre(nombreVersion);

		doc.setRutaLocal(path + nombreVersion);

		doc.setTipo("VER");
		
		ClienteFicheros.cf.modificarFichero(doc,
				DConector.Daplicacion);
	}
	

	/**
	 * Encargada de localizar el JavaSpace
	 */
	private class HebraLocalizadora implements Runnable
	{
		String nombre = null;

		Thread t = null;

		int i = 0;

		/**
		 * @param nombreJavaSpace
		 *            String Nombre del JavaSpace que deseamos localizar
		 */
		public HebraLocalizadora( String nombreJavaSpace )
		{
			this.nombre = nombreJavaSpace;

			t = new Thread(this);
			t.start();
		}

		/**
		 * Metodo que ejecuta la hebra
		 */
		public void run()
		{
			try
			{
				// Intentamos localizar el JavaSpace
				space = SpaceLocator.getSpace(nombre);
				// Localizacion del JavaSpace exitosa
				monitor.setInicializado(true);
			}
			catch (Exception e)
			{
				JOptionPane
						.showMessageDialog(
								null,
								"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
								"Error", JOptionPane.ERROR_MESSAGE);
				DConector.obtenerDC().salir();
				System.exit(1);
			}
		}
	}

	/**
	 * 
	 * Mediante esta clase podemos indicar que no ha sido posible localizar el
	 * JavaSpace pasado el tiempo indicado. De esta forma evitamos que el
	 * programa se quede bloqueado en la fase de localizacion del JavasPace
	 */
	private class HebraDetectoraError implements Runnable
	{

		int tiempoEspera = -1;

		Thread t = null;

		/**
		 * @param tiempoEspera
		 *            int Tiempo que deseamos esperar
		 */
		public HebraDetectoraError( int tiempoEspera )
		{
			this.tiempoEspera = tiempoEspera;

			t = new Thread(this);
			t.start();
		}

		/**
		 * Metodo que ejecuta la hebra
		 */
		public void run()
		{
			try
			{
				Thread.sleep(tiempoEspera * 1000);
				if (!monitor.getInicializado()) monitor.setError(true);
			}
			catch (Exception e)
			{
				JOptionPane
						.showMessageDialog(
								null,
								"Hubo un error en la comunicacion en el cliente de ficheros\nDebera identificarse de nuevo.",
								"Error", JOptionPane.ERROR_MESSAGE);
				DConector.obtenerDC().salir();
				System.exit(1);
			}
		}
	}

	/**
	 * Dado el comportamiento concurrente de las 2 Hebras mediante este Monitor
	 * gestionamos la informacion sobre la inicializacion. La clase DConector
	 * realizara una llamada a inicializacionCorrecta() qued�ndose bloqueada
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
		 * @return boolean Devuelve si se ha producido o no la inicializacion
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
				JOptionPane
						.showMessageDialog(
								null,
								"Hubo un error en la comunicacion en el cliente de ficheros\nDebera identificarse de nuevo.",
								"Error", JOptionPane.ERROR_MESSAGE);
				DConector.obtenerDC().salir();
				System.exit(1);
			}

			return inicializado;
		}

		/**
		 * Devuelve si se ha sobrepasado el tiempo de espera sin haber sido
		 * localizado el JavaSpace
		 */
		public synchronized boolean getError()
		{
			return error;
		}

		/**
		 * Cambamos el valor d ela variable error
		 * 
		 * @param b
		 *            boolean Valor deseado
		 */
		public synchronized void setError(boolean b)
		{
			error = b;
			notifyAll();
		}

		/**
		 * Devuelve si se ha localizado el JavaSpace
		 */
		public synchronized boolean getInicializado()
		{
			return inicializado;
		}

		/**
		 * Cambiamos el valor de la variable inicializado
		 * 
		 * @param b
		 *            boolean Valor deseado
		 */
		public synchronized void setInicializado(boolean b)
		{
			inicializado = b;
			notifyAll();
		}

		/**
		 * Devuelve si se ha sincronizado
		 */
		public synchronized boolean getSincronizado()
		{
			return sincronizado;
		}

		/**
		 * Cambiamos el valor de la variable sincronizado
		 * 
		 * @param b
		 *            boolean Valor deseado
		 */
		public synchronized void setSincronizado(boolean b)
		{
			sincronizado = b;
			notifyAll();
		}
	}

}
