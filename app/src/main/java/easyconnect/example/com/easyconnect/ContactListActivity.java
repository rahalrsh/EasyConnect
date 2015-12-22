package easyconnect.example.com.easyconnect;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;



public class ContactListActivity extends AppCompatActivity implements View.OnClickListener{

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
        FloatingActionButton settingsButton = (FloatingActionButton)findViewById(R.id.contacts_button);
        settingsButton.setOnClickListener(this);

        FloatingActionButton createAdButton = (FloatingActionButton)findViewById(R.id.createAd_button);
        createAdButton.setOnClickListener(this);


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
            case R.id.contacts_button: {
                Intent intent = new Intent(this, ConfirmInfoActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.createAd_button: {
                Intent intent = new Intent(this, CreateAdActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
