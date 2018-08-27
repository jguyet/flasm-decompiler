package com.flasm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.flagstone.transform.DoAction;
import com.flagstone.transform.action.Action;
import com.flagstone.transform.action.BasicAction;
import com.flagstone.transform.action.Push;
import com.flagstone.transform.action.Table;
import com.flagstone.transform.action.TableIndex;
import com.flasm.action.FlasmActionFactory;

public class Flasm {
	public FlasmConversion conversion;
	public Table table;
	public List<Object> stack = new ArrayList<Object>();
	public String as = "";
	public String pcode = "";
	
	private List<Action> actions;
	
	public Flasm(DoAction doaction) {
		this.actions = doaction.getActions();
	}
	
	public Object currentVar;
	
	public void parse(FlasmConversion conversion) {
		this.conversion = conversion;
		this.stack.clear();
    	for (Action a : actions) {
    		FlasmActionFactory.executeActionManager(a, this);
    	}
	}
	
	public static final class asClass {
		
		private String value;
		
		public asClass(String name) {
			this.value = name;
		}
		
		public String toString() {
			return this.value;
		}	
	}
	
	public String getAS() {
		return this.as;
	}
	
	public String getPCODE() {
		return this.pcode;
	}
}
