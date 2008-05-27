/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacion.fisica.webcam;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * 
 * @author carlosrodriguezdominguez
 */
public class ImageComponent extends JComponent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6869102214250977942L;

	private Image image;

	private Dimension size;

	private JFrame parent;

	public ImageComponent( JFrame padre )
	{
		parent = padre;
	}

	public void setImage(Image image)
	{
		SwingUtilities.invokeLater(new ImageRunnable(image));

	}

	public void setImageSize(Dimension newSize)
	{
		if (!newSize.equals(size))
		{ // System.out.println("New size " + newSize + " from " + size);
			size = newSize;
			setSize(size);
			parent.pack();
		}
	}

	private class ImageRunnable implements Runnable
	{

		private final Image newImage;

		public ImageRunnable( Image newImage )
		{
			super();
			this.newImage = newImage;
		}

		public void run()
		{
			setImageInSwingThread(newImage);
		}
	}

	private synchronized void setImageInSwingThread(Image image)
	{
		this.image = image;
		final Dimension newSize = new Dimension(image.getWidth(null), image
				.getHeight(null));
		setImageSize(newSize);
		repaint();
	}

	public ImageComponent()
	{
		size = new Dimension(0, 0);
		setSize(size);
	}

	@Override
	public synchronized void paint(Graphics g)
	{
		if (image != null) // g.drawImage(image, 0, 0, this);
			g.drawImage(image, image.getWidth(this), 0, 0, image
					.getHeight(this), 0, 0, image.getWidth(this), image
					.getHeight(this), this);
	}

	@Override
	public synchronized Dimension getPreferredSize()
	{
		return size;
	}
	
	public synchronized Image getImage(){
		return image;
	}
}
