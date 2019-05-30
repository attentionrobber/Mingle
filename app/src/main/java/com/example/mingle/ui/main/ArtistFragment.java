package com.example.mingle.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mingle.Constants;
import com.example.mingle.MediaLoader;
import com.example.mingle.R;
import com.example.mingle.adapter.AdapterListener;
import com.example.mingle.adapter.ArtistAdapter;
import com.example.mingle.adapter.FragmentTabAdapter;
import com.example.mingle.domain.Music;

import java.util.List;

/**
 * Playlist Tab 을 표현하는 Fragment
 */
public class ArtistFragment extends Fragment implements OnBackPressedListener {

    private static final String ARG_KEY = "ArtistKey";
    private Context context;

    // Widget(View)
    private RecyclerView rv_artistList, rv_inArtist;

    private FragmentListener fragmentListener;

    public ArtistFragment() {
    }

    public static ArtistFragment newInstance(int index) {
//        PlaylistFragment fragment = new PlaylistFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_KEY, index);
//        fragment.setArguments(bundle);
        return new ArtistFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_artist, container, false);

        setWidget(root);

        return root;
    }

    private void setWidget(View root) {
        rv_artistList = root.findViewById(R.id.rv_artistList);
        rv_inArtist = root.findViewById(R.id.rv_inArtist);

        //gridView.setOnItemClickListener((parent, view, position, id) -> gridViewItemClicked()); // Replace AdapterListener
        rv_artistList.setLayoutManager(new LinearLayoutManager(context));
        rv_artistList.setAdapter(new ArtistAdapter(context, MediaLoader.loadArtists(context), adapterListener));
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
            List<Music> musics = MediaLoader.loadSongsInArtist(context, bucketID);

            rv_artistList.setVisibility(View.GONE);
            rv_inArtist.setVisibility(View.VISIBLE);
            rv_inArtist.setLayoutManager(new LinearLayoutManager(context));
            rv_inArtist.setAdapter(new FragmentTabAdapter(context, musics, R.layout.item_frag_song, adapterListener));
        }
    };

    @Override
    public String onBackPressed() {
        Log.i("ArtistFragment", "onBackPressed()");
        if (rv_inArtist.getVisibility() == View.VISIBLE) { // list of song
            rv_inArtist.setVisibility(View.GONE);
            rv_artistList.setVisibility(View.VISIBLE);
            return Constants.TAB.ARTIST;
        }
        return "";
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
        Log.i("ArtistFragment", "onResume ArtistTAB");
    }

}
