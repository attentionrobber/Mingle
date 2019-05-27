package com.example.mingle.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Music 별표시 Favorite 체크
 */
@DatabaseTable(tableName = "favorite")
public class Favorite extends Music {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true)
    private String musicUri;

    @DatabaseField
    private String path;

    @DatabaseField
    private String title;

    @DatabaseField
    private String artist;

    @DatabaseField
    private String album;

    @DatabaseField
    private String albumImgUri;

    // 있어야함. 그래야 DBHelper 에서 getDao 가능.
    public Favorite() {

    }

    public Favorite(Music music) {
        musicUri = music.getMusicUri();
        path = music.getPath();
        title = music.getTitle();
        artist = music.getArtist();
        album = music.getAlbum();
        albumImgUri = music.getAlbumImgUri();
    }

    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
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
}
