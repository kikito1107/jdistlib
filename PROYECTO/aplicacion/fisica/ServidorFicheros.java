package aplicacion.fisica;

import java.io.File;
import java.net.InetAddress;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import aplicacion.fisica.documentos.FicheroBD;
import aplicacion.fisica.eventos.DDocumentEvent;
import aplicacion.fisica.eventos.DFileEvent;
import aplicacion.fisica.eventos.DNodeEvent;

import Deventos.*;
import net.jini.space.*;
import net.jini.core.lease.Lease;
import util.*;

/**
 * Implementacion del servidor de ficheros
 */

public class ServidorFicheros {
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

	public ServidorFicheros() {

		System.out.println("");


		gestor = new GestorFicherosBD();
		System.out.println(
		"ServidorFicheros: Almacen de ficheros creado");
		System.out.println("ServidorFicheros: Localizando JavaSpace");
		try {
			space = SpaceLocator.getSpace("JavaSpace");
			System.out.println("ServidorFicheros: JavaSpace localizado");
		}
		catch (Exception e) {
			System.out.println("ServidorFicheros: Error localizando JavaSpace  " + e.getMessage());
			System.exit(1);
		}

		hebraProcesadora = new Thread(new HebraProcesadora());
		System.out.println("ServidorFicheros: HebraProcesadora creada");
		hebraProcesadora.start();
		System.out.println("ServidorFicheros: HebraProcesadora iniciada");
		hebraEnvio = new Thread(new HebraEnvio());
		System.out.println("ServidorFicheros: HebraEnvio creada");
		hebraEnvio.start();
		System.out.println("ServidorFicheros: HebraEnvio iniciada");
		System.out.println(
		"ServidorFicheros: HebraDesconexionUsuarios iniciada");

		Transfer.establecerServidor();
	}



	public void salvar() {
		gestor.salvar();
	}




	public DefaultMutableTreeNode obtenerArbol(String usuario, String rol) {

		if (gestor == null)
			gestor = new GestorFicherosBD();

		DefaultMutableTreeNode raiz = new DefaultMutableTreeNode();

		Vector<FicheroBD> fich = gestor.recuperar();

		this.agregarFichero(fich.get(0), raiz, usuario, rol);


		return raiz;
	}

	private void agregarFichero(FicheroBD f, DefaultMutableTreeNode padre, String usuario, String rol) {

		// agregamos una nueva hoja al arbol
		if ( ParserPermisos.comprobarPermisoLectura(f, usuario, rol) ) {

			DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(f);

			if (f.esDirectorio()) {
				Vector<FicheroBD> fs = gestor.recuperarDirectorio(f.getId());

				for (int i=1; i<fs.size(); ++i)
					agregarFichero(fs.get(i), nodo, usuario, rol);
			}

			padre.add(nodo);
		}
	}
	
	
	

	private class HebraProcesadora
	implements Runnable {
		DEvent leido = null;
		DEvent plantilla = new DEvent();

		public void run() {
			plantilla.destino =  new Integer(30); // Servidor ficheros
			///plantilla.tipo = null;

			while (true) {
				try {

					System.out.println("preparado para leer evento");

					leido = (DEvent) space.take(plantilla, null, leaseReadTime);
					System.out.println("ServidorFicheros: evento leido: " +
							leido);

					if (leido.tipo.intValue() == DNodeEvent.SINCRONIZACION.intValue()) {
						System.out.println("Recibido evento de sincronizcion ficheros");


						DNodeEvent nuevo = new DNodeEvent();

						nuevo.tipo = new Integer(DNodeEvent.RESPUESTA_SINCRONIZACION.intValue());
						nuevo.origen = new Integer(30);
						nuevo.destino = new Integer(31);
						nuevo.aplicacion = new String(leido.aplicacion);
						nuevo.raiz = obtenerArbol(leido.usuario, leido.rol);
						nuevo.direccionRMI = new String(InetAddress.getLocalHost().getHostName());

						colaEnvio.nuevoEvento(nuevo);
					}
					else if(leido.tipo.intValue() == DFileEvent.NOTIFICAR_INSERTAR_FICHERO.intValue()) {

						System.out.println("Leido evento insercion nuevo fichero");

						//TODO insertar un nuevo fichero en la BD

						if (gestor == null)
							gestor = new GestorFicherosBD();

						gestor.insertarNuevoFichero(((DFileEvent)leido).fichero);
						
					}
					else if(leido.tipo.intValue() == DFileEvent.NOTIFICAR_ELIMINAR_FICHERO.intValue()) {
						System.out.println("Leido evento eliminacion  fichero");

						if (gestor == null)
							gestor = new GestorFicherosBD();

						gestor.eliminarFichero(((DFileEvent)leido).fichero);
						
						String path = ((DFileEvent)leido).fichero.getRutaLocal();
						
						System.out.println("Fichero a borrar: " +path);
						
						File f = new File(path);
						File fAnot = new File(path + ".anot");
						
						if (!f.delete() || !fAnot.delete())
							System.err.println("Error borrando el archivo: " + path);
						
					}
					else if(leido.tipo.intValue() == DFileEvent.NOTIFICAR_MODIFICACION_FICHERO.intValue()) {
						System.out.println("Leido evento modificacion  fichero");

						if (gestor == null)
							gestor = new GestorFicherosBD();
						

						int id = ((DFileEvent)leido).fichero.getId();
						
						// cambiar el nombre del fichero 
						String old = gestor.buscarFichero(id).getRutaLocal();
						String new_ = ((DFileEvent)leido).fichero.getRutaLocal();
						File f = new File(old);
						
						File f2 = new File(new_);
						
						System.err.println("Ruta local: " + old);
						System.err.println("Ruta nueva " + new_);
						
						if(!f.renameTo(f2))
							System.err.println("Error al renombrar el fichero");
						else {
	
							f = new File(old+".anot");
							f2 = new File(new_+".anot");
							
							if(!f.renameTo(f2))
								System.err.println("Error al renombrar el fichero de anotaciones");
							else
								System.err.println("Fichero renombrado con Žxito");
							
						}
						gestor.modificarFichero(((DFileEvent)leido).fichero);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class HebraEnvio
	implements Runnable {
		public void run() {
			while (true) {
				try {
					DEvent evento = colaEnvio.extraerEvento();
					space.write(evento, null, leaseWriteTime);
					System.out.println("Escrito evento: " +  (DEvent)evento);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
