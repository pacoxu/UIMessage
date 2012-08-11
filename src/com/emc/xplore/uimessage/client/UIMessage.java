package com.emc.xplore.uimessage.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UIMessage implements EntryPoint {
	JsonCallback jsonCallback = new JsonCallback();
	RequestBuilder reqBuilderSpell = new RequestBuilder(RequestBuilder.POST,"proxy/http://10.37.10.57:9300/dsearch/autocomplete.iig");
    MultiWordSuggestOracle oracle= new MultiWordSuggestOracle();
    final SuggestBox suggestbox= new SuggestBox(oracle);
    final FlexTable suggestTable = new FlexTable();

	@Override
	public void onModuleLoad() {
		
		oracleInit();
		
        //product
        ListBox products = new ListBox();
		products.ensureDebugId("cwListBox-dropBox");
	    String[] product = { "xPlore" , "xDB" };
		for (int i = 0; i < product.length; i++) {
	    	products.addItem(product[i]);
	    }
        
        //suggestion libraries
    	String[] libsName = {"Content Server","xPlore","xDB"};
    	Panel libs = new HorizontalPanel();
        CheckBox[] sugLibs= new CheckBox[3]; 
        for(int i=0; i<sugLibs.length ;i++){
			sugLibs[i] = new CheckBox(libsName[i]);
			libs.add(sugLibs[i]);
        }
        
        //Library 
        ListBox libraries = new ListBox();
        libraries.ensureDebugId("cwListBox-dropBox");
	    String[] library = { "xPlore Installer Error messages" , "Microsoft Strings" };
		for (int i = 0; i < library.length; i++) {
			libraries.addItem(library[i]);
	    }
        Button export = new Button("Export ...");
        
        
        //Class name
        TextBox classname = new TextBox();
        //String -- maybe a suggest box to auto-complete
        suggestbox.addStyleName("searchBox");
        suggestbox.setFocus(true);
        suggestbox.addKeyUpHandler(new KeyUpHandler(){
        	public void onKeyUp(KeyUpEvent event) {		
        		String key = suggestbox.getText();
        		if( !key.equals("")){
        			try {
        				reqBuilderSpell.sendRequest(key, jsonCallback);
        			} catch (RequestException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			
        		}
        		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//        			queryServer();
        		}
        	}
        });

        
        
        Panel bs = new HorizontalPanel(); //VerticalPanel();
        Button clear = new Button("Clear");
        bs.add(clear);
        Button create = new Button("Create");
        bs.add(create);
        
        
        //Suggestions_ suggest result here 
        TextArea suggestions = new TextArea();
        
        String wid = "400px";
        String hei = "300px";
        suggestTable.setWidget(0, 0, new Label("Product"));
        products.setWidth(wid);
        suggestTable.setWidget(0, 1, products);
        suggestTable.setWidget(1, 0, new Label("Suggestion libraries"));
        libs.setWidth(wid);
        suggestTable.setWidget(1, 1, libs);
        suggestTable.setWidget(2, 0, new Label("Library"));
        libraries.setWidth(wid);
        suggestTable.setWidget(2, 1, libraries);
        suggestTable.setWidget(2, 2, export);
        suggestTable.setWidget(3, 0, new Label("Class nmae"));
        classname.setWidth(wid);
        suggestTable.setWidget(3, 1, classname);
        suggestTable.setWidget(4, 0, new Label("String"));
        suggestbox.setWidth(wid);
        suggestTable.setWidget(4, 1, suggestbox);
        suggestTable.setWidget(4, 2, bs);
        suggestTable.setWidget(5, 0, new Label("Suggesttions"));
        suggestions.setWidth(wid);
        suggestions.setHeight(hei);
        suggestTable.setWidget(5, 1, suggestions);

        suggestTable.setVisible(true);
		RootPanel.get("suggesttable").add(suggestTable);
		
		clear.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				suggestbox.setText("");
			}});
		create.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				String newstring = suggestbox.getText();
				Window.confirm(newstring);
//				if(Window.)
			}});
	}
	private void oracleInit() {
		String[] s = { "about","buy","create","delete","edit","file","generate",
				"height","identify","java","kick off","leek","look","make","node"
				,"open","play","query","root","slides","take","uima","vendor","wide",
				"you","zan","Content Server","xPlore","xDB"};
		for (int i = 0; i < s.length; i++) {
			oracle.add(s[i]);
	    }
		
	}
	class JsonCallback implements RequestCallback {

		@Override
		public void onResponseReceived(Request request, Response response) {
			String result = response.getText();
			if (result.trim().length() == 0) {
				GWT.log("empty JSON");
				return;
			}
			GWT.log("responseText" + result);
			try {				
				int start = 0 ;
            	int end = 0;
//            	Window.alert(result);
            	int length = result.length();
            	for(int i = 0 ; i < length ; i ++){
            		if( result.charAt(i) == '\n'){
            			end = i;
        				oracle.add(result.substring(start, end));
        				start = i+1;
            		}
            	}
            	suggestbox.showSuggestionList();
			} catch (Exception e) {
//				hideBusyIndicator();
				GWT.log("Fail to parse JSON " + result, e);
				Window.alert("Sorry, server is under maintainence. Please try later.");
				//Window.alert("Fail to parse JSON " + responseText);
			}
		}

		@Override
		public void onError(Request request, Throwable e) {
			Window.alert("Error " + e.getMessage());
			GWT.log("Fail", e);
		}

	}
}
