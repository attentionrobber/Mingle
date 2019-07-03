package com.example.mingle.ui.main;

import com.example.mingle.domain.Music;

import java.util.List;

/**
 * Adapter 에서 전달 받은 값을 Fragment 에서 MainActivity 로 넘겨준다.
 * Fragment 에서 사용, MainActivity 에서 생성 및 정의한다.
 */
public interface FragmentListener {
    void onRecyclerViewItemClicked(List<Music> musics, int position, boolean isShuffle);
}