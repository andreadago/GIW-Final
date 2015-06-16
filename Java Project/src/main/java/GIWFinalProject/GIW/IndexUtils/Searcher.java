package GIWFinalProject.GIW.IndexUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import GIWFinalProject.GIW.utils.FileUtils;
/**
 * 
 * @author Daniele
 *
 */
public class Searcher {
	final static String INDEX_DIRECTORY = "/home/nielo/Scrivania/AgiwProject/index/";
	final static String INDEX2_DIRECTORY = "/home/nielo/Scrivania/AgiwProject/index2/";
	static HashSet<String> found=new HashSet<String>();
	
	public static void search(String line){


		try{

			int hitsPerPage = 50;
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			TopScoreDocCollector collector2= TopScoreDocCollector.create(hitsPerPage, true);

			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(INDEX_DIRECTORY)));
			IndexReader reader2 = DirectoryReader.open(FSDirectory.open(new File(INDEX2_DIRECTORY)));
			IndexSearcher searcher = new IndexSearcher(reader);
			IndexSearcher searcher2 = new IndexSearcher(reader2);
			String[] temp=line.split("\t");
			String[] temp1=temp[1].split(" and ");

			String title=temp[0].toLowerCase();
			
			System.out.println(title);

			if(!found.contains(title)){
				if(temp1.length>1){


					for(int i=0;i<temp1.length;i++){
						searcher.search(DirectorQuery(temp1[i]), collector);
					}

				}else{//Caso in cui il regista è unico

					searcher.search(DirectorQuery(temp1[0]), collector);

				}
				
				Query q = new QueryParser(Version.LUCENE_4_9, "title", analyzer).parse(title);
				searcher2.search(q,collector2);
				ScoreDoc[] hitsDirector = collector.topDocs().scoreDocs;
				ScoreDoc[] hitsFilm = collector2.topDocs().scoreDocs;
				String stringToWrite="";



				System.out.println("Found " + hitsFilm.length + " hits.");
				if(hitsFilm.length!=0){
					//for(int i=0;i<hitsFilm.length;++i) {
						
						//System.out.println((i + 1) + ". " + d.get("code") + "\t" + d.get("title")+"\t");
					//}
					int docId = hitsFilm[0].doc;
					Document d = searcher2.doc(docId);
					stringToWrite+=d.get("code")+" "+d.get("title")+"\t";
				}else{
					stringToWrite="null"+"\t";
				}

				System.out.println("Found " + hitsDirector.length + " hits.");
				if(hitsDirector.length==0){
					for(int i=0;i<temp1.length;i++){
						stringToWrite+=temp1[i]+"\t";
					}
					stringToWrite+="\n";
					FileUtils.appendWrite(stringToWrite,"output2");

				}else{
					if(hitsDirector.length>1){
						for(int i=0;i<hitsDirector.length;++i) {
							int docId = hitsDirector[i].doc;
							Document d = searcher.doc(docId);
							stringToWrite+=d.get("code")+" "+d.get("name")+" "+d.get("surname")+"\t";
							System.out.println((i + 1) + ". " + d.get("code") + "\t" + d.get("name")+"\t"+d.get("surname"));
						}
						stringToWrite+="\n";
						FileUtils.appendWrite(stringToWrite,"output");
						
					}else{
						int docId = hitsDirector[0].doc;
						Document d = searcher.doc(docId);
						stringToWrite+=d.get("code")+" "+d.get("name")+" "+d.get("surname")+"\n";
						FileUtils.appendWrite(stringToWrite,"output");
					}
					
					/*
					for(int i=0;i<hits.length;++i) {
						int docId = hits[i].doc;
						Document d = searcher.doc(docId);
						stringToWrite+=d.get("code")+" "+d.get("name")+" "+d.get("surname")+"\n";
						System.out.println((i + 1) + ". " + d.get("code") + "\t" + d.get("name")+"\t"+d.get("surname"));

					}*/
					
				}
				found.add(title);

			}





		}catch(Exception e){
			System.out.println(e.getMessage());
		}

	}
	private static BooleanQuery DirectorQuery(String direct)throws Exception{
		String [] director=direct.split(" ");

		if(director.length>1){
			String surnam=director[director.length-1];
			String surname=surnam.toLowerCase().replace(" ", ""); 

			String name="";
			for(int i =0; i<director.length-1; i++)
				name+=director[i]+" ";
			name=name.toLowerCase();
			String nome=name.toLowerCase().replace(" ", "");


			Query query1=new TermQuery(new Term("surname",surname));
			Query query2=new TermQuery(new Term("name", nome));
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(query1, BooleanClause.Occur.MUST);
			booleanQuery.add(query2, BooleanClause.Occur.MUST);

			return booleanQuery;

		}else{//caso in cui è presente solo il cognome
			String surname=director[0].toLowerCase().replace(" ", "");
			Query query1=new TermQuery(new Term("surname", surname));
			Query query2=new TermQuery(new Term("name", surname));
			
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(query1, BooleanClause.Occur.MUST);
			booleanQuery.add(query2, BooleanClause.Occur.MUST);
			return booleanQuery;
		}

	}


}
