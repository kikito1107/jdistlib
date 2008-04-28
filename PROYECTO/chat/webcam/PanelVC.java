package chat.webcam;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import chat.webcam.VideoFrame.Hebra;
import chat.webcam.VideoFrame.HebraMiWebcam;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.ImageIcon;

public class PanelVC extends JPanel
{

	private static final long serialVersionUID = 1L;
	private JLabel Lable = null;
	ImageComponent img_remota = null;
    ImageComponent img_local = null;
    boolean init = false;
    String ip = null;
    Hebra th = new Hebra();
    HebraMiWebcam th2 = new HebraMiWebcam();
    private javax.swing.JButton ini_stop;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel myWebcam;
    private javax.swing.JPanel webcamPanel;
	private JButton Capturar = null;
	/**
	 * This is the default constructor
	 */
	public PanelVC(JFrame ventana, String ip)
	{
		super();
		img_remota = new ImageComponent(ventana);
	    img_local = new ImageComponent(ventana);
		initialize();
		
		this.ip = ip;
        this.webcamPanel.add(img_remota);
        this.myWebcam.add(img_local);
        
        VideoConferencia.stopped = true;
        
        th2.start();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		webcamPanel = new javax.swing.JPanel();
        myWebcam = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        ini_stop = new javax.swing.JButton();

        webcamPanel.setBackground(new java.awt.Color(102, 102, 102));

        myWebcam.setBackground(new java.awt.Color(0, 0, 0));
        myWebcam.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                myWebcamMouseDragged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout myWebcamLayout = new org.jdesktop.layout.GroupLayout(myWebcam);
        myWebcam.setLayout(myWebcamLayout);
        myWebcamLayout.setHorizontalGroup(
            myWebcamLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 132, Short.MAX_VALUE)
        );
        myWebcamLayout.setVerticalGroup(
            myWebcamLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 102, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout webcamPanelLayout = new org.jdesktop.layout.GroupLayout(webcamPanel);
        webcamPanel.setLayout(webcamPanelLayout);
        webcamPanelLayout.setHorizontalGroup(
            webcamPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(webcamPanelLayout.createSequentialGroup()
                .add(myWebcam, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(410, Short.MAX_VALUE))
        );
        webcamPanelLayout.setVerticalGroup(
            webcamPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, webcamPanelLayout.createSequentialGroup()
                .addContainerGap(319, Short.MAX_VALUE)
                .add(myWebcam, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        jButton1.setText("Cheese!");
        jButton1.setIcon(new ImageIcon(getClass().getResource("/Resources/camera_16x16.png")));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        ini_stop.setText("Iniciar");
        ini_stop.setIcon(new ImageIcon(getClass().getResource("/Resources/control_play_blue.png")));
        ini_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ini_stopActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        this.add(getCapturar(), null);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(29, 29, 29)
                .add(ini_stop)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 299, Short.MAX_VALUE)
                .add(jButton1)
                .add(60, 60, 60))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, webcamPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(webcamPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1)
                    .add(ini_stop))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        

	}
	
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ini_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ini_stopActionPerformed
        if (init)
        {
            try
            {
                init = false;
                this.ini_stop.setText("Iniciar");
                ini_stop.setIcon(new ImageIcon(getClass().getResource("/Resources/control_play_blue.png")));
                VideoConferencia.stopped = true;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            try
            {
                VideoConferencia.stopped = false;
                th = new Hebra();
                th.start();
                init = true;
                this.ini_stop.setText("Detener");
                ini_stop.setIcon(new ImageIcon(getClass().getResource("/Resources/control_pause_blue.png")));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_ini_stopActionPerformed

    private void myWebcamMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myWebcamMouseDragged
        
        int x = evt.getX()+myWebcam.getX();
        int y = evt.getY()+myWebcam.getY();
        
        if (x < 0)
            x = 0;
        
        if (y < 0)
            y = 0;
        
        if (x+myWebcam.getWidth() > webcamPanel.getWidth())
            x = webcamPanel.getWidth()-myWebcam.getWidth();
        
        if (y+myWebcam.getHeight() > webcamPanel.getHeight())
            y = webcamPanel.getHeight()-myWebcam.getHeight();
        
        
        myWebcam.setLocation(x, y);
    }//GEN-LAST:event_myWebcamMouseDragged


    public void setIP(String ip){
    	this.ip = ip;
    	init = false;
    	
    }
    
    public void iniciarHebra(){
    	th.start();
        th2.start();
    }
	
	class Hebra extends Thread
    {

		public void run()
        {
            VideoConferencia t = new VideoConferencia(ip);
            Image im = null;
            boolean pintado = false;
            while (true)
            {
                if (webcamPanel.getWidth() != 0 && webcamPanel.getHeight() != 0)
                {
                    try{
                        im = t.receive(webcamPanel.getWidth(), webcamPanel.getHeight()).getImage();
                    }catch(Exception ex){im = null;}
                    
                    if (im != null)
                    {
                        pintado = false;
                        img_remota.setImage(im);
                    }
                    
                    else if (!pintado && im == null)
                    {
                        pintado = true;
                        Graphics g = img_remota.getGraphics();
                        g.setColor(Color.MAGENTA);
                        g.drawString("Sin Conexion", img_remota.getWidth()/3, img_remota.getHeight()/2);
                    }
                }
            }
        }
    }

    class HebraMiWebcam extends Thread
    {
    	public void run()
        {
            //Graphics g = myWebcam.getGraphics();
            //Image img = null;
            while (true)
            {
                try
                {
                    if (myWebcam.getWidth() != 0 && myWebcam.getHeight() != 0 && VideoConferencia.image_now_local != null)
                    {
                        img_local.setImage(VideoConferencia.image_now_local.getImage().getScaledInstance(myWebcam.getWidth(), myWebcam.getHeight(), Image.SCALE_FAST));
                    }

                    Thread.sleep(500);
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

	/**
	 * This method initializes Capturar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCapturar()
	{
		if (Capturar == null)
		{
			Capturar = new JButton();
		}
		return Capturar;
	}



}
