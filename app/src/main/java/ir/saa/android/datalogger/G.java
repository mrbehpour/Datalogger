package ir.saa.android.datalogger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Stack;

import org.json.JSONObject;

import gps.MyLocationListener;

import mycomponents.MyToast;

import socket.structs.DataRef;
import socket.structs.MyPacket;

//import project.G;

import database.helper.DatabaseHelper;
import database.structs.dtoSetting;
import database.structs.dtoUsers;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


public class G extends Application {


	public static boolean RTL=true;
	public static String appLang="fa_IR";
	public static boolean isMultiMedia=false;
	public static Handler handler;
	Thread threadCheckVersion = null;
//usb key connection
	public final static byte 			CMD_GetUsers = 1;
	public final static byte 			CMD_GetItems = 2;
	public final static byte 			CMD_GetSetting = 3;
	public final static byte 			CMD_GetLastValues = 4;
	public final static byte 			CMD_SaveItemValues = 5;
	public final static byte  			CMD_GetApkVersion = 5; 
	public final static byte  			CMD_GetApk = 6;
	//connectionWay to usb
	// 0 is noting
	// 1 is usb
	// 2 is wifi
	// 3 is both
	public static int 					connectionWay = 0;
	//usb stuff
	public static int 					bufferSize = 10 * 1024;
	public static Socket 				socket = new Socket();
	private ServerSocket 				server;
	public static  DataOutputStream 	dos;
	public static  BufferedInputStream 	bis; 
	public static int 					socketPort = 6000;
	public static int 					sizeOfPacket = 5;
	public static boolean				RegisterItem=false;
	//app context
    public static Context              	context;
    //current activity
    public static Activity             	currentActivity;

    public static LayoutInflater       	inflater;
    public static android.support.v7.app.ActionBar actionBar;
    public static DrawerLayout 			mDrawerLayout;
    public static LinearLayout 			llMenu;
    public static FragmentManager 		fragmentManager;
    public static Stack<Fragment> 		fragmentStack ;	
    public static Fragment 				currentFragment ;	
    public static ViewPager 			viewPager;
	public static CoordinatorLayout 	coordinatorLayout;
    
    public static String 				gpsProvider = "";
    public static String 				latitude = "";
    public static String 				longitude = "";

    public static class Mode{
    	public static int NewMode = 0;
    	public static int EditMode = 1;
    }
    public static int 					selectedMode = Mode.NewMode;
    public static int 					selectedPBPos = 0;
    public static int 					selectedViewId = 0;
    public static String 				selectedOid = null;
    public static String 				selectedCodeZirTajhiz;
    public static String 				selectedNameZirTajhiz;
    public static String 				selectedNoeZirTajhiz;
    public static String 				selectedCodeTajhizAsli;
    public static String 				selectedKeyZamanBandi;
    public static int 					selectedMenuItemType;
    public static MenuItemMode 			selectedMenuItemMode;

	public static Integer 				PostId;
	public static Integer 				selectedVahedId;
    public static Integer 				selectedLogshitId;
    public static String 				selectedTajhiz;
	public static String 				selectedZirTajhiz;
	public static String 				selectedItem;
	public static String 				selectedVahed;
	public static String 				selectedLogshit;
	public static boolean				isSave=true;
    
    public final static int 			EYB_NAME_LENGTH = 20;
    public final static int 			PIC_REQUIRED_SIZE = 300;
    public final static int 			VOICE_MINUTES_LIMIT = 3;
    
    //public static final String         	DIR_SDCARD   = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String         		DIR_VOICES= "";
    public static String         		DIR_PICTURES= "";
    public static String         		DIR_DATABASE= "";
    public static String         		DIR_DOWNLOAD= "";
    public static String         		DB_NAME = "pm.db";
    //public static String         		DIR_CACHE;
    public static Stack<String> 		navigationStackTitle;
    public static Stack<Integer> 		navigationStackId;

