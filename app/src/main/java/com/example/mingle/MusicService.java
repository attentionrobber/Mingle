package com.example.mingle;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.example.mingle.domain.Music;

import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer player; // media player
    private List<Music> songs; // song list
    private int position; // current position

    private final IBinder musicBind = new MusicBinder();

    public MusicService() {
    }

    public void setList(List<Music> theSongs){
        songs = theSongs;
    }

    public void setSong(int songIndex){
        position = songIndex;
    }

    private void initMusicPlayer() {
        //player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        position = 0;
        player = new MediaPlayer();

        initMusicPlayer();
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


    public void playSong(){
        //play a song
        player.reset();
        //get song
        Music playSong = songs.get(position);
        //get id
        Uri trackUri = Uri.parse(playSong.getMusicUri());

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}
