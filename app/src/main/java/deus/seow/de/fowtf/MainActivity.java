package deus.seow.de.fowtf;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.fragment.CreationFragment;
import deus.seow.de.fowtf.fragment.MainFragment;
import deus.seow.de.fowtf.fragment.OverviewFragment;
import deus.seow.de.fowtf.fragment.ResultFragment;

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

        appDatabase.userDao().insert(new Player("10", "Seow", "Deus"));
        appDatabase.userDao().insert(new Player("11", "Nico", "Fenchel"));
        appDatabase.userDao().insert(new Player("12", "Jonathan", "Dante"));
        appDatabase.userDao().insert(new Player("13", "Thomas", "Brunnen"));
        appDatabase.userDao().insert(new Player("14", "Felix", "Weser"));
        appDatabase.userDao().insert(new Player("15", "Vieh", "Tee"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleBackKey();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void handleBackKey() {
        if (fragmentManager.findFragmentByTag(CreationFragment.TAG) != null)
            fragmentManager.beginTransaction().replace(R.id.fullscreenContainer, new MainFragment(), MainFragment.TAG).commit();
        if (fragmentManager.findFragmentByTag(OverviewFragment.TAG) != null)
            fragmentManager.beginTransaction().replace(R.id.fullscreenContainer, new MainFragment(), MainFragment.TAG).commit();
        if (fragmentManager.findFragmentByTag(ResultFragment.TAG) != null)
            fragmentManager.beginTransaction().replace(R.id.fullscreenContainer, new OverviewFragment(), OverviewFragment.TAG).commit();
    }
}
