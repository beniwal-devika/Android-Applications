package mobile_application_development.multinotes;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by devikabeniwal on 24/02/17.
 */

public class AsyncJsonData extends AsyncTask<String, Integer, String> {
    private MainActivity mainActivity;

    public AsyncJsonData(MainActivity main) {
        mainActivity = main;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Notes> notesList = parseJSON(s);
        mainActivity.updateData(notesList);
    }

    @Override
    protected String doInBackground(String... strings) {

        return readJsonFileFromDevice();
    }

    private String readJsonFileFromDevice() {
        try {

            InputStream inputStream = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.file_name));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            return new String(buffer, "UTF-8");
        } catch (FileNotFoundException e) { return null;
        } catch (Exception e) { return null; }
    }

    private ArrayList<Notes> parseJSON(String s) {
        ArrayList<Notes> notesList = new ArrayList<>();

        try {
            JSONArray list = new JSONArray(s);
            JSONArray sortedJsonArray = new JSONArray();
            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < list.length(); i++) {
                jsonValues.add(list.getJSONObject(i));
            }

            Collections.sort( jsonValues, new Comparator<JSONObject>() {
                private static final String KEY_NAME = "datetime";
                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    }
                    catch (JSONException e) {
                        //do something
                    }
                    return -valA.compareTo(valB);
                }
            });

            for (int i = 0; i < list.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }

            for (int i = 0; i < sortedJsonArray.length(); i++) {
                JSONObject jNotes = (JSONObject) sortedJsonArray.get(i);
                String title = jNotes.getString("title");
                String datetime = jNotes.getString("datetime");
                String notes = jNotes.getString("notes");
                notesList.add(new Notes(title, datetime, notes));
            }

            return notesList;
        } catch (Exception e) { return null; }
    }
}
