package deus.seow.de.fowtf.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.db.AppDatabase;
import deus.seow.de.fowtf.db.dao.PlayerDao;
import deus.seow.de.fowtf.db.table.Player;

public class NewPlayerDialog extends DialogFragment {

    public static final String TAG = NewPlayerDialog.class.getSimpleName();
    String firstname = "";
    String lastname = "";
    String fowId = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText editFirstname = view.findViewById(R.id.firstname);
        editFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                firstname = s.toString();
            }
        });
        final EditText editLastname = view.findViewById(R.id.lastname);
        editLastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lastname = s.toString();
            }
        });
        final EditText editFowId = view.findViewById(R.id.fow_id);
        editFowId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fowId = s.toString();
            }
        });
        Button submit = view.findViewById(R.id.submit);
        final NewPlayerDialog thisFragment = this;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName(firstname)) {
                    Toast.makeText(getContext(), "Check firstname input" + firstname, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validateName(lastname)) {
                    Toast.makeText(getContext(), "Check lastname input" + lastname, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fowId.equals(""))
                    fowId = generatePseudoId();
                else if (!validateId(fowId)) {
                    Toast.makeText(getContext(), "Check FOW-ID" + fowId, Toast.LENGTH_SHORT).show();
                    return;
                }

                AppDatabase.getAppDatabase(getContext()).userDao().insert(new Player(fowId, firstname, lastname));
                System.out.println("saved new Player");
                if (getFragmentManager() != null)
                    getFragmentManager().beginTransaction().remove(thisFragment).commit();

            }
        });
    }

    private String generatePseudoId() {
        PlayerDao playerDao = AppDatabase.getAppDatabase(getContext()).userDao();
        String pseudo = "";
        boolean foundPseudoId = false;
        int idCounter = 3;
        while (!foundPseudoId) {
            idCounter++;
            pseudo = String.valueOf(idCounter);
            if (playerDao.findById(pseudo) == null)
                foundPseudoId = true;
        }
        return pseudo;
    }

    private boolean validateId(String fowId) {
        Pattern idPattern = Pattern.compile("\\p{Digit}{10}");

        return idPattern.matcher(fowId).matches() && AppDatabase.getAppDatabase(getContext()).userDao().findById(fowId) == null;
    }

    @SuppressWarnings("all")
    private boolean validateName(String name) {
        Pattern namePattern = Pattern.compile("[a-zA-z]+([ '-][a-zA-Z]+)*");
        return namePattern.matcher(name).matches();
    }
}
