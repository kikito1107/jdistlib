package util;

import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class DComboBoxSelectionManager extends MenuSelectionManager
{
	private static final DComboBoxSelectionManager dinstance = new DComboBoxSelectionManager();

	@SuppressWarnings( "unused" )
	private static final boolean TRACE = true; // trace creates and disposes

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

	/*
	 * private Vector pathToVector(MenuElement[] path) { Vector v = new
	 * Vector(); int i, j, n; v.add(new Integer(0)); for (i = 1; i <
	 * path.length; i++) { if (path[i] instanceof JPopupMenu) { v.add(new
	 * Integer(-5)); } else { MenuElement[] me = path[i - 1].getSubElements(); n =
	 * -1; for (j = 0; j < me.length; j++) { if (me[j].equals(path[i])) { n = j; } }
	 * v.add(new Integer(n)); } } return v; }
	 */

	/*
	 * private MenuElement[] vectorToPath(Vector v) { MenuElement[] me = new
	 * MenuElement[v.size()]; MenuElement elementoActual = null; int i, j; if
	 * (me.length > 0) { me[0] = barraMenu; elementoActual = barraMenu; for (i =
	 * 1; i < v.size(); i++) { if ( ( (Integer) v.elementAt(i)).intValue() ==
	 * -5) { // es un popup me[i] = ( (JMenu) elementoActual).getPopupMenu();
	 * elementoActual = me[i]; } else { int indice = ( (Integer)
	 * v.elementAt(i)).intValue(); me[i] = (MenuElement) ( (Container)
	 * elementoActual).getComponent( indice); elementoActual = me[i]; } } }
	 * return me; }
	 */

	/*
	 * public void prueba(){ MenuElement[] me = dinstance.getSelectedPath();
	 * dinstance.clearSelectedPath(); Vector v = pathToVector(me); me =
	 * vectorToPath(v); dinstance.setSelectedPath(me); }
	 */

}
