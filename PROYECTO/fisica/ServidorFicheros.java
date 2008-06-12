package fisica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;

import javaspaces.SpaceLocator;

import javax.swing.tree.DefaultMutableTreeNode;

import fisica.documentos.MIDocumento;
import fisica.eventos.DFileEvent;
import fisica.eventos.DNodeEvent;
import fisica.net.Transfer;

import Deventos.ColaEventos;
import Deventos.DEvent;

import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;
import util.ParserPermisos;

/**
 * Clase que implementa al servidor de ficheros
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz.
 */
public class ServidorFicheros
{
	GestorFicherosBD gestor = null;

	JavaSpace space = null;

	Thread hebraProcesadora = null;

	Thread hebraEnvio = null;

	Thread hebraDesconexionUsuarios = null;

	ColaEventos colaRecepcion = new ColaEventos();

	ColaEventos colaEnvio = new ColaEventos();

	long contador = 0;

	private static long leaseWriteTime = Lease.FOREVER;

	private static long leaseReadTime = Long.MAX_VALUE;

	private static String directorioBase = "data";

	/**
	 * Devuelve el directorio en el que se almacenan los ficheros y directorios
	 * localmente. Todas las peticiones a '/' que se hagan al servidor, seran
	 * automaticamente redirigidas a este directorio fisico.
	 * 
	 * @return Directorio donde se estan almacenando los documentos y
	 *         directorios del servidor en el ordenador local.
	 */
	public static String getDirectorioBase()
	{
		return directorioBase;
	}

	/**
	 * Constructor por defecto
	 */
	public ServidorFicheros()
	{

		FrameServFich.println("");

		File f = new File("config");
		FileReader fr = null;
		try
		{
			fr = new FileReader(f);
		}
		catch (FileNotFoundException e)
		{
			System.err
					.println("Error en apertura de fichero. No se ha encontrado el fichero .config");
			System.exit(1);
		}
		BufferedReader br = new BufferedReader(fr);
		try
		{
			String datos;

			datos = br.readLine();

			String data[] = datos.split(" ");

			if (data.length != 2)
			{
				System.err
						.println("Error en lectura de fichero de configuracion de directorio");
				System.exit(1);
			}

			directorioBase = data[1];
		}
		catch (IOException e)
		{
			System.err
					.println("Error en lectura de fichero: " + e.getMessage());
			System.exit(2);
		}

		gestor = new GestorFicherosBD();
		FrameServFich.println("ServidorFicheros: Almacen de ficheros creado");
		FrameServFich.println("ServidorFicheros: Localizando JavaSpace");
		try
		{
			space = SpaceLocator.getSpace("JavaSpace");
			FrameServFich.println("ServidorFicheros: JavaSpace localizado");
		}
		catch (Exception e)
		{
			FrameServFich
					.println("ServidorFicheros: Error localizando JavaSpace  "
							+ e.getMessage());
			System.exit(1);
		}

		hebraProcesadora = new Thread(new HebraProcesadora());
		FrameServFich.println("ServidorFicheros: HebraProcesadora creada");
		hebraProcesadora.start();
		FrameServFich.println("ServidorFicheros: HebraProcesadora iniciada");
		hebraEnvio = new Thread(new HebraEnvio());
		FrameServFich.println("ServidorFicheros: HebraEnvio creada");
		hebraEnvio.start();
		FrameServFich.println("ServidorFicheros: HebraEnvio iniciada");
		FrameServFich
				.println("ServidorFicheros: HebraDesconexionUsuarios iniciada");

		Transfer.establecerServidor();
	}

	/**
	 * Recupera los ficheros a los que un usuario bajo un determinado rol tiene
	 * acceso
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param rol
	 *            Nombre del rol que desempeña el usuario en estos momentos
	 * @return Arbol con la estructura de directorios
	 */
	private DefaultMutableTreeNode obtenerArbol(String usuario, String rol)
	{

		if (gestor == null) gestor = new GestorFicherosBD();

		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode();

		Vector<MIDocumento> fich = gestor.recuperar();

		this.agregarFichero(fich.get(0), raiz, usuario, rol);

		return raiz;
	}

	/**
	 * Agrega un fichero al arbol con la estructura de directorios
	 * 
	 * @param f
	 *            Documento que se desea agregar
	 * @param padre
	 *            Directorio padre que tendra el documento
	 * @param usuario
	 *            Usuario propietario del documento
	 * @param rol
	 *            Rol con el que el usuario creo el documento
	 */
	private void agregarFichero(MIDocumento f, DefaultMutableTreeNode padre,
			String usuario, String rol)
	{

		// si es una version la ocultamos al usuario
		if (f.getTipo() != null && f.getTipo().equals("VER")) return;

		// agregamos una nueva hoja al arbol
		if (ParserPermisos.comprobarPermisoLectura(f, usuario, rol))
		{

			DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(f);

			if (f.esDirectorio())
			{
				Vector<MIDocumento> fs = gestor.recuperarDirectorio(f.getId());

				for (int i = 1; i < fs.size(); ++i)
					agregarFichero(fs.get(i), nodo, usuario, rol);
			}

			padre.add(nodo);
		}
	}

	/**
	 * Hebra encargada de procesar los eventos recibidos
	 */
	private class HebraProcesadora implements Runnable
	{
		DEvent leido = null;

		DEvent plantilla = new DEvent();

