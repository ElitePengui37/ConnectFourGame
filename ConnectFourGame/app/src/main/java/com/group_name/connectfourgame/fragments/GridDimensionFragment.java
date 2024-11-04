package com.group_name.connectfourgame.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.db.GameConfigModel;

import org.jetbrains.annotations.NotNull;

public class GridDimensionFragment extends Fragment {
    private final String SELECTED_DIMENSION = "GRID_DIMENSION-SELECTED_DIMENSION";

    private String onLoad = null;
    private Spinner dimension;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid_config, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        dimension = view.findViewById(R.id.dimension_selector);
        setDimension(onLoad != null? onLoad : (savedInstanceState != null?
            savedInstanceState.getString(SELECTED_DIMENSION)
            : GameConfigModel.getConfig()
        ));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SELECTED_DIMENSION, dimension.getSelectedItem().toString());
    }

    public String getDimension() {
        return dimension.getSelectedItem().toString();
    }

    private void setDimension(final String selected) {
        dimension.setSelection(
            selected.equals("7x6")? 0 : (
                selected.equals("6x5")? 1: (
                    selected.equals("8x7")? 2 : 0
                )
            )
        );
    }

    public void setDimensionOnLoad(final String selected) {
        onLoad = selected;
    }
}
