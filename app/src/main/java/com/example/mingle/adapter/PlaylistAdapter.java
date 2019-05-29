package com.example.mingle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mingle.R;
import com.example.mingle.domain.Playlist;

import java.util.List;

public class PlaylistAdapter extends BaseAdapter {

    private Context context;
    private List<Playlist> playLists;
    private AdapterListener adapterListener;

    private LayoutInflater inflater;

    public PlaylistAdapter(Context context, List<Playlist> playLists, AdapterListener listener) {
        this.context = context;
        this.playLists = playLists;
        this.adapterListener = listener;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return playLists.size();
    }

    @Override
    public Object getItem(int position) {
        return playLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = inflater.inflate(R.layout.item_frag_playlist, parent, false);

        final LinearLayout layout_item = view.findViewById(R.id.layout_item);
        final ImageView iv_albumCover = view.findViewById(R.id.iv_albumCover);
        final TextView tv_playlistTitle = view.findViewById(R.id.tv_playlistTitle);
        final TextView tv_songCount = view.findViewById(R.id.tv_songCount);

        Playlist playlist = playLists.get(position);

        tv_playlistTitle.setText(playlist.getTitle());
        // TODO: count of songs

        layout_item.setOnClickListener(v -> adapterListener.onBucketItemClicked(playlist.getId()));

        return view;
    }


}
