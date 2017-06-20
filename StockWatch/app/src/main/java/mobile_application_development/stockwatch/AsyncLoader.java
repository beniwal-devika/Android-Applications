package mobile_application_development.stockwatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by devikabeniwal on 18/03/17.
 */

public class AsyncLoader extends AsyncTask<String, Void, String> {

    private static final String TAG = "AsyncLoaderTask";
    private Exception exceptionToBeThrown = null;

    private String task = "";
    private MainActivity mainActivity;

    private HashMap<String, String> wData = new HashMap<>();
    public static HashMap<String, String> tickData = new HashMap<>();
    public static HashMap<String, Double> finData = new HashMap<>();
    public static HashMap<String, String> loadData;

    public  String[] tempStockArray = new String[10];
    private String[] stockArray;
    public String loadSymbol , loadName = "";
    public String errorSymbol = "";


    private final String stockUrl = "http://stocksearchapi.com/api/";
    private final String yourAPIKey = "3d20daa3b7d69e5e28ec05db1ed261dd5320820b";
    private final String financialURL = "https://finance.google.com/finance/info?client=ig";

    public AsyncLoader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected String doInBackground(String... params) {

        String value = params[1];
        task = value;
        StringBuilder sb = new StringBuilder();


        switch(value){
            case "Stock_Data":
                Uri.Builder buildURL = Uri.parse(stockUrl).buildUpon();
                buildURL.appendQueryParameter("search_text", params[0]);
                buildURL.appendQueryParameter("api_key", yourAPIKey);
                String urlToUse = buildURL.build().toString();

                try {
                    URL url = new URL(urlToUse);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                } catch (Exception e) {
                    errorSymbol = params[0];
                    exceptionToBeThrown = e;
                }
                break;
            case "Financial_Data":
                Uri.Builder buildFinURL = Uri.parse(financialURL).buildUpon();
                buildFinURL.appendQueryParameter("q", params[0]);
                String finUrlToUse = buildFinURL.build().toString();

                try {
                    URL url = new URL(finUrlToUse);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                    String line;
                    while ((line = reader.readLine()) != null) {
                            sb.append(line).append('\n');
                    }
                    sb.replace(0,3,"");
                } catch (Exception e) {
                    errorSymbol = params[0];
                    exceptionToBeThrown = e;
                }

                break;
            case "Fin_Load_Data":

                String symbol = params[0];
                String company_name = params[2];

                loadSymbol = symbol;
                loadName = company_name;

                Uri.Builder buildLoadFinURL = Uri.parse(financialURL).buildUpon();
                buildLoadFinURL.appendQueryParameter("q", params[0]);
                String finLoadUrlToUse = buildLoadFinURL.build().toString();

                try {
                    URL url = new URL(finLoadUrlToUse);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                    sb.replace(0,3,"");
                } catch (Exception e) {
                    exceptionToBeThrown = e;
                }
                break;
            default:
                break;
        }

        if (exceptionToBeThrown == null) {
            parseJSON(sb.toString());
        }
        return value;
    }

    private void parseJSON(String s) {

        switch(task){
            case "Stock_Data":
                try {
                    JSONArray stockData = new JSONArray(s);

                    for(int i=0;i<stockData.length();i++){
                        JSONObject obj = (JSONObject) stockData.get(i);
                        String symbol = obj.getString("company_symbol");
                        String name = obj.getString("company_name");
                        if(symbol != null && symbol.length()>0 && name != null && name.length()>0) {
                            tempStockArray[i] = symbol+"-"+name;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Financial_Data":
                try{
                    JSONArray financialData = new JSONArray(s);
                    JSONObject jsonFinObj = (JSONObject) financialData.get(0);
                    tickData.put("TICKER_VALUE", jsonFinObj.getString("t"));
                    finData.put("LAST_TRADE_PRICE", jsonFinObj.getDouble("l"));
                    finData.put("PRICE_CHANGE_AMOUNT", jsonFinObj.getDouble("c"));
                    finData.put("PRICE_CHANGE_PER", jsonFinObj.getDouble("cp"));
                }catch (Exception e) {

                    e.printStackTrace();
                }

                break;
            case "Fin_Load_Data":
                try{
                    JSONArray financialData = new JSONArray(s);
                    JSONObject jsonFinObj = (JSONObject) financialData.get(0);
                    finData.put("LAST_TRADE_PRICE", jsonFinObj.getDouble("l"));
                    finData.put("PRICE_CHANGE_AMOUNT", jsonFinObj.getDouble("c"));
                    finData.put("PRICE_CHANGE_PER", jsonFinObj.getDouble("cp"));
                }catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;

        }
    }

    public String[] removeNullValues(){
        int j=0;
        int cnt = 0;

        for(int i =0;i<tempStockArray.length;i++){
            if(tempStockArray[i] != null){
                cnt++;
            }
        }
       stockArray = new String[cnt];

        for(int i =0;i<tempStockArray.length;i++){
            if(tempStockArray[i] != null){
                stockArray[j] = tempStockArray[i];
                j++;
            }
        }
        return stockArray;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        if (exceptionToBeThrown != null) {
            mainActivity.errorHandle(errorSymbol);
        }else {
            switch (s) {
                case "Stock_Data":

                    stockArray = removeNullValues();
                    if (stockArray.length > 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                        builder.setTitle("Make a selection");
                        builder.setItems(stockArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (stockArray[which] != null) {
                                    String[] parts = stockArray[which].split("-");
                                    if (parts.length > 0 && parts != null) {
                                        String symbol = parts[0];
                                        String name = parts[1];
                                        wData.put("SYMBOL", symbol);
                                        wData.put("COMP_NAME", name);
                                        mainActivity.updateStockData(symbol, name);
                                    }
                                }

                            }
                        });
                        builder.setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        String[] parts = stockArray[0].split("-");
                        if (parts.length > 0 && parts != null) {
                            String symbol = parts[0];
                            String name = parts[1];
                            mainActivity.updateStockData(symbol, name);
                        }
                    }
                    break;
                case "Financial_Data":
                    mainActivity.updateFinancialData(finData,tickData);
                    break;
                case "Fin_Load_Data":
                    mainActivity.loadFinancialData(loadSymbol, loadName, finData);
                    break;
                default:
                    break;
            }
        }

    }
}
