package mycomponents;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ir.saa.android.datalogger.G;
import ir.saa.android.datalogger.R;

public class MyDialog {
	private Context _context;
	private Dialog _dialog;
	private LinearLayout _llBodyPanel;
	private LinearLayout _llButtonPanel;
	private  LinearLayout _llButtonPanelR;
	private  LinearLayout _llButtonPanelL;
	private LinearLayout _lrButtonPanel;

	private TextView _txtDialogTitle;
	private TextView _txtDialogLeftTitle;
	private TextView _txtDialogRightTitle;
	private ImageView _imgDialogLeftTitle;
	private ImageView _imgDialogRightTitle;
	int heightB;
	int heighText;
	int TextSizeE;
	int TextSizeB;
	int marginRight;

	LinearLayout llR ;
	LinearLayout llL ;

	int HorVer;
	private Typeface tf;
	public MyDialog(Context context) {
		_context = context;
		llR = new LinearLayout(_context);
		llL = new LinearLayout(_context);
		_dialog = new Dialog(_context, R.style.FullHeightDialogNotFloating); 
		_dialog.getWindow().getAttributes().windowAnimations = R.style.FullHeightDialogNotFloating;
		_dialog.setContentView(R.layout.custom_dialog);
		_txtDialogTitle = (TextView) _dialog.findViewById(R.id.txtDialogTitle);
		_txtDialogLeftTitle = (TextView) _dialog.findViewById(R.id.txtDialogLeftTitle);
		_txtDialogRightTitle = (TextView) _dialog.findViewById(R.id.txtDialogRightTitle);
		_imgDialogLeftTitle = (ImageView) _dialog.findViewById(R.id.imgDialogLeftTitle);
		_imgDialogRightTitle = (ImageView) _dialog.findViewById(R.id.imgDialogRightTitle);
		_llBodyPanel = (LinearLayout) _dialog.findViewById(R.id.llBodyPanel);
		_llButtonPanel = (LinearLayout) _dialog.findViewById(R.id.llButtonPanel);
		_lrButtonPanel=(LinearLayout)_dialog.findViewById(R.id.lrButtonPanel);
		_txtDialogTitle.setTypeface(tf);
		_txtDialogLeftTitle.setTypeface(tf);
		_txtDialogRightTitle.setTypeface(tf);
		if(G.RTL){
			_llBodyPanel.setGravity(Gravity.RIGHT);
			_llButtonPanel.setGravity(Gravity.RIGHT);
			_llBodyPanel.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
			_llButtonPanel.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
			tf= Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");

			heightB=70;
			heighText=40;
			TextSizeE=15;
			TextSizeB=17;
			marginRight=5;
		}else {
			_llBodyPanel.setGravity(Gravity.LEFT);
			_llButtonPanel.setGravity(Gravity.LEFT);
			_llBodyPanel.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
			_llButtonPanel.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
			tf= Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");

			_llButtonPanelL=(LinearLayout) _dialog.findViewById(R.id.llButtonPanelL);
			_llButtonPanelR=(LinearLayout) _dialog.findViewById(R.id.llButtonPanelR);
			heightB=-2;
			heighText=20;
			TextSizeE=12;
			TextSizeB=11;
			marginRight=1;
		}

    }
	public MyDialog(Context context,int theme) {
		_context = context;
		_dialog = new Dialog(_context,theme); 
		_dialog.getWindow().getAttributes().windowAnimations = theme;
		_dialog.setContentView(R.layout.custom_dialog);
		_txtDialogTitle = (TextView) _dialog.findViewById(R.id.txtDialogTitle);
		_txtDialogLeftTitle = (TextView) _dialog.findViewById(R.id.txtDialogLeftTitle);
		_txtDialogRightTitle = (TextView) _dialog.findViewById(R.id.txtDialogRightTitle);
		_imgDialogLeftTitle = (ImageView) _dialog.findViewById(R.id.imgDialogLeftTitle);
		_imgDialogRightTitle = (ImageView) _dialog.findViewById(R.id.imgDialogRightTitle);
		_llBodyPanel = (LinearLayout) _dialog.findViewById(R.id.llBodyPanel);
		_llButtonPanel = (LinearLayout) _dialog.findViewById(R.id.llButtonPanel);
		_txtDialogTitle.setTypeface(tf);
		_txtDialogLeftTitle.setTypeface(tf);
		_txtDialogRightTitle.setTypeface(tf);
		if(G.RTL){
			_llBodyPanel.setGravity(Gravity.RIGHT);
			_llBodyPanel.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
			tf= Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
			heightB=70;
			heighText=40;
			TextSizeE=17;
			TextSizeB=20;
			HorVer=0;

		}else {
			_llBodyPanel.setGravity(Gravity.LEFT);
			_llBodyPanel.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
			tf= Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
			heightB=-2;
			heighText=20;
			TextSizeE=13;
			TextSizeB=12;
			HorVer=1;
		}
    }
	
	
	public Dialog getDialog(){
		return _dialog;
	}
	public MyDialog setLeftImageTitle(Drawable drw,OnClickListener onClickListener){
		_imgDialogLeftTitle.setImageDrawable(drw);
		_imgDialogLeftTitle.setVisibility(View.VISIBLE);
		_imgDialogLeftTitle.setOnClickListener(onClickListener);
		return this;
	}
	public MyDialog setRightImageTitle(Drawable drw,OnClickListener onClickListener){
		_imgDialogRightTitle.setImageDrawable(drw);
		_imgDialogRightTitle.setVisibility(View.VISIBLE);
		_imgDialogRightTitle.setOnClickListener(onClickListener);
		return this;
	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public MyDialog addContentView(View v) {
		_llBodyPanel.addView(v);
		return this;
	}
	public MyDialog addContentXml(int layoutId) {
	    _llBodyPanel.addView((LinearLayout) View.inflate(_context, layoutId, null));
		return this;
	}
	public MyDialog clearButtonPanel() {
		if(_llButtonPanel.getChildCount() > 0)
			_llButtonPanel.removeAllViews(); 
		return this;
	}

	public MyDialog clearButtonPanelLR() {
		if(_llButtonPanelL.getChildCount() > 0)
			_llButtonPanelL.removeAllViews();
		if(_llButtonPanelR.getChildCount() > 0)
			_llButtonPanelR.removeAllViews();
		return this;
	}
	public MyDialog clearContentPanel() {
		if(_llBodyPanel.getChildCount() > 0)
			_llBodyPanel.removeAllViews(); 
		return this;
	}
	public MyDialog clearAllPanel() {
		if(_llBodyPanel.getChildCount() > 0)
			_llBodyPanel.removeAllViews(); 
		if(_llButtonPanel.getChildCount() > 0)
			_llButtonPanel.removeAllViews(); 
		return this;
	}
	public MyDialog setContentSplitter() {
		LinearLayout llSplitter = new LinearLayout(_context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,1);
		llSplitter.setBackgroundColor(Color.rgb(184, 186, 188));
		lp.setMargins(5, 0, 5, 0);
		llSplitter.setLayoutParams(lp);
	    _llBodyPanel.addView(llSplitter);
		return this;
	}

	public MyDialog setBodyMatchParent(){
        LinearLayout llSplitter = new LinearLayout(_context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        _llBodyPanel.setLayoutParams(lp);
        return this;
    }

	public MyDialog setVerticalMargin(int verticalMargin) {
		LinearLayout llMakeMargin = new LinearLayout(_context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,verticalMargin);
		llMakeMargin.setLayoutParams(lp);
	    _llBodyPanel.addView(llMakeMargin);
		return this;
	}
	public MyDialog setBackgroundAlpha(float alpha){
		((LinearLayout)_llBodyPanel.getParent()).setAlpha(alpha);
		return this;
	}
	@RequiresApi(api = Build.VERSION_CODES.M)
	public MyDialog addBodyText(String text, int textSize) {
		LinearLayout ll = new LinearLayout(_context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,heightB);
		//ll.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

		
		TextView txt= new TextView(_context);
		txt.setText(text);
		txt.setTextSize(textSize);
		txt.setTypeface(tf);
		txt.setTextColor(Color.BLACK);
		//txt.setGravity(Gravity.CENTER | Gravity.LEFT);
		ll.addView(txt);
		
		ll.setLayoutParams(lp);
	    _llBodyPanel.addView(ll);
		return this;
	}
	public MyDialog setBodyMargin(int left,int top,int right,int bottom) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(_llBodyPanel.getLayoutParams());
		lp.setMargins(left, top, right, bottom);	
	    _llBodyPanel.setLayoutParams(lp);
		return this;
	}
	public MyDialog setTitle(String title){
		_txtDialogTitle.setTextSize(TextSizeE);
		_txtDialogTitle.setText(title);
		return this;
	}
	public MyDialog setLeftTitle(String title){
		_txtDialogLeftTitle.setText(title);
		return this;
	}
	public MyDialog setRightTitle(String title){
		_txtDialogRightTitle.setText(title);
		return this;
	}
	public MyDialog addButton(String caption , View.OnClickListener onClickEvent){
		Button btn = new Button(_context);
		btn.setText(caption);
		btn.setTypeface(tf);
		btn.setTextSize(TextSizeB);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundDrawable(_context.getResources().getDrawable(R.drawable.login_button));
		LayoutParams btnLp = new LayoutParams();
		btnLp.width = LayoutParams.MATCH_PARENT;
		btnLp.height = LayoutParams.WRAP_CONTENT;
		btn.setOnClickListener(onClickEvent);
		btn.setLayoutParams(btnLp);

		LinearLayout ll = new LinearLayout(_context);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

		lp.weight = 1f;
		lp.setMargins(0, 0, marginRight, 5);
		ll.setLayoutParams(lp);
		ll.setVisibility(View.VISIBLE);
		btn.setVisibility(View.VISIBLE);
		ll.addView(btn);
		if(_lrButtonPanel!=null) {
			_lrButtonPanel.setVisibility(View.GONE);
		}

		_llButtonPanel.setVisibility(View.VISIBLE);
		_llButtonPanel.addView(ll);
		return this;



	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public MyDialog addButtonR(String caption , View.OnClickListener onClickEvent){
		Button btn = new Button(_context);
		btn.setText(caption);
		btn.setTypeface(tf);
		btn.setTextSize(TextSizeB);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundDrawable(_context.getResources().getDrawable(R.drawable.login_button));
		LayoutParams btnLp = new LayoutParams();
		btn.setBackground(ContextCompat.getDrawable(_context,R.drawable.button_selector));
		btn.setOnClickListener(onClickEvent);
		btnLp.width = LayoutParams.MATCH_PARENT;
		btnLp.height =LayoutParams.WRAP_CONTENT;
		btn.setLayoutParams(btnLp);

		LinearLayout ll = new LinearLayout(_context);


		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);



		lp.weight = 1f;
		lp.setMargins(0, 0, marginRight, 5);
		btn.setVisibility(View.VISIBLE);
		ll.setVisibility(View.VISIBLE);
		//ll.setLayoutParams(lp);
		ll.addView(btn);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(lp);


		_llButtonPanel.setVisibility(View.GONE);
		_llButtonPanelR.setVisibility(View.VISIBLE);
		_llButtonPanelR.addView(ll);
		return this;
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public MyDialog addButtonL(String caption , View.OnClickListener onClickEvent){
		Button btn = new Button(_context);
		btn.setText(caption);
		btn.setTypeface(tf);
		btn.setTextSize(TextSizeB);
		btn.setTextColor(Color.WHITE);


		btn.setBackgroundDrawable(_context.getResources().getDrawable(R.drawable.login_button));
		LayoutParams btnLp = new LayoutParams();
		btnLp.width = LayoutParams.MATCH_PARENT;
		btnLp.height =LayoutParams.WRAP_CONTENT;
		btn.setBackground(ContextCompat.getDrawable(_context,R.drawable.button_selector));

		btn.setOnClickListener(onClickEvent);
		btn.setLayoutParams(btnLp);
		btn.setVisibility(View.VISIBLE);
		LinearLayout ll = new LinearLayout(_context);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		lp.weight = 1f;
		lp.setMargins(0, 0, marginRight, 5);

		ll.setVisibility(View.VISIBLE);
		ll.setLayoutParams(lp);
		ll.addView(btn);

		_llButtonPanel.setVisibility(View.GONE);
		_llButtonPanelL.setVisibility(View.VISIBLE);
		_llButtonPanelL.addView(ll);
		return this;
	}

	public MyDialog show() {
		try {
			_dialog.show();
		}
		catch(Exception ex){

			Log.i("err",ex.getMessage());
		}
		return this;
	}
	public MyDialog dismiss(){
		_dialog.dismiss();
		return this;
	}
}
