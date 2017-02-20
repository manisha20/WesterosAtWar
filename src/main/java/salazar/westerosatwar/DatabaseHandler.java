package salazar.westerosatwar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salazar on 07-01-2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "WesterosDB";
    private static final String TableKings = "WesterosKings";

    // Table Columns names

    private static final String name = "name";
    private static final String currentRating = "currentRating";
    private static final String battlesWon = "battlesWon";
    private static final String battlesLost="battlesLost";
    private static final String totalBattles="totalBattles";
    private static final String mainBattleType="mainBattleType";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createKingsTable = "CREATE TABLE " + TableKings + "("
                + name + " TEXT PRIMARY KEY," + currentRating + " NUMERIC,"
                +battlesWon+ " NUMERIC," +battlesLost+ " NUMERIC,"+totalBattles+ " NUMERIC,"
                + mainBattleType + " TEXT" + ")";
        db.execSQL(createKingsTable);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TableKings);
        // Create tables again
        onCreate(db);
    }


    void addKing(King king) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(name, king.getName());
        values.put(currentRating, king.getCurrentRating());
        values.put(battlesWon, king.getBattlesWon());
        values.put(battlesLost, king.getBattlesLost());
        values.put(totalBattles, king.getTotalBattles());
        values.put(mainBattleType, king.getMainBattleType());

        // Inserting Row
        db.insert(TableKings, null, values);
        db.close(); // Closing database connection

    }

    // Getting single king data
    King getKing(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TableKings + " WHERE Name like '"+ name+"'";
        Log.d("selectQuery",selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            King king = new King();
            king.setName(cursor.getString(0));
            king.setCurrentRating(Double.parseDouble(cursor.getString(1)));
            king.setBattlesWon(Integer.parseInt(cursor.getString(2)));
            king.setBattlesLost(Integer.parseInt(cursor.getString(3)));
            king.setTotalBattles(Integer.parseInt(cursor.getString(4)));
            king.setMainBattleType(cursor.getString(5));
            return king;
        }
        else return null;
    }

    // Getting All kings data
    public List<King> getAllKingsData() {
        List<King> kingsList = new ArrayList<King>();
        String selectQuery = "SELECT  * FROM " + TableKings;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                King king = new King();
                king.setName(cursor.getString(0));
                king.setCurrentRating(Double.parseDouble(cursor.getString(1)));
                king.setBattlesWon(Integer.parseInt(cursor.getString(2)));
                king.setBattlesLost(Integer.parseInt(cursor.getString(3)));
                king.setTotalBattles(Integer.parseInt(cursor.getString(4)));
                king.setMainBattleType(cursor.getString(5));
                kingsList.add(king);
            } while (cursor.moveToNext());
        }

        return kingsList;
    }


    public int getKingsCount() {
        String countQuery = "SELECT  * FROM " + TableKings;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();

    }
}
