package componentes.base;

import javax.swing.JPopupMenu;

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

public class DJMenuPopup extends JPopupMenu
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String uiClassID = "DMenuMetalPopupMenuUI";

	public DJMenuPopup()
	{
	}

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
