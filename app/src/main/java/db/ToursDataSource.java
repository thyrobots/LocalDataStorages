package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Tour;

/**
 * Created by THY on 12/29/2014.
 */
public class ToursDataSource {
    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    private static final String LOGTAG = "LD";

    private static final String[] allColumns = {
            ToursDBOpenHelper.COLUMN_ID,
            ToursDBOpenHelper.COLUMN_TITLE,
            ToursDBOpenHelper.COLUMN_DESC,
            ToursDBOpenHelper.COLUMN_PRICE,
            ToursDBOpenHelper.COLUMN_IMAGE
    };

    public ToursDataSource(Context context){
        dbhelper = new ToursDBOpenHelper(context);
    }

    public void open(){
        database = dbhelper.getWritableDatabase();
        Log.i(LOGTAG, "Database opened.");
    }

    public void close(){
        database.close();
        Log.i(LOGTAG, "Database closed.");
    }

    public Tour create(Tour tour){
        ContentValues values = new ContentValues();
        values.put(ToursDBOpenHelper.COLUMN_TITLE, tour.getTitle());
        values.put(ToursDBOpenHelper.COLUMN_DESC, tour.getDescription());
        values.put(ToursDBOpenHelper.COLUMN_PRICE, tour.getPrice());
        values.put(ToursDBOpenHelper.COLUMN_IMAGE, tour.getImage());
        long insertedid = database.insert(ToursDBOpenHelper.TABLE_TOURS,null,values);
        tour.setId(insertedid);
        return tour;
    }

    public List<Tour> findAll(){
        List<Tour> tours = new ArrayList<Tour>();

        Cursor cursor = database.query(ToursDBOpenHelper.TABLE_TOURS, allColumns,
                null,null,null,null,null);
        Log.i(LOGTAG,"Returned " + cursor.getCount() + "row(s).");

        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                Tour tour = new Tour();
                tour.setId(cursor.getLong(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_ID)));
                tour.setTitle(cursor.getString(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_TITLE)));
                tour.setDescription(cursor.getString(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_DESC)));
                tour.setPrice(cursor.getDouble(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_PRICE)));
                tour.setImage(cursor.getString(cursor.getColumnIndex(ToursDBOpenHelper.COLUMN_IMAGE)));

                tours.add(tour);
            }
        }
        return tours;
    }
}
