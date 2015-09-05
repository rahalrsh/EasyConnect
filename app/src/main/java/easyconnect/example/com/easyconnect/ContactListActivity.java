package easyconnect.example.com.easyconnect;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;


public class ContactListActivity extends Activity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);

        // initialize all the buttons
        CircleButton settingsButton = (CircleButton)findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

        CircleButton nfcConnect = (CircleButton) findViewById(R.id.nfcConnect);
        nfcConnect.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);

                Intent intent = new Intent(ContactListActivity.this, ContactInfoActivity.class);

                // Get the data of the item that user touches
                //String contactName = ((TextView) v).getText().toString();

                // put the dummy contact info as an extra field
                //intent.putExtra("Contact Name", ""+ contactName);
                startActivity(intent);
            }
        });
    }

    // adding dummy data for now - need to get all contact info from DB
    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        for (int index = 0; index < 20; index++) {
            DataObject obj = new DataObject("Some Primary Text " + index,
                    "Secondary " + index);
            results.add(index, obj);
        }
        return results;
    }

    // handing the circle buttons the side
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_button: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
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
}
