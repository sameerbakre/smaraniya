package com.maitrey.subhashit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.content.ContextWrapper;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.maitrey.database.helper.EZCursorHelper;
import com.maitrey.subhashit.AbstractActivity;
import com.maitrey.subhashit.R;
import com.maitrey.subhashit.SubhashitConstants;
import com.maitrey.subhashit.db.TableList;
import com.maitrey.subhashit.db.schema.Favourite;
public class DataStore {


	private static List<String[]> m_subhashit;
	private static List<String[]> m_dohe;
	private static List<String[]> m_shlok;
	
	private static Map<String,List<String>> mFavouriteData;
	private static List<String> mFavGroupList;
	
	private static SparseIntArray mSubhashitFavMap;
	private static SparseIntArray mDoheFavMap;
	private static SparseIntArray mShlokFavMap;
	
	protected static SparseArray<String> mTitleMap = new SparseArray<String>();
	
	private static boolean mIsInitialized = false;
		
	public static void initFiles(ContextWrapper context){
		if(mIsInitialized && checkSanity())
			return;
		
		try {
			initData(context);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
	
	private static void initData(ContextWrapper context) throws NotFoundException, XmlPullParserException {
		if(m_subhashit == null){			
			m_subhashit = new XMLParse(context.getResources().openRawResource(R.raw.subhshitxml)).parseXMLAndStoreIt("subhashit");
		}
		
		if(m_dohe == null){
			m_dohe = new XMLParse(context.getResources().openRawResource(R.raw.dohexml)).parseXMLAndStoreIt("dohe");
		}
		
		if(m_shlok == null){
			m_shlok = new XMLParse(context.getResources().openRawResource(R.raw.shlokxml)).parseXMLAndStoreIt("shloka");
		}
		
		if(mTitleMap == null || mTitleMap.size() == 0){
			mTitleMap.append(SubhashitConstants.SUBHASHIT_VIEW, context.getResources().getString(R.string.subhshit));
			mTitleMap.append(SubhashitConstants.DOHE_VIEW, context.getResources().getString(R.string.dohe));
			mTitleMap.append(SubhashitConstants.SHLOK_VIEW, context.getResources().getString(R.string.shlok));
			mTitleMap.append(SubhashitConstants.FAVOURITE, context.getResources().getString(R.string.favourite));
		}
		
		initFavData(context);
		mIsInitialized = true;
	}
	
	private static void initFavData(ContextWrapper context){
		if(AbstractActivity.m_databaseHelper == null)
			return;
		
		Cursor cursor = AbstractActivity.m_databaseHelper.selectData(TableList.favourite, null, null, Favourite.TYPE);
		
		List<String> subhashits = new ArrayList<String>();
		List<String> dohe = new ArrayList<String>();
		List<String> shloks = new ArrayList<String>();
		
		mFavouriteData = new HashMap<String, List<String>>();
		mFavGroupList = new ArrayList<String>();
		
		mSubhashitFavMap = new SparseIntArray();
		mDoheFavMap = new SparseIntArray();
		mShlokFavMap = new SparseIntArray();
		
		int subhashitIndex = 0;
		int doheIndex = 0;
		int shlokIndex = 0;
		
		String subhashitStr = context.getResources().getString(R.string.subhshit);
		String doheStr = context.getResources().getString(R.string.dohe);
		String shlokStr = context.getResources().getString(R.string.shlok);
		
		while(cursor.moveToNext()){
			int type = EZCursorHelper.getInt(cursor, Favourite.TYPE);
			int recNumber = EZCursorHelper.getInt(cursor, Favourite.REC_NUMBER);
			String name = EZCursorHelper.getString(cursor, Favourite.NAME);
			
			if(type == SubhashitConstants.SUBHASHIT_VIEW){
				subhashits.add(name);					
				mSubhashitFavMap.append(subhashitIndex, recNumber);
				subhashitIndex++;
			}else if(type == SubhashitConstants.DOHE_VIEW){
				dohe.add(name);
				mDoheFavMap.append(doheIndex, recNumber);
				doheIndex++;
			}else if(type == SubhashitConstants.SHLOK_VIEW){
				shloks.add(name);
				mShlokFavMap.append(shlokIndex, recNumber);
				shlokIndex++;
			}
		}
		
		if(!subhashits.isEmpty()){
			mFavouriteData.put(subhashitStr, subhashits);
			mFavGroupList.add(subhashitStr);
		}		
		if(!dohe.isEmpty()){
			mFavouriteData.put(doheStr, dohe);
			mFavGroupList.add(doheStr);
		}		
		if(!shloks.isEmpty()){
			mFavouriteData.put(shlokStr, shloks);
			mFavGroupList.add(shlokStr);
		}
	}
	
	
	public String[] getRecordData(int viewType, int index){
		if(!checkSanity())
			return null;
			
		if(viewType == SubhashitConstants.SUBHASHIT_VIEW){
			return getDataFromList(m_subhashit, index);
		}else if(viewType == SubhashitConstants.DOHE_VIEW){
			return getDataFromList(m_dohe, index);
		}else if(viewType == SubhashitConstants.SHLOK_VIEW){
			return getDataFromList(m_shlok, index);
		}
		
		return null;
	}
	
	public static List<String[]> getRecordList(int viewType){
		if(!checkSanity())
			return null;
		
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
	
	public static List<String> getFavGroup(){
		if(!checkSanity())
			return null;
		return mFavGroupList;
	}
	
	public static Map<String, List<String>> getFavouriteData(){
		return mFavouriteData;
	}
	
	public static void addToFav(int viewType, int recNumber, String name){
		if(!checkSanity())
			return;
		if(viewType == SubhashitConstants.SUBHASHIT_VIEW){
			addToList(viewType, recNumber,name,mSubhashitFavMap);
		}else if(viewType == SubhashitConstants.DOHE_VIEW){
			addToList(viewType,recNumber,name,mDoheFavMap);
		}else if(viewType == SubhashitConstants.SHLOK_VIEW){
			addToList(viewType,recNumber,name,mShlokFavMap);
		}
	}
	
	private static void addToList(int viewType, int recNumber, String name, SparseIntArray favRecordMap) {
		int newIndex = favRecordMap.size();
		
		List<String> list = mFavouriteData.get(mTitleMap.get(viewType));
		boolean putInMap = false;
		if(list == null){
			putInMap = true;
			list = new ArrayList<String>();
		}
		
		list.add(name);
		favRecordMap.put(newIndex, recNumber);
		
		if(putInMap){
			mFavouriteData.put(mTitleMap.get(viewType), list);
			mFavGroupList.add(mTitleMap.get(viewType));
		}
	}

	public static void removeFromFav(int viewType, int recNumber){
		if(!checkSanity())
			return;
		
		if(viewType == SubhashitConstants.SUBHASHIT_VIEW){
			mSubhashitFavMap = removeFromList(viewType,recNumber,mSubhashitFavMap);
		}else if(viewType == SubhashitConstants.DOHE_VIEW){
			mDoheFavMap = removeFromList(viewType,recNumber,mDoheFavMap);
		}else if(viewType == SubhashitConstants.SHLOK_VIEW){
			mShlokFavMap = removeFromList(viewType,recNumber,mShlokFavMap);
		}
	}
	
	private static SparseIntArray removeFromList(int viewType, int recNumber,SparseIntArray favRecordMap){
		int index = favRecordMap.indexOfValue(recNumber);
		if(index == -1)
			return favRecordMap;
		
		List<String> list = mFavouriteData.get(mTitleMap.get(viewType));
		int recordKey =  favRecordMap.keyAt(index);
		
		if(list.size() > recordKey){
			list.remove(recordKey);
			favRecordMap.delete(recordKey);
			SparseIntArray newFavRecordMap = new SparseIntArray();
			for(int favIndex = 0; favIndex < favRecordMap.size(); favIndex++){
				newFavRecordMap.put(favIndex, favRecordMap.valueAt(favIndex));
			}
			if(newFavRecordMap.size() == 0){
				mFavouriteData.remove(mTitleMap.get(viewType));
				mFavGroupList.remove(mTitleMap.get(viewType));
			}
			return newFavRecordMap;
		}
		return favRecordMap;
	}

	public static int getIndexForPosition(int viewType, int childPosition) {
		if(!checkSanity())
			return 0;
		
		if(viewType == SubhashitConstants.SUBHASHIT_VIEW){
			return mSubhashitFavMap.get(childPosition);
		}else if(viewType == SubhashitConstants.DOHE_VIEW){
			return mDoheFavMap.get(childPosition);
		}else if(viewType == SubhashitConstants.SHLOK_VIEW){
			return mShlokFavMap.get(childPosition);
		}
		return 0;
	}
	
	public static boolean checkSanity(){
		if(!mIsInitialized || mFavouriteData == null || mFavGroupList == null || mSubhashitFavMap == null
				|| mDoheFavMap == null || mShlokFavMap == null)
			return false;
		
		return true;
	}
}
