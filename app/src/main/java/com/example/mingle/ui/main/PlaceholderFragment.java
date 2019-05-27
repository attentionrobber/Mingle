package com.example.mingle.ui.main;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mingle.MediaLoader;
import com.example.mingle.R;
import com.example.mingle.SongFragment;
import com.example.mingle.adapter.AlbumAdapter;
import com.example.mingle.adapter.ArtistAdapter;
import com.example.mingle.adapter.FragmentTabAdapter;
import com.example.mingle.domain.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String TAB_NAME = "";

    private List<Music> musics = new ArrayList<>();

    private int resLayout = R.layout.item_frag_song;
    private RecyclerView recyclerView;
    private FragmentTabAdapter tabAdapter;



    private FragmentListener mListener;

    public interface FragmentListener {
        void onRecyclerViewItemClicked(List<Music> musics, int position);
    }

    public PlaceholderFragment() {
    }

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

        int index;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            // TODO: Tab 마다 알맞은 데이터 로드
            switch (index) {
                case 1: // Favorite
                    TAB_NAME = "Favorite";
                    resLayout = R.layout.item_frag_favorite;
                    musics = MediaLoader.loadFavorite();
                    break;
                case 2: // Playlist
                    TAB_NAME = "Playlist";
                    resLayout = R.layout.item_frag_playlist;
                    break;
                case 3: // Song
                    TAB_NAME = "Song";
                    resLayout = R.layout.item_frag_song;
                    musics = MediaLoader.musics;
                    //Log.i("TESTS", "song: "+musics.size());
                    break;
                case 4: // Album
                    TAB_NAME = "Album";
                    resLayout = R.layout.item_frag_album;
                    musics = MediaLoader.selectionByAlbum(getContext());
                    //Log.i("TESTS", "album: "+musics.size());
                    break;
                case 5: // Artist
                    TAB_NAME = "Artist";
                    resLayout = R.layout.item_frag_artist;
                    //musics = MediaLoader.selectionByArtist(getContext());
                    musics = MediaLoader.musics;
                    break;
                case 6: // Folder
                    TAB_NAME = "Folder";
                    resLayout = R.layout.item_frag_folder;
                    break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = root.findViewById(R.id.rv_song);
        setViewAdapterEachTab(); // Tab 마다 알맞은 layout 설정

        return root;
    }

    private void setViewAdapterEachTab() {
        switch (resLayout) {
            case R.layout.item_frag_favorite:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(tabAdapter = new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            case R.layout.item_frag_playlist:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(tabAdapter = new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            case R.layout.item_frag_song:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(tabAdapter = new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            case R.layout.item_frag_album:
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(new AlbumAdapter(getContext(), musics));
                break;
            case R.layout.item_frag_artist:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(new ArtistAdapter(getContext(), musics));
                break;
            case R.layout.item_frag_folder:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(tabAdapter = new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            default:
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(tabAdapter = new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
        }
    }

    /**
     * Adapter 에서 Fragment 로 보내는 Listener
     */
    FragmentTabAdapter.AdapterListener adapterListener = new FragmentTabAdapter.AdapterListener() {
        @Override
        public void onRecyclerViewItemClicked(List<Music> musics, int position) {
            mListener.onRecyclerViewItemClicked(musics, position);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlaceholderFragment.FragmentListener) {
            mListener = (PlaceholderFragment.FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("PlaceHolderFragment",""+TAB_NAME);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        //tabAdapter.notifyDataSetChanged();
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }
}