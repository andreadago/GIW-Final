package GIWFinalProject.GIW;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;

public class BingSearch {

	/**
	 * Execute a query search 
	 * @param languageModel
	 * @throws Exception
	 */

	public static void getBing(String subject, String languageModel, String object, AbstractSequenceClassifier<CoreLabel> classifier) throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		String accountKey = ":Q7DBohTyS3b/WXF0uVAXkJ+C8Ng6hdabjCY+3dJ3Iz8=";
		byte[] accountKeyBytes = Base64.encodeBase64(accountKey.getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		String leftQuote = "&ldquo;";
		String rightQuote = "&rdquo;";

		String query = subject + " " + languageModel; // + " " +  object;
		String sub = languageModel;
		query = StringUtils.parseQueryInURL(query);


		try {

			HttpGet httpget;
			if(languageModel.contains("DATE")|| languageModel.contains("NUM")) { 
				query = "%22" + subject + "%22" + " " + "%22" + languageModel.replaceFirst("(DATE|NUM)", "") + "%22";
				query = StringUtils.parseQueryInURL(query);
				httpget = new HttpGet("https://api.datamarket.azure.com/Data.ashx/Bing/Search/Web?Query=%27" + query + "%27&$top=50&$format=Json");
			} else {
				httpget = new HttpGet("https://api.datamarket.azure.com/Data.ashx/Bing/Search/Web?Query=%27%22" + query + "%22%27&$top=50&$format=Json");
			}
			httpget.setHeader("Authorization", "Basic "+ accountKeyEnc);

			System.out.println("executing request " + languageModel + "\n" + httpget.getURI());

			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			System.out.println("----------------------------------------");
			
			String parsedResults = null;
			if(object != null) {
				parsedResults = StringUtils.parseResults(StringUtils.parseJSON(responseBody, subject, sub, object));
			} else {
				parsedResults = StringUtils.parseResults(StringUtils.parseJSON(responseBody, subject, sub, classifier));
			}

			/*Write a file with search results*/
			FileUtils.appendWrite(parsedResults, "SearchResults.txt");

			System.out.println(parsedResults);



		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			//do nothing
		}
		finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}
}
