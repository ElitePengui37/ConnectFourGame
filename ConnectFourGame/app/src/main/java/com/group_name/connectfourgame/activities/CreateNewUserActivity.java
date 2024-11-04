package com.group_name.connectfourgame.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.widget.Toast;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.User;
import com.group_name.connectfourgame.db.DatabaseHelper;
import com.group_name.connectfourgame.db.UserModel;
import com.group_name.connectfourgame.resource.ResourceColorStateList;
import com.group_name.connectfourgame.resource.ResourceDrawable;

import org.jetbrains.annotations.NotNull;

public class CreateNewUserActivity extends AppCompatActivity {
    public static final String FORM_MODE = "CREATE_NEW_USER-CREATE_USER_MODE";
    public static final String EDIT_USER = "CREATE_NEW_USER-EDIT_USER";
    public static final String UPDATED_USER = "CREATE_NEW_USER-UPDATED_USER";
    public static final String NEW_USER = "CREATE_NEW_USER-NEW_USER";
    private final String USERNAME_BUNDLE = "CREATE_NEW_USER-USERNAME_BUNDLE";
    private final String FORM_DEFAULT_AVATAR = "CREATE_NEW_USER-CREATE_USER_AVATAR";
    private final String FORM_DEFAULT_COLOR1 = "CREATE_NEW_USER-CREATE_USER_COLOR1";
    private final String FORM_DEFAULT_COLOR2 = "CREATE_NEW_USER-CREATE_USER_COLOR2";
    private final String FORM_DEFAULT_COLOR3 = "CREATE_NEW_USER-CREATE_USER_COLOR3";

    public static class FormModel extends ViewModel {
        public final MutableLiveData<ResourceDrawable> avatar = new MutableLiveData<>();
        public final MutableLiveData<ResourceColorStateList> color1 = new MutableLiveData<>();
        public final MutableLiveData<ResourceColorStateList> color2 = new MutableLiveData<>();
        public final MutableLiveData<ResourceColorStateList> color3 = new MutableLiveData<>();
        public final MutableLiveData<Integer> disc_pointer = new MutableLiveData<>(-1);

        private ResourceDrawable[] avatars = null;
        private ResourceColorStateList[] colors = null;

        public void setAvatar(final int id) {
            avatar.setValue(avatars[id]);
        }

        public void setColor(final int colorId) {
            switch(disc_pointer.getValue()) {
                case 1:
                    if(colorId == color2.getValue().getResId())
                        color2.setValue(color1.getValue());
                    else if(colorId == color3.getValue().getResId())
                        color3.setValue(color1.getValue());
                    color1.setValue(colors[colorId]);
                    break;
                case 2:
                    if(colorId == color1.getValue().getResId())
                        color1.setValue(color2.getValue());
                    else if(colorId == color3.getValue().getResId())
                        color3.setValue(color2.getValue());
                    color2.setValue(colors[colorId]);
                    break;
                case 3:
                    if(colorId == color2.getValue().getResId())
                        color2.setValue(color3.getValue());
                    else if(colorId == color1.getValue().getResId())
                        color1.setValue(color3.getValue());
                    color3.setValue(colors[colorId]);
                    break;
            }
        }

        public void _initialize(final ResourceDrawable[] pAvatars, final ResourceColorStateList[] pColors) {
            avatars = pAvatars;
            colors = pColors;
        }

        public void _initialize(
            final ResourceDrawable pAvatar, final ResourceColorStateList pDisc1,
            final ResourceColorStateList pDisc2, final ResourceColorStateList pDisc3
        ) {
            avatar.setValue(pAvatar);
            color1.setValue(pDisc1);
            color2.setValue(pDisc2);
            color3.setValue(pDisc3);
        }

        public void initialize(final ResourceDrawable[] pAvatars, final ResourceColorStateList[] pColors) {
            _initialize(pAvatars, pColors);
            _initialize(avatars[0], colors[0], colors[1], colors[2]);
        }

        public void initialize(
            final ResourceDrawable[] pAvatars, final ResourceColorStateList[] pColors,
            final ResourceDrawable pAvatar, final ResourceColorStateList pDisc1,
            final ResourceColorStateList pDisc2, final ResourceColorStateList pDisc3
        ) {
            _initialize(pAvatars, pColors);
            _initialize(
                pAvatar, pDisc1,
                pDisc2, pDisc3
            );
        }

        public boolean refresh() {
            // when process is killed, mutable might be reset to have null values
            // notify by return true to re initialize view model using savedInstanceState data
            if(avatars == null || colors == null)
                return true;
            avatar.setValue(avatar.getValue());
            color1.setValue(color1.getValue());
            color2.setValue(color2.getValue());
            color3.setValue(color3.getValue());
            return false;
        }
    }

