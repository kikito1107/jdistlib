package metainformacion;

import java.util.Date;
import java.util.Random;
import java.util.Vector;

import javaspaces.SpaceLocator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import metainformacion.gui.DialogoMetaInformacion;
import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;
import Deventos.DMIEvent;
import Deventos.enlaceJS.DConector;

import componentes.listeners.DMIListener;

/**
 * Cliente del modulo de metainformacion
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
@SuppressWarnings( "unchecked" )
public class ClienteMetaInformacion
{
	/**
	 * Instancia ejecutandose actualmente del cliente de metainformacion
	 */
	public static ClienteMetaInformacion cmi = null;

	/**
	 * Nombre del usuario que poseemos en el sistema
	 */
	public static String usuario = null;

	/**
	 * Clave asociada al usuario
	 */
	public static String clave = null;

	/**
	 * Nombre de la aplicacion que se esta ejecutando
	 */
	public static String aplicacion = null;

	private DialogoMetaInformacion dmi = null;

	private static final long leaseWriteTime = Lease.FOREVER;

	private static final long leaseReadTime = 10000L;

	private JavaSpace space = null;

	private static final Vector<DMIListener> dmilisteners = new Vector<DMIListener>();

	private static boolean permisoAdministracion = false;

	private Monitor monitor = new Monitor();

	private long contador = -1;

	/**
	 * Constructor con parametros
	 * 
	 * @param aplicacion2
	 *            Nombre de la aplicacion en ejecucion
	 * @param usuario2
	 *            Nombre del usuario que poseemos actualmente
	 * @param clave2
	 *            Clave asociada al usuario
	 */
	public ClienteMetaInformacion( String aplicacion2, String usuario2,
			String clave2 )
	{
		ClienteMetaInformacion.aplicacion = new String(aplicacion2);
		ClienteMetaInformacion.usuario = new String(usuario2);
		ClienteMetaInformacion.clave = new String(clave2);
		localizarJavaSpace();
		cmi = this;

		JFrame fr = new JFrame();
		dmi = new DialogoMetaInformacion(fr, ".:: MetaInformacion ::.", false);

		inicializar();
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param aplicacion
	 *            Nombre de la aplicacion en ejecucion
	 * @param usuario
	 *            Nombre del usuario que poseemos actualmente
	 * @param clave
	 *            Clave asociada al usuario
	 * @param space
	 *            JavaSpace asociado a las comunicaciones
	 */
	public ClienteMetaInformacion( String aplicacion, String usuario,
			String clave, JavaSpace space )
	{
		ClienteMetaInformacion.aplicacion = new String(aplicacion);
		ClienteMetaInformacion.usuario = new String(usuario);
		ClienteMetaInformacion.clave = clave;
		this.space = space;
		cmi = this;

		JFrame fr = new JFrame();
		dmi = new DialogoMetaInformacion(fr, ".:: MetaInformacion ::.", false);

		inicializar();
	}

	/**
	 * Localiza el JavaSpace
	 */
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
		else // System.out.println("Error localizando el JavaSpace");
		System.exit(1);
	}

	/**
	 * Obtiene la instancia en ejecucion del cliente de metainformacion
	 * 
	 * @return Cliente de metainformacion en ejecucion
	 */
	public static ClienteMetaInformacion obtenerCMI()
	{
		return cmi;
	}

	/**
	 * Inicializa el cliente enviando una solicitud de sincronizacion al
	 * servidor de Metainformacion
	 */
	private void inicializar()
	{
		try
		{
			// ********** SINCRONIZACION CON EL SERVIDOR DE METAINFORMACION
			// *********//

			// evento en el que se almacenara la respuesta del servidor a la
			// peticion de sincronizacion
			DMIEvent leido = null;

			// evento que sera enviado al servidor para solicitar sincronizacion
			DMIEvent evento = new DMIEvent();

			// evento "template" utilizado para leer la respuesta a la peticion
			// de sincronizacion del servidor
			DMIEvent plantilla = new DMIEvent();

			// aleatorio que servira de ID de la peticion de sincronizacion
			int idEvento = aleatorio();

			// origen y destino del evento
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion

			// tipo de evento
			evento.tipo = new Integer(DMIEvent.SINCRONIZACION.intValue());
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			evento.sincrono = new Boolean(true);

			// escribimos en el JavaSpace la solicitud
			space.write(evento, null, leaseWriteTime);

			// inicializamos la plantilla
			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_SINCRONIZACION
					.intValue());
			plantilla.entero = new Integer(idEvento);
			plantilla.aplicacion = new String(aplicacion);

			// leemos la respuesta a la solicitud
			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);

			if (leido == null)
			{ // Sin respuesta del coordinador
				JOptionPane
						.showMessageDialog(
								null,
								"Error sincronizando con el Servidor de MetaInformacion",
								"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			else contador = leido.contador.longValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion en el cliente de metainformacion\nDebera identificarse de nuevo lalala.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * Identificamos al usuario para concederle, o no, el acceso a la aplicacion
	 * en cuestion
	 * 
	 * @return Metainformacion de la conexion establecida
	 */
	public MIInformacionConexion identificar()
	{

		// inicializamos los eventos a null
		DMIEvent evento = null;
		DMIEvent plantilla = null;
		DMIEvent leido = null;

		// informacion de la conexion
		MIInformacionConexion info = new MIInformacionConexion();

		// ID del evento
		int idEvento = aleatorio();
		try
		{
			// *********** IDENTIFICANDO USUARIO *****************************//
			evento = new DMIEvent();
			plantilla = new DMIEvent();
			idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.IDENTIFICACION.intValue());
			evento.aplicacion = new String(aplicacion);
			evento.usuario = new String(usuario);
			evento.clave = new String(clave); // ********************************************************
			evento.entero = new Integer(idEvento);
			evento.sincrono = new Boolean(true);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_IDENTIFICACION
					.intValue());
			plantilla.entero = new Integer(idEvento);
			plantilla.aplicacion = new String(aplicacion);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del coordinador
				JOptionPane.showMessageDialog(null, "Error de identificacion",
						"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			else
			{
				info = leido.infoConexion;
				if (leido.infoConexion.identificacionValida.booleanValue())
					permisoAdministracion = leido.infoConexion.esAdministrador
							.booleanValue();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion en el cliente de metainformacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		dmi.inicializar(info.rol); // ***************************************************************************************

		// ***************************************************************//

		return info;
	}

	/**
	 * Agrega un Listener para los eventos de metainformacion
	 * 
	 * @param listener
	 *            Listener a agregar
	 */
	public void addDMIListener(DMIListener listener)
	{
		dmilisteners.add(listener);
	}

	/**
	 * Muestra el dialogo que permite modificar la metainformacion del sistema.
	 * 
	 * @pre El usuario debe tener permisos de administracion
	 */
	public void mostrarDialogo()
	{
		if (( dmi != null ) && permisoAdministracion)
		{
			System.out.println("intentando iniciar dmi");
			dmi.setVisible(true);
		}
		else System.out.println("=> Sin permisos para iniciar dmi!!!!");
	}

	/**
	 * Indica si un usuario tiene o no permisos de administracion
	 * 
	 * @return True si el usuario tiene permisos de administracion. False en
	 *         caso contrario
	 */
	public boolean permisosAdministracion()
	{
		return permisoAdministracion;
	}

	/**
	 * Conecta un usuario al servidor de metainformacion
	 * 
	 * @param usuario
	 *            Nombre del usuario a conectar
	 * @param clave
	 *            Clave del usuario
	 * @return Metainformacion completa del nuevo usuario conectado o null si
	 *         ocurrio algun error.
	 */
	public MICompleta conectarUsuario(String usuario, String clave)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		MICompleta info = null;

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.clave = new String(clave);
			evento.tipo = new Integer(DMIEvent.IDENTIFICACION.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.usuario = new String(usuario);
			plantilla.clave = new String(clave);
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_IDENTIFICACION
					.intValue());
			plantilla.entero = new Integer(idEvento);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
				info = new MICompleta();
				info.mensajeError = new String(
						"No hubo respuesta desde el servidor");
			}
			else
			{
				info = leido.infoCompleta;
				ClienteMetaInformacion.permisoAdministracion = info.esAdministrador
						.booleanValue();
				if (info.identificacionValida.booleanValue())
				{
					// ********** PREPARANDO HEBRA RECOLECTORA DE EVENTOS
					// *****//
					Thread recolectora = new Thread(new HebraRecolectora());
					recolectora.start();
					recolectora.setPriority(Thread.MAX_PRIORITY);

					// ********** PREPARANDO HEBRA ENVIO DE KEEPALIVE
					// *********//
					Thread envioKA = new Thread(new HebraEnvioKeepAlive());
					envioKA.start();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return info;
	}

	/**
	 * Permite cambiar el rol a un usuario
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param nuevoRol
	 *            Nombre del nuevo rol que tendra en el sistema
	 * @return Metainformacion nueva del sistema o null si ocurrio algun error.
	 */
	public MICompleta cambiarRolUsuario(String usuario, String nuevoRol)
	{
		MICompleta info = new MICompleta();
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.rol = new String(nuevoRol);
			evento.tipo = new Integer(DMIEvent.CAMBIO_ROL.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.usuario = new String(usuario);
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_CAMBIO_ROL
					.intValue());
			plantilla.entero = new Integer(idEvento);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
				info = new MICompleta();
				info.mensajeError = new String(
						"No hubo respuesta desde el servidor");
			}
			else info = leido.infoCompleta;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return info;
	}

	/**
	 * Permite obtener los usuarios que estan asociados a un determinado rol
	 * 
	 * @param rol
	 *            Nombre del rol
	 * @return Vector con los usuarios o null si hubo algun problema
	 */
	public Vector obtenerUsuariosBajoRol(String rol)
	{
		Vector v = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.OBTENER_USUARIOS_BAJO_ROL
					.intValue());
			evento.rol = new String(rol);
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(
					DMIEvent.RESPUESTA_OBTENER_USUARIOS_BAJO_ROL.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else v = leido.v1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return v;

	}

	/**
	 * Obtiene los usuarios del sistema
	 * 
	 * @return Vector con los usuarios del sistema o null si ocurrio algun
	 *         problema
	 */
	public Vector obtenerUsuarios()
	{
		Vector v = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.OBTENER_USUARIOS.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_USUARIOS
					.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else v = leido.v1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return v;

	}

	/**
	 * Obtiene la metainformacion de un usuario
	 * 
	 * @param nombre
	 *            Nombre del usuario
	 * @return Metainformacion del usuario
	 */
	public MIUsuario obtenerDatosUsuario(String nombre)
	{
		Vector<MIUsuario> v = obtenerDatosUsuarios();

		for (int i = 0; i < v.size(); ++i)
			if (v.get(i).getNombreUsuario().equals(nombre)) return v.get(i);

		return null;
	}

	/**
	 * Obtiene la metainformacion de todos los usuarios del sistema
	 * 
	 * @return Vector con la metainformacion de todos los usuarios o null si
	 *         ocurrio algun error
	 */
	@SuppressWarnings( "unchecked" )
	public Vector<MIUsuario> obtenerDatosUsuarios()
	{
		Vector v = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.OBTENER_USUARIOS.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_USUARIOS
					.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else v = leido.usuarios;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return v;

	}

	/**
	 * Obtiene todos los roles del sistema
	 * 
	 * @return Vector con todos los roles disponibles en el sistema o null si
	 *         ocurrio algun error
	 */
	public Vector obtenerRoles()
	{
		Vector v = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.OBTENER_ROLES.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_ROLES
					.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else v = leido.v1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return v;

	}

	/**
	 * Obtiene la metainformacion de todos los usuarios conectados
	 * 
	 * @return Vector con la metainformacion de todos los usuarios conectados o
	 *         null si ocurrio algun error
	 */
	public Vector<MIUsuario> getUsuariosConectados()
	{

		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.DATOS_USUARIOS.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_DATOS_USUARIOS
					.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
				return null;
			else return leido.usuarios;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion a la hora de obtener los datos de usuario\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return null;
	}

	/**
	 * Obtiene la metainformacion de un usuario, dado su nombre
	 * 
	 * @param nombre
	 *            Nombre del usuario
	 * @return Metainformacion del usuario
	 */
	@SuppressWarnings( "unchecked" )
	public MIUsuario getUsuarioConectado(String nombre)
	{

		Vector<MIUsuario> v = null;
		MIUsuario u = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.DATOS_USUARIOS.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_DATOS_USUARIOS
					.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
				System.out.println("enviada vacio");
			else if (leido.usuarios != null)
			{
				v = leido.usuarios;

				// System.out.println("Tamaño v " + v.size());

				for (int i = 0; i < v.size(); ++i)
				{

					System.out.println("Comparando con: "
							+ v.get(i).getNombreUsuario());

					if (v.get(i).getNombreUsuario().equals(nombre))
						u = v.get(i);
				}
			}
			else System.out.println("v vacio");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion a la hora de obtener los datos de usuario\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return u;
	}

	/**
	 * Obtiene todos los componentes del sistema
	 * 
	 * @return Vector con los componentes del sistema o null si ocurrio algun
	 *         error
	 */
	public Vector obtenerComponentes()
	{
		Vector v = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.OBTENER_COMPONENTES.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_OBTENER_COMPONENTES
					.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else v = leido.v1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return v;

	}

	/**
	 * Obtiene los usuarios conectados en este momento
	 * 
	 * @return Vector con los usuarios conectados en este momento o null si
	 *         ocurrio algun error
	 */
	public Vector obtenerUsuariosConectados()
	{
		Vector v = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.OBTENER_USUARIOS_CONECTADOS
					.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(
					DMIEvent.RESPUESTA_OBTENER_USUARIOS_CONECTADOS.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else v = leido.v1;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return v;

	}

	/**
	 * Obtiene los roles permitidos para un usuario
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @return Vector con los roles permitidos o null si ocurre algun error
	 */
	public Vector obtenerRolesPermitidos(String usuario)
	{
		Vector v = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.tipo = new Integer(DMIEvent.OBTENER_ROLES_PERMITIDOS
					.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.usuario = new String(usuario);
			plantilla.tipo = new Integer(
					DMIEvent.RESPUESTA_OBTENER_ROLES_PERMITIDOS.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else v = leido.v1;
		}
		catch (Exception e)
		{
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(1);
		}
		return v;

	}

	/**
	 * Permite cambiar los permisos a un componente para un usuario concreto
	 * 
	 * @param usuario
	 *            Nombre del usuario
	 * @param componente
	 *            Nombre del componente
	 * @param nuevoPermiso
	 *            Nuevos permisos para el componente
	 * @return True si se cambiaron los permisos con exito. False en caso
	 *         contrario
	 */
	public boolean cambiarPermisoComponenteUsuario(String usuario,
			String componente, int nuevoPermiso)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		boolean cambiado = false;

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.componente = new String(componente);
			evento.permiso = new Integer(nuevoPermiso);
			evento.tipo = new Integer(
					DMIEvent.CAMBIO_PERMISO_COMPONENTE_USUARIO.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.usuario = new String(usuario);
			plantilla.componente = new String(componente);
			plantilla.tipo = new Integer(
					DMIEvent.RESPUESTA_CAMBIO_PERMISO_COMPONENTE_USUARIO
							.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else cambiado = leido.bool.booleanValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return cambiado;
	}

	/**
	 * Cambia los permisos de un componente para un rol
	 * @param rol Nombre del rol
	 * @param componente Nombre del componente
	 * @param nuevoPermiso Nuevo nivel de permisos
	 * @return True si se cambiaron los permisos con exito. False en caso contrario
	 */
	public boolean cambiarPermisoComponenteRol(String rol, String componente,
			int nuevoPermiso)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		boolean cambiado = false;

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.rol = new String(rol);
			evento.componente = new String(componente);
			evento.permiso = new Integer(nuevoPermiso);
			evento.tipo = new Integer(DMIEvent.CAMBIO_PERMISO_COMPONENTE_ROL
					.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.rol = new String(rol);
			plantilla.componente = new String(componente);
			plantilla.tipo = new Integer(
					DMIEvent.RESPUESTA_CAMBIO_PERMISO_COMPONENTE_ROL.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else cambiado = leido.bool.booleanValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return cambiado;
	}

	/**
	 * Obtiene los permisos que tiene un usuario para un componente
	 * @param usuario Nombre del usuario
	 * @param componente Nombre del componente
	 * @return Nivel de permisos o -1 si ocurrio algun error
	 */
	public int obtenerPermisoComponenteUsuario(String usuario, String componente)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		int permiso = -1;

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.componente = new String(componente);
			evento.tipo = new Integer(
					DMIEvent.OBTENER_PERMISO_COMPONENTE_USUARIO.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.usuario = new String(usuario);
			plantilla.componente = new String(componente);
			plantilla.tipo = new Integer(
					DMIEvent.RESPUESTA_OBTENER_PERMISO_COMPONENTE_USUARIO
							.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else permiso = leido.permiso.intValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return permiso;
	}

	/**
	 * Obtiene los permisos que tiene un rol para un componente
	 * @param rol Nombre del rol
	 * @param componente Nombre del componente
	 * @return Nivel de permisos o -1 si ocurrio algun error
	 */
	public int obtenerPermisoComponenteRol(String rol, String componente)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		int permiso = -1;

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.rol = new String(rol);
			evento.componente = new String(componente);
			evento.tipo = new Integer(DMIEvent.OBTENER_PERMISO_COMPONENTE_ROL
					.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.rol = new String(rol);
			plantilla.componente = new String(componente);
			plantilla.tipo = new Integer(
					DMIEvent.RESPUESTA_OBTENER_PERMISO_COMPONENTE_ROL
							.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else permiso = leido.permiso.intValue();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return permiso;
	}

	/**
	 * Permite desconectar un usuario del sistema
	 * @param usuario Nombre del usuario
	 */
	public void desconectarUsuario(String usuario)
	{
		DMIEvent evento = new DMIEvent();
		try
		{
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.tipo = new Integer(DMIEvent.DESCONEXION.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			space.write(evento, null, leaseWriteTime);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * Crea un nuevo usuario
	 * @param nombreUsuario Nombre del usuario a crear
	 * @param clave Clave para el usuario
	 * @param rolDefecto Rol por defecto para el usuario
	 * @param administrador Indica si es administrador o no
	 * @return Mensaje producido por el sistema
	 */
	public String nuevoUsuario(String nombreUsuario, String clave,
			String rolDefecto, boolean administrador)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		String mensaje = new String();

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.nuevoUsuario = new String(nombreUsuario);
			evento.clave = new String(clave);
			evento.rol = new String(rolDefecto);
			evento.bool = new Boolean(administrador);
			evento.tipo = new Integer(DMIEvent.NUEVO_USUARIO.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.nuevoUsuario = new String(nombreUsuario);
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_USUARIO
					.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else mensaje = leido.mensaje;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return mensaje;

	}

	/**
	 * Crea un nuevo rol en el sistema
	 * @param rol Nombre del rol
	 * @return Mensaje producido por el sistema
	 */
	public String nuevoRol(String rol)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		String mensaje = new String();

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.rol = new String(rol);
			evento.tipo = new Integer(DMIEvent.NUEVO_ROL.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.rol = new String(rol);
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_ROL
					.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else mensaje = leido.mensaje;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return mensaje;

	}

	/**
	 * Asigna un nuevo rol permitido a un usuario concreto
	 * @param usuario Nombre del usuario
	 * @param rol Nombre del nuevo rol permitido
	 * @return Mensaje producido por el sistema
	 */
	public String nuevoRolPermitido(String usuario, String rol)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		String mensaje = new String();

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.rol = new String(rol);
			evento.tipo = new Integer(DMIEvent.NUEVO_ROL_PERMITIDO.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.usuario = new String(usuario);
			plantilla.rol = new String(rol);
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_NUEVO_ROL_PERMITIDO
					.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else mensaje = leido.mensaje;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return mensaje;

	}

	/**
	 * Elimina un rol permitido para un usuario
	 * @param usuario Nombre del usuario
	 * @param rol Nombre del rol
	 * @return Mensaje producido por el sistema
	 */
	public String eliminarRolPermitido(String usuario, String rol)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		String mensaje = new String();

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(usuario);
			evento.rol = new String(rol);
			evento.tipo = new Integer(DMIEvent.ELIMINAR_ROL_PERMITIDO
					.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.usuario = new String(usuario);
			plantilla.rol = new String(rol);
			plantilla.tipo = new Integer(
					DMIEvent.RESPUESTA_ELIMINAR_ROL_PERMITIDO.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else mensaje = leido.mensaje;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return mensaje;

	}

	/**
	 * Elimina un usuario del sistema
	 * @param nombreUsuario Nombre del usuario a eliminar
	 * @return Mensaje producido por el sistema
	 */
	public String eliminarUsuario(String nombreUsuario)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		String mensaje = new String();

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.usuario = new String(nombreUsuario);
			evento.tipo = new Integer(DMIEvent.ELIMINAR_USUARIO.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.usuario = new String(nombreUsuario);
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_ELIMINAR_USUARIO
					.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else mensaje = leido.mensaje;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return mensaje;

	}

	/**
	 * Elimina un rol del sistema
	 * @param rol Nombre del rol a eliminar
	 * @return Mensaje producido por el sistema
	 */
	public String eliminarRol(String rol)
	{
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();
		String mensaje = new String();

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.rol = new String(rol);
			evento.tipo = new Integer(DMIEvent.ELIMINAR_ROL.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.rol = new String(rol);
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_ELIMINAR_ROL
					.intValue());
			plantilla.entero = new Integer(aleatorio);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.sincrono = new Boolean(true);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else mensaje = leido.mensaje;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return mensaje;

	}
	
	/**
	 * Obtiene la metainformacion de un rol, dado su nombre
	 * @param drol Nombre del rol
	 * @return Metainformacion del rol o null si ocurrio algun error
	 */
	public MIRol getRol(String drol)
	{
		MIRol rol = null;
		DMIEvent leido = null;
		DMIEvent evento = new DMIEvent();
		DMIEvent plantilla = new DMIEvent();

		try
		{
			int idEvento = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.DATOS_ROL.intValue());
			evento.sincrono = new Boolean(true);
			evento.aplicacion = new String(aplicacion);
			evento.entero = new Integer(idEvento);
			evento.clave = new String(drol);
			evento.rol = new String(DConector.Drol);
			space.write(evento, null, leaseWriteTime);

			System.err.println(drol);

			plantilla.origen = new Integer(10); // Servidor MetaInformacion
			plantilla.destino = new Integer(11); // Cliente MetaInformacion
			plantilla.tipo = new Integer(DMIEvent.RESPUESTA_DATOS_ROL
					.intValue());
			plantilla.sincrono = new Boolean(true);
			plantilla.aplicacion = new String(aplicacion);
			plantilla.entero = new Integer(idEvento);

			leido = (DMIEvent) space.take(plantilla, null, leaseReadTime);
			if (leido == null)
			{ // Sin respuesta del servidor
			}
			else rol = leido.datosRol;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion a la hora de obtener rol en el CMI\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		return rol;
	}

	/**
	 * Guarda los cambios fisicamente
	 * @return Mensaje producido por el sistema
	 */
	public String guardarCambios()
	{
		DMIEvent evento = new DMIEvent();
		String mensaje = new String();

		try
		{
			int aleatorio = aleatorio();
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.tipo = new Integer(DMIEvent.SAVE_CHANGES.intValue());
			evento.entero = new Integer(aleatorio);
			space.write(evento, null, leaseWriteTime);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane
					.showMessageDialog(
							null,
							"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
							"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		return mensaje;

	}

	/**
	 * Genera un numero aleatorio
	 * @return Numero aleatorio en el intervalo [0, 51000)
	 */
	private int aleatorio()
	{
		Date fecha = new Date();
		Random r = new Random(fecha.getTime());
		int aleatorio = r.nextInt(50000);
		aleatorio += 1000;
		return aleatorio;
	}

	/**
	 * Hebra encargada de localizar el JavaSpace
	 */
	private class HebraLocalizadora implements Runnable
	{
		private String nombre = null;

		private Thread t = null;

		/**
		 * Constructor
		 * @param nombreJavaSpace
		 *            Nombre del JavaSpace que deseamos localizar
		 */
		public HebraLocalizadora( String nombreJavaSpace )
		{
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
				space = SpaceLocator.getSpace(nombre);
				// Localizacion del JavaSpace exitosa
				monitor.setInicializado(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(
								null,
								"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
								"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
	}

	/**
	 * Permite indicar que no ha sido posible localizar el
	 * JavaSpace pasado el tiempo indicado. De esta forma evitamos que el
	 * programa se quede bloqueado en la fase de localizacion del JavasPace
	 */
	private class HebraDetectoraError implements Runnable
	{
		private int tiempoEspera = -1;

		private Thread t = null;

		/**
		 * Constructor
		 * @param tiempoEspera
		 *            Tiempo que deseamos esperar
		 */
		public HebraDetectoraError( int tiempoEspera )
		{
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
				if (!monitor.getInicializado()) monitor.setError(true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(
								null,
								"Hubo un error en la comunicacion\nDebera identificarse de nuevo.",
								"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
	}

	/**
	 * Permite mantener vivo al usuario en el sistema
	 */
	private class HebraEnvioKeepAlive implements Runnable
	{
		private DMIEvent evento = new DMIEvent();

		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			evento.origen = new Integer(11); // Cliente MetaInformacion
			evento.destino = new Integer(10); // Servidor MetaInformacion
			evento.aplicacion = new String(aplicacion);
			evento.usuario = new String(usuario);
			evento.tipo = new Integer(DMIEvent.KEEPALIVE.intValue());
			try
			{
				while (true)
				{
					Thread.sleep(30000);
					space.write(evento, null, leaseWriteTime);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				JOptionPane
						.showMessageDialog(
								null,
								"Hubo un error en la comunicacion a la hora de enviar el KeepAlive\nDebera identificarse de nuevo.",
								"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
	}

	/**
	 * Dado el comportamiento concurrente de las 2 Hebras, mediante este Monitor
	 * gestionamos la informacion sobre la inicializacion. La clase DConector
	 * realizara una llamada a inicializacionCorrecta() quedandose bloqueada
	 * hasta que se produzca la correcta localizacion del JavaSpace o se
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
		 * @return Devuelve si se ha producido o no la inicializacion
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
								"Hubo un error en la comunicacion al inicializar el CMI\nDebera identificarse de nuevo.",
								"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}

			return inicializado;
		}

		/**
		 * Permite obtener si se ha sobrepasado el tiempo de espera sin haber sido
		 * localizado el JavaSpace
		 * 
		 * @return Indica si se ha producido o no un error
		 */
		public synchronized boolean getError()
		{
			return error;
		}

		/**
		 * Permite cambiar el valor de la variable de error
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
		 * Permite obtener si se ha localizado el JavaSpace
		 * 
		 * @return True si se ha inicializado. False en caso contrario
		 */
		public synchronized boolean getInicializado()
		{
			return inicializado;
		}

		/**
		 * Permite cambiar el valor de la variable de inicializacion
		 * 
		 * @param b
		 *            Valor deseado
		 */
		public synchronized void setInicializado(boolean b)
		{
			inicializado = b;
			notifyAll();
		}

		/**
		 * Permite obtener si se ha sincronizado correctamente o no
		 * 
		 * @return True si se ha sincronizado correctamente. False en caso contrario
		 */
		public synchronized boolean getSincronizado()
		{
			return sincronizado;
		}

		/**
		 * Permite cambiar el valor de la variable de sincronizacion
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
	 * Hebra que permite recopilar los eventos del sistema
	 */
	private class HebraRecolectora implements Runnable
	{
		/**
		 * Ejecucion de la hebra
		 */
		public void run()
		{
			DMIEvent leido = null;
			DMIEvent plantilla = new DMIEvent();
			plantilla.origen = new Integer(10); // El Servidor de
			// MetaInformacion
			plantilla.destino = new Integer(11); // El Cliente de
			// MetaInformacion
			plantilla.sincrono = new Boolean(false);
			while (true)
				try
				{
					plantilla.contador = new Long(contador);
					// System.out.println("ClienteMI: Esperando evento " +
					// contador);
					leido = (DMIEvent) space.read(plantilla, null,
							Long.MAX_VALUE);
					contador++;

					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_CONEXION_USUARIO
							.intValue())
					{
						String usuario = leido.usuario;
						String rol = leido.rol;
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) ).conexionUsuario(
									usuario, rol);

					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_DESCONEXION_USUARIO
							.intValue())
					{
						String usuario = leido.usuario;
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) )
									.desconexionUsuario(usuario);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO
							.intValue())
					{
						String usuario = leido.usuario;
						String rol = leido.rol;
						String rolAntiguo = leido.rolAntiguo;
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) ).cambioRolUsuario(
									usuario, rol, rolAntiguo,
									leido.infoCompleta);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_USUARIO
							.intValue())
					{
						String usuario = leido.usuario;
						String componente = leido.componente;
						int permiso = leido.permiso.intValue();
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) )
									.cambioPermisoComponenteUsuario(usuario,
											componente, permiso);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_ROL
							.intValue())
					{
						String rol = leido.rol;
						String componente = leido.componente;
						int permiso = leido.permiso.intValue();
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) )
									.cambioPermisoComponenteRol(rol,
											componente, permiso);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_NUEVO_ROL_PERMITIDO
							.intValue())
					{
						String usuario = leido.usuario;
						String rol = leido.rol;
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) ).nuevoRolPermitido(
									usuario, rol);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_ELIMINAR_ROL_PERMITIVO
							.intValue())
					{
						String usuario = leido.usuario;
						String rol = leido.rol;
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) ).eliminarRolPermitido(
									usuario, rol);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_USUARIO_ELIMINADO
							.intValue())
					{
						String usuario = leido.usuario;
						dmi.eliminarUsuario(usuario);
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) )
									.usuarioEliminado(usuario);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_ROL_ELIMINADO
							.intValue())
					{
						String rol = leido.rol;
						dmi.eliminarRol(rol);
						for (int i = 0; i < dmilisteners.size(); i++)
							( dmilisteners.elementAt(i) ).rolEliminado(rol);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_NUEVO_USUARIO
							.intValue())
					{
						String usuario = leido.usuario;
						dmi.nuevoUsuario(usuario);
					}
					if (leido.tipo.intValue() == DMIEvent.NOTIFICACION_NUEVO_ROL
							.intValue())
					{
						String rol = leido.rol;
						dmi.nuevoRol(rol);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					JOptionPane
							.showMessageDialog(
									null,
									"Hubo un error en la comunicacion en la hebra recolectora\nDebera identificarse de nuevo.",
									"Error", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
				}
		}
	}
}
