package metainformacion;

import java.util.*;

import util.*;

/**
 * <p>Description: Con esta clase se realiza el parsing del fichero de
 * metainformacion. En la documentacion se puede encontrar una descripcion
 * detallada del formato posible del fichero</p>
 * @author Juan Antonio Ibañez Santorum (looper@telefonica.net)
 */

public class MIParser {
  private Fichero errores = null;
  private Fecha fecha = null;
  
  public MIParser() {

  }

  public MIParser(Fichero ficheroSalida) {
	 errores = ficheroSalida;
  }

  /**
	* Realiza el parsing del fichero cuyo nombre se indica
	* @param fichero String Nombre del fichero del que queremos hacer el parsgin
	* @return Vector Devuelve un vector de MIAplicacion con las aplicaciones
	* definidas en el fichero
	*/
  public Vector<MIAplicacion> parse(String fichero) {
	 int i, j;
	 Vector<MIAplicacion> datosAplicaciones = new Vector<MIAplicacion>();
	 MIAplicacion aplicacion = null;
	 Vector textoAplicaciones = new Vector();
	 Vector lineasAplicacion = null;

	 try {
		// Creamos el nombre del fichero de salida
		if (errores == null) {
		  fecha = new Fecha();
		  int dia = fecha.getDay();
		  int mes = fecha.getMonth();
		  int anio = fecha.getYear();
		  int hora = fecha.getHoras();
		  int minutos = fecha.getMinutos();
		  int segundos = fecha.getSegundos();
		  String nombreFichero = new String("parselog_" + dia + "_" + mes + "_" +
														anio + "__" + hora + "." + minutos +
														"." + segundos + ".txt");
		  errores = new Fichero(nombreFichero, "rw");
		}
	 }
	 catch (Exception ff) {
		ff.printStackTrace();
	 }

	 Fichero toParse = null;
	 try {
		toParse = new Fichero(fichero, "r");
		// Obtenemos un vector el cual tiene en cada posicion un vector con el texto
		// de cada una de las aplicaciones definidas. Este segundo vector contiene
		// en cada una de sus posiciones un String con la linea correspondiente
		textoAplicaciones = obtenerDefinicionAplicaciones(toParse);
	 }
	 catch (Exception e) {}

	 for (i = 0; i < textoAplicaciones.size(); i++) {
		aplicacion = new MIAplicacion();
		lineasAplicacion = (Vector) textoAplicaciones.elementAt(i);
		String lineaTexto = null;
		// Hacemos parsing de cada una de las lineas definidas
		for (j = 0; j < lineasAplicacion.size(); j++) {
		  lineaTexto = (String) lineasAplicacion.elementAt(j);
		  procesarLineaTextoAplicacion(aplicacion, lineaTexto);
		}
		datosAplicaciones.add(aplicacion);
	 }

	 /*for(i=0; i<datosAplicaciones.size(); i++){
	  System.out.println((MIAplicacion)datosAplicaciones.elementAt(i));
	  }*/

	 return datosAplicaciones;
  }

  /**
	* Lee el fichero de entrada eliminando lineas en blanco y devolviendo la
	* definicion de cada una de las aplicaciones
	* @param f Fichero Fichero de entrada
	* @return Vector Vector que contiene en cada posicion la definicion de
	* cada una de las aplicaciones definidas en el fichero de entrada. El vector
	* de cada una de las aplicaciones contiene un vector de String con cada una
	* de las lineas de la definicion
	*/
  private Vector obtenerDefinicionAplicaciones(Fichero f) {
	 Vector aplicaciones = new Vector();
	 Vector v = new Vector();

	 try {
		String ln = null;
		boolean encontradoPrincipio = false;
		ln = f.leerLinea();
		// Buscamos la primera definicion ignorando las lineas que no sean
		// de definicion del nombre de la aplicacion
		while (ln != null && !encontradoPrincipio) {
		  encontradoPrincipio = esDefinicionNombreAplicacion(ln);
		  if (!encontradoPrincipio) {
			 ln = f.leerLinea();
		  }
		}

		boolean siguienteAplicacion = false;
		// Subdividimos el texto en tantas partes como aplicaciones definidas
		while (ln != null) {
		  siguienteAplicacion = false;
		  while (ln != null && !siguienteAplicacion) {
			 if (!esLineaVacia(ln)) {
				v.add(eliminarEspacios(ln));
			 }
			 ln = f.leerLinea();
			 if (ln != null) {
				if (esDefinicionNombreAplicacion(ln)) { //Comienza una nueva definicion
				  siguienteAplicacion = true;
				  aplicaciones.add(v);
				  v = new Vector();
				}
			 }
			 else {
				if (v.size() > 0) {
				  aplicaciones.add(v);
				}
			 }
		  }
		}
	 }
	 catch (Exception e) {}

	 return aplicaciones;
  }

