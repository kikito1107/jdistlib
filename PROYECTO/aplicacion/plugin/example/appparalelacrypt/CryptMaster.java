package aplicacion.plugin.example.appparalelacrypt;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import aplicacion.plugin.DAbstractPlugin;

import calculoparalelo.GenericMaster;
import calculoparalelo.eventos.PoisonPill;


import net.jini.core.entry.Entry;

public class CryptMaster extends GenericMaster implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2196888859501206486L;

	private TextField triesTextField = new TextField();

	private TextField wordTextField = new TextField();

	private TextField highTextField = new TextField();

	private TextField lowTextField = new TextField();

	private Button startButton = new Button();

	private Label wordLabel = new Label();

	private Label encryptedLabel = new Label();

	private Label encryptedTextField = new Label();

	private Label triesLabel = new Label();

	private Label highLabel = new Label();

	private Label lowLabel = new Label();

	private Label triedLabel = new Label();

	private Label perfLabel = new Label();

	private Label waterMarkLabel = new Label();

	private Label answerLabel = new Label();

	private Label saltLabel = new Label();

	private TextField saltTextField = new TextField();

	private int lowmark;

	private int highmark;

	private int triesPerTask;

	private int waterlevel = 0;

	private boolean start = false;

	private long startTime;

	private String salt;

	private String unencrypted;
	
	private JPanel panel = new JPanel();
	
	JFrame ventana = new JFrame();

	public CryptMaster() throws Exception{
		super("", false, null);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		ventana.setSize(426, 246);
		
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		panel.setLayout(null);
		panel.setSize(426, 246);
		panel.add(triesTextField);
		triesTextField.setBounds(120, 108, 100, 24);
		panel.add(wordTextField);
		wordTextField.setBounds(120, 36, 100, 24);
		panel.add(highTextField);
		highTextField.setBounds(120, 144, 100, 24);
		panel.add(lowTextField);
		lowTextField.setBounds(120, 180, 100, 24);
		startButton.setLabel("Start");
		panel.add(startButton);
		startButton.setBackground(Color.lightGray);
		startButton.setBounds(312, 180, 84, 24);
		wordLabel.setText("Password:");
		wordLabel.setAlignment(Label.RIGHT);
		panel.add(wordLabel);
		wordLabel.setBounds(60, 36, 48, 24);
		encryptedLabel.setText("Encrypted:");
		encryptedLabel.setAlignment(Label.RIGHT);
		panel.add(encryptedLabel);
		encryptedLabel.setBounds(36, 72, 72, 24);
		panel.add(encryptedTextField);
		encryptedTextField.setBounds(120, 72, 96, 24);
		triesLabel.setText("Tries per Task:");
		triesLabel.setAlignment(Label.RIGHT);
		panel.add(triesLabel);
		triesLabel.setBounds(12, 108, 96, 24);
		highLabel.setText("High mark:");
		highLabel.setAlignment(Label.RIGHT);
		panel.add(highLabel);
		highLabel.setBounds(36, 144, 72, 24);
		lowLabel.setText("Low mark:");
		lowLabel.setAlignment(Label.RIGHT);
		panel.add(lowLabel);
		lowLabel.setBounds(12, 180, 96, 24);
		panel.add(triedLabel);
		triedLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		triedLabel.setBounds(264, 48, 120, 24);
		panel.add(perfLabel);
		perfLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		perfLabel.setBounds(264, 72, 120, 24);
		panel.add(waterMarkLabel);
		waterMarkLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		waterMarkLabel.setBounds(264, 96, 120, 24);
		panel.add(answerLabel);
		answerLabel.setForeground(Color.red);
		answerLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		answerLabel.setBounds(264, 132, 120, 24);
		saltLabel.setText("Salt:");
		saltLabel.setAlignment(java.awt.Label.RIGHT);
		panel.add(saltLabel);
		saltLabel.setBounds(60, 0, 48, 24);
		panel.add(saltTextField);
		saltTextField.setBounds(120, 0, 100, 24);

		highTextField.setText("20");
		lowTextField.setText("10");
		triesTextField.setText("1000");
		saltTextField.setText("js");
		startButton.addActionListener(this);
		
		ventana.setContentPane(panel);
	}
	
	public static void main(String[] args){
		CryptMaster m;
		try
		{
			m = new CryptMaster();
			m.setVisible(true);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void actionPerformed(ActionEvent event)
	{
		if (start)
		{
			return;
		}

		String msg = null;
		salt = saltTextField.getText();
		if (salt.equals(""))
		{
			msg = "Supply a salt value";
		}

		unencrypted = wordTextField.getText();
		if (unencrypted.equals(""))
		{
			msg = "Supply a word to encrypt";
		}

		if (unencrypted.length() != 4)
		{
			msg = "Supply a word of four characters";
		}

		// JCrypt expects 8 chars
		unencrypted = "!!!!" + unencrypted;

		try
		{
			triesPerTask = Integer.parseInt(triesTextField.getText());
		}
		catch (NumberFormatException e)
		{
			msg = "Enter an integer value in \"Tries per Task\".";
		}
		try
		{
			highmark = Integer.parseInt(highTextField.getText());
		}
		catch (NumberFormatException e)
		{
			msg = "Enter an integer value in \"High Mark\".";
		}
		try
		{
			lowmark = Integer.parseInt(lowTextField.getText());
		}
		catch (NumberFormatException e)
		{
			msg = "Enter an integer value in \"Low Mark\".";
		}

		if (msg == null)
		{
			start = true;
		}
		else
		{
			System.err.println(msg);
		}
	}

	@Override
	public void generateTasks()
	{
		while (!start)
		{
			try
			{
				Thread.sleep(250);
			}
			catch (InterruptedException e)
			{
				return; // thread told to stop
			}
		}

		startTime = System.currentTimeMillis();

		String encrypted = JCrypt.crypt(salt, unencrypted);
		encryptedTextField.setText(encrypted);

		byte[] testWord = getFirstWord();

		for (;;)
		{
			waitForLowWaterMark(lowmark);

			while (waterlevel < highmark)
			{
				if (testWord[1] != salt.charAt(1))
				{
					return;
				}
				CryptTask task = new CryptTask(triesPerTask, testWord,
						encrypted);
				System.out.println("Writing task");
				writeTask(task);
				changeWaterLevel(1);
				for (int i = 0; i < triesPerTask; i++)
				{
					CryptTask.nextWord(testWord);
				}
			}
		}
	}

	@Override
	public void collectResults()
	{
		int count = 0;
		Entry template;

		try
		{
			template = space.snapshot(new CryptResult());
		}
		catch (RemoteException e)
		{
			throw new RuntimeException("Can't create a snapshot");
		}

		for (;;)
		{
			CryptResult result = (CryptResult) takeTask(template);
			if (result != null)
			{
				count++;
				triedLabel.setText("Tried " + ( count * triesPerTask )
						+ " words.");
				updatePerformance(count * triesPerTask);
				changeWaterLevel(-1);
				if (result.word != null)
				{
					String word = CryptTask.getPrintableWord(result.word);
					answerLabel.setText("Unencrypted: " + word.substring(6));
					addPoison();
					return;
				}
			}
		}
	}

	private byte[] getFirstWord()
	{
		byte[] word = new byte[2 + 8];
		word[0] = (byte) salt.charAt(0);
		word[1] = (byte) salt.charAt(1);

		for (int i = 2; i < 6; i++)
		{
			// lowest printable char
			word[i] = (byte) '!';
		}

		word[6] = (byte) ' ';
		word[7] = (byte) ' ';
		word[8] = (byte) ' ';
		word[9] = (byte) ' ';

		return word;
	}

	private void addPoison()
	{
		PoisonPill poison = new PoisonPill();

		try
		{
			space.write(poison, null, 60 * 1000 * 5);
		}
		catch (Exception e)
		{
			// ignore, leases will eventually clear space
		}
	}

	synchronized void waitForLowWaterMark(int level)
	{
		while (waterlevel > level)
		{
			try
			{
				wait();
			}
			catch (InterruptedException e)
			{
				; // continue
			}
		}
	}

	private synchronized void changeWaterLevel(int delta)
	{
		waterlevel += delta;
		waterMarkLabel.setText("Waterlevel is at " + waterlevel + " tasks");
		notifyAll();
	}

	public void updatePerformance(long wordsTried)
	{
		long now = System.currentTimeMillis();
		
			long wordRate;
		if (now-startTime != 0)
			wordRate = (long)(wordsTried / ( ( now - startTime ) / 1000. ));
		else
			wordRate = Long.MAX_VALUE;
			
		perfLabel.setText(wordRate + " words per second.");
	}

	@Override
	public DAbstractPlugin getInstance() throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
}
