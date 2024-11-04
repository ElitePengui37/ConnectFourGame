package com.group_name.connectfourgame.resource;

import android.content.Context;
import android.graphics.drawable.DrawableWrapper;

public class ResourceDrawable extends DrawableWrapper {
    private final int resId;
    public ResourceDrawable(Context context, int resId, int drawableId) {
        super(context.getResources().getDrawable(drawableId, null));
        this.resId = resId;
    }
    public int getResId() {
        return resId;
    }
}