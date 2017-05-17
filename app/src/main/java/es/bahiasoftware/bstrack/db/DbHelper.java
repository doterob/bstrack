package es.bahiasoftware.bstrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.neura.standalonesdk.events.NeuraEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.bahiasoftware.bstrack.iot.IoTEvent;

public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "bstrack.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.createEventTable());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.dropEventTable());
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void save(IoTEvent event){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.EventTable.COLUMN_NAME_TYPE, event.getType().getName());
        values.put(DbContract.EventTable.COLUMN_NAME_CLASS, event.getClass().getSimpleName());
        values.put(DbContract.EventTable.COLUMN_NAME_TIME, event.getTime().toString());
        values.put(DbContract.EventTable.COLUMN_NAME_DATA, event.getData() != null ? event.getData().toString() : null);

        long newRowId = db.insert(DbContract.EventTable.TABLE_NAME, null, values);
    }

    public void save(NeuraEvent event){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.EventTable.COLUMN_NAME_TYPE, event.getEventName());
        values.put(DbContract.EventTable.COLUMN_NAME_CLASS, event.getClass().getSimpleName());
        values.put(DbContract.EventTable.COLUMN_NAME_TIME, event.getEventTimestamp());
        values.put(DbContract.EventTable.COLUMN_NAME_DATA, event.getMetadata().toString());

        long newRowId = db.insert(DbContract.EventTable.TABLE_NAME, null, values);
    }

    public List<Map> findAll(){

        List<Map> result = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cr = db.rawQuery("SELECT * FROM " + DbContract.EventTable.TABLE_NAME,null);

            if(cr != null){
                if(cr.moveToFirst()){
                    do{

                        String id = cr.getString(cr.getColumnIndex(DbContract.EventTable._ID));
                        String type = cr.getString(cr.getColumnIndex(DbContract.EventTable.COLUMN_NAME_TYPE));
                        String clazz = cr.getString(cr.getColumnIndex(DbContract.EventTable.COLUMN_NAME_CLASS));
                        String time = cr.getString(cr.getColumnIndex(DbContract.EventTable.COLUMN_NAME_TIME));
                        String data = cr.getString(cr.getColumnIndex(DbContract.EventTable.COLUMN_NAME_DATA));

                        Map e = new HashMap();
                        e.put("id", id);
                        e.put("time", time);
                        e.put("class", clazz);
                        e.put("type", type);
                        e.put("data", data);
                        result.add(e);
                    }while (cr.moveToNext());

                }
            }
            cr.close();
            db.close();
        }catch (Exception e){
            Log.e(getClass().getSimpleName(), e.toString());
        }finally {
            return result;
        }
    }
}