package mobile_application_development.tempratureconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String STATE_SAVE = "savestate";
    private static double tempValue;
    RadioButton c2f,f2c;
    Button button;
    EditText edittext;
    TextView textview,t2;
    ArrayList<String> m_listItems = new ArrayList<String>();
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setEnabled(false);
        edittext = (EditText)findViewById(R.id.editText);
        t2 = (TextView)findViewById(R.id.textView5) ;
        c2f = (RadioButton)findViewById(R.id.radioButton4);
        f2c = (RadioButton)findViewById(R.id.radioButton3);
        listview = (ListView) findViewById(R.id.list_view);

        if (savedInstanceState != null) {
            m_listItems = (ArrayList<String>) savedInstanceState.getSerializable(STATE_SAVE);
            tempValue = (Double)savedInstanceState.getDouble("temp_float");
            t2.setText("" + tempValue);
        }else{
            m_listItems = new ArrayList<String>();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, m_listItems);
        listview.setAdapter(adapter);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                button.setEnabled(true);
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView lv = new ListView(getApplicationContext());


                if(edittext.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter value in input box",Toast.LENGTH_SHORT).show();
                }else{
                    String str1 = edittext.getText().toString();
                    double value = Double.parseDouble(str1);
                    if (c2f.isChecked()) {
                        double result = convertTempratureToFahrenheit(value);
                        tempValue = result;
                        String rep = "C to F " + value + " -> " + result;
                        m_listItems.add(new String(rep));
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);
                        t2.setText("" + result);


                    } else if (f2c.isChecked()) {
                        double result = convertTempratureToCelsius(value);
                        tempValue = result;
                        String rep = "F to C " + value + " -> " + result;
                        m_listItems.add(new String(rep));
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(adapter);
                        t2.setText("" + result);
                    }

                }

            }
        });
    }


    protected double convertTempratureToCelsius(double value){

        double ret =  (value-32)*5/9;
        double roundOff = Math.round(ret * 100.0) / 100.0;
        return roundOff;

    }

    protected double convertTempratureToFahrenheit(double value){
        double ret =  (value*9)/5+32;
        double roundOff = Math.round(ret * 100.0) / 100.0;
        return roundOff;

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("temp_float", tempValue);
        outState.putSerializable(STATE_SAVE, m_listItems);
    }



}
