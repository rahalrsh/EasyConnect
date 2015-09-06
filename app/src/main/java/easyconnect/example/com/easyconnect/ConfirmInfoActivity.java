package easyconnect.example.com.easyconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;



public class ConfirmInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private ProfilePictureView profilepic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_info);

        setContentView(R.layout.activity_confirm_info);
        profilepic = (ProfilePictureView) findViewById(R.id.fb_profile_pic);

        Intent loginIntent = getIntent();
        Bundle extras = loginIntent.getExtras();
        if (extras != null) {

            TextView FBFirstNametextview = (TextView) findViewById(R.id.FBFirstName);
            FBFirstNametextview.setText(extras.getString("FBFirstName"));
            TextView FBLastNametextview = (TextView) findViewById(R.id.FBLastName);
            FBLastNametextview.setText(extras.getString("FBLastName"));
            profilepic.setProfileId(extras.getString("FBProfileID"));
        }

        FloatingActionButton settingsButton = (FloatingActionButton) findViewById(R.id.confirm_profile_button);
        settingsButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_info, menu);
        return true;
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
            case R.id.confirm_profile_button: {
                Intent intent = new Intent(this, ContactListActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

}