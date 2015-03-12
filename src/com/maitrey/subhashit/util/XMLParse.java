package com.maitrey.subhashit.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLParse{

	InputStream m_inputStream;
	
	public XMLParse(String fileName){
        try {
			this.m_inputStream = new BufferedInputStream(new FileInputStream(new File("/res/raw/" + fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public XMLParse(InputStream inputStream){
			this.m_inputStream = inputStream;
    }

	
	public List<String[]> parseXMLAndStoreIt(String recordEndTag) throws XmlPullParserException {
	      int event;
	      String text=null;
	      
	      XmlPullParser myParser = XmlPullParserFactory.newInstance().newPullParser();
	      myParser.setInput(m_inputStream,"UTF-8");
	      
	      
	      List<String[]> allSubhashits = new ArrayList<String[]>();
	      String value = null;
	      String meaning = null;
	      String indexStr = null;
	      String indexNumStr = null;
	      
	      try {
	         event = myParser.getEventType();
	         while (event != XmlPullParser.END_DOCUMENT) {
	        	 
	        	String name=myParser.getName();
	            switch (event){
	               case XmlPullParser.START_TAG:
	            	   if(name.equals(recordEndTag)){
	            		   value = null;
	            		   meaning = null;
	            		   indexStr = null;
	            		   indexNumStr = null;
		                }
	            	   break;
	            	   
	               case XmlPullParser.TEXT:
	            	   text = myParser.getText();
	            	   break;

	               case XmlPullParser.END_TAG:
	                  if(name.equals(recordEndTag)){
	                	  if(indexStr == null)
	                		  indexStr = "Index not available";
	                	  allSubhashits.add(new String[]{value,meaning,indexStr,indexNumStr});
	                  }
	                  else if(name.equals("value")){ 	
	                	  value = text;
	                  }
	                  else if(name.equals("meaning")){
	                     meaning = text;
	                  }else if(name.equals("index")){
	                	  indexStr = text;
	                  }else if(name.equals("indexNum")){
	                	  indexNumStr = text;
	                  }
	              }	 
                  event = myParser.next();
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      java.util.Collections.sort(allSubhashits, new Comparator<String[]>() {
	            @Override
	            public int compare(final String[] object1, final String[] object2) {
	                return object1[0].compareTo(object2[0]);
	            }
	           } );
	      return allSubhashits;
	   }
	
	public Map<String, List<String[]>> parseTagXMLAndStoreIt(String recordEndTag) throws XmlPullParserException {
	      int event;
	      String text=null;
	      
	      XmlPullParser myParser = XmlPullParserFactory.newInstance().newPullParser();
	      myParser.setInput(m_inputStream,"UTF-8");
	      
	      Map<String, List<String[]>> allTags = new HashMap<String, List<String[]>>();
	      List<String[]> allSubhashits = new ArrayList<String[]>();
	      String value = null;
	      String meaning = null;
	      String indexStr = null;
	      String tagStr = null;
	      
	      try {
	         event = myParser.getEventType();
	         while (event != XmlPullParser.END_DOCUMENT) {
	        	 
	        	String name=myParser.getName();
	            switch (event){
	               case XmlPullParser.START_TAG:
	            	   if(name.equals(recordEndTag)){
	            		   value = null;
	            		   meaning = null;
	            		   indexStr = null;
		                }
	            	   break;
	            	   
	               case XmlPullParser.TEXT:
	            	   text = myParser.getText();
	            	   break;

	               case XmlPullParser.END_TAG:
	                  if(name.equals(recordEndTag)){
	                	  if(indexStr == null)
	                		  indexStr = "Index not available";
	                	  
	                	  allSubhashits = allTags.get(tagStr);
	                	  if(allSubhashits == null)
	                		  allSubhashits = new ArrayList<String[]>();
	                	  allSubhashits.add(new String[]{value,meaning,indexStr});
	                	  
	                	  allTags.put(tagStr, allSubhashits);
	                  }
	                  else if(name.equals("value")){ 	
	                	  value = text;
	                  }
	                  else if(name.equals("meaning")){
	                     meaning = text;
	                  }else if(name.equals("tag")){
	                	  tagStr = text;
	                  }
	              }	 
                event = myParser.next();
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return allTags;
	   }
}
