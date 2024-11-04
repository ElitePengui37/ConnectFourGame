package com.group_name.connectfourgame.adapter;

import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group_name.connectfourgame.R;
import com.group_name.connectfourgame.User;
import com.group_name.connectfourgame.fragments.UserCardFragment;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private final List<User> users;
    private Consumer<UserCardFragment> onClick = null;
    private final SparseArray<UserCardFragment> fragArrs = new SparseArray<>();
    private final FragmentManager fragmentManager;

    public UsersAdapter(List<User> p_users, FragmentManager p_fragmentManager) {
        users = p_users;
        this.fragmentManager = p_fragmentManager;
    }

    public UsersAdapter(List<User> p_users, Consumer<UserCardFragment> p_onClick, FragmentManager p_fragmentManager) {
        users = p_users;
        fragmentManager = p_fragmentManager;
        onClick = p_onClick;
    }

    public void removeUser(final User user) {
        for(int i = 0; i < users.size(); ++i) {
            final User other = users.get(i);
            if(other.id == user.id) {
                users.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setId(View.generateViewId());
        return new UserViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Fragment existingFragment = fragmentManager.findFragmentById(holder.container.getId());
        if (existingFragment == null)
            fragmentManager.beginTransaction().add(holder.container.getId(),
                onClick == null? new UserCardFragment(users.get(position))
                    : new UserCardFragment(users.get(position), onClick)
            ).commit();
        else if (existingFragment instanceof UserCardFragment)
            ((UserCardFragment) existingFragment).setUser(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addUser(final User user) {
        users.add(0, user);
        notifyItemInserted(0);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;

        public UserViewHolder(@NonNull View userView) {
            super(userView);
            container = (LinearLayout) userView;
        }
    }
}
