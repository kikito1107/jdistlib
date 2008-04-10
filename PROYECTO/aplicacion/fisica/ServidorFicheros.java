package aplicacion.fisica;

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
		hebraDesconexionUsuarios = new Thread(new HebraDesconexionUsuarios());
		System.out.println(
		"ServidorFicheros: HebraDesconexionUsuarios creada");
		hebraDesconexionUsuarios.start();
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
						// TODO eliminar el fichero de la BD
						System.out.println("Leido evento eliminacion  fichero");


						//TODO insertar un nuevo fichero en la BD

						DFileEvent nuevo = new DFileEvent();
						nuevo.tipo = new Integer(DFileEvent.RESPUESTA_ELIMINAR_FICHERO.intValue());
						nuevo.origen = new Integer(30);
						nuevo.destino = new Integer(31);
						nuevo.aplicacion = new String(leido.aplicacion);

						//TODO asignar a dfile event el nuevo fichero

						// ???? no ser’a mejor un broadcast o algo as’??
						colaEnvio.nuevoEvento(nuevo);
					}
					else if(leido.tipo.intValue() == DFileEvent.NOTIFICAR_MODIFICACION_FICHERO.intValue()) {
						//TODO modificar los atributos de un fichero en la BD
						System.out.println("Leido evento modificacion  fichero");

						//TODO insertar un nuevo fichero en la BD

						DFileEvent nuevo = new DFileEvent();
						nuevo.tipo = new Integer(DFileEvent.RESPUESTA_MODIFICACION_FICHERO.intValue());
						nuevo.origen = new Integer(30);
						nuevo.destino = new Integer(31);
						nuevo.aplicacion = new String(leido.aplicacion);

						//TODO asignar a dfile event el nuevo fichero

						// ???? no ser’a mejor un broadcast o algo as’??
						colaEnvio.nuevoEvento(nuevo);
					}
					else if(leido.tipo.intValue() == DDocumentEvent.OBTENER_FICHERO.intValue()) {
						//TODO modificar los atributos de un fichero en la BD
						System.out.println("Leido evento solicitud documento");


						//TODO insertar un nuevo fichero en la BD

						DDocumentEvent nuevo = new DDocumentEvent();
						nuevo.tipo = new Integer(DDocumentEvent.RESPUESTA_FICHERO.intValue());
						nuevo.origen = new Integer(30);
						nuevo.destino = new Integer(31);
						nuevo.aplicacion = new String(leido.aplicacion);

						nuevo.direccionRespuesta = new String(InetAddress.getLocalHost().getHostName());

						nuevo.path = new String( ((DDocumentEvent)leido).path);

						//TODO asignar a dfile event el nuevo fichero

						colaEnvio.nuevoEvento(nuevo);
					}

//					if (leido.tipo.intValue() == DMIEvent.SINCRONIZACION.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros

//					System.out.println("ServidorFicheros: evento leido " +
//					(DMIEvent) leido);

//					evento.tipo = new Integer(DMIEvent.RESPUESTA_SINCRONIZACION.
//					intValue());
//					evento.aplicacion = new String(leido.aplicacion);
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.contador = new Long(contador);
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() == DMIEvent.IDENTIFICACION.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = ( (DMIEvent) leido).usuario;
//					String clave = ( (DMIEvent) leido).clave;

