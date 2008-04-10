package metainformacion;

import java.io.*;
import java.util.*;

public class MIInformacionConexion
	 implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
public String mensajeError = null;
  public String rol = null;
  public Boolean esAdministrador = new Boolean(false);
  public Vector<MIComponente> componentes = new Vector<MIComponente>();
  public Boolean identificacionValida = new Boolean(false);
  public Vector rolesPermitidos = new Vector();
  public Vector usuariosConectados = new Vector();

}
