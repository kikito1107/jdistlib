package plugins.examples.whiteboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import componentes.gui.visualizador.ControlesDibujo;
import componentes.util.ComboBoxRenderer;
import componentes.util.Separador;


/**
 * Panel con los controles para la pizarra distribuida
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class ControlesPizarra extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JComboBox listaPinceles = null;

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
	 * Constructor por defecto
	 * @param l Pizarra a la que queremos asignar los controles
	 */
	public ControlesPizarra( Pizarra l )
	{
		super();
		pizarra = l;
		initialize();
	}
	
	/**
	 * Devuelve el color actual
	 * 
	 * @return Número asociado al color actual
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
	 * Permite seleccionar un color en un selector de colores
	 * @return Color seleccionado
	 */
	private Color elegirColor()
	{
		Color c = javax.swing.JColorChooser.showDialog(null, "Escoge el Color",
				pizarra.getColor());
		return c;
	}

	private JComboBox getListaPinceles()
	{
		if (listaPinceles == null)
		{
Integer[] enteros = new Integer[5];
			
			for(int i=0; i<5; ++i){
				enteros[i] = i;
			}
			
			listaPinceles = new JComboBox(enteros);
			
			//creamos la lista de 
			String[] pinceles = {"Lineas","Mano Alzada","Texto","Rectangulo","Ovalo"};
			ImageIcon[] images = new ImageIcon[5];
			images[0] = ControlesDibujo.createImageIcon("Resources/line_16x16.gif");
			images[1] = ControlesDibujo.createImageIcon("Resources/stock_draw-freeform-line_16x16.png");
			images[2] = ControlesDibujo.createImageIcon("Resources/font_16x16.png");
			images[3] = ControlesDibujo.createImageIcon("Resources/mini_rect.png");
			images[4] = ControlesDibujo.createImageIcon("Resources/circle_20x20.png");
			
			
			ComboBoxRenderer renderer= new ComboBoxRenderer(images, pinceles);
	        renderer.setPreferredSize(new Dimension(30, 30));
	        listaPinceles.setRenderer(renderer);
	        listaPinceles.setMaximumRowCount(5);

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

	private JButton getBotonRehacer()
	{
		if (botonRehacer == null)
		{
			botonRehacer = new JButton();
			botonRehacer.setText("");
			botonRehacer.setToolTipText("Rehace el último trazo eliminado");
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

}