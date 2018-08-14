package deus.seow.de.fowtf.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import deus.seow.de.fowtf.MainActivity;
import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.adapter.TournamentAdapter;

public class OverviewFragment extends Fragment {

    public static final String TAG = OverviewFragment.class.getSimpleName();

    private TournamentAdapter tournamentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ListView tournamentList = view.findViewById(R.id.list);
        tournamentAdapter = new TournamentAdapter(getContext(), getFragmentManager());
        tournamentList.setAdapter(tournamentAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.backup, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Activity activity = getActivity();
        Context context = getContext();

        if (activity != null && context != null)
            switch (item.getItemId()) {
                case R.id.backup_save:
                    MainActivity.save = true;
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MainActivity.MY_WRITE_EXTERNAL_STORAGE);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        activity.startActivityForResult(intent, MainActivity.RESULT_SAVE_PATH);
                    }
                    return true;
                case R.id.backup_load:
                    MainActivity.save = false;
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MainActivity.MY_WRITE_EXTERNAL_STORAGE);

                    } else {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("*/*");
                        activity.startActivityForResult(intent, MainActivity.RESULT_LOAD_PATH);
                    }
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        else return super.onOptionsItemSelected(item);
    }

    public void onBackupLoaded(){
        tournamentAdapter.notifyDataSetChanged();
    }
}
