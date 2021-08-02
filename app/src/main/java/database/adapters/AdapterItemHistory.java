package database.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import database.structs.dtoItemValues;
import database.structs.dtoItems;
import database.structs.dtoRemarks;
import enums.ItemValueType;
import ir.saa.android.datalogger.FragmentItemHistory;
import ir.saa.android.datalogger.G;
import ir.saa.android.datalogger.R;
import mycomponents.MyCheckList;
import mycomponents.MyCheckListItem;
import mycomponents.MyCheckListMode;
import mycomponents.MyDialog;
import mycomponents.MyToast;
import mycomponents.MyUtilities;


public class AdapterItemHistory extends ArrayAdapter<dtoItemValues> {

    public AdapterItemHistory(ArrayList<dtoItemValues> array) {
        super(G.context, R.layout.custom_item_history, array);
    }

    private static class ViewHolder {
        public ViewGroup layoutRoot;
		public TextView txtRowNumberTextHistory;
		public TextView  txtDateTextHistory;
		public TextView  txtTimeTextHistory;
		public TextView txtValueTextHistory;
		int TextSize;
		Typeface tf ;
    	MyDialog dialog ;

        public ViewHolder(View view) {
			dialog = new MyDialog(G.currentActivity);
            layoutRoot = (ViewGroup) view.findViewById(R.id.layoutRootItemHistory);
			txtRowNumberTextHistory = (TextView) view.findViewById(R.id.txtRowNumberTextHistory);
			txtRowNumberTextHistory.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
			txtDateTextHistory = (TextView) view.findViewById(R.id.txtDateTextHistory);
			txtDateTextHistory.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
			txtTimeTextHistory = (TextView) view.findViewById(R.id.txtTimeTextHistory);
			txtTimeTextHistory.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
			txtValueTextHistory = (TextView) view.findViewById(R.id.txtValueTextHistory);
			txtValueTextHistory.setTextSize(TypedValue.COMPLEX_UNIT_SP,G.fontSize);
			TextSize=(int)G.fontSize;
			if(G.RTL){
				tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
				//TextSize=15;
			}else{
				tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
			}
			txtRowNumberTextHistory.setTypeface(tf);
			txtDateTextHistory.setTypeface(tf);
			txtDateTextHistory.setTextSize(TextSize);
			txtTimeTextHistory.setTypeface(tf);
			txtTimeTextHistory.setTextSize(TextSize);
			txtValueTextHistory.setTypeface(tf);
			txtValueTextHistory.setTextSize(TextSize);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		public void fill(final ArrayAdapter<dtoItemValues> adapter, final dtoItemValues item, final int position) {
			txtRowNumberTextHistory.setText(String.valueOf(position+1));
			txtDateTextHistory.setText(item.PDate);
			txtTimeTextHistory.setText(item.PTime);
			txtValueTextHistory.setText(item.ItemVal);

			if(position%2==1){
				layoutRoot.setBackgroundColor(Color.parseColor("#eeeeee"));
			}else{
				layoutRoot.setBackgroundColor(Color.parseColor("#ffffff"));
			}

			dtoItems oItem = G.DB.GetItemByItemInfId(item.ItemInfID);
			if (oItem.AmountTypID == ItemValueType.Remark) {
				txtValueTextHistory.setText(G.context.getResources().getText(R.string.DisplayValues));
				dialog.clearAllPanel();
				final ArrayList<dtoRemarks> remarks = G.DB.getRemarksByRemarkGroupId(oItem.RemGroupID);
				final MyCheckList myRemarkcheckList = new MyCheckList(G.context, new MyCheckListItem("", "",false),new MyCheckListItem("", "",false));
				if(remarks.size()>0){
					myRemarkcheckList.removeAllCkeckItems();
					for(int i=0;i<remarks.size();i++){
						myRemarkcheckList.addCheckItem(new MyCheckListItem(remarks.get(i).RemName,remarks.get(i).RemID.toString(),false));
					}
					myRemarkcheckList
							.setReadOnly(true)
							.setCheckListOrientation(LinearLayout.VERTICAL)
							.setSelectionMode(MyCheckListMode.MultiSelection)
							.setCheckItemsHeight(60);
					String strDateTimeTitle="";
					if(G.RTL){
						strDateTimeTitle=item.PTime+ " "+item.PDate+" ";
					}else{
						strDateTimeTitle=item.PDate+" "+item.PTime;
					}
					dialog.setTitle(strDateTimeTitle).addContentView(myRemarkcheckList);

					OnClickListener ocl = new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.show();
						}
					};
					layoutRoot.setOnClickListener(ocl);
					txtValueTextHistory.setOnClickListener(ocl);
				}else{
					OnClickListener ocl = new OnClickListener() {
						@Override
						public void onClick(View v) {
							MyToast.Show(G.context,
									(String) G.context.getResources().getText(R.string.NoDataRemark), Toast.LENGTH_SHORT);
						}
					};
					layoutRoot.setOnClickListener(ocl);
					txtValueTextHistory.setOnClickListener(ocl);
				}
				dialog.addButton((String) G.context.getResources().getText(R.string.Close), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});

