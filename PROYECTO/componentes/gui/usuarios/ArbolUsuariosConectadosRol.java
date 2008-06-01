package componentes.gui.usuarios;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import metainformacion.ClienteMetaInformacion;

import componentes.base.DComponenteBase;
import Deventos.DMIEvent;

/**
 * Con este componente podemos ver todos los usuarios que hay conectados que
 * estan desempe–ando el mismo rol que nosotros
 */

@SuppressWarnings( "serial" )
/**
 * Clase que muestra un panel con los usuarios conectados clasificados por roles
 */
public class ArbolUsuariosConectadosRol extends DComponenteBase
{

	/**
	 * Layout del panel
	 */
	BorderLayout borderLayout1 = new BorderLayout();

	/**
	 * ScrollPane donde se guarda el arbol
	 */
	JScrollPane jScrollPane1 = new JScrollPane();

	/**
	 * Arbol con los usuarios clasificados por rol
	 */
	ArbolUsuarios arbol = null;

	/**
	 * Ra’z del ‡rbol
	 */
	DefaultMutableTreeNode raiz = null;

	/**
	 * @param nombre
	 *            String Nombre del componente.
	 * @param conexionDC
	 *            boolean TRUE si esta en contacto directo con el DConector (no
	 *            es hijo de ningun otro componente) y FALSE en otro caso
	 * @param padre
	 *            DComponenteBase Componente padre de este componente. Si no
	 *            tiene padre establecer a null
	 */
	public ArbolUsuariosConectadosRol( String nombre, boolean conexionDC,
			DComponenteBase padre )
	{
		super(nombre, conexionDC, padre);

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ArbolUsuariosConectadosRol(){
		
	}

	/**
	 * Procesa los eventos de Metainformacion que le llegan
	 * 
	 * @param evento
	 *            DMIEvent Evento recibido
	 */
	@Override
	public void procesarMetaInformacion(DMIEvent evento)
	{
		super.procesarMetaInformacion(evento);
		if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue())
		{
			this.obtenerPanelContenido().repaint();

			this.actualizarLista();

			arbol.eliminarUsuario(evento.usuario);
			arbol.insertarNuevoUsuario(evento.rol, evento.usuario);

			arbol.expandir();
		}
		if (evento.tipo.intValue() == DMIEvent.NOTIFICACION_CONEXION_USUARIO
				.intValue())
		{
			arbol.insertarNuevoUsuario(evento.rol, evento.usuario);
		}
		if (evento.tipo.intValue() == DMIEvent.NOTIFICACION_DESCONEXION_USUARIO
				.intValue())
		{

			arbol.eliminarUsuario(evento.usuario);
		}
		if (evento.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO
				.intValue())
		{
			arbol.cambiarRol(evento.rolAntiguo, evento.rol, evento.usuario);
		}
	}

	public String getUsuarioSeleccionado()
	{

		TreePath tp = arbol.getSelectionPath();

		if (tp != null)
		{

			if (tp.getPathCount() == 3)
			{
				Object vector[] = tp.getPath();

				return vector[vector.length - 1].toString();
			}
			else return null;
		}
		else return null;
	}

	/**
	 * Inicializa el componente
	 * 
	 * @throws Exception
	 */
	private void jbInit() throws Exception
	{

		this.setLayout(borderLayout1);
		raiz = new DefaultMutableTreeNode("Usuarios");
		arbol = new ArbolUsuarios(raiz);
		jScrollPane1 = new JScrollPane(arbol);
		this.add(jScrollPane1, BorderLayout.CENTER);

		ArbolUsuariosConectadosRol.cambiarIconosArbol(arbol,
				"Resources/page_user_dark.gif",
				"Resources/page_user_dark.gif", "Resources/icon_user.gif",
				"Resources/page_user_dark.gif");

	}

	/**
	 * Actualiza la el arbol con los datos iniciales
	 */
	private void actualizarLista()
	{
		Vector v = ClienteMetaInformacion.obtenerCMI().obtenerRoles();
		Vector v2;
		DefaultMutableTreeNode aux;

		if (v != null) {
			
			for (int i = 0; i < v.size(); ++i)
			{
				aux = new DefaultMutableTreeNode(v.get(i));

				v2 = ClienteMetaInformacion.obtenerCMI()
						.obtenerUsuariosBajoRol(v.get(i).toString());

				
				if (( v2 != null ) && ( v2.size() > 0 )) {
					
					for (int j = 0; j < v2.size(); j++) {
						aux.insert(new DefaultMutableTreeNode(v2.get(j)), j);
					}
				}
				raiz.insert(aux, i);
			}
		}
	}

	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE
	 * devuelve 0
	 * 
	 * @return int Numero de componentes hijos. SIEMPRE devuelve 0.
	 */
	@Override
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	public static void cambiarIconosArbol(JTree arbol2, String close,
			String open, String leaf, String no)
	{
		// Retrieve the three icons
		Icon leafIcon = new ImageIcon(leaf);
		Icon openIcon = new ImageIcon(open);
		Icon closedIcon = new ImageIcon(close);
		Icon noIcon = new ImageIcon(no);

		// Update only one tree instance
		CustomCellRenderer renderer = new CustomCellRenderer(noIcon);

		renderer.setLeafIcon(leafIcon);
		renderer.setClosedIcon(closedIcon);
		renderer.setOpenIcon(openIcon);

		arbol2.setCellRenderer(renderer);
	}
}

class CustomCellRenderer extends DefaultTreeCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7075327761579832712L;

	Icon noIcon = null;

	public CustomCellRenderer( Icon n )
	{
		noIcon = n;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		if (leaf && isRolNode(value))
		{
			setIcon(noIcon);
			setToolTipText("Sin usuarios conectados.");
		}
		else setToolTipText(null); // no tool tip

		return this;
	}

	private boolean isRolNode(Object value)
	{
		boolean res = false;

		if (value != null)
		{

			DefaultMutableTreeNode dftn = (DefaultMutableTreeNode) value;

			if (dftn.getParent() == null)
				res = false;

			else if (dftn.getParent().getParent() == null) res = true;
		}

		return res;
	}
}
