package org.project.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * To load files and resources.
 * 
 * @author Tim Holtkötter
 */
public class FileLoader {

	/**
	 * Loads a file as a String.
	 * 
	 * @param	String	path	The path of the file
	 * @return	String			The content of the file. Null if an error occurs.
	 */
	public static String loadFile(String path) {
		try {
			FileInputStream inputStream = new FileInputStream(new File(path));
			StringBuilder builder = new StringBuilder();
			
			int input;
			while((input = inputStream.read()) != -1)
				builder.append((char) input);
			
			inputStream.close();
			
			return builder.toString();
		} catch(IOException e) {
			return null;
		}
	}
}
