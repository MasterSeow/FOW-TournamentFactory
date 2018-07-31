package deus.seow.de.fowtf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView == null ? inflater.inflate(R.layout.item_round, parent, false) : convertView;

        Duel duel = (Duel) getItem(position);

        Player player1 = duelDao.getPlayer1(tournamentId,round,duel.getPlayerOneId());
        Player player2 = duelDao.getPlayer2(tournamentId,round,duel.getPlayerTwoId());


        TextView tvPlayer1 = view.findViewById(R.id.player1);
        tvPlayer1.setText(String.format("%s %s", player1.getFirstname(), player1.getLastname()));
        TextView tvPlayer2 = view.findViewById(R.id.player2);
        tvPlayer2.setText(String.format("%s %s", player2.getFirstname(), player2.getLastname()));

        return view;
    }

    public void setRound(int round){
        this.round = round;
        notifyDataSetChanged();
    }
}