				//Load if has value
				if(item.ItemVal != null && item.ItemVal.trim().length()>0){
					myRemarkcheckList.setSelectionByValue(getRemarkIdsFromString(item.ItemVal));
				}

			}
			else if (oItem.AmountTypID == ItemValueType.Mamooli) {
				if(item.ItemVal.equals("")==false) {
					txtValueTextHistory.setText(item.ItemVal);
					if(item.RemValues.contains(",")) {
						txtValueTextHistory.setText(item.ItemVal +"\n"+ G.context.getResources().getText(R.string.DisplayRemark));
					}
					dialog.clearAllPanel();
					final ArrayList<dtoRemarks> remarks = G.DB.getRemarksByRemarkGroupId(oItem.RemGroupID);
					final MyCheckList myRemarkcheckList = new MyCheckList(G.context, new MyCheckListItem("", "",false),new MyCheckListItem("", "",false));
					if(remarks.size()>0){
						myRemarkcheckList.removeAllCkeckItems();
						for(int i=0;i<remarks.size();i++){
							myRemarkcheckList.addCheckItem(new MyCheckListItem(remarks.get(i).RemName,remarks.get(i).RemID.toString(),false));
						}
						myRemarkcheckList
								.setReadOnly(true)
								.setCheckListOrientation(LinearLayout.VERTICAL)
								.setSelectionMode(MyCheckListMode.MultiSelection)
								.setCheckItemsHeight(60);
						dialog.setTitle(item.PDate+" "+item.PTime).addContentView(myRemarkcheckList);

						OnClickListener ocl = new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(!item.RemValues.equals("")) {
									dialog.show();
								}
							}
						};
						layoutRoot.setOnClickListener(ocl);
						txtValueTextHistory.setOnClickListener(ocl);
					}else{
						OnClickListener ocl = new OnClickListener() {
							@Override
							public void onClick(View v) {
								MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.NoDataRemark), Toast.LENGTH_SHORT);
							}
						};
						layoutRoot.setOnClickListener(ocl);
						txtValueTextHistory.setOnClickListener(ocl);
					}
					dialog.addButton((String) G.context.getResources().getText(R.string.Close), new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					//Load if has value
					if(item.RemValues != null && item.ItemVal.trim().length()>0){
						myRemarkcheckList.setSelectionByValue(getRemarkIdsFromString(item.RemValues));
					}
				}
			}


        }

		private static ArrayList<String> getRemarkIdsFromString(String strRemarks){
			ArrayList<String> lst = new ArrayList<String>();
			for(String remarkId : strRemarks.split(",")){
				lst.add(remarkId);
			}
			return lst;
		}

		private static String getValidDigit(String strDigit){
			String res = "";
			if(strDigit != null && strDigit.trim().length()>0 && MyUtilities.isNumber(strDigit)){
				if(strDigit.contains(".")){
					strDigit = strDigit.replace(".", ",");
					if(strDigit.trim().split(",")[1].length()>2){
						if(Integer.valueOf(strDigit.trim().split(",")[1].substring(0, 2))==0){
							res = strDigit.trim().split(",")[0].trim();
						}else{
							DecimalFormat decimalforamt = new DecimalFormat("#0.00");
							res = decimalforamt.format(Double.valueOf(strDigit.replace(",", ".").trim()));
						}
					}else{
						if(Integer.valueOf(strDigit.trim().split(",")[1])==0){
							res = strDigit.trim().split(",")[0].trim();
						}else{
							DecimalFormat decimalforamt = new DecimalFormat("#0.00");
							res = decimalforamt.format(Double.valueOf(strDigit.replace(",", ".").trim()));
						}
					}
				}else{
					res = strDigit.trim();
				}
			}
			return res;
		}

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
		dtoItemValues item = getItem(position);

        if (convertView == null) {
            convertView = G.inflater.inflate(R.layout.custom_item_history, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(this, item, position);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
    	// TODO Auto-generated method stub
    	return super.getItemViewType(position);
    }

 // Filter Class
 	public static void filter(String charText) {

// 		charText = charText.toLowerCase(Locale.getDefault());
		FragmentItemHistory.arrListItemFiltered.clear();
		for (dtoItemValues item : FragmentItemHistory.arrListItem)
		{
			if (	item.ItemVal.toLowerCase().contains(charText.toLowerCase()) ||
					item.PDate.toLowerCase().contains(charText.toLowerCase()) ||
					item.PTime.toLowerCase().contains(charText.toLowerCase()))
			{
				FragmentItemHistory.arrListItemFiltered.add(item);
			}
		}
		FragmentItemHistory.adapterItem.notifyDataSetChanged();
 	}

	public static void filterDateTime(String fromDate,String fromTime,String toDate,String toTime) {
		FragmentItemHistory.arrListItemFiltered.clear();

		if(fromTime.trim().length()==0 || toTime.trim().length()==0){
			fromTime = "0000";
			toTime="2359";
			if(fromDate.trim().length()==0 || toDate.trim().length()==0){
				FragmentItemHistory.arrListItemFiltered.clear();
				FragmentItemHistory.arrListItemFiltered.addAll(FragmentItemHistory.arrListItem);
				FragmentItemHistory.adapterItem.notifyDataSetChanged();
				return;
			}
		}
		if(fromTime.length()>=6){
			fromTime=fromTime.substring(0,4);
		}
		if(toTime.length()>=6){
			toTime=toTime.substring(0,4);
		}
		Long fromDateTime = Long.parseLong((fromDate+fromTime).trim());
		Long toDateTime = Long.parseLong((toDate+toTime).trim());
		for (dtoItemValues item : FragmentItemHistory.arrListItem)
		{
			String thePdate,thePtime ;
			if(item.PDate ==null || item.PDate.trim().length()==0){
				continue;
			}
			thePdate = item.PDate.replace(":","").replace("/","").trim();
			if(item.PTime.length()>=8) {
				thePtime = item.PTime.replace(":", "").replace("/", "").trim().substring(0,4);
			}else{
				thePtime = item.PTime.replace(":", "").replace("/", "").trim();
			}
			if(item.PTime ==null || item.PTime.trim().length()==0){
				thePtime = "0000";
			}
			Long theDateTime ;
			if(fromDate.trim().length()==0 || toDate.trim().length()==0){
				theDateTime = Long.parseLong(thePtime.replace(":","").trim());
			}else{
				theDateTime = Long.parseLong(thePdate.replace("-","")
						.trim()+thePtime.replace(":","").trim());
			}
			if (theDateTime>= fromDateTime && theDateTime <= toDateTime )
			{
				FragmentItemHistory.arrListItemFiltered.add(item);
			}
		}
		FragmentItemHistory.adapterItem.notifyDataSetChanged();
	}
}
