package easyconnect.example.com.easyconnect;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAdActivity extends AppCompatActivity implements View.OnClickListener{

        DBHandler dbHandler;
        TextView fullName;
        TextView phoneNumber;
        TextView adTitle;
        TextView adDetails;
        TextView adImageUrl;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_ad);

            dbHandler = new DBHandler(getBaseContext());

            FloatingActionButton createAdButton = (FloatingActionButton) findViewById(R.id.create_ad_button);
            createAdButton.setOnClickListener(this);
            fullName = (TextView) findViewById(R.id.fullName);
            phoneNumber = (TextView) findViewById(R.id.phoneNumber);
            adTitle = (TextView) findViewById(R.id.adTitle);
            adDetails = (TextView) findViewById(R.id.adDetails);
            adImageUrl = (TextView) findViewById(R.id.adImageUrl);

            Intent intent = getIntent();
            ComponentName caller = getCallingActivity();

            //check the caller activity
            if(caller != null && caller.getClassName().compareTo("easyconnect.example.com.easyconnect.NfcTagReaderActivity") == 0){
                //[contact_name]|[phone_number]|[ad_title]|[ad_description]|[image_url]
                fullName.setText(intent.getStringExtra("contact_name"));
                phoneNumber.setText(intent.getStringExtra("phone_number"));
                adTitle.setText(intent.getStringExtra("ad_title"));
                adDetails.setText(intent.getStringExtra("ad_description"));
                adImageUrl.setText(intent.getStringExtra("image_url"));
            }


        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_create_ad, menu);
            return true;
        }

        @Override
        protected void onResume() {
            super.onResume();

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.create_ad_button: {

                    // Todo: Check which parent activity invoked this activity.
                    // Todo: if it is the NFC read, then make isMyAd=0
                    int isMyAd = 1;

                    String Name = fullName.getText().toString();
                    String phone = phoneNumber.getText().toString();
                    String Title = adTitle.getText().toString();
                    String Details = adDetails.getText().toString();
                    String ImageUrl = adImageUrl.getText().toString();

                    dbHandler.open();
                    long rowID = dbHandler.insertAd(Title, Name, Details, ImageUrl, phone, isMyAd);
                    long adID = dbHandler.selectLastInsearted();
                    dbHandler.close();

                    Toast.makeText(getApplicationContext(), "Inserted to AD_ID="+adID, Toast.LENGTH_LONG).show();

                    if (rowID != -1) {
                        Intent intent = new Intent(this, ContactInfoActivity.class);
                        intent.putExtra("AD_ID", adID);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error Inserting Data. Please Try Again", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        }
    }
