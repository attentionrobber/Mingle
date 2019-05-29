package com.example.mingle.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import com.example.mingle.MediaLoader;
import com.example.mingle.R;
import com.example.mingle.adapter.AdapterListener;
import com.example.mingle.adapter.AlbumAdapter;
import com.example.mingle.adapter.FragmentTabAdapter;
import com.example.mingle.adapter.PlaylistAdapter;
import com.example.mingle.domain.Album;
import com.example.mingle.domain.Music;
import com.example.mingle.domain.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Playlist Tab 을 표현하는 Fragment
 */
public class AlbumFragment extends Fragment implements OnBackPressedListener {

    private static final String ARG_KEY = "PlaylistKey";
    private Context context;

    // Widget(View)
    private RecyclerView rv_albumList, rv_inAlbum;

    private FragmentListener fragmentListener;

    public AlbumFragment() {
    }

    public static AlbumFragment newInstance(int index) {
//        PlaylistFragment fragment = new PlaylistFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_KEY, index);
//        fragment.setArguments(bundle);
        return new AlbumFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_album, container, false);

        setWidget(root);

        return root;
    }

    private void setWidget(View root) {
        rv_albumList = root.findViewById(R.id.rv_albumList);
        rv_inAlbum = root.findViewById(R.id.rv_inAlbum);

        //gridView.setOnItemClickListener((parent, view, position, id) -> gridViewItemClicked()); // Replace AdapterListener
        rv_albumList.setLayoutManager(new GridLayoutManager(context, 2));
        rv_albumList.setAdapter(new AlbumAdapter(context, MediaLoader.loadAlbum(context), adapterListener));
    }

    /**
     * Adapter 에서 Fragment 로 보내는 Listener
     */
    AdapterListener adapterListener = new AdapterListener() {
        @Override
        public void onRecyclerViewItemClicked(List<Music> musics, int position) {
            fragmentListener.onRecyclerViewItemClicked(musics, position);
        }

        @Override
        public void onBucketItemClicked(String bucketID) {
            // TODO: load musics in Album
            List<Music> musics = MediaLoader.loadSongsInAlbum(context, bucketID);

            rv_albumList.setVisibility(View.GONE);
            rv_inAlbum.setVisibility(View.VISIBLE);
            rv_inAlbum.setLayoutManager(new LinearLayoutManager(context));
            rv_inAlbum.setAdapter(new FragmentTabAdapter(context, musics, R.layout.item_frag_song, adapterListener));
        }
    };

    @Override
    public boolean onBackPressed() {
        Log.i("AlbumFragment", "onBackPressed()");

        if (rv_inAlbum.getVisibility() == View.VISIBLE) { // list of song
            rv_inAlbum.setVisibility(View.GONE);
            rv_albumList.setVisibility(View.VISIBLE);
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
        Log.i("AlbumFragment", "onResume AlbumTAB");
    }

}
