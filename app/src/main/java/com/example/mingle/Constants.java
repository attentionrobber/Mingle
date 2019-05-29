package com.example.mingle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {
    public interface TAB {
        public static String FAVORITE = "Favorite";
        public static String PLAYLIST = "Playlist";
        public static String SONG = "Song";
        public static String ALBUM = "Album";
        public static String ARTIST = "Artist";
        public static String FOLDER = "Folder";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_album_image, options);
        } catch (Error ignored) {
        } catch (Exception ignored) {
        }
        return bm;
    }

}