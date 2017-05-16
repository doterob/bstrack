package es.bahiasoftware.bstrack;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.neura.standalonesdk.events.NeuraEvent;
import com.neura.standalonesdk.events.NeuraPushCommandFactory;
import com.neura.standalonesdk.service.NeuraApiClient;

import  es.bahiasoftware.bstrack.R;
import es.bahiasoftware.bstrack.db.DbContract;
import es.bahiasoftware.bstrack.db.DbHelper;

import java.sql.Timestamp;
import java.util.Map;

public class NeuraEventsService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Map data = message.getData();
        Log.i(getClass().getSimpleName(), "Received push type -> " + data.get("pushType"));
        if (NeuraPushCommandFactory.getInstance().isNeuraEvent(data)) {
            NeuraEvent event = NeuraPushCommandFactory.getInstance().getEvent(data);
            String eventText = event != null ? event.toString() : "couldn't parse data";
            Log.i(getClass().getSimpleName(), "received Neura event - " + eventText);
            generateNotification(getApplicationContext(), eventText);
            saveEvent(event);
        } else {
            saveEvent(String.valueOf(data.get("pushType")), System.currentTimeMillis(), null);
        }

    }

    private  void saveEvent(NeuraEvent event) {
        saveEvent(event.getEventName(), event.getEventTimestamp(), event.toString());
    }

    private  void saveEvent(String type, long time, String data) {

        DbHelper dbHelper = new DbHelper(getApplicationContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbContract.FeedEntry.COLUMN_NAME_TYPE, type);
        values.put(DbContract.FeedEntry.COLUMN_NAME_TIME, time);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DbContract.FeedEntry.TABLE_NAME, null, values);
    }

    private void generateNotification(Context context, String eventText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        String appName = context.getString(R.string.app_name);
        int stringId = context.getApplicationInfo().labelRes;
        if (stringId > 0)
            appName = context.getString(stringId);

        builder.setContentTitle(appName + " detected event")
                .setContentText(eventText)
                .setSmallIcon(R.drawable.neura_sdk_notification_status_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), context.getApplicationInfo().icon))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(eventText));
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notification);

        Toast.makeText(context, "Evento recibido : " + eventText, Toast.LENGTH_SHORT).show();
    }

}
