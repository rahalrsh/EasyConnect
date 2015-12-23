package easyconnect.example.com.easyconnect;

/**
 * Created by nisalperera on 15-09-05.
 *
 * This object will encapsulate contact info of each person
 */
public class DataObject {

    private String mText1;
    private String mText2;
    private Long ad_id;
    private String imageURL;

    DataObject(String text1, String text2 , Long id, String URL){
        mText1 = text1;
        mText2 = text2;
        ad_id = id;
        imageURL = URL;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) { this.mText2 = mText2; }

    public Long getadId() {
        return ad_id;
    }

    public String getImageURL() { return imageURL;}
}