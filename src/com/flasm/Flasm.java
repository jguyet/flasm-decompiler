package com.flasm;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import com.flagstone.transform.DoAction;
import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieHeader;
import com.flagstone.transform.MovieTag;
import com.flagstone.transform.action.Action;
import com.flagstone.transform.action.Table;
import com.flasm.action.FlasmDecompileActionFactory;

public class Flasm {
	public FlasmConversion conversion;
	public Table table;
	public List<Object> stack = new ArrayList<Object>();
	public String as = "";
	public String pcode = "";
	private StringBuilder builder = new StringBuilder();
	
	private List<Action> actions = new ArrayList<Action>();
	
	public Flasm() {
		
	}
	
	public Object currentVar;
	
	public void decompile(FlasmConversion conversion, DoAction doaction) {
		this.actions = doaction.getActions();
		this.conversion = conversion;
		this.stack.clear();
    	for (Action a : actions) {
    		FlasmDecompileActionFactory.executeActionManager(a, this);
    	}
    	switch (this.conversion)
    	{
    		case AS:
    			this.as = this.builder.toString();
    			break ;
    		case PCODE:
    			this.pcode = this.as = this.builder.toString();
    			break ;
    		default:
    			break ;
    	}
	}
	
	
	public void compile(FlasmConversion conversion, String swfContent) throws URISyntaxException, DataFormatException, IOException {
		this.conversion = conversion;
		
		Movie movie = new Movie();
		
		URL blankResource = Flasm.class.getResource("/com/resource/blank/blank.swf");
		
		movie.decodeFromFile(new File(blankResource.getFile()));
		
		MovieHeader header = movie.getObject(MovieHeader.class);
		
		DoAction action = movie.getObject(DoAction.class);
		
		Table table = action.getAction(Table.class);
		
//		table.getValues().clear();
		
		String content = "";
		StringBuilder b = new StringBuilder();
		boolean word = false;
		System.out.println("START");
		for (int i = 0; i < swfContent.length(); i++) {
			if (swfContent.charAt(i) == '"') {
				if (i > 0 && swfContent.charAt(i - 1) == '\\') {
					
				} else {
					word = !word;
				}
			}
			if (!word && i > 3 && swfContent.charAt(i) == ' '
					&& swfContent.charAt(i - 3) == 'n'
					&& swfContent.charAt(i - 2) == 'e'
					&& swfContent.charAt(i - 1) == 'w') {
				b.append(swfContent.charAt(i));
				continue ;
			}
			if (!word && (swfContent.charAt(i) == ' '
					|| swfContent.charAt(i) == '\t'
					|| swfContent.charAt(i) == '\f'
					|| swfContent.charAt(i) == '\n'
					|| swfContent.charAt(i) == '\r'))
				continue ;
			b.append(swfContent.charAt(i));
		}
		System.out.println("FINISH");
		
		String[] parsable = b.toString().split(";");
		int o = 0;
		for (String s : parsable) {
			if (o >= 100) {
				break ;
			}
			int parenthese = 0;
			word = false;
			List<String> lst = new ArrayList<String>();
			StringBuilder currentword = new StringBuilder();
			for (int i = 0; i < s.length(); i++) {
				if (swfContent.charAt(i) == '"') {
					if (i > 0 && swfContent.charAt(i - 1) == '\\') {
						
					} else {
						word = !word;
					}
				}
				if (!word && s.charAt(i) == '(') {
					parenthese++;
				}
				if (!word && s.charAt(i) == ')') {
					parenthese--;
				}
				if (word || parenthese > 0) {
					currentword.append(s.charAt(i));
					continue ;
				}
				if (s.charAt(i) == '.') {
					lst.add(currentword.toString());
					currentword = new StringBuilder();
					continue ;
				}
				currentword.append(s.charAt(i));
			}
			lst.add(currentword.toString());
			
			System.out.println(lst.toString());
			o++;
		}
		
//		action.clearActions();
//		action.add(table);
		
		for (MovieTag mt : movie.getObjects()) {
            System.out.println(mt.getClass().getName() + " " + mt.toString());
        }
		
		try {
			movie.encodeToFile(new File("./compile.swf"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StringBuilder getBuilder() {
		return this.builder;
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
