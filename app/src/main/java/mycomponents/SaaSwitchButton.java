package mycomponents;
import ir.saa.android.datalogger.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class SaaSwitchButton extends ImageView implements View.OnClickListener {

	private View.OnClickListener clickListener;
	private boolean _checked = false;
	private Context _context;
	
	public SaaSwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		super.setImageDrawable(_context.getResources().getDrawable(R.drawable.controler_off_icon));

		setOnClickListener(this);
	}
	
	public SaaSwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		super.setImageDrawable(_context.getResources().getDrawable(R.drawable.controler_off_icon));

		setOnClickListener(this);
	}
	
	public SaaSwitchButton(Context context) {
		super(context);
		_context = context;
		super.setImageDrawable(_context.getResources().getDrawable(R.drawable.controler_off_icon));

		setOnClickListener(this);
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l == this) {
		    super.setOnClickListener(l);
		} else {
		    clickListener = l;
		}
	}

	@Override
	public void onClick(View v) {
		ImageView img = (ImageView)v;

		setChecked(!_checked);
		
		if (clickListener != null) {
		    clickListener.onClick(this);
		}
	}

	public boolean getChecked(){
		return _checked;
	}
	public void setChecked(boolean bool){
		_checked = bool; 
		
		if(_checked){
			super.setImageDrawable(_context.getResources().getDrawable(R.drawable.controler_on_icon));
		}else{
			super.setImageDrawable(_context.getResources().getDrawable(R.drawable.controler_off_icon));
		}
	}
}