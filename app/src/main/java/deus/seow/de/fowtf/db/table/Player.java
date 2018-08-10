package deus.seow.de.fowtf.db.table;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "player")
public class Player {

    @NonNull
    @PrimaryKey
    private final String id;

    private final String firstname;

    private final String lastname;

    public Player(@NonNull String id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
