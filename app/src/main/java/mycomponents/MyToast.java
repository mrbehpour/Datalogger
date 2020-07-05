package mycomponents;


import ir.saa.android.datalogger.G;
import ir.saa.android.datalogger.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast extends Toast {

	static  Typeface  tfByekan;
	public MyToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static void Show(Context context ,String message,int duration ){
		 LinearLayout  layout=new LinearLayout(context);
		   layout.setBackgroundResource(R.drawable.toast_gradient);
		   layout.setPadding(15, 15, 15, 15);
		   if(G.RTL){
			    tfByekan = Typeface.createFromAsset(G.context.getAssets(), "fonts/byekan.ttf");
		   }else {
			    tfByekan = Typeface.createFromAsset(G.context.getAssets(), "fonts/bfd.ttf");
		   }
		    TextView  tv = new TextView(context);
		    tv.setTypeface(tfByekan);
		    tv.setTextColor(Color.rgb(20, 20, 20));
		    tv.setTextSize(20);        
		    tv.setGravity(Gravity.CENTER_VERTICAL);
		    tv.setText(message);  
		    
		    ImageView   img=new ImageView(context);
		    layout.addView(img);
		    layout.addView(tv);
		    
		Toast t =Toast.makeText(context,"",duration);
       t.setGravity(Gravity.CENTER,0,0);
       t.setView(layout);
       t.show();
	}

}
