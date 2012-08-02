package com.emc.xplore.liberary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.annotation.Resource;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class StringLiberary {
	
	public static String microsoftResourceStirngsFilePath = "Microsoft Resource Strings.txt";
	public static String microsoftResourceStirngsPlainDictionary = "MicroStringsDict.txt";
	public static String dictionaryPath = "dict";
	
	public void createStringLiberary() throws IOException{
		String filename = microsoftResourceStirngsPlainDictionary;
		RandomAccessFile random = new RandomAccessFile(filename, "rw" );	
		File file = new File(microsoftResourceStirngsFilePath);
		FileReader fileread = new FileReader(file);
		BufferedReader bufread = new BufferedReader(fileread);
		String resource ="";
		while ((resource = bufread.readLine()) != null) {
        	if(resource.startsWith("<Seg L=EN-US>")){
        		resource = resource.substring(12).trim();
        		if(resource.endsWith("\"")){
            		resource = resource.substring(0,resource.length()-1);
        		}
        		int i;
        		for(i = 0 ; i < resource.length(); i++){
//        			if(resource.charAt(i) == ' '){
        				if(resource.trim().length() != i){
        	            	if(resource.contains("\\\\n")){
        	        			resource = resource.substring(i);
        	        			showAll(resource, random);
        	            	}else{
        	            		random.writeBytes(resource.substring(i+1).trim()+"\n");        
        	            	}
        				}
        				break;
//        			}
        		}
        	}
//        	else if(resourse.startsWith("<Seg L=EN-US># ")){
//            	random.writeBytes(resourse.substring(15));        		
//        	}
//        	else if(read.startsWith("<Seg L=ZH-CN>")){
//        		
//        	}        	
        }
    	
		


	}
	
	public void showAll(String resource, RandomAccessFile random) throws IOException{
		int s=0;
		int j;
		for(j = 0 ; j < resource.length(); j++){
			if(resource.charAt(j) == '\\'){
        		random.writeBytes(resource.substring(s+1,j).trim()+"\n");
        		for(int k = j; k < resource.length(); k++){
        			if(resource.charAt(k) == ' '){
        				s = k;
        				j = k;
        				break;
        			}
        		}
			}			
		}
		random.writeBytes(resource.substring(s+1,j).trim()+"\n");

	}

	public void createLuceneDictionary(){
		 try {
		    	Dictionary d = new PlainTextDictionary(new FileReader( new File(microsoftResourceStirngsPlainDictionary )));

		    	//lucene 3.4
//		    	IndexWriterConfig iw = new IndexWriterConfig(Version.LUCENE_34,new SimpleAnalyzer(Version.LUCENE_34));
		    	//lucene 3.6
		    	IndexWriterConfig iw = new IndexWriterConfig(Version.LUCENE_36,new SimpleAnalyzer(Version.LUCENE_36));
		    	SpellChecker sc = new SpellChecker(FSDirectory.open(new File(dictionaryPath )),new LevensteinDistance());

				//lucene 3.4
//				sc.indexDictionary( d );  
		    	//lucene 3.6
				sc.indexDictionary( d , iw,  false);  
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }	
	}
}
