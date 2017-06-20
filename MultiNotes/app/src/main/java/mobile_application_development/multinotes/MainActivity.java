package mobile_application_development.multinotes;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static final int B_REQ = 1;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private List<Notes> notesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        myAdapter = new MyAdapter(notesList, this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initFilesIfNeeded();
        new AsyncJsonData(this).execute();
    }

    private void initFilesIfNeeded() {
        File file = this.getFileStreamPath(getString(R.string.file_name));
        if(!file.exists()) {
            try {
                FileOutputStream file_out_stream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(file_out_stream);
                writer.write("[]");
                writer.close();
            }
            catch (Exception e) { }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this,EditActivity.class);
                intent.putExtra("", -1);
                startActivityForResult(intent, B_REQ);
                return true;
            case R.id.about_us:
                Intent in = new Intent(this,AboutActivity.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJson();
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Notes c = notesList.get(pos);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("", pos);
        intent.putExtra(Notes.class.getName(), c);
        startActivityForResult(intent, B_REQ);
    }

    @Override
    public boolean onLongClick(View view) {

        final int position = recyclerView.getChildLayoutPosition(view);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesList.remove(position);
                myAdapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setMessage("Delete Note '"+notesList.get(position).getTitle()+"'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == B_REQ) {
            if (resultCode == RESULT_OK) {

                if (data.hasExtra("USER_MESSAGE")){
                    Toast.makeText(this,data.getStringExtra("USER_MESSAGE"),Toast.LENGTH_SHORT).show();
                }else{
                    String title = data.getStringExtra("USER_TITLE");
                    String datetime = data.getStringExtra("USER_DATETIME");
                    String note = data.getStringExtra("USER_NOTES");
                    int position = data.getIntExtra("NOTE_INDEX", -1);

                    if(position != -1 && notesList.size() > 0) {
                        Notes n = notesList.get(position);
                        n.setTitle(title);
                        n.setDatetime(datetime);
                        n.setNotes(note);
                    }
                    else notesList.add(new Notes(title, datetime, note));
                    writeJson();
                }

            }
        }
    }

    private void writeJson() {
        try {
            FileOutputStream file_out_stream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(file_out_stream, getString(R.string.encoding)));
            jsonWriter.setIndent("  ");
            jsonWriter.beginArray();

            for (Notes n: this.notesList) {
                jsonWriter.beginObject();
                jsonWriter.name("title").value(n.getTitle());
                jsonWriter.name("datetime").value(n.getDatetime());
                jsonWriter.name("notes").value(n.getNotes());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
            jsonWriter.close();
        } catch (Exception e) { Log.e("DEVIKA", "WF", e); }
    }

    public void updateData(ArrayList<Notes> cList) {
        if(cList == null) return;
        myAdapter.updateList(cList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFilesIfNeeded();
        new AsyncJsonData(this).execute();

    }
}
