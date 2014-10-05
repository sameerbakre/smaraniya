package com.rss.subhashit;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.rss.subhashit.util.DataStore;

public class SubhashitViewActivity extends Activity implements OnClickListener, OnTouchListener{
	private TextView mSubhashitView;
	private TextView mSubhashiMeantView;
	
	private int m_currentRecord;
	private int m_viewType;
	
	private List<String[]> m_currentList;
	
	private View mPrevButton;
	private View mNextButton;
	
	private View m_homeButton;
	private View m_SubhashitButton;
	private View m_anukramanikaButton;
	private View m_DoheButton;
	private View m_ShlokButton;
	
	private ShareActionProvider m_shareActionProvider;
	
	private static DataStore m_dataStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_subhashit_view);
		initActData(savedInstanceState);
		
		setUIData();
	}
	
	protected void initActData(Bundle savedInstance){

		Bundle params = getIntent().getExtras();
		m_viewType = params.getInt("viewType");
		m_currentRecord = params.getInt("recordIndex");
		
		if(savedInstance == null)
			return;
		
		int savedViewType = savedInstance.getInt("viewType");
		int savedCurrentRecord = savedInstance.getInt("currentRecord");
		
		if(savedCurrentRecord == 0 || savedViewType == 0)
			return;
		
		m_viewType = savedViewType;
		m_currentRecord = savedCurrentRecord;
		savedInstance.clear();		
	}
	
	
	@Override
	public void onClick(View v) {
		handleButtonClick(v.getId());
	}
	
	public static DataStore getDataStore(Activity activity){
		if(m_dataStore == null)
			m_dataStore = new DataStore(activity);
		
		return m_dataStore;
	}
	
	private void handleButtonClick(int id){
		Intent i;
		switch (id){
			case R.id.subhashit:
				changeViewByType(SubhashitConstants.SUBHASHIT_VIEW);
				return;
			case R.id.dohe:
				changeViewByType(SubhashitConstants.DOHE_VIEW);
				return;
			case R.id.shlok:
				changeViewByType(SubhashitConstants.SHLOK_VIEW);
				return;
			case R.id.anukramanika:
				i = new Intent(this, IndexActivity.class);
				i.putExtra("viewType", m_viewType);
				break;
			default:
				i = new Intent(SubhashitViewActivity.this, SubhashitActivity.class);
				break;
		}
		
		startActivity(i);
	}
	
	private void changeViewByType(int gotoViewType){
		if(m_viewType == gotoViewType)
			return;
		
		Intent i = getIntent();
		i.putExtra("viewType", gotoViewType);
		m_currentRecord = 0;
		m_viewType = gotoViewType;
		setUIData();
	}
	
	protected void setUIData(){
		m_currentList = getDataStore(this).getRecordList(m_viewType);
		
        mSubhashitView = (TextView)findViewById(R.id.text_subhashit);
        mSubhashiMeantView = (TextView)findViewById(R.id.text_mean_subhashit);
        
        String[] subhashits = m_currentList.get(m_currentRecord);
        mSubhashitView.setText(subhashits[0]);
        if(subhashits.length < 3 || subhashits[1] == null)
			mSubhashiMeantView.setText("Meaning not available");
		else
			mSubhashiMeantView.setText(subhashits[1]);
        
        mPrevButton = findViewById(R.id.button_prev);  
        mPrevButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_currentRecord--;
				String[] subhashits = m_currentList.get(m_currentRecord);
				
				while(subhashits.length < 3 || subhashits[1] == null){
					m_currentRecord--;
					subhashits = m_currentList.get(m_currentRecord);
				}
				
				mSubhashitView.setText(subhashits[0]);
				if(subhashits.length < 3 || subhashits[1] == null)
					mSubhashiMeantView.setText("Meaning not available");
				else
					mSubhashiMeantView.setText("     " + subhashits[1]);
				
				mNextButton.setEnabled(true);
				if(m_currentRecord-1 < 0){
					mPrevButton.setEnabled(false);
				}
				setShareContents();
			}
		});
        if(m_currentRecord == 0)
        	mPrevButton.setEnabled(false);
        mNextButton = findViewById(R.id.button_next);
        mNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_currentRecord++;
				String[] subhashits = m_currentList.get(m_currentRecord);
				
				while(subhashits.length < 3 || subhashits[1] == null){
					m_currentRecord++;
					subhashits = m_currentList.get(m_currentRecord);
				}
					
				mSubhashitView.setText(subhashits[0]);
				if(subhashits.length < 3 || subhashits[1] == null)
					mSubhashiMeantView.setText("Meaning not available");
				else
					mSubhashiMeantView.setText(subhashits[1]);
				
				mPrevButton.setEnabled(true);
				if(m_currentRecord+1 >= m_currentList.size()){
					mNextButton.setEnabled(false);	
				}
				setShareContents();
			}
		});


        if(m_currentRecord == m_currentList.size() - 1)
        	mNextButton.setEnabled(false);
        
        if(m_shareActionProvider != null)
        	setShareContents();
        
        m_homeButton = (TextView)findViewById(R.id.home);
        m_anukramanikaButton = (TextView)findViewById(R.id.anukramanika);
        m_SubhashitButton = findViewById(R.id.subhashit);  
        m_DoheButton = findViewById(R.id.dohe);
        m_ShlokButton = findViewById(R.id.shlok);

        m_homeButton.setOnClickListener(this);
        m_anukramanikaButton.setOnClickListener(this);
        m_SubhashitButton.setOnClickListener(this);
        m_DoheButton.setOnClickListener(this);
        m_ShlokButton.setOnClickListener(this);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("viewType", m_viewType);
		outState.putInt("currentRecord", m_currentRecord);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuInflater mnIftor = getMenuInflater();
		
		mnIftor.inflate(R.menu.action_bar_share_menu, menu);
		mnIftor.inflate(R.menu.subhashit_view, menu);
		
		MenuItem item = menu.findItem(R.id.menu_item_share);
		m_shareActionProvider = (ShareActionProvider) item.getActionProvider();
		setShareContents();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		if(item.getItemId() != R.id.menu_item_share){
			handleButtonClick(item.getItemId());
			return true;
		}
		return false;
	}

	
	private void setShareContents(){		
		String shareContents = mSubhashitView.getText() + "\n\n " + mSubhashiMeantView.getText();
		Intent myIntent = new Intent();
		myIntent.setAction(Intent.ACTION_SEND);
		myIntent.putExtra(Intent.EXTRA_TEXT, shareContents);
		myIntent.setType("text/plain");
		m_shareActionProvider.setShareIntent(myIntent);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
