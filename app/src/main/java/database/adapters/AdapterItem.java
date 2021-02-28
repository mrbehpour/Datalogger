package database.adapters;

import database.fields.Tbl_ItemValues;
import enums.ValidForSubmitType;
import ir.saa.android.datalogger.*;
import ir.saa.android.datalogger.R.drawable;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import database.structs.dtoFormulas;
import database.structs.dtoItemFormulas;
import database.structs.dtoItemValues;
import database.structs.dtoItems;
import database.structs.dtoLogics;
import database.structs.dtoMaxItemVal;
import database.structs.dtoRemarks;
import enums.FormulaType;
import enums.ItemValueType;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import mycomponents.MyCheckList;
import mycomponents.MyCheckList.OnCheckListItemClickListener;
import mycomponents.MyCheckListItem;
import mycomponents.MyCheckListMode;
import mycomponents.MyDialog;
import mycomponents.MyToast;
import mycomponents.MyUtilities;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.v4.content.ContextCompat.*;


public class AdapterItem extends ArrayAdapter<dtoItems> {
	public enum ItemDirection{
		None,
		Left,
		Right
	}

	Typeface tf = null;
	public static ItemDirection itemDirection = ItemDirection.None;
	public MyDialog dialog;
	static EditText edtItemDate;
	static EditText edtItemTime;
	static EditText edtItemTimeOnly;
	static EditText edtItemMeghdari;
	static EditText edtItemDateOnly;
	int ClickButton=0;
	String DateAndTime="";
	 EditText edtItemMamooli;
	int selectedDateLayout;
	int selectedTimeLayout;
	int lengthDate;
	int lengthTime;
	int PageLayout;
	int FontSize;
	public ViewHolder viewHolder;
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
	private String blockCharacterSet = "+-.";

	private InputFilter filter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

			if (source != null && blockCharacterSet.contains(("" + source))) {
				return "";
			}
			return null;
		}
	};
    public AdapterItem(ArrayList<dtoItems> array,int LayoutPage,View view) {
		super(G.context, LayoutPage, array);
		PageLayout=LayoutPage;

		selectedDateLayout=R.layout.body_item_date;
		selectedTimeLayout=R.layout.body_item_time;
		if(G.RTL){
			tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
			lengthDate=8;
			lengthTime=6;
			FontSize=22;
		}else{
			tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
			lengthDate=8;
			lengthTime=6;
			FontSize=12;
		}


		((ActivityDrawer)G.currentActivity).adapterItem = this;
		dialog = new MyDialog(G.currentActivity);

		if(viewHolder==null){
			viewHolder=new ViewHolder(view);
		}


    }





	static Drawable  drwGreen;
	static Drawable drwWhite;
	static Drawable drwGray;
	static Drawable drwInfoRed;
	static Drawable drwInfoBlack;
	//public View currentRowView;


	@RequiresApi(api = Build.VERSION_CODES.N)
	private Boolean
	checkValidations(dtoItems item, String strValue){
		Boolean canSubmit = true;
		String strErrorMessage = "";

//		if(strValue.trim().length()==0 || !MyUtilities.isNumber(strValue.trim()))
//			return false;

		Double value = Double.valueOf(MyUtilities.changeNumberLocaleString(strValue.trim()));
		if(G.Setting.Range1==1){
			if(item.MinAmount1!=null && item.MinAmount1.trim().length()>0 && item.MaxAmount1!=null && item.MaxAmount1.trim().length()>0)
			{
				Double minRange = Double.valueOf(item.MinAmount1);
				Double maxRange = Double.valueOf(item.MaxAmount1);
				if(value<minRange || value>maxRange){
					canSubmit = false;
					strErrorMessage = (String) G.context.getResources().getText(R.string.OutOfRange1) + "\n";
					strErrorMessage += G.Setting.Dsc1 + "\n";
				}
//				else{
//					canSubmit = true;
//				}
//				if(strErrorMessage.trim().length()>0){
//					MyToast.Show(G.context, strErrorMessage, Toast.LENGTH_LONG);
//				}
//				return canSubmit;
			}
		}

		if(G.Setting.Range2==1){
			if(item.MinAmount2!=null && item.MinAmount2.trim().length()>0 && item.MaxAmount2!=null && item.MaxAmount2.trim().length()>0 )
			{
				Double minRange = Double.valueOf(item.MinAmount2);
				Double maxRange = Double.valueOf(item.MaxAmount2);
				if(value<minRange || value>maxRange){
					canSubmit = false;
					strErrorMessage = (String) G.context.getResources().getText(R.string.OutOfRange2) + "\n";
					strErrorMessage += G.Setting.Dsc2 + "\n";
				}
//				else{
//					canSubmit = true;
//				}
//				if(strErrorMessage.trim().length()>0){
//					MyToast.Show(G.context, strErrorMessage, Toast.LENGTH_LONG);
//				}
//				return canSubmit;
			}
		}
		if(G.Setting.Range3==1){
			if(item.MinAmount3!=null && item.MinAmount3.trim().length()>0 && item.MaxAmount3!=null && item.MaxAmount3.trim().length()>0 )
			{
				Double minRange = Double.valueOf(item.MinAmount3);
				Double maxRange = Double.valueOf(item.MaxAmount3);
				if(value<minRange || value>maxRange){
					canSubmit = false;
					strErrorMessage = (String) G.context.getResources().getText(R.string.OutOfRange3) + "\n";
					strErrorMessage += G.Setting.Dsc3 + "\n";
				}
//				else{
//					canSubmit = true;
//				}
//				if(strErrorMessage.trim().length()>0){
//					MyToast.Show(G.context, strErrorMessage, Toast.LENGTH_LONG);
//				}
//				return canSubmit;
			}
		}
		if(strErrorMessage.trim().length()>0){
			MyToast.Show(G.context, strErrorMessage, Toast.LENGTH_LONG);
		}
		return canSubmit;
	}

