/***************************************************************************
 * 
 * This file is part of the 'NDEF Tools for Android' project at
 * http://code.google.com/p/ndef-tools-for-android/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ****************************************************************************/


package org.ndeftools.boilerplate;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

/**
 * 
 * Boilerplate activity selector.
 * 
 * @author Thomas Rorvik Skjolberg
 *
 */

public class AndroidNfcActivity extends Activity {
	
    private static final String TAG = AndroidNfcActivity.class.getName();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.main);
		
		if (android.os.Build.VERSION.SDK_INT >= 14) {
            View view = findViewById(R.id.beamer);
            view.setVisibility(View.VISIBLE);
        }

        // check to see if NFC is enabled on the device
        NfcAdapter mNfcAdapter= android.nfc.NfcAdapter.getDefaultAdapter(this);

        if (!mNfcAdapter.isEnabled()) {

            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Info");
            alertbox.setMessage("Enable NFC");
            alertbox.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                }
            });
            alertbox.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertbox.show();

        }


    }
    
    public void writer(View view) {
    	Log.d(TAG, "Show tag writer");
    	
    	Intent intent = new Intent(this, DefaultNfcTagWriterActivity.class);
    	startActivity(intent);
    }

    public void reader(View view) {
    	Log.d(TAG, "Show reader");
    	
    	Intent intent = new Intent(this, DefaultNfcReaderActivity.class);
    	startActivity(intent);
    }
    
    public void beamer(View view) {
    	Log.d(TAG, "Show beam writer");
    	
    	Intent intent = new Intent(this, DefaultNfcBeamWriterActivity.class);
    	startActivity(intent);
    }
    
}
