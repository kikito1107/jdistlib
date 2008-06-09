package componentes.gui.usuarios;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import awareness.ClienteMetaInformacion;

import Deventos.enlaceJS.DConector;

import componentes.base.DComponenteBase;
import componentes.util.ListaElementos;
import Deventos.DMIEvent;

/**
 * Componente para poder cambiar el rol que estamos desempeñando. Aparte
 * nos muestra que rol es el que estamos desempeñando en cada momento.
 * 
 * @author Juan Antonio Ibañez Santorum. Carlos Rodriguez Dominguez. 
 * 		   Ana Belen Pelegrina Ortiz
 */
public class DICambioRol extends DComponenteBase
{
	private static final long serialVersionUID = 3140734302852368992L;

	private BorderLayout borderLayout1 = new BorderLayout();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private ListaElementos lista = new ListaElementos();

	private TitledBorder borde;

	private JPanel jPanel1 = new JPanel();

	private JLabel etiquetaRol = new JLabel();

	private JLabel etiquetaTexto = new JLabel();

	private JPanel jPanel2 = new JPanel();

	private JButton botonCambiar = new JButton();

	/**
	 * Constructor por defecto
	 */
	public DICambioRol()
	{
		super("CambioRol", true, null);

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Constructor con parametros
	 * @param nombre Nombre del componente
	 * @param conexionDC True si queremos una conexion directa con el @see DConector.
	 * 					False en otro caso.
	 * @param padre Componente padre a este componente
	 */
	public DICambioRol( String nombre, boolean conexionDC, DComponenteBase padre )
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

	/**
	 * Inicializacion de los componentes graficos
	 * @throws Exception
	 */
	private void jbInit() throws Exception
	{
		borde = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,
				new Color(165, 163, 151)), "Cambio de rol");
		this.setLayout(borderLayout1);
		this.obtenerPanelContenido().setBorder(borde);
		etiquetaTexto.setFont(new java.awt.Font("Dialog", 1, 11));
		etiquetaTexto.setText("Rol Actual: ");
		etiquetaRol.setFont(new java.awt.Font("Dialog", 1, 11));
		etiquetaRol.setForeground(Color.blue);
		botonCambiar.setText("Cambiar");
		botonCambiar
				.addActionListener(new DCambioRol_botonCambiar_actionAdapter(
						this));
		this.add(jScrollPane1, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.NORTH);
		jPanel1.add(etiquetaTexto, null);
		jPanel1.add(etiquetaRol, null);
		this.add(jPanel2, BorderLayout.SOUTH);
		jPanel2.add(botonCambiar, null);
		jScrollPane1.getViewport().add(lista, null);
	}

	/**
	 * Procesa los eventos de Metainformacion que le llegan
	 * 
	 * @param evento
	 *            Evento recibido
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void procesarMetaInformacion(DMIEvent evento)
	{
		super.procesarMetaInformacion(evento);
		if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue())
		{
			String rol = evento.infoCompleta.rol;
			etiquetaRol.setText(rol);
			Vector v = evento.infoCompleta.rolesPermitidos;
			lista.eliminarElementos();
			for (int i = 0; i < v.size(); i++)
				lista.aniadirElemento((String) v.elementAt(i));
		}
		if (evento.tipo.intValue() == DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO
				.intValue()) if (evento.usuario.equals(DConector.Dusuario))
		{
			String rol = evento.infoCompleta.rol;
			etiquetaRol.setText(rol);
		}
		if (evento.tipo.intValue() == DMIEvent.NOTIFICACION_NUEVO_ROL_PERMITIDO
				.intValue()) if (evento.usuario.equals(DConector.Dusuario))
		{
			String rol = evento.rol;
			lista.aniadirElemento(rol);
		}
		if (evento.tipo.intValue() == DMIEvent.NOTIFICACION_ELIMINAR_ROL_PERMITIVO
				.intValue()) if (evento.usuario.equals(DConector.Dusuario))
		{
			String rol = evento.rol;
			lista.eliminarElemento(rol);
		}
	}

	/**
	 * Obtiene el numero de componentes hijos de este componente. SIEMPRE
	 * devuelve 0
	 * 
	 * @return Numero de componentes hijos. SIEMPRE devuelve 0.
	 */
	@Override
	public int obtenerNumComponentesHijos()
	{
		return 0;
	}

	private void botonCambiar_actionPerformed(ActionEvent e)
	{
		String nuevoRol = lista.obtenerElementoSeleccionado();
		if (nuevoRol != null)
			if (!nuevoRol.equals("") && !nuevoRol.equals(DConector.Drol))
				ClienteMetaInformacion.obtenerCMI().cambiarRolUsuario(
						DConector.Dusuario, nuevoRol);
	}

	private class DCambioRol_botonCambiar_actionAdapter implements
			java.awt.event.ActionListener
	{
		DICambioRol adaptee;

		DCambioRol_botonCambiar_actionAdapter( DICambioRol adaptee )
		{
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e)
		{
			adaptee.botonCambiar_actionPerformed(e);
		}
	}
}
