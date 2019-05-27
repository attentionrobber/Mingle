package com.example.mingle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {
    public interface TAB {
        public static String FAVORITE = "om.example.mingle.tab.favorite";
        public static String PLAYLIST = "om.example.mingle.tab.playlist";
        public static String SONG = "om.example.mingle.tab.song";
        public static String ALBUM = "om.example.mingle.tab.album";
        public static String ARTIST = "om.example.mingle.tab.artist";
        public static String FOLDER = "om.example.mingle.tab.folder";
    }
    public interface ACTION {
//        public static String MAIN_ACTION = "com.example.mingle.action.main";
//        public static String INIT_ACTION = "com.example.mingle.action.init";
//        public static String PREV_ACTION = "com.example.mingle.action.prev";
//        public static String PLAY_ACTION = "com.example.mingle.action.play";
//        public static String NEXT_ACTION = "com.example.mingle.action.next";
//        public static String STARTFOREGROUND_ACTION = "com.example.mingle.action.startforeground";
//        public static String STOPFOREGROUND_ACTION = "com.example.mingle.action.stopforeground";
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