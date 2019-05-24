package com.example.mingle.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

public class MethodCollection {

    /**
     * Uri 가 존재하는 파일인지 검사하는 함수.
     */
    public static Uri existAlbumArt(Context context, String strUri) {
        Uri mUri = Uri.parse(strUri);
        Drawable d = null;
        if (mUri != null) {
            if ("content".equals(mUri.getScheme())) {
                try {
                    d = Drawable.createFromStream(context.getContentResolver().openInputStream(mUri), null);
                } catch (Exception e) {
                    Log.w("checkUriExists", "Unable to open content: " + mUri, e);
                    mUri = null;
                }
            } else
                d = Drawable.createFromPath(mUri.toString());

            if (d == null)
                mUri = null;
        }

        return mUri;
    }
}
