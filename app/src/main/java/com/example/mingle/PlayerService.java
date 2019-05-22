package com.example.mingle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.example.mingle.domain.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 서비스로 실행되는 음악 플레이어 (노티바도 포함)
 * Music Player Service (contains Notifications)
 */
public class PlayerService extends Service implements PlayerInterface {

    // Media
    public static MediaPlayer mMediaPlayer = null;
    public static Music cur_music = new Music();
    public static List<Music> cur_musics = new ArrayList<>();
    private int position = 0;

    // Actions to control media
    public static final String ACTION_INIT = "ACTION_INIT";
    public static final String ACTION_MAIN = "ACTION_INIT";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_PLAYPAUSE = "ACTION_PLAYPAUSE";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_PREV = "ACTION_PREV";
    public static final String ACTION_NEXT = "ACTION_NEXT";

    // Notification
    private static final int NOTIFICATION_ID = 1234;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private RemoteViews mRemoteViews;
    private Notification mNotification;


    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Service", "onBind");
        // Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "StartCommand. flags:"+flags+" startId: "+startId);

        if (intent.getExtras() != null && intent.getAction() != null) {
            init(intent); // both extras and action
            handleAction(intent);
        } else if (intent.getExtras() == null && intent.getAction() != null) {
            handleAction(intent); // only action
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 초기화
     */
    private void init(Intent intent) {
        if(intent == null || intent.getExtras() == null)
            return;

        if (mMediaPlayer != null)
            mMediaPlayer.release();

        String strUri;
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            cur_musics = MediaLoader.musics; // TODO: Fix it
            position = extras.getInt("position");
            strUri = cur_musics.get(position).getMusicUri();
        } else // Noti Bar 에서 명령한 Intent 가 여기로 오고있는 중. 수정필요
            strUri = "content://media/external/audio/media/967";

        Log.i("Service init()", ""+strUri);
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
            setUpNotification();
            play();
        }
        else if (action.equalsIgnoreCase(ACTION_PAUSE))
            pause();
        else if (action.equalsIgnoreCase(ACTION_PLAYPAUSE))
            playPause();
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

            views.setTextViewText(R.id.status_bar_track_name, cur_musics.get(position).getTitle());
            bigViews.setTextViewText(R.id.status_bar_track_name, cur_musics.get(position).getTitle());
            views.setTextViewText(R.id.status_bar_artist_name, cur_musics.get(position).getArtist());
            bigViews.setTextViewText(R.id.status_bar_artist_name, cur_musics.get(position).getArtist());
            bigViews.setTextViewText(R.id.status_bar_album_name, cur_musics.get(position).getAlbum());

            Notification status = new Notification.Builder(this).build();

            status.contentView = views;
            status.bigContentView = bigViews;
            status.flags = Notification.FLAG_ONGOING_EVENT;
            status.icon = R.mipmap.ic_launcher;
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


    /**
     * init Notification
     */
    private void setUpNotification(){

        // TODO 위에있는 노티바함수 이걸로 바꾸기
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set Notification's layout
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_small);
        if (cur_musics.get(position).getAlbumImgUri() != null) {
            mRemoteViews.setImageViewUri(R.id.iv_noti, Uri.parse(cur_musics.get(position).getAlbumImgUri())); // notification's icon
        } else {
            mRemoteViews.setImageViewResource(R.id.iv_noti, R.drawable.default_album_image); // notification's icon
        }
        mRemoteViews.setTextViewText(R.id.tv_notiTitle, cur_musics.get(position).getTitle()); // notification's title
        mRemoteViews.setTextViewText(R.id.tv_notiContent, cur_musics.get(position).getArtist()); // notification's content

        // Add Button Preview
        Intent prevIntent = new Intent(this, PlayerService.class);
        prevIntent.setAction(ACTION_PREV);
        PendingIntent pPrevIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, prevIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_notiPrev, pPrevIntent);
        // Add Button Pause
        Intent pauseIntent = new Intent(this, PlayerService.class);
        pauseIntent.setAction(ACTION_PLAYPAUSE);
        PendingIntent pPauseIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, pauseIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_notiPlayPause, pPauseIntent);
        // Add Button Next
        Intent nextIntent = new Intent(this, PlayerService.class);
        nextIntent.setAction(ACTION_NEXT);
        PendingIntent pNextIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, nextIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_notiNext, pNextIntent);
        // Add Button Close
        Intent closeIntent = new Intent(this, PlayerService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent pCloseIntent = PendingIntent.getService(getApplicationContext(), NOTIFICATION_ID, closeIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_notiClose, pCloseIntent);


        mBuilder = new NotificationCompat.Builder(this);
        CharSequence ticker = "ticker"; // 노티바 생성시 상태표시줄에 처음 표시되는 글자
        mBuilder.setSmallIcon(R.drawable.default_album_image) // 맨위 상태표시줄에 작은아이콘
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pendIntent)
                .setContent(mRemoteViews)
                .setTicker(ticker);

        startForeground(NOTIFICATION_ID, mBuilder.build()); // starting service with notification in foreground mode
    }

    // update the Notification's UI
    private void updateNotification(){
        // 현재 음악 재생중이면
        if (mMediaPlayer.isPlaying())
            mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_pause); // 노티바 버튼 변경
        else if (!mMediaPlayer.isPlaying())
            mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_play); // 노티바 버튼 변경

        // 앨범아트가 있는지 없는지 검사한다. 없으면 null 반환
        Uri albumArtUri = existAlbumArt(getBaseContext(), cur_musics.get(position).getAlbumImgUri());
        if (albumArtUri != null)
            mRemoteViews.setImageViewUri(R.id.iv_noti, albumArtUri); // set Album Artwork
        else
            mRemoteViews.setImageViewResource(R.id.iv_noti, R.drawable.default_album_image); // set default image

        mRemoteViews.setTextViewText(R.id.tv_notiTitle, cur_musics.get(position).getTitle()); /// update the title
        mRemoteViews.setTextViewText(R.id.tv_notiContent, cur_musics.get(position).getArtist()); // update the content

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());  // update the notification
    }

    /**
     * check if AlbumArt is exist
     * 앨범아트가 존재하는지 검사하는 함수.
     */
    private Uri existAlbumArt(Context mContext, String strUri) {
        Uri mUri = Uri.parse(strUri);
        Drawable d = null;
        if (mUri != null) {
            if ("content".equals(mUri.getScheme())) {
                try {
                    d = Drawable.createFromStream(mContext.getContentResolver().openInputStream(mUri), null);
                } catch (Exception e) {
                    Log.w("checkUriExists", "Unable to open content: " + mUri, e);
                    mUri = null;
                }
            } else
                d = Drawable.createFromPath(mUri.toString());

            if (d == null)
                mUri = null;
        }

        return mUri;
    }


    @Override
    public void play() {
        mMediaPlayer.start();
        updateNotification();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
        updateNotification();
    }

    @Override
    public void playPause() {
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
        else
            mMediaPlayer.start();

        updateNotification();
    }

    @Override
    public void prev() {
        if (position > 0)
            position = position - 1;

        Uri uri = Uri.parse(cur_musics.get(position).getMusicUri());
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getBaseContext(), uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateNotification();
    }

    @Override
    public void next() {
        if (cur_musics.size() > position)
            position = position + 1;

        Uri uri = Uri.parse(cur_musics.get(position).getMusicUri());
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getBaseContext(), uri);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateNotification();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        stopForeground(true);
        stopSelf(); // Stop Service
    }

    @Override
    public void onDestroy() {
        Log.i("Service", "onDestroy");
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
