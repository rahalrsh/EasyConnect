package easyconnect.example.com.easyconnect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;


public class CreateAdActivity extends AppCompatActivity implements View.OnClickListener{

    int isMyAd = 1;
    Button uploadImageButton;
    // this is the action code we use in our intent
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private String objectID;
    private ParseFile imageUploadFile;
    //This is the parseObject containing all relevant ad info stored in parse
    private ParseObject retrieveObject;
    //This is the image (bytes) contained in the above mentioned ParseObject
    private byte[] retrieveImage;
    DBHandler dbHandler;
    TextView fullName;
    TextView phoneNumber;
    TextView adTitle;
    TextView adDetails;
    TextView adImageUrl;
    ImageView adImage;
    // the adImage ImageView in bytes
    byte[] image;

    SharedPreferences sharedPrefs;

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
        // Locate the button in main.xml
        uploadImageButton = (Button) findViewById(R.id.uploadbtn);

        Intent intent = getIntent();
        ComponentName caller = getCallingActivity();

        //check the caller activity
        if(caller != null && caller.getClassName().compareTo("easyconnect.example.com.easyconnect.NfcTagReaderActivity") == 0){
            //Set global identifier to 0 i.e its not my ad
            isMyAd = 0;
            //[contact_name]|[phone_number]|[ad_title]|[ad_description]|[image_url]
            // No editing permission In the case of Extract from NFC
            fullName.setText(intent.getStringExtra("contact_name"));
            fullName.setEnabled(false);
            phoneNumber.setText(intent.getStringExtra("phone_number"));
            phoneNumber.setEnabled(false);
            adTitle.setText(intent.getStringExtra("ad_title"));
            adTitle.setEnabled(false);
            adDetails.setText(intent.getStringExtra("ad_description"));
            adDetails.setEnabled(false);
            // for now just hard code the default image url , otherwise picassa will crash
            adImageUrl.setText("http://www.bestbuy.ca/multimedia/Products/500x500/103/10399/10399242.jpg");
            adImageUrl.setEnabled(false);
            uploadImageButton.setVisibility(View.GONE);

            //If we are using NFC the Image is getting loaded from parse. This function sets the retrieveParseObject and retrieveImage
            RetrieveParseObjects(intent.getStringExtra("ad_objectID"));

            objectID = intent.getStringExtra("ad_objectID");
        }

        else{
            // User is creating this add.
            // Check ShredPreferenced and auto fill user information if available

            sharedPrefs = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String firstName = sharedPrefs.getString("firstName", "");
            String lastName = sharedPrefs.getString("lastName", "");
            String UserPhoneNumber =  sharedPrefs.getString("phoneNumber", "");
            String UserFullName="";
            if (!firstName.isEmpty() || !lastName.isEmpty()){
                UserFullName = firstName + " " + lastName;
            }

            if(!UserFullName.isEmpty())
                fullName.setText(UserFullName);
            if(!UserPhoneNumber.isEmpty())
                phoneNumber.setText(UserPhoneNumber);
        }

        // Capture button clicks
        uploadImageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                //You can only Upload if you are creating not if you are extracting NFC info
                if (isMyAd == 1) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_PICTURE);
                }
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
                // Todo: if it is the NFC read, then make isMyAd=0 Done


                String Name = fullName.getText().toString();
                String phone = phoneNumber.getText().toString();
                String Title = adTitle.getText().toString();
                String Details = adDetails.getText().toString();
                String ImageUrl = adImageUrl.getText().toString();

                // changing adImage to a BitMap
                adImage.setDrawingCacheEnabled(true);
                adImage.buildDrawingCache();
                Bitmap bitmap = adImage.getDrawingCache();

                //changing bitmap to a Byte stream and then byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                //changing image to a Byte stream
                image = dbHandler.getBytes(bitmap);

                // Do the following if you are creating an ad
                if(isMyAd == 1) {
                    // Create the ParseFile
                    ParseFile file = new ParseFile("adpic.jpg", image);
                    // Upload the Default image into Parse Cloud
                    try {
                        file.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //IMAGE URL, Moataz you can use this url to download a copy to the database
                    String image_url = file.getUrl();
                    adImageUrl.setText(image_url);
                    ImageUrl = adImageUrl.getText().toString();

                    //set global variable
                    imageUploadFile = file;


                    // Create a New Class called "ImageUpload" in Parse
                    final ParseObject imgupload = new ParseObject("Ad_info_test2");

                    // Create a column named "ImageName" and set the string
                    imgupload.put("ImageName", "Ad Pic");

                    // Create a column named "ImageFile" and insert the image
                    imgupload.put("ImageFile", imageUploadFile);
                    imgupload.put("Name", Name);
                    imgupload.put("Title", Title);
                    imgupload.put("Phone", phone);
                    imgupload.put("Details", Details);
                    // Create the class and the columns
                    try {
                        imgupload.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    objectID = imgupload.getObjectId();
                    Log.d("Bk:", "ObjectID:" + objectID);


                    //Toast.makeText(getApplicationContext(), objectID, Toast.LENGTH_LONG).show();
                    // Show a simple toast message
                    Toast.makeText(CreateAdActivity.this, "Image Uploaded",
                            Toast.LENGTH_SHORT).show();
                }

                //insert ad info locally. This is always done regardless if the ad is yours or not
                dbHandler.open();
                long rowID = dbHandler.insertAd(Title, Name, Details, ImageUrl, phone, isMyAd,image,objectID);
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
        if (resultCode == RESULT_OK ) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                String test = getPath(selectedImageUri);

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
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                //image = stream.toByteArray();
                //Bharath how do I uncompress
                adImage.setImageBitmap(bitmap);

            }
        }
    }
    /**
     * helper to set retrieve objects : retrieveResult and retrieveImageMap
     */
    public void RetrieveParseObjects(String ObjectID) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Ad_info_test2");
        query.getInBackground(ObjectID, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your imgupload ParseObject
                    retrieveObject = object;

                    // get the image bitmap from retrieveResult
                    ParseFile imageFile = (ParseFile) retrieveObject.get("ImageFile");


                    imageFile.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                // data has the bytes for the image
                                retrieveImage = data;
                                adImage.setImageBitmap(dbHandler.getImage(retrieveImage));
                            } else {
                                // something went wrong
                            }
                        }
                    });
                } else {
                    // something went wrong
                }
            }
        });

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
