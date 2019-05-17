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

    @DatabaseField
    private String musicUri;

    @DatabaseField
    private boolean favorite;

    public Favorite(String musicUri, boolean favorite) {
        this.musicUri = musicUri;
        this.favorite = favorite;
    }

    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
