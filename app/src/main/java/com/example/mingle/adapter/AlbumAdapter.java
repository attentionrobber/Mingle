package com.example.mingle.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mingle.R;
import com.example.mingle.domain.Album;
import com.example.mingle.domain.Common;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<?> musicList;

    public AlbumAdapter(Context context, List<?> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frag_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Album album = (Album) musicList.get(position);

        holder.tv_albumTitle.setText(album.getAlbum());
        holder.tv_artist.setText(album.getArtist()+" "+album.getCount()+"곡");
        holder.iv_albumCover.setImageURI(Uri.parse(album.getAlbumImgUri()));
        // TODO: Glide didn't Work. Need to Fix
//        Glide.with(context).load(common.getAlbum_img())
//                .placeholder(R.drawable.default_album_image)
//                .into(holder.iv_albumCover);
        //Log.i("TESTS", "rvAdapter: "+((Common) musicList.get(position)).getAlbum_img());
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_item;
        ImageView iv_albumCover;
        TextView tv_albumTitle, tv_artist;

        ViewHolder(View view) { // position == getAdapterPosition();
            super(view);
            layout_item = view.findViewById(R.id.layout_item);
            iv_albumCover = view.findViewById(R.id.iv_albumCover);
            tv_albumTitle = view.findViewById(R.id.tv_albumTitle);
            tv_artist = view.findViewById(R.id.tv_artist);
        }
    }
}
