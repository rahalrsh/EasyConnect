package easyconnect.example.com.easyconnect;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import at.markushi.ui.CircleButton;


public class LoginActivity extends FragmentActivity implements View.OnClickListener{

    boolean isLoggedIn;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize
        // this was replaced by bharath
        //ImageButton facebookLoginButton = (ImageButton)findViewById(R.id.facebookLoginButton);
        //facebookLoginButton.setOnClickListener(this);

        CircleButton next = (CircleButton)findViewById(R.id.next);
        next.setOnClickListener(this);
        // do the same for the linkedinLoginButton and the other buttons
        int i;
        //test
        // need logic to set 'isLoggedIn'
        Toast.makeText(getApplicationContext(), "need logic to check if user is already logged in to a social media site", Toast.LENGTH_LONG).show();
        isLoggedIn = true;

        dbHandler = new DBHandler(getBaseContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            /*case R.id.facebookLoginButton: {
                Log.i("LoginActivity","facebookLoginButton clicked");
                Toast.makeText(getApplicationContext(), "logged in with facebook successful ", Toast.LENGTH_SHORT).show();
                isLoggedIn = true;
                break;
            }*/ // bharath need to add the new logic

            case R.id.next:{
                if (isLoggedIn){

                    // insert data that we pulled from social media to the database
                    dbHandler.open();
                    long id = dbHandler.insertData("David", "david123@gmail.com");
                    Toast.makeText(getBaseContext(), "Data inserted to database", Toast.LENGTH_LONG).show();
                    Log.i("LoginActivity","Data inserted to database");
                    dbHandler.close();


                    // retrieve data from database
                    dbHandler.open();
                    Cursor c = dbHandler.returnData();
                    if (c.moveToFirst()){ // if cursor move to first that means there are some data
                        do{
                            Log.i ("LoginActivity","Name: "+ c.getString(0));
                            Log.i ("LoginActivity","Email: "+ c.getString(1));

                        }while (c.moveToNext());
                    }
                    dbHandler.close();


                    Intent intent = new Intent(this, ConfirmInfoActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "sorry you need to login first", Toast.LENGTH_SHORT).show();
                }

            }
            default:{
                Log.i("LoginActivity","nothing");
            }
        }

    }
}
