package componentes.gui.docs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import Deventos.enlaceJS.DConector;
import awareness.ClienteMetaInformacion;
import awareness.MIRol;
import awareness.MIUsuario;

import componentes.base.DComponenteBase;
import fisica.ClienteFicheros;
import fisica.documentos.Documento;
import fisica.documentos.MIDocumento;
import fisica.eventos.DFileEvent;
import fisica.net.Transfer;

public class DIArbolDocumentos extends DComponenteBase
{
	private static final long serialVersionUID = 5277749260155721460L;

	public DJArbolDocumentos arbol = null;

	public DIArbolDocumentos( DefaultMutableTreeNode raiz )
	{
		super();
		init(raiz);
	}

	public DIArbolDocumentos( String nombre, boolean conexionDC,
			DComponenteBase padre, DefaultMutableTreeNode raiz )
	{
		super(nombre, conexionDC, padre);
		init(raiz);
	}

	private void init(DefaultMutableTreeNode raiz)
	{

		arbol = new DJArbolDocumentos(raiz);
		this.add(arbol);
	}

	@Override
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	/**
	 * Permite obtener la metainformacion del documento seleccionado en el arbol
	 * 
	 * @return Metainformacion del documento seleccionado
	 */
	public MIDocumento getDocumentoSeleccionado()
	{
		TreePath camino = arbol.getSelectionPath();

		Object[] objetos = null;

		if (camino != null) objetos = camino.getPath();

		if (( objetos != null ) && ( objetos.length > 0 ))
			return (MIDocumento) ( (DefaultMutableTreeNode) objetos[objetos.length - 1] )
					.getUserObject();
		else return null;
	}

	/**
	 * Cambia la MI asociada a un documento, la actualiza en el servidor y envia
	 * el evento al resto de aplicaciones conectadas
	 * 
	 * @param f
	 *            MI nueva
	 */
	public void cambiarMIDocumento(MIDocumento f)
	{
		DFileEvent evento = new DFileEvent();
		evento.fichero = f;

		DefaultMutableTreeNode r = (DefaultMutableTreeNode) this
				.getNodoSeleccionado().getParent();

		evento.padre = (MIDocumento) r.getUserObject();

		if (evento.padre != null) // por si es la raiz
		{
			evento.tipo = new Integer(DFileEvent.NOTIFICAR_MODIFICACION_FICHERO
					.intValue());
			enviarEvento(evento);
			ClienteFicheros.obtenerClienteFicheros().modificarFichero(f,
					DConector.Daplicacion);
		}
	}

	public DefaultTreeModel getModelo()
	{
		return arbol.getModelo();
	}

	/**
	 * Permite obtener el nodo seleccionado en el arbol
	 * 
	 * @return Nodo seleccionado
	 */
	public DefaultMutableTreeNode getNodoSeleccionado()
	{
		TreePath camino = arbol.getSelectionPath();

		Object[] objetos = null;

		if (camino != null) objetos = camino.getPath();

		if (( objetos != null ) && ( objetos.length > 0 ))
			return (DefaultMutableTreeNode) objetos[objetos.length - 1];
		else return null;
	}

	/**
	 * Permite buscar un nodo en el arbol iniciando la busqueda desde otro nodo
	 * 
	 * @param n
	 *            Nodo desde el que iniciar la busqueda
	 * @param id
	 *            Identificador del nodo a buscar
	 * @return Nodo que se deseaba buscar o null si no se encontro u ocurrio
	 *         algun error.
	 */
	public static DefaultMutableTreeNode buscarFichero(
			DefaultMutableTreeNode n, int id)
	{
		if (!n.isRoot() && ( ( (MIDocumento) n.getUserObject() ).getId() == id ))
			return n;
		else if (n.getChildCount() > 0)
		{
			DefaultMutableTreeNode nodo = null;

			for (int i = 0; i < n.getChildCount(); ++i)
			{
				nodo = buscarFichero((DefaultMutableTreeNode) n.getChildAt(i),
						id);

				if (nodo != null) return nodo;
			}
			return nodo;
		}
		else return null;
	}

	/**
	 * Elimina un nodo del arbol
	 * 
	 * @param id
	 *            Identificador del nodo a eliminar
	 */
	public void eliminarNodo(int id)
	{
		DefaultMutableTreeNode n = buscarFichero(arbol.getRaiz(), id);
		arbol.getModelo().removeNodeFromParent(n);
	}

	/**
	 * Permite imprimir un documento
	 */
	public void imprimirFichero()
	{
		MIDocumento doc = getDocumentoSeleccionado();

		if (doc.esDirectorio()) return;

		Transfer t = new Transfer(ClienteFicheros.ipConexion, doc
				.getRutaLocal());

		Documento d = t.receiveDocumento(true);

		d.imprimir();
	}

