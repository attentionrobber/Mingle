package com.example.mingle.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.example.mingle.Constants;
import com.example.mingle.MainActivity;
import com.example.mingle.MediaLoader;
import com.example.mingle.R;
import com.example.mingle.adapter.AdapterListener;
import com.example.mingle.adapter.FragmentTabAdapter;
import com.example.mingle.adapter.PlaylistAdapter;
import com.example.mingle.domain.Music;
import com.example.mingle.domain.Playlist;

import java.util.ArrayList;
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

        //gridView.setOnItemClickListener((parent, view, position, id) -> gridViewItemClicked()); // Replace AdapterListener
        fab.setOnClickListener(v -> createPlaylist());

        gridView.setAdapter(new PlaylistAdapter(context, MediaLoader.loadPlaylist(context), adapterListener));
    }

    private void createPlaylist() {
        // TODO: Dialog UI 변경, Add Auto display Keyboard
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("새로운 재생목록 추가");

        final EditText input = new EditText(context);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (text.equals(""))
                    MediaLoader.createPlaylist(context, text);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void playlistItemClicked(List<Music> musics) {
        gridView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new FragmentTabAdapter(context, musics, R.layout.item_frag_song, adapterListener));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
            //Log.i("PlaylistFragment", "onGridViewItemClicked "+position);

            List<Music> musics = MediaLoader.loadSongsInPlaylist(context, bucketID);
            playlistItemClicked(musics);
        }
    };

    @Override
    public String onBackPressed() {
        Log.i("PlaylistFragment", "onBackPressed()");
        if (recyclerView.getVisibility() == View.VISIBLE) { // list of song
            recyclerView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            return Constants.TAB.PLAYLIST;
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
        Log.i("PlaylistFragment", "onResume PlaylistTab");
    }

}
