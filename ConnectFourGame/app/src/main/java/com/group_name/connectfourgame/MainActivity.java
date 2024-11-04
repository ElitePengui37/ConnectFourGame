package com.group_name.connectfourgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.group_name.connectfourgame.activities.ConfigurationActivity;
import com.group_name.connectfourgame.activities.CreateNewUserActivity;
import com.group_name.connectfourgame.activities.GameActivity;
import com.group_name.connectfourgame.activities.PreGameActivity;
import com.group_name.connectfourgame.db.DatabaseHelper;
import com.group_name.connectfourgame.db.UserModel;

public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> pregame_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        DatabaseHelper.initialize(this);

        pregame_settings = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() != RESULT_OK)
                    return;
                startActivity(new Intent(MainActivity.this, GameActivity.class)
                    .putExtras(result.getData().getExtras()));
            }
        );
        findViewById(R.id.option_player_1v1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregame_settings.launch(
                    new Intent(MainActivity.this, PreGameActivity.class)
                        .putExtra(PreGameActivity.GAME_MODE, "pvp")
                );
            }
        });
        findViewById(R.id.option_ai_vs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregame_settings.launch(
                    new Intent(MainActivity.this, PreGameActivity.class)
                        .putExtra(PreGameActivity.GAME_MODE, "ai")
                );
            }
        });
        findViewById(R.id.option_configuration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ConfigurationActivity.class));
            }
        });
        findViewById(R.id.program_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(0);
            }
        });
        findViewById(R.id.add_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateNewUserActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHelper.closeDb();
    }
}