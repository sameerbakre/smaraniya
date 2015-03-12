package com.maitrey.subhashit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.maitrey.database.DatabaseHelper;
import com.maitrey.subhashit.util.DataService;
import com.maitrey.subhashit.util.DataStore;

public abstract class AbstractActivity extends Activity {
	

	protected DrawerLayout drawerLayout;
	protected ListView listView;
	protected ActionBarDrawerToggle drawerListener;
	
	protected CharSequence mDrawerTitle;
	protected CharSequence mTitle;
	public static DatabaseHelper m_databaseHelper;

	protected boolean m_disableTouch;
    
	protected static final SparseArray<String> mTitleMap = new SparseArray<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(m_databaseHelper == null)
			m_databaseHelper = new DatabaseHelper(this);
		
		if(mTitleMap == null || mTitleMap.size() == 0){
			mTitleMap.append(SubhashitConstants.SUBHASHIT_VIEW, getResources().getString(R.string.subhshit));
			mTitleMap.append(SubhashitConstants.DOHE_VIEW, getResources().getString(R.string.dohe));
			mTitleMap.append(SubhashitConstants.SHLOK_VIEW, getResources().getString(R.string.shlok));
			mTitleMap.append(SubhashitConstants.FAVOURITE, getResources().getString(R.string.favourite));
		}
		initDataService();
		if(!(this instanceof SubhashitActivity)){
			if(!DataStore.checkSanity()){
				int count=0;
				Toast.makeText(this, "Loading, Please wait...", Toast.LENGTH_LONG).show();
				while(!DataStore.checkSanity() && count < 1000){
					count++;
				}
			}
		}
	}
	
	private void initDataService() {
		Intent intent = new Intent(this, DataService.class); 
		startService(intent);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerListener.syncState();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		drawerListener.onConfigurationChanged(newConfig);
	}
	
    
    private void selectItem(int position, CharSequence title,boolean changeInActionBar) {
        // update selected item and title, then close the drawer
    	listView.setItemChecked(position, true);
        mTitle = title;
        if(changeInActionBar)
        	getActionBar().setTitle(mTitle);
        
        drawerLayout.closeDrawer(listView);
    }
    
    protected void initDrawer(int position, CharSequence title, boolean changeInActionBar){
    	
    	mDrawerTitle = getTitle();
        
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);

        listView= (ListView)findViewById(R.id.listLayout);
		listView.setAdapter(new ArrayAdapter<String>(this,R.layout.do_item_drawer,new String[] {"Home","सुभाषित", "दोहे", "श्लोक","Favourites"}));
		
        // set a custom shadow that overlays the main content when the drawer opens
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        listView.setItemChecked(0, true);
		
		
        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){

        	@Override
        	public void onDrawerClosed(View drawerView) {
        		getActionBar().setTitle(mDrawerTitle);
        		m_disableTouch = false;
        		invalidateOptionsMenu();
        	}
        	
        	@Override
        	public void onDrawerOpened(View drawerView) {
        		getActionBar().setTitle(mTitle);
        		m_disableTouch = true;
        		invalidateOptionsMenu();
        	}
        };
        
        drawerLayout.setDrawerListener(drawerListener);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        selectItem(position,title,changeInActionBar);
        registerClickDrawerCallback();
    }
    
    public void redirect(Intent i){
    	redirect(i,false);
    }
    
    public void redirect(Intent i, boolean forceStart){
    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
	    if(forceStart){
	    	startActivity(i);
	    }else{
	    	startActivityIfNeeded(i,0);
	    }
    }

	public abstract void registerClickDrawerCallback();

}