//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_IDENTIFICACION.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.usuario = new String(usuario);
//					evento.clave = new String(clave);
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.infoCompleta = (MICompleta) gestor.identificar(aplicacion,
//					usuario, clave);

//					System.out.println(evento.infoCompleta.mensajeError);
//					if (evento.infoCompleta.identificacionValida.booleanValue()) {
//					notificarConexionUsuario(aplicacion, usuario,
//					evento.infoCompleta.rol);
//					}

//					colaEnvio.nuevoEvento(evento);
//					}

//					if (leido.tipo.intValue() == DMIEvent.DESCONEXION.intValue()) {
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = ( (DMIEvent) leido).usuario;

//					gestor.usuarioDesconectado(aplicacion, usuario);

//					notificarDesconexionUsuario(aplicacion, usuario);
//					}
//					if (leido.tipo.intValue() == DMIEvent.KEEPALIVE.intValue()) {
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = ( (DMIEvent) leido).usuario;

//					gestor.actualizarMarcaTiempoUsuario(aplicacion, usuario);
//					}
//					if (leido.tipo.intValue() == DMIEvent.CAMBIO_ROL.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = new String(leido.usuario);
//					String rol = new String( ( (DMIEvent) leido).rol);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.usuario = new String(usuario);
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_CAMBIO_ROL.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.rolAntiguo = gestor.obtenerRolActualUsuario(aplicacion,
//					usuario);
//					evento.infoCompleta = gestor.cambiarRolUsuarioConectado(aplicacion,
//					usuario, rol);
//					if (evento.infoCompleta.identificacionValida.booleanValue() &&
//					!evento.infoCompleta.rol.equals(evento.rolAntiguo)) {
//					notificarCambioRolUsuario(aplicacion, usuario, rol,
//					evento.rolAntiguo, evento.infoCompleta);
//					}
//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() ==
//					DMIEvent.CAMBIO_PERMISO_COMPONENTE_USUARIO.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = new String(leido.usuario);
//					String componente = new String( ( (DMIEvent) leido).componente);
//					int permiso = ( (DMIEvent) leido).permiso.intValue();

//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.usuario = new String(usuario);
//					evento.componente = new String(componente);
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.
//					RESPUESTA_CAMBIO_PERMISO_COMPONENTE_USUARIO.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.bool = new Boolean(gestor.setPermisoComponenteUsuario(
//					aplicacion, usuario, componente, permiso));

//					if (evento.bool.booleanValue()) { // Cambio efecto
//					boolean notificar = true;
//					String rol = gestor.obtenerRolActualUsuario(aplicacion, usuario);
//					int aux = gestor.obtenerPermisoComponenteRol(aplicacion, rol,
//					componente);

//					if (aux != -1 && permiso < aux/* && permiso < 20*/) {
//					notificar = false;
//					}
//					System.out.println("Rol=" + rol + " Permiso rol=" + aux +
//					" permiso usuario=" + permiso);
//					if (notificar) {
//					notificarCambioPermisoComponenteUsuario(aplicacion, usuario,
//					componente, permiso);
//					}
//					}

//					colaEnvio.nuevoEvento(evento);
//					}

//					if (leido.tipo.intValue() ==
//					DMIEvent.CAMBIO_PERMISO_COMPONENTE_ROL.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String rol = new String( ( (DMIEvent) leido).rol);
//					String componente = new String( ( (DMIEvent) leido).componente);
//					int permiso = ( (DMIEvent) leido).permiso.intValue();
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.rol = new String(rol);
//					evento.componente = new String(componente);
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.
//					RESPUESTA_CAMBIO_PERMISO_COMPONENTE_ROL.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.bool = new Boolean(gestor.setPermisoComponenteRol(
//					aplicacion, rol, componente, permiso));

//					if (evento.bool.booleanValue()) { // Cambio correcto
//					boolean notificar = true;
//					int aux = gestor.obtenerPermisoComponenteUsuario(aplicacion,
//					leido.usuario, componente);

//					if (aux != -1 && permiso < aux) {
//					notificar = false;
//					}
//					if (notificar) {
//					notificarCambioPermisoComponenteRol(aplicacion, rol, componente,
//					permiso);
//					}

//					}

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() ==
//					DMIEvent.OBTENER_PERMISO_COMPONENTE_USUARIO.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = new String(leido.usuario);
//					String componente = new String( ( (DMIEvent) leido).componente);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.usuario = new String(usuario);
//					evento.componente = new String(componente);
//					evento.aplicacion = new String(aplicacion);
//					evento.permiso = new Integer(gestor.
//					obtenerPermisoComponenteUsuario(
//					aplicacion, usuario, componente));
//					evento.tipo = new Integer(DMIEvent.
//					RESPUESTA_OBTENER_PERMISO_COMPONENTE_USUARIO.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());

//					colaEnvio.nuevoEvento(evento);
//					}

//					if (leido.tipo.intValue() ==
//					DMIEvent.OBTENER_PERMISO_COMPONENTE_ROL.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String rol = new String( ( (DMIEvent) leido).rol);
//					String componente = new String( ( (DMIEvent) leido).componente);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.rol = new String(rol);
//					evento.componente = new String(componente);
//					evento.aplicacion = new String(aplicacion);
//					evento.permiso = new Integer(gestor.obtenerPermisoComponenteRol(
//					aplicacion, rol, componente));
//					evento.tipo = new Integer(DMIEvent.
//					RESPUESTA_OBTENER_PERMISO_COMPONENTE_ROL.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());

//					colaEnvio.nuevoEvento(evento);
//					}

//					if (leido.tipo.intValue() ==
//					DMIEvent.OBTENER_USUARIOS_BAJO_ROL.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String rol = new String( ( (DMIEvent) leido).rol);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.
//					RESPUESTA_OBTENER_USUARIOS_BAJO_ROL.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.v1 = gestor.obtenerUsuariosBajoRol(aplicacion, rol);

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() == DMIEvent.OBTENER_USUARIOS.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_USUARIOS.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.v1 = gestor.obtenerUsuarios(aplicacion);

//					colaEnvio.nuevoEvento(evento);
//					}

//					if (leido.tipo.intValue() == DMIEvent.OBTENER_ROLES.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_ROLES.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.v1 = gestor.obtenerRoles(aplicacion);

//					colaEnvio.nuevoEvento(evento);
//					}

//					if (leido.tipo.intValue() == DMIEvent.OBTENER_COMPONENTES.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_COMPONENTES.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.v1 = gestor.obtenerComponentes(aplicacion);

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() ==
//					DMIEvent.OBTENER_ROLES_PERMITIDOS.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.usuario = new String(leido.usuario);
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.
//					RESPUESTA_OBTENER_ROLES_PERMITIDOS.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.v1 = gestor.obtenerRolesPermitidos(aplicacion,
//					leido.usuario);

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() ==
//					DMIEvent.OBTENER_USUARIOS_CONECTADOS.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.tipo = new Integer(DMIEvent.
//					RESPUESTA_OBTENER_USUARIOS_CONECTADOS.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.v1 = gestor.obtenerUsuariosConectados(aplicacion);

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() == DMIEvent.NUEVO_USUARIO.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = new String( ( (DMIEvent) leido).nuevoUsuario);
//					String clave = new String( ( (DMIEvent) leido).clave);
//					String rolDefecto = new String( ( (DMIEvent) leido).rol);
//					boolean administrador = ( (DMIEvent) leido).bool.booleanValue();
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.nuevoUsuario = new String(usuario);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_USUARIO.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.mensaje = gestor.nuevoUsuario(aplicacion, usuario, clave,
//					rolDefecto, administrador);

//					if (evento.mensaje.length() == 0) {
//					notificarNuevoUsuario(aplicacion, usuario);
//					}

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() == DMIEvent.ELIMINAR_USUARIO.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = new String( ( (DMIEvent) leido).usuario);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.usuario = new String(usuario);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_ELIMINAR_USUARIO.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.mensaje = gestor.eliminarUsuario(aplicacion, usuario);

//					if (evento.mensaje.length() == 0) {
//					notificarUsuarioEliminado(aplicacion, usuario);
//					}

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() == DMIEvent.NUEVO_ROL.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String rol = new String( ( (DMIEvent) leido).rol);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.rol = new String(rol);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_ROL.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.mensaje = gestor.nuevoRol(aplicacion, rol);

//					if (evento.mensaje.length() == 0) {
//					notificarNuevoRol(aplicacion, rol);
//					}

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() == DMIEvent.ELIMINAR_ROL.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String rol = new String( ( (DMIEvent) leido).rol);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.rol = new String(rol);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_ELIMINAR_ROL.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.mensaje = gestor.eliminarRol(aplicacion, rol);

//					if (evento.mensaje.length() == 0) {
//					notificarRolEliminado(aplicacion, rol);
//					}

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() == DMIEvent.NUEVO_ROL_PERMITIDO.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = new String( ( (DMIEvent) leido).usuario);
//					String rol = new String( ( (DMIEvent) leido).rol);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.usuario = new String(usuario);
//					evento.rol = new String(rol);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_ROL_PERMITIDO.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.mensaje = gestor.nuevoRolPermitidoUsuario(aplicacion,
//					usuario, rol);

//					if (evento.mensaje.length() == 0) {
//					notificarNuevoRolPermitido(aplicacion, usuario, rol);
//					}

//					colaEnvio.nuevoEvento(evento);
//					}
//					if (leido.tipo.intValue() == DMIEvent.ELIMINAR_ROL_PERMITIDO.intValue()) {
//					DMIEvent evento = new DMIEvent();
//					String aplicacion = new String(leido.aplicacion);
//					String usuario = new String( ( (DMIEvent) leido).usuario);
//					String rol = new String( ( (DMIEvent) leido).rol);
//					evento.origen = new Integer(10); // Servidor ficheros
//					evento.destino = new Integer(11); // Cliente ficheros
//					evento.aplicacion = new String(aplicacion);
//					evento.usuario = new String(usuario);
//					evento.rol = new String(rol);
//					evento.tipo = new Integer(DMIEvent.RESPUESTA_ELIMINAR_ROL_PERMITIDO.
//					intValue());
//					evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
//					evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
//					booleanValue());
//					evento.mensaje = gestor.eliminarRolPermitidoUsuario(aplicacion,
//					usuario, rol);

//					if (evento.mensaje.length() == 0) {
//					notificarRolPermitidoEliminado(aplicacion, usuario, rol);
//					}

//					colaEnvio.nuevoEvento(evento);
//					}
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

	private class HebraDesconexionUsuarios
	implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(10000);
					// Vector apls = gestor.obtenerAplicaciones();
					/* Vector usrs = null;
			 String apl = "";
			 String usr = "";
			// for (int i = 0; i < apls.size(); i++) {
				//apl = (String) apls.elementAt(i);
				//usrs = gestor.obtenerUsuariosNoActualizados(apl);
				for (int j = 0; j < usrs.size(); j++) {
				  usr = (String) usrs.elementAt(j);
				  //gestor.usuarioDesconectado(apl, usr);
				  //notificarDesconexionUsuario(apl, usr);
				}*/




				}
				// }
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
