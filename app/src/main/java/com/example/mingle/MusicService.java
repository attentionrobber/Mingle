package com.example.mingle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
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

    // Actions to control media
    public static final String ACTION_INIT = "ACTION_INIT";
    public static final String ACTION_MAIN = "ACTION_MAIN";
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


    public MusicService() {

    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void setList(List<Music> musics) {
        playlist = musics;
    }

    public void setSong(int songIndex) {
        position = songIndex;
    }

    public boolean isPlaying() {
        if (player != null)
            return player.isPlaying();
        return false;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MusicService_" ,"onCreate");
        context = getApplicationContext();
        position = 0;
        player = new MediaPlayer();

        initMediaPlayer();
    }

    private void initMediaPlayer() {
        //player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MusicService_" ,"onStartCommand");
        if (intent != null && intent.getAction() != null)
            handleAction(intent);

        return super.onStartCommand(intent, flags, startId);
    }
    // Intent Action 에 넘어온 명령어를 분기시키는 함수
    private void handleAction(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();
        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            play();
            //sendToMainActivity(position);
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            pause();
            //sendToMainActivity(position);
        }
        else if (action.equalsIgnoreCase(ACTION_PLAYPAUSE)) {
            playPause();
            //sendToMainActivity(position);
        }
        else if (action.equalsIgnoreCase(ACTION_PREV)) {
            prev();
            //sendToMainActivity(position);
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            next();
            //sendToMainActivity(position);
        }
        else if (action.equalsIgnoreCase(ACTION_STOP)) {
            stop();
        }
    }

    public void play() {
        Log.i("MusicService_", "play()");
//        if (player != null)
//            player.release();
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
        updateNotification(song);
    }

    public void playPause() {
        Log.i("MusicService_", "playPause()");
        if (player.isPlaying())
            player.pause();
        else player.start();
        updateNotification(song);
    }

    public void prev() {
        Log.i("MusicService_", "prev()");
        if (position > 0) position = position - 1;
        play();
    }

    public void next() {
        Log.i("MusicService_", "next()");
        if (playlist.size()-1 > position) position = position + 1;
        play();
    }

    public void stop() {
        Log.i("MusicService_", "stop()");
        if (player != null) {
            player.stop();
            stopForeground(true);
            stopSelf(); // Stop Service
            Log.i("MusicService_", "stop if()");
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
        // TODO 위에있는 노티바함수 이걸로 바꾸기
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

    // update the Notification's UI
    private void updateNotification(Music song){
        // 현재 음악 재생중이면
        if (player.isPlaying())
            mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_pause); // 노티바 버튼 변경
        else if (!player.isPlaying())
            mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_play); // 노티바 버튼 변경

        // 앨범아트가 있는지 없는지 검사한다. 없으면 null 반환
        Uri albumArtUri = existAlbumArt(getBaseContext(), song.getAlbumImgUri());
        if (albumArtUri != null)
            mRemoteViews.setImageViewUri(R.id.iv_noti, albumArtUri); // set Album Artwork
        else
            mRemoteViews.setImageViewResource(R.id.iv_noti, R.drawable.default_album_image); // set default image

        mRemoteViews.setTextViewText(R.id.tv_notiTitle, song.getTitle()); /// update the title
        mRemoteViews.setTextViewText(R.id.tv_notiContent, song.getArtist()); // update the content

        CharSequence ticker = song.getTitle(); // 노티바 생성시 상태표시줄에 표시되는 글자
        mBuilder.setTicker(ticker);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());  // update the notification
    }
    /**
     * check if AlbumArt is exist
     * 앨범아트가 존재하는지 검사하는 함수.
     */
    private Uri existAlbumArt(Context context, String strUri) {
        Uri mUri = Uri.parse(strUri);
        Drawable d = null;
        if (mUri != null) {
            if ("content".equals(mUri.getScheme())) {
                try {
                    d = Drawable.createFromStream(context.getContentResolver().openInputStream(mUri), null);
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

}
