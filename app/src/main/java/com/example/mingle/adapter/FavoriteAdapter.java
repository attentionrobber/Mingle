package com.example.mingle.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mingle.Constants;
import com.example.mingle.PlayerService;
import com.example.mingle.R;
import com.example.mingle.domain.Music;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context context;
    private List<Music> musics;
    private AdapterListener adapterListener;

    public FavoriteAdapter(Context context, List<Music> music, AdapterListener listener) {
        this.context = context;
        this.musics = music;
        this.adapterListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frag_favorite, viewGroup, false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Music music = musics.get(position);

        viewHolder.tv_artist.setText(music.getArtist());
        viewHolder.tv_title.setText(music.getTitle());
        Glide.with(context)
                .load(R.drawable.default_album_image)
                .into(viewHolder.iv_albumCover);

        viewHolder.layout_item.setOnClickListener(v -> {
//            Intent intent = new Intent(context, PlayerService.class);
//            Bundle extras = new Bundle();
//            extras.putString("tab", Constants.TAB.FAVORITE);
//            extras.putInt("position", position);
//            intent.putExtras(extras);
//            intent.setAction(PlayerService.ACTION_PLAY);
//            context.startService(intent);

            if (adapterListener != null) // MainActivity 로 보냄
                adapterListener.onRecyclerViewItemClicked(musics, position);
        });
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_item;
        ImageView iv_albumCover;
        TextView tv_title, tv_artist;

        ViewHolder(View itemView) {
            super(itemView);
            layout_item = itemView.findViewById(R.id.layout_item);
            iv_albumCover = itemView.findViewById(R.id.iv_albumCover);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_artist = itemView.findViewById(R.id.tv_artist);
        }
    }
}
