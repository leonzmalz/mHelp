package com.ifma.appmhelp.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.enums.IntentType;
import com.ifma.appmhelp.lib.MediaLib;
import com.ifma.appmhelp.views.SplashActivity;

/**
 * Created by leo on 1/18/17.
 */

public class ServiceNotification {

    public static int ID_NOTIFICATION = 12345;

    public static Notification createNotification(Context context, String ticker, String msg){
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        Intent deleteIntent = new Intent(IntentType.FINALIZAR_SERVICO.toString());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent deletePedingIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification);
        icon = MediaLib.getCircleBitmap(icon);

        return new NotificationCompat.Builder(context)
                .setLargeIcon(icon)
                .setSmallIcon(R.mipmap.ic_launcher) //Sem setar o icone não funciona
                .setContentTitle("mHelp")
                .setVisibility(Notification.VISIBILITY_SECRET)
                .setTicker(ticker)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .addAction(R.drawable.ic_cancel, "Fechar", deletePedingIntent)
                .setPriority(Notification.PRIORITY_MIN)
                .setContentIntent(pendingIntent).build();
    }
}
