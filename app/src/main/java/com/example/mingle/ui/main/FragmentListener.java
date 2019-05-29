package com.example.mingle.ui.main;

import com.example.mingle.domain.Music;

import java.util.List;

public interface FragmentListener {
    void onRecyclerViewItemClicked(List<Music> musics, int position);
}