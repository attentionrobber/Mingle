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

    // 있어야함. 그래야 DBHelper 에서 getDao 가능.
    public Favorite() {

    }

    public Favorite(String musicUri) {
        this.musicUri = musicUri;
    }

    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

}
