package com.group_name.connectfourgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.FragmentManager;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.db.GameConfigModel;
import com.group_name.connectfourgame.fragments.GridDimensionFragment;

public class ConfigurationActivity extends AppCompatActivity {
    private static final String IS_SETINGS_MODAL_OPEN = "IS_SETINGS_MODAL_OPEN";

    private Button settingsOpt;
    private LinearLayout gridDimension;
    private Guideline guideline1;

    private void openSettingsModal() {
        settingsOpt.setVisibility(View.GONE);
        gridDimension.setVisibility(View.VISIBLE);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline1.getLayoutParams();
        params.guidePercent = 1.0f;
        guideline1.setLayoutParams(params);
    }

    private void closeSettingsModal() {
        settingsOpt.setVisibility(View.VISIBLE);
        gridDimension.setVisibility(View.GONE);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideline1.getLayoutParams();
        params.guidePercent = 0.8f;
        guideline1.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_configuration);
        settingsOpt = findViewById(R.id.option_game_settings);
        gridDimension = findViewById(R.id.grid_dimension_fragment);
        guideline1 = findViewById(R.id.guideline1);

        if(savedInstanceState != null && savedInstanceState.getBoolean(IS_SETINGS_MODAL_OPEN))
            openSettingsModal();

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        settingsOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsModal();
            }
        });
        findViewById(R.id.grid_dimension_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSettingsModal();
                FragmentManager fm = getSupportFragmentManager();
                GameConfigModel.saveConfig(
                    ((GridDimensionFragment)fm.findFragmentById(R.id.grid_dimension_container))
                        .getDimension()
                );
            }
        });
        findViewById(R.id.option_user_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfigurationActivity.this, UserProfileActivity.class));
            }
        });
        findViewById(R.id.option_user_statistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfigurationActivity.this, UserStatisticsActivity.class));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(IS_SETINGS_MODAL_OPEN, gridDimension.getVisibility() == View.VISIBLE);
    }
}
