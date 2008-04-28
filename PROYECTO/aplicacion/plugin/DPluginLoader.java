package aplicacion.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Vector;

public class DPluginLoader
{
	@SuppressWarnings("unchecked")
	public static DAbstractPlugin getPlugin(String file) throws Exception
	{						
		String classname = file.replaceAll("/", ".");
		classname = classname.replaceAll(".class", "");
		
		System.out.println(classname);
		
		Class c = Class.forName(classname);
		
		DAbstractPlugin dap = (DAbstractPlugin)c.newInstance();
				
		return dap;
	}
	
	public static Vector<DAbstractPlugin> getAllPlugins(String directory) throws Exception
	{
		Vector<DAbstractPlugin> res = new Vector<DAbstractPlugin>();
		
		File dir = new File(directory);
		String children[] = dir.list();
		if (children == null)
		{
			throw new Exception("El directorio no existe o el path no corresponde a un directorio");
		}
		
		else
		{
			BufferedReader br = new BufferedReader(new FileReader(directory+"/dplugin.cnf"));
			
			Vector<String> tipos = new Vector<String>();
			
			boolean fin = false;
			while(!fin)
			{
				String aux = br.readLine();
				if (aux != null) tipos.add(aux);
				else fin = true;
			}
			
			File childs[] = dir.listFiles();
			for (int i=0; i<childs.length; i++)
			{
				if (!childs[i].isDirectory() && tipos.contains(childs[i].getPath()))
				{
					res.add(getPlugin(childs[i].getPath()));
				}
			}
		}
		
		return res;
	}
}
