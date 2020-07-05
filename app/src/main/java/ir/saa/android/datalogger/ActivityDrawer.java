package ir.saa.android.datalogger;

import database.adapters.AdapterItem;
import database.structs.dtoItemValuesForSend;
import database.structs.dtoTajhiz;
import database.structs.dtoUsrPost;
import ir.saa.android.datalogger.R.string;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.Inflater;

import mycomponents.MyDialog;
import mycomponents.MyToast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ndeftools.Message;

import nfc.ActivityNfcTest;
import nfc.NfcReaderActivity;
import socket.structs.DataRef;
import socket.structs.MyPacket;


import com.google.gson.Gson;

import database.structs.dtoCorsRems;
import database.structs.dtoEquipments;
import database.structs.dtoFormulas;
import database.structs.dtoItemFormulas;
import database.structs.dtoItemTypeValues;
import database.structs.dtoItemValues;
import database.structs.dtoItems;
import database.structs.dtoLogShits;
import database.structs.dtoLogics;
import database.structs.dtoMaxItemVal;
import database.structs.dtoMeasures;
import database.structs.dtoPackItems;
import database.structs.dtoPackUser;
import database.structs.dtoPosts;
import database.structs.dtoRelateUserLogshitTbl;
import database.structs.dtoRemarkGroup;
import database.structs.dtoRemarks;
import database.structs.dtoSetting;
import database.structs.dtoShifts;
import database.structs.dtoSubEquipments;
import database.structs.dtoUserShift;
import database.structs.dtoUsers;
import usb.UsbController;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.widget.Toolbar;

import static android.view.View.FOCUS_DOWN;

public class ActivityDrawer extends NfcReaderActivity implements
        ActionBar.TabListener {
    //--- for test header----
//	Integer[] bgList = {R.drawable.header1, R.drawable.header2, R.drawable.header3, R.drawable.header4, R.drawable.header5, R.drawable.header6};
//	private int counter;
    //----------------NFC--------------
    private static final String TAG = ActivityNfcTest.class.getName();

    boolean isOpenDrawer = false;
    protected Message message;
    private static MyDialog nfcDialog;
    private ImageView imgNFC = new ImageView(G.context);
    View vLastFocuse;
    public dtoTajhiz nfcLogshit = null;
    public dtoItems nfcItem = null;
    public Integer nfcItemPos = null;
    public AdapterItem.ItemDirection nfcItemDirection = AdapterItem.ItemDirection.None;
    public AdapterItem adapterItem;
    int indexItem=1;
    int indexPage=1;
    //---------------------------------
    private static Typeface tf;
    // Storage for camera image URI components
    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    private final static String CAPTURED_PHOTO_URI_KEY = "mCapturedImageURI";

    // Required for camera operations in order to save the image file on resume.
    private String mCurrentPhotoPath = null;
    private Uri mCapturedImageURI = null;
    //---------------------------------
    LinearLayout drawer_layout;
    public static Thread threadGetSetting = null;
    public static Thread threadGetItems = null;
    public static Thread threadGetUsers = null;
    public static Thread threadGetMaxItemVal = null;
    public static Thread threadSend = null;
    private Handler handler;
    ProgressBar pb01;
    ProgressBar pb02;
    ProgressBar pb03;
    ProgressBar pb04;
    ProgressBar pb05;
    Toolbar toolbar;

    TextView txtMenu1;
    TextView txtMenu2;
    TextView txtMenu3;
    TextView txtMenu4;
    TextView txtMenuLastUpdateDate01;
    TextView txtMenuLastUpdateDate02;
    TextView txtMenuLastUpdateDate03;
    TextView txtMenuLastUpdateDate04;
    TextView txtMenuLastUpdate01;
    TextView txtMenuLastUpdate02;
    TextView txtMenuLastUpdate03;
    TextView txtMenuLastUpdate04;
    LinearLayout menuLayout1;
    LinearLayout menuUpdateLayout1;
    LinearLayout menuLayout2;
    LinearLayout menuUpdateLayout2;
    LinearLayout menuLayout3;
    LinearLayout menuUpdateLayout3;
    LinearLayout menuLayout4;
    LinearLayout menuUpdateLayout4;
    LinearLayout llMain;
    TextView txtMenuDate;
    int menuCustom;

    TextView txtMenuGetBaseInfo;
    TextView txtMenuLastGetBaseInfo;
    TextView txtMenuLastGetBaseInfoDate;
    TextView txtMenuSend;
    TextView txtMenuLastSend;
    TextView txtMenuLastSendDate;
    TextView txtMenuHome;
    TextView txtMenuShowNfcContent;
    LinearLayout llMenuShowNfcContent;
    TextView txtMenuSetting;
    TextView txtMenuAboutUs;
    TextView txtMenuExit;
    TextView txtMenuItem1;
    TextView txtMenuItem2;
    TextView txtCircle1;
    TextView txtCircle2;
    RelativeLayout rlCircle1;
    RelativeLayout rlCircle2;
    LinearLayout llMenu;
    dtoPackItems packItems = null;
    dtoSetting objSetting = null;
    dtoPackUser packUser = null;
    dtoMaxItemVal[] lstMaxItemVal = null;
    DrawerLayout.LayoutParams params;
    LinearLayout.LayoutParams paramsTextView;
    LinearLayout.LayoutParams paramsProgressbar;
    int setLayoutDisplay;

    public void adjustFontScale(Configuration configuration, Float fontSize) {

        configuration.fontScale = (float) fontSize;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paramsTextView = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        //paramsProgressbar= new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        setLayoutDisplay = R.layout.activity_drawer;
        menuCustom = R.layout.custom_menu;
        if (G.RTL) {//TODO: LTR
            tf= Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
        } else {
            tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
            adjustFontScale(getResources().getConfiguration(), (float) 1.5);
        }

        setContentView(setLayoutDisplay);

        G.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        llMain = (LinearLayout) findViewById(R.id.llMain);
        G.llMenu = (LinearLayout) findViewById(R.id.llMenu);
        int width = (getResources().getDisplayMetrics().widthPixels * 90) / 100;
        params = (DrawerLayout.LayoutParams) G.llMenu.getLayoutParams();
        params.width = width;

        // set llMenu's width to 90% of screen's width

        //llMenu.setLayoutParams(params);
        G.llMenu.setLayoutParams(params);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        G.currentActivity = this;

        nfcDialog = new MyDialog(ActivityDrawer.this);

        G.fragmentManager = getSupportFragmentManager();
        Fragment fragment = new FragmentHome();
        G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        //drawer adjustments


        //-----SwipeView Tabs----------
        // Initialization
        G.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.frame_container);
        G.actionBar = getSupportActionBar(); //getActionBar();

        //----------Set NFC Detecting ON---------------
//		G.Setting.ModTag = 1; //TODO--should be comment in future--
//		G.Setting.LayerTag=1;
        if (G.Setting != null && G.Setting.ModTag != 0) {
            setDetecting(true);
        }
    }

    @Override
    protected void onNfcStateEnabled() {

    }

    @Override
    protected void onNfcStateDisabled() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ShowNfcDialog() {
        nfcDialog.clearAllPanel();
        imgNFC.setImageDrawable(G.context.getResources().getDrawable(R.drawable.nfc_black));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300, 150);
        lp.gravity = Gravity.CENTER;
        imgNFC.setLayoutParams(lp);
        nfcDialog.setTitle((String) G.context.getResources().getText(string.Msg_NfcTag2))
                .setBackgroundAlpha(0.95f)
                .addContentView(imgNFC)
                .setVerticalMargin(5)
                .addContentXml(R.layout.body_one_textview)
                .addButton((String) G.context.getResources().getText(string.Close), new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//						setDetecting(false);
//						stopDetecting();
                        if (G.selectedMenuItemType == G.MenuItemTypes.ITEM && G.selectedMenuItemMode == G.MenuItemMode.Sabt) {
                            if (nfcItemDirection == AdapterItem.ItemDirection.Left) {
                                if ((nfcItemPos - 1) > 0) {
                                    adapterItem.showDialog(TabFragmentItem.arrListItemFiltered.get(nfcItemPos - 1), nfcItemPos - 1);
                                }else{
                                    adapterItem.dialog.dismiss();
                                    nfcDialog.dismiss();
                                }
                            }
                            else if (nfcItemDirection == AdapterItem.ItemDirection.Right) {
                                if((nfcItemPos + 1)<TabFragmentItem.arrListItemFiltered.size()) {
                                    adapterItem.showDialog(TabFragmentItem.arrListItemFiltered.get(nfcItemPos + 1), nfcItemPos + 1);
                                }else{
                                    adapterItem.dialog.dismiss();
                                    nfcDialog.dismiss();

                                }
                            }
                        }
                        nfcDialog.dismiss();
                    }
                });
        nfcDialog.show();

        TextView txt1 = nfcDialog.getDialog().findViewById(R.id.txtBodymessage);
        txt1.setText(G.context.getResources().getText(string.Msg_NfcTag3));
        txt1.setTypeface(tf);

