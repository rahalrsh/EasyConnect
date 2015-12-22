package easyconnect.example.com.easyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CreateAdActivity extends AppCompatActivity implements View.OnClickListener{


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_ad);

            FloatingActionButton settingsButton = (FloatingActionButton) findViewById(R.id.confirm_ad_button);
            settingsButton.setOnClickListener(this);

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
                case R.id.confirm_ad_button: {
                    Intent intent = new Intent(this, ContactInfoActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }
