package ir.saa.android.datalogger;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import enums.ValidForSubmitType;
import mycomponents.MyDialog;
import mycomponents.MyToast;

import database.adapters.AdapterItem;
import database.structs.dtoItemValues;
import database.structs.dtoItems;
import mycomponents.MyUtilities;
import mycomponents.Range;
import saman.zamani.persiandate.PersianDate;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.WINDOW_SERVICE;

public class TabFragmentItem extends Fragment {

	Timer timer;
	TimerTask timerTask;
	public static  ListView listItem ;
	public static ImageView imgSaveItem;
	TextView txtDateTimeForItems;
	public static Spinner searchSpinner;
	public static EditText edtSearch;
	public static String OldVoicePath="";
	public static String OldImagePath="";
	public static String OldVideoPath="";
	public static ArrayList<dtoItems> arrListItem = new ArrayList<dtoItems>();
	public static ArrayList<dtoItems> arrListItemFiltered = new ArrayList<dtoItems>();
	public static Map<Integer,dtoItemValues> dicItemValues ;
	public static AdapterItem adapterItem; 
	Typeface tf ;
	int LayoutPage;
	int  LayoutPageItem;
	@Override
	public void onResume() {
		adapterItem.notifyDataSetChanged();
		AdapterItem.filter(edtSearch.getText().toString());
		super.onResume();
	}
	public  void adjustFontScale(Configuration configuration, Float fontSize) {

		configuration.fontScale = (float) fontSize;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		metrics.scaledDensity = configuration.fontScale * metrics.density;
		getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);

	}
	
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		LayoutPage=R.layout.tab_fragment_item;
		LayoutPageItem=R.layout.custom_item;

		if(G.RTL){
            tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");

        }else{
            tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
            adjustFontScale(getResources().getConfiguration(),(float)1);
        }
		View rootView = inflater
				.inflate(LayoutPage, container, false);
		View v=inflater
				.inflate(LayoutPageItem, container, false);
		adapterItem = new AdapterItem(arrListItemFiltered,LayoutPageItem,v);

		return rootView;
	}
	
	public void startTimer() {
	    timer = new Timer();
	    initializeTimerTask();
	    timer.schedule(timerTask, 0, 1000); 
	}
	public void stoptimertask() {
	    if (timer != null) {
	        timer.cancel();
	        timer = null;
	    }
	}
	public void initializeTimerTask() {
	    timerTask = new TimerTask() {
	        public void run() {
	            G.handler.post(new Runnable() {
	                @RequiresApi(api = Build.VERSION_CODES.N)
					public void run() {
	                	String DateAndTime="";
	                	if(G.RTL){
							DateAndTime=Tarikh.getCurrentShamsidatetime();
						}else {
							Date today = Calendar.getInstance().getTime();
							SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
							DateAndTime=df.format(today);
						}

	                	txtDateTimeForItems.setText(DateAndTime);


	                }
	            });
	        }
	    };
	}
	
	@Override
	public void onDestroy() {
		G.llMenu.setVisibility(View.VISIBLE);
		super.onDestroy();
	}
	
	@SuppressLint("UseSparseArrays")
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

