package com.flasm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import com.flagstone.transform.DoAction;
import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieHeader;
import com.flagstone.transform.MovieTag;
import com.flagstone.transform.action.Action;
import com.flagstone.transform.action.Table;
import com.flasm.action.FlasmCompileActionFactory;
import com.flasm.action.FlasmDecompileActionFactory;
import com.google.common.collect.Lists;

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

    private String writeResourceToFile(String resourceName) throws IOException {
        ClassLoader loader = getClass().getClassLoader();
        InputStream configStream = loader.getResourceAsStream(resourceName);
        File tmpFile = new File("./.tmp");
        
        tmpFile.createNewFile();
        Files.copy(configStream, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return tmpFile.getAbsolutePath();
    }
	
	
	public void compile(FlasmConversion conversion, String swfContent) throws URISyntaxException, DataFormatException, IOException {
		this.conversion = conversion;
		
		Movie movie = new Movie();
		
		//URL blankResource = Flasm.class.getClassLoader().getResource("blank.swf");
		
		//System.out.println(blankResource);
		
		movie.decodeFromFile(new File(writeResourceToFile("com/resource/blank/blank.swf")));
		
		MovieHeader header = movie.getObject(MovieHeader.class);
		
		DoAction action = movie.getObject(DoAction.class);
		
		Table table = action.getAction(Table.class);
		
		//TODO: Writing swfContent to blank.swf
		for (String line : swfContent.split("\n")) {
			System.out.println(line);
			//FlasmCompileActionFactory.executeActionManager(line, this);
		}
		action.clearActions();
		action.add(table);
		
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
