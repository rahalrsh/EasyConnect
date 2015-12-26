package easyconnect.example.com.easyconnect;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import org.ndeftools.Message;
import org.ndeftools.externaltype.AndroidApplicationRecord;
import org.ndeftools.wellknown.TextRecord;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by nisalperera on 2015-12-25.
 */
public class NfcTagWriterActivity extends org.ndeftools.util.activity.NfcTagWriterActivity {

    // adinfo is pased in from the detailed view and it will be written to a nfc tag in createNdefMessage function
    private String adInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nfcwriter);

        Intent intent = getIntent();
        adInfo = intent.getStringExtra("AD_Info");
        Toast.makeText(getApplicationContext(), "Title:"+ adInfo, Toast.LENGTH_SHORT).show();
        setDetecting(true);

    }

    /**
     *
     * Create an NDEF message to be written when a tag is within range.
     *
     * @return the message to be written
     */

    @Override
    protected NdefMessage createNdefMessage() {

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

        return message.getNdefMessage();
    }

    /**
     * Get Google Play application identifier
     *
     * @return
     */

    private String getPlayIdentifier() {
        PackageInfo pi;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.applicationInfo.packageName;
        } catch (final PackageManager.NameNotFoundException e) {
            return getClass().getPackage().getName();
        }
    }

    /**
     *
     * Writing NDEF message to tag failed.
     *
     * @param e exception
     */

    @Override
    protected void writeNdefFailed(Exception e) {
        toast(getString(org.ndeftools.boilerplate.R.string.ndefWriteFailed, e.toString()));
    }

    /**
     *
     * Tag is not writable or write-protected.
     *
     * @param e exception
     */

    @Override
    public void writeNdefNotWritable() {
        toast(getString(org.ndeftools.boilerplate.R.string.tagNotWritable));
    }

    /**
     *
     * Tag capacity is lower than NDEF message size.
     *
     * @param e exception
     */

    @Override
    public void writeNdefTooSmall(int required, int capacity) {
        toast(getString(org.ndeftools.boilerplate.R.string.tagTooSmallMessage,  required, capacity));
    }


    /**
     *
     * Unable to write this type of tag.
     *
     */

    @Override
    public void writeNdefCannotWriteTech() {
        toast(getString(org.ndeftools.boilerplate.R.string.cannotWriteTechMessage));
    }

    /**
     *
     * Successfully wrote NDEF message to tag.
     *
     */

    @Override
    protected void writeNdefSuccess() {
        toast(getString(org.ndeftools.boilerplate.R.string.ndefWriteSuccess));
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
            toast(getString(org.ndeftools.boilerplate.R.string.nfcSettingEnabled));
        } else {
            toast(getString(org.ndeftools.boilerplate.R.string.nfcSettingDisabled));
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

