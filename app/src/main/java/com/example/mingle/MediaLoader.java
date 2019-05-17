package com.example.mingle;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mingle.domain.Album;
import com.example.mingle.domain.Music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MediaLoader {

    public static List<Music> musics = new ArrayList<>();
    public static List<Music> musicsByAlbum = new ArrayList<>();
    //private Context context;

    private static final Uri URI_MUSIC = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    private static final String[] PROJ = { // 데이터에서 가져올 데이터 컬럼명을 projections 에 담는다.
            MediaStore.Audio.Media._ID,         // 0, int id
            MediaStore.Audio.Media.TITLE,       // 1, String title
            MediaStore.Audio.Media.ARTIST_ID,   // 2, int artist_id
            MediaStore.Audio.Media.ARTIST,      // 3, String artist
            MediaStore.Audio.Media.ARTIST_KEY,  // 4, String artist_key
            MediaStore.Audio.Media.ALBUM_ID,    // 5, int album_id
            MediaStore.Audio.Media.ALBUM,       // 6, String album
            MediaStore.Audio.Media.COMPOSER,    // 7, String composer
            MediaStore.Audio.Media.YEAR,        // 8, String year
            MediaStore.Audio.Media.DURATION,    // 9, int duration
            MediaStore.Audio.Media.IS_MUSIC     // 10, String isMusic
            //MediaStore.Audio.Genres.NAME
    };

    public static void load(Context context) {
        musics.clear();

        ContentResolver resolver = context.getContentResolver(); // 데이터에 접근하기 위해 Content Resolver 를 불러온다.
        //String selection = MediaStore.Audio.Media.+" =?"; // 폴더명으로 선별(bucketName)
        Cursor cursor = resolver.query(URI_MUSIC, PROJ, null, null, null); // Content Resolver 로 Query 한 데이터를 cursor 에 담게된다.

        if(cursor != null) {
            while(cursor.moveToNext()) {  // cursor 에 담긴 데이터를 반복문을 돌면서 꺼내 담아준다.

                Music music = new Music();

                music.setId(getInt(cursor, PROJ[0])); // 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 proj을 세팅
                music.setUri(Uri.withAppendedPath(URI_MUSIC, music.getId()+""));
                music.setTitle(getString(cursor, PROJ[1]));
                music.setArtist_id(getInt(cursor, PROJ[2]));
                music.setArtist(getString(cursor, PROJ[3]));
                music.setArtist_key(getString(cursor, PROJ[4]));
                music.setAlbum_id(getInt(cursor, PROJ[5]));
                music.setAlbum(getString(cursor, PROJ[6]));
                music.setAlbum_img(Uri.withAppendedPath(Uri.parse("content://media/external/audio/albumart/"), music.getAlbum_id()+""));
                music.setComposer(getString(cursor, PROJ[7]));
                music.setYear(getString(cursor, PROJ[8]));
                music.setDuration(getInt(cursor, PROJ[9]));
                music.setIsMusic(getString(cursor, PROJ[10]));

                //music.setAlbum_img(Uri.parse("content://media/external/audio/albumart/" + music.getAlbum_id()));
                //music.album_img = getAlbumImageSimple(music.album_id); // URI로 직접 이미지를 로드한다. (이미지 못불러오는 경우 있음)
                //music.bitmap_img = getAlbumImageBitmap(music.album_id, context); // Bitmap으로 처리해서 이미지를 로드한다. (매우느림)

                musics.add(music);
            }
            cursor.close(); // 사용 후 close 해주지 않으면 메모리 누수가 발생할 수 있다.
        }
    }

    public static List<Music> loadFavorite() {
        // TODO: 음악마다 즐겨찾기 설정 or 취소 기능 추가
        List<Music> favorite = new ArrayList<>(musics);

        for (int i = 0; i < favorite.size(); i++) {

        }


        return favorite;
    }
    public static List<Album> selectionByAlbum() {

        // TODO: musics 에 담겨있는 List 로 Album 분류한다.
        // TODO: 그리고 그 Album 클릭 시 Album 해당 Album ID 를 통해 곡을 선별
        // TODO: 필요한것 Album 안에 있는 커버, 아티스트, 대표곡, 곡 개수

        List<Music> sorted = new ArrayList<>(musics); // List of Song Sorted by Album
        Collections.sort(sorted, (o1, o2) -> Long.compare(o2.getAlbum_id(), o1.getAlbum_id())); // musics 를 앨범순으로 정렬
        List<Album> musicsByAlbum = new ArrayList<>(); // List of Album Selected by Album

        List<Integer> albumSet = new ArrayList<>(); // musics 의 Album ID 의 List
        List<Integer> albumCount = new ArrayList<>(); // musics 의 Album ID 의 List

        int j = 0, count = 1;
        for (int i = 0; i < sorted.size(); i++) {
            //albumCount.add(sorted.get(i).getAlbum_id());
            if (!albumSet.contains(sorted.get(i).getAlbum_id())) { // AlbumSet 에 Album ID 가 없는 경우
                albumSet.add(sorted.get(i).getAlbum_id()); // AlbumSet 에 Album ID 추가.

                Album album = new Album();
                album.setAlbum_id(sorted.get(i).getAlbum_id());
                album.setAlbum(sorted.get(i).getAlbum());
                album.setAlbum_img(sorted.get(i).getAlbum_img());
                album.setArtist(sorted.get(i).getArtist());
                album.setTitle(sorted.get(i).getTitle());
                count = 1;
                album.setCount(count);
                musicsByAlbum.add(album);
            } else { // 존재할 경우
                count++;
                //Log.i("TESTS", "" + count);
            }
            //Log.i("TESTS", "albumID: " + sorted.get(i).getAlbum_id());
        }

        Log.i("TESTS", "frequency: " + Collections.frequency(albumSet, sorted.get(17).getAlbum_id()));
        Log.i("TESTS", "albumSetSize: " + albumSet.size());
        Log.i("TESTS", "musicByAlbum: " + musicsByAlbum.size());
        Log.i("TESTS", "sortedSize: " + sorted.size());


        //Log.i("TESTS", "albums.size(): " + albums.size());
        return musicsByAlbum;
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
