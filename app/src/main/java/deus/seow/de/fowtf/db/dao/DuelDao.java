package deus.seow.de.fowtf.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;

@SuppressWarnings("unused")
@Dao
public interface DuelDao {

    @Query("SELECT * FROM duel WHERE tournamentId = :tournamentId")
    List<Duel> getAll(int tournamentId);

    @Query("SELECT COUNT(*) from duel WHERE tournamentId = :tournamentId AND round = :round")
    int countDuels(int tournamentId, int round);

    @Query("SELECT * from duel WHERE tournamentId = :tournamentId AND round = :round AND ((playerTwoId = :playerTwoId AND playerOneId = :playerOneId)OR(playerOneId = :playerTwoId AND playerTwoId = :playerOneId))")
    Duel getDuel(int tournamentId, int round, String playerOneId, String playerTwoId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Duel duel);

    @Insert
    void insertAll(Duel... duels);

    @Delete
    void delete(Duel duel);

    @Query("SELECT * from duel WHERE tournamentId = :tournamentId AND round = :round")
    List<Duel> getRound(int tournamentId, int round);

    @Query("SELECT COUNT(*) from duel WHERE tournamentId = :tournamentId AND round = :round AND winner ='none'")
    int countDuelsWithoutResult(int tournamentId, int round);

    @Query("SELECT id,firstname,lastname FROM duel LEFT JOIN player ON playerOneId = id WHERE tournamentId = :tournamentId AND round = :round AND playerOneId = :playerId")
    Player getPlayer1(int tournamentId, int round, String playerId);

    @Query("SELECT id,firstname,lastname FROM duel LEFT JOIN player ON playerTwoId = id WHERE tournamentId = :tournamentId AND round = :round AND playerTwoId = :playerId")
    Player getPlayer2(int tournamentId, int round, String playerId);

    @Query("SELECT round FROM duel WHERE (playerOneId = :playerId OR playerTwoId = :playerId) AND NOT(winner = :playerId OR winner = 'draw' OR winner = 'none')AND tournamentId = :tournamentId")
    List<Integer> getLostRounds(String playerId, int tournamentId);

    @Query("SELECT COUNT(*) FROM duel WHERE tournamentId = :tournamentId AND(playerOneId = :playerId OR playerTwoId = :playerId) AND winner = :playerId")
    int getWonRoundCount(String playerId, int tournamentId);

    @Query("SELECT COUNT(*) FROM duel WHERE tournamentId = :tournamentId AND(playerOneId = :playerId OR playerTwoId = :playerId) AND winner = 'draw'")
    int getDrawRoundCount(String playerId, int tournamentId);

    @Query("SELECT COUNT(DISTINCT round) FROM duel WHERE tournamentId = :tournamentId")
    int getRoundCount(int tournamentId);

    @Query("SELECT id,firstname,lastname FROM duel LEFT JOIN player ON playerOneId = id WHERE tournamentId = :tournamentId AND playerTwoId = '1'")
    List<Player> getPlayersWithFreeWins(int tournamentId);

    @Query("SELECT id,firstname,lastname FROM duel LEFT JOIN player ON CASE playerOneId WHEN :playerId THEN playerTwoId = id ELSE playerOneId = id END WHERE tournamentId = :tournamentId AND round = :round AND(playerOneId = :playerId OR playerTwoId = :playerId)")
    Player getOpponent(int tournamentId, int round, String playerId);

    @Query("SELECT id,firstname,lastname FROM duel LEFT JOIN player ON CASE playerOneId WHEN :playerId THEN playerTwoId = id ELSE playerOneId = id END WHERE tournamentId = :tournamentId AND (playerOneId = :playerId OR playerTwoId = :playerId)")
    List<Player> getOpponents(int tournamentId, String playerId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Duel duel);
}
