package aplicacion.gui.componentes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import metainformacion.ClienteMetaInformacion;
import metainformacion.MIRol;
import metainformacion.MIUsuario;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.MetainformacionFichero;
import aplicacion.fisica.net.Transfer;

/**
 * Clase que se encargar‡ de almacenar los datos de los documentos
 * @author anab
 */
public class ArbolDocumentos extends JTree
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3359982437919729727L;

	/**
	 * 
	 * @param raiz
	 */
	public ArbolDocumentos(DefaultMutableTreeNode raiz){
		super(raiz);
		this.setRootVisible(false);
		this.setCellRenderer(new DocumentosCellRenderer());
		this.expandRow(0);
	}
	
	/**
	 * 
	 * @return
	 */
	public MetainformacionFichero getDocumentoSeleccionado()
	{
		TreePath camino = getSelectionPath();

		Object[] objetos = null;

		if (camino != null) objetos = camino.getPath();

		if (( objetos != null ) && ( objetos.length > 0 ))
			return (MetainformacionFichero) ( (DefaultMutableTreeNode) objetos[objetos.length - 1] )
					.getUserObject();
		else return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getNodoSeleccionado()
	{
		TreePath camino = this.getSelectionPath();

		Object[] objetos = null;

		if (camino != null) objetos = camino.getPath();

		if (( objetos != null ) && ( objetos.length > 0 ))
			return (DefaultMutableTreeNode) objetos[objetos.length - 1];
		else return null;
	}
	
	/**
	 * 
	 * @param n
	 * @param id
	 * @return
	 */
	public static DefaultMutableTreeNode buscarFichero(DefaultMutableTreeNode n,
			int id)
	{
		if (!n.isRoot() && ( ( (MetainformacionFichero) n.getUserObject() ).getId() == id ))
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
	 * 
	 *
	 */
	public void imprimirFichero(){
		MetainformacionFichero doc = getDocumentoSeleccionado();
		
		if (doc.esDirectorio()) return;
		
		Transfer t = new Transfer(
				ClienteFicheros.ipConexion, doc.getRutaLocal());

		Documento d = t.receiveDocumento(true);
		
		d.imprimir();
	}
	
	/**
	 * 
	 *
	 */
	public void guardarDocumentoLocalmente(){
		MetainformacionFichero doc = this.getDocumentoSeleccionado();

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
	
	/**
	 * 
	 * @return
	 */
	public boolean eliminarFichero(){
		MetainformacionFichero f = this.getDocumentoSeleccionado();

		if (f != null) {
			if (!f.esDirectorio() && f.comprobarPermisos(DConector.Dusuario, DConector.Drol, MetainformacionFichero.PERMISO_ESCRITURA))
			{

				ClienteFicheros.obtenerClienteFicheros().borrarFichero(f,DConector.Daplicacion);
				return true;
			}
			else
			{
				DefaultMutableTreeNode nodo = this.getNodoSeleccionado();
				
				if (nodo.getChildCount() == 0) {
					ClienteFicheros.obtenerClienteFicheros().borrarFichero(f,DConector.Daplicacion);
					return true;
				}
				else {
					JOptionPane.showMessageDialog(
									null,
									"No se puede eliminar la carpeta dado que esta tiene documentos y/o otras carpetas");
					
					return false;
				}

			}
		}
		else return false;
	}
	
	/**
	 * Agrega una carpeta a la carpeta seleccionada anteriormente
	 * @param nombre nuevo nombre de la carpeta
	 * @return el ficheroBD con los datos de la nueva carpeta
	 */
	public MetainformacionFichero agregarCarpeta(String nombre){
		MetainformacionFichero f = this.getDocumentoSeleccionado();
		
		if (f.esDirectorio()){
			
			//creamos el nodo
			MetainformacionFichero nuevo = new MetainformacionFichero();
			
			if (!f.getRutaLocal().equals("/"))
				nuevo.setRutaLocal(f.getRutaLocal()+"/"+nombre);
			else
				nuevo.setRutaLocal("/"+nombre);
			
			if (existeFichero((DefaultMutableTreeNode)this.getModel().getRoot(), nuevo.getRutaLocal())) {
				JOptionPane.showMessageDialog(null, "La carpeta ya existe");
				return null;
			}
				
			
			//recuperamos el usuario y el rol
			MIUsuario user = ClienteMetaInformacion.cmi.getUsuario(DConector.Dusuario);
			MIRol rol = ClienteMetaInformacion.cmi.getRol(DConector.Drol);
			
			if (user==null || rol == null) return null;
			
			nuevo.setNombre(nombre);
			nuevo.setPadre(f.getId());
			nuevo.setRol(rol);
			nuevo.setUsuario(user);
			nuevo.setPermisos("rwrw--");
			nuevo.setTipo("NULL");
			
			
			nuevo.esDirectorio(true);
			
			return nuevo;
		}
		else  return null;
	}
	
	/**
	 * Comprueba si un fichero determinado existe en un determinado nodo
	 * @param n nodo nodo del arbol en el que buscamos el documento
	 * @param ruta ruta del fichero
	 * @return true si el fichero ya existe en la ruta y false en caso contrario
	 */
	public boolean existeFichero(DefaultMutableTreeNode n, String ruta){
		
		if (!n.isRoot() && ( ( (MetainformacionFichero) n.getUserObject() ).getRutaLocal().equals(ruta) ))
			return true;
		
		else if (n.getChildCount() > 0)
		{

			for (int i = 0; i < n.getChildCount(); ++i)
			{
				if (existeFichero((DefaultMutableTreeNode) n.getChildAt(i),
						ruta))
					return true;

			}
			return false;
		}
		else return false;
	}
	
	/**
	 * Comprueba si un fichero determinado existe en un determinado nodo
	 * @param n nodo nodo del arbol en el que buscamos el documento
	 * @param ruta ruta del fichero
	 * @return true si el fichero ya existe en la ruta y false en caso contrario
	 */
	public MetainformacionFichero buscarFichero(DefaultMutableTreeNode n, String ruta){
		
		if (!n.isRoot() && ( ( (MetainformacionFichero) n.getUserObject() ).getRutaLocal().equals(ruta) ))
			return (MetainformacionFichero)n.getUserObject();
		
		else if (n.getChildCount() > 0)
		{

			MetainformacionFichero f;
			
			for (int i = 0; i < n.getChildCount(); ++i)
			{
				f = buscarFichero((DefaultMutableTreeNode) n.getChildAt(i),
						ruta);
				if (f != null)
					return f;

			}
			return null;
		}
		else return null;
	}
}
