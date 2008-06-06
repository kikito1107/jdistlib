package aplicacion.fisica.documentos.filtros;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import aplicacion.fisica.ServidorFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.MIDocumento;

/**
 * Filtro de documento para ficheros de texto plano
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz.
 *
 */
public class TXTFilter implements DocumentFilter
{
	/**
	 * Constructor por defecto
	 */
	public TXTFilter()
	{
		
	}

	/**
	 * Obtiene un documento interpretado como texto plano
	 * @param path Path del documento a abrir
	 * @param usuario Nombre del usuario que quiere abrir el documento
	 * @param rol Rol que desempeľa el usuario
	 * @return Objeto de la clase @see Documento. Devuelve null si ha ocurrido algun error
	 */
	public Documento getDocumento(String path, String usuario, String rol)
	{
		StringBuffer contents = new StringBuffer();

		try
		{
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			File f = new File(ServidorFicheros.getDirectorioBase() + path);
			BufferedReader input = new BufferedReader(new FileReader(f));
			try
			{
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				while (( line = input.readLine() ) != null)
				{
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			}
			finally
			{
				input.close();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		String contenido = contents.toString();

		Font font = new Font("Arial", Font.PLAIN, 16);

		BufferedImage bi = new BufferedImage(800, 1280,
				BufferedImage.TYPE_INT_RGB);

		Graphics g = bi.getGraphics();
		g.setColor(java.awt.Color.white);
		g.fillRect(0, 0, 800, 1280);
		g.setColor(java.awt.Color.black);
		g.setFont(font);

		int k = 1;
		Documento doc = new Documento(usuario, rol);
		String[] lineas = contenido.split("\n");
		String str;
		int npag = 1;
		for (String element : lineas)
		{
			str = element;

			// Pintamos la linea. Si la longitud es mayor de 80 la dividimos
			// sucesivamente.
			do
				if (str.length() < 80)
				{
					g.drawString(str, 25, ( 8 * k + 32 )
							+ ( 8 * ( k - 1 ) + 32 ));
					k++;
					str = "";
				}
				else
				{
					String aux = str.substring(0, 79);
					g.drawString(aux, 25, ( 8 * k + 32 )
							+ ( 8 * ( k - 1 ) + 32 ));
					k++;
					str = str.substring(80);
				}
			while (!str.equals(""));

			// si ya hemos llenado una pagina entera
			if (( 8 * k + 32 ) + ( 8 * ( k - 1 ) + 32 ) >= 1240)
			{
				doc.addPagina(bi);
				bi = new BufferedImage(800, 1280, BufferedImage.TYPE_INT_RGB);
				g = bi.getGraphics();
				g.setColor(java.awt.Color.white);
				g.fillRect(0, 0, 800, 1280);
				g.setColor(java.awt.Color.black);
				g.setFont(font);
				k = 1;
				npag++;

				System.gc();
			}
		}

		if (doc.getNumeroPaginas() != npag) doc.addPagina(bi);
		doc.setPath(path);
		return doc;
	}

	/**
	 * Comprueba si la extension se corresponde con un fichero de texto plano (txt)
	 * @param extension Extension del tipo de documento a consultar
	 * @return True si es una extension tipica para un texto plano y False en caso contrario
	 */
	public boolean isSupported(String extension)
	{
		if (extension.toLowerCase().compareTo(MIDocumento.TIPO_BANDEJA_TXT) == 0)
			return true;
		
		else return false;
	}

}
