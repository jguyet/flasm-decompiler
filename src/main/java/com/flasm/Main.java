package com.flasm;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;

import com.flagstone.transform.Background;
import com.flagstone.transform.DoABC;
import com.flagstone.transform.DoAction;
import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieAttributes;
import com.flagstone.transform.MovieHeader;
import com.flagstone.transform.MovieTag;
import com.flagstone.transform.datatype.Bounds;
import com.flagstone.transform.datatype.Color;

public class Main {

	public static void main(String[] args) throws DataFormatException, IOException, URISyntaxException {
		compile();
		decompile();
	}
	
	public static void compile() throws URISyntaxException, DataFormatException, IOException {
		Flasm stack = new Flasm();
		
		stack.compile(FlasmConversion.AS, getContent());
	}
	
	public static String getContent() throws DataFormatException, IOException {
		Movie m = new Movie();
		
		m.decodeFromFile(new File("items_fr_432.swf"));
		
		Flasm stack = new Flasm();
    	
		System.out.println("START DEC  :");
		long start = System.currentTimeMillis();
    	stack.decompile(FlasmConversion.AS, m.getObject(DoAction.class));
    	System.out.println("FINISH DEC : " + (System.currentTimeMillis() - start) + " MS");
    	return (stack.as);
	}
	
	public static void decompile() throws DataFormatException, IOException {
		
		Movie m = new Movie();
		
		m.decodeFromFile(new File("items_fr_432.swf"));
		
		DoAction d = null;
		String s = "";
		
		for (MovieTag mt : m.getObjects()) {
            if (mt instanceof DoAction) {
            	d = ((DoAction)mt);
            	break ;
            } else {
            	s += mt.getClass().getName() + " " + mt.toString() + System.lineSeparator();
            }
        }
		
		Files.write(Paths.get("./content.txt"), s.getBytes());
		
		if (d != null) {
			Flasm stack = new Flasm();
        	
        	stack.decompile(FlasmConversion.AS, d);
        	
        	Files.write(Paths.get("./as.txt"), stack.as.getBytes());
        	Files.write(Paths.get("./flasm.txt"), stack.pcode.getBytes());
		} else {
			System.out.println("DoAction doesn't exists");
		}
	}
}
