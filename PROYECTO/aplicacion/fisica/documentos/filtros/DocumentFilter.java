package aplicacion.fisica.documentos.filtros;

import aplicacion.fisica.documentos.Documento;

/**
 * Interfaz que deben implementar los filtros asociados a Documento
 * @author carlos
 */
public interface DocumentFilter
{
	/**
	 * Abre un documento
	 * @param path path del documento a abrir
	 * @param usuario nombre del usuario que quiere abrir el documento
	 * @param rol rol del usuario
	 * @return el documento. null si ha ocurrido algun error
	 */
	public Documento getDocumento(String path, String usuario, String rol);
	
	/**
	 * Comprueba si el filtro soporta un determinado tipo de documento
	 * @param extension extension del tipo de documento a consultar
	 * @return true si soporta ese tipo y false en caso contrario
	 */
	public boolean isSupported(String extension);
}
