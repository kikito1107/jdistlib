package aplicacion.plugin.example.calculadora;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.KeyEvent;

/**
 * Calculadora sencilla
 */
public class Calc extends Applet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7918703585424936374L;

	Display display = new Display();

	/**
	 * inicializa el applet Calc
	 */
	public void init()
	{

		setLayout(new BorderLayout());
		Keypad keypad = new Keypad(display);

		add("North", display);
		add("Center", keypad);
	}

	/**
	 * manipulador para las funciones de la calculadora.
	 */
	public boolean action(Event ev, Object arg)
	{

		if (ev.target instanceof Button)
		{

			String label = (String) arg;
			if (label.equals("C"))
			{
				display.Clear();
				return true;
			}
			else if (label.equals("."))
			{
				display.Dot();
				return true;
			}
			else if (label.equals("+"))
			{
				display.Plus();
				return true;
			}
			else if (label.equals("-"))
			{
				display.Minus();
				return true;
			}
			else if (label.equals("x"))
			{
				display.Mul();
				return true;
			}
			else if (label.equals("/"))
			{
				display.Div();
				return true;
			}
			else if (label.equals("+/-"))
			{
				display.Chs();
				return true;
			}
			else if (label.equals("="))
			{
				display.Equals();
				return true;
			}
			else
			{
				display.Digit(label);
				return true;
			}
		}
		return false;
	}

	/**
	 * Esto permite que la clase se use como applet o como aplicaci�n
	 * independiente
	 */
	public static void main(String args[])
	{

		Frame f = new Frame("Calculator");
		Calc calc = new Calc();

		calc.init();

		f.setSize(210, 200);
		f.add("Center", calc);
		f.setVisible(true);
	}
}

/* -------------------------------------------------- */

/**
 * Keypad manipula la entrada de datos y la visualizaci�n de resultados
 */
class Keypad extends Panel
{
	private static final long serialVersionUID = -2027493051307560236L;
	Display d;
	
