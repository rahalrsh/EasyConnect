package easyconnect.example.com.easyconnect;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class ConfirmInfoActivity extends AppCompatActivity implements View.OnClickListener{

    DBHandler dbHandler;
    private byte[] img=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_info);

        TextView FBFirstNametextview = (TextView) findViewById(R.id.FBFirstName);
        TextView FBLastNametextview = (TextView) findViewById(R.id.FBLastName);
        ImageView profilepic = (ImageView) findViewById(R.id.fb_profile_pic);

        dbHandler = new DBHandler(getBaseContext());

        // Print info in db
        dbHandler.printDBInfo();
        Log.i("printDBInfo","Print Done");

        Intent loginIntent = getIntent();
        Bundle extras = loginIntent.getExtras();
        if (extras != null) {
            //FBFirstNametextview.setText(extras.getString("FBFirstName"));
            //TextView FBLastNametextview = (TextView) findViewById(R.id.FBLastName);
            //FBLastNametextview.setText(extras.getString("FBLastName"));
            //profilepic.setProfileId(extras.getString("FBProfileID"));
        }


        dbHandler.open();
        // Ideally this should be returnDataInRowOne
        Cursor c = dbHandler.returnData();
        if (c.moveToFirst()){
            FBFirstNametextview.setText(c.getString(c.getColumnIndex("firstName")));
            FBLastNametextview.setText(c.getString(c.getColumnIndex("lastName")));
            img=c.getBlob(c.getColumnIndex("img"));
            Bitmap b1= BitmapFactory.decodeByteArray(img, 0, img.length);
            profilepic.setImageBitmap(b1);
        }
        dbHandler.close();

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
            case R.id.confirm_profile_button: {
                Intent intent = new Intent(this, ContactListActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

}