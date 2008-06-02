package aplicacion.plugin.example.pizarra;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import util.Separador;

public class ControlesPizarra extends JPanel
{

	private static final long serialVersionUID = 1L;

	private JComboBox listaPinceles = null;

	DefaultComboBoxModel modeloPincel = null;

	DefaultComboBoxModel modeloColores = null;

	private Pizarra pizarra = null;

	private int colorActual;

	private int trazoActual;

	private JButton botonLimpiarLienzo = null;

	private JButton botonDeshacer = null;

	private JButton botonBorrar = null;

	private JButton botonAnterior = null;

	private JButton botonSiguiente = null;

	private JButton botonRehacer = null;

	private JToolBar barraHerramientas = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JButton botonPaletaColores = null;
	
	private JToggleButton botonSeleccionar = null;

	/**
	 * Devuelve el color actual
	 * 
	 * @return el nœmero asociado al color actual
	 */
	public int getColorActual()
	{
		return colorActual;
	}

	/**
	 * Obtiene el tipo de trazo usado en este momento
	 * 
	 * @return el entero que representa el tipo de trazo
	 */
	public int getTrazoActual()
	{
		return trazoActual;
	}

	/**
	 * This is the default constructor
	 */
	public ControlesPizarra( Pizarra l )
	{
		super();
		pizarra = l;
		initialize();
	}

