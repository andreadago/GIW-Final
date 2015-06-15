package GIWFinalProject.GIW;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class StringUtils {

	/**
	 * @param Query string. 
	 * @return The string without white spaces (they're replaced by '%20').
	 */
	public static String parseQueryInURL(String query) {
		String parsedQuery = "";

		parsedQuery = query.replace(" ", "%20");

		if(parsedQuery.indexOf("&")>=0) {
			parsedQuery = parsedQuery.replace("&", "%26");
		}
		if(parsedQuery.indexOf(",")>=0) {
			parsedQuery = parsedQuery.replace(",", "%2C");
		}
		return parsedQuery;
	}

	/**
	 * @param The body response from an executed query in JSON, the substring that you want to find.
	 * @return The parsed body that contains the substring.
	 */

	public static String parseJSON(String responseBody, String subject, String subString, String object) {
		String parsedBody = "";
		JSONArray obj = new JSONObject(responseBody).getJSONObject("d").getJSONArray("results");
		//		JSONObject root = new JSONObject(obj.get("d"));
		//		JSONArray results = root.getJSONArray("results");
		for(int i = 0; i < obj.length(); i++) {
			JSONObject r = obj.getJSONObject(i);
			String s = r.getString("Description").toLowerCase();
			if(subString.contains("NUM")||subString.contains("DATE")) {
				if(subString.contains("NUM")) {
					String subString2 = null;
					subString2 = subString.replace("NUM", "").replaceFirst(" ", "");
					String NUM2 = ".*\\s(IX|IV|V?I{0,3}).?\\s";
					String NUM = ".*" + subject.toLowerCase() + "(\\s*(IX|IV|V?I{0,3}).?\\s)|(\\s*[0-9].?\\s)" + subString.replace("NUM","").replaceFirst(" ", "").toLowerCase() + object.toLowerCase() + ".*";

					if(s.matches(NUM)) {
						parsedBody += subString + "\t" + r.getString("Description")+"\n";
					}
				} else { //contains DATE
					String subString2 = null;
					subString2 = subString.replace("DATE", "");
					//					String ORD = "\\b\\d+th\\b";
					//					String DATE = "(\\b(january|february|march|april|may|june|july|august|september|october|november|december)\\b)|(\\b(jan|feb|marc|apr|may|jun|jul|aug|sep|oct|nov|dec)\\s"+ ORD +")";
					//					String DATE1 = "(([0-3]\\d|\\d)\\s+" + DATE + "\\b)|(\\b"+ DATE + "\\s+(\\d\\d\\d\\d|\\d\\d))";

					String ORD = "\\b\\d+th\\b";
					String D = "\\b(january|february|march|april|may|june|july|august|september|october|november|december)\\b";
					String D1 = "\\b(jan|feb|marc|apr|may|jun|jul|aug|sep|oct|nov|dec)\\s+"+ ORD;
					String DATE1 = "([0-3]\\d|\\d)\\s+" + D + "\\b";
					String DATE2 = "\\b"+ D + "\\s+(\\d\\d\\d\\d|\\d\\d)";

					//String DATE = ".*\\s[\\(]?([1-2]\\d\\d\\d)[\\)]?\\s.*";

					//					if(s.matches(D + "\\s+" + subString2.toLowerCase() + "\\s+" + object.toLowerCase()) 
					//							|| s.matches(D1+ "\\s+" + subString2.toLowerCase() + "\\s+" + object.toLowerCase()) 
					//							|| s.matches(DATE1 + "\\s+" + subString2.toLowerCase() + "\\s+" + object.toLowerCase()) 
					//							|| s.matches(DATE2 + "\\s+" + subString2.toLowerCase() + "\\s+" + object.toLowerCase())) {
					String DATE = ".*" + subject.toLowerCase() + "\\s*.?\\s*[\\(]?([1-2]\\d\\d\\d)[\\)]?\\s*.?\\s" + subString.replace("DATE","").replaceFirst(" ", "").toLowerCase() + "\\s*.?\\s+" + object.toLowerCase() + ".*";
					//					String DATE2 = ".*" + "The Hurt Locker".toLowerCase() + "\\s*.?\\s*[\\(]?([1-2]\\d\\d\\d)[\\)]?\\s*.?\\s" + "DATE directed by ".replace("DATE","").replaceFirst(" ", "").toLowerCase() + "Kathryn Bigelow".toLowerCase() + ".*";


					if(s.matches(DATE)) {
						parsedBody += subString + "\t" + r.getString("Description")+"\n";
					}
				}
			} else {
				if(s.contains(subString.toLowerCase() + " " + object.toLowerCase())) {
					//if(s.contains(subject.toLowerCase()))
					parsedBody += subString + "\t" + r.getString("Description")+"\n";
				}
			}
		}
		return parsedBody;
	}

	/**
	 * @param The body response from an executed query in JSON, the substring that you want to find. For the SECOND PART.
	 * @return The parsed body that contains the substring.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws ClassCastException 
	 */

	public static String parseJSON(String responseBody, String subject, String subString, AbstractSequenceClassifier<CoreLabel> classifier) throws ClassCastException, ClassNotFoundException, IOException {
		String parsedBody = "";
		JSONArray obj = new JSONObject(responseBody).getJSONObject("d").getJSONArray("results");

		for(int i = 0; i < obj.length(); i++) {
			JSONObject r = obj.getJSONObject(i);
			String s = r.getString("Description");
			if(subString.contains("NUM")||subString.contains("DATE")) {
				String afterSub = "";
				if(subString.contains("NUM")) {
					String NUM = ".*" + subject + "(\\s*(IX|IV|V?I{0,3}).?\\s)|(\\s*[0-9].?\\s)" + subString.replace("NUM","").replaceFirst(" ", "") + ".*";
					int index = NUM.indexOf(NUM.charAt(NUM.length()-1)); // index of the last char of the string from which try to get director

					if(s.matches(NUM)) {
						afterSub = s.toLowerCase().substring(index);

					}
				} else { //contains DATE		
					String DATE = ".*" + subject + "\\s*.?\\s*[\\(]?([1-2]\\d\\d\\d)[\\)]?\\s*.?\\s" + subString.replace("DATE","").replaceFirst(" ", "") + "\\s*.?\\s+" + ".*";
					int index = DATE.indexOf(DATE.charAt(DATE.length()-1)); // index of the last char of the string from which try to get director

					if(s.matches(DATE)) {
						afterSub = s.toLowerCase().substring(index);
					}
				}
				boolean nameFound = false;
				String classified = classifier.classifyWithInlineXML(afterSub);

				String[] split = classified.split("<\\/PERSON>");
				for(String k : split) {
					if(k.contains("<PERSON>")) {
						if(nameFound) {
							if(k.startsWith(" and") || k.startsWith("and")) {
								int index2 = k.indexOf("<PERSON>");
								
//								parsedBody = parsedBody.replace(parsedBody.charAt(parsedBody.lastIndexOf("\n")), ' ');
								parsedBody = parsedBody.substring(0, parsedBody.lastIndexOf("\n")-1);
								
								
								parsedBody += " and " + (k.substring(index2+ "<PERSON>".length())) + "\n";
								//									nameFound = true;
							}
						} else {
							//							System.out.println(k);
							int index2 = k.indexOf("<PERSON>");

							parsedBody += subject + "\t" + subString + "\t" + (k.substring(index2+ "<PERSON>".length())) + "\n";
							nameFound = true;
						}
					}
				}
			} else {
				if(s.contains(subject) && s.toLowerCase().contains(subString)) {
					//if(s.contains(subject.toLowerCase()))
					//int index = s.indexOf(subString);
					int index = s.toLowerCase().indexOf(subString) + subString.length();
					String afterSub = s.substring(index + 1);
					//					String serializedClassifier = "C:\\Users\\Andrea\\Downloads\\stanford-ner-2014-08-27\\stanford-ner-2014-08-27\\classifiers\\english.all.3class.distsim.crf.ser.gz";

					//					System.out.print(classifier.classifyToString(afterSub, "slashTags", false));
					String classified = classifier.classifyWithInlineXML(afterSub);
					//					System.out.println(classified);

					Pattern p = Pattern.compile("<PERSON>.*<\\/PERSON>");
					Matcher matcher = p.matcher(classified);

					boolean nameFound = false;

					String[] split = classified.split("<\\/PERSON>");
					for(String k : split) {
						if(k.contains("<PERSON>")) {
							if(nameFound) {
								if(k.startsWith(" and") || k.startsWith("and") || k.startsWith(" &") || k.startsWith("&")) {
									int index2 = k.indexOf("<PERSON>");
									
//									parsedBody = parsedBody.replace(parsedBody.charAt(parsedBody.lastIndexOf("\n")), ' ');
									parsedBody = parsedBody.substring(0, parsedBody.lastIndexOf("\n"));
									
									
									parsedBody += " and " + (k.substring(index2+ "<PERSON>".length())) + "\n";
									//									nameFound = true;
								}
							} else {
								//							System.out.println(k);
								int index2 = k.indexOf("<PERSON>");

								parsedBody += subject + "\t" + subString + "\t" + (k.substring(index2+ "<PERSON>".length())) + "\n";
								nameFound = true;
							}
						}
					}
					//					parsedBody += subject + "\t" + subString + "\t" + r.getString("Description")+"\n";
				}
			}
		}
		return parsedBody;
	}



	/**
	 * @param Results string. 
	 * @return The string without double quotes.
	 */
	public static String parseResults(String query) {
		String parsedResults = "";

		parsedResults = query.replace("\"", "\\\"");
		return parsedResults;
	}


}
