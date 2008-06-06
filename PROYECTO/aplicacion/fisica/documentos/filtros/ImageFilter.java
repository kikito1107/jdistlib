package aplicacion.fisica.documentos.filtros;

import java.awt.Image;
import java.awt.Toolkit;

import javax.imageio.ImageIO;

import aplicacion.fisica.ServidorFicheros;
import aplicacion.fisica.documentos.Documento;

/**
 * Filtro de documentos para imagenes
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz.
 */
public class ImageFilter implements DocumentFilter
{
	/**
	 * Constructor por defecto
	 */
	public ImageFilter()
	{
		
	}
	
	/**
	 * Obtiene un documento interpretado como imagen
	 * @param path Path de la imagen a abrir
	 * @param usuario Nombre del usuario que quiere abrir el documento
	 * @param rol Rol que desempe–a el usuario
	 * @return Objeto de la clase @see Documento. Devuelve null si ha ocurrido algun error
	 */
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

	/**
	 * Comprueba si la extension se corresponde con un fichero de imagen
	 * @param extension Extension del tipo de documento a consultar
	 * @return True si es una extension tipica para una imagen y False en caso contrario
	 */
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
