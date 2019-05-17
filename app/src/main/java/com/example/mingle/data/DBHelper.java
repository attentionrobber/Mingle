package com.example.mingle.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.mingle.domain.Favorite;
import com.example.mingle.domain.Playlist;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "mingle.db";
    private static final int DB_VERSION = 1; // Field 를 수정했을 경우 이것을 바꿔주면 onUpgrade 호출됨.

    private Dao<Favorite, Integer> favoriteDao = null;
    private Dao<Playlist, Integer> playlistDao = null;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Favorite.class);
            TableUtils.createTable(connectionSource, Playlist.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Favorite.class, false); // Table 삭제 후
            TableUtils.dropTable(connectionSource, Playlist.class, false);
            onCreate(database, connectionSource); // Table Create 한다.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Data Access Object
    // DBHelper 를 사용하기 때문에 dao 객체도 열어놓고 사용가능하다.
    public Dao<Favorite, Integer> getFavoriteDao() throws SQLException {
        if(favoriteDao == null) {
            favoriteDao = getDao(Favorite.class);
        }
        return favoriteDao;
    }

    public Dao<Playlist, Integer> getPlaylistDao() throws SQLException {
        if(playlistDao == null) {
            playlistDao = getDao(Playlist.class);
        }
        return playlistDao;
    }

    public void releaseFavoriteDao() {
        if (favoriteDao != null) {
            favoriteDao = null;
        }
    }

    public void releasePlaylistDao() {
        if (playlistDao != null) {
            playlistDao = null;
        }
    }
}
