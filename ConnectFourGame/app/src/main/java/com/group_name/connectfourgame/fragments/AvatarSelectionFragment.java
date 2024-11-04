package com.group_name.connectfourgame.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.group_name.connectfourgame.activities.CreateNewUserActivity.FormModel;
import androidx.lifecycle.ViewModelProvider;

import com.group_name.connectfourgame.R;

public class AvatarSelectionFragment extends Fragment {
    private final int avatars[] = new int[] {
        R.drawable.avatar1,R.drawable.avatar2,
        R.drawable.avatar3,R.drawable.avatar4,
        R.drawable.avatar5,R.drawable.avatar6,
    };

    private FormModel form;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_avatar_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        form = new ViewModelProvider(requireActivity()).get(FormModel.class);
        view.findViewById(R.id.avatar_selection1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAvatar(0);
            }
        });
        view.findViewById(R.id.avatar_selection2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAvatar(1);
            }
        });
        view.findViewById(R.id.avatar_selection3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAvatar(2);
            }
        });
        view.findViewById(R.id.avatar_selection4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAvatar(3);
            }
        });
        view.findViewById(R.id.avatar_selection5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAvatar(4);
            }
        });
        view.findViewById(R.id.avatar_selection6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAvatar(5);
            }
        });
    }

    public void selectAvatar(final int id) {
        form.setAvatar(id);
    }
}
