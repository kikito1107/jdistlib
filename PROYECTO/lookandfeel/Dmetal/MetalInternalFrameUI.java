/*
 * @(#)MetalInternalFrameUI.java	1.29 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 * Metal implementation of JInternalFrame.
 * <p>
 * 
 * @version 1.29 01/23/03
 * @author Steve Wilson
 */
public class MetalInternalFrameUI extends BasicInternalFrameUI
{

	private MetalInternalFrameTitlePane titlePane;

	private static final PropertyChangeListener metalPropertyChangeListener = new MetalPropertyChangeHandler();

	private static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);

	protected static String IS_PALETTE = "JInternalFrame.isPalette";

	private static String FRAME_TYPE = "JInternalFrame.frameType";

	@SuppressWarnings("unused")
	private static String NORMAL_FRAME = "normal";

	private static String PALETTE_FRAME = "palette";

	private static String OPTION_DIALOG = "optionDialog";

	public MetalInternalFrameUI( JInternalFrame b )
	{
		super(b);
	}

	public static ComponentUI createUI(JComponent c)
	{
		return new MetalInternalFrameUI((JInternalFrame) c);
	}

	@Override
	public void installUI(JComponent c)
	{
		super.installUI(c);

		Object paletteProp = c.getClientProperty(IS_PALETTE);
		if (paletteProp != null)
			setPalette(( (Boolean) paletteProp ).booleanValue());

		Container content = frame.getContentPane();
		stripContentBorder(content);
		// c.setOpaque(false);
	}

	@Override
	public void uninstallUI(JComponent c)
	{
		frame = (JInternalFrame) c;

		Container cont = ( (JInternalFrame) ( c ) ).getContentPane();
		if (cont instanceof JComponent)
		{
			JComponent content = (JComponent) cont;
			if (content.getBorder() == handyEmptyBorder)
				content.setBorder(null);
		}
		super.uninstallUI(c);
	}

	@Override
	protected void installListeners()
	{
		super.installListeners();
		frame.addPropertyChangeListener(metalPropertyChangeListener);
	}

	@Override
	protected void uninstallListeners()
	{
		frame.removePropertyChangeListener(metalPropertyChangeListener);
		super.uninstallListeners();
	}

	@Override
	protected void installKeyboardActions()
	{
		super.installKeyboardActions();
		ActionMap map = SwingUtilities.getUIActionMap(frame);
		if (map != null) // BasicInternalFrameUI creates an action with the
			// same name, we
			// override
			// it as Metal frames do not have system menus.
			map.remove("showSystemMenu");
	}

	@Override
	protected void uninstallKeyboardActions()
	{
		super.uninstallKeyboardActions();
	}

	@Override
	protected void uninstallComponents()
	{
		titlePane = null;
		super.uninstallComponents();
	}

	private void stripContentBorder(Object c)
	{
		if (c instanceof JComponent)
		{
			JComponent contentComp = (JComponent) c;
			Border contentBorder = contentComp.getBorder();
			if (( contentBorder == null )
					|| ( contentBorder instanceof UIResource ))
				contentComp.setBorder(handyEmptyBorder);
		}
	}

	@Override
	protected JComponent createNorthPane(JInternalFrame w)
	{
		titlePane = new MetalInternalFrameTitlePane(w);
		return titlePane;
	}

	private void setFrameType(String frameType)
	{
		if (frameType.equals(OPTION_DIALOG))
		{
			LookAndFeel
					.installBorder(frame, "InternalFrame.optionDialogBorder");
			titlePane.setPalette(false);
		}
		else if (frameType.equals(PALETTE_FRAME))
		{
			LookAndFeel.installBorder(frame, "InternalFrame.paletteBorder");
			titlePane.setPalette(true);
		}
		else
		{
			LookAndFeel.installBorder(frame, "InternalFrame.border");
			titlePane.setPalette(false);
		}
	}

	// this should be deprecated - jcs
	public void setPalette(boolean isPalette)
	{
		if (isPalette)
			LookAndFeel.installBorder(frame, "InternalFrame.paletteBorder");
		else LookAndFeel.installBorder(frame, "InternalFrame.border");
		titlePane.setPalette(isPalette);

	}

	private static class MetalPropertyChangeHandler implements
			PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent e)
		{
			String name = e.getPropertyName();
			JInternalFrame jif = (JInternalFrame) e.getSource();

			if (!( jif.getUI() instanceof MetalInternalFrameUI )) return;

			MetalInternalFrameUI ui = (MetalInternalFrameUI) jif.getUI();

			if (name.equals(FRAME_TYPE))
			{
				if (e.getNewValue() instanceof String)
					ui.setFrameType((String) e.getNewValue());
			}
			else if (name.equals(IS_PALETTE))
			{
				if (e.getNewValue() != null)
					ui.setPalette(( (Boolean) e.getNewValue() ).booleanValue());
				else ui.setPalette(false);
			}
			else if (name.equals(JInternalFrame.CONTENT_PANE_PROPERTY))
				ui.stripContentBorder(e.getNewValue());
		}
	} // end class MetalPropertyChangeHandler
}
