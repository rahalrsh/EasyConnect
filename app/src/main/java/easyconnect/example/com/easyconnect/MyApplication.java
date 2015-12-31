package easyconnect.example.com.easyconnect;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by bkashyap on 6/17/2015.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        printHashKey();
        // Add your initialization code here
        Parse.initialize(this, "ZFcKKaoiLqzeMdbEJXQjHVv7zIruhphnGRX6EePX", "1k6qhMReEU1iCDQNaPQDEHxLGlGQVZvgTVoOEBD8");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "easyconnect.example.com.easyconnect",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("BK:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
