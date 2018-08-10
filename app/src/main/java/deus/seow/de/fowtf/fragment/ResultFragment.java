package deus.seow.de.fowtf.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import deus.seow.de.fowtf.R;
import deus.seow.de.fowtf.adapter.ResultAdapter;
import deus.seow.de.fowtf.adapter.ResultRoundAdapter;

public class ResultFragment extends Fragment {
    public static final String TAG = ResultFragment.class.getSimpleName();

    int tournamentId;
    ResultAdapter resultAdapter;
    ResultRoundAdapter resultRoundAdapter;

    public static ResultFragment newInstance(int tournamentid) {

        ResultFragment fragment = new ResultFragment();
        fragment.tournamentId = tournamentid;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultAdapter = new ResultAdapter(getContext(), tournamentId);
        resultRoundAdapter = new ResultRoundAdapter(getContext(), tournamentId);

        final ListView list = view.findViewById(R.id.list);
        list.setAdapter(resultAdapter);

        final Button switchView = view.findViewById(R.id.switcher);
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.getAdapter() == resultAdapter) {
                    switchView.setText(R.string.show_results);
                    list.setAdapter(resultRoundAdapter);
                } else {
                    switchView.setText(R.string.show_rounds);
                    list.setAdapter(resultAdapter);
                }
            }
        });
    }
}
