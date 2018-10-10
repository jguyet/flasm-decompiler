package com.flasm.action.conversion;

import java.util.List;

import com.flagstone.transform.action.BasicAction;
import com.flagstone.transform.action.Push;
import com.flagstone.transform.action.Table;
import com.flagstone.transform.action.TableIndex;
import com.flasm.Flasm;
import com.flasm.FlasmConversion;
import com.flasm.action.FlasmAction;
import com.flasm.basicaction.FlasmDecompileBasicActionFactory;

public class ASDecompileConversionActionManager {

	@FlasmAction(value = "BasicAction", conversion = FlasmConversion.AS)
	public static String basicActionHandler(BasicAction action, Flasm flasm) {
		return FlasmDecompileBasicActionFactory.executeBasicActionManager(action, flasm);
	}
	
	@FlasmAction(value = "Push", conversion = FlasmConversion.AS)
	public static String pushActionHandler(Push action, Flasm flasm) {
		for (Object o : action.getValues()) {
			
			Object value = null;
			
			if (o instanceof TableIndex) {
				int index = ((TableIndex)o).getIndex();
				
				value = flasm.table.getValues().get(index);
			} else {
				value = o;
			}
			flasm.stack.add(value);
		}
		return null;
	}
	
	@FlasmAction(value = "Table", conversion = FlasmConversion.AS)
	public static String tablecActionHandler(Table action, Flasm flasm) {
		
		flasm.table = action;
		return null;
	}
	
}
