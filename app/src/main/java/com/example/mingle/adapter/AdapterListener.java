package com.example.mingle.adapter;

import com.example.mingle.domain.Album;
import com.example.mingle.domain.Music;
import com.example.mingle.domain.Playlist;

import java.util.List;

/**
 * Adapter 의 RecyclerView Position 을 Fragment 에 넘겨준다.
 * Adapter 에서 사용, Fragment 에서 생성 및 정의한다.
 */
public interface AdapterListener {
    void onRecyclerViewItemClicked(List<Music> musics, int position);
    void onBucketItemClicked(String bucketID);
}
