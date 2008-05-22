/*
 * @(#)MetalProgressBarUI.java	1.25 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package lookandfeel.Dmetal;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * The Metal implementation of ProgressBarUI.
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
 * @version 1.25 01/23/03
 * @author Michael C. Albers
 */
public class MetalProgressBarUI
	 extends BasicProgressBarUI {

  private Rectangle innards;
  private Rectangle box;

  public static ComponentUI createUI(JComponent c) {
	 return new MetalProgressBarUI();
  }

  /**
	* Draws a bit of special highlighting on the progress bar.
	* The core painting is deferred to the BasicProgressBar's
	* <code>paintDeterminate</code> method.
	*/
  public void paintDeterminate(Graphics g, JComponent c) {
	 super.paintDeterminate(g, c);

	 if (! (g instanceof Graphics2D)) {
		return;
	 }

	 if (progressBar.isBorderPainted()) {
		Insets b = progressBar.getInsets(); // area for border
		int barRectWidth = progressBar.getWidth() - (b.left + b.right);
		int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
		int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
		boolean isLeftToRight = MetalUtils.isLeftToRight(c);
		int startX, startY, endX, endY;

		// The progress bar border is painted according to a light source.
		// This light source is stationary and does not change when the
		// component orientation changes.
		startX = b.left;
		startY = b.top;
		endX = b.left + barRectWidth - 1;
		endY = b.top + barRectHeight - 1;

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1.f));

		if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
		  // Draw light line lengthwise across the progress bar.
		  g2.setColor(MetalLookAndFeel.getControlShadow());
		  g2.drawLine(startX, startY, endX, startY);

		  if (amountFull > 0) {
			 // Draw darker lengthwise line over filled area.
			 g2.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());

			 if (isLeftToRight) {
				g2.drawLine(startX, startY,
								startX + amountFull - 1, startY);
			 }
			 else {
				g2.drawLine(endX, startY,
								endX - amountFull + 1, startY);
				if (progressBar.getPercentComplete() != 1.f) {
				  g2.setColor(MetalLookAndFeel.getControlShadow());
				}
			 }
		  }
		  // Draw a line across the width.  The color is determined by
		  // the code above.
		  g2.drawLine(startX, startY, startX, endY);

		}
		else { // VERTICAL
		  // Draw light line lengthwise across the progress bar.
		  g2.setColor(MetalLookAndFeel.getControlShadow());
		  g2.drawLine(startX, startY, startX, endY);

		  if (amountFull > 0) {
			 // Draw darker lengthwise line over filled area.
			 g2.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
			 g2.drawLine(startX, endY,
							 startX, endY - amountFull + 1);
		  }
		  // Draw a line across the width.  The color is determined by
		  // the code above.
		  g2.setColor(MetalLookAndFeel.getControlShadow());

		  if (progressBar.getPercentComplete() == 1.f) {
			 g2.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
		  }
		  g2.drawLine(startX, startY, endX, startY);
		}
	 }
  }

  /**
	* Draws a bit of special highlighting on the progress bar
	* and bouncing box.
	* The core painting is deferred to the BasicProgressBar's
	* <code>paintIndeterminate</code> method.
	*/
  public void paintIndeterminate(Graphics g, JComponent c) {
	 super.paintIndeterminate(g, c);

	 if (!progressBar.isBorderPainted() || (! (g instanceof Graphics2D))) {
		return;
	 }

	 Insets b = progressBar.getInsets(); // area for border
	 int barRectWidth = progressBar.getWidth() - (b.left + b.right);
	 int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
	 int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
	 boolean isLeftToRight = MetalUtils.isLeftToRight(c);
	 int startX, startY, endX, endY;
	 Rectangle box = null;
	 box = getBox(box);

	 // The progress bar border is painted according to a light source.
	 // This light source is stationary and does not change when the
	 // component orientation changes.
	 startX = b.left;
	 startY = b.top;
	 endX = b.left + barRectWidth - 1;
	 endY = b.top + barRectHeight - 1;

	 Graphics2D g2 = (Graphics2D) g;
	 g2.setStroke(new BasicStroke(1.f));

	 if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
		// Draw light line lengthwise across the progress bar.
		g2.setColor(MetalLookAndFeel.getControlShadow());
		g2.drawLine(startX, startY, endX, startY);
		g2.drawLine(startX, startY, startX, endY);

		// Draw darker lengthwise line over filled area.
		g2.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
		g2.drawLine(box.x, startY, box.x + box.width - 1, startY);

	 }
	 else { // VERTICAL
		// Draw light line lengthwise across the progress bar.
		g2.setColor(MetalLookAndFeel.getControlShadow());
		g2.drawLine(startX, startY, startX, endY);
		g2.drawLine(startX, startY, endX, startY);

		// Draw darker lengthwise line over filled area.
		g2.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
		g2.drawLine(startX, box.y, startX, box.y + box.height - 1);
	 }
  }
}
