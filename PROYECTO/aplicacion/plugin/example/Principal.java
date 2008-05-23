package aplicacion.plugin.example;

import java.util.Vector;

import aplicacion.plugin.DAbstractPlugin;
import aplicacion.plugin.DPluginLoader;

public class Principal
{
	public static void main(String[] args)
	{
		try
		{
			Vector<DAbstractPlugin> v = DPluginLoader
					.getAllPlugins("aplicacion/plugin/example");

			for (int i = 0; i < v.size(); ++i)
				v.get(i).start();

			// v.get(0).start();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
