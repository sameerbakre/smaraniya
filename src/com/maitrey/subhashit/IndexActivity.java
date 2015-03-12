package com.maitrey.subhashit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.maitrey.subhashit.util.DataStore;

public class IndexActivity extends AbstractActivity{
	
	protected int m_viewType;
	private int m_savedViewType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		Bundle params = getIntent().getExtras();
		m_viewType = params.getInt("viewType");
		if(m_savedViewType != 0){
			m_viewType = m_savedViewType;
			m_savedViewType = 0;
		}
		
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
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("viewType", m_viewType);
		super.onSaveInstanceState(outState);
	}
	
	
	private void populateList(List<String[]> currentList){
		Iterator<String[]> itr = currentList == null ? null : currentList.iterator(); 
		
		int listIndex =-1;
		
		List<String> values = new ArrayList<String>();
		
		if(itr != null){
			while(itr.hasNext()){
				String[] subhashits = itr.next();
				if(subhashits.length < 3 || subhashits[2] == null){
					continue;
				}
				listIndex ++;
				int subhashitNo = listIndex +1;
				values.add(subhashitNo + ". " + subhashits[2]);
			}
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.do_item,values);
		
		ListView list = (ListView) findViewById(R.id.indexView1);
		list.setAdapter(adapter);
	}
	
	private void registerClickCallback(){
		ListView list = (ListView) findViewById(R.id.indexView1);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				Intent i = new Intent(IndexActivity.this, SubhashitViewActivity.class);
				i.putExtra("viewType", m_viewType);
				i.putExtra("recordIndex", position);
				startActivityIfNeeded(i,0);
			}
			
		});
	}

	private void changeViewByType(int gotoViewType){
		if(m_viewType == gotoViewType)
			return;
		
		m_viewType = gotoViewType;
		setUIData();
	}


	private void setUIData() {
		List<String[]> currentList = DataStore.getRecordList(m_viewType);
		
		mDrawerTitle = getResources().getString(R.string.title_activity_index) + " - " +mTitleMap.get(m_viewType);		
		mTitle = "Main Menu";
		
		populateList(currentList);		
		registerClickCallback();
	}
	
	
	public void registerClickDrawerCallback(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				drawerLayout.closeDrawer(listView);
				if(position == 0){
					redirect(new Intent(IndexActivity.this, SubhashitActivity.class));
				    return;
				}
				if(position == SubhashitConstants.SUBHASHIT_VIEW|| position == SubhashitConstants.FAVOURITE){
					Intent i = new Intent(IndexActivity.this, ExpandableIndex.class);
					i.putExtra("viewType", position);
					redirect(i);
					return;
				}
				
				changeViewByType(position);
			}
			
		});
	}
}
