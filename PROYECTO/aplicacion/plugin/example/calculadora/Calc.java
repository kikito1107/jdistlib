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
		Keypad keypad = new Keypad();

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
	 * Esto permite que la clase se use como applet o como aplicación
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
 * Keypad manipula la entrada de datos y la visualización de resultados
 */
class Keypad extends Panel
{

	/**
	 * inicializa keypad, añade los botones, establece los colores, etc.
	 */
	Keypad()
	{

		Button b = new Button();
		Font font = new Font("Arial", Font.BOLD, 14);
		Color functionColor = new Color(255, 255, 0);
		Color numberColor = new Color(0, 255, 255);
		Color equalsColor = new Color(0, 255, 0);
		setFont(font);
		b.setForeground(Color.black);

		add(b = new Button("7"));
		b.setBackground(numberColor);
		add(b = new Button("8"));
		b.setBackground(numberColor);
		add(b = new Button("9"));
		b.setBackground(numberColor);
		add(b = new Button("/"));
		b.setBackground(functionColor);

		add(b = new Button("4"));
		b.setBackground(numberColor);
		add(b = new Button("5"));
		b.setBackground(numberColor);
		add(b = new Button("6"));
		b.setBackground(numberColor);
		add(b = new Button("x"));
		b.setBackground(functionColor);

		add(b = new Button("1"));
		b.setBackground(numberColor);
		add(b = new Button("2"));
		b.setBackground(numberColor);
		add(b = new Button("3"));
		b.setBackground(numberColor);
		add(b = new Button("-"));
		b.setBackground(functionColor);

		add(b = new Button("."));
		b.setBackground(functionColor);
		add(b = new Button("0"));
		b.setBackground(numberColor);
		add(b = new Button("+/-"));
		b.setBackground(functionColor);
		add(b = new Button("+"));
		b.setBackground(functionColor);

		add(b = new Button("C"));
		b.setBackground(functionColor);
		add(new Label(""));
		add(new Label(""));
		add(b = new Button("="));
		b.setBackground(equalsColor);

		setLayout(new GridLayout(5, 4, 4, 4));
	}

}

/* -------------------------------------------------- */

/**
 * la clase Display gestiona la visualización del resultado calculado y también
 * implementa las tecla de fucnión de la calculadora
 */
class Display extends Panel
{

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
	 * manipulación del pulsado de un dígito
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
	 * manipulación del punto decimal
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
	 * cero en la visualización
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
	 * operador multiplicación para uso posterior.
	 */
	void Mul()
	{
		op = 3;
		operation();
	}

	/**
	 * operador división para uso posterior.
	 */
	void Div()
	{
		op = 4;
		operation();
	}

	/**
	 * Interpreta el valor de la visualización como double y lo almacena para
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
	 * termina el último cálculo y visualiza el resultado
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
	 * limpia la visualización y el último valor interno 
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
