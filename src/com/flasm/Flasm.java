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

public class Flasm {

	private List<Action> actions;
	private Table table;
	private List<Object> stack = new ArrayList<Object>();
	
	public String as = "";
	public Map<String, Object> objects = new HashMap<String, Object>();
	
	
	public Flasm(DoAction doaction) {
		this.actions = doaction.getActions();
		this.table = (Table)doaction.getActions().get(0);
		
		for (String s : this.table.getValues()) {
			//System.out.println(s);
		}
	}
	
	private Object currentVar;
	
	public String parse() {
    	actions.remove(0);
    	for (Action a : actions) {
    		if (a instanceof BasicAction) {
    			
    			if (((BasicAction)a) == BasicAction.SET_VARIABLE) {
    				
    				Object s1 = this.stack.get(0);
    				this.stack.remove(0);
    				Object s2 = this.stack.get(0);
    				this.stack.remove(0);
    				
    				if (s2 instanceof String) {
    					s2 = "\"" + ((String)s2).replace("'", "\\'") + "\"";
    				}
    				objects.put(s1.toString(), s2);
    				System.out.println(s1.toString() + " = " + s2.toString() + ";");
    				this.as += s1.toString() + " = " + s2.toString() + ";" + System.lineSeparator();
    			} else if (((BasicAction)a) == BasicAction.END) {
    				//todo
    			} else if (((BasicAction)a) == BasicAction.NAMED_OBJECT) {
    				Object s1 = this.stack.get(0);
    				this.stack.remove(0);
    				Object s2 = this.stack.get(0);
    				this.stack.remove(0);
    				Object s3 = this.stack.get(0);
    				this.stack.remove(0);
    				
    				this.stack.add(s1);
    				this.stack.add(new asClass("new Object()"));
    			} else if (((BasicAction)a) == BasicAction.GET_VARIABLE) {
    				Object s1 = this.stack.get(this.stack.size() - 1);
    				this.stack.remove(this.stack.size() - 1);
	    			this.currentVar = s1;
	    		} else if (((BasicAction)a) == BasicAction.NEW_ARRAY) {
	    			int size = (int)this.stack.get(this.stack.size() - 1);
	    			this.stack.remove(this.stack.size() - 1);
	    			ArrayList<Object> array = new ArrayList<Object>();
	    			
	    			for (;size > 0; size--) {
	    				Object s1 = this.stack.get(this.stack.size() - 1);
	    				this.stack.remove(this.stack.size() - 1);
	    				array.add(s1);
	    			}
	    			this.stack.add(array);
	    			//System.out.println("NEW_ARRAY :" + array.toString());
	    		} else if (((BasicAction)a) == BasicAction.NEW_OBJECT) {
	    			int size = (int)this.stack.get(this.stack.size() - 1);
	    			this.stack.remove(this.stack.size() - 1);
	    			JSONObject obj = new JSONObject();
	    			for (;size > 0; size--) {
	    				Object s2 = this.stack.get(this.stack.size() - 1);
	    				this.stack.remove(this.stack.size() - 1);
	    				Object s1 = this.stack.get(this.stack.size() - 1);
	    				this.stack.remove(this.stack.size() - 1);
	    				obj.put((String)s1, s2);
	    			}
	    			this.stack.add(obj);
	    			//System.out.println("NEW_OBJECT :" + result);
	    		} else if (((BasicAction)a) == BasicAction.SET_ATTRIBUTE) {//SetMember
	    			Object value = this.stack.get(this.stack.size() - 1);
	    			this.stack.remove(this.stack.size() - 1);
	    			
	    			Object index = this.stack.get(this.stack.size() - 1);
	    			this.stack.remove(this.stack.size() - 1);
	    			
	    			objects.put(this.currentVar.toString() + "[" + index + "]", value);
	    			this.as += this.currentVar.toString() + "[" + index + "]" + " = " + value + System.lineSeparator();
	    			System.out.println(this.currentVar.toString() + "[" + index + "]" + " = " + value);
	    		} else if (((BasicAction)a) == BasicAction.GET_ATTRIBUTE) {//GetMember
	    			Object value = this.stack.get(this.stack.size() - 1);
	    			this.stack.remove(this.stack.size() - 1);
	    			
	    			this.stack.add(this.currentVar.toString() + "." + value.toString());
	    			//System.out.println(this.currentVar.toString() + "." + value);
	    		} else if (((BasicAction)a) == BasicAction.EXECUTE_METHOD) {
	    			Object method = this.stack.get(this.stack.size() - 1);
	    			this.stack.remove(this.stack.size() - 1);
	    			Object value = this.stack.get(this.stack.size() - 1);
	    			this.stack.remove(this.stack.size() - 1);
	    			int sizeArgs = (int)this.stack.get(this.stack.size() - 1);
	    			this.stack.remove(this.stack.size() - 1);
	    			
	    			String args = "";
	    			
	    			for (int i = 0; i < sizeArgs; i++) {
	    				Object arg = this.stack.get(this.stack.size() - 1);
		    			this.stack.remove(this.stack.size() - 1);
		    			if (!args.isEmpty()) {
		    				args += ", ";
		    			}
		    			args += arg.toString();
	    			}
	    			this.stack.add(value.toString() + "." + method.toString() + "(" + args + ");");
	    			this.as += value.toString() + "." + method.toString() + "(" + args + ");" + System.lineSeparator();
	    			System.out.println(value.toString() + "." + method.toString() + "(" + args + ");");
	    		} else if (((BasicAction)a) == BasicAction.POP) {
	    			this.stack.remove(this.stack.size() - 1);
	    		} else {
	    			System.out.println("Stack unknow BasicAction " + a.getClass() + "  " + a.toString());
	    		}
    		}
    		else if (a instanceof Push) {
    			this.push(((Push)a).getValues());
    		} else {
				System.out.println("Stack unknow Action " + a.getClass() + "  " + a.toString());
			}
    	}
    	return (this.as);
	}
	
	public void push(List<Object> lst) {
		for (Object o : lst) {
			if (o instanceof TableIndex) {
				int index = ((TableIndex)o).getIndex();
				
				stack.add(table.getValues().get(index));
				//System.out.println("Stack PUSH0 " + o.getClass() + "  " + table.getValues().get(index));
			} else {
				stack.add(o);
				//System.out.println("Stack PUSH1 " + o.getClass() + "  " + o);
			}
		}
	}
	
	public final class asClass {
		
		private String value;
		
		public asClass(String name) {
			this.value = name;
		}
		
		public String toString() {
			return this.value;
		}
		
	}
}
