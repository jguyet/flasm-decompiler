package com.flasm.basicaction.conversion;

import java.util.ArrayList;

import org.json.JSONObject;

import com.flagstone.transform.action.BasicAction;
import com.flasm.Flasm;
import com.flasm.FlasmConversion;
import com.flasm.Flasm.asClass;
import com.flasm.basicaction.FlasmBasicAction;

public class ASDecompileConversionBasicActionManager {

	@FlasmBasicAction(value = BasicAction.SET_VARIABLE, conversion = FlasmConversion.AS)//
	public static String setVariable(Flasm flasm) {
		Object s1 = flasm.stack.get(0);
		flasm.stack.remove(0);
		Object s2 = flasm.stack.get(0);
		flasm.stack.remove(0);
		
		if (s2 instanceof String) {
			s2 = "\"" + ((String)s2).replace("'", "\\'") + "\"";
		}
		
		flasm.getBuilder().append(s1.toString())
			.append(" = ")
			.append(s2.toString())
			.append(";")
			.append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.NAMED_OBJECT, conversion = FlasmConversion.AS)//NewObject
	public static String namedObject(Flasm flasm) {
		String s = "";
		
		Object name = flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		
		int size = (int)flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);//number
		
		while (size > 0) {
			Object s1 = flasm.stack.get(flasm.stack.size() - 1);
			flasm.stack.remove(flasm.stack.size() - 1);
			
			if (!s.isEmpty() && !s1.toString().isEmpty())
				s += ".";
			s += s1;
			size--;
		}
		if (!s.isEmpty())
			flasm.stack.add(s);
		flasm.stack.add(new asClass("new " + name + "()"));
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.GET_VARIABLE, conversion = FlasmConversion.AS)
	public static String getVariable(Flasm flasm) {
		Object s1 = flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		flasm.currentVar = s1;
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.NEW_ARRAY, conversion = FlasmConversion.AS)
	public static String newArray(Flasm flasm) {
		int size = (int)flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		ArrayList<Object> array = new ArrayList<Object>();
		
		while (size > 0) {
			Object s1 = flasm.stack.get(flasm.stack.size() - 1);
			flasm.stack.remove(flasm.stack.size() - 1);
			array.add(s1);
			size--;
		}
		flasm.stack.add(array);
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.NEW_OBJECT, conversion = FlasmConversion.AS)
	public static String newObject(Flasm flasm) {
		int size = (int)flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		JSONObject obj = new JSONObject();
		while (size > 0) {
			Object s2 = flasm.stack.get(flasm.stack.size() - 1);
			flasm.stack.remove(flasm.stack.size() - 1);
			Object s1 = flasm.stack.get(flasm.stack.size() - 1);
			flasm.stack.remove(flasm.stack.size() - 1);
			obj.put((String)s1, s2);
			size--;
		}
		flasm.stack.add(obj);
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.GET_ATTRIBUTE, conversion = FlasmConversion.AS)
	public static String getAttribute(Flasm flasm) {
		Object value = flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		
		//flasm.stack.add(value);
		flasm.currentVar = flasm.currentVar.toString() + "." + value.toString();
		flasm.stack.add(flasm.currentVar);
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.SET_ATTRIBUTE, conversion = FlasmConversion.AS)
	public static String setAttribute(Flasm flasm) {
		Object value = flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		
		if (value instanceof String) {
			value = "\"" + ((String)value).replace("'", "\\'") + "\"";
		} else if (value instanceof JSONObject) {
			value = value.toString().replaceAll("\"(\\w+)\":", "$1:");
		}
		
		Object index = flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		try {
			int v = Integer.parseInt(index.toString());
			
			flasm.getBuilder().append(flasm.currentVar.toString())
			.append("[").append(v).append("]");
			flasm.stack.remove(flasm.stack.size() - 1);
		} catch (Exception e) {
			flasm.getBuilder().append(flasm.currentVar.toString())
			.append(".")
			.append(index);
		}
		flasm.getBuilder().append(" = ")
		.append(value)
		.append(";")
		.append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.EXECUTE_METHOD, conversion = FlasmConversion.AS)
	public static String executeMethod(Flasm flasm) {
		Object method = flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		Object value = flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		int sizeArgs = (int)flasm.stack.get(flasm.stack.size() - 1);
		flasm.stack.remove(flasm.stack.size() - 1);
		
		String args = "";
		int i = 0;
		while (i < sizeArgs) {
			Object arg = flasm.stack.get(flasm.stack.size() - 1);
			flasm.stack.remove(flasm.stack.size() - 1);
			if (!args.isEmpty()) {
				args += ", ";
			}
			args += arg.toString();
			i++;
		}
		flasm.stack.add(value.toString() + "." + method.toString() + "(" + args + ");");
		flasm.getBuilder().append(value.toString())
		.append(".")
		.append(method.toString()).append("(").append(args).append(");")
		.append(System.lineSeparator());
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.POP, conversion = FlasmConversion.AS)
	public static String pop(Flasm flasm) {
		flasm.stack.remove(flasm.stack.size() - 1);
		return null;
	}
	
	@FlasmBasicAction(value = BasicAction.END, conversion = FlasmConversion.AS)
	public static String end(Flasm flasm) {
		//FINISHED
		return null;
	}
}