//		Animation a = new RotateAnimation(0.0f, 360.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		a.setRepeatCount(0);
//		a.setDuration(700);
//		imgNFC.startAnimation(a);
    }

    public void SuccessNfcDialog() {
//		Animation a = new RotateAnimation(0.0f, 360.0f,
//		Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//		0.5f);
//		a.setRepeatCount(0);
//		a.setDuration(700);
//		imgNFC.startAnimation(a);
        ObjectAnimator
                .ofFloat(imgNFC, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(500)
                .start();

        imgNFC.setImageDrawable(G.context.getResources().getDrawable(R.drawable.nfc_green));
        TextView txt1 = nfcDialog.getDialog().findViewById(R.id.txtBodymessage);
        if (txt1 != null) {
            txt1.setText(G.context.getResources().getText(R.string.Msg_NfcTag4));
            txt1.setTextColor(Color.parseColor("#007700"));
            txt1.setTypeface(tf);
        }
        if (G.mDrawerLayout.isDrawerOpen(G.llMenu))
            G.mDrawerLayout.closeDrawer(G.llMenu);
    }

    public void FailedNfcDialog() {
//		Animation a = new RotateAnimation(0.0f, 360.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		a.setRepeatCount(0);
//		a.setDuration(700);
//		imgNFC.startAnimation(a);
        ObjectAnimator
                .ofFloat(imgNFC, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(500)
                .start();

        imgNFC.setImageDrawable(G.context.getResources().getDrawable(R.drawable.nfc_red));
        TextView txt1 = nfcDialog.getDialog().findViewById(R.id.txtBodymessage);
        txt1.setText(G.context.getResources().getText(string.Msg_NfcTag5));
        txt1.setTextColor(Color.parseColor("#990000"));
        txt1.setTypeface(tf);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        handler = new Handler();

        G.actionBar.setHomeButtonEnabled(false);

        //G.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        G.actionBar.setCustomView(menuCustom);

        View view = G.actionBar.getCustomView();
        view.setLayoutParams(paramsTextView);
        G.actionBar.setDisplayShowHomeEnabled(false);
        G.actionBar.setDisplayShowTitleEnabled(false);
        G.actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


        TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
//        TextView txtNav1 = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtNav1);
//        TextView txtNav2 = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtNav2);

        rlCircle1 = (RelativeLayout) G.llMenu.findViewById(R.id.rlCircle1);
        rlCircle2 = (RelativeLayout) G.llMenu.findViewById(R.id.rlCircle2);
        txtMenuDate = (TextView) G.llMenu.findViewById(R.id.txtMenuDate);

        txtMenuLastUpdate01 = (TextView) G.llMenu.findViewById(R.id.txtMenuLastUpdate01);
        txtMenuLastUpdate02 = (TextView) G.llMenu.findViewById(R.id.txtMenuLastUpdate02);
        txtMenuLastUpdate03 = (TextView) G.llMenu.findViewById(R.id.txtMenuLastUpdate03);
        txtMenuLastUpdate04 = (TextView) G.llMenu.findViewById(R.id.txtMenuLastUpdate04);
        txtMenuLastUpdateDate01 = (TextView) G.llMenu.findViewById(R.id.txtMenuLastUpdateDate01);
        txtMenuLastUpdateDate01.setTypeface(tf);
        txtMenuLastUpdateDate02 = (TextView) G.llMenu.findViewById(R.id.txtMenuLastUpdateDate02);
        txtMenuLastUpdateDate02.setTypeface(tf);
        txtMenuLastUpdateDate03 = (TextView) G.llMenu.findViewById(R.id.txtMenuLastUpdateDate03);
        txtMenuLastUpdateDate03.setTypeface(tf);
        txtMenuLastUpdateDate04 = (TextView) G.llMenu.findViewById(R.id.txtMenuLastUpdateDate04);
        txtMenuLastUpdateDate04.setTypeface(tf);
        menuLayout1 = (LinearLayout) G.llMenu.findViewById(R.id.menuLayout1);
        menuUpdateLayout1 = (LinearLayout) G.llMenu.findViewById(R.id.menuUpdateLayout1);
        menuLayout2 = (LinearLayout) G.llMenu.findViewById(R.id.menuLayout2);
        menuUpdateLayout2 = (LinearLayout) G.llMenu.findViewById(R.id.menuUpdateLayout2);
        menuLayout3 = (LinearLayout) G.llMenu.findViewById(R.id.menuLayout3);
        menuUpdateLayout3 = (LinearLayout) G.llMenu.findViewById(R.id.menuUpdateLayout3);
        menuLayout4 = (LinearLayout) G.llMenu.findViewById(R.id.menuLayout4);
        menuUpdateLayout4 = (LinearLayout) G.llMenu.findViewById(R.id.menuUpdateLayout4);

        txtMenuGetBaseInfo = (TextView) G.llMenu.findViewById(R.id.txtMenuGetBaseInfo);
        txtMenuLastGetBaseInfo = (TextView) G.llMenu.findViewById(R.id.txtMenuLastGetBaseInfo);
        txtMenuLastGetBaseInfoDate = (TextView) G.llMenu.findViewById(R.id.txtMenuLastGetBaseInfoDate);
        txtMenuSend = (TextView) G.llMenu.findViewById(R.id.txtMenuSend);
        txtMenuLastSend = (TextView) G.llMenu.findViewById(R.id.txtMenuLastSend);
        txtMenuLastSendDate = (TextView) G.llMenu.findViewById(R.id.txtMenuLastSendDate);
        txtMenuHome = (TextView) G.llMenu.findViewById(R.id.txtMenuHome);
        txtMenuShowNfcContent = G.llMenu.findViewById(R.id.txtMenuShowNfcContent);
        llMenuShowNfcContent = G.llMenu.findViewById(R.id.llMenuShowNfcContent);
        txtMenuSetting = (TextView) G.llMenu.findViewById(R.id.txtMenuSetting);
        txtMenuAboutUs = (TextView) G.llMenu.findViewById(R.id.txtMenuAboutUs);
        txtMenuExit = (TextView) G.llMenu.findViewById(R.id.txtMenuExit);
        txtMenuItem1 = (TextView) G.llMenu.findViewById(R.id.txtMenuItem1);
        txtMenuItem1.requestFocus();
        txtMenuItem2 = (TextView) G.llMenu.findViewById(R.id.txtMenuItem2);
        txtCircle1 = (TextView) G.llMenu.findViewById(R.id.txtCircle1);
        txtCircle2 = (TextView) G.llMenu.findViewById(R.id.txtCircle2);
        pb01 = (ProgressBar) G.llMenu.findViewById(R.id.pb01);
        //pb01.setLayoutParams(paramsProgressbar);
        pb02 = (ProgressBar) G.llMenu.findViewById(R.id.pb02);
        //pb02.setLayoutParams(paramsProgressbar);
        pb03 = (ProgressBar) G.llMenu.findViewById(R.id.pb03);
        //pb03.setLayoutParams(paramsProgressbar);
        pb04 = (ProgressBar) G.llMenu.findViewById(R.id.pb04);
        //pb04.setLayoutParams(paramsProgressbar);
        pb05 = (ProgressBar) G.llMenu.findViewById(R.id.pb05);
        //pb05.setLayoutParams(paramsProgressbar);
        txtMenu1 = (TextView) G.llMenu.findViewById(R.id.txtMenu1);
        txtMenu2 = (TextView) G.llMenu.findViewById(R.id.txtMenu2);
        txtMenu3 = (TextView) G.llMenu.findViewById(R.id.txtMenu3);
        txtMenu4 = (TextView) G.llMenu.findViewById(R.id.txtMenu4);
        txtMenu1.setTypeface(tf);
        txtMenu2.setTypeface(tf);
        txtMenu3.setTypeface(tf);
        txtMenu4.setTypeface(tf);
        //----------Menu--------
//        txtMenu1.setLayoutParams(paramsTextView);
//        txtMenu2.setLayoutParams(paramsTextView);
//        txtMenu3.setLayoutParams(paramsTextView);
//        txtMenu4.setLayoutParams(paramsTextView);
        //----------------------
//        LinearLayout  llSpace1=(LinearLayout)findViewById(R.id.llSpace1);
//        LinearLayout  llSpace2=(LinearLayout)findViewById(R.id.llSpace2);
//        LinearLayout  llSpace3=(LinearLayout)findViewById(R.id.llSpace3);
//        LinearLayout  llSpace4=(LinearLayout)findViewById(R.id.llSpace4);
//       //llSpace1.setLayoutParams(paramsTextView);
        //----------------------
//        txtMenuSend.setTypeface(tf);
//        txtMenuLastSend.setTypeface(tf);
//        txtMenuLastSendDate.setTypeface(tf);
//        txtMenuGetBaseInfo.setTypeface(tf);
//        txtMenuLastGetBaseInfo.setTypeface(tf);
//        txtMenuLastGetBaseInfoDate.setTypeface(tf);
//        txtMenuDate.setTypeface(tf);
//
//        txtMenuLastUpdate01.setTypeface(tf);
//        txtMenuLastUpdate02.setTypeface(tf);
//        txtMenuLastUpdate03.setTypeface(tf);
//        txtMenuLastUpdate04.setTypeface(tf);
//        txtMenuLastUpdateDate01.setTypeface(tf);
//        txtMenuLastUpdateDate02.setTypeface(tf);
//        txtMenuLastUpdateDate03.setTypeface(tf);
//        txtMenuLastUpdateDate04.setTypeface(tf);
//
//        txtMenuHome.setTypeface(tf);
//        txtMenuShowNfcContent.setTypeface(tf);
//        txtMenuSetting.setTypeface(tf);
//        txtMenuAboutUs.setTypeface(tf);
//        txtMenuExit.setTypeface(tf);
//
//        txtMenuItem1.setTypeface(tf);
//        txtMenuItem2.setTypeface(tf);
//        txtCircle1.setTypeface(tf);
//        txtCircle2.setTypeface(tf);
//
//        txtTitle.setTypeface(tf);
//        txtNav1.setTypeface(tf);
//        txtNav2.setTypeface(tf);
        String strDate = "";
        // set menu date
        if (G.RTL) {
            strDate = Tarikh.getFullCurrentShamsidate();
        } else {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            strDate = df.format(today);

        }
        txtMenuDate.setText(strDate);
        // todo --- remove and change later
        //String strDate1 = Tarikh.getCurrentShamsidate();
//		txtMenuLastGetBaseInfoDate.setText(strDate1);
//		txtMenuLastUpdateDate.setText(strDate1);
//		txtMenuLastSendDate.setText(strDate1);
        if (G.currentUser.IsManager == 1)
            llMenuShowNfcContent.setVisibility(View.VISIBLE);
        else
            llMenuShowNfcContent.setVisibility(View.GONE);


        UpdateUiViews();

        // set Update Code

        txtMenu1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.RegisterItem=false;
                if (G.getConnectionWay() == 0) {
                    MyToast.Show(G.context, String.format("%s\n%s\n%s", G.context.getResources().getText(R.string.WiFiNoconnected)
                            , G.context.getResources().getText(R.string.WifiOn), G.context.getResources().getText(R.string.ConnectToNetWork)), Toast.LENGTH_SHORT);
                    return;
                }
                if (threadGetMaxItemVal == null && threadGetSetting == null && threadGetUsers == null && threadGetItems == null && threadSend == null) {
                    gotoHomeFragment();
                    UpdateUiViews();
                    MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(string.UpdateSetting),
                            G.context.getResources().getText(string.PleaseWait)), 500);
                    pb01.setProgress(0);
                    pb01.setVisibility(View.VISIBLE);
                    threadGetSetting = new Thread(new TaskGetSetting());
                    threadGetSetting.start();
                } else {
                    MyToast.Show(G.context, (String) G.context.getResources().getText(string.PleaseWaitForOtherItem), 500);
                }
            }
        });


        txtMenu2.setOnClickListener(new OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                G.RegisterItem=false;
                if (G.getConnectionWay() == 0) {
                    MyToast.Show(G.context, String.format("%s\n%s\n%s", G.context.getResources().getText(R.string.WiFiNoconnected)
                            , G.context.getResources().getText(R.string.WifiOn), G.context.getResources().getText(R.string.ConnectToNetWork)), Toast.LENGTH_SHORT);
                    return;
                }
                if (threadGetMaxItemVal == null && threadGetSetting == null && threadGetUsers == null && threadGetItems == null && threadSend == null) {
                    if (G.DB.HasItemValuesAnyRecords()) {
                        if (G.Setting.SendItem == 1) {
//                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDrawer.this);
//                            alertDialogBuilder.setMessage(string.overwrite_packitems)
//                                    .setCancelable(false)
//                                    .setPositiveButton((String) G.context.getResources().getText(R.string.yes),
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    UpdateUiViews();
//                                                    MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(string.UpdateItem)
//                                                            , G.context.getResources().getText(string.PleaseWait)), 500);
//                                                    pb02.setProgress(0);
//                                                    pb02.setVisibility(View.VISIBLE);
//                                                    threadGetItems = new Thread(new TaskGetItems());
//                                                    threadGetItems.start();
//                                                }
//                                            });
//                            alertDialogBuilder.setNegativeButton((String) G.context.getResources().getText(R.string.no),
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });
//                            AlertDialog alert = alertDialogBuilder.create();
//                            alert.show();

                            MyDialog myDialog=new MyDialog(ActivityDrawer.this);
                            myDialog.addBodyText(getString(R.string.overwrite_packitems),10);
                            myDialog.addButtonL((String) G.context.getResources().getText(string.yes), new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UpdateUiViews();
                                    MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(string.UpdateItem)
                                            , G.context.getResources().getText(string.PleaseWait)), 500);
                                    pb02.setProgress(0);
                                    pb02.setVisibility(View.VISIBLE);
                                    threadGetItems = new Thread(new TaskGetItems());
                                    threadGetItems.start();
                                    myDialog.dismiss();
                                }
                            });
                            myDialog.addButtonR((String) G.context.getResources().getText(string.no), new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.dismiss();
                                }
                            }).show();
                        } else {
                            //TODO-check if itemValues isSend == 0 has items show error
                            int notSendedItemValuesCount = G.DB.getNotSendedItemValuesCount();
                            if (notSendedItemValuesCount > 0)
                                MyToast.Show(G.context, (String) G.context.getResources().getText(string.GetItemBeforeSend), Toast.LENGTH_SHORT);
                            else {
//                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDrawer.this);
//
//                                alertDialogBuilder.setMessage(string.overwrite_packitems)
//                                        .setCancelable(false)
//
//                                        .setPositiveButton((String) G.context.getResources().getText(string.yes),
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int id) {
//                                                        gotoHomeFragment();
//                                                        UpdateUiViews();
//                                                        MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(string.UpdateItem),
//                                                                G.context.getResources().getText(string.PleaseWait)), 500);
//                                                        pb02.setProgress(0);
//                                                        pb02.setVisibility(View.VISIBLE);
//                                                        threadGetItems = new Thread(new TaskGetItems());
//                                                        threadGetItems.start();
//                                                    }
//                                                });
//                                alertDialogBuilder.setNegativeButton((String) G.context.getResources().getText(string.no),
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//                                AlertDialog alert = alertDialogBuilder.create();
//                                alert.show();

                                MyDialog myDialog=new MyDialog(ActivityDrawer.this);
                                myDialog.addBodyText(getString(R.string.overwrite_packitems),12);
                                myDialog.addButtonL((String) G.context.getResources().getText(string.yes), new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        gotoHomeFragment();
                                        UpdateUiViews();
                                        MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(string.UpdateItem),
                                                G.context.getResources().getText(string.PleaseWait)), 500);
                                        pb02.setProgress(0);
                                        pb02.setVisibility(View.VISIBLE);
                                        threadGetItems = new Thread(new TaskGetItems());
                                        threadGetItems.start();
                                        myDialog.dismiss();
                                    }
                                });
                                myDialog.addButtonR((String) G.context.getResources().getText(string.no), new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myDialog.dismiss();
                                    }
                                }).show();
                            }
                        }
                    } else {
                        gotoHomeFragment();
                        UpdateUiViews();
                        MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(string.UpdateItem),
                                G.context.getResources().getText(string.PleaseWait)), 500);
                        pb02.setProgress(0);
                        pb02.setVisibility(View.VISIBLE);
                        threadGetItems = new Thread(new TaskGetItems());
                        threadGetItems.start();
                    }
                } else {
                    MyToast.Show(G.context, (String) G.context.getResources().getText(string.PleaseWaitForOtherItem), 500);
                }
            }
        });

        txtMenu3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.RegisterItem=false;
                if (G.getConnectionWay() == 0) {
                    MyToast.Show(G.context, String.format("%s\n%s\n%s", G.context.getResources().getText(R.string.WiFiNoconnected), G.context.getResources().getText(R.string.WifiOn),
                            G.context.getResources().getText(R.string.ConnectToNetWork)), Toast.LENGTH_SHORT);
                    return;
                }
                if (threadGetMaxItemVal == null && threadGetSetting == null && threadGetUsers == null && threadGetItems == null && threadSend == null) {
                    gotoHomeFragment();
                    UpdateUiViews();
                    MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(R.string.UpdateUser), G.context.getResources().getText(R.string.PleaseWait)), 500);
                    pb03.setProgress(0);
                    pb03.setVisibility(View.VISIBLE);
                    threadGetUsers = new Thread(new TaskGetUsers());
                    threadGetUsers.start();
                } else {
                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.PleaseWaitForOtherItem), 500);
                }
            }
        });

        txtMenu4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.RegisterItem=false;
                if (G.getConnectionWay() == 0) {
                    MyToast.Show(G.context, String.format("%s\n%s\n%s", G.context.getResources().getText(R.string.WiFiNoconnected), G.context.getResources().getText(R.string.WifiOn),
                            G.context.getResources().getText(R.string.ConnectToNetWork)), Toast.LENGTH_SHORT);
                    return;
                }
                if (threadGetMaxItemVal == null && threadGetSetting == null && threadGetUsers == null && threadGetItems == null && threadSend == null) {
                    gotoHomeFragment();
                    UpdateUiViews();
                    MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(R.string.UpdateLastData),
                            G.context.getResources().getText(R.string.PleaseWait)), 500);
                    pb04.setProgress(0);
                    pb04.setVisibility(View.VISIBLE);
                    threadGetMaxItemVal = new Thread(new TaskGetLastValues());
                    threadGetMaxItemVal.start();
                } else {
                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.PleaseWaitForOtherItem), 500);
                }
            }
        });


        txtMenuSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.RegisterItem=false;
                if (G.getConnectionWay() == 0) {
                    MyToast.Show(G.context, String.format("%s\n%s\n%s", G.context.getResources().getText(R.string.WiFiNoconnected), G.context.getResources().getText(R.string.WifiOn),
                            G.context.getResources().getText(R.string.ConnectToNetWork)), Toast.LENGTH_SHORT);
                    return;
                }
                if (threadGetMaxItemVal == null && threadGetSetting == null && threadGetUsers == null && threadGetItems == null && threadSend == null) {
                    gotoHomeFragment();
                    UpdateUiViews();
                    MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(R.string.SendItem)
                            , G.context.getResources().getText(R.string.PleaseWait)), 500);
                    pb05.setProgress(0);
                    pb05.setVisibility(View.VISIBLE);
                    threadSend = new Thread(new TaskSend());
                    threadSend.start();
                } else {
                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.PleaseWaitForOtherItem), 500);
                }
            }
        });

        txtMenuHome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.RegisterItem=false;

                gotoHomeFragment();
                if (G.mDrawerLayout.isDrawerOpen(G.llMenu)) {
                    isOpenDrawer = !isOpenDrawer;
                    G.mDrawerLayout.closeDrawer(G.llMenu);
                }
            }
        });
        txtMenuShowNfcContent.setOnClickListener(v -> {
            Intent intent = new Intent(G.context, ShowContentNfcTagActivity.class);
            startActivity(intent);
        });
        txtMenuItem1.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                //----for test header----
