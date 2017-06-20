package mobile_application_development.multinotes;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by devikabeniwal on 24/02/17.
 */

public class EditActivity extends AppCompatActivity {
    EditText edt_notes,edt_title;

    private int location;
    private String datetime;
    private Notes notes;
    private MainActivity mainActivity;
    private Notes c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        edt_title = (EditText) findViewById(R.id.title_edittext);
        edt_notes = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        if (intent.hasExtra(Notes.class.getName())) {
            c = (Notes) intent.getSerializableExtra(Notes.class.getName());
            edt_title.setText(c.getTitle());
            edt_notes.setText(c.getNotes());
            datetime = (String) intent.getSerializableExtra(c.getDatetime());
        }
        location = (Integer) intent.getSerializableExtra("");
    }

    public String getDateTimeFormat(){

        Date date  = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("EEE, MMM dd,h:mm a", Locale.US);
        return sd.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Intent data = new Intent();

                if (edt_title.getText().toString().trim().equals("")){
                    data.putExtra("USER_MESSAGE", "un-titled activity was not saved");
                }else{

                    if (c != null && c.getTitle().equals(edt_title.getText().toString()) && c.getNotes().equals(edt_notes.getText().toString())){
                        data.putExtra("USER_DATETIME", c.getDatetime());
                    }else{
                        data.putExtra("USER_DATETIME", getDateTimeFormat());
                    }
                    data.putExtra("USER_TITLE", edt_title.getText().toString());
                    data.putExtra("USER_NOTES", edt_notes.getText().toString());
                }
                data.putExtra("NOTE_INDEX", location);
                setResult(RESULT_OK, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

        if(c != null && (!(c.getTitle().equals(edt_title.getText().toString())) || !(c.getNotes().equals(edt_notes.getText().toString())))){
            showDialogBox();
        }else if (c == null && (edt_title.getText().toString().length() > 0 || edt_notes.getText().toString().length() > 0)){
            showDialogBox();
        }else{
            super.onBackPressed();
        }
    }

    public void showDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent data = new Intent();
                if (c != null && c.getTitle().equals(edt_title.getText().toString()) && c.getNotes().equals(edt_notes.getText().toString())){
                    data.putExtra("USER_DATETIME", c.getDatetime());
                }else{
                    data.putExtra("USER_DATETIME", getDateTimeFormat());
                }
                data.putExtra("USER_TITLE", edt_title.getText().toString());
                data.putExtra("USER_NOTES", edt_notes.getText().toString());
                data.putExtra("NOTE_INDEX", location);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent data = new Intent();
                finish();

            }
        });
        builder.setMessage("Your note is not saved! Save note '"+edt_title.getText().toString()+"'?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
