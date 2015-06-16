package GIWFinalProject.GIW.IndexUtils;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;



import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * @author Daniele
 *
 */
public class IndexCreator {

	public static void createIndexes(){
		try{
			String path="/home/nielo/Scrivania/AgiwProject/swaf/filmdirector.tsv";
			String path2="/home/nielo/Scrivania/input_film_no_object_first50.tsv";
			final String INDEX_DIRECTORY = "/home/nielo/Scrivania/AgiwProject/index/";
			final String INDEX2_DIRECTORY = "/home/nielo/Scrivania/AgiwProject/index2/";
			
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);

			Directory index = FSDirectory.open(new File(INDEX_DIRECTORY));


			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
			config.setOpenMode(OpenMode.CREATE);

			IndexWriter writer = new IndexWriter(index, config);

			BufferedReader br = new BufferedReader(new FileReader(path));
			String current;
			while((current=br.readLine())!=null){
				String [] regista=current.split("\t");
				String [] n=regista[1].split(" ");

				Document doc=new Document();
				Field code = new StringField("code",regista[0], Field.Store.YES);
				Field name=null;
				Field surname;
				
				if(n.length>1){
					String nome="";
					for(int i =0; i<n.length-1; i++)
						nome+=n[i]+" ";
					name = new TextField("name",nome, Field.Store.YES);
					
					surname= new TextField("surname",n[n.length-1], Field.Store.YES);
				}else{
					name = new TextField("name",n[0], Field.Store.YES);
					surname= new TextField("surname",n[0], Field.Store.YES);
				}

				doc.add(code);
				
				doc.add(surname);
				doc.add(name);

				writer.addDocument(doc);

				System.out.println("codice "+regista[0]+" nome: "+regista[1]);
			}
			writer.close();
			br.close();
			System.out.println("fine registi");
			
			//inizio secondo indice
			StandardAnalyzer analyzer2 = new StandardAnalyzer(Version.LUCENE_4_9);
			Directory index2 = FSDirectory.open(new File(INDEX2_DIRECTORY));
			IndexWriterConfig config2 = new IndexWriterConfig(Version.LUCENE_4_9, analyzer2);
			config2.setOpenMode(OpenMode.CREATE);

			IndexWriter writer2 = new IndexWriter(index2, config2);
			BufferedReader br2 = new BufferedReader(new FileReader(path2));
			String current2;
			while((current2=br2.readLine())!=null){
				String [] film=current2.split("\t");
				//String [] n=regista[1].split(" ");

				Document doc=new Document();
				Field code = new StringField("code",film[0], Field.Store.YES);
				
				
				
				doc.add(code);
				Field title=new TextField("title",film[1],Field.Store.YES);
				doc.add(title);
				
				writer2.addDocument(doc);

				System.out.println("codice "+film[0]+" titolo: "+film[1]);
			}
			writer2.close();
			br2.close();

			System.out.println("fine");
	 
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}

