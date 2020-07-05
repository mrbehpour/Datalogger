package ir.saa.android.datalogger;

import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ndeftools.Message;

import nfc.NfcReaderActivity;

public class ShowContentNfcTagActivity extends NfcReaderActivity {

    ImageView   imgNfcContent ;
    TextView    txtNfcContentInfo ;
    TextView    txtNfcContent ;
    public  void adjustFontScale(Configuration configuration, Float fontSize) {

        configuration.fontScale = (float) fontSize;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_content_nfc_tag);
        LinearLayout llNfcTag=(LinearLayout)findViewById(R.id.llNfcTag );

        if(G.RTL)//TODO: LTR
            llNfcTag.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        else {

            llNfcTag.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            adjustFontScale(getResources().getConfiguration(),(float)1);
        }


        setDetecting(true);
        final Typeface tfByekan = Typeface.createFromAsset(getAssets(), "fonts/byekan.ttf");
        imgNfcContent = (ImageView) findViewById(R.id.imgNfcContent);
        txtNfcContentInfo = (TextView) findViewById(R.id.txtNfcContentInfo);
        txtNfcContent = (TextView) findViewById(R.id.txtNfcContent);

        txtNfcContentInfo.setTypeface(tfByekan);



    }

    @Override
    protected void onNfcStateEnabled() {

    }

    @Override
    protected void onNfcStateDisabled() {

    }

    @Override
    protected void onNfcStateChange(boolean enabled) {

    }

    @Override
    protected void onNfcFeatureNotFound() {

    }

    @Override
    protected void onTagLost() {

    }

    @Override
    protected void readNdefMessage(Message message) {
        showNfcContent();
    }

    @Override
    protected void readEmptyNdefMessage() {

    }

    @Override
    protected void readNonNdefMessage() {
        showNfcContent();
    }
    private void showNfcContent(){
        if(NfcTagId!=null){
            G.vibrator.vibrate(200);
            ObjectAnimator
                    .ofFloat(imgNfcContent, "translationX", 0, 25, -25, 25, -25,15, -15, 6, -6, 0)
                    .setDuration(500)
                    .start();
            txtNfcContent.setText(NfcTagId.toUpperCase());
        }
    }
}
