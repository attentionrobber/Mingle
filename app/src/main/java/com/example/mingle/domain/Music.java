package com.example.mingle.domain;


public class Music extends Common {

    // Music Info.
    private String path; // MediaStore.Audio.Media.DATA
    private String musicUri; // Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media._ID).toString();
    private String title; // MediaStore.Audio.Media.TITLE
    private int artist_id; // MediaStore.Audio.Media.ARTIST_ID
    private String artist; // MediaStore.Audio.Media.ARTIST
    private String artist_key; // MediaStore.Audio.Media.ARTIST_KEY
    private int album_id; // MediaStore.Audio.Media.ALBUM_ID
    private String album; // MediaStore.Audio.Media.ALBUM
    private String albumImgUri; // Uri.parse("content://media/external/audio/albumart/" + music.album_id);
    private String composer; // MediaStore.Audio.Media.COMPOSER
    private String year; // MediaStore.Audio.Media.YEAR
    private int duration; // MediaStore.Audio.Media.DURATION
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

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String getAlbumImgUri() {
        return albumImgUri;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getIs_music() {
        return isMusic;
    }

    public void setIsMusic(String is_music) {
        this.isMusic = is_music;
    }

}