package com.example.mingle;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mingle.domain.Music;

import java.util.ArrayList;
import java.util.List;

public class MediaLoader {

    public static List<Music> musics = new ArrayList<>();
    public static List<Music> musicsByAlbum = new ArrayList<>();
    //private Context context;

    private static final Uri URI_MUSIC = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    private static final String[] PROJ = { // 데이터에서 가져올 데이터 컬럼명을 projections 에 담는다.
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_KEY,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.IS_MUSIC,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.YEAR
    };

    public static void load(Context context) {
        ContentResolver resolver = context.getContentResolver(); // 데이터에 접근하기 위해 Content Resolver 를 불러온다.
        //String selection = MediaStore.Audio.Media.+" =?"; // 폴더명으로 선별(bucketName)
        Cursor cursor = resolver.query(URI_MUSIC, PROJ, null, null, null); // Content Resolver 로 Query 한 데이터를 cursor 에 담게된다.

        if(cursor != null) {
            while(cursor.moveToNext()) {  // cursor 에 담긴 데이터를 반복문을 돌면서 꺼내 담아준다.

                Music music = new Music();

                // 5. 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 proj을 세팅
                music.setId(getInt(cursor, PROJ[0]));
                music.setAlbum_id(getInt(cursor, PROJ[1]));
                music.setTitle(getString(cursor, PROJ[2]));
                music.setArtist_id(getInt(cursor, PROJ[3]));
                music.setArtist(getString(cursor, PROJ[4]));
                music.setArtist(getString(cursor, PROJ[5]));
                music.setDuration(getInt(cursor, PROJ[6]));
                music.setIs_music(getString(cursor, PROJ[7]));
                music.setComposer(getString(cursor, PROJ[8]));
                music.setYear(getString(cursor, PROJ[9]));

                music.setMusic_uri(Uri.withAppendedPath(URI_MUSIC, music.getId()+""));
                music.setAlbum_img(Uri.parse("content://media/external/audio/albumart/" + music.getAlbum_id()));
                //music.album_img = getAlbumImageSimple(music.album_id); // URI로 직접 이미지를 로드한다. (이미지 못불러오는 경우 있음)
                //music.bitmap_img = getAlbumImageBitmap(music.album_id, context); // Bitmap으로 처리해서 이미지를 로드한다. (매우느림)

                musics.add(music);
            }
            cursor.close(); // 사용 후 close 해주지 않으면 메모리 누수가 발생할 수 있다.
        }
    }

    public static void selectionByAlbum(Context context) {
        Log.i("TESTS", "selectionByAlbum");
        ContentResolver resolver = context.getContentResolver(); // 데이터에 접근하기 위해 Content Resolver 를 불러온다.
        String selection = MediaStore.Audio.Media.ALBUM + " =?"; // 앨범명으로 선별
        Cursor cursor = resolver.query(URI_MUSIC, PROJ, selection, null, null);

        if(cursor != null) {
            while( cursor.moveToNext() ) {
                Music music = new Music();

                music.setId(getInt(cursor, PROJ[0])); // 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 projection 을 세팅
                music.setAlbum_id(getInt(cursor, PROJ[1]));
                music.setTitle(getString(cursor, PROJ[2]));
                music.setArtist_id(getInt(cursor, PROJ[3]));
                music.setArtist(getString(cursor, PROJ[4]));
                music.setArtist(getString(cursor, PROJ[5]));
                music.setDuration(getInt(cursor, PROJ[6]));
                music.setIs_music(getString(cursor, PROJ[7]));
                music.setComposer(getString(cursor, PROJ[8]));
                music.setYear(getString(cursor, PROJ[9]));

                music.setMusic_uri(Uri.withAppendedPath(URI_MUSIC, music.getId()+""));
                music.setAlbum_img(Uri.parse("content://media/external/audio/albumart/" + music.getAlbum_id()));
                //music.album_img = getAlbumImageSimple(music.album_id); // URI로 직접 이미지를 로드한다. (이미지 못불러오는 경우 있음)
                //music.bitmap_img = getAlbumImageBitmap(music.album_id, context); // Bitmap으로 처리해서 이미지를 로드한다. (매우느림)

                musicsByAlbum.add(music);
            }
            cursor.close();
        }
    }

    private static String getString(Cursor cursor, String columnName){
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getString(idx);
    }

    private static int getInt(Cursor cursor, String columnName){
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getInt(idx);
    }
}
