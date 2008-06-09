package metainformacion;

import java.io.Serializable;
import java.util.Vector;

public class MIInformacionConexion implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String mensajeError = null;

	public String rol = null;

	public Boolean esAdministrador = new Boolean(false);

	public Vector<MIComponente> componentes = new Vector<MIComponente>();

	public Boolean identificacionValida = new Boolean(false);

	@SuppressWarnings( "unchecked" )
	public Vector rolesPermitidos = new Vector();

	@SuppressWarnings( "unchecked" )
	public Vector usuariosConectados = new Vector();

}
