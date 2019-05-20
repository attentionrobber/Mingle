package com.example.mingle.domain;

public class Album extends Music {

    private int album_id; // MediaStore.Audio.Media.ALBUM_ID
    private String album; // MediaStore.Audio.Media.ALBUM
    private String albumImgUri; // Uri.parse("content://media/external/audio/albumart/" + music.album_id);
    private String artist; // MediaStore.Audio.Media.ARTIST
    private int artist_id; // MediaStore.Audio.Media.ARTIST_ID
    private String title; // MediaStore.Audio.Media.TITLE
    private int count;

    @Override
    public int getAlbum_id() {
        return album_id;
    }

    @Override
    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    @Override
    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String getAlbumImgUri() {
        return albumImgUri;
    }

    @Override
    public void setAlbumImgUri(String albumImgUri) {
        this.albumImgUri = albumImgUri;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public int getArtist_id() {
        return artist_id;
    }

    @Override
    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
