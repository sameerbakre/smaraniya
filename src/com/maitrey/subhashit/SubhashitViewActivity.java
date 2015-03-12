package com.maitrey.subhashit;

import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.maitrey.database.helper.EZCursorHelper;
import com.maitrey.subhashit.db.TableList;
import com.maitrey.subhashit.db.schema.Favourite;
import com.maitrey.subhashit.util.DataStore;
import com.maitrey.subhashit.util.SimpleGestureFilter;
import com.maitrey.subhashit.util.SimpleGestureFilter.SimpleGestureListener;

public class SubhashitViewActivity extends AbstractActivity implements OnClickListener, SimpleGestureListener{
	private TextView mSubhashitView;
	private TextView mSubhashiMeantView;
	private TextView mSubhashitIndexView;
	
	private int m_currentRecord;
	private int m_viewType;
	
	private List<String[]> m_currentList;
	
	private View mPrevButton;
	private View mNextButton;
	
	private View mFavButton;
	
	private ShareActionProvider m_shareActionProvider;
	private long mFavoriteId;
	
	
	SimpleGestureFilter gestureFilter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_subhashit_view);
		gestureFilter = new SimpleGestureFilter(this, this);
		
		initActData(savedInstanceState);
		
		setUIData();
		initDrawer(m_viewType, mDrawerTitle, true);
		mDrawerTitle = mTitle;
		mTitle = "Main Menu";
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
	
	protected void setUIData(){
		m_currentList = DataStore.getRecordList(m_viewType);
		
		mDrawerTitle = mTitleMap.get(m_viewType);
		mTitle = "Main Menu";
		
        mSubhashitView = (TextView)findViewById(R.id.text_subhashit);
        mSubhashiMeantView = (TextView)findViewById(R.id.text_mean_subhashit);
        mSubhashitIndexView = (TextView)findViewById(R.id.indexNum);
        
        updateUI();
        
        mPrevButton = findViewById(R.id.button_prev);  
        mPrevButton.setOnClickListener(this);
        
        mNextButton = findViewById(R.id.button_next);
        mNextButton.setOnClickListener(this);
        
        mFavButton = findViewById(R.id.favorite);
        mFavButton.setOnClickListener(this);
        setFavoriteImage();
        
        initButtonInfo();
	}
	
	private void showPrevious(){
		if(m_currentRecord == 0){
			Toast.makeText(this, "You have reached to start of the list", Toast.LENGTH_LONG).show();
			return;
		}
		
		m_currentRecord--;
		
		initButtonInfo();
		updateUI();
		setFavoriteImage();
	}

	private void showNext(){
		if(m_currentRecord == m_currentList.size() - 1){
			Toast.makeText(this, "You have reached to end of the list", Toast.LENGTH_LONG).show();
			return;
		}
		
		m_currentRecord++;
		
		initButtonInfo();
		updateUI();
		setFavoriteImage();
	}
	
	private void initButtonInfo() {
		if(m_currentRecord-1 < 0){
			mPrevButton.setEnabled(false);
		}else{
			mPrevButton.setEnabled(true);
		}
		
		if(m_currentRecord+1 >= m_currentList.size()){
			mNextButton.setEnabled(false);	
		}else{
			mNextButton.setEnabled(true);
		}
	}

	private void updateUI(){
		String[] smaraniyam = m_currentList.get(m_currentRecord);
		
		mSubhashitView.setText(smaraniyam[0]);
		int indx = m_currentRecord + 1;
		String index = indx + "/" + m_currentList.size();
		mSubhashitIndexView.setText(index);
		if(smaraniyam.length < 3 || smaraniyam[1] == null)
			mSubhashiMeantView.setText("Meaning not available");
		else
			mSubhashiMeantView.setText(smaraniyam[1]);
		
		setFavouriteData();
		
		if(m_shareActionProvider != null)
			setShareContents();
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
		
		MenuItem item = menu.findItem(R.id.menu_item_share);
		m_shareActionProvider = (ShareActionProvider) item.getActionProvider();
		setShareContents();
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// toggle nav drawer on selecting action bar app icon/title
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		return false;
	}

	
	private void setShareContents(){		
		String shareContents = mSubhashitView.getText() + "\n\n " + mSubhashiMeantView.getText() + "\n - Shared Via Smaraniyam \n tr.im/Smaraniyam";
		Intent myIntent = new Intent();
		myIntent.setAction(Intent.ACTION_SEND);
		myIntent.putExtra(Intent.EXTRA_TEXT, shareContents);
		myIntent.setType("text/plain");
		m_shareActionProvider.setShareIntent(myIntent);
	}
	
	public void registerClickDrawerCallback(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				drawerLayout.closeDrawer(listView);
				if(position == 0){
					redirect(new Intent(SubhashitViewActivity.this, SubhashitActivity.class));
				    return;
				}else if(position == SubhashitConstants.SUBHASHIT_VIEW || position == SubhashitConstants.FAVOURITE){
					Intent i = new Intent(SubhashitViewActivity.this, ExpandableIndex.class);
					i.putExtra("viewType", position);
					redirect(i);
				}else{
					Intent i = new Intent(SubhashitViewActivity.this, IndexActivity.class);
					i.putExtra("viewType", position);
					redirect(i);
				}
			}
			
		});
	}
	
	@Override
    protected void onResume() {
    	super.onResume();
    	listView.setItemChecked(m_viewType, true);
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.button_next){
			showNext();
		}else if(id == R.id.button_prev){
			showPrevious();
		}else if(id == R.id.favorite){
			handleFavorite();
		}
		
	}

	private void handleFavorite() {
		if(mFavoriteId == 0){
			mFavoriteId = insertFavourite();
			Toast.makeText(this, "Record is added in Favorite", Toast.LENGTH_LONG).show();
		}else{
			mFavoriteId = removeFavourite();
			Toast.makeText(this, "Record is removed from Favorite", Toast.LENGTH_LONG).show();
		}
		setFavoriteImage();
		
	}

	private void setFavoriteImage() {
		if(mFavoriteId == 0){
			mFavButton.setBackgroundResource(R.drawable.star_silver);
		}else{
			mFavButton.setBackgroundResource(R.drawable.brand);
		}
		
	}

	private long removeFavourite() {
		ContentValues conditions = new ContentValues();
		conditions.put(Favourite.UID, mFavoriteId);
		
		int rows = m_databaseHelper.deleteRow(TableList.favourite,  conditions);
		if(rows == 0)
			return mFavoriteId;
		
		DataStore.removeFromFav(m_viewType, m_currentRecord);
		return 0;
	}
	
	private void setFavouriteData(){
		ContentValues conditions = getBasicCondition();
		Cursor cursor = m_databaseHelper.selectData(TableList.favourite, conditions, new String[] {Favourite.UID});
		if(cursor.getCount() == 0)
			mFavoriteId = 0;
		
		if(cursor.moveToNext())
			mFavoriteId = EZCursorHelper.getLong(cursor, Favourite.UID);
	}

	private long insertFavourite() {
		String[] smaraniyam = m_currentList.get(m_currentRecord);
		
		ContentValues values = getBasicCondition();
		values.put(Favourite.NAME, smaraniyam[2]);
		values.put(Favourite.INDEX_NUMBER, smaraniyam[3]);
		
		long favId = m_databaseHelper.insertData(values, TableList.favourite);
		
		DataStore.addToFav(m_viewType, m_currentRecord, smaraniyam[2]);
		
		return favId;
	}
	
	private ContentValues getBasicCondition(){
		ContentValues values = new ContentValues();
		values.put(Favourite.TYPE, m_viewType);
		values.put(Favourite.REC_NUMBER, m_currentRecord);
		
		return values;
	}
	
	public void onSwipe(int direction) {
		switch (direction) {
			case SimpleGestureFilter.SWIPE_RIGHT :
				showPrevious();
				break;
			case SimpleGestureFilter.SWIPE_LEFT :
				showNext();
				break;
	      
	      }		
	}
	
	@Override
	public void onDoubleTap() {}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(!m_disableTouch)
			gestureFilter.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	


}
