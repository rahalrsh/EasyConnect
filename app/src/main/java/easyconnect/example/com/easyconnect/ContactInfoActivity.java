package easyconnect.example.com.easyconnect;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.database.Cursor;
import android.app.AlertDialog;
import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class ContactInfoActivity extends Activity implements OnClickListener{

    TextView contact_name ;
    TextView home_address ;
    TextView email_address ;
    TextView phone_number ;
    ImageView profile_pic;
    ImageView TwitterButton;
    ImageView LinkedInButton;
    ImageView FacebookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        //insert dummy values into the database for testing purposes
        DatabaseInsertTest();


        //get the data base values for this contact name assuming it is unique for now
        String email = "n/a";
        DBHandler MyDataBaseHandler = new  DBHandler(this);
        MyDataBaseHandler = MyDataBaseHandler.open();
        String query = "Select "+MyDataBaseHandler.EMAIL +" FROM "+ MyDataBaseHandler.TABLE_NAME +" WHERE "+MyDataBaseHandler.NAME+" LIKE 'evil'";
        Cursor c = MyDataBaseHandler.db.rawQuery(query,null);
        c.moveToFirst();

            if(c.getString(c.getColumnIndex("email"))!= null){
               email =  c.getString(c.getColumnIndex("email"));
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
        profile_pic = (ImageView)findViewById(R.id.profile_pic);
        profile_pic.setImageResource(R.drawable.austin);

        //Initialize links
        FacebookButton = (ImageView)findViewById(R.id.facebook_button);
        FacebookButton.setOnClickListener(this);


        TwitterButton = (ImageView)findViewById(R.id.twitter_button);
        TwitterButton.setOnClickListener(this);


        LinkedInButton = (ImageView)findViewById(R.id.linkedin_button);
        LinkedInButton.setOnClickListener(this);
    }

    public void DatabaseInsertTest()
    {
        DBHandler MyDataBaseHandler = new  DBHandler(this);
        MyDataBaseHandler = MyDataBaseHandler.open();
        MyDataBaseHandler.insertData("evil","drEvil@gmail.com");
        MyDataBaseHandler.close();
    }

    public void onClick(View selected) {


        AlertDialog message ;
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
            }
            case R.id.twitter_button: {
                message.show();
            }
            case R.id.linkedin_button: {
                message.show();
            }
            

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_info, menu);
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
}