    public static String 				serialNumberOfDevice = "";
    public static DatabaseHelper		DB;
    public static String 				WEB_SERVICE =	""; //http://192.168.3.244:8012/Service1.svc
    public static String 				WEB_ADDRESS =	"http://192.168.3.196:8012"; //http://192.168.3.244:8012/Service1.svc
    public static String 				APK_ADDRESS =	"http://192.168.3.130:8019/pm.apk"; //http://192.168.3.244:8012/Service1.svc
    public static String 				MY_VERSION = "" ;
//    public static String 				USER_NAME =	"خوش آمدید";
    public static String 				USER_ID = "16e917d8-125b-49f7-862f-079f530d314d";// =	"3a7dc14d-6043-46e0-93b9-f7ebc4e07b92";
    public static String 				SALT_KEY =	"h^&$%#&*h$#@";
    public static dtoUsers				currentUser;
    public static dtoSetting			Setting = null;
    public static Integer 				selectedId = null;
//    public static String 				USER_ID =	"245F75F2-31BB-4ECB-9CE8-E6D9A7B0A3C9";
    public static class MenuItemTypes{
    	public static int VAHED = 1;
    	public static int LOGSHIT = 2;
    	public static int TAJHIZ = 3;
    	public static int ZIRTAJHIZ = 4;
    	public static int ITEM = 5;
		public static int ITEMREPORT = 6;
		public static int ITEMHISTORY = 7;

    }
    public static enum MenuItemMode{
    	Sabt,
    	Gozareshat
    }



	public static String convertToEnglishDigits(String value, Boolean replacePointChar)
	{
		String newValue="";

		if(value!=null && value.length()>0) {
			newValue = value.replace("١", "1").replace("٢", "2").replace("٣", "3").replace("٤", "4").replace("٥", "5")
					.replace("٦", "6").replace("٧","7").replace("٨", "8").replace("٩", "9").replace("٠", "0")
					.replace("۱", "1").replace("۲", "2").replace("۳", "3").replace("۴", "4").replace("۵", "5")
					.replace("۶", "6").replace("۷", "7").replace("۸", "8").replace("۹", "9").replace("۰", "0");

			if (replacePointChar) newValue = newValue.replace("٫", ".");
		}
		return newValue;
	}

    
    public static SharedPreferences myPref;
    public static SharedPreferences sharedPref ;
    public static SharedPreferences.Editor prefEditor; // the object for saving last dates infos
    private final String PREF_NAME = "MyPrefs" ;
    public static String PREF_KEY_LAST_UPDATE_SETTING = "updateSettingKey";
    public static String PREF_KEY_LAST_UPDATE_PACKUSER = "updatePackUserKey";
    public static String PREF_KEY_LAST_UPDATE_PACKITEMS = "updatePackItemsKey";
    public static String PREF_KEY_LAST_UPDATE_MAXITEMVAL = "updateMaxItemValKey";
    public final static String PREF_KEY_WEB_SERVICE = "pref_ws_address";
    public final static String PREF_KEY_USB_PORT = "pref_usb_port";
    public final static String PREF_KEY_LAST_VERSION = "lastVersion";
	public final static String PREF_KEY_ITEMS_ORDER_TYPE = "pref_items_order_type";
    

	public static LocationManager locationManager;
	public static Criteria criteria;
	public static MyLocationListener mylistener;
	public static TelephonyManager telephonyManager;
	public static Vibrator vibrator = null;

