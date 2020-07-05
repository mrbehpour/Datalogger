package ir.saa.android.datalogger;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ActivitySettings extends PreferenceActivity  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preferences);
        getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new FragmentSettings())
        .commit();
    }

	
}

