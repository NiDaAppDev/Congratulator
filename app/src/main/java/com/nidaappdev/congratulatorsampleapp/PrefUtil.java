package com.nidaappdev.congratulatorsampleapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class PrefUtil {

    private Context context;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String PREF_NAME = "prefs";
    private static final String SOUND_PREFERENCE_NAME = "soundEnabled";
    private static final String IMAGE_ENABLED_PREFERENCE_NAME = "imageEnabled";
    private static final String IMAGE_ANIMATIONS_ENABLED_PREFERENCE_NAME = "imageAnimationsEnabled";
    private static final String TITLE_PREFERENCE_NAME = "title";
    private static final String CONTENT_PREFERENCE_NAME = "content";
    private static final String CONFETTI_COLORS_PREFERENCE_NAME = "confettiColors";

    public PrefUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void enableSound(boolean enable) {
        editor.putBoolean(SOUND_PREFERENCE_NAME, enable);
        editor.apply();
    }

    public boolean isSoundEnabled() {
        return sharedPreferences.getBoolean(SOUND_PREFERENCE_NAME, true);
    }

    public void enableImage(boolean enable) {
        editor.putBoolean(IMAGE_ENABLED_PREFERENCE_NAME, enable);
        editor.apply();
    }

    public boolean isImageEnabled() {
        return sharedPreferences.getBoolean(IMAGE_ENABLED_PREFERENCE_NAME, true);
    }

    public void enableImageAnimation(boolean enable) {
        editor.putBoolean(IMAGE_ANIMATIONS_ENABLED_PREFERENCE_NAME, enable);
        editor.apply();
    }

    public boolean isImageAnimationEnabled() {
        return sharedPreferences.getBoolean(IMAGE_ANIMATIONS_ENABLED_PREFERENCE_NAME, true);
    }

    public void setTitle(String title) {
        editor.putString(TITLE_PREFERENCE_NAME, title);
        editor.apply();
    }

    public String getTitle() {
        return sharedPreferences.getString(TITLE_PREFERENCE_NAME, "Congratulations");
    }

    public void setContent(String content) {
        editor.putString(CONTENT_PREFERENCE_NAME, content);
        editor.apply();
    }

    public String getContent() {
        return sharedPreferences.getString(CONTENT_PREFERENCE_NAME, "Well Done!");
    }

    public void setConfettiColors(List<String> colors) {
        StringBuilder colorsStr = new StringBuilder();

        for(String color : colors) {
            colorsStr.append(color).append(",");
        }

        editor.putString(CONFETTI_COLORS_PREFERENCE_NAME, colorsStr.toString());
        editor.apply();
    }

    public ArrayList<String> getConfettiColors() {
        ArrayList<String> confettiColors = new ArrayList<>(Arrays.asList(sharedPreferences.getString(CONFETTI_COLORS_PREFERENCE_NAME, "").split(",")));
        if(confettiColors.isEmpty())
            return new ArrayList<>();
        return confettiColors;
    }

}
