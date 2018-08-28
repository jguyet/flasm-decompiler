package com.flasm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;

import com.flagstone.transform.DoABC;
import com.flagstone.transform.DoAction;
import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieAttributes;
import com.flagstone.transform.MovieTag;

public class Main {

	public static void main(String[] args) throws DataFormatException, IOException {
		Movie m = new Movie();
		
		m.decodeFromFile(new File("DofusInvoker.swf"));
		
		DoAction d = null;
		String s = "";
		
		for (MovieTag mt : m.getObjects()) {
            if (mt instanceof DoAction) {
            	d = ((DoAction)mt);
            	break ;
            } else if (mt instanceof MovieAttributes) {
            	//DoABC tmp = ((DoABC)mt);
            	
            	//DoAction d = new DoAction(tmp);
            	//tmp.
            	System.out.println(mt.getClass().getName() + " " + mt.toString());
			} else {
            	s += mt.getClass().getName() + " " + mt.toString() + System.lineSeparator();
            	//System.out.println(mt.getClass().getName() + " " + mt.toString());
            }
        }
		
		Files.write(Paths.get("./content.txt"), s.getBytes());
		
		if (d != null) {
			Flasm stack = new Flasm(d);
        	
        	//stack.parse(FlasmConversion.PCODE);
        	stack.parse(FlasmConversion.AS);
        	
        	Files.write(Paths.get("./as.txt"), stack.as.getBytes());
        	Files.write(Paths.get("./flasm.txt"), stack.pcode.getBytes());
		} else {
			System.out.println("DoAction dosen't exists");
		}
	}
}
