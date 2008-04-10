/*
 * @(#)MetalPopupMenuSeparatorUI.java	1.7 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;

/**
 * A Metal L&F implementation of PopupMenuSeparatorUI.  This implementation
 * is a "combined" view/controller.
 *
 * @version 1.7 01/23/03
 * @author Jeff Shapiro
 */

public class MetalPopupMenuSeparatorUI
	 extends MetalSeparatorUI {
  public static ComponentUI createUI(JComponent c) {
	 return new MetalPopupMenuSeparatorUI();
  }

  public void paint(Graphics g, JComponent c) {
	 Dimension s = c.getSize();

	 g.setColor(c.getForeground());
	 g.drawLine(0, 1, s.width, 1);

	 g.setColor(c.getBackground());
	 g.drawLine(0, 2, s.width, 2);
	 g.drawLine(0, 0, 0, 0);
	 g.drawLine(0, 3, 0, 3);
  }

  public Dimension getPreferredSize(JComponent c) {
	 return new Dimension(0, 4);
  }
}