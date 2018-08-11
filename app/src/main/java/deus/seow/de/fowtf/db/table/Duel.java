package deus.seow.de.fowtf.db.table;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.OnConflictStrategy;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "duel", primaryKeys = {"tournamentId", "round", "playerOneId", "playerTwoId"},
        foreignKeys = {@ForeignKey(entity = Player.class, parentColumns = "id", childColumns = "playerOneId" ),
                @ForeignKey(entity = Player.class, parentColumns = "id", childColumns = "playerTwoId"),
                @ForeignKey(entity = Tournament.class, parentColumns = "id", childColumns = "tournamentId",onDelete = CASCADE)}
)
public class Duel {


    private final int tournamentId;

    private final int round;

    @NonNull
    private final String playerOneId;

    @NonNull
    private final String playerTwoId;

    private String winner;

    public Duel(int tournamentId, int round, @NonNull String playerOneId, @NonNull String playerTwoId, String winner) {
        this.tournamentId = tournamentId;
        this.round = round;
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.winner = winner;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    @NonNull
    public String getPlayerOneId() {
        return playerOneId;
    }

    @NonNull
    public String getPlayerTwoId() {
        return playerTwoId;
    }

    public String getWinner() {
        return winner;
    }

    public int getRound() {
        return round;
    }

    @Ignore
    @Override
    public String toString() {
        return tournamentId + " " + round + " " + playerOneId + " " + playerTwoId + " " + winner;
    }
}
