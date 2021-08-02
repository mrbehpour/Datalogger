package ir.saa.android.datalogger;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import database.adapters.AdapterItemHistory;
import database.structs.dtoItemValues;
import database.structs.dtoItems;
import database.structs.dtoLogics;
import enums.ItemValueType;
import mycomponents.MaskedEditText;
import mycomponents.MyDialog;
import mycomponents.MyLabel;
import mycomponents.MyToast;
import mycomponents.MyUtilities;

import static android.content.Context.WINDOW_SERVICE;

public class FragmentItemHistory extends FragmentEnhanced {

	ListView listItem ;
	public static EditText edtSearch;
	public static ArrayList<dtoItemValues> arrListItem = new ArrayList<dtoItemValues>();
	public static ArrayList<dtoItemValues> arrListItemFiltered = new ArrayList<dtoItemValues>();
	public static AdapterItemHistory adapterItem;
	Typeface tf;
	MyDialog filterDialog = null;
	static Integer selectedItemInfId = -1;
	LinearLayout llMinMaxReport;

	LinearLayout llMinReport;
	LinearLayout llMaxReport;

	MyLabel lblMinValue ;
	MyLabel lblMaxValue;

	int layoutFilter;
	@Override
	public void onResume() {
		adapterItem.notifyDataSetChanged();
		AdapterItemHistory.filter(edtSearch.getText().toString());
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.fragment_item_history, container, false);
		layoutFilter=R.layout.body_item_from_datetime_to_datetime;

		if(G.RTL){
			llMinMaxReport = rootView.findViewById(R.id.llMinMaxReport);
			tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");


		}else{
			adjustFontScale(getResources().getConfiguration(),(float)0.85);
			tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
			llMaxReport=rootView.findViewById(R.id.llMaxReport);
			llMinReport=rootView.findViewById(R.id.llMinReport);
		}
		return rootView;
	}

	@Override
	public void onDestroy() {
		G.llMenu.setVisibility(View.VISIBLE);
		super.onDestroy();
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("UseSparseArrays")
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		//-----------Get Bundle ItemInfId------------
		filterDialog= new MyDialog(G.currentActivity);
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			int seid = bundle.getInt("ID",-1);
			if(seid>0){
				selectedItemInfId = seid;
			}
		}
		//-------------------------------------------

		G.mDrawerLayout.setEnabled(false);

		TextView txtRowNumberTitleReport = (TextView) view.findViewById(R.id.txtRowNumberTitleReport);
		txtRowNumberTitleReport.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		TextView txtDateTitleReport = (TextView) view.findViewById(R.id.txtDateTitleReport);
		txtDateTitleReport.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		TextView txtTimeTitleReport = (TextView) view.findViewById(R.id.txtTimeTitleReport);
		txtTimeTitleReport.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		TextView txtValueTitleReport = (TextView) view.findViewById(R.id.txtValueTitleReport);
		txtValueTitleReport.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		listItem = (ListView) view.findViewById(R.id.lstItemHistory);
		edtSearch = (EditText) view.findViewById(R.id.edtSearchItemHistory);
		edtSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		ImageView imgFilterHistory = (ImageView) view.findViewById(R.id.imgFilterHistory);
		TextView txtFilterHistory = (TextView) view.findViewById(R.id.txtFilterHistory);
		//txtFilterHistory
		LinearLayout llFilterHistory = (LinearLayout) view.findViewById(R.id.llFilterHistory);

		txtRowNumberTitleReport.setTypeface(tf);
		txtDateTitleReport.setTypeface(tf);

		txtTimeTitleReport.setTypeface(tf);

		txtValueTitleReport.setTypeface(tf);
		txtFilterHistory.setTypeface(tf);
		edtSearch.setTypeface(tf);

		adapterItem = new AdapterItemHistory(arrListItemFiltered);
		listItem.setAdapter(adapterItem);

		//-----------Filter Dialog-------------
		filterDialog.clearAllPanel();
		filterDialog.setTitle((String) getResources().getText(R.string.FilterReports))
				.setBackgroundAlpha(0.95f)
				.setVerticalMargin(5)
				.addContentXml(layoutFilter);



		final MaskedEditText edtItemFromDate = (MaskedEditText) filterDialog.getDialog().findViewById(R.id.edtItemFromDate);
		edtItemFromDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		final MaskedEditText edtItemToDate = (MaskedEditText) filterDialog.getDialog().findViewById(R.id.edtItemToDate);
		edtItemToDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		final MaskedEditText edtItemFromTime = (MaskedEditText) filterDialog.getDialog().findViewById(R.id.edtItemFromTime);
		edtItemFromTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		final MaskedEditText edtItemToTime = (MaskedEditText) filterDialog.getDialog().findViewById(R.id.edtItemToTime);
		edtItemToTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		TextView txtItemFromDate = (TextView) filterDialog.getDialog().findViewById(R.id.txtItemFromDate);
		txtItemFromDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		TextView txtItemToDate = (TextView) filterDialog.getDialog().findViewById(R.id.txtItemToDate);
		txtItemToDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		TextView txtItemFromTime = (TextView) filterDialog.getDialog().findViewById(R.id.txtItemFromTime);
		txtItemFromTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
		TextView txtItemToTime = (TextView) filterDialog.getDialog().findViewById(R.id.txtItemToTime);
		txtItemToTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);

		edtItemFromDate.setInputType(InputType.TYPE_CLASS_NUMBER);
		edtItemToDate.setInputType(InputType.TYPE_CLASS_NUMBER);
		edtItemFromTime.setInputType(InputType.TYPE_CLASS_NUMBER);
		edtItemToTime.setInputType(InputType.TYPE_CLASS_NUMBER);
