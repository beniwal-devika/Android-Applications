package mobile_application_development.newsgateway;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by devikabeniwal on 04/05/17.
 */

public class AsyncTask1 extends android.os.AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncTask";
    private MainActivity mainActivity;
    private final String newsUrl = "https://newsapi.org/v1/sources";
    private final String yourAPIKey = "b99a2111d4814386a055b01fad7fe5b5";
    private ArrayList<String> newsCategory;


    public AsyncTask1(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected String doInBackground(String... params) {

        if(params[0].equals("specific")){
            StringBuilder sb = new StringBuilder();

            Uri.Builder buildURL = Uri.parse(newsUrl).buildUpon();
            buildURL.appendQueryParameter("apiKey", yourAPIKey);
            buildURL.appendQueryParameter("language", "en");
            buildURL.appendQueryParameter("country", "us");
            buildURL.appendQueryParameter("category", params[1]);
            String urlToUse = buildURL.build().toString();

            try {
                URL url = new URL(urlToUse);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
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
        }else{

            StringBuilder sb = new StringBuilder();

            Uri.Builder buildURL = Uri.parse(newsUrl).buildUpon();
            buildURL.appendQueryParameter("apiKey", yourAPIKey);
            buildURL.appendQueryParameter("language", "en");
            buildURL.appendQueryParameter("country", "us");
            String urlToUse = buildURL.build().toString();

            try {
                URL url = new URL(urlToUse);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
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

    }

    private ArrayList<NewsList> parseJSON(String s) {
        ArrayList<NewsList> newsList = new ArrayList<>();
        newsCategory = new ArrayList<>();
        newsCategory.add("all");
        try{
            JSONObject jsonObj = new JSONObject(s);
            JSONArray offices = jsonObj.getJSONArray("sources");

            for(int i = 0 ; i < offices.length() ; i++){
                String id = offices.getJSONObject(i).optString("id");
                String name = offices.getJSONObject(i).optString("name");
                String category = offices.getJSONObject(i).optString("category");
                newsList.add(new NewsList(id,name,category));
                if(!newsCategory.contains(category)){
                    newsCategory.add(category);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return newsList;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<NewsList> newsList = parseJSON(s);
        mainActivity.updateData(newsList, newsCategory);
    }
}