//		if(G.isGpsEnabled())
//        	G.updateGpsLocation();
//        else{
//        	G.latitude = "";
//        	G.longitude = "";
//        }
		G.mDrawerLayout.setEnabled(false);
		
		txtDateTimeForItems = (TextView) view.findViewById(R.id.txtDateTimeForItems);
		dicItemValues = new HashMap<Integer,dtoItemValues>();
		listItem = (ListView) view.findViewById(R.id.lstItem);
		imgSaveItem = (ImageView) view.findViewById(R.id.imgSaveItem);
		edtSearch = (EditText) view.findViewById(R.id.edtSearchItem);
		searchSpinner = (Spinner) view.findViewById(R.id.searchSpinnerItem);
		txtDateTimeForItems.setTypeface(tf);
		startTimer();

		List<String> categories = new ArrayList<String>();
        categories.add((String) G.context.getText(R.string.AllItem));
        categories.add((String) G.context.getText(R.string.NotRegister));
        categories.add((String) G.context.getText(R.string.Registered));
        //spinner search
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(G.context, R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.spinner_chtxt_item);
        searchSpinner.setAdapter(dataAdapter);


		listItem.setAdapter(adapterItem);

		searchSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	AdapterItem.filter(edtSearch.getText().toString());
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		listItem.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {
					if (listItem.onKeyUp(keyCode, event)) {
						int postion = listItem.getSelectedItemPosition();
						dtoItems item = ((dtoItems) listItem.getSelectedItem());
						adapterItem.viewHolder.ActionCommand(item,postion);



					}
				}

				if(keyCode==KeyEvent.KEYCODE_3){
					edtSearch.requestFocus();

				}

				return false;
			}
		});

		edtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String strSearch = String.valueOf(s);
				AdapterItem.filter(strSearch);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		imgSaveItem.setOnClickListener(new OnClickListener() {
            public boolean deleteFileFromSdCard(String filePath) {
                File file;
                file = new File(filePath);
                boolean isExist = file.delete();
                return isExist;
            }
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onClick(View v) {

					final MyDialog mydialog = new MyDialog(G.currentActivity);
					if (G.RTL) {

						mydialog.setTitle((String) G.context.getText(R.string.SaveMessage))
								.addContentXml(R.layout.body_save_statistics)
								.setVerticalMargin(10)
								.setContentSplitter()
								.setVerticalMargin(10)
								.addContentXml(R.layout.body_one_textview)
								.addButton((String) G.context.getText(R.string.yes), new OnClickListener() {

									@Override
									public void onClick(View v) {
										if(G.isSave==false) {
											for (dtoItems item : arrListItem) {
												if (!(dicItemValues.get(item.ItemInfID).ItemVal == null || dicItemValues.get(item.ItemInfID).ItemVal.replaceAll("/", "").replaceAll(":", "").replaceAll(",", "").trim().length() == 0))
													G.DB.InsertItemValues(dicItemValues.get(item.ItemInfID));
												deleteFileFromSdCard(OldVideoPath);
												deleteFileFromSdCard(OldVoicePath);
												deleteFileFromSdCard(OldImagePath);
											}
										}
										populateFromDatabase();
										adapterItem.notifyDataSetChanged();
										//MyToast.Show(G.context, "اطلاعات آیتم ها با موفقیت ذخیره شد", Toast.LENGTH_LONG);
										edtSearch.setText("");
										searchSpinner.setSelection(0);
										mydialog.dismiss();
										//--------Double BackPress Simulation for back to EquipmentList--------
										//--------Request from Ms Rezaei -> EDIT: One BackPress Simulation for back to One Layer Back--------
										TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
										String prevTxtTitle = txtTitle.getText().toString();
										getActivity().onBackPressed();
										//getActivity().onBackPressed();

										//MyToast.Show(G.context, String.format("اطلاعات آیتم های %s با موفقیت ثبت شد",prevTxtTitle), Toast.LENGTH_LONG);
										MyToast.Show(G.context, String.format((String) G.context.getText(R.string.ItemSaveSuccesfully)), Toast.LENGTH_LONG);
										//Snackbar.make(G.coordinatorLayout, String.format("اطلاعات آیتم های %s ثبت شد",prevTxtTitle) , Snackbar.LENGTH_LONG).show();

										//---------------------------------------------------------------------
									}
								})
								.addButton((String) G.context.getText(R.string.no), new OnClickListener() {

									@Override
									public void onClick(View v) {

										mydialog.dismiss();
									}
								})

								.show();

					} else {
						mydialog.setTitle((String) G.context.getText(R.string.SaveMessage))
								.addContentXml(R.layout.body_save_statistics)
								.setVerticalMargin(10)
								.setContentSplitter()
								.setVerticalMargin(10)
								.addContentXml(R.layout.body_one_textview)
								.addButtonL((String) G.context.getText(R.string.no), new OnClickListener() {

									@Override
									public void onClick(View v) {

										mydialog.dismiss();
									}
								})
								.addButtonR((String) G.context.getText(R.string.yes), new OnClickListener() {

									@Override
									public void onClick(View v) {
										if(G.isSave==false) {
											for (dtoItems item : arrListItem) {
												if (!(dicItemValues.get(item.ItemInfID).ItemVal == null || dicItemValues.get(item.ItemInfID).ItemVal.replaceAll("/", "").replaceAll(":", "").replaceAll(",", "").trim().length() == 0))
													G.DB.InsertItemValues(dicItemValues.get(item.ItemInfID));
												deleteFileFromSdCard(OldVideoPath);
												deleteFileFromSdCard(OldVoicePath);
												deleteFileFromSdCard(OldImagePath);
											}
										}
										populateFromDatabase();
										adapterItem.notifyDataSetChanged();
										//MyToast.Show(G.context, "اطلاعات آیتم ها با موفقیت ذخیره شد", Toast.LENGTH_LONG);
										edtSearch.setText("");
										searchSpinner.setSelection(0);
										mydialog.dismiss();
										//--------Double BackPress Simulation for back to EquipmentList--------
										//--------Request from Ms Rezaei -> EDIT: One BackPress Simulation for back to One Layer Back--------
										TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
										String prevTxtTitle = txtTitle.getText().toString();

										if(getActivity()!=null) {
											getActivity().onBackPressed();
										}
										//getActivity().onBackPressed();

										//MyToast.Show(G.context, String.format("اطلاعات آیتم های %s با موفقیت ثبت شد",prevTxtTitle), Toast.LENGTH_LONG);
										MyToast.Show(G.context, String.format((String) G.context.getText(R.string.ItemSaveSuccesfully)), Toast.LENGTH_LONG);
										//Snackbar.make(G.coordinatorLayout, String.format("اطلاعات آیتم های %s ثبت شد",prevTxtTitle) , Snackbar.LENGTH_LONG).show();

										//---------------------------------------------------------------------
									}
								})
								.show();
					}

					TextView txtBodyMessage = (TextView) mydialog.getDialog().findViewById(R.id.txtBodymessage);
					txtBodyMessage.setText((String) G.context.getText(R.string.Msg_SaveItem));
					txtBodyMessage.setTypeface(tf);
					TextView txtSaveAmar1 = (TextView) mydialog.getDialog().findViewById(R.id.txtSaveAmar1);
					TextView txtSaveAmar2 = (TextView) mydialog.getDialog().findViewById(R.id.txtSaveAmar2);
					TextView txtSaveAmar3 = (TextView) mydialog.getDialog().findViewById(R.id.txtSaveAmar3);
					Button btnSaveAmar = (Button) mydialog.getDialog().findViewById(R.id.btnSaveAmar);
					txtSaveAmar1.setTypeface(tf);
					txtSaveAmar2.setTypeface(tf);
					txtSaveAmar3.setTypeface(tf);
					btnSaveAmar.setTypeface(tf);

					int countSabtNashode = getCountOfNotEnteredItem();

					txtSaveAmar1.setText(getText(R.string.Msg_CountAllItem).toString() + arrListItem.size());
					txtSaveAmar2.setText(getText(R.string.Msg_RegiserAllItem).toString() + (arrListItem.size() - countSabtNashode));
					txtSaveAmar3.setText(getText(R.string.Msg_NotRegiserAllItem).toString() + countSabtNashode);

					btnSaveAmar.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							edtSearch.setText("");
							// sabt nashodeha
							listItem.requestFocus();
							listItem.setSelection(0);
//							((LinearLayout)((LinearLayout)((LinearLayout)listItem.getChildAt(0)).getChildAt(0))
//									.getChildAt(0)).getChildAt(5).requestFocus();
							mydialog.dismiss();
							searchSpinner.setSelection(1);
						}
					});
			}
		});

		//if(G.selectedId == null)
		populateFromDatabase();
		adapterItem.notifyDataSetChanged();
		listItem.requestFocus();
