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
import android.widget.Toast;

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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FragmentListener {

    // Widget
    private ViewPager viewPager; // fragment 를 담는 view
    private TextView tv_title, tv_artist; // 하단
    private ImageView iv_albumArtMain;
    private ImageButton btn_playPause, btn_favorite;

    private List<Music> playlist = new ArrayList<>(); // Current Music Playlist
    private Music song = null;
    private int position = 0; // Current Music Playlist's Position
    private boolean isShuffle = false;

    // Glide for Using Adapter
    public RequestManager glideRequestManger;

    // Interface to Control Music(old)
//    ServiceInterface serviceInterface;
    //PlayerService playerService;

    // connect to the music service
    private MusicService musicService;
    private Intent srvIntent;
    private boolean musicBound = false;

    // RecyclerView 에서 클릭을 해도 MainActivity 에서 cur_music 를 접근하는것 보다 서비스가 나중에 실행되므로
    // 서비스에서 cur_music 을 초기화 해줘도 cur_music 은 초기화되지 않아 size 가 0 인 상태이다.

    // Interface to interaction adapter
    private BroadcastReceiver broadcastReceiver; // Service 에서 넘어온 명령을 처리하는 리시버

    // Related DB(Favorites)
    public static List<Favorite> favorites = new ArrayList<>();
    DBHelper dbHelper;
    Dao<Favorite, Integer> favoriteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 반복문 안에서 객체 생성한거 다 바꾸기. .set()함수로

        setWidget();
        init();

        setVolumeControlStream(AudioManager.STREAM_MUSIC); // Volume 조절시 Ringtone 이 아닌 Media Volume 이 조절되도록 설정

        // Service 에서 넘어온 Message 를 처리하는 Receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                song = (Music) intent.getSerializableExtra(PlayerService.SERVICE_MESSAGE);
                position = intent.getIntExtra(PlayerService.SERVICE_MESSAGE, 0);
                setMusicInfo(song);
                //Log.i("MusicService_", "onReceive: "+song.getTitle()+" | "+position);
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
        btn_playPause = findViewById(R.id.btn_playPause);
        btn_favorite.setOnClickListener(this::btnClick);
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
    public void setMusicInfo(Music song) {
        if (song != null) {
            new Handler().postDelayed(() -> {
                if (musicService.isPlaying())
                    btn_playPause.setImageResource(R.drawable.pause);
                else
                    btn_playPause.setImageResource(R.drawable.play);
            }, 100);

//        Glide.with(this)
//            .load(Uri.parse(cur_musics.get(position).getAlbumImgUri()))
//            .placeholder(R.drawable.default_album_image)
//            .into(iv_albumArtMain);

            iv_albumArtMain.setImageURI(Uri.parse(song.getAlbumImgUri()));
            if (isFavorite(song.getMusicUri()))
                btn_favorite.setImageResource(R.drawable.favorite_on);
            else
                btn_favorite.setImageResource(R.drawable.favorite_off);
            tv_title.setText(song.getTitle());
            tv_artist.setText(song.getArtist());
        }
    }

    private void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_prev:
                musicService.prev();
                break;

            case R.id.btn_playPause:
                musicService.playPause();
                break;

            case R.id.btn_next:
                musicService.next();
                break;

            case R.id.btn_favorite:
                try {
                    if (isFavorite(song.getMusicUri())) { // 해당 곡이 Favorite 인 경우
                        btn_favorite.setImageResource(R.drawable.favorite_off); // 버튼 아이콘 set OFF
                        deleteFavorite(song.getMusicUri()); // 해당 곡을 Favorite 에서 제거
                    } else {
                        addFavorite(new Favorite(song)); // 해당 곡을 Favorite 에 추가 후 DB에 저장
                        btn_favorite.setImageResource(R.drawable.favorite_on); // 버튼 아이콘 set OFF
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_shuffle:
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(musicService, "순차 재생", Toast.LENGTH_SHORT).show();
                } else {
                    isShuffle = true;
                    Toast.makeText(musicService, "랜덤 재생", Toast.LENGTH_SHORT).show();
                }
                musicService.setShuffle(isShuffle);
                break;

            case R.id.iv_albumArtMain:
            case R.id.layout_titleArtist:
                Intent intent = new Intent(this, PlayerActivity.class);
                // TODO: 곡탭에서는 PlayerActivity 가지지 않음. putExtra 대신 다른 방법 사용하자.
                Bundle bundle = new Bundle();
                bundle.putSerializable("playlist", (Serializable) playlist);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
                //PlayerActivity playerActivity = new PlayerActivity(playlist, position, isShuffle);

                break;
            default:
                break;
        }
    }

    /**
     * RecyclerView Item 클릭 했을 때 실행되는 리스너
     * FragmentTabAdapter -> PlaceholderFragment -> MainActivity
     * Fragment 에서 MainActivity 로 보내는 Listener
     */
    @Override
    public void onRecyclerViewItemClicked(List<Music> musics, int position, boolean isShuffle) {
        playlist = musics;
        this.position = position;
        if (isShuffle) this.isShuffle = true;
        song = musics.get(position);

        //Toast.makeText(this, "clicked"+song.getDuration(), Toast.LENGTH_SHORT).show();

        //Log.i("MusicService_", "onRecyclerViewItemClicked: "+playlist.size());

        //setService(); // for start Service(not bind)
        songPicked(); // for bindService
        setMusicInfo(song);
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
        Log.i("MusicService_", "onStart");
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
        Log.i("MusicService_", "onRestart");
        setMusicInfo(musicService.getSong());
    }

    @Override
    protected void onDestroy() {
        Log.i("MusicService_", "Main_onDestroy");
//        favorites.clear();
        //stopService(srvIntent);
        //musicService = null;
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
            if (musicService != null) {
                setMusicInfo(musicService.getSong());
            }
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void initService() {
        if (srvIntent == null) {
            Log.i("MusicService_", "initService()");
            srvIntent = new Intent(this, MusicService.class);
            bindService(srvIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(srvIntent);
        }
    }

    public void songPicked() {
        musicService.setList(playlist);
        musicService.setSong(position);
        musicService.setShuffle(isShuffle);
        musicService.play();
    }

}
