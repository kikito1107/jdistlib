package aplicacion.fisica;

import java.rmi.RemoteException;
import java.util.*;
import desarrollo.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import aplicacion.fisica.documentos.FicheroBD;
import aplicacion.fisica.eventos.DDocumentEvent;
import aplicacion.fisica.eventos.DFileEvent;
import aplicacion.fisica.eventos.DNodeEvent;

import Deventos.*;
import interfaces.listeners.*;
import metainformacion.MICompleta;
import metainformacion.MIInformacionConexion;
import net.jini.space.*;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import util.*;

/**
 * Cliente del modulo de ficheros
 */

public class ClienteFicheros {
  public static ClienteFicheros cf = null;
  public static String usuario = null;
  public static String clave = null;
  public static String aplicacion = null;
  public static String rol = null;
  
  private static final long leaseWriteTime = Lease.FOREVER;
  private static final long leaseReadTime = 10000L;

  private JavaSpace space = null;
  private static boolean permisoAdministracion = false;

  private Monitor monitor = new Monitor();
  private long contador = -1;
  
  private DefaultMutableTreeNode raiz = null;
  public static String ipConexion = null;
  
  @SuppressWarnings("static-access")
public ClienteFicheros(String aplicacion2, String usuario2, String clave2, String rol2) {
	  
	  
	  
	 this.aplicacion = new String(aplicacion2);
	 this.usuario = new String(usuario2);
	 this.clave = new String(clave2);
	 this.rol = new String(rol2);
	 localizarJavaSpace();
	 cf = this;
	 
	 inicializar();
  }

  @SuppressWarnings("static-access" )
public ClienteFicheros(String aplicacion, String usuario, String clave,
										  JFrame frame, JavaSpace space) {
	 this.aplicacion = new String(aplicacion);
	 this.usuario = new String(usuario);
	 this.clave = clave;
	 this.space = space;
	 cf = this;

	 JFrame fr = new JFrame();

	 inicializar();
  }

  private void localizarJavaSpace() {
	 // Encargada de localizar el JavaSpace
	 Thread t1 = new Thread(new HebraLocalizadora("JavaSpace"));
	 t1.start();
	 // Espera un tiempo la localizacion del JavaSpace. En caso de no
	 // localizarse en este tiempo lo indica. Asi evitamos que el
	 // programa se quede bloqueado
	 Thread t2 = new Thread(new HebraDetectoraError(10));
	 t2.start();
	 // Espera un evento sobre la inicializacion
	 if (monitor.inicializacionCorrecta()) {
		inicializar();
		//System.out.println("Localizacion correcta");
	 }
	 else {
		//System.out.println("Error localizando el JavaSpace");
		System.exit(1);
	 }
  }
  
  public static ClienteFicheros obtenerClienteFicheros() {
	 return cf;
  }

