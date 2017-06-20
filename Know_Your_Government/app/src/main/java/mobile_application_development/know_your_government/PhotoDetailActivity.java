package mobile_application_development.know_your_government;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by devikabeniwal on 08/04/17.
 */

public class PhotoDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    TextView des,na,loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photodetail);
        imageView = (ImageView) findViewById(R.id.bigpicture);

        Intent intent = getIntent();
        String photo_url = intent.getStringExtra("photoUrl");
        String designation = intent.getStringExtra("designation");
        String name = intent.getStringExtra("name");
        String party = intent.getStringExtra("party");
        String location = intent.getStringExtra("location");

        View someView = findViewById(R.id.detail);

        // Find the root view
        View root = someView.getRootView();

        if(party.equals("Democratic")){
            root.setBackgroundColor(Color.BLUE);
        }else if(party.equals("Republican")){
            root.setBackgroundColor(Color.RED);
        }else{
            root.setBackgroundColor(Color.BLACK);
        }

        loc = (TextView)findViewById(R.id.location) ;
        loc.setText(location);

        na = (TextView)findViewById(R.id.name) ;
        na.setText(name);

        des = (TextView)findViewById(R.id.designation) ;
        des.setText(designation);

        if(!photo_url.equals("")){
            loadImage(photo_url);
        }


    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
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
                    .fit().centerCrop().into(imageView);
        }else{
            Picasso.with(this).load(imageURL)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.missingimage)
                    .into(imageView);
        }

    }


}
