package Deventos;

import java.util.Vector;

import awareness.MICompleta;
import awareness.MIComponente;
import awareness.MIInformacionConexion;
import awareness.MIRol;
import awareness.MIUsuario;


/**
 * Eventos distribuidos de metainformacion
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DMIEvent extends DEvent
{
	private static final long serialVersionUID = -2794199637466668512L;

	public static final Integer IDENTIFICACION = new Integer(0);

	public static final Integer RESPUESTA_IDENTIFICACION = new Integer(1);

	public static final Integer DESCONEXION = new Integer(2);

	public static final Integer CAMBIO_ROL = new Integer(3);

	public static final Integer RESPUESTA_CAMBIO_ROL = new Integer(4);

	public static final Integer CAMBIO_PERMISO = new Integer(5);

	public static final Integer RESPUESTA_CAMBIO_PERMISO = new Integer(6);

	public static final Integer NUEVO_ROL_PERMITIDO = new Integer(7);

	public static final Integer ELIMINAR_ROL_PERMITIDO = new Integer(8);

	public static final Integer CAMBIO_PERMISO_COMPONENTE = new Integer(9);

	public static final Integer RESPUESTA_CAMBIO_PERMISO_COMPONENTE = new Integer(
			10);

	public static final Integer OBTENER_USUARIOS_BAJO_ROL = new Integer(11);

	public static final Integer RESPUESTA_OBTENER_USUARIOS_BAJO_ROL = new Integer(
			12);

	public static final Integer OBTENER_USUARIOS = new Integer(13);

	public static final Integer RESPUESTA_OBTENER_USUARIOS = new Integer(14);

	public static final Integer OBTENER_ROLES = new Integer(15);

	public static final Integer RESPUESTA_OBTENER_ROLES = new Integer(16);

	public static final Integer CAMBIO_PERMISO_COMPONENTE_USUARIO = new Integer(
			17);

	public static final Integer RESPUESTA_CAMBIO_PERMISO_COMPONENTE_USUARIO = new Integer(
			18);

	public static final Integer CAMBIO_PERMISO_COMPONENTE_ROL = new Integer(19);

	public static final Integer RESPUESTA_CAMBIO_PERMISO_COMPONENTE_ROL = new Integer(
			20);

	public static final Integer OBTENER_PERMISO_COMPONENTE_USUARIO = new Integer(
			21);

	public static final Integer RESPUESTA_OBTENER_PERMISO_COMPONENTE_USUARIO = new Integer(
			22);

	public static final Integer OBTENER_PERMISO_COMPONENTE_ROL = new Integer(23);

	public static final Integer RESPUESTA_OBTENER_PERMISO_COMPONENTE_ROL = new Integer(
			24);

	public static final Integer SINCRONIZACION = new Integer(25);

	public static final Integer RESPUESTA_SINCRONIZACION = new Integer(26);

	public static final Integer OBTENER_COMPONENTES = new Integer(27);

	public static final Integer RESPUESTA_OBTENER_COMPONENTES = new Integer(28);

	public static final Integer OBTENER_ROLES_PERMITIDOS = new Integer(29);

	public static final Integer RESPUESTA_OBTENER_ROLES_PERMITIDOS = new Integer(
			30);

	public static final Integer OBTENER_USUARIOS_CONECTADOS = new Integer(31);

	public static final Integer RESPUESTA_OBTENER_USUARIOS_CONECTADOS = new Integer(
			32);

	public static final Integer NOTIFICACION_CONEXION_USUARIO = new Integer(33);

	public static final Integer NOTIFICACION_DESCONEXION_USUARIO = new Integer(
			34);

	public static final Integer NOTIFICACION_CAMBIO_ROL_USUARIO = new Integer(
			35);

	public static final Integer NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_USUARIO = new Integer(
			36);

	public static final Integer NOTIFICACION_CAMBIO_PERMISO_COMPONENTE_ROL = new Integer(
			37);

	public static final Integer INFO_COMPLETA = new Integer(38);

	public static final Integer NUEVO_USUARIO = new Integer(39);

	public static final Integer RESPUESTA_NUEVO_USUARIO = new Integer(39);

	public static final Integer ELIMINAR_USUARIO = new Integer(40);

	public static final Integer RESPUESTA_ELIMINAR_USUARIO = new Integer(41);

	public static final Integer ELIMINAR_ROL = new Integer(42);

	public static final Integer RESPUESTA_ELIMINAR_ROL = new Integer(43);

	public static final Integer NUEVO_ROL = new Integer(44);

	public static final Integer RESPUESTA_NUEVO_ROL = new Integer(45);

	public static final Integer RESPUESTA_NUEVO_ROL_PERMITIDO = new Integer(46);

	public static final Integer RESPUESTA_ELIMINAR_ROL_PERMITIDO = new Integer(
			47);

	public static final Integer NOTIFICACION_USUARIO_ELIMINADO = new Integer(48);

	public static final Integer NOTIFICACION_ROL_ELIMINADO = new Integer(49);

	public static final Integer NOTIFICACION_NUEVO_ROL_PERMITIDO = new Integer(
			50);

	public static final Integer NOTIFICACION_ELIMINAR_ROL_PERMITIVO = new Integer(
			51);

	public static final Integer NOTIFICACION_NUEVO_USUARIO = new Integer(52);

	public static final Integer NOTIFICACION_NUEVO_ROL = new Integer(53);

	public static final Integer DATOS_USUARIOS = new Integer(54);

	public static final Integer RESPUESTA_DATOS_USUARIOS = new Integer(55);

	public static final Integer DATOS_ROL = new Integer(56);

	public static final Integer RESPUESTA_DATOS_ROL = new Integer(57);

	public static final Integer KEEPALIVE = new Integer(500);

	public static final Integer SAVE_CHANGES = new Integer(58);

	@SuppressWarnings( "unchecked" )
	public Vector v1 = null;

	@SuppressWarnings( "unchecked" )
	public Vector v2 = null;

	public String clave = null;

	public String rol = null;

	public String rolAntiguo = null;

	public String usuarioAEliminar = null;

	public String componente = null;

	public String mensaje = null;

	public Integer permiso = null;

	public Integer permisoU = null;

	public Integer permisoR = null;

	public Boolean bool = null;

	public Boolean sincrono = null;

	public Integer entero = null;

	public MIInformacionConexion infoConexion = null;

	public MICompleta infoCompleta = null;

	public String nuevoUsuario = null;

	public MIRol datosRol = null;

	public Vector<MIUsuario> usuarios = null;

	/**
	 * Obtiene los permisos asociados a un componente
	 * 
	 * @param componente
	 *            Componente del cual hallar los permisos
	 * @return Permisos del componente
	 */
	public int obtenerPermisoComponente(String componente)
	{
		int permiso = -1;
		MIComponente cmp = obtenerComponente(componente);
		if (cmp != null) permiso = cmp.permisoComponente();
		return permiso;
	}

	/**
	 * Obtiene la metainformacion de un componente dando su nombre registrado en
	 * el sistema
	 * 
	 * @param componente
	 *            Nombre del componente
	 * @return Metainformacion del componente o null en caso de que no exista
	 */
	private MIComponente obtenerComponente(String componente)
	{
		MIComponente cmp = null;
		MIComponente aux = null;
		if (infoCompleta != null)
			for (int i = 0; i < infoCompleta.componentes.size(); i++)
			{
				aux = infoCompleta.componentes.elementAt(i);
				if (aux.getNombreComponente().equals(componente)) cmp = aux;
			}
		return cmp;
	}

	/**
	 * Convierte el evento a una cadena de texto. Permite realizar logging
	 */
	@Override
	public String toString()
	{
		return new String("tipo=" + tipo + " sincrono=" + sincrono
				+ " aplicacion=" + aplicacion + " usuario=" + usuario
				+ " clave=" + clave + " rol=" + rol + " entero=" + entero
				+ " permiso=" + permiso + " contador=" + contador);
	}
}
