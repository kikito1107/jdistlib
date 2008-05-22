/*
 * @(#)MetalLabelUI.java	1.10 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 * A Windows L&F implementation of LabelUI.  This implementation
 * is completely static, i.e. there's only one UIView implementation
 * that's shared by all JLabel objects.
 *
 * @version 1.10 01/23/03
 * @author Hans Muller
 */

public class MetalLabelUI
	 extends BasicLabelUI {
  protected static MetalLabelUI metalLabelUI = new MetalLabelUI();

  public static ComponentUI createUI(JComponent c) {
	 return metalLabelUI;
  }

  /**
	* Just paint the text gray (Label.disabledForeground) rather than
	* in the labels foreground color.
	*
	* @see #paint
	* @see #paintEnabledText
	*/
  protected void paintDisabledText(JLabel l, Graphics g, String s, int textX,
											  int textY) {
	 int mnemIndex = l.getDisplayedMnemonicIndex();
	 g.setColor(UIManager.getColor("Label.disabledForeground"));
	 BasicGraphicsUtils.drawStringUnderlineCharAt(g, s, mnemIndex,
																 textX, textY);
  }
}
