package mycomponents;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by h.eskandari on 2/27/2018.
 */

public class MenuDialogItem extends LinearLayout {

    private Context _context;
    //private LinearLayout _llMain;
    private LinearLayout _llFrame;
    private LinearLayout _llSubFrame;
    private ImageView _img;
    private TextView _txtMenuText;
    private int titleColor = Color.parseColor("#cc111111");
    private Typeface tf = null;
    private int frameColor = Color.parseColor("#55eeeeee");
    private int subFrameColor = Color.parseColor("#00888800");
    //--------- Total height is 200 -----------
    private int frameSize = 150;
    private int subFrameHeightSize = 10;
    private int imageSize = 85;
    private int textMenuHeightSize = 60;
    private int textMenuFontSize = 14;

    public MenuDialogItem(Context context) {
        super(context);
        _context = context;
        initializeSelf();
        _llFrame = MyUiCreator.createLinearLayout(context, frameSize, frameSize,null,LinearLayout.VERTICAL, Gravity.CENTER, frameColor);
        _llSubFrame = MyUiCreator.createLinearLayout(context, frameSize, subFrameHeightSize,null,LinearLayout.VERTICAL, Gravity.CENTER, subFrameColor);
        _img = MyUiCreator.createImageView(context,imageSize,imageSize,null,null);
        _txtMenuText = MyUiCreator.createTextView(_context,ViewGroup.LayoutParams.WRAP_CONTENT,textMenuHeightSize,"---",textMenuFontSize,Gravity.CENTER,titleColor, View.TEXT_ALIGNMENT_CENTER, Typeface.NORMAL,tf);

        _llFrame.addView(_img);
        _llFrame.addView(_txtMenuText);
        this.addView(_llFrame);
        this.addView(_llSubFrame);
    }
    public MenuDialogItem(Context context,Drawable imageDrawable,String menuText) {
        super(context);
        _context = context;
        initializeSelf();
        _llFrame = MyUiCreator.createLinearLayout(context, frameSize, frameSize,null,LinearLayout.VERTICAL, Gravity.CENTER, frameColor);
        _llSubFrame = MyUiCreator.createLinearLayout(context, frameSize, subFrameHeightSize,null,LinearLayout.VERTICAL, Gravity.CENTER, subFrameColor);
        _img = MyUiCreator.createImageView(context,imageSize,imageSize,imageDrawable,null);
        _txtMenuText = MyUiCreator.createTextView(_context,ViewGroup.LayoutParams.WRAP_CONTENT,textMenuHeightSize,menuText,textMenuFontSize,Gravity.CENTER,titleColor, View.TEXT_ALIGNMENT_CENTER, Typeface.NORMAL,tf);

        _llFrame.addView(_img);
        _llFrame.addView(_txtMenuText);
        this.addView(_llFrame);
        this.addView(_llSubFrame);
        //this.addView(_llMain);
    }
    private void initializeSelf(){
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight=1;

        this.setLayoutParams(lp);
    }

    public MenuDialogItem(Context context,Drawable imageDrawable,String menuText,Typeface typeface) {
        super(context);
        _context = context;
        tf = typeface;
        initializeSelf();
        _llFrame = MyUiCreator.createLinearLayout(context, frameSize, frameSize,null,LinearLayout.VERTICAL, Gravity.CENTER, frameColor);
        _llSubFrame = MyUiCreator.createLinearLayout(context, frameSize, subFrameHeightSize,null,LinearLayout.VERTICAL, Gravity.CENTER, subFrameColor);
        _img = MyUiCreator.createImageView(context,imageSize,imageSize,imageDrawable,null);
        _txtMenuText = MyUiCreator.createTextView(_context,ViewGroup.LayoutParams.WRAP_CONTENT,textMenuHeightSize,menuText,textMenuFontSize,Gravity.CENTER,titleColor, View.TEXT_ALIGNMENT_CENTER, Typeface.NORMAL,tf);

        _llFrame.addView(_img);
        _llFrame.addView(_txtMenuText);
        this.addView(_llFrame);
        this.addView(_llSubFrame);
    }
    public MenuDialogItem(Context context,Drawable imageDrawable,String menuText,Typeface typeface,int subFrameColor) {
        super(context);
        _context = context;
        tf = typeface;
        initializeSelf();
        _llFrame = MyUiCreator.createLinearLayout(context, frameSize, frameSize,null,LinearLayout.VERTICAL, Gravity.CENTER, frameColor);
        _llSubFrame = MyUiCreator.createLinearLayout(context, frameSize, subFrameHeightSize,null,LinearLayout.VERTICAL, Gravity.CENTER, subFrameColor);
        _img = MyUiCreator.createImageView(context,imageSize,imageSize,imageDrawable,null);
        _txtMenuText = MyUiCreator.createTextView(_context,ViewGroup.LayoutParams.WRAP_CONTENT,textMenuHeightSize,menuText,textMenuFontSize,Gravity.CENTER,titleColor, View.TEXT_ALIGNMENT_CENTER, Typeface.NORMAL,tf);

        _llFrame.addView(_img);
        _llFrame.addView(_txtMenuText);
        this.addView(_llFrame);
        this.addView(_llSubFrame);
    }
    @Override
    public void setOnClickListener(OnClickListener onClickListener){
        //this.setOnClickListener(onClickListener);
        _llFrame.setOnClickListener(onClickListener);
        _llSubFrame.setOnClickListener(onClickListener);
        _img.setOnClickListener(onClickListener);
        _txtMenuText.setOnClickListener(onClickListener);
        super.setOnClickListener(onClickListener);
    }



    public MenuDialogItem setSubFrameColor(int color){
        subFrameColor = color;
        _llSubFrame.setBackgroundColor(color);
        return this;
    }
    public MenuDialogItem setTypeFace(Typeface typeface){
        tf = typeface;
        _txtMenuText.setTypeface(typeface);
        return this;
    }
    public MenuDialogItem setImageDrawable(Drawable drawable){
        _img.setImageDrawable(drawable);
        return this;
    }
    public MenuDialogItem setImageBackgroundColor(int backgroundColor){
        _img.setBackgroundColor(backgroundColor);
        return this;
    }
    public MenuDialogItem setMenuText(String text){
        _txtMenuText.setText(text);
        return this;
    }
}
