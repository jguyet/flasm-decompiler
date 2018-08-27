package com.flasm.action.conversion;

import com.flagstone.transform.action.BasicAction;
import com.flagstone.transform.action.Push;
import com.flagstone.transform.action.Table;
import com.flagstone.transform.action.TableIndex;
import com.flasm.Flasm;
import com.flasm.FlasmConversion;
import com.flasm.action.FlasmAction;
import com.flasm.basicaction.FlasmBasicActionFactory;

public class PCODEConversionActionManager {

	@FlasmAction(value = "BasicAction", conversion = FlasmConversion.PCODE)
	public static String basicActionHandler(BasicAction action, Flasm flasm) {
		return FlasmBasicActionFactory.executeBasicActionManager(action, flasm);
	}
	
	@FlasmAction(value = "Push", conversion = FlasmConversion.PCODE)
	public static String basicActionHandler(Push action, Flasm flasm) {
		
		String pcode = "Push";
		
		for (Object o : action.getValues()) {
			
			Object value = null;
			
			if (o instanceof TableIndex) {
				int index = ((TableIndex)o).getIndex();
				
				value = flasm.table.getValues().get(index);
			} else {
				value = o;
			}
			if (!pcode.isEmpty()) {
				pcode += " ";
			}
			if (value instanceof String) {
				value = "\"" + value + "\"";
			}
			pcode += value;
		}
		flasm.pcode += pcode + System.lineSeparator();
		return null;
	}
	
	@FlasmAction(value = "Table", conversion = FlasmConversion.PCODE)
	public static String tableActionHandler(Table action, Flasm flasm) {
		flasm.table = action;
		String t = "ConstantPool";
		
		for (String v : action.getValues()) {
			
			if (!t.isEmpty()) {
				t += " ";
			}
			t += "\"" + v + "\"";
		}
		flasm.pcode += t + System.lineSeparator();
		return null;
	}
	
}