if(G.RTL) {
	filterDialog
			.addButton((String) G.context.getText(R.string.DeleteFilter), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//MyToast.Show(G.context,edtItemFromDate.getText().toString()+","+edtItemToDate.getText().toString()+","+edtItemFromTime.getText().toString()+","+edtItemToTime.getText().toString(),Toast.LENGTH_SHORT);
					edtItemFromDate.setText("");
					edtItemFromTime.setText("");
					edtItemToDate.setText("");
					edtItemToTime.setText("");
				}
			})
			.addButton((String) G.context.getText(R.string.Filter), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String fromDate = edtItemFromDate.getText().toString().replace(":", "").replace("/", "").trim();
					String fromTime = edtItemFromTime.getText().toString().replace(":", "").replace("/", "").trim();
					String toDate = edtItemToDate.getText().toString().replace(":", "").replace("/", "").trim();
					String toTime = edtItemToTime.getText().toString().replace(":", "").replace("/", "").trim();

					if ((fromDate.length() == 0 && toDate.length() == 0) || (fromDate.length() == 8 && toDate.length() == 8)) {

					} else {
						MyToast.Show(G.context, (String) G.context.getText(R.string.Msg_ForceFilterField), Toast.LENGTH_LONG);
						return;
					}

					AdapterItemHistory.filterDateTime(fromDate, fromTime, toDate, toTime);
					filterDialog.dismiss();
					refreshMinMaxReport();
				}
			})
			.addButton((String) G.context.getText(R.string.Close), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					filterDialog.dismiss();
				}

			});
}else{
	filterDialog
			.addButtonL((String) G.context.getText(R.string.DeleteFilter), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//MyToast.Show(G.context,edtItemFromDate.getText().toString()+","+edtItemToDate.getText().toString()+","+edtItemFromTime.getText().toString()+","+edtItemToTime.getText().toString(),Toast.LENGTH_SHORT);
					edtItemFromDate.setText("");
					edtItemFromTime.setText("");
					edtItemToDate.setText("");
					edtItemToTime.setText("");
				}
			})
			.addButtonR((String) G.context.getText(R.string.Filter), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String fromDate = edtItemFromDate.getText().toString().replace(":", "").replace("/", "").trim();
					String fromTime = edtItemFromTime.getText().toString().replace(":", "").replace("/", "").trim();
					String toDate = edtItemToDate.getText().toString().replace(":", "").replace("/", "").trim();
					String toTime = edtItemToTime.getText().toString().replace(":", "").replace("/", "").trim();

					if ((fromDate.length() == 0 && toDate.length() == 0) || (fromDate.length() == 8 && toDate.length() == 8)) {

					} else {
						MyToast.Show(G.context, (String) G.context.getText(R.string.Msg_ForceFilterField), Toast.LENGTH_LONG);
						return;
					}

					AdapterItemHistory.filterDateTime(fromDate, fromTime, toDate, toTime);
					filterDialog.dismiss();
					refreshMinMaxReport();
				}
			})
			.addButtonL((String) G.context.getText(R.string.Close), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					filterDialog.dismiss();
				}

			});
}

		edtItemFromDate.setTypeface(tf);
		edtItemToDate.setTypeface(tf);
		edtItemFromTime.setTypeface(tf);
		edtItemToTime.setTypeface(tf);
		txtItemFromDate.setTypeface(tf);
		txtItemToDate.setTypeface(tf);
		txtItemFromTime.setTypeface(tf);
		txtItemToTime.setTypeface(tf);
		//-------------------------------------
		View.OnClickListener filterClick = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				filterDialog.show();
			}
		};

		llFilterHistory.setOnClickListener(filterClick);
		txtFilterHistory.setOnClickListener(filterClick);
		imgFilterHistory.setOnClickListener(filterClick);

		edtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String strSearch = String.valueOf(s);
				AdapterItemHistory.filter(strSearch);
				refreshMinMaxReport();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {			}

			@Override
			public void afterTextChanged(Editable s) {			}
		});

		populateFromDatabase();
