package util;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class ElementoLista implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7133828227559355237L;

	transient public ImageIcon imagen = null;

	transient public String texto = null;

	public ElementoLista( ImageIcon imagen, String texto )
	{
		this.imagen = imagen;
		this.texto = texto;
	}

	public ElementoLista( ElementoLista elemento )
	{
		if (elemento.imagen != null)
			this.imagen = new ImageIcon(elemento.imagen.getImage());
		if (elemento.texto != null) this.texto = new String(elemento.texto);
	}

	@Override
	public String toString()
	{
		return texto;
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean iguales = false;
		if (obj instanceof String)
			iguales = texto.equals(obj);
		else if (obj instanceof ElementoLista)
			iguales = texto.equals(( (ElementoLista) obj ).texto);
		return iguales;
	}

	private void writeObject(ObjectOutputStream stream)
			throws java.io.IOException
	{
		stream.defaultWriteObject(); // write non-transient, non-static data
		PixelGrabber grabber = null;
		Object pix = null;
		Dimension dim = null;

		if (imagen != null)
		{
			grabber = new PixelGrabber(imagen.getImage(), 0, 0, -1, -1, true);
			try
			{
				grabber.grabPixels();
			}
			catch (InterruptedException e)
			{
			}

			pix = grabber.getPixels();
		}

		if (pix != null)
		{
			stream.writeObject(pix);
			dim = new Dimension(imagen.getIconWidth(), imagen.getIconHeight());
		}
		else
		{
			stream.writeObject(new String(""));
			dim = new Dimension(0, 0);
		}

		stream.writeObject(new String(texto));
		stream.writeObject(dim);

	}

	private void readObject(ObjectInputStream stream)
			throws java.io.IOException
	{
		try
		{
			stream.defaultReadObject(); // read non-transient, non-static data

			Object rimagen = stream.readObject();
			texto = (String) stream.readObject();
			Dimension dim = (Dimension) stream.readObject();

			if (!( rimagen instanceof String ))
			{ // Habia imagen
				int[] pix = (int[]) rimagen;
				Canvas canvas = new Canvas();
				Image auximg = canvas.createImage(new MemoryImageSource(
						dim.width, dim.height, pix, 0, dim.width));
				imagen = new ImageIcon(auximg);
			}

		}
		catch (ClassNotFoundException e)
		{
			throw new java.io.IOException();
		}
	}

}
