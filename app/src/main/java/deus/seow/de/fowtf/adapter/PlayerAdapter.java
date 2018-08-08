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

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.PlayerDao;
import deus.seow.de.fowtf.db.table.Player;

public class PlayerAdapter extends BaseAdapter {
    private PlayerDao playerDao;
    private LayoutInflater inflater;
    private List<Player> chosenPlayers;

    public PlayerAdapter(Context context) {
        playerDao = AppDatabase.getAppDatabase(context).userDao();
        inflater = LayoutInflater.from(context);
        chosenPlayers = new ArrayList<>();
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

        final View view = convertView == null ? inflater.inflate(R.layout.item_user, parent, false) : convertView;

        final Player player = (Player) getItem(position);

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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosenPlayers.contains(player)) {
                    chosenPlayers.remove(player);
                    view.setBackgroundColor(Color.WHITE);
                }else {
                    chosenPlayers.add(player);
                    view.setBackgroundColor(Color.GREEN);
                }
            }
        });

        if(chosenPlayers.contains(player))
            view.setBackgroundColor(Color.GREEN);
        else
            view.setBackgroundColor(Color.WHITE);

        return view;
    }

    public List<Player> getChosenPlayers() {
        return chosenPlayers;
    }
}
