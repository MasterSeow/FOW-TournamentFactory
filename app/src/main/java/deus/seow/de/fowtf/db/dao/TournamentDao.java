package deus.seow.de.fowtf.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.db.table.Tournament;

@SuppressWarnings("unused")
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

    @Query("SELECT id,firstname,lastname FROM duel LEFT JOIN player ON playerOneId = id WHERE tournamentId = :tournamentId AND NOT playerOneId = '1' AND round = 1 UNION SELECT id,firstname,lastname FROM duel LEFT JOIN player ON playerTwoId = id WHERE tournamentId = :tournamentId AND round = 1  AND NOT playerTwoId = '1'")
    List<Player> getParticipants(int tournamentId);

    @Insert
    void insertAll(Tournament... tournaments);

    @Delete
    void delete(Tournament tournament);
}