		/**
		 * Ejecucion de la hebra.
		 */
		public void run()
		{
			plantilla.destino = new Integer(30); // Servidor ficheros
			// /plantilla.tipo = null;

			while (true)
			{
				try
				{

					FrameServFich.println("preparado para leer evento");

					leido = (DEvent) space.take(plantilla, null, leaseReadTime);
					FrameServFich.println("ServidorFicheros: evento leido: "
							+ leido);

					if (leido.tipo.intValue() == DNodeEvent.SINCRONIZACION
							.intValue())
					{
						FrameServFich
								.println("Recibido evento de sincronizcion ficheros");

						DNodeEvent nuevo = new DNodeEvent();

						nuevo.tipo = new Integer(
								DNodeEvent.RESPUESTA_SINCRONIZACION.intValue());
						nuevo.origen = new Integer(30);
						nuevo.destino = new Integer(31);
						nuevo.aplicacion = new String(leido.aplicacion);
						nuevo.raiz = obtenerArbol(leido.usuario, leido.rol);
						nuevo.direccionRMI = new String(InetAddress
								.getLocalHost().getHostName());

						colaEnvio.nuevoEvento(nuevo);
					}
					else if (leido.tipo.intValue() == DFileEvent.NOTIFICAR_INSERTAR_FICHERO
							.intValue())
					{

						FrameServFich
								.println("Leido evento insercion nuevo fichero");

						if (gestor == null) gestor = new GestorFicherosBD();

						DFileEvent evt = (DFileEvent) leido;

						if (evt.fichero.esDirectorio())
						{
							File f = new File(directorioBase
									+ evt.fichero.getRutaLocal());
							f.mkdir();
							System.err.println("Carpeta creada");
						}

						MIDocumento res = gestor
								.insertarNuevoFichero(evt.fichero);

						DFileEvent nuevo = new DFileEvent();

						nuevo.tipo = new Integer(
								DFileEvent.RESPUESTA_INSERTAR_FICHERO);
						nuevo.origen = new Integer(30);
						nuevo.destino = new Integer(31);
						nuevo.aplicacion = new String(leido.aplicacion);
						nuevo.fichero = res;

						if (res != null)
							nuevo.res = new Boolean(true);
						else nuevo.res = new Boolean(false);

						colaEnvio.nuevoEvento(nuevo);

					}
					else if (leido.tipo.intValue() == DFileEvent.NOTIFICAR_ELIMINAR_FICHERO
							.intValue())
					{
						FrameServFich
								.println("Leido evento eliminacion  fichero");

						if (gestor == null) gestor = new GestorFicherosBD();

						gestor.eliminarFichero(( (DFileEvent) leido ).fichero);

						String path = ( (DFileEvent) leido ).fichero
								.getRutaLocal();

						FrameServFich.println("Fichero a borrar: " + path);

						File f = new File(directorioBase + path);

						if (!( (DFileEvent) leido ).fichero.esDirectorio())
						{
							File fAnot = new File(directorioBase + path
									+ ".anot");

							if (fAnot.exists() && !fAnot.delete())
								System.err
										.println("Error borrando el archivo de anotaciones: "
												+ path);
						}

						if (!f.delete())
							System.err.println("Error borrando el archivo: "
									+ path);

					}
					else if (leido.tipo.intValue() == DFileEvent.NOTIFICAR_MODIFICACION_FICHERO
							.intValue())
					{
						FrameServFich
								.println("Leido evento modificacion  fichero");

						if (gestor == null) gestor = new GestorFicherosBD();

						int id = ( (DFileEvent) leido ).fichero.getId();

						// cambiar el nombre del fichero
						String old = gestor.buscarFichero(id).getRutaLocal();
						String new_ = ( (DFileEvent) leido ).fichero
								.getRutaLocal();
						File f = new File(directorioBase + old);

						File f2 = new File(directorioBase + new_);

						System.err.println("Ruta local: " + old);
						System.err.println("Ruta nueva " + new_);

						if (!f.renameTo(f2))
							System.err.println("Error al renombrar el fichero");
						else
						{

							f = new File(directorioBase + old + ".anot");
							f2 = new File(directorioBase + new_ + ".anot");

							if (!f.renameTo(f2))
								System.err
										.println("Error al renombrar el fichero de anotaciones");
							else System.err
									.println("Fichero renombrado con éxito");

						}
						gestor.modificarFichero(( (DFileEvent) leido ).fichero);
					}

					else if (leido.tipo.intValue() == DFileEvent.EXISTE_FICHERO
							.intValue())
					{
						System.err.println("Leida solicitud info fichero");

						if (gestor == null) gestor = new GestorFicherosBD();

						// cambiar el nombre del fichero
						MIDocumento f = gestor
								.buscarFicheroPath(( (DFileEvent) leido ).path);

						DFileEvent nuevo = new DFileEvent();

						nuevo.tipo = new Integer(
								DFileEvent.RESPUESTA_EXISTE_FICHERO);
						nuevo.origen = new Integer(30);
						nuevo.destino = new Integer(31);
						nuevo.aplicacion = new String(leido.aplicacion);
						nuevo.fichero = f;

						if (f.getId() == -100)
						{
							System.err.println("El fichero no existe");
							nuevo.res = new Boolean(false);
						}
						else
						{
							System.err.println("El fichero existe");
							nuevo.res = new Boolean(true);
						}

						colaEnvio.nuevoEvento(nuevo);
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Hebra encargada de enviar los eventos
	 */
	private class HebraEnvio implements Runnable
	{
		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			while (true)
			{
				try
				{
					DEvent evento = colaEnvio.extraerEvento();
					space.write(evento, null, leaseWriteTime);
					FrameServFich.println("Escrito evento: " + evento);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}