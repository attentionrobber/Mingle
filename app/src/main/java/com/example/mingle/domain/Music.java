package com.example.mingle.domain;


import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.mingle.utility.MethodCollection;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Music extends Common {

    // Music Info.
    private String path; // MediaStore.Audio.Media.DATA
    private String musicUri; // Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media._ID).toString();
    private String title; // MediaStore.Audio.Media.TITLE
    private long artist_id; // MediaStore.Audio.Media.ARTIST_ID
    private String artist; // MediaStore.Audio.Media.ARTIST
    private String artist_key; // MediaStore.Audio.Media.ARTIST_KEY
    private long album_id; // MediaStore.Audio.Media.ALBUM_ID
    private String album; // MediaStore.Audio.Media.ALBUM
    private String albumImgUri; // Uri.parse("content://media/external/audio/albumart/" + music.album_id);
    private Bitmap albumImgBitmap;
    private String composer; // MediaStore.Audio.Media.COMPOSER
    private String year; // MediaStore.Audio.Media.YEAR
    private long duration; // MediaStore.Audio.Media.DURATION
    private String isMusic; // MediaStore.Audio.Media.IS_MUSIC
    //private int genre_id;
    //private String content_type;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(long artist_id) {
        this.artist_id = artist_id;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbumImgBitmap(Bitmap albumImgBitmap) {
        this.albumImgBitmap = albumImgBitmap;
    }

    @Override
    public Bitmap getAlbumImgBitmap() {
        return albumImgBitmap;
    }

    //    @Override
//    public Bitmap getAlbumImgBitmap(Context context, int album_id) {
//        Uri uri = Uri.parse("content://media/external/audio/albumart/" + album_id); // 앨범아이디로 URI 생성
//        ContentResolver resolver = context.getContentResolver(); // ContentResolver 가져오기
//        try {
//            InputStream is = resolver.openInputStream(uri); // resolver 에서 Stream 열기
//            return BitmapFactory.decodeStream(is); // BitmapFactory 를 통해 이미지 데이터를 가져온다.
//        } catch (FileNotFoundException e) {
//            Log.e(e.toString(), "getAlbumImageBitmap");
//            e.printStackTrace(); // 동일 스레드가 아닌 다른 스레드에서 실행되므로 시스템에 영향을 미치지 않는다. Sysout은 시스템 성능에 영향을미침.
//        }
//        return null;
//    }


    public String getArtist_key() {
        return artist_key;
    }

    public void setArtist_key(String artist_key) {
        this.artist_key = artist_key;
    }

    @Override
    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumImgUri() {
        return albumImgUri;
    }

    public String getAlbumImgPath() {
        Uri uri = Uri.parse(getAlbumImgUri());
        return "";
    }

    public void setAlbumImgUri(String albumImgUri) {
        this.albumImgUri = albumImgUri;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setIsMusic(String is_music) {
        this.isMusic = is_music;
    }

    public String getIsMusic() {
        return isMusic;
    }

}