package com.example.mingle.domain;

public class Artist extends Music {

    private int artist_id; // MediaStore.Audio.Media.ARTIST_ID
    private String artist; // MediaStore.Audio.Media.ARTIST
    private int album_id; // MediaStore.Audio.Media.ALBUM_ID
    private String album; // MediaStore.Audio.Media.ALBUM
    private String albumImgUri; // Uri.parse("content://media/external/audio/albumart/" + music.album_id);
    private int songCount;
    private int albumCount;


    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(int albumCount) {
        this.albumCount = albumCount;
    }

    @Override
    public String getPath() {
        return super.getPath();
    }

    @Override
    public void setPath(String path) {
        super.setPath(path);
    }

    @Override
    public String getMusicUri() {
        return super.getMusicUri();
    }

    @Override
    public void setMusicUri(String musicUri) {
        super.setMusicUri(musicUri);
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public long getArtist_id() {
        return super.getArtist_id();
    }

    @Override
    public void setArtist_id(long artist_id) {
        super.setArtist_id(artist_id);
    }

    @Override
    public String getArtist() {
        return super.getArtist();
    }

    @Override
    public void setArtist(String artist) {
        super.setArtist(artist);
    }

    @Override
    public long getAlbum_id() {
        return super.getAlbum_id();
    }

    @Override
    public void setAlbum_id(long album_id) {
        super.setAlbum_id(album_id);
    }

    @Override
    public String getAlbum() {
        return super.getAlbum();
    }

    @Override
    public void setAlbum(String album) {
        super.setAlbum(album);
    }

    @Override
    public String getAlbumImgUri() {
        return super.getAlbumImgUri();
    }

    @Override
    public void setAlbumImgUri(String albumImgUri) {
        super.setAlbumImgUri(albumImgUri);
    }

    @Override
    public String getComposer() {
        return super.getComposer();
    }

    @Override
    public void setComposer(String composer) {
        super.setComposer(composer);
    }

    @Override
    public String getYear() {
        return super.getYear();
    }

    @Override
    public void setYear(String year) {
        super.setYear(year);
    }

    @Override
    public long getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(long duration) {
        super.setDuration(duration);
    }

    @Override
    public String getIsMusic() {
        return super.getIsMusic();
    }

    @Override
    public void setIsMusic(String is_music) {
        super.setIsMusic(is_music);
    }
}
