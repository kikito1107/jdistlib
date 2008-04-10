package aplicacion.fisica.documentos;

import metainformacion.MIUsuario;
import metainformacion.MIRol;

public class FicheroBD 
{
	private String nombre;
	private int id_documento;
	private boolean es_directorio;
	private String permisos;
	private MIUsuario usuario;
	private MIRol rol;
	private int padre;
	private String ruta_local;
	private String tipo;
	
	public FicheroBD()
	{
		
	}
	
	public FicheroBD(int id, String nom, boolean dir, String perm, MIUsuario usu, MIRol ro, int pad, String ruta, String tip)
	{
		nombre = nom;
		id_documento = id;
		es_directorio = dir;
		permisos = perm;
		usuario = usu;
		rol = ro;
		padre = pad;
		ruta_local = ruta;
		tipo = tip;
	}

	public String getNombre()
	{
		return nombre;
	}

	public int getId()
	{
		return id_documento;
	}

	public boolean esDirectorio()
	{
		return es_directorio;
	}

	public String getPermisos()
	{
		return permisos;
	}

	public MIUsuario getUsuario()
	{
		return usuario;
	}

	public MIRol getRol()
	{
		return rol;
	}
	
	public int getPadre()
	{
		return padre;
	}

	public String getRutaLocal()
	{
		return ruta_local;
	}
	
	public String getTipo()
	{
		return tipo;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public void setId(int id_documento)
	{
		this.id_documento = id_documento;
	}

	public void esDirectorio(boolean es_directorio)
	{
		this.es_directorio = es_directorio;
	}

	public void setPermisos(String permisos)
	{
		this.permisos = permisos;
	}

	public void setUsuario(MIUsuario usuario)
	{
		this.usuario = usuario;
	}

	public void setRol(MIRol rol)
	{
		this.rol = rol;
	}

	public void setPadre(int padre)
	{
		this.padre = padre;
	}

	public void setRutaLocal(String ruta_local)
	{
		this.ruta_local = ruta_local;
	}
	
	public void setTipo(String tip)
	{
		this.tipo = tip;
	}
	
	public String toString()
	{
		return this.nombre;
	}
	
}
