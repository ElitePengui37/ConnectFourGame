package com.group_name.connectfourgame.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.adapter.StatisticsAdapter;
import com.group_name.connectfourgame.adapter.UsersAdapter;
import com.group_name.connectfourgame.db.UserModel;

public class UserStatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_statistics);
        RecyclerView recyclerView = findViewById(R.id.user_rankings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new StatisticsAdapter(UserModel.getUserRankings(), getSupportFragmentManager()));
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
