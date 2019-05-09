package com.example.mingle.domain;

import android.net.Uri;

public class Music extends Common {

    // Music Info.
    private int id; // MediaStore.Audio.Media._ID
    private Uri music_uri; // Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, music_id+"");
    private String title; // MediaStore.Audio.Media.TITLE
    private String artist; // MediaStore.Audio.Media.ARTIST
    private int artist_id; // MediaStore.Audio.Media.ARTIST_ID
    private String artist_key; // MediaStore.Audio.Media.ARTIST_KEY
    private int album_id; // MediaStore.Audio.Media.ALBUM_ID
    private Uri album_img; // Uri.parse("content://media/external/audio/albumart/" + music.album_id);
    private int genre_id;
    private String composer; // MediaStore.Audio.Media.COMPOSER
    private String content_type;
    private int duration; // MediaStore.Audio.Media.DURATION
    private String is_music; // MediaStore.Audio.Media.IS_MUSIC
    private String year; // MediaStore.Audio.Media.YEAR

    // Additional Info.
    private boolean favorite; // 즐겨찾기


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getMusic_uri() {
        return music_uri;
    }

    public void setMusic_uri(Uri music_uri) {
        this.music_uri = music_uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_key() {
        return artist_key;
    }

    public void setArtist_key(String artist_key) {
        this.artist_key = artist_key;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public Uri getAlbum_img() {
        return album_img;
    }

    public void setAlbum_img(Uri album_img) {
        this.album_img = album_img;
    }

    public int getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getIs_music() {
        return is_music;
    }

    public void setIs_music(String is_music) {
        this.is_music = is_music;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}


