package aplicacion.fisica.documentos.filtros;

import java.awt.Image;
import java.awt.Toolkit;

import javax.imageio.ImageIO;

import aplicacion.fisica.ServidorFicheros;
import aplicacion.fisica.documentos.Documento;

public class ImageFilter implements DocumentFilter
{
	public ImageFilter()
	{
		
	}
	
	public Documento getDocumento(String path, String usuario, String rol)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image imagen = toolkit.getImage(ServidorFicheros.getDirectorioBase()
				+ path);

		Documento doc = new Documento(usuario, rol);
		doc.addPagina(imagen);
		doc.setPath(path);
		return doc;
	}

	public boolean isSupported(String extension)
	{
		String[] readFormats = ImageIO.getReaderFormatNames();
		for (String element : readFormats)
			if (element.toLowerCase().compareTo(extension) == 0)
			{
				return true;
			}
		
		return false;
	}

}