  /**
   * inicializa el cliente enviando una solicitud de sincronizaci�n al servidor de Metainformaci�n
   */
  public void inicializar() {
	 try {
		//********** SINCRONIZACION CON EL SERVIDOR DE ficheros *********//
		 
		// evento en el que se almacenar� la respuesta del servidor a la petici�n de sincronizaci�n del SF
		DNodeEvent leido = null;
		
		// evento que ser� enviado al servidor para solicitar sincronizaci�n
		DEvent evento = new DEvent();
		
		// evento "template" utilizado para leer la respuesta a la petici�n de sincronizaci�n del servidor
		DNodeEvent plantilla = new DNodeEvent();
		
		// aleatorio que servir� de ID de la petici�n de sincronizacion
		int idEvento = aleatorio();
		
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
		plantilla.tipo = new Integer(DNodeEvent.RESPUESTA_SINCRONIZACION.intValue());
		
		
		// leemos la respuesta a la solicitud
		leido = (DNodeEvent) space.take(plantilla, null, leaseReadTime);	
		
		if (leido == null) { // Sin respuesta del coordinador
		  JOptionPane.showMessageDialog(null,
										"Error sincronizando con el Servidor de ficheros",
										"Error", JOptionPane.ERROR_MESSAGE);
		  System.exit(1);
		}
		else {
		//contador = leido.contador.longValue();
		  
			raiz = leido.raiz;
			this.ipConexion = leido.direccionRMI;
			//hr = new Thread(new HebraRecolectora());
			//hr.start();
		}
		
		
	 }
	 catch (Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(null,
			 "Hubo un error en la comunicacion en el cliente de fichero\nDebera identificarse de nuevo.",
												"Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	 }
  }
  
  public DefaultMutableTreeNode getRaiz() {
	  return raiz;
  }
  

  public void borrarFichero(FicheroBD f, String aplicacion) {
	  try
		{
	  DFileEvent evt = new DFileEvent();
	  	// origen y destino del evento
		evt.origen = new Integer(31); // Cliente ficheros
		evt.destino = new Integer(30); // Servidor ficheros
		
		// tipo de evento 
		evt.tipo = new Integer(DFileEvent.NOTIFICAR_ELIMINAR_FICHERO.intValue());
		evt.aplicacion = new String(aplicacion);
		evt.usuario = new String(usuario);
		evt.rol = new String(rol);

	  

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
  
  
  
  public void insertarNuevoFichero(FicheroBD f, String aplicacion) {
	  try
		{
	  DFileEvent evt = new DFileEvent();
	  	// origen y destino del evento
		evt.origen = new Integer(31); // Cliente ficheros
		evt.destino = new Integer(30); // Servidor ficheros
		
		// tipo de evento 
		evt.tipo = new Integer(DFileEvent.NOTIFICAR_INSERTAR_FICHERO.intValue());
		evt.aplicacion = new String(aplicacion);
		evt.usuario = new String(usuario);
		evt.rol = new String(rol);

	  

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
  
  
  public void modificarFichero(FicheroBD f, String aplicacion) {
	  try
		{
	  DFileEvent evt = new DFileEvent();
	  	// origen y destino del evento
		evt.origen = new Integer(31); // Cliente ficheros
		evt.destino = new Integer(30); // Servidor ficheros
		
		// tipo de evento 
		evt.tipo = new Integer(DFileEvent.NOTIFICAR_MODIFICACION_FICHERO.intValue());
		evt.aplicacion = new String(aplicacion);
		evt.usuario = new String(usuario);
		evt.rol = new String(rol);

	  

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


  public String solicitarFichero(String path){

	  try
	  {
		  DDocumentEvent evt = new DDocumentEvent();
		  evt.origen = new Integer(31); // Cliente ficheros
		  evt.destino = new Integer(30); // Servidor ficheros

		  System.out.println("Recibido evento de solicitud de fichero " + path);

		  // tipo de evento 
		  evt.tipo = new Integer(DDocumentEvent.OBTENER_FICHERO.intValue());
		  evt.aplicacion = new String(aplicacion);
		  evt.usuario = new String(usuario);
		  evt.rol = new String(rol);
		  evt.path = path;


		  space.write(evt, null, leaseWriteTime);
		  
		  DDocumentEvent plantilla = new DDocumentEvent();
		  
		  DDocumentEvent leido;
		  
		  leido=  (DDocumentEvent)space.take(plantilla, null, 4*leaseReadTime);
		  
		  System.out.println("Recibida respuesta direcci�n " + leido.direccionRespuesta);
		  System.out.println("para el path " + leido.path);
		  System.out.println("para el usuario " + leido.direccionRespuesta);
		  
		  return leido.direccionRespuesta;
		  
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
	catch (UnusableEntryException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch (InterruptedException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
  }
  

  private int aleatorio() {
	 Date fecha = new Date();
	 Random r = new Random(fecha.getTime());
	 int aleatorio = r.nextInt(50000);
	 aleatorio += 1000;
	 return aleatorio;
  }

  /**
	* Encargada de localizar el JavaSpace
	*/
  private class HebraLocalizadora
		implements Runnable {
	 String nombre = null;
	 Thread t = null;
	 int i = 0;

	 /**
	  * @param nombreJavaSpace String Nombre del JavaSpace que deseamos localizar
	  */
	 public HebraLocalizadora(String nombreJavaSpace) {
		this.nombre = nombreJavaSpace;

		t = new Thread(this);
		t.start();
	 }

	 /**
	  * M�todo que ejecuta la hebra
	  */
	 public void run() {
		try {
		  // Intentamos localizar el JavaSpace
		  space = SpaceLocator.getSpace(nombre);
		  // Localizacion del JavaSpace exitosa
		  monitor.setInicializado(true);
		}
		catch (Exception e) {
		  JOptionPane.showMessageDialog(null,
				"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
												  "Error", JOptionPane.ERROR_MESSAGE);
		  System.exit(1);
		}
	 }
  }

  /**
	*
	* Mediante esta clase podemos indicar que no ha sido posible
	* localizar el JavaSpace pasado el tiempo indicado. De esta forma evitamos
	* que el programa se quede bloqueado en la fase de localizacion del
	* JavasPace
	*/
  private class HebraDetectoraError
		implements Runnable {

	 int tiempoEspera = -1;
	 Thread t = null;

	 /**
	  * @param tiempoEspera int Tiempo que deseamos esperar
	  */
	 public HebraDetectoraError(int tiempoEspera) {
		this.tiempoEspera = tiempoEspera;

		t = new Thread(this);
		t.start();
	 }

	 /**
	  * M�todo que ejecuta la hebra
	  */
	 public void run() {
		try {
		  Thread.sleep(tiempoEspera * 1000);
		  if (!monitor.getInicializado()) {
			 monitor.setError(true);
		  }
		}
		catch (Exception e) {
		  JOptionPane.showMessageDialog(null,
				"Hubo un error en la comunicacion en el cliente de ficheros\nDebera identificarse de nuevo.",
												  "Error", JOptionPane.ERROR_MESSAGE);
		  System.exit(1);
		}
	 }
  }

  private class HebraEnvioKeepAlive
		implements Runnable {
	 DMIEvent evento = new DMIEvent();
	 public void run() {
		evento.origen = new Integer(11); // Cliente ficheros
		evento.destino = new Integer(10); // Servidor ficheros
		evento.aplicacion = new String(aplicacion);
		evento.usuario = new String(usuario);
		evento.tipo = new Integer(DMIEvent.KEEPALIVE.intValue());
		try {
		  while (true) {
			 Thread.sleep(30000);
			 space.write(evento, null, leaseWriteTime);
		  }
		}
		catch (Exception e) {
		  JOptionPane.showMessageDialog(null,
				"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
												  "Error", JOptionPane.ERROR_MESSAGE);
		  System.exit(1);
		}
	 }
  }

  /**
	* Dado el comportamiento concurrente de las 2 Hebras mediante este Monitor
	* gestionamos la informacion sobre la inicializacion. La clase DConector
	* realizara una llamada a inicializacionCorrecta() qued�ndose bloqueada
	* hasta que se producza la correcta localizacion del JavaSpace o se sobrepase
	* el tiempo de espera sin haber sido localizado.
	*/
  private class Monitor {
	 private boolean inicializado = false;
	 private boolean error = false;
	 private boolean sincronizado = false;

	 /**
	  * Bloquea al llamante hasta que se produzca una actualizacion de las
	  * variables inicializado o error
	  * @return boolean Devuelve si se ha producido o no la inicializacion
	  */
	 public synchronized boolean inicializacionCorrecta() {
		try {
		  while (!inicializado && !error) {
			 wait();
		  }
		}
		catch (Exception e) {
			e.printStackTrace();
		  JOptionPane.showMessageDialog(null,
				"Hubo un error en la comunicacion en el cliente de ficheros\nDebera identificarse de nuevo.",
												  "Error", JOptionPane.ERROR_MESSAGE);
		  System.exit(1);
		}

		return inicializado;
	 }

	 /**
	  * Devuelve si se ha sobrepasado el tiempo de espera sin haber sido localizado
	  * el JavaSpace
	  */
	 public synchronized boolean getError() {
		return error;
	 }

	 /**
	  * Cambamos el valor d ela variable error
	  * @param b boolean Valor deseado
	  */
	 public synchronized void setError(boolean b) {
		error = b;
		notifyAll();
	 }

	 /**
	  * Devuelve si se ha localizado el JavaSpace
	  */
	 public synchronized boolean getInicializado() {
		return inicializado;
	 }

	 /**
	  * Cambiamos el valor de la variable inicializado
	  * @param b boolean Valor deseado
	  */
	 public synchronized void setInicializado(boolean b) {
		inicializado = b;
		notifyAll();
	 }

	 /**
	  * Devuelve si se ha sincronizado
	  */
	 public synchronized boolean getSincronizado() {
		return sincronizado;
	 }

	 /**
	  * Cambiamos el valor de la variable sincronizado
	  * @param b boolean Valor deseado
	  */
	 public synchronized void setSincronizado(boolean b) {
		sincronizado = b;
		notifyAll();
	 }
  }

  
  private class HebraRecolectora
		implements Runnable {

	 public void run() {
		 
		 System.out.println("HEBRA RECOLECTORA CLIENTE FICHEROS INICIADA");
		 
		DDocumentEvent leido = null;
		DDocumentEvent plantilla = new DDocumentEvent();
		plantilla.destino = new Integer(31); // El Cliente de ficheros

		while (true) {
		  try {
			 //plantilla.contador = new Long(contador);
			 //System.out.println("ClienteMI: Esperando evento " + contador);
			 leido = (DDocumentEvent) space.read(plantilla, null, Long.MAX_VALUE);
			 contador++;
			 
			 System.out.println("Leido evento de respuesta " +  leido.tipo);
		  
			 
			 if (leido.tipo.intValue() ==
				 DDocumentEvent.RESPUESTA_FICHERO.intValue()) {
				 
				 DDocumentEvent evt = new DDocumentEvent();
				 
				 evt.destino = new Integer(1);
				 evt.path = new String(((DDocumentEvent) leido ).path);
				 evt.direccionRespuesta = new String(((DDocumentEvent) leido ).direccionRespuesta);
				 evt.rol = new String(rol);
				 evt.aplicacion = new String(aplicacion);
				 evt.usuario = new String(usuario);
				 
				 space.write(evt, null, leaseWriteTime);
			 }
			 
			 /*
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_CONEXION_USUARIO.intValue()) {
				String usuario = leido.usuario;
				String rol = leido.rol;
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).conexionUsuario(
						usuario, rol);
				}

			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_DESCONEXION_USUARIO.intValue()) {
				String usuario = leido.usuario;
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).desconexionUsuario(
						usuario);
				}
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO.intValue()) {
				String usuario = leido.usuario;
				String rol = leido.rol;
				String rolAntiguo = leido.rolAntiguo;
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).cambioRolUsuario(
						usuario, rol, rolAntiguo, leido.infoCompleta);
				}
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_USUARIO.intValue()) {
				String usuario = leido.usuario;
				String componente = leido.componente;
				int permiso = leido.permiso.intValue();
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).
						cambioPermisoComponenteUsuario(usuario, componente, permiso);
				}
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_ROL.intValue()) {
				String rol = leido.rol;
				String componente = leido.componente;
				int permiso = leido.permiso.intValue();
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).
						cambioPermisoComponenteRol(rol, componente, permiso);
				}
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_NUEVO_ROL_PERMITIDO.intValue()) {
				String usuario = leido.usuario;
				String rol = leido.rol;
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).nuevoRolPermitido(
						usuario, rol);
				}
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_ELIMINAR_ROL_PERMITIVO.intValue()) {
				String usuario = leido.usuario;
				String rol = leido.rol;
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).eliminarRolPermitido(
						usuario, rol);
				}
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_USUARIO_ELIMINADO.intValue()) {
				String usuario = leido.usuario;
				dmi.eliminarUsuario(usuario);
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).usuarioEliminado(
						usuario);
				}
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_ROL_ELIMINADO.intValue()) {
				String rol = leido.rol;
				dmi.eliminarRol(rol);
				for (int i = 0; i < dmilisteners.size(); i++) {
				  ( (DMIListener) dmilisteners.elementAt(i)).rolEliminado(rol);
				}
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_NUEVO_USUARIO.intValue()) {
				String usuario = leido.usuario;
				dmi.nuevoUsuario(usuario);
			 }
			 if (leido.tipo.intValue() ==
				  DMIEvent.NOTIFICACION_NUEVO_ROL.intValue()) {
				String rol = leido.rol;
				dmi.nuevoRol(rol);
			 }*/
		  }
		  catch (Exception e) {
			  e.printStackTrace();
			 JOptionPane.showMessageDialog(null,
				  "Hubo un error en la comunicacion en el cliente de ficheros\nDebera identificarse de nuevo.",
													 "Error", JOptionPane.ERROR_MESSAGE);
			 System.exit(1);
		  }
		  
		}
	 }
  }


}
