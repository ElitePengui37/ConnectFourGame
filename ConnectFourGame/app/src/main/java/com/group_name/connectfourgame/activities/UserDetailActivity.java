package com.group_name.connectfourgame.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_name.connectfourgame.MatchHistory;
import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.User;
import com.group_name.connectfourgame.db.MatchHistoryModel;
import com.group_name.connectfourgame.db.UserModel;

import java.util.List;
import com.group_name.connectfourgame.MatchHistory.ResultType;

public class UserDetailActivity extends AppCompatActivity {
    public final static String USER_INSTANCE = "USER_DETAIL_FRAGMENT-USER_INST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_user_details);
        User user = (User) getIntent().getSerializableExtra(USER_INSTANCE);

        final int[] avatars = new int[]{ R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6 };
        final int[] colors = new int[]{ R.color.red, R.color.yellow, R.color.green, R.color.purple, R.color.brown, R.color.black };

        ((TextView)findViewById(R.id.player_name)).setText(user.username);
        ((ImageView)findViewById(R.id.player_avatar)).setImageDrawable(ContextCompat.getDrawable(this, avatars[user.avatar]));
        ((ImageView)findViewById(R.id.player_disc_color1)).setImageTintList(ContextCompat.getColorStateList(this, colors[user.disc1]));
        ((ImageView)findViewById(R.id.player_disc_color2)).setImageTintList(ContextCompat.getColorStateList(this, colors[user.disc2]));
        ((ImageView)findViewById(R.id.player_disc_color3)).setImageTintList(ContextCompat.getColorStateList(this, colors[user.disc3]));
        ((TextView)findViewById(R.id.pvp_wins_count)).setText(Integer.toString(user.pvp_win_count));
        ((TextView)findViewById(R.id.pvp_loss_count)).setText(Integer.toString(user.pvp_loss_count));
        ((TextView)findViewById(R.id.pvp_draw_count)).setText(Integer.toString(user.pvp_draw_count));
        ((TextView)findViewById(R.id.ai_wins_count)).setText(Integer.toString(user.ai_win_count));
        ((TextView)findViewById(R.id.ai_loss_count)).setText(Integer.toString(user.ai_loss_count));
        ((TextView)findViewById(R.id.ai_draw_count)).setText(Integer.toString(user.ai_draw_count));

        final RecyclerView matchHistory = findViewById(R.id.match_history);
        matchHistory.setLayoutManager(new LinearLayoutManager(this));
        matchHistory.setAdapter(new MatchHistoryAdapter(this, user.id, MatchHistoryModel.retrieveResults(user)));

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
    }

    public static class MatchHistoryAdapter extends RecyclerView.Adapter<MatchHistoryAdapter.MatchHistoryHolder> {

        private final List<MatchHistory> items;
        private final Context context;
        private final int playerId;

        public MatchHistoryAdapter(Context context, final int playerId, List<MatchHistory> items) {
            this.context = context;
            this.items = items;
            this.playerId = playerId;
        }

        // ViewHolder class to hold the view references
        public static class MatchHistoryHolder extends RecyclerView.ViewHolder {
            public TextView resultType;
            public TextView opponentName;
            public LinearLayout container;

            public MatchHistoryHolder(View itemView) {
                super(itemView);
                container = itemView.findViewById(R.id.match_history_container);
                resultType = itemView.findViewById(R.id.result_text);
                opponentName = itemView.findViewById(R.id.opponent_name);
            }
        }

        @NonNull
        @Override
        public MatchHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the layout for each item of the recycler view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_match_history, parent, false);
            return new MatchHistoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MatchHistoryHolder holder, int position) {
            // Bind data to the views here
            final MatchHistory currentItem = items.get(position);
            Integer enemy_id;
            if(currentItem.player1_id == playerId) { // user as player 1
                holder.resultType.setText(currentItem.resultType.toString());
                enemy_id = currentItem.player2_id;
            } else { // user as player 2
                holder.resultType.setText(currentItem.resultType.toP2String());
                enemy_id = currentItem.player1_id;
            }
            if("Win".equals(holder.resultType.getText().toString()))
                holder.container.setBackgroundTintList(context.getResources().getColorStateList(R.color.win_color, null));
            else if("Lose".equals(holder.resultType.getText().toString()))
                holder.container.setBackgroundTintList(context.getResources().getColorStateList(R.color.lose_color, null));
            else
                holder.container.setBackgroundTintList(null);
            if(enemy_id == null)
                holder.opponentName.setText(context.getResources().getString(R.string.text_land_guest));
            else {
                User user = UserModel.getUser(enemy_id);
                holder.opponentName.setText(user == null? "Anonymous" : user.username);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
