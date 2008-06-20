package fisica.audio;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Ventana principal del chat de audio
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
public class MainAudioChatFrame
{
	public MainAudioChatFrame()
	{
		JFrame frame = new JFrame();
		frame.setTitle("Chat");
		frame.setSize(new Dimension(300, 80));
		WindowAdapter windowAdapter = new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				System.exit(0);
			}
		};
		frame.addWindowListener(windowAdapter);

		JComponent botones = new PanelEstado();
		frame.getContentPane().add(botones);

		frame.validate();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation(( screenSize.width - frameSize.width ) / 2,
				( screenSize.height - frameSize.height ) / 2);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		try
		{
			new MainAudioChatFrame();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			System.exit(1);
		}
	}
}
