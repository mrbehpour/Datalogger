package ir.saa.android.datalogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import database.structs.dtoEquipments;
import database.structs.dtoItemValues;
import database.structs.dtoItems;
import database.structs.dtoLogShits;
import database.structs.dtoPosts;
import database.structs.dtoSubEquipments;
import database.structs.dtoTajhiz;
import enums.ValidForSubmitType;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import static android.content.Context.WINDOW_SERVICE;

public class FragmentTajhizat1 extends FragmentEnhanced {

	private ListView list;
	EditText edtFocuse;
	private AdapterTajhizat1 adapter;
	private int PostId;
    int counter=1;
	private ArrayList<dtoTajhiz> arrayTajhiz1 = new ArrayList<dtoTajhiz>();

	public void adjustFontScale(Configuration configuration, Float fontSize) {

		configuration.fontScale = (float) fontSize;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		metrics.scaledDensity = configuration.fontScale * metrics.density;
		getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.fragment_tajhizat_1, container, false);

		G.context.setTheme(R.style.AppTheme50dp);
		if (G.RTL == false) {
			adjustFontScale(getResources().getConfiguration(), (float) 1.5);
		}
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		list = (ListView) view.findViewById(R.id.listTajhiz1);

		//backupDatabase();
		populateFromDatabase();

		list.setOnKeyListener(new View.OnKeyListener() {
			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {


                    if( keyCode == KeyEvent.KEYCODE_DPAD_CENTER)  {
                        if (  list.onKeyUp(keyCode,event) ){

                            dtoTajhiz item = (dtoTajhiz) list.getSelectedItem();
                            if (G.currentUser.NeedTag == 0 && G.Setting.ModTag == 2 && item.HasTag && G.Setting.LayerTag == 1) {
                                ((ActivityDrawer) G.currentActivity).nfcLogshit = item;
                                ((ActivityDrawer) G.currentActivity).ShowNfcDialog();

                            }

                            AdapterTajhizat1.GoToNextTajhizPage(item);
                        }
                    }

				return false;
			}
		});


		adapter = new AdapterTajhizat1(arrayTajhiz1);
		list.setAdapter(adapter);
        list.requestFocus();

