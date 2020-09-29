package ir.saa.android.datalogger;

import chart.MyXAxisValueFormatter;
import database.structs.dtoEquipments;
import database.structs.dtoItemValues;
import database.structs.dtoItems;
import database.structs.dtoLogShits;
import database.structs.dtoPosts;
import database.structs.dtoSubEquipments;
import database.structs.dtoTajhiz;
import enums.ValidForSubmitType;
import mycomponents.MyLabel;
import mycomponents.MyToast;

import android.Manifest;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.WINDOW_SERVICE;

public class FragmentHome extends FragmentEnhanced {
	TextView txtJobs;
	LinearLayout lfHome;
	RelativeLayout rlCircleHome;
	int LayoutMain;
	int textLeftSize;
	int textRightSize;
	int lineChartTextSize;
	int mLableWidth;
	int pieChartCenterText;
	int EnteryTextsize;
	LinearLayout.LayoutParams paramsTextView;
	LinearLayout.LayoutParams paramsLabl;
	ImageView imgDashboard5_1;
	LinearLayout llDashboardRow5_1;
	MyLabel lblSubmitedItemCount;
	MyLabel lblVahedCount;
	LinearLayout llDashboardRow5_2;
	MyLabel lblLogshitCount;
	MyLabel lblUnsubmitedItemCount;
	MyLabel lblTajhizCount;
	MyLabel lblZirTajhizCount;
	MyLabel lblItemCount;

	ImageView imgDashboard5_2;

	public  void adjustFontScale(Configuration configuration, Float fontSize) {

		configuration.fontScale = (float) fontSize;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		metrics.scaledDensity = configuration.fontScale * metrics.density;
		getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);

	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		LayoutMain=R.layout.fragment_home;


