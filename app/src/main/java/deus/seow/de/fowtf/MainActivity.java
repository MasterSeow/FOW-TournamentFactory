package deus.seow.de.fowtf;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.db.table.Tournament;
import deus.seow.de.fowtf.fragment.MainFragment;
import deus.seow.de.fowtf.db.AppDatabase;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fullscreenContainer,new MainFragment(),MainFragment.TAG).commit();
        createDummyData(AppDatabase.getAppDatabase(this));
    }

    private void createDummyData(AppDatabase appDatabase) {
        appDatabase.userDao().insert(new Player("10","Dummy","Dummy"));
        appDatabase.userDao().insert(new Player("11","Dummy2","Dummy2"));
        appDatabase.tournamentDao().insert(new Tournament(1,"Local","22.07.2018"));
        appDatabase.duelDao().insert(new Duel(1,1,"10","11","none"));

    }


}
