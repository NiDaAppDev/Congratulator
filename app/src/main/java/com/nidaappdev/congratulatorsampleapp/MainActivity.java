package com.nidaappdev.congratulatorsampleapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.material.textfield.TextInputEditText;
import com.nidaappdev.congratulator.CongratulationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button congratulateButton;
    private CongratulationView congratulationViewBuilder;
    private TextInputEditText titleET, contentET;
    private CheckBox enableSoundCheckBox, greenCheckBox, blueCheckBox, purpleCheckBox;
    private PrefUtil util;
    private int[] confettiColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        util = new PrefUtil(this);


        /**
         * Initialize objects
         */
        titleET = findViewById(R.id.title_et);
        titleET.setText(util.getTitle());
        titleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                util.setTitle(editable.toString());
            }
        });

        contentET = findViewById(R.id.content_et);
        contentET.setText(util.getContent());
        contentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                util.setContent(editable.toString());
            }
        });

        enableSoundCheckBox = findViewById(R.id.enable_sound_checkBox);
        enableSoundCheckBox.setChecked(util.isSoundEnabled());
        enableSoundCheckBox.setOnCheckedChangeListener((checkBox, checked) -> {
            util.enableSound(checked);
        });

        greenCheckBox = findViewById(R.id.green_checkBox);
        greenCheckBox.setChecked(util.getConfettiColors().contains("green"));
        greenCheckBox.setOnCheckedChangeListener((checkBox, checked) -> {
            List<String> updatedColors = util.getConfettiColors();
            if (checked) {
                updatedColors.add("green");
            } else {
                updatedColors.remove("green");
            }
            util.setConfettiColors(updatedColors);
            updateConfettiColors();
        });

        blueCheckBox = findViewById(R.id.blue_checkBox);
        blueCheckBox.setChecked(util.getConfettiColors().contains("blue"));
        blueCheckBox.setOnCheckedChangeListener((checkBox, checked) -> {
            List<String> updatedColors = util.getConfettiColors();
            if (checked) {
                updatedColors.add("blue");
            } else {
                updatedColors.remove("blue");
            }
            util.setConfettiColors(updatedColors);
            updateConfettiColors();
        });

        purpleCheckBox = findViewById(R.id.purple_checkBox);
        purpleCheckBox.setChecked(util.getConfettiColors().contains("purple"));
        purpleCheckBox.setOnCheckedChangeListener((checkBox, checked) -> {
            ArrayList<String> updatedColors = util.getConfettiColors();
            if (checked) {
                updatedColors.add("purple");
            } else {
                updatedColors.remove("purple");
            }
            util.setConfettiColors(updatedColors);
            updateConfettiColors();
            Log.d(TAG, "onCreate: " + util.getConfettiColors().toString());
        });

        updateConfettiColors();

        congratulateButton = findViewById(R.id.congratulate_button);
        congratulateButton.setOnClickListener((v) ->
                congratulationViewBuilder = new CongratulationView.Builder(this)
                        .setTitle(util.getTitle())
                        .setContent(util.getContent())
                        .enableSound(util.isSoundEnabled())
                        .setConfettiColors(confettiColors)
                        .show()
        );

    }

    private void updateConfettiColors() {
        List<Integer> confettiColorsArr = new ArrayList<>();
        if(util.getConfettiColors().contains("green")) {
            confettiColorsArr.add(Color.parseColor("#8BC34A"));
        }
        if(util.getConfettiColors().contains("blue")) {
            confettiColorsArr.add(Color.parseColor("#03A9F4"));
        }
        if(util.getConfettiColors().contains("purple")) {
            confettiColorsArr.add(Color.parseColor("#9C27B0"));
        }
        if(confettiColorsArr.isEmpty()) {
            confettiColorsArr.add(Color.parseColor("#8BC34A"));
            confettiColorsArr.add(Color.parseColor("#03A9F4"));
        }


        confettiColors = new int[confettiColorsArr.size()];
        for(int i = 0; i < confettiColorsArr.size(); i ++) {
            confettiColors[i] = confettiColorsArr.get(i);
        }
    }
}