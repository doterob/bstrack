package es.bahiasoftware.bstrack.db;

import android.provider.BaseColumns;

/**
 * Created by dotebar on 16/05/2017.
 */

public class DbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DbContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_DATA = "data";
    }
}
