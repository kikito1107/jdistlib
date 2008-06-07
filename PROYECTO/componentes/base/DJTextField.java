package componentes.base;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import util.DCaret;
import Deventos.ColaEventos;
import Deventos.DEvent;
import Deventos.DJTextFieldEvent;
import Deventos.enlaceJS.DConector;

import componentes.listeners.DJTextFieldListener;

/**
 * Implementacion de la clase captadora de eventos para el componente TextField
 * 
 * @author Juan Antonio Iba–ez Santorum. Carlos Rodriguez Dominguez. Ana Belen
 *         Pelegrina Ortiz
 */
public class DJTextField extends JTextField
{
	private static final String uiClassID = "DTextFieldUI";

	private static final long serialVersionUID = 1L;

	private Vector<Object> djtextfieldlisteners = new Vector<Object>(5);

	private Integer DID = new Integer(-1);

	private String nombre = null;

	private ColaEventos colaRecepcion = new ColaEventos();

	private ColaEventos colaEnvio = null;

	private Integer ultimoProcesado = new Integer(-1);

	private int nivelPermisos = 10;

	public DJTextField()
	{
		this.nombre = null;
		extrasConstructor();
	}

	public DJTextField( int p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	public DJTextField( String p0 )
	{
		super(p0);
		this.nombre = null;
		extrasConstructor();
	}

	public DJTextField( String p0, int p1 )
	{
		super(p0, p1);
		this.nombre = null;
		extrasConstructor();
	}

	public DJTextField( Document p0, String p1, int p2 )
	{
		super(p0, p1, p2);
		this.nombre = null;
		extrasConstructor();
	}

	public void addDJTextFieldListener(DJTextFieldListener listener)
	{
		djtextfieldlisteners.add(listener);
	}

	public void removeDJTextFieldListeners()
	{
		djtextfieldlisteners.removeAllElements();
	}

	public Vector<Object> getDJTextFieldListeners()
	{
		return djtextfieldlisteners;
	}

	@Override
	public String getUIClassID()
	{
		return uiClassID;
	}

	private void extrasConstructor()
	{
		/*
		 * addDJTextFieldListener(new Listener()); DID = new
		 * Integer(DConector.alta(this)); colaEnvio =
		 * DConector.getColaEventos();
		 */

		this.getKeymap().setDefaultAction(new DDefaultKeyTypedAction());
		// ActionMap m = this.getActionMap();

		this.setCaret(new DCaret());

		this.getActionMap().put("delete-previous", new DDeletePrevCharAction());
		this.getActionMap().put("delete-next", new DDeleteNextCharAction());
		this.getActionMap().put("insert-content", new DInsertContentAction());
		this.getActionMap().put("cut-to-clipboard", new DCut());
		this.getActionMap().put("paste-from-clipboard", new DPaste());
	}

	public void activar()
	{
		setEnabled(true);
	}

	public void desactivar()
	{
		setEnabled(false);
	}

	public void iniciarHebraProcesadora()
	{
		Thread t = new Thread(new HebraProcesadora(colaRecepcion, this));
		t.start();
	}

	public DJTextFieldEvent obtenerInfoEstado()
	{
		DJTextFieldEvent evento = new DJTextFieldEvent();
		evento.contenido = new String(getText());
		return evento;
	}

	public void procesarEvento(DEvent ev)
	{
		DJTextFieldEvent evento = (DJTextFieldEvent) ev;
		if (evento.tipo.intValue() == DJTextFieldEvent.REMOVE.intValue()) try
		{
			int longitud = getText().length();

			int p1 = evento.p1.intValue(); // Posicion
			int p2 = evento.p2.intValue(); // Longitud
			if (( p1 + 1 ) > longitud)
			{
				//
			}
			else
			{
				if (( p1 + p2 + 1 ) > longitud) p2 = longitud - p1;

				/*
				 * System.out.println("Remove: longitud=" + evento.p2.intValue() + "
				 * posicion=" + evento.p1.intValue());
				 */
				getDocument().remove(p1, p2);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (evento.tipo.intValue() == DJTextFieldEvent.REPLACE.intValue())
			try
			{
				int longitud = getText().length();
				int p1 = evento.p1.intValue();
				int p2 = evento.p2.intValue();

				if (!( ( p1 ) > longitud )) // System.out.println("replace " +
					// p1 + " " + p2);
					( (AbstractDocument) getDocument() ).replace(p1, p2,
							evento.contenido, null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		if (evento.tipo.intValue() == DJTextFieldEvent.INSERT.intValue()) try
		{
			int p1 = evento.p1.intValue();
			int longitud = getText().length();
			if (p1 > ( longitud - 1 )) p1 = longitud - 1;
			getDocument().insertString(p1, evento.contenido, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void sincronizar()
	{
		DJTextFieldEvent evento = new DJTextFieldEvent();
		evento.origen = new Integer(1); // Aplicacion
		evento.destino = new Integer(0); // Coordinador
		evento.componente = new Integer(DID.intValue());
		evento.tipo = new Integer(DJTextFieldEvent.SINCRONIZACION.intValue());
		evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

		colaEnvio.nuevoEvento(evento);
	}

	public int getNivelPermisos()
	{
		return nivelPermisos;
	}

	public void setNivelPermisos(int nivel)
	{
		if (!isEnabled()) setEnabled(true);
		nivelPermisos = nivel;
		if (nivel >= 20)
			setForeground(Color.BLACK);
		else setForeground(Color.GRAY);
	}

	public Integer getID()
	{
		return DID;
	}

	public String getNombre()
	{
		return nombre;
	}

	public ColaEventos obtenerColaRecepcion()
	{
		return colaRecepcion;
	}

	public ColaEventos obtenerColaEnvio()
	{
		return colaEnvio;
	}

	public HebraProcesadoraBase crearHebraProcesadora()
	{
		return null;
	}

	@SuppressWarnings( "unused" )
	private class Listener implements DJTextFieldListener
	{
		public void replace(DJTextFieldEvent evento)
		{
			if (nivelPermisos >= 20)
			{
				evento.componente = new Integer(DID.intValue());
				evento.origen = new Integer(1); // Aplicacion
				evento.destino = new Integer(0); // Coordinador
				evento.componente = new Integer(DID.intValue());
				evento.tipo = new Integer(DJTextFieldEvent.REPLACE.intValue());
				evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

				colaEnvio.nuevoEvento(evento);
			}
		}

		public void remove(DJTextFieldEvent evento)
		{
			if (nivelPermisos >= 20)
			{
				evento.componente = new Integer(DID.intValue());
				evento.origen = new Integer(1); // Aplicacion
				evento.destino = new Integer(0); // Coordinador
				evento.componente = new Integer(DID.intValue());
				evento.tipo = new Integer(DJTextFieldEvent.REMOVE.intValue());
				evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

				colaEnvio.nuevoEvento(evento);
			}
		}

		public void insert(DJTextFieldEvent evento)
		{
			if (nivelPermisos >= 20)
			{
				evento.componente = new Integer(DID.intValue());
				evento.origen = new Integer(1); // Aplicacion
				evento.destino = new Integer(0); // Coordinador
				evento.componente = new Integer(DID.intValue());
				evento.tipo = new Integer(DJTextFieldEvent.INSERT.intValue());
				evento.ultimoProcesado = new Integer(ultimoProcesado.intValue());

				colaEnvio.nuevoEvento(evento);
			}
		}
	}

	class HebraProcesadora implements Runnable
	{

		ColaEventos cola = null;

		DJTextField campo = null;

		HebraProcesadora( ColaEventos cola, DJTextField campo )
		{
			this.cola = cola;
			this.campo = campo;
		}

		public void run()
		{
			DJTextFieldEvent evento = null;
			DJTextFieldEvent saux = null;
			DJTextFieldEvent respSincr = null;
			ColaEventos colaAux = new ColaEventos();

			int numEventos = colaRecepcion.tamanio(); // Para evitar quedarnos
			// bloqueados
			// int i = 0;
			// int posicion = -1;
			// boolean encontradaRespuestaSincronizacion = false;

			// Buscamos si se ha recibido una respuesta de sincronizacion
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJTextFieldEvent) colaRecepcion.extraerEvento();
				if (( respSincr == null )
						&& ( saux.tipo.intValue() == DJTextFieldEvent.RESPUESTA_SINCRONIZACION
								.intValue() ))
					respSincr = saux;
				else colaAux.nuevoEvento(saux);
			}

			if (respSincr != null)
			{ // Se ha recibido respuesta de sincronizacion
				ultimoProcesado = new Integer(respSincr.ultimoProcesado
						.intValue());
				campo.setText(respSincr.contenido);
			}

			// Colocamos en la cola de recepcion los eventos que deben ser
			// procesados (recibidos mientras se realizaba la sincronizacion )
			numEventos = colaAux.tamanio();
			for (int j = 0; j < numEventos; j++)
			{
				saux = (DJTextFieldEvent) colaAux.extraerEvento();
				if (saux.ultimoProcesado.intValue() > ultimoProcesado
						.intValue()) colaRecepcion.nuevoEvento(saux);
			}

			// campo.setEnabled(true);

			while (true)
			{
				evento = (DJTextFieldEvent) cola.extraerEvento();
				ultimoProcesado = new Integer(evento.contador.intValue());
				if (nivelPermisos >= 10)
				{
					/*
					 * System.out.println("HebraProcesadora(" + DID + "):
					 * Procesado: Tipo=" + evento.tipo + " Ult. Proc.=" +
					 * evento.ultimoProcesado);
					 */
					if (( evento.tipo.intValue() == DJTextFieldEvent.SINCRONIZACION
							.intValue() )
							&& !evento.usuario.equals(DConector.Dusuario))
					{
						DJTextFieldEvent aux = new DJTextFieldEvent();
						aux.origen = new Integer(1); // Aplicacion
						aux.destino = new Integer(0); // Coordinador
						aux.componente = new Integer(DID.intValue());
						aux.tipo = new Integer(
								DJTextFieldEvent.RESPUESTA_SINCRONIZACION
										.intValue());
						aux.ultimoProcesado = new Integer(ultimoProcesado
								.intValue());
						aux.contenido = new String(campo.getText());

						colaEnvio.nuevoEvento(aux);
					}
					if (evento.tipo.intValue() == DJTextFieldEvent.REMOVE
							.intValue()) try
					{
						int longitud = campo.getText().length();

						int p1 = evento.p1.intValue(); // Posicion
						int p2 = evento.p2.intValue(); // Longitud
						if (( p1 + 1 ) > longitud)
						{
							//
						}
						else
						{
							if (( p1 + p2 + 1 ) > longitud) p2 = longitud - p1;

							/*
							 * System.out.println("Remove: longitud=" +
							 * evento.p2.intValue() + " posicion=" +
							 * evento.p1.intValue());
							 */
							campo.getDocument().remove(p1, p2);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					if (evento.tipo.intValue() == DJTextFieldEvent.REPLACE
							.intValue())
						try
						{
							int longitud = campo.getText().length();
							int p1 = evento.p1.intValue();
							int p2 = evento.p2.intValue();

							if (!( ( p1 ) > longitud ))
								/*
								 * System.out.println("replace " + p1 + " " +
								 * p2);
								 */
								( (AbstractDocument) campo.getDocument() )
										.replace(p1, p2, evento.contenido, null);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					if (evento.tipo.intValue() == DJTextFieldEvent.INSERT
							.intValue())
						try
						{
							int p1 = evento.p1.intValue();
							int longitud = campo.getText().length();
							if (p1 > ( longitud - 1 )) p1 = longitud - 1;
							campo.getDocument().insertString(p1,
									evento.contenido, null);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
				}
			}
		}
	}

	// *****************************************************************************
	// ************ CLASES SUSTITUTORIAS DE LAS ACCIONES MODIFICADAS
	// ***************
	// *****************************************************************************
	private class DDefaultKeyTypedAction extends TextAction
	{

		private static final long serialVersionUID = 3L;

		DDefaultKeyTypedAction()
		{
			super("default-typed");
		}

		public void actionPerformed(ActionEvent e)
		{
			JTextComponent target = getTextComponent(e);
			if (( target != null ) && ( e != null ))
			{
				if (( !target.isEditable() ) || ( !target.isEnabled() ))
					return;
				String content = e.getActionCommand();
				int mod = e.getModifiers();
				if (( content != null )
						&& ( content.length() > 0 )
						&& ( ( mod & ActionEvent.ALT_MASK ) == ( mod & ActionEvent.CTRL_MASK ) ))
				{
					char c = content.charAt(0);
					if (( c >= 0x20 ) && ( c != 0x7F ))
					{
						// target.replaceSelection(content);
						Document doc = getDocument();
						if (doc != null)
							try
							{
								int p0 = Math.min(getCaret().getDot(),
										getCaret().getMark());
								int p1 = Math.max(getCaret().getDot(),
										getCaret().getMark());
								if (doc instanceof AbstractDocument)
								{
									// ( (AbstractDocument) doc).replace(p0, p1
									// - p0, content, null);
									// ************ NOTIFICAMOS EL EVENTO
									// *****************
									Vector<Object> v = getDJTextFieldListeners();
									DJTextFieldEvent ev = new DJTextFieldEvent();
									ev.p1 = new Integer(p0);
									ev.p2 = new Integer(p1 - p0);
									ev.contenido = new String(content);
									ev.tipo = new Integer(
											DJTextFieldEvent.REPLACE.intValue());
									// System.out.println("replace " + ev.p1 + "
									// " + ev.p2);
									for (int i = 0; i < v.size(); i++)
										( (DJTextFieldListener) v.elementAt(i) )
												.replace(ev);
								}
								else
								{
									if (p0 != p1)
									{
										// doc.remove(p0, p1 - p0);
										// ************ NOTIFICAMOS EL EVENTO
										// *****************
										Vector<Object> v = getDJTextFieldListeners();
										DJTextFieldEvent ev = new DJTextFieldEvent();
										ev.p1 = new Integer(p0);
										ev.p2 = new Integer(p1 - p0);
										ev.tipo = new Integer(
												DJTextFieldEvent.REMOVE
														.intValue());
										for (int i = 0; i < v.size(); i++)
											( (DJTextFieldListener) v
													.elementAt(i) ).remove(ev);
									}
									if (( content != null )
											&& ( content.length() > 0 ))
									{
										// doc.insertString(p0, content, null);
										// ************ NOTIFICAMOS EL EVENTO
										// *****************
										Vector<Object> v = getDJTextFieldListeners();
										DJTextFieldEvent ev = new DJTextFieldEvent();
										ev.p1 = new Integer(p0);
										ev.contenido = new String(content);
										ev.tipo = new Integer(
												DJTextFieldEvent.INSERT
														.intValue());
										for (int i = 0; i < v.size(); i++)
											( (DJTextFieldListener) v
													.elementAt(i) ).insert(ev);
									}
								}
							}
							catch (Exception ef)
							{
								// UIManager.getLookAndFeel().provideErrorFeedback(JTextComponent.this);
							}
					}
				}
			}
		}
	}

	private class DDeletePrevCharAction extends TextAction
	{

		private static final long serialVersionUID = 5L;

		DDeletePrevCharAction()
		{
			super("delete-previous");
		}

		public void actionPerformed(ActionEvent e)
		{
			JTextComponent target = getTextComponent(e);
			boolean beep = true;
			if (( target != null ) && ( target.isEditable() )) try
			{
				Document doc = target.getDocument();
				Caret caret = target.getCaret();
				int dot = caret.getDot();
				int mark = caret.getMark();
				if (dot != mark)
				{
					// doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
				beep = false;
				// ************ NOTIFICAMOS EL EVENTO *****************
				int p0 = Math.min(getCaret().getDot(), getCaret().getMark());
				int p1 = Math.max(getCaret().getDot(), getCaret().getMark());
				// System.out.println("**" + p0 + " " + p1);
				Vector<Object> v = getDJTextFieldListeners();
				DJTextFieldEvent ev = new DJTextFieldEvent();
				ev.p1 = new Integer(p0);
				ev.p2 = new Integer(p1 - p0);
				ev.tipo = new Integer(DJTextFieldEvent.REMOVE.intValue());
				for (int i = 0; i < v.size(); i++)
					( (DJTextFieldListener) v.elementAt(i) ).remove(ev);
			}
			else if (dot > 0)
			{
				int delChars = 1;

				if (dot > 1)
				{
					String dotChars = doc.getText(dot - 2, 2);
					char c0 = dotChars.charAt(0);
					char c1 = dotChars.charAt(1);

					if (( c0 >= '\uD800' ) && ( c0 <= '\uDBFF' )
							&& ( c1 >= '\uDC00' ) && ( c1 <= '\uDFFF' ))
						delChars = 2;
				}

				// doc.remove(dot - delChars, delChars);
				beep = false;
				// ************ NOTIFICAMOS EL EVENTO *****************
				Vector<Object> v = getDJTextFieldListeners();
				DJTextFieldEvent ev = new DJTextFieldEvent();
				ev.p1 = new Integer(dot - delChars);
				ev.p2 = new Integer(delChars);
				ev.tipo = new Integer(DJTextFieldEvent.REMOVE.intValue());
				for (int i = 0; i < v.size(); i++)
					( (DJTextFieldListener) v.elementAt(i) ).remove(ev);
			}
		}
		catch (BadLocationException bl)
		{
		}
			if (beep) UIManager.getLookAndFeel().provideErrorFeedback(target);
		}
	}

	private class DDeleteNextCharAction extends TextAction
	{

		private static final long serialVersionUID = 7L;

		/* Create this object with the appropriate identifier. */
		DDeleteNextCharAction()
		{
			super("delete-next");
		}

		/** The operation to perform when this action is triggered. */
		public void actionPerformed(ActionEvent e)
		{
			JTextComponent target = getTextComponent(e);
			boolean beep = true;
			if (( target != null ) && ( target.isEditable() )) try
			{
				Document doc = target.getDocument();
				Caret caret = target.getCaret();
				int dot = caret.getDot();
				int mark = caret.getMark();
				if (dot != mark)
				{
					// doc.remove(Math.min(dot, mark), Math.abs(dot - mark));
				// ************ NOTIFICAMOS EL EVENTO *****************
				Vector<Object> v = getDJTextFieldListeners();
				DJTextFieldEvent ev = new DJTextFieldEvent();
				ev.p1 = new Integer(Math.min(dot, mark));
				ev.p2 = new Integer(Math.abs(dot - mark));
				ev.tipo = new Integer(DJTextFieldEvent.REMOVE.intValue());
				for (int i = 0; i < v.size(); i++)
					( (DJTextFieldListener) v.elementAt(i) ).remove(ev);
				// ****************************************************
				beep = false;
			}
			else if (dot < doc.getLength())
			{
				int delChars = 1;

				if (dot < doc.getLength() - 1)
				{
					String dotChars = doc.getText(dot, 2);
					char c0 = dotChars.charAt(0);
					char c1 = dotChars.charAt(1);

					if (( c0 >= '\uD800' ) && ( c0 <= '\uDBFF' )
							&& ( c1 >= '\uDC00' ) && ( c1 <= '\uDFFF' ))
						delChars = 2;
				}

				// doc.remove(dot, delChars);
				// ************ NOTIFICAMOS EL EVENTO *****************
				Vector<Object> v = getDJTextFieldListeners();
				DJTextFieldEvent ev = new DJTextFieldEvent();
				ev.p1 = new Integer(dot);
				ev.p2 = new Integer(delChars);
				ev.tipo = new Integer(DJTextFieldEvent.REMOVE.intValue());
				for (int i = 0; i < v.size(); i++)
					( (DJTextFieldListener) v.elementAt(i) ).remove(ev);
				// ****************************************************
				beep = false;
			}
		}
		catch (BadLocationException bl)
		{
		}
			if (beep) UIManager.getLookAndFeel().provideErrorFeedback(target);
		}
	}

	private static class DInsertContentAction extends TextAction
	{

		private static final long serialVersionUID = 11L;

		/**
		 * Creates this object with the appropriate identifier.
		 */
		public DInsertContentAction()
		{
			super("insert-content");
		}

		/**
		 * The operation to perform when this action is ed.
		 * 
		 * @param e
		 *            the action event
		 */
		public void actionPerformed(ActionEvent e)
		{
			// System.out.println("insert-content");
			JTextComponent target = getTextComponent(e);
			if (( target != null ) && ( e != null ))
			{
				if (( !target.isEditable() ) || ( !target.isEnabled() ))
				{
					UIManager.getLookAndFeel().provideErrorFeedback(target);
					return;
				}
				String content = e.getActionCommand();
				if (content != null)
					target.replaceSelection(content);
				else UIManager.getLookAndFeel().provideErrorFeedback(target);
			}
		}
	}

	static class DCut extends TextAction
	{

		private static final long serialVersionUID = 13L;

		DCut()
		{
			super("cut-to-clipboard");
		}

		public void actionPerformed(ActionEvent e)
		{
		}
	}

	static class DPaste extends TextAction
	{

		private static final long serialVersionUID = 17L;

		DPaste()
		{
			super("paste-from-clipboard");
		}

		public void actionPerformed(ActionEvent e)
		{
		}
	}

	static class DPrueba extends TextAction
	{

		private static final long serialVersionUID = 19L;

		DPrueba( String nombre )
		{
			super(nombre);
		}

		public void actionPerformed(ActionEvent e)
		{
			// System.out.println("prueba");
		}
	}

}
