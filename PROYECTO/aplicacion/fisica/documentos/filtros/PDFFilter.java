package aplicacion.fisica.documentos.filtros;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.ImageIcon;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import aplicacion.fisica.documentos.Documento;

public class PDFFilter implements DocumentFilter
{
	public PDFFilter()
	{
		 
	}
	
	public Documento getDocumento(String path, String usuario, String rol)
	{
		File file = new File(path);
		RandomAccessFile raf;
		Documento res = new Documento(usuario, rol);

		try
		{
			raf = new RandomAccessFile(file, "r");

			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
					channel.size());
			PDFFile pdffile = new PDFFile(buf);

			// pasar cada pagina a una imagen
			int num = pdffile.getNumPages();
			for (int i = 1; i <= num; i++)
			{
				PDFPage page = pdffile.getPage(i);

				// Poner el zoom por defecto a la pagina
				int width = (int) page.getBBox().getWidth();
				int height = (int) page.getBBox().getHeight();

				Rectangle rect = new Rectangle(0, 0, width, height);
				int rotation = page.getRotation();
				Rectangle rect1 = rect;
				if (( rotation == 90 ) || ( rotation == 270 ))
					rect1 = new Rectangle(0, 0, rect.height, rect.width);

				// generar la imagen
				BufferedImage img = (BufferedImage) page.getImage(
						(int) ( ( rect.width * 1.1 ) + 0.5 ),
						(int) ( ( rect.height * 1.1 ) + 0.5 ), rect1, null,
						true, true);

				res.addPagina(new ImageIcon(img));

				System.gc();
			}

			res.setPath(path);

			return res;
		}
		catch (FileNotFoundException e1)
		{
			System.err.println(e1.getLocalizedMessage());
		}
		catch (IOException e)
		{
			System.err.println(e.getLocalizedMessage());
		}

		return null;
	}

	public boolean isSupported(String extension)
	{
		if (extension.toLowerCase().compareTo("pdf") == 0)
			return true;
		else return false;
	}
	
}
