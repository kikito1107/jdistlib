package aplicacion.fisica.documentos.filtros;

import aplicacion.fisica.documentos.Documento;

public interface DocumentFilter
{
	public Documento getDocumento(String path, String usuario, String rol);
	public boolean isSupported(String extension);
}
