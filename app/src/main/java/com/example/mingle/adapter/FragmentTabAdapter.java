package com.example.mingle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mingle.PlayerService;
import com.example.mingle.R;
import com.example.mingle.domain.Common;

import java.util.List;

public class FragmentTabAdapter extends RecyclerView.Adapter<FragmentTabAdapter.ViewHolder> {

    private Context context;
    private List<?> musicList;
    private int item_frag_layout;

    public FragmentTabAdapter(Context context, List<?> musicList, int resLayout) {
        this.context = context;
        this.musicList = musicList;
        //Log.i("TESTS", "size: "+musicList.size());

        switch (resLayout) {
            case R.layout.item_frag_favorite:
                item_frag_layout = R.layout.item_frag_favorite;
                break;
            case R.layout.item_frag_playlist:
                item_frag_layout = R.layout.item_frag_playlist;
                break;
            case R.layout.item_frag_song:
                item_frag_layout = R.layout.item_frag_song;
                break;
            case R.layout.item_frag_album:
                item_frag_layout = R.layout.item_frag_album; // TODO: 맞는 레이아웃 바꾸기
                break;
            case R.layout.item_frag_artist:
                item_frag_layout = R.layout.item_frag_artist;
                break;
            case R.layout.item_frag_folder:
                item_frag_layout = R.layout.item_frag_folder;
                break;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(item_frag_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Common common = (Common) musicList.get(position);

        holder.tv_title.setText(common.getTitle());
        holder.tv_artist.setText(common.getArtist());
        holder.iv_albumCover.setImageURI(common.getAlbum_img());
        // TODO: Glide didn't Work. Need to Fix
//        Glide.with(context).load(common.getAlbum_img())
//                .placeholder(R.drawable.default_album_image)
//                .into(holder.iv_albumCover);

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerService.class);
                intent.putExtra("MusicUri", common.getUri().toString());
                intent.setAction(PlayerService.ACTION_PLAY);
                context.startService(intent);
                Log.i("Service", ""+common.getUri().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_item;
        ImageView iv_albumCover;
        TextView tv_title, tv_artist;

        ViewHolder(View view) { // position == getAdapterPosition();
            super(view);
            layout_item = view.findViewById(R.id.layout_item);
            iv_albumCover = view.findViewById(R.id.iv_albumCover);
            tv_title = view.findViewById(R.id.tv_title);
            tv_artist = view.findViewById(R.id.tv_artist);

            //if (position > 0)
//            Common common = (Common) musicList.get(getAdapterPosition());

//            layout_item.setOnClickListener(v -> {
//                Log.i("adapterTEST", "pos: "+getAdapterPosition()+" title: ");
//                Intent intent = new Intent(context, PlayerService.class);
//                intent.putExtra("MusicUri", common.getUri().toString());
//                intent.setAction(PlayerService.ACTION_PLAY);
//                context.startService(intent);
//            });
        }
    }
}