	/**
	 * Permite guardar un documento de forma local
	 */
	public void guardarDocumentoLocalmente()
	{
		MIDocumento doc = this.getDocumentoSeleccionado();

		JFileChooser jfc = new JFileChooser("Guardar Documento Localmente");

		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		jfc.setSelectedFile(new File(doc.getNombre()));

		int op = jfc.showDialog(null, "Aceptar");

		if (op == JFileChooser.APPROVE_OPTION)
		{
			java.io.File f = jfc.getSelectedFile();

			if (doc.esDirectorio())
			{
				JOptionPane.showMessageDialog(null,
						"No puede guardar un directorio de forma local");
			}

			else
			{

				Transfer t = new Transfer(ClienteFicheros.ipConexion, doc
						.getRutaLocal());

				byte[] datos = t.receiveFileBytes();

				try
				{
					RandomAccessFile acf = new RandomAccessFile(f
							.getAbsolutePath(), "rw");

					acf.write(datos);

					acf.close();
				}
				catch (FileNotFoundException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (IOException e3)
				{
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			}
		}
	}

	/**
	 * Recupera la metainformacion relativa a un mensaje
	 * 
	 * @return Metainformacion del mensaje
	 */
	public MIDocumento recuperarMail()
	{
		// obtenemos el documento seleccionado
		MIDocumento doc = this.getDocumentoSeleccionado();

		// nos aseguramos de que sea un mensaje
		if (doc.esDirectorio()) return null;

		if (!doc.getTipo().equals(MIDocumento.TIPO_MENSAJE)) return null;

		// recuperamos los datos del mensaje
		Transfer t = new Transfer(ClienteFicheros.ipConexion, doc
				.getRutaLocal());

		byte[] datos = t.receiveFileBytes();

		// enviamos los datos
		doc.setMensaje(new String(datos));

		// devolvemos la metainformacion
		return doc;
	}

	/**
	 * Elimina el documento seleccionado (si se tienen los permisos suficientes)
	 * 
	 * @return True si se elimino el documento con exito. False en caso
	 *         contrario.
	 */
	public boolean eliminarFichero()
	{
		MIDocumento f = this.getDocumentoSeleccionado();

		if (f != null)
		{
			if (!f.esDirectorio()
					&& f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
							MIDocumento.PERMISO_ESCRITURA))
			{

				ClienteFicheros.obtenerClienteFicheros().borrarFichero(f,
						DConector.Daplicacion);

				// enviamos el evento de eliminacion
				DFileEvent evento = new DFileEvent();
				evento.fichero = f;
				evento.tipo = new Integer(DFileEvent.NOTIFICAR_ELIMINAR_FICHERO
						.intValue());
				enviarEvento(evento);

				return true;
			}
			else if (f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
					MIDocumento.PERMISO_ESCRITURA))
			{
				DefaultMutableTreeNode nodo = this.getNodoSeleccionado();

				if (nodo.getChildCount() == 0)
				{
					ClienteFicheros.obtenerClienteFicheros().borrarFichero(f,
							DConector.Daplicacion);

					// enviamos el evento de eliminacion
					DFileEvent evento = new DFileEvent();
					evento.fichero = f;
					evento.tipo = new Integer(
							DFileEvent.NOTIFICAR_ELIMINAR_FICHERO.intValue());
					enviarEvento(evento);

					return true;
				}
				else
				{
					JOptionPane
							.showMessageDialog(
									null,
									"No se puede eliminar la carpeta dado que esta tiene documentos y/o otras carpetas");

					return false;
				}

			}
			else return false;
		}
		else return false;
	}

