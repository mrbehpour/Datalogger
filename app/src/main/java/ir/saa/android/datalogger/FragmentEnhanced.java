package ir.saa.android.datalogger;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class FragmentEnhanced extends Fragment {

	@Override
	public void onResume() {
		G.currentFragment = this;
		super.onResume();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {

		return true;
	}
	
}
