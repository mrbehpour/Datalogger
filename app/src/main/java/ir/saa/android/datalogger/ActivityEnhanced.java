package ir.saa.android.datalogger;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;

public class ActivityEnhanced extends AppCompatActivity {

	@Override
	protected void onResume() {
        G.currentActivity = this;
		
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		
		return super.onCreateOptionsMenu(menu);
	}


}
