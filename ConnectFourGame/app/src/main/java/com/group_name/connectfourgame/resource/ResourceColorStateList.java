package com.group_name.connectfourgame.resource;

import android.content.Context;
import android.content.res.ColorStateList;

public class ResourceColorStateList extends ColorStateList {
    private final int resId;

    public ResourceColorStateList(Context context, int id, int colorResId) {
        super(new int[][] { new int[0] }, new int[] { context.getResources().getColor(colorResId, null) });
        this.resId = id;
    }

    public int getResId() {
        return resId;
    }
}
