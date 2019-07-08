package com.example.mingle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mingle.R;
import com.example.mingle.domain.Music;

import java.text.Collator;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context context;
    private List<Music> musics;
    private AdapterListener adapterListener;

    private static final int TYPE_BUTTON = 0;
    private static final int TYPE_ITEM = 1;

    private boolean isShuffle = false;
    private String sortedBy = "";
    private final String TITLE = "TITLE";
    private final String ARTIST = "ARTIST";
    private final String ADDED = "ADDED";

    public FavoriteAdapter(Context context, List<Music> music, AdapterListener listener) {
        this.context = context;
        this.musics = music;
        this.adapterListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_BUTTON;
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == TYPE_BUTTON)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_button_in_recycler_view, viewGroup, false);
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frag_favorite, viewGroup, false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == TYPE_BUTTON) {
            viewHolder.btn_sort.setOnClickListener(v -> {
                PopupMenu dropDownMenu = new PopupMenu(context, viewHolder.btn_sort);
                dropDownMenu.getMenuInflater().inflate(R.menu.drop_down_menu, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.menuItem_sort_title) {
                        sortedBy = TITLE;
                        sortBy(sortedBy);
                    } else if (item.getItemId() == R.id.menuItem_sort_artist) {
                        sortedBy = ARTIST;
                        sortBy(sortedBy);
                    } else if (item.getItemId() == R.id.menuItem_sort_added) {
                        sortedBy = ADDED;
                        sortBy(sortedBy);
                    }
                    return true;
                });
                dropDownMenu.show();
            });

            viewHolder.btn_playAll.setOnClickListener(v -> {
//                    playMusicService();
            });

            viewHolder.btn_shuffle.setOnClickListener(v -> {
                if (adapterListener != null) { // MainActivity 로 보냄
                    int pos = new Random().nextInt(musics.size());
                    adapterListener.onRecyclerViewItemClicked(musics, pos, true);
                }
            });
        } else {
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
                    adapterListener.onRecyclerViewItemClicked(musics, position, isShuffle);
            });
        }
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_item;
        ImageView iv_albumCover;
        TextView tv_title, tv_artist;

        ImageButton btn_sort, btn_playAll, btn_shuffle;

        ViewHolder(View itemView) {
            super(itemView);
            layout_item = itemView.findViewById(R.id.layout_item);
            iv_albumCover = itemView.findViewById(R.id.iv_albumCover);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_artist = itemView.findViewById(R.id.tv_artist);

            btn_sort = itemView.findViewById(R.id.btn_sort);
            btn_playAll = itemView.findViewById(R.id.btn_playAll);
            btn_shuffle = itemView.findViewById(R.id.btn_shuffle);
        }
    }

    private void sortBy(String sortedBy) {
        //Collections.sort(musics, (o1, o2) -> Integer.compare(o1.getTitle().charAt(0), o2.getTitle().charAt(0))); // musics 를 타이틀순(영,한,기타)으로 정렬
        switch (sortedBy) {
            case TITLE:
                Collections.sort(musics, (o1, o2) -> compareStringWithLocale(o1.getTitle(), o2.getTitle()));
                break;
            case ARTIST:
                Collections.sort(musics, (o1, o2) -> compareStringWithLocale(o1.getArtist(), o2.getArtist()));
                break;
            case ADDED:
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
