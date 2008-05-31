package aplicacion.fisica.documentos;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.JobAttributes;
import java.awt.RenderingHints;
import java.awt.JobAttributes.DialogType;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;

import aplicacion.fisica.documentos.filtros.DocumentFilter;

public class Documento implements Serializable, Printable
{
	private static final long serialVersionUID = 1L;

	private Vector<Pagina> paginas = new Vector<Pagina>();

	private String usuario = "";

	private String rol = "";

	private String path = "";

	private MIFichero datosBD = null;

	private static Vector<DocumentFilter> filtros = new Vector<DocumentFilter>();

	public Documento()
	{

	}

	public MIFichero getDatosBD()
	{
		return datosBD;
	}

	public void setDatosBD(MIFichero datosBD)
	{
		this.datosBD = datosBD;
	}

	public void insertarPagina(int index, Pagina o)
	{
		paginas.insertElementAt(o, index);
	}

	public void insertarPagina(int index, Image o)
	{
		Pagina nueva = new Pagina();
		nueva.setImagen(o);

		paginas.insertElementAt(nueva, index);
	}

	public Documento( String usu, String ro )
	{
		usuario = usu;
		rol = ro;
	}

	public void addPagina(Pagina pag)
	{
		paginas.add(pag);
	}

	public void addPagina(Image img)
	{
		Pagina nueva = new Pagina();
		nueva.setImagen(new ImageIcon(img));

		paginas.add(nueva);
	}

	public void addPagina(ImageIcon img)
	{
		Pagina nueva = new Pagina();
		nueva.setImagen(img);

		paginas.add(nueva);
	}

	public void delPagina(int index)
	{
		paginas.remove(index);
	}

	public void setPagina(int index, Pagina img)
	{
		paginas.set(index, img);
	}

	public void addAnotacion(Anotacion anot, int numPag)
	{
		paginas.get(numPag).addAnotacion(anot);
	}

	public void delAnotacion(int numAnotacion, int numPag)
	{
		paginas.get(numPag).delAnotacion(numAnotacion);
	}

	public void setAnotacion(int numPagina, int index, Anotacion anot)
	{
		paginas.get(numPagina).setAnotacion(index, anot);
	}

	public void setUsuario(String us)
	{
		usuario = us;
	}

	public void setRol(String ro)
	{
		rol = ro;
	}

	public int getNumeroPaginas()
	{
		return paginas.size();
	}

	public Pagina getPagina(int index)
	{
		if (index < paginas.size())
			return paginas.get(index);
		else return null;
	}

	public String getUsuario()
	{
		return usuario;
	}

	public String getRol()
	{
		return rol;
	}

	public static void addFilter(DocumentFilter flt)
	{
		filtros.add(flt);
	}

	@SuppressWarnings( "unchecked" )
	public static Documento openDocument(String path, String usuario, String rol)
	{
		int ind_ext = path.lastIndexOf(".");

		String sub = path.substring(ind_ext + 1);

		sub = sub.toLowerCase();

		Documento doc = null;

		// comprobar si es fichero soportado
		for (int i = 0; i < filtros.size(); i++)
			if (filtros.get(i).isSupported(sub))
				doc = filtros.get(i).getDocumento(path, usuario, rol);

		// comprobar si el fichero .anot existe
		if (doc != null)
		{
			File f = new File(path + ".anot");
			if (f.exists())
				try
				{
					FileInputStream fis = new FileInputStream(f);
					ObjectInputStream ois = new ObjectInputStream(fis);

					int npaginas = ois.readInt();
					if (npaginas != doc.getNumeroPaginas()) return doc;

					for (int i = 0; i < npaginas; i++)
					{
						Vector<Anotacion> anot = (Vector<Anotacion>) ois
								.readObject();
						doc.getPagina(i).setAnotaciones(anot);
					}

					ois.close();
					fis.close();
				}
				catch (Exception ex)
				{
				} // siempre existe el fichero
		}

		// tipo no soportado
		return doc;
	}

	public static boolean saveDocument(Documento doc, String path_original)
	{
		File f = new File(path_original + ".anot");

		try
		{
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeInt(doc.getNumeroPaginas());

			for (int i = 0; i < doc.getNumeroPaginas(); i++)
				oos.writeObject(doc.getPagina(i).getAnotaciones());

			oos.close();
			fos.close();

			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	public void imprimir()
	{
		Impresora p = new Impresora();
		p.inicializar(this);
	}

	private class Impresora
	{
		int x;

		PrinterJob pjob;

		Graphics pg;

		Font grande, peque;

		/**
		 * Constructor
		 * 
		 */
		public Impresora()
		{
		}

		/**
		 * Inicializa la impresora
		 * 
		 * @param nombredoc
		 *            nombre del documento a imprimir
		 * @return true si la inicializaci—n a sido exitosa false en caso
		 *         contrario
		 */
		public boolean inicializar(Documento d)
		{
			x = 0;
			grande = new Font("Courier New", Font.PLAIN, 16);
			peque = new Font("Courier New", Font.PLAIN, 12);

			Frame f = new Frame("");
			f.pack();

			JobAttributes atributosImpresion = new JobAttributes();
			atributosImpresion.setCopies(1);
			atributosImpresion.setDialog(DialogType.NATIVE);

			pjob = PrinterJob.getPrinterJob();

			pjob.setPrintable(d);

			if (pjob.printDialog())
				try
				{
					pjob.print();
					return true;
				}
				catch (PrinterException e)
				{
					System.out.println(e);
				}
			else return false;
			return false;

		}
	}

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException
	{
		if (pageIndex < paginas.size())
		{
			Image im = paginas.get(pageIndex).getImagen();
			Pagina p = paginas.get(pageIndex);
			Graphics2D g = (Graphics2D) graphics;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(im, 0, 0, im.getWidth(null), im.getHeight(null), null);
			Vector<Anotacion> v = p.getAnotaciones();
			int na = v.size();

			for (int i = 0; i < na; ++i)
			{
				graphics.setColor(v.get(i).getContenido().getColor());
				v.get(i).getContenido().dibujar(graphics);
			}

			System.out.println("Solicitando p‡gina " + pageIndex);

			return PAGE_EXISTS;
		}
		else return NO_SUCH_PAGE;
	}

}
