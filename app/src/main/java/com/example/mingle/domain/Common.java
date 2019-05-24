package com.example.mingle.domain;


import android.content.Context;
import android.graphics.Bitmap;

/**
 * Music 를 사용할 때 공통적으로 사용하는 클래스
 */
public abstract class Common {
    public abstract String getMusicUri();
    public abstract String getTitle();
    public abstract String getArtist();
    public abstract int getAlbum_id();
    public abstract Bitmap getAlbumImgBitmap(Context context, int album_id);
}