		View rootView = inflater.inflate(LayoutMain, container, false);
		paramsTextView= new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
		paramsLabl= new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);

		lfHome=rootView.findViewById(R.id.lfHome);
		lfHome.requestFocus();
		if(G.RTL) {//TODO: LTR
			lfHome.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
			paramsLabl.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
			paramsTextView.gravity= Gravity.RIGHT;
			tfByekan = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
			paramsTextView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
			textLeftSize=40;
			textRightSize=18;
			lineChartTextSize=14;
			mLableWidth=40;
			pieChartCenterText=35;
			EnteryTextsize=14;
		}
		else {
			lfHome.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
			paramsLabl.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
			paramsLabl.gravity=Gravity.LEFT;
			paramsTextView.gravity=Gravity.LEFT;
			tfByekan = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
			paramsTextView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
			adjustFontScale(getResources().getConfiguration(),(float)1.5);
			textLeftSize=15;
			textRightSize=14;
			lineChartTextSize=8;
			mLableWidth=35;
			pieChartCenterText=16;
			EnteryTextsize=15;
		}
		G.context.setTheme(R.style.AppTheme50dp);
		return rootView;
	}
	
	@Override
	public void onResume() {
		if(ActivityDrawer.threadGetSetting ==null &&ActivityDrawer.threadGetItems ==null &&ActivityDrawer.threadSend ==null){
			UpdateUiViews();
		}
		
		super.onResume();
	}

	private void UpdateUiViews(){
		
		//G.DB.SelectViews();
//		
//		int countShabake20KiloVoltHavayee = G.DB.GetViewCount(G.MenuItemTypes.VAHED,G.USER_ID);
//		int countShabake20KiloVoltZamini = G.DB.GetViewCount(G.MenuItemTypes.LOGSHIT,G.USER_ID);
//		int countTajhizateShabake20KiloWatHavayee = G.DB.GetViewCount(G.MenuItemTypes.TAJHIZ,G.USER_ID);
//		int countShabakeFesharZaeefVaMaaberHavayee = G.DB.GetViewCount(G.MenuItemTypes.ZIRTAJHIZ,G.USER_ID);
//		int countShabakeFesharZaeefZamini = G.DB.GetViewCount(G.MenuItemTypes.ITEM,G.USER_ID);
//		int countPosthayeHavayee = G.DB.GetViewCount(G.MenuItemTypes.POSTHAYE_HAVAYEE,G.USER_ID);
//		int countPosthayeZamini = G.DB.GetViewCount(G.MenuItemTypes.POSTHAYE_ZAMINI,G.USER_ID);
//		int countBazdidMaaber = G.DB.GetViewCountRM(G.MenuItemTypes.BAZDID_ROSHANAYI_ROSHANAYI,G.USER_ID);
//		int countEtesalZamin = G.DB.GetViewCountEZ(G.MenuItemTypes.ANDAZEGIRI_MOGHAVEMAT_ETESAL_ZAMIN,G.USER_ID);
//		int countRoghanTrans = G.DB.GetViewCountRT(G.MenuItemTypes.BAZDID_ROGHAN_TRANS,G.USER_ID);
//		int countBargiri = G.DB.GetViewCountBARGIRI(G.MenuItemTypes.BARGIRI,G.USER_ID);
//		
//		int totalCount = 
//				countShabake20KiloVoltHavayee+
//				countShabake20KiloVoltZamini+
//				countTajhizateShabake20KiloWatHavayee+
//				countShabakeFesharZaeefVaMaaberHavayee+
//				countShabakeFesharZaeefZamini+
//				countPosthayeHavayee+
//				countPosthayeZamini+
//				countBazdidMaaber+
//				countEtesalZamin+
//				countRoghanTrans+
//				countBargiri;
//		
//		txtJobs.setText(Integer.toString(totalCount));
//		if(totalCount>0){
//			rlCircleHome.setVisibility(View.VISIBLE);
//		}else{
//			rlCircleHome.setVisibility(View.INVISIBLE);
//		}
		

		
		
	}
	 Typeface tfByekan;
	private void backupDatabase(){
		try {
			File sd = Environment.getExternalStorageDirectory();
			String directory="/dataLogger";
			String directory1="/dataLogger/Backup";
			if (sd.canWrite()) {
				String currentDBPath = "/data/data/ir.saa.android.datalogger/databases/dl.db";;
				String backupDBPath = "/dataLogger/Backup/Data_Logger.db";
				//String backupDBPath = String.format("/SAMiN_Backup_%s.db",PersianCalendar.getCurrentShamsiDate());
				File currentDB =  new File(currentDBPath);
				File bcdirectory=new File(sd,directory);
				File dir1=new File(sd,directory1);
				File backupDB = new File(sd, backupDBPath);

				if(!bcdirectory.exists()){
					bcdirectory.mkdir();

				}
				if(!dir1.exists()){
					dir1.mkdir();
				}
				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					MyToast.Show(G.context, (String) getResources().getText(R.string.Backup_Message), Toast.LENGTH_SHORT);
					//ShowToast.crateToast(G.context, G.get_string(R.string.backup_ok), ShowToast.State.Successfull);
					//CustomToast.create_toast(G.get_string(R.string.backup_ok),2500, CustomToast.State.Successful);
				}
			}
		} catch (Exception e) {
			String rs=e.getMessage();
		}
	}
	private void restoreDatabase(){


		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("رمز");

		// Set up the input
		final EditText input = new EditText(getActivity());
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("بازيابي اطلاعات", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {


				if(input.getText().toString().equals("88799914")) {
					try {
						File sd = Environment.getExternalStorageDirectory();
						File data = Environment.getDataDirectory();

						if (sd.canWrite()) {
							String currentDBPath = "/data/ir.saa.android.datalogger/databases/dl.db";
							String backupDBPath = "/Data_Logger.db";

							File currentDB = new File(data, currentDBPath);
							File backupDB = new File(sd, backupDBPath);

							if (currentDB.exists()) {
								FileChannel dst = new FileOutputStream(currentDB).getChannel();
								FileChannel src = new FileInputStream(backupDB).getChannel();
								//FileChannel src = fileInputStream.getChannel();


								dst.transferFrom(src, 0, src.size());
								src.close();
								dst.close();
								MyToast.Show(getActivity(), "Ok", Toast.LENGTH_SHORT);
							}
						}
					} catch (Exception e) {
						Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
					}
				}else{
					MyToast.Show(getActivity(), "NOk", Toast.LENGTH_SHORT);
				}
			}
		});
		builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();


	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//----------Bottom Menu----------
		
		LinearLayout llHomeSetting = (LinearLayout) view.findViewById(R.id.llHomeSetting);
		LinearLayout llHomeGozareshat = (LinearLayout) view.findViewById(R.id.llHomeGozareshat);
		LinearLayout llHomeSabt = (LinearLayout) view.findViewById(R.id.llHomeSabt);
		LinearLayout llBackup = (LinearLayout) view.findViewById(R.id.llBackup);

		llHomeSetting.requestFocus();

		ImageView imgHomeSetting = (ImageView) view.findViewById(R.id.imgHomeSetting);
		ImageView imgHomeGozareshat = (ImageView) view.findViewById(R.id.imgHomeGozareshat);
		ImageView imgHomeSabt = (ImageView) view.findViewById(R.id.imgHomeSabt);

		TextView txtHomeSetting = (TextView) view.findViewById(R.id.txtHomeSetting);
		TextView txtHomeGozareshat = (TextView) view.findViewById(R.id.txtHomeGozareshat);
		TextView txtHomeSabt = (TextView) view.findViewById(R.id.txtHomeSabt);
		txtHomeSetting.setTypeface(tfByekan);
		txtHomeGozareshat.setTypeface(tfByekan);
		txtHomeSabt.setTypeface(tfByekan);


		if(G.currentUser!=null && G.currentUser.IsManager == 1)
			llHomeSetting.setVisibility(View.VISIBLE);
		else
			llHomeSetting.setVisibility(View.GONE);

		OnClickListener clickSetting = new OnClickListener() {
			@Override
			public void onClick(View v) {
				G.RegisterItem=false;
				Intent intent = new Intent(G.context,ActivitySettings.class);
				startActivity(intent);
			}
		};
		llHomeSetting.setOnClickListener(clickSetting);
		imgHomeSetting.setOnClickListener(clickSetting);
		txtHomeSetting.setOnClickListener(clickSetting);
		
		OnClickListener clickGozareshat = new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView txtMenuItem2 = (TextView) G.llMenu.findViewById(R.id.txtMenuItem2);
				G.RegisterItem=false;

				txtMenuItem2.performClick();
			}
		};
		llHomeGozareshat.setOnClickListener(clickGozareshat);
		imgHomeGozareshat.setOnClickListener(clickGozareshat);
		txtHomeGozareshat.setOnClickListener(clickGozareshat);
		
		OnClickListener clickSabt = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(G.Setting !=null) {
					G.PostId=null;
					G.selectedMenuItemMode = G.MenuItemMode.Sabt;
//					if (G.Setting.ModTag == 1 && G.currentUser.IsManager!=1) {
//						((ActivityDrawer)getActivity()).ShowNfcDialog();
//					} else {
						TextView txtMenuItem1 = (TextView) G.llMenu.findViewById(R.id.txtMenuItem1);
					G.RegisterItem=true;
						txtMenuItem1.performClick();
//					}
				}else{
					MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.GetSetting), Toast.LENGTH_SHORT);
				}
			}
		};
		llHomeSabt.setOnClickListener(clickSabt);
		imgHomeSabt.setOnClickListener(clickSabt);
		txtHomeSabt.setOnClickListener(clickSabt);
		llBackup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				G.RegisterItem=false;
				//restoreDatabase();
				backupDatabase();
			}
		});

		lblUnsubmitedItemCount = new MyLabel(G.context,"-",(String)G.context.getText(R.string.ItemRegister),textLeftSize,textRightSize);
		lblUnsubmitedItemCount.setBackgroundColor(Color.parseColor("#e63f29"));
		lblSubmitedItemCount = new MyLabel(G.context,"-",(String)G.context.getText(R.string.ItemNotRegister),textLeftSize,textRightSize);
		lblSubmitedItemCount.setBackgroundColor(Color.parseColor("#fa6800"));
		imgDashboard5_1 =  view.findViewById(R.id.imgDashboard5_1);
		imgDashboard5_2 =  view.findViewById(R.id.imgDashboard5_2);
		MyLabel lblVahedCount ;
		MyLabel lblZirTajhizCount;
		llDashboardRow5_1 =  view.findViewById(R.id.llDashboardRow5_1);
		llDashboardRow5_2 =  view.findViewById(R.id.llDashboardRow5_2);
		//----------Dashboard----------
		//ArrayList<dtoPosts> dtoPostsArrayList = new ArrayList<>();
		if(G.RTL==false) {

		LinearLayout llDashboardRow1 = view.findViewById(R.id.llDashboardRow1);
		//llDashboardRow1.setLayoutParams(paramsTextView);
		LinearLayout llDashboardRow2 =  view.findViewById(R.id.llDashboardRow2);
		//llDashboardRow2.setLayoutParams(paramsTextView);
		LinearLayout llDashboardRow3 =  view.findViewById(R.id.llDashboardRow3);
		//llDashboardRow3.setLayoutParams(paramsTextView);
		LinearLayout llDashboardRow4 =  view.findViewById(R.id.llDashboardRow4);
		LinearLayout lllDashboardRow6=view.findViewById(R.id.llDashboardRow6);
		LinearLayout lllDashboardRow7=view.findViewById(R.id.llDashboardRow7);
		LinearLayout lllDashboardRow8=view.findViewById(R.id.llDashboardRow8);
		//llDashboardRow4.setLayoutParams(paramsTextView);
		LinearLayout llDashboardRow5_1 =  view.findViewById(R.id.llDashboardRow5_1);
		//llDashboardRow5_1.setLayoutParams(paramsTextView);
		 llDashboardRow5_2 =  view.findViewById(R.id.llDashboardRow5_2);
		//llDashboardRow5_2.setLayoutParams(paramsTextView);
		lblVahedCount = new MyLabel(G.context,"-", (String) G.context.getText(R.string.UnitCount),textLeftSize,textRightSize);

		lblVahedCount.setBackgroundColor(Color.parseColor("#009d81"));
		lblLogshitCount = new MyLabel(G.context,"-",(String)G.context.getResources().getText(R.string.LogshitCount),textLeftSize,textRightSize);
		lblLogshitCount.setBackgroundColor(Color.parseColor("#f0991e"));

		llDashboardRow1.addView(lblVahedCount);
		llDashboardRow2.addView(lblLogshitCount);


		lblTajhizCount = new MyLabel(G.context,"-",(String)G.context.getResources().getText(R.string.EquipCount),textLeftSize,textRightSize);
		lblTajhizCount.setBackgroundColor(Color.parseColor("#1a3c55"));
		lblZirTajhizCount = new MyLabel(G.context,"-",(String)G.context.getResources().getText(R.string.SubEquipCount),textLeftSize,textRightSize);
		lblZirTajhizCount.setBackgroundColor(Color.parseColor("#0f7ab7"));

		llDashboardRow3.addView(lblTajhizCount);
		llDashboardRow4.addView(lblZirTajhizCount);

			lblItemCount=new MyLabel(G.context,"-",(String)G.context.getResources().getText(R.string.ItemCount),textLeftSize,textRightSize);

			lblItemCount.setBackgroundColor(Color.parseColor("#a20025"));
			lllDashboardRow6.addView(lblItemCount);


		//llDashboardRow3.addView(lblItemCount);

		final MyLabel lblSubmitedItemCount = new MyLabel(G.context,"-",(String)G.context.getText(R.string.ItemNotRegister),textLeftSize,textRightSize);
		lblSubmitedItemCount.setBackgroundColor(Color.parseColor("#fa6800"));
		final MyLabel lblUnsubmitedItemCount = new MyLabel(G.context,"-",(String)G.context.getText(R.string.ItemRegister),textLeftSize,textRightSize);
		lblUnsubmitedItemCount.setBackgroundColor(Color.parseColor("#e63f29"));

			lllDashboardRow7.addView(lblUnsubmitedItemCount);
			lllDashboardRow8.addView(lblSubmitedItemCount);
		}
		else{
			LinearLayout llDashboardRow1 = view.findViewById(R.id.llDashboardRow1);
			LinearLayout llDashboardRow2 =  view.findViewById(R.id.llDashboardRow2);
			LinearLayout llDashboardRow3 =  view.findViewById(R.id.llDashboardRow3);
			LinearLayout llDashboardRow4 =  view.findViewById(R.id.llDashboardRow4);
			LinearLayout lllDashboardRow5 = view.findViewById(R.id.lllDashboardRow5);
			LinearLayout llDashboardRow6 =  view.findViewById(R.id.llDashboardRow6);
			LinearLayout llDashboardRow7 =  view.findViewById(R.id.llDashboardRow7);
			LinearLayout llDashboardRow8 =  view.findViewById(R.id.llDashboardRow8);
			//LinearLayout llDashboardRowGetCountItem=view.findViewById(R.id.llDashboardRowGetCountItem);


			imgDashboard5_1 =  view.findViewById(R.id.imgDashboard5_1);
			ImageView imgDashboard5_2 =  view.findViewById(R.id.imgDashboard5_2);

			lblVahedCount = new MyLabel(G.context,"-", (String) G.context.getText(R.string.UnitCount),40,18);

			lblVahedCount.setBackgroundColor(Color.parseColor("#009d81"));
			lblLogshitCount = new MyLabel(G.context,"-",(String)G.context.getResources().getText(R.string.LogshitCount),40,18);
			lblLogshitCount.setBackgroundColor(Color.parseColor("#f0991e"));

			llDashboardRow2.addView(lblLogshitCount);
			llDashboardRow1.addView(lblVahedCount);

			lblTajhizCount = new MyLabel(G.context,"-",(String)G.context.getResources().getText(R.string.EquipCount),40,18);
			lblTajhizCount.setBackgroundColor(Color.parseColor("#1a3c55"));
			lblZirTajhizCount = new MyLabel(G.context,"-",(String)G.context.getResources().getText(R.string.SubEquipCount),40,18);
			lblZirTajhizCount.setBackgroundColor(Color.parseColor("#0f7ab7"));

			llDashboardRow4.addView(lblZirTajhizCount);
			llDashboardRow3.addView(lblTajhizCount);

			lblItemCount = new MyLabel(G.context,"-",(String)G.context.getResources().getText(R.string.ItemCount),40,18);
			lblItemCount.setBackgroundColor(Color.parseColor("#a20025"));

			lllDashboardRow5.addView(lblItemCount);



			llDashboardRow7.addView(lblSubmitedItemCount);
			llDashboardRow6.addView(lblUnsubmitedItemCount);

		}
		//llDashboardRow4.addView(lblUnsubmitedItemCount);
		//llDashboardRow4.addView(lblSubmitedItemCount);

