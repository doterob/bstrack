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
import com.neura.standalonesdk.events.NeuraPushCommandFactory;

import es.bahiasoftware.bstrack.db.DbContract;
import es.bahiasoftware.bstrack.db.DbHelper;
import es.bahiasoftware.bstrack.iot.IoTManager;

import java.util.Map;

public class BstrackEventsService extends FirebaseMessagingService {

    private final IoTManager iotManager;

    public BstrackEventsService(){
        iotManager = IoTManager.getInstance();
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        iotManager.process(message);
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