		//populateFakeData();
	}

	private void backupDatabase() {
		try {
			File sd = Environment.getExternalStorageDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "/data/data/ir.saa.android.datalogger/databases/dl.db";
				String backupDBPath = "/dl.db";
				//String backupDBPath = String.format("/SAMiN_Backup_%s.db",PersianCalendar.getCurrentShamsiDate());
				File currentDB = new File(currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					//ShowToast.crateToast(G.context, G.get_string(R.string.backup_ok), ShowToast.State.Successfull);
					//CustomToast.create_toast(G.get_string(R.string.backup_ok),2500, CustomToast.State.Successful);
				}
			}
		} catch (Exception e) {

		}
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	private void populateFromDatabase() {
		arrayTajhiz1.clear();

		Integer selectedId = null;

		Bundle bundle = this.getArguments();
		if (bundle != null) {
			int id = bundle.getInt("ID", -1);
			if (id > 0) {
				selectedId = id;
			}

		}

//		ArrayList<dtoItemValues> dtoItemValuesList=G.DB.getItemValuesByUserId(G.currentUser.UsrID);
//		ArrayList<dtoItems> dtoItemsArrayList=new ArrayList<>();
//		for(dtoItemValues itemValues:dtoItemValuesList){
//			 dtoItems items=G.DB.GetItemByItemInfId(itemValues.ItemInfID);
//			 if(items!=null){
//				 dtoItemsArrayList.add(items);
//			 }
//		}

		if(G.selectedMenuItemType == G.MenuItemTypes.VAHED){

			for (dtoPosts post : G.DB.GetVAHEDList()) {
				dtoTajhiz tajhiz = new dtoTajhiz();
				tajhiz.ID = post.PostID;
				tajhiz.Name = post.PostName;
				tajhiz.Description = post.Des;
//				ArrayList<dtoItems> items = G.DB.getItemsByUserIdAndPostId(G.currentUser.UsrID, post.PostID);
//				for (dtoItems item : items) {
//					//dtoItems items=G.DB.GetItemByItemInfId(itemval.ItemInfID);
//					if (item.PostID != null) {
//						tajhiz.IsFillItem = true;
//						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmitWitoutCheckSetting(item);
//						if (IsValidForSubmit == ValidForSubmitType.InRange || IsValidForSubmit == ValidForSubmitType.OutOfRange) {
//							tajhiz.IsFillItem = false;
//						} else {
//							tajhiz.IsFillItem = true;
//							break;
//						}
//
//					} else {
//						if (!tajhiz.IsFillItem) {
//							tajhiz.IsFillItem = false;
//						}
//					}
//				}

				int c = G.DB.GetSavedItemCount(post.PostID,-1,-1,-1,-1);
				if(c>0) tajhiz.IsFillItem=true;


				arrayTajhiz1.add(tajhiz);

			}
		}
		else if(G.selectedMenuItemType == G.MenuItemTypes.LOGSHIT){
			for (dtoLogShits logshit : G.DB.GetLogshitListByPostId(selectedId)){
				dtoTajhiz tajhiz = new dtoTajhiz();
				tajhiz.ID = logshit.LogshitInfID;
				tajhiz.Name = logshit.LogshitName;
				tajhiz.Description = logshit.Des;
				G.PostId=selectedId;
				if(G.currentUser.IsManager!=1 && (logshit.TagID.trim().compareTo("NONE")!=0) && logshit.TagID !=null && logshit.TagID.trim().compareTo("null")!=0 && logshit.TagID.trim().length()>0 && G.selectedMenuItemMode == G.MenuItemMode.Sabt){
					tajhiz.HasTag = true;
					tajhiz.TagId = logshit.TagID;
				}
				//				ArrayList<dtoItemValues> itemsValue=G.DB.getItemValuesByUserIdAndLogshit(G.currentUser.UsrID,logshit.LogshitInfID,selectedId);
//				//dtoItems items=G.DB.GetItemBylogsheetId(logshit.PostID,logshit.LogshitInfID,G.currentUser.UsrID);
//				for (dtoItemValues itemval:itemsValue) {
//					dtoItems items=G.DB.GetItemByItemInfId(itemval.ItemInfID);
//					if(items.LogshitInfID!=null) {
//
//						tajhiz.IsFillItem=true;
//						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmitWitoutCheckSetting(items);
//						if (IsValidForSubmit == ValidForSubmitType.InRange || IsValidForSubmit == ValidForSubmitType.OutOfRange) {
//							tajhiz.IsFillItem = false;
//						}else{
//							tajhiz.IsFillItem = true;
//							break;
//						}
//
//					}else {
//						if(!tajhiz.IsFillItem){
//							tajhiz.IsFillItem=false;
//						}
//					}
//				}
				int c = G.DB.GetSavedItemCount(logshit.PostID,logshit.LogshitInfID,-1,-1,-1);
				if(c>0) tajhiz.IsFillItem=true;


				arrayTajhiz1.add(tajhiz);

			}
		}
		else if(G.selectedMenuItemType == G.MenuItemTypes.TAJHIZ){
			for (dtoEquipments equip : G.DB.GetEquipmentListByLogshitInfId(selectedId,G.PostId)){
				dtoTajhiz tajhiz = new dtoTajhiz();
				tajhiz.ID = equip.EquipInfoID;
				tajhiz.Name = equip.EquipName;
				tajhiz.Description = equip.Des;
				//				ArrayList<dtoItemValues> itemsValue=G.DB.getItemValuesByUserIdAndTajhizId(G.currentUser.UsrID,equip.EquipInfoID,selectedId);
//				for (dtoItemValues itemval:itemsValue) {
//					dtoItems items=G.DB.GetItemByItemInfId(itemval.ItemInfID);
//					if(items.EquipInfID!=null){
//
//						tajhiz.IsFillItem=true;
//						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmitWitoutCheckSetting(items);
//						if (IsValidForSubmit == ValidForSubmitType.InRange || IsValidForSubmit == ValidForSubmitType.OutOfRange) {
//							tajhiz.IsFillItem = false;
//						}else{
//							tajhiz.IsFillItem = true;
//							break;
//						}
//
//
//					}else {
//						if(!tajhiz.IsFillItem){
//							tajhiz.IsFillItem=false;
//						}
//					}
//				}
				int c = G.DB.GetSavedItemCount(-1,-1,equip.EquipInfoID,-1,-1);
				if(c>0) tajhiz.IsFillItem=true;
				arrayTajhiz1.add(tajhiz);
			}
		}
		else if(G.selectedMenuItemType == G.MenuItemTypes.ZIRTAJHIZ){
			for (dtoSubEquipments subEquip : G.DB.GetSubEquipmentList(selectedId)){
				dtoTajhiz tajhiz = new dtoTajhiz();
				tajhiz.ID = subEquip.SubEquipID;
				tajhiz.Name = subEquip.SubEquipName;
				tajhiz.Description = subEquip.Des;
				//				ArrayList<dtoItemValues> itemsValue=G.DB.getItemValuesByUserIdAndZirTajhizId(G.currentUser.UsrID,subEquip.SubEquipID,selectedId,G.selectedLogshitId);
//
//				for (dtoItemValues itemval:itemsValue) {
//					dtoItems items=G.DB.GetItemByItemInfId(itemval.ItemInfID);
//					if(items.SubEquipID!=null){
//
//						tajhiz.IsFillItem=true;
//						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmitWitoutCheckSetting(items);
//						if (IsValidForSubmit == ValidForSubmitType.InRange || IsValidForSubmit == ValidForSubmitType.OutOfRange) {
//							tajhiz.IsFillItem = false;
//						}else{
//							tajhiz.IsFillItem = true;
//							break;
//						}
//
//					}else {
//						if(!tajhiz.IsFillItem){
//							tajhiz.IsFillItem=false;
//						}
//					}
//				}

				int c = G.DB.GetSavedItemCount(-1,-1,subEquip.EquipInfID,subEquip.SubEquipID,-1);
				if(c>0) tajhiz.IsFillItem=true;

				arrayTajhiz1.add(tajhiz);
			}
		}



//		Cursor cur = null;
//		if(G.selectedMenuItemType == G.MenuItemTypes.VAHED){
//			cur = G.DB.GetVAHEDList();
//		}
//		else if(G.selectedMenuItemType == G.MenuItemTypes.LOGSHIT){
//			cur = G.DB.GetTajhizat1listRT(G.selectedMenuItemType);
//		}

//		while (cur.moveToNext()) {
//            dtoPosts tajhizAsli = new dtoPosts();
//
//            tajhizAsli.id = cur.getString(cur.getColumnIndex(TableViewInfo.CodeTajhizAsli));
//            tajhizAsli.tajhizName = cur.getString(cur.getColumnIndex(TableViewInfo.NameTajhizAsli));
//            tajhizAsli.keyZamanBandi = cur.getString(cur.getColumnIndex(TableViewInfo.KeyZambandi));
//            tajhizAsli.tozihat = String.format("%s : %s", "آدرس",cur.getString(cur.getColumnIndex(TableViewInfo.AddressTajhizAsli))) ;
//
//            arrayTajhiz1.add(tajhizAsli);
//        }
//		cur.close();

		//adapter.notifyDataSetChanged();
	}