    @Override
    public void onCreate() {

    	
        super.onCreate();
//        try {
//			URL u = new URL("http://192.168.3.196:8012/Service1.svc");
//			Log.i("wsss", u.getPath());
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		handler= new Handler();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        myPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        WEB_SERVICE = sharedPref.getString(PREF_KEY_WEB_SERVICE, "");
        
        fragmentStack = new Stack<Fragment>();
        navigationStackTitle = new Stack<String>();
        navigationStackId = new Stack<Integer>();
        context = this.getApplicationContext();
		telephonyManager = (TelephonyManager) this.getSystemService(this.getApplicationContext().TELEPHONY_SERVICE);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	
        Log.i("ctx",  ""+Environment.getExternalStorageState());
        
        DIR_PICTURES = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        DIR_VOICES = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        DIR_DOWNLOAD = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        DIR_DATABASE = String.format("data/data/%s/databases/", getApplicationContext().getPackageName());
//        WEB_SERVICE = G.myPref.getString(G.PREF_KEY_WEB_SERVICE, "");

        if(!(new File(DIR_PICTURES).isDirectory()))
        {
        	new File(DIR_PICTURES).mkdir();
        }else{
        	Log.i("tag", "ax2");

        }
        if(!(new File(DIR_VOICES).isDirectory()))
        {
        	new File(DIR_VOICES).mkdir();
        }else{
        	Log.i("tag", "ax3");

        }
        if(!(new File(DIR_DATABASE).isDirectory()))
        {
        	new File(DIR_DATABASE).mkdir();
        }else{
        	Log.i("tag", "ax4");
        }
        Log.i("usb", "Class .G RAN");
        
        DB = new DatabaseHelper(context);
        
        
        //-------Initialize Data--------
        
        Setting = DB.getSetting();

        //----set MY_VERSION ------
        PackageInfo pinfo = null;
		try {
			pinfo = G.context.getPackageManager().getPackageInfo(G.context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		//int versionNumber = pinfo.versionCode;  
		MY_VERSION = pinfo.versionName; 
        //-------------------------
		G.connectionWay= G.getConnectionWay();
		
//		if(threadCheckVersion==null){
//			threadCheckVersion = new Thread(new checkApkVersionFromServer(),"thread_threadCheckVersion");
//			threadCheckVersion.start();
//		}
        
//        try {
//            CopyDataBaseFromAsset();
//        }
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        DbInitials();
        
        //---------------------socket code part--------------------
        
        acceptSocket();

		try {
			//-------------GET IMEI-----------
		if(G.telephonyManager!=null) {
			serialNumberOfDevice =  G.telephonyManager.getDeviceId().toString() ;
		}

        //-----GPS--------------

        // the last known location of this provider

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// Define the criteria how to select the location provider
			criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default
			criteria.setCostAllowed(false);
			// get the best provider depending on the criteria
			gpsProvider = locationManager.getBestProvider(criteria, false);

			Location location = locationManager.getLastKnownLocation(gpsProvider);
			mylistener = new MyLocationListener();
			if (location != null) {
				mylistener.onLocationChanged(location);
			}
			// location updates: at least 1 meter and 200millsecs change
			locationManager.requestLocationUpdates(gpsProvider, 200, 1, mylistener);
		}catch (SecurityException ex){
			Log.i("tag",ex.getMessage());
		}catch (Exception ex){
			Log.i("tag",ex.getMessage());
		}
    }

    public static boolean isGpsEnabled(){
//    	if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            return true;
//        }else{
//        	return false;
//        }
		return false;
    }

    public static void updateGpsLocation(){
    	try {
			G.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, G.mylistener);
		}catch (SecurityException ex){
			Log.i("Tag","SecurityException : "+ex.getMessage());
		}
		catch (Exception ex){
			Log.i("Tag","Exception : "+ex.getMessage());
		}
    }

    public class checkApkVersionFromServer implements Runnable {
		public void run() {
			try {
				Log.i("ver","G.connectionWay : "+G.connectionWay);
				
				if(G.connectionWay==1 || G.connectionWay==3){

//					Log.i("ver","1");
//					byte[] byteMyVersion = new byte[G.MY_VERSION.length()];
//					byteMyVersion = G.MY_VERSION.getBytes();
					Log.i("ver","2");

			        MyPacket packet = new MyPacket();
			        packet.cmd = G.CMD_GetApkVersion;
			        packet.len = 0;
					Log.i("ver","3");

			        G.dos.write(MyPacket.toBytes(packet));
					Log.i("ver","4");

			        G.dos.flush();
//					Log.i("ver","5");
//			        G.dos.write(byteMyVersion);
					Log.i("ver","6  - "+G.bis.available());

			        byte[] recvBytesPacket = new byte[G.sizeOfPacket]; 
			        int countRead = G.bis.read(recvBytesPacket, 0, recvBytesPacket.length);

					Log.i("ver","countRead : "+countRead);

			        if(countRead == G.sizeOfPacket){	        
			        	MyPacket recvPacket = MyPacket.toMyPacket(recvBytesPacket);
			        	Log.i("ver",String.format("cmd = %s ,  len = %s ",String.valueOf(recvPacket.cmd),String.valueOf(recvPacket.len)));
			        	
				        byte[] recvBytes = new byte[recvPacket.len]; 
			        	int countRead2 = G.bis.read(recvBytes, 0, recvPacket.len);
			        	if(countRead2 == recvPacket.len){
			        		String strLastVersion = new String(recvBytes);
			        		Log.i("ver","strVersion : "+strLastVersion);
			        		G.prefEditor = G.myPref.edit();
	                		G.prefEditor.putString(G.PREF_KEY_LAST_VERSION, strLastVersion);
	                		G.prefEditor.commit();
				            if(Integer.parseInt(G.MY_VERSION.replace(".", "")) < Integer.parseInt(strLastVersion.replace(".", "")) ){
				            	G.handler.post(new Runnable() {
					                @Override
					                public void run() {
					                	//MyToast.Show(G.context,String.format("%s %d %s", "تعداد",lstTableUser.length,"کاربر بروزرسانی شد"), Toast.LENGTH_SHORT);
					                	Intent intent = new Intent(G.context, ActivityUpdateVersion.class);
					                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					                	startActivity(intent);
					                	G.currentActivity.finish();
					                }
					            });
				            }
			        	}
			        }
				}else if(G.connectionWay == 2){
				
		            Log.i("ver","thread_getUsersInfoFromServer started ");
		            Log.i("ver","s : "+Build.SERIAL);
	
		            // Replace it with your own WCF service path
		            URL json = new URL(G.WEB_SERVICE+"/GetAndroidAppVersion"); 
		            URLConnection jc = json.openConnection();
		            
		            int resCode = -1;
		            if ( !(jc instanceof HttpURLConnection)) {
		                throw new IOException("URL is not an Http URL");
		            }
		            HttpURLConnection httpConn = (HttpURLConnection) jc;
		            httpConn.setAllowUserInteraction(false);
		            httpConn.setInstanceFollowRedirects(true);
		            httpConn.setRequestMethod("GET");
		            httpConn.connect();
		            resCode = httpConn.getResponseCode();
		        	Log.i("ver","resCode : "+resCode);
		            BufferedReader reader = null ;
		            if (resCode == HttpURLConnection.HTTP_OK) {
		                reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
		            }
		            
		            String line = "";
		            String strJson = "";
		            while ((line = reader.readLine()) != null) {
		                strJson += line + "\n";
		            }
	
		            Log.i("ver","strjson : "+strJson);
		            JSONObject jsonResponse = new JSONObject(strJson);
		            String strLastVersion = jsonResponse.getString("GetAndroidAppVersionResult");
		            Log.i("ver","strLastVersion : "+ strLastVersion);
		            G.prefEditor = G.myPref.edit();
            		G.prefEditor.putString(G.PREF_KEY_LAST_VERSION, strLastVersion);
            		G.prefEditor.commit();
		            if(Integer.parseInt(G.MY_VERSION.replace(".", "")) < Integer.parseInt(strLastVersion.replace(".", "")) ){
		            	G.handler.post(new Runnable() {
			                @Override
			                public void run() {
			                	//MyToast.Show(G.context,String.format("%s %d %s", "تعداد",lstTableUser.length,"کاربر بروزرسانی شد"), Toast.LENGTH_SHORT);
			                	Intent intent = new Intent(G.context, ActivityUpdateVersion.class);
			                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			                	startActivity(intent);
			                	G.currentActivity.finish();
			                }
			            });
		            }

		            reader.close();
				}
				else{
					if(Integer.parseInt(G.MY_VERSION.replace(".", "")) < Integer.parseInt(G.myPref.getString(G.PREF_KEY_LAST_VERSION, G.MY_VERSION).replace(".", "")) ){
						G.handler.post(new Runnable() {
			                @Override
			                public void run() {
			                	Intent intent = new Intent(G.context, ActivityUpdateVersion.class);
			                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			                	startActivity(intent);
			                	G.currentActivity.finish();
			                }
			            });
		            }
				}
	        }
	        catch (Exception e) {
	        	G.handler.post(new Runnable() {
	                @Override
	                public void run() {
	                	MyToast.Show(G.context,String.format("%s", context.getText(R.string.ErrorInConnectServer)), Toast.LENGTH_SHORT);
	                }
	            });
	            Log.i("ver", e.toString());
	        }
            threadCheckVersion = null;
		}
	}
    
    public void acceptSocket(){
    	try {
			server = new ServerSocket(socketPort);
	        
	        new Thread(new Runnable() {
                public void run() {
                	try {
                		while(true){
                		Log.i("usb", "Accepting ... ");
                			Socket newsocket = server.accept();
                			if(socket.isConnected()){
	                    		Log.i("usb", "Accepting ...111 ");
	                			if(!socket.isInputShutdown())
	                				socket.shutdownInput();
	                    		Log.i("usb", "Accepting ...222 ");
	                			if(!socket.isOutputShutdown())
	                				socket.shutdownOutput();
	                    		Log.i("usb", "Accepting ...333 ");
	                			if(!socket.isClosed())
	                				socket.close();
	                    		Log.i("usb", "Accepting ...444 ");
                			}
                			socket = newsocket;
							socket.setReceiveBufferSize(G.bufferSize);
							socket.setSendBufferSize(G.bufferSize);
					        Log.i("usb", "Client connected");
					        G.connectionWay = G.getConnectionWay();
		                	bis = new BufferedInputStream(socket.getInputStream());
		                	dos = new DataOutputStream(socket.getOutputStream());
		                	
//		            		if(threadCheckVersion==null){
//		            			threadCheckVersion = new Thread(new checkApkVersionFromServer(),"thread_threadCheckVersion");
//		            			threadCheckVersion.start();
//		            		}
		            		
                		}
					} catch (IOException e) {
				        Log.i("usb", e.getMessage());
					}
			
                   }
                }).start();
			
		} catch (IOException e) {
	        Log.i("usb", e.getMessage());
		}
    }

    public static int getConnectionWay(){
    	int connectionWay = 0;
    	
    	if(G.socket.isConnected()){
    		connectionWay=1;
    	}
    	
        ConnectivityManager check = (ConnectivityManager)
                G.context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo[] info = check.getAllNetworkInfo();
//        for (int i = 0; i < info.length; i++) {
//            //Log.i("tag","net 1 : "+ info[i].getTypeName() + " , " + info[i].getState().toString());
//            if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                //MyToast.Show(G.context, "Net is connected", Toast.LENGTH_SHORT);
//                connectionWay+=2;;
//            }
//        }
        NetworkInfo mWifi = check.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
        	connectionWay+=2;
        }
        
        Log.i("usb","Connection way : "+connectionWay);
//        if ( !isNetConnected)
//            return;
    	return connectionWay;
    }
    
