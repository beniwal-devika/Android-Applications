package mobile_application_development.know_your_government;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerview;
    private SenateListAdapter mAdapter;
    public List<SenateList> senateList = new ArrayList<>();
    private Locator locator;
    public static String address = "";
    public  static String locationAddr = "";
    TextView loc;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        locator = new Locator(this);

        recyclerview = (RecyclerView)findViewById(R.id.recycler);
        mAdapter = new SenateListAdapter(senateList,this);

        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        if(networkCheck()){
            new AsyncTask(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, address);
        }else{
            openNetworkAlertBox();
        }

    }

    public void setData(double lat, double lon) {
        String address = doAddress(lat, lon);
        ((TextView) findViewById(R.id.location)).setText(address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent parentIntent1 = new Intent(this,MainActivity.class);
                startActivity(parentIntent1);
                return true;
            case R.id.search:

                if(networkCheck()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final EditText edittext = new EditText(this);
                    edittext.setInputType(InputType.TYPE_CLASS_TEXT);
                    edittext.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setView(edittext);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String name =  edittext.getText().toString();
                            doLocationName(name);
                            ((TextView) findViewById(R.id.location)).setText(locationAddr);
                            new AsyncTask(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, locationAddr);
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    builder.setTitle("Enter City, State or a Zip Code :");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    openNetworkAlertBox();
                }
                return true;

            case R.id.help:
                Intent in = new Intent(this,AboutActivity.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerview.getChildLayoutPosition(view);
        SenateList senatelist = senateList.get(pos);

        TextView loc = ((TextView) findViewById(R.id.location));

        String photourl = senatelist.getPhotoUrl();

        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("location", loc.getText());
        intent.putExtra("photoUrl", photourl);
        intent.putExtra("address", senatelist.getAddress());
        intent.putExtra("email", senatelist.getEmail());
        intent.putExtra("phone", senatelist.getPhone());
        intent.putExtra("name", senatelist.getName());
        intent.putExtra("designation", senatelist.getDesignation());
        intent.putExtra("party", senatelist.getParty());
        intent.putExtra("website", senatelist.getWebsite());
        intent.putExtra("facebook", senatelist.getFacebook_id());
        intent.putExtra("googleplus", senatelist.getGoogleplus_id());
        intent.putExtra("twitter", senatelist.getTwitter_id());
        intent.putExtra("youtube", senatelist.getYoutube_id());
        startActivity(intent);
    }

    private String doAddress(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);



        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");


                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();

                for (Address ad : addresses) {
                    Log.d(TAG, "doLocation: " + ad);

                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++)
                        sb.append("\t" + ad.getAddressLine(i) + "\n");

                        sb.append("\t" + ad.getCountryName() + " (" + ad.getCountryCode() + ")\n");
                        address =  ad.getPostalCode();

                }



                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());

            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        locator.shutdown();
        super.onDestroy();
    }

    public void updateData(ArrayList<SenateList> cList) {
        if(cList == null) return;
        mAdapter.updateList(cList);
    }

    public boolean networkCheck() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void openNetworkAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Data cannot be accessed/loaded without an internet connection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void doLocationName(String loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocationName(loc, 10);
            displayAddresses(addresses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayAddresses(List<Address> addresses) {
        StringBuilder sb = new StringBuilder();
        address = "";
        if (addresses.size() == 0) {
            locationAddr = "Nothing Found";
            //((TextView) findViewById(R.id.location)).setText("Nothing Found");
            return;
        }

        for (Address ad : addresses) {
            for (int i = 0; i < ad.getMaxAddressLineIndex(); i++)
                  sb.append("\t" + ad.getAddressLine(i));
        }

        locationAddr = sb.toString();
    }

    public void doLatLon(Double Latitude, Double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;

            addresses = geocoder.getFromLocation(
                    Latitude,
                    longitude,
                    10);
            displayAddressesAfterlatLong(addresses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayAddressesAfterlatLong(List<Address> addresses) {
        StringBuilder sb = new StringBuilder();
        if (addresses.size() == 0) {
            ((TextView) findViewById(R.id.location)).setText("Nothing Found");
            return;
        }

        for (Address ad : addresses) {
            for (int i = 0; i < ad.getMaxAddressLineIndex(); i++)
                address = ad.getPostalCode();
                if(!address.equals("")){
                    return;
                }
        }

    }
}
