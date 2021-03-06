package deus.seow.de.fowtf.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import deus.seow.de.fowtf.db.table.Player;

@SuppressWarnings("unused")
@Dao
public interface PlayerDao {

    @Query("SELECT * FROM Player WHERE NOT id = '1'")
    List<Player> getAll();

    @Query("SELECT * FROM Player")
    List<Player> getAllInc();

    @Query("SELECT * FROM Player ORDER BY id DESC LIMIT 1")
    Player getLast();

    @Query("SELECT * FROM Player where id = :id")
    Player findById(String id);

    @Query("SELECT COUNT(*) from Player WHERE NOT id = '1'")
    int countUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Player player);

    @Query("SELECT COUNT(*) FROM duel WHERE playerOneId = :playerId OR playerTwoId = :playerId")
    int participatedDuelCount(String playerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Player> players);

    @Delete
    void delete(Player player);
}
