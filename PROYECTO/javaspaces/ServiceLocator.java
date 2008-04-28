/*
*  This software is intended to be used for educational purposes only.
*
*  We make no representations or warranties about the
*  suitability of the software.
*
*  Any feedback relating to this software or the book can
*  be sent to jsip@jsip.info
*
*  For updates to this and related examples visit www.jsip.info
*
*  JavaSpaces in Practice
*  by Philip Bishop & Nigel Warren
*  Addison Wesley; ISBN: 0321112318
*
*/

package javaspaces;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.DiscoveryPermission;
import net.jini.discovery.LookupDiscovery;
import net.jini.lookup.entry.Name;

public class ServiceLocator{
	private Object _proxy;
	private Object _lock=new Object();
	private ServiceTemplate _template;

	public static Object getService(Class<?> serviceClass)
			throws java.io.IOException,InterruptedException{

		return getService(serviceClass,null);
	}
	public static Object getService(Class<?> serviceClass,String serviceName)
			throws java.io.IOException,InterruptedException{

		ServiceLocator sl=new ServiceLocator();
		return sl.getServiceImpl(serviceClass,serviceName);
	}


	private Object getServiceImpl(Class<?> serviceClass,String serviceName)
			throws java.io.IOException,InterruptedException{

		Class<?> [] types=new Class[]{serviceClass};
		Entry [] entry=null;

		if(serviceName!=null){
			entry=new Entry[]{new Name(serviceName)};
		}

		_template=new ServiceTemplate(null,types,entry);

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
			System.out.println("Establecido el Manejador de Seguridad");
		}
		
		LookupDiscovery disco = null;
		
		DiscoveryPermission dp = new DiscoveryPermission("*");
		
		try{
			dp.checkGuard(null);
		}
		catch(SecurityException se){
			System.out.println("[Error] no te dejan acceder: " + se.getMessage());
			se.printStackTrace();
		}
		
		try{
			//MODIFIED FROM BOOK EXAMPLE TO USE ALL_GROUPS INSTEAD OF PUBLIC
			disco = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
		}
		catch(Exception E){
			System.out.println("Error en la inicializaci—n del sistema"+E.getMessage());
		}
		
		System.out.println("llegamos hasta aki");
		
		disco.addDiscoveryListener(new Listener());


		while(_proxy==null){
			synchronized(_lock){
				_lock.wait();
			}
		}
		disco.terminate();
		return _proxy;

	}

	class Listener implements DiscoveryListener {
		public void discovered(DiscoveryEvent ev) {


			ServiceRegistrar[] reg = ev.getRegistrars();
			for (int i=0 ; i<reg.length && _proxy==null ; i++) {
				findService(reg[i]);
			}
		}
		public void discarded(DiscoveryEvent ev) {
		}
	}

	private void findService(ServiceRegistrar lus) {

		try {
			synchronized(_lock){

				_proxy=lus.lookup(_template);

				if(_proxy!=null){

					_lock.notifyAll();
				}
			}
		}catch(RemoteException ex) {
		  ex.printStackTrace();
		}

	}
}
