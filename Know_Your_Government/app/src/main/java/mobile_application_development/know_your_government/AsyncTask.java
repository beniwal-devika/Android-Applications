package mobile_application_development.know_your_government;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by devikabeniwal on 06/04/17.
 */

public class AsyncTask extends android.os.AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncTask";
    private Exception exceptionToBeThrown = null;
    private MainActivity mainActivity;


    private final String senateUrl = "https://www.googleapis.com/civicinfo/v2/representatives";
    private final String yourAPIKey = "AIzaSyCbh2ICGf7B1IRtUVwlvCavY1p7Nwr0Vzs";

    public AsyncTask(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected String doInBackground(String... params) {

        StringBuilder sb = new StringBuilder();

        Uri.Builder buildURL = Uri.parse(senateUrl).buildUpon();
        buildURL.appendQueryParameter("key", yourAPIKey);
        buildURL.appendQueryParameter("address", params[0]);
        String urlToUse = buildURL.build().toString();

        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setDoOutput(true);
            //conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();

        /*if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            is = conn.getInputStream();
            } else {

               is = conn.getErrorStream();
            }*/

           BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
           while ((line = reader.readLine()) != null) {
               sb.append(line).append('\n');
            }


        } catch (Exception e) {
            e.printStackTrace();
           return null;
        }

        return sb.toString();

    }

    private ArrayList<SenateList> parseJSON(String s) {
        ArrayList<SenateList> senateList = new ArrayList<>();

        ArrayList<String> namesList = new ArrayList<String>();

        try{
            JSONObject jsonObj = new JSONObject(s);
            JSONArray offices = jsonObj.getJSONArray("offices");
            JSONArray officials = jsonObj.getJSONArray("officials");

            for(int i = 0 ; i < offices.length() ; i++){
                String name = offices.getJSONObject(i).optString("name");
                namesList.add(name);

            }
            for(int i = 0 ; i < officials.length() ; i++){


                String designation = officials.getJSONObject(i).optString("name");
                String party = officials.getJSONObject(i).optString("party");
                if(party.equals("")){
                    party = "Unknown";
                }
                String personName = namesList.get(i);

                String address = "";
                try{
                    JSONArray addressStr = officials.getJSONObject(i).getJSONArray("address");
                     address = addressStr.getJSONObject(0).optString("line1")+", "+addressStr.getJSONObject(0).optString("line2")+", "+
                            addressStr.getJSONObject(0).optString("city")+", "+addressStr.getJSONObject(0).optString("state")+", "+
                            addressStr.getJSONObject(0).optString("zip");

                }catch(Exception e){
                    e.printStackTrace();
                }

                String phone = "";
                try{
                    JSONArray phoneArray = officials.getJSONObject(i).getJSONArray("phones");
                    phone = (String) phoneArray.get(0);

                }catch(Exception e){
                    e.printStackTrace();
                }

                String website = "";

                try{
                    JSONArray urlsArray = officials.getJSONObject(i).getJSONArray("urls");
                     website = (String) urlsArray.get(0);

                }catch(Exception e){
                    e.printStackTrace();
                }

                String email = "";
                try{
                    JSONArray emailArray = officials.getJSONObject(i).getJSONArray("emails");
                    email = (String) emailArray.get(0);
                }catch(Exception e){
                    e.printStackTrace();
                }

                String photourl = "";
                try{
                    photourl = officials.getJSONObject(i).optString("photoUrl");
                }catch(Exception e){
                    e.printStackTrace();
                }

                String facebook_id = "";
                String googleplus_id = "";
                String twitter_id = "";
                String youtube_id = "";

                try{
                    JSONArray channelsArray = officials.getJSONObject(i).getJSONArray("channels");
                    for(int k = 0 ; k < channelsArray.length() ; k++){

                        String type = channelsArray.getJSONObject(k).optString("type");
                        if(type.equals("GooglePlus")){
                            googleplus_id = channelsArray.getJSONObject(k).optString("id");
                        }else if(type.equals("Facebook")){
                            facebook_id = channelsArray.getJSONObject(k).optString("id");
                        }else if(type.equals("Twitter")){
                            twitter_id = channelsArray.getJSONObject(k).optString("id");
                        }else if(type.equals("Youtube")){
                            youtube_id = channelsArray.getJSONObject(k).optString("id");
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                senateList.add(new SenateList(designation, personName, party, photourl,address,phone,email,website,facebook_id,twitter_id,googleplus_id,youtube_id));
            }

        }catch(Exception e){
            e.printStackTrace();
        }


        return senateList;

    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<SenateList> senateList = parseJSON(s);
        mainActivity.updateData(senateList);
    }
}
