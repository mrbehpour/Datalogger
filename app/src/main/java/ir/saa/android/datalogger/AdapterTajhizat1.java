package ir.saa.android.datalogger;

import database.structs.dtoItems;
import enums.ValidForSubmitType;
import ir.saa.android.datalogger.G.MenuItemMode;

import java.util.ArrayList;

import mycomponents.MyToast;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import database.structs.dtoTajhiz;


public class AdapterTajhizat1 extends ArrayAdapter<dtoTajhiz> {

    //public static HashMap<ImageView, String> imageMap = new HashMap<ImageView, String>();

    public AdapterTajhizat1(ArrayList<dtoTajhiz> array) {
        super(G.context, R.layout.custom_tajhizat_1, array);
    }


    private static class ViewHolder {

        public ViewGroup layoutRoot;
        public TextView  txtNameTajhiz;
		public ImageView imgTajhizInfo1;
		public ImageView imgTajhizNfc;


        public ViewHolder(View view) {
            layoutRoot = (ViewGroup) view.findViewById(R.id.layoutRoot1);

            txtNameTajhiz = (TextView) view.findViewById(R.id.txtTajhiz1);
            imgTajhizInfo1 = (ImageView) view.findViewById(R.id.imgTajhizInfo1);
			imgTajhizNfc = (ImageView) view.findViewById(R.id.imgTajhizNfc);
            Typeface tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
            txtNameTajhiz.setTypeface(tf);
            
        }
        static Drawable drwGreen;
        static Drawable drwWhite;

        public void fill(final ArrayAdapter<dtoTajhiz> adapter, final dtoTajhiz item, final int position) {
            txtNameTajhiz.setText(item.Name);
            float[] hsv =new float[3];
            hsv[0]=(float) 55;
            hsv[1]=(float) 134;
            hsv[2]= (float) 230;



            if(item.IsFillItem){
            	txtNameTajhiz.setTextColor(Color.argb(255, 63, 103, 255));
            	txtNameTajhiz.setTypeface(null, Typeface.BOLD_ITALIC);

            }else{
				txtNameTajhiz.setTextColor(Color.BLACK);
				txtNameTajhiz.setTypeface(null,Typeface.NORMAL);
            }
            imgTajhizInfo1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					MyToast.Show(G.context, item.Description, Toast.LENGTH_SHORT);
				}
			});

            if(G.Setting.ModTag ==2 && item.HasTag == true && G.Setting.LayerTag == 1) {
				imgTajhizNfc.setVisibility(View.VISIBLE);
				imgTajhizNfc.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						MyToast.Show(G.context,(String)G.context.getResources().getText(R.string.Msg_NfcTag1),Toast.LENGTH_LONG);
					}
				});
			}
            layoutRoot.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                	if(G.currentUser.NeedTag==0 && G.Setting.ModTag==2 && item.HasTag && G.Setting.LayerTag == 1) {
						((ActivityDrawer) G.currentActivity).nfcLogshit = item;
						((ActivityDrawer) G.currentActivity).ShowNfcDialog();
						return;
					}

					GoToNextTajhizPage(item);
                }
            });




        }
    }

    public static void GoToNextTajhizPage(dtoTajhiz item){

		Fragment currentFragment = G.fragmentManager.findFragmentById(R.id.frame_container);
		if (currentFragment != null) {
			G.fragmentStack.push(currentFragment);
			//G.navigationStack.clear();
			G.navigationStackTitle.push(((TextView)G.actionBar.getCustomView().findViewById(R.id.txtTitle)).getText().toString());
//					   TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);
//					   txtTitle.setText(item.Name);
		}
		TextView txtTitle = (TextView) G.actionBar.getCustomView().findViewById(R.id.txtTitle);

		if (G.selectedMenuItemType == G.MenuItemTypes.VAHED) {
			G.selectedMenuItemType = G.MenuItemTypes.LOGSHIT;
			txtTitle.setText(item.Name);//("لاگشیت های واحد "+item.Name);
			G.selectedVahed = item.Name;
			G.selectedVahedId = item.ID;

		}else if (G.selectedMenuItemType == G.MenuItemTypes.LOGSHIT) {
			G.selectedMenuItemType = G.MenuItemTypes.TAJHIZ;
			txtTitle.setText(item.Name);//("تجهیز های لاگشیت "+item.Name);
			G.selectedLogshit = item.Name;
			G.selectedLogshitId = item.ID;
		}else if (G.selectedMenuItemType == G.MenuItemTypes.TAJHIZ) {
			G.selectedMenuItemType = G.MenuItemTypes.ZIRTAJHIZ;
			txtTitle.setText(item.Name);//("زیرتجهیزهای تجهیز "+item.Name);
			G.selectedTajhiz = item.Name;
		}else if (G.selectedMenuItemType == G.MenuItemTypes.ZIRTAJHIZ) {
			if(G.selectedMenuItemMode == MenuItemMode.Sabt)
				G.selectedMenuItemType = G.MenuItemTypes.ITEM;
			else
				G.selectedMenuItemType = G.MenuItemTypes.ITEMREPORT;
			txtTitle.setText(item.Name);//("آیتم های زیرتجهیز "+item.Name);
			G.selectedZirTajhiz = item.Name;
		}else if (G.selectedMenuItemType == G.MenuItemTypes.ZIRTAJHIZ) {
			if(G.selectedMenuItemMode == MenuItemMode.Sabt)
				G.selectedMenuItemType = G.MenuItemTypes.ITEM;
			else
				G.selectedMenuItemType = G.MenuItemTypes.ITEMREPORT;
			txtTitle.setText(item.Name);//("آیتم های زیرتجهیز "+item.Name);
			G.selectedZirTajhiz = item.Name;
		}
		if(G.selectedId != null){
			G.navigationStackId.push(G.selectedId);
		}

		if (G.selectedMenuItemType == G.MenuItemTypes.ITEM) {
			Bundle bundle = new Bundle();
			bundle.putInt("EquipID", G.selectedId);
			bundle.putInt("SubEquipID", item.ID);
			Fragment fragment =  new TabFragmentItem();
			fragment.setArguments(bundle);
			G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}else if (G.selectedMenuItemType == G.MenuItemTypes.ITEMREPORT) {
			Bundle bundle = new Bundle();
			bundle.putInt("EquipID", G.selectedId);
			bundle.putInt("SubEquipID", item.ID);
			Fragment fragment = new FragmentItemReport();
			fragment.setArguments(bundle);
			G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}else{
			Bundle bundle = new Bundle();
			bundle.putInt("ID", item.ID);
			Fragment fragment =  new FragmentTajhizat1();
			fragment.setArguments(bundle);
			G.fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
		}
		G.selectedId = item.ID;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        dtoTajhiz item = getItem(position);
        if (convertView == null) {
            convertView = G.inflater.inflate(R.layout.custom_tajhizat_1, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(this, item, position);
        return convertView;
    }
}
