package easyconnect.example.com.easyconnect;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.ndeftools.Message;
import org.ndeftools.boilerplate.DefaultNfcBeamWriterActivity;
import org.ndeftools.externaltype.GenericExternalTypeRecord;
import org.ndeftools.wellknown.TextRecord;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by nisalperera on 2016-03-06.
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NfcBeamWriterActivity extends org.ndeftools.util.activity.NfcBeamWriterActivity {

    private static final String TAG = DefaultNfcBeamWriterActivity.class.getName();

    protected Message message;
    // adinfo is pased in from the detailed view and it will be written to a nfc tag in createNdefMessage function
    private String adInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nfc_beam_writer);

        Intent intent = getIntent();
        adInfo = intent.getStringExtra("AD_Info");
        //Toast.makeText(getApplicationContext(), "Title:"+ adInfo, Toast.LENGTH_SHORT).show();

        setDetecting(true); // will start detecting NFC actions once onResume() is called.
    }

    /**
     * Implementation of {@link CreateNdefMessageCallback} interface.
     * <p/>
     * This method is called when another device is within reach (communication is to take place).
     */

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        Log.d(TAG, "Create message to be beamed");

        // compose our own message
        Message message = new Message();

        // add an Android Application Record so that this app is launches if a tag is scanned :-)
        //AndroidApplicationRecord androidApplicationRecord = new AndroidApplicationRecord();
        //androidApplicationRecord.setPackageName(getPlayIdentifier());
        //message.add(androidApplicationRecord);

        TextRecord textRecord = new TextRecord();
        textRecord.setText(adInfo);
        textRecord.setEncoding(Charset.forName("UTF-8"));
        textRecord.setLocale(Locale.ENGLISH);
        message.add(textRecord);

        // encode to NdefMessage, will be pushed via beam (now!) (unless there is a collision)
        return message.getNdefMessage();
    }

    /**
     * Implementation of {@link OnNdefPushCompleteCallback} interface.
     * <p/>
     * This method is called after a successful transfer (push) of a message from this device to another.
     */

    @Override
    protected void onNdefPushCompleted() {
        // make toast
        toast(org.ndeftools.boilerplate.R.string.nfcBeamed);

        // vibrate
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(500);
    }


    public void toast(int id) {

        toast(getString(id));
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    /**
     * NFC was found and enabled in settings, and push is enabled too.
     */

    @Override
    protected void onNfcPushStateEnabled() {
        toast(getString(org.ndeftools.boilerplate.R.string.nfcBeamAvailableEnabled));
    }

    /**
     * NFC was found and enabled in settings, but push is disabled
     */


    @Override
    protected void onNfcPushStateDisabled() {
        toast(getString(org.ndeftools.boilerplate.R.string.nfcBeamAvailableDisabled));
    }

    /**
     * NFC beam setting changed since last check. For example, the user enabled beam in the wireless settings.
     */

    @Override
    protected void onNfcPushStateChange(boolean enabled) {
        if (enabled) {
            toast(getString(org.ndeftools.boilerplate.R.string.nfcBeamAvailableEnabled));
        } else {
            toast(getString(org.ndeftools.boilerplate.R.string.nfcBeamAvailableDisabled));
        }
    }

    /**
     * NFC feature was found and is currently enabled
     */

    @Override
    protected void onNfcStateEnabled() {
        toast(getString(org.ndeftools.boilerplate.R.string.nfcAvailableEnabled));
    }

    /**
     * NFC feature was found but is currently disabled
     */

    @Override
    protected void onNfcStateDisabled() {
        toast(getString(org.ndeftools.boilerplate.R.string.nfcAvailableDisabled));
    }

    /**
     * NFC setting changed since last check. For example, the user enabled NFC in the wireless settings.
     */

    @Override
    protected void onNfcStateChange(boolean enabled) {
        if (enabled) {
            toast(getString(org.ndeftools.boilerplate.R.string.nfcAvailableEnabled));
        } else {
            toast(getString(org.ndeftools.boilerplate.R.string.nfcAvailableDisabled));
        }
    }

    /**
     * This device does not have NFC hardware
     */

    @Override
    protected void onNfcFeatureNotFound() {
        toast(getString(org.ndeftools.boilerplate.R.string.noNfcMessage));
    }

    /**
     * An NDEF message was read and parsed
     *
     * @param message the message
     */

    @Override
    protected void readNdefMessage(Message message) {
        if (message.size() > 1) {
            toast(getString(org.ndeftools.boilerplate.R.string.readMultipleRecordNDEFMessage));
        } else {
            toast(getString(org.ndeftools.boilerplate.R.string.readSingleRecordNDEFMessage));
        }
    }

    /**
     * An empty NDEF message was read.
     */

    @Override
    protected void readEmptyNdefMessage() {
        toast(getString(org.ndeftools.boilerplate.R.string.readEmptyMessage));
    }

    /**
     * Something was read via NFC, but it was not an NDEF message.
     * <p/>
     * Handling this situation is out of scope of this project.
     */

    @Override
    protected void readNonNdefMessage() {
        toast(getString(org.ndeftools.boilerplate.R.string.readNonNDEFMessage));
    }

    @Override
    protected void onNfcFeatureFound() {
        super.onNfcFeatureFound();

        startPushing();
    }

    @Override
    protected void onTagLost() {
        toast(getString(org.ndeftools.boilerplate.R.string.tagLost));
    }

}