//	private void setBackgroundColorDependsOnItemVal(dtoItems item){
////		ViewGroup layoutRoot = (ViewGroup) currentRowView.findViewById(R.id.layoutRootItem);
////		ImageView imgItemInfo = (ImageView) currentRowView.findViewById(R.id.imgItemInfo);
////
////		if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal == null || TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.replaceAll("/", "").replaceAll(":", "").replaceAll(",", "").trim().length()==0){
////			layoutRoot.setBackgroundDrawable(drwWhite);
////		}else{
////			layoutRoot.setBackgroundDrawable(drwGreen);
////		}
////
////		if(!item.IsInTimeRange && G.Setting.ContorolTime.compareTo("2")==0){
////			imgItemInfo.setImageDrawable(drwInfoRed);
////		}else{
////			imgItemInfo.setImageDrawable(drwInfoBlack);
////		}
////
////		if(G.Setting.NoCheckMaxData!=1){
////			if(item.IsFullOfMaxData){
////				layoutRoot.setBackgroundDrawable(drwGray);
////				imgItemInfo.setImageDrawable(drwInfoBlack);
////			}
////		}
//		this.notifyDataSetChanged();
//	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showDialog(final dtoItems item, final int pos){
		if(G.Setting.ModTag==2 && item.HasTag && G.Setting.LayerTag == 2 && G.currentUser.NeedTag==0)
		{
			((ActivityDrawer)G.currentActivity).nfcItemDirection = itemDirection;
			((ActivityDrawer)G.currentActivity).nfcItemPos = pos;
			((ActivityDrawer)G.currentActivity).nfcItem = item;
			if(!G.currentActivity.isFinishing()) {
				((ActivityDrawer) G.currentActivity).ShowNfcDialog();
			}
			return;
		}

		showDialog2(item,pos);
	}



	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public void showDialog2(final dtoItems item, final int pos){

		if (item.AmountTypID == ItemValueType.Meghdari) {
			dialog.clearAllPanel();
			dialog.setTitle(item.ItemName)
					.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
					.addContentXml(R.layout.body_item_meghdari)
					.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
						@Override
						public void onClick(View v) {
							showJozeeatDialog(item);
						}
					});
			TextView txtMeghdariMinTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMinTitle);
			TextView txtMeghdariMin = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMin);
			TextView txtMeghdariMaxTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMaxTitle);
			TextView txtMeghdariMax = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMax);
			TextView txtMeghdariUnitTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariUnitTitle);
			TextView txtMeghdariUnit = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariUnit);
			edtItemMeghdari = (EditText) dialog.getDialog().findViewById(R.id.edtItemMeghdari);
			edtItemMeghdari.setTypeface(tf);
			//edtItemMeghdari.setFilters(new InputFilter[] { filter });

			txtMeghdariMinTitle.setTypeface(tf);
			txtMeghdariMin.setTypeface(tf);
			txtMeghdariMaxTitle.setTypeface(tf);
			txtMeghdariMax.setTypeface(tf);
			txtMeghdariUnitTitle.setTypeface(tf);
			txtMeghdariUnit.setTypeface(tf);
			if(G.RTL){
				dialog.addButton((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
					@Override
					public void onClick(View v) {

						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if(IsValidForSubmit == ValidForSubmitType.OutOfRange){
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}else if(IsValidForSubmit==ValidForSubmitType.InRangeButHasOtherValue){
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if(IsValidForSubmit==ValidForSubmitType.InRange  || G.Setting.ContorolTime.compareTo("2") != 0)
						{
							String itemval;
							if(edtItemMeghdari.getText().toString().trim().equals("")){
								if(edtItemMeghdari.getHint().toString().trim().equals("")){
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForValidText), Toast.LENGTH_SHORT);
									return;
								}else{
									itemval=edtItemMeghdari.getHint().toString().trim();
								}
							}else{
								itemval=edtItemMeghdari.getText().toString().trim();
							}
							if (!checkValidations(item, itemval))
								return;


							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = itemval;
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
							item.HasValueInRange=true;
							if(G.isSave){
								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
									G.isSave = true;
									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
								}
							}

							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if(pos == (TabFragmentItem.arrListItemFiltered.size()-1)){
							dialog.dismiss();

						}else{
							itemDirection = ItemDirection.Right;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos+1), pos+1);
						}
					}
				})

						.addButton((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						})

						.addButton((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if(IsValidForSubmit == ValidForSubmitType.OutOfRange){
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}else if(IsValidForSubmit==ValidForSubmitType.InRangeButHasOtherValue){
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if(IsValidForSubmit==ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0)
								{
									String itemval;
									if(edtItemMeghdari.getText().toString().trim().equals("")){
										if(edtItemMeghdari.getHint().toString().trim().equals("")){
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForValidText), Toast.LENGTH_SHORT);
											return;
										}else{
											itemval=edtItemMeghdari.getHint().toString().trim();
										}
									}else{
										itemval=edtItemMeghdari.getText().toString().trim();
									}
									if (!checkValidations(item, itemval))
										return;



									TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal =itemval;

									TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();
									TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

//							if(G.isSave){
//								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//									G.isSave = true;
//									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//								}
//							}

									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if(pos == 0){
									dialog.dismiss();
								}else{
									itemDirection = ItemDirection.Left;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos-1), pos-1);
								}
							}
						})

						;

				if(G.isMultiMedia) {
					dialog.addButton((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}
			else{
				//edtItemMeghdari.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				edtItemMeghdari.setKeyListener(DigitsKeyListener.getInstance("-0123456789."));
				edtItemMeghdari.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

					    //hide_keyboard(v);
					}
				});
				edtItemMeghdari.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						//hide_keyboard(v);

						if(keyCode==KeyEvent.KEYCODE_POUND){

							ClickButton++;
							if(ClickButton==2) {
								if(edtItemMeghdari.getText().toString().indexOf(".")>-1){

									return false;
								}
								edtItemMeghdari.setText(edtItemMeghdari.getText().toString() + ".");
								edtItemMeghdari.setSelection(edtItemMeghdari.getText().toString().length());
								ClickButton=0;
								return true;
							}
							if(ClickButton==4) {
								if(edtItemMeghdari.getText().toString().indexOf("-")>-1){

									return false;
								}
								ClickButton=0;
								edtItemMeghdari.setText("-"+edtItemMeghdari.getText().toString());
								edtItemMeghdari.setSelection(edtItemMeghdari.getText().toString().length());
								return true;
							}
						}
						if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){

						}
						return false;
					}
				});
				dialog.clearButtonPanelLR();
				dialog.addButtonL((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
					@Override
					public void onClick(View v) {

						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if(IsValidForSubmit == ValidForSubmitType.OutOfRange){
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}else if(IsValidForSubmit==ValidForSubmitType.InRangeButHasOtherValue){
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if(IsValidForSubmit==ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0)
						{
							String itemval;
							if(edtItemMeghdari.getText().toString().trim().equals("")){
								if(edtItemMeghdari.getHint().toString().trim().equals("")){
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForValidText), Toast.LENGTH_SHORT);
									return;
								}else{
									itemval=edtItemMeghdari.getHint().toString().trim();
								}
							}else{
								itemval=edtItemMeghdari.getText().toString().trim();
							}
							if (!checkValidations(item, itemval))
								return;

							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = itemval;

							TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

//							if(G.isSave){
//								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//									G.isSave = true;
//									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//								}
//							}
							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if(pos == 0){
							dialog.dismiss();
						}else{
							itemDirection = ItemDirection.Left;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos-1), pos-1);
						}
					}
				})
						.addButtonR((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if(IsValidForSubmit == ValidForSubmitType.OutOfRange){
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}else if(IsValidForSubmit==ValidForSubmitType.InRangeButHasOtherValue){
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if(IsValidForSubmit==ValidForSubmitType.InRange  || G.Setting.ContorolTime.compareTo("2") != 0)
								{
									String itemval;
									if(edtItemMeghdari.getText().toString().trim().equals("")){
										if(edtItemMeghdari.getHint().toString().trim().equals("")){
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForValidText), Toast.LENGTH_SHORT);
											return;
										}else{
											itemval=edtItemMeghdari.getHint().toString().trim();
										}
									}else{
										itemval=edtItemMeghdari.getText().toString().trim();
									}
									if (!checkValidations(item, itemval))
										return;

									TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = itemval;
									TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
									TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
									item.HasValueInRange=true;
									if(G.isSave){
										if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
											G.isSave = true;
											G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
										}
									}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if(pos == (TabFragmentItem.arrListItemFiltered.size()-1)){
									TabFragmentItem.imgSaveItem.requestFocus();
									dialog.dismiss();
								}else{
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos+1), pos+1);
								}
							}
						})


						.addButtonR((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});


				if(G.isMultiMedia) {
					dialog.addButtonL((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}



			if(!G.currentActivity.isFinishing())
			{
				dialog.show();
			}

			txtMeghdariMin.setText(MyUtilities.changeNumberLocaleString(item.MinAmount1));
			txtMeghdariMax.setText(MyUtilities.changeNumberLocaleString(item.MaxAmount1));
			txtMeghdariUnit.setText(item.MeasureUnitName.toString());

			//--------Load if has value--------
			edtItemMeghdari.setHint(getLastItemValByItemInfoId(item.ItemInfID));
			if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal != null && TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length()>0){
				edtItemMeghdari.setText(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim());
				edtItemMeghdari.selectAll();
			}
		}
		else if (item.AmountTypID == ItemValueType.DateTime) {

			dialog.clearAllPanel();
			dialog	.setTitle(item.ItemName)
					.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
					.addContentXml(selectedDateLayout)
					.addContentXml(selectedTimeLayout)
					.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
						@Override
						public void onClick(View v) {
							showJozeeatDialog(item);
						}
					});
			TextView txtItemDate = (TextView) dialog.getDialog().findViewById(R.id.txtItemDate);
			TextView txtItemTime = (TextView) dialog.getDialog().findViewById(R.id.txtItemTime);
			edtItemDate = (EditText) dialog.getDialog().findViewById(R.id.edtItemDate);
			edtItemTime = (EditText) dialog.getDialog().findViewById(R.id.edtItemTime);
			edtItemDate.setInputType(InputType.TYPE_CLASS_NUMBER);
			edtItemTime.setInputType(InputType.TYPE_CLASS_NUMBER);
			txtItemDate.setTypeface(tf);
			txtItemTime.setTypeface(tf);
			edtItemDate.setTypeface(tf);
			edtItemTime.setTypeface(tf);
			if(G.RTL) {
				dialog.addButton((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
					@Override
					public void onClick(View v) {
						String ItemValDate = "";

						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
							if ((edtItemDate.getText().toString().replaceAll("/", "").trim().length() == 0 && edtItemTime.getText().toString().replaceAll(":", "").trim().length() == 0) ||
									(edtItemDate.getText().toString().replaceAll("/", "").trim().length() == lengthDate && edtItemTime.getText().toString().replaceAll(":", "").trim().length() == lengthTime)) {
								if (edtItemDate.getText().toString().replaceAll("/", "").trim().length() > 0 && !Tarikh.isDateValid(edtItemDate.getText().toString())) {
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDate), Toast.LENGTH_LONG);
									return;
								}
								if (edtItemTime.getText().toString().replaceAll(":", "").trim().length() > 0 && !Tarikh.isTimeValid(edtItemTime.getText().toString())) {
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckTime), Toast.LENGTH_LONG);
									return;
								}
								if (G.RTL) {

									DateAndTime = Tarikh.getMiladiDate(edtItemDate.getText().toString()) + " " + edtItemTime.getText().toString();

								} else {
									DateAndTime = edtItemDate.getText().toString() + " " + edtItemTime.getText().toString();
								}
								TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = DateAndTime;//edtItemTime.getText().toString().replaceAll(":", "").trim() + edtItemDate.getText().toString().replaceAll("/", "").trim();
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
								item.HasValueInRange=true;
								if(G.isSave){
									if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
										G.isSave = true;
										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
									}
								}

							} else {
								MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDateTime), Toast.LENGTH_LONG);
								return;
							}

							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
							dialog.dismiss();
						} else {
							itemDirection = ItemDirection.Right;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
						}
					}
				})

						.addButton((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						})
						.addButton((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if ((edtItemDate.getText().toString().replaceAll("/", "").trim().length() == 0 && edtItemTime.getText().toString().replaceAll(":", "").trim().length() == 0) ||
											(edtItemDate.getText().toString().replaceAll("/", "").trim().length() == lengthDate && edtItemTime.getText().toString().replaceAll(":", "").trim().length() == lengthTime)) {
										if (edtItemDate.getText().toString().replaceAll("/", "").trim().length() > 0 && !Tarikh.isDateValid(edtItemDate.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDate), Toast.LENGTH_LONG);
											return;
										}
										if (edtItemTime.getText().toString().replaceAll(":", "").trim().length() > 0 && !Tarikh.isTimeValid(edtItemTime.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckTime), Toast.LENGTH_LONG);
											return;
										}
										if (G.RTL) {

											DateAndTime = Tarikh.getMiladiDate(edtItemDate.getText().toString()) + " " + edtItemTime.getText().toString();

										} else {
											DateAndTime = edtItemDate.getText().toString() + " " + edtItemTime.getText().toString();
										}
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = DateAndTime;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

//								if(G.isSave){
//									if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//										G.isSave = true;
//										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//									}
//								}
									} else {
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDateTime), Toast.LENGTH_LONG);
										return;
									}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == 0) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Left;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
								}
							}
						});
				if (G.isMultiMedia) {
					dialog.addButton((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}
			else{
				edtItemDate.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						hide_keyboard(v);
						return false;
					}
				});
				edtItemTime.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						hide_keyboard(v);
						return false;
					}
				});
				edtItemDate.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hide_keyboard(v);
					}
				});
				edtItemTime.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hide_keyboard(v);
					}
				});
				dialog.clearButtonPanelLR();
				dialog.addButtonL((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
					@Override
					public void onClick(View v) {

						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
							if ((edtItemDate.getText().toString().replaceAll("/", "").trim().length() == 0 && edtItemTime.getText().toString().replaceAll(":", "").trim().length() == 0) ||
									(edtItemDate.getText().toString().replaceAll("/", "").trim().length() == lengthDate && edtItemTime.getText().toString().replaceAll(":", "").trim().length() == lengthTime)) {
								if (edtItemDate.getText().toString().replaceAll("/", "").trim().length() > 0 && !Tarikh.isDateValid(edtItemDate.getText().toString())) {
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDate), Toast.LENGTH_LONG);
									return;
								}
								if (edtItemTime.getText().toString().replaceAll(":", "").trim().length() > 0 && !Tarikh.isTimeValid(edtItemTime.getText().toString())) {
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckTime), Toast.LENGTH_LONG);
									return;
								}
								if (G.RTL) {

									DateAndTime = Tarikh.getMiladiDate(edtItemDate.getText().toString()) + " " + edtItemTime.getText().toString();

								} else {
									DateAndTime = edtItemDate.getText().toString() + " " + edtItemTime.getText().toString();
								}
								TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = DateAndTime;
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

