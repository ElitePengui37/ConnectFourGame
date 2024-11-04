package com.group_name.connectfourgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.User;
import com.group_name.connectfourgame.fragments.GridDimensionFragment;
import com.group_name.connectfourgame.fragments.GuestCardFragment;
import com.group_name.connectfourgame.fragments.UserCardFragment;

import java.io.Serializable;

public class PreGameActivity extends AppCompatActivity {

    public static final String GAME_MODE = "GAME_MODE";
    private final String PLAYER1 = "PREGAME_ACTIVITY-PLAYER1";
    private final String PLAYER2 = "PREGAME_ACTIVITY-PLAYER2";

    public static abstract class SelectedWrapper implements Serializable {
        public abstract User get();
        public abstract Fragment makeFragment(Runnable onClick);
    }
    public static class UserWrapper extends SelectedWrapper {
        public User user;
        public UserWrapper(final User p_user) {
            user = p_user;
        }
        public User get() {
            return user;
        }
        public Fragment makeFragment(Runnable onClick) {
            return new UserCardFragment(user, (userFrag) -> onClick.run());
        }
    }
    public static class GuestWrapper extends SelectedWrapper {
        public GuestWrapper() {}
        public User get() {
            return null;
        }
        public Fragment makeFragment(Runnable onClick) {
            return new GuestCardFragment(onClick);
        }
    }

    private boolean isPointingAtPlayer1 = true;
    private SelectedWrapper player1 = null;
    private SelectedWrapper player2 = null;

    private LinearLayout player1_select_contianer;
    private LinearLayout player2_select_contianer;

    private ActivityResultLauncher<Intent> selectPlayer;
    private Consumer<Boolean> startSelectActivity = (p_isPointingAtPlayer1) -> {
        isPointingAtPlayer1 = p_isPointingAtPlayer1;
        Intent data = new Intent(PreGameActivity.this, UserProfileActivity.class)
            .putExtra(UserProfileActivity.SELECTED_USER, true);
        final SelectedWrapper player = p_isPointingAtPlayer1? player2 : player1;
        final User except = player != null? player.get() : null;
        if(except != null)
            data.putExtra(UserProfileActivity.EXCEPT_USER, except);
        selectPlayer.launch(data);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pregame);
        Intent data = getIntent();

        final boolean aiGameMode = data.getStringExtra(GAME_MODE).equals("ai");
        if(aiGameMode) {
            findViewById(R.id.player2_user_profile).setVisibility(View.INVISIBLE);
            findViewById(R.id.select_player2_container).setVisibility(View.INVISIBLE);
        }
        player1_select_contianer = findViewById(R.id.select_player1_container);
        player2_select_contianer = findViewById(R.id.select_player2_container);

        if(savedInstanceState != null) {
            player1 = (SelectedWrapper) savedInstanceState.getSerializable(PLAYER1);
            player2 = (SelectedWrapper) savedInstanceState.getSerializable(PLAYER2);
            updateSelectedPlayer1();
            updateSelectedPlayer2();
        }

        selectPlayer = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() != RESULT_OK)
                    return;
                if(isPointingAtPlayer1) {
                    player1 = new UserWrapper((User) result.getData().getSerializableExtra(UserProfileActivity.SELECTED_USER));
                    updateSelectedPlayer1();
                } else {
                    player2 = new UserWrapper((User) result.getData().getSerializableExtra(UserProfileActivity.SELECTED_USER));
                    updateSelectedPlayer2();
                }
            }
        );
        findViewById(R.id.select_player1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectActivity.accept(true);
            }
        });
        findViewById(R.id.select_player1_as_guest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player1 = new GuestWrapper();
                updateSelectedPlayer1();
            }
        });
        findViewById(R.id.select_player2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectActivity.accept(false);
            }
        });
        findViewById(R.id.select_player2_as_guest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player2 = new GuestWrapper();
                updateSelectedPlayer2();
            }
        });
        findViewById(R.id.enter_game_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player1 == null) {
                    Toast.makeText(PreGameActivity.this, R.string.text_invalid_player1, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!aiGameMode && player2 == null) {
                    Toast.makeText(PreGameActivity.this, R.string.text_invalid_player2, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent data = new Intent()
                    .putExtra(GameActivity.GAME_MODE, aiGameMode)
                    .putExtra(GameActivity.PLAYER1, player1.get())
                    .putExtra(GameActivity.GRID_DIMENSION,
                        ((GridDimensionFragment)getSupportFragmentManager().findFragmentById(R.id.grid_dimension_fragment))
                            .getDimension());
                if(!aiGameMode)
                    data.putExtra(GameActivity.PLAYER2, player2.get());
                setResult(RESULT_OK, data);
                finish();
            }
        });
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLAYER1, player1);
        outState.putSerializable(PLAYER2, player2);
    }

    private void updateSelectedPlayer1() {
        if(player1 == null) {
            player1_select_contianer.setVisibility(View.VISIBLE);
            Fragment temp = getSupportFragmentManager().findFragmentById(R.id.player1_user_profile);
            if(temp != null)
                getSupportFragmentManager().beginTransaction().remove(temp).commit();
        } else {
            player1_select_contianer.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.player1_user_profile, player1.makeFragment(() -> startSelectActivity.accept(true)))
                .commit();
        }
    }

    private void updateSelectedPlayer2() {
        if(player2 == null) {
            player2_select_contianer.setVisibility(View.VISIBLE);
            Fragment temp = getSupportFragmentManager().findFragmentById(R.id.player2_user_profile);
            if(temp != null)
                getSupportFragmentManager().beginTransaction().remove(temp).commit();
        } else {
            player2_select_contianer.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.player2_user_profile, player2.makeFragment(() -> startSelectActivity.accept(false)))
                .commit();
        }
    }
}
