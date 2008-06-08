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
 */

@SuppressWarnings( "unchecked" )
public class ClienteMetaInformacion
{
	public static ClienteMetaInformacion cmi = null;

	public static String usuario = null;

	public static String clave = null;

	public static String aplicacion = null;

	private DialogoMetaInformacion dmi = null;

	private static final long leaseWriteTime = Lease.FOREVER;

	private static final long leaseReadTime = 10000L;

	private JavaSpace space = null;

	private static final Vector<DMIListener> dmilisteners = new Vector<DMIListener>();

	private static boolean permisoAdministracion = false;

	private Monitor monitor = new Monitor();

	private long contador = -1;

	@SuppressWarnings( "static-access" )
	public ClienteMetaInformacion( String aplicacion2, String usuario2,
			String clave2 )
	{

		this.aplicacion = new String(aplicacion2);
		this.usuario = new String(usuario2);
		this.clave = new String(clave2);
		localizarJavaSpace();
		cmi = this;

		JFrame fr = new JFrame();
		dmi = new DialogoMetaInformacion(fr, ".:: MetaInformacion ::.", false);

		inicializar();
	}

	@SuppressWarnings( "static-access" )
	public ClienteMetaInformacion( String aplicacion, String usuario,
			String clave, JFrame frame, JavaSpace space )
	{
		this.aplicacion = new String(aplicacion);
		this.usuario = new String(usuario);
		this.clave = clave;
		this.space = space;
		cmi = this;

		JFrame fr = new JFrame();
		dmi = new DialogoMetaInformacion(fr, ".:: MetaInformacion ::.", false);

		inicializar();
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
		else // System.out.println("Error localizando el JavaSpace");
		System.exit(1);
	}

	public static ClienteMetaInformacion obtenerCMI()
	{
		return cmi;
	}

	/**
	 * inicializa el cliente enviando una solicitud de sincronizacion al
	 * servidor de Metainformacion
	 */
	public void inicializar()
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
	 * @return
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

	public void addDMIListener(DMIListener listener)
	{
		dmilisteners.add(listener);
	}

	public void mostrarDialogo()
	{
		if (( dmi != null ) && permisoAdministracion)
		{
			System.out.println("intentando iniciar dmi");
			dmi.setVisible(true);
		}
		else System.out.println("=> Sin permisos para iniciar dmi!!!!");
	}

	public boolean permisosAdministracion()
	{
		return permisoAdministracion;
	}

	@SuppressWarnings( "static-access" )
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
				this.permisoAdministracion = info.esAdministrador
						.booleanValue();
				if (info.identificacionValida.booleanValue())
				{
					// ********** PREPARANDO HEBRA RECOLECTORA DE EVENTOS
					// ************//
					Thread recolectora = new Thread(new HebraRecolectora());
					recolectora.start();
					recolectora.setPriority(Thread.MAX_PRIORITY);
					// ***************************************************************//
					// ********** PREPARANDO HEBRA ENVIO DE KEEPALIVE
					// ************//
					Thread envioKA = new Thread(new HebraEnvioKeepAlive());
					envioKA.start();
					// ***************************************************************//
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

	public MIUsuario obtenerDatosUsuario(String nombre)
	{
		Vector<MIUsuario> v = obtenerDatosUsuarios();

		for (int i = 0; i < v.size(); ++i)
			if (v.get(i).getNombreUsuario().equals(nombre)) return v.get(i);

		return null;
	}

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

	@SuppressWarnings( "unchecked" )
	public MIUsuario getUsuarioConectado(String Nombre)
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

				//System.out.println("Tamaño v " + v.size());

				for (int i = 0; i < v.size(); ++i)
				{

					System.out.println("Comparando con: "
							+ v.get(i).getNombreUsuario());

					if (v.get(i).getNombreUsuario().equals(Nombre))
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

	public String guardarCambiosBD()
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

	private int aleatorio()
	{
		Date fecha = new Date();
		Random r = new Random(fecha.getTime());
		int aleatorio = r.nextInt(50000);
		aleatorio += 1000;
		return aleatorio;
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

	private class HebraEnvioKeepAlive implements Runnable
	{
		DMIEvent evento = new DMIEvent();

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
								"Hubo un error en la comunicacion al inicializar el CMI\nDebera identificarse de nuevo.",
								"Error", JOptionPane.ERROR_MESSAGE);
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

	private class HebraRecolectora implements Runnable
	{

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

	public static void main(String[] args)
	{
		// ClienteMetaInformacion cmi = new
		// ClienteMetaInformacion("AplicacionDePrueba");
		// cmi.desconectarUsuario("Looper");
		// System.out.println(cmi.obtenerRoles());
	}

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
}
