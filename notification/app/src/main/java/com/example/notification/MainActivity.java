package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String channel_id = "01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Notification Permission (Android 13+)
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
        }

        Button b = findViewById(R.id.button);

        // ✅ ONLY ONE CLICK LISTENER
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MainActivity.this, channel_id)
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentTitle("Notification of Application")
                                .setContentText("This is my first push notification");

                NotificationManager nm =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // For Android Oreo and above
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    NotificationChannel nc = new NotificationChannel(
                            channel_id,
                            "CHANNEL_01",
                            NotificationManager.IMPORTANCE_HIGH
                    );

                    nm.createNotificationChannel(nc);
                }

                       }
        });
    }
}