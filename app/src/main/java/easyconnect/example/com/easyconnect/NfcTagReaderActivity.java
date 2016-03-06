package easyconnect.example.com.easyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;

import org.ndeftools.Message;
import org.ndeftools.boilerplate.NdefRecordAdapter;
import org.ndeftools.util.activity.NfcReaderActivity;
import org.ndeftools.wellknown.TextRecord;

/**
 * Created by nisalperera on 2015-12-25.
 */
public class NfcTagReaderActivity extends NfcReaderActivity {

    private static final String TAG = NfcTagReaderActivity.class.getName();

    protected Message message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(org.ndeftools.boilerplate.R.layout.reader);

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
        if(message.size() > 1) {
            toast(getString(org.ndeftools.boilerplate.R.string.readMultipleRecordNDEFMessage));
        } else {
            toast(getString(org.ndeftools.boilerplate.R.string.readSingleRecordNDEFMessage));
        }

        this.message = message;

        TextRecord textRecord  = (TextRecord)message.get(0);
        String tagText = textRecord.getText();
        String[] separated = tagText.split("\\|");

        // process message

        //
        // [contact_name]|[phone_number]|[ad_title]|[ad_description]|[ad_objectID]|[image_url]
        Intent intent = new Intent(NfcTagReaderActivity.this, CreateAdActivity.class);
        intent.putExtra("contact_name",separated[0]);
        intent.putExtra("phone_number",separated[1]);
        intent.putExtra("ad_title",separated[2]);
        intent.putExtra("ad_description", separated[3]);
        intent.putExtra("ad_objectID", separated[4]);
        intent.putExtra("image_url", separated[5]);
        String ObjectId = separated[4];
        Log.i("ObjectID=", ""+ObjectId);
        SendDataToBackend(ObjectId);
        startActivityForResult(intent, 0);
    }

    /*
        When the user is reading data from a NFC tag we collect the geological position of the user
        and send it to a backend server
     */
    private void SendDataToBackend(String AdObjId) {
        final String AdObjectId = AdObjId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserLocation.updateUserLocation(NfcTagReaderActivity.this);
                Double latitude = UserLocation.getLatitude();
                Double longitude = UserLocation.getLongitude();

                ParseObject locationupload = new ParseObject("tap_location");
                locationupload.put("AdId", ""+AdObjectId);
                locationupload.put("latitude", ""+latitude);
                locationupload.put("longitude", ""+longitude);

                Log.i("Loc", "read from tag: objectID=" + AdObjectId);

                try {
                    locationupload.save();
                    Log.i("Loc", "saved");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * An empty NDEF message was read.
     *
     */

    @Override
    protected void readEmptyNdefMessage() {
        toast(getString(org.ndeftools.boilerplate.R.string.readEmptyMessage));

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
        toast(getString(org.ndeftools.boilerplate.R.string.readNonNDEFMessage));

        hideList();
    }

    /**
     *
     * NFC feature was found and is currently enabled
     *
     */

    @Override
    protected void onNfcStateEnabled() {
        toast(getString(org.ndeftools.boilerplate.R.string.nfcAvailableEnabled));
    }

    /**
     *
     * NFC feature was found but is currently disabled
     *
     */

    @Override
    protected void onNfcStateDisabled() {
        toast(getString(org.ndeftools.boilerplate.R.string.nfcAvailableDisabled));
    }

    /**
     *
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     *
     */

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if(enabled) {
            toast(getString(org.ndeftools.boilerplate.R.string.nfcAvailableEnabled));
        } else {
            toast(getString(org.ndeftools.boilerplate.R.string.nfcAvailableDisabled));
        }
    }

    /**
     *
     * This device does not have NFC hardware
     *
     */

    @Override
    protected void onNfcFeatureNotFound() {
        toast(getString(org.ndeftools.boilerplate.R.string.noNfcMessage));
    }

    /**
     *
     * Show NDEF records in the list
     *
     */

    private void showList() {
        // display the message
        // show in gui
        ArrayAdapter<? extends Object> adapter = new NdefRecordAdapter(this, message);
        ListView listView = (ListView) findViewById(org.ndeftools.boilerplate.R.id.recordListView);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
    }

    /**
     *
     * Hide the NDEF records list.
     *
     */

    public void hideList() {
        ListView listView = (ListView) findViewById(org.ndeftools.boilerplate.R.id.recordListView);
        listView.setVisibility(View.GONE);
    }

    /**
     *
     * Clear NDEF records from list
     *
     */

    private void clearList() {
        ListView listView = (ListView) findViewById(org.ndeftools.boilerplate.R.id.recordListView);
        listView.setAdapter(null);
        listView.setVisibility(View.VISIBLE);
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    @Override
    protected void onTagLost() {
        toast(getString(org.ndeftools.boilerplate.R.string.tagLost));
    }

}
