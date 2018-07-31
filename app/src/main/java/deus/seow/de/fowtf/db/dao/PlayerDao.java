package deus.seow.de.fowtf.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


import java.util.List;

import deus.seow.de.fowtf.db.table.Player;

@Dao
public interface PlayerDao {

    @Query("SELECT * FROM Player")
    List<Player> getAll();

    @Query("SELECT * FROM Player ORDER BY id DESC LIMIT 1")
    Player getLast();

    @Query("SELECT * FROM Player where id = :id")
    Player findById(String id);

    @Query("SELECT COUNT(*) from Player")
    int countUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Player player);

    @Insert
    void insertAll(Player... players);

    @Delete
    void delete(Player player);
}
