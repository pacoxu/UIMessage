package com.emc.xplore.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.StringDistance;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.search.suggest.jaspell.JaspellLookup;

//lucene 3.6
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class StringLiberaryJFrame {

	private static String indexpath = "MicroStringsDict.txt";
	private static String dict = "dict";
	public static void main(String[] args){
		final JFrame f = new JFrame("Spell Checker");
		f.setVisible(true);
		f.setSize(500, 400);
		f.setLocation(400,200);
		f.setLayout(new BorderLayout());
		Container top = new Container();
		f.add(top,BorderLayout.NORTH);
		top.setLayout(new FlowLayout());

		JLabel input = new JLabel("Input:");
		top.add(input);
		final JTextField jt = new JTextField("docu");
		jt.setColumns(15);
		top.add(jt);
		JButton abc = new JButton("spell checker");
		top.add(abc);

		
		Container main = new Container();
		main.setLayout(new GridLayout(1,2));
		
		//Suggest
		Container allSuggest = new Container();
		allSuggest.setLayout(new GridLayout(1,2));
		
		Container sugg = new Container();
		sugg.setLayout(new BorderLayout());
		
		JLabel sugLable = new JLabel("Suggests by Jaspell:");
		sugg.add(sugLable, BorderLayout.NORTH );		
		final JTextArea sug = new JTextArea();
		sugg.add(sug ,BorderLayout.CENTER);
		sug.setBackground(Color.lightGray );
		
		allSuggest.add(sugg);

		
		//Spell
		Container allSpell = new Container();
		allSpell.setLayout(new GridLayout(1,1));
		


		Container spell2 = new Container();
		spell2.setLayout(new BorderLayout());
		
		JLabel spellLable2 = new JLabel("Spell Checker from Microsoft Resource String Liberary.");
		spell2.add(spellLable2, BorderLayout.NORTH );
		final JTextArea spellchecker2 = new JTextArea();
		spell2.add(spellchecker2 ,BorderLayout.CENTER);
	
		allSpell.add(spell2);
//		allSpell.add(spell3);
		
		main.add( allSpell, 0, 0);
		main.add(  allSuggest, 0, 1);

		f.add(main);
		
		f.show();
		
		jt.addKeyListener(
				new KeyListener() {
		    public void keyPressed(KeyEvent e) {}

		    public void keyReleased(KeyEvent e) {
		    	

				String inputs = jt.getText().toLowerCase();
		    	long start = System.currentTimeMillis();
				
				String autoComplete ="";
				try {
					autoComplete = suggest(inputs);
				} catch (IOException e1) {
					System.err.println("++");
				}
				long stop = System.currentTimeMillis();
				long time = (stop - start) ;
				sug.setText(autoComplete+"\n run time: "+ time+" s");
				
		    	
		    	
				start = System.currentTimeMillis();
				StringDistance dis2 = new LevensteinDistance();
		    	String leveResult = spellchecker(inputs);
		    	stop = System.currentTimeMillis();
		    	time = (stop - start) ;
				spellchecker2.setText(leveResult+"\n run time: "+ time+" s");	

				
		    }

		    public void keyTyped(KeyEvent e) { 

				
		    }
		});
	}
	public static String suggest(String inputs) throws IOException{
		String autoComplete = "";
		
		Dictionary ptd = new PlainTextDictionary(new File(indexpath ));
		Lookup lk = new JaspellLookup() ;
		lk.build(ptd);
		List<LookupResult> result = lk.lookup(inputs, false, 10) ;
		for(int i = 0 ; i < result.size(); i++){
			autoComplete = autoComplete + String.valueOf( result.get(i).key) +"\n" ;
		}

		return autoComplete;
	}
	public static String spellchecker(String inputs){
		if(!inputs.equals("")){
			String allresult = "";
			SpellChecker sc;
		    String[] strs = null;
			try {
				sc = new SpellChecker(FSDirectory.open(new File(dict  )),new LevensteinDistance());
				sc.indexDictionary(new PlainTextDictionary(new File(indexpath )), 
						new IndexWriterConfig(Version.LUCENE_36,new SimpleAnalyzer(Version.LUCENE_36)), false);  
				sc.setAccuracy(0.5f);
			    strs=sc.suggestSimilar(inputs , 10 );  
			} catch (IOException e) {
				e.printStackTrace();
			}
	        for (int i = 0; i < strs.length; i++) {  
	        	allresult = allresult + strs[i] +"\n";
	        }
			return allresult;
		}
		else{
			return "";
		}
	}
	

}
