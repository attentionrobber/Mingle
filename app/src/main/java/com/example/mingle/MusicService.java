package com.example.mingle;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.mingle.domain.Music;

import java.io.IOException;
import java.util.List;

public class MusicService extends Service {

    private Context context;
    private final IBinder musicBind = new MusicBinder();

    // Control Music
    private MediaPlayer player; // media player
    private List<Music> playlist; // song list
    private Music song;
    private int position; // current position
    public boolean isPlaying = false;

    // Actions to control media
    //public static final String ACTION_INIT = "ACTION_INIT";
    //public static final String ACTION_MAIN = "ACTION_MAIN";
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
    //private Notification mNotification;

    // Communicate with MainActivity(PlayerService -> MainActivity)
    private LocalBroadcastManager localBroadcastManager;
    public static final String SERVICE_RESULT = "com.service.result";
    public static final String SERVICE_MESSAGE = "com.service.message";


    public MusicService() {

    }

    class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.reset();
        return false;
    }

    public void setList(List<Music> musics) {
        playlist = musics;
    }

    public void setSong(int songIndex) {
        position = songIndex;
    }

    public int getPosition() {
        return position;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MusicService_" ,"onCreate");
        context = getApplicationContext();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        position = 0;

        initMediaPlayer();
    }

    private void initMediaPlayer() {
        player = new MediaPlayer();
        //player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(mp -> {
            isPlaying = true;
            mp.start();
        });
        player.setOnCompletionListener(mp -> {
            Log.i("MusicService_" ,"onCompletionListener");
            next();
            sendToMainActivity(position);
        });
        player.setOnErrorListener((mp, what, extra) -> false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MusicService_" ,"onStartCommand");
        if (intent != null && intent.getAction() != null)
            handleAction(intent);

        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    // Intent Action 에 넘어온 명령어를 분기시키는 함수
    private void handleAction(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();
        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            play();
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            pause();
        } else if (action.equalsIgnoreCase(ACTION_PLAYPAUSE)) {
            playPause();
            sendToMainActivity(position);
        } else if (action.equalsIgnoreCase(ACTION_PREV)) {
            prev();
            sendToMainActivity(position);
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            next();
            sendToMainActivity(position);
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            Log.i("MusicService_", "ACTION_STOP");
            stop();
        }
    }

    public void play() {
        Log.i("MusicService_", "play()");
        if (player == null)
            initMediaPlayer();

        player.reset();

        if (playlist.size() != 0) {
            song = playlist.get(position); // get song
            Uri songUri = Uri.parse(song.getMusicUri()); // get Music URI

            try {
                player.setDataSource(context, songUri);
            } catch (Exception e) {
                Log.e("MusicService_", "Error setting data source", e);
            }
            player.prepareAsync();

            if (mNotificationManager == null)
                setUpNotification(song);
            else updateNotification(song);
        }
    }

    public void pause() {
        Log.i("MusicService_", "pause()");
        player.pause();
        isPlaying = false;
        updateNotification(song);
    }

    public void playPause() {
        Log.i("MusicService_", "playPause()");
        if (player != null) {
            if (isPlaying) {
                player.pause();
                isPlaying = false;
                mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_play); // 노티바 버튼 변경
            }
            else {
                player.start();
                isPlaying = true;
                mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_pause); // 노티바 버튼 변경
            }

            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());  // update the notification
        }
    }

    public void prev() {
        Log.i("MusicService_", "prev()");
        if (position > 0) position = position - 1;
        //play();

        if (player != null && playlist.size() != 0) {
            song = playlist.get(position); // get song
            String path = song.getPath();
            try {
                player.reset();
                player.setDataSource(path);
                player.prepareAsync();
                isPlaying = true;
                updateNotification(song);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void next() {
        Log.i("MusicService_", "next()");
        if (playlist.size()-1 > position) position = position + 1;
        //play();
        if (player != null && playlist.size() != 0) {
            song = playlist.get(position); // get song
            String path = song.getPath();
            try {
                player.reset();
                player.setDataSource(path);
                player.prepareAsync();
                isPlaying = true;
                updateNotification(song);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void stop() {
        Log.i("MusicService_", "stop()");
        if (player!= null) {
            player.stop();
            mNotificationManager.cancelAll();
            stopForeground(true);
            stopSelf(); // Stop Service
        }
    }

    @Override
    public void onDestroy() {
        Log.i("MusicService_", "onDestroy");
        if (player != null) {
            player.release();
            player = null;
        }
    }


    /**
     * init Notification
     */
    private void setUpNotification(Music song) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set Notification's layout
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_small);
        if (song.getAlbumImgUri() != null) {
            mRemoteViews.setImageViewUri(R.id.iv_noti, Uri.parse(song.getAlbumImgUri())); // notification's icon
        } else {
            mRemoteViews.setImageViewResource(R.id.iv_noti, R.drawable.default_album_image); // notification's icon
        }
        mRemoteViews.setTextViewText(R.id.tv_notiTitle, song.getTitle()); // notification's title
        mRemoteViews.setTextViewText(R.id.tv_notiContent, song.getArtist()); // notification's content

        // Add Button Preview
        Intent prevIntent = new Intent(this, MusicService.class);
        prevIntent.setAction(ACTION_PREV);
        PendingIntent pPrevIntent = PendingIntent.getService(context, NOTIFICATION_ID, prevIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_notiPrev, pPrevIntent);
        // Add Button Pause
        Intent pauseIntent = new Intent(this, MusicService.class);
        pauseIntent.setAction(ACTION_PLAYPAUSE);
        PendingIntent pPauseIntent = PendingIntent.getService(context, NOTIFICATION_ID, pauseIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_notiPlayPause, pPauseIntent);
        // Add Button Next
        Intent nextIntent = new Intent(this, MusicService.class);
        nextIntent.setAction(ACTION_NEXT);
        PendingIntent pNextIntent = PendingIntent.getService(context, NOTIFICATION_ID, nextIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_notiNext, pNextIntent);
        // Add Button Close
        Intent closeIntent = new Intent(this, MusicService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent pCloseIntent = PendingIntent.getService(context, NOTIFICATION_ID, closeIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_notiClose, pCloseIntent);


        mBuilder = new NotificationCompat.Builder(this);
        CharSequence ticker = song.getTitle(); // 노티바 생성시 상태표시줄에 처음 표시되는 글자
        mBuilder.setSmallIcon(R.drawable.default_album_image) // 맨위 상태표시줄에 작은아이콘
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pendIntent)
                .setContent(mRemoteViews)
                .setTicker(ticker);

        startForeground(NOTIFICATION_ID, mBuilder.build()); // starting service with notification in foreground mode
    }

    // Update the Notification's UI
    private void updateNotification(Music song) {
        if (isPlaying)
            mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_pause); // 노티바 버튼 변경
        else
            mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_play); // 노티바 버튼 변경

        // 앨범아트가 있는지 없는지 검사하는 함수 삭제함(성능문제)
//        if (albumArtUri != null)
//            mRemoteViews.setImageViewUri(R.id.iv_noti, Uri.parse(song.getAlbumImgUri())); // set Album Artwork
//        else
            mRemoteViews.setImageViewResource(R.id.iv_noti, R.drawable.default_album_image); // set default image

        mRemoteViews.setTextViewText(R.id.tv_notiTitle, song.getTitle()); /// update the title
        mRemoteViews.setTextViewText(R.id.tv_notiContent, song.getArtist()); // update the content

        CharSequence ticker = song.getTitle(); // 노티바 생성시 상태표시줄에 표시되는 글자
        mBuilder.setTicker(ticker);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());  // update the notification
    }

    /**
     * MainActivity 에 명령을 보내는 함수
     */
    private void sendToMainActivity(int position) {
        Intent intent = new Intent(SERVICE_RESULT);
        //if (position >= 0)
            intent.putExtra(SERVICE_MESSAGE, position);

        localBroadcastManager.sendBroadcast(intent);
    }

}
