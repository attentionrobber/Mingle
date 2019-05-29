package com.example.mingle.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mingle.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TABS = new int[]{R.string.tab_favorite, R.string.tab_playlist, R.string.tab_song, R.string.tab_album, R.string.tab_artist, R.string.tab_folder};
    private final Context mContext;

    public TabPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
         //getItem is called to instantiate the fragment for the given page.
         //Return a PlaceholderFragment (defined as a static inner class below).

        if (position == 1) // Playlist Tab
            return PlaylistFragment.newInstance(position+1);

        return PlaceholderFragment.newInstance(position+1);
//return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TABS[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TABS.length;
    }
}