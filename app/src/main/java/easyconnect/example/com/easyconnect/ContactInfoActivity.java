package easyconnect.example.com.easyconnect;

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
import android.widget.Toast;


public class ContactInfoActivity extends AppCompatActivity implements OnClickListener {

    TextView contact_name;
    TextView ad_description;
    TextView phone_number;
    ImageView profile_pic;

    //TODO: integrate social media
    ImageView TwitterButton;
    ImageView LinkedInButton;
    ImageView FacebookButton;

    DBHandler dbHandler;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        dbHandler = new DBHandler(getBaseContext());
        Intent intent = getIntent();
        long adID = intent.getLongExtra("AD_ID", 1L);
        dbHandler.open();
        c = dbHandler.searchAdbyID(adID);
        c.moveToFirst();
        Toast.makeText(getApplicationContext(), "Title:"+ c.getString(0), Toast.LENGTH_LONG).show();
        dbHandler.close();

        // moving nfc to detailed contact page
        FloatingActionButton nfcConnect = (FloatingActionButton) findViewById(R.id.nfcConnect);
        nfcConnect.setOnClickListener(this);

        // initialize all User Info
        contact_name = (TextView) findViewById(R.id.ad_title);
        contact_name.setText(c.getString(0));

        contact_name = (TextView) findViewById(R.id.contact_name);
        contact_name.setText(c.getString(1));

        ad_description = (TextView) findViewById(R.id.ad_description);
        ad_description.setText(c.getString(2));

        phone_number = (TextView) findViewById(R.id.phone_number);
        phone_number.setText(c.getString(4));


        profile_pic = (ImageView) findViewById(R.id.ad_pic);
        profile_pic.setImageResource(R.drawable.default_img);

        //Initialize links
        FacebookButton = (ImageView) findViewById(R.id.facebook_button);
        FacebookButton.setOnClickListener(this);


        TwitterButton = (ImageView) findViewById(R.id.twitter_button);
        TwitterButton.setOnClickListener(this);


        LinkedInButton = (ImageView) findViewById(R.id.linkedin_button);
        LinkedInButton.setOnClickListener(this);

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
                Intent intent = new Intent();
                intent.setAction("launch.me.action.LAUNCH_IT");
                intent.putExtra("AD_Info",c.getString(0));
                startActivityForResult(intent, 0);
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
