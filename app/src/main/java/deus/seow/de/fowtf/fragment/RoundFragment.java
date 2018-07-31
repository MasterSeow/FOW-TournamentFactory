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

import java.util.List;

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.adapter.PlayerAdapter;
import deus.seow.de.fowtf.adapter.RoundAdapter;
import deus.seow.de.fowtf.db.table.Player;

public class RoundFragment extends Fragment implements PlayerAdapter.Callback{

    public static final String TAG = RoundFragment.class.getSimpleName();
    private int round = 1;
    private int tournamentId;
    private int maxRounds;
    private Button previous;
    private Button next;
    private RoundAdapter roundAdapter;
    private List<Player> players;

    public static RoundFragment newInstance(int tournamentId, int maxRounds, List<Player> players){
        RoundFragment rf = new RoundFragment();
        rf.maxRounds = maxRounds;
        rf.players = players;
        rf.tournamentId = tournamentId;
        return rf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_round,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        roundAdapter = new RoundAdapter(getContext(),tournamentId,round);

        previous = view.findViewById(R.id.prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                round--;
                roundAdapter.setRound(round);
                updateButtons();
            }
        });
        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                round++;
                roundAdapter.setRound(round);
                updateButtons();
            }
        });
        updateButtons();
        ListView duelList = view.findViewById(R.id.list);
        duelList.setAdapter(roundAdapter);
    }

    private void updateButtons(){
        if(round > 1)
            previous.setEnabled(true);
        else
            previous.setEnabled(false);

        if(round<maxRounds)
            next.setEnabled(true);
        else
            next.setEnabled(false);
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);
    }
}
