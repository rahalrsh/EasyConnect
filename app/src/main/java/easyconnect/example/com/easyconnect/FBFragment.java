package easyconnect.example.com.easyconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class FBFragment extends Fragment {

    public FBFragment() {
    }

    private CallbackManager mCallbackManager;
    private TextView mTextDetails;
    private TextView profile_link;

    // Profile Picture
    private Bitmap profilePicBitmap;
    private byte[] img=null;

    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    Button saveButton;
    TextView firstnameTextview;
    TextView lastnameTextview;
    TextView phoneNumberTextview;

    SharedPreferences sharedPrefs;

    private void displayWelcomeMessage(Profile profile){
        if(profile!=null) {
            String FBName =profile.getName();
            mTextDetails.setText("Welcome " + FBName);
            profile_link.setText(
                    Html.fromHtml(
                            "<a href=\"" + profile.getLinkUri() + "\">See " + profile.getFirstName() + "'s profile</a> "));

            firstnameTextview.setText(profile.getFirstName());
            lastnameTextview.setText(profile.getLastName());
            //phoneNumber.setText(profile.getPhone());

            saveToSharedPreferences();
        }
    }

    private FacebookCallback<LoginResult> mCallback=new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken= loginResult.getAccessToken();
            Log.i("rahal", "*"+loginResult.getAccessToken().toString());
            Profile profile = Profile.getCurrentProfile();
            displayWelcomeMessage(profile);
            Toast.makeText(getActivity(), "Login to Facebook was successful", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(getActivity(), "Login to Facebook was not successful", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        mProfileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayWelcomeMessage(newProfile);
            }
        };

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fb, container, false);
        saveButton = (Button)v.findViewById(R.id.save_my_profile_button);
        firstnameTextview = (TextView)v.findViewById(R.id.my_first_name);
        lastnameTextview = (TextView)v.findViewById(R.id.my_last_name);
        phoneNumberTextview = (TextView)v.findViewById(R.id.my_phone);

        sharedPrefs = this.getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        firstnameTextview.setText(sharedPrefs.getString("firstName", ""));
        lastnameTextview.setText(sharedPrefs.getString("lastName", ""));
        phoneNumberTextview.setText(sharedPrefs.getString("phoneNumber", ""));

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends", "email", "user_location","user_likes");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);

        mTextDetails = (TextView)view.findViewById(R.id.text_details);
        profile_link =(TextView)view.findViewById(R.id.profile_link);
        profile_link.setMovementMethod(LinkMovementMethod.getInstance());

        addClickListener();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        mTokenTracker.stopTracking();
        mProfileTracker.startTracking();
    }

    public void addClickListener(){

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if name fileds are empty dont do anything
                if (firstnameTextview.getText() == null || firstnameTextview.getText().toString().isEmpty() ||
                        lastnameTextview.getText() == null || lastnameTextview.getText().toString().isEmpty() ||
                        phoneNumberTextview.getText() == null || phoneNumberTextview.getText().toString().isEmpty()
                        ) {
                    Toast.makeText(getActivity(), "Please Enter First Name, Last Name and Phone Number",
                            Toast.LENGTH_SHORT).show();
                }
                // else save first name and last name in android shared preferences
                else {
                    Toast.makeText(getActivity(), "Saved",
                            Toast.LENGTH_SHORT).show();
                    saveToSharedPreferences();
                }
            }
        });
    }

    public void saveToSharedPreferences(){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("firstName", firstnameTextview.getText().toString());
        editor.putString("lastName", lastnameTextview.getText().toString());
        editor.putString("phoneNumber", phoneNumberTextview.getText().toString());
        editor.apply();

        Log.i("SharedPref firstName", "" + firstnameTextview.getText().toString());
        Log.i("SharedPref lastName", ""+lastnameTextview.getText().toString());
        Log.i("SharedPref phoneNumber", ""+phoneNumberTextview.getText().toString());
    }
}



