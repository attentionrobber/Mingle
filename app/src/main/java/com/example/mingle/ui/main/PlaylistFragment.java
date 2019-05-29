package com.example.mingle.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mingle.MainActivity;
import com.example.mingle.MediaLoader;
import com.example.mingle.R;
import com.example.mingle.adapter.AdapterListener;
import com.example.mingle.adapter.FragmentTabAdapter;
import com.example.mingle.adapter.PlaylistAdapter;
import com.example.mingle.domain.Music;

import java.util.List;

/**
 * Playlist Tab 을 표현하는 Fragment
 */
public class PlaylistFragment extends Fragment implements OnBackPressedListener {

    private static final String ARG_KEY = "PlaylistKey";
    private Context context;

    // Widget(View)
    private GridView gridView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private FragmentListener fragmentListener;

    public PlaylistFragment() {
    }

    public static PlaylistFragment newInstance(int index) {
//        PlaylistFragment fragment = new PlaylistFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_KEY, index);
//        fragment.setArguments(bundle);
        return new PlaylistFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_playlist, container, false);

        setWidget(root);

        return root;
    }

    private void setWidget(View root) {
        gridView = root.findViewById(R.id.gv_playlist);
        recyclerView = root.findViewById(R.id.rv_playlist);
        fab = root.findViewById(R.id.fab_playlist);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gridViewItemClicked();
            }
        });
        fab.setOnClickListener(v -> createPlaylist());

        gridView.setAdapter(new PlaylistAdapter(context, MediaLoader.loadPlaylist(context), adapterListener));
    }

    private void gridViewItemClicked() {
        // TODO: MediaLoader.loadPlaylistMusic and RecyclerView 세팅

        //MediaLoader.loadPlaylistMusic(context, position);
        gridView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new FragmentTabAdapter(context, MediaLoader.loadPlaylistMusic(context, "124431"), R.layout.item_frag_song, adapterListener));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void createPlaylist() {
        // TODO: Dialog UI 추가
        MediaLoader.createPlaylist(context, "test");

    }

    /**
     * Adapter 에서 Fragment 로 보내는 Listener
     */
    AdapterListener adapterListener = new AdapterListener() {
        @Override
        public void onRecyclerViewItemClicked(List<Music> musics, int position) {
            fragmentListener.onRecyclerViewItemClicked(musics, position);
        }
    };

    @Override
    public boolean onBackPressed() {
        Log.i("PlaylistFragment", "onBackPressed()");

        if (recyclerView.getVisibility() == View.VISIBLE) { // list of song
            recyclerView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

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
        Log.i("PlaylistFragment", "onResume PlaylistTab");
    }

}
