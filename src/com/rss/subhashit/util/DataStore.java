package com.rss.subhashit.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;

import com.rss.subhashit.R;
import com.rss.subhashit.SubhashitConstants;
public class DataStore {


	public static List<String[]> m_subhashit;
	public static List<String[]> m_dohe;
	public static List<String[]> m_shlok;
	
	public DataStore(Activity activity){
		try {
			initData(activity);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	private void initData(Activity activity) throws NotFoundException, XmlPullParserException {
		if(m_subhashit == null){			
			m_subhashit = new XMLParse(activity.getResources().openRawResource(R.raw.subhshitxml)).parseXMLAndStoreIt("subhashit");
		}
		
		if(m_dohe == null){
			m_dohe = new XMLParse(activity.getResources().openRawResource(R.raw.dohexml)).parseXMLAndStoreIt("dohe");
		}
		
		if(m_shlok == null){
			m_shlok = new XMLParse(activity.getResources().openRawResource(R.raw.shlokxml)).parseXMLAndStoreIt("shloka");
		}
	}
	
	
	public String[] getRecordData(int viewType, int index){
		
		if(viewType == SubhashitConstants.SUBHASHIT_VIEW){
			return getDataFromList(m_subhashit, index);
		}else if(viewType == SubhashitConstants.DOHE_VIEW){
			return getDataFromList(m_dohe, index);
		}else if(viewType == SubhashitConstants.SHLOK_VIEW){
			return getDataFromList(m_shlok, index);
		}
		
		return null;
	}
	
	public List<String[]> getRecordList(int viewType){
		
		if(viewType == SubhashitConstants.SUBHASHIT_VIEW){
			return m_subhashit;
		}else if(viewType == SubhashitConstants.DOHE_VIEW){
			return m_dohe;
		}else if(viewType == SubhashitConstants.SHLOK_VIEW){
			return m_shlok;
		}
		
		return null;
	}
	
	private String[] getDataFromList(List<String[]> recordList, int index){
		if(recordList == null)
			return null;
		
		if(recordList.size() <= index)
			return null;
		
		return recordList.get(index);
	}
	
	public static void saveToDataFile(Activity activity, String userInfo){
		FileOutputStream file = null;
		try {
			file = activity.openFileOutput("smaraniya.txt", Context.MODE_PRIVATE);
			file.write(userInfo.getBytes());
			file.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(file != null)
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static String loadFromFile(Activity activity){		
		FileInputStream file = null;
		StringBuffer buff = new StringBuffer();
		
		try {
			file = activity.openFileInput("smaraniya.txt");
			
			int fileIndex = -1;

			while((fileIndex = file.read()) != -1){
				buff.append((char) fileIndex);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(file != null){
				try{
					file.close();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		
		return buff.toString();
	}
}