//								if(G.isSave){
//									if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//										G.isSave = true;
//										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//									}
//								}
							} else {
								MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDateTime), Toast.LENGTH_LONG);
								return;
							}
							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if (pos == 0) {
							dialog.dismiss();
						} else {
							itemDirection = ItemDirection.Left;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
						}
					}
				})

						.addButtonR((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {
								String ItemValDate = "";

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if ((edtItemDate.getText().toString().replaceAll("/", "").trim().length() == 0 && edtItemTime.getText().toString().replaceAll(":", "").trim().length() == 0) ||
											(edtItemDate.getText().toString().replaceAll("/", "").trim().length() == lengthDate && edtItemTime.getText().toString().replaceAll(":", "").trim().length() == lengthTime)) {
										if (edtItemDate.getText().toString().replaceAll("/", "").trim().length() > 0 && !Tarikh.isDateValid(edtItemDate.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDate), Toast.LENGTH_LONG);
											return;
										}
										if (edtItemTime.getText().toString().replaceAll(":", "").trim().length() > 0 && !Tarikh.isTimeValid(edtItemTime.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckTime), Toast.LENGTH_LONG);
											return;
										}
										if (G.RTL) {

											DateAndTime = Tarikh.getMiladiDate(edtItemDate.getText().toString()) + " " + edtItemTime.getText().toString();

										} else {
											DateAndTime = edtItemDate.getText().toString() + " " + edtItemTime.getText().toString();
										}
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = DateAndTime;//edtItemTime.getText().toString().replaceAll(":", "").trim() + edtItemDate.getText().toString().replaceAll("/", "").trim();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										item.HasValueInRange=true;
										if(G.isSave){
											if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
												G.isSave = true;
												G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
											}
										}
									} else {
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDateTime), Toast.LENGTH_LONG);
										return;
									}

									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									TabFragmentItem.imgSaveItem.requestFocus();
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})
						.addButtonR((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
				if (G.isMultiMedia) {
					dialog.addButtonL((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}

			if(!G.currentActivity.isFinishing())
			{
				dialog.show();
			}

			//--------Load if has value--------
			String displayItemDate;
			String displayItemTime;

//				edtItemDate.setText("13961016");
//				edtItemTime.setText("1628");
			if(getLastItemValByItemInfoId(item.ItemInfID).equals("")==false) {
				if(G.RTL) {
					displayItemDate =Tarikh.getShamsiDate(getLastItemValByItemInfoId(item.ItemInfID).trim().substring(0,10));
					displayItemTime=G.convertToEnglishDigits(getLastItemValByItemInfoId(item.ItemInfID).trim().substring(11),false);
				}else{
					displayItemDate = G.convertToEnglishDigits(getLastItemValByItemInfoId(item.ItemInfID).trim().substring(0,10),false);
					displayItemTime=G.convertToEnglishDigits(getLastItemValByItemInfoId(item.ItemInfID).trim().substring(11),false);
				}
				edtItemDate.setText(displayItemDate);
				edtItemTime.setText(displayItemTime);
			}
			if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal != null && TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length()>0){
				if(G.RTL) {
					displayItemDate =Tarikh.getShamsiDate(getLastItemValByItemInfoId(item.ItemInfID).trim().substring(0,10));
					displayItemTime=G.convertToEnglishDigits(getLastItemValByItemInfoId(item.ItemInfID).trim().substring(11),false);
				}else{
					displayItemDate = G.convertToEnglishDigits(getLastItemValByItemInfoId(item.ItemInfID).trim().substring(0,10),false);
					displayItemTime=G.convertToEnglishDigits(getLastItemValByItemInfoId(item.ItemInfID).trim().substring(11),false);
				}
				edtItemDate.setText(displayItemDate);
				edtItemDate.selectAll();
				edtItemTime.setText(displayItemTime);
				edtItemTime.selectAll();
			}

		}
		else if (item.AmountTypID == ItemValueType.Time) {

			dialog.clearAllPanel();
			dialog	.setTitle(item.ItemName)
					.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
					.addContentXml(selectedTimeLayout)
					.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
						@Override
						public void onClick(View v) {
							showJozeeatDialog(item);
						}
					});
			TextView txtItemTimeOnly = (TextView) dialog.getDialog().findViewById(R.id.txtItemTime);
			edtItemTimeOnly = (EditText) dialog.getDialog().findViewById(R.id.edtItemTime);
			edtItemTimeOnly.setInputType(InputType.TYPE_CLASS_NUMBER);
			txtItemTimeOnly.setTypeface(tf);
			edtItemTimeOnly.setTypeface(tf);
			if(G.RTL) {
				dialog
						.addButton((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if ((edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() == 0) || (edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() == lengthTime)) {
										if ((edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() > 0) && !Tarikh.isTimeValid(edtItemTimeOnly.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckTime), Toast.LENGTH_LONG);
											return;
										}
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = edtItemTimeOnly.getText().toString();//edtItemTimeOnly.getText().toString().replaceAll(":", "").trim();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										item.HasValueInRange=true;
										if(G.isSave){
											if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
												G.isSave = true;
												G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
											}
										}

									} else {
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckRegisterTime), Toast.LENGTH_LONG);
										return;
									}

									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})

						.addButton((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						})
						.addButton((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
							@Override
							public void onClick(View v) {
								String ItemValTime = "";
								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if ((edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() == 0) || (edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() == lengthTime)) {
										if ((edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() > 0) && !Tarikh.isTimeValid(edtItemTimeOnly.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckTime), Toast.LENGTH_LONG);
											return;
										}
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = ItemValTime;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
//								if(G.isSave){
//									if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//										G.isSave = true;
//										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//									}
//								}

									} else {
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckRegisterTime), Toast.LENGTH_LONG);
										return;
									}

									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == 0) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Left;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
								}
							}
						});
				if (G.isMultiMedia) {
					dialog.addButton((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}
			else{
				edtItemTimeOnly.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hide_keyboard(v);
					}
				});
				edtItemTimeOnly.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						hide_keyboard(v);
						return false;
					}
				});
				dialog.clearButtonPanelLR();
				dialog.addButtonL((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
					@Override
					public void onClick(View v) {
						String ItemValTime = "";
						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
							if ((edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() == 0) || (edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() == lengthTime)) {
								if ((edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() > 0) && !Tarikh.isTimeValid(edtItemTimeOnly.getText().toString())) {
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckTime), Toast.LENGTH_LONG);
									return;
								}
								TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = ItemValTime;
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

//								if(G.isSave){
//									if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//										G.isSave = true;
//										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//									}
//								}
							} else {
								MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckRegisterTime), Toast.LENGTH_LONG);
								return;
							}

							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if (pos == 0) {
							dialog.dismiss();
						} else {
							itemDirection = ItemDirection.Left;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
						}
					}
				})

						.addButtonR((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if ((edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() == 0) || (edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() == lengthTime)) {
										if ((edtItemTimeOnly.getText().toString().replaceAll(":", "").trim().length() > 0) && !Tarikh.isTimeValid(edtItemTimeOnly.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckTime), Toast.LENGTH_LONG);
											return;
										}
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = edtItemTimeOnly.getText().toString();//edtItemTimeOnly.getText().toString().replaceAll(":", "").trim();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										item.HasValueInRange=true;
										if(G.isSave){
											if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
												G.isSave = true;
												G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
											}
										}
									} else {
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckRegisterTime), Toast.LENGTH_LONG);
										return;
									}

									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									TabFragmentItem.imgSaveItem.requestFocus();
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})
						.addButtonR((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
				if (G.isMultiMedia) {
					dialog.addButtonL((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}

			if(!G.currentActivity.isFinishing())
			{
				dialog.show();
			}

			//--------Load if has value--------
			if(getLastItemValByItemInfoId(item.ItemInfID).equals("")==false) {
				edtItemTimeOnly.setText(G.convertToEnglishDigits(getLastItemValByItemInfoId(item.ItemInfID),false));
			}
			if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal != null && TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length()>0){
				edtItemTimeOnly.setText(G.convertToEnglishDigits(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim(),false));
				edtItemTimeOnly.selectAll();
			}

		}
		else if (item.AmountTypID == ItemValueType.Date) {

			dialog.clearAllPanel();
			dialog	.setTitle(item.ItemName)
					.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
					.addContentXml(selectedDateLayout)
					.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
						@Override
						public void onClick(View v) {
							showJozeeatDialog(item);
						}
					});
			TextView txtItemDateOnly = (TextView) dialog.getDialog().findViewById(R.id.txtItemDate);
			edtItemDateOnly = (EditText) dialog.getDialog().findViewById(R.id.edtItemDate);
			edtItemDateOnly.setInputType(InputType.TYPE_CLASS_NUMBER);
			txtItemDateOnly.setTypeface(tf);
			edtItemDateOnly.setTypeface(tf);

			if(G.RTL) {
				dialog
						.addButton((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {
								String ItemValDate;

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if ((edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() == 0) || (edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() == lengthDate)) {
										if ((edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() > 0) && !Tarikh.isDateValid(edtItemDateOnly.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDate), Toast.LENGTH_LONG);
											return;
										}
										if (G.RTL) {

											ItemValDate = Tarikh.getMiladiDate(edtItemDateOnly.getText().toString());

										} else {
											ItemValDate = edtItemDateOnly.getText().toString();
										}
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = ItemValDate;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										item.HasValueInRange=true;
										if(G.isSave){
											if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
												G.isSave = true;
												G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
											}
										}

									} else {
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckRegisterDate), Toast.LENGTH_LONG);
										return;
									}

									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})

						.addButton((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						})
						.addButton((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
							@Override
							public void onClick(View v) {
								String ItemValDate;

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if ((edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() == 0) || (edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() == lengthDate)) {
										if ((edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() > 0) && !Tarikh.isDateValid(edtItemDateOnly.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDate), Toast.LENGTH_LONG);
											return;
										}
										if (G.RTL) {

											ItemValDate = Tarikh.getMiladiDate(edtItemDateOnly.getText().toString());

										} else {
											ItemValDate = edtItemDateOnly.getText().toString();
										}
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = ItemValDate;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
//								if(G.isSave){
//									if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//										G.isSave = true;
//										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//									}
//								}

									} else {
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckRegisterDate), Toast.LENGTH_LONG);
										return;
									}

									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == 0) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Left;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
								}
							}
						});
				if (G.isMultiMedia) {
					dialog.addButton((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}
			else{
				edtItemDateOnly.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						hide_keyboard(v);
						return false;
					}
				});
				edtItemDateOnly.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hide_keyboard(v);
					}
				});
				dialog.clearButtonPanelLR();
				dialog.addButtonL((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
					@Override
					public void onClick(View v) {
						String ItemValDate;

						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
							if ((edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() == 0) || (edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() == lengthDate)) {
								if ((edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() > 0) && !Tarikh.isDateValid(edtItemDateOnly.getText().toString())) {
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDate), Toast.LENGTH_LONG);
									return;
								}
								if (G.RTL) {

									ItemValDate = Tarikh.getMiladiDate(edtItemDateOnly.getText().toString());

								} else {
									ItemValDate = edtItemDateOnly.getText().toString();
								}
								TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = ItemValDate;
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

//								if(G.isSave){
//									if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//										G.isSave = true;
//										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//									}
//								}
							} else {
								MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckRegisterDate), Toast.LENGTH_LONG);
								return;
							}

							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if (pos == 0) {
							dialog.dismiss();
						} else {
							itemDirection = ItemDirection.Left;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
						}
					}
				})
						.addButtonR((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {
								String ItemValDate;

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if ((edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() == 0) || (edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() == lengthDate)) {
										if ((edtItemDateOnly.getText().toString().replaceAll("/", "").trim().length() > 0) && !Tarikh.isDateValid(edtItemDateOnly.getText().toString())) {
											MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckDate), Toast.LENGTH_LONG);
											return;
										}
										if (G.RTL) {

											ItemValDate = Tarikh.getMiladiDate(edtItemDateOnly.getText().toString());

										} else {
											ItemValDate = edtItemDateOnly.getText().toString();
										}
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = ItemValDate;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										item.HasValueInRange=true;
										if(G.isSave){
											if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
												G.isSave = true;
												G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
											}
										}
									} else {
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.Msg_CheckRegisterDate), Toast.LENGTH_LONG);
										return;
									}

									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									TabFragmentItem.imgSaveItem.requestFocus();
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})
						.addButtonR((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});

				if (G.isMultiMedia) {
					dialog.addButtonL((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}

			if(!G.currentActivity.isFinishing())
			{
				dialog.show();
			}


			//--------Load if has value--------
			String Date=getLastItemValByItemInfoId(item.ItemInfID);
			String DisplayItemValDate="";
			if(G.RTL){

				DisplayItemValDate=Tarikh.getShamsiDate(Date);

			}else{
				DisplayItemValDate=G.convertToEnglishDigits(Date.replace("-","/").replace("/","").trim(),false);

			}
			if(DisplayItemValDate.equals("")==false) {
				edtItemDateOnly.setText(DisplayItemValDate);
			}
			if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal != null && TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length()>0){
				if(G.RTL){

					DisplayItemValDate=Tarikh.getShamsiDate(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim()).replace("/","").trim();

				}else{
					DisplayItemValDate=G.convertToEnglishDigits(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().replace("/","").trim(),false);

				}
				edtItemDateOnly.setText(DisplayItemValDate);
				edtItemDateOnly.selectAll();
			}
		}
		else if (item.AmountTypID == ItemValueType.Logic) {
			dialog.clearAllPanel();
			final dtoLogics logic = G.DB.getLogicByLogicTypeId(item.LogicTypID);
			final MyCheckList mycheckList = new MyCheckList(G.context, new MyCheckListItem("", "",false),new MyCheckListItem("", "",false));
			if(logic!=null){
				mycheckList.removeAllCkeckItems();
				mycheckList.addCheckItem(new MyCheckListItem(logic.LogicVal1, "0",false))
						.addCheckItem(new MyCheckListItem(logic.LogicVal2, "1",false));
				mycheckList.setCheckListOrientation(LinearLayout.VERTICAL)
						.setSelectionMode(MyCheckListMode.SingleSelection)
						.setCheckItemsHeight(100)

				;
				dialog	.setTitle(item.ItemName)
						.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
						.addContentView(mycheckList)
						.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
							@Override
							public void onClick(View v) {
								showJozeeatDialog(item);
							}
						});

			}else{
				dialog	.setTitle(item.ItemName)
						.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
						.addContentXml(R.layout.body_one_textview)
						.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
							@Override
							public void onClick(View v) {
								showJozeeatDialog(item);
							}
						});
				TextView txtBodyMessage = (TextView) dialog.getDialog().findViewById(R.id.txtBodymessage);
				txtBodyMessage.setText((String) G.context.getResources().getText(R.string.NoDataLogical));
				txtBodyMessage.setTypeface(tf);
			}

			if(G.RTL) {
				dialog
						.addButton((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (logic != null) {
										if (mycheckList.getSelectedCheckListItems().size() > 0) {
											MyCheckListItem checkedItem = mycheckList.getSelectedCheckListItems().get(0);
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = checkedItem.Value.toString();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();
											;//Tarikh.getCurrentShamsidateWithoutSlash();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
											item.HasValueInRange=true;

										} else {
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
										}
									} else {
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
									}

									if(G.isSave){
										if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
											G.isSave = true;
											G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
										}
									}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})

						.addButton((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						})
						.addButton((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (logic != null) {
										if (mycheckList.getSelectedCheckListItems().size() > 0) {
											MyCheckListItem checkedItem = mycheckList.getSelectedCheckListItems().get(0);
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = checkedItem.Value.toString();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();


										} else {
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
										}
									} else {
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
									}
//							if(G.isSave){
//								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//									G.isSave = true;
//									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//								}
//							}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == 0) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Left;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
								}
							}
						});
				if (G.isMultiMedia) {
					dialog.addButton((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}
			else{
				dialog.clearButtonPanelLR();
				dialog.addButtonL((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
					@Override
					public void onClick(View v) {

						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
							if (logic != null) {
								if (mycheckList.getSelectedCheckListItems().size() > 0) {
									MyCheckListItem checkedItem = mycheckList.getSelectedCheckListItems().get(0);
									TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = checkedItem.Value.toString();
									TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
									TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

								} else {
									TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
								}
							} else {
								TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
							}
//							if(G.isSave){
//								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//									G.isSave = true;
//									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//								}
//							}
							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if (pos == 0) {
							dialog.dismiss();
						} else {
							itemDirection = ItemDirection.Left;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
						}
					}
				})
						.addButtonR((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (logic != null) {
										if (mycheckList.getSelectedCheckListItems().size() > 0) {
											MyCheckListItem checkedItem = mycheckList.getSelectedCheckListItems().get(0);
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = checkedItem.Value.toString();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();
											;//Tarikh.getCurrentShamsidateWithoutSlash();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
											item.HasValueInRange=true;
										} else {
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
										}
									} else {
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
									}

									if(G.isSave){
										if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
											G.isSave = true;
											G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
										}
									}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									TabFragmentItem.imgSaveItem.requestFocus();
									dialog.dismiss();

								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})
						.addButtonR((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
								@Override
								public void onClick(View v) {

									dialog.dismiss();
								}
						});
				if (G.isMultiMedia) {
					dialog.addButtonL((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}

			if(!G.currentActivity.isFinishing())
			{
				dialog.show();
			}
			mycheckList.setSelectionByValue((Object) getLastItemValByItemInfoId(item.ItemInfID));

			//TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal= getLastItemValByItemInfoId(item.ItemInfID);


			if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal != null && TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length()>0){
				mycheckList.setSelectionByValue(-1);
				mycheckList.setSelectionByValue(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal);   //setText(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim());
			}
		}
		else if (item.AmountTypID == ItemValueType.Remark) {

			dialog.clearAllPanel();
			final ArrayList<dtoRemarks> remarks = G.DB.getRemarksByRemarkGroupId(item.RemGroupID);
			final MyCheckList myRemarkcheckList = new MyCheckList(G.context, new MyCheckListItem("", "",false),new MyCheckListItem("", "",false));
			if(remarks.size()>0){
				myRemarkcheckList.removeAllCkeckItems();
				for(int i=0;i<remarks.size();i++){
					myRemarkcheckList.addCheckItem(new MyCheckListItem(remarks.get(i).RemName,remarks.get(i).RemID.toString(),false));
				}

				myRemarkcheckList.setCheckListOrientation(LinearLayout.VERTICAL).setCheckItemsHeight(50);

				if(item.RemTyp==0)
					myRemarkcheckList.setSelectionMode(MyCheckListMode.MultiSelection);
				else {
					myRemarkcheckList.setSelectionMode(MyCheckListMode.SingleSelection);
				}

				//myRemarkcheckList.setBackground(ContextCompat.getDrawable(getContext(), drawable.checklist_selector));
//					myRemarkcheckList.setOnCheckListItemClickListener(new OnCheckListItemClickListener() {
//						@Override
//						public void onCheckListItemClick(MyCheckListItem selectedCheckListItem, Boolean isChecked) {
//							MyToast.Show(G.context, selectedCheckListItem.Text+" "+(isChecked?"Checked":"UnCheked"), Toast.LENGTH_SHORT);
//						}
//					});
				dialog
						.setTitle(item.ItemName)
						.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
						.addContentView(myRemarkcheckList)
						.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
							@Override
							public void onClick(View v) {
								showJozeeatDialog(item);
							}
						});

			}else{
				dialog	.setTitle(item.ItemName)
						.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
						.addContentXml(R.layout.body_one_textview)
						.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
							@Override
							public void onClick(View v) {
								showJozeeatDialog(item);
							}
						});
				TextView txtBodyMessage = (TextView) dialog.getDialog().findViewById(R.id.txtBodymessage);
				txtBodyMessage.setText((String)G.context.getResources().getText(R.string.NoDataRemark));
				txtBodyMessage.setTypeface(tf);
			}

			if(G.RTL) {
				dialog
						.addButton((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (myRemarkcheckList.getSelectedCheckListItems().size() > 0) {
										String value = getStringFromRemarks(myRemarkcheckList.getSelectedCheckListItems());
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = value;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate =  Tarikh.getCurrentMiladidatetime(); //Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										item.HasValueInRange=true;


									} else {
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
									}

									if(G.isSave){
										if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
											G.isSave = true;
											G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
										}
									}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})

						.addButton((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						})
						.addButton((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (myRemarkcheckList.getSelectedCheckListItems().size() > 0) {
										String value = getStringFromRemarks(myRemarkcheckList.getSelectedCheckListItems());
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = value;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();


									} else {
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
									}
//							if(G.isSave){
//								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//									G.isSave = true;
//									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//								}
//							}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == 0) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Left;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
								}
							}
						});
				if (G.isMultiMedia) {
					dialog.addButton((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}
			else{
				dialog.clearButtonPanelLR();
				dialog.addButtonL((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
					@Override
					public void onClick(View v) {

						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
							if (myRemarkcheckList.getSelectedCheckListItems().size() > 0) {
								String value = getStringFromRemarks(myRemarkcheckList.getSelectedCheckListItems());
								TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = value;
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
							} else {
								TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
							}

//							if(G.isSave){
//								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//									G.isSave = true;
//									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//								}
//							}
							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if (pos == 0) {
							dialog.dismiss();
						} else {
							itemDirection = ItemDirection.Left;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
						}
					}
				})

						.addButtonR((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);

									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (myRemarkcheckList.getSelectedCheckListItems().size() > 0) {
										String value = getStringFromRemarks(myRemarkcheckList.getSelectedCheckListItems());
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = value;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime(); //Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										item.HasValueInRange=true;

									} else {
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
									}

									if(G.isSave){
										if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
											G.isSave = true;
											G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
										}
									}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									TabFragmentItem.imgSaveItem.requestFocus();
									dialog.dismiss();

								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})
						.addButtonR((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});

				if (G.isMultiMedia) {
					dialog.addButtonL((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}

			if(!G.currentActivity.isFinishing())
			{
				dialog.show();
			}
			//Load if has value
			myRemarkcheckList.setSelectionByValue((Object) getRemarkIdsFromString(getLastItemValByItemInfoId(item.ItemInfID)));
			if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal!= null && TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length()>0){
				//myRemarkcheckList.setSelectionByValue(-1);
				myRemarkcheckList.setSelectionByValue(getRemarkIdsFromString(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal));
			}
		}
		else if (item.AmountTypID == ItemValueType.Mamooli) {
			dialog.clearAllPanel();
			dialog.setTitle(item.ItemName)
					.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
					.addContentXml(R.layout.body_item_meghdari)
					.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
						@Override
						public void onClick(View v) {
							showJozeeatDialog(item);
						}
					});
			TextView txtMamooliMinTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMinTitle);
			TextView txtMamooliMin = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMin);
			TextView txtMamooliMaxTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMaxTitle);
			TextView txtMamooliMax = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMax);
			TextView txtMamooliUnitTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariUnitTitle);
			TextView txtMamooliUnit = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariUnit);
			edtItemMamooli = (EditText) dialog.getDialog().findViewById(R.id.edtItemMeghdari);
			edtItemMamooli.setTypeface(tf);
			//edtItemMamooli.setFilters(new InputFilter[] { filter });
			txtMamooliMinTitle.setTypeface(tf);
			txtMamooliMin.setTypeface(tf);
			txtMamooliMaxTitle.setTypeface(tf);
			txtMamooliMax.setTypeface(tf);
			txtMamooliUnitTitle.setTypeface(tf);
			txtMamooliUnit.setTypeface(tf);
			final MyCheckListItem checkListItemOut = new MyCheckListItem("OUT", "OUT",false);
			final MyCheckListItem checkListItemReserve = new MyCheckListItem("RES", "RES",false);
			final MyCheckListItem checkListItemRemark = new MyCheckListItem("REM", "REM",false);
			final MyCheckList myCheckList1 = new MyCheckList(G.context,checkListItemOut,checkListItemReserve);



			final ArrayList<dtoRemarks> remarks2 = G.DB.getRemarksByRemarkGroupId(item.RemGroupID);
			final MyCheckList myRemarkcheckList2 =
					new MyCheckList(G.context, new MyCheckListItem("", "",false),new MyCheckListItem("", "",false));

			edtItemMamooli.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					if(editable.toString().equals("")==false) {
						myCheckList1.setSelectionByValue(-1);
						myRemarkcheckList2.setSelectionByValue(-1);
					}
				}
			});

			myCheckList1.addCheckItem(checkListItemRemark)
					.setCheckListOrientation(LinearLayout.HORIZONTAL)
					.setSelectionMode(MyCheckListMode.SingleSelection)
					.setCheckItemsHeight(50);
			myCheckList1.setOnCheckListItemClickListener(new OnCheckListItemClickListener() {
				@Override
				public void onCheckListItemClick(MyCheckListItem selectedCheckListItem, Boolean isChecked) {
					if(isChecked) {
						if (myCheckList1.getSelectedCheckListItems().size() > 0) {
							edtItemMamooli.setText("");
							edtItemMamooli.setEnabled(false);
							edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(drawable.edittext_dark));
							myRemarkcheckList2.setVisibility(View.GONE);
						} else {
							edtItemMamooli.setEnabled(true);
							edtItemMamooli.setVisibility(View.VISIBLE);
							edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(drawable.edittext_light));
							myRemarkcheckList2.setVisibility(View.GONE);
						}
						if (selectedCheckListItem.Text == "REM") {
							myRemarkcheckList2.setVisibility(isChecked ? View.VISIBLE : View.GONE);
						} else if (isChecked) {
							myRemarkcheckList2.setVisibility(View.GONE);
						}
					}
				}
			});
			dialog.setVerticalMargin(8);
			dialog.addContentView(myCheckList1);
			dialog.setVerticalMargin(8);

			if(remarks2.size()>0){
				myRemarkcheckList2.removeAllCkeckItems();
				for(int i=0;i<remarks2.size();i++){
					myRemarkcheckList2.addCheckItem(new MyCheckListItem(remarks2.get(i).RemName,remarks2.get(i).RemID.toString(),false));
				}
				myRemarkcheckList2.setCheckListOrientation(LinearLayout.VERTICAL)
						.setCheckItemsHeight(60);
				if(item.RemTyp==0)
					myRemarkcheckList2.setSelectionMode(MyCheckListMode.MultiSelection);
				else
					myRemarkcheckList2.setSelectionMode(MyCheckListMode.SingleSelection);
					myRemarkcheckList2.setVisibility(View.GONE);
					dialog.addContentView(myRemarkcheckList2);
			}
			else{
				dialog	.setTitle(item.ItemName)
						.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
						.addContentXml(R.layout.body_one_textview);
				TextView txtBodyMessage = (TextView) dialog.getDialog().findViewById(R.id.txtBodymessage);
				txtBodyMessage.setText((String)G.context.getResources().getText(R.string.NoDataRemark));
				txtBodyMessage.setTypeface(tf);
			}
			if(G.RTL) {
				dialog
						.addButton((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {


								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}

								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (myCheckList1.getSelectedCheckListItems().size() > 0) {
										if (myCheckList1.getSelectedCheckListItems().get(0).Text == "REM") {
											if (myRemarkcheckList2.getSelectedCheckListItems().size() > 0) {
												TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = getStringFromRemarks(myRemarkcheckList2.getSelectedCheckListItems());
												TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
												TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

											} else {
												TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
											}
										} else// if(myCheckList1.getSelectedCheckListItems().get(0).Text=="OUT" || myCheckList1.getSelectedCheckListItems().get(0).Text=="RESERVE")
										{
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = myCheckList1.getSelectedCheckListItems().get(0).Value.toString();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										}
									} else {
										String itemval;
										if(edtItemMamooli.getText().toString().trim().equals("")){
											if(edtItemMamooli.getHint().toString().equals("")){
												MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForValidText), Toast.LENGTH_SHORT);
												return;
											}else{
												itemval=edtItemMamooli.getHint().toString().trim();
											}
										}else{
											itemval=edtItemMamooli.getText().toString().trim();
										}
										if (!checkValidations(item,itemval))
											return;


										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal =  itemval;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
									}
									item.HasValueInRange=true;
									if(G.isSave){
										if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
											G.isSave = true;
											G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
										}
									}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}


							}

						})

						.addButton((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {


								dialog.dismiss();
							}
						})
						.addButton((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
							@Override
							public void onClick(View v) {


								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (myCheckList1.getSelectedCheckListItems().size() > 0) {
										if (myCheckList1.getSelectedCheckListItems().get(0).Text == "REM") {
											if (myRemarkcheckList2.getSelectedCheckListItems().size() > 0) {
												TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = getStringFromRemarks(myRemarkcheckList2.getSelectedCheckListItems());
												TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime(); //Tarikh.getCurrentShamsidateWithoutSlash();
												TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
											} else {
												TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
											}
										} else// if(myCheckList1.getSelectedCheckListItems().get(0).Text=="OUT" || myCheckList1.getSelectedCheckListItems().get(0).Text=="RESERVE")
										{
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = myCheckList1.getSelectedCheckListItems().get(0).Value.toString();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										}
									} else {

										String itemval;
										if(edtItemMamooli.getText().toString().trim().equals("")){
											if(edtItemMamooli.getHint().toString().equals("")){
												MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForValidText), Toast.LENGTH_SHORT);
												return;
											}else{
												itemval=edtItemMamooli.getHint().toString().trim();
											}
										}else{
											itemval=edtItemMamooli.getText().toString().trim();
										}
										if (!checkValidations(item, itemval))
											return;


										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = itemval;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
									}

//							if(G.isSave){
//								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//									G.isSave = true;
//									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//								}
//							}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == 0) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Left;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
								}
								if (edtItemMamooli.getText().toString().equals("") == false) {
									myCheckList1.setSelectionByValue(null);
									myRemarkcheckList2.setSelectionByValue(null);
								}
							}
						});
				if (G.isMultiMedia) {
					dialog.addButton((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}
			else{
				edtItemMamooli.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//hide_keyboard(v);
					}
				});
				edtItemMamooli.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
