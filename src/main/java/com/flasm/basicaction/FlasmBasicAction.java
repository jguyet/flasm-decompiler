package com.flasm.basicaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.flagstone.transform.action.BasicAction;
import com.flasm.FlasmConversion;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlasmBasicAction {
	public BasicAction value();
	
	public FlasmConversion conversion();
}
