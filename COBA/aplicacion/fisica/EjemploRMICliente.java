package aplicacion.fisica;

import aplicacion.fisica.documentos.*;
import javax.imageio.*;
import java.awt.image.*;

public class EjemploRMICliente
{
	public static void main(String args[])
	{
		Transfer ts = new Transfer("192.168.0.15", "/a.pdf");
		Documento f = ts.receive();
		
		if (f != null)
		System.out.println(f.getNumeroPaginas());
		
		else System.out.println("Tipo No Soportado");
	}
}
