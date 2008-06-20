package fisica.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

/**
 * Clase que guarda toda la configuracion del audio
 * 
 * @author Carlos Rodriguez Dominguez. Ana Belen Pelegrina Ortiz
 */
@SuppressWarnings( "unchecked" )
public class ConfiguracionAudio
{
	/**
	 * Indice del tamaño del buffer por defecto
	 */
	private static final int INDICE_TAM_BUFFER = 2;

	/**
	 * Tamaño del buffer en milisegundos
	 */
	private static final int[] TAM_BUFFER_MILISEGUNDOS =
	{ 30, 40, 50, 70, 85, 100, 130, 150, 180, 220, 400 };

	/**
	 * Codigo de formato por defecto
	 */
	private static final int CODIGO_FORMATO_POR_DEFECTO = ConstantesAudio.CODIGO_FORMATO_TELEFONO;

	private Port[] puerto = new Port[2];

	private FloatControl[] volumenPuerto = new FloatControl[2];

	private BooleanControl[] puertoSeleccionado = new BooleanControl[2];

	private List<String> nombresPuertos[] = new List[2];

	private List<Port>[] puertos = new List[2];

	private List<Integer>[] indices_controles = new List[2];

	private Mixer[] mezclador = new Mixer[2];

	private List<Mixer>[] mezcladores = new List[2];

	private int[] indice_tam_buffer = new int[2];

	private int codigo_formato;

	private boolean inicializado = false;

	/**
	 * Contructor
	 * 
	 * @param master
	 *            Modelo de datos general para el sistema de captura
	 */
	public ConfiguracionAudio( DatosGenerales master )
	{
		nombresPuertos[0] = new ArrayList<String>();
		nombresPuertos[1] = new ArrayList<String>();
		puertos[0] = new ArrayList<Port>();
		puertos[1] = new ArrayList<Port>();
		indices_controles[0] = new ArrayList<Integer>();
		indices_controles[1] = new ArrayList<Integer>();
		mezcladores[0] = new ArrayList<Mixer>();
		mezcladores[1] = new ArrayList<Mixer>();
		indice_tam_buffer[0] = INDICE_TAM_BUFFER;
		indice_tam_buffer[1] = INDICE_TAM_BUFFER;
		codigo_formato = CODIGO_FORMATO_POR_DEFECTO;
	}

