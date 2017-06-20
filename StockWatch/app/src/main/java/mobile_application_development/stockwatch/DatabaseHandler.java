package mobile_application_development.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;

/**
 * Created by devikabeniwal on 17/03/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StockAppDB";
    private static final String TABLE_NAME = "StockWatchTable";
    private static final String SYMBOL = "StockSymbol";
    private static final String COMPANY = "CompanyName";

    // DB Table Create Code

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANY + " TEXT not null)";

    private SQLiteDatabase database;
    private static DatabaseHandler instance;

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: C'tor DONE");
    }

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHandler(context);
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    public void setupDb() {
        database = getWritableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void shutDown() {
        database.close();
    }

    public void deleteStock(String symbol) {
        Log.d(TAG, "deleteStock: Deleting Stock " + symbol);
        int cnt = database.delete(
                TABLE_NAME, SYMBOL+"= ?", new String[] { symbol });

        Log.d(TAG, "deleteStock: " + cnt);
    }

    public void addStock(Stock stock) {
        Log.d(TAG, "addStock: Adding " + stock.getSymbol());
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getSymbol());
        values.put(COMPANY, stock.getCompanyName());
        database.insert(TABLE_NAME, null, values);
        Log.d(TAG, "addStock: Add Complete");
    }

    public ArrayList<String[]> loadStocks() {
        Log.d(TAG, " loadStocks: Load all symbol-company entries from DB");
        ArrayList<String[]> stocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{ SYMBOL, COMPANY },
                null,
                null,
                null,
                null,
                null);

        if (cursor != null) { // Only proceed if cursor is not null
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0); // 1st returned column
                String company = cursor.getString(1);
                stocks.add(new String[]{symbol, company});
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stocks;
    }

    public void dumpLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
                Log.d(TAG, "dumpLog: " +
                        String.format("%s %-18s", SYMBOL + ":", symbol) +
                        String.format("%s %-18s", COMPANY + ":", company));
                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }
}