    private User user = null;
    private FormModel form;
    private EditText usernameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_create_user);

        usernameField = findViewById(R.id.input_name);
        ImageView inputAvatar = findViewById(R.id.input_avatar);
        ImageView inputDisc1 = findViewById(R.id.input_disc_color_1);
        ImageView inputDisc2 = findViewById(R.id.input_disc_color_2);
        ImageView inputDisc3 = findViewById(R.id.input_disc_color_3);

        FragmentContainerView avatarModal = findViewById(R.id.avatar_selection_modal);
        FragmentContainerView discModal = findViewById(R.id.disc_selection_modal);

        form = new ViewModelProvider(this).get(FormModel.class);
        Intent data = getIntent();

        form.avatar.observe(this, (newValue) -> {
            avatarModal.setVisibility(View.GONE);
            inputAvatar.setImageDrawable((ResourceDrawable) newValue);
        });
        form.color1.observe(this, (newValue) -> {
            discModal.setVisibility(View.GONE);
            inputDisc1.setImageTintList((ResourceColorStateList) newValue);
        });
        form.color2.observe(this, (newValue) -> {
            discModal.setVisibility(View.GONE);
            inputDisc2.setImageTintList((ResourceColorStateList) newValue);
        });
        form.color3.observe(this, (newValue) -> {
            discModal.setVisibility(View.GONE);
            inputDisc3.setImageTintList((ResourceColorStateList) newValue);
        });

        final String mode = data.getStringExtra(FORM_MODE);
        final boolean isEdit = mode != null && mode.equals("edit");

        if(savedInstanceState != null) {
            final String editUsername = savedInstanceState.getString(USERNAME_BUNDLE);
            usernameField.setText(editUsername);
            if(isEdit) {
                user = (User) data.getSerializableExtra(EDIT_USER);
                ((TextView) findViewById(R.id.create_user_form_header)).setText(R.string.text_edit);
                if(editUsername == null)
                    usernameField.setText(user.username);
            }
            if(form.refresh()) {
                ResourceDrawable[] avatars = createDrawables();
                ResourceColorStateList[] colors = createColors();
                form.initialize(avatars, colors,
                    avatars[savedInstanceState.getInt(FORM_DEFAULT_AVATAR)],
                    colors[savedInstanceState.getInt(FORM_DEFAULT_COLOR1)],
                    colors[savedInstanceState.getInt(FORM_DEFAULT_COLOR2)],
                    colors[savedInstanceState.getInt(FORM_DEFAULT_COLOR3)]
                );
            }
        } else {
            ResourceDrawable[] avatars = createDrawables();
            ResourceColorStateList[] colors = createColors();
            if(isEdit) {
                user = (User) data.getSerializableExtra(EDIT_USER);
                ((TextView) findViewById(R.id.create_user_form_header)).setText(R.string.text_edit);
                ((EditText) findViewById(R.id.input_name)).setText(user.username);
                form.initialize(avatars, colors,
                    avatars[user.avatar],
                    colors[user.disc1],
                    colors[user.disc2],
                    colors[user.disc3]
                );
            } else
                form.initialize(avatars, colors);
        }

        inputAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { avatarModal.setVisibility(View.VISIBLE); }
        });
        inputDisc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discModal.setVisibility(View.VISIBLE);
                form.disc_pointer.setValue(1);
            }
        });
        inputDisc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discModal.setVisibility(View.VISIBLE);
                form.disc_pointer.setValue(2);
            }
        });
        inputDisc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discModal.setVisibility(View.VISIBLE);
                form.disc_pointer.setValue(3);
            }
        });
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameField.getText().toString();
                final int avatar = ((ResourceDrawable)inputAvatar.getDrawable()).getResId();
                final int disc_1 = ((ResourceColorStateList)inputDisc1.getImageTintList()).getResId();
                final int disc_2 = ((ResourceColorStateList)inputDisc2.getImageTintList()).getResId();
                final int disc_3 = ((ResourceColorStateList)inputDisc3.getImageTintList()).getResId();
                if(username.isEmpty()) {
                    Toast.makeText(CreateNewUserActivity.this, R.string.text_invalid_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(isEdit)
                    setResult(RESULT_OK, new Intent().putExtra(UPDATED_USER, UserModel.updateUser(user.editUser(username, avatar, disc_1, disc_2, disc_3))));
                else
                    setResult(RESULT_OK, new Intent().putExtra(NEW_USER, UserModel.storeUser(username, avatar, disc_1, disc_2, disc_3)));
                finish();
            }
        });
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NotNull  Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(USERNAME_BUNDLE, usernameField.getText().toString());
        savedInstanceState.putInt(FORM_DEFAULT_AVATAR, form.avatar.getValue().getResId());
        savedInstanceState.putInt(FORM_DEFAULT_COLOR1, form.color1.getValue().getResId());
        savedInstanceState.putInt(FORM_DEFAULT_COLOR2, form.color2.getValue().getResId());
        savedInstanceState.putInt(FORM_DEFAULT_COLOR3, form.color3.getValue().getResId());
    }

    private ResourceDrawable[] createDrawables() {
        return new ResourceDrawable[] {
            new ResourceDrawable(this, 0, R.drawable.avatar1), new ResourceDrawable(this, 1, R.drawable.avatar2),
            new ResourceDrawable(this, 2, R.drawable.avatar3), new ResourceDrawable(this, 3, R.drawable.avatar4),
            new ResourceDrawable(this, 4, R.drawable.avatar5), new ResourceDrawable(this, 5, R.drawable.avatar6),
        };
    }

    private ResourceColorStateList[] createColors() {
        return new ResourceColorStateList[] {
            new ResourceColorStateList(this, 0, R.color.red), new ResourceColorStateList(this, 1, R.color.yellow),
            new ResourceColorStateList(this, 2, R.color.green), new ResourceColorStateList(this, 3, R.color.purple),
            new ResourceColorStateList(this, 4, R.color.brown), new ResourceColorStateList(this, 5, R.color.black),
        };
    }
}
