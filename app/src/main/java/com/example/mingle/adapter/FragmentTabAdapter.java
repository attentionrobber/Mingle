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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mingle.Constants;
import com.example.mingle.MusicService;
import com.example.mingle.PlayerService;
import com.example.mingle.R;
import com.example.mingle.domain.Common;
import com.example.mingle.domain.Music;

import java.io.Serializable;
import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FragmentTabAdapter extends RecyclerView.Adapter<FragmentTabAdapter.ViewHolder> {

    private static final int TYPE_BUTTON = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Music> musics;
    private int layout_item;
    private String TAB_NAME = Constants.TAB.SONG;

    private AdapterListener adapterListener;

    public FragmentTabAdapter(Context context, List<Music> musics, int layoutRes, AdapterListener listener) {
        this.context = context;
        this.musics = musics;
        adapterListener = listener;

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

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_BUTTON;
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_BUTTON) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button_in_recycler_view, parent, false);
        } else
            view = LayoutInflater.from(parent.getContext()).inflate(layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // first item(index 0) set button layout
        if (holder.getItemViewType() == TYPE_BUTTON) {
            // TODO: Button Event
            holder.btn_sort.setOnClickListener(v -> {
                PopupMenu dropDownMenu = new PopupMenu(context, holder.btn_sort);
                dropDownMenu.getMenuInflater().inflate(R.menu.drop_down_menu, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menuItem_sort_title)
                        sortBy("title");
                    else if (item.getItemId() == R.id.menuItem_sort_artist)
                        sortBy("artist");
                    else if (item.getItemId() == R.id.menuItem_sort_added)
                        sortBy("added");
                    return true;
                });
                dropDownMenu.show();
            });

            holder.btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //playMusicService();
                }
            });
        } else {
            int pos_view = position-1; // -1 because index 0 is button layout
            Common common = musics.get(pos_view);

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
//                Intent intent = new Intent(context, PlayerService.class);
//                Bundle extras = new Bundle();
//
//
//                extras.putString("tab", TAB_NAME); // TODO: switch value
//                extras.putInt("position", pos_view);
//                //extras.putSerializable("song", musics.get(pos_view));
//                extras.putSerializable("playlist", (Serializable) musics);
//                intent.putExtras(extras);
//
//
//                intent.setAction(PlayerService.ACTION_PLAY);
//                context.startService(intent);
//                Log.i("Service - TabAdapter", "" + pos_view + " | " + ((Common) musics.get(pos_view)).getMusicUri());


                // MainActivity 로 보냄
                if (adapterListener != null) {
                    //Log.i("Main_Listener", "not null");
                    adapterListener.onRecyclerViewItemClicked(musics, pos_view);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return musics.size() + 1; // +1 because index 0 is button layout
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_item;
        ImageView iv_albumCover;
        TextView tv_title, tv_artist;

        Button btn_sort, btn_play, btn3;

        ViewHolder(View view) { // position == getAdapterPosition();
            super(view);
            layout_item = view.findViewById(R.id.layout_item);
            iv_albumCover = view.findViewById(R.id.iv_albumCover);
            tv_title = view.findViewById(R.id.tv_title);
            tv_artist = view.findViewById(R.id.tv_artist);

            btn_sort = view.findViewById(R.id.btn_sort);
            btn_play = view.findViewById(R.id.btn2);
            btn3 = view.findViewById(R.id.btn3);

//            layout_item.setOnClickListener(v -> {
//                Log.i("adapterTEST", "pos: "+getAdapterPosition()+" title: ");
//                Intent intent = new Intent(context, PlayerService.class);
//                intent.putExtra("MusicUri", common.getUri().toString());
//                intent.setAction(PlayerService.ACTION_PLAY);
//                context.startService(intent);
//            });
        }
    }

    private void sortBy(String by) {
        //Collections.sort(musics, (o1, o2) -> Integer.compare(o1.getTitle().charAt(0), o2.getTitle().charAt(0))); // musics 를 타이틀순(영,한,기타)으로 정렬
        switch (by) {
            case "title":
                Collections.sort(musics, (o1, o2) -> compareStringWithLocale(o1.getTitle(), o2.getTitle()));
                break;
            case "artist":
                Collections.sort(musics, (o1, o2) -> compareStringWithLocale(o1.getArtist(), o2.getArtist()));
                break;
            case "added":
                Collections.sort(musics, (o1, o2) -> Long.compare(o2.getDate_added(), o1.getDate_added()));
                break;
        }
        notifyDataSetChanged();
    }

    private int compareStringWithLocale(String arg1, String arg2) {
        Collator coll = Collator.getInstance(Locale.KOREA);
        coll.setStrength(Collator.PRIMARY);
        return coll.compare(arg1, arg2);
    }
}
