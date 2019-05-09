package com.example.mingle.ui.main;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
    private PageViewModel pageViewModel;

    private List<Music> album = new ArrayList<>();

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
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (index) {
                case 1: // Favorite
                    break;
                case 2: // Playlist
                    break;
                case 3: // Song
                    //MediaLoader.load(getContext());
                    break;
                case 4: // Album
                    MediaLoader.selectionByAlbum(getContext());
                    break;
                case 5: // Artist
                    break;
                case 6: // Folder
                    break;
            }
        }
        pageViewModel.setIndex(index);

        // TODO: Tab 마다 알맞은 데이터 로드
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        // TODO: Tab 마다 알맞은 layout 설정

        final RecyclerView rv_song = root.findViewById(R.id.rv_song);
        rv_song.setAdapter(new RecyclerViewAdapter(root.getContext(), MediaLoader.musicsByAlbum, R.string.tab_song));


        final TextView textView = root.findViewById(R.id.section_label);

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s+"ADDED");
            }
        });

        return root;
    }
}