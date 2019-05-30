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
        import com.example.mingle.domain.Album;
        import com.example.mingle.domain.Common;

        import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> albums;

    private AdapterListener adapterListener;

    public AlbumAdapter(Context context, List<Album> albums, AdapterListener listener) {
        this.context = context;
        this.albums = albums;
        adapterListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frag_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Album album = albums.get(position);

        holder.tv_albumTitle.setText(album.getAlbum());
        holder.tv_artist.setText(album.getArtist()+" "+album.getCountOfSongs()+"곡");
        //holder.iv_albumCover.setImageURI(Uri.parse(album.getAlbumImgUri()));
        Glide.with(context)
                .load(album.getAlbumImgUri())
                .placeholder(R.drawable.default_album_image)
                .into(holder.iv_albumCover);

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onBucketItemClicked(String.valueOf(album.getAlbum_id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
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
