package metainformacion;

import java.util.*;

import Deventos.*;
import net.jini.space.*;
import net.jini.core.lease.Lease;
import util.*;

/**
 * Implementacion del servidor de metainformacion
 */

public class ServidorMetaInformacion {
  AlmacenMetaInformacion almacen = null;
  JavaSpace space = null;
  Thread hebraProcesadora = null;
  Thread hebraEnvio = null;
  Thread hebraDesconexionUsuarios = null;
  ColaEventos colaRecepcion = new ColaEventos();
  ColaEventos colaEnvio = new ColaEventos();
  long contador = 0;
  
  private static long leaseWriteTime = Lease.FOREVER;
  private static long leaseReadTime = Long.MAX_VALUE;
  
  public ServidorMetaInformacion() {
	  
	  System.out.println("");
	  
	  
	 almacen = new AlmacenMetaInformacion();
	 System.out.println(
		  "ServidorMetaInformacion: Almacen de MetaInformacion creado");
	 System.out.println("ServidorMetaInformacion: Localizando JavaSpace");
	 try {
		space = SpaceLocator.getSpace("JavaSpace");
		System.out.println("ServidorMetaInformacion: JavaSpace localizado");
	 }
	 catch (Exception e) {
		System.out.println("ServidorMetaInformacion: Error localizando JavaSpace  " + e.getMessage());
		System.exit(1);
	 }
	 
	 hebraProcesadora = new Thread(new HebraProcesadora());
	 System.out.println("ServidorMetaInformacion: HebraProcesadora creada");
	 hebraProcesadora.start();
	 System.out.println("ServidorMetaInformacion: HebraProcesadora iniciada");
	 hebraEnvio = new Thread(new HebraEnvio());
	 System.out.println("ServidorMetaInformacion: HebraEnvio creada");
	 hebraEnvio.start();
	 System.out.println("ServidorMetaInformacion: HebraEnvio iniciada");
	 hebraDesconexionUsuarios = new Thread(new HebraDesconexionUsuarios());
	 System.out.println(
		  "ServidorMetaInformacion: HebraDesconexionUsuarios creada");
	 hebraDesconexionUsuarios.start();
	 System.out.println(
		  "ServidorMetaInformacion: HebraDesconexionUsuarios iniciada");
  }

  public static void main(String[] args) {
	 new ServidorMetaInformacion();
  }

  public void salvar() {
	 almacen.salvar();
  }

