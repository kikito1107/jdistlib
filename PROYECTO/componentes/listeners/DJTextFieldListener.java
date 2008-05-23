package componentes.listeners;

import Deventos.DJTextFieldEvent;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public interface DJTextFieldListener
{
	public void replace(DJTextFieldEvent evento);

	public void remove(DJTextFieldEvent evento);

	public void insert(DJTextFieldEvent evento);
}