	/**
	 * inicializa keypad, a�ade los botones, establece los colores, etc.
	 * @param display 
	 */
	Keypad(Display display)
	{

		Button b = new Button();
		Font font = new Font("Arial", Font.BOLD, 14);
		Color functionColor = new Color(255, 255, 0);
		Color numberColor = new Color(0, 255, 255);
		Color equalsColor = new Color(0, 255, 0);
		setFont(font);
		b.setForeground(Color.black);
		d = display;
		
		this.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});

		
		b = new Button("7");
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		add(b);
		b.setBackground(numberColor);
		
		
		b = new Button("8");
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		add(b);
		b.setBackground(numberColor);
		
		
		b = new Button("9");
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		add(b);
		b.setBackground(numberColor);
		
		
		

		add(b = new Button("/"));
		b.setBackground(functionColor);		
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		
		add(b = new Button("4"));
		b.setBackground(numberColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		
		add(b = new Button("5"));
		b.setBackground(numberColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		
		add(b = new Button("6"));
		b.setBackground(numberColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		
		add(b = new Button("x"));
		b.setBackground(functionColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		

		add(b = new Button("1"));
		b.setBackground(numberColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		add(b = new Button("2"));
		b.setBackground(numberColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		
		add(b = new Button("3"));
		b.setBackground(numberColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		
		add(b = new Button("-"));
		b.setBackground(functionColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		
		add(b = new Button("."));
		b.setBackground(functionColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		add(b = new Button("0"));
		b.setBackground(numberColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		add(b = new Button("+/-"));
		b.setBackground(functionColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		add(b = new Button("+"));
		b.setBackground(functionColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});

		add(b = new Button("C"));
		b.setBackground(functionColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});
		
		add(new Label(""));
		add(new Label(""));
		add(b = new Button("="));
		b.setBackground(equalsColor);
		b.setBackground(functionColor);
		b.addKeyListener(new java.awt.event.KeyAdapter()
		{
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				funcionTecla(e);
			}
		});

		setLayout(new GridLayout(5, 4, 4, 4));
	}
	
	
	private void funcionTecla(KeyEvent e){
		switch( e.getKeyChar() ){
			case '1':
				d.Digit("1");
				break;
			case '2':
				d.Digit("2");
				break;
			case '3':
				d.Digit("3");
				break;
			case '4':
				d.Digit("4");
				break;
			case '5':
				d.Digit("5");
				break;
			case '6':
				d.Digit("6");
				break;
			case '7':
				d.Digit("7");
				break;
			case '8':
				d.Digit("8");
				break;
			case '9':
				d.Digit("9");
				break;
			case '0':
				d.Digit("0");
				break;
			case '.':
				d.Dot();
				break;
			case '+':
				d.Plus();
				break;
			case '-':
				d.Minus();
				break;
			case 'x':
			case '*':
				d.Mul();
				break;
			case '/':
				d.Div();
				break;
			case '=':
			case '\n':
				d.Equals();
				break;
		}
	}

}

/* -------------------------------------------------- */

/**
 * la clase Display gestiona la visualizaci�n del resultado calculado y tambi�n
 * implementa las tecla de fucni�n de la calculadora
 */
class Display extends Panel
{
	private static final long serialVersionUID = 4565678797237286940L;

	double last = 0;

	int op = 0;

	boolean equals = false;

	int maxlen = 10;

	String s;

	Label readout = new Label("");

	/**
	 * Inicializa display
	 */
	Display()
	{

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		setFont(new Font("Courier New", Font.BOLD + Font.ITALIC, 30));
		readout.setAlignment(1);
		add("Center", readout);
		repaint();
		Clear();
	}

	/**
	 * manipulaci�n del pulsado de un d�gito
	 */
	void Digit(String digit)
	{
		checkEquals();

		/*
		 * quita los ceros precedentes
		 */
		if (s.length() == 1 && s.charAt(0) == '0' && digit.charAt(0) != '.')
			s = s.substring(1);

		if (s.length() < maxlen) s = s + digit;
		showacc();
	}

	/**
	 * manipulaci�n del punto decimal
	 */
	void Dot()
	{
		checkEquals();

		/*
		 * ya tiene un '.'
		 */
		if (s.indexOf('.') != -1) return;

		if (s.length() < maxlen) s = s + ".";
		showacc();
	}

	/**
	 * si el usuario pulsa = sin haber pulsado antes un operador (+,-,x,/), pone
	 * cero en la visualizaci�n
	 */
	private void checkEquals()
	{
		if (equals == true)
		{
			equals = false;
			s = "0";
		}
	}

	/**
	 * operador suma para uso posterior.
	 */
	void Plus()
	{
		op = 1;
		operation();
	}

	/**
	 * operador resta para uso posterior.
	 */
	void Minus()
	{
		op = 2;
		operation();
	}

	/**
	 * operador multiplicaci�n para uso posterior.
	 */
	void Mul()
	{
		op = 3;
		operation();
	}

	/**
	 * operador divisi�n para uso posterior.
	 */
	void Div()
	{
		op = 4;
		operation();
	}

	/**
	 * Interpreta el valor de la visualizaci�n como double y lo almacena para
	 * uso posterior
	 */
	private void operation()
	{
		if (s.length() == 0) return;

		Double xyz = Double.valueOf(s);
		last = xyz.doubleValue();

		equals = false;
		s = "0";
	}

	/**
	 * invalida el valor actual y revisualiza.
	 */
	void Chs()
	{
		if (s.length() == 0) return;

		if (s.charAt(0) == '-')
			s = s.substring(1);
		else s = "-" + s;

		showacc();
	}

	/**
	 * termina el �ltimo c�lculo y visualiza el resultado
	 */
	void Equals()
	{
		double acc;

		if (s.length() == 0) return;
		Double xyz = Double.valueOf(s);
		switch (op)
		{
			case 1:
				acc = last + xyz.doubleValue();
				break;

			case 2:
				acc = last - xyz.doubleValue();
				break;

			case 3:
				acc = last * xyz.doubleValue();
				break;

			case 4:
				acc = last / xyz.doubleValue();
				break;

			default:
				acc = 0;
				break;
		}

		s = new Double(acc).toString();
		showacc();
		equals = true;
		last = 0;
		op = 0;
	}

	/**
	 * limpia la visualizaci�n y el �ltimo valor interno 
	 */
	void Clear()
	{
		last = 0;
		op = 0;
		s = "0";
		equals = false;
		showacc();
	}

	/**
	 * pide que se repinte el resultado
	 */
	private void showacc()
	{
		readout.setText(s);
		repaint();
	}
}
