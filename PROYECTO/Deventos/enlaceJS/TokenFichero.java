package Deventos.enlaceJS;

public class TokenFichero extends Token
{
	public String Fichero = null;
	public String ip = null;
	public Integer NumUsuarios = null;
	public Boolean sincronizar = null;
	
	public TokenFichero(String aplicacion, String fichero){
		this.aplicacion = aplicacion;
		this.NumUsuarios = null;
		this.Fichero = fichero;
		
	}
	
	public TokenFichero(){
		super();
	}

	public void bajaUsuario()
	{
		if (NumUsuarios == null)
			NumUsuarios = new Integer(0);
		else if (NumUsuarios.intValue() > 0)
			NumUsuarios = new Integer(NumUsuarios.intValue() - 1);
	}

	public void nuevoUsuario()
	{
		if (NumUsuarios == null)
			NumUsuarios = new Integer(1);
		else
			NumUsuarios = new Integer(NumUsuarios.intValue() + 1);
	}
}
