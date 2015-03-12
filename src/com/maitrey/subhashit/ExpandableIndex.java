package com.maitrey.subhashit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.maitrey.subhashit.R;
import com.maitrey.subhashit.util.DataService;
import com.maitrey.subhashit.util.DataStore;
import com.maitrey.subhashit.util.ExpandableListAdapter;

public class ExpandableIndex extends AbstractActivity{

	
		
	private final static int SUBHASHIT = SubhashitConstants.SUBHASHIT_VIEW;
	private static Map<String, List<String>> mSubhashitData;
	private static List<String> mGroupList;
	
	private int m_viewType;
	private int m_savedViewType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandable_index);
		
		Bundle params = getIntent().getExtras();
		m_viewType = params.getInt("viewType");
		if(m_savedViewType != 0){
			m_viewType = m_savedViewType;
			m_savedViewType = 0;
		}
		while(DataService.isRunning());
		setUIData();
		
		initDrawer(SUBHASHIT,mDrawerTitle,true);
		mDrawerTitle = mTitle;
		mTitle = "Main Menu";
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle params = intent.getExtras();
		m_viewType = params.getInt("viewType");
		
		setUIData();
		
		initDrawer(m_viewType,mDrawerTitle,true);
		mDrawerTitle = mTitle;
		mTitle = "Main Menu";
	}
	
	@Override
    protected void onResume() {
    	super.onResume();
    	listView.setItemChecked(m_viewType, true);
    }
	
	private void populateList(){
		
		if(mSubhashitData == null || mGroupList == null){
			mSubhashitData = new HashMap<String, List<String>>();
			mGroupList = new ArrayList<String>();

			initUIData();
		}
		
		ExpandableListAdapter  adapter = new ExpandableListAdapter(this,mGroupList,mSubhashitData);
		
		ExpandableListView list = (ExpandableListView) findViewById(R.id.indexView1);
		list.setAdapter(adapter);
	}
	
	private void populateFavList(){

		ExpandableListAdapter  adapter = new ExpandableListAdapter(this,DataStore.getFavGroup(),DataStore.getFavouriteData());
		
		ExpandableListView list = (ExpandableListView) findViewById(R.id.indexView1);
		list.setAdapter(adapter);
	}
	
	private void initUIData() {
		String[] title = new String[2];
		int subhashitIndex =-1;
		int listIndex =-1;

		List<String[]> currentList = DataStore.getRecordList(SUBHASHIT );
		List<String> values = new ArrayList<String>();
		Iterator<String[]> itr = currentList.iterator();
		String[] subhashits = null;
		
		while(itr.hasNext()){
			subhashitIndex ++;
			subhashits = itr.next();

			if(subhashits.length < 3 || subhashits[2] == null){
				continue;
			}
			
			if(subhashitIndex % 100 == 0){
				if(subhashitIndex != 0){
					String titleStr = title[0] + "" + subhashitIndex + " ( " + title[1] + " - "+  subhashits[2].substring(0, 1) + " )";  
					mGroupList.add(titleStr);
					mSubhashitData.put(titleStr, values);
					values = new ArrayList<String>();
				}
				int baseCount = subhashitIndex + 1;
				title[0] = baseCount + " - ";
				title[1] = subhashits[2].substring(0, 1);
			}
			listIndex ++;
			int subhashitNo = listIndex +1;
			values.add(subhashitNo + ". " + subhashits[2]);
		}
		if(subhashitIndex % 100 != 0){
			String titleStr = title[0] + "" + subhashitIndex + " ( " + title[1] + " - "+  subhashits[2].substring(0, 1) + " )";  
			mGroupList.add(titleStr);
			mSubhashitData.put(titleStr, values);
			values = new ArrayList<String>();
		}
		
	}


	private void registerClickCallback(){
		ExpandableListView list = (ExpandableListView) findViewById(R.id.indexView1);
		list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            	
            	int subhashitIndex = 0;
            	int viewType = 0;
            	if(m_viewType == SubhashitConstants.SUBHASHIT_VIEW){
            		subhashitIndex = groupPosition * 100 + childPosition;
            		viewType = SubhashitConstants.SUBHASHIT_VIEW;
            	}else{
            		
            		int index = mTitleMap.indexOfValue(DataStore.getFavGroup().get(groupPosition));
            		viewType = mTitleMap.keyAt(index);
            		subhashitIndex = DataStore.getIndexForPosition(viewType, childPosition);
            	}
            	Intent i = new Intent(ExpandableIndex.this, SubhashitViewActivity.class);
				i.putExtra("viewType", viewType);
				i.putExtra("recordIndex", subhashitIndex);
				startActivityIfNeeded(i,0);
                return true;
            }
        });
	}
		
	private void setUIData() {
		
		mDrawerTitle = getResources().getString(R.string.title_activity_index) + " - " + mTitleMap.get(m_viewType);		
		mTitle = "Main Menu";
		
		if(m_viewType == SubhashitConstants.SUBHASHIT_VIEW){
			populateList();
		}else{
			populateFavList();
		}
		
		registerClickCallback();
	}

	@Override
	public void registerClickDrawerCallback() {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				drawerLayout.closeDrawer(listView);
				if(position == 0){
					redirect(new Intent(ExpandableIndex.this, SubhashitActivity.class));
				    return;
				}
				if(position == SubhashitConstants.SUBHASHIT_VIEW || position == SubhashitConstants.FAVOURITE){
					m_viewType = position;
					setUIData();
				}else {
					Intent i = new Intent(ExpandableIndex.this, IndexActivity.class);
					i.putExtra("viewType", position);
					redirect(i);
					return;
				}
			}
			
		});
	}
}
