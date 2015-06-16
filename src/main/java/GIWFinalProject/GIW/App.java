package GIWFinalProject.GIW;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LMiddleAnnotation;


public class App 
{
	static String serializedClassifier = "C:\\Users\\Andrea\\Downloads\\stanford-ner-2014-08-27\\stanford-ner-2014-08-27\\classifiers\\english.all.3class.distsim.crf.ser.gz";


	public static void main( String[] args ) throws Exception
	{
		BingSearch searcher = new BingSearch();

		/*PART 1: LANGUAGE MODEL EVALUATION*/
		String path = "Queries.txt";
		lmQuery(path, searcher);

		/*PART 2: MISSING VALUES*/
		String path2 = "QueriesLast50.txt"; //EXAMPLE: Queries of last 50 subject with no object

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		missingValuesQuery(path2, searcher, classifier);


		System.out.println("Done.");
	}

	/**
	 * Executes query to get missing values. For the Second Part.
	 * @param queryPath of the query file formatted: mid1\tsubject\tlanguageModel
	 * @param searcher
	 * @param Stanford NER classifier
	 * @throws Exception 
	 * @throws IOException 
	 */

	public static void missingValuesQuery(String queryPath, BingSearch searcher, AbstractSequenceClassifier<CoreLabel> classifier) throws IOException, Exception {

		BufferedReader br = FileUtils.readFile(queryPath);
		String currentLine;

		while((currentLine = br.readLine())!=null) {
			String[] split = currentLine.split("\t");
			String mid1 = split[0];
			String subj = split[1];
			String lm = split[2];
			String obj = null;

			searcher.getBing(subj, lm, obj, classifier);

		}

	}

	/**
	 * Executes query with language model. For the First Part.
	 * @param queryPath of the query file formatted: subject\languageModel\object
	 * @param searcher
	 * @throws Exception 
	 * @throws IOException 
	 */

	public static void lmQuery(String queryPath, BingSearch searcher) throws IOException, Exception {

		BufferedReader br = FileUtils.readFile(queryPath);
		String currentLine;

		while((currentLine = br.readLine())!=null) {
			int index = currentLine.indexOf("\\");
			String subj = currentLine.substring(0, index);
			int index2 = currentLine.indexOf("\\", index + 1);
			String lm = currentLine.substring(index+1, index2);
			String obj = currentLine.substring(index2 + 1);

			searcher.getBing(subj, lm, obj, null);
		}

	}
	

}
