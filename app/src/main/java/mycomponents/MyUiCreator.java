package mycomponents;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by h.eskandari on 2/28/2018.
 */

public class MyUiCreator {

    public static ScrollView createScrollView(Context _context, int width, int height, Integer weight){
        ScrollView sv = new ScrollView(_context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        if(weight!=null)
            lp.weight=weight;
        sv.setLayoutParams(lp);
        return sv;
    }

    public static LinearLayout createLinearLayout(Context _context, int width, int height, Integer weight, Integer orientation , Integer gravity, Integer backgroundColor){
        LinearLayout ll = new LinearLayout(_context);
        if(backgroundColor!=null){
            ll.setBackgroundColor(backgroundColor);
        }
        if(orientation!=null)
            ll.setOrientation(orientation);
        if(gravity!=null)
            ll.setGravity(gravity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        if(weight!=null)
            lp.weight=weight;
        ll.setLayoutParams(lp);
        return ll;
    }

    public static TextView createTextView(Context _context, int width, int height, String text, Integer textSize, Integer gravity, Integer textColor, Integer textAlign, Integer textStyle, Typeface tf){
        TextView tv = new TextView(_context);
        if(tf!=null) {
            if (textStyle != null)
                tv.setTypeface(tf, textStyle);
            else
                tv.setTypeface(tf);
        }
        if(text!=null)
            tv.setText(text);
        if(textSize!=null)
            tv.setTextSize(textSize);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
		if(gravity!=null)
			tv.setGravity(gravity);
        if(textColor!=null){
            tv.setTextColor(textColor);
        }
        if(textAlign!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tv.setTextAlignment(textAlign);
            }
        }

        tv.setLayoutParams(lp);
        return tv;
    }

    public static ImageView createImageView(Context _context, int width, int height, Drawable drawable,Integer backgroundColor){
        ImageView img = new ImageView(_context);
        if(drawable!=null)
            img.setImageDrawable(drawable);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        if(backgroundColor!=null){
            img.setBackgroundColor(backgroundColor);
        }
        img.setLayoutParams(lp);
        return img;
    }
}
