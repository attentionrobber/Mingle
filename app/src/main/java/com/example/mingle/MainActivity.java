package com.example.mingle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mingle.ui.main.TabPagerAdapter;

public class MainActivity extends AppCompatActivity {

    // Widget
    TextView tv_title, tv_artist; // 하단

    MediaLoader mediaLoader;

    // Glide for Using Adapter
    public RequestManager glideRequestManger;

    // Interface to Control Music
    PlayerInterface playerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glideRequestManger = Glide.with(this);
        playerInterface = new PlayerService();

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        setWidget();
        init();

    }

    private void setWidget() {

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
        MediaLoader.load(this);


//        Uri uri = MediaLoader.musics.get(0).getMusic_uri();
//        String artist = MediaLoader.musics.get(0).getArtist();
//        String title = MediaLoader.musics.get(0).getTitle();
//        tv_title.setText("");tv_artist.setText("");
//        Log.i("TESTS", uri+""+artist+""+title);
    }

    private void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                break;
            case R.id.btn_play:
                break;
            case R.id.btn_next:
                break;
            case R.id.layout_player_bot:
                playerInterface.stop();
//                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
//                startActivity(intent);
                break;
            default: break;
        }
    }
}