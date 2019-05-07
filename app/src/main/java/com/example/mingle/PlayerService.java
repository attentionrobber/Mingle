package com.example.mingle;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

/**
 * 서비스로 실행되는 음악 플레이어 (노티바도 포함)
 * Music Player Service (contains Notifications)
 */
public class PlayerService extends Service implements PlayerInterface {

    // Media
    public static MediaPlayer mMediaPlayer = null;

    // Actions to control media
    public static final String ACTION_INIT = "ACTION_INIT";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_PREV = "ACTION_PREV";
    public static final String ACTION_NEXT = "ACTION_NEXT";


    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        init();

        handleAction(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 초기화
     */
    private void init() {

        if (mMediaPlayer != null)
            mMediaPlayer.release();

        String strUri = "content://media/external/audio/media/967";
        Uri mediaUri = Uri.parse(strUri);
        mMediaPlayer = MediaPlayer.create(this, mediaUri);
    }

    // 2. 명령어 실행.
    // Intent Action 에 넘어온 명령어를 분기시키는 함수
    private void handleAction(Intent intent) {
        if(intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();
        if (action.equalsIgnoreCase(ACTION_PLAY))
            play();
        else if (action.equalsIgnoreCase(ACTION_PAUSE))
            pause();
        else if (action.equalsIgnoreCase(ACTION_PREV))
            prev();
        else if (action.equalsIgnoreCase(ACTION_NEXT))
            next();
    }

    /**
     * NotificationBar 만드는 함수
     */
    private void createNotificationBar() {

        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("setTitle")
                .setContentText("setText");
        //builder.addAction();

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
        // TODO: add prev
    }

    @Override
    public void next() {
        // TODO: add next
    }
}