//        final MyLabel lblRegisterItemCount = new MyLabel(G.context,"-",(String)G.context.getText(R.string.ItemCountRegister),44,22);
//        lblRegisterItemCount.setBackgroundColor(Color.parseColor("#eb9600"));
//
//        llDashboardRowCountItem.addView(lblRegisterItemCount);


//-------------------------LineChart--------------------------------

		Map<String,Integer> mapDateCount = G.DB.GetDateAndCountMapByTopNumber(7);

		TextView txtHomeLineChartLabel = view.findViewById(R.id.txtHomeLineChartLabel);
		txtHomeLineChartLabel.setText((String)G.context.getText(R.string.ItemSevenLastDay));
		txtHomeLineChartLabel.setTextSize(lineChartTextSize);
		txtHomeLineChartLabel.setTypeface(tfByekan);

		List<Entry> entries = new ArrayList<>();
		if(mapDateCount.size()!=0) {

			LineChart chart = new LineChart(G.context);
			if(G.RTL==false) {
				chart.setVisibility(View.GONE);
			}
			chart.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
			llDashboardRow5_1.addView(chart);

			chart.setDrawGridBackground(false);
			chart.setDescription(null);
			Legend legend = chart.getLegend();
			legend.setEnabled(false);

			XAxis xAxis = chart.getXAxis();
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
			xAxis.setTypeface(tfByekan);
			xAxis.setTextSize(lineChartTextSize);
			xAxis.mLabelWidth=mLableWidth;
			xAxis.setTextColor(Color.parseColor("#333333"));
			xAxis.setDrawGridLines(false);
			xAxis.setDrawAxisLine(true);
			xAxis.setLabelRotationAngle(-90f);

			YAxis leftAxis = chart.getAxisLeft();
			leftAxis.setTypeface(tfByekan);

			leftAxis.setTextSize(lineChartTextSize);
			leftAxis.setTextColor(Color.parseColor("#333333"));
			leftAxis.setDrawGridLines(true);
			leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
			leftAxis.setLabelCount(mapDateCount.size(), false);
			leftAxis.setGranularity(1);
			leftAxis.setGranularityEnabled(true);
			leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

			YAxis rightAxis = chart.getAxisRight();
			rightAxis.setDrawLabels(false);
			rightAxis.setTypeface(tfByekan);
			rightAxis.setTextSize(lineChartTextSize);
			rightAxis.setTextColor(Color.parseColor("#333333"));
			rightAxis.setDrawGridLines(false);
			rightAxis.setAxisMinimum(0f);
			rightAxis.setMaxWidth(10);

			// this replaces setStartAtZero(true)

			String[] strArray = new String[7];


			if (mapDateCount.size() == 1) {
				strArray[0] = "";
				strArray[1] = mapDateCount.keySet().toArray()[0].toString();
				xAxis.setLabelCount(2, true);
				xAxis.setValueFormatter(new MyXAxisValueFormatter(strArray));
			} else if (mapDateCount.size() > 1) {
				strArray = getSortAndCloneList(mapDateCount.keySet().toArray());
				xAxis.setLabelCount(mapDateCount.size(), true);
				xAxis.setValueFormatter(new MyXAxisValueFormatter(strArray));
			}


			if (mapDateCount.size() == 1) {
				entries.add(new Entry(0, 0));
				entries.add(new Entry(1, mapDateCount.get(strArray[1])));
			} else if (mapDateCount.size() > 1) {
				Integer count = 0;
				for (String strKey : strArray) {
					if (strKey != null)
						entries.add(new Entry(count++, mapDateCount.get(strKey)));
				}
			}

			LineDataSet dataSet = new LineDataSet(entries, "");

			dataSet.setLineWidth(textRightSize);
			dataSet.setValueTextSize(lineChartTextSize);
			dataSet.setValueTypeface(tfByekan);
			dataSet.setColor(Color.parseColor("#2793d0"));
			dataSet.setCircleColor(Color.parseColor("#165273"));
			dataSet.setValueTextColor(Color.parseColor("#194157"));

			LineData lineData = new LineData(dataSet);
			chart.setData(lineData);
			chart.invalidate();

			chart.animateY(2000);
		}else{
			imgDashboard5_1.setVisibility(View.VISIBLE);
			txtHomeLineChartLabel.setText(G.context.getText(R.string.NoItemData));
		}

		//-------------------PieChart---------------------


		final PieChart pieChart = new PieChart(G.context);
		pieChart.setDescription(null);
		Legend legend2 = pieChart.getLegend();
		legend2.setEnabled(false);

		pieChart.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

		TextView txtHomePieChartLabel = view.findViewById(R.id.txtHomePieChartLabel);
		txtHomePieChartLabel.setText((String)G.context.getText(R.string.ItemRegisterNotRegister));
		txtHomePieChartLabel.setTextSize(lineChartTextSize);
		txtHomePieChartLabel.setTypeface(tfByekan);

		llDashboardRow5_2.addView(pieChart);

		pieChart.setHoleColor((Color.parseColor("#eeeeee")));
		pieChart.setCenterTextColor(Color.parseColor("#000000"));
		pieChart.setCenterTextSize(pieChartCenterText);
		pieChart.setCenterTextTypeface(tfByekan);

		pieChart.setEntryLabelTextSize(EnteryTextsize);
		pieChart.setEntryLabelColor(Color.parseColor("#000000"));
		pieChart.setEntryLabelTypeface(tfByekan);

		pieChart.setNoDataText((String) G.context.getText(R.string.NoItemData));
		pieChart.setNoDataTextTypeface(tfByekan);
		pieChart.setNoDataTextColor(Color.parseColor("#000000"));


