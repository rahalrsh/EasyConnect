package easyconnect.example.com.easyconnect;

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
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_confirm_info, menu);
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

                    dbHandler.open();

                    String Name = fullName.getText().toString();
                    String phone = phoneNumber.getText().toString();
                    String Title = adTitle.getText().toString();
                    String Details = adDetails.getText().toString();
                    String ImageUrl = adImageUrl.getText().toString();

                    long rowID = dbHandler.insertAd(Title, Name, Details, ImageUrl, phone);
                    long adID = dbHandler.selectLastInsearted();
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
