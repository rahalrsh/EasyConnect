package easyconnect.example.com.easyconnect;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by rahal on 2016-01-05.
 */
public class MyProfileActivity extends AppCompatActivity{
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private EditText firstName;

    //TextView firstnameTextview;
    //TextView lastnameTextview;

    //SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //firstnameTextview = (TextView)findViewById(R.id.my_first_name);
        //lastnameTextview = (TextView)findViewById(R.id.my_last_name);

        //sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        //firstnameTextview.setText(sharedPrefs.getString("firstName", ""));
        //lastnameTextview.setText(sharedPrefs.getString("lastName", ""));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //addClickListener();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MyProfile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://easyconnect.example.com.easyconnect/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

        //firstnameTextview.setText(sharedPrefs.getString("firstName", ""));
        //lastnameTextview.setText(sharedPrefs.getString("lastName", ""));
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MyProfile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://easyconnect.example.com.easyconnect/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /*
    public void addClickListener(){
        Button save = (Button)findViewById(R.id.save_my_profile_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if name fileds are empty dont do anything
                if (firstnameTextview.getText() == null || firstnameTextview.getText().toString().isEmpty() ||
                        lastnameTextview.getText() == null || lastnameTextview.getText().toString().isEmpty()){
                    Toast.makeText(MyProfileActivity.this, "Please Enter First Name and Last Name",
                            Toast.LENGTH_SHORT).show();
                }
                // else save first name and last name in android shared preferences
                else{
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("firstName",firstnameTextview.getText().toString());
                    editor.putString("lastName",lastnameTextview.getText().toString());
                    editor.apply();

                    Toast.makeText(MyProfileActivity.this, "Saved",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    */

}
