package easyconnect.example.com.easyconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class ContactListActivity extends Activity implements OnClickListener, AdapterView.OnItemClickListener{

    private ListView contactsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // initialize all the buttons
        Button settingsButton = (Button)findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);


        contactsListView = (ListView)findViewById(R.id.contactslistView);

        // this is the floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(contactsListView);
        fab.setOnClickListener(this);

        List<String> dummyDataArray = new ArrayList<String>();
        //TODO: How and where do we sort the names alphabetically?
        dummyDataArray.add("Me");
        dummyDataArray.add("Rahal");
        dummyDataArray.add("Nisal");
        dummyDataArray.add("Bharath");
        dummyDataArray.add("Moataz");
        dummyDataArray.add("Obama");
        dummyDataArray.add("Putin");
        dummyDataArray.add("Iron man");
        dummyDataArray.add("Bat man");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dummyDataArray );

        contactsListView.setAdapter(arrayAdapter);
        contactsListView.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
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
            case R.id.settings_button: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.fab: {
                Intent i=new Intent();
                i.setAction("launch.me.action.LAUNCH_IT");
                startActivityForResult(i,0);
                break;
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, ContactInfoActivity.class);

        // Get the data of the item that user touches
        String contactName = ((TextView) view).getText().toString();

        // put the dummy contact info as an extra field
        intent.putExtra("Contact Name", ""+contactName);
        startActivity(intent);
    }
}
