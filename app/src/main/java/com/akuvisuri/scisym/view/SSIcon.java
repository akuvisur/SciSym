package com.akuvisuri.scisym.view;

import android.graphics.drawable.Drawable;

import com.akuvisuri.scisym.containers.MainUtils;

import java.lang.reflect.Field;

/**
 * Created by Aku on 7.7.2015.
 */
public class SSIcon {

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Drawable getIcon(String n) {
        return MainUtils.a.getResources().getDrawable(getResId(n, Drawable.class));
    }
}
