package deus.seow.de.fowtf.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.TournamentDao;
import deus.seow.de.fowtf.db.table.Tournament;
import deus.seow.de.fowtf.fragment.ResultFragment;

public class TournamentAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private FragmentManager fragmentManager;
    private TournamentDao tournamentDao;
    Context context;

    public TournamentAdapter(Context context, FragmentManager fragmentManager) {
        tournamentDao = AppDatabase.getAppDatabase(context).tournamentDao();
        inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        this.context = context;
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

        final Tournament tournament = (Tournament) getItem(position);

        TextView date = view.findViewById(R.id.date);
        date.setText(tournament.getDate());
        TextView type = view.findViewById(R.id.type);
        type.setText(tournament.getType());
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        context);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tournamentDao.delete(tournament);
                        notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.show();
                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.fullscreenContainer, ResultFragment.newInstance(tournament.getId()), ResultFragment.TAG).commit();
            }
        });
        return view;
    }


}