//						hide_keyboard(v);
						if(keyCode==KeyEvent.KEYCODE_POUND){

							ClickButton++;
							if(ClickButton==2) {
								if(edtItemMamooli.getText().toString().indexOf(".")>-1){

									return false;
								}
								edtItemMamooli.setText(edtItemMeghdari.getText().toString() + ".");
								edtItemMamooli.setSelection(edtItemMeghdari.getText().toString().length());
								ClickButton=0;
								return true;
							}
							if(ClickButton==4) {
								if(edtItemMamooli.getText().toString().indexOf("-")>-1){

									return false;
								}
								ClickButton=0;
								edtItemMamooli.setText("-"+edtItemMeghdari.getText().toString());
								edtItemMamooli.setSelection(edtItemMeghdari.getText().toString().length());
								return true;
							}
						}
						return false;
					}
				});
				dialog.clearButtonPanelLR();
				dialog.addButtonL((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
					@Override
					public void onClick(View v) {


						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
							if (myCheckList1.getSelectedCheckListItems().size() > 0) {
								if (myCheckList1.getSelectedCheckListItems().get(0).Text == "REM") {
									if (myRemarkcheckList2.getSelectedCheckListItems().size() > 0) {
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = getStringFromRemarks(myRemarkcheckList2.getSelectedCheckListItems());
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime(); //Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
									} else {
										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
									}
								} else// if(myCheckList1.getSelectedCheckListItems().get(0).Text=="OUT" || myCheckList1.getSelectedCheckListItems().get(0).Text=="RESERVE")
								{
									TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = myCheckList1.getSelectedCheckListItems().get(0).Value.toString();
									TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
									TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
								}
							} else {
								String itemval;
								if(edtItemMamooli.getText().toString().trim().equals("")){
									if(edtItemMamooli.getHint().toString().equals("")){
										MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForValidText), Toast.LENGTH_SHORT);
										return;
									}else{
										itemval=edtItemMamooli.getHint().toString().trim();
									}
								}else{
									itemval=edtItemMamooli.getText().toString().trim();
								}

								if (!checkValidations(item, itemval))
									return;

								TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = itemval;
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
								TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
							}
//							if(G.isSave){
//								if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
//									G.isSave = true;
//									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
//								}
//							}
							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if (pos == 0) {
							dialog.dismiss();
						} else {
							itemDirection = ItemDirection.Left;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
						}
						if (edtItemMamooli.getText().toString().equals("") == false) {
							myCheckList1.setSelectionByValue(null);
							myRemarkcheckList2.setSelectionByValue(null);
						}
					}
				})
						.addButtonR((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {


								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									if (myCheckList1.getSelectedCheckListItems().size() > 0) {
										if (myCheckList1.getSelectedCheckListItems().get(0).Text == "REM") {
											if (myRemarkcheckList2.getSelectedCheckListItems().size() > 0) {
												TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = getStringFromRemarks(myRemarkcheckList2.getSelectedCheckListItems());
												TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
												TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
											} else {
												TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
											}
										} else// if(myCheckList1.getSelectedCheckListItems().get(0).Text=="OUT" || myCheckList1.getSelectedCheckListItems().get(0).Text=="RESERVE")
										{
											TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = myCheckList1.getSelectedCheckListItems().get(0).Value.toString();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
											TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
										}
									} else {
										String itemval;
										if(edtItemMamooli.getText().toString().trim().equals("")){
											if(edtItemMamooli.getHint().toString().equals("")){
												MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForValidText), Toast.LENGTH_SHORT);
												return;
											}else{
												itemval=edtItemMamooli.getHint().toString().trim();
											}
										}else{
											itemval=edtItemMamooli.getText().toString().trim();
										}

										if (!checkValidations(item, itemval))
											return;

										TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = itemval;
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
										TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
									}
									item.HasValueInRange=true;
									if(G.isSave){
										if(TabFragmentItem.dicItemValues.get(item.ItemInfID)!=null) {
											G.isSave = true;
											G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
										}
									}
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									TabFragmentItem.imgSaveItem.requestFocus();
									dialog.dismiss();

								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}


							}

						})
						.addButtonR((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {


								dialog.dismiss();
							}
						});
				if (G.isMultiMedia) {
					dialog.addButtonL((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}

			if(!G.currentActivity.isFinishing())
			{
				dialog.show();
			}

			txtMamooliMin.setText(MyUtilities.changeNumberLocaleString(item.MinAmount1));
			txtMamooliMax.setText(MyUtilities.changeNumberLocaleString(item.MaxAmount1));
			txtMamooliUnit.setText(item.MeasureUnitName.toString());

			//Load if has value
			//edtItemMamooli.setHint(getLastItemValByItemInfoId(item.ItemInfID));
           //ArrayList<dtoItemValues> dtoItemValues= G.DB.getItemValuesByItemInfoId(item.ItemInfID);
           String ItemValues= getLastItemValByItemInfoId(item.ItemInfID);

			   if (ItemValues !=null  || ItemValues.equals("")==false) {

				   //dtoItemValues lastItemValue = new dtoItemValues();
				  // lastItemValue = dtoItemValues.get(dtoItemValues.size() - 1);

				   if (ItemValues.trim().equals(checkListItemOut.Value.toString())) {
					   myCheckList1.setSelectionByValue(checkListItemOut.Value);
					   //edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(R.drawable.edittext_dark));
					   //edtItemMamooli.setEnabled(false);
					   myRemarkcheckList2.setVisibility(View.GONE);
				   }
				   else if (ItemValues.trim().equals(checkListItemReserve.Value.toString())) {
					   myCheckList1.setSelectionByValue(checkListItemReserve.Value);
					   //edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(R.drawable.edittext_dark));
					   //edtItemMamooli.setEnabled(false);
					   myRemarkcheckList2.setVisibility(View.GONE);
				   }
				   else if (ItemValues.trim().contains(",")) {
					   myRemarkcheckList2.setVisibility(View.VISIBLE);
					   myCheckList1.setSelectionByValue(checkListItemRemark.Value);
					   myRemarkcheckList2.setSelectionByValue(getRemarkIdsFromString(ItemValues));
					   //edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(R.drawable.edittext_dark));
					   //edtItemMamooli.setEnabled(false);
				   }
				   else {
					   edtItemMamooli.setHint(ItemValues.trim());
					   edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(drawable.edittext_light));
					   edtItemMamooli.setEnabled(true);
					   myRemarkcheckList2.setVisibility(View.GONE);
				   }
			   }
			   if (TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal != null && TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length() > 0) {
				   if (TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().equals(checkListItemOut.Value.toString())) {
					   myCheckList1.setSelectionByValue(checkListItemOut.Value);
					   edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(drawable.edittext_dark));
					   edtItemMamooli.setEnabled(false);
					   myRemarkcheckList2.setVisibility(View.GONE);
				   } else if (TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().equals(checkListItemReserve.Value.toString())) {
					   myCheckList1.setSelectionByValue(checkListItemReserve.Value);
					   edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(drawable.edittext_dark));
					   edtItemMamooli.setEnabled(false);
					   myRemarkcheckList2.setVisibility(View.GONE);
				   } else if (TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().contains(",")) {
					   myRemarkcheckList2.setVisibility(View.VISIBLE);
					   myCheckList1.setSelectionByValue(checkListItemRemark.Value);
					   myRemarkcheckList2.setSelectionByValue(getRemarkIdsFromString(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal));
					   edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(drawable.edittext_dark));
					   edtItemMamooli.setEnabled(false);
				   } else {
					   edtItemMamooli.setText(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim());
					   edtItemMamooli.setBackgroundDrawable(G.context.getResources().getDrawable(drawable.edittext_light));
					   edtItemMamooli.setEnabled(true);
					   edtItemMamooli.selectAll();
					   myRemarkcheckList2.setVisibility(View.GONE);
				   }
			   }


		}
		else if (item.AmountTypID == ItemValueType.Mohasebati) {
			dialog.clearAllPanel();
			dialog	.setTitle(item.ItemName)
					.setRightTitle(String.format("%d/%d", pos+1,TabFragmentItem.arrListItemFiltered.size()))
					.addContentXml(R.layout.body_item_meghdari)
					.setLeftImageTitle(G.context.getResources().getDrawable(drawable.info_white),new OnClickListener() {
						@Override
						public void onClick(View v) {
							showJozeeatDialog(item);
						}
					});
			LinearLayout llMaxValue = (LinearLayout) dialog.getDialog().findViewById(R.id.llMaxValue);
			LinearLayout llMinValue = (LinearLayout) dialog.getDialog().findViewById(R.id.llMinValue);
			LinearLayout llUnit = (LinearLayout) dialog.getDialog().findViewById(R.id.llUnit);
			TextView txtFormulaMinTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMinTitle);
			TextView txtFormulaMin = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMin);
			TextView txtFormulaMaxTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMaxTitle);
			TextView txtFormulaMax = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariMax);
			TextView txtFormulaUnitTitle = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariUnitTitle);
			TextView txtFormulaUnit = (TextView) dialog.getDialog().findViewById(R.id.txtMeghdariUnit);
			final EditText edtItemFormula = (EditText) dialog.getDialog().findViewById(R.id.edtItemMeghdari);
			edtItemFormula.setTypeface(tf);
			txtFormulaMinTitle.setTypeface(tf);
			txtFormulaMin.setTypeface(tf);
			txtFormulaMaxTitle.setTypeface(tf);
			txtFormulaMax.setTypeface(tf);
			txtFormulaUnitTitle.setTypeface(tf);
			txtFormulaUnit.setTypeface(tf);

			dtoItemFormulas itemFormulas = G.DB.getItemFormula(item.ItemInfID);
			if(itemFormulas!=null) {
				dtoFormulas formula = G.DB.getFormula(itemFormulas.FormulaID);

				if (formula.FormulID == FormulaType.MIANGIN) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.Avarage));
						}
					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {
						int count = 0;
						Double sum = 0.;
						for (dtoItems oItem : TabFragmentItem.arrListItem) {
							if(TabFragmentItem.dicItemValues.get(oItem.ItemInfID)!=null) {
								if (Arrays.asList(itemFormulas.ItemSelect.trim().split(",")).contains(oItem.ItemInfID.toString())) {
									if (TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim().length() > 0
											&& MyUtilities.isNumber(MyUtilities.changeNumberLocaleString(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal))) {
										sum += Double.valueOf(MyUtilities.changeNumberLocaleString(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal));
										count++;
									}
								}
							}
						}
						if (count > 0) {

							String valueMain = MyUtilities.changeNumberLocaleString(String.format("%.2f",(sum / count)));

							txtFormulaUnit.setText(valueMain);
						}else {
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
				else if (formula.FormulID == FormulaType.EKHTELAF_MIN_MAX) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.MinAndMaxDifference));
						}
					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {

						Double min = null;
						Double max = null;
						int count = 0;
						for (dtoItems oItem : TabFragmentItem.arrListItem) {
							if(TabFragmentItem.dicItemValues.get(oItem.ItemInfID)!=null) {
								if (Arrays.asList(itemFormulas.ItemSelect.trim().split(",")).contains(oItem.ItemInfID.toString())) {
									if (TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim().length() > 0
											&& MyUtilities.isNumber(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal)) {
										if (min == null) {
											min = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
											max = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
										}
										if (min > Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal))
											min = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
										if (max < Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal))
											max = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
										count++;
									}
								}
							}
						}

						if (count > 1) {
							String valueMain=MyUtilities.changeNumberLocaleString(String.format("%.2f",(max - min)));
							if(valueMain.contains(".")){
								int firstPoint=valueMain.indexOf(".");
								int endPoint= valueMain.indexOf(".")+2;
								if(valueMain.length()>endPoint) {
									valueMain = valueMain.substring(0,firstPoint) +valueMain.substring(firstPoint+1, endPoint);
								}
							}
							txtFormulaUnit.setText(valueMain);
						}
						else {
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
				else if (formula.FormulID == FormulaType.TAFAZOL_2_ITEM) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.TwoItemsDifference));
						}
					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {
						Integer itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
						Integer itemId2 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[1]);
						String itemVal1="";
						String itemVal2="";
						if(TabFragmentItem.dicItemValues.get(itemId1)!=null) {
							itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
						}
						if(TabFragmentItem.dicItemValues.get(itemId2)!=null) {
							itemVal2 = TabFragmentItem.dicItemValues.get(itemId2).ItemVal;
						}

						if (itemVal1.trim().length() > 0 && itemVal2.trim().length() > 0
								&& MyUtilities.isNumber(itemVal1.trim()) && MyUtilities.isNumber(itemVal2.trim())) {
							Double digit1 = Double.valueOf(itemVal1.trim());
							Double digit2 = Double.valueOf(itemVal2.trim());
							txtFormulaUnit.setText(MyUtilities.changeNumberLocaleString(String.format("%.2f",(digit1 - digit2))));
						} else {
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
				else if (formula.FormulID == FormulaType.TAFAZOL_SAATI) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.HourDifference));
						}

					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {
						Integer itemId1 = 0;
						if (itemFormulas.ItemSelect.contains(",")) {
							itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
						} else {
							itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim());
						}
						String itemVal1="";
						if(TabFragmentItem.dicItemValues.get(itemId1)!=null) {
							itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
						}

						if (itemVal1.trim().length() > 0) {
							ArrayList<dtoItemValues> itemLastValues = G.DB.getItemValuesByItemInfoId(itemId1);
							String itemVal2 = null;
							if (itemLastValues.size() > 0) {
								itemVal2 = itemLastValues.get(0).ItemVal;
							} else {
								txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_NoDataInLastCor));
							}
							if (itemVal2 != null && itemVal2.trim().length() > 0 && MyUtilities.isNumber(itemVal2.trim())
									&& itemVal1 != null && itemVal1.trim().length() > 0 && MyUtilities.isNumber(itemVal1.trim())) {
								Double digit1 = Double.valueOf(itemVal1.trim());
								Double digit2 = Double.valueOf(itemVal2.trim());
								txtFormulaUnit.setText(MyUtilities.changeNumberLocaleString(String.format("%.2f",(digit1 - digit2))));
							} else {
								txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_NoDataInLastCor));
							}

						} else {
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
				else if (formula.FormulID == FormulaType.MAJMOO_TAFAZOL_SAATI) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.SumOfHourDifference));
						}

					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {
						Integer itemId1 = 0;
						if (itemFormulas.ItemSelect.contains(",")) {
							itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
						} else {
							itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim());
						}
						String itemVal1="";
						if(TabFragmentItem.dicItemValues.get(itemId1)!=null) {
							itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
						}
						if (itemVal1.trim().length() > 0) {
							ArrayList<dtoItemValues> itemLastValues = G.DB.getItemValuesByItemInfoId(itemId1);
							ArrayList<Double> tafazolList = new ArrayList<Double>();
							if (itemLastValues.size() > 0) {
								if (itemLastValues.get(0).ItemVal != null && itemLastValues.get(0).ItemVal.trim().length() > 0
										&& MyUtilities.isNumber(itemVal1.trim()) && MyUtilities.isNumber(itemLastValues.get(0).ItemVal.trim())) {
									Double digit1 = Double.valueOf(itemVal1.trim());
									tafazolList.add(digit1 - Double.valueOf(itemLastValues.get(0).ItemVal.trim()));
								}
								for (int i = 0; i < itemLastValues.size() - 1; i++) {
									if (itemLastValues.get(i).ItemVal != null && itemLastValues.get(i).ItemVal.trim().length() > 0
											&& MyUtilities.isNumber(itemLastValues.get(i).ItemVal.trim())
											&& MyUtilities.isNumber(itemLastValues.get(i + 1).ItemVal.trim())) {
										tafazolList.add(Double.valueOf(itemLastValues.get(i).ItemVal.trim()) - Double.valueOf(itemLastValues.get(i + 1).ItemVal.trim()));
									}
								}
							} else {
								txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_NoDataInLastCor));
							}
							if (tafazolList.size() > 0) {
								Double sum = 0.;
								for (Double s : tafazolList) {
									sum += s;
								}
								txtFormulaUnit.setText(MyUtilities.changeNumberLocaleString(String.format("%.2f",sum)));
							} else {
								txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_NoDataInLastCor));
							}
						} else {
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
				else if (formula.FormulID == FormulaType.MAJMOO) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.Sum));
						}

					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {
						int count = 0;
						Double sum = 0.;
						for (dtoItems oItem : TabFragmentItem.arrListItem) {
							if (Arrays.asList(itemFormulas.ItemSelect.trim().split(",")).contains(oItem.ItemInfID.toString())) {
								if (TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim().length() > 0
										&& MyUtilities.isNumber(MyUtilities.changeNumberLocaleString(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim()))) {
									sum += Double.valueOf(MyUtilities.changeNumberLocaleString(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal));
									count++;
								}
							}
						}
						if (count > 0)
							txtFormulaUnit.setText(MyUtilities.changeNumberLocaleString(String.format("%.2f",sum)));
						else {
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
				else if (formula.FormulID == FormulaType.COPY) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.Copy));
						}

					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0 ) {
						Integer itemId = 0;
						if (itemFormulas.ItemSelect.contains(",")) {
							itemId = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
						} else {
							itemId = Integer.valueOf(itemFormulas.ItemSelect.trim());
						}
						if (TabFragmentItem.dicItemValues.get(itemId) != null) {
							if (TabFragmentItem.dicItemValues.get(itemId).ItemVal.trim().length() > 0) {

								txtFormulaUnit.setText(TabFragmentItem.dicItemValues.get(itemId).ItemVal);

							} else {
								txtFormulaUnitTitle.setVisibility(View.GONE);
								txtFormulaUnit.setText((String) G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
							}
						} else {
							txtFormulaUnit.setText((String) G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
						}
					}else{
						txtFormulaUnitTitle.setVisibility(View.GONE);
						txtFormulaUnit.setText((String) G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
					}
				}
				else if (formula.FormulID == FormulaType.ZARB) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.Multiplication));
						}

					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {
						Integer itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
						Integer itemId2 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[1]);
						String itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
						String itemVal2 = TabFragmentItem.dicItemValues.get(itemId2).ItemVal;

						if (itemVal1.trim().length() > 0 && itemVal2.trim().length() > 0
								&& MyUtilities.isNumber(itemVal1.trim()) && MyUtilities.isNumber(itemVal2.trim())) {
							Double digit1 = Double.valueOf(itemVal1.trim());
							Double digit2 = Double.valueOf(itemVal2.trim());
							txtFormulaUnit.setText(MyUtilities.changeNumberLocaleString(String.format("%.2f",(digit1 * digit2))));
						} else {
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
				else if (formula.FormulID == FormulaType.TAGHSIM) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.Division));
						}

					} else {
						txtFormulaUnitTitle.setText("-");
					}
					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {
						Integer itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
						Integer itemId2 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[1]);
						String itemVal1="",itemVal2="";
						if(TabFragmentItem.dicItemValues.get(itemId1)!=null) {
							itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
						}
						if(TabFragmentItem.dicItemValues.get(itemId2)!=null) {
							itemVal2 = TabFragmentItem.dicItemValues.get(itemId2).ItemVal;
						}

						if (itemVal1.trim().length() > 0 && itemVal2.trim().length() > 0
								&& MyUtilities.isNumber(itemVal1.trim()) && MyUtilities.isNumber(itemVal2.trim())) {
							Double digit1 = Double.valueOf(itemVal1.trim());
							Double digit2 = Double.valueOf(itemVal2.trim());
							if (digit2 != 0) {
								txtFormulaUnit.setText(MyUtilities.changeNumberLocaleString(String.format("%.2f",(digit1 / digit2))));
							} else {
								txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_DivForConstNumber));
							}
						} else {
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
				else if (formula.FormulID == FormulaType.TAGHSIM_BAR_SABET) {
					llMaxValue.setVisibility(View.GONE);
					llMinValue.setVisibility(View.GONE);
					edtItemFormula.setVisibility(View.GONE);
					if (formula != null && formula.FormulName.trim().length() > 0) {
						txtFormulaUnitTitle.setText(formula.FormulName.trim());
						if(G.RTL==false) {
							txtFormulaUnitTitle.setText(G.context.getText(R.string.DivisionOnConstantNumber));
						}

					} else {
						txtFormulaUnitTitle.setText("-");
					}

					if (itemFormulas != null && itemFormulas.ItemSelect.trim().length() > 0) {
						Integer itemId = 0;
						if (itemFormulas.ItemSelect.contains(",")) {
							itemId = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
						} else {
							itemId = Integer.valueOf(itemFormulas.ItemSelect.trim());
						}
						if (TabFragmentItem.dicItemValues.get(itemId) != null){
							if (TabFragmentItem.dicItemValues.get(itemId).ItemVal.trim().length() > 0
									&& MyUtilities.isNumber(TabFragmentItem.dicItemValues.get(itemId).ItemVal)) {
								Double digit = Double.valueOf(TabFragmentItem.dicItemValues.get(itemId).ItemVal);
								if (itemFormulas.constantNumber != 0) {
									txtFormulaUnit.setText(MyUtilities.changeNumberLocaleString(String.format("%.2f",(digit / itemFormulas.constantNumber))));
								} else {
									txtFormulaUnit.setText((String) G.context.getResources().getText(R.string.Msg_DivForConstNumber));
								}
							} else {
								txtFormulaUnitTitle.setVisibility(View.GONE);
								txtFormulaUnit.setText((String) G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
							}
					}else{
							txtFormulaUnitTitle.setVisibility(View.GONE);
							txtFormulaUnit.setText((String) G.context.getResources().getText(R.string.Msg_CountItemForCalculate));
						}
					} else {
						txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
					}
				}
			}else {
				llMaxValue.setVisibility(View.GONE);
				llMinValue.setVisibility(View.GONE);
				edtItemFormula.setVisibility(View.GONE);
				txtFormulaUnit.setText((String)G.context.getResources().getText(R.string.Msg_SendDataFromServerFail));
				txtFormulaUnit.setTextSize(FontSize);
				txtFormulaUnitTitle.setVisibility(View.GONE);
			}
			if(G.RTL) {
				dialog
						.addButton((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {
								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime)
											, Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(1,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})

						.addButton((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						})
						.addButton((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
							@Override
							public void onClick(View v) {

								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(0,item);
								}


								if (pos == 0) {
									dialog.dismiss();
								} else {
									itemDirection = ItemDirection.Left;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
								}
							}
						})
						;
				if (G.isMultiMedia) {
					dialog.addButton((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}
			else{
				dialog.clearButtonPanelLR();
				dialog.addButtonL((String) G.context.getResources().getText(R.string.RegisterPreviuse), new OnClickListener() {
					@Override
					public void onClick(View v) {

						Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
						if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
							item.IsInTimeRange = false;
							MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
							AdapterItem.this.notifyDataSetChanged();
						}
						if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
							AdapterItem.this.notifyDataSetChanged();
							updateMohasebatiItems(0,item);
						}
						if (pos == 0) {
							dialog.dismiss();
						} else {
							itemDirection = ItemDirection.Left;
							showDialog(TabFragmentItem.arrListItemFiltered.get(pos - 1), pos - 1);
						}
					}
				})

						.addButtonR((String) G.context.getResources().getText(R.string.RegisterNext), new OnClickListener() {
							@Override
							public void onClick(View v) {
								Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
								if (IsValidForSubmit == ValidForSubmitType.OutOfRange) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingTime)
											, Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								} else if (IsValidForSubmit == ValidForSubmitType.InRangeButHasOtherValue) {
									item.IsInTimeRange = false;
									MyToast.Show(G.context, (String) G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
									AdapterItem.this.notifyDataSetChanged();
								}
								if (IsValidForSubmit == ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0) {
									AdapterItem.this.notifyDataSetChanged();
									updateMohasebatiItems(1,item);
								}
								if (pos == (TabFragmentItem.arrListItemFiltered.size() - 1)) {
									TabFragmentItem.imgSaveItem.requestFocus();
									dialog.dismiss();

								} else {
									itemDirection = ItemDirection.Right;
									showDialog(TabFragmentItem.arrListItemFiltered.get(pos + 1), pos + 1);
								}
							}
						})
						.addButtonR((String) G.context.getResources().getText(R.string.Cancel), new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						})
				;
				if (G.isMultiMedia) {
					dialog.addButtonL((String) G.context.getResources().getText(R.string.Multimedia), new OnClickListener() {
						@Override
						public void onClick(View view) {
							Gson gson = new Gson();
							dtoItemValues lastValueFromServer = G.DB.getLastItemValueByItemInfId(item.ItemInfID);
							Intent intent = new Intent(G.context, ActivityMultiMedia.class);
							String strClass = gson.toJson(lastValueFromServer);
							intent.putExtra("ItemInfoValues", strClass);
							intent.putExtra("ItemInfoId", String.valueOf(item.ItemInfID));
							intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
							G.context.startActivity(intent);

						}
					});
				}
			}

			if(!G.currentActivity.isFinishing())
			{
				dialog.show();
			}
		}
		else{
			//There is no type like this
		}



	}

	private void updateMohasebatiItems(int State,dtoItems item){

		//for(dtoItems item:TabFragmentItem.arrListItem) {
			dtoItemFormulas itemFormulas = G.DB.getItemFormula(item.ItemInfID);
			item.HasValueInRange=true;
			if(itemFormulas==null || item.IsFullOfMaxData ) {
				//continue;
				return;
			}
			if(itemFormulas.FormulaID == FormulaType.MIANGIN) {
				if (itemFormulas.ItemSelect.trim().length() > 0) {
					int count = 0;
					Double sum = 0.;
					for (dtoItems oItem : TabFragmentItem.arrListItem) {
						if (Arrays.asList(itemFormulas.ItemSelect.trim().split(",")).contains(oItem.ItemInfID.toString())) {
							if (TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim().length() > 0
									&& MyUtilities.isNumber(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim())) {
								Double value = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
								sum += value;
								count++;
							}
						}
					}
					if (count > 0) {
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(sum / count));
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
						if (State == 1) {
							if (G.isSave) {
								if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
									G.isSave = true;
									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
								}
							}
						}


					} else
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				} else {
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}


			}
			else if(itemFormulas.FormulaID == FormulaType.EKHTELAF_MIN_MAX){
				if(itemFormulas!=null && itemFormulas.ItemSelect.trim().length()>0){
					Double min = null;
					Double max = null;
					int count = 0;
					for(dtoItems oItem:TabFragmentItem.arrListItem){
						if(Arrays.asList(itemFormulas.ItemSelect.trim().split(",")).contains(oItem.ItemInfID.toString())){
							if(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim().length()>0
									&& MyUtilities.isNumber(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal)){
								if(min==null){
									min = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
									max = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
								}
								if(min>Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal))
									min = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
								if(max<Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal))
									max = Double.valueOf(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal);
								count++;
							}
						}
					}
					if(count>1) {
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(max - min));
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime(); //Tarikh.getCurrentShamsidateWithoutSlash();
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();

						if (State == 1) {
							if (G.isSave) {
								if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
									G.isSave = true;
									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
								}
							}
						}
					}
					else
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}else{
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}
			else if(itemFormulas.FormulaID == FormulaType.TAFAZOL_2_ITEM){
				if(itemFormulas!=null && itemFormulas.ItemSelect.trim().length()>0){
					Integer itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
					Integer itemId2 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[1]);
					String itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
					String itemVal2 = TabFragmentItem.dicItemValues.get(itemId2).ItemVal;

					if(itemVal1.trim().length()>0 && itemVal2.trim().length()>0
							&& MyUtilities.isNumber(itemVal1.trim()) && MyUtilities.isNumber(itemVal2.trim())){
						Double digit1 = Double.valueOf(itemVal1.trim());
						Double digit2 = Double.valueOf(itemVal2.trim());
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(digit1-digit2));
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
						if (State == 1) {
							if (G.isSave) {
								if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
									G.isSave = true;
									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
								}
							}
						}


					}else{
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
					}
				}else{
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}
			else if(itemFormulas.FormulaID == FormulaType.TAFAZOL_SAATI){
				if(itemFormulas!=null && itemFormulas.ItemSelect.trim().length()>0){
					Integer itemId1 = 0;
					if(itemFormulas.ItemSelect.contains(",")){
						itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
					}else{
						itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim());
					}
					String itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
					if(itemVal1.trim().length()>0){
						ArrayList<dtoItemValues> itemLastValues = G.DB.getItemValuesByItemInfoId(itemId1);
						String itemVal2 = null;
						if(itemLastValues.size()>0){
							itemVal2 = itemLastValues.get(0).ItemVal;
						}else{
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
						}
						if(itemVal2!=null && itemVal2.trim().length()>0 && MyUtilities.isNumber(itemVal2)
								&& itemVal1!=null && itemVal1.trim().length()>0 && MyUtilities.isNumber(itemVal1)){
							Double digit1 = Double.valueOf(itemVal1.trim());
							Double digit2 = Double.valueOf(itemVal2.trim());
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(digit1-digit2));
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
							if (State == 1) {
								if (G.isSave) {
									if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
										G.isSave = true;
										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
									}
								}
							}

						}else{
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
						}

					}else{
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
					}
				}else{
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}
			else if(itemFormulas.FormulaID == FormulaType.MAJMOO_TAFAZOL_SAATI){
				if(itemFormulas!=null && itemFormulas.ItemSelect.trim().length()>0){
					Integer itemId1 = 0;
					if(itemFormulas.ItemSelect.contains(",")){
						itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
					}else{
						itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim());
					}
					String itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
					if(itemVal1.trim().length()>0){
						ArrayList<dtoItemValues> itemLastValues = G.DB.getItemValuesByItemInfoId(itemId1);
						ArrayList<Double> tafazolList = new ArrayList<Double>();
						if(itemLastValues.size()>0){
							if(itemLastValues.get(0).ItemVal!=null && itemLastValues.get(0).ItemVal.trim().length()>0
									&& MyUtilities.isNumber(itemVal1.trim()) && MyUtilities.isNumber(itemLastValues.get(0).ItemVal.trim())){
								Double digit1 = Double.valueOf(itemVal1.trim());
								tafazolList.add(digit1-Double.valueOf(itemLastValues.get(0).ItemVal.trim()));
							}
							for(int i=0;i<itemLastValues.size()-1;i++){
								if(itemLastValues.get(i).ItemVal!=null && itemLastValues.get(i).ItemVal.trim().length()>0
										&& MyUtilities.isNumber(itemLastValues.get(i).ItemVal.trim())
										&& MyUtilities.isNumber(itemLastValues.get(i+1).ItemVal.trim())){
									tafazolList.add(Double.valueOf(itemLastValues.get(i).ItemVal.trim())-Double.valueOf(itemLastValues.get(i+1).ItemVal.trim()));
								}
							}
						}else{
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
						}
						if(tafazolList.size()>0){
							Double sum = 0.;
							for(Double s :tafazolList){
								sum+=s;
							}
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(sum));
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
							if (State == 1) {
								if (G.isSave) {
									if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
										G.isSave = true;
										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
									}
								}
							}


						}else{
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
						}
					}else{
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
					}
				}else{
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}
			else if(itemFormulas.FormulaID == FormulaType.MAJMOO){
				if(itemFormulas.ItemSelect.trim().length()>0){
					int count = 0;
					Double sum = 0.;
					for(dtoItems oItem:TabFragmentItem.arrListItem){
						if(Arrays.asList(itemFormulas.ItemSelect.trim().split(",")).contains(oItem.ItemInfID.toString())){
							if(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim().length()>0
									&& MyUtilities.isNumber(MyUtilities.changeNumberLocaleString(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal.trim())) ){
								Double value = Double.valueOf(MyUtilities.changeNumberLocaleString(TabFragmentItem.dicItemValues.get(oItem.ItemInfID).ItemVal));
								sum += value;
								count++;
							}
						}
					}
					if(count>0) {
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(sum));
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
						if (State == 1) {
							if (G.isSave) {
								if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
									G.isSave = true;
									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
								}
							}
						}

					}
					else
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}else{
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}
			else if(itemFormulas.FormulaID == FormulaType.COPY){
				if(itemFormulas!=null && itemFormulas.ItemSelect.trim().length()>0) {
					Integer itemId = 0;
					if (itemFormulas.ItemSelect.contains(",")) {
						itemId = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
					} else {
						itemId = Integer.valueOf(itemFormulas.ItemSelect.trim());
					}
					if (TabFragmentItem.dicItemValues.get(itemId) != null) {
						if (TabFragmentItem.dicItemValues.get(itemId).ItemVal.trim().length() > 0) {
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = TabFragmentItem.dicItemValues.get(itemId).ItemVal;
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
							if (State == 1) {
								if (G.isSave) {
									if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
										G.isSave = true;
										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
									}
								}
							}

						} else {
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
						}
					} else {
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
					}
				}else {
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}
			else if(itemFormulas.FormulaID == FormulaType.ZARB){
				if(itemFormulas!=null && itemFormulas.ItemSelect.trim().length()>0){
					Integer itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
					Integer itemId2 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[1]);
					String itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;
					String itemVal2 = TabFragmentItem.dicItemValues.get(itemId2).ItemVal;

					if(itemVal1.trim().length()>0 && itemVal2.trim().length()>0
							&& MyUtilities.isNumber(itemVal1.trim()) && MyUtilities.isNumber(itemVal2.trim())){
						Double digit1 = Double.valueOf(itemVal1.trim());
						Double digit2 = Double.valueOf(itemVal2.trim());
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(digit1*digit2));
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
						TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
						if (State == 1) {
							if (G.isSave) {
								if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
									G.isSave = true;
									G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
								}
							}
						}
					}else{
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
					}
				}else{
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}
			else if(itemFormulas.FormulaID == FormulaType.TAGHSIM){
				if(itemFormulas!=null && itemFormulas.ItemSelect.trim().length()>0){
					Integer itemId1 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
					Integer itemId2 = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[1]);
					String itemVal1="",itemVal2="";
					if(TabFragmentItem.dicItemValues.get(itemId1)!=null) {
						itemVal1 = TabFragmentItem.dicItemValues.get(itemId1).ItemVal;

					}
					if(TabFragmentItem.dicItemValues.get(itemId2)!=null) {
						itemVal2 = TabFragmentItem.dicItemValues.get(itemId2).ItemVal;
					}

					if(itemVal1.trim().length()>0 && itemVal2.trim().length()>0
							&& MyUtilities.isNumber(itemVal1.trim()) && MyUtilities.isNumber(itemVal2.trim())){
						Double digit1 = Double.valueOf(itemVal1.trim());
						Double digit2 = Double.valueOf(itemVal2.trim());
						if(digit2!=0){
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(digit1/digit2));
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate = Tarikh.getCurrentMiladidatetime();//Tarikh.getCurrentShamsidateWithoutSlash();
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
							if (State == 1) {
								if (G.isSave) {
									if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
										G.isSave = true;
										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
									}
								}
							}

						}else{
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
						}
					}else{
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
					}
				}else{
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}
			else if(itemFormulas.FormulaID == FormulaType.TAGHSIM_BAR_SABET){
				if(itemFormulas!=null && itemFormulas.ItemSelect.trim().length()>0){
					Integer itemId = 0;
					if(itemFormulas.ItemSelect.contains(",")){
						itemId = Integer.valueOf(itemFormulas.ItemSelect.trim().split(",")[0]);
					}else{
						itemId = Integer.valueOf(itemFormulas.ItemSelect.trim());
					}
					if(TabFragmentItem.dicItemValues.get(itemId).ItemVal.trim().length()>0
							&& MyUtilities.isNumber(TabFragmentItem.dicItemValues.get(itemId).ItemVal.trim())){
						Double digit = Double.valueOf(TabFragmentItem.dicItemValues.get(itemId).ItemVal.trim());
						if(itemFormulas.constantNumber!=0){
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = MyUtilities.changeNumberLocaleString(String.valueOf(digit/itemFormulas.constantNumber));
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PDate =Tarikh.getCurrentMiladidatetime(); //Tarikh.getCurrentShamsidateWithoutSlash();
							TabFragmentItem.dicItemValues.get(item.ItemInfID).PTime = Tarikh.getTimeWithoutColon();
							if (State == 1) {
								if (G.isSave) {
									if (TabFragmentItem.dicItemValues.get(item.ItemInfID) != null) {
										G.isSave = true;
										G.DB.InsertItemValues(TabFragmentItem.dicItemValues.get(item.ItemInfID));
									}
								}
							}

						}else{
							TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
						}
					}else{
						TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
					}
				}
				else{
					TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal = "";
				}

			}

			AdapterItem.this.notifyDataSetChanged();
		//}
	}

	public   void showJozeeatDialog(dtoItems item){

		final MyDialog myDialog =  new MyDialog(G.currentActivity, R.style.DialogAnimFromBottom);
		if(G.RTL) {
			///جهت عوض کردن محل علا مت منفی
			if (item.MaxAmount3 != null) {
				item.MaxAmount3 = item.MaxAmount3.contains("-") == true ? item.MaxAmount3.substring(1) + "-" : item.MaxAmount3;
			}
			if (item.MinAmount3 != null) {
				item.MinAmount3 = item.MinAmount3.contains("-") == true ? item.MinAmount3.substring(1) + "-" : item.MinAmount3;
			}
			if (item.MaxAmount2 != null) {
				item.MaxAmount2 = item.MaxAmount2.contains("-") == true ? item.MaxAmount2.substring(1) + "-" : item.MaxAmount2;
			}
			if (item.MinAmount2 != null) {
				item.MinAmount2 = item.MinAmount2.contains("-") == true ? item.MinAmount2.substring(1) + "-" : item.MinAmount2;
			}
		}else{
			myDialog.setBodyMatchParent();
		}
		myDialog.setTitle((String) G.context.getResources().getText(R.string.Detail))
				.setBackgroundAlpha(0.95f)
				.setBodyMargin(30, 0, 30, 0)
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.UnitWithParam), G.selectedVahed),FontSize)
				.setContentSplitter()
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.LogshitwithParam), G.selectedLogshit),FontSize)
				.setContentSplitter()
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.EquipWithParam), G.selectedTajhiz),FontSize)
				.setContentSplitter()
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.SubEquipWithParam), G.selectedZirTajhiz),FontSize)
				.setContentSplitter()
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.UnitmeasurementWithParam), item.MeasureUnitName),FontSize)
				.setContentSplitter()
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.CountDataWithParam),String.valueOf(G.DB.getItemValuesByItemInfoId(item.ItemInfID).size())),FontSize)
				.setContentSplitter()
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.AllowNumberWithParam), item.MaxSampleNo),FontSize)
				.setContentSplitter()
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.CodeItemWithParam), item.ItemInfID),FontSize)
				.setContentSplitter()
				.addBodyText((String) G.context.getResources().getText(R.string.Secondlimitation),FontSize)
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.MinWitParam), item.MinAmount2==null?"-":item.MinAmount2),FontSize)
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.MaxWithParam), item.MaxAmount2==null?"-":item.MaxAmount2),FontSize)
				.setContentSplitter()
				.addBodyText((String) G.context.getResources().getText(R.string.Thirdlimitation),FontSize)
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.MinWitParam), item.MinAmount3==null?"-":item.MinAmount3),FontSize)
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.MaxWithParam), item.MaxAmount3==null?"-":item.MaxAmount3),FontSize)
				.setContentSplitter()
				.addBodyText(String.format((String) G.context.getResources().getText(R.string.ZaribWithParam), MyUtilities.changeNumberLocaleString(item.Zarib.toString())),FontSize)
				.addButton((String) G.context.getResources().getText(R.string.Close), new OnClickListener() {
					@Override
					public void onClick(View v) {
						myDialog.dismiss();
					}
				});
		if(!G.currentActivity.isFinishing())
		{
			myDialog.show();
		}
	}

	private String getLastItemValByItemInfoId(Integer itemInfoId) {
		String lastval = "";
		if(G.Setting.ShowLastData == 1) {
			ArrayList<dtoItemValues> lastValuesInDevice = G.DB.getItemValuesByItemInfoId(itemInfoId);
			if(lastValuesInDevice.size()>0) {
				if(G.RTL) {
					lastval = MyUtilities.changeNumberLocaleString(lastValuesInDevice.get(lastValuesInDevice.size() - 1).ItemVal.trim());

				}else{
					lastval=lastValuesInDevice.get(lastValuesInDevice.size() - 1).ItemVal.trim();
					lastval=lastval.substring(0,lastval.length()-1);
				}
			}else{
				dtoMaxItemVal lastValueFromServer = G.DB.getLastValueByItemInfoId(itemInfoId);
				if(lastValueFromServer!=null)
				{
					lastval = MyUtilities.changeNumberLocaleString(lastValueFromServer.ItemVal.trim());
//					if(lastValueFromServer.ItemValTyp.equals("=")){
//						lastval=lastValueFromServer.ItemVal.trim();
//					}
					if(lastValueFromServer.ItemValTyp.equals("6")){
						lastval=lastValueFromServer.ItemVal.trim();
					}
					if(lastValueFromServer.ItemValTyp.equals("3")){
						if(lastValueFromServer.ItemVal.contains(",")) {
							lastval = lastValueFromServer.ItemVal.trim()
									.substring(0, lastValueFromServer.ItemVal.trim().length() - 1);
						}else{
							lastval = lastValueFromServer.ItemVal.trim();
						}
					}
					if(lastval.equals("")){
						lastval=lastValueFromServer.ItemVal.trim();
					}
					if(lastValueFromServer.ItemValTyp.equals("4")){
						lastval=lastValueFromServer.RemAns.trim();
					}
				}
			}
		}
		return lastval;
	}



	public  class ViewHolder {

		public   TextView  txtItemDesc;
		public   ImageView imgTajhizNfcItem;
		public   ViewGroup layoutRoot;
		public   ViewGroup layoutRoot1;
		public  ImageView imgItemInfo;
		public  TextView  txtItem;
    	
        public ViewHolder(View view) {
            layoutRoot = (ViewGroup) view.findViewById(R.id.layoutRootItem);

            layoutRoot1 = (ViewGroup) view.findViewById(R.id.layoutRootItem1);
            imgItemInfo = (ImageView) view.findViewById(R.id.imgItemInfo);
			imgTajhizNfcItem = (ImageView) view.findViewById(R.id.imgTajhizNfcItem);
            txtItem = (TextView) view.findViewById(R.id.txtItemName);
            txtItemDesc = (TextView) view.findViewById(R.id.txtItemDesc);
            drwGreen = G.context.getResources().getDrawable(drawable.list_item_green_gradient);
            drwWhite = G.context.getResources().getDrawable(drawable.list_item_gradient);
            drwGray =  G.context.getResources().getDrawable(drawable.list_item_gray_gradient);
			drwInfoRed = G.context.getResources().getDrawable(drawable.info_red);
			drwInfoBlack = G.context.getResources().getDrawable(drawable.info_icon);
        	txtItem.setTypeface(tf);
        	txtItemDesc.setTypeface(tf);
        }
		public  void ActionCommand(dtoItems item,int position) {
			itemDirection = ItemDirection.None;

			if(G.Setting.NoCheckMaxData!=1) {
				if(item.IsFullOfMaxData){
					MyToast.Show(G.context, (String)G.context.getResources().getText(R.string.Msg_FillItem), Toast.LENGTH_SHORT);
				}
				else{
					Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmit(item);
					if(IsValidForSubmit == ValidForSubmitType.OutOfRange){
						item.IsInTimeRange = false;
						MyToast.Show(G.context, (String)G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
						setBackgroundColorDependsOnItemVal(item);
						AdapterItem.this.notifyDataSetChanged();
					}else if(IsValidForSubmit==ValidForSubmitType.InRangeButHasOtherValue){
						item.IsInTimeRange = false;
						MyToast.Show(G.context, (String)G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
						setBackgroundColorDependsOnItemVal(item);
						AdapterItem.this.notifyDataSetChanged();
					}
					if(IsValidForSubmit==ValidForSubmitType.InRange || G.Setting.ContorolTime.compareTo("2") != 0)
					{
						showDialog(item,position);
					}
				}
			}
			else{
				if(TabFragmentItem.IsValidForSubmit(item)==ValidForSubmitType.OutOfRange){
					item.IsInTimeRange = false;
					MyToast.Show(G.context, (String)G.context.getResources().getText(R.string.MessageForSettingTime), Toast.LENGTH_SHORT);
					setBackgroundColorDependsOnItemVal(item);
					if(G.Setting.ContorolTime.compareTo("2") != 0)
					{
						showDialog(item,position);
					}
				}else if(TabFragmentItem.IsValidForSubmit(item)==ValidForSubmitType.InRangeButHasOtherValue){
					item.IsInTimeRange = false;
					MyToast.Show(G.context, (String)G.context.getResources().getText(R.string.MessageForSettingCount), Toast.LENGTH_SHORT);
					setBackgroundColorDependsOnItemVal(item);
					if(G.Setting.ContorolTime.compareTo("2") != 0) //
					{
						showDialog(item,position);
					}
				}
				else {
					showDialog(item,position);
				}
			}
		}
        @RequiresApi(api = Build.VERSION_CODES.N)
		public void fill(final ArrayAdapter<dtoItems> adapter, final dtoItems item, final int position) {
            txtItem.setText(item.ItemName);
            if(item.Desc.equals("")==false) {
				txtItemDesc.setText(item.Desc);
			}
            txtItemDesc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MyToast.Show(G.context, item.Desc, Toast.LENGTH_SHORT);
				}
			});

            if(item.HasTag && G.Setting.ModTag == 2 && G.Setting.LayerTag == 2){
				imgTajhizNfcItem.setVisibility(View.VISIBLE);
				imgTajhizNfcItem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						MyToast.Show(G.context,(String)G.context.getResources().getText(R.string.Msg_NfcTag1)
								,Toast.LENGTH_LONG);
					}
				});
			}else
				imgTajhizNfcItem.setVisibility(View.GONE);

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
//          h.m.j.2

			setBackgroundColorDependsOnItemVal(item);


