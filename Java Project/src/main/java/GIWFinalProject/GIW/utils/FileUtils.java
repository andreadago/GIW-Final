package GIWFinalProject.GIW.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Andrea
 *
 */
public class FileUtils {

	/***
	 * Writes a .txt file
	 * @param stringToWrite
	 * @param fileName
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public static void writeFile(String stringToWrite, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		writer.println(stringToWrite);
		writer.close();
	}

	/***
	 * Returns a BufferedReader of a file
	 * @param path
	 * @return BufferedReader
	 * @throws FileNotFoundException
	 */
	public static BufferedReader readFile(String path) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		return br;
	}


	public static void appendWrite(String stringToWrite, String fileName) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		out.print(stringToWrite);
		out.close();
	}

}