	/**
	 * Agrega un nuevo directorio al directorio seleccionado actualmente
	 * 
	 * @param nombre
	 *            Nombre del directorio a agregar
	 * @return Metainformacion sobre el directorio creado o null si ocurrio
	 *         algun error.
	 */
	public MIDocumento agregarCarpeta(String nombre)
	{
		MIDocumento f = this.getDocumentoSeleccionado();

		if (f.esDirectorio())
		{

			// creamos el nodo
			MIDocumento nuevo = new MIDocumento();

			if (!f.getRutaLocal().equals("/"))
				nuevo.setRutaLocal(f.getRutaLocal() + "/" + nombre);
			else nuevo.setRutaLocal("/" + nombre);

			if (existeFichero((DefaultMutableTreeNode) arbol.getModelo()
					.getRoot(), nuevo.getRutaLocal()))
			{
				JOptionPane.showMessageDialog(null, "La carpeta ya existe");
				return null;
			}

			// recuperamos el usuario y el rol
			MIUsuario user = ClienteMetaInformacion.cmi
					.getUsuarioConectado(DConector.Dusuario);
			MIRol rol = ClienteMetaInformacion.cmi.getRol(DConector.Drol);

			if (user == null || rol == null) return null;

			if (!f.comprobarPermisos(user.getNombreUsuario(), rol
					.getNombreRol(), MIDocumento.PERMISO_ESCRITURA))
			{
				JOptionPane.showMessageDialog(null,
						"No tiene permisos suficientes para crear la carpeta");
				return null;
			}

			nuevo.setNombre(nombre);
			nuevo.setPadre(f.getId());
			nuevo.setRol(rol);
			nuevo.setUsuario(user);
			nuevo.setPermisos("rwrw--");
			nuevo.setTipo("NULL");

			nuevo.esDirectorio(true);

			MIDocumento f2 = ClienteFicheros.cf.insertarNuevoFichero(nuevo,
					DConector.Daplicacion);

			if (f2 != null)
			{
				DFileEvent evento = new DFileEvent();
				evento.padre = getDocumentoSeleccionado();
				evento.fichero = f2;
				evento.tipo = new Integer(DFileEvent.NOTIFICAR_INSERTAR_FICHERO
						.intValue());
				enviarEvento(evento);
			}

			return nuevo;
		}
		else return null;
	}

	/**
	 * Comprueba si un documento existe en un determinado nodo
	 * 
	 * @param n
	 *            Nodo del arbol en el que buscamos el documento
	 * @param ruta
	 *            Path del documento
	 * @return True si el fichero existe en la ruta especificada. False en caso
	 *         contrario
	 */
	public boolean existeFichero(DefaultMutableTreeNode n, String ruta)
	{

		if (!n.isRoot()
				&& ( ( (MIDocumento) n.getUserObject() ).getRutaLocal()
						.equals(ruta) ))
			return true;

		else if (n.getChildCount() > 0)
		{

			for (int i = 0; i < n.getChildCount(); ++i)
			{
				if (existeFichero((DefaultMutableTreeNode) n.getChildAt(i),
						ruta)) return true;

			}
			return false;
		}
		else return false;
	}

	/**
	 * Buscar un documento en un nodo determinado
	 * 
	 * @param n
	 *            Nodo del arbol en el que buscamos el documento
	 * @param ruta
	 *            Path del documento
	 * @return Metainformacion del documento si fue encontrado o null si no se
	 *         pudo encontrar
	 */
	public MIDocumento buscarFichero(DefaultMutableTreeNode n, String ruta)
	{

		if (!n.isRoot()
				&& ( ( (MIDocumento) n.getUserObject() ).getRutaLocal()
						.equals(ruta) ))
			return (MIDocumento) n.getUserObject();

		else if (n.getChildCount() > 0)
		{

			MIDocumento f;

			for (int i = 0; i < n.getChildCount(); ++i)
			{
				f = buscarFichero((DefaultMutableTreeNode) n.getChildAt(i),
						ruta);
				if (f != null) return f;

			}
			return null;
		}
		else return null;
	}

