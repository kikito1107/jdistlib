/*
 * @(#)MetalButtonUI.java	1.29 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/**
 * MetalButtonUI implementation
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 * 
 * @version 1.29 01/23/03
 * @author Tom Santos
 */
@SuppressWarnings( "unchecked" )
public class DMetalButtonUI extends BasicButtonUI
{

	private final static DMetalButtonUI metalButtonUI = new DMetalButtonUI();

	// NOTE: These are not really needed, but at this point we can't pull
	// them. Their values are updated purely for historical reasons.
	protected Color focusColor;

	protected Color selectColor;

	protected Color disabledTextColor;

	// ********************************
	// Create PLAF
	// ********************************
	public static ComponentUI createUI(JComponent c)
	{
		return metalButtonUI;
	}

	// ********************************
	// Install
	// ********************************
	@Override
	public void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
	}

	@Override
	public void uninstallDefaults(AbstractButton b)
	{
		super.uninstallDefaults(b);
	}

	// ********************************
	// Create Listeners
	// ********************************
	protected DMetalButtonListener createDButtonListener(AbstractButton b)
	{
		return new DMetalButtonListener(b);
	}

	// *************************************************************
	@Override
	protected void installListeners(AbstractButton b)
	{
		DMetalButtonListener listener = createDButtonListener(b);
		if (listener != null)
		{
			// put the listener in the button's client properties so that
			// we can get at it later
			b.putClientProperty(this, listener);

			b.addMouseListener(listener);
		}
	}

	@Override
	protected void installKeyboardActions(AbstractButton b)
	{
	}

	// *************************************************************

	// ********************************
	// Default Accessors
	// ********************************
	protected Color getSelectColor()
	{
		selectColor = UIManager.getColor(getPropertyPrefix() + "select");
		return selectColor;
	}

	protected Color getDisabledTextColor()
	{
		disabledTextColor = UIManager.getColor(getPropertyPrefix()
				+ "disabledText");
		return disabledTextColor;
	}

	protected Color getFocusColor()
	{
		focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
		return focusColor;
	}

	// ********************************
	// Paint
	// ********************************
	@Override
	protected void paintButtonPressed(Graphics g, AbstractButton b)
	{
		if (b.isContentAreaFilled())
		{
			Dimension size = b.getSize();
			g.setColor(getSelectColor());
			g.fillRect(0, 0, size.width, size.height);
		}
	}

	@Override
	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect,
			Rectangle textRect, Rectangle iconRect)
	{

		Rectangle focusRect = new Rectangle();
		String text = b.getText();
		boolean isIcon = b.getIcon() != null;

		// If there is text
		if (( text != null ) && !text.equals(""))
		{
			if (!isIcon)
				focusRect.setBounds(textRect);
			else focusRect.setBounds(iconRect.union(textRect));
		}
		// If there is an icon and no text
		else if (isIcon) focusRect.setBounds(iconRect);

		g.setColor(getFocusColor());
		g.drawRect(( focusRect.x - 1 ), ( focusRect.y - 1 ),
				focusRect.width + 1, focusRect.height + 1);

	}

	@Override
	protected void paintText(Graphics g, JComponent c, Rectangle textRect,
			String text)
	{
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		FontMetrics fm = g.getFontMetrics();
		int mnemIndex = b.getDisplayedMnemonicIndex();

		/* Draw the Text */
		if (model.isEnabled())
		{
			/** * paint the text normally */
			g.setColor(b.getForeground());
			BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex,
					textRect.x, textRect.y + fm.getAscent());
		}
		else
		{
			/** * paint the text disabled ** */
			g.setColor(getDisabledTextColor());
			BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex,
					textRect.x, textRect.y + fm.getAscent());

		}
	}

}
