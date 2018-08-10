package deus.seow.de.fowtf.db.table;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tournament")
public class Tournament {

    @PrimaryKey(autoGenerate = true)
    private final int id;

    private final String type;

    private final String date;

    @Ignore
    public Tournament(String type, String date) {
        this(0, type, date);
    }

    public Tournament(int id, String type, String date) {
        this.id = id;
        this.type = type;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }
}
