package componentes.base;

import javax.swing.JPopupMenu;

/**
 * Implementacion de la clase captadora de eventos para el componente Popup Menu
 * 
 * @author Juan Antonio Iba–ez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJMenuPopup extends JPopupMenu
{
	private static final long serialVersionUID = 1L;

	private static final String uiClassID = "DMenuMetalPopupMenuUI";

	/**
	 * Constructor por defecto
	 */
	public DJMenuPopup()
	{
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param p0
	 *            Etiqueta que mostrara el popup
	 */
	public DJMenuPopup( String p0 )
	{
		super(p0);
	}

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

}
