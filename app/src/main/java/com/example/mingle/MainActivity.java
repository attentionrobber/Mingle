package com.example.mingle;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mingle.data.DBHelper;
import com.example.mingle.domain.Favorite;
import com.example.mingle.domain.Music;
import com.example.mingle.ui.main.FragmentListener;
import com.example.mingle.ui.main.TabPagerAdapter;
import com.example.mingle.ui.main.OnBackPressedListener;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FragmentListener {

    // Widget
    private ViewPager viewPager; // fragment 를 담는 view
    private TextView tv_title, tv_artist; // 하단
    private ImageView iv_albumArtMain;
    private ImageButton btn_playPause, btn_favorite, btn_shuffle;

    // Glide for Using Adapter
    public RequestManager glideRequestManger;

    // Interface to Control Music(old)
//    ServiceInterface serviceInterface;
    PlayerService playerService;

    // connect to the music service
    private MusicService musicService;
    private Intent srvIntent;
    private boolean musicBound = false;

    // 포인트: 서비스는 RecyclerView 에서 클릭을 해도
    // MainActivity 에서 cur_musics 를 접근하는것 보다 서비스가 나중에 실행되므로
    // 서비스에서 cur_musics 를 초기화 해줘도 cur_musics 는 초기화되지 않아 size 가 0 인 상태이다.

    // Interface to interaction adapter
    public static List<Music> playlist = new ArrayList<>(); // Current Music Playlist
    private int position = 0; // Current Music Playlist's Position
    private BroadcastReceiver broadcastReceiver; // Service 에서 넘어온 명령을 처리하는 리시버

    // Related DB(Favorites)
    public static List<Favorite> favorites = new ArrayList<>();
    DBHelper dbHelper;
    Dao<Favorite, Integer> favoriteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidget();
        init();

        setVolumeControlStream(AudioManager.STREAM_MUSIC); // Volume 조절시 Ringtone 이 아닌 Media Volume 이 조절되도록 설정

        // Service 에서 넘어온 Message 를 처리하는 Receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("MusicService_", "broadcastReceiver: " + position);
                position = intent.getIntExtra(PlayerService.SERVICE_MESSAGE, 0);

                setMusicInfo(position);
            }
        };
    }

    private void setWidget() {
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(tabPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        iv_albumArtMain = findViewById(R.id.iv_albumArtMain);
        iv_albumArtMain.setOnClickListener(this::btnClick);
        tv_artist = findViewById(R.id.tv_artist);
        tv_title = findViewById(R.id.tv_title);

        findViewById(R.id.layout_titleArtist).setOnClickListener(this::btnClick);
        btn_favorite = findViewById(R.id.btn_favorite);
        btn_favorite.setOnClickListener(this::btnClick);
        btn_playPause = findViewById(R.id.btn_playPause);
        btn_playPause.setOnClickListener(this::btnClick);
        findViewById(R.id.btn_prev).setOnClickListener(this::btnClick);
        findViewById(R.id.btn_next).setOnClickListener(this::btnClick);
        findViewById(R.id.btn_shuffle).setOnClickListener(this::btnClick);
    }

    private void init() {
        glideRequestManger = Glide.with(this); // Glide RequestManager
        initService(); // start and bind Service

        MediaLoader.loadMusic(this); // 전체 곡 로드 // TODO: Load 할때 전체 다 로드 말고 최근 플레이한(savedInstance 필요)플레이리스트 로드로 변경

        try {
            loadDB(); // DB에 저장된 Favorite 로드
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDB() throws SQLException {
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        favoriteDao = dbHelper.getFavoriteDao();

        favorites = favoriteDao.queryForAll();
    }

    private void addFavorite(Favorite favorite) throws SQLException {
        favoriteDao.create(favorite);
        favorites = favoriteDao.queryForAll();
        Log.i("Main_DB", "size: " + favorites.size());
    }

    private void deleteFavorite(String musicUri) throws SQLException {
        int i;
        for (i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getMusicUri().equals(musicUri))
                break;
        }
        favoriteDao.delete(favorites.get(i));
        favorites = favoriteDao.queryForAll();
        //Log.i("Main_DB", "size: "+favorites.size()+" | ?: "+a);
    }

    private boolean isFavorite(String musicUri) {
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getMusicUri().equals(musicUri))
                return true;
        }
        return false;
    }

    private void check() {
        for (int i = 0; i < favorites.size(); i++) {
            Log.i("Main_DBCheck", "" + favorites.size() + " | " + favorites.get(i).getMusicUri());
        }
    }

    private void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                if (position > 0) position = position - 1;
                setMusicInfo(position);
                musicService.prev();
                break;

            case R.id.btn_playPause:
                setMusicInfo(position);
                musicService.playPause();
                break;

            case R.id.btn_next:
                if (playlist.size() - 1 > position) position = position + 1;
                setMusicInfo(position);
                musicService.next();
                break;

            case R.id.btn_favorite:
                try {
                    if (isFavorite(playlist.get(position).getMusicUri())) { // 해당 곡이 Favorite 인 경우
                        btn_favorite.setImageResource(android.R.drawable.btn_star_big_off); // 버튼 아이콘 set OFF
                        deleteFavorite(playlist.get(position).getMusicUri()); // 해당 곡을 Favorite 에서 제거
                    } else {
                        addFavorite(new Favorite(playlist.get(position))); // 해당 곡을 Favorite 에 추가 후 DB에 저장
                        btn_favorite.setImageResource(android.R.drawable.btn_star_big_on); // 버튼 아이콘 set OFF
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_shuffle:
                check();
                break;

            case R.id.iv_albumArtMain:
            case R.id.layout_titleArtist:
                // TODO: 위 두개 레이아웃 터치시 PlayerActivity 로 가도록 설정하기
                break;

            default:
                break;
        }
    }

    /**
     * RecyclerView Item 클릭 했을 때 실행되는 리스너
     * Communicate MainActivity <-> PlaceholderFragment <-> FragmentTabAdapter
     * Fragment 에서 MainActivity 로 보내는 Listener
     */
    @Override
    public void onRecyclerViewItemClicked(List<Music> musics, int position) {
        playlist = musics;
        this.position = position;

        songPicked(); // for bindService
        //setService(); // for start Service(not bind)
        setMusicInfo(position);
    }

    /**
     * set playlist, position in PlayerService and start Service(not bind)
     */
    private void setService() {
        Intent intent = new Intent(this, PlayerService.class);
        intent.putExtra("tab", Constants.TAB.FAVORITE);
        startService(intent);
    }

    /**
     * Main 하단부 Now Playing Music 레이아웃 세팅
     */
    public void setMusicInfo(int position) {
        this.position = position;

        if (playlist.size() != 0) {
            new Handler().postDelayed(() -> {
                Log.i("MusicService_", "isPlaying: "+musicService.isPlaying);
                if (musicService.isPlaying())
                    btn_playPause.setImageResource(android.R.drawable.ic_media_pause);
                //else if (position == -1)
                    //btn_playPause.setImageResource(android.R.drawable.ic_media_play);
                else
                    btn_playPause.setImageResource(android.R.drawable.ic_media_play);
            }, 100);

//            Glide.with(this)
//                .load(Uri.parse(cur_musics.get(position).getAlbumImgUri()))
//                .placeholder(R.drawable.default_album_image)
//                .into(iv_albumArtMain);

            iv_albumArtMain.setImageURI(Uri.parse(playlist.get(position).getAlbumImgUri()));
            if (isFavorite(playlist.get(position).getMusicUri()))
                btn_favorite.setImageResource(android.R.drawable.btn_star_big_on);
            else
                btn_favorite.setImageResource(android.R.drawable.btn_star_big_off);
            tv_title.setText(playlist.get(position).getTitle());
            tv_artist.setText(playlist.get(position).getArtist());
        } else {
            btn_playPause.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    @Override
    public void onBackPressed() {
        // Fragment 에서도 onBackPressed()를 쓸 수 있도록 한다.
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
        String fragmentName = "";
        if (fragment instanceof OnBackPressedListener) {
            fragmentName = ((OnBackPressedListener) fragment).onBackPressed();
        }
//        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
//        for (Fragment fragment : fragmentList) {
//            if (fragment instanceof OnBackPressedListener) {
//                fragmentName = ((OnBackPressedListener) fragment).onBackPressed();
//                break;
//            }
//        }
        switch (fragmentName) {
            case Constants.TAB.FAVORITE:
                super.onBackPressed();
                break;
            case Constants.TAB.PLAYLIST:
                break;
            case Constants.TAB.SONG:
                super.onBackPressed();
                break;
            case Constants.TAB.ALBUM:
                break;
            case Constants.TAB.ARTIST:
                break;
            case Constants.TAB.FOLDER:
                super.onBackPressed();
                break;
            default:
                super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver), new IntentFilter(MusicService.SERVICE_RESULT));
        //LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver), new IntentFilter(PlayerService.SERVICE_RESULT));
    }

    @Override
    protected void onStop() {
        Log.i("MainActivity_", "onStop");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (playlist.size() > 0)
            setMusicInfo(musicService.getPosition());
    }

    @Override
    protected void onDestroy() {
        favorites.clear();
        stopService(srvIntent);
        unbindService(musicConnection);
        musicService = null;
        super.onDestroy();
    }


    /**
     * Related BindService
     */
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService(); // get service
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void initService() {
        if (srvIntent == null) {
            srvIntent = new Intent(this, MusicService.class);
            bindService(srvIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(srvIntent);
        }
    }

    public void songPicked() {
        //musicService.setSong(Integer.parseInt(view.getTag().toString()));
        musicService.setList(playlist);
        musicService.setSong(position);
        musicService.play();
    }


}
