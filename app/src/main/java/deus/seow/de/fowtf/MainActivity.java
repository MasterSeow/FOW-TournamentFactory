package deus.seow.de.fowtf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.fragment.CreationFragment;
import deus.seow.de.fowtf.fragment.MainFragment;
import deus.seow.de.fowtf.fragment.OverviewFragment;
import deus.seow.de.fowtf.fragment.ResultFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    public static final int MY_WRITE_EXTERNAL_STORAGE = 102;
    public static final int RESULT_LOAD_PATH = 103;
    public static boolean save = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fullscreenContainer, new MainFragment(), MainFragment.TAG).commit();
        createDummyData(AppDatabase.getAppDatabase(this));
    }

    private void createDummyData(AppDatabase appDatabase) {
        appDatabase.playerDao().insert(new Player("1", "Frei", "Los"));

//        appDatabase.playerDao().insert(new Player("10", "Seow", "Deus"));
//        appDatabase.playerDao().insert(new Player("11", "Nico", "Fenchel"));
//        appDatabase.playerDao().insert(new Player("12", "Jonathan", "Dante"));
//        appDatabase.playerDao().insert(new Player("13", "Thomas", "Brunnen"));
//        appDatabase.playerDao().insert(new Player("14", "Felix", "Weser"));
//        appDatabase.playerDao().insert(new Player("15", "Vieh", "Tee"));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_WRITE_EXTERNAL_STORAGE:

                if (save)
                    Backup.createDbBackupFile( AppDatabase.getAppDatabase(this));
                else
                    Backup.loadDbBackupFile( AppDatabase.getAppDatabase(this));
                break;
        }
    }
}