  private void notificarConexionUsuario(String aplicacion, String usuario,
													 String rol) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_CONEXION_USUARIO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.usuario = new String(usuario);
	 evento.rol = new String(rol);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarDesconexionUsuario(String aplicacion, String usuario) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_DESCONEXION_USUARIO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.usuario = new String(usuario);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarCambioRolUsuario(String aplicacion, String usuario,
													  String rol, String rolAntiguo,
													  MICompleta info) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.usuario = new String(usuario);
	 evento.rol = new String(rol);
	 evento.rolAntiguo = new String(rolAntiguo);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);
	 evento.infoCompleta = (MICompleta) info;

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarCambioPermisoComponenteUsuario(String aplicacion,
		String usuario, String componente, int permiso) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.
										NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_USUARIO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.usuario = new String(usuario);
	 evento.componente = new String(componente);
	 evento.permiso = new Integer(permiso);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 System.out.println("Notificado permiso componente=" + componente +
							  " permiso" + permiso);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarCambioPermisoComponenteRol(String aplicacion,
		String rol, String componente, int permiso) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.
										NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_ROL.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.rol = new String(rol);
	 evento.componente = new String(componente);
	 evento.permiso = new Integer(permiso);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarUsuarioEliminado(String aplicacion, String usuario) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_USUARIO_ELIMINADO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.usuario = new String(usuario);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarRolEliminado(String aplicacion, String rol) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_ROL_ELIMINADO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.rol = new String(rol);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarNuevoRolPermitido(String aplicacion, String usuario,
														String rol) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_NUEVO_ROL_PERMITIDO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.usuario = new String(usuario);
	 evento.rol = new String(rol);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarNuevoRol(String aplicacion, String rol) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_NUEVO_ROL.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.rol = new String(rol);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarNuevoUsuario(String aplicacion, String usuario) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_NUEVO_USUARIO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.usuario = new String(usuario);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private void notificarRolPermitidoEliminado(String aplicacion, String usuario,
															 String rol) {
	 DMIEvent evento = new DMIEvent();
	 evento.origen = new Integer(10); // Servidor MetaInformacion
	 evento.destino = new Integer(11); // Cliente MetaInformacion
	 evento.tipo = new Integer(DMIEvent.NOTIFICACION_ELIMINAR_ROL_PERMITIVO.
										intValue());
	 evento.aplicacion = new String(aplicacion);
	 evento.usuario = new String(usuario);
	 evento.rol = new String(rol);
	 evento.contador = new Long(contador);
	 evento.sincrono = new Boolean(false);

	 colaEnvio.nuevoEvento(evento);
	 contador++;
  }

  private class HebraProcesadora
		implements Runnable {
	 DEvent leido = null;
	 DEvent plantilla = new DEvent();

	 public void run() {
		plantilla.destino = new Integer(10); // Servidor MetaInformacion

		while (true) {
		  try {
			 leido = (DEvent) space.take(plantilla, null, leaseReadTime);
			 System.out.println("ServidorMetaInformacion: evento leido: " +
									  (DMIEvent) leido);
			 if (leido.tipo.intValue() == DMIEvent.SINCRONIZACION.intValue()) {
				DMIEvent evento = new DMIEvent();
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				
				System.out.println("ServidorMetaInformacion: evento leido " +
						  (DMIEvent) leido);
				
				evento.tipo = new Integer(DMIEvent.RESPUESTA_SINCRONIZACION.
												  intValue());
				evento.aplicacion = new String(leido.aplicacion);
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.contador = new Long(contador);
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() == DMIEvent.IDENTIFICACION.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String usuario = ( (DMIEvent) leido).usuario;
				String clave = ( (DMIEvent) leido).clave;

				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_IDENTIFICACION.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.usuario = new String(usuario);
				evento.clave = new String(clave);
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.infoCompleta = (MICompleta) almacen.identificar(aplicacion,
					 usuario, clave);

				System.out.println(evento.infoCompleta.mensajeError);
				if (evento.infoCompleta.identificacionValida.booleanValue()) {
				  notificarConexionUsuario(aplicacion, usuario,
													evento.infoCompleta.rol);
				}

				colaEnvio.nuevoEvento(evento);
			 }

			 if (leido.tipo.intValue() == DMIEvent.DESCONEXION.intValue()) {
				String aplicacion = new String(leido.aplicacion);
				String usuario = ( (DMIEvent) leido).usuario;

				almacen.usuarioDesconectado(aplicacion, usuario);

				notificarDesconexionUsuario(aplicacion, usuario);
			 }
			 if (leido.tipo.intValue() == DMIEvent.KEEPALIVE.intValue()) {
				String aplicacion = new String(leido.aplicacion);
				String usuario = ( (DMIEvent) leido).usuario;

				almacen.actualizarMarcaTiempoUsuario(aplicacion, usuario);
			 }
			 if (leido.tipo.intValue() == DMIEvent.CAMBIO_ROL.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String usuario = new String(leido.usuario);
				String rol = new String( ( (DMIEvent) leido).rol);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.usuario = new String(usuario);
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_CAMBIO_ROL.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.rolAntiguo = almacen.obtenerRolActualUsuario(aplicacion,
					 usuario);
				evento.infoCompleta = almacen.cambiarRolUsuarioConectado(aplicacion,
					 usuario, rol);
				if (evento.infoCompleta.identificacionValida.booleanValue() &&
					 !evento.infoCompleta.rol.equals(evento.rolAntiguo)) {
				  notificarCambioRolUsuario(aplicacion, usuario, rol,
													 evento.rolAntiguo, evento.infoCompleta);
				}
				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.CAMBIO_PERMISO_COMPONENTE_USUARIO.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String usuario = new String(leido.usuario);
				String componente = new String( ( (DMIEvent) leido).componente);
				int permiso = ( (DMIEvent) leido).permiso.intValue();

				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.usuario = new String(usuario);
				evento.componente = new String(componente);
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.
												  RESPUESTA_CAMBIO_PERMISO_COMPONENTE_USUARIO.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.bool = new Boolean(almacen.setPermisoComponenteUsuario(
					 aplicacion, usuario, componente, permiso));

				if (evento.bool.booleanValue()) { // Cambio efecto
				  boolean notificar = true;
				  String rol = almacen.obtenerRolActualUsuario(aplicacion, usuario);
				  int aux = almacen.obtenerPermisoComponenteRol(aplicacion, rol,
						componente);

				  if (aux != -1 && permiso < aux/* && permiso < 20*/) {
					 notificar = false;
				  }
				  System.out.println("Rol=" + rol + " Permiso rol=" + aux +
											" permiso usuario=" + permiso);
				  if (notificar) {
					 notificarCambioPermisoComponenteUsuario(aplicacion, usuario,
						  componente, permiso);
				  }
				}

				colaEnvio.nuevoEvento(evento);
			 }

			 if (leido.tipo.intValue() ==
				  DMIEvent.CAMBIO_PERMISO_COMPONENTE_ROL.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String rol = new String( ( (DMIEvent) leido).rol);
				String componente = new String( ( (DMIEvent) leido).componente);
				int permiso = ( (DMIEvent) leido).permiso.intValue();
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.rol = new String(rol);
				evento.componente = new String(componente);
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.
												  RESPUESTA_CAMBIO_PERMISO_COMPONENTE_ROL.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.bool = new Boolean(almacen.setPermisoComponenteRol(
					 aplicacion, rol, componente, permiso));

				if (evento.bool.booleanValue()) { // Cambio correcto
				  boolean notificar = true;
				  int aux = almacen.obtenerPermisoComponenteUsuario(aplicacion,
						leido.usuario, componente);

				  if (aux != -1 && permiso < aux) {
					 notificar = false;
				  }
				  if (notificar) {
					 notificarCambioPermisoComponenteRol(aplicacion, rol, componente,
						  permiso);
				  }

				}

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.OBTENER_PERMISO_COMPONENTE_USUARIO.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String usuario = new String(leido.usuario);
				String componente = new String( ( (DMIEvent) leido).componente);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.usuario = new String(usuario);
				evento.componente = new String(componente);
				evento.aplicacion = new String(aplicacion);
				evento.permiso = new Integer(almacen.
													  obtenerPermisoComponenteUsuario(
					 aplicacion, usuario, componente));
				evento.tipo = new Integer(DMIEvent.
												  RESPUESTA_OBTENER_PERMISO_COMPONENTE_USUARIO.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());

				colaEnvio.nuevoEvento(evento);
			 }

			 if (leido.tipo.intValue() ==
				  DMIEvent.OBTENER_PERMISO_COMPONENTE_ROL.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String rol = new String( ( (DMIEvent) leido).rol);
				String componente = new String( ( (DMIEvent) leido).componente);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.rol = new String(rol);
				evento.componente = new String(componente);
				evento.aplicacion = new String(aplicacion);
				evento.permiso = new Integer(almacen.obtenerPermisoComponenteRol(
					 aplicacion, rol, componente));
				evento.tipo = new Integer(DMIEvent.
												  RESPUESTA_OBTENER_PERMISO_COMPONENTE_ROL.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());

				colaEnvio.nuevoEvento(evento);
			 }

			 if (leido.tipo.intValue() ==
				  DMIEvent.OBTENER_USUARIOS_BAJO_ROL.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String rol = new String( ( (DMIEvent) leido).rol);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.
												  RESPUESTA_OBTENER_USUARIOS_BAJO_ROL.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.v1 = almacen.obtenerUsuariosBajoRol(aplicacion, rol);

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() == DMIEvent.OBTENER_USUARIOS.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_USUARIOS.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.v1 = almacen.obtenerUsuarios(aplicacion);

				colaEnvio.nuevoEvento(evento);
			 }

			 if (leido.tipo.intValue() == DMIEvent.OBTENER_ROLES.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_ROLES.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.v1 = almacen.obtenerRoles(aplicacion);

				colaEnvio.nuevoEvento(evento);
			 }

			 if (leido.tipo.intValue() == DMIEvent.OBTENER_COMPONENTES.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_COMPONENTES.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.v1 = almacen.obtenerComponentes(aplicacion);

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.OBTENER_ROLES_PERMITIDOS.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.usuario = new String(leido.usuario);
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.
												  RESPUESTA_OBTENER_ROLES_PERMITIDOS.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.v1 = almacen.obtenerRolesPermitidos(aplicacion,
					 leido.usuario);

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.OBTENER_USUARIOS_CONECTADOS.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.tipo = new Integer(DMIEvent.
												  RESPUESTA_OBTENER_USUARIOS_CONECTADOS.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.v1 = almacen.obtenerUsuariosConectados(aplicacion);

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() == DMIEvent.NUEVO_USUARIO.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String usuario = new String( ( (DMIEvent) leido).nuevoUsuario);
				String clave = new String( ( (DMIEvent) leido).clave);
				String rolDefecto = new String( ( (DMIEvent) leido).rol);
				boolean administrador = ( (DMIEvent) leido).bool.booleanValue();
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.nuevoUsuario = new String(usuario);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_USUARIO.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.mensaje = almacen.nuevoUsuario(aplicacion, usuario, clave,
																  rolDefecto, administrador);

				if (evento.mensaje.length() == 0) {
				  notificarNuevoUsuario(aplicacion, usuario);
				}

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() == DMIEvent.ELIMINAR_USUARIO.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String usuario = new String( ( (DMIEvent) leido).usuario);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.usuario = new String(usuario);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_ELIMINAR_USUARIO.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.mensaje = almacen.eliminarUsuario(aplicacion, usuario);

				if (evento.mensaje.length() == 0) {
				  notificarUsuarioEliminado(aplicacion, usuario);
				}

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() == DMIEvent.NUEVO_ROL.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String rol = new String( ( (DMIEvent) leido).rol);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.rol = new String(rol);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_ROL.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.mensaje = almacen.nuevoRol(aplicacion, rol);

				if (evento.mensaje.length() == 0) {
				  notificarNuevoRol(aplicacion, rol);
				}

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() == DMIEvent.ELIMINAR_ROL.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String rol = new String( ( (DMIEvent) leido).rol);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.rol = new String(rol);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_ELIMINAR_ROL.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.mensaje = almacen.eliminarRol(aplicacion, rol);

				if (evento.mensaje.length() == 0) {
				  notificarRolEliminado(aplicacion, rol);
				}

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() == DMIEvent.NUEVO_ROL_PERMITIDO.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String usuario = new String( ( (DMIEvent) leido).usuario);
				String rol = new String( ( (DMIEvent) leido).rol);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.usuario = new String(usuario);
				evento.rol = new String(rol);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_ROL_PERMITIDO.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.mensaje = almacen.nuevoRolPermitidoUsuario(aplicacion,
					 usuario, rol);

				if (evento.mensaje.length() == 0) {
				  notificarNuevoRolPermitido(aplicacion, usuario, rol);
				}

				colaEnvio.nuevoEvento(evento);
			 }
			 if (leido.tipo.intValue() == DMIEvent.ELIMINAR_ROL_PERMITIDO.intValue()) {
				DMIEvent evento = new DMIEvent();
				String aplicacion = new String(leido.aplicacion);
				String usuario = new String( ( (DMIEvent) leido).usuario);
				String rol = new String( ( (DMIEvent) leido).rol);
				evento.origen = new Integer(10); // Servidor MetaInformacion
				evento.destino = new Integer(11); // Cliente MetaInformacion
				evento.aplicacion = new String(aplicacion);
				evento.usuario = new String(usuario);
				evento.rol = new String(rol);
				evento.tipo = new Integer(DMIEvent.RESPUESTA_ELIMINAR_ROL_PERMITIDO.
												  intValue());
				evento.entero = new Integer( ( (DMIEvent) leido).entero.intValue());
				evento.sincrono = new Boolean( ( (DMIEvent) leido).sincrono.
														booleanValue());
				evento.mensaje = almacen.eliminarRolPermitidoUsuario(aplicacion,
					 usuario, rol);

				if (evento.mensaje.length() == 0) {
				  notificarRolPermitidoEliminado(aplicacion, usuario, rol);
				}

				colaEnvio.nuevoEvento(evento);
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
			 System.out.println("Escrito evento: " + (DMIEvent) evento);
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
			 Vector apls = almacen.obtenerAplicaciones();
			 Vector usrs = null;
			 String apl = "";
			 String usr = "";
			 for (int i = 0; i < apls.size(); i++) {
				apl = (String) apls.elementAt(i);
				usrs = almacen.obtenerUsuariosNoActualizados(apl);
				for (int j = 0; j < usrs.size(); j++) {
				  usr = (String) usrs.elementAt(j);
				  almacen.usuarioDesconectado(apl, usr);
				  notificarDesconexionUsuario(apl, usr);
				}
			 }
		  }
		  catch (Exception e) {
			 e.printStackTrace();
		  }
		}
	 }
  }
}
