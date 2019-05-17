package com.example.mingle;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

/**
 * 서비스로 실행되는 음악 플레이어 (노티바도 포함)
 * Music Player Service (contains Notifications)
 */
public class PlayerService extends Service implements PlayerInterface {

    // Media
    public static MediaPlayer mMediaPlayer = null;

    // Actions to control media
    public static final String ACTION_INIT = "ACTION_INIT";
    public static final String ACTION_MAIN = "ACTION_INIT";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_PREV = "ACTION_PREV";
    public static final String ACTION_NEXT = "ACTION_NEXT";

    // Notification
    private Notification.Builder builder;
    private NotificationCompat.Builder builder2;
    private static final int NOTIFICATION_ID = 1;


    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "StartCommand");

        //if (mMediaPlayer == null)
            init(intent);

        handleAction(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 초기화
     */
    private void init(Intent intent) {
        //Log.i("Service", "init");
        if (mMediaPlayer != null)
            mMediaPlayer.release();

//        String strUri = "content://media/external/audio/media/967";
        String strUri = "";
        if (intent.getExtras() != null) {
            strUri = intent.getStringExtra("MusicUri");
        } else
            strUri = "content://media/external/audio/media/967";
        Log.i("ServiceInit", ""+strUri);
        Uri mediaUri = Uri.parse(strUri);
        mMediaPlayer = MediaPlayer.create(this, mediaUri);
    }

    // Intent Action 에 넘어온 명령어를 분기시키는 함수
    private void handleAction(Intent intent) {
        if(intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();
        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            showNotification();
            play();
        }
        else if (action.equalsIgnoreCase(ACTION_PAUSE))
            pause();
        else if (action.equalsIgnoreCase(ACTION_PREV))
            prev();
        else if (action.equalsIgnoreCase(ACTION_NEXT))
            next();
        else if (action.equalsIgnoreCase(ACTION_STOP)) {
            stop();
        }
        //Log.i("Service", "init"+action);

//        switch (intent.getAction()) {
//            case Constants.ACTION.STARTFOREGROUND_ACTION: showNotification();
//                break;
//            case Constants.ACTION.PLAY_ACTION: play(); Log.i("Service", "handlePlay");
//                break;
//            case Constants.ACTION.PREV_ACTION: prev();
//                break;
//            case Constants.ACTION.NEXT_ACTION: next();
//                break;
//            case Constants.ACTION.STOPFOREGROUND_ACTION:
//                stopForeground(true);
//                stopSelf();
//                break;
//        }
    }

    /**
     * NotificationBar 만드는 함수
     */
//    private void createNotification(String action) {
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
//
//        if (Build.VERSION.SDK_INT >= 20) {
//            builder = new Notification.Builder(this);
//            builder.setStyle(new Notification.BigTextStyle(builder))
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("setTitle")
//                    .setContentText("setText");
//            builder.addAction(generateAction(ACTION_PREV, android.R.drawable.ic_media_previous, ""));
//            builder.addAction(generateAction(action, android.R.drawable.ic_media_pause, ""));
//            builder.addAction(generateAction(ACTION_NEXT, android.R.drawable.ic_media_next, ""));
//
//            notificationManager.notify(NOTIFICATION_ID, builder.build()); // 노티바를 띄운다. 첫번째 인자는 notification 닫을 때 id 값이 들어간다.
//            Log.i("Service", "builder1");
//        } else {
//            builder2 = new NotificationCompat.Builder(this);
//            builder2.setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle("setTitle")
//                    .setContentText("setText");
//            builder2.addAction(generateAction2(ACTION_PREV, android.R.drawable.ic_media_previous, ""));
//            builder2.addAction(generateAction2(action, android.R.drawable.ic_media_pause, ""));
//            builder2.addAction(generateAction2(ACTION_NEXT, android.R.drawable.ic_media_next, ""));
//
//            notificationManager.notify(NOTIFICATION_ID, builder2.build()); // 노티바를 띄운다. 첫번째 인자는 notification 닫을 때 id 값이 들어간다.
//        }
//    }
//
//    /**
//     * Notification Action 을 생성한다.
//     */
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH) // API 20
//    private Notification.Action generateAction(String action, int icon, String title) {
//        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
//        intent.setAction(action);
//        // PendingIntent : 실행 대상이 되는 인텐트를 지연시키는 역할
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
//
//        return new Notification.Action.Builder(icon, title, pendingIntent).build();
//    }
//    private NotificationCompat.Action generateAction2(String action, int icon, String title) {
//        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
//        intent.setAction(action);
//        // PendingIntent : 실행 대상이 되는 인텐트를 지연시키는 역할
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
//
//        return new NotificationCompat.Action.Builder(icon, title, pendingIntent).build();
//    }


    private void showNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // Using RemoteViews to bind custom layouts into Notification
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.status_bar);
            RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

            // showing default album image
            views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
            views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
            bigViews.setImageViewBitmap(R.id.status_bar_album_art, Constants.getDefaultAlbumArt(this));

            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(ACTION_MAIN);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, 0);

            Intent prevIntent = new Intent(this, PlayerService.class);
            Intent playIntent = new Intent(this, PlayerService.class);
            Intent nextIntent = new Intent(this, PlayerService.class);
            Intent stopIntent = new Intent(this, PlayerService.class);
            prevIntent.setAction(ACTION_PREV);
            playIntent.setAction(ACTION_PLAY);
            nextIntent.setAction(ACTION_NEXT);
            stopIntent.setAction(ACTION_STOP);
            PendingIntent pPrevIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, prevIntent, 0);
            PendingIntent pPlayIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, playIntent, 0);
            PendingIntent pNextIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, nextIntent, 0);
            PendingIntent pStopIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, stopIntent, 0);

            views.setOnClickPendingIntent(R.id.status_bar_play, pPlayIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_play, pPlayIntent);

            views.setOnClickPendingIntent(R.id.status_bar_next, pNextIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_next, pNextIntent);

            views.setOnClickPendingIntent(R.id.status_bar_prev, pPrevIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_prev, pPrevIntent);

            views.setOnClickPendingIntent(R.id.status_bar_collapse, pStopIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pStopIntent);

            views.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
            bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);

            views.setTextViewText(R.id.status_bar_track_name, "Song Title");
            bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

            views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
            bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

            bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

            Notification status = new Notification.Builder(this).build();
            status.contentView = views;
            status.bigContentView = bigViews;
            //status.flags = Notification.FLAG_ONGOING_EVENT;
            status.icon = R.mipmap.ic_launcher; // TODO: SET ALBUM COVER
            status.contentIntent = pendingIntent;
            startForeground(NOTIFICATION_ID, status);
        }
    }

    @Override
    public void play() {
        //createNotification(ACTION_PLAY);
        mMediaPlayer.start();

//        if (mMediaPlayer.isPlaying()) {
//            //createNotification(ACTION_PAUSE);
//            mMediaPlayer.pause();
//        }
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public void prev() {
        // TODO: add prev
    }

    @Override
    public void next() {
        // TODO: add next
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        //stopForeground(true);
        //stopSelf();
    }
}
