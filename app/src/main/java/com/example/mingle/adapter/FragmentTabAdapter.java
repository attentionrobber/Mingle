package com.example.mingle.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.mingle.Constants;
import com.example.mingle.PlayerService;
import com.example.mingle.R;
import com.example.mingle.domain.Common;
import com.example.mingle.domain.Music;

import java.util.List;

public class FragmentTabAdapter extends RecyclerView.Adapter<FragmentTabAdapter.ViewHolder> {

    private Context context;
    private List<Music> musics;
    private int layout_item;
    private String TAB_NAME = Constants.TAB.SONG;

    private AdapterListener adapterListener;

    public FragmentTabAdapter(Context context, List<Music> musics, int layoutRes, AdapterListener listener) {
        this.context = context;
        this.musics = musics;
        adapterListener = listener;
        //Log.i("TESTS", "size: "+musicList.size());

        switch (layoutRes) {
            case R.layout.item_frag_favorite:
                TAB_NAME = Constants.TAB.FAVORITE;
                layout_item = R.layout.item_frag_favorite;
                break;
            case R.layout.item_frag_playlist:
                TAB_NAME = Constants.TAB.PLAYLIST;
                layout_item = R.layout.item_frag_playlist;
                break;
            case R.layout.item_frag_song:
                TAB_NAME = Constants.TAB.SONG;
                layout_item = R.layout.item_frag_song;
                break;
            case R.layout.item_frag_album:
                TAB_NAME = Constants.TAB.ALBUM;
                layout_item = R.layout.item_frag_album;
                break;
            case R.layout.item_frag_artist:
                TAB_NAME = Constants.TAB.ARTIST;
                layout_item = R.layout.item_frag_artist;
                break;
            case R.layout.item_frag_folder:
                TAB_NAME = Constants.TAB.FOLDER;
                layout_item = R.layout.item_frag_folder;
                break;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Common common = musics.get(position);

        holder.tv_title.setText(common.getTitle());
        holder.tv_artist.setText(common.getArtist());
        //holder.iv_albumCover.setImageURI(Uri.parse(common.getAlbumImgUri()));
        //holder.iv_albumCover.setImageURI(Uri.parse("content://media/external/audio/albumart/460"));
        Glide.with(context)
                .load(R.drawable.default_album_image)
                .into(holder.iv_albumCover);
//        Glide.with(context)
//                .load(common.getAlbumImgPath())
//                .thumbnail(0.5f)// 50%의 비율로 로드
//                .override(100) // 강제 사이즈 제한
//                .centerCrop()
//                .placeholder(R.drawable.default_album_image)
//                .into(holder.iv_albumCover);

        //Uri uri = Uri.parse(common.getAlbumImgUri());
        //Log.i("Adapter_Tab", "tab: "+((Music) common).getPath() +" | "+common.getAlbumImgUri());


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

        holder.layout_item.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayerService.class);
            Bundle extras = new Bundle();
            extras.putString("tab", TAB_NAME); // TODO: switch value
            extras.putInt("position", position);
            intent.putExtras(extras);
            intent.setAction(PlayerService.ACTION_PLAY);
            context.startService(intent);
            Log.i("Service - TabAdapter", ""+position+" | "+((Common) musics.get(position)).getMusicUri());

            // MainActivity 로 보냄
            if (adapterListener != null) {
                //Log.i("Main_Listener", "not null");
                adapterListener.onRecyclerViewItemClicked(musics, position);
            }
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
