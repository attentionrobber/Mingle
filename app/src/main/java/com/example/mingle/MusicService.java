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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MusicService extends Service {

    private String TAG = "MusicService_";

    private Context context;
    private final IBinder musicBind = new MusicBinder();

    // Control Music
    private MediaPlayer player; // media player
    private List<Music> playlist; // song list
    private List<Music> shuffledList; // shuffled playlist
    private Music song; // playlist.get(position)
    private int position; // current position
    public boolean isPlaying = false;
    private boolean isShuffle = false;

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
    private final int STOPPED_SERVICE = -1; // using sendToMainActivity


    public MusicService() { }

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
        Log.i(TAG, "onUnbind");
        // activity onDestroy 될 때 unBind 되는게 정상인지 확인해보기.
        //player.stop();
        //player.reset();
        return false;
    }

    public void setList(List<Music> musics) {
        playlist = musics;
        shuffledList = new ArrayList<>(playlist);
        Collections.shuffle(shuffledList, new Random(System.nanoTime()));
    }

    public void setSong(int songIndex) {
        position = songIndex;
    }

    public Music getSong() {
        return song;
    }

    public void setShuffle(boolean isShuffle) {
        this.isShuffle = isShuffle;
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
        Log.i(TAG ,"onCreate");
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
            next();
            sendToMainActivity(song);
        });
        player.setOnErrorListener((mp, what, extra) -> false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i(TAG ,"onStartCommand");
        if (intent != null && intent.getAction() != null)
            handleAction(intent);

        return super.onStartCommand(intent, flags, startId);
        //return START_STICKY;
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
        } else if (action.equalsIgnoreCase(ACTION_PREV)) {
            prev();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            next();
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            stop();
            sendToMainActivity(song);
        }
    }

    public void play() {
        Log.i(TAG, "play() size: "+playlist.size() + " | pos: " + position);
        if (player == null)
            initMediaPlayer();

        player.reset();

        if (playlist.size() != 0) {
            song = playlist.get(position); // get song
            Uri songUri = Uri.parse(song.getMusicUri()); // get Music URI

            try {
                player.setDataSource(context, songUri);
            } catch (Exception e) {
                Log.e(TAG, "Error setting data source", e);
            }
            player.prepareAsync();

            if (mNotificationManager == null)
                setUpNotification(song);
            else updateNotification(song);
        }
    }

    public void pause() {
        Log.i(TAG, "pause()");
        player.pause();
        isPlaying = false;
        updateNotification(song);
    }

    public void playPause() {
        Log.i(TAG, "playPause()");
        if (player != null) {
            if (isPlaying) {
                player.pause();
                isPlaying = false;
                mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_play); // 노티바 버튼 변경
            } else {
                player.start();
                isPlaying = true;
                mRemoteViews.setImageViewResource(R.id.btn_notiPlayPause, android.R.drawable.ic_media_pause); // 노티바 버튼 변경
            }
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());  // update the notification
        } else
            play();

        sendToMainActivity(song);
    }

    public void prev() {
        if (player != null && playlist.size() != 0) {
            Log.i(TAG, "prev() size: " + playlist.size() + " | pos: " + position);
            if (position > 0) {
                if (!isShuffle) {
                    position = position - 1; // 셔플이 아닌 경우 이전곡 재생
                    song = playlist.get(position); // get song
                } else {
                    position = position - 1;
                    song = shuffledList.get(position);
                }
            }

            String path = song.getPath();
            try {
                player.reset();
                player.setDataSource(path);
                player.prepareAsync();
                isPlaying = true;
                updateNotification(song);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendToMainActivity(song); // 메인액티비티에도 곡 변경사항을 알려준다.
        }
    }

    public void next() {
        if (player != null && playlist.size() != 0) {
            Log.i(TAG, "next() size: " + playlist.size() + " | pos: " + position);
            if (playlist.size() - 1 > position) {
                if (!isShuffle) {
                    position = position + 1; // 셔플이 아닌 경우 다음곡 재생
                    song = playlist.get(position); // get song
                } else { // 셔플인 경우 랜덤 재생
                    //position = new Random().nextInt(playlist.size());
                    position = position + 1; // 셔플이 아닌 경우 다음곡 재생
                    song = shuffledList.get(position); // get song
                }
            }

            String path = song.getPath();
            try {
                player.reset();
                player.setDataSource(path);
                player.prepareAsync();
                isPlaying = true;
                updateNotification(song);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendToMainActivity(song); // 메인액티비티에도 곡 변경사항을 알려준다.
        }
    }

    public void stop() {
        Log.i(TAG, "stop()");
        if (player!= null) {
            player.stop();
            player = null;
            isPlaying = false;
            if (mNotificationManager != null)
                mNotificationManager.cancelAll();
            stopForeground(true);
            stopSelf(); // Stop Service
        }
    }

//    @Override
//    public void onDestroy() {
//        Log.i(TAG, "onDestroy");
//        stop();
//    }

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
    private void sendToMainActivity(Music song) {
        Intent intent = new Intent(SERVICE_RESULT);
        //if (position >= 0)
        intent.putExtra(SERVICE_MESSAGE, song);

        localBroadcastManager.sendBroadcast(intent);
    }

}