//            updateMohasebatiItems();

			txtItem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActionCommand(item,position);
				}
			});
			layoutRoot.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActionCommand(item,position);
				}
			});
			layoutRoot1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActionCommand(item,position);
				}
			});
        }


//		private Boolean checkValidations(dtoItems item,String strValue){
//			Boolean hasError = false;
//			Boolean canSubmit = false;
//			String strErrorMessage = "";
//
//			if(strValue.trim().length()==0 || !MyUtilities.isNumber(strValue.trim()))
//				return !hasError;
//			Double value = Double.valueOf(strValue.trim());
//			if(G.Setting.Range1==1){
//				if(item.MinAmount1!=null && item.MinAmount1.trim().length()>0 && item.MaxAmount1!=null && item.MaxAmount1.trim().length()>0)
//				{
//					Double minRange = Double.valueOf(item.MinAmount1);
//					Double maxRange = Double.valueOf(item.MaxAmount1);
//					if(value<minRange || value>maxRange){
//						canSubmit = false;
//						hasError = true;
//						strErrorMessage += "???? ?? ?????? ??? :" + "\n";
//						strErrorMessage += G.Setting.Dsc1 + "\n";
//					}else{
//						canSubmit = true;
//					}
//				}
//			}else{
//				canSubmit = true;
//			}
//			if(G.Setting.Range2==1){
//				if(item.MinAmount2!=null && item.MinAmount2.trim().length()>0 && item.MaxAmount2!=null && item.MaxAmount2.trim().length()>0 )
//				{
// 					Double minRange = Double.valueOf(item.MinAmount2);
//					Double maxRange = Double.valueOf(item.MaxAmount2);
//					if(value<minRange || value>maxRange){
//						canSubmit = false;
//						hasError = true;
//						strErrorMessage += "???? ?? ?????? ??? :" + "\n";
//						strErrorMessage += G.Setting.Dsc2 + "\n";
//					}else{
//						canSubmit = true;
//					}
//				}
//			}else{
//				canSubmit = true;
//			}
//			if(G.Setting.Range3==1){
//				if(item.MinAmount3!=null && item.MinAmount3.trim().length()>0 && item.MaxAmount3!=null && item.MaxAmount3.trim().length()>0 )
//				{
//					Double minRange = Double.valueOf(item.MinAmount3);
//					Double maxRange = Double.valueOf(item.MaxAmount3);
//					if(value<minRange || value>maxRange){
//						canSubmit = false;
//						hasError = true;
//						strErrorMessage += "???? ?? ?????? ??? :" + "\n";
//						strErrorMessage += G.Setting.Dsc3 + "\n";
//					}else{
//						canSubmit = true;
//					}
//				}
//			}else{
//				canSubmit = true;
//			}
//
//			if(hasError == true){
//				MyToast.Show(G.context, strErrorMessage, Toast.LENGTH_LONG);
//			}
//
//			return canSubmit;
//		}
@RequiresApi(api = Build.VERSION_CODES.N)
private void setBackgroundColorDependsOnItemVal(dtoItems item)
{
//	if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal == null
//			|| TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.
//			replaceAll("/", "").replaceAll(":", "").
//			replaceAll(",", "").trim().length()==0){
//		layoutRoot.setBackgroundDrawable(drwWhite);
//		if(G.DB.getItemValuesByUserIdAndItemInfIdWithDate(G.currentUser.UsrID,item.ItemInfID).size()>0)  {
//			Integer IsValidForSubmit = TabFragmentItem.IsValidForSubmitWitoutCheckSetting(item);
//			layoutRoot.setBackgroundDrawable(drwGreen);
//			if (IsValidForSubmit == ValidForSubmitType.InRange || IsValidForSubmit == ValidForSubmitType.OutOfRange) {
//				layoutRoot.setBackgroundDrawable(drwWhite);
//			}
//
//		}
//
//	}else{
//		layoutRoot.setBackgroundDrawable(drwGreen);
//		ArrayList<dtoItemValues> listdtoItemValues=G.DB.getItemValuesByUserIdAndItemInfId(G.currentUser.UsrID,item.ItemInfID);
//		if(listdtoItemValues.size()>0){
//			layoutRoot.setBackgroundDrawable(drwGreen);
//			for(dtoItemValues itemValues:listdtoItemValues) {
//				if(TabFragmentItem.IsValidForSubmitWitoutCheckSetting(G.DB.GetItemByItemInfId(itemValues.ItemInfID))==ValidForSubmitType.InRange ||
//						TabFragmentItem.IsValidForSubmitWitoutCheckSetting(G.DB.GetItemByItemInfId(itemValues.ItemInfID))==ValidForSubmitType.OutOfRange) {
//					layoutRoot.setBackgroundDrawable(drwWhite);
//
//				}
//			}
//		}
//	}


	if(item.HasValueInRange) {
		layoutRoot.setBackgroundDrawable(drwGreen);
	}else{
		layoutRoot.setBackgroundDrawable(drwWhite);
	}
	if(!item.IsInTimeRange && G.Setting.ContorolTime.compareTo("2")==0){
		imgItemInfo.setImageDrawable(drwInfoRed);
	}else{
		imgItemInfo.setImageDrawable(drwInfoBlack);
	}

		if (G.Setting.NoCheckMaxData != 1) {
			if (item.IsFullOfMaxData) {
				layoutRoot.setBackgroundDrawable(drwGray);
				imgItemInfo.setImageDrawable(drwInfoBlack);
			}
		}

}
//		private void setBackgroundColorDependsOnItemVal(dtoItems item)
//		{
//			if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal == null || TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.replaceAll("/", "").replaceAll(":", "").replaceAll(",", "").trim().length()==0){
//				layoutRoot.setBackgroundDrawable(drwWhite);
//			}else{
//				layoutRoot.setBackgroundDrawable(drwGreen);
//			}
//
//			if(!item.IsInTimeRange && G.Setting.ContorolTime.compareTo("2")==0){
//				imgItemInfo.setImageDrawable(drwInfoRed);
//			}else{
//				imgItemInfo.setImageDrawable(drwInfoBlack);
//			}
//
//			if(G.Setting.NoCheckMaxData!=1){
//				if(item.IsFullOfMaxData){
//					layoutRoot.setBackgroundDrawable(drwGray);
//					imgItemInfo.setImageDrawable(drwInfoBlack);
//				}
//			}
//		}


	}


    private String getStringFromRemarks(ArrayList<MyCheckListItem> remarks){
    	String remarkIds = "";
    	for(int i=0;i<remarks.size();i++)
    	{
    		remarkIds+=remarks.get(i).Value.toString();
//    		if(i!=remarks.size()-1){
    			remarkIds+=",";
//    		}
    	}
		return remarkIds;
    }


    private  ArrayList<String> getRemarkIdsFromString(String strRemarks){
    	ArrayList<String> lst = new ArrayList<String>();
    	for(String remarkId : strRemarks.split(",")){
    		lst.add(remarkId);
    	}
		return lst;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        dtoItems item = getItem(position);

        if (convertView == null) {
            convertView = G.inflater.inflate(PageLayout, parent, false);
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
 		TabFragmentItem.arrListItemFiltered.clear();
 		if(TabFragmentItem.searchSpinner.getSelectedItem().toString().equals(
 				(String)G.context.getResources().getText(R.string.AllItem)))
 		{
 			for (dtoItems item : TabFragmentItem.arrListItem) 
 			{
 				if (String.valueOf(item.ItemName.toLowerCase()).contains(charText.toLowerCase()))
 				{
 		 			TabFragmentItem.arrListItemFiltered.add(item);
 				}
 			}
 		}
 		else if(TabFragmentItem.searchSpinner.getSelectedItem().toString().equals(
 				(String)G.context.getResources().getText(R.string.NotRegister)))
 		{
 			for (dtoItems item : TabFragmentItem.arrListItem) 
 			{
 				if (String.valueOf(item.ItemName.toLowerCase()).contains(charText.toLowerCase()))
 				{
 					if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal == null || TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length()==0)
 						TabFragmentItem.arrListItemFiltered.add(item);
 				}
 			}
 		}
 		else if(TabFragmentItem.searchSpinner.getSelectedItem().toString().equals(
				(String)G.context.getResources().getText(R.string.Registered)
		))
 		{
 			for (dtoItems item : TabFragmentItem.arrListItem) 
 			{
 				if (String.valueOf(item.ItemName.toLowerCase()).contains(charText.toLowerCase()))
 				{
 					if(TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal != null && TabFragmentItem.dicItemValues.get(item.ItemInfID).ItemVal.trim().length()>0)
 						TabFragmentItem.arrListItemFiltered.add(item);
 				}
 			}
 		}
 		
 		TabFragmentItem.adapterItem.notifyDataSetChanged();
 	}


}
