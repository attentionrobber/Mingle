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
import com.example.mingle.domain.Artist;
import com.example.mingle.domain.Music;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private Context context;
    private List<Artist> artists;

    private AdapterListener adapterListener;

    public ArtistAdapter(Context context, List<Artist> artists, AdapterListener listener) {
        this.context = context;
        this.artists = artists;
        adapterListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frag_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Artist artist = artists.get(position);

        holder.tv_artist.setText(artist.getArtist());
        holder.tv_songCount.setText(artist.getNumOfAlbums() + "개의 앨범 | " + artist.getNumOfTracks()+"곡");
        //holder.iv_albumCover.setImageURI(Uri.parse(musicList.get(position).getAlbumImgUri()));
        // TODO: Glide didn't Work. Need to Fix
        Glide.with(context)
                .load("/storage/emulated/0/Android/data/com.android.providers.media/albumthumbs/1447312718211")
                .placeholder(R.drawable.default_album_image)
                .into(holder.iv_albumCover);

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onBucketItemClicked(String.valueOf(artist.getArtist_id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
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