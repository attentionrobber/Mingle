package com.example.mingle.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mingle.R;
import com.example.mingle.domain.Music;

import java.util.List;

public class PlayerAdapter extends PagerAdapter {

    private List<Music> musics;
    private Context context;

    private LayoutInflater inflater;

    public PlayerAdapter(List<Music> musics, Context context) {
        this.musics = musics;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // 데이터 총 개수
    @Override
    public int getCount() {
        return musics.size();
    }

    // ListView의 getView와 같은 역할. 화면 하나하나를 만들어준다.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //return super.instantiateItem(container, position);

        View view = inflater.inflate(R.layout.player_card_item, null); // parent가 없는 inflater를 사용해야하므로

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_artist = (TextView) view.findViewById(R.id.tv_artist);

        // 데이터 가져오기
        Music music = musics.get(position);

        tv_title.setText(music.getTitle());
        tv_artist.setText(music.getArtist());

        Glide.with(context).load(music.getAlbumImgUri()).placeholder(R.drawable.default_album_image).into(imageView); // placeholder()는 디폴트 이미지를 지정해줄 수 있다.

        // 생성한 뷰를 컨테이너에 담아준다. 컨테이너 = 뷰페이저를 생성한 최외곽 레이아웃 개념
        container.addView(view);

        return view;
    }

    // 화면에 보이지 않는 요소(View)들을 메모리에서 지워줌.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    // instantiateItem에서 리턴된 Object가 View인지 아닌지 확인.
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}