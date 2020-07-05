package mycomponents;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import ir.saa.android.datalogger.G;

public class MyLabel extends LinearLayout {

	private Context _context;
	private int defultTextSize = 30;
	private int _labelHeight = LayoutParams.MATCH_PARENT;
	private int _labelBackgroundColor = Color.parseColor("#238927");
	private int _rightTextColor = Color.parseColor("#ffffff");
	private int _leftTextColor = Color.parseColor("#ffffff");
	private Typeface tf ;
	private TextView txtRight;
	private TextView txtLeft;
	private  int _directionLeft=Gravity.LEFT ;
	private  int _directionRight=Gravity.RIGHT ;



	public MyLabel(Context context,String textLeft,String textRight,int textLeftSize,int textRightSize) {
		super(context);
		_context =context;
		if(G.RTL){
			_directionLeft=Gravity.LEFT ;
			_directionRight=Gravity.RIGHT ;
			tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
		}else{
			_directionLeft=Gravity.RIGHT ;
			_directionRight=Gravity.LEFT ;
			tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
		}
		txtRight = new TextView(_context);
		txtLeft = new TextView(_context);
		LinearLayout.LayoutParams lpMain = new LinearLayout.LayoutParams(0,_labelHeight);
		lpMain.weight = 1f;
		setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(_labelBackgroundColor);
		this.setLayoutParams(lpMain);
		//-------------------
		LinearLayout llMainTop = new LinearLayout(_context);
		llMainTop.setGravity(Gravity.CENTER);
		llMainTop.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lpMainTop = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0);
		lpMainTop.weight = 1f;
		llMainTop.setLayoutParams(lpMainTop);
		//-------------------
		LinearLayout llMainBottom = new LinearLayout(_context);
		llMainBottom.setGravity(Gravity.CENTER);
		llMainBottom.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lpMainBottom = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,7);
		llMainBottom.setLayoutParams(lpMainBottom);
		llMainBottom.setBackgroundColor(Color.parseColor("#000000"));
		llMainBottom.setAlpha(0.3f);

		txtRight.setGravity(_directionRight | Gravity.CENTER_VERTICAL);
		txtLeft.setGravity(_directionLeft | Gravity.CENTER_VERTICAL);
		txtRight.setTypeface(tf);
		txtLeft.setTypeface(tf);
		txtRight.setText(textRight);
		txtLeft.setText(textLeft);
		txtRight.setTextSize(textRightSize);
		txtLeft.setTextSize(textLeftSize);
		txtRight.setTextColor(_rightTextColor);
		txtLeft.setTextColor(_leftTextColor);

		LinearLayout llRight = new LinearLayout(_context);
		llRight.setGravity(_directionRight | Gravity.CENTER_VERTICAL);
//		llRight.setBackgroundColor(Color.parseColor("#ff0000"));
		LinearLayout llLeft = new LinearLayout(_context);
		llLeft.setGravity(_directionLeft | Gravity.CENTER_VERTICAL);
//		llLeft.setBackgroundColor(Color.parseColor("#0000ff"));
		LinearLayout.LayoutParams lpRight = new LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams lpLeft = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		lpRight.weight = 0.5f;
		if(G.RTL) {
			lpRight.setMargins(0, 0, 20, 0);
			lpLeft.setMargins(20, 0, 0, 0);
		}else {
			lpRight.setMargins(20, 0, 0, 0);
			lpLeft.setMargins(0, 0, 20, 0);
		}

		//lpLeft.weight = 0.5f;



		llRight.addView(txtRight);
		llLeft.addView(txtLeft);
		llRight.setLayoutParams(lpRight);
		llLeft.setLayoutParams(lpLeft);

		llMainTop.addView(llLeft);
		llMainTop.addView(llRight);
		this.addView(llMainTop);
		this.addView(llMainBottom);
	}
//	public MyLabel setLeftTextSize(int size){
//		txtLeft.setTextSize(size);
//		return this;
//	}
//	public MyLabel setRightTextSize(int size){
//		txtRight.setTextSize(size);
//		return this;
//	}

	public MyLabel setMargin(int left,int top,int right,int bottom){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(this.getLayoutParams());
		lp.setMargins(left, top, right, bottom);
		this.setLayoutParams(lp);
		return this;
	}

	public MyLabel setRightText(String text){
		txtRight.setText(text);
		return this;
	}
	public MyLabel setLeftText(String text){
		txtLeft.setText(text);
		return this;
	}

	public MyLabel setHeight(int height){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(this.getLayoutParams().width,height);
		this.setLayoutParams(lp);
		return this;
	}


//	@Override
//	public void setOnClickListener(OnClickListener l) {
//		// TODO Auto-generated method stub
//		super.setOnClickListener(l);
//		txtLeft.setOnClickListener(l);
//		txtRight.setOnClickListener(l);
//	}

	public MyLabel setRightTextColor(int color){
		_rightTextColor = color;
		return this;
	}

	public MyLabel setLeftTextColor(int color){
		_leftTextColor = color;
		return this;
	}
	@Override
	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
		
	}
	
//	@Override
//	public void setOrientation(int orientation) {
//		super.setOrientation(orientation);
//		
//		if(orientation == LinearLayout.HORIZONTAL){
//			int childCount = this.getChildCount();
//			for (int i = 0; i < childCount; i++) {
//				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,_checkItemheight);
//				lp.weight = 1f;
//				lp.setMargins(0, 0, 5, 0);
//			    getChildAt(i).setLayoutParams(lp);
//			}
//		}else{
//			int childCount = this.getChildCount();
//			for (int i = 0; i < childCount; i++) {
//				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,_checkItemheight);
//				lp.setMargins(0, 0, 0, 5);
//			    getChildAt(i).setLayoutParams(lp);
//			}
//		}
//	}

}