	/**
	 * Inicializar la configuracion
	 */
	public void init()
	{
		for (int d = 0; d < 2; d++)
		{
			nombresPuertos[d].clear();
			puertos[d].clear();
			indices_controles[d].clear();
			mezcladores[d].clear();
		}

		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		for (int i = 0; i < infos.length; i++)
			try
			{
				Mixer mixer = AudioSystem.getMixer(infos[i]);
				addMixer(mixer, mixer.getSourceLineInfo(),
						ConstantesAudio.DIR_SPK);
				addMixer(mixer, mixer.getTargetLineInfo(),
						ConstantesAudio.DIR_MIC);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		for (int d = 0; d < 2; d++)
			if (mezcladores[d].size() > 1) mezcladores[d].add(0, null);
		inicializado = true;
	}

	/**
	 * Deshacer la configuracion
	 */
	public void terminar()
	{
		for (int d = 0; d < 2; d++)
		{
			cerrarPuerto(d);
			nombresPuertos[d].clear();
			indices_controles[d].clear();
			puertos[d].clear();
			mezcladores[d].clear();
		}
	}

	/**
	 * Agregar un mezclador a la lista disponible
	 * 
	 * @param mixer
	 *            Mezclador a agregar
	 * @param infos
	 *            Informacion de la linea
	 * @param indice_data_line
	 *            Linea de datos seleccionada
	 */
	private void addMixer(Mixer mixer, Line.Info[] infos, int indice_data_line)
	{
		for (int i = 0; i < infos.length; i++)
			try
			{
				if (infos[i] instanceof Port.Info)
				{
					Port.Info info = (Port.Info) infos[i];
					int d;

					// es el microfono
					if (info.isSource())
						d = ConstantesAudio.DIR_MIC;

					// es el altavoz
					else d = ConstantesAudio.DIR_SPK;

					Port p = (Port) mixer.getLine(info);
					p.open();
					try
					{
						Control[] cs = p.getControls();
						for (int c = 0; c < cs.length; c++)
							if (cs[c] instanceof CompoundControl)
							{
								puertos[d].add(p);
								nombresPuertos[d].add(mixer.getMixerInfo()
										.getName()
										+ ": " + cs[c].getType().toString());
								indices_controles[d].add(c);
							}
					}
					finally
					{
						p.close();
					}
				}
				if (infos[i] instanceof DataLine.Info)
					if (!mezcladores[indice_data_line].contains(mixer))
						mezcladores[indice_data_line].add(mixer);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}

	/**
	 * Obtiene los nombres de los mezcladores disponibles
	 * 
	 * @param d
	 *            Indice del mezclador
	 * @return Lista con los nombres de los mezcladores
	 */
	public List<String> getNombresMezcladores(int d)
	{
		if (!inicializado) init();
		List<String> res = new ArrayList<String>();
		for (Mixer m : mezcladores[d])
			if (m == null)
				res.add("(Default)");
			else res.add(m.getMixerInfo().getName());
		return res;
	}

	/**
	 * Obtiene el mezclador seleccionado
	 * 
	 * @param d
	 *            Indice del mezclador
	 * @return Mezclador seleccionado
	 */
	public Mixer getMezcladorSeleccionado(int d)
	{
		return mezclador[d];
	}

	/**
	 * Asigna el mezclador de la lista de mezcladores
	 * 
	 * @param d
	 *            Indice en la lista de mezcladores
	 * @param index
	 *            Indice del mezclador en la lista
	 */
	public void setMezcladorSeleccionado(int d, int index)
	{
		if (( index < 0 ) || ( index >= mezcladores[d].size() ))
			return;
		else mezclador[d] = mezcladores[d].get(index);
	}

	/**
	 * Obtiene el nombre de los puertos para un mezclador
	 * 
	 * @param d
	 *            Indice de la lista de mezcladores
	 * @return Nombres asociados a los puertos
	 */
	public List<String> getPuertos(int d)
	{
		if (!inicializado) init();
		return nombresPuertos[d];
	}

	/**
	 * Obtiene el puerto seleccionado
	 * 
	 * @param d
	 *            Indice del puerto
	 * @return Puerto seleccionado
	 */
	public Port getPuertoSeleccionado(int d)
	{
		return puerto[d];
	}

	/**
	 * Obtiene el volumen del puerto seleccionado
	 * 
	 * @param d
	 *            Indice del puerto
	 * @return Control de volumen asociado al puerto
	 */
	public FloatControl getControlVolumenSeleccionado(int d)
	{
		return volumenPuerto[d];
	}

	/**
	 * Asigna el puerto seleccionado
	 * 
	 * @param d
	 *            Indice del puerto seleccionado
	 * @param index
	 *            Indice del puerto en cuestion
	 */
	public void setPuertoSeleccionado(int d, int index)
	{
		setPuertoSeleccionado(d, index, true);
	}

	/**
	 * Asigna el puerto seleccionado
	 * 
	 * @param d
	 *            Indice del puerto seleccionado
	 * @param index
	 *            Indice del puerto en cuestion
	 * @param seleccionar
	 *            Indica si hay que seleccionar el puerto o no
	 */
	public void setPuertoSeleccionado(int d, int index, boolean seleccionar)
	{
		if (( index < 0 ) || ( index >= puertos[d].size() ))
		{
			cerrarPuerto(d);
			return;
		}
		else
		{
			configurarPuerto(d, puertos[d].get(index), indices_controles[d]
					.get(index));

			if (seleccionar && ( d == ConstantesAudio.DIR_MIC )
					&& ( puertoSeleccionado[d] != null ))
				puertoSeleccionado[d].setValue(true);
		}
	}

	/**
	 * Cierra el puerto seleccionado
	 * 
	 * @param d
	 *            Indice del puerto
	 */
	private void cerrarPuerto(int d)
	{
		if (puerto[d] != null)
		{
			puerto[d].close();
			puerto[d] = null;
		}
		volumenPuerto[d] = null;
		puertoSeleccionado[d] = null;
	}

	/**
	 * Configurar un puerto
	 * 
	 * @param d
	 *            Indice del puerto
	 * @param p
	 *            Puerto configurado
	 * @param controlIndex
	 *            Indice del control de volumen
	 */
	private void configurarPuerto(int d, Port p, int controlIndex)
	{
		if (( p != puerto[d] ) && ( puerto[d] != null )) puerto[d].close();
		volumenPuerto[d] = null;
		puertoSeleccionado[d] = null;
		puerto[d] = p;
		try
		{
			p.open();
			Control[] cs = p.getControls();
			if (controlIndex >= cs.length)
				throw new Exception("Control no encontrado");
			if (!( cs[controlIndex] instanceof CompoundControl ))
				throw new Exception("Control no encontrado");
			CompoundControl cc = (CompoundControl) cs[controlIndex];
			cs = cc.getMemberControls();

			for (Control c : cs)
			{
				if (( c instanceof FloatControl )
						&& c.getType().equals(FloatControl.Type.VOLUME)
						&& ( volumenPuerto[d] == null ))
					volumenPuerto[d] = (FloatControl) c;
				if (( c instanceof BooleanControl )
						&& c.getType().toString().contains("elect")
						&& ( puertoSeleccionado[d] == null ))
					puertoSeleccionado[d] = (BooleanControl) c;
			}

		}
		catch (Exception e)
		{
			cerrarPuerto(d);
		}
	}

	/**
	 * Obtien el tamaño del buffer en milisegundos de un puerto
	 * 
	 * @param d
	 *            Puerto
	 * @return Tamaño del buffer
	 */
	public int getTamBufferMilisegundos(int d)
	{
		return TAM_BUFFER_MILISEGUNDOS[indice_tam_buffer[d]];
	}

	/**
	 * Obtiene el indice del tamaño del buffer de un puerto
	 * 
	 * @param d
	 *            Puerto
	 * @return Indice del tamaño del buffer
	 */
	public int getIndiceTamBuffer(int d)
	{
		return indice_tam_buffer[d];
	}

	/**
	 * Asignar el indice del tamaño del buffer
	 * 
	 * @param d
	 *            Puerto
	 * @param indice
	 *            Indice del tamaño del buffer
	 */
	public void setIndiceTamBuffer(int d, int indice)
	{
		this.indice_tam_buffer[d] = indice;
	}

	/**
	 * Asigna el codigo de formato
	 * 
	 * @param c_for
	 *            Codigo de formato
	 */
	public void setCodigoFormato(int c_for)
	{
		codigo_formato = c_for;
	}

	/**
	 * Obtiene el codigo de formato
	 * 
	 * @return Codigo de formato
	 */
	public int getCodigoFormato()
	{
		return codigo_formato;
	}
}
