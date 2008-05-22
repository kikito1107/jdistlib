package lookandfeel.Dmetal;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicListUI;

/**
 * <p>Title: </p>
 * <p>Description: UI usada unica y exclusivamente por la lista interna
 * del DJComboBox.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MetalComboListUI
	 extends BasicListUI {

  protected void installDefaults() {
	 super.installDefaults();
	 //LookAndFeel.installColorsAndFont(list, "ToolBar.background", "List.foreground", "List.font");
  }

  /**
	* Con la sobrecarga de este método hacemos que se instalen solo
	* los listener que deseamos
	*/
  protected void installListeners() {
	 focusListener = createFocusListener();
	 mouseInputListener = createMouseInputListener();
	 propertyChangeListener = createPropertyChangeListener();
	 listSelectionListener = createListSelectionListener();
	 listDataListener = createListDataListener();

	 list.addFocusListener(focusListener);
	 list.addPropertyChangeListener(propertyChangeListener);

	 ListModel model = list.getModel();
	 if (model != null) {
		model.addListDataListener(listDataListener);
	 }

	 ListSelectionModel selectionModel = list.getSelectionModel();
	 if (selectionModel != null) {
		selectionModel.addListSelectionListener(listSelectionListener);
	 }
  }
}
