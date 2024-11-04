package com.group_name.connectfourgame.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.group_name.connectfourgame.activities.CreateNewUserActivity.FormModel;
import com.group_name.connectfourgame.R;

public class DiscSelectionFragment extends Fragment {
    private FormModel form;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_disc_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        form = new ViewModelProvider(requireActivity()).get(FormModel.class);

        view.findViewById(R.id.select_disc_color_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form.setColor(0);
            }
        });
        view.findViewById(R.id.select_disc_color_yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form.setColor(1);
            }
        });
        view.findViewById(R.id.select_disc_color_green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form.setColor(2);
            }
        });
        view.findViewById(R.id.select_disc_color_purple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form.setColor(3);
            }
        });
        view.findViewById(R.id.select_disc_color_brown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form.setColor(4);
            }
        });
        view.findViewById(R.id.select_disc_color_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form.setColor(5);
            }
        });
    }
}
