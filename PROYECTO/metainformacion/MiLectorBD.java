package metainformacion;

import java.util.*;
import util.*;
import java.sql.*;

/**
 * <p>Description: Con esta clase se realiza la lectura de la
 * metainformacion desde la BD. En la documentacion se puede encontrar una descripcion
 * detallada de la estructura de la Base de Datos</p>
 * @author Ana Belén Pelegrina Ortiz
 */
public class MiLectorBD {
  ConectorBD conexion = null;
  
  /**
   * Inicializa la clase, creando un nuevo objeto conexión BD
   */
  public MiLectorBD() {
	  conexion = new ConectorBD();
	  conexion.abrir();
  }

  /**
	* Recupera de la base de datos la información de las aplicaciones
	* @return Vector Devuelve un vector de MIAplicacion con las aplicaciones
	* definidas en la base de datos
	*/
  public Vector<MIAplicacion> Recuperar() {
	 Vector<MIAplicacion> datosAplicaciones = new Vector<MIAplicacion>();
	 MIAplicacion aplicacion = null;
	 MIComponente componente = null;
	 MIRol rol = null;
	 MIUsuario usuario = null;
	 	 
	 //Estos ResultSet se utilizarán para almacenar los datos recuperados de la bd mediante consultas
	 ResultSet aplicaciones = null;
	 ResultSet roles = null;
	 ResultSet usuarios = null;
	 ResultSet componentes = null;
	 
	 /*
	  * 1) recuperar todas las aplicaciones 
	  */
	 
	 aplicaciones = conexion.select("SELECT * FROM aplicacion");
	 
	 try{
		 while(aplicaciones.next()){
			 
			 /*
			  * 1.1) Recuperamos los datos propios de cada aplicacion
			  */
			 aplicacion = new MIAplicacion();
			 aplicacion.setNombre((String)aplicaciones.getString("nombre_aplicacion"));
			 aplicacion.setPermisoDefault(aplicaciones.getInt("nivel"));
			 
			 int idApp = aplicaciones.getInt("id_aplicacion");
			 
			 /*
			  * 1.2) Recuperamos los datos de relaciones
			  */
			 //   a) Recuperamos los componentes asociados a la aplicacion actual
			 componentes = conexion.select("SELECT * FROM componente WHERE id_aplicacion="+idApp);
			 
			 while(componentes.next()){
				 componente = new MIComponente(componentes.getString("nombre_componente"), aplicacion.getPermisoDefault());
				 aplicacion.nuevoComponente(componente);
			 }
			 
			 //   b) Recuperamos los roles asociados a la aplicación actual
			 roles = conexion.select("SELECT rol.id_rol, rol.nombre_rol " +
			 		"FROM posibleRol, rol " +
			 		"WHERE posibleRol.id_rol=rol.id_rol AND posibleRol.id_aplicacion="+idApp);
			 
			 while(roles.next()){
				 rol = new MIRol(roles.getString("nombre_rol"));
				 int id_rol = roles.getInt("id_rol");
				 
				 // Buscamos los componente sobre los que el rol tiene permisos
				 componentes = conexion.select( "SELECT * " +
				 		"FROM permisosRol, componente " +
				 		"WHERE permisosRol.id_rol="+id_rol+" AND" +
				 				" permisosRol.id_componente=componente.id_componente" );
				 
				 while(componentes.next()){
					 String Componente = componentes.getString("nombre_componente");
					 int permiso = componentes.getInt("nivel");
					 rol.nuevoPermisoComponente(Componente, permiso);
				 }
				 
				 aplicacion.nuevoRol(rol);
			 }
			 
			 // cerramos el enlace con la bd y liberamos memoria
			 roles.close();
			 
			 //   c) Recuperamos los usuarios asociados a la aplicación actual
			 usuarios = conexion.select("SELECT * " +
			 		"FROM posibleUsuario, usuario " +
			 		"WHERE	 posibleUsuario.id_usuario = usuario.id_usuario  AND posibleUsuario.id_aplicacion="+idApp);
			 
			 while(usuarios.next()){
				 usuario = new MIUsuario(usuarios.getString("nombre_usuario"), usuarios.getString("clave"));
				 int id_usuario =  usuarios.getInt("id_usuario");
				 int id_rol_defecto = usuarios.getInt("id_rol_predeterminado");
				 Boolean admin = usuarios.getBoolean("administrador");
				 				
				 usuario.setEsAdministrador(admin);
				 
				 //buscamos el rol por defecto
				 roles = conexion.select("SELECT * FROM rol WHERE rol.id_rol ="+ id_rol_defecto);
				 
				 while(roles.next()){
					 String nombre_rol = roles.getString("nombre_rol");
					 usuario.setRolPorDefecto(nombre_rol);
				 }
				 
				 // Buscamos los componente sobre los que el rol tiene permisos
				 componentes = conexion.select( "SELECT * " +
				 		"FROM permisosUsuario, componente " +
				 		"WHERE permisosUsuario.id_usuario="+id_usuario+" AND" +
				 				" permisosUsuario.id_componente=componente.id_componente" );
				 
				 while(componentes.next()){
					 String Componente = componentes.getString("nombre_componente");
					 int permiso = componentes.getInt("nivel");
					 usuario.nuevoPermisoComponente(Componente, permiso);
				 }
				 
				 // buscamos los roles permitidos
				 roles = conexion.select("SELECT * " +
				 		"FROM Permitidos, rol " +
				 		"WHERE Permitidos.id_rol = rol.id_rol AND Permitidos.id_usuario="+id_usuario);
				 
				 while(roles.next()){
					 usuario.nuevoRolPermitido(roles.getString("rol.nombre_rol"));
				 }
				 
				 aplicacion.nuevoUsuario(usuario);
			 }
			 
			 //System.out.println( '\n' + aplicacion.toString());
			 datosAplicaciones.add(aplicacion);
		 }
	 }
	 catch(SQLException e){
		 System.out.println("Error en la ejecución");
		 
	 }
	 
	 try {
		//Liberamos la memoria ocupada por los ResultSet
		aplicaciones.close(); 
		roles.close();
		usuarios.close();
		componentes.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 return datosAplicaciones;
  }
  
  public void crearLog()
  {
	  Fecha fecha = new Fecha();
	  int anho = fecha.getYear();
	  int mes = fecha.getMonth();
	  int dia = fecha.getDay();
	  int hora = fecha.getHoras();
	  int minuto = fecha.getMinutos();
	  int segundo = fecha.getSegundos();
	  
	  String f = ""+anho+"-"+mes+"-"+dia+" "+hora+":"+minuto+":"+segundo;
	  
	  if (!conexion.insert("INSERT INTO InitLog VALUES('"+f+"')"))
	  {
		  System.out.println("Error al crear el log: Ya existe un log con esa fecha en el sistema.");
	  }
	  
	  //conexion.cerrar();
  }
  
  public void actualizarBD(Vector aplicaciones)
  {	  
	  try{
		  Vector v = null;
		  Vector v2 = null;
		  ResultSet rs = null;
		  MIAplicacion aplicacion = null;
		  MIRol rol = null;
		  MIUsuario usuario = null;
		  MIComponente componente = null;
		  int i, j, k;
		  
		  //vaciar BD antes de insertar todo de nuevo
		  vaciarBD();
		  
		  for (i = 0; i < aplicaciones.size(); i++) { // Para cada aplicacion
			  aplicacion = (MIAplicacion) aplicaciones.elementAt(i);
			  
			  int id_aplicacion = i+1;

			  // Aplicacion
			  conexion.insert("INSERT INTO aplicacion VALUES("+id_aplicacion+",'"+aplicacion.getNombre()+"','"+aplicacion.getPermisoDefault()+"')");
			  
			  // Roles definidos
			  v = aplicacion.getRoles();
			  for (j = 0; j < v.size(); j++) {
				  
				  int id_rol = j+1;
				  String nombre_rol = ((MIRol) v.elementAt(j)).getNombreRol();
				  
				  //comprobar si existe en la tabla de rol, si no existe, agregarlo
				  rs = conexion.select("SELECT id_rol FROM rol WHERE nombre_rol='"+nombre_rol+"'");
				  if (rs.next())
				  {
					  id_rol = rs.getInt(1);
				  }
				  else
				  {
					  id_rol = conexion.generaId("rol", "id_rol");
					  conexion.insert("INSERT INTO rol VALUES('"+id_rol+"','"+nombre_rol+"')");
				  }
				  
				  rs.close();
				  
				  conexion.insert("INSERT INTO posibleRol VALUES("+id_rol+",'"+id_aplicacion+"')");
			  }

			  // Usuarios definidos
			  v2 = aplicacion.getUsuarios();
			  for (j = 0; j < v.size(); j++) {
				 usuario = (MIUsuario) v2.elementAt(j);
				 int id_usuario = j+1;
				 int administrador = usuario.esAdministrador()?1:0;
				 String nombre_usuario = usuario.getNombreUsuario();
				 String clave = usuario.getClave();
				 			
				 rs = conexion.select("SELECT id_rol FROM rol WHERE nombre_rol='"+usuario.getRolPorDefecto()+"'");
				 rs.next();
				 int id_rol_predeterminado = rs.getInt(1);
				 rs.close();
				 
				 //comprobar si existe el usuario
				 rs = conexion.select("SELECT id_usuario FROM usuario WHERE nombre_usuario='"+nombre_usuario+"'");
				 if (rs.next())
				 {
					 id_usuario = rs.getInt(1);
				 }
				 
				 else //insertarlo
				 {
					 id_usuario = conexion.generaId("usuario", "id_usuario");
					 conexion.insert("INSERT INTO usuario VALUES('"+id_usuario+"','"+nombre_usuario+"','"+clave+"','"+id_rol_predeterminado+"','"+administrador+"')");
				 }
				 
				 rs.close();
				 
				 conexion.insert("INSERT INTO posibleUsuario VALUES('"+id_usuario+"','"+id_aplicacion+"')");
				 
			  }

			  // Componentes definidos
			  v = aplicacion.getComponentes();
			  for (j = 0; j < v.size(); j++) {
				  
				  componente =  (MIComponente) v.elementAt(j);
				  int id_componente = j+1;
				  conexion.insert("INSERT INTO componente VALUES('"+id_componente+"','"+componente.getNombreComponente()+"','"+id_aplicacion+"')");
			  }

			  // Permisos especificados a los roles sobre componentes
			  v = aplicacion.getRoles();
			  System.out.println("ID_COMPONENTES");
			  for (j = 0; j < v.size(); j++) {
				 rol = (MIRol) v.elementAt(j);
				 int id_rol = j+1;
				 v2 = rol.getPermisosComponentes();
				 for (k = 0; k < v2.size(); k++) {
					componente = (MIComponente) v2.elementAt(k);
					rs = conexion.select("SELECT id_componente FROM componente WHERE nombre_componente='"+componente.getNombreComponente()+
							                       "' AND id_aplicacion='"+id_aplicacion+"'");
					rs.next();
					int id_componente = rs.getInt(1);
					System.out.println(id_componente);
					rs.close();
					
					conexion.insert("INSERT INTO permisosRol VALUES('"+id_rol+"','"+id_componente+"','"+componente.permisoComponente()+"')");
				 }
			  }

			  // Permisos especificados a los usuarios sobre componentes
			  v = aplicacion.getUsuarios();
			  for (j = 0; j < v.size(); j++) {
				 usuario = (MIUsuario) v.elementAt(j);
				 v2 = usuario.getPermisosComponentes();
				 int id_usuario = j+1;
				 for (k = 0; k < v2.size(); k++) {
					componente = (MIComponente) v2.elementAt(k);
					rs = conexion.select("SELECT id_componente FROM componente WHERE nombre_componente='"+componente.getNombreComponente()+
		                       "' AND id_aplicacion='"+id_aplicacion+"'");
					rs.next();
					int id_componente = rs.getInt(1);
					rs.close();
					
					conexion.insert("INSERT INTO permisosUsuario VALUES('"+id_usuario+"','"+id_componente+"','"+componente.permisoComponente()+"')");
				 }
			  }

			  // Roles permitidos a los usuarios
			  v = aplicacion.getUsuarios();
			  for (j = 0; j < v.size(); j++) {
				 usuario = (MIUsuario) v.elementAt(j);
				 int id_usuario = j+1;
				 v2 = usuario.getRolesPermitidos();
				 for (k = 0; k < v2.size(); k++) {
					String aux = (String) v2.elementAt(k);
					rs = conexion.select("SELECT id_rol FROM rol WHERE nombre_rol='"+aux+"'");
					rs.next();
					int id_rol = rs.getInt(1);
					rs.close();
					
					conexion.insert("INSERT INTO permitidos VALUES('"+id_usuario+"','"+id_rol+"')");
				 }
			  }

			}
		  
		  System.out.println("Guardado todo con exito");
	  }
	  catch(Exception ex)
	  {
		  ex.printStackTrace();
	  }
  }
  
  private void vaciarBD()
  {
	  conexion.delete("DELETE FROM aplicacion");
	  conexion.delete("DELETE FROM componente");
	  conexion.delete("DELETE FROM posibleRol"); //roles para la aplicacion actual
	  conexion.delete("DELETE FROM posibleUsuario"); //usuarios para la aplicacion actual
	  conexion.delete("DELETE FROM permitidos");
	  conexion.delete("DELETE FROM permisosRol");
	  conexion.delete("DELETE FROM permisosUsuario");
  }
  
  public void cerrarConexion()
  {
	  conexion.cerrar();
  }
  
  /**
   * Método main que prueba si la lectura de los datos desde la base de datos es la correcta, pintándolos en pantalla
   * @param args argumentos del main. Se ignoran
   */
  public static void main(String[] args){
	  MiLectorBD pbd = new MiLectorBD();
	  pbd.Recuperar();
  }
  
}
