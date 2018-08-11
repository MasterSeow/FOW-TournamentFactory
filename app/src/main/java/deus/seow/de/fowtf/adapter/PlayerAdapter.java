package deus.seow.de.fowtf.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
    Context context;

    public PlayerAdapter(Context context) {
        playerDao = AppDatabase.getAppDatabase(context).userDao();
        inflater = LayoutInflater.from(context);
        chosenPlayers = new ArrayList<>();
        this.context = context;
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

    boolean longclick = false;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view = convertView == null ? inflater.inflate(R.layout.item_user, parent, false) : convertView;

        final Player player = (Player) getItem(position);

        final TextView id = view.findViewById(R.id.fow_id);

        if (player.getId().length() == 10)
            id.setText(player.getId());
        else {
            id.setText(R.string.missing_id);
        }
        final TextView firstname = view.findViewById(R.id.firstname);
        firstname.setText(player.getFirstname());
        final TextView lastname = view.findViewById(R.id.lastname);
        lastname.setText(player.getLastname());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!longclick)
                    if (chosenPlayers.contains(player)) {
                        chosenPlayers.remove(player);
                        firstname.setTextColor(Color.WHITE);
                        id.setTextColor(Color.WHITE);
                        lastname.setTextColor(Color.WHITE);
                        view.setBackgroundColor(Color.BLACK);
                    } else {
                        chosenPlayers.add(player);
                        firstname.setTextColor(Color.BLACK);
                        lastname.setTextColor(Color.BLACK);
                        id.setTextColor(Color.BLACK);
                        view.setBackgroundColor(Color.GREEN);
                    }
            }
        });

        if (chosenPlayers.contains(player))
            view.setBackgroundColor(Color.GREEN);
        else
            view.setBackgroundColor(Color.BLACK);
        if (playerDao.participatedDuelCount(player.getId()) == 0)
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longclick = true;
                    android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(
                            context);
                    alert.setTitle("Alert!!");
                    alert.setMessage("Are you sure to delete record");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            playerDao.delete(player);
                            notifyDataSetChanged();
                            longclick = false;
                            dialogInterface.dismiss();
                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            longclick = false;
                            dialogInterface.dismiss();
                        }
                    });

                    alert.show();
                    return true;
                }
            });
        return view;
    }

    public List<Player> getChosenPlayers() {
        return chosenPlayers;
    }
}