//		populateFakeData();

		super.onViewCreated(view, savedInstanceState);
	}
	
	public static int getCountOfNotEnteredItem() {
		int countSabtNashode = 0;
		for (dtoItems item : arrListItem) 
			{
				if(dicItemValues.get(item.ItemInfID).ItemVal == null || dicItemValues.get(item.ItemInfID).ItemVal.trim().length()==0)
					countSabtNashode++;
			}
		return countSabtNashode;
	}


	public void populateFromDatabase() {
		arrListItem.clear();
		arrListItemFiltered.clear();
		if(dicItemValues!=null) {
			dicItemValues.clear();
		}
		dicItemValues=new HashMap<Integer, dtoItemValues>();
		Integer selectedEquipId = -1 ;
		Integer selectedSubEquipId = -1;
		Integer selectedItemInfId = -1;
		Bundle bundle = this.getArguments();
		if (bundle != null) {
		    int eid = bundle.getInt("EquipID",-1);
		    if(eid>=0){
		    	selectedEquipId = eid;
		    }
		    int seid = bundle.getInt("SubEquipID",-1);
		    if(seid>=0){
		    	selectedSubEquipId = seid;
		    }
			int itemInfId = bundle.getInt("ItemInfId",-1);
			if(itemInfId>=0){
				selectedItemInfId = itemInfId;
			}
		}

		ArrayList<dtoItems> lstItems ;
		if(selectedItemInfId!=-1){
			lstItems = G.DB.GetItemList(selectedEquipId, selectedSubEquipId,selectedItemInfId);
		}else{
			//lstItems = G.DB.(selectedEquipId, selectedSubEquipId);
			lstItems = G.DB.GetItemListWithValue(selectedEquipId, selectedSubEquipId);
		}
		//arrListItem.addAll(lstItems);
		for(dtoItems item:lstItems){

        	if(G.Setting.NoCheckMaxData!=1){
        		ArrayList<dtoItemValues> itemVals = G.DB.getItemValuesByItemInfoId(item.ItemInfID);
        		if(itemVals.size() >= item.MaxSampleNo){
        			item.IsFullOfMaxData = true;
        		}
        	}
			if(G.currentUser.IsManager!=1 && item.TagID !=null && item.TagID.trim().compareTo("null")!=0
					&& item.TagID.trim().compareTo(getActivity().getString(R.string.None))!=0   && item.TagID.trim().length()>0
					&& G.selectedMenuItemMode == G.MenuItemMode.Sabt){
				item.HasTag = true;
			}
        	if(G.Setting.ContorolTime.compareTo("0") != 0)
        		item.IsInTimeRange = IsValidForSubmit(item)==ValidForSubmitType.InRange?true:false;

			arrListItem.add(item);
			dtoItemValues itemValues = new dtoItemValues();
			itemValues.ItemInfID = item.ItemInfID;
			itemValues.ShiftID = G.currentUser.ShiftID;
			itemValues.UsrID = G.currentUser.UsrID;
			itemValues.ItemValTyp = item.AmountTypID.toString();
			dicItemValues.put(item.ItemInfID,itemValues );
		}
		arrListItemFiltered.addAll(arrListItem);
        adapterItem.notifyDataSetChanged();
    }


