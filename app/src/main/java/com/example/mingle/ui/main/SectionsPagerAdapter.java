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
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TABS = new int[]{R.string.tab_favorite, R.string.tab_playlist, R.string.tab_song, R.string.tab_album, R.string.tab_artist, R.string.tab_folder};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
         //getItem is called to instantiate the fragment for the given page.
         //Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1);



//        switch (TABS[position]) {
//            case R.string.tab_text_1:
//                return SongFragment.newInstance(position);
//            case R.string.tab_text_2:
//                //return CallsFragment.newInstance();
//            case R.string.tab_text_3:
//                //return ChatsFragment.newInstance();
//            case R.string.tab_text_4:
//                //return
//            case R.string.tab_text_5:
//                //return
//            case R.string.tab_text_6:
//                //return
//        }
//        return null;
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