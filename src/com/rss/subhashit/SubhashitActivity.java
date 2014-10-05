package com.rss.subhashit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SubhashitActivity extends Activity implements OnClickListener{
	private View m_SubhashitButton;
	private View m_DoheButton;
	private View m_ShlokButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subhashit);
        
        m_SubhashitButton = findViewById(R.id.subhashit);  
        m_DoheButton = findViewById(R.id.dohe);
        m_ShlokButton = findViewById(R.id.shlok);
        
        m_SubhashitButton.setOnClickListener(this);
        m_DoheButton.setOnClickListener(this);
        m_ShlokButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		Intent i = new Intent(this, IndexActivity.class);
		switch (v.getId()){
			case R.id.subhashit:
				i.putExtra("viewType", SubhashitConstants.SUBHASHIT_VIEW);
				break;
			case R.id.dohe:
				i.putExtra("viewType", SubhashitConstants.DOHE_VIEW);
				break;
			case R.id.shlok:
				i.putExtra("viewType", SubhashitConstants.SHLOK_VIEW);
				break;
		}
		startActivity(i);
	}
}
