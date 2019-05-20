package com.example.mingle;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.mingle.domain.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * 서비스로 실행되는 음악 플레이어 (노티바도 포함)
 * Music Player Service (contains Notifications)
 */
public class PlayerService extends Service implements PlayerInterface {

    // Media
    public static MediaPlayer mMediaPlayer = null;
    private List<Music> current_musics = new ArrayList<>();
    private int position = 0;

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
        Log.i("Service", "StartCommand. flags:"+flags+" startId: "+startId);


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

        String strUri;
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            current_musics = MediaLoader.musics; // TODO: Fix it
            position = extras.getInt("position");
//            strUri = extras.getString("MusicUri");
            strUri = current_musics.get(position).getMusicUri();
        } else // Noti Bar 에서 Intent 가 여기로 오고있는 중. 수정필요
            strUri = "content://media/external/audio/media/967";


        Log.i("ServiceInit", ""+strUri);
        Uri mediaUri = Uri.parse(strUri);
        mMediaPlayer = MediaPlayer.create(this, mediaUri);
        mMediaPlayer.setOnCompletionListener(mp -> next());
    }

    // Intent Action 에 넘어온 명령어를 분기시키는 함수
    private void handleAction(Intent intent) {
        if(intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();
        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            createNotification();
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


    private void createNotification() {

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
            prevIntent.setAction(ACTION_PREV);
            PendingIntent pPrevIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, prevIntent, 0);
            views.setOnClickPendingIntent(R.id.status_bar_prev, pPrevIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_prev, pPrevIntent);

            Intent playIntent = new Intent(this, PlayerService.class);
            playIntent.setAction(ACTION_PLAY);
            PendingIntent pPlayIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, playIntent, 0);
            views.setOnClickPendingIntent(R.id.status_bar_play, pPlayIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_play, pPlayIntent);

            Intent nextIntent = new Intent(this, PlayerService.class);
            nextIntent.setAction(ACTION_NEXT);
            PendingIntent pNextIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, nextIntent, 0);
            views.setOnClickPendingIntent(R.id.status_bar_next, pNextIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_next, pNextIntent);

            Intent stopIntent = new Intent(this, PlayerService.class);
            stopIntent.setAction(ACTION_STOP);
            PendingIntent pStopIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, stopIntent, 0);
            views.setOnClickPendingIntent(R.id.status_bar_collapse, pStopIntent);
            bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pStopIntent);

            views.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
            bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);

            views.setTextViewText(R.id.status_bar_track_name, current_musics.get(position).getTitle());
            bigViews.setTextViewText(R.id.status_bar_track_name, current_musics.get(position).getTitle());
            views.setTextViewText(R.id.status_bar_artist_name, current_musics.get(position).getArtist());
            bigViews.setTextViewText(R.id.status_bar_artist_name, current_musics.get(position).getArtist());
            bigViews.setTextViewText(R.id.status_bar_album_name, current_musics.get(position).getAlbum());

            Notification status = new Notification.Builder(this).build();

            status.contentView = views;
            status.bigContentView = bigViews;
            status.flags = Notification.FLAG_ONGOING_EVENT;
            status.icon = R.mipmap.ic_launcher; // TODO 앨범커버로 바꾸기
            //status.icon = convertUriToResInt(current_musics.get(position).getAlbumImgUri());
            status.contentIntent = pendingIntent;
            startForeground(NOTIFICATION_ID, status);
        }
    }

    /**
     * Uri 를 Resource ID 로 바꿔주는 함수(not working)
     */
    private int convertUriToResInt(String uriStr) {
        return getResources().getIdentifier(uriStr, "raw", getBaseContext().getPackageName());
    }

    @Override
    public void play() {
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public void prev() {
        if (position > 0)
            position = position - 1;

        Uri uri = Uri.parse(current_musics.get(position).getMusicUri());
        mMediaPlayer.release();
        mMediaPlayer = MediaPlayer.create(this, uri);
        //mMediaPlayer.setDataSource(this, uri);
        mMediaPlayer.start();

        createNotification();
    }

    @Override
    public void next() {
        if (current_musics.size() > position)
            position = position + 1;

        Uri uri = Uri.parse(current_musics.get(position).getMusicUri());
        mMediaPlayer.release();
        mMediaPlayer = MediaPlayer.create(this, uri);
        mMediaPlayer.start();

        createNotification();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
