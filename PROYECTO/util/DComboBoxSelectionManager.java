package util;

import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

/**
 * Implementa un manejador de seleccion para los combobox distribuidos
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DComboBoxSelectionManager extends MenuSelectionManager
{
	private static final DComboBoxSelectionManager dinstance = new DComboBoxSelectionManager();

	@SuppressWarnings( "unused" )
	private static final boolean TRACE = true;

	/**
	 * Obtiene el manejador por defecto
	 * 
	 * @return Manejador por defecto
	 */
	public static MenuSelectionManager defaultManager()
	{
		return dinstance;
	}

	@Override
	public void setSelectedPath(MenuElement[] path)
	{
		super.setSelectedPath(path);
	}

	@Override
	public MenuElement[] getSelectedPath()
	{
		return super.getSelectedPath();
	}
}
