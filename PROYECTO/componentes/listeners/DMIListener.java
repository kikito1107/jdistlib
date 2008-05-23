package componentes.listeners;

import metainformacion.MICompleta;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public interface DMIListener
{
	public void conexionUsuario(String usuario, String rol);

	public void desconexionUsuario(String usuario);

	public void cambioRolUsuario(String usuario, String rol, String rolAntiguo,
			MICompleta info);

	public void cambioPermisoComponenteUsuario(String usuario,
			String componente, int permiso);

	public void cambioPermisoComponenteRol(String rol, String componente,
			int permiso);

	public void nuevoRolPermitido(String usuario, String rol);

	public void eliminarRolPermitido(String usuario, String rol);

	public void usuarioEliminado(String usuario);

	public void rolEliminado(String rol);
}
