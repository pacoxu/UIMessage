package com.emc.xplore.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

import com.emc.xplore.liberary.LiberaryManager;

public class StringLiberaryJFrame {

	private static String indexpath = "MicroStringsDict.txt";
	private static String dict = "dict";
	public static void main(String[] args){
		final JFrame f = new JFrame("Spell Checker");
		f.setVisible(true);
		f.setSize(800, 600);
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
		
		//new Container for Liberary Management
		Container bottom = new Container();
		
		bottom.setLayout(new GridLayout(3,1));

		
		Container bottom1 = new Container();
		bottom1.setLayout(new FlowLayout());

		JLabel word = new JLabel("new String: ");
		bottom1.add(word);
		final JTextField jt1 = new JTextField("has been deleted");
		jt1.setColumns(30);
		bottom1.add(jt1);
		
		final LiberaryManager lm = new LiberaryManager();

		Button add = new Button("Add to Liberary");
		bottom1.add(add);
		add.setVisible(true);
		bottom1.setVisible(true);
		
		add.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					lm.addString(jt1.getText().trim());
					JOptionPane.showMessageDialog(null, "'"+jt1.getText() + "' is added to the liberary.");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "error");
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});


		Container bottom2 = new Container();
		bottom2.setLayout(new FlowLayout());

		JLabel dict = new JLabel("new Document: ");
		bottom2.add(dict);
	    final JTextArea ta = new JTextArea("          ");
	    JButton brow = new JButton("Choose Dict File");
	    brow.addActionListener(new ActionListener()  
        {  
			@Override
            public void actionPerformed(ActionEvent ae)  
            {  
				JFileChooser fDialog = new JFileChooser();
				int result=fDialog.showOpenDialog(f);
				if(result==JFileChooser.APPROVE_OPTION){
					String fname=fDialog.getSelectedFile().getAbsolutePath();
					f.setTitle(fname);
					ta.setText(fname);
					f.repaint();
				}else{
				}
            }
			
        }); 
	    bottom2.add(ta);
	    bottom2.add(brow);
		

		Button add2 = new Button("Add to Liberary");
		bottom2.add(add2);
		add.setVisible(true);
		bottom2.setVisible(true);
		
		Container bottom3 = new Container();
		bottom3.setLayout(new FlowLayout());

		Button reindex = new Button( "Reindex!" );
		reindex.addActionListener(new ActionListener()  
	        {  
				@Override
	            public void actionPerformed(ActionEvent ae)  
	            {  
					lm.reindex();
					JOptionPane.showMessageDialog(null, "The files has been reindexed!");
	            }
				
	        }); 
		bottom3.add(reindex);

		
		bottom.add(bottom1 , 0 , 0);
		bottom.add(bottom2 , 1 , 0);
		bottom.add(bottom3 , 2 , 0);

		
		f.add(bottom,BorderLayout.SOUTH);
		
		
		
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
