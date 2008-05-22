package componentes.gui.usuarios;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;

import Deventos.DMIEvent;
import Deventos.enlaceJS.DConector;

import componentes.base.DComponenteBase;

/**
 * Etiqueta que nos muestra en cada momento el rol que estamos desempeñando
 */

public class DIEtiquetaRolActual
	 extends DComponenteBase {
  private FlowLayout flowLayout1 = new FlowLayout();
  private JLabel etiquetaTexto = new JLabel();
  private JLabel etiquetaRol = new JLabel();
  private String textoDefault = "Rol actual:";
  private Color colorTextoDefault = etiquetaTexto.getForeground();
  private Color colorRolDefault = Color.blue;

  /**
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	*/
  public DIEtiquetaRolActual(String nombre, boolean conexionDC,
									  DComponenteBase padre) {
	 super(nombre, conexionDC, padre);

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }
  }

  /**
	*
	* @param nombre String Nombre del componente.
	* @param conexionDC boolean TRUE si esta en contacto directo con el DConector
	* (no es hijo de ningun otro componente) y FALSE en otro caso
	* @param padre DComponenteBase Componente padre de este componente. Si no
	* tiene padre establecer a null
	* @param textoEtiqueta String Cadena de texto que habra antes del nombre del
	* rol. Por defecto es "Rol actual:"
	* @param colorTexto Color Color de la cadena de texto que hay antes del rol.
	* Por defecto es es color de Foreground del componente
	* @param colorRol Color Color de la cadena de texto del rol. Por defecto
	* es de color azul.
	*/
  public DIEtiquetaRolActual(String nombre, boolean conexionDC,
									  DComponenteBase padre, String textoEtiqueta,
									  Color colorTexto, Color colorRol) {
	 super(nombre, conexionDC, padre);

	 try {
		jbInit();
	 }
	 catch (Exception e) {
		e.printStackTrace();
	 }

	 textoDefault = textoEtiqueta;
	 colorTextoDefault = colorTexto;
	 colorRolDefault = colorRol;
	 etiquetaTexto.setText(textoDefault);
	 etiquetaTexto.setForeground(colorTextoDefault);
	 etiquetaRol.setForeground(colorRolDefault);
  }

  public void setTextoEtiqueta(String texto) {
	 etiquetaTexto.setText(texto + " ");
  }

  public void setColorTexto(Color color) {
	 etiquetaTexto.setForeground(color);
  }

  public void setColorRol(Color color) {
	 etiquetaRol.setForeground(color);
  }

  public void setDefault() {
	 etiquetaTexto.setText(textoDefault);
	 etiquetaTexto.setForeground(colorTextoDefault);
	 etiquetaRol.setForeground(colorRolDefault);
  }

  /**
	* Procesa los eventos de Metainformacion que le llegan
	* @param evento DMIEvent Evento recibido
	*/
  public void procesarMetaInformacion(DMIEvent evento) {
	 super.procesarMetaInformacion(evento);
	 if (evento.tipo.intValue() == DMIEvent.INFO_COMPLETA.intValue()) {
		String rol = evento.infoCompleta.rol;
		etiquetaRol.setText(rol);
	 }
	 if (evento.tipo.intValue() ==
		  DMIEvent.NOTIFICACION_CAMBIO_ROL_USUARIO.intValue()) {
		if (evento.usuario.equals(DConector.Dusuario)) {
		  String rol = evento.infoCompleta.rol;
		  etiquetaRol.setText(rol);
		}
	 }
  }

  private void jbInit() throws Exception {
	 etiquetaTexto.setFont(new java.awt.Font("Dialog", 1, 11));
	 etiquetaTexto.setText("Rol Actual: ");
	 etiquetaRol.setFont(new java.awt.Font("Dialog", 1, 11));
	 etiquetaRol.setForeground(Color.blue);
	 etiquetaRol.setText("");
	 this.setLayout(flowLayout1);
	 this.add(etiquetaTexto, null);
	 this.add(etiquetaRol, null);
  }

  /**
	* Obtiene el numero de componentes hijos de este componente. SIEMPRE devuelve 0
	* @return int Número de componentes hijos. SIEMPRE devuelve 0.
	*/
  public int obtenerNumComponentesHijos() {
	 return 0;
  }

}
