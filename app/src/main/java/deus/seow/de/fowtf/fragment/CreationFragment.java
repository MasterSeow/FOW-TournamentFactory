package deus.seow.de.fowtf.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.adapter.PlayerAdapter;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.TournamentDao;
import deus.seow.de.fowtf.db.table.Tournament;
import deus.seow.de.fowtf.fragment.dialog.NewPlayerDialog;

public class CreationFragment extends Fragment {

    public static final String TAG = CreationFragment.class.getSimpleName();

    EditText dateText;
    int numberOfRounds = 1;
    String type = "Local";
    String dateString;
    Calendar myCalendar = Calendar.getInstance();
    TournamentDao tournamentDao;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar.getTime());
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_creation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tournamentDao = AppDatabase.getAppDatabase(getContext()).tournamentDao();
        Button buttonNew = view.findViewById(R.id.button_new);
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null)
                    new NewPlayerDialog().show(getFragmentManager(), NewPlayerDialog.TAG);
            }
        });
        dateText = view.findViewById(R.id.date);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null)
                    new DatePickerDialog(getContext(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        updateLabel(new Date(System.currentTimeMillis()));

        final TextView textRounds = view.findViewById(R.id.textround);
        SeekBar rounds = view.findViewById(R.id.rounds);


        textRounds.setText(String.valueOf(1));
        rounds.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress++;
                textRounds.setText(String.valueOf(progress));
                numberOfRounds = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rounds.setProgress(3);

        final Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter;
        if (getContext() != null) {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.tournamentType, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (String) spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ListView playerList = view.findViewById(R.id.list);
        final PlayerAdapter playerAdapter = new PlayerAdapter(getContext());
        playerList.setAdapter(playerAdapter);
        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerAdapter.getChosenPlayers().size() > 0) {
                    tournamentDao.insert(new Tournament(type, dateString));
                    if (getFragmentManager() != null)
                        getFragmentManager().beginTransaction().replace(R.id.fullscreenContainer, RoundFragment.newInstance(tournamentDao.getLast().getId(), numberOfRounds, playerAdapter.getChosenPlayers()), RoundFragment.TAG).commit();
                } else {
                    Toast.makeText(getContext(), "No Players selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLabel(Date date) {
        String myFormat = "dd.MM.yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dateString = sdf.format(date);
        dateText.setText(dateString);
    }

}
