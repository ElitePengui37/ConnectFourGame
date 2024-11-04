package com.group_name.connectfourgame.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.core.util.Supplier;
import androidx.fragment.app.Fragment;

import com.group_name.connectfourgame.User;
import com.group_name.connectfourgame.activities.CreateNewUserActivity;
import com.group_name.connectfourgame.R;

public class UserCardFragment extends Fragment {
    private final static String USER_INST = "USER_CARD_FRAGMENT-USER_INST";

    private User user;
    private Consumer<UserCardFragment> onClick = null;

    private ImageView playerAvatar;
    private TextView playerName;
    private ImageView playerDiscColor1;
    private ImageView playerDiscColor2;
    private ImageView playerDiscColor3;

    public UserCardFragment(final User p_user) {
        user = p_user;
    }

    public UserCardFragment() {}

    public UserCardFragment(final User p_user, final Consumer<UserCardFragment> p_onClick) {
        user = p_user;
        onClick = p_onClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_card, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        playerAvatar = view.findViewById(R.id.player_avatar);
        playerName = view.findViewById(R.id.player_name);
        playerDiscColor1 = view.findViewById(R.id.player_disc_color1);
        playerDiscColor2 = view.findViewById(R.id.player_disc_color2);
        playerDiscColor3 = view.findViewById(R.id.player_disc_color3);

        if(user != null)
            refreshUser();
        else if(savedInstanceState != null)
            user = (User) savedInstanceState.getSerializable(USER_INST);

        if(onClick != null)
            view.findViewById(R.id.user_card_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.accept(UserCardFragment.this);
                }
            });

        ActivityResultLauncher<Intent> editUserActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() != AppCompatActivity.RESULT_OK)
                    return;
                Intent intent = result.getData();
                user.copy((User) intent.getSerializableExtra(CreateNewUserActivity.UPDATED_USER));
                refreshUser();
            }
        );
        view.findViewById(R.id.edit_profile_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUserActivity.launch(
                    new Intent(getActivity(), CreateNewUserActivity.class)
                        .putExtra(CreateNewUserActivity.FORM_MODE, "edit")
                        .putExtra(CreateNewUserActivity.EDIT_USER, user)
                );
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER_INST, user);
    }

    public void setUser(final User p_user) {
        user = p_user;
        refreshUser();
    }

    public User getUser() {
        return user;
    }

    private void refreshUser() {
        final int[] avatars = new int[]{ R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6 };
        final int[] colors = new int[]{ R.color.red, R.color.yellow, R.color.green, R.color.purple, R.color.brown, R.color.black };
        playerAvatar.setImageDrawable(ContextCompat.getDrawable(getContext(), avatars[user.avatar]));
        playerName.setText(user.username);
        playerDiscColor1.setImageTintList(ContextCompat.getColorStateList(getContext(), colors[user.disc1]));
        playerDiscColor2.setImageTintList(ContextCompat.getColorStateList(getContext(), colors[user.disc2]));
        playerDiscColor3.setImageTintList(ContextCompat.getColorStateList(getContext(), colors[user.disc3]));
    }
}
