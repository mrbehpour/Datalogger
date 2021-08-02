package ir.saa.android.datalogger;

import ir.saa.android.datalogger.R.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.Inflater;

import mycomponents.MyDialog;
import mycomponents.MyEditText;
import mycomponents.MyToast;
import database.structs.dtoShifts;
import database.structs.dtoUsers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class ActivityLogin extends Activity {
    MyDialog permissionDialog;
    //Bitmap bm;
    Thread threadGetUsersFromDB = null;

    ArrayList<dtoUsers> lstUsers = new ArrayList<dtoUsers>();
    ArrayAdapter<dtoUsers> dataAdapterUsers;
    ArrayList<dtoShifts> lstShifts = new ArrayList<dtoShifts>();
    ArrayAdapter<dtoShifts> dataAdapterShift;
    ImageView imgLoginLogo;
    boolean isKeyPress=false;
    boolean isMessageShow=false;
    Spinner spnUsers;

    Typeface tfByekan;

    public  void adjustFontScale(Configuration configuration, Float fontSize) {

        configuration.fontScale = (float) fontSize;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }

    public static void hide_keyboard(View vew) {
        InputMethodManager inputMethodManager = (InputMethodManager) vew.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = vew;
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = vew;
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getSystemLocale(){
        G.appLang = getResources().getConfiguration().locale.toString();
        switch (G.appLang){
            case "fa_IR":
                G.RTL=true;
                break;
            default:
                G.RTL=false;
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LinearLayout llLogin=(LinearLayout)findViewById(R.id.llLogin);
        imgLoginLogo=(ImageView)findViewById(R.id.imgLoginLogo);
        getSystemLocale();

        if(G.RTL) {//TODO: LTR
            llLogin.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            tfByekan = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
            imgLoginLogo.setImageDrawable(getResources().getDrawable( R.drawable.title1));
        }
        else {
            llLogin.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            imgLoginLogo.setImageDrawable(getResources().getDrawable(R.drawable.title1_en));
            tfByekan = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
            adjustFontScale(getResources().getConfiguration(),(float)0.75);


        }
        final WebView myWebview = (WebView) findViewById(R.id.myWebview);


        myWebview.getSettings().setJavaScriptEnabled(true);
        //myWebview.getSettings().setPluginsEnabled(true);
        myWebview.getSettings().setAllowFileAccess(true);
        myWebview.requestFocus();
        myWebview.getSettings().setLoadWithOverviewMode(false);
        myWebview.getSettings().setUseWideViewPort(false);
        myWebview.loadUrl("file:///android_asset/htmls/login.html");

        G.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myWebview.setVisibility(View.VISIBLE);
            }
        },1000);

        PreferenceManager.setDefaultValues(G.context, R.xml.preferences, false);
        permissionDialog = new MyDialog(ActivityLogin.this);

        ArrayList<Boolean> permissionGrants = new ArrayList<>();
        permissionGrants.add(checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        permissionGrants.add(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
        permissionGrants.add(checkPermission(Manifest.permission.CAMERA));
        permissionGrants.add(checkPermission(Manifest.permission.RECORD_AUDIO));
        //permissionGrants.add(checkPermission(Manifest.permission.CAPTURE_AUDIO_OUTPUT));
        permissionGrants.add(checkPermission(Manifest.permission.READ_PHONE_STATE));
        permissionGrants.add(checkPermission(Manifest.permission.ACCESS_NETWORK_STATE));
        permissionGrants.add(checkPermission(Manifest.permission.ACCESS_WIFI_STATE));
        permissionGrants.add(checkPermission(Manifest.permission.INTERNET));
        permissionGrants.add(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        permissionGrants.add(checkPermission(Manifest.permission.NFC));
        permissionGrants.add(checkPermission(Manifest.permission.VIBRATE));

        if (permissionGrants.contains(false)) {
            if (!permissionDialog.getDialog().isShowing())
                showPermissionDialog();
        } else {
            if (permissionDialog.getDialog().isShowing()) {
                permissionDialog.dismiss();

                G.telephonyManager = (TelephonyManager) this.getSystemService(this.getApplicationContext().TELEPHONY_SERVICE);
                if(G.telephonyManager!=null) {
                    G.serialNumberOfDevice =  G.telephonyManager.getDeviceId().toString() ;
                }
            }
        }



        //G.DB.SelectViews();
        //----Gps check----
//		if(!G.isGpsEnabled()){
//			showGpsStatusDialog();
//		}
        //------------------

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
       // Button btnSettingInLogin = (Button) findViewById(R.id.btnSettingInLogin);
        final MyEditText edtPassword = (MyEditText) findViewById(R.id.edtPassword);
        edtPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
        final TextView txtVersionLogin = (TextView) findViewById(R.id.txtVersionLogin);
        final TextView txtUserCode = (TextView) findViewById(R.id.txtUserCode);
        txtUserCode.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
        spnUsers = (Spinner) findViewById(R.id.spnUsername);
        final Spinner spnShifts = (Spinner) findViewById(R.id.spnShift);


        // Spinner Users--------------------------------------------------
        dtoUsers dtoUser = new dtoUsers();
        dtoUser.FullName = "";
        lstUsers.add(dtoUser);
        dtoShifts dtoShift = new dtoShifts();
        dtoShift.Name = "";
        lstShifts.add(dtoShift);

        dataAdapterUsers = new ArrayAdapter<dtoUsers>(G.context, R.layout.spinner_item, lstUsers);
        dataAdapterUsers.sort(new Comparator<dtoUsers>() {
            @Override
            public int compare(dtoUsers o1, dtoUsers o2) {
                return o1.FirstName==o2.FirstName?1:0 ;
            }
        });
        dataAdapterUsers.setDropDownViewResource(R.layout.spinner_chtxt_item_alpha);
        spnUsers.setAdapter(dataAdapterUsers);
        // Spinner Shift--------------------------------------------------
        dataAdapterShift = new ArrayAdapter<dtoShifts>(G.context, R.layout.spinner_item, lstShifts);
        dataAdapterShift.setDropDownViewResource(R.layout.spinner_chtxt_item_alpha);
        spnShifts.setAdapter(dataAdapterShift);

        //TODO initialize edttexts
//		edtUsername.setText("mojri");
//		edtPassword.setText("123");
        //------------------------
//		G.DB.SelectUsers();
//		int countBazdidMaaber = G.DB.GetViewCountRM(G.NoeBarnameRizi.BAZDID_ROSHANAYI_ROSHANAYI,"20560f56-66d1-4bc4-8a95-f5edf9b775c9");
//		G.DB.SelectViews();
        //G.DB.GetSelectTest();
//		ArrayList<String> picNames = G.DB.GetNotSendPics(G.USER_ID);
//        Log.i("pic","pic name len : "+picNames.size());
        //------------------------
        txtUserCode.setTypeface(tfByekan);
        btnLogin.setTypeface(tfByekan);
        //btnSettingInLogin.setTypeface(tfByekan);
        edtPassword.setTypeface(tfByekan);
        txtVersionLogin.setTypeface(tfByekan);
        txtVersionLogin.setText(String.format("%s %s", G.context.getResources().getText(string.Version), G.MY_VERSION));


//        btnSettingInLogin.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(G.context, ActivitySettings.class);
//                startActivity(intent);
//            }
//        });

        edtPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(G.RTL==false){
                    hide_keyboard(v);
                }
            }
        });

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK){
                    edtPassword.setText("");
                    isKeyPress=false;
                    return true;
                }

                edtPassword.onKeyPreIme(keyCode,event);

                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    try {
                        if(!isKeyPress) {
                            isKeyPress=true;
                            //G.updateGpsLocation();
                            boolean IsLoginOk = G.DB.IsLoginOk(
                                    ((dtoUsers) spnUsers.getSelectedItem()).UserName,
                                    edtPassword.getText().toString()
                            );
                            if (IsLoginOk) {
                                G.currentUser = ((dtoUsers) spnUsers.getSelectedItem());
//                    G.PREF_KEY_LAST_UPDATE_SETTING = "updateViewsKey_" + G.currentUser.UsrID;
                                G.WEB_SERVICE = G.sharedPref.getString(G.PREF_KEY_WEB_SERVICE, "");

                                Intent intent = new Intent(G.context, ActivityDrawer.class);
                                startActivity(intent);
                                finish();

                            } else {
                                if(!isMessageShow) {
                                    isMessageShow=true;
                                    isKeyPress = false;
                                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.LoginFail), Toast.LENGTH_SHORT);
                                }
//					if(threadGetUsers==null){
//			    		threadGetUsers = new Thread(new getUsersInfoFromServer(),"thread_getUsersInfoFromServer");
//			    		threadGetUsers.start();
//					}
                            }
                        }
                    }catch (Exception e){

                    }

                    return true;
                }
                if(G.RTL==false){
                   // hide_keyboard( v);
                }
                return false;
            }
        });



        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    //G.updateGpsLocation();
                    boolean IsLoginOk = G.DB.IsLoginOk(
                            ((dtoUsers) spnUsers.getSelectedItem()).UserName,
                            edtPassword.getText().toString()
                    );
                    if (IsLoginOk) {
                        G.currentUser = ((dtoUsers) spnUsers.getSelectedItem());
//                    G.PREF_KEY_LAST_UPDATE_SETTING = "updateViewsKey_" + G.currentUser.UsrID;
                        G.WEB_SERVICE = G.sharedPref.getString(G.PREF_KEY_WEB_SERVICE, "");

                        Intent intent = new Intent(G.context, ActivityDrawer.class);
                        startActivity(intent);
                        finish();

                    } else {
                        MyToast.Show(G.context, (String)G.context.getResources().getText(R.string.LoginFail), Toast.LENGTH_SHORT);
//					if(threadGetUsers==null){
//			    		threadGetUsers = new Thread(new getUsersInfoFromServer(),"thread_getUsersInfoFromServer");
//			    		threadGetUsers.start();
//					}
                    }
                }catch (Exception e){

                }
            }

        });

        spnUsers.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final dtoUsers oUser = (dtoUsers) spnUsers.getSelectedItem();
                if(oUser == null || oUser.UserCode == null)
                    return;
                txtUserCode.setText(String.format((String) G.context.getResources().getText(R.string.CodePersonWithParam), oUser.UserCode.toString()));
                //MyToast.Show(G.context, oUser.FullName+" "+oUser.UsrID, Toast.LENGTH_SHORT);
                ArrayList<dtoShifts> lstShift = G.DB.getShiftsByUserId(oUser);
                if (lstShift.size() > 0) {
                    lstShifts.clear();
                    for (dtoShifts dtoShift : lstShift) {
                        lstShifts.add(dtoShift);
                    }
                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {

                            dataAdapterShift.notifyDataSetChanged();
                            if (oUser.ShiftID != null) {
                                for (int i = 0; i < spnShifts.getCount(); i++) {
                                    if (((dtoShifts) spnShifts.getItemAtPosition(i)).ID == oUser.ShiftID) {
                                        spnShifts.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (threadGetUsersFromDB == null) {
            threadGetUsersFromDB = new Thread(new getUsersInfoFromDB(), "thread_getUsersInfoFromDB");
            threadGetUsersFromDB.start();
        }


    }

    private void showPermissionDialog() {
        permissionDialog.clearAllPanel();
        permissionDialog.setTitle((String) getText(string.Requestlicense))
                .setBackgroundAlpha(0.95f)
                .addContentXml(R.layout.body_one_textview)
                .addButton((String) getResources().getText(R.string.Licensing), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        permissionDialog.show();

        TextView txt1 = permissionDialog.getDialog().findViewById(R.id.txtBodymessage);
        txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
        txt1.setText(getText(R.string.AllPermesion));
        txt1.setTypeface(tfByekan);
    }

    private boolean checkPermission(String permission) {
        return (G.context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    private void showGpsStatusDialog() {
        try {
            //------------------------------------------------------------->>ActivityLogin.this
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityLogin.this);
            alertDialogBuilder.setMessage(string.wanna_turn_on_gps)
                    .setCancelable(false)
                    .setPositiveButton(string.go_to_gps_setting,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton(string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        } catch (Exception ex) {
            //MyToast.Show(G.context, ex.getMessage(), 3000);
        }
    }


    public class getUsersInfoFromDB implements Runnable {
        public void run() {
            try {
                ArrayList<dtoUsers> lstUsersFromDB = G.DB.getUsersFromDB();
                if (lstUsers.size() > 0) {
                    lstUsers.clear();
                    for (dtoUsers dtoUser : lstUsersFromDB) {
                        lstUsers.add(dtoUser);
                    }



                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dataAdapterUsers.notifyDataSetChanged();
                            spnUsers.setSelection(0);
                        }
                    });
                }

            } catch (Exception e) {

                Log.i("tag", e.toString());
                threadGetUsersFromDB = null;
            }
            System.gc();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Boolean> permissionGrants = new ArrayList<>();
        permissionGrants.add(checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        permissionGrants.add(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
        permissionGrants.add(checkPermission(Manifest.permission.CAMERA));
        permissionGrants.add(checkPermission(Manifest.permission.RECORD_AUDIO));
        //permissionGrants.add(checkPermission(Manifest.permission.CAPTURE_AUDIO_OUTPUT));
        permissionGrants.add(checkPermission(Manifest.permission.READ_PHONE_STATE));
        permissionGrants.add(checkPermission(Manifest.permission.ACCESS_NETWORK_STATE));
        permissionGrants.add(checkPermission(Manifest.permission.ACCESS_WIFI_STATE));
        permissionGrants.add(checkPermission(Manifest.permission.INTERNET));
        permissionGrants.add(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION));
        permissionGrants.add(checkPermission(Manifest.permission.NFC));
        permissionGrants.add(checkPermission(Manifest.permission.VIBRATE));

        if (permissionGrants.contains(false)) {
            if (!permissionDialog.getDialog().isShowing())
                showPermissionDialog();
        } else {
            if (permissionDialog.getDialog().isShowing()) {
                permissionDialog.dismiss();

                G.telephonyManager = (TelephonyManager) this.getSystemService(this.getApplicationContext().TELEPHONY_SERVICE);
                if(G.telephonyManager!=null) {
                    G.serialNumberOfDevice =  G.telephonyManager.getDeviceId().toString() ;
                }
            }
        }

        G.WEB_SERVICE = G.sharedPref.getString(G.PREF_KEY_WEB_SERVICE, "");
        G.connectionWay = G.getConnectionWay();
        if (G.connectionWay == 0) {

//			MyToast.Show(G.context, String.format("%s\n%s\n%s","وایفای و USB قطع است", "لطفا ابتدا وایفای یا USB را متصل کنید ","و به شبکه وصل شوید"), Toast.LENGTH_SHORT);
            return;
        }
//		if(threadCheckWs==null){
//			threadCheckWs = new Thread(new TaskCheckWs(),"thread_getUsersInfoFromServer");
//			threadCheckWs.start();
//		}

    }



    //	Boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

//		   if (doubleBackToExitPressedOnce) {
//			   	ActivityLogin.this.finish();
//			   	System.exit(0);
//		        return;
//		    }
//
//		    this.doubleBackToExitPressedOnce = true;
//		    MyToast.Show(this, "برای خروج دوبار کلیک کنید", Toast.LENGTH_SHORT);
//		    new Handler().postDelayed(new Runnable() {
//
//		        @Override
//		        public void run() {
//		            doubleBackToExitPressedOnce=false;
//		        }
//		    }, 1500);

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //Toast.makeText(this, "You pressed the menu button!", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void onAttachedToWindow() {
//        //super.onAttachedToWindow();
//        //this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//
//        return;
//    }

//    public void onUserLeaveHint() { // this only executes when Home is selected.
//        // do stuff
//        super.onUserLeaveHint();
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//            keyguardManager.requestDismissKeyguard(ActivityLogin.this, null);
//        }
//    }



}
