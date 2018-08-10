package deus.seow.de.fowtf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.Util;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.DuelDao;
import deus.seow.de.fowtf.db.dao.TournamentDao;
import deus.seow.de.fowtf.db.table.Player;

public class ResultAdapter extends BaseAdapter {

    private DuelDao duelDao;
    private LayoutInflater inflater;
    private int tournamentId;
    private List<Player> players;

    public ResultAdapter(Context context, int tournamentId) {
        duelDao = AppDatabase.getAppDatabase(context).duelDao();
        TournamentDao tournamentDao = AppDatabase.getAppDatabase(context).tournamentDao();
        inflater = LayoutInflater.from(context);
        this.tournamentId = tournamentId;
        players = Util.sortByTB(new CopyOnWriteArrayList<>(tournamentDao.getParticipants(tournamentId)), tournamentId, duelDao);
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView == null ? inflater.inflate(R.layout.item_result, parent, false) : convertView;

        Player player = (Player) getItem(position);

        final TextView tvPlace = view.findViewById(R.id.place);
        tvPlace.setText(String.format("%s.", String.valueOf(position + 1)));
        final TextView tvPlayer = view.findViewById(R.id.player);
        tvPlayer.setText(String.format("%s %s", player.getFirstname(), player.getLastname()));
        final TextView tvTB = view.findViewById(R.id.tiebreak);
        tvTB.setText(Util.calculateTiebreak(player.getId(), tournamentId, duelDao));

        return view;
    }
}
