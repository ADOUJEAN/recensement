package ci.k2jts.recensement.ui.statistique;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ci.k2jts.recensement.R;

public class StatistiqueRecFragment extends Fragment {

    private StatistiqueRecViewModel statistcViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statistcViewModel =
                ViewModelProviders.of(this).get(StatistiqueRecViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistiques_recensement, container, false);
        final TextView textView = root.findViewById(R.id.text_statistique);
        statistcViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}