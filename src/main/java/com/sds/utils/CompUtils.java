package com.sds.utils;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;

public class CompUtils {

	public static void doDisableComponent(Component comps, boolean isView, List<Component> compExclude) {
		if (compExclude == null)
			compExclude = new ArrayList<>();
		for (Component comp : comps.getChildren()) {			
			if (comp.getChildren().size() > 0)
				doDisableComponent(comp, isView, compExclude);
			else {
				if (!compExclude.contains(comp)) {
					if (comp instanceof Textbox) {
						((Textbox) comp).setReadonly(isView);
						if (comp instanceof Combobox) {
							((Combobox) comp).setButtonVisible(!isView);
							((Combobox) comp).setReadonly(isView);
						}
					} else if (comp instanceof Datebox) {
						((Datebox) comp).setButtonVisible(!isView);
						((Datebox) comp).setReadonly(isView);
					} else if (comp instanceof Intbox) {
						((Intbox) comp).setReadonly(isView);
					} else if (comp instanceof Decimalbox) {
						((Decimalbox) comp).setReadonly(isView);
					} else if (comp instanceof Radio) {
						((Radio) comp).setDisabled(isView);
					} else if (comp instanceof Button) {
						((Button) comp).setVisible(!isView);
					}
				}				
			}			
		}
	}
}
