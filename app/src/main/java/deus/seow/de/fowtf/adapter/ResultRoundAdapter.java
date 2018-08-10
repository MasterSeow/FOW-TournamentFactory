package deus.seow.de.fowtf.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import deus.seow.de.fowtf.Constants;
import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.DuelDao;
import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;

public class ResultRoundAdapter extends BaseAdapter {

    private DuelDao duelDao;
    private LayoutInflater inflater;
    private int tournamentId;
    private int round = 0;
    private List<Integer> roundMarker;

    public ResultRoundAdapter(Context context, int tournamentId) {
        duelDao = AppDatabase.getAppDatabase(context).duelDao();
        inflater = LayoutInflater.from(context);
        roundMarker =new ArrayList<>();
        this.tournamentId = tournamentId;
    }

    @Override
    public int getCount() {
        return duelDao.getAll(tournamentId).size();
    }

    @Override
    public Object getItem(int position) {
        return duelDao.getAll(tournamentId).get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView == null ? inflater.inflate(R.layout.item_round, parent, false) : convertView;

        Duel duel = (Duel) getItem(position);

        Player player1 = duelDao.getPlayer1(tournamentId, duel.getRound(), duel.getPlayerOneId());
        Player player2 = duelDao.getPlayer2(tournamentId, duel.getRound(), duel.getPlayerTwoId());

        final String p1Id = player1.getId();

        final TextView roundTitle = view.findViewById(R.id.roundTitle);
        if(duel.getRound()>round)
        {
            roundMarker.add(position);
            round++;
        }
        if(roundMarker.contains(position))
            roundTitle.setVisibility(View.VISIBLE);
        else
            roundTitle.setVisibility(View.GONE);
        roundTitle.setText("Round " + String.valueOf(duel.getRound())+":");

        final TextView tvPlayer1 = view.findViewById(R.id.player1);
        tvPlayer1.setText(String.format("%s %s", player1.getFirstname(), player1.getLastname()));

        final TextView tvPlayer2 = view.findViewById(R.id.player2);
        tvPlayer2.setText(String.format("%s %s", player2.getFirstname(), player2.getLastname()));

        String winner = duel.getWinner();
        switch (duel.getWinner()) {
            case Constants.DRAW:
                tvPlayer1.setBackgroundColor(Color.CYAN);
                tvPlayer2.setBackgroundColor(Color.CYAN);
                break;
            case Constants.NO_PLAYER_WON:
                tvPlayer1.setBackgroundColor(Color.WHITE);
                tvPlayer2.setBackgroundColor(Color.WHITE);
                break;
            default:
                if (winner.equals(p1Id)) {
                    tvPlayer1.setBackgroundColor(Color.GREEN);
                    tvPlayer2.setBackgroundColor(Color.RED);
                } else {
                    tvPlayer1.setBackgroundColor(Color.RED);
                    tvPlayer2.setBackgroundColor(Color.GREEN);
                }
        }

        return view;
    }
}
