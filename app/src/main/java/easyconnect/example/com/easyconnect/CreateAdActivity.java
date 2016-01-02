package easyconnect.example.com.easyconnect;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.graphics.Bitmap;



import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class CreateAdActivity extends AppCompatActivity implements View.OnClickListener{

    Button button;
    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private String objectID;

        DBHandler dbHandler;
        TextView fullName;
        TextView phoneNumber;
        TextView adTitle;
        TextView adDetails;
        ImageView adImage;
        TextView adImageUrl;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_ad);

            dbHandler = new DBHandler(getBaseContext());

            FloatingActionButton createAdButton = (FloatingActionButton) findViewById(R.id.create_ad_button);
            createAdButton.setOnClickListener(this);
            fullName = (TextView) findViewById(R.id.fullName);
            phoneNumber = (TextView) findViewById(R.id.phoneNumber);
            adTitle = (TextView) findViewById(R.id.adTitle);
            adDetails = (TextView) findViewById(R.id.adDetails);
            adImageUrl = (TextView) findViewById(R.id.adImageUrl);
            adImage = (ImageView)findViewById(R.id.adImage);

            Intent intent = getIntent();
            ComponentName caller = getCallingActivity();

            //check the caller activity
            if(caller != null && caller.getClassName().compareTo("easyconnect.example.com.easyconnect.NfcTagReaderActivity") == 0){
                //[contact_name]|[phone_number]|[ad_title]|[ad_description]|[image_url]
                fullName.setText(intent.getStringExtra("contact_name"));
                phoneNumber.setText(intent.getStringExtra("phone_number"));
                adTitle.setText(intent.getStringExtra("ad_title"));
                adDetails.setText(intent.getStringExtra("ad_description"));
                adImageUrl.setText(intent.getStringExtra("image_url"));
            }

            // Locate the button in main.xml
            button = (Button) findViewById(R.id.uploadbtn);

            // Capture button clicks
            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {

                    // in onCreate or any event where your want the user to
                    // select a file
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_PICTURE);

                }
            });
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_create_ad, menu);
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
                case R.id.create_ad_button: {

                    // Todo: Check which parent activity invoked this activity.
                    // Todo: if it is the NFC read, then make isMyAd=0
                    int isMyAd = 1;

                    String Name = fullName.getText().toString();
                    String phone = phoneNumber.getText().toString();
                    String Title = adTitle.getText().toString();
                    String Details = adDetails.getText().toString();
                    String ImageUrl = adImageUrl.getText().toString();

                    // changing image to a BitMap
                    adImage.setDrawingCacheEnabled(true);
                    adImage.buildDrawingCache();
                    Bitmap bm = adImage.getDrawingCache();

                    dbHandler.open();

                    //changing image to a Byte stream
                    byte[] image = dbHandler.getBytes(bm);

                    //insert ad info locally
                    long rowID = dbHandler.insertAd(Title, Name, Details, ImageUrl, phone, isMyAd,image);
                    long adID = dbHandler.selectLastInsearted();
                    dbHandler.close();

                    Toast.makeText(getApplicationContext(), "Inserted to AD_ID="+adID, Toast.LENGTH_LONG).show();

                    if (rowID != -1) {
                        Intent intent = new Intent(this, ContactInfoActivity.class);
                        intent.putExtra("AD_ID", adID);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error Inserting Data. Please Try Again", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);


                // Locate the image in res > drawable-hdpi
                //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                Bitmap bitmap = null;
                try {
                    bitmap = getBitmapFromUri(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("adpic.png", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();

                // Create a New Class called "ImageUpload" in Parse
                final ParseObject imgupload = new ParseObject("AdInfo_test");

                // Create a column named "ImageName" and set the string
                imgupload.put("ImageName", "Ad Pic");

                // Create a column named "ImageFile" and insert the image
                imgupload.put("ImageFile", file);
                imgupload.put("Name", "BeeKay");
                imgupload.put("Address", "Blah");
                // Create the class and the columns
                imgupload.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        // Now you can do whatever you want with the object ID, like save it in a variable
                        objectID = imgupload.getObjectId();
                        Log.d("BK:", objectID);
                    }
                });

                ParseQuery<ParseObject> query= ParseQuery.getQuery("AdInfo_test");
                query.getInBackground(objectID, new GetCallback<ParseObject>() {

                    @Override
                    public void done(ParseObject arg0, ParseException arg1) {
                        // TODO Auto-generated method stub

                        if (arg1 == null) {
                            //String ImageURL = arg0.getString("ImageFile");
                            ParseFile Image = (ParseFile) arg0.get("ImageFile");
                            Log.d("ImageURL:","the URL of " + objectID+ " is " + Image.getUrl());

                        } else {
                            Log.d("ImageURL", "Error: " + arg1.getMessage());
                        }
                    }
                });
                //Toast.makeText(getApplicationContext(), objectID, Toast.LENGTH_LONG).show();
                // Show a simple toast message
                Toast.makeText(CreateAdActivity.this, "Image Uploaded",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}