//				ImageView imgHeaderMenu = G.llMenu.findViewById(R.id.imgHeaderMenu);
//				if(counter >5){
//					counter=0;
//				}
//				imgHeaderMenu.setImageDrawable(getResources().getDrawable(bgList[counter]));
//				counter++;
                G.PostId=null;
                if (G.Setting == null) {
                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.GetSetting), 500);
                } else if (threadGetSetting == null && threadGetUsers == null && threadGetItems == null && threadSend == null && threadGetMaxItemVal == null) {
                    G.selectedMenuItemMode = G.MenuItemMode.Sabt;
                    G.RegisterItem=true;
                    if (G.currentUser.NeedTag == 0 && G.Setting.ModTag == 1 && G.currentUser.IsManager != 1) {
                        ShowNfcDialog();
                    } else {
                        G.fragmentStack.clear();
                        G.navigationStackTitle.clear();
                        Fragment fragmentHome = new FragmentHome();
                        G.fragmentStack.push(fragmentHome);
                        G.navigationStackTitle.push(G.context.getResources().getString(string.empty));

                        TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
                        txtTitle.setText(G.context.getResources().getString(R.string.menu_item_1)); //G.context.getResources().getString(R.string.menu_item_1)
                        Fragment fragment = new FragmentTajhizat1();
                        G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                        G.selectedMenuItemType = G.MenuItemTypes.VAHED;
                        G.selectedMenuItemMode = G.MenuItemMode.Sabt;
                        if (G.mDrawerLayout.isDrawerOpen(G.llMenu))
                            G.mDrawerLayout.closeDrawer(G.llMenu);
                    }
                } else {
                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.PleaseWaitForOtherItem), 500);
                }
            }
        });

        txtMenuItem2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                G.RegisterItem=false;
                if (threadGetSetting == null && threadGetUsers == null && threadGetItems == null && threadSend == null && threadGetMaxItemVal == null) {
                    G.fragmentStack.clear();
                    G.navigationStackTitle.clear();
                    Fragment fragmentHome = new FragmentHome();
                    G.fragmentStack.push(fragmentHome);
                    G.navigationStackTitle.push(G.context.getResources().getString(string.empty));

                    TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
                    //txtTitle.setText("واحد ها"); //G.context.getResources().getString(R.string.menu_item_1)
                    Fragment fragment = new FragmentTajhizat1();
                    G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                    G.selectedMenuItemType = G.MenuItemTypes.VAHED;
                    G.selectedMenuItemMode = G.MenuItemMode.Gozareshat;
                    if (G.mDrawerLayout.isDrawerOpen(G.llMenu))
                        G.mDrawerLayout.closeDrawer(G.llMenu);
                } else {
                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.PleaseWaitForOtherItem), 500);
                }
            }
        });

        txtMenuAboutUs.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Fragment currentFragment = G.fragmentManager.findFragmentById(R.id.frame_container);
                G.RegisterItem=false;
                G.fragmentStack.clear();
                G.fragmentStack.push(new FragmentHome());
                //G.navigationStack.clear();
                G.navigationStackTitle.push("");
                TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
                txtTitle.setText(G.context.getResources().getString(string.menu_about_us));

                Fragment fragment = new FragmentAbout();
                G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                if (G.mDrawerLayout.isDrawerOpen(G.llMenu))
                    isOpenDrawer=!isOpenDrawer;
                    G.mDrawerLayout.closeDrawer(G.llMenu);
            }
        });
        // set ToggleMenuButton open close drawer
        //G.actionBar = getActionBar();
        ImageView btnShowMenu = (ImageView) G.actionBar.getCustomView().findViewById(R.id.btnShowMenu);
        btnShowMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //MyToast.Show(getApplicationContext(), "You clicked on hambergery button :D", Toast.LENGTH_SHORT);
                G.RegisterItem=false;
                txtMenuItem1.requestFocus();
                G.mDrawerLayout.requestFocus();
                isOpenDrawer=!isOpenDrawer;
                // open and close drawer
                if (G.mDrawerLayout.isDrawerOpen(G.llMenu)) {
                    G.mDrawerLayout.closeDrawer(G.llMenu);

                }
                else
                    G.mDrawerLayout.openDrawer(G.llMenu);
            }
        });


        // set exit event
        txtMenuExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                G.RegisterItem=false;
                //ActivityDrawer.this.finish();
                Intent intent = new Intent(G.context, ActivityLogin.class);
                startActivity(intent);
                ActivityDrawer.this.finish();
            }
        });

        checkMenuPermissions();

        return super.onCreateOptionsMenu(menu);
    }

    protected void gotoHomeFragment() {
        Fragment currentFragment = G.fragmentManager.findFragmentById(R.id.frame_container);
        if (currentFragment != null) {
            G.fragmentStack.clear();
            G.navigationStackTitle.clear();
            TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
            txtTitle.setText(G.context.getResources().getString(string.empty));
        }

        Fragment fragment = new FragmentHome();
        G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @Override
    protected void readNdefMessage(Message message) {
//		NdefMessage lowLevel = message.getNdefMessage();
//		lowLevel.getRecords()[0].getId();
//		this.message = message;
//
//		Log.d(TAG, "Found " + message.size() + " NDEF records");
//
//		for(int k = 0; k < message.size(); k++) {
//			Record record = message.get(k);
//
//			Log.d(TAG, "Record " + k + " type " + record.getClass().getSimpleName());
//
//			// your own code here, for example:
//			if(record instanceof MimeRecord) {
//				//txtNfc1.setText("MimeRecord : "+record.toString());
//			} else if(record instanceof ExternalTypeRecord) {
//				//txtNfc1.setText("ExternalTypeRecord : "+record.toString());
//			} else if(record instanceof TextRecord) {
//				//txtNfc1.setText(((TextRecord) record).getText());
//				//txtNfc1.setText(this.NfcTagId);
//				//MyToast.Show(G.context,this.NfcTagId,Toast.LENGTH_SHORT);
//				searchAndOpenTajhiz(this.NfcTagId);
//				G.vibrator.vibrate(200);
//
//			} else { // more else
//				//txtNfc1.setText("else : "+record.toString());
//			}
//		}
        if (this.NfcTagId != null) {
            if (G.Setting.ModTag == 1)
                searchAndOpenTajhiz(this.NfcTagId);
            else if (G.Setting.ModTag == 2)
                checkAndOpenTajhiz(this.NfcTagId);
        } else {
            MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_NfcTag6), Toast.LENGTH_SHORT);
        }
    }

    private void searchAndOpenTajhiz(String tagId) {
        if (!nfcDialog.getDialog().isShowing())
            return;
        G.vibrator.vibrate(200);
        if (G.Setting.LayerTag == 1) {
            final dtoLogShits logshit = G.DB.GetLogshitByTagId(tagId);
            if (logshit != null) {
                SuccessNfcDialog();
//			setDetecting(false);
//			stopDetecting();
                G.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        G.fragmentStack.clear();
                        G.navigationStackTitle.clear();
                        Fragment currentFragment = new FragmentHome();
                        G.fragmentStack.push(currentFragment);
                        G.navigationStackTitle.push("");

                        G.selectedVahedId = logshit.PostID;
                        G.selectedLogshitId = logshit.LogshitInfID;
                        G.selectedMenuItemType = G.MenuItemTypes.TAJHIZ;
                        TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
                        txtTitle.setText(logshit.LogshitName);//("تجهیز های لاگشیت "+item.Name);
                        G.selectedLogshit = logshit.LogshitName;
                        Bundle bundle = new Bundle();
                        bundle.putInt("ID", logshit.LogshitInfID);
                        Fragment fragment = new FragmentTajhizat1();
                        fragment.setArguments(bundle);
                        G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                        nfcDialog.dismiss();
                    }
                }, 1000);

            } else { // check for item level
                FailedNfcDialog();
            }
        }
        else if (G.Setting.LayerTag == 2) {
            final dtoItems item = G.DB.GetItemByTagId(tagId);
            if (item != null) {
                SuccessNfcDialog();
//				setDetecting(false);
//				stopDetecting();

                G.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        G.fragmentStack.clear();
                        G.navigationStackTitle.clear();
                        Fragment currentFragment = new FragmentHome();
                        G.fragmentStack.push(currentFragment);
                        G.navigationStackTitle.push("");

                        G.selectedMenuItemType = G.MenuItemTypes.ITEM;
                        TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
                        txtTitle.setText(item.ItemName);//("تجهیز های لاگشیت "+item.Name);
                        G.selectedItem = item.ItemName;
                        Bundle bundle = new Bundle();
                        bundle.putInt("EquipID", item.EquipInfID);
                        bundle.putInt("SubEquipID", item.SubEquipID);
                        bundle.putInt("ItemInfId", item.ItemInfID);
                        Fragment fragment = new TabFragmentItem();
                        fragment.setArguments(bundle);
                        G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                        nfcDialog.dismiss();
                    }
                }, 1000);

            } else {
                FailedNfcDialog();
            }
        } else {
            MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_NfcTag7), Toast.LENGTH_SHORT);
        }


    }

    private void checkAndOpenTajhiz(String tagId) {
        if (!nfcDialog.getDialog().isShowing())
            return;
        G.vibrator.vibrate(200);
        if (G.selectedMenuItemType == G.MenuItemTypes.LOGSHIT) {
            if (nfcLogshit.TagId.trim().toUpperCase().compareTo(tagId.toUpperCase()) == 0) {
                SuccessNfcDialog();
                G.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AdapterTajhizat1.GoToNextTajhizPage(nfcLogshit);
                        nfcDialog.dismiss();
                    }
                }, 1000);
            } else {
                FailedNfcDialog();
            }
        } else if (G.selectedMenuItemType == G.MenuItemTypes.ITEM) {
            if (nfcItem.TagID.trim().toUpperCase().compareTo(tagId.toUpperCase()) == 0) {
                SuccessNfcDialog();
                G.handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
//						if(nfcItemDirection== AdapterItem.ItemDirection.None) {
//							AdapterItem.ViewHolder.showDialog2(nfcItem, nfcItemPos);
//							nfcDialog.dismiss();
//						}else{
//							if(G.selectedMenuItemType == G.MenuItemTypes.ITEM && G.selectedMenuItemMode==G.MenuItemMode.Sabt){
//								if(nfcItemDirection == AdapterItem.ItemDirection.Left)
//									AdapterItem.ViewHolder.showDialog(TabFragmentItem.arrListItemFiltered.get(nfcItemPos-1), nfcItemPos-1);
//								else if(nfcItemDirection == AdapterItem.ItemDirection.Right)
//									AdapterItem.ViewHolder.showDialog(TabFragmentItem.arrListItemFiltered.get(nfcItemPos+1), nfcItemPos+1);
//							}
//							nfcDialog.dismiss();
//						}
                        adapterItem.showDialog2(nfcItem, nfcItemPos);
                        nfcDialog.dismiss();
                    }
                }, 1000);
            } else {
                FailedNfcDialog();
            }
        }


    }


    @Override
    protected void readEmptyNdefMessage() {

    }

    @Override
    protected void readNonNdefMessage() {
        if (this.NfcTagId != null) {
            if (G.Setting.ModTag == 1)
                searchAndOpenTajhiz(this.NfcTagId);
            else if (G.Setting.ModTag == 2)
                checkAndOpenTajhiz(this.NfcTagId);
        } else {
            MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_NfcTag6), Toast.LENGTH_SHORT);
        }

    }

    class TaskGetSetting implements Runnable {
        @Override
        public void run() {
            try {
                String strJson = null;
                if (G.connectionWay == 1 || G.connectionWay == 3) {
                    //--------USB: Step 1 : get response from calling webservice method-----------
                    strJson = UsbController.CallMethod(G.CMD_GetSetting, G.serialNumberOfDevice);
                } else {
                    //-------WIFI: Step 1 : get response from calling webservice method-----------
                    strJson = getJsonStringFromUrl("/GetSetting/" + G.serialNumberOfDevice); //+Build.SERIAL
                }

                Log.i("vvv", "strjson : " + strJson);
                if (strJson != null) {
                    if (strJson.length() > 3) { //maybe its [] and its empty
//                    //-----------------Step 2 : parse data into specific object----------------
                        Gson gson = new Gson();
                        objSetting = gson.fromJson(new JSONArray(strJson).get(0).toString(), dtoSetting.class);
                        G.DB.TruncateSetting();
                        pb01.setMax(1);
                        if (G.DB.InsertSetting(objSetting)) {
                            G.Setting = objSetting;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb01.setProgress(1);
                                pb01.setVisibility(View.GONE);
                                threadGetSetting = null;
                                MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.SettingUpdating), Toast.LENGTH_SHORT);
                                //-----------UpdateUiViews----------
                                UpdateUiViews();
                                //--------------PREF SAVE-----------
                                String currentDateTime = Tarikh.getCurrentShamsidatetime();
                                G.prefEditor = G.myPref.edit();
                                G.prefEditor.putString(G.PREF_KEY_LAST_UPDATE_SETTING, currentDateTime);
                                G.prefEditor.commit();
                                txtMenuLastUpdateDate01.setText(currentDateTime);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb01.setVisibility(View.GONE);
                                MyToast.Show(G.context, String.format("%s", getResources().getText(R.string.Setting_msg)), 700);
                            }
                        });
                    }
                }
                threadGetSetting = null;
            } catch (Exception e) {
                Log.i("tag", e.toString());
//	            threadGetSetting.interrupt();
                threadGetSetting = null;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pb01.setVisibility(View.GONE);
                        MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(R.string.ErrorInServer), G.context.getResources().getText(R.string.TryAgian)), 700);
                    }
                });
            }

        }
    }

    class TaskGetItems implements Runnable {
        @Override
        public void run() {

            try {
                String strJson = null;
                if (G.connectionWay == 1 || G.connectionWay == 3) {
                    //--------USB: Step 1 : get response from calling webservice method-----------
                    strJson = UsbController.CallMethod(G.CMD_GetItems, G.serialNumberOfDevice);
                } else {
                    //-------WIFI: Step 1 : get response from calling webservice method-----------
                    strJson = getJsonStringFromUrl("/GetItems/" + G.serialNumberOfDevice); //+Build.SERIAL
                }
                Log.i("tag", "strjson : " + strJson);
                if (strJson != null && strJson.trim().length() > 0) {
                    //-----------------Step 2 : parse data into specific object----------------
                    JSONObject jsonResponse = new JSONObject(strJson);
                    JSONObject jsonObject = jsonResponse.getJSONObject("GetItemsResult");
                    Gson gson = new Gson();
                    packItems = gson.fromJson(jsonObject.toString(), dtoPackItems.class);
                    //------------------Step 3 : insert data into DB----------------------
                    G.DB.TruncateItemValues();
                    G.DB.TruncatePackItems();

                    pb02.setMax(packItems.tbl_Posts.size() +
                            packItems.tbl_Posts.size() +
                            packItems.tbl_LogShits.size() +
                            packItems.tbl_Equipments.size() +
                            packItems.tbl_SubEquipments.size() +
                            packItems.tbl_Items.size() +
                            packItems.tbl_Logics.size() +
                            packItems.tbl_Remarks.size() +
                            packItems.tbl_ItemTypeValues.size() +
                            packItems.tbl_Measures.size() +
                            packItems.tbl_Formulas.size() +
                            packItems.tbl_ItemFormulas.size() +
                            packItems.tbl_RemarkGroup.size() +
                            packItems.tbl_RelateUserLogshitTbl.size() +
                            packItems.tbl_CorsRems.size() +
                            packItems.tbl_UsrPost.size()
                    );

                    int counterValue = 0;
                    for (dtoPosts post : packItems.tbl_Posts) {
                        G.DB.InsertPost(post);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoLogShits logshit : packItems.tbl_LogShits) {
                        G.DB.InsertLogshit(logshit);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoEquipments equipment : packItems.tbl_Equipments) {
                        G.DB.InsertEquipment(equipment);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoSubEquipments subEquipment : packItems.tbl_SubEquipments) {
                        G.DB.InsertSubEquipment(subEquipment);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoItems item : packItems.tbl_Items) {
                        G.DB.InsertItem(item);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoLogics logic : packItems.tbl_Logics) {
                        G.DB.InsertLogic(logic);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoRemarks remark : packItems.tbl_Remarks) {
                        G.DB.InsertRemark(remark);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoItemTypeValues itemTypeValues : packItems.tbl_ItemTypeValues) {
                        G.DB.InsertItemTypeValue(itemTypeValues);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoMeasures measure : packItems.tbl_Measures) {
                        G.DB.InsertMeasure(measure);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoFormulas formula : packItems.tbl_Formulas) {
                        G.DB.InsertFormula(formula);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoItemFormulas itemFormulas : packItems.tbl_ItemFormulas) {
                        G.DB.InsertItemFormula(itemFormulas);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoRemarkGroup remarkGroup : packItems.tbl_RemarkGroup) {
                        G.DB.InsertRemarkGroup(remarkGroup);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoRelateUserLogshitTbl userLogshit : packItems.tbl_RelateUserLogshitTbl) {
                        G.DB.InsertUserLogshit(userLogshit);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoCorsRems corsRems : packItems.tbl_CorsRems) {
                        G.DB.InsertCorsRems(corsRems);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb02.setProgress(counter);
                            }
                        });
                    }

                    for (dtoUsrPost usrPost : packItems.tbl_UsrPost) {
                        G.DB.InsertUsrPost(usrPost);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                pb02.setProgress(counter);
                            }
                        });
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb02.setVisibility(View.GONE);
                            threadGetItems = null;
                            MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.ItemUpdating), Toast.LENGTH_SHORT);
                            //-----------UpdateUiViews----------
                            UpdateUiViews();
                            gotoHomeFragment();
                            //--------------PREF SAVE-----------
                            String currentDateTime = Tarikh.getCurrentShamsidatetime();
                            G.prefEditor = G.myPref.edit();
                            G.prefEditor.putString(G.PREF_KEY_LAST_UPDATE_PACKITEMS, currentDateTime);
                            G.prefEditor.commit();
                            txtMenuLastUpdateDate02.setText(currentDateTime);
                        }
                    });
                }
                threadGetItems = null;
            } catch (Exception e) {
                Log.i("tag", e.toString());
//	            threadGetBaseInfo.interrupt();
                threadGetItems = null;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pb02.setVisibility(View.GONE);
                        MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(R.string.ErrorInServer), G.context.getResources().getText(R.string.TryAgian)), 700);
                    }
                });
            }

            //-----------------------------------