//    private void populateFakeData() {
////        for (int i = 0; i < 50; i++) {
////            StructTajhizat1 tajhiz1 = new StructTajhizat1();
////            tajhiz1.id = "guid" + i; // temporary fake guid
////            tajhiz1.tajhizName = "تجهیز " + i;
////            tajhiz1.tozihat = "توضیحاتی برای تجهیز " + i;
////
////            arrayTajhiz1.add(tajhiz1);
////        }
//    	arrayTajhiz1.clear();
//    	StructTajhizat1 tajhiz1 = new StructTajhizat1();
//        tajhiz1.id = "post1"; // temporary fake guid
//        tajhiz1.tajhizName = "پست هوایی وحدت";
//        tajhiz1.tozihat = "پست هوایی وحدت";
//        arrayTajhiz1.add(tajhiz1);
//        
//
//    	StructTajhizat1 tajhiz2 = new StructTajhizat1();
//        tajhiz2.id = "post2"; // temporary fake guid
//        tajhiz2.tajhizName = "پست هوایی هاشمی";
//        tajhiz2.tozihat = "پست هوایی هاشمی";
//        arrayTajhiz1.add(tajhiz2);
//        
//        adapter.notifyDataSetChanged();
//    }


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		LinearLayout ll = (LinearLayout) G.actionBar.getCustomView().findViewById(R.id.appBar2);
		ll.setVisibility(View.GONE);
		Log.i("tag", "visible Home fragment");
	}
}