  /**
	* Realiza las acciones pertinentes con la linea indicada
	* @param aplicacion MIAplicacion Objeto sobre el que se realizaran las acciones
	* pertinentes dependiendo de lo que nos indique la linea
	* @param linea String Linea sobre la que hacemos el parsing correspondiente
	*/
  private void procesarLineaTextoAplicacion(MIAplicacion aplicacion,
														  String linea) {
	 boolean procesado = false;
	 if (esDefinicionNombreAplicacion(linea)) {
		aplicacion.setNombre(eliminarEspacios(linea.split("###")[1]));
		procesado = true;
	 }
	 if (esDefinicionNivelDefault(linea)) {
		aplicacion.setPermisoDefault(new Integer(linea.split("#")[1]).intValue());
		procesado = true;
	 }
	 else if (!procesado && esDefinicionRol(linea)) {
		String nombreRol = new String(eliminarEspacios(linea.split("#")[1]));
		if (aplicacion.existeRol(nombreRol)) {
		  try {
			 errores.escribirLinea("Error parse: Ya ha sido definido el rol " +
										  nombreRol + " -> " + linea);
		  }
		  catch (Exception e) {}
		}
		else {
		  MIRol rol = new MIRol(nombreRol);
		  aplicacion.nuevoRol(rol);
		  procesado = true;
		}
	 }
	 else if (!procesado && esDefinicionUsuario(linea)) {
		MIUsuario usuario = null;
		String aux = new String(eliminarEspacios(linea.split("#")[1]));
		String[] partes = aux.split(":");
		if (aplicacion.existeUsuario(partes[0])) {
		  try {
			 errores.escribirLinea("Error parse: Ya ha sido definido el usuario " +
										  partes[0] + " -> " + linea);
		  }
		  catch (Exception e) {}
		}
		else {
		  usuario = new MIUsuario(partes[0], partes[1]);
		  if (partes[3].equals("t") || partes[3].equals("T")) {
			 usuario.setEsAdministrador(true);
		  }
		  if (aplicacion.existeRol(partes[2])) {
			 usuario.setRolPorDefecto(partes[2]);
		  }
		  else {
			 usuario.setRolPorDefecto("Basico");
			 try {
				errores.escribirLinea("Error parse: No ha sido definido el rol " +
											 partes[2] + " -> " + linea);
			 }
			 catch (Exception e) {}
		  }
		  aplicacion.nuevoUsuario(usuario);
		}
		procesado = true;
	 }
	 else if (!procesado && esDefinicionComponente(linea)) {
		String nombreComponente = new String(eliminarEspacios(linea.split("#")[1]));
		MIComponente componente = new MIComponente(nombreComponente, -1);
		if (aplicacion.existeComponente(componente.getNombreComponente())) {
		  try {
			 errores.escribirLinea(
				  "Error parse: Ya ha sido definido el componente " +
				  componente.getNombreComponente() + " -> " + linea);
		  }
		  catch (Exception e) {}
		}
		else {
		  aplicacion.nuevoComponente(componente);
		}
		procesado = true;
	 }
	 else if (!procesado && esDefinicionPermisoRol(linea)) {
		String[] partes = linea.split("#");
		if (!aplicacion.existeRol(partes[1])) {
		  try {
			 errores.escribirLinea("Error parse: No ha sido definido el rol " +
										  partes[1] + " -> " + linea);
		  }
		  catch (Exception e) {}
		}
		else {
		  if (!aplicacion.existeComponente(partes[2])) {
			 try {
				errores.escribirLinea(
					 "Error parse: No ha sido definido el componente " +
					 partes[2] + " -> " + linea);
			 }
			 catch (Exception e) {}
		  }
		  else {
			 aplicacion.nuevoPermisoRol(partes[1], partes[2],
												 new Integer(partes[3]).intValue());
		  }
		}
		procesado = true;
	 }
	 else if (!procesado && esDefinicionPermisoUsuario(linea)) {
		String[] partes = linea.split("#");
		if (!aplicacion.existeUsuario(partes[1])) {
		  try {
			 errores.escribirLinea("Error parse: No ha sido definido el usuario " +
										  partes[1] + " -> " + linea);
		  }
		  catch (Exception e) {}
		}
		else {
		  if (!aplicacion.existeComponente(partes[2])) {
			 try {
				errores.escribirLinea(
					 "Error parse: No ha sido definido el componente " +
					 partes[2] + " -> " + linea);
			 }
			 catch (Exception e) {}
		  }
		  else {
			 aplicacion.nuevoPermisoUsuario(partes[1], partes[2],
													  new Integer(partes[3]).intValue());
		  }
		}
		procesado = true;
	 }
	 else if (!procesado && esDefinicionRolPermitido(linea)) {
		String[] partes = linea.split("#");
		if (!aplicacion.existeUsuario(partes[1])) {
		  try {
			 errores.escribirLinea("Error parse: No ha sido definido el usuario " +
										  partes[1] + " -> " + linea);
		  }
		  catch (Exception e) {}
		}
		else {
		  if (!aplicacion.existeRol(partes[2])) {
			 try {
				errores.escribirLinea("Error parse: No ha sido definido el rol " +
											 partes[2] + " -> " + linea);
			 }
			 catch (Exception e) {}
		  }
		  else {
			 aplicacion.nuevoRolPermitidoUsuario(partes[1], partes[2]);
		  }
		}
		procesado = true;
	 }
	 if (!procesado) {
		try {
		  errores.escribirLinea("Error parse: Linea con formato incorrecto -> " +
										linea);
		}
		catch (Exception e) {}
	 }

  }

// ************* FUNCIONES DE RECONOCIMIENTO DE TEXTO *************************
  /**
	* Comprobamos si es una linea vacia
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esLineaVacia(String linea) {
	 return linea.matches("\\s*");
  }

  /**
	* Comprobamos si es una linea de definicion del nombre de una aplicacion
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esDefinicionNombreAplicacion(String linea) {
	 return linea.matches("[\\t ]*###[\\t ]*\\w+[\\t ]*");
  }

  /**
	* Comprobamos si es una linea que define un nuevo rol
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esDefinicionRol(String linea) {
	 return linea.matches("[\\t ]*[Rr][Oo][Ll](#\\w+)+[\\t ]*");
  }

  /**
	* Comprobamos si es una linea que define un nuevo usuario
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esDefinicionUsuario(String linea) {
	 return linea.matches(
		  "[\\t ]*[Uu][Ss][Uu][Aa][Rr][Ii][Oo]#\\w+:\\w+:\\w+:[TfFf][\\t ]*");
  }

  /**
	* Comprobamos si es una linea que define un nuevo componente
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esDefinicionComponente(String linea) {
	 return linea.matches(
		  "[\\t ]*[Cc][Oo][Mm][Pp][Oo][Nn][Ee][Nn][Tt][Ee](#\\w+)+[\\t ]*");
  }

  /**
	* Comprobamos si es una linea que define el permiso de un componente para un
	* rol
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esDefinicionPermisoRol(String linea) {
	 return linea.matches(
		  "[\\t ]*[Pp][Ee][Rr][Mm][Ii][Ss][Oo][Rr][Oo][Ll]#\\w+#\\w+#\\d{1,2}[\\t ]*");
  }

  /**
	* Comprobamos si es una linea que define el permiso de un componente para un
	* usuario
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esDefinicionPermisoUsuario(String linea) {
	 return linea.matches("[\\t ]*[Pp][Ee][Rr][Mm][Ii][Ss][Oo][Uu][Ss][Uu][Aa][Rr][Ii][Oo]#\\w+#\\w+#\\d{1,2}[\\t ]*");
  }

  /**
	* Comprobamos si es una linea que define un nuevo rol permitido para un usuario
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esDefinicionRolPermitido(String linea) {
	 return linea.matches(
		  "[\\t ]*[Rr][Oo][Ll][Pp][Ee][Rr][Mm][Ii][Tt][Ii][Dd][Oo]#\\w+#\\w+[\\t ]*");
  }

  /**
	* Comprobamos si es una linea que define el nivel de permiso por defecto para
	* los componentes de la aplicacion
	* @param linea String Linea a comprobar
	* @return boolean True en caso afirmativo. False en otro caso
	*/
  private boolean esDefinicionNivelDefault(String linea) {
	 return linea.matches(
		  "[\\t ]*[Nn][Ii][Vv][Ee][Ll][Dd][Ee][Ff][Aa][Uu][Ll][Tt]#\\d{1,2}[\\t ]*");
  }

  /**
	* Elimina los espacios en blanco al principio y al final de la linea
	* @param cadena String Linea a la que queremos eliminar los espacios
	* @return String La linea de entrada pero con los espacios eliminados
	*/
  private String eliminarEspacios(String cadena) {
	 String cad = new String("");
	 char[] c = cadena.toCharArray();
	 int i = 0, j = 0;

	 if (cadena.length() > 0) {
		while (c[i] == ' ') {
		  i++;
		}

		j = c.length - 1;
		while (c[j] == ' ') {
		  j--;
		}

		cad = new String(cadena.substring(i, j + 1));
	 }

	 return cad;
  }
}
