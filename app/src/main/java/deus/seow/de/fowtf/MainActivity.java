package deus.seow.de.fowtf;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fullscreenContainer, new MainFragment(), MainFragment.TAG).commit();
        createDummyData(AppDatabase.getAppDatabase(this));
    }

    private void createDummyData(AppDatabase appDatabase) {
        appDatabase.userDao().insert(new Player("1", "Frei", "Los"));

        appDatabase.userDao().insert(new Player("10", "Christian", "Redox"));
        appDatabase.userDao().insert(new Player("11", "Nico", "Fenchel"));
        appDatabase.userDao().insert(new Player("12", "Jonathan", "Dante"));
        appDatabase.userDao().insert(new Player("13", "Thomas", "Brunnen"));
        appDatabase.userDao().insert(new Player("14", "Felix", "Weser"));
        appDatabase.userDao().insert(new Player("15", "Vieh", "Tee"));
    }


}
