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

import net.jini.space.JavaSpace;

public class SpaceLocator
{

	public static JavaSpace getSpace() throws Exception
	{

		return (JavaSpace) ServiceLocator.getService(JavaSpace.class);

	}

	public static JavaSpace getSpace(String spaceName) throws Exception
	{

		try
		{
			return (JavaSpace) ServiceLocator.getService(JavaSpace.class,
					spaceName);
		}
		catch (Exception e)
		{
			System.err.println("error buscando el espacio " + e.getMessage());
			throw new Exception();
		}
	}
}