//		populateFakeData();
		if(G.RTL) {
			lblMinValue = new MyLabel(G.context, (String) G.context.getText(R.string.MinValue),"-", (int)(G.fontSize-5), (int)(G.fontSize-5));
			lblMaxValue = new MyLabel(G.context,  (String) G.context.getText(R.string.MaxValue),"-", (int)(G.fontSize-5), (int)(G.fontSize-5));
			lblMinValue.setPadding(0,0,20,0);
			lblMaxValue.setPadding(0,0,20,0);

		}else{
			lblMinValue = new MyLabel(G.context, "-", (String) G.context.getText(R.string.MinValue), (int)(G.fontSize-5), (int)(G.fontSize-5));
			lblMaxValue = new MyLabel(G.context, "-", (String) G.context.getText(R.string.MaxValue), (int)(G.fontSize-5), (int)(G.fontSize-5));
		}


		lblMinValue.setBackgroundColor(Color.parseColor("#2593d0"));

		lblMaxValue.setBackgroundColor(Color.parseColor("#e63f29"));
		if(G.RTL) {
			llMinMaxReport.addView(lblMaxValue);
			llMinMaxReport.addView(lblMinValue);
		}else
		{
			llMinReport.addView(lblMinValue);
			llMaxReport.addView(lblMaxValue);
		}

		refreshMinMaxReport();

		super.onViewCreated(view, savedInstanceState);
	}

	public void refreshMinMaxReport() {
		dtoItems dtoItem = G.DB.GetItemByItemInfId(selectedItemInfId);
		Integer amountTypId = dtoItem.AmountTypID;
		if ((amountTypId == ItemValueType.Mamooli || amountTypId == ItemValueType.Meghdari) && arrListItemFiltered.size()>0) {
			Double maxValue = null;
			Double minValue = null;
			for(dtoItemValues item:arrListItemFiltered) {
				if(MyUtilities.isNumber(item.ItemVal)){
					if(maxValue==null){
						minValue = maxValue = Double.valueOf(item.ItemVal);
					}else {
						if (minValue > Double.valueOf(item.ItemVal)) {
							minValue = Double.valueOf(item.ItemVal);
						}
						if (maxValue < Double.valueOf(item.ItemVal)) {
							maxValue = Double.valueOf(item.ItemVal);
						}
					}
				}
			}
			if(maxValue!=null && maxValue!=null) {
				if(G.RTL){
					lblMinValue.setRightText(MyUtilities.getValidDigit(String.valueOf(minValue)));
					lblMaxValue.setRightText(MyUtilities.getValidDigit(String.valueOf(maxValue)));
				}else {
					lblMinValue.setLeftText(MyUtilities.getValidDigit(String.valueOf(minValue)));
					lblMaxValue.setLeftText(MyUtilities.getValidDigit(String.valueOf(maxValue)));
				}
			}else{
				if(G.RTL) {
					llMinMaxReport.setVisibility(View.GONE);
				}else
				{
					lblMinValue.setVisibility(View.GONE);
					lblMaxValue.setVisibility(View.GONE);
				}
			}
		}else{
			if(G.RTL) {
				llMinMaxReport.setVisibility(View.GONE);
			}else
			{
				lblMinValue.setVisibility(View.GONE);
				lblMaxValue.setVisibility(View.GONE);
			}
		}
	}

	private void populateFromDatabase() {
		arrListItem.clear();
		arrListItemFiltered.clear();

		ArrayList<dtoItemValues> lstItems = G.DB.getItemValuesByItemInfoIdHistory(selectedItemInfId);//G.DB.getItemValuesByUserIdAndItemInfId(G.currentUser.UsrID, selectedItemInfId);
		//arrListItem.addAll(lstItems);
		String DateRegister="";
		String TimeRegister="";
		String ItemValue="";
		for(dtoItemValues item:lstItems){

			dtoItems dtoItem = G.DB.GetItemByItemInfId(item.ItemInfID);
			Integer amountTypId = dtoItem.AmountTypID;
			if(G.RTL){

				if (amountTypId == ItemValueType.DateTime) {
					if(item.ItemVal.split(" ").length==2) {
						ItemValue = Tarikh.getShamsiDate(item.ItemVal) + " " + item.ItemVal.split(" ")[1];
					}
				}else if(amountTypId == ItemValueType.Date){
					ItemValue=Tarikh.getShamsiDate(item.ItemVal);

				}else{
					ItemValue=item.ItemVal;
				}
				if(item.PDate!=null) {
					DateRegister = Tarikh.getShamsiDate(item.PDate
							.replace("/", "-")
							);
					TimeRegister = item.PDate.substring(11);
				}else{
					DateRegister = "";
					TimeRegister = "";
				}

			}else{
				ItemValue=item.ItemVal;
				if(item.PDate!=null) {
					DateRegister = item.PDate.substring(0, 10);
					TimeRegister = item.PDate.substring(11);
				}

			}

			item.PTime=TimeRegister;
			item.PDate=DateRegister;
			if (amountTypId == ItemValueType.Meghdari) {
				//Noting
			}else if (amountTypId == ItemValueType.DateTime) {
//				String time = Tarikh.getColonedStringTime(item.ItemVal.trim().substring(0,4));
//				String date = Tarikh.getSlashedStringDate(item.ItemVal.trim().substring(4));
				item.ItemVal =ItemValue;
			}else if (amountTypId == ItemValueType.Time) {
				//item.ItemVal = Tarikh.getColonedStringTime(item.ItemVal.trim());
				item.ItemVal =ItemValue; //item.ItemVal.trim();
			}else if (amountTypId == ItemValueType.Date) {
				//item.ItemVal = Tarikh.getSlashedStringDate(item.ItemVal.trim());
				item.ItemVal =ItemValue; //item.ItemVal.trim();
			}else if (amountTypId == ItemValueType.Logic) {
				dtoLogics logic = G.DB.getLogicByLogicTypeId(dtoItem.LogicTypID);
				item.ItemVal = item.ItemVal.trim().compareTo("1")==0?logic.LogicVal2:logic.LogicVal1;
			}else if (amountTypId == ItemValueType.Remark) {
				// Check in AdapterItemHistory - Click Text and show readonly remark dialog
			}else if (amountTypId == ItemValueType.Mamooli) {
				// Check in AdapterItemHistory - Click Text and show readonly remark dialog
			}else if (amountTypId == ItemValueType.Mohasebati) {
				//Noting
			}
			else{
				//There is no type like this
			}
			arrListItem.add(item);
		}
		arrListItemFiltered.addAll(arrListItem);
        adapterItem.notifyDataSetChanged();
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
