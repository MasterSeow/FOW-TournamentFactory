package deus.seow.de.fowtf.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import deus.seow.de.fowtf.db.dao.DuelDao;
import deus.seow.de.fowtf.db.dao.PlayerDao;
import deus.seow.de.fowtf.db.dao.TournamentDao;
import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.db.table.Tournament;


@Database(entities = {Player.class, Tournament.class, Duel.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tournament_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    @SuppressWarnings("unused")
    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract PlayerDao userDao();

    public abstract DuelDao duelDao();

    public abstract TournamentDao tournamentDao();
}
