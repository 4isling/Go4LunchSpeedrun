package com.Hayse.go4lunch.services.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.services.firebase.FirebaseHelper;
import com.Hayse.go4lunch.ui.activitys.MainActivity;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseMessagingHelper extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANEL_ID = MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY);


    public FirebaseMessagingHelper() {
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String notificationBody = remoteMessage.getNotification().getBody();
            if (remoteMessage.getNotification().getBody() != null) {
                sendNotification(notificationBody);
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
        firebaseHelper.getUserDataFireStore().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Workmate user = task.getResult().toObject(Workmate.class);
                if (user != null && user.getPlaceId() != null) {
                    firebaseHelper.getWorkmateForNotification(user.getPlaceId()).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            List<String> coworkers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task1.getResult()) {
                                coworkers.add(document.toObject(Workmate.class).getName());
                            }
                            String notificationContent = buildNotificationContent(user, coworkers);
                            showNotification(notificationContent);
                        } else {
                            Log.w(TAG, "sendNotification: ", task.getException());
                        }
                    });
                }
            } else {
                Log.w(TAG, "sendNotification:onComplete:", task.getException());
            }
        });
    }

    private String buildNotificationContent(Workmate user, List<String> coworkers) {
        String notificationContent = "Hey " + user.getName() + " remember to come at: " + user.getRestaurantName();
        if (coworkers.size() < 1) {
            notificationContent += " no other coworker signal";
        } else {
            notificationContent += " with: " + String.join(", ", coworkers);
        }
        return notificationContent;
    }

    private void showNotification(String notificationContent) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANEL_ID)
                        .setSmallIcon(R.drawable.ic_go4lunch_logo)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(notificationContent)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannelIfNeeded(notificationManager);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void createNotificationChannelIfNeeded(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }
}