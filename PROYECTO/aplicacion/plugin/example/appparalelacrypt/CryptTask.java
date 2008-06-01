package aplicacion.plugin.example.appparalelacrypt;

import net.jini.space.JavaSpace;
import calculoparalelo.eventos.PoisonPill;
import calculoparalelo.eventos.ResultEntry;
import calculoparalelo.eventos.TaskEntry;

public class CryptTask extends TaskEntry
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6287343312700465044L;

	public Integer tries;

	public byte[] word;

	public String encrypted;

	public CryptTask()
	{
	}

	public CryptTask( int tries, byte[] word, String encrypted )
	{
		this.tries = new Integer(tries);
		this.word = word;
		this.encrypted = encrypted;
	}

	public ResultEntry execute(JavaSpace space)
	{
		PoisonPill template = new PoisonPill();
		try
		{
			if (space.readIfExists(template, null, JavaSpace.NO_WAIT) != null)
			{
				return null;
			}
		}
		catch (Exception e)
		{
			; // continue on
		}
		int num = tries.intValue();
		System.out.println("Trying " + getPrintableWord(word));
		for (int i = 0; i < num; i++)
		{
			if (encrypted.equals(JCrypt.crypt(word)))
			{
				CryptResult result = new CryptResult(word);
				return result;
			}
			nextWord(word);
		}
		CryptResult result = new CryptResult(null);
		return result;
	}

	static String[] charMap =
	{ "^@", "^A", "^B", "^C", "^D", "^E", "^F", "^G", "^H", "^I", "^J", "^K",
			"^L", "^M", "^N", "^O", "^P", "^Q", "^R", "^S", "^T", "^U", "^V",
			"^W", "^X", "^Y", "^Z", "^[", "^\\", "^]", "^^", "^_", " ", "!",
			"\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".",
			"/", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";",
			"<", "=", ">", "?", "@", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "[", "\\", "]", "^", "_", "`", "a", "b",
			"c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|",
			"}", "~", "^?", };

	static void nextWord(byte[] word)
	{
		int pos = 5;
		for (;;)
		{
			word[pos]++;
			if (( word[pos] & 0x80 ) != 0)
			{
				word[pos--] = (byte) '!';
			}
			else
			{
				break;
			}
		}
	}

	public static String getPrintableWord(byte[] word)
	{
		String string = "";
		for (byte element : word)
		{
			string = string + charMap[element];
		}
		return string;
	}
}