//	public static boolean IsValidForSubmit(dtoItems item){
//		boolean isValid = false;
//		if(G.Setting.ContorolTime.compareTo("0") == 0) // Do not check
//			return true;
//		if (item.STTime==null || item.PeriodTypTime==null||item.PeriodTime==null||item.RangeTypTime==null||item.RangeTime==null){
//			return  false;
//		}
//
//		Integer periodTime = item.PeriodTime;
//		Integer rangeTime = item.RangeTime;
//		//-------Convert PeriodTime to Minute---------
//		if(item.PeriodTypTime == 1){ // if hour
//			periodTime = periodTime * 60;
//		}else if(item.PeriodTypTime == 3){ // if day
//			periodTime = periodTime * 24 * 60;
//		}
//		//-------Convert RangeTime to Minute---------
//		if(item.RangeTypTime == 1){ // if hour
//			rangeTime= rangeTime * 60;
//		}else if(item.RangeTypTime == 3){ // if day
//			rangeTime = rangeTime * 24 * 60;
//		}
//
//		String strDateTime =Tarikh.getCurrentShamsidatetimeWithoutSlash().substring(0,12);
//		Long longCurrentDateTime = Long.parseLong(strDateTime);
//		dtoItemValues itemVal = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
//		Long periodSum = 0l;
//		Integer periodValue = periodTime%2==0?periodTime:periodTime+1;
//		if( (periodValue/2 <= rangeTime) ){
//			isValid = true;
//		}else if(itemVal==null){
//			String levelDateTime = strDateTime.substring(0,8)+item.STTime;
//			do {
//				Long startRange = Tarikh.subMinutesFromDateTime(levelDateTime,rangeTime);
//				Long endRange = Tarikh.addMinutesToDateTime(levelDateTime,rangeTime);
//				if(longCurrentDateTime >= startRange && longCurrentDateTime <= endRange){
//					isValid = true;
//					break;
//				}
//				if(longCurrentDateTime <= endRange) // don't check useless ranges and break
//					break;
//				levelDateTime = Tarikh.addMinutesToDateTime(levelDateTime,periodTime).toString();
//				periodSum+= periodTime;
//			}while (periodSum<=1440);
//		}else {
//			Integer year1 = Integer.valueOf(strDateTime.substring(0,4));
//			Integer month1 = Integer.valueOf(strDateTime.substring(4,6));
//			Integer day1 = Integer.valueOf(strDateTime.substring(6,8));
//			Integer hour1 = Integer.valueOf(strDateTime.substring(8,10));
//			Integer min1 = Integer.valueOf(strDateTime.substring(10,12));
//			String strLastItemDateTime = itemVal.PDate+itemVal.PTime;
//			Integer year2 = Integer.valueOf(strLastItemDateTime.substring(0,4));
//			Integer month2 = Integer.valueOf(strLastItemDateTime.substring(4,6));
//			Integer day2 = Integer.valueOf(strLastItemDateTime.substring(6,8));
//			Integer hour2 = Integer.valueOf(strLastItemDateTime.substring(8,10));
//			Integer min2 = Integer.valueOf(strLastItemDateTime.substring(10,12));
//
//			Long elapsedMins = getElapsedMins(
//					new PersianDate().initJalaliDate(year1,month1,day1,hour1,min1,0).toDate(),
//					new PersianDate().initJalaliDate(year2,month2,day2,hour2,min2,0).toDate()
//			);
//
//			//TODO--CHECK THIS OUT
//			long mode = elapsedMins % periodTime;
//			if (mode <= rangeTime || mode >= periodTime-rangeTime)
//			{
//				isValid = true;
//			}
////			String res = String.valueOf(longCurrentDateTime - lastItemDateTime);
////			List<Integer> lst = new ArrayList<>();
////
////			if (res.length() > 0 && Integer.valueOf(res)!=0)
////			{
////				while (res.length()<8){
////					res = "0" + res;
////				}
////				for (int i = 0; i < res.length() / 2; i++)
////				{
////					lst.add(Integer.valueOf(res.substring(i * 2,(i * 2)+ 2)));
////				}
////
////				long elapsedMins = 0;
////				elapsedMins += lst.get(1) * 24 * 60;
////				elapsedMins += lst.get(2) * 60;
////				elapsedMins += lst.get(3);
////
////				//long periodCount = elapsedMins / periodTime;
////				long mode = elapsedMins % periodTime;
////
////				if (mode <= rangeTime || mode >= periodTime-rangeTime)
////				{
////					isValid = true;
////				}
////			}else{
////				isValid = true;
////			}
//		}
//		return isValid;
//	}

	//----------Result----------
	//0
	//1
	//2
	//3
	public static Integer IsValidForSubmit(dtoItems item){
		Integer result = ValidForSubmitType.OutOfRange;
		if(G.Setting.ContorolTime.compareTo("0") == 0) // Do not check
			return ValidForSubmitType.InRange;
		if (item.STTime==null || item.PeriodTypTime==null||item.PeriodTime==null||item.RangeTypTime==null||item.RangeTime==null){
			return ValidForSubmitType.OutOfRange;
		}

		Integer periodTime = item.PeriodTime;
		Integer rangeTime = item.RangeTime;
		//-------Convert PeriodTime to Minute---------
		if(item.PeriodTypTime == 1){ // if hour
			periodTime = periodTime * 60;
		}else if(item.PeriodTypTime == 3){ // if day
			periodTime = periodTime * 24 * 60;
		}
		//-------Convert RangeTime to Minute---------
		if(item.RangeTypTime == 1){ // if hour
			rangeTime= rangeTime * 60;
		}else if(item.RangeTypTime == 3){ // if day
			rangeTime = rangeTime * 24 * 60;
		}

		String strDateTime =Tarikh.getCurrentMiladidatetimewithoutSlash();
		Long longCurrentDateTime = Long.parseLong(strDateTime);
		Long periodSum = 0l;

		String levelDateTime = strDateTime.substring(0,8)+item.STTime+"00";
		do {
			Long startRange = Tarikh.subMinutesFromDateTimeMiladi(levelDateTime,rangeTime);
			Long endRange = Tarikh.subMinutesFromDateTimeMiladi(levelDateTime,-rangeTime);
			if(longCurrentDateTime >= startRange && longCurrentDateTime <= endRange){

				dtoItemValues itemVal = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
				if(itemVal!=null){
					Long longLastValueDateTime = Tarikh.getLongDateTimeMiladi(itemVal.PDate);
					if(longLastValueDateTime >= startRange && longLastValueDateTime <= endRange){
						result = ValidForSubmitType.InRangeButHasOtherValue;
					}else
					{
						result = ValidForSubmitType.InRange;
					}
				}else {
					result = ValidForSubmitType.InRange;
				}
				break;
			}
			if(longCurrentDateTime <= endRange) // don't check useless ranges and break
				break;
			levelDateTime = Tarikh.subMinutesFromDateTimeMiladi(levelDateTime,-periodTime).toString();
			periodSum+= periodTime;
		}while (periodSum<=1440);

		return result;
	}
	public static String parseDateToddMMyyyy(String time) {
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern , Locale.UK);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(time);
			str=outputFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequiresApi(api = Build.VERSION_CODES.N)
	public static Integer IsValidForSubmitWitoutCheckSetting(dtoItems item){
		Integer result = ValidForSubmitType.OutOfRange;
//		if(G.Setting.ContorolTime.compareTo("0") == 0) // Do not check
//			return ValidForSubmitType.InRange;
//		if (item.STTime==null || item.PeriodTypTime==null||item.PeriodTime==null||item.RangeTypTime==null||item.RangeTime==null){
//			return ValidForSubmitType.OutOfRange;
//		}
		if(item.PeriodTypTime==null || item.RangeTime==null){
			return ValidForSubmitType.InRange;
		}
		Integer periodTime = item.PeriodTime;
		Integer rangeTime = item.RangeTime;
		//-------Convert PeriodTime to Minute---------
		if(item.PeriodTypTime == 1){ // if hour
			periodTime = periodTime * 60;
		}else if(item.PeriodTypTime == 3){ // if day
			periodTime = periodTime * 24 * 60;
		}
		//-------Convert RangeTime to Minute---------
		if(item.RangeTypTime == 1){ // if hour
			rangeTime= rangeTime * 60;
		}else if(item.RangeTypTime == 3){ // if day
			rangeTime = rangeTime * 24 * 60;
		}
		String strDateTime="";

		strDateTime = MyUtilities.changeNumberLocaleString(Tarikh.getCurrentMiladidatetimewithoutSlash());

		Long longCurrentDateTime = Long.parseLong(strDateTime);
		Long periodSum = 0l;
		Long startRange;
		Long endRange;
		String levelDateTime = strDateTime.substring(0,8)+item.STTime+"00";

		if(item.RangeTypTime==3 && item.PeriodTypTime==3){
			startRange = Tarikh.subMinutesFromDateTimeMiladi(levelDateTime, rangeTime);
			endRange = Tarikh.subMinutesFromDateTimeMiladi(levelDateTime, -rangeTime);

		}else {
			do {

				startRange = Tarikh.subMinutesFromDateTimeMiladi(levelDateTime, rangeTime);
				endRange = Tarikh.subMinutesFromDateTimeMiladi(levelDateTime, -rangeTime);
				String RangeFirst = "";
				String RangeEnd = "";

				if (startRange != null) {
					if (startRange.toString().length() == 14) {
						RangeFirst = startRange.toString().substring(0, 4) + "-"
								+ startRange.toString().substring(4, 6) + "-"
								+ startRange.toString().substring(6, 8) + " "
								+ startRange.toString().substring(8, 10) + ":"
								+ startRange.toString().substring(10, 12) + ":"
								+ startRange.toString().substring(12, 14);


					}
				}
				if (endRange != null) {
					if (endRange.toString().length() == 14) {
						RangeEnd = endRange.toString().substring(0, 4) + "-"
								+ endRange.toString().substring(4, 6) + "-"
								+ endRange.toString().substring(6, 8) + " "
								+ endRange.toString().substring(8, 10) + ":"
								+ endRange.toString().substring(10, 12) + ":"
								+ endRange.toString().substring(12, 14);
					}
				}

				if (periodTime > (2 * rangeTime)) {
					if (longCurrentDateTime >= startRange && longCurrentDateTime <= endRange) {

						dtoItemValues itemVal = G.DB.getLastItemValueByItemInfIdforCheckPeriod(item.ItemInfID, RangeFirst, RangeEnd);
						if (itemVal != null) {
							Long longLastValueDateTime = Tarikh.getLongDateTimeMiladi(itemVal.PDate);
							if (longLastValueDateTime >= startRange && longLastValueDateTime <= endRange) {
								result = ValidForSubmitType.InRangeButHasOtherValue;
							} else {
								result = ValidForSubmitType.InRange;
							}
						} else {
							result = ValidForSubmitType.InRange;
						}
						break;
					}
				} else {
					if (longCurrentDateTime >= startRange && longCurrentDateTime < endRange) {

						dtoItemValues itemVal = G.DB.getLastItemValueByItemInfIdforCheckPeriod(item.ItemInfID, RangeFirst, RangeEnd);
						if (itemVal != null) {
							Long longLastValueDateTime = Tarikh.getLongDateTimeMiladi(itemVal.PDate);
							if (longLastValueDateTime >= startRange && longLastValueDateTime < endRange) {
								result = ValidForSubmitType.InRangeButHasOtherValue;
							} else {
								result = ValidForSubmitType.InRange;
							}
						} else {
							result = ValidForSubmitType.InRange;
						}
						break;
					}
				}
//			if(longCurrentDateTime <= endRange) // don't check useless ranges and break
//				break;
				levelDateTime = Tarikh.subMinutesFromDateTimeMiladi(levelDateTime, -periodTime).toString();
				periodSum += periodTime;
			} while (periodSum <= 1440);
		}

		return result;
	}


	private static Long getElapsedMins(Date d1, Date d2){
		Long diffMinutes = 0l;
		try {
			//in milliseconds
			long diff =Math.abs(d2.getTime() - d1.getTime());
			//long diffSeconds = diff / 1000 % 60;
			diffMinutes = diff / (60 * 1000) % 60;
			//long diffHours = diff / (60 * 60 * 1000) % 24;
			//long diffDays = diff / (24 * 60 * 60 * 1000);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return diffMinutes;
	}
	
//	private void populateFakeData() {
//		G.arrListEyb.clear();
//        for (int i = 1; i <= 50; i++) {
//            StructTableEyb eyb = new StructTableEyb();
//            //eyb.tajhizGuid = strGuid; // temporary fake guid
//            eyb.Oid = "Oid"+ i ;
//            eyb.NameEyb = "عیب " + i;
//            eyb.Checked = false;
//            //eyb.eybPriority = i + 1;
//
//            G.arrListEyb.add(eyb);
//        }
//		
//        Log.i("tag", "arrlist count : " + G.arrListEyb.size());
//
//        G.adapterEyb.notifyDataSetChanged();
//        Log.i("tag", "line enddd ");
//
//        for (StructTableEyb stru: G.arrListEyb) {
//            Log.i("tag", "eybName : " + stru.NameEyb);
//        }
//
//    }


}
