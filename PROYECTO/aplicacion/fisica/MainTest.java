package aplicacion.fisica;

import aplicacion.fisica.documentos.FicheroBD;
import java.util.Vector;

public class MainTest
{
	
	private static int nivel = 0;
	
	/**
	 * Metodo que nos permite visualizar en forma de arbol el contenido de la base de datos de ficheros
	 * @param f fichero del que queremos partir
	 * @param g gestor de ficheros asociado a la base de datos
	 */
	public static void pintarFichero(FicheroBD f, GestorFicherosBD g) {
		
		String vineta = "- ";
		
		if (f.esDirectorio())
			vineta = "+ ";
		
		System.out.println(vineta + f.toString());
		
		if ( f.esDirectorio() ) {
			Vector<FicheroBD> fs = g.recuperarDirectorio(f.getId());
			
			nivel ++;
			
			for (int i=1; i < fs.size(); ++i) {
				for (int j=0; j<nivel; ++j)
					System.out.print("\t");
				
				MainTest.pintarFichero(fs.get(i), g);
			}
			
			nivel --;
		}
	}
	
	public static void main(String args[])
	{
		GestorFicherosBD g = new GestorFicherosBD();
		Vector<FicheroBD> fich = g.recuperar();
		System.out.println("\n"+fich);
		
		pintarFichero(fich.get(0), g);
	}
}
