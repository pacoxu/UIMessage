package com.emc.xplore.liberary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternMatch {
	
	
	
	public int match(String part,String docText){
		
		Pattern mPatterns = Pattern.compile(part);
	    Matcher matcher = mPatterns.matcher(docText);
	    int n = 0; 
	    int start = 0;
	    int end = 0;
	    while (matcher.find()) {
	    	start = matcher.start();
            end = matcher.end();
            System.out.println(start + " " + end);
            n++;
        }
	    return n;
	}
}
