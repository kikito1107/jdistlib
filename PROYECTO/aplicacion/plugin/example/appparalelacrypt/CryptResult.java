package aplicacion.plugin.example.appparalelacrypt;

import calculoparalelo.eventos.ResultEntry;

public class CryptResult extends ResultEntry
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
