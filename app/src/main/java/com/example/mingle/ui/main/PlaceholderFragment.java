package com.example.mingle.ui.main;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.mingle.adapter.FragmentTabAdapter;
import com.example.mingle.domain.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private List<Music> musics = new ArrayList<>();
    private int resLayout = R.layout.item_frag_song;


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
                    resLayout = R.layout.item_frag_favorite;
                    break;
                case 2: // Playlist
                    resLayout = R.layout.item_frag_playlist;
                    break;
                case 3: // Song
                    resLayout = R.layout.item_frag_song;
                    musics = MediaLoader.musics;
                    //Log.i("TESTS", "song: "+musics.size());
                    break;
                case 4: // Album
                    resLayout = R.layout.item_frag_album;
                    musics = MediaLoader.selectionByAlbum();
                    //Log.i("TESTS", "album: "+musics.size());
                    break;
                case 5: // Artist
                    resLayout = R.layout.item_frag_artist;
                    break;
                case 6: // Folder
                    resLayout = R.layout.item_frag_folder;
                    break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // Tab 마다 알맞은 layout 설정
        final RecyclerView rv_song = root.findViewById(R.id.rv_song);

        FragmentTabAdapter.AdapterListener adapterListener = new FragmentTabAdapter.AdapterListener() {
            @Override
            public void onRecyclerViewItemClicked(List<Music> musics, int position) {
                mListener.onRecyclerViewItemClicked(musics, position);
            }
        };

        switch (resLayout) {
            case R.layout.item_frag_favorite:
                rv_song.setLayoutManager(new LinearLayoutManager(root.getContext()));
                rv_song.setAdapter(new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            case R.layout.item_frag_playlist:
                rv_song.setLayoutManager(new LinearLayoutManager(root.getContext()));
                rv_song.setAdapter(new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            case R.layout.item_frag_song:
                rv_song.setLayoutManager(new LinearLayoutManager(root.getContext()));
                rv_song.setAdapter(new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            case R.layout.item_frag_album:
                rv_song.setLayoutManager(new GridLayoutManager(root.getContext(), 2));
                rv_song.setAdapter(new AlbumAdapter(getContext(), musics));
                break;
            case R.layout.item_frag_artist:
                rv_song.setLayoutManager(new LinearLayoutManager(root.getContext()));
                rv_song.setAdapter(new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            case R.layout.item_frag_folder:
                rv_song.setLayoutManager(new LinearLayoutManager(root.getContext()));
                rv_song.setAdapter(new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
            default:
                rv_song.setLayoutManager(new LinearLayoutManager(root.getContext()));
                rv_song.setAdapter(new FragmentTabAdapter(getContext(), musics, resLayout, adapterListener));
                break;
        }

        return root;
    }


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

}