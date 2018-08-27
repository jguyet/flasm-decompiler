package com.flasm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;

import com.flagstone.transform.DoAction;
import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieTag;

public class Main {

	public static void main(String[] args) throws DataFormatException, IOException {
		Movie m = new Movie();
		
		m.decodeFromFile(new File("lol.swf"));
		
		for (MovieTag mt : m.getObjects()) {
            if (mt instanceof DoAction) {
            	DoAction d = ((DoAction)mt);
            	
            	Flasm stack = new Flasm(d);
            	
            	stack.parse(FlasmConversion.PCODE);
            	stack.parse(FlasmConversion.AS);
            	
            	Files.write(Paths.get("./as.txt"), stack.as.getBytes());
            	Files.write(Paths.get("./flasm.txt"), stack.pcode.getBytes());
            }
        }
	}
}
