package com.example.mingle;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.mingle.domain.Album;
import com.example.mingle.domain.Artist;
import com.example.mingle.domain.Music;
import com.example.mingle.domain.Playlist;
import com.example.mingle.utility.MethodCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaLoader {

    public static List<Music> audio = new ArrayList<>();
    public static List<Music> musics = new ArrayList<>();
    public static List<Music> musicsByArtist = new ArrayList<>();
    public static List<Music> musicsByFolder = new ArrayList<>();

    private static final Uri URI_MUSIC = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


    /**
     * Load All Audio
     */
    public static void loadAudio(Context context) {
        audio.clear();

        ContentResolver resolver = context.getContentResolver(); // 데이터에 접근하기 위해 Content Resolver 를 불러온다.
        final String[] PROJECTION = { // 데이터에서 가져올 데이터 컬럼명을 projections 에 담는다.
                MediaStore.Audio.Media.DATA,         // 0, String path
                MediaStore.Audio.Media._ID,         // 1, String musicUri 의 뒷부분
                MediaStore.Audio.Media.TITLE,       // 2, String title
                MediaStore.Audio.Media.ARTIST_ID,   // 3, long artist_id
                MediaStore.Audio.Media.ARTIST,      // 4, String artist
                MediaStore.Audio.Media.ARTIST_KEY,  // 5, String artist_key
                MediaStore.Audio.Media.ALBUM_ID,    // 6, long album_id
                MediaStore.Audio.Media.ALBUM,       // 7, String album
                MediaStore.Audio.Media.COMPOSER,    // 8, String composer
                MediaStore.Audio.Media.YEAR,        // 9, String year
                MediaStore.Audio.Media.DURATION,    // 10, long duration
                MediaStore.Audio.Media.IS_MUSIC,     // 11, String isMusic
        };
        //String selection = MediaStore.Audio.Media.+" =?"; // XX 으로 선별
        String orderBy = MediaStore.Audio.Media.TITLE+" COLLATE LOCALIZED ASC"; // 기타 한글 영문순 정렬 (+" DESC" 내림차순, ASC 오름차순)

        Cursor cursor = resolver.query(URI_MUSIC, PROJECTION, null, null, orderBy); // Content Resolver 로 Query 한 데이터를 cursor 에 담게된다.
        if(cursor != null) {
            while(cursor.moveToNext()) {  // cursor 에 담긴 데이터를 반복문을 돌면서 꺼내 담아준다.

                Music music = new Music();
                music.setPath(getString(cursor, PROJECTION[0])); // 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 proj을 세팅
                music.setMusicUri(Uri.withAppendedPath(URI_MUSIC, getString(cursor, PROJECTION[1])).toString());
                music.setTitle(getString(cursor, PROJECTION[2]));
                music.setArtist_id(getLong(cursor, PROJECTION[3]));
                music.setArtist(getString(cursor, PROJECTION[4]));
                music.setArtist_key(getString(cursor, PROJECTION[5]));
                music.setAlbum_id(getLong(cursor, PROJECTION[6]));
                music.setAlbum(getString(cursor, PROJECTION[7]));
                music.setAlbumImgUri("content://media/external/audio/albumart/" + music.getAlbum_id());
                music.setComposer(getString(cursor, PROJECTION[8]));
                music.setYear(getString(cursor, PROJECTION[9]));
                music.setDuration(getLong(cursor, PROJECTION[10]));
                music.setIsMusic(getString(cursor, PROJECTION[11]));
                //music.setAlbumImgBitmap(getAlbumImageBitmap(music.getAlbum_id(), context)); // Bitmap으로 처리해서 이미지를 로드한다. (매우느림)
                //music.setAlbum_img(Uri.parse("content://media/external/audio/albumart/" + music.getAlbum_id()));
                //music.album_img = getAlbumImageSimple(music.album_id); // URI로 직접 이미지를 로드한다. (이미지 못불러오는 경우 있음)
                audio.add(music);
            }
            cursor.close(); // 사용 후 close 해주지 않으면 메모리 누수가 발생할 수 있다.
        }
        Log.i("MediaLoader", "audio size: "+audio.size());
        //return musics;
    } // loadAudio()

    public static void loadMusic(Context context) {
        musics.clear();

        ContentResolver resolver = context.getContentResolver(); // 데이터에 접근하기 위해 Content Resolver 를 불러온다.
        final String[] PROJECTION = { // 데이터에서 가져올 데이터 컬럼명을 projections 에 담는다.
                MediaStore.Audio.Media.DATA,         // 0, String path
                MediaStore.Audio.Media._ID,         // 1, String musicUri 의 뒷부분
                MediaStore.Audio.Media.TITLE,       // 2, String title
                MediaStore.Audio.Media.ARTIST_ID,   // 3, long artist_id
                MediaStore.Audio.Media.ARTIST,      // 4, String artist
                MediaStore.Audio.Media.ARTIST_KEY,  // 5, String artist_key
                MediaStore.Audio.Media.ALBUM_ID,    // 6, long album_id
                MediaStore.Audio.Media.ALBUM,       // 7, String album
                MediaStore.Audio.Media.COMPOSER,    // 8, String composer
                MediaStore.Audio.Media.YEAR,        // 9, String year
                MediaStore.Audio.Media.DURATION,    // 10, long duration
                MediaStore.Audio.Media.IS_MUSIC,     // 11, String isMusic
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + "=1"; // XX 으로 선별
        String orderBy = MediaStore.Audio.Media.TITLE+" COLLATE LOCALIZED ASC"; // 기타 한글 영문순 정렬 (+" DESC" 내림차순, ASC 오름차순)

        Cursor cursor = resolver.query(URI_MUSIC, PROJECTION, selection, null, orderBy); // Content Resolver 로 Query 한 데이터를 cursor 에 담게된다.
        if(cursor != null) {
            while(cursor.moveToNext()) {  // cursor 에 담긴 데이터를 반복문을 돌면서 꺼내 담아준다.
                Music music = new Music();
                music.setPath(getString(cursor, PROJECTION[0])); // 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 proj을 세팅
                music.setMusicUri(Uri.withAppendedPath(URI_MUSIC, getString(cursor, PROJECTION[1])).toString());
                music.setTitle(getString(cursor, PROJECTION[2]));
                music.setArtist_id(getLong(cursor, PROJECTION[3]));
                music.setArtist(getString(cursor, PROJECTION[4]));
                music.setArtist_key(getString(cursor, PROJECTION[5]));
                music.setAlbum_id(getLong(cursor, PROJECTION[6]));
                music.setAlbum(getString(cursor, PROJECTION[7]));
                music.setAlbumImgUri("content://media/external/audio/albumart/" + music.getAlbum_id());
                music.setComposer(getString(cursor, PROJECTION[8]));
                music.setYear(getString(cursor, PROJECTION[9]));
                music.setDuration(getLong(cursor, PROJECTION[10]));
                music.setIsMusic(getString(cursor, PROJECTION[11]));
                //music.setAlbumImgBitmap(getAlbumImageBitmap(music.getAlbum_id(), context)); // Bitmap으로 처리해서 이미지를 로드한다. (매우느림)
                //music.setAlbum_img(Uri.parse("content://media/external/audio/albumart/" + music.getAlbum_id()));
                //music.album_img = getAlbumImageSimple(music.album_id); // URI로 직접 이미지를 로드한다. (이미지 못불러오는 경우 있음)
                musics.add(music);
            }
            cursor.close(); // 사용 후 close 해주지 않으면 메모리 누수가 발생할 수 있다.
        }
        Log.i("MediaLoader", "musics size: "+musics.size());
    } // loadMusic()

    public static List<Music> loadFavorite() {
        return new ArrayList<>(MainActivity.favorites);
    }

    // TODO: create Playlist Method
    public static int createPlaylist(Context context, String name) {
        int id = -1;
        Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        if (name != null && name.length() > 0) {
            try {
                Cursor cursor = context.getContentResolver().query(
                        EXTERNAL_CONTENT_URI, // uri
                        new String[] { MediaStore.Audio.Playlists._ID }, // projection
                        MediaStore.Audio.PlaylistsColumns.NAME + "=?", // selection
                        new String[] { name }, // selection args
                        null); // sort
                if (cursor == null || cursor.getCount() < 1) {
                    final ContentValues values = new ContentValues(1);
                    values.put(MediaStore.Audio.PlaylistsColumns.NAME, name);
                    final Uri uri = context.getContentResolver().insert(EXTERNAL_CONTENT_URI, values);
                    if (uri != null) {
                        // necessary because somehow the MediaStoreObserver is not notified when adding a playlist
                        context.getContentResolver().notifyChange(Uri.parse("content://media"), null);
                        //Toast.makeText(context, context.getResources().getString(R.string.created_playlist_x, name), Toast.LENGTH_SHORT).show();
                        id = Integer.parseInt(uri.getLastPathSegment());
                    }
                } else {
                    if (cursor.moveToFirst()) {
                        id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (SecurityException ignored) {
            }
        }
        if (id == -1)
            Toast.makeText(context, "플레이리스트를 만드는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, context.getResources().getString(R.string.could_not_create_playlist), Toast.LENGTH_SHORT).show();

        return id;
//        ContentResolver resolver = context.getContentResolver();
//        ContentValues values = new ContentValues(1);
//        values.put(MediaStore.Audio.Playlists.NAME, name);
//        resolver.insert(uri, values);
    } // createPlaylist()

    public static List<Playlist> loadPlaylist(Context context) {

        List<Playlist> playLists = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Uri contentUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        final String [] PROJECTION = {
                MediaStore.Audio.Playlists._ID, // unique ID
                MediaStore.Audio.Playlists.DATA, // path
                MediaStore.Audio.Playlists.NAME, // Playlist Title
                MediaStore.Audio.Playlists.DATE_ADDED,
                MediaStore.Audio.Playlists.DATE_MODIFIED,
        };
        //String selection = MediaStore.Audio.Playlists.BUCKET_DISPLAY_NAME+" =?"; // 폴더명으로 선별(bucketName)
        //String orderBy = MediaStore.Audio.Playlists.DATE_ADDED+" DESC";

        Cursor cursor = resolver.query(contentUri, PROJECTION, null, null, null);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                Playlist playlist = new Playlist();
                playlist.setId(getString(cursor, PROJECTION[0]));
                playlist.setPath(getString(cursor, PROJECTION[1]));
                playlist.setTitle(getString(cursor, PROJECTION[2]));
                playlist.setDateAdded(getString(cursor, PROJECTION[3]));
                playlist.setDateModified(getString(cursor, PROJECTION[4]));

                playLists.add(playlist);
            }
            cursor.close();
        }

        return playLists;
    } // loadPlaylist()

    public static List<Music> loadSongsInPlaylist(Context context, String playlistID) {

        List<Music> musics = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Uri contentUri = MediaStore.Audio.Playlists.Members.getContentUri("external", Long.parseLong(playlistID));
        final String [] PROJECTION = {
                MediaStore.Audio.Playlists.Members.DATA, // path
                MediaStore.Audio.Playlists.Members._ID, // uri
                MediaStore.Audio.Playlists.Members.TITLE,
                MediaStore.Audio.Playlists.Members.ARTIST,
                MediaStore.Audio.Playlists.Members.ALBUM,
                MediaStore.Audio.Playlists.Members.DURATION
                // TODO: get Count of Songs
        };
        //String selection = MediaStore.Audio.Playlists.BUCKET_DISPLAY_NAME+" =?"; // 폴더명으로 선별(bucketName)
        //String orderBy = MediaStore.Audio.Playlists.DATE_ADDED+" DESC";

        Cursor cursor = resolver.query(contentUri, PROJECTION, null, null, null);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                Music music = new Music();
                music.setPath(getString(cursor, PROJECTION[0]));
                music.setMusicUri(getString(cursor, PROJECTION[1]));
                music.setTitle(getString(cursor, PROJECTION[2]));
                music.setArtist(getString(cursor, PROJECTION[3]));
                music.setAlbum(getString(cursor, PROJECTION[4]));
                music.setDuration(getLong(cursor, PROJECTION[5]));

                musics.add(music);
            }
            cursor.close();
        }

        return musics;
    } // loadPlaylistMusic()

    public static List<Album> loadAlbums(Context context) {

        List<Album> albums = new ArrayList<>();

        // musics 에 담겨있는 List 로 Album 분류한다.
        // 그리고 그 Album 클릭 시 Album 해당 Album ID 를 통해 곡을 선별
        // 필요한것 Album 안에 있는 커버, 아티스트, 대표곡, 곡 개수
        ContentResolver resolver = context.getContentResolver(); // 데이터에 접근하기 위해 Content Resolver 를 불러온다.
        final Uri URI_ALBUMS = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String[] PROJECTION = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };
        //String selection = MediaStore.Audio.Media.+" =?"; // XX 으로 선별
        String orderBy = MediaStore.Audio.Albums.ALBUM + " COLLATE LOCALIZED ASC"; // 기타 한글 영문순 정렬 (+" DESC" 내림차순, ASC 오름차순)

        Cursor cursor = resolver.query(URI_ALBUMS, PROJECTION, null, null, orderBy); // Content Resolver 로 Query 한 데이터를 cursor 에 담게된다.
        if (cursor != null) {
            while (cursor.moveToNext()) {  // cursor 에 담긴 데이터를 반복문을 돌면서 꺼내 담아준다.
                Album album = new Album();
                album.setAlbum_id(getInt(cursor, PROJECTION[0]));
                album.setAlbum(getString(cursor, PROJECTION[1]));
                album.setAlbumImgUri(getString(cursor, PROJECTION[2]));
                album.setArtist(getString(cursor, PROJECTION[3]));
                album.setCountOfSongs(getInt(cursor, PROJECTION[4]));

                albums.add(album);
            }
            cursor.close();
        }
        Log.i("MediaLoader", "size: "+musics.size());


        return albums;

        // 이전에 쓰던 함수
//        List<Music> sorted = new ArrayList<>(musics); // List of Song Sorted by Album
//        Collections.sort(sorted, (o1, o2) -> Long.compare(o2.getAlbum_id(), o1.getAlbum_id())); // musics 를 앨범순으로 정렬
//        List<Music> musicsByAlbum = new ArrayList<>(); // List of Album Selected by Album
//
//        int count = 1;
//        for (int i = 0; i < sorted.size(); i++) {
//            if (i == sorted.size()-1) break;
//            if (sorted.get(i).getAlbum_id() == sorted.get(i+1).getAlbum_id()) { // 같은 앨범일 경우
//                count++;
//            } else {
//                Album album = new Album();
//                album.setAlbum_id(sorted.get(i).getAlbum_id());
//                album.setAlbum(sorted.get(i).getAlbum());
//                album.setAlbumImgUri(sorted.get(i).getAlbumImgUri());
//                album.setArtist(sorted.get(i).getArtist());
//                album.setTitle(sorted.get(i).getTitle());
//                album.setCount(count);
//                count = 1;
//                musicsByAlbum.add(album);
//            }
//        }
    } // loadAlbum()

    public static List<Music> loadSongsInAlbum(Context context, @Nullable String albumID) {

        List<Music> musics = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        final String[] PROJECTION = {
                MediaStore.Audio.Media.DATA,         // 0, String path
                MediaStore.Audio.Media._ID,         // 1, String musicUri 의 뒷부분
                MediaStore.Audio.Media.TITLE,       // 2, String title
                MediaStore.Audio.Media.ARTIST_ID,   // 3, long artist_id
                MediaStore.Audio.Media.ARTIST,      // 4, String artist
                MediaStore.Audio.Media.ARTIST_KEY,  // 5, String artist_key
                MediaStore.Audio.Media.ALBUM_ID,    // 6, long album_id
                MediaStore.Audio.Media.ALBUM,       // 7, String album
                MediaStore.Audio.Media.COMPOSER,    // 8, String composer
                MediaStore.Audio.Media.YEAR,        // 9, String year
                MediaStore.Audio.Media.DURATION,    // 10, long duration
                MediaStore.Audio.Media.IS_MUSIC,     // 11, String isMusic
        };
        String selection = MediaStore.Audio.Media.ALBUM_ID+" =?"; // 앨범 ID로 선별
        String[] selectionArgs = {albumID};
        //String orderBy = MediaStore.Audio.Playlists.DATE_ADDED+" DESC";

        Cursor cursor = resolver.query(URI_MUSIC, PROJECTION, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Music music = new Music();
                music.setPath(getString(cursor, PROJECTION[0])); // 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 proj을 세팅
                music.setMusicUri(Uri.withAppendedPath(URI_MUSIC, getString(cursor, PROJECTION[1])).toString());
                music.setTitle(getString(cursor, PROJECTION[2]));
                music.setArtist_id(getLong(cursor, PROJECTION[3]));
                music.setArtist(getString(cursor, PROJECTION[4]));
                music.setArtist_key(getString(cursor, PROJECTION[5]));
                music.setAlbum_id(getLong(cursor, PROJECTION[6]));
                music.setAlbum(getString(cursor, PROJECTION[7]));
                music.setAlbumImgUri("content://media/external/audio/albumart/" + music.getAlbum_id());
                music.setComposer(getString(cursor, PROJECTION[8]));
                music.setYear(getString(cursor, PROJECTION[9]));
                music.setDuration(getLong(cursor, PROJECTION[10]));
                music.setIsMusic(getString(cursor, PROJECTION[11]));

                musics.add(music);
            }
            cursor.close();
        }

        return musics;
    } // loadSongsInAlbum()

    public static List<Artist> loadArtists(Context context) {

        List<Artist> artists = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver(); // 데이터에 접근하기 위해 Content Resolver 를 불러온다.
        final Uri URI_ARTISTS = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        final String[] PROJECTION = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        };
        //String selection = MediaStore.Audio.Media.+" =?"; // XX 으로 선별
        String orderBy = MediaStore.Audio.Artists.ARTIST+" COLLATE LOCALIZED ASC"; // 기타 한글 영문순 정렬 (+" DESC" 내림차순, ASC 오름차순)

        Cursor cursor = resolver.query(URI_ARTISTS, PROJECTION, null, null, orderBy); // Content Resolver 로 Query 한 데이터를 cursor 에 담게된다.
        if(cursor != null) {
            while (cursor.moveToNext()) {  // cursor 에 담긴 데이터를 반복문을 돌면서 꺼내 담아준다.
                Artist artist = new Artist();
                artist.setArtist_id(getLong(cursor, PROJECTION[0]));
                artist.setArtist(getString(cursor, PROJECTION[1]));
                artist.setNumOfAlbums(getInt(cursor, PROJECTION[2]));
                artist.setNumOfTracks(getInt(cursor, PROJECTION[3]));

                artists.add(artist);
            }
            cursor.close();
        }

        return artists;
    } // loadArtists()

    public static List<Music> loadSongsInArtist(Context context, @Nullable String artistID) {

        List<Music> musics = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        final String[] PROJECTION = {
                MediaStore.Audio.Media.DATA,         // 0, String path
                MediaStore.Audio.Media._ID,         // 1, String musicUri 의 뒷부분
                MediaStore.Audio.Media.TITLE,       // 2, String title
                MediaStore.Audio.Media.ARTIST_ID,   // 3, long artist_id
                MediaStore.Audio.Media.ARTIST,      // 4, String artist
                MediaStore.Audio.Media.ARTIST_KEY,  // 5, String artist_key
                MediaStore.Audio.Media.ALBUM_ID,    // 6, long album_id
                MediaStore.Audio.Media.ALBUM,       // 7, String album
                MediaStore.Audio.Media.COMPOSER,    // 8, String composer
                MediaStore.Audio.Media.YEAR,        // 9, String year
                MediaStore.Audio.Media.DURATION,    // 10, long duration
                MediaStore.Audio.Media.IS_MUSIC,     // 11, String isMusic
        };
        String selection = MediaStore.Audio.Media.ARTIST_ID+" =?"; // 앨범 ID로 선별
        String[] selectionArgs = {artistID};
        //String orderBy = MediaStore.Audio.Playlists.DATE_ADDED+" DESC";

        Cursor cursor = resolver.query(URI_MUSIC, PROJECTION, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Music music = new Music();
                music.setPath(getString(cursor, PROJECTION[0])); // 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 proj을 세팅
                music.setMusicUri(Uri.withAppendedPath(URI_MUSIC, getString(cursor, PROJECTION[1])).toString());
                music.setTitle(getString(cursor, PROJECTION[2]));
                music.setArtist_id(getLong(cursor, PROJECTION[3]));
                music.setArtist(getString(cursor, PROJECTION[4]));
                music.setArtist_key(getString(cursor, PROJECTION[5]));
                music.setAlbum_id(getLong(cursor, PROJECTION[6]));
                music.setAlbum(getString(cursor, PROJECTION[7]));
                music.setAlbumImgUri("content://media/external/audio/albumart/" + music.getAlbum_id());
                music.setComposer(getString(cursor, PROJECTION[8]));
                music.setYear(getString(cursor, PROJECTION[9]));
                music.setDuration(getLong(cursor, PROJECTION[10]));
                music.setIsMusic(getString(cursor, PROJECTION[11]));

                musics.add(music);
            }
            cursor.close();
        }

        return musics;
    } // loadSongsInArtist

    private static String getString(Cursor cursor, String columnName){
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getString(idx);
    }

    private static int getInt(Cursor cursor, String columnName){
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getInt(idx);
    }
    private static long getLong(Cursor cursor, String columnName){
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getLong(idx);
    }



    private static String getPath(Context context, String strUri) {
        // 너무 느림. uri 대신 path 얻어야 Glide 적용가능.
        Log.i("MediaLoader", "getRealPathFromURI(), path: " + strUri);

        if (MethodCollection.existAlbumArt(context, strUri) != null) {
            String[] proj = { MediaStore.Audio.Media.DATA };
            Cursor cursor = context.getContentResolver().query(Uri.parse(strUri), proj, null, null, null);
            cursor.moveToNext();
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            Uri uri = Uri.fromFile(new File(path));
            Log.i("MediaLoader", "getRealPathFromURI(), path : " + uri.toString());
            cursor.close();
            return path;
        } else return "";
    }
}
