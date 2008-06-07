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
 * Componente que muestra las imagenes capturadas por la webcam
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class ImageComponent extends JComponent
{
	private static final long serialVersionUID = -6869102214250977942L;

	private Image image;

	private Dimension size;

	private JFrame parent;

	/**
	 * Constructor por defecto
	 */
	public ImageComponent()
	{
		size = new Dimension(0, 0);
		setSize(size);
	}

	/**
	 * Constructor con parametros
	 * 
	 * @param padre
	 *            Ventana donde esta situada este componente
	 */
	public ImageComponent( JFrame padre )
	{
		parent = padre;
	}

	/**
	 * Asigna una imagen a este componente para que sea mostrada
	 * 
	 * @param image
	 *            Imagen a asignar al componente
	 */
	public void setImage(Image image)
	{
		SwingUtilities.invokeLater(new ImageRunnable(image));

	}

	/**
	 * Pone el tamaño de la imagen mostrada
	 * 
	 * @param newSize
	 *            Nuevo tamaño para la imagen mostrada
	 */
	public void setImageSize(Dimension newSize)
	{
		if (!newSize.equals(size))
		{ // System.out.println("New size " + newSize + " from " + size);
			size = newSize;
			setSize(size);
			parent.pack();
		}
	}

	/**
	 * Hebra encargada de actualizar la imagen mostrada en el componente. Si
	 * actualizamos la imagen a mostrar, entonces este componente se encargara
	 * de actualizar su visualizacion.
	 */
	private class ImageRunnable implements Runnable
	{
		private final Image newImage;

		/**
		 * Constructor
		 * 
		 * @param newImage
		 *            Imagen a poner en el componente
		 */
		public ImageRunnable( Image newImage )
		{
			super();
			this.newImage = newImage;
		}

		/**
		 * Ejecucion de la hebra. Tan solo pone la imagen en el componente
		 */
		public void run()
		{
			setImageInSwingThread(newImage);
		}
	}

	/**
	 * Repinta la imagen con el tamaño requerido en el componente. Es usado por
	 * la clase privada
	 * 
	 * @see ImageRunnable.
	 * @param image
	 *            Imagen a mostrar en el componente
	 */
	private synchronized void setImageInSwingThread(Image image)
	{
		this.image = image;
		final Dimension newSize = new Dimension(image.getWidth(null), image
				.getHeight(null));
		setImageSize(newSize);
		repaint();
	}

	/**
	 * Reimplementacion del metodo paint para que muestre la imagen que queremos
	 * pintar en el componente.
	 */
	@Override
	public synchronized void paint(Graphics g)
	{
		if (image != null) // g.drawImage(image, 0, 0, this);
			g.drawImage(image, image.getWidth(this), 0, 0, image
					.getHeight(this), 0, 0, image.getWidth(this), image
					.getHeight(this), this);
	}

	/**
	 * Devuelve el tamaño preferido para el componente
	 */
	@Override
	public synchronized Dimension getPreferredSize()
	{
		return size;
	}

	/**
	 * Obtiene la imagen mostrada en el componente. El metodo esta sincronizado
	 * por si se accede desde multiples hebras a la imagen.
	 * 
	 * @return
	 */
	public synchronized Image getImage()
	{
		return image;
	}
}
