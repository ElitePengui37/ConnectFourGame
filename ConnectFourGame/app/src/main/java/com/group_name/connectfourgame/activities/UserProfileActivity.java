package com.group_name.connectfourgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.User;
import com.group_name.connectfourgame.adapter.UsersAdapter;
import com.group_name.connectfourgame.db.UserModel;

public class UserProfileActivity extends AppCompatActivity {
    public static final String SELECTED_USER = "USER_PROFILE_ACTIVITY-SELECTED_USER";
    public static final String EXCEPT_USER = "USER_PROFILE_ACTIVITY-EXCEPT_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_user_profile);

        boolean isSelecting = getIntent().getBooleanExtra(SELECTED_USER, false);

        RecyclerView recyclerView = findViewById(R.id.user_card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        UsersAdapter adapter = new UsersAdapter(UserModel.getAllUsers(), isSelecting?
            (userFrag) -> {
                setResult(RESULT_OK, new Intent().putExtra(SELECTED_USER, userFrag.getUser()));
                finish();
            } : (userFrag) -> {
                startActivity(new Intent(UserProfileActivity.this, UserDetailActivity.class)
                    .putExtra(UserDetailActivity.USER_INSTANCE, userFrag.getUser()));
        }, getSupportFragmentManager());
        User user = (User) getIntent().getSerializableExtra(EXCEPT_USER);
        if(user != null)
            adapter.removeUser(user);
        recyclerView.setAdapter(adapter);

        ActivityResultLauncher<Intent> createUserActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() != AppCompatActivity.RESULT_OK)
                    return;
                Intent intent = result.getData();
                adapter.addUser((User) intent.getSerializableExtra(CreateNewUserActivity.NEW_USER));
            }
        );

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelecting)
                    setResult(RESULT_CANCELED);
                finish();
            }
        });
        findViewById(R.id.add_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserActivity.launch(new Intent(UserProfileActivity.this, CreateNewUserActivity.class));
                recyclerView.scrollToPosition(0);
            }
        });
    }
}
