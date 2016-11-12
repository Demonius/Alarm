package by.lykashenko.alarm.Reciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;

import by.lykashenko.alarm.MainActivity;
import by.lykashenko.alarm.R;

public class AlarmReciever extends BroadcastReceiver {
    public AlarmReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String note = intent.getStringExtra("note");
        Integer id = Integer.parseInt(intent.getStringExtra("id"));
        long[] vibrate = new long[] {250,100,200,100,200};

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,id,intent1,PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.i)
                .setTicker(context.getResources().getString(R.string.alarm_set))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(note)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setVibrate(vibrate)
                .build();
        notificationManager.notify(id,notification);

//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
