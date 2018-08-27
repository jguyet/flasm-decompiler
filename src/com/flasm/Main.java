package com.flasm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.DataFormatException;

import com.flagstone.transform.DoAction;
import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieTag;
import com.flagstone.transform.action.Action;
import com.flagstone.transform.action.BasicAction;
import com.flagstone.transform.action.Push;
import com.flagstone.transform.action.Table;
import com.flagstone.transform.action.TableIndex;
import com.flagstone.transform.datatype.Color;
import com.flagstone.transform.text.DefineText;

public class Main {

	public static void main(String[] args) throws DataFormatException, IOException {
		Movie m = new Movie();
		
		m.decodeFromFile(new File("12270_0909301141X.swf"));
		
		for (MovieTag mt : m.getObjects()) {
            if (mt instanceof DoAction) {
            	DoAction d = ((DoAction)mt);
            	
            	Flasm stack = new Flasm(d);
            	
            	String as = stack.parse();
            	
            	Files.write(Paths.get("./result.txt"), as.getBytes());
            }
        }
	}

}
