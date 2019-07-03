package com.example.mingle.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


import com.example.mingle.Constants;
import com.example.mingle.MediaLoader;
import com.example.mingle.R;
import com.example.mingle.adapter.AdapterListener;
import com.example.mingle.adapter.ArtistAdapter;
import com.example.mingle.adapter.FavoriteAdapter;
import com.example.mingle.adapter.FragmentTabAdapter;
import com.example.mingle.domain.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Favorite, Song, Album, Artist, Folder Tab 을 표현하는 Fragment
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String TAB_NAME = "TAB_NAME";

    private int layout_item = R.layout.item_frag_song;
    private RecyclerView recyclerView;

    private Context context;

    private List<Music> musics = new ArrayList<>();


    private FragmentListener fragmentListener;

    public PlaceholderFragment() { }

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("PlaceHolderFragment","onCreate");
        context = getContext();
        // 최초 1회만 실행되므로 레이아웃 세팅만 하는게 좋다. 데이터 세팅은 onCreateView 에서
        int index;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (index) {
                case 1: // Favorite
                    TAB_NAME = Constants.TAB.FAVORITE;
                    layout_item = R.layout.item_frag_favorite;
                    break;
//                case 2: // Playlist (Replace PlaylistFragment)
//                    TAB_NAME = Constants.TAB.PLAYLIST;
//                    layout_item = R.layout.item_frag_playlist;
//                    break;
                case 3: // Song
                    TAB_NAME = Constants.TAB.SONG;
                    layout_item = R.layout.item_frag_song;
                    musics = MediaLoader.musics;
                    break;
//                case 4: // Album (Replace PlaylistFragment)
//                    TAB_NAME = Constants.TAB.ALBUM;
//                    layout_item = R.layout.item_frag_album;
//                    musics = MediaLoader.selectionByAlbum(context);
//                    break;
//                case 5: // Artist (Replace ArtistFragment)
//                    TAB_NAME = Constants.TAB.ARTIST;
//                    layout_item = R.layout.item_frag_artist;
//                    break;
                case 6: // Folder
                    TAB_NAME = Constants.TAB.FOLDER;
                    layout_item = R.layout.item_frag_folder;
                    break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        //Log.i("PlaceHolderFragment","onCreateView");

        // Tab 옮길 때 마다 View 가 호출되므로 Data 세팅을 여기서 하는게 좋음.
        recyclerView = root.findViewById(R.id.rv_song);
        setViewAdapterEachTab(); // Tab 마다 알맞은 layout 설정

        return root;
    }

    private void setViewAdapterEachTab() {
        switch (layout_item) {
            case R.layout.item_frag_favorite:
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new FavoriteAdapter(context, MediaLoader.loadFavorite(), adapterListener));
                break;
//            case R.layout.item_frag_playlist: // (Replace PlaylistFragment)
//                gridView.setAdapter(new PlaylistAdapter(context, MediaLoader.loadPlaylist(context), adapterListener));
//                break;
            case R.layout.item_frag_song:
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new FragmentTabAdapter(context, musics, layout_item, adapterListener));
                break;
//            case R.layout.item_frag_album: // (Replace AlbumFragment)
//                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
//                recyclerView.setAdapter(new AlbumAdapter(context, musics));
//                break;
//            case R.layout.item_frag_artist: // (Replace ArtistFragment)
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                recyclerView.setAdapter(new ArtistAdapter(context, musics));
//                break;
            case R.layout.item_frag_folder:
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new FragmentTabAdapter(context, musics, layout_item, adapterListener));
                break;
            default:
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new FragmentTabAdapter(context, musics, layout_item, adapterListener));
                break;
        }
    }

    /**
     * Adapter 에서 Fragment 로 보내는 Listener
     */
    AdapterListener adapterListener = new AdapterListener() {
        @Override
        public void onRecyclerViewItemClicked(List<Music> musics, int position, boolean isShuffle) {
            fragmentListener.onRecyclerViewItemClicked(musics, position, isShuffle);
        }

        @Override
        public void onBucketItemClicked(String bucketID) {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fragmentListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.i("PlaceHolderFragment","onResume, "+TAB_NAME);
    }
}