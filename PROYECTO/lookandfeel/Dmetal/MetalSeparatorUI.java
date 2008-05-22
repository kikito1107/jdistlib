/*
 * @(#)MetalSeparatorUI.java	1.15 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/**
 * A Metal L&F implementation of SeparatorUI.  This implementation
 * is a "combined" view/controller.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @version 1.15 01/23/03
 * @author Jeff Shapiro
 */

public class MetalSeparatorUI
	 extends BasicSeparatorUI {
  public static ComponentUI createUI(JComponent c) {
	 return new MetalSeparatorUI();
  }

  protected void installDefaults(JSeparator s) {
	 LookAndFeel.installColors(s, "Separator.background", "Separator.foreground");
  }

  public void paint(Graphics g, JComponent c) {
	 Dimension s = c.getSize();

	 if ( ( (JSeparator) c).getOrientation() == JSeparator.VERTICAL) {
		g.setColor(c.getForeground());
		g.drawLine(0, 0, 0, s.height);

		g.setColor(c.getBackground());
		g.drawLine(1, 0, 1, s.height);
	 }
	 else { // HORIZONTAL
		g.setColor(c.getForeground());
		g.drawLine(0, 0, s.width, 0);

		g.setColor(c.getBackground());
		g.drawLine(0, 1, s.width, 1);
	 }
  }

  public Dimension getPreferredSize(JComponent c) {
	 if ( ( (JSeparator) c).getOrientation() == JSeparator.VERTICAL) {
		return new Dimension(2, 0);
	 }
	 else {
		return new Dimension(0, 2);
	 }
  }
}
