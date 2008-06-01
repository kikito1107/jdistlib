package aplicacion.fisica.documentos.filtros;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.MIDocumento;

public class MSGFilter implements DocumentFilter
{
	public MSGFilter()
	{
		
	}

	public Documento getDocumento(String path, String usuario, String rol)
	{
		StringBuffer contents = new StringBuffer();

		try
		{
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			File f = new File(path);
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

	public boolean isSupported(String extension)
	{
		if (extension.toLowerCase().compareTo(MIDocumento.TIPO_MENSAJE) == 0)
			return true;
		
		else return false;
	}

}
