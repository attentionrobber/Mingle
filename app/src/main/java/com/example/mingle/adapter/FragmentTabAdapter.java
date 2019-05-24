package com.example.mingle.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.os.Parcelable;
import android.provider.MediaStore;
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
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.example.mingle.MainActivity;
import com.example.mingle.PlayerService;
import com.example.mingle.R;
import com.example.mingle.domain.Common;
import com.example.mingle.domain.Music;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FragmentTabAdapter extends RecyclerView.Adapter<FragmentTabAdapter.ViewHolder> {

    private Context context;
    private List<Music> musicList;
    private int item_frag_layout;
    private AdapterListener adapterListener;


    public interface AdapterListener {
        void onRecyclerViewItemClicked(List<Music> musics, int position);
    }

    public FragmentTabAdapter(Context context, List<Music> musicList, int resLayout, AdapterListener listener) {
        this.context = context;
        this.musicList = musicList;
        adapterListener = listener;
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

        Common common = musicList.get(position);

        holder.tv_title.setText(common.getTitle());
        holder.tv_artist.setText(common.getArtist());
        //holder.iv_albumCover.setImageURI(Uri.parse(common.getAlbumImgUri()));
        Glide.with(context)
                .load(common.getAlbumImgBitmap(context, common.getAlbum_id()))
                .thumbnail(0.5f)// 50%의 비율로 로드
                .override(100) // 강제 사이즈 제한
                .centerCrop()
                .placeholder(R.drawable.default_album_image)
                .into(holder.iv_albumCover);

        //Uri uri = Uri.parse(common.getAlbumImgUri());
        //Log.i("Adapter_Tab", "tab: "+new File(common.getAlbumImgUri()).toString());
        // TODO: Glide didn't Work. Need to Fix
//        Uri albumArtUri = Uri.parse(musicList.get(position).getAlbumImgUri());
//        String mimeType = musicList.get(position).getYear();
//        long lastModified = Long.parseLong(musicList.get(position).getIs_music());
//        Log.i("TabAdapter", ""+albumArtUri+" | MIME: "+mimeType+" | modi: "+lastModified);
//        holder.iv_albumCover.setImageDrawable(null);
//        Glide.with(context)
//                .load(albumArtUri)
//                .signature(new MediaStoreSignature(mimeType, lastModified, 0))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .placeholder(R.drawable.default_album_image)
//                .into(holder.iv_albumCover);

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerService.class);
                Bundle extras = new Bundle();
                extras.putInt("position", position);
                intent.putExtras(extras);
                intent.setAction(PlayerService.ACTION_PLAY);
                context.startService(intent);
                Log.i("Service - TabAdapter", ""+position+" | "+((Common) musicList.get(position)).getMusicUri());

                // MainActivity 로 보냄
                if (adapterListener != null) {
                    //Log.i("Main_Listener", "not null");
                    adapterListener.onRecyclerViewItemClicked(musicList, position);
                }
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
