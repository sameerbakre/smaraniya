package com.maitrey.subhashit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;

import com.maitrey.subhashit.R;

public class SubhashitActivity extends AbstractActivity implements OnClickListener{

	private Button mMenuButton;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setUIInfo();
    	initDrawer(SubhashitConstants.HOME,"Main Menu", false);
    }
    
    private void setUIInfo(){
    	setContentView(R.layout.activity_subhashit);
    	
    	mMenuButton = (Button)findViewById(R.id.menuButton);
    	mMenuButton.setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.menuButton){
			drawerLayout.openDrawer(listView);
			return;
		}
	}
	
	@Override
    protected void onResume() {
    	super.onResume();
    	listView.setItemChecked(SubhashitConstants.HOME, true);
    }
	
	public void registerClickDrawerCallback(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				drawerLayout.closeDrawer(listView);
				if(position == SubhashitConstants.HOME)
					return;
				
				Intent i = null;
				if(position == SubhashitConstants.SUBHASHIT_VIEW || position == SubhashitConstants.FAVOURITE){
					i = new Intent(SubhashitActivity.this, ExpandableIndex.class);
				}else{
					i = new Intent(SubhashitActivity.this, IndexActivity.class);
				}

				i.putExtra("viewType", position);
				redirect(i);
			}
			
		});
	}
	
}
