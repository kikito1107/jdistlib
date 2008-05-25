package aplicacion.plugin.example.appparalelacrypt;

import net.jini.core.entry.Entry;

public class CryptResult implements Entry
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7137843920467830117L;

	public byte[] word = null;

	public CryptResult()
	{
	}

	public CryptResult( byte[] word )
	{
		this.word = word;
	}
}
