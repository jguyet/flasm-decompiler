package com.flasm.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.flagstone.transform.action.Action;
import com.flasm.Flasm;
import com.flasm.FlasmConversion;
import com.flasm.action.conversion.ASCompileConversionActionManager;
import com.flasm.action.conversion.PCODECompileConversionActionManager;

public class FlasmCompileActionFactory {

	public static Map<String, Method> ASactionHandler = new HashMap<String, Method>();
	public static Map<String, Method> PCODEactionHandler = new HashMap<String, Method>();
	public static Map<FlasmConversion, Map<String, Method>> actionHandler = new HashMap<FlasmConversion, Map<String, Method>>();

	/**
	 * load all method on directory org.fixme.router.servers.broker.handler with MethodMessageHandler anotation
	 */
	static//load once
	{
		Method[] allASmethods = ASCompileConversionActionManager.class.getDeclaredMethods();
		Method[] allFLASMmethods = PCODECompileConversionActionManager.class.getDeclaredMethods();
		
		for (Method m : allASmethods)
		{
			Annotation[] annots = m.getAnnotations();
			
			if (annots.length < 0)
				continue ;
			for (Annotation a : annots)
			{
				if (!(a instanceof FlasmAction))
					continue ;
				ASactionHandler.put(((FlasmAction)a).value(), m);
			}
		}
		
		for (Method m : allFLASMmethods)
		{
			Annotation[] annots = m.getAnnotations();
			
			if (annots.length < 0)
				continue ;
			for (Annotation a : annots)
			{
				if (!(a instanceof FlasmAction))
					continue ;
				PCODEactionHandler.put(((FlasmAction)a).value(), m);
			}
		}
		
		actionHandler.put(FlasmConversion.AS, ASactionHandler);
		actionHandler.put(FlasmConversion.PCODE, PCODEactionHandler);
	}
	
	public static String executeActionManager(Action a, Flasm flasm) {
		
		try {
			if (!actionHandler.get(flasm.conversion).containsKey(a.getActionName())) {
				System.out.println("Unknow Action " + flasm.conversion + " " + a.getActionName());
				return null;
			}
			
			Method m  = actionHandler.get(flasm.conversion).get(a.getActionName());
			
			return (String)m.invoke(String.class, a, flasm);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
