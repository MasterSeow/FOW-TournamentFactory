package deus.seow.de.fowtf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.TournamentDao;
import deus.seow.de.fowtf.db.table.Duel;
import deus.seow.de.fowtf.db.table.Player;
import deus.seow.de.fowtf.db.table.Tournament;

public class TournamentAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private TournamentDao tournamentDao;

    public TournamentAdapter(Context context) {
        tournamentDao = AppDatabase.getAppDatabase(context).tournamentDao();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tournamentDao.countTournaments();
    }

    @Override
    public Object getItem(int position) {
        return tournamentDao.getAll().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView == null ? inflater.inflate(R.layout.item_tournament, parent, false) : convertView;

        Tournament tournament = (Tournament) getItem(position);

        TextView date = view.findViewById(R.id.date);
        date.setText(tournament.getDate());
        TextView type = view.findViewById(R.id.type);
        type.setText(tournament.getType());

        return view;
    }


}
