package database.adapters;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import database.structs.dtoItems;
import ir.saa.android.datalogger.FragmentItemHistory;
import ir.saa.android.datalogger.G;
import ir.saa.android.datalogger.R;
import ir.saa.android.datalogger.FragmentItemReport;
import mycomponents.MyDialog;
import mycomponents.MyToast;
import mycomponents.MyUtilities;


public class AdapterItemReport extends ArrayAdapter<dtoItems> {

    public AdapterItemReport(ArrayList<dtoItems> array) {
        super(G.context, R.layout.custom_item, array);
    }

    private static class ViewHolder {
        public ViewGroup layoutRoot;
        public TextView  txtItem;
        public TextView  txtItemDesc;
        public ImageView imgItemInfo;
		public   ViewGroup layoutRoot1;
    	Typeface tf;
//    	Drawable drwGreen;
//    	Drawable drwWhite;
//    	Drawable drwGray;

        public ViewHolder(View view) {
            layoutRoot = (ViewGroup) view.findViewById(R.id.layoutRootItem);
            imgItemInfo = (ImageView) view.findViewById(R.id.imgItemInfo);
            txtItem = (TextView) view.findViewById(R.id.txtItemName);
            txtItemDesc = (TextView) view.findViewById(R.id.txtItemDesc);
			layoutRoot1 = (ViewGroup) view.findViewById(R.id.layoutRootItem1);
            if(G.RTL){
				tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
			}else{
				tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
			}
//            drwGreen = G.context.getResources().getDrawable(drawable.list_item_green_gradient);
//            drwWhite = G.context.getResources().getDrawable(drawable.list_item_eyb_gradient);
//            drwGray =  G.context.getResources().getDrawable(drawable.list_item_gray_gradient);
        	txtItem.setTypeface(tf);
        	txtItemDesc.setTypeface(tf);
        }

        public void fill(final ArrayAdapter<dtoItems> adapter, final dtoItems item, final int position) {
            txtItem.setText(item.ItemName);
            txtItemDesc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MyToast.Show(G.context, item.ItemName, Toast.LENGTH_SHORT);
				}
			});
            imgItemInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showJozeeatDialog(item);
				}
			});
//            if(item.Desc == null || item.Desc.trim().length()==0)
//            {
//            	imgItemInfo.setVisibility(View.INVISIBLE);
//            }else{
//            	imgItemInfo.setVisibility(View.VISIBLE);
//            }

            //setBackgroundColorDependsOnItemVal(item);


            layoutRoot.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
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

					//G.selectedId = item.EquipInfID;
				}
			});
			layoutRoot1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
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

					//G.selectedId = item.EquipInfID;
				}
			});

			txtItem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
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

					//G.selectedId = item.EquipInfID;
				}
			});

        }

//        private void setBackgroundColorDependsOnItemVal(dtoItems item)
//        {
//        	if(FragmentItemReport.dicItemValues.get(item.ItemInfID).ItemVal == null || FragmentItemReport.dicItemValues.get(item.ItemInfID).ItemVal.replaceAll("/", "").replaceAll(":", "").replaceAll(",", "").trim().length()==0){
//            	layoutRoot.setBackgroundDrawable(drwWhite);
//            }else{
//            	layoutRoot.setBackgroundDrawable(drwGreen);
//            }
//        	if(G.Setting.NoCheckMaxData!=1){
//        		if(item.IsFullOfMaxData){
//        			layoutRoot.setBackgroundDrawable(drwGray);
//        		}
//        	}
//        }

		private void showDialog(final dtoItems item,final int pos){

		}

        private void showJozeeatDialog(dtoItems item){
			final MyDialog myDialog =  new MyDialog(G.currentActivity, R.style.DialogAnimFromBottom);
			myDialog.setTitle((String)G.context.getResources().getText(R.string.Detail))
			.setBackgroundAlpha(0.95f)
			.setBodyMargin(30, 0, 30, 0)
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.UnitWithParam), G.selectedVahed),20)
			.setContentSplitter()
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.LogshitwithParam), G.selectedLogshit),20)
			.setContentSplitter()
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.EquipWithParam), G.selectedTajhiz),20)
			.setContentSplitter()
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.SubEquipWithParam), G.selectedZirTajhiz),20)
			.setContentSplitter()
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.UnitmeasurementWithParam), item.MeasureUnitName),20)
			.setContentSplitter()
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.CountDataWithParam),String.valueOf(G.DB.getItemValuesByItemInfoId(item.ItemInfID).size())),20)
			.setContentSplitter()
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.AllowNumberWithParam), item.MaxSampleNo),20)
			.setContentSplitter()
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.CodeItemWithParam), item.ItemInfID),20)
			.setContentSplitter()
			.addBodyText((String)G.context.getResources().getText(R.string.Secondlimitation),20)
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.MinWitParam), item.MinAmount2),18)
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.MaxWithParam), item.MaxAmount2),18)
			.setContentSplitter()
			.addBodyText((String)G.context.getResources().getText(R.string.Thirdlimitation),20)
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.MinWitParam), item.MinAmount3),18)
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.MaxWithParam), item.MaxAmount3),18)
			.setContentSplitter()
			.addBodyText(String.format((String)G.context.getResources().getText(R.string.ZaribWithParam), getValidDigit(item.Zarib.toString())),20)
			.addButton((String)G.context.getResources().getText(R.string.Close), new OnClickListener() {
				@Override
				public void onClick(View v) {
					myDialog.dismiss();
				}
			});
			myDialog.show();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        dtoItems item = getItem(position);

        if (convertView == null) {
			convertView = G.inflater.inflate(R.layout.custom_item, parent, false);
			convertView.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.listitem_background));
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
		FragmentItemReport.arrListItemFiltered.clear();
		for (dtoItems item : FragmentItemReport.arrListItem)
		{
			if (String.valueOf(item.ItemName.toLowerCase()).contains(charText.toLowerCase()))
			{
				FragmentItemReport.arrListItemFiltered.add(item);
			}
		}
		FragmentItemReport.adapterItem.notifyDataSetChanged();
 	}
 	
 	
}