	/**
	 * Sube un fichero al servidor en la carpeta que esta seleccionada
	 * actualmente. Si se intenta subir un fichero duplicado, muestra un mensaje
	 * de error indicando si el usuario desea: cancelar, cambiar el nombre o
	 * sobreescribir.
	 * 
	 * @return Evento para enviar al servidor de ficheros o null si ocurrio
	 *         algun error
	 */
	public void subirFicheroServidor()
	{
		// obtenemos los datos del fichero asociados al nodo seleccionado
		MIDocumento carpeta = getDocumentoSeleccionado();

		// si el fichero escogido no es directorio, salimos
		if (carpeta == null)
		{
			JOptionPane.showMessageDialog(null,
					"Debe escoger un directorio al cual subir el documento");
			return;
		}

		if (!carpeta.esDirectorio())
		{
			DefaultMutableTreeNode df = buscarFichero(
					(DefaultMutableTreeNode) arbol.getModelo().getRoot(),
					carpeta.getPadre());

			carpeta = buscarFichero(df, ( (MIDocumento) df.getUserObject() )
					.getRutaLocal());
		}

		String path = carpeta.getRutaLocal() + "/";

		if (path.equals("//")) path = "/";

		// recuperamos el usuario y el rol
		MIUsuario user = ClienteMetaInformacion.cmi
				.getUsuarioConectado(DConector.Dusuario);
		MIRol rol = ClienteMetaInformacion.cmi.getRol(DConector.Drol);

		// si se ha producido algun error, salimos
		if (( user == null ) || ( rol == null )) return;

		if (!carpeta.comprobarPermisos(user.getNombreUsuario(), rol
				.getNombreRol(), MIDocumento.PERMISO_ESCRITURA))
		{
			JOptionPane
					.showMessageDialog(null,
							"No tiene permiso para escribir en el directorio seleccionado");
			return;
		}

		// mostramos el selector de ficheros
		JFileChooser jfc = new JFileChooser("Subir Documento Servidor");

		int op = jfc.showDialog(null, "Aceptar");

		// si no se ha escogido la opcion aceptar en el dialogo de apertura de
		// fichero salimos
		if (op != JFileChooser.APPROVE_OPTION) return;

		java.io.File f = jfc.getSelectedFile();

		String nombre = f.getName();

		MIDocumento anterior = ClienteFicheros.cf.existeFichero(path + nombre,
				DConector.Daplicacion);

		while (anterior != null)
		{
			// si no tenemos permisos de escritura sobre el documento no podemos
			// sobrescribirlo
			if (!anterior.comprobarPermisos(DConector.Dusuario, DConector.Drol,
					MIDocumento.PERMISO_ESCRITURA))
			{
				JOptionPane
						.showMessageDialog(null,
								"No tiene suficientes privilegios para subir ese documento");
				return;
			}

			Object[] options =
			{ "Sobreescribir", "Renombrar", "Cancelar" };

			int sel = JOptionPane.showOptionDialog(this,
					"El documento ya existe ¿Que desea hacer?",
					"Fichero ya existente", JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

			// el usuario ha cancelado la accion
			if (sel == JOptionPane.CANCEL_OPTION)
				return;

			// el usuario desea sobreescribir el documento
			else if (sel == JOptionPane.YES_OPTION)
			{
				ClienteFicheros.cf.generarVersion(anterior, path);

				eliminarNodo(anterior.getId());

				repaint();

				anterior = null;
			}

			// el usuario desea renombrar el fichero
			else if (sel == JOptionPane.NO_OPTION)
			{
				nombre = JOptionPane.showInputDialog("Nuevo nombre");

				if (nombre != null && !nombre.equals(""))
				{
					anterior = ClienteFicheros.cf.existeFichero(path + nombre,
							DConector.Daplicacion);
				}
				else return;
			}
			// el usuario ha cerrado el dialogo
			else if (sel == JOptionPane.CLOSED_OPTION) return;
		}

		byte[] bytes = null;
		try
		{
			// abrimos el fichero en modo lectura
			RandomAccessFile raf = new RandomAccessFile(f.getAbsolutePath(),
					"r");

			// consultamos el tamanio del fichero, reservamos
			// memoria suficiente, leemos el fichero y lo cerramos
			bytes = new byte[(int) raf.length()];
			raf.read(bytes);
			raf.close();
		}
		catch (FileNotFoundException ex)
		{
			JOptionPane.showMessageDialog(null, "El fichero no existe",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		catch (IOException e1)
		{
			JOptionPane.showMessageDialog(null,
					"Error en la lectura del fichero", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// creamos el nuevo fichero a almacenar
		MIDocumento fbd = new MIDocumento(-1, nombre, false, "rwrw--", user,
				rol, carpeta.getId(), path + nombre, MIDocumento
						.getExtension(nombre));

		// enviamos el nuevo fichero al servidor
		Transfer t = new Transfer(ClienteFicheros.ipConexion, path + nombre);

		// si se ha producido algun error: MENSAJE y SALIMOS
		if (!t.sendFile(bytes))
		{
			JOptionPane
					.showMessageDialog(
							null,
							"No se ha podido subir el fichero.\nSe ha producido un error en la transmisión del documento",
							"Error", JOptionPane.ERROR_MESSAGE);

			return;
		}

		// si no se ha producido ningun error al subir el fichero
		else
		{
			// insertamos el nuevo fichero en el servidor
			MIDocumento f2 = ClienteFicheros.cf.insertarNuevoFichero(fbd,
					DConector.Daplicacion);

			// si ha habido algun error salimos
			if (f2 == null)
			{
				JOptionPane
						.showMessageDialog(this,
								"Ha ocurrido un error: no se ha podido subir el documento al servidor");
				return;
			}

			// notificamos al resto de usuarios la "novedad"
			DFileEvent evento = new DFileEvent();
			evento.fichero = f2;
			evento.padre = carpeta;
			evento.tipo = new Integer(DFileEvent.NOTIFICAR_INSERTAR_FICHERO
					.intValue());

			this.enviarEvento(evento);
		}
	}
}
