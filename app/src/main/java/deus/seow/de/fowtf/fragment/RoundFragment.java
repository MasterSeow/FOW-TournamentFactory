package deus.seow.de.fowtf.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import deus.seow.de.fowtf.Constants;
import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.Tiebreak;
import deus.seow.de.fowtf.adapter.RoundAdapter;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.DuelDao;
import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;

public class RoundFragment extends Fragment {

    public static final String TAG = RoundFragment.class.getSimpleName();
    private final List<Player> players = new ArrayList<>();
    private int round = 1;
    private int tournamentId;
    private int maxRounds;
    private Button previous;
    private Button next;
    private RoundAdapter roundAdapter;
    private DuelDao duelDao;
    private TextView roundText;

    public static RoundFragment newInstance(int tournamentId, int maxRounds, List<Player> players) {
        RoundFragment rf = new RoundFragment();
        rf.maxRounds = maxRounds;
        rf.players.addAll(players);
        rf.tournamentId = tournamentId;
        return rf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_round, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        duelDao = AppDatabase.getAppDatabase(getContext()).duelDao();
        Collections.shuffle(players);
        generateMatches(players);

        roundText = view.findViewById(R.id.round);
        roundText.setText(String.format(Locale.getDefault(), "Round %d:", round));

        roundAdapter = new RoundAdapter(getContext(), tournamentId, round);

        previous = view.findViewById(R.id.prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                round--;
                roundText.setText(String.format(Locale.getDefault(), "Round %d:", round));
                roundAdapter.setRound(round);
                updateButtons();
            }
        });
        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noResults = duelDao.countDuelsWithoutResult(tournamentId, round);
                if (noResults == 0) {
                    round++;
                    roundText.setText(String.format(Locale.getDefault(), "Round %d:", round));
                    generateRound();
                    roundAdapter.setRound(round);
                    updateButtons();
                } else {
                    Toast.makeText(getContext(), String.valueOf(noResults) + " duels left without result", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateButtons();
        ListView duelList = view.findViewById(R.id.list);
        duelList.setAdapter(roundAdapter);
    }

    private void generateRound() {
        if (duelDao.getRoundCount(tournamentId) < round)
            generateMatches(Tiebreak.sortByTB(new CopyOnWriteArrayList<>(players), tournamentId, duelDao));
    }

    private void updateButtons() {
        if (round > 1)
            previous.setEnabled(true);
        else
            previous.setEnabled(false);

        if (round < maxRounds) {
            next.setText(R.string.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int noResults = duelDao.countDuelsWithoutResult(tournamentId, round);
                    if (noResults == 0) {
                        round++;
                        roundText.setText(String.format(Locale.getDefault(), "Round %d:", round));
                        generateRound();
                        roundAdapter.setRound(round);
                        updateButtons();
                    } else {
                        Toast.makeText(getContext(), String.valueOf(noResults) + " duels left without result", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            next.setText(R.string.finish);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragmentManager() != null)
                        getFragmentManager().beginTransaction().replace(R.id.fullscreenContainer, ResultFragment.newInstance(tournamentId), ResultFragment.TAG).commit();
                }
            });
        }
    }

    private void generateMatches(List<Player> playersInOrder) {
        List<Player> cow = new CopyOnWriteArrayList<>(playersInOrder);

        if (cow.size() % 2 == 1) {
            Player freewinner = placeFreeWin(new ArrayList<>(playersInOrder));
            cow.remove(freewinner);
        }
        generateMatchesByRanking(cow);
    }

    private Player placeFreeWin(List<Player> players) {
        List<Player> cow = new CopyOnWriteArrayList<>(players);
        for (Player playerwfw : duelDao.getPlayersWithFreeWins(tournamentId)) {
            for (Player player : cow) {
                if (player.getId().equals(playerwfw.getId()))
                    cow.remove(player);
            }
        }
        Player winner = cow.size() > 0 ? cow.get(cow.size() - 1) : players.get(players.size() - 1);
        duelDao.insert(new Duel(tournamentId, round, winner.getId(), "1", winner.getId()));
        return winner;
    }

    private void generateMatchesByRanking(List<Player> playersInOrder) {
        Player firstPlayer = null;
        for (Player player : playersInOrder) {
            if (firstPlayer == null) {
                firstPlayer = player;
            } else {
                duelDao.insert(new Duel(tournamentId, round, firstPlayer.getId(), player.getId(), Constants.NO_PLAYER_WON));
                firstPlayer = null;
            }
        }

    }
}