//            for (int i = 0; i <= 100; i++) {
//                final int value = i;
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        pb02.setProgress(value);
//                        if(value == 100){
//                        	pb02.setVisibility(View.GONE);
//                        	threadGetBaseInfo = null;
//                        }
//                    }
//                });
//            }
        }
    }

    class TaskGetUsers implements Runnable {
        public void run() {
            try {
                String strJson = null;
                if (G.connectionWay == 1 || G.connectionWay == 3) {
                    //--------USB: Step 1 : get response from calling webservice method-----------
                    strJson = UsbController.CallMethod(G.CMD_GetUsers, G.serialNumberOfDevice);
                } else {
                    //-------WIFI: Step 1 : get response from calling webservice method-----------
                    strJson = getJsonStringFromUrl("/GetUsers/" + G.serialNumberOfDevice); //+Build.SERIAL
                }
                Log.i("tag", "strjson : " + strJson);
                if (strJson != null && strJson.trim().length() > 3) {
                    //----------Step 2 : parse data into specific object-----------
                    JSONObject jsonResponse = new JSONObject(strJson);
                    JSONObject jsonObject = jsonResponse.getJSONObject("GetUsersResult");
                    Gson gson = new Gson();
                    packUser = gson.fromJson(jsonObject.toString(), dtoPackUser.class);
                    //-----------------------Step 3 : insert data into DB----------------------
                    G.DB.TruncatePackUser();
                    pb03.setMax(packUser.Users.size() + packUser.Shifts.size() + packUser.RelateUserToShift.size());
                    int counterValue = 0;
                    for (dtoUsers user : packUser.Users) {
                        G.DB.InsertUser(user);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb03.setProgress(counter);
                            }
                        });
                    }
                    for (dtoShifts shift : packUser.Shifts) {
                        G.DB.InsertShift(shift);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb03.setProgress(counter);
                            }
                        });
                    }
                    for (dtoUserShift userShift : packUser.RelateUserToShift) {
                        G.DB.InsertUserShift(userShift);
                        final int counter = ++counterValue;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb03.setProgress(counter);
                            }
                        });
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb03.setVisibility(View.GONE);
                            MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.UserUpdating), Toast.LENGTH_SHORT);
                            //-----------UpdateUiViews----------
                            UpdateUiViews();
                            //--------------PREF SAVE-----------
                            String currentDateTime = Tarikh.getCurrentShamsidatetime();
                            G.prefEditor = G.myPref.edit();
                            G.prefEditor.putString(G.PREF_KEY_LAST_UPDATE_PACKUSER, currentDateTime);
                            G.prefEditor.commit();
                            txtMenuLastUpdateDate03.setText(currentDateTime);
                        }
                    });
                    threadGetUsers = null;
                }
            } catch (Exception e) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //MyToast.Show(G.context,String.format("%s", "خطا در برقراری ارتباط"), Toast.LENGTH_SHORT);
                    }
                });
                Log.i("tag", e.toString());
                threadGetUsers = null;
            }
            System.gc();
        }
    }

    class TaskGetLastValues implements Runnable {
        @Override
        public void run() {
            try {
                String strJson = null;
                if (G.connectionWay == 1 || G.connectionWay == 3) {
                    //--------USB: Step 1 : get response from calling webservice method-----------
                    strJson = UsbController.CallMethod(G.CMD_GetLastValues, G.serialNumberOfDevice);
                } else {
                    //-------WIFI: Step 1 : get response from calling webservice method-----------
                    strJson = getJsonStringFromUrl("/GetLastValues/" + G.serialNumberOfDevice); //+Build.SERIAL
                }
                Log.i("tag", "strjson : " + strJson);
                if (strJson != null) {
                    if (strJson.length() > 3) { //maybe its [] and its empty
                        //-------Step 2 : parse data into specific object-----------
                        Gson gson = new Gson();
                        lstMaxItemVal = gson.fromJson(strJson, dtoMaxItemVal[].class);
                        //-------Step 3 : insert into DB-----------
                        G.DB.TruncateMaxItemVal();
                        pb04.setMax(lstMaxItemVal.length);
                        int counterValue = 0;
                        for (dtoMaxItemVal maxItemVal : lstMaxItemVal) {
                            if(maxItemVal.ItemValTyp.equals("3")){
                                maxItemVal.ItemVal="RES";
                            }
                            if(maxItemVal.ItemValTyp.equals("2")){
                                maxItemVal.ItemVal="OUT";
                            }

                            G.DB.InsertMaxItemVal(maxItemVal);
                            final int counter = ++counterValue;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pb04.setProgress(counter);
                                }
                            });
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb04.setVisibility(View.GONE);
                                MyToast.Show(G.context, (String) G.context.getResources().getText(string.LastDataUpdating), Toast.LENGTH_SHORT);
                                //-----------UpdateUiViews----------
                                UpdateUiViews();
                                //--------------PREF SAVE-----------
                                String currentDateTime = Tarikh.getCurrentShamsidatetime();
                                G.prefEditor = G.myPref.edit();
                                G.prefEditor.putString(G.PREF_KEY_LAST_UPDATE_MAXITEMVAL, currentDateTime);
                                G.prefEditor.commit();
                                txtMenuLastUpdateDate04.setText(currentDateTime);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                pb04.setVisibility(View.GONE);
                                MyToast.Show(G.context, String.format("%s", G.context.getResources().getText(R.string.NoLastData)), 700);
                            }
                        });
                    }
                }
                threadGetMaxItemVal = null;
            } catch (Exception e) {
                Log.i("tag", e.toString());
//	            threadGetBaseInfo.interrupt();
                threadGetMaxItemVal = null;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pb04.setVisibility(View.GONE);
                        MyToast.Show(G.context, String.format("%s\n%s", G.context.getResources().getText(string.ErrorInServer), G.context.getResources().getText(string.TryAgian)), 700);
                    }
                });
            }

            //-----------------------------------
