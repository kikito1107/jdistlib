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

import aplicacion.fisica.ServidorFicheros;
import aplicacion.fisica.documentos.filtros.DocumentFilter;
import aplicacion.fisica.documentos.filtros.ImageFilter;
import aplicacion.fisica.documentos.filtros.MSGFilter;
import aplicacion.fisica.documentos.filtros.PDFFilter;
import aplicacion.fisica.documentos.filtros.TXTFilter;

/**
 * Clase que representa un documento, sus paginas y sus anotaciones.
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class Documento implements Serializable, Printable
{
	private static final long serialVersionUID = 1L;

	private Vector<Pagina> paginas = new Vector<Pagina>();

	private String usuario = "";

	private String rol = "";

	private String path = "";

	private MIDocumento metainformacion = null;

	private static Vector<DocumentFilter> filtros = new Vector<DocumentFilter>();

	/**
	 * De forma estatica se agregan los filtros predeterminados para los
	 * distintos formatos de fichero.
	 */
	static
	{
		Documento.addFilter(new MSGFilter());
		Documento.addFilter(new ImageFilter());
		Documento.addFilter(new PDFFilter());
		Documento.addFilter(new TXTFilter());
	}

	/**
	 * Constructor por defecto
	 */
	public Documento()
	{

	}

	/**
	 * Constructor con parametros
	 * 
	 * @param usu
	 *            Usuario que crea el documento
	 * @param ro
	 *            Rol que desempeña actualmente el usuario que crea el documento
	 */
	public Documento( String usu, String ro )
	{
		usuario = usu;
		rol = ro;
	}

	/**
	 * Accede a la metainformacion del documento: path, propietario, ...
	 * 
	 * @return Objeto de la clase
	 * @see MIDocumento con la metainformacion del documento
	 */
	public MIDocumento getMetainformacion()
	{
		return metainformacion;
	}

	/**
	 * Actualiza la metainformacion del documento
	 * 
	 * @param datosBD
	 *            Objeto de la clase
	 * @see MIDocumento con la nueva metainformacion del documento
	 */
	public void setMetainformacion(MIDocumento datosBD)
	{
		this.metainformacion = datosBD;
	}

	/**
	 * Agrega una nueva pagina al documento
	 * 
	 * @param index
	 *            Posicion de la nueva pagina dentro del documento
	 * @param o
	 *            Objeto de la clase
	 * @see Pagina con la nueva pagina a insertar
	 */
	public void insertarPagina(int index, Pagina o)
	{
		paginas.insertElementAt(o, index);
	}

	/**
	 * Inserta una imagen en el documento como una nueva pagina
	 * 
	 * @param index
	 *            Posicion de la nueva pagina dentro del documento
	 * @param o
	 *            Imagen a insertar como nueva pagina
	 */
	public void insertarPagina(int index, Image o)
	{
		Pagina nueva = new Pagina();
		nueva.setImagen(o);

		paginas.insertElementAt(nueva, index);
	}

	/**
	 * Agrega una nueva pagina al final del documento
	 * 
	 * @param pag
	 *            Objeto de la clase
	 * @see Pagina a agregar
	 */
	public void addPagina(Pagina pag)
	{
		paginas.add(pag);
	}

	/**
	 * Agrega una imagen como nueva pagina al final del documento
	 * 
	 * @param img
	 *            Imagen a agregar como nueva pagina
	 */
	public void addPagina(Image img)
	{
		Pagina nueva = new Pagina();
		nueva.setImagen(new ImageIcon(img));

		paginas.add(nueva);
	}

	/**
	 * Agrega una imagen como nueva pagina al final del documento
	 * 
	 * @param img
	 *            Imagen a agregar como nueva pagina
	 */
	public void addPagina(ImageIcon img)
	{
		Pagina nueva = new Pagina();
		nueva.setImagen(img);

		paginas.add(nueva);
	}

	/**
	 * Elimina una pagina del documento
	 * 
	 * @param index
	 *            Numero de pagina a eliminar
	 */
	public void delPagina(int index)
	{
		paginas.remove(index);
	}

	/**
	 * Establece el usuario propietario del documento
	 * 
	 * @param us
	 *            Nombre del usuario propietario del documento
	 */
	public void setUsuario(String us)
	{
		usuario = us;
	}

	/**
	 * Establece el rol que desempeña el usuario propietario del documento
	 * 
	 * @param ro
	 *            Nombre del rol que desempeña el usuario propietario del
	 *            documento
	 */
	public void setRol(String ro)
	{
		rol = ro;
	}

	/**
	 * Consulta el numero de paginas del documento
	 * 
	 * @return Numero de paginas del documento
	 */
	public int getNumeroPaginas()
	{
		return paginas.size();
	}

	/**
	 * Accede a una pagina del documento
	 * 
	 * @param index
	 *            posicion de la pagina
	 * @return Objeto de la clase
	 * @see Pagina con la pagina solicitada. Si la posicion de la pagina no es
	 *      valida devuelve null.
	 */
	public Pagina getPagina(int index)
	{
		if (index < paginas.size())
			return paginas.get(index);
		else return null;
	}

	/**
	 * Obtiene el usuario propietario del documento
	 * 
	 * @return Nombre del usuario propietario del documento
	 */
	public String getUsuario()
	{
		return usuario;
	}

	/**
	 * Obtiene el rol que desempeña el propietario del documento
	 * 
	 * @return Nombre del rol que desempeña el propietario del documento
	 */
	public String getRol()
	{
		return rol;
	}

	/**
	 * Agrega un filtro para interpretar nuevos tipos de ficheros
	 * 
	 * @param flt
	 *            Filtro nuevo a agregar
	 */
	public static void addFilter(DocumentFilter flt)
	{
		filtros.add(flt);
	}

	/**
	 * Comprueba si el tipo de un documento es soportado
	 * 
	 * @param path
	 *            Path del fichero a comprobar
	 * @return True si el tipo es soportado y False en caso contrario
	 */
	public static boolean isSupported(String path)
	{

		int ind_ext = path.lastIndexOf(".");

		String sub = path.substring(ind_ext + 1);

		sub = sub.toLowerCase();

		// comprobar si es fichero soportado
		for (int i = 0; i < filtros.size(); i++)
			if (filtros.get(i).isSupported(sub)) return true;

		return false;
	}

	/**
	 * Abre un fichero y lo devuelve en forma de objeto de esta clase
	 * 
	 * @param path
	 *            Path del documento a abrir
	 * @param usuario
	 *            Usuario que desea abrir el documento
	 * @param rol
	 *            Rol que desempeña actualmente el usuario que desea abrir el
	 *            documento
	 * @return Objeto de esta clase con los contenidos del documento a abrir.
	 *         Devuelve null si ha ocurrido algun error.
	 */
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
			File f = new File(ServidorFicheros.getDirectorioBase() + path
					+ ".anot");
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

	/**
	 * Guarda el documento
	 * 
	 * @param doc
	 *            Documento a guardar
	 * @param path_original
	 *            Path en el que guardar el documento
	 * @return True si el documento ha sido guardado con exito y False en caso
	 *         contrario
	 */
	public static boolean saveDocument(Documento doc, String path_original)
	{
		File f = new File(ServidorFicheros.getDirectorioBase() + path_original
				+ ".anot");

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
	 * Path del documento
	 * 
	 * @return El path del documento
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * Establece el path del documento
	 * 
	 * @param path
	 *            El path nuevo que tendra el documento actual
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * Imprime el documento actual.
	 */
	public void imprimir()
	{
		Impresora p = new Impresora();
		p.inicializar(this);
	}

	/**
	 * Clase que permite imprimir un documento soportado.
	 */
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
		 *            Nombre del documento a imprimir
		 * @return True si la inicializacion ha sido exitosa. False en caso
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

	/**
	 * Metodo creado para dar soporte a la interfaz Printable.
	 * 
	 * @param graphics
	 *            Graficos a imprimir
	 * @param pageFormat
	 *            Formato de cada pagina que se desea imprimir
	 * @param pageIndex
	 *            Pagina que se desea imprimir
	 * @return De la interfaz Printable, la constante PAGE_EXISTS si fue todo
	 *         correcto o NO_SUCH_PAGE si la pagina a imprimir no existe.
	 */
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

			System.out.println("Solicitando pagina " + pageIndex);

			return PAGE_EXISTS;
		}
		else return NO_SUCH_PAGE;
	}

}
