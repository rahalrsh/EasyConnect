package easyconnect.example.com.easyconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class LoginActivity extends Activity implements View.OnClickListener{

    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize
        ImageButton facebookLoginButton = (ImageButton)findViewById(R.id.facebookLoginButton);
        facebookLoginButton.setOnClickListener(this);

        Button next = (Button)findViewById(R.id.next);
        next.setOnClickListener(this);
        // do the same for the linkedinLoginButton and the other buttons

        // need logic to set 'isLoggedIn'
        Toast.makeText(getApplicationContext(), "need logic to check if user is already logged in to a social media site", Toast.LENGTH_LONG).show();
        isLoggedIn = false;
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
            case R.id.facebookLoginButton: {
                Log.i("LoginActivity","facebookLoginButton clicked");
                Toast.makeText(getApplicationContext(), "logged in with facebook successful ", Toast.LENGTH_SHORT).show();
                isLoggedIn = true;
                break;
            }

            case R.id.next:{
                if (isLoggedIn){
                    Intent intent = new Intent(this, ContactListActivity.class);
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
