/*
 * @(#)MetalTextFieldUI.java	1.14 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package lookandfeel.Dmetal;

import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * Basis of a look and feel for a JTextField.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
 * <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
 * 
 * @author Steve Wilson
 * @version 1.14 01/23/03
 */
public class MetalTextFieldUI extends BasicTextFieldUI
{

	public static ComponentUI createUI(JComponent c)
	{
		return new MetalTextFieldUI();
	}

	/**
	 * This method gets called when a bound property is changed on the
	 * associated JTextComponent. This is a hook which UI implementations may
	 * change to reflect how the UI displays bound properties of JTextComponent
	 * subclasses.
	 * 
	 * @param evt
	 *            the property change event
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		super.propertyChange(evt);
	}

}
