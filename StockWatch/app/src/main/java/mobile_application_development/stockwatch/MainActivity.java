package mobile_application_development.stockwatch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    private RecyclerView recyclerview;
    private SwipeRefreshLayout swiper;
    private StockAdapter mAdapter;
    private DatabaseHandler dbHandler;
    public List<Stock> stockList = new ArrayList<>();
    TextView text_symbol;

    private HashMap<String, String> sycmData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_symbol = (TextView) findViewById(R.id.symbol);
        recyclerview = (RecyclerView)findViewById(R.id.recycler);
        swiper = (SwipeRefreshLayout)findViewById(R.id.swiper);
        mAdapter = new StockAdapter(stockList,this);

        dbHandler = DatabaseHandler.getInstance(this);
        dbHandler.dumpLog();
        //dbHandler.setupDb();
        ArrayList<String[]> loadList = dbHandler.loadStocks();
        if(networkCheck()){
            for (int i = 0; i<loadList.size();i++) {
                String loadSym = loadList.get(i)[0];
                String loadCom = loadList.get(i)[1];
                new AsyncLoader(MainActivity.this).executeOnExecutor(AsyncLoader.THREAD_POOL_EXECUTOR, loadSym, "Fin_Load_Data",loadCom);
            }

            recyclerview.setAdapter(mAdapter);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                     doRefresh();
                }
            });
        }else{
           openNetworkAlertBox("Updated");
        }

    }

    public void openNetworkAlertBox(String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Stocks Cannot Be "+value+" Without A Network Connection");
        AlertDialog dialog = builder.create();
        dialog.show();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void doRefresh() {
        stockList.clear();
        mAdapter.notifyDataSetChanged();
        swiper.setRefreshing(false);

        if(networkCheck()){
            ArrayList<String[]> list = dbHandler.loadStocks();
            int len = list.size();
            for (int i = 0; i<len ;i++){
                String loadSym = list.get(i)[0];
                String loadCom = list.get(i)[1];
                new AsyncLoader(MainActivity.this).executeOnExecutor(AsyncLoader.THREAD_POOL_EXECUTOR, loadSym, "Fin_Load_Data",loadCom);
            }
        } else {
            openNetworkAlertBox("Updated");
        }
    }

    @Override
    public void onClick(View view) {

        int pos = recyclerview.getChildLayoutPosition(view);
        Stock m = stockList.get(pos);

        String URL = "http://www.marketwatch.com/investing/stock/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendPath(m.getSymbol());
        URL = buildURL.toString();
        intent.setData(Uri.parse(URL));
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        final int pos = recyclerview.getChildLayoutPosition(view);

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dbHandler.deleteStock(stockList.get(pos).getSymbol());
                stockList.remove(pos);
                mAdapter.notifyDataSetChanged();
                recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });
        build.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // dialog cancelled
            }
        });
        build.setIcon(R.drawable.delete);
        build.setTitle("Delete Stock?");
        build.setMessage("Delete Stock Symbol "+stockList.get(pos).getSymbol()+"?");
        AlertDialog dialog = build.create();
        dialog.show();
        return false;

    }

    @Override
    protected void onDestroy() {
        //DatabaseHandler.getInstance(this).shutDown();
        super.onDestroy();
    }

    public void loadFinancialData(String loadSymbol, String loadName,HashMap<String, Double> finData){

            Stock stock = new Stock(loadSymbol, loadName,
                    finData.get("LAST_TRADE_PRICE"),
                    finData.get("PRICE_CHANGE_AMOUNT"),
                    finData.get("PRICE_CHANGE_PER"));

            stockList.add(stock);
            Collections.sort(stockList, new Comparator<Stock>(){
                @Override
                public int compare(Stock a, Stock b) {
                    String valA = "", valB = "";
                    try {
                        valA = (String) a.getSymbol();
                        valB = (String) b.getSymbol();
                    } catch (Exception e) {

                    }
                    return -valB.compareTo(valA);
                }
            });
            mAdapter = new StockAdapter(stockList, this);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateFinancialData(HashMap<String, Double> finData,HashMap<String, String> tickData){

        String tick = tickData.get("TICKER_VALUE");
        String sym = sycmData.get("SYMBOL");

        if(!tick.equals(sym)){
            errorHandle(sym);
        }else{
            Stock stock = new Stock(sycmData.get("SYMBOL"),
                    sycmData.get("COMP_NAME"),
                    finData.get("LAST_TRADE_PRICE"),
                    finData.get("PRICE_CHANGE_AMOUNT"),
                    finData.get("PRICE_CHANGE_PER"));

            stockList.add(stock);


            Collections.sort(stockList, new Comparator<Stock>(){
                @Override
                public int compare(Stock a, Stock b) {
                    String valA = "", valB = "";
                    try {
                        valA = (String) a.getSymbol();
                        valB = (String) b.getSymbol();
                    } catch (Exception e) {

                    }
                    return -valB.compareTo(valA);
                }
            });


            mAdapter = new StockAdapter(stockList, this);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
        }

    }

    public void updateStockData(String symbol, String name) {

        if(checkDuplicateStock(symbol)){
            openDialogAlertForDuplicateEntry(symbol);
        }else{
            sycmData.put("SYMBOL", symbol);
            sycmData.put("COMP_NAME", name);
            Stock s = new Stock(symbol,name);
            dbHandler.addStock(s);
            new AsyncLoader(MainActivity.this).executeOnExecutor(AsyncLoader.THREAD_POOL_EXECUTOR, symbol, "Financial_Data");
            Toast.makeText(this, "Stock Added", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkDuplicateStock(String symbol){
        boolean match = false;
        ArrayList<String[]> list = dbHandler.loadStocks();
        for(int i=0;i<list.size();i++) {
            if (list.get(i)[0].equals(symbol)) {
                match = true;
                break;
            }
        }
        return match;
    }

    public void openDialogAlertForDuplicateEntry(String symbol){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.delete);
        builder.setTitle("Duplicate Stock");
        builder.setMessage("Stock Symbol " + symbol + " is already displayed");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void errorHandle(String symbol){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Symbol Not Found : " + symbol);
        builder.setMessage("Data for Stock Symbol");
        AlertDialog dialog = builder.create();
        dialog.show();
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
                if(networkCheck()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final EditText edittext = new EditText(this);
                    edittext.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                    edittext.setInputType(InputType.TYPE_CLASS_TEXT);
                    edittext.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setView(edittext);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String name =  edittext.getText().toString();
                            new AsyncLoader(MainActivity.this).executeOnExecutor(AsyncLoader.THREAD_POOL_EXECUTOR,name,"Stock_Data");
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    builder.setMessage("Please enter a Stock Symbol:");
                    builder.setTitle("Stock Selection");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    openNetworkAlertBox("Added");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
