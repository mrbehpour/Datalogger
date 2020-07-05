package mycomponents;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.saa.android.datalogger.G;
import ir.saa.android.datalogger.R;

public class MyDialogMenu {
	private Context _context;
	private Dialog _dialog;
	private LinearLayout _llMainPanel;
	private LinearLayout _llClose;
	private LinearLayout _llBodyPanel;
	private ScrollView _scrollView;
	private TextView _txtClose;
	private Typeface tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
	private int bodyBackgroundColor = Color.parseColor("#ddeeeeee");
	private int closeBackgroundColor = Color.parseColor("#dddddddd");
	private int titleColor = Color.parseColor("#cc111111");
	private int textColor = Color.parseColor("#cc111111");
	private int titleBarColor = Color.parseColor("#55999999");
	private int groupHeightSize = 160;
	private int titleBarHeightSize = 3;
	private int titleTextSize = 18;
	private int llCloseHeightSize = 80;

	public MyDialogMenu(Context context) {
		_context = context;
		_dialog = new Dialog(_context, R.style.MenuDialogTheme);
		//_dialog.getWindow().getAttributes().windowAnimations = R.style.MenuDialogTheme;

		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;
		_llMainPanel = MyUiCreator.createLinearLayout(_context,width, height,null,LinearLayout.VERTICAL,Gravity.CENTER,null);
		_llClose = MyUiCreator.createLinearLayout(_context,width-60, llCloseHeightSize,null,LinearLayout.VERTICAL,Gravity.CENTER,closeBackgroundColor);
		_scrollView = MyUiCreator.createScrollView(_context,ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
		_llBodyPanel = MyUiCreator.createLinearLayout(_context,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,null,LinearLayout.VERTICAL,Gravity.CENTER_HORIZONTAL,bodyBackgroundColor);
		_txtClose = MyUiCreator.createTextView(_context,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,"بستن",17,Gravity.CENTER,Color.parseColor("#222222"),View.TEXT_ALIGNMENT_CENTER,Typeface.BOLD,tf);
		LinearLayout.LayoutParams lpScrollView = new LinearLayout.LayoutParams(_scrollView.getLayoutParams());
		lpScrollView.setMargins(30,30,30,0);
		lpScrollView.weight = 1;
		_llBodyPanel.setPadding(10,10,10,10);
		_scrollView.setLayoutParams(lpScrollView);

//		_scrollView.setBackgroundColor(Color.parseColor("#ff0000"));
		//_llClose.setBackgroundColor(Color.parseColor("#0000ff"));
		LinearLayout.LayoutParams lpClosePanel = new LinearLayout.LayoutParams(_llClose.getLayoutParams());
		lpClosePanel.setMargins(0,0,0,30);
		_llClose.setLayoutParams(lpClosePanel);

		_txtClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_dialog.dismiss();
			}
		});
		_llClose.addView(_txtClose);
		_scrollView.addView(_llBodyPanel);
		_llMainPanel.addView(_scrollView);
		_llMainPanel.addView(_llClose);
		_dialog.setContentView(_llMainPanel);

	}
