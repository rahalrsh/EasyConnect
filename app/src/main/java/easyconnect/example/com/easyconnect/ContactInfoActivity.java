package easyconnect.example.com.easyconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;




public class ContactInfoActivity extends AppCompatActivity implements OnClickListener {

    TextView contact_name;
    TextView home_address;
    TextView email_address;
    TextView phone_number;
    ImageView profile_pic;
    ImageView TwitterButton;
    ImageView LinkedInButton;
    ImageView FacebookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        // moving nfc to detailed contact page
        FloatingActionButton nfcConnect = (FloatingActionButton) findViewById(R.id.nfcConnect);
        nfcConnect.setOnClickListener(this);


        //insert dummy values into the database for testing purposes
        DatabaseInsertTest();


        //get the data base values for this contact name assuming it is unique for now
        String email = "n/a";
        DBHandler MyDataBaseHandler = new DBHandler(this);
        MyDataBaseHandler = MyDataBaseHandler.open();
        String query = "Select " + MyDataBaseHandler.EMAIL + " FROM " + MyDataBaseHandler.TABLE_NAME + " WHERE " + MyDataBaseHandler.FIRST_NAME + " LIKE 'evil'";
        Cursor c = MyDataBaseHandler.db.rawQuery(query, null);
        c.moveToFirst();

        if (c.getString(c.getColumnIndex("email")) != null) {
            email = c.getString(c.getColumnIndex("email"));
        }

        MyDataBaseHandler.close();


        // initialize all User Info
        contact_name = (TextView) findViewById(R.id.contact_name);
        contact_name.setText("DR.Evil");
        home_address = (TextView) findViewById(R.id.home_address);
        home_address.setText("h3e1m2");
        email_address = (TextView) findViewById(R.id.email_address);
        email_address.setText(email.toString());
        phone_number = (TextView) findViewById(R.id.phone_number);
        phone_number.setText("5142208630");
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        profile_pic.setImageResource(R.drawable.austin);

        //Initialize links
        FacebookButton = (ImageView) findViewById(R.id.facebook_button);
        FacebookButton.setOnClickListener(this);


        TwitterButton = (ImageView) findViewById(R.id.twitter_button);
        TwitterButton.setOnClickListener(this);


        LinkedInButton = (ImageView) findViewById(R.id.linkedin_button);
        LinkedInButton.setOnClickListener(this);
    }

    public void DatabaseInsertTest() {
        DBHandler MyDataBaseHandler = new DBHandler(this);
        MyDataBaseHandler = MyDataBaseHandler.open();
        MyDataBaseHandler.insertData("evil", "last name", "drEvil@gmail.com", "647-222-4567", "Evil Company", 1234567, "fb.com/DR.Evil", 454673, "twitter.com/D.Evil");
        MyDataBaseHandler.close();
    }

    public void onClick(View selected) {


        AlertDialog message;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/intent/user?screen_name=drevil"));
                        startActivity(browserIntent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


        // Create the AlertDialog object and return it
        message = builder.create();

        switch (selected.getId()) {
            case R.id.facebook_button: {
                message.show();
                break;
            }
            case R.id.twitter_button: {
                message.show();
                break;
            }
            case R.id.linkedin_button: {
                message.show();
                break;
            }
            case R.id.nfcConnect: {
                Intent i = new Intent();
                i.setAction("launch.me.action.LAUNCH_IT");
                startActivityForResult(i, 0);
                break;
            }

        }
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_contact_info, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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
    }
