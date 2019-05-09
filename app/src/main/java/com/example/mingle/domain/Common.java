package com.example.mingle.domain;

import android.net.Uri;

/**
 * Music 를 사용할 때 공통적으로 사용하는 클래스
 */
public abstract class Common {
    public abstract String getTitle();
    public abstract String getArtist();
    public abstract Uri getAlbum_img();
}
