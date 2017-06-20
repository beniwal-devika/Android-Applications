package mobile_application_development.notepad;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.widget.EditText;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView dTimeTextView;
    EditText edt;
    private Description Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dTimeTextView = (TextView) findViewById(R.id.date_time);
        edt = (EditText) findViewById(R.id.editText);
    }

    public String getDateTimeFormat(){

        Date date  = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("EEE, MMM dd,h:mm a",Locale.US);
        return sd.format(date);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String desc = "Last Update : "+getDateTimeFormat();
        Description.setNotes(edt.getText().toString());
        Description.setLastUpdateDatetime(desc);
        writeJsonFileToDevice();
    }
    @Override
    protected void onResume() {
        super.onStart();
        Description = readJsonFileFromDevice();
        if (Description != null) {
            edt.setText(Description.getNotes());
            edt.setSelection(edt.getText().toString().length());
            dTimeTextView.setText(Description.getLastUpdateDatetime());
        }
    }

    private Description readJsonFileFromDevice() {

        Description = new Description();
        try {
            InputStream input = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader jsonReader = new JsonReader(new InputStreamReader(input, getString(R.string.encoding)));

            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String data = jsonReader.nextName();
                if (data.equals("text")) {
                    Description.setNotes(jsonReader.nextString());
                } else if (data.equals("save_datetime")) {
                    Description.setLastUpdateDatetime(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

        } catch (FileNotFoundException e) {
            String desc = "Last Update : "+getDateTimeFormat();
            Description.setLastUpdateDatetime(desc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Description;
    }


    private void writeJsonFileToDevice() {

        try {
            FileOutputStream file_out_stream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(file_out_stream, getString(R.string.encoding)));
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("text").value(Description.getNotes());
            jsonWriter.name("save_datetime").value(Description.getLastUpdateDatetime());
            jsonWriter.endObject();
            jsonWriter.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}