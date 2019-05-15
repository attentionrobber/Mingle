package com.example.mingle.ui.main;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.TextView;

import com.example.mingle.MediaLoader;
import com.example.mingle.R;
import com.example.mingle.RecyclerViewAdapter;
import com.example.mingle.domain.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private List<Music> album = new ArrayList<>();
    private int resLayout = R.layout.item_frag_song;

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

            switch (index) {
                case 1: // Favorite
                    resLayout = R.layout.item_frag_favorite;
                    break;
                case 2: // Playlist
                    resLayout = R.layout.item_frag_playlist;
                    break;
                case 3: // Song
                    resLayout = R.layout.item_frag_song;
                    //MediaLoader.load(getContext());
                    break;
                case 4: // Album
                    resLayout = R.layout.item_frag_song;
                    MediaLoader.selectionByAlbum(getContext());
                    break;
                case 5: // Artist
                    resLayout = R.layout.item_frag_artist;
                    break;
                case 6: // Folder
                    resLayout = R.layout.item_frag_folder;
                    break;
            }
        }

        // TODO: Tab 마다 알맞은 데이터 로드
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // TODO: Tab 마다 알맞은 layout 설정
        Log.i("TESTS", "PlaceHolderFragment"+MediaLoader.musicsByAlbum.size());

        final RecyclerView rv_song = root.findViewById(R.id.rv_song);
        rv_song.setLayoutManager(new LinearLayoutManager(root.getContext()));
        rv_song.setAdapter(new RecyclerViewAdapter(getContext(), MediaLoader.musics, resLayout));

        return root;
    }
}