//            for (int i = 0; i <= 100; i++) {
//                final int value = i;
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        pb02.setProgress(value);
//                        if(value == 100){
//                        	pb02.setVisibility(View.GONE);
//                        	threadGetBaseInfo = null;
//                        }
//                    }
//                });
//            }
        }
    }

    BufferedReader reader;

    private String getJsonStringFromUrl(String strUrl) throws Exception {
        String strJson = "";
        URL json = new URL(G.WEB_SERVICE + strUrl); //+Build.SERIAL
        URLConnection jc = json.openConnection();

        int resCode = -1;
        if (!(jc instanceof HttpURLConnection)) {
            throw new IOException("URL is not an Http URL");
        }
        HttpURLConnection httpConn = (HttpURLConnection) jc;
        httpConn.setAllowUserInteraction(false);
        httpConn.setInstanceFollowRedirects(true);
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        resCode = httpConn.getResponseCode();
        Log.i("tag", "resCode : " + resCode);

        if (resCode == HttpURLConnection.HTTP_OK) {
            //reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line = "";
            while ((line = new BufferedReader(new InputStreamReader(httpConn.getInputStream())).readLine()) != null) {
                strJson += line + "\n";
            }
        }


        //reader.close();
        return strJson;
    }

    class TaskSend implements Runnable {
        @Override
        public void run() {
            try {
                String DateEng = "";
                ArrayList<dtoItemValues> itemValues1 = G.DB.getItemValuesByUserId(G.currentUser.UsrID);
                ArrayList<dtoItemValuesForSend> itemValues = new ArrayList<>();
                for (dtoItemValues values : itemValues1) {
                    if (G.RTL) {
                        DateEng = Tarikh.setTextValueDate(values.PDate
                                .replace("/", " ")
                                .replace(":", " ")
                                .replace(" ", " ")
                                .split(" "), 0);
                    } else {
                        DateEng = values.PDate;

                    }
                    dtoItemValuesForSend valuesForSend = new dtoItemValuesForSend();
                    if(values.ItemInfID==933){
                        String s="1";
                        s=s+"2";
                    }
                    valuesForSend.Id = values.Id;
                    valuesForSend.IsSend = values.IsSend;
                    valuesForSend.ItemInfID = values.ItemInfID;
                    valuesForSend.ItemVal = values.ItemVal;
                    valuesForSend.ItemValTyp = values.ItemValTyp;
                    valuesForSend.RegisterDateTime = DateEng;
                    valuesForSend.ShiftID = values.ShiftID;
                    valuesForSend.UsrID = values.UsrID;
                    itemValues.add(valuesForSend);
                }
                if (itemValues.size() == 0) {
                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.NoItemForSend), Toast.LENGTH_LONG);
                            pb05.setVisibility(View.GONE);
                        }
                    });
                }
                else {

                    G.handler.post(new Runnable() {
                        @Override
                        public void run() {

                            MyToast.Show(G.context, String.format(String.valueOf(getText(R.string.msg_CountRecord)), itemValues.size()), Toast.LENGTH_LONG);


                        }
                    });

                    Gson gson = new Gson();
                    String strJson = gson.toJson(itemValues);
                    String response;
                    if (G.connectionWay == 1 || G.connectionWay == 3) {
                        //--------USB: Step 1 : get response from calling webservice method-----------
                        String strData = String.format("%s|%s|%s", G.serialNumberOfDevice, G.currentUser.UsrID, strJson);
                        response = UsbController.CallMethod(G.CMD_SaveItemValues, strData);
                    } else {
                        //-------WIFI: Step 1 : send post to webservice method directly from android-----------
                        response = sendByHttpPost(strJson, G.currentUser.UsrID.toString());
                    }

                    if (response == "FAIL") {
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.SendFail), Toast.LENGTH_LONG);
                                pb05.setVisibility(View.GONE);
                            }
                        });
                    } else if (response == "null") {
                        G.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.SendWithError), Toast.LENGTH_LONG);
                                pb05.setVisibility(View.GONE);
                            }
                        });
                    } else {

                        Integer[] lstItemValuesIds = gson.fromJson(response, Integer[].class);
                        pb05.setMax(itemValues.size());
                        int counterValue = 0;
                        if (lstItemValuesIds.length == 0) {
                            for (dtoItemValues itemVal : itemValues1) {
                                itemVal.IsSend = 0;
                                G.DB.UpdateItemValueById(itemVal.Id, itemVal);
                                final int counter = ++counterValue;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb05.setProgress(counter);
                                    }
                                });
                            }
                            G.handler.post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void run() {
                                    pb05.setVisibility(View.GONE);
                                    if (G.RTL) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDrawer.this);
                                        alertDialogBuilder.setMessage((String) G.context.getResources().getText(R.string.Msg_ConfirmSendData))
                                                .setCancelable(false)
                                                .setPositiveButton((String) G.context.getResources().getText(R.string.yes),
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                gotoHomeFragment();
                                                                UpdateUiViews();
                                                                G.DB.TruncateItemValues();
                                                                MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.DeleteSucessfully), Toast.LENGTH_LONG);
                                                                gotoHomeFragment();
                                                                UpdateUiViews();
                                                            }
                                                        });
                                        alertDialogBuilder.setNegativeButton((String) G.context.getResources().getText(R.string.no),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        dialog.cancel();
                                                        dialog.dismiss();
                                                    }
                                                });
                                        AlertDialog alert = alertDialogBuilder.create();
                                        alert.show();
                                    } else {
                                        if (G.currentUser.IsManager == 1) {
//                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityDrawer.this);
//                                            alertDialogBuilder.setMessage((String) G.context.getResources().getText(R.string.Msg_ConfirmSendData))
//                                                    .setCancelable(false)
//                                                    .setPositiveButton((String) G.context.getResources().getText(R.string.yes),
//                                                            new DialogInterface.OnClickListener() {
//                                                                public void onClick(DialogInterface dialog, int id) {
//                                                                    gotoHomeFragment();
//                                                                    UpdateUiViews();
//                                                                    G.DB.TruncateItemValues();
//                                                                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.DeleteSucessfully), Toast.LENGTH_LONG);
//                                                                    gotoHomeFragment();
//                                                                    UpdateUiViews();
//                                                                }
//                                                            });
//                                            alertDialogBuilder.setNegativeButton((String) G.context.getResources().getText(R.string.no),
//                                                    new DialogInterface.OnClickListener() {
//                                                        public void onClick(DialogInterface dialog, int id) {
//
//                                                            dialog.cancel();
//                                                            dialog.dismiss();
//                                                        }
//                                                    });
//                                            AlertDialog alert = alertDialogBuilder.create();
//                                            alert.show();

                                            MyDialog dialog=new MyDialog(ActivityDrawer.this);
                                            dialog.addBodyText((String) G.context.getResources().getText(R.string.Msg_ConfirmSendData),10);
                                            dialog .addButtonL((String) G.context.getResources().getText(R.string.yes), new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            gotoHomeFragment();
                                                                    UpdateUiViews();
                                                                    G.DB.TruncateItemValues();
                                                                   MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.DeleteSucessfully), Toast.LENGTH_LONG);
                                                                   gotoHomeFragment();
                                                                    UpdateUiViews();
                                                                    dialog.dismiss();
                                                        }
                                                    }).addButtonR((String) G.context.getResources().getText(R.string.no), new OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                        } else {
                                            MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_SendData), Toast.LENGTH_LONG);
                                        }
                                    }


                                }
                            });

                        } else {
                            for (dtoItemValues itemVal : itemValues1) {
                                Boolean isNotSavedInServer = false;
                                for (Integer id : lstItemValuesIds) {
                                    if (itemVal.Id == id)
                                        isNotSavedInServer = true;
                                }
                                if (isNotSavedInServer)
                                    continue;
                                itemVal.IsSend = 0;
                                G.DB.UpdateItemValueById(itemVal.Id, itemVal);
                                final int counter = ++counterValue;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb05.setProgress(counter);
                                    }
                                });
                            }
                            G.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.ErrorInSend), Toast.LENGTH_LONG);
                                    pb05.setVisibility(View.GONE);
                                }
                            });
                        }

                    }
                }
            } catch (Exception e) {
                G.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.ConnectionWithError), Toast.LENGTH_LONG);
                        pb05.setVisibility(View.GONE);
                    }
                });
            }
            threadSend = null;
        }
    }

    private String sendByHttpPost(String jsonStr) throws IOException {
        return sendByHttpPost(jsonStr, "");
    }

    private String sendByHttpPost(String jsonStr, String strGet) throws IOException {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        byte[] buffer;
        String stringForSend = jsonStr;//"\\\""+ jsonStr.replace("\"", "\\\"")+"\\\"";

        int bytesAvailable = stringForSend.getBytes().length;
        String upLoadServerUri = String.format("%s/SaveItemValues/%s/%s", G.WEB_SERVICE, G.serialNumberOfDevice, strGet); //G.currentUser.UsrID

        URL url = new URL(upLoadServerUri);

        conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true); // Allow Inputs
        conn.setDoOutput(true); // Allow Outputs
        conn.setUseCaches(false); // Don't use a Cached Copy
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Length", String.valueOf(stringForSend.getBytes().length));
        conn.setRequestProperty("Content-Type", "application/json");
        //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.3; Win64; x64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729)");
        Log.i("wifisend", "1");

        dos = new DataOutputStream(conn.getOutputStream());