	public void setPadre(JFrame p)
	{
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.setRows(1);
		gridLayout.setColumns(3);
		gridLayout.setVgap(2);
		gridLayout.setHgap(2);
		this.setLayout(gridLayout);
		this.setSize(551, 39);

		this.setMinimumSize(new Dimension(552, 103));
		this.add(getBarraHerramientas(), null);
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */

	private Color elegirColor()
	{
		Color c = javax.swing.JColorChooser.showDialog(null, "Escoge el Color",
				pizarra.getColor());
		return c;
	}

	/**
	 * This method initializes jComboBox1
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getListaPinceles()
	{
		if (listaPinceles == null)
		{
			listaPinceles = new JComboBox();

			modeloPincel = new DefaultComboBoxModel();
			modeloPincel.addElement("Lineas");
			modeloPincel.addElement("Mano Alzada");
			modeloPincel.addElement("Texto");
			modeloPincel.addElement("Rectangulo");
			modeloPincel.addElement("Ovalo");

			listaPinceles.setModel(modeloPincel);

			listaPinceles.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					trazoActual = listaPinceles.getSelectedIndex();
					pizarra.setTrazo(trazoActual);
				}
			});

			listaPinceles.setSelectedIndex(0);
			listaPinceles.setPreferredSize(new Dimension(100, 20));
		}
		return listaPinceles;
	}

	/**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonLimpiar()
	{
		if (botonLimpiarLienzo == null)
		{
			botonLimpiarLienzo = new JButton();
			botonLimpiarLienzo.setText("");
			botonLimpiarLienzo.setToolTipText("Limpia el lienzo");

			botonLimpiarLienzo.setBorder(null);
			botonLimpiarLienzo.setBorderPainted(false);
			botonLimpiarLienzo.setPreferredSize(new Dimension(30, 24));
			botonLimpiarLienzo.setSize(new Dimension(30, 24));

			botonLimpiarLienzo.setIcon(new ImageIcon(
					"./Resources/edit-clear_16x16.png"));

			botonLimpiarLienzo
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{

							// enviamos el evento para sincronizar el borrado
							// del lienzo
							DJPizarraEvent el = new DJPizarraEvent();
							el.tipo = DJPizarraEvent.LIMPIEZA_LIENZO;
							pizarra.enviarEvento(el);
						}
					});
		}
		return botonLimpiarLienzo;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonDeshacer()
	{
		if (botonDeshacer == null)
		{
			botonDeshacer = new JButton();
			botonDeshacer.setText("");
			botonDeshacer.setBorder(null);
			botonDeshacer.setBorderPainted(false);
			botonDeshacer.setToolTipText("Deshace el ultimo trazo");
			botonDeshacer.setPreferredSize(new Dimension(35, 24));
			botonDeshacer.setSize(new Dimension(35, 16));
			botonDeshacer.setIcon(new ImageIcon("./Resources/arrow_undo.png"));
			botonDeshacer.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					// creamos un nuevo evento para indicar la accion de
					// deshacer
					DJPizarraEvent evt = new DJPizarraEvent();
					evt.tipo = DJPizarraEvent.DESHACER;
					pizarra.enviarEvento(evt);

				}
			});
		}
		return botonDeshacer;
	}

	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonBorrar()
	{
		if (botonBorrar == null)
		{
			botonBorrar = new JButton();
			botonBorrar.setText("");
			botonBorrar.setBorder(null);
			botonBorrar.setBorderPainted(false);
			botonBorrar.setIcon(new ImageIcon("./Resources/delete2.png"));
			botonBorrar.setPreferredSize(new Dimension(32, 24));
			botonBorrar.setToolTipText("Borrar el elemento seleccionado");
			botonBorrar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					DJPizarraEvent ev = new DJPizarraEvent();
					ev.tipo = DJPizarraEvent.BORRADO;
					ev.aBorrar = pizarra.getObjetoSeleccionado();
					pizarra.enviarEvento(ev);
				}
			});
		}
		return botonBorrar;
	}
	
	private JToggleButton getBotonSeleccionar()
	{
		if (botonSeleccionar == null)
		{
			botonSeleccionar = new JToggleButton();
			botonSeleccionar.setText("");
			botonSeleccionar.setBorder(null);
			botonSeleccionar.setBorderPainted(false);
			botonSeleccionar.setPreferredSize(new Dimension(30, 24));
			botonSeleccionar.setSize(new Dimension(30, 24));
			botonSeleccionar.setIcon(new ImageIcon("Resources/puntero_seleccion.png"));
			botonSeleccionar
					.setToolTipText("Permite alternar entre el modo de seleccion y el modo de pintura");
			botonSeleccionar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					pizarra.setEstaSeleccionando(botonSeleccionar.isSelected());
				}
			});
		}
		
		return botonSeleccionar;
	}

	/**
	 * This method initializes jButton21
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonAnterior()
	{
		if (botonAnterior == null)
		{
			botonAnterior = new JButton();
			botonAnterior.setIcon(new ImageIcon("./Resources/arrow_left.png"));
			botonAnterior.setToolTipText("Seleccionar el elemento anterior");
			botonAnterior.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					pizarra.anteriorObjeto();
				}
			});

			botonAnterior.setBorder(null);
			botonAnterior.setText("");
			botonAnterior.setBorderPainted(false);
		}
		return botonAnterior;
	}

	/**
	 * This method initializes jButton211
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonSiguiente()
	{
		if (botonSiguiente == null)
		{
			botonSiguiente = new JButton();
			botonSiguiente
					.setIcon(new ImageIcon("./Resources/arrow_right.png"));
			botonSiguiente.setToolTipText("Seleccionar el siguiente elemento");
			botonSiguiente.setPreferredSize(new Dimension(32,24));
			botonSiguiente
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							pizarra.siguienteObjeto();
						}
					});

			botonSiguiente.setBorder(null);
			botonSiguiente.setBorderPainted(false);
		}
		return botonSiguiente;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonRehacer()
	{
		if (botonRehacer == null)
		{
			botonRehacer = new JButton();
			botonRehacer.setText("");
			botonRehacer.setToolTipText("Rehace el œltimo trazo eliminado");
			botonRehacer.setIcon(new ImageIcon("./Resources/arrow_redo.png"));
			botonRehacer.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					DJPizarraEvent ev = new DJPizarraEvent();
					ev.tipo = DJPizarraEvent.REHACER;
					pizarra.enviarEvento(ev);
				}
			});
			botonRehacer.setBorder(null);
			botonRehacer.setPreferredSize(new Dimension(35, 24));
			botonRehacer.setBorderPainted(false);
		}
		return botonRehacer;
	}

	/**
	 * This method initializes jToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getBarraHerramientas()
	{
		if (barraHerramientas == null)
		{
			jLabel2 = new JLabel();
			jLabel2.setText("Pincel  ");
			jLabel1 = new JLabel();
			jLabel1.setIcon(new ImageIcon("./Resources/pencil.png"));
			jLabel1.setText(" ");

			Separador separator1 = new Separador();
			Separador separator2 = new Separador();
			Separador separator3 = new Separador();
			Separador separator4 = new Separador();

			barraHerramientas = new JToolBar();
			barraHerramientas.setFloatable(false);
			barraHerramientas.setPreferredSize(new Dimension(533, 35));
			barraHerramientas.add(getBotonDeshacer());
			barraHerramientas.add(getBotonRehacer());
			barraHerramientas.add(separator2);
			barraHerramientas.add(getBotonAnterior());
			barraHerramientas.add(getBotonSiguiente());
			barraHerramientas.add(getBotonSeleccionar());
			barraHerramientas.add(getBotonBorrar());
			barraHerramientas.add(separator3);
			barraHerramientas.add(getBotonLimpiar());
			barraHerramientas.add(separator4);
			barraHerramientas.add(jLabel1);
			barraHerramientas.add(jLabel2);

			barraHerramientas.add(getListaPinceles());
			barraHerramientas.add(separator1);
			barraHerramientas.add(getBotonColores());
		}
		return barraHerramientas;
	}

	/**
	 * This method initializes botonLimpiarImagen1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonColores()
	{
		if (botonPaletaColores == null)
		{
			botonPaletaColores = new JButton("Colores");
			botonPaletaColores.setBorder(null);
			botonPaletaColores.setBorderPainted(false);
			botonPaletaColores
					.setIcon(new ImageIcon("./Resources/palette.png"));
			botonPaletaColores.setText("Colores   ");
			botonPaletaColores.setToolTipText("Elimina la imagen de fondo");
			botonPaletaColores
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							Color c = elegirColor();

							if (c != null) pizarra.setColor(c);
						}
					});
		}
		return botonPaletaColores;
	}

} // @jve:decl-index=0:visual-constraint="-78,19"
