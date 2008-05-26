package aplicacion.gui.componentes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.FicheroBD;
import aplicacion.fisica.eventos.DFileEvent;
import aplicacion.fisica.net.Transfer;

/**
 * Clase que se encargar‡ de almacenar los datos de los documentos NOTA: de
 * momento esto es una clase STUB, los datos son generados desde el programa.
 * 
 * @author anab
 */
public class ArbolDocumentos extends JTree
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3359982437919729727L;

	public ArbolDocumentos(DefaultMutableTreeNode raiz){
		super(raiz);
		this.setRootVisible(false);
		this.setCellRenderer(new DocumentosCellRenderer());
		this.expandRow(0);
	}
	
	public FicheroBD getDocumentoSeleccionado()
	{
		TreePath camino = getSelectionPath();

		Object[] objetos = null;

		if (camino != null) objetos = camino.getPath();

		if (( objetos != null ) && ( objetos.length > 0 ))
			return (FicheroBD) ( (DefaultMutableTreeNode) objetos[objetos.length - 1] )
					.getUserObject();
		else return null;
	}
	
	public DefaultMutableTreeNode getNodoSeleccionado()
	{
		TreePath camino = this.getSelectionPath();

		Object[] objetos = null;

		if (camino != null) objetos = camino.getPath();

		if (( objetos != null ) && ( objetos.length > 0 ))
			return (DefaultMutableTreeNode) objetos[objetos.length - 1];
		else return null;
	}
	
	public static DefaultMutableTreeNode buscarFichero(DefaultMutableTreeNode n,
			int id)
	{
		if (!n.isRoot() && ( ( (FicheroBD) n.getUserObject() ).getId() == id ))
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
	
	public void imprimirFichero(){
		FicheroBD doc = getDocumentoSeleccionado();
		
		if (doc.esDirectorio()) return;
		
		Transfer t = new Transfer(
				ClienteFicheros.ipConexion, doc.getRutaLocal());

		Documento d = t.receiveDocumento();
		
		d.imprimir();
	}
	
	public void guardarDocumentoLocalmente(){
		FicheroBD doc = this.getDocumentoSeleccionado();

		JFileChooser jfc = new JFileChooser(
				"Guardar Documento Localmente");
		
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		jfc.setSelectedFile(new File(doc.getNombre()));

		int op = jfc.showDialog(null, "Aceptar");

		if (op == JFileChooser.APPROVE_OPTION)
		{
			java.io.File f = jfc.getSelectedFile();
			
			
			
			if (doc.esDirectorio()) return;

			Transfer t = new Transfer(
					ClienteFicheros.ipConexion, doc.getRutaLocal());

			byte[] datos = t.receiveFileBytes();

			try
			{
				RandomAccessFile acf = new RandomAccessFile(
						f.getAbsolutePath(), "rw");

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
	
	public boolean eliminarFichero(){
		FicheroBD f = this.getDocumentoSeleccionado();

		if (f != null) {
			if (!f.esDirectorio() && f.comprobarPermisos(DConector.Dusuario, DConector.Drol, FicheroBD.PERMISO_ESCRITURA))
			{

				ClienteFicheros.obtenerClienteFicheros().borrarFichero(f,DConector.Daplicacion);
				return true;
			}
			else
			{
				int res = JOptionPane.showConfirmDialog(
								null,
								"Si elimina un directorio tambiŽn se eliminar‡n todos\nlos "
										+ "ficheros contenidos en la carpeta y/o en las subcarpetas.\nÀSeguro que desea continuar?",
								"ÀDesea continuar?",
								JOptionPane.OK_CANCEL_OPTION);

				if (res == 0)
					if (!comprobarDirectorio(f))
						return false;
					else
					{

						ClienteFicheros.obtenerClienteFicheros().borrarFichero(f,DConector.Daplicacion);
						return true;
					}
				
				else return false;

			}
		}
		else return false;
	}
	
	/**
	 * Comprueba los permisos que se tienen sobre un direcotorio
	 * @param dir directorio
	 * @return true si tenemos pleno acceso al directorio y false en caso contrario
	 */
	private boolean comprobarDirectorio(FicheroBD dir)
	{
		String u = DConector.Dusuario;
		String r = DConector.Drol;

		boolean permisosDir = dir.comprobarPermisos(u, r,
				FicheroBD.PERMISO_ESCRITURA);

		if (!dir.esDirectorio())
			return permisosDir;
		else if (permisosDir)
		{
			// si es un directorio
			boolean res = true;

			DefaultTreeModel modelo = (DefaultTreeModel) this
					.getModel();
			DefaultMutableTreeNode raiz = (DefaultMutableTreeNode) modelo
					.getRoot();

			DefaultMutableTreeNode nodo = ArbolDocumentos.buscarFichero(raiz, dir.getId());

			// tenemos que recorrer todos los directorios que cuelgan de dir
			int numHijos = modelo.getChildCount(nodo);

			for (int i = 0; i < numHijos; ++i)
			{
				FicheroBD h = (FicheroBD) ( (DefaultMutableTreeNode) modelo
						.getChild(nodo, i) ).getUserObject();

				// comprobamos si tenemos permiso escritura en el fichero
				if (!h.comprobarPermisos(u, r, FicheroBD.PERMISO_ESCRITURA))
					return false;

				// si es un directorio nos aseguramos de que tengamso acceso a
				// todos los ficheros
				if (h.esDirectorio())
					if (!comprobarDirectorio(h)) return false;
			}

			return res;
		}
		else return false;
	}
}
