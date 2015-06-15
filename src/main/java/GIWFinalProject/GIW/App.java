package GIWFinalProject.GIW;

import java.io.BufferedReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;


public class App 
{
	static String serializedClassifier = "C:\\Users\\Andrea\\Downloads\\stanford-ner-2014-08-27\\stanford-ner-2014-08-27\\classifiers\\english.all.3class.distsim.crf.ser.gz";


	public static void main( String[] args ) throws Exception
	{
		//Language model path
		//    	String path = "C:\\Users\\Andrea\\Documents\\Università\\GIW\\Highfive\\directed_by_LM100.tsv";

		//		String path = "C:\\Users\\Andrea\\Documents\\Università\\GIW\\Highfive\\triples_film_director_200.tsv";
		//		String path = "C:\\Users\\Andrea\\Nuova cartella\\GIW\\films2directors";

		String r = "film.film.directed_by"; 

		//Get the language model for the query
		String path = "C:\\Users\\Andrea\\Documents\\Università\\GIW\\Highfive\\Queries.txt";
		BufferedReader br = FileUtils.readFile(path);

		String path2 = "C:\\Users\\Andrea\\Documents\\Università\\GIW\\Highfive\\directed_by_LM100.tsv";
		BufferedReader br2 = FileUtils.readFile(path2);

		String currentLine;
		String currentLine2;



		//		/*PART 2*/
		//
		//		String path3 = "C:\\Users\\Andrea\\Documents\\Università\\GIW\\Highfive\\input_film_no_object_last50.tsv";
		//		BufferedReader br3 = FileUtils.readFile(path3);
		//
		//		String path4 = "C:\\Users\\Andrea\\Documents\\Università\\GIW\\Highfive\\results10000queries.txt";
		//
		//		String toWrite = "";
		//		while((currentLine = br3.readLine())!=null) {
		//			String mid1 = currentLine.split("\t")[0]; 
		//			String film = currentLine.split("\t")[1];
		//			BufferedReader br4 = FileUtils.readFile(path4);
		//			while((currentLine2 = br4.readLine())!=null) {
		//				String lm = currentLine2.split("\\t")[0];
		//				toWrite += mid1 + "\t" + film + "\t" + lm + "\n";
		//			}
		//			br4.close();
		//		}
		//		br3.close();
		//		FileUtils.writeFile(toWrite, "QueriesLast50.txt");


		//		String toWrite = "";
		//		while((currentLine = br.readLine())!=null) {
		//			if(currentLine.contains(r)) {
		//				
		//				currentLine = currentLine.replace("\t", "\\");
		//				int index = currentLine.indexOf("\\");
		//				int index2 = currentLine.indexOf("\\", index + 1);
		//				String subj = currentLine.substring(index+1, index2);
		//				
		//				int index3 = currentLine.indexOf("\\", index2 + 1);
		//				int index4 = currentLine.indexOf("\\", index3 + 1);
		//				String obj = currentLine.substring(index3+1, index4);
		//				
		//				
		//				//System.out.println(currentLine);
		//				toWrite += subj + "\\" + obj +"\n";
		//				
		//			}
		//		}
		//		FileUtils.writeFile(toWrite, "films2directors");
		//		br.close();
		//		
		//		FileUtils.appendWrite("BARAGAG", "C:\\Users\\Andrea\\Nuova cartella\\GIW\\films2directors");

		BingSearch search = new BingSearch();
		int i = 1;

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);

		Date timeStart = new Date();

		//		/*PART 1*/
		//		while((currentLine = br.readLine())!=null) {
		//			int index = currentLine.indexOf("\\");
		//			String subj = currentLine.substring(0, index);
		//			int index2 = currentLine.indexOf("\\", index + 1);
		//			String lm = currentLine.substring(index+1, index2);
		//			String obj = currentLine.substring(index2 + 1);
		//
		//			if(i==10001)
		//				break;
		//
		//			if(i >= 7901) {
		//				search.getBing(subj, lm, obj, classifier);
		//			}
		//
		//			i++;
		//
		//		}

		/*PART 2*/

		String path3 = "C:\\Users\\Andrea\\Nuova cartella\\GIW\\QueriesLast50.txt";

		BufferedReader br3 = FileUtils.readFile(path3);

		while((currentLine = br3.readLine())!=null) {
			String[] split = currentLine.split("\t");
			String mid1 = split[0];
			String subj = split[1];
			String lm = split[2];
			String obj = null;

			if(i==2501)
				break;

			if(i >= 782) {
				search.getBing(mid1, subj, lm, obj, classifier);
			}

			i++;

		}

		//		//		String s = "The Lotus Eaters; Directed by: Paul Shapiro: Written by: Peggy Thompson: Starring: R. H. Thomson; Sheila McCarthy; ... The Lotus Eaters is a Canadian drama film ...";
		//		String s = "The Hoaxters (The Hoaxters), regia di Herman Hoffman; Navajo (Navajo), regia di Norman Foster; Miglior cortometraggio [modifica | modifica wikitesto]";
		//		String subString = "regia";
		//		if(s.toLowerCase().contains(subString)) {
		//			//if(s.contains(subject.toLowerCase()))
		//			int index = s.toLowerCase().indexOf(subString) + subString.length();
		//			String afterSub = s.substring(index + 1);
		//			//			String serializedClassifier = "C:\\Users\\Andrea\\Downloads\\stanford-ner-2014-08-27\\stanford-ner-2014-08-27\\classifiers\\english.all.3class.distsim.crf.ser.gz";
		//
		//			if (args.length > 0) {
		//				serializedClassifier = args[0];
		//			}
		//
		//
		//
		//			System.out.print(classifier.classifyToString(afterSub, "slashTags", false));
		//			String classified = classifier.classifyWithInlineXML(afterSub);
		//			System.out.println(classified);
		//
		//			Pattern p = Pattern.compile("<PERSON>.*<\\/PERSON>");
		//			Matcher matcher = p.matcher(classified);
		//
		//			String[] split = classified.split("<\\/PERSON>");
		//			for(String k : split) {
		//				if(k.contains("<PERSON>")) {
		//					//					System.out.println(k);
		//					int index2 = k.indexOf("<PERSON>");
		//
		//					System.out.println(k.substring(index2+ "<PERSON>".length()));
		//				}
		//			}
		//
		//
		//			while (matcher.find()) 
		//			{
		//				System.out.println(matcher.group()+"");
		//			}
		//
		//		}

		Date timeStop = new Date();
		long diff = timeStop.getTime() - timeStart.getTime();

		long diffSeconds = diff / 1000 ;
		System.out.println("Tempo: " + diffSeconds + "s");

		//search.getBing();
	}
}
