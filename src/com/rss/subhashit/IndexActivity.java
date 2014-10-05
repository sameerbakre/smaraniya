package com.rss.subhashit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class IndexActivity extends Activity implements OnClickListener{

	
	private SparseIntArray m_indexMap;
	protected int m_viewType;
	
	private View m_homeButton;
	private View m_SubhashitButton;
	private View m_DoheButton;
	private View m_ShlokButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		Bundle params = getIntent().getExtras();
		m_viewType = params.getInt("viewType");
		
		if(savedInstanceState != null){
			int savedViewType = savedInstanceState.getInt("viewType");
			if(savedViewType != 0){
				m_viewType = savedViewType;
			}
			savedInstanceState.clear();		
		}
		
		setUIData();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("viewType", m_viewType);
		super.onSaveInstanceState(outState);
	}
	
	
	private void populateList(List<String[]> currentList){
		Iterator<String[]> itr = currentList.iterator(); 
		
		m_indexMap = new SparseIntArray();
		
		int subhashitIndex =-1;
		int listIndex =-1;
		
		List<String> values = new ArrayList<String>();
		
		while(itr.hasNext()){
			subhashitIndex ++;
			String[] subhashits = itr.next();
			if(subhashits.length < 3 || subhashits[2] == null){
				continue;
			}
			listIndex ++;
			m_indexMap.put(listIndex, subhashitIndex);
			int subhashitNo = listIndex +1;
			values.add(subhashitNo + ". " + subhashits[2]);
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
				int subhashitIndex = m_indexMap.get(position);
				
				Intent i = new Intent(IndexActivity.this, SubhashitViewActivity.class);
				i.putExtra("viewType", m_viewType);
				i.putExtra("recordIndex", subhashitIndex);
				startActivity(i);
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		handleButtonClick(v.getId());
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuInflater mnIftor = getMenuInflater();
		mnIftor.inflate(R.menu.subhashit_view, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		handleButtonClick(item.getItemId());
		return true;
				
	}


	private void handleButtonClick(int itemId) {
		Intent i;
		switch (itemId){
			case R.id.subhashit:
				changeViewByType(SubhashitConstants.SUBHASHIT_VIEW);
				return;
			case R.id.dohe:
				changeViewByType(SubhashitConstants.DOHE_VIEW);
				return;
			case R.id.shlok:
				changeViewByType(SubhashitConstants.SHLOK_VIEW);
				return;
			default:
				i = new Intent(this, SubhashitActivity.class);
				break;
		}
		
		startActivity(i);
		
	}
	
	private void changeViewByType(int gotoViewType){
		if(m_viewType == gotoViewType)
			return;
		
		Intent i = getIntent();
		i.putExtra("viewType", gotoViewType);
		m_viewType = gotoViewType;
		setUIData();
	}


	private void setUIData() {
		List<String[]> currentList = SubhashitViewActivity.getDataStore(this).getRecordList(m_viewType);
		
		populateList(currentList);		
		registerClickCallback();
		
		m_homeButton = (TextView)findViewById(R.id.home);
        m_SubhashitButton = findViewById(R.id.subhashit);  
        m_DoheButton = findViewById(R.id.dohe);
        m_ShlokButton = findViewById(R.id.shlok);

        m_homeButton.setOnClickListener(this);
        m_SubhashitButton.setOnClickListener(this);
        m_DoheButton.setOnClickListener(this);
        m_ShlokButton.setOnClickListener(this);
		
	}
}
