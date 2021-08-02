package ir.saa.android.datalogger;


import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import mycomponents.MyToast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import static android.content.Context.WINDOW_SERVICE;

public class FragmentSettings extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	
	boolean IsConnectionOk = false;
	Thread threadCheckWs = null;
	public  void adjustFontScale(Configuration configuration, Float fontSize) {

		configuration.fontScale = (float) fontSize;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		metrics.scaledDensity = configuration.fontScale * metrics.density;
		getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);

	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		if(G.RTL==false){
			adjustFontScale(getResources().getConfiguration(),(float)0.85);
		}
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.i("wsss", key +" changed.");
		if (G.getConnectionWay()==0){
			MyToast.Show(G.context, String.format("%s\n%s\n%s",(String)getText(R.string.UsbWifiIsDC), (String)getText(R.string.UsbWifiIsC),(String)getText(R.string.NetWorkConnect)), Toast.LENGTH_LONG);
	        return;
		}
		switch (key) {
		case G.PREF_KEY_WEB_SERVICE:
			G.WEB_SERVICE = G.sharedPref.getString(key, "")+"/PDLWeb.svc";
			
			if(threadCheckWs==null){
				threadCheckWs = new Thread(new TaskCheckWs(),"thread_getUsersInfoFromServer");
				threadCheckWs.start();
			}else{
				MyToast.Show(G.context, (String)getText(R.string.CheckAddressConnect), Toast.LENGTH_LONG);
			}
			
			break;
		case G.PREF_KEY_USB_PORT:
			G.socketPort = Integer.parseInt(G.sharedPref.getString(key, ""));
			MyToast.Show(G.context, (String) getText(R.string.RegisterPortNumber), Toast.LENGTH_LONG);
			
			break;

		}
		
	}
    
	class TaskCheckWs implements Runnable {
        @Override
        public void run() {
        	URL json = null;
            Log.i("wsss","ws : "+G.WEB_SERVICE);

        	try{
        		json = new URL(G.WEB_SERVICE); 
        		IsConnectionOk = true;
        	}catch(Exception eex){
        		IsConnectionOk = false;
        	}
        	try{
        		if(IsConnectionOk){
		            URLConnection jc = json.openConnection();
		            int resCode = -1;
		            if (jc instanceof HttpURLConnection) {
		            	HttpURLConnection httpConn = (HttpURLConnection) jc;
		                httpConn.setAllowUserInteraction(false);
		                httpConn.setInstanceFollowRedirects(true);
		                httpConn.setRequestMethod("GET");
		                httpConn.connect();
		                resCode = httpConn.getResponseCode();
		            	Log.i("wsss","resCode : "+resCode);
		                if (resCode == HttpURLConnection.HTTP_OK) {
		                	IsConnectionOk = true;
							G.WEB_SERVICE=G.WEB_SERVICE;
		                }else{
		                	IsConnectionOk = false;
		                }
		            }else
		            {
		            	IsConnectionOk = false;
		            }
        		}
            G.handler.post(new Runnable() {
                @Override
                public void run() {
                	
                	if(IsConnectionOk){
                		MyToast.Show(G.context,(String) G.context.getText(R.string.ValidationAddress), Toast.LENGTH_LONG);
                		if (G.getConnectionWay()==0){
							MyToast.Show(G.context, String.format("%s\n%s\n%s",getText(R.string.UsbWifiIsDC), getText(R.string.UsbWifiIsC),getText(R.string.NetWorkConnect)), Toast.LENGTH_LONG);
				            return;
						}
                	}else{
                		MyToast.Show(G.context,String.format("%s",getText(R.string.NotValidationAddress)), Toast.LENGTH_LONG);
                	}
                	threadCheckWs= null;
                }
            });
        	}catch(Exception ex){
        		Log.i("wsss", "eeeerr : "+ex.getMessage());
        		if(ex.getMessage().toLowerCase().contains("failed to connect")){
					G.handler.post(new Runnable() {
						@Override
						public void run() {
							MyToast.Show(G.context, String.format("%s", getText(R.string.NotValidationAddress)), Toast.LENGTH_LONG);
						}
					});
				}else{
					G.handler.post(new Runnable() {
						@Override
						public void run() {
							MyToast.Show(G.context, String.format("%s", getText(R.string.NotValidationAddress)), Toast.LENGTH_LONG);
						}
					});
				}
			;
        		threadCheckWs= null;

        	}
        }
            
	           
	}
	
}