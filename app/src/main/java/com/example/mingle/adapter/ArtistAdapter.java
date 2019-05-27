package com.example.mingle.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mingle.R;
import com.example.mingle.domain.Music;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    private Context context;
    private List<Music> musicList;

    public ArtistAdapter(Context context, List<Music> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frag_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //Artist artist = (Artist) musicList.get(position);
        //Album album = (Album) musicList.get(position);

        holder.tv_artist.setText(musicList.get(position).getArtist());
        //holder.tv_songCount.setText(artist.getSongCount() + "곡");
        //holder.iv_albumCover.setImageURI(Uri.parse(musicList.get(position).getAlbumImgUri()));
        // TODO: Glide didn't Work. Need to Fix
        Glide.with(context).load("/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1447312718211")
                .placeholder(R.drawable.default_album_image)
                .into(holder.iv_albumCover);
        //Log.i("Adapter_Artist", "artist: "+musicList.get(position).getAlbumImgUri());
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_item;
        ImageView iv_albumCover;
        TextView tv_songCount, tv_artist;

        ViewHolder(View view) { // position == getAdapterPosition();
            super(view);
            layout_item = view.findViewById(R.id.layout_item);
            iv_albumCover = view.findViewById(R.id.iv_albumCover);
            tv_songCount = view.findViewById(R.id.tv_songCount);
            tv_artist = view.findViewById(R.id.tv_artist);
        }
    }
}