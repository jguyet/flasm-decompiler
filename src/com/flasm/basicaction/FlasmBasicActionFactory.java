package com.flasm.basicaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flagstone.transform.action.BasicAction;
import com.flasm.Flasm;
import com.flasm.FlasmConversion;
import com.flasm.basicaction.conversion.ASConversionBasicActionManager;
import com.flasm.basicaction.conversion.PCODEConversionBasicActionManager;

public class FlasmBasicActionFactory {

	public static Map<BasicAction, Method> ASbasicactionHandler = new HashMap<BasicAction, Method>();
	public static Map<BasicAction, Method> PCODEbacisactionHandler = new HashMap<BasicAction, Method>();
	public static Map<FlasmConversion, Map<BasicAction, Method>> basicActionHandler = new HashMap<FlasmConversion, Map<BasicAction, Method>>();

	/**
	 * load all method on directory org.fixme.router.servers.broker.handler with MethodMessageHandler anotation
	 */
	static//load once
	{
		Method[] allASMethod = ASConversionBasicActionManager.class.getDeclaredMethods();
		Method[] allFLASMMethod = PCODEConversionBasicActionManager.class.getDeclaredMethods();
		
		for (Method m : allASMethod)
		{
			Annotation[] annots = m.getAnnotations();
			
			if (annots.length < 0)
				continue ;
			for (Annotation a : annots)
			{
				if (!(a instanceof FlasmBasicAction))
					continue ;
				ASbasicactionHandler.put(((FlasmBasicAction)a).value(), m);
			}
		}
		
		for (Method m : allFLASMMethod)
		{
			Annotation[] annots = m.getAnnotations();
			
			if (annots.length < 0)
				continue ;
			for (Annotation a : annots)
			{
				if (!(a instanceof FlasmBasicAction))
					continue ;
				PCODEbacisactionHandler.put(((FlasmBasicAction)a).value(), m);
			}
		}
		
		basicActionHandler.put(FlasmConversion.AS, ASbasicactionHandler);
		basicActionHandler.put(FlasmConversion.PCODE, PCODEbacisactionHandler);
	}
	
	public static String executeBasicActionManager(BasicAction ba, Flasm flasm) {
		
		try {
			if (!basicActionHandler.get(flasm.conversion).containsKey(ba)) {
				System.out.println("Unknow BasicAction " + flasm.conversion + " " + ba.toString());
				return null;
			}
			
			Method m  = basicActionHandler.get(flasm.conversion).get(ba);
			
			return (String)m.invoke(String.class, flasm);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
