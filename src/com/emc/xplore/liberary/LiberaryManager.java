package com.emc.xplore.liberary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LiberaryManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	// a string or a phrase
	public void addString(String words) throws IOException{
		String filename = words.trim() + System.currentTimeMillis() +".dic";
		File file =  new File(filename);
		if(!file.exists()){
			file.createNewFile();
		}
		RandomAccessFile random = new RandomAccessFile(filename, "rw" );	
		random.writeBytes(words);
		random.close();
		
		addToTempDictionary(filename, StringLiberary.dictionaryPath);
	}
	
	private static void addToTempDictionary(String filename , String dictionaryPath) throws IOException {
		Dictionary d = new PlainTextDictionary(new FileReader( new File(filename )));

    	//lucene 3.4
//    	IndexWriterConfig iw = new IndexWriterConfig(Version.LUCENE_34,new SimpleAnalyzer(Version.LUCENE_34));
    	//lucene 3.6
    	IndexWriterConfig iw = new IndexWriterConfig(Version.LUCENE_36,new SimpleAnalyzer(Version.LUCENE_36));
    	SpellChecker sc = new SpellChecker(FSDirectory.open(new File(dictionaryPath )),new LevensteinDistance());

		sc.indexDictionary( d , iw,  true);  

	}

	public void addDictDocument(String filename) throws IOException{
		String filename1 = filename.trim() + System.currentTimeMillis() +".dic";
		File file =  new File(filename1);
		if(!file.exists()){
			file.createNewFile();
		}
		RandomAccessFile random = new RandomAccessFile(filename1, "rw" );	

		File file1 =  new File(filename);
		if(!file1.exists()){
			System.err.println("file:"+filename+" not found");
			return;
		}
		FileReader fileread = new FileReader(file1);
		BufferedReader bufread = new BufferedReader(fileread);
		String resource ="";
		while ((resource = bufread.readLine()) != null) {
			resource = resource.trim();
			for(int i =0 ; i < resource.length() ; i ++){
		        String separator = "!?.<>";

		        StringTokenizer st = new StringTokenizer( resource, separator, true );

		        while ( st.hasMoreTokens() ) {
		            String token = st.nextToken();
		            if ( token.length() == 1 && separator.indexOf( token.charAt( 0 ) ) >= 0 ) {
		                System.out.println( "special char:" + token );
		            }
		            else {
						random.writeBytes(resource.trim());
		            }
		        }				
			}
		}
		
		addToTempDictionary(filename1, StringLiberary.dictionaryPath);

	}

}
