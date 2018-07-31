package deus.seow.de.fowtf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.PlayerDao;
import deus.seow.de.fowtf.db.table.Player;

public class PlayerAdapter extends BaseAdapter {
    PlayerDao playerDao;
    LayoutInflater inflater;

    //TODO
    public interface Callback{
        void addPlayer(Player player);
        void removePlayer(Player player);
    }

    public PlayerAdapter(Context context) {
        playerDao = AppDatabase.getAppDatabase(context).userDao();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return playerDao.countUsers();
    }

    @Override
    public Object getItem(int position) {
        return playerDao.getAll().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView == null ? inflater.inflate(R.layout.item_user, parent, false) : convertView;

        Player player = (Player) getItem(position);

        TextView id = view.findViewById(R.id.fow_id);
        if (player.getId().length() == 10)
            id.setText(player.getId());
        else {
            id.setText(R.string.missing_id);
        }
        TextView firstname = view.findViewById(R.id.firstname);
        firstname.setText(player.getFirstname());
        TextView lastname = view.findViewById(R.id.lastname);
        lastname.setText(player.getLastname());

        return view;
    }


}
