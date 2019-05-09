package com.example.mingle;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mingle.domain.Common;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<?> datas;
    private int item_frag_layout;

    public RecyclerViewAdapter(Context context, List<?> datas, int resString) {
        this.context = context;
        this.datas = datas;
        switch (resString) { // MainActivity에서 Adapter를 호출할 때 View를 바꿔줄 수 있다.
            case R.string.tab_favorite:
                item_frag_layout = R.layout.item_frag_song;
                break;
            case R.string.tab_playlist:
                item_frag_layout = R.layout.item_frag_song;
                break;
            case R.string.tab_song:
                item_frag_layout = R.layout.item_frag_song;
                break;
            case R.string.tab_album:
                item_frag_layout = R.layout.item_frag_song; // TODO: 맞는 레이아웃 바꾸기
                break;
            case R.string.tab_artist:
                item_frag_layout = R.layout.item_frag_song;
                break;
            case R.string.tab_folder:
                item_frag_layout = R.layout.item_frag_song;
                break;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_frag_layout, parent, false);
        Log.i("TESTS", "createViewHolder"+item_frag_layout);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

//        Common common = (Common) datas.get(position);
//
//        holder.tv_title.setText(common.getTitle());
//        holder.tv_artist.setText(common.getArtist());;
//        Glide.with(context).load(common.getAlbum_img())
//                .placeholder(R.drawable.default_album_image)
//                .into(holder.iv_albumCover);
        holder.tv_title.setText("title");
        holder.tv_artist.setText("artist");
        Log.i("TESTS", "rvAdapter"+((Common) datas.get(position)).getTitle());


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_item;
        ImageView iv_albumCover;
        TextView tv_title, tv_artist;

        ViewHolder(View view) {
            super(view);
            // position == getAdapterPosition();
            layout_item = view.findViewById(R.id.layout_item);
            iv_albumCover = view.findViewById(R.id.iv_albumCover);
            tv_title = view.findViewById(R.id.tv_title);
            tv_artist = view.findViewById(R.id.tv_artist);
        }
    }
}
