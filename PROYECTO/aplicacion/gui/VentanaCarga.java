package aplicacion.gui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class VentanaCarga extends JFrame
{

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel etiquetaIcono = null; 
		
	protected ImageIcon icono1 = null;
	protected ImageIcon icono2 = null;
	protected ImageIcon icono3 = null;
	
	//private Monitor monitor = new Monitor();

	/**
	 * @param owner
	 */
	public VentanaCarga(  )
	{
		super();
		
		initialize();
		//pack();
		Thread hp = new Thread(new HebraParpadeo(this));
		
		hp.start();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(519, 85);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);  // Generated
		this.setContentPane(getJContentPane());
		icono1 = new ImageIcon("./Resources/logo_1.png");
		icono2 = new ImageIcon("./Resources/logo_2.png");
		icono3 = new ImageIcon("./Resources/logo_3.png");
	}

	
	/**
	 * Muesta el dialogo de carga
	 * @param mensaje
	 * @param parpadeo
	 */
	public void mostrar(String titulo, String mensaje, boolean parpadeo)
	{
		setTitle(titulo);
		etiquetaIcono.setText(mensaje);
		etiquetaIcono.setIcon(icono3);
		if (!isVisible())
		{
			//this.setLocation(x, y);
			this.setVisible(true);
		}
	}

	public void ocultar()
	{
		this.setVisible(false);
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setHgap(9);  // Generated
			borderLayout.setVgap(9);  // Generated
			etiquetaIcono = new JLabel();
			etiquetaIcono.setText("Mensaje...");  // Generated
			etiquetaIcono.setIcon(new ImageIcon(getClass().getResource("/Resources/logo_1.png")));  // Generated
			jContentPane = new JPanel();
			jContentPane.setLayout(borderLayout);  // Generated
			jContentPane.add(etiquetaIcono, BorderLayout.CENTER);  // Generated
		}
		return jContentPane;
	}
	private class HebraParpadeo implements Runnable
	{
		VentanaCarga vc  = null;
		
		public HebraParpadeo(VentanaCarga v){
			vc = v;
		}
		
		public void run()
		{
			while (true)
				try
				{

					//System.out.println("Iniciada hebra parpadeo");
					
					vc.etiquetaIcono.setIcon(icono1);
					vc.repaint();
					Thread.sleep(150L);
					
					vc.etiquetaIcono.setIcon(icono2);
					vc.repaint();
					Thread.sleep(150L);
					
					vc.etiquetaIcono.setIcon(icono3);
					vc.repaint();
					Thread.sleep(150L);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

		}
	}

	
	public static void main(String[] args){
		VentanaCarga v = new VentanaCarga();
		
		v.mostrar("","hola", true);
		
		try
		{
			Thread.sleep(10000L);
			
			System.out.println("Ahora salicmos");
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		v.ocultar();
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
