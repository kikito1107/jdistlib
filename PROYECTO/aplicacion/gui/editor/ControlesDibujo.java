package aplicacion.gui.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import Deventos.DJLienzoEvent;

import util.Separador;
import Deventos.enlaceJS.DConector;
import aplicacion.fisica.ClienteFicheros;
import aplicacion.fisica.documentos.Documento;
import aplicacion.fisica.documentos.MIDocumento;
import aplicacion.fisica.net.Transfer;
import aplicacion.gui.VentanaCarga;
import aplicacion.gui.componentes.SelectorFicherosDistribuido;

public class ControlesDibujo extends JPanel
{

	private static final long serialVersionUID = 1L;

	private JComboBox listaPinceles = null;

	DefaultComboBoxModel modeloPincel = null;

	DefaultComboBoxModel modeloColores = null;

	private DILienzo lienzo = null;

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

	private Separador separator41 = null;

	private JButton botonPaletaColores = null;

	private JButton botonAbrir = null;

	private JButton botonGuardar = null;

	private JButton botonImprimir = null;

	private JButton botonGuardarLocal = null;
	
	private JToggleButton botonSeleccionar = null;
	
	private MonitorAbrir m = null;
	
	MIDocumento f = null;
	
	public ControlesDibujo(){
		
		lienzo = new DILienzo("", false, null);
		m = new MonitorAbrir();
		new HebraAbrir();
		initialize();
	}

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
	public ControlesDibujo( DILienzo l, BarraEstado be )
	{
		super();
		lienzo = l;
		m = new MonitorAbrir();
		new HebraAbrir();
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
				lienzo.getColor());
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
					lienzo.setTrazo(trazoActual);
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
					"Resources/edit-clear_16x16.png"));

			botonLimpiarLienzo
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							lienzo.limpiarLienzo();

							// enviamos el evento para sincronizar el borrado
							// del lienzo
							DJLienzoEvent el = new DJLienzoEvent();
							el.tipo = DJLienzoEvent.LIMPIEZA_LIENZO;
							el.path = new String(lienzo.getDocumento()
									.getPath());
							lienzo.enviarEvento(el);
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
			botonDeshacer.setIcon(new ImageIcon("Resources/arrow_undo.png"));
			botonDeshacer.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					lienzo.deshacer(lienzo.getPaginaActual() - 1);
					// creamos un nuevo evento para indicar la accion de
					// deshacer
					DJLienzoEvent evt = new DJLienzoEvent();
					evt.numPagina = new Integer(lienzo.getPaginaActual() - 1);
					evt.tipo = DJLienzoEvent.DESHACER;
					evt.path = new String(lienzo.getDocumento().getPath());
					lienzo.enviarEvento(evt);

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
			botonBorrar.setIcon(new ImageIcon("Resources/delete2.png"));
			botonBorrar.setPreferredSize(new Dimension(32, 24));
			botonBorrar.setToolTipText("Borrar el elemento seleccionado");
			botonBorrar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					DJLienzoEvent ev = new DJLienzoEvent();
					ev.tipo = DJLienzoEvent.BORRADO;
					ev.aBorrar = lienzo.getObjetoSeleccionado();
					ev.numPagina = new Integer(lienzo.getPaginaActual() - 1);
					lienzo.borrarObjeto(lienzo.getObjetoSeleccionado(), lienzo
							.getPaginaActual() - 1);
					ev.path = new String(lienzo.getDocumento().getPath());
					lienzo.enviarEvento(ev);
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
					lienzo.setEstaSeleccionando(botonSeleccionar.isSelected());
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
			botonAnterior.setIcon(new ImageIcon("Resources/arrow_left.png"));
			botonAnterior.setToolTipText("Seleccionar el elemento anterior");
			botonAnterior.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					lienzo.seleccionarAnteriorAnotacion();
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
					.setIcon(new ImageIcon("Resources/arrow_right.png"));
			botonSiguiente.setToolTipText("Seleccionar el siguiente elemento");
			botonSiguiente.setPreferredSize(new Dimension(32,24));
			botonSiguiente
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							lienzo.seleccionarSiguienteAnotacion();
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
			botonRehacer.setIcon(new ImageIcon("Resources/arrow_redo.png"));
			botonRehacer.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					lienzo.rehacer();
					DJLienzoEvent ev = new DJLienzoEvent();
					ev.tipo = DJLienzoEvent.REHACER;
					ev.path = new String(lienzo.getDocumento().getPath());
					lienzo.enviarEvento(ev);
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
			jLabel1.setIcon(new ImageIcon("Resources/pencil.png"));
			jLabel1.setText(" ");

			Separador separator1 = new Separador();
			Separador separator2 = new Separador();
			Separador separator3 = new Separador();
			Separador separator4 = new Separador();

			barraHerramientas = new JToolBar();
			barraHerramientas.setFloatable(false);
			barraHerramientas.setPreferredSize(new Dimension(533, 35));
			barraHerramientas.add(getBotonAbrir());
			barraHerramientas.add(getBotonGuardar());
			barraHerramientas.add(getBotonGuardarLocal());
			barraHerramientas.add(getBotonImprimir());
			barraHerramientas.add(getSeparator41());
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
	 * This method initializes separator41
	 * 
	 * @return util.Separador
	 */
	private Separador getSeparator41()
	{
		if (separator41 == null) separator41 = new Separador();
		return separator41;
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
					.setIcon(new ImageIcon("Resources/palette.png"));
			botonPaletaColores.setText("Colores   ");
			botonPaletaColores.setToolTipText("Cambia el color que usara para pintar");
			botonPaletaColores
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							Color c = elegirColor();

							if (c != null) lienzo.setColor(c);
						}
					});
		}
		return botonPaletaColores;
	}

	/**
	 * This method initializes botonCargarImagen11
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonAbrir()
	{
		if (botonAbrir == null)
		{
			botonAbrir = new JButton();
			botonAbrir.setBorder(null);
			botonAbrir.setToolTipText("Carga la imagen de fondo");
			botonAbrir.setBorderPainted(false);
			botonAbrir.setIcon(new ImageIcon("Resources/folder-open_16x16.png"));
			botonAbrir.setPreferredSize(new Dimension(35, 24));
			botonAbrir.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{

					f = SelectorFicherosDistribuido.getDatosFichero(
							lienzo.getPadre(), DConector.raiz);

					if (( f != null ) && !f.getRutaLocal().equals(""))
					{
					
						m.notificarAbrir();
					}

				}
			});
		}
		return botonAbrir;
	}

	private void accionAbrir() {
		
		// cerramos la ventana
		if (lienzo.getPadre() != null) {
			lienzo.getPadre().dispose();
			lienzo.getPadre().this_windowClosing(null);
		}
		
		// sincronizamos el lienzo
		lienzo.getDocumento().setPath(f.getRutaLocal());
		lienzo.sincronizar();
		
		// volvemos a mostrar la ventana
		if (lienzo.getPadre()!=null)
			lienzo.getPadre().setVisible(true);
	}
	
	/**
	 * This method initializes botonCargarImagen1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonGuardar()
	{
		if (botonGuardar == null)
		{
			botonGuardar = new JButton();
			botonGuardar.setBorder(null);
			botonGuardar.setToolTipText("Guarda el documento en la red");
			botonGuardar.setBorderPainted(false);
			botonGuardar.setIcon(new ImageIcon("Resources/disk_share.png"));
			botonGuardar.setPreferredSize(new Dimension(35, 24));

			botonGuardar.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{

					MIDocumento f = lienzo.getDocumento().getDatosBD();
					if (f.comprobarPermisos(DConector.Dusuario, DConector.Drol,
							MIDocumento.PERMISO_ESCRITURA))
					{
						if (!( new Transfer(ClienteFicheros.ipConexion, "") )
								.sendDocumento(lienzo.getDocumento()))
							JOptionPane
									.showMessageDialog(null,
											"Error: no se ha podido guardar el documento en el servidor");
					}
					else JOptionPane
							.showMessageDialog(null,
									"No tiene suficientes permisos para guardar el documento");

				}
			});
		}
		return botonGuardar;
	}

	/**
	 * This method initializes botonImprimir
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonImprimir()
	{
		if (botonImprimir == null)
		{
			botonImprimir = new JButton();
			botonImprimir.setBorder(null);
			botonImprimir.setToolTipText("Imprime el documento");
			botonImprimir.setBorderPainted(false);
			botonImprimir.setIcon(new ImageIcon("Resources/printer.png"));
			botonImprimir.setText("");
			botonImprimir.setPreferredSize(new Dimension(35, 24));
			botonImprimir.addActionListener(new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					lienzo.getDocumento().imprimir();
				}
			});
		}
		return botonImprimir;
	}

	/**
	 * This method initializes botonGuardarLocal
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBotonGuardarLocal()
	{
		if (botonGuardarLocal == null)
		{
			botonGuardarLocal = new JButton();
			botonGuardarLocal.setBorder(null);
			botonGuardarLocal
					.setToolTipText("Guarda el documento actual localmente");
			botonGuardarLocal.setBorderPainted(false);
			botonGuardarLocal.setIcon(new ImageIcon("Resources/disk_local.png"));
			botonGuardarLocal.setPreferredSize(new Dimension(35, 24));
			botonGuardarLocal
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(java.awt.event.ActionEvent e)
						{
							JFileChooser jfc = new JFileChooser(
									"Guardar Documento Localmente");

							int op = jfc.showDialog(null, "Aceptar");

							if (op == JFileChooser.APPROVE_OPTION)
							{
								java.io.File f = jfc.getSelectedFile();
								Documento d = lienzo.getDocumento();

								Transfer t = new Transfer(
										ClienteFicheros.ipConexion, d.getPath());

								byte[] datos = t.receiveFileBytes();

								try
								{
									RandomAccessFile acf = new RandomAccessFile(
											f.getAbsolutePath(), "rw");

									acf.write(datos);

									acf.close();
									Documento.saveDocument(d, f
											.getAbsolutePath());
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
					});
		}
		return botonGuardarLocal;
	}
	
	
//	 ============= HEBRAS
	// ===================================================================
	/**
	 * Hebra que se encarga de abrir los documentos
	 * 
	 * @author anab
	 */
	private class HebraAbrir implements Runnable
	{

		public HebraAbrir()
		{
			Thread hebra = new Thread(this);
			hebra.start();
		}

		public void run()
		{
			VentanaCarga v = new VentanaCarga();
			
			while (true)
			{

				
				// esperamos a que se solicite la lectura
				m.abrir();
				
				v.mostrar("Abriendo...", "Abriendo el fichero " + f.getNombre(), true);
				// abrimos el documento
				accionAbrir();
				v.ocultar();
			}

		}

	}
	
	
	/**
	 * Monitor que controla la apertura de documentos
	 */
	private class MonitorAbrir
	{

		public synchronized void abrir()
		{
			try
			{
				wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

		public synchronized void notificarAbrir()
		{
			notifyAll();
		}
	}

} // @jve:decl-index=0:visual-constraint="-78,19"
