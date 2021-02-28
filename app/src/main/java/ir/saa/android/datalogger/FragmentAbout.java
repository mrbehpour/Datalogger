package ir.saa.android.datalogger;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import static android.content.Context.WINDOW_SERVICE;

public class FragmentAbout extends Fragment {

	public  void adjustFontScale(Configuration configuration, Float fontSize) {

		configuration.fontScale = (float) fontSize;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		metrics.scaledDensity = configuration.fontScale * metrics.density;
		getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);

	}
	Typeface tf;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.fragment_about, container, false);

		if(G.RTL==false){
			adjustFontScale(getResources().getConfiguration(),(float)0.85);

			tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
		}else{
			 tf= Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
		}
		
		return rootView;
	}

	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		
		
//		WebView mWebView = (WebView)view.findViewById(R.id.webView1);
//        mWebView.getSettings().setJavaScriptEnabled(false);
//        //mWebView.getSettings().setPluginsEnabled(true);
//        mWebView.getSettings().setAllowFileAccess(true);
//        mWebView.getSettings().setLoadWithOverviewMode(false);
//        mWebView.getSettings().setUseWideViewPort(false);
//        mWebView.loadUrl("file:///android_asset/htmls/login.html");
//------------------------
        TextView txtApp = (TextView) view.findViewById(R.id.txtApp);
        TextView txtAppName = (TextView) view.findViewById(R.id.txtAppName);
        TextView txtAppVersion = (TextView) view.findViewById(R.id.txtAppVersion);
		TextView txtPublishBy = (TextView) view.findViewById(R.id.txtPublishBy);
		TextView txtPublisher = (TextView) view.findViewById(R.id.txtPublisher);
		TextView txtDeviceIMEI = (TextView) view.findViewById(R.id.txtDeviceIMEI);
		TextView txtIMEINumber = (TextView) view.findViewById(R.id.txtIMEINumber);


		txtApp.setTypeface(tf);
		txtAppName.setTypeface(tf);
		txtAppVersion.setTypeface(tf);
		txtPublishBy.setTypeface(tf);
		txtPublisher.setTypeface(tf);
		txtDeviceIMEI.setTypeface(tf);
		txtIMEINumber.setTypeface(tf);
		
//		PackageInfo pinfo = null;
//		try {
//			pinfo = G.context.getPackageManager().getPackageInfo(G.context.getPackageName(), 0);
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//		//int versionNumber = pinfo.versionCode;  
//		String versionName = pinfo.versionName; 
		if(G.MY_VERSION.trim().length()>0)
			txtAppVersion.setText(getString(R.string.VersionTitle)+ " : "+  G.MY_VERSION);
		if(G.telephonyManager!=null) {

			txtIMEINumber.setText(Html.fromHtml("<b>" + G.serialNumberOfDevice + "</b>"));

		}
//		String youtContentStr = String.valueOf(Html
//                .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#222222; \">"
//                            + getResources().getString(R.string.about_text)
//                            + "</body>]]>"));
//
//		mWebView.loadData(youtContentStr, "text/html", "utf-8");
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		//TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
		//txtTitle.setText(getResources().getString(R.string.menu_about_us));
		//txtTitle.setText("asdasdasdsa");
		
		super.onCreateOptionsMenu(menu, inflater);
		TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
		txtTitle.setText(G.context.getResources().getString(R.string.menu_about_us));
	}
}
