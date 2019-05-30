package com.example.mingle.domain;

public class Artist extends Music {

    private long artist_id; // MediaStore.Audio.Media.ARTIST_ID
    private String artist; // MediaStore.Audio.Media.ARTIST
    private long album_id; // MediaStore.Audio.Media.ALBUM_ID
    private String album; // MediaStore.Audio.Media.ALBUM
    private String albumImgUri; // Uri.parse("content://media/external/audio/albumart/" + music.album_id);
    private int numOfAlbums; // MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
    private int numOfTracks; // MediaStore.Audio.Artists.NUMBER_OF_TRACKS


    @Override
    public long getArtist_id() {
        return artist_id;
    }

    @Override
    public void setArtist_id(long artist_id) {
        this.artist_id = artist_id;
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
    public long getAlbum_id() {
        return album_id;
    }

    @Override
    public void setAlbum_id(long album_id) {
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

    public int getNumOfAlbums() {
        return numOfAlbums;
    }

    public void setNumOfAlbums(int numOfAlbums) {
        this.numOfAlbums = numOfAlbums;
    }

    public int getNumOfTracks() {
        return numOfTracks;
    }

    public void setNumOfTracks(int numOfTracks) {
        this.numOfTracks = numOfTracks;
    }
}
