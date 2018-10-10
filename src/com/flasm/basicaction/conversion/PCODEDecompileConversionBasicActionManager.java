package com.flasm.basicaction.conversion;

import org.json.JSONObject;

import com.flagstone.transform.action.BasicAction;
import com.flasm.Flasm;
import com.flasm.FlasmConversion;
import com.flasm.Flasm.asClass;
import com.flasm.basicaction.FlasmBasicAction;

public class PCODEDecompileConversionBasicActionManager {

	@FlasmBasicAction(value = BasicAction.SET_VARIABLE, conversion = FlasmConversion.PCODE)
	public static String setVariable(Flasm flasm) {
		flasm.getBuilder().append("SetVariable").append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.GET_VARIABLE, conversion = FlasmConversion.PCODE)
	public static String getVariable(Flasm flasm) {
		flasm.getBuilder().append("GetVariable").append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.GET_ATTRIBUTE, conversion = FlasmConversion.AS)
	public static String getAttribute(Flasm flasm) {
		flasm.getBuilder().append("GetMember").append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.SET_ATTRIBUTE, conversion = FlasmConversion.AS)
	public static String setAttribute(Flasm flasm) {
		flasm.getBuilder().append("SetMember").append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.POP, conversion = FlasmConversion.PCODE)
	public static String pop(Flasm flasm) {
		flasm.getBuilder().append("Pop").append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.NEW_ARRAY, conversion = FlasmConversion.PCODE)
	public static String newArray(Flasm flasm) {
		flasm.getBuilder().append("InitArray").append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.NEW_OBJECT, conversion = FlasmConversion.PCODE)
	public static String newObject(Flasm flasm) {
		flasm.getBuilder().append("InitObject").append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.NAMED_OBJECT, conversion = FlasmConversion.AS)
	public static String namedObject(Flasm flasm) {
		flasm.getBuilder().append("NewObject").append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.END, conversion = FlasmConversion.PCODE)
	public static String end(Flasm flasm) {
		//FINISHED
		return null;
	}
	
}
