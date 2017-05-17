package es.bahiasoftware.bstrack.db;

import android.provider.BaseColumns;

public class DbContract {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private DbContract() {}

    public static class EventTable implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_CLASS = "class";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DATA = "data";
    }

    public static String createEventTable(){
        EventTable definition = new DbContract.EventTable();
        return
                "CREATE TABLE " + definition.TABLE_NAME + " (" +
                        definition._ID + " INTEGER PRIMARY KEY," +
                        definition.COLUMN_NAME_TYPE + TEXT_TYPE + COMMA_SEP +
                        definition.COLUMN_NAME_CLASS + TEXT_TYPE + COMMA_SEP +
                        definition.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                        definition.COLUMN_NAME_DATA + " )";
    }

    public static String dropEventTable(){
        EventTable definition = new DbContract.EventTable();
        return "DROP TABLE IF EXISTS " + definition.TABLE_NAME;
    }
}
