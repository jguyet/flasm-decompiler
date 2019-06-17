package com.flasm.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.flasm.FlasmConversion;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlasmAction {
	public String value();
	
	public FlasmConversion conversion();
}
