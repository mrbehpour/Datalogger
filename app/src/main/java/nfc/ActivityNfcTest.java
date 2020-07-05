package nfc;

import android.nfc.NdefMessage;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import org.ndeftools.Message;
import org.ndeftools.MimeRecord;
import org.ndeftools.Record;
import org.ndeftools.externaltype.ExternalTypeRecord;
import org.ndeftools.wellknown.TextRecord;

import ir.saa.android.datalogger.G;
import ir.saa.android.datalogger.R;


public class ActivityNfcTest extends NfcReaderActivity {

    private static final String TAG = ActivityNfcTest.class.getName();
    protected Message message;

    TextView txtNfc1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_test);

        txtNfc1 = (TextView) findViewById(R.id.txtNfc1);

//        final Intent intent = new Intent(NfcAdapter.ACTION_TAG_DISCOVERED);
//        intent.putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, "Hiiii");
//        startActivity(intent);
        // lets start detecting NDEF message using foreground mode
        setDetecting(true);
    }

    /**
     * An NDEF message was read and parsed. This method prints its contents to log and then shows its contents in the GUI.
     *
     * @param message the message
     */

    @Override
    public void readNdefMessage(Message message) {
//        if(message.size() > 1) {
//            toast("readMultipleRecordNDEFMessage");
//        } else {
//            toast("readSingleRecordNDEFMessage");
//        }
        NdefMessage lowLevel = message.getNdefMessage();
        lowLevel.getRecords()[0].getId();
        this.message = message;

        // process message

        // show in log
        // iterate through all records in message
        Log.d(TAG, "Found " + message.size() + " NDEF records");

        for(int k = 0; k < message.size(); k++) {
            Record record = message.get(k);

            Log.d(TAG, "Record " + k + " type " + record.getClass().getSimpleName());

            // your own code here, for example:
            if(record instanceof MimeRecord) {
                txtNfc1.setText("MimeRecord : "+record.toString());
            } else if(record instanceof ExternalTypeRecord) {
                txtNfc1.setText("ExternalTypeRecord : "+record.toString());
            } else if(record instanceof TextRecord) {
                //txtNfc1.setText(((TextRecord) record).getText());
                txtNfc1.setText(this.NfcTagId);
                G.vibrator.vibrate(200);

//----wedding boogh----
//                    G.vibrator.vibrate(300);
//                    Thread.sleep(300);
//                    G.vibrator.vibrate(300);
//                    Thread.sleep(300);
//                    G.vibrator.vibrate(200);
//                    Thread.sleep(100);
//                    G.vibrator.vibrate(200);
//                    Thread.sleep(300);
//                    G.vibrator.vibrate(300);

            } else { // more else
                txtNfc1.setText("else : "+record.toString());
            }
        }

        // show in gui
        //showList();
    }


    /**
     * An empty NDEF message was read.
     *
     */

    @Override
    protected void readEmptyNdefMessage() {
        toast("readEmptyMessage");

        clearList();
    }

    /**
     *
     * Something was read via NFC, but it was not an NDEF message.
     *
     * Handling this situation is out of scope of this project.
     *
     */

    @Override
    protected void readNonNdefMessage() {
        toast("readNonNDEFMessage");

        hideList();
    }

    /**
     *
     * NFC feature was found and is currently enabled
     *
     */

    @Override
    protected void onNfcStateEnabled() {
        toast("nfcAvailableEnabled");
    }

    /**
     *
     * NFC feature was found but is currently disabled
     *
     */

    @Override
    protected void onNfcStateDisabled() {
        toast("nfcAvailableDisabled");
    }

    /**
     *
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     *
     */

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if(enabled) {
            toast("nfcAvailableEnabled");
        } else {
            toast("nfcAvailableDisabled");
        }
    }

    /**
     *
     * This device does not have NFC hardware
     *
     */

    @Override
    protected void onNfcFeatureNotFound() {

        toast("noNfcMessage");
    }

    /**
     *
     * Show NDEF records in the list
     *
     */

    private void showList() {
        // display the message
        // show in gui
//        ArrayAdapter<? extends Object> adapter = new NdefRecordAdapter(this, message);
//        ListView listView = (ListView) findViewById(R.id.recordListView);
//        listView.setAdapter(adapter);
//        listView.setVisibility(View.VISIBLE);
    }

    /**
     *
     * Hide the NDEF records list.
     *
     */

    public void hideList() {
//        ListView listView = (ListView) findViewById(R.id.recordListView);
//        listView.setVisibility(View.GONE);
    }

    /**
     *
     * Clear NDEF records from list
     *
     */

    private void clearList() {
//        ListView listView = (ListView) findViewById(R.id.recordListView);
//        listView.setAdapter(null);
//        listView.setVisibility(View.VISIBLE);
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    protected void onTagLost() {
        toast("tagLost");
    }


}