//		txtName.setText(Html.fromHtml("<b>"+G.currentUser.FullName+"</b>"));


		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					ArrayList<dtoPosts> postList = G.DB.GetVAHEDList();
					final int vahedCount = postList.size();
					ArrayList<dtoLogShits> logshitList = G.DB.GetLogshitListByPosts(postList);
					final int logshitCount = logshitList.size();
					ArrayList<dtoEquipments> tajhizList = G.DB.GetEquipmentListByLogshits(logshitList);
					final int tajhizCount = tajhizList.size();
					ArrayList<dtoSubEquipments> zirTajhizList = G.DB.GetSubEquipmentListByEquipments(tajhizList);
					final int zirTajhizCount = zirTajhizList.size();
					final ArrayList<dtoItems> itemList = G.DB.GetItemListByEquipmentsAndSubEquipments(tajhizList,zirTajhizList);
					final int itemCount = itemList.size();
//					final ArrayList<dtoItemValues> dtoItemsDtoItemValues=G.DB.getItemValuesByUserIdDistinct(G.currentUser.UsrID);
//					final int itemCountValues=dtoItemsDtoItemValues.size();

					G.handler.post(new Runnable() {
						@Override
						public void run() {

								lblVahedCount.setLeftText(String.valueOf(vahedCount));
								lblLogshitCount.setLeftText(String.valueOf(logshitCount));
								lblTajhizCount.setLeftText(String.valueOf(tajhizCount));
								lblZirTajhizCount.setLeftText(String.valueOf(zirTajhizCount));
								lblItemCount.setLeftText(String.valueOf(itemCount));

							//lblRegisterItemCount.setLeftText(String.valueOf(itemCountValues));
						}
					});

					//-------------------

					Integer itemSubmitedCount = 0;
					if(G.Setting!=null)
						for(dtoItems it : itemList){
							if (G.Setting.ContorolTime.compareTo("2") == 0 && !(TabFragmentItem.IsValidForSubmit(it)==ValidForSubmitType.InRange)) {
								itemSubmitedCount++;
								continue;
							}
							if (G.Setting.NoCheckMaxData != 1) {
								ArrayList<dtoItemValues> itemVals = G.DB.getItemValuesByItemInfoId(it.ItemInfID);
								if (itemVals.size() == it.MaxSampleNo) {
									itemSubmitedCount++;
								}
							}
						}

					Integer itemUnSubmitedCount = itemCount - itemSubmitedCount;

					if(itemCount==0){
						itemSubmitedCount = 0;
						itemUnSubmitedCount = 0;
					}

					//----------------------

					final Integer finalItemUnSubmitedCount = itemUnSubmitedCount;
					final Integer finalItemSubmitedCount = itemSubmitedCount;
					//------------charts---------------
					DecimalFormat decimalforamt = new DecimalFormat("#0.0");

					String itemSubmitedPercent = G.convertToEnglishDigits(decimalforamt.format(round(itemSubmitedCount*100./itemCount,1)),true);
					String itemUnSubmitedPercent = decimalforamt.format(round(100. -Double.valueOf(itemSubmitedPercent),1) );

					if(itemCount==0){
						itemSubmitedPercent = "0";
						itemUnSubmitedPercent = "100";
					}

					final String finalItemSubmitedPercent = G.convertToEnglishDigits(itemSubmitedPercent,true);
					final String finalItemUnSubmitedPercent = G.convertToEnglishDigits(itemUnSubmitedPercent,true);

					G.handler.post(new Runnable() {
						@Override
						public void run() {
							lblSubmitedItemCount.setLeftText(finalItemSubmitedCount.toString());
							lblUnsubmitedItemCount.setLeftText(finalItemUnSubmitedCount.toString());
							//----------
							pieChart.setCenterText( finalItemSubmitedPercent +"%" );
							ArrayList<PieEntry> entries1 = new ArrayList<>();
							entries1.add(new PieEntry(Float.valueOf(finalItemSubmitedPercent), (String)G.context.getText(R.string.ItemNotRegister)));
							entries1.add(new PieEntry(Float.valueOf(finalItemUnSubmitedPercent), (String)G.context.getText(R.string.ItemRegister)));

							PieDataSet dataset1 = new PieDataSet(entries1, "");

							dataset1.setColors(Color.parseColor("#f79e1a"),Color.parseColor("#7d8888"));
							//dataset1.setColors(ColorTemplate.COLORFUL_COLORS);
							PieData data = new PieData(dataset1);
							pieChart.setData(data);
							pieChart.animateY(2000);
						}
					});



				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		thread.start();
	}
	private static double round (double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
	private String[] getSortAndCloneList(Object[] lst){

		String[] inputList = new String[lst.length];
		//inputList= (String[]) lst.clone();
		System.arraycopy(lst, 0, inputList, 0, lst.length);

		Arrays.sort(inputList);

		return  inputList;
	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		LinearLayout ll = (LinearLayout) G.actionBar.getCustomView().findViewById(R.id.appBar2);
		ll.setVisibility(View.GONE);
		
	}
	
	
}
