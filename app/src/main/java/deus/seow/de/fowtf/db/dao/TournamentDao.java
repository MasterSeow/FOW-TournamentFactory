package deus.seow.de.fowtf.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import deus.seow.de.fowtf.db.table.Tournament;

@Dao
public interface TournamentDao {

    @Query("SELECT * FROM tournament")
    List<Tournament> getAll();

    @Query("SELECT * FROM tournament ORDER BY id DESC LIMIT 1")
    Tournament getLast();

    @Query("SELECT * FROM tournament where id = :id")
    Tournament findById(int id);

    @Query("SELECT COUNT(*) from tournament")
    int countTournaments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tournament tournament);

    @Insert
    void insertAll(Tournament... tournaments);

    @Delete
    void delete(Tournament tournament);
}
