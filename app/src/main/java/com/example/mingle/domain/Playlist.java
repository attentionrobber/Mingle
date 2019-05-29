package com.example.mingle.domain;

public class Playlist {

    private String id; // MediaStore.Audio.Playlists._ID
    private String path; // MediaStore.Audio.Playlists.DATA
    private String title; // MediaStore.Audio.Playlists.NAME
    private String dateAdded; // MediaStore.Audio.Playlists.DATE_ADDED
    private String dateModified; // MediaStore.Audio.Playlists.DATE_MODIFIED


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
}
