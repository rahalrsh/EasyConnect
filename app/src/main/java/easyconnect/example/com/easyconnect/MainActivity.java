package easyconnect.example.com.easyconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
This is the first Activity that starts when we open the app
This activity checks if the user is loged in to facebook, twitter, google+ , linked in

// Begin pseudo code
If logged in
    open ContactListActivity
else
    open LoginActivity

then call finish() to finish this Activity
// End pseudo code

Note: we don't call setContentView(R.layout.activity_main) on this activity because we don't want users to see this activity
      i.e: we don't need a VIEW for this activity

StackOverFlow link: http://stackoverflow.com/questions/4856539/dynamic-start-activity-in-android
 **/

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Need logic to check if user is already logged in or if we have user(my self) info on a database. For now creating a boolean 'isLoggedIn'
        boolean isLoggedIn = false;
        Intent intent;

        if (isLoggedIn) {
            intent = new Intent(this, ContactListActivity.class);
        }
        else
        {
            intent = new Intent(this, LoginActivity.class);
        }

        // start a new activity
        startActivity(intent);

        // finish this activity
        finish();

    }
}
