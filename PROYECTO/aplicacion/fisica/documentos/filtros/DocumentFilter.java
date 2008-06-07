package aplicacion.fisica.documentos.filtros;

import aplicacion.fisica.documentos.Documento;

/**
 * Interfaz que deben implementar los filtros asociados a Documento
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz.
 */
public interface DocumentFilter
{
	/**
	 * Obtiene un documento interpretado mediante el filtro
	 * 
	 * @param path
	 *            Path del documento a abrir
	 * @param usuario
	 *            Nombre del usuario que quiere abrir el documento
	 * @param rol
	 *            Rol que desempe–a el usuario
	 * @return Objeto de la clase
	 * @see Documento. Devuelve null si ha ocurrido algun error
	 */
	public Documento getDocumento(String path, String usuario, String rol);

	/**
	 * Comprueba si el filtro soporta un determinado tipo de documento
	 * 
	 * @param extension
	 *            Extension del tipo de documento a consultar
	 * @return True si soporta ese tipo y False en caso contrario
	 */
	public boolean isSupported(String extension);
}
