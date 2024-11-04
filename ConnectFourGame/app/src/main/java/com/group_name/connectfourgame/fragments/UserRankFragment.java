package com.group_name.connectfourgame.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.User;

public class UserRankFragment extends Fragment {
    private int rank_no;
    private User user;

    private TextView rank;
    private TextView username;
    private ImageView avatar;
    private TextView win;
    private TextView loss;
    private TextView draw;

    public UserRankFragment(final int p_rank_no, final User p_user) {
        rank_no = p_rank_no;
        user = p_user;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_rank_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rank = view.findViewById(R.id.ranking_no);
        username = view.findViewById(R.id.player_name);
        avatar = view.findViewById(R.id.player_avatar);
        win = view.findViewById(R.id.pvp_wins_count);
        loss = view.findViewById(R.id.pvp_loss_count);
        draw = view.findViewById(R.id.pvp_draw_count);
        refresh();
    }

    public void setUser(final int p_rank_no, final User p_user) {
        rank_no = p_rank_no;
        user = p_user;
        refresh();
    }

    public void refresh() {
        final int[] avatars = new int[]{ R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6 };
        rank.setText(Integer.toString(rank_no));
        username.setText(user.username);
        avatar.setImageDrawable(ContextCompat.getDrawable(getContext(), avatars[user.avatar]));
        win.setText(Integer.toString(user.pvp_win_count));
        loss.setText(Integer.toString(user.pvp_loss_count));
        draw.setText(Integer.toString(user.pvp_draw_count));
    }
}