//        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bytesAvailable];
        buffer = stringForSend.getBytes();
        dos.write(buffer, 0, bytesAvailable);

//        int totalBytesRead = 0;
//        while (bytesAvailable>0) {
//            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//            dos.write(buffer, 0, bytesRead);
//            totalBytesRead += bytesRead;
//            bytesAvailable = fileInputStream.available();
//            bufferSize = Math.min(bytesAvailable , maxBufferSize);
//        }

        int serverResponseCode = conn.getResponseCode();
        String serverResponseMessage = ""; //conn.getResponseMessage();
        dos.flush();
        //close the streams //
        //fileInputStream.close();
        dos.flush();
        dos.close();

        if (serverResponseCode != 200) {
            serverResponseMessage = "FAIL";
        } else {
            dis = new DataInputStream(conn.getInputStream());
            //serverResponseMessage = dis.read
            String inputLine;
            while ((inputLine = dis.readLine()) != null) {
                serverResponseMessage += inputLine;
            }
            Log.i("wifisend", serverResponseMessage + " " + serverResponseCode);
        }
        return serverResponseMessage;
    }

    private void UpdateUiViews() {

//		int countShabake20KiloVoltHavayee = G.DB.GetViewCount(G.NoeBarnameRizi.SHABAKE_20_KILO_VOLT_HAVAYEE,G.currentUser.UsrID);
//		int countShabake20KiloVoltZamini = G.DB.GetViewCount(G.NoeBarnameRizi.SHABAKE_20_KILO_VOLT_ZAMINI,G.currentUser.UsrID);
//		
//		//--TODO
//		
//		txtCircle1.setText(Integer.toString(countShabake20KiloVoltHavayee));
//		txtCircle2.setText(Integer.toString(countShabake20KiloVoltZamini));
//
//		if(countShabake20KiloVoltHavayee>0){
//			rlCircle1.setVisibility(View.VISIBLE);
//		}else{
//			rlCircle1.setVisibility(View.INVISIBLE);
//		}
//		
//		if(countShabake20KiloVoltZamini>0){
//			rlCircle2.setVisibility(View.VISIBLE);
//		}else{
//			rlCircle2.setVisibility(View.INVISIBLE);
//		}
//		

//		if(G.currentFragment != null){
//			if(G.currentFragment instanceof FragmentHome){
//				
//				Fragment currentFragment = G.fragmentManager.findFragmentById(R.id.frame_container);
//				   if (currentFragment != null) {
//					   G.fragmentStack.clear();
//					   G.fragmentStack.push(currentFragment);
//					   G.navigationStack.clear();
//					   //G.navigationStack.push(((TextView)G.actionBar.getCustomView().findViewById(R.id.txtTitle)).getText().toString());
//					   TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
//					   txtTitle.setText(G.context.getResources().getString(R.string.empty));
//				   }
//					Fragment fragment =  new FragmentHome();
//					G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
//					if(G.mDrawerLayout.isDrawerOpen(G.llMenu))
//						G.mDrawerLayout.closeDrawer(G.llMenu);
//				
//			}
//		}
//		
//
//    	//--------------PREF READ-----------
        String strLastUpadteSettingDate = G.myPref.getString(G.PREF_KEY_LAST_UPDATE_SETTING, "");
        String strLastUpadtePackItemsDate = G.myPref.getString(G.PREF_KEY_LAST_UPDATE_PACKITEMS, "");
        String strLastUpadtePackUserDate = G.myPref.getString(G.PREF_KEY_LAST_UPDATE_PACKUSER, "");
        String strLastUpadteMaxItemValDate = G.myPref.getString(G.PREF_KEY_LAST_UPDATE_MAXITEMVAL, "");
//		String strLastUpadteDate = G.myPref.getString(G.PREF_KEY_LAST_UPDATE_SETTING, "");
//		String strLastGetBaseInfoDate = G.myPref.getString(G.PREF_KEY_LAST_GET_BASE_INFO, "");
//		String strLastSendViewsDate = G.myPref.getString(G.PREF_KEY_LAST_SEND_EYBS, "");
//		
        txtMenuLastUpdateDate01.setText(strLastUpadteSettingDate);
        txtMenuLastUpdateDate02.setText(strLastUpadtePackItemsDate);
        txtMenuLastUpdateDate03.setText(strLastUpadtePackUserDate);
        txtMenuLastUpdateDate04.setText(strLastUpadteMaxItemValDate);
//		txtMenuLastSendDate.setText(strLastSendViewsDate);

    }

    @Override
    protected void onResume() {

        G.actionBar = getSupportActionBar();
        G.currentActivity = ActivityDrawer.this;

        super.onResume();
    }

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if (enabled) {
            //toast("nfcAvailableEnabled");
        } else {
            //toast("nfcAvailableDisabled");
        }
    }

    @Override
    protected void onNfcFeatureNotFound() {

    }

    @Override
    protected void onTagLost() {

    }

    Boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        Log.i("tag", "onBackPressed Called");
        isOpenDrawer=false;
        txtMenuItem1.requestFocus();
        G.mDrawerLayout.requestFocus();
        if (G.fragmentManager.getFragments().get(0) instanceof TabFragmentItem) {
            if (TabFragmentItem.getCountOfNotEnteredItem() < TabFragmentItem.arrListItem.size()) {
                if(G.isSave==false) {
                    TabFragmentItem.imgSaveItem.performClick();
                }
                //return;
            }
        }
        if (G.fragmentStack != null && G.fragmentStack.size() > 0) {
            Fragment f = G.fragmentStack.pop();
            if (f == null) return;
            if (G.navigationStackId.size() > 0) {
                if (f instanceof FragmentItemReport) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("EquipID", ((FragmentItemReport) f).EquipId);
                    bundle.putInt("SubEquipID", ((FragmentItemReport) f).SubEquipId);
                    f.setArguments(bundle);
                    G.selectedId = ((FragmentItemReport) f).SubEquipId;
                } else {
                    Integer Id = G.navigationStackId.pop();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", Id);
                    f.setArguments(bundle);
                    G.selectedId = Id;
                }
            } else {
                G.selectedId = null;
            }
            if (G.selectedMenuItemType == G.MenuItemTypes.ITEM) {
                G.selectedMenuItemType = G.MenuItemTypes.ZIRTAJHIZ;
            } else if (G.selectedMenuItemType == G.MenuItemTypes.ITEMREPORT) {
                G.selectedMenuItemType = G.MenuItemTypes.ZIRTAJHIZ;
            } else if (G.selectedMenuItemType == G.MenuItemTypes.ITEMHISTORY) {
                G.selectedMenuItemType = G.MenuItemTypes.ITEMREPORT;
            } else if (G.selectedMenuItemType == G.MenuItemTypes.ZIRTAJHIZ) {
                G.selectedMenuItemType = G.MenuItemTypes.TAJHIZ;
            } else if (G.selectedMenuItemType == G.MenuItemTypes.TAJHIZ) {
                G.selectedMenuItemType = G.MenuItemTypes.LOGSHIT;
            } else if (G.selectedMenuItemType == G.MenuItemTypes.LOGSHIT) {
                G.selectedMenuItemType = G.MenuItemTypes.VAHED;
            }
            G.fragmentManager.beginTransaction().replace(R.id.frame_container, f).commit();
            if (G.mDrawerLayout.isDrawerOpen(G.llMenu))
                G.mDrawerLayout.closeDrawer(G.llMenu);
            //Navigation Text set on back pressed
            if (G.navigationStackTitle.size() > 0) {
                String str = G.navigationStackTitle.pop();
                TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
                txtTitle.setText(str);
            }

        } else {

            if (doubleBackToExitPressedOnce) {
//			   	ActivityDrawer.this.finish();
//			   	System.exit(0);
                Intent intent = new Intent(G.context, ActivityLogin.class);
                startActivity(intent);
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            MyToast.Show(this, (String) G.context.getResources().getText(R.string.Msg_Exit), Toast.LENGTH_SHORT);
            G.handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1500);

        }
    }

    int index=0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_5){
            if(TabFragmentItem.listItem!=null) {
                dtoItems item = (dtoItems) TabFragmentItem.listItem.getSelectedItem();
                if(item!=null) {
                    adapterItem.showJozeeatDialog(item);
                }
            }
        }
