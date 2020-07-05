package ir.saa.android.datalogger;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import database.adapters.AdapterItemReport;
import database.structs.dtoItemValues;
import database.structs.dtoItems;

import static android.content.Context.WINDOW_SERVICE;

public class FragmentItemReport extends Fragment {

	public static int EquipId;
	public static int SubEquipId;

	ListView listItem ; 
	public LinearLayout llSaveItem;
	LinearLayout llDateTimeForItems;
	public static Spinner searchSpinner;
	public static EditText edtSearch;
	public static ArrayList<dtoItems> arrListItem = new ArrayList<dtoItems>();
	public static ArrayList<dtoItems> arrListItemFiltered = new ArrayList<dtoItems>();
	public static Map<Integer,dtoItemValues> dicItemValues ;
	public static AdapterItemReport adapterItem;
	final Typeface tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
	
	@Override
	public void onResume() {
		adapterItem.notifyDataSetChanged();
		AdapterItemReport.filter(edtSearch.getText().toString());
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
				.inflate(R.layout.tab_fragment_item, container, false);

		if(G.RTL==false){
			adjustFontScale(getResources().getConfiguration(),(float)1);
		}
		
		return rootView;
	}

	@Override
	public void onDestroy() {
		G.llMenu.setVisibility(View.VISIBLE);
		super.onDestroy();
	}
	
	@SuppressLint("UseSparseArrays")
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		if(G.isGpsEnabled())
        	G.updateGpsLocation();
        else{
        	G.latitude = "";
        	G.longitude = "";
        }
		G.mDrawerLayout.setEnabled(false);

		llDateTimeForItems = (LinearLayout) view.findViewById(R.id.llDateTimeForItems);
		dicItemValues = new HashMap<Integer,dtoItemValues>();
		listItem = (ListView) view.findViewById(R.id.lstItem);
		llSaveItem =  view.findViewById(R.id.llSaveItem);
		edtSearch = (EditText) view.findViewById(R.id.edtSearchItem);
		searchSpinner = (Spinner) view.findViewById(R.id.searchSpinnerItem);

		llDateTimeForItems.setVisibility(View.GONE);
		llSaveItem.setVisibility(View.GONE);
		searchSpinner.setVisibility(View.GONE);

//		List<String> categories = new ArrayList<String>();
//        categories.add("همه موارد");
//        categories.add("ثبت نشده");
//        categories.add("ثبت شده");
//        //spinner search
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(G.context, R.layout.spinner_item, categories);
//        dataAdapter.setDropDownViewResource(R.layout.spinner_chtxt_item);
        //searchSpinner.setAdapter(dataAdapter);

		adapterItem = new AdapterItemReport(arrListItemFiltered);
		listItem.setAdapter(adapterItem);

//		searchSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//		    @Override
//		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//				AdapterItemReport.filter(edtSearch.getText().toString());
//		    }
//
//		    @Override
//		    public void onNothingSelected(AdapterView<?> parentView) {
//		        // your code here
//		    }
//
//		});
		
		edtSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String strSearch = String.valueOf(s);
				AdapterItemReport.filter(strSearch);
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
		
//		imgSaveItem.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				final MyDialog mydialog = new MyDialog(G.currentActivity);
//
//				mydialog.setTitle("پیام ذخیره سازی")
//					.addContentXml(R.layout.body_save_statistics)
//					.setVerticalMargin(10)
//					.setContentSplitter()
//					.setVerticalMargin(10)
//					.addContentXml(R.layout.body_one_textview)
//					.addButton("خیر", new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							mydialog.dismiss();
//						}
//					})
//					.addButton("بله", new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//
//							for (dtoItems item : arrListItem) {
//								if(!(dicItemValues.get(item.ItemInfID).ItemVal == null || dicItemValues.get(item.ItemInfID).ItemVal.replaceAll("/", "").replaceAll(":", "").replaceAll(",", "").trim().length()==0))
//									G.DB.InsertItemValues(dicItemValues.get(item.ItemInfID));
//							}
//							populateFromDatabase();
//							MyToast.Show(G.context, "اطلاعات آیتم ها با موفقیت ذخیره شد", Toast.LENGTH_LONG);
//							edtSearch.setText("");
//							searchSpinner.setSelection(0);
//							mydialog.dismiss();
//						}
//					})
//					.show();
//
//				TextView txtBodyMessage = (TextView) mydialog.getDialog().findViewById(R.id.txtBodymessage);
//				txtBodyMessage.setText("آیا مایلید اطلاعات ذخیره شود؟");
//				txtBodyMessage.setTypeface(tf);
//				TextView txtSaveAmar1 = (TextView) mydialog.getDialog().findViewById(R.id.txtSaveAmar1);
//				TextView txtSaveAmar2 = (TextView) mydialog.getDialog().findViewById(R.id.txtSaveAmar2);
//				TextView txtSaveAmar3 = (TextView) mydialog.getDialog().findViewById(R.id.txtSaveAmar3);
//				Button btnSaveAmar = (Button) mydialog.getDialog().findViewById(R.id.btnSaveAmar);
//				txtSaveAmar1.setTypeface(tf);
//				txtSaveAmar2.setTypeface(tf);
//				txtSaveAmar3.setTypeface(tf);
//				btnSaveAmar.setTypeface(tf);
//
//				int countSabtNashode = getCountOfNotEnteredItem();
//
//				txtSaveAmar1.setText("تعداد کل آیتم ها : "+arrListItem.size());
//				txtSaveAmar2.setText("تعداد آیتم های ثبت شده : "+(arrListItem.size()-countSabtNashode));
//				txtSaveAmar3.setText("تعداد آیتم های ثبت نشده : "+countSabtNashode);
//
//				btnSaveAmar.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						edtSearch.setText("");
//						searchSpinner.setSelection(1); // sabt nashodeha
//						mydialog.dismiss();
//					}
//				});
//			}
//		});
		
		//if(G.selectedId == null)
		populateFromDatabase();
		listItem.requestFocus();
		listItem.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER){
					if(listItem.onKeyUp(keyCode,event)){
						dtoItems item=(dtoItems) listItem.getSelectedItem();
						//showDialog(item,position);
						//MyToast.Show(G.context,"",Toast.LENGTH_SHORT);
						Fragment currentFragment = G.fragmentManager.findFragmentById(R.id.frame_container);
						if (currentFragment != null) {
							G.fragmentStack.push(currentFragment);
							G.navigationStackTitle.push(((TextView)G.actionBar.getCustomView().findViewById(R.id.txtTitle)).getText().toString());
						}
						TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
						txtTitle.setText(item.ItemName);
						G.selectedMenuItemType = G.MenuItemTypes.ITEMHISTORY;
						Bundle bundle = new Bundle();
						bundle.putInt("ID", item.ItemInfID);
						android.support.v4.app.Fragment fragment =  new FragmentItemHistory();
						fragment.setArguments(bundle);
						G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
					}
				}
				return false;
			}
		});
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


	private void populateFromDatabase() {
		arrListItem.clear();
		arrListItemFiltered.clear();
		dicItemValues.clear();
		Integer selectedEquipId = -1 ;
		Integer selectedSubEquipId = -1;
		Bundle bundle = this.getArguments();
		if (bundle != null) {
		    int eid = bundle.getInt("EquipID",-1);
		    if(eid>=0){
		    	selectedEquipId = eid;
				EquipId = eid;
		    }
		    int seid = bundle.getInt("SubEquipID",-1);
		    if(seid>=0){
		    	selectedSubEquipId = seid;
				SubEquipId = seid;
		    }
		}
		ArrayList<dtoItems> lstItems = G.DB.GetItemList(selectedEquipId, selectedSubEquipId);
		//arrListItem.addAll(lstItems);
		for(dtoItems item:lstItems){

        	if(G.Setting.NoCheckMaxData!=1){
        		ArrayList<dtoItemValues> itemVals = G.DB.getItemValuesByItemInfoId(item.ItemInfID);
        		if(itemVals.size() == item.MaxSampleNo){
        			item.IsFullOfMaxData = true;
        		}
        	}
			
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
