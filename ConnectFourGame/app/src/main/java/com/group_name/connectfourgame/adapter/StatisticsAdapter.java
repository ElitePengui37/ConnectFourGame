package com.group_name.connectfourgame.adapter;

import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_name.connectfourgame.User;
import com.group_name.connectfourgame.fragments.UserCardFragment;
import com.group_name.connectfourgame.fragments.UserRankFragment;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.UserViewHolder> {
    private final List<User> users;
    private final SparseArray<UserRankFragment> fragArrs = new SparseArray<>();
    private final FragmentManager fragmentManager;

    public StatisticsAdapter(List<User> p_users, FragmentManager p_fragmentManager) {
        users = p_users;
        this.fragmentManager = p_fragmentManager;
    }

    @NonNull
    @Override
    public StatisticsAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setId(View.generateViewId());
        return new StatisticsAdapter.UserViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsAdapter.UserViewHolder holder, int position) {
        Fragment existingFragment = fragmentManager.findFragmentById(holder.container.getId());
        if (existingFragment == null)
            fragmentManager.beginTransaction().add(holder.container.getId(), new UserRankFragment(position+1, users.get(position))).commit();
        else if (existingFragment instanceof UserCardFragment)
            ((UserRankFragment) existingFragment).setUser(position+1, users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;

        public UserViewHolder(@NonNull View userView) {
            super(userView);
            container = (LinearLayout) userView;
        }
    }
}
