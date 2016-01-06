package easyconnect.example.com.easyconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


/**
 * A placeholder fragment containing a simple view.
 */
public class FBFragment extends Fragment {

    public FBFragment() {
    }

    private CallbackManager mCallbackManager;
    private TextView mTextDetails;
    private TextView profile_link;

    // Database
    DBHandler dbHandler;

    // Profile Picture
    private Bitmap profilePicBitmap;
    private byte[] img=null;

    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    private Intent displayWelcomeMessage(Profile profile){
        Intent intent = getActivity().getIntent();
        if(profile!=null) {
            String FBName =profile.getName();
            mTextDetails.setText("Welcome " + FBName + profile.getId());
            profile_link.setText(
                    Html.fromHtml(
                            "<a href=\"" + profile.getLinkUri() + "\">See " + profile.getFirstName() + "'s profile</a> "));
            intent.putExtra("FBFirstName", profile.getFirstName());
            intent.putExtra("FBLastName", profile.getLastName());
            intent.putExtra("FBProfileID", profile.getId());

            new DownloadImageTask(profilePicBitmap).execute(new String[]{profile.getProfilePictureUri(200, 200).toString(), profile.getFirstName(), profile.getLastName(), profile.getLinkUri().toString(), profile.getId().toString()});

        }
        return intent;
    }

    // Download the FB profile image
    // Save profile image and other FB data in the database (ex: first name, fb uid, fb link...etc)
    private class DownloadImageTask extends AsyncTask<String, Bitmap, Bitmap> {
        Bitmap bmImage;
        String firstName;
        String lastName;
        String FBLink;
        String FBuid;

        public DownloadImageTask(Bitmap bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            this.firstName = urls[1];
            this.lastName = urls[2];
            this.FBLink = urls[3];
            this.FBuid = urls[4];

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            profilePicBitmap = result;
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            profilePicBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            img = bos.toByteArray();

        }
    }

    private FacebookCallback<LoginResult> mCallback=new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken= loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Intent intent = displayWelcomeMessage(profile);
            Toast.makeText(getActivity(), "Login to Facebook was successful", Toast.LENGTH_SHORT).show();

            startActivity(intent);

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
        dbHandler = new DBHandler(getActivity().getApplicationContext());
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
        return inflater.inflate(R.layout.fragment_fb, container, false);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        mTokenTracker.stopTracking();
        mProfileTracker.startTracking();
    }
}



