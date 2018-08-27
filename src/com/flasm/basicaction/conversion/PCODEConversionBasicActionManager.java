package com.flasm.basicaction.conversion;

import com.flagstone.transform.action.BasicAction;
import com.flasm.Flasm;
import com.flasm.FlasmConversion;
import com.flasm.basicaction.FlasmBasicAction;

public class PCODEConversionBasicActionManager {

	@FlasmBasicAction(value = BasicAction.SET_VARIABLE, conversion = FlasmConversion.PCODE)
	public static String setVariable(Flasm flasm) {
		flasm.pcode += "SetVariable" + System.lineSeparator();
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.GET_VARIABLE, conversion = FlasmConversion.PCODE)
	public static String getVariable(Flasm flasm) {
		flasm.pcode += "GetVariable" + System.lineSeparator();
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.POP, conversion = FlasmConversion.PCODE)
	public static String pop(Flasm flasm) {
		flasm.pcode += "Pop" + System.lineSeparator();
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.NEW_ARRAY, conversion = FlasmConversion.PCODE)
	public static String newArray(Flasm flasm) {
		flasm.pcode += "InitArray" + System.lineSeparator();
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.END, conversion = FlasmConversion.PCODE)
	public static String end(Flasm flasm) {
		//FINISHED
		return null;
	}
	
}
