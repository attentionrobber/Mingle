package com.example.mingle;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mingle.domain.Music;
import com.example.mingle.ui.main.PlaceholderFragment;
import com.example.mingle.ui.main.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PlaceholderFragment.FragmentListener {

    // Widget
    private TextView tv_title, tv_artist; // 하단
    private ImageView iv_albumArtMain;

    MediaLoader mediaLoader;

    // Glide for Using Adapter
    public RequestManager glideRequestManger;

    // Interface to Control Music
    ServiceInterface serviceInterface;

    // 중요 포인트: 서비스는 RecyclerView 에서 클릭을 해도
    // MainActivity 에서 cur_musics 를 접근하는것 보다 서비스가 나중에 실행되므로
    // 서비스에서 cur_musics 를 초기화 해줘도 cur_musics 는 초기화되지 않아 size 가 0 인 상태이다.

    // Interface to interaction adapter
    public static List<Music> cur_musics = new ArrayList<>();
    private int position = 0;

    // Service 에서 넘어온 명령을 처리하는 리시버
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glideRequestManger = Glide.with(this);
        serviceInterface = new PlayerService();

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        /**
         * Service 에서 넘어온 명령을 처리하는 Receiver
         */
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = intent.getIntExtra(PlayerService.SERVICE_MESSAGE, 0);
                // do something here.
                Log.i("Service MainBroadCast",""+position);
                setMusicInfo(position);
            }
        };




        setWidget();
        init();

    }

    private void setWidget() {

        iv_albumArtMain = findViewById(R.id.iv_albumArtMain);
        tv_artist = findViewById(R.id.tv_artist);
        tv_title = findViewById(R.id.tv_title);

        findViewById(R.id.layout_player_bot).setOnClickListener(this::btnClick);
        findViewById(R.id.btn_prev).setOnClickListener(this::btnClick);
        findViewById(R.id.btn_play).setOnClickListener(this::btnClick);
        findViewById(R.id.btn_next).setOnClickListener(this::btnClick);
    }

    private void init() {

        // TODO: Load 할때 전체 다 로드 말고
        //  최근 플레이한(savedInstance 필요)플레이리스트 로드로 변경
        //mediaLoader = new MediaLoader(this);
        MediaLoader.loadSong(this);


    }

    private void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                serviceInterface.prev();
                setMusicInfo(position--);
                break;
            case R.id.btn_play:
                break;
            case R.id.btn_next:
                serviceInterface.next();
                setMusicInfo(position++);
                break;
            case R.id.layout_player_bot:
//                serviceInterface.stop();
                //Log.i("Main_Activity", ""+cur_music.getTitle());
//
//                if (PlayerService.mMediaPlayer != null) {
//                    if (PlayerService.mMediaPlayer.isPlaying())
//                        PlayerService.mMediaPlayer.pause();
//                    else PlayerService.mMediaPlayer.start();
//                }
//                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
//                startActivity(intent);
                break;
            default: break;
        }
    }

    private void prev() {

    }

    private void play() {

    }

    private void next() {

    }

    /**
     * Communicate MainActivity <-> PlaceholderFragment <-> FragmentTabAdapter
     */
    @Override
    public void onRecyclerViewItemClicked(List<Music> musics, int position) {
        cur_musics = musics;
        Log.i("Main RecyclerClicked", ""+cur_musics.size()+" | "+musics.size());
        Log.i("Main RecyclerClicked", ""+position);
        this.position = position;
        setMusicInfo(position);
    }

    public void setMusicInfo(int position) {
        Log.i("Main setMusicInfo", ""+cur_musics.size());
        this.position = position;
        iv_albumArtMain.setImageURI(Uri.parse(cur_musics.get(position).getAlbumImgUri()));
        tv_title.setText(cur_musics.get(position).getTitle());
        tv_artist.setText(cur_musics.get(position).getArtist());
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter(PlayerService.SERVICE_RESULT));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}
