package ir.saa.android.datalogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import mycomponents.MyToast;
import socket.structs.MyPacket;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityUpdateVersion extends Activity {

	Thread threadGetNewApk = null;
	TextView txtVersion3;
	ImageView imgUpVersion;
	ProgressBar pbVer ;
	LinearLayout llUpdateVersion;

	public  void adjustFontScale(Configuration configuration, Float fontSize) {

		configuration.fontScale = (float) fontSize;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		metrics.scaledDensity = configuration.fontScale * metrics.density;
		getBaseContext().getResources().updateConfiguration(configuration, metrics);

	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_update_version);
		llUpdateVersion=(LinearLayout)findViewById(R.id.llUpdateVersion);

		if(G.RTL)//TODO: LTR
			llUpdateVersion.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
		else {
			adjustFontScale(getResources().getConfiguration(),(float)1.5);
			llUpdateVersion.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
		}


		TextView txtVersion1 = (TextView) findViewById(R.id.txtVersion1);
		TextView txtVersion2 = (TextView) findViewById(R.id.txtVersion2);
		txtVersion3 = (TextView) findViewById(R.id.txtVersion3);
		imgUpVersion = (ImageView) findViewById(R.id.imgUpVersion);
		pbVer = (ProgressBar) findViewById(R.id.pbVer);
		
		
		final Typeface tfByekan = Typeface.createFromAsset(getAssets(), "fonts/byekan.ttf");
		
		txtVersion1.setTypeface(tfByekan);
		txtVersion2.setTypeface(tfByekan);
		txtVersion3.setTypeface(tfByekan);
		
		imgUpVersion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imgUpVersion.setImageDrawable(G.context.getResources().getDrawable(R.drawable.download_green));
				txtVersion3.setVisibility(View.VISIBLE);
				pbVer.setVisibility(View.VISIBLE);
				
				if(threadGetNewApk==null){
					threadGetNewApk = new Thread(new getNewApkFromServer(),"thread_threadGetNewApk");
					threadGetNewApk.start();
        		}
			}
		});
		
	}
	public class getNewApkFromServer implements Runnable {
		public void run() {
			try {
				G.connectionWay = G.getConnectionWay();
				Log.i("apk","G.connectionWay : "+G.connectionWay);
				if(G.connectionWay==1 || G.connectionWay==3){

//					Log.i("ver","1");
//					byte[] byteMyVersion = new byte[G.MY_VERSION.length()];
//					byteMyVersion = G.MY_VERSION.getBytes();
					Log.i("apk","2");

			        MyPacket packet = new MyPacket();
			        packet.cmd = G.CMD_GetApk;
			        packet.len = 0;
					Log.i("apk","3");

			        G.dos.write(MyPacket.toBytes(packet));
					Log.i("apk","4");

			        G.dos.flush();
//					Log.i("ver","5");
//			        G.dos.write(byteMyVersion);
//					Log.i("ver","6  - "+G.bis.available());

//			        byte[] recvBytesPacket = new byte[G.sizeOfPacket]; 
//			        int countRead = G.bis.read(recvBytesPacket, 0, recvBytesPacket.length);
//
//					Log.i("usb","countRead : "+countRead);
//
//			        if(countRead == G.sizeOfPacket){	        
//			        	MyPacket recvPacket = MyPacket.toMyPacket(recvBytesPacket);
//			        	Log.i("usb",String.format("cmd = %s ,  len = %s ",String.valueOf(recvPacket.cmd),String.valueOf(recvPacket.len)));
//			        	
//				        byte[] recvBytes = new byte[recvPacket.len]; 
//			        	int countRead2 = G.bis.read(recvBytes, 0, recvPacket.len);
//			        	if(countRead2 == recvPacket.len){
//			        		String strLastVersion = new String(recvBytes);
//			        		Log.i("usb","strVersion : "+strLastVersion);
//			        		G.prefEditor = G.myPref.edit();
//	                		G.prefEditor.putString(G.PREF_KEY_LAST_VERSION, strLastVersion);
//	                		G.prefEditor.commit();
//				            if(Integer.parseInt(G.MY_VERSION.replace(".", "")) < Integer.parseInt(strLastVersion.replace(".", "")) ){
//				            	G.handler.post(new Runnable() {
//					                @Override
//					                public void run() {
//					                	//MyToast.Show(G.context,String.format("%s %d %s", "تعداد",lstTableUser.length,"کاربر بروزرسانی شد"), Toast.LENGTH_SHORT);
//					                	Intent intent = new Intent(G.context, ActivityUpdateVersion.class);
//					                	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					                	startActivity(intent);
//					                	G.currentActivity.finish();
//					                }
//					            });
//				            }
//			        	}
//			        }
				}else if(G.connectionWay == 2){
				
					try {
					      URL u = new URL(G.APK_ADDRESS);
					      URLConnection conn = u.openConnection();
					      int contentLength = conn.getContentLength();
				          Log.i("apk", "contentLength : "+contentLength);

					      DataInputStream stream = new DataInputStream(u.openStream());

					        byte[] buffer = new byte[contentLength];
					        stream.readFully(buffer);
					        stream.close();

					        DataOutputStream fos = new DataOutputStream(new FileOutputStream(G.DIR_DOWNLOAD+"/PM.apk"));
//					        int total = 0;
//					        while(stream.available()>0){
//					        	int bufferLen = Math.min(G.bufferSize, contentLength);
//					        	fos.write(buffer,total,bufferLen);
//					        	total+=bufferLen;
//					        }
					        fos.write(buffer);
					        fos.flush();
					        fos.close();
					        
					        if(new File(G.DIR_DOWNLOAD+"/PM.apk").exists()){
						          Log.i("apk", "file exist");
					        }else{
						          Log.i("apk", "file NOT exist");
					        }
					        
					        G.handler.post(new Runnable() {
								
								@Override
								public void run() {
									Intent intent = new Intent(Intent.ACTION_VIEW);
							        intent.setDataAndType(Uri.fromFile(new File(G.DIR_DOWNLOAD+"/PM.apk")), "application/vnd.android.package-archive");
							        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							        startActivity(intent);
								}
							});
					        
					        
					        
					  } catch(FileNotFoundException e) {
					      return; // swallow a 404
					  } catch (IOException e) {
					      return; // swallow a 404
					  }
					
				}
				else{
					G.handler.post(new Runnable() {
		                @Override
		                public void run() {
		                	MyToast.Show(G.context, String.format("%s\n%s\n%s",getText(R.string.UsbWifiIsDC), getText(R.string.UsbWifiIsC),getText(R.string.NetWorkConnect)), Toast.LENGTH_SHORT);
		                	imgUpVersion.setImageDrawable(G.context.getResources().getDrawable(R.drawable.download_white));
		    				txtVersion3.setVisibility(View.INVISIBLE);
		    				pbVer.setVisibility(View.INVISIBLE);
		                }
		            });
				}

	        }
	        catch (Exception e) {
	        	G.handler.post(new Runnable() {
	                @Override
	                public void run() {
	                	MyToast.Show(G.context,String.format("%s", getText(R.string.ErrorInConnectServer)), Toast.LENGTH_SHORT);
	                }
	            });
	            Log.i("apk", e.toString());
	        }
            threadGetNewApk = null;
		}
	}
	
	Boolean doubleBackToExitPressedOnce = false;
	@Override
	public void onBackPressed() {
	   Log.i("tag", "onBackPressed Called");

		   if (doubleBackToExitPressedOnce) {
			   	ActivityUpdateVersion.this.finish();
			   	System.exit(0);
		        return;
		    }

		    this.doubleBackToExitPressedOnce = true;
		    MyToast.Show(this, (String) getText(R.string.Msg_Exit), Toast.LENGTH_SHORT);
		    new Handler().postDelayed(new Runnable() {

		        @Override
		        public void run() {
		            doubleBackToExitPressedOnce=false;                       
		        }
		    }, 1500);

	}
	
}
