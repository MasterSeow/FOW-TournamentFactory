package deus.seow.de.fowtf.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import deus.seow.de.fowtf.Constants;
import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.DuelDao;
import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;

public class RoundAdapter extends BaseAdapter {

    private DuelDao duelDao;
    private LayoutInflater inflater;
    private int tournamentId;
    private int round;

    public RoundAdapter(Context context, int TournamentId, int round) {
        duelDao = AppDatabase.getAppDatabase(context).duelDao();
        inflater = LayoutInflater.from(context);
        tournamentId = TournamentId;
        this.round = round;
    }

    @Override
    public int getCount() {
        return duelDao.countDuels(tournamentId, round);
    }

    @Override
    public Object getItem(int position) {
        return duelDao.getRound(tournamentId, round).get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView == null ? inflater.inflate(R.layout.item_round, parent, false) : convertView;

        Duel duel = (Duel) getItem(position);

        Player player1 = duelDao.getPlayer1(tournamentId, round, duel.getPlayerOneId());
        Player player2 = duelDao.getPlayer2(tournamentId, round, duel.getPlayerTwoId());

        final String p1Id = player1.getId();
        final String p2Id = player2.getId();
        final TextView tvPlayer1 = view.findViewById(R.id.player1);
        tvPlayer1.setText(String.format("%s %s", player1.getFirstname(), player1.getLastname()));

        final TextView tvPlayer2 = view.findViewById(R.id.player2);
        tvPlayer2.setText(String.format("%s %s", player2.getFirstname(), player2.getLastname()));
        tvPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String winner = duelDao.getRound(tournamentId, round).get(position).getWinner();
                switch (winner) {
                    case Constants.DRAW:
                        tvPlayer1.setBackgroundColor(Color.GREEN);
                        tvPlayer2.setBackgroundColor(Color.RED);
                        winner = p1Id;
                        break;
                    case Constants.NO_PLAYER_WON:
                        tvPlayer1.setBackgroundColor(Color.RED);
                        tvPlayer2.setBackgroundColor(Color.GREEN);
                        winner = p2Id;
                        break;
                    default:
                        if (winner.equals(p2Id)) {
                            winner = Constants.NO_PLAYER_WON;
                            tvPlayer1.setBackgroundColor(Color.WHITE);
                            tvPlayer2.setBackgroundColor(Color.WHITE);
                        } else {
                            winner = Constants.DRAW;
                            tvPlayer1.setBackgroundColor(Color.CYAN);
                            tvPlayer2.setBackgroundColor(Color.CYAN);
                        }

                }
                duelDao.insert(new Duel(tournamentId, round,p1Id, p2Id, winner));
            }
        });
        tvPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String winner = duelDao.getRound(tournamentId, round).get(position).getWinner();
                switch (winner) {
                    case Constants.DRAW:
                        tvPlayer1.setBackgroundColor(Color.RED);
                        tvPlayer2.setBackgroundColor(Color.GREEN);
                        winner = p2Id;
                        break;
                    case Constants.NO_PLAYER_WON:
                        tvPlayer1.setBackgroundColor(Color.GREEN);
                        tvPlayer2.setBackgroundColor(Color.RED);
                        winner = p1Id;
                        break;
                    default:
                        if (winner.equals(p1Id)) {
                            tvPlayer1.setBackgroundColor(Color.WHITE);
                            tvPlayer2.setBackgroundColor(Color.WHITE);
                            winner = Constants.NO_PLAYER_WON;
                        } else {
                            tvPlayer1.setBackgroundColor(Color.CYAN);
                            tvPlayer2.setBackgroundColor(Color.CYAN);
                            winner = Constants.DRAW;
                        }
                }
                duelDao.insert(new Duel(tournamentId, round,p1Id, p2Id, winner));
            }
        });
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

    public void setRound(int round) {
        this.round = round;
        notifyDataSetChanged();
    }

    public class MyOnClickListener implements View.OnClickListener
    {

        String winner;
        public MyOnClickListener(String winner) {
            this.winner = winner;
        }

        @Override
        public void onClick(View v)
        {
            //read your lovely variable
        }

    }
}