//	public MyDialogMenu(Context context, int theme) {
//		_context = context;
//		_dialog = new Dialog(_context,theme);
//		_dialog.getWindow().getAttributes().windowAnimations = theme;
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		int height = displayMetrics.heightPixels;
//		int width = displayMetrics.widthPixels;
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
//		_llBodyPanel.setBackgroundColor(Color.parseColor("#aaffffff"));
//		lp.setMargins(10,10,10,10);
//		_llBodyPanel.setLayoutParams(lp);
//		_dialog.setContentView(_llBodyPanel);
//    }

	public Dialog getDialog(){
		return _dialog;
	}


	public MyDialogMenu addTitleMenuGroup(String title){
		LinearLayout llMain = MyUiCreator.createLinearLayout(_context,ViewGroup.LayoutParams.MATCH_PARENT, 60,null,LinearLayout.HORIZONTAL,Gravity.CENTER,null);
		//LinearLayout llSubCenter = MyUiCreator.createLinearLayout(_context,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,null,LinearLayout.HORIZONTAL,Gravity.CENTER,null);
		LinearLayout llSubLeft = MyUiCreator.createLinearLayout(_context,0, titleBarHeightSize,1,LinearLayout.HORIZONTAL,Gravity.CENTER,titleBarColor);
		LinearLayout llSubRight = MyUiCreator.createLinearLayout(_context,0, titleBarHeightSize,1,LinearLayout.HORIZONTAL,Gravity.CENTER,titleBarColor);

		TextView tv = MyUiCreator.createTextView(_context,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,title,titleTextSize,Gravity.CENTER,titleColor,View.TEXT_ALIGNMENT_CENTER, Typeface.BOLD,tf);
		LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(tv.getLayoutParams());
		lpTv.setMargins(10,0,10,0);
		tv.setLayoutParams(lpTv);

		//llSubCenter.addView(tv);
		llMain.addView(llSubLeft);
		llMain.addView(tv);
		llMain.addView(llSubRight);

		_llBodyPanel.addView(llMain);
		return this;
	}

	public MyDialogMenu addMenuGroup(ArrayList<MenuDialogItem> menuDialogItems){
		LinearLayout llMain = MyUiCreator.createLinearLayout(_context,ViewGroup.LayoutParams.MATCH_PARENT, groupHeightSize,null,LinearLayout.HORIZONTAL,Gravity.CENTER,null);

		for(MenuDialogItem mi : menuDialogItems){
			llMain.addView(mi);
		}

		_llBodyPanel.addView(llMain);
		return this;
	}
	public MyDialogMenu addMenuGroup(ArrayList<MenuDialogItem> menuDialogItems,Typeface tf){
		LinearLayout llMain = MyUiCreator.createLinearLayout(_context,ViewGroup.LayoutParams.MATCH_PARENT, groupHeightSize,null,LinearLayout.HORIZONTAL,Gravity.CENTER,null);

		for(MenuDialogItem mi : menuDialogItems){
			if(tf!=null)
				mi.setTypeFace(tf);
			llMain.addView(mi);
		}

		_llBodyPanel.addView(llMain);
		return this;
	}
	public MyDialogMenu addMenuGroup(ArrayList<MenuDialogItem> menuDialogItems,int groupSubFrameColor){
		LinearLayout llMain = MyUiCreator.createLinearLayout(_context,ViewGroup.LayoutParams.MATCH_PARENT, groupHeightSize,null,LinearLayout.HORIZONTAL,Gravity.CENTER,null);

		for(MenuDialogItem mi : menuDialogItems){
			if(tf!=null)
				mi.setTypeFace(tf);
			mi.setSubFrameColor(groupSubFrameColor);
			llMain.addView(mi);
		}

		_llBodyPanel.addView(llMain);
		return this;
	}
	public MyDialogMenu addMenuGroup(ArrayList<MenuDialogItem> menuDialogItems,Typeface tf, int groupSubFrameColor){
		LinearLayout llMain = MyUiCreator.createLinearLayout(_context,ViewGroup.LayoutParams.MATCH_PARENT, groupHeightSize,null,LinearLayout.HORIZONTAL,Gravity.CENTER,null);

		for(MenuDialogItem mi : menuDialogItems){
			if(tf!=null)
				mi.setTypeFace(tf);
			mi.setSubFrameColor(groupSubFrameColor);
			llMain.addView(mi);
		}

		_llBodyPanel.addView(llMain);
		return this;
	}
	public MyDialogMenu addContentView(View v) {
		_llBodyPanel.addView(v);
		return this;
	}
	public MyDialogMenu addContentXml(int layoutId) {
	    _llBodyPanel.addView((LinearLayout) View.inflate(_context, layoutId, null));
		return this;
	}
	public MyDialogMenu clearBodyPanel() {
		if(_llBodyPanel.getChildCount() > 0)
			_llBodyPanel.removeAllViews();
		return this;
	}
	public MyDialogMenu clearAllPanel() {
		if(_llBodyPanel.getChildCount() > 0)
			_llBodyPanel.removeAllViews();
		return this;
	}
	public MyDialogMenu setContentSplitter() {
		LinearLayout llSplitter = new LinearLayout(_context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,1);
		llSplitter.setBackgroundColor(Color.rgb(184, 186, 188));
		lp.setMargins(5, 0, 5, 0);
		llSplitter.setLayoutParams(lp);
	    _llBodyPanel.addView(llSplitter);
		return this;
	}
	public MyDialogMenu setVerticalMargin(int verticalMargin) {
		LinearLayout llMakeMargin = new LinearLayout(_context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,verticalMargin);
		llMakeMargin.setLayoutParams(lp);
	    _llBodyPanel.addView(llMakeMargin);
		return this;
	}
	public MyDialogMenu setBackgroundAlpha(float alpha){
		((LinearLayout)_llBodyPanel.getParent()).setAlpha(alpha);
		return this;
	}
	public MyDialogMenu addBodyText(String text, int textSize) {
		LinearLayout ll = new LinearLayout(_context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,40);
		ll.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

		TextView txt= new TextView(_context);
		txt.setText(text);
		txt.setTextSize(textSize);
		txt.setTypeface(tf);
		txt.setGravity(Gravity.CENTER);
		ll.addView(txt);

		ll.setLayoutParams(lp);
	    _llBodyPanel.addView(ll);
		return this;
	}
	public MyDialogMenu setBodyMargin(int left, int top, int right, int bottom) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(_llBodyPanel.getLayoutParams());
		lp.setMargins(left, top, right, bottom);
	    _llBodyPanel.setLayoutParams(lp);
		return this;
	}
	public MyDialogMenu show() {
		_dialog.show();
		return this;
	}
	public MyDialogMenu dismiss(){
		_dialog.dismiss();
		return this;
	}
}
