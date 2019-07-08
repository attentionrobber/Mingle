package com.example.mingle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mingle.domain.Music;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private final String TAG = "PlayerActivity_";

    // Widget
    private ImageView iv_albumArt;
    private SeekBar seekBar;
    private TextView tv_title, tv_artist, tv_current, tv_duration;

    // Player
    private List<Music> playlist = new ArrayList<>();
    private Music song = null;
    private int position = 0;
    private boolean isShuffle = false;

//    public PlayerActivity(List<Music> playlist, int position, boolean isShuffle) {
//        this.playlist = playlist;
//        this.position = position;
//        this.isShuffle = isShuffle;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Log.i(TAG, "onCreate: ");
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            playlist = (List<Music>) bundle.getSerializable("playlist");
            position = bundle.getInt("position");
            song = playlist.get(position);
        }

        //Log.i(TAG, "onCreate: "+playlist.get(position).getTitle());

        setWidget();
        init();
    }

    private void setWidget() {
        Log.i(TAG, "setWidget: ");
        iv_albumArt = findViewById(R.id.iv_albumArt);
        seekBar = findViewById(R.id.seekBar);
        //seekBar.setMax();
        tv_title = findViewById(R.id.tv_title);
        tv_artist = findViewById(R.id.tv_artist);
        tv_current = findViewById(R.id.tv_current);
        tv_duration = findViewById(R.id.tv_duration);
        findViewById(R.id.btn_prev).setOnClickListener(this::btnClick);
        findViewById(R.id.btn_play).setOnClickListener(this::btnClick);
        findViewById(R.id.btn_next).setOnClickListener(this::btnClick);
    }

    private void init() {
        Log.i(TAG, "init: ");
        iv_albumArt.setImageURI(Uri.parse(song.getAlbumImgUri()));
        tv_title.setText(song.getTitle());
        tv_artist.setText(song.getArtist());
        String duration = Long.toString(song.getDuration());
        tv_duration.setText(duration);
        Log.i(TAG, "init: "+duration+" | "+song.getDuration());
    }

    private void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                break;
            case R.id.btn_play:
                break;
            case R.id.btn_next:
                break;
            default:
                break;
        }
    }
}