    public static String MD5(String input,String salt) {
		
		String md5 = null;
		
		if(null == input) return null;
		
		try {
			String str = input+salt;
			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			//Update input string in message digest
			digest.update(str.getBytes(), 0, input.length());
	
			//Converts message digest value in base 16 (hex) 
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}
    
    public static int ReadAll(DataRef d,int len){
    	int lenDownloaded = 0;
    	int bytesRead = 0;
		int bufferSize = G.bufferSize;
    	try{
	    	while (lenDownloaded <  len)
	    	{
				bufferSize = Math.min(G.bufferSize, len-lenDownloaded);
	    		bytesRead = G.bis.read(d.data, lenDownloaded , bufferSize);
	    		lenDownloaded += bytesRead;
	    		G.bis.available();
	    		Log.i("usb","lenDownloaded : "+lenDownloaded+"  len : "+len);
	    	}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}

    	return lenDownloaded;
    }




	public static int WriteAll(byte [] data,int len){
		int lenUploaded = 0;
		int bufferSize = G.bufferSize;
		try{
			while (lenUploaded < len)
			{
				bufferSize = Math.min(G.bufferSize, len-lenUploaded);
				G.dos.write(data,lenUploaded,bufferSize);
				lenUploaded += bufferSize;
		        Log.i("usb","lenUploaded : "+lenUploaded+"  len : "+len);

			}
		}catch(Exception ex){
    		ex.printStackTrace();
    		Log.i("usb","ex : "+ex.getMessage());
    	}
		return lenUploaded;
	}
    
    
//    private void DbInitials() {
//        Log.i("pm", DIR_DATABASE + DB_NAME);
//
//        //database = SQLiteDatabase.openOrCreateDatabase(DIR_DATABASE + DB_NAME, null);
//        database = SQLiteDatabase.openDatabase(DIR_DATABASE + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
//        database.execSQL("CREATE TABLE IF NOT EXISTS 'User' 		('ItemInfID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Oid' TEXT, 'UserName' TEXT, 'SaaUserName' TEXT, 'StoredPassword' TEXT)");
//        database.execSQL("CREATE TABLE IF NOT EXISTS 'Eyb' 			('ItemInfID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Oid' TEXT, 'NameEyb' TEXT, 'CodeEyb' TEXT, 'NoeTajhiz' TEXT)");
//        database.execSQL("CREATE TABLE IF NOT EXISTS 'TableView' 	('ItemInfID' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'ZoneId' TEXT, 'ZoneName' TEXT, 'CodeTajhizAsli' TEXT, 'NameTajhizAsli' TEXT, 'Address' TEXT, 'CodePaye' TEXT, 'BarTrans' TEXT, 'GhodratTrans' TEXT, 'JarianNamiKelidAutomat' TEXT, 'MaghtaKablRabetTransBeTablo' TEXT, 'NameZirTajhiz' TEXT, 'NameFider20kiloVolt' TEXT, 'NoeTajhizat' TEXT, 'CodeZireTajhiz' TEXT, 'SNTrans' TEXT, 'SaleSakhtTrans' TEXT, 'Section' TEXT, 'TarikhSakht' TEXT, 'TedadKolKhoroji' TEXT, 'Tul' TEXT, 'VaziatTap' TEXT, 'UserId' TEXT, 'SaaUserName' TEXT, 'NoeEyb' TEXT, 'KeyZambandi' TEXT, 'KeyJozeat' TEXT, 'NameZamanBandi' TEXT, 'Code' TEXT, 'PicName' TEXT, 'VoiceName' TEXT, 'Olaviat' TEXT, 'Value' TEXT, 'TarikhSabt' TEXT, 'IsSent' TEXT)");
//    }
//
//    // Copy existance db from assets
//    public void CopyDataBaseFromAsset() throws IOException {
//    	
//        InputStream myInput = context.getAssets().open(DB_NAME);
//        String outFileName = DIR_DATABASE + DB_NAME;
//        File f = new File(DIR_DATABASE);
//        if ( !f.exists())
//            f.mkdir();
//
//        OutputStream myOutput = new FileOutputStream(outFileName);
//        byte[] buffer = new byte[1024];
//        
//        int length;
//        while ((length = myInput.read(buffer)) > 0) {
//            myOutput.write(buffer, 0, length);
//        }
//        
//        myOutput.flush();
//        myOutput.close();
//        myInput.close();
//    }


}