//        if (isOpenDrawer) {
//            G.mDrawerLayout.setFocusable(true);
//                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//
//                            txtMenuItem1.setFocusable(true);
//
//
//                            View tvFucoce = getCurrentFocus();
//                            if (tvFucoce != null) {
//
//
//                        if (!(tvFucoce instanceof ScrollView)) {
//                            tvFucoce.setBackgroundColor(Color.parseColor("#91B863"));
//                        }
//
//                        if (tvFucoce.equals(vLastFocuse) == false) {
//                            if (vLastFocuse != null) {
//                                vLastFocuse.setBackgroundColor(Color.WHITE);
//                                if (vLastFocuse instanceof LinearLayout) {
//                                    vLastFocuse.setBackgroundColor(Color.parseColor("#2f2f2f"));
//                                }
//                            }
//                            vLastFocuse = tvFucoce;
//                        }
//                    }
//
//
//                }
//                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//
//                    if (G.mDrawerLayout.isDrawerOpen(G.llMenu)) {
//                        txtMenuItem1.requestFocus();
//                    }
//                    View tvFucoce = getCurrentFocus();
//                    if (tvFucoce != null) {
//
//                        if (!(tvFucoce instanceof ScrollView)) {
//                            tvFucoce.setBackgroundColor(Color.parseColor("#91B863"));
//                        }
//
//                        if (tvFucoce.equals(vLastFocuse) == false) {
//                            if (vLastFocuse != null) {
//                                vLastFocuse.setBackgroundColor(Color.WHITE);
//                                if (vLastFocuse instanceof LinearLayout) {
//                                    vLastFocuse.setBackgroundColor(Color.parseColor("#2f2f2f"));
//                                }
//                            }
//                            vLastFocuse = tvFucoce;
//                        }
//                    }
//
//
//                }
//            }
        else {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    View tvFucoce = getCurrentFocus();
                    if (tvFucoce != null) {
                        if (tvFucoce instanceof LinearLayout) {
                            tvFucoce.setBackgroundColor(Color.parseColor("#91B863"));
                            if (tvFucoce.equals(vLastFocuse) == false) {
                                if (vLastFocuse != null) {
                                    vLastFocuse.setBackgroundColor(Color.parseColor("#2f2f2f"));
                                }
                                vLastFocuse = tvFucoce;
                            }
                        }
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    View tvFucoce = getCurrentFocus();
                    if (tvFucoce != null) {
                        if (tvFucoce instanceof LinearLayout) {
                            tvFucoce.setBackgroundColor(Color.parseColor("#91B863"));
                            if (tvFucoce.equals(vLastFocuse) == false) {
                                if (vLastFocuse != null) {
                                    vLastFocuse.setBackgroundColor(Color.parseColor("#2f2f2f"));
                                }
                                vLastFocuse = tvFucoce;
                            }
                        }
                    }


                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    View tvFucoce = getCurrentFocus();
                    if (tvFucoce != null) {
                        if (tvFucoce instanceof LinearLayout) {
                            tvFucoce.setBackgroundColor(Color.parseColor("#91B863"));
                            if (tvFucoce.equals(vLastFocuse) == false) {
                                if (vLastFocuse != null) {
                                    vLastFocuse.setBackgroundColor(Color.parseColor("#2f2f2f"));
                                }
                                vLastFocuse = tvFucoce;
                            }
                        }
                    }


                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    View tvFucoce = getCurrentFocus();
                    if (tvFucoce != null) {
                        if (tvFucoce instanceof LinearLayout) {
                            tvFucoce.setBackgroundColor(Color.parseColor("#91B863"));
                            if (tvFucoce.equals(vLastFocuse) == false) {
                                if (vLastFocuse != null) {
                                    vLastFocuse.setBackgroundColor(Color.parseColor("#2f2f2f"));
                                }
                                vLastFocuse = tvFucoce;
                            }
                        }
                    }
                }
            }


        return super.onKeyUp(keyCode, event);
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {



        if (keyCode == KeyEvent.KEYCODE_MENU) {
            txtMenuItem1.requestFocus();
            G.mDrawerLayout.requestFocus();
            isOpenDrawer=!isOpenDrawer;
            if (G.mDrawerLayout.isDrawerOpen(G.llMenu))

                G.mDrawerLayout.closeDrawer(G.llMenu);
            else {
                txtMenuItem1.requestFocus();
                G.mDrawerLayout.openDrawer(G.llMenu);
//                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//
//                    txtMenuItem1.setFocusable(true);
//
//
//                    View tvFucoce = getCurrentFocus();
//                    if (tvFucoce != null) {
//
//
//                        if (!(tvFucoce instanceof ScrollView)) {
//                            tvFucoce.setBackgroundColor(Color.parseColor("#91B863"));
//                        }
//
//                        if (tvFucoce.equals(vLastFocuse) == false) {
//                            if (vLastFocuse != null) {
//                                vLastFocuse.setBackgroundColor(Color.WHITE);
//                                if (vLastFocuse instanceof LinearLayout) {
//                                    vLastFocuse.setBackgroundColor(Color.parseColor("#2f2f2f"));
//                                }
//                            }
//                            vLastFocuse = tvFucoce;
//                        }
//                    }
//
//
//                }
//                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//
//                    if (G.mDrawerLayout.isDrawerOpen(G.llMenu)) {
//                        txtMenuItem1.setFocusable(true);
//                    }
//                    View tvFucoce = getCurrentFocus();
//                    if (tvFucoce != null) {
//
//                        if (!(tvFucoce instanceof ScrollView)) {
//                            tvFucoce.setBackgroundColor(Color.parseColor("#91B863"));
//                        }
//
//                        if (tvFucoce.equals(vLastFocuse) == false) {
//                            if (vLastFocuse != null) {
//                                vLastFocuse.setBackgroundColor(Color.WHITE);
//                                if (vLastFocuse instanceof LinearLayout) {
//                                    vLastFocuse.setBackgroundColor(Color.parseColor("#2f2f2f"));
//                                }
//                            }
//                            vLastFocuse = tvFucoce;
//                        }
//                    }
//
//
//                }

            }
            //Toast.makeText(this, "You pressed the menu button!", Toast.LENGTH_LONG).show();

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        G.viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }
    //-----------------

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void setCurrentPhotoPath(String mCurrentPhotoPath) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
    }

    public Uri getCapturedImageURI() {
        return mCapturedImageURI;
    }

    public void setCapturedImageURI(Uri mCapturedImageURI) {
        this.mCapturedImageURI = mCapturedImageURI;
    }

    //-----------------
    private void checkMenuPermissions() {
        if (G.currentUser != null && G.currentUser.IsManager != 1) {

            menuLayout1.setVisibility(View.GONE);
            pb01.setVisibility(View.GONE);
            menuUpdateLayout1.setVisibility(View.GONE);

            menuLayout2.setVisibility(View.GONE);
            pb02.setVisibility(View.GONE);
            menuUpdateLayout2.setVisibility(View.GONE);

            menuLayout3.setVisibility(View.GONE);
            pb03.setVisibility(View.GONE);
            menuUpdateLayout3.setVisibility(View.GONE);

            menuLayout4.setVisibility(View.GONE);
            pb04.setVisibility(View.GONE);
            menuUpdateLayout4.setVisibility(View.GONE);

        }
    }
}
