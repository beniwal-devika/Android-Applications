package mobile_application_development.know_your_government;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

/**
 * Created by devikabeniwal on 06/04/17.
 */

public class OfficialActivity extends AppCompatActivity{


    private ImageView imageView;
    private MainActivity mainActivity;
    TextView adr,eml,prty,des,web,ph,na;
    static String facebook_id = "";
    static String twitter_id = "";
    static String youtube_id = "";
    static String googleplus_id = "";

    private ImageView fb,tw,ytube,gplus;

    public void OfficialActivity(MainActivity ma){
        this.mainActivity = ma;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        View someView = findViewById(R.id.layout);
        // Find the root view
        View root = someView.getRootView();

        imageView = (ImageView) findViewById(R.id.sentatepicture);

        Intent intent = getIntent();
        final String location = intent.getStringExtra("location");

        ((TextView) findViewById(R.id.location)).setText(location);

        final String photo_url = intent.getStringExtra("photoUrl");
        String address = intent.getStringExtra("address");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String website = intent.getStringExtra("website");
        final String designation = intent.getStringExtra("designation");
        final String name = intent.getStringExtra("name");
        final String party = intent.getStringExtra("party");

        facebook_id = intent.getStringExtra("facebook");
        googleplus_id = intent.getStringExtra("googleplus");
        twitter_id = intent.getStringExtra("twitter");
        youtube_id = intent.getStringExtra("youtube");

        adr = (TextView)findViewById(R.id.address) ;
        address = address.replaceAll("  ", "");
        adr.setText(address);

        eml = (TextView)findViewById(R.id.email) ;
        eml.setText(email);

        ph = (TextView)findViewById(R.id.phone) ;
        ph.setText(phone);

        web = (TextView)findViewById(R.id.website) ;
        web.setText(website);

        na = (TextView)findViewById(R.id.name) ;
        na.setText(name);

        des = (TextView)findViewById(R.id.designation) ;
        des.setText(designation);

        prty = (TextView)findViewById(R.id.party) ;
        prty.setText("("+party+")");

        if(party.equals("Democratic")){
            root.setBackgroundColor(Color.BLUE);
        }else if(party.equals("Republican")){
            root.setBackgroundColor(Color.RED);
        }else{
            root.setBackgroundColor(Color.BLACK);
        }

        if(!photo_url.equals("")){
            loadImage(photo_url);
        }

        ImageView fb_iv = (ImageView) findViewById(R.id.facebook);
        ImageView tw_iv = (ImageView) findViewById(R.id.twitter);
        ImageView gp_iv = (ImageView) findViewById(R.id.googleplus);
        ImageView yt_iv = (ImageView) findViewById(R.id.youtube);

        if(facebook_id!=null && !facebook_id.equals("")) {
            fb_iv.setVisibility(View.VISIBLE);
            fb_iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    facebookClicked();
                }
            });
        }else{
            fb_iv.setVisibility(View.INVISIBLE);
        }

        if(twitter_id!=null && !twitter_id.equals("")) {
            tw_iv.setVisibility(View.VISIBLE);
            tw_iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    twitterClicked();
                }
            });
        }else{
            tw_iv.setVisibility(View.INVISIBLE);
        }

        if(googleplus_id!=null && !googleplus_id.equals("")) {
            gp_iv.setVisibility(View.VISIBLE);
            gp_iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    googlePlusClicked();
                }
            });
        }else{
            gp_iv.setVisibility(View.INVISIBLE);
        }

        if(youtube_id!=null && !youtube_id.equals("")) {
            yt_iv.setVisibility(View.VISIBLE);
            yt_iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    youTubeClicked();
                }
            });
        }else{
            yt_iv.setVisibility(View.INVISIBLE);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            // Start new list activity
            public void onClick(View v) {

                if(!photo_url.equals("") && photo_url!=null){
                    Intent mainIntent = new Intent(OfficialActivity.this, PhotoDetailActivity.class);
                    mainIntent.putExtra("name", name.toString());
                    mainIntent.putExtra("designation", designation.toString());
                    mainIntent.putExtra("party", party.toString());
                    mainIntent.putExtra("location", location.toString());
                    mainIntent.putExtra("photoUrl", photo_url);
                    startActivity(mainIntent);
                }
            }
        });



    }

    private void loadImage(final String imageURL) {

        if(imageURL!=null && !imageURL.equals("")){
            Picasso picasso = new Picasso.Builder(this)
                    .listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            final String changedUrl = imageURL.replace("http:", "https:");
                            picasso.load(changedUrl) .error(R.drawable.brokenimage)
                                    .placeholder(R.drawable.placeholder) .into(imageView);
                        }
                    })
                    .build();

            picasso.load(imageURL)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .resize(400, 400).centerCrop().into(imageView);
        }else{
            Picasso.with(this).load(imageURL)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(imageView);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void facebookClicked() {

            String FACEBOOK_URL = "https://www.facebook.com/" + facebook_id;
            String urlToUse;
            PackageManager packageManager = getPackageManager(); try {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) { //newer versions of fb app
                    urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else { //older versions of fb app
                    urlToUse = "fb://page/" + facebook_id;
                }
            } catch (PackageManager.NameNotFoundException e) {
                urlToUse = FACEBOOK_URL; //normal web url
            }
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(urlToUse));
            startActivity(facebookIntent);

    }

    public void twitterClicked() {
            Intent intent = null;
            String name = twitter_id;
            try {
                getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } catch (Exception e) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name)); }
            startActivity(intent);
    }

    public void googlePlusClicked() {

            String name = googleplus_id;
            Intent intent = null;
            try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setClassName("com.google.android.apps.plus",
                        "com.google.android.apps.plus.phone.UrlGatewayActivity");
                intent.putExtra("customAppUri", name);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
            }
    }

    public void youTubeClicked() {
            String name = youtube_id;
            Intent intent = null;
            try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.youtube");
                intent.setData(Uri.parse("https://www.youtube.com/" + name));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
            }
    }

